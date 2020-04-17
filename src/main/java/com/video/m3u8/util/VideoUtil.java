package com.video.m3u8.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        if (StringUtils.isEmpty(videoUrl)) throw new NullPointerException("视频路径(videoName)为空!");
        if (StringUtils.isEmpty(videoName)) throw new NullPointerException("视频名称(videoName)为空!");
        if (StringUtils.isEmpty(ffmpeg)) throw new NullPointerException("ffmpeg为空!");

        //将视频文件分割成视频流文件ts，并生成索引文件m3u8
        //根据视频大小选择用哪个。比如：视频大于100M就选择第二种，小于100M就选择第一种
        //问题：可能存在视频太大出现切分的ts会很大，导致指定视频进度时可能会卡住，原视频拆分
        //优点：生成ts文件快，可以重新命名ts文件
        //最常用：ffmpeg -i foo.mp4 -codec copy -vbsf h264_mp4toannexb -map 0 -f segment -segment_list out.m3u8 -segment_time 10 out%03d.ts
        String name = videoName.substring(0, videoName.lastIndexOf('.')); //获取视频名称
        String id = UUID.randomUUID().toString().replace("-","");
        List<String> command = new ArrayList<>();
//        command.add(ffmpeg);
//        command.add("-i");
//        command.add(videoUrl+"\\"+videoName);
//        command.add("-codec");
//        command.add("copy");
//        command.add("-vbsf");
//        command.add("h264_mp4toannexb");
//        command.add("-map");
//        command.add("0");
//        command.add("-f");
//        command.add("segment");
//        command.add("-segment_list");
//        command.add(videoUrl+"\\"+name+"\\index.m3u8");
//        command.add("-segment_time");
//        command.add("8");
//        command.add(videoUrl+"\\"+name+"\\"+id+"%03d.ts");
        //问题：生成ts文件慢，不能重新命名ts文件，默认为index00.ts，会按照默认标准压缩，如果视频在标准下就会越压大，在标准上就会压小
        //优点：压缩视频并拆分，可以减少视频大小
        //ts：ffmpeg -i E:\demo\demo.MP4 -profile:v baseline -level 3.0 -s 640x360 -start_number 0 -hls_time 10 -hls_list_size 0 -f hls E:\demo\demo.m3u8
        command.add(ffmpeg);
        command.add("-i");
        command.add(videoUrl+"\\"+videoName);
        command.add("-profile:v");
        command.add("baseline");
        command.add("-level");
        command.add("3.0");
        command.add("-start_number");
        command.add("0");
        command.add("-hls_time");
        command.add("8");
        command.add("-hls_list_size");
        command.add("0");
        command.add("-f");
        command.add("hls");
        command.add(videoUrl+"\\"+name+"\\index.m3u8");
        File file = new File(videoUrl + "\\" + name);
        if (!file.exists()) {
            file.mkdirs();
        }
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
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
        log.info("ffmpeg生成m3u8视频流文件信息：" + sbf.toString());
    }
}
