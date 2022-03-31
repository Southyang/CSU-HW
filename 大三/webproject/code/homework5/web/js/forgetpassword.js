function getXhr(){
    var xhr = new XMLHttpRequest();
    console.log(xhr);
    return xhr;
}
function findpassword(){
    var zhanghao = document.getElementById("inputzhanghao");
    var dongtaima = document.getElementById("inputdongtaima");
    //发送异步请求进行校验
    var xhr = getXhr();
    console.log(zhanghao.value + " test " + dongtaima.value);
    //设置请求信息
    xhr.open("get" , "../Getpassword?username=" + zhanghao.value  + "&dongtaima=" + dongtaima.value, true);
    //发送请求
    xhr.send();
    //监听readyState状态
    xhr.onreadystatechange = function (){
        if(xhr.readyState == 4){ //响应处理完成
            if(xhr.status == 200){ //处理正确
                //获取响应数据
                var msg = xhr.responseText;
                //显示提示信息
                document.getElementById("passwordcontent").innerHTML = "您的密码为:" + msg;
            }
        }
    }
}

function getdongtaima(){
    var youxiang = document.getElementById("inputyouxiang");
    //发送异步请求进行校验
    var xhr = getXhr();
    console.log(inputyouxiang.value);
    //设置请求信息
    xhr.open("get" , "../Getdongtaima?youxiang=" + youxiang.value, true);
    //发送请求
    xhr.send();
    //监听readyState状态
    xhr.onreadystatechange = function (){
        if(xhr.readyState == 4){ //响应处理完成
            if(xhr.status == 200){ //处理正确
                //获取响应数据
                var msg = xhr.responseText;
                //显示提示信息
                document.getElementById("dongtaimacontent").value = msg;
            }
        }
    }
}
