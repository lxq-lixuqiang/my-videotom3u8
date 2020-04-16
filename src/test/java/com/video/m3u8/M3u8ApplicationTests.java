package com.video.m3u8;

import com.video.m3u8.util.Common;
import com.video.m3u8.util.VideoUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class M3u8ApplicationTests {

    @Test
    void contextLoads() {
        try {
            /**
             * 将video转为m3u8
             */
            VideoUtil.videoToM3u8(Common.ffmpeg,Common.videoUrl,Common.videoName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
