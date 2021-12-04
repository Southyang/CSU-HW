## 关于homework5的碎碎念：

- java是后端业务

- web是前端 UI

- **只上传了代码和用到的jar包，直接下载肯定是不可以运行的！！！**

- java文件夹里的结构，自己百度一下就知道每个是干什么用的了，其实我也分的很粗糙

- **不要直接复制！！！因为不能直接运行**

- 关于图形验证码需要对web.xml文件进行配置,配置方式如下：

  `<servlet>
          <servlet-name>KaptchaServlet</servlet-name>
          <servlet-class>com.google.code.kaptcha.servlet.KaptchaServlet</servlet-class>
      </servlet>
      <servlet-mapping>
          <servlet-name>KaptchaServlet</servlet-name>
          <url-pattern>/kaptcha.jpg</url-pattern>
      </servlet-mapping>`

- 邮箱需要开启自己邮箱的SMTP/POP服务
- 数据库需要自己写一个properties，然后模仿我的接口文件直接用就好了
- 放进仓库的本意是让大家共同学习，可以直接发issue问我，也可以直接用邮箱联系我
- 邮箱：i@southyang.cn