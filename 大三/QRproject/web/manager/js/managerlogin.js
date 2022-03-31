function getXhr(){
    var xhr = new XMLHttpRequest();
    console.log(xhr);
    return xhr;
}

function login(){
    var username = document.getElementById("username");
    var password = document.getElementById("password");

    console.log(username.value + " " + password.value);

    console.log('调用异步任务登录');
    //发送异步请求进行校验
    var xhr = getXhr();
    //设置请求信息
    xhr.open("get" , "http://47.242.148.245:27905/manager/login?username=" + username.value + "&password=" + password.value, true);
    //发送请求
    xhr.send();
    //监听readyState状态
    xhr.onreadystatechange = function (){
        if(xhr.readyState == 4 && xhr.status == 200){ //响应处理完成
 //处理正确
                //获取响应数据
                var msg = xhr.responseText;
                console.log(msg);
                //显示提示信息
                if(msg === "1"){
                    console.log("登录成功");
                    //window.location.href='http://southyang.cn:11731/demo/manager/pages/manager.html';
                    location.href="http://southyang.cn:11731/demo/manager/pages/manager.html?"+"username="+username.value;
                }
                else{
                    alert("账号或密码错误");
                    password.value = "";
                }
        }
    }
}