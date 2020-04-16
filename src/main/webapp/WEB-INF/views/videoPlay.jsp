<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>video视频加密播放(Blob Url)</title>
</head>
<body style="text-align: center;">
    <h1>video视频加密播放(Blob Url)</h1>
    <div>
        <video id="video" width="400px" controls="controls"></video>
    </div>
    <script type="text/javascript">
        // 完整视频流
        //创建XMLHttpRequest对象
        var xhr = new XMLHttpRequest();
        //配置请求方式、请求地址以及是否同步
        xhr.open('POST', '/videoPlay', true);
        //设置请求结果类型为blob
        xhr.responseType = 'blob';
        //请求成功回调函数
        xhr.onload = function(e) {
            if (this.status == 200) {//请求成功
                //获取blob对象
                var blob = this.response;
                //获取blob对象地址，并把值赋给容器
                document.getElementById("video").src = URL.createObjectURL(blob);
            }
        };
        xhr.send();
    </script>

</body>
</html>
