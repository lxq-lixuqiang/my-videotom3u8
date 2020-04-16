<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>m3u8视频加密播放(Blob Url)</title>
</head>
<body style="text-align: center;">
    <h1>m3u8视频加密播放(Blob Url)</h1>
    <div>
        <video id="video" width="400px" controls="controls"></video>
    </div>
    <script type="text/javascript" src="/static/js/hls.min.js"></script>
    <script type="text/javascript">
        if(Hls.isSupported()) {
            var video = document.getElementById('video');
            var hls = new Hls();
            hls.loadSource('/m3u8Play/index.m3u8');
            hls.attachMedia(video);
            hls.on(Hls.Events.MANIFEST_PARSED,function() {
                video.play();
            });
        }else if (video.canPlayType('application/vnd.apple.mpegurl')) {
            video.src = '/m3u8Play/index.m3u8';
            video.addEventListener('canplay',function() {
                video.play();
            });
        }
    </script>

</body>
</html>
