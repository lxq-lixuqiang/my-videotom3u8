package com.video.m3u8.controller;

import com.video.m3u8.util.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
@Slf4j
public class M3u8Controller {

    @GetMapping("/m3u8Play")
    public String m3u8Play(){
        return "m3u8Play";
    }

    /**
     * m3u8流进行返回，视频会被切分为多个
     * @param request
     * @param response
     * @param name 传入的标识获取对应的视频
     */
    @GetMapping("/m3u8Play/{name}")
    @ResponseBody
    public void m3u8Play(HttpServletResponse request,
                          HttpServletResponse response,
                          @PathVariable(value="name",required=false) String name){
        /*
         * 在这里可以进行权限验证等操作
         */
        //创建文件对象
        File f = new File(Common.videoUrl+"\\嗜血代码\\"+name);
        //获取文件名称
        String fileName = f.getName();
        //导出文件
        String agent = request.getHeader("User-Agent")!=null?request.getHeader("User-Agent").toUpperCase():"";
        InputStream fis = null;
        OutputStream os = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(f.getPath()));
            byte[] buffer;
            buffer = new byte[fis.available()];
            fis.read(buffer);
            response.reset();
            //由于火狐和其他浏览器显示名称的方式不相同，需要进行不同的编码处理
            if(agent.indexOf("FIREFOX") != -1){//火狐浏览器
                response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("GB2312"),"ISO-8859-1"));
            }else{//其他浏览器
                response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            }
            //设置response编码
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Length", "" + f.length());
            //设置输出文件类型
            response.setContentType("video/mpeg4");
            //获取response输出流
            os = response.getOutputStream();
            // 输出文件
            os.write(buffer);
        }catch(Exception e){
            log.error("获取视频流异常！原因：",e);
        } finally{
            //关闭流
            try {
                if(fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                log.error("fis.close()关闭异常！原因：",e);
            } finally{
                try {
                    if(os != null){
                        os.flush();
                    }
                } catch (IOException e) {
                    log.error("os.flush()清除异常！原因：",e);
                } finally{
                    try {
                        if(os != null){
                            os.close();
                        }
                    } catch (IOException e) {
                        log.error("os.close()关闭异常！原因：",e);
                    }
                }
            }
        }
    }
}
