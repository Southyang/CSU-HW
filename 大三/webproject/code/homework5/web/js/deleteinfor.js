function getXhr(){
    var xhr = new XMLHttpRequest();
    console.log(xhr);
    return xhr;
}
function deleteinfor(){
    var zhanghao = document.getElementById("inputzhanghao");
    var password = document.getElementById("inputmima");
    var youxiang = document.getElementById("inputyouxiang");
    //发送异步请求进行校验
    var xhr = getXhr();
    console.log(zhanghao.value);
    //设置请求信息
    xhr.open("get" , "../Deleteinfor?username=" + zhanghao.value + "&password=" + password.value
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
                alert("您的账号已注销");
                //document.getElementById("passwordcontent").innerHTML = "您的密码为:" + msg;
            }
        }
    }
}