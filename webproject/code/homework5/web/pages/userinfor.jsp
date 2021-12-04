<%@ page import="User.User" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 杨昊楠
  Date: 2021/12/3
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理端</title>
    <link rel="stylesheet" href="http://localhost:8080/test_war_exploded/css/userinfor.css">
</head>
<body>
    <h1 align="center"> 欢迎 <font color="blue">${sessionScope.loginUser}</font> 登录 </h1>
    <br><br>
    <h2 align="center"> 用户信息表 </h2>
    <table border="1px" width="70%" align="center" cellspacing="0px">
        <tr align="center">
            <th>ID</th>
            <th>Username</th>
            <th>Password</th>
            <th>Email</th>
            <th>Operation</th>
        </tr>
        <!-- 通过循环显示 -->
        <%
            //获取数据
            List<User> users = (List<User>) request.getAttribute("users");

            for(User user : users){
        %>
            <tr align="center">
                <td><%=user.getId()%></td>
                <td><%=user.getUsername()%></td>
                <td><%=user.getPassword()%></td>
                <td><%=user.getEmail()%></td>
                <td>
                    <span style="color: black" onclick="edituser()"> Edit </span>
                    &nbsp; &nbsp;
                    <span style="color: black" onclick="deleteuser()"> Delete </span>
                </td>
            </tr>
        <%
            }
        %>
    </table>
    <h3 align="center"><div href="#" style="color: dodgerblue;" onclick="adduser()">Add New User</div></h3>

    <div class="edituser" id="edituser">
        <b>输入账号</b>
        <input type="text" name="editusername" id="editusername"> <br>
        <b>输入原密码</b>
        <input type="text" name="editoldpassword" id="editoldpassword"> <br>
        <b>输入新密码</b>
        <input type="text" name="editnewpassword" id="editnewpassword"> <br>
        <a class="editconfirm" id="editconfirm" onclick="edituserconfirm()">确认</a>
        <a class="editback" id="editback" onclick="edituserback()">取消</a>
    </div>

    <div class="deleteuser" id="deleteuser">
        <b>输入账号</b>
        <input type="text" name="deleteusername" id="deleteusername"> <br>
        <b>输入密码</b>
        <input type="text" name="deletepassword" id="deletepassword"> <br>
        <b>输入邮箱</b>
        <input type="text" name="deleteemail" id="deleteemail"> <br>
        <a class="deleteconfirm" id="deleteconfirm" onclick="deleteuserconfirm()">确认</a>
        <a class="deleteback" id="deleteback" onclick="deleteuserback()">取消</a>
    </div>

    <div class="adduser" id="adduser">
        <b>输入账号</b>
        <input type="text" name="deleteusername" id="addusername"> <br>
        <b>输入密码</b>
        <input type="text" name="deletepassword" id="addpassword"> <br>
        <b>输入邮箱</b>
        <input type="text" name="deleteemail" id="addemail"> <br>
        <a class="addconfirm" id="addconfirm" onclick="adduserconfirm()">确认</a>
        <a class="addback" id="addback" onclick="adduserback()">取消</a>
    </div>

    <!-- 回到首页 -->
    <a class="go-login" href="./login.jsp">
        回到首页
    </a>
</body>
<script type="text/javascript" src="http://localhost:8080/test_war_exploded/js/userinfor.js"></script>
</html>
