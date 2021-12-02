<%--
  Created by IntelliJ IDEA.
  User: 杨昊楠
  Date: 2021/11/29
  Time: 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <%   // 重定向到新地址
    String site = new String("/test_war_exploded/pages/login.jsp");
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", site);
  %>
  </body>
</html>
