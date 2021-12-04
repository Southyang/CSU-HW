function getXhr(){
    var xhr = new XMLHttpRequest();
    console.log(xhr);
    return xhr;
}

function edituser(){
    document.getElementById("edituser").style.display = "block";
}

function deleteuser(){
    document.getElementById("deleteuser").style.display = "block";
}

function adduser(){
    document.getElementById("adduser").style.display = "block";
}

function edituserback(){
    document.getElementById("edituser").style.display = "none";
}

function deleteuserback(){
    document.getElementById("deleteuser").style.display = "none";
}

function adduserback(){
    document.getElementById("adduser").style.display = "none";
}

function edituserconfirm(){
    var username = document.getElementById("editusername");
    var oldpassword = document.getElementById("editoldpassword");
    var newpassword = document.getElementById("editnewpassword");
    //发送异步请求进行校验
    var xhr = getXhr();
    console.log(username.value);
    //设置请求信息
    xhr.open("get" , "./Modifypassword?username=" + username.value + "&password=" + oldpassword.value
        + "&newpassword=" + newpassword.value, true);
    //发送请求
    xhr.send();
    //监听readyState状态
    xhr.onreadystatechange = function (){
        if(xhr.readyState == 4){ //响应处理完成
            if(xhr.status == 200){ //处理正确
                //获取响应数据
                var msg = xhr.responseText;
                //显示提示信息
                alert("已成功修改");
                document.getElementById("edituser").style.display = "none";
            }
        }
    }
}

function deleteuserconfirm(){
    var username = document.getElementById("deleteusername");
    var password = document.getElementById("deletepassword");
    var youxiang = document.getElementById("deleteemail");
    //发送异步请求进行校验
    var xhr = getXhr();
    console.log(username.value);
    //设置请求信息
    xhr.open("get" , "./Deleteinfor?username=" + username.value + "&password=" + password.value
        + "&youxiang=" + youxiang.value, true);
    //发送请求
    xhr.send();
    //监听readyState状态
    xhr.onreadystatechange = function (){
        if(xhr.readyState == 4){ //响应处理完成
            if(xhr.status == 200){ //处理正确
                //获取响应数据
                var msg = xhr.responseText;
                //显示提示信息
                alert("账号已注销");
                document.getElementById("deleteuser").style.display = "none";
            }
        }
    }
}

function adduserconfirm(){
    var username = document.getElementById("addusername");
    var password = document.getElementById("addpassword");
    var youxiang = document.getElementById("addemail");
    //发送异步请求进行校验
    var xhr = getXhr();
    console.log(username.value);
    //设置请求信息
    xhr.open("get" , "./RegisterServlet?zhanghao=" + username.value + "&mima=" + password.value
        + "&youxiang=" + youxiang.value, true);
    //发送请求
    xhr.send();
    //监听readyState状态
    xhr.onreadystatechange = function (){
        if(xhr.readyState == 4){ //响应处理完成
            if(xhr.status == 200){ //处理正确
                //获取响应数据
                var msg = xhr.responseText;
                //显示提示信息
                alert("账号已成功添加");
                document.getElementById("adduser").style.display = "none";
            }
        }
    }
}
