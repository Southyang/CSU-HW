<%--
  Created by IntelliJ IDEA.
  User: 杨昊楠
  Date: 2021/12/2
  Time: 15:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <link rel="stylesheet" href="../css/manager.css">
</head>
<body>
<div class="manager">
    <!-- 背景轮播图 -->
    <div class="manager-lunbo">
        <!-- 仅放一张图片，通过JS来改变图片地址即可-->
        <img id="lunboimg" src="../sources/background/background1.jpg"/>
    </div>

    <!-- logo栏目 -->
    <div class="manager-logo">
        <img id="logoimg" src="../sources/Logo_00.png">
        <div class="manager-logo-text">
            <span style="float: left"> Southyang.cn </span>
            <div class="fenge"></div>
            <span> 统一身份认证 </span>
        </div>
    </div>

    <!-- 注册面板 -->
    <div class="manager-infor">
        <div class="manager-infor-insert">
            <div class="manager-infor-insert-content">
                管理员登录
            </div>
            <form method="POST" name="biaodanpost">
                <div class="biaodan">
                    <div class="biaodan-input">
                        <b>管理员账号</b>
                        <input id="inputzhanghao" placeholder="请输入账号" name="zhanghao">
                    </div>
                    <div class="biaodan-input">
                        <b>管理员密码</b>
                        <input id="inputmima" placeholder="请输入密码" name="mima">
                    </div>
                    <input type="submit" class="manager-button" value="登录">
                </div>
            </form>
        </div>
        <div class="manager-infor-developer">
            开发者 : Southyang. <br>
            联系方式 : 178xxxxxxxx <br>
            邮箱地址 : 8208xxxxxx@csu.edu.cn
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="../js/lunbotu.js"></script>
</html>
