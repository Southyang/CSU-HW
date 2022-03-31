<%--
  Created by IntelliJ IDEA.
  User: 杨昊楠
  Date: 2021/12/2
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <link rel="stylesheet" href="../css/forgetpassword.css">
</head>
<body>
<div class="forgetpassword">
    <!-- 背景轮播图 -->
    <div class="forgetpassword-lunbo">
        <!-- 仅放一张图片，通过JS来改变图片地址即可-->
        <img id="lunboimg" src="../sources/background/background1.jpg"/>
    </div>

    <!-- logo栏目 -->
    <div class="forgetpassword-logo">
        <img id="logoimg" src="../sources/Logo_00.png">
        <div class="forgetpassword-logo-text">
            <span style="float: left"> Southyang.cn </span>
            <div class="fenge"></div>
            <span> 统一身份认证 </span>
        </div>
    </div>

    <!-- 注册面板 -->
    <div class="forgetpassword-infor">
        <div class="forgetpassword-infor-insert">
            <div class="forgetpassword-infor-insert-content">
                找回密码
            </div>
            <div class="biaodan">
                <div class="biaodan-input">
                    <b>账号</b>
                    <input id="inputzhanghao" placeholder="请输入账号" name="zhanghao">
                </div>
                <div class="biaodan-input">
                    <b>邮箱</b>
                    <input id="inputyouxiang" placeholder="请输入邮箱" name="youxiang">
                </div>
                <div class="biaodan-input">
                    <b>动态码</b>
                    <input id="inputdongtaima" placeholder="请输入动态码" name="dongtaima">
                    <div class="huoqu" onclick="getdongtaima()"> 获取动态码</div>
                </div>
                <div id="passwordcontent" class="passwordcontent"></div>
                <input type="button" class="forgetpassword-button" value="查找" onclick="findpassword()">
                <input id="dongtaimacontent" type="text" value="" name="dongtaimacontent" style="display: none">
            </div>
        </div>
        <div class="forgetpassword-infor-developer">
            开发者 : Southyang. <br>
            联系方式 : 178xxxxxxxx <br>
            邮箱地址 : 8208xxxxxx@csu.edu.cn
        </div>
    </div>

    <!-- 回到首页 -->
    <a class="go-login" href="../login.jsp">
        回到首页
    </a>
</div>
</body>
<script type="text/javascript" src="../js/forgetpassword.js"></script>
<script type="text/javascript" src="../js/lunbotu.js"></script>
</html>
