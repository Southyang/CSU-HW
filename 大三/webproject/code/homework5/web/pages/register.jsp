<%--
  Created by IntelliJ IDEA.
  User: 杨昊楠
  Date: 2021/12/2
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <link rel="stylesheet" href="../css/register.css">
</head>
<body>
<div class="register">
    <!-- 背景轮播图 -->
    <div class="register-lunbo">
        <!-- 仅放一张图片，通过JS来改变图片地址即可-->
        <img id="lunboimg" src="http://localhost:8080/test_war_exploded/sources/background/background1.jpg"/>
    </div>

    <!-- logo栏目 -->
    <div class="register-logo">
        <img id="logoimg" src="../sources/Logo_00.png">
        <div class="register-logo-text">
            <span style="float: left"> Southyang.cn </span>
            <div class="fenge"></div>
            <span> 统一身份认证 </span>
        </div>
    </div>

    <!-- 注册面板 -->
    <div class="register-infor">
        <div class="register-infor-insert">
            <div class="register-infor-insert-content">
                注册账号
            </div>
            <form method="POST" name="biaodanpost" action="../RegisterServlet" onsubmit="return checksubmit()">
                <div class="biaodan">
                    <div class="biaodan-input">
                        <b>账号</b>
                        <input type="text" id="inputzhanghao" placeholder="请输入账号" name="zhanghao" onfocus="clearerrorcontent()" onblur="checkzhanghao()">
                    </div>
                    <div class="errorzhanghao" id="errorzhanghao" style="color: red">${requestScope.register_msg}</div>
                    <div class="biaodan-input">
                        <b>密码</b>
                        <input type="password" id="inputmima" placeholder="请输入密码" name="mima">
                    </div>
                    <div class="biaodan-input">
                        <b>确认密码</b>
                        <input type="password" id="reinputmima" placeholder="请再次输入密码" name="remima" onblur="checkrepass()">
                    </div>
                    <div class="errorrepas" id="errorrepas" style="color: red"></div>
                    <div class="biaodan-input">
                        <b>邮箱</b>
                        <input type="email" id="inputyouxiang" placeholder="请输入邮箱" name="youxiang">
                    </div>
                    <input type="submit" class="register-button" value="注册">
                </div>
            </form>
        </div>
        <div class="register-infor-developer">
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
<script type="text/javascript" src="../js/lunbotu.js"></script>
<script type="text/javascript" src="../js/register.js"></script>
</html>
