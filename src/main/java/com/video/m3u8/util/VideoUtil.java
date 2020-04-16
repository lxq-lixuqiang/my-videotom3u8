package com.video.m3u8.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class VideoUtil {

    /**
     * ffmpeg生成m3u8视频流文件
     * @param ffmpeg ffmpeg-amd64-2.4.6-SNAPSHOT.exe运行器-添加依赖 jave-native-win64
     * @param videoUrl  视频地址
     * @param videoName 视频名称
     * @throws IOException
     * @throws NullPointerException
     */
    public static void videoToM3u8(String ffmpeg,String videoUrl,String videoName) throws IOException,NullPointerException {
        if(StringUtils.isEmpty(videoUrl)) throw new NullPointerException("视频路径(videoName)为空!");
        if(StringUtils.isEmpty(videoName)) throw new NullPointerException("视频名称(videoName)为空!");
        if(StringUtils.isEmpty(ffmpeg)) throw new NullPointerException("ffmpeg为空!");
        //先将视频转换成视频ts文件
        //ffmpeg -y -i F://video/1.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb F://video3/output.ts
        List<List<String>> commandList = new ArrayList<>();
        List<String> command = new ArrayList<>();
        command.add(ffmpeg);
        command.add("-y");
        command.add("-i");
        command.add(videoUrl+"\\"+videoName);
        command.add("-vcodec");
        command.add("copy");
        command.add("-acodec");
        command.add("copy");
        command.add("-vbsf");
        command.add("h264_mp4toannexb");
        command.add(videoUrl+"\\videotots.ts");
        commandList.add(command);
        //将ts视频文件分割成视频流文件ts，并生成索引文件m3u8
        //ffmpeg -i F://video3/output.ts  -c copy -map 0 -f segment -segment_list F://video3/index.m3u8 -segment_time 10 F://video3/video-%03d.ts
        String name = videoName.substring(0,videoName.lastIndexOf('.')); //获取视频名称
        List<String> command2 = new ArrayList<>();
        command2.add(ffmpeg);
        command2.add("-i");
        command2.add(videoUrl+"\\videotots.ts");
        command2.add("-c");
        command2.add("copy");
        command2.add("-map");
        command2.add("0");
        command2.add("-f");
        command2.add("segment");
        command2.add("-segment_list");
        command2.add(videoUrl+"\\"+name+"\\index.m3u8");
        command2.add("-segment_time");
        command2.add("5");
        command2.add(videoUrl+"\\"+name+"\\%03d.ts");
        commandList.add(command2);
        File file = new File(videoUrl+"\\"+name);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (List<String> saveCommand : commandList){
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(saveCommand);
            //正常信息和错误信息合并输出
            builder.redirectErrorStream(true);
            //开始执行命令
            Process process = builder.start();
            //如果你想获取到执行完后的信息，那么下面的代码也是需要的
            StringBuffer sbf = new StringBuffer();
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                sbf.append(line);
                sbf.append(" ");
            }
            log.info("ffmpeg生成m3u8视频流文件信息："+sbf.toString());
        }
        //删除xxx.ts文件
        File deleteFile = new File(videoUrl+"\\videotots.ts");
        deleteFile.delete();
    }
}
