<%--
  Created by IntelliJ IDEA.
  User: 杨昊楠
  Date: 2021/11/29
  Time: 11:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
<div class="login-menhu">
    <!-- 背景轮播图 -->
    <div class="login-menhu-lunbo">
        <!-- 仅放一张图片，通过JS来改变图片地址即可-->
        <img id="lunboimg" src="sources/background/background1.jpg"/>
    </div>

    <!-- logo栏目 -->
    <div class="login-menhu-logo">
        <img id="logoimg" src="sources/Logo_00.png">
        <div class="login-menhu-logo-text">
            <span style="float: left"> Southyang.cn </span>
            <div class="fenge"></div>
            <span> 统一身份认证 </span>
        </div>
    </div>

    <!-- 登录 -->
    <div class="login-menhu-infor">
        <div class="login-menhu-infor-insert">
            <div class="choose" id="choose">
                <div class="zhanghao" id="zhanghao" onclick="choosezhanghao()">
                    账号登录
                    <div class="hengxian" id="zhanghaohengxian"></div>
                </div>
                <div class="youxiang" id="youxiang" onclick="chooseyouxiang()">
                    邮箱登录
                    <div class="hengxian" id="youxianghengxian"></div>
                </div>
            </div>

            <div class="content" id="content">
                <form method="POST" name="biaodanpost" action="LoginServlet">
                    <div class="biaodan" id="biaodan">
                        <div class="biaodan-input">
                            <b>账号</b>
                            <input type="text" id="inputzhanghao" placeholder="请输入账号" name="zhanghao" onfocus="clearLoginMsg()">
                        </div>
                        <div id="errorinput" class="errorinput" style="color: red"> ${requestScope.login_msg} </div>
                        <div class="biaodan-input">
                            <b>密码</b>
                            <input type="password" id="inputmima" placeholder="请输入密码" name="mima">
                        </div>
                        <div class="biaodan-input">
                            <b>验证码</b>
                            <input type="text" id="inputyanzhengma" placeholder="请输入验证码" name="yanzhengma">
                            <span id="imgyanzhengma">
                                <img id="code_img" src="kaptcha.jpg" alt="imgyanzhengma" onclick="this.src='./kaptcha.jpg?d=' + new Date()*1">
                            </span>
                        </div>
                        <input type="submit" class="login-button1" value="登录">
                    </div>
                </form>

                <form method="POST" name="youxiangbiaodanpost" action="LoginemailServlet">
                    <div class="youxiangbiaodan" id="youxiangbiaodan">
                        <div class="youxiangbiaodan-input">
                            <b>邮箱</b>
                            <input id="inputyouxiang" placeholder="请输入邮箱" name="youxiang">
                        </div>
                        <div class="youxiangbiaodan-input">
                            <b>验证码</b>
                            <input id="inputyanzhengma1" placeholder="请输入验证码" name="yanzhengma1">
                            <span id="imgyanzhengma1">
                                <img id="code_img1" src="kaptcha.jpg" alt="imgyanzhengma" onclick="this.src='./kaptcha.jpg?d=' + new Date()*1">
                            </span>
                        </div>
                        <div class="youxiangbiaodan-input">
                            <b>动态码</b>
                            <input id="inputdongtaima" placeholder="请输入邮箱动态码" name="dongtaima">
                            <div class="huoqu" onclick="getdongtaima()"> 获取动态码</div>
                        </div>
                        <div id="errorinput1" class="errorinput1" style="color: red"> ${requestScope.login_msg1} </div>
                        <input id="dongtaimacontent" type="text" value="" name="dongtaimacontent" style="display: none">
                        <input type="submit" class="login-button2" value="登录">
                    </div>
                </form>

        </div>

        <div class="otherthings">
            <div class="otherthings-mima">
                <a href="pages/forgetpassword.jsp">忘记密码</a>
                <a href="pages/modifypassword.jsp">修改密码</a>
            </div>
            <div class="otherthings-zhanghao">
                <a href="pages/register.jsp">注册账号</a>
                <a href="pages/deleteinfor.jsp">注销账号</a>
            </div>
        </div>
    </div>
    <div class="login-menhu-infor-developer">
        开发者 : Southyang. <br>
        联系方式 : 17873149260 <br>
        邮箱地址 : 8208190213@csu.edu.cn
    </div>
</div>

    <!-- 管理员按钮 -->
    <a class="go-manager" href="./pages/manager.jsp">
        我是管理员
    </a>
</div>
</body>
<script type="text/javascript" src="js/login.js"></script>
</html>
