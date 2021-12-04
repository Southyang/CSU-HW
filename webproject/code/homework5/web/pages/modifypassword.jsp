<%--
  Created by IntelliJ IDEA.
  User: 杨昊楠
  Date: 2021/12/2
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <link rel="stylesheet" href="../css/modifypassword.css">
</head>
<body>
<div class="modifypassword">
    <!-- 背景轮播图 -->
    <div class="modifypassword-lunbo">
        <!-- 仅放一张图片，通过JS来改变图片地址即可-->
        <img id="lunboimg" src="../sources/background/background1.jpg"/>
    </div>

    <!-- logo栏目 -->
    <div class="modifypassword-logo">
        <img id="logoimg" src="../sources/Logo_00.png">
        <div class="modifypassword-logo-text">
            <span style="float: left"> Southyang.cn </span>
            <div class="fenge"></div>
            <span> 统一身份认证 </span>
        </div>
    </div>

    <!-- 注册面板 -->
    <div class="modifypassword-infor">
        <div class="modifypassword-infor-insert">
            <div class="modifypassword-infor-insert-content">
                修改密码
            </div>
            <div class="biaodan">
                <div class="biaodan-input">
                    <b>账号</b>
                    <input id="inputzhanghao" placeholder="请输入账号" name="zhanghao">
                </div>
                <div class="biaodan-input">
                    <b>原密码</b>
                    <input id="inputoldmima" placeholder="请输入原密码" name="oldmima">
                </div>
                <div class="biaodan-input">
                    <b>新密码</b>
                    <input id="inputnewmima" placeholder="请输入新密码" name="newmima">
                </div>
                <input type="button" class="modifypassword-button" value="确认修改" onclick="modifypassword()">
            </div>
        </div>
        <div class="modifypassword-infor-developer">
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
<script type="text/javascript" src="../js/modifypassword.js"></script>
<script type="text/javascript" src="../js/lunbotu.js"></script>
</html>
