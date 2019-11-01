<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport"
          content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <title>转盘抽奖</title>
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        #div1 {
            margin: 0 auto;
            height: 100px;
            width: 500px;
            margin-top: 10px;
            border-radius: 10px;
            background: red;
            text-align: center;
            margin-bottom: 10px;
        }

        #divhead {
            width: 100%;
            background: blue;
            height: 30%;
            border-radius: 10px 10px 0 0;
            color: white;
        }

        #divbody {
            width: 100%;
            height: 70%;
            font-size: 20px;
            margin-top: 20px;
        }

        #divbody input {
            height: 40px;
            line-height: 100%;
        }

        #divbody button {
            height: 40px;
            width: 80px;
        }
    </style>
    <script type="text/javascript" src="../../js/jquery.min.js" ></script>
    <script type="text/javascript" src="../../js/jQueryRotate.js"></script>
    <script type="text/javascript" src="../../js/jquery.easing.min.js"></script>
    <script>
        $(function(){
            $("#start").click(function(){
                runTurnTable();
            });
        });
        function runTurnTable(){
            $("#turnTableDiv").rotate({
                duration:5000,
                angle:0,
                animateTo: 360*6+45,
                callback:function(){
                    console.log("结束回调函数");
                }
            });
        }
    </script>
</head>

<body>
<!--canvas的样式必须写在标签内部，不能写在style-->
<div id="turnTableDiv">
    <canvas id="canvas" width="500px" height="500px" style="background:#fff;">您的浏览器不支持canvas标签</canvas>
</div>

<div style="position: absolute;">
    <canvas id="canvasCenter"></canvas>
</div>
<div><button id="start">开始</button></div>
</body>
<script>
    //在jquery中不能使用$("#canvas")获取画布对象
    var canvas = document.getElementById("canvas");
    var cxt = canvas.getContext("2d");

    var canvas2 = document.getElementById("canvasCenter");
    var cxt2 = canvas2.getContext("2d");

    var text = ["太善良", "异性缘好", "没有弱点", "过于专一", "憋尿工夫茶", "喜欢卖萌", "太招人喜欢", "太TM聪明", "ggsggggg"];
    var num = text.length;
    var time = -Math.PI / 2 + (Math.PI * 2 / num / 2); //屏幕宽度
    var clientW = document.body.scrollWidth; //圆心位置
    var center = clientW / 2; //外部圆半径
    var radiu = center * 0.9; //内部圆半径
    var innerRadiu = radiu * 0.2; //白线宽度
    var lineWidth = 6; //文字大小
    var fontSize = 20 * 8 / num; //文字最大长度
    var fontMaxLength = radiu * 0.6; //选择圈数
    var circleNum = 100; //结果序号
    var resIndex = -1;
    var clock = null;
    construct();

    function construct(isfirst) {
        canvas.width = clientW;
        canvas.height = clientW;
        cxt.clearRect(0, 0, clientW, clientW);
        darwSector();
        drawWhite();


        $("#canvasCenter").css("margin-top","-"+canvas.height+"px");
        canvas2.width = clientW;
        canvas2.height = clientW;
        cxt2.clearRect(0, 0, clientW, clientW);
        drawHand(0);
        drawineer();
    }
    /*		*随机生成颜色		*/
    function randColor() {
        var rand1 = Math.round(255 * Math.random());
        var rand2 = Math.round(255 * Math.random());
        var rand3 = Math.round(255 * Math.random());
        return "rgba(" + rand1 + "," + rand2 + "," + rand3 + "," + 1 + ")";
    }
    /*		*以水平向右为起边,顺时针画扇形		*/
    function darwSector() {
        var startPoint = 0;
        var add = Math.PI * 2 / num;
        for(var i = 0; i < num; i++) { //画扇形
            cxt.fillStyle = "#abcdef";
            cxt.beginPath();
            cxt.moveTo(center, center);
            cxt.arc(center, center, radiu * 0.9, startPoint, startPoint + add, false);
            cxt.closePath();
            cxt.fill();

            //画分割线
            cxt.save();
            cxt.beginPath();
            cxt.translate(center, center);
            cxt.moveTo(0, 0);
            cxt.lineWidth = lineWidth;
            cxt.strokeStyle = "rgb(250,250,250)";
            cxt.rotate(startPoint + add);
            cxt.lineTo(radiu * 0.9, 0);
            cxt.closePath();
            cxt.stroke();
            cxt.restore();
            startPoint = startPoint + add;
        } //写文字
        startPoint = 0;
        for(var i = 0; i < num; i++) {
            var txt = text[i];
            cxt.save();
            cxt.beginPath();
            cxt.textBaseline = "middle";
            cxt.translate(center, center);
            cxt.fillStyle = randColor();
            cxt.font = fontSize + "px Arial bolder";
            cxt.rotate(startPoint + add / 2);
            cxt.fillText(txt, innerRadiu * 2.4, 0, fontMaxLength);
            cxt.closePath();
            cxt.restore();
            startPoint = startPoint + add;
        }
    }
    /*		*画中间空白圆形		*/
    function drawWhite() { //画内部白色圆
        cxt.fillStyle = "#fff";
        cxt.beginPath();
        cxt.moveTo(center, center);
        cxt.arc(center, center, innerRadiu, 0, Math.PI * 2, false);
        cxt.closePath();
        cxt.fill();
    }
    /*		*画中间圆形		*/
    function drawineer() { //画内部蓝色圆
        cxt2.fillStyle = "blue";
        cxt2.beginPath();
        cxt2.moveTo(center, center);
        cxt2.arc(center, center, innerRadiu / 2, 0, Math.PI * 2, false);
        cxt2.closePath();
        cxt2.fill(); //画橙色圈
        cxt2.fillStyle = "#CC0000";
        cxt2.beginPath();
        cxt2.moveTo(center, center);
        cxt2.arc(center, center, innerRadiu * 0.5 * 0.8, 0, Math.PI * 2, false);
        cxt2.closePath();
        cxt2.fill(); //画黄色圈
        cxt2.fillStyle = "#FFCC00";
        cxt2.beginPath();
        cxt2.moveTo(center, center);
        cxt2.arc(center, center, innerRadiu * 0.5 * 0.75, 0, Math.PI * 2, false);
        cxt2.closePath();
        cxt2.fill();
    }
    /*		*画出指针		*/
    function drawHand(angle) {
        cxt2.save();
        cxt2.fillStyle = "blue";
        cxt2.beginPath();
        cxt2.translate(center, center);
        cxt2.rotate(angle);
        cxt2.moveTo(0, innerRadiu * 1.1);
        cxt2.lineTo(5, 0);
        cxt2.lineTo(-5, 0);
        cxt2.closePath();
        cxt2.fill();
        cxt2.restore();
        time = time + Math.PI * 2 / num;
        if(resIndex != num - 1) {
            resIndex++;
        } else {
            resIndex = 0;
        }
        if(time > 2 * Math.PI / num * circleNum) stopHand();
    }

</script>

</html>