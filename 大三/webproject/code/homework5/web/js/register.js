var flagmima = true;
var flagzhanghao = true;
function getXhr(){
    var xhr = new XMLHttpRequest();
    console.log(xhr);
    return xhr;
}
function checkzhanghao(){
    var zhanghao = document.getElementById("inputzhanghao");
    //发送异步请求进行校验
    var xhr = getXhr();
    console.log(zhanghao.value);
    //设置请求信息
    xhr.open("get" , "../CheckUsername?username=" + zhanghao.value , true);
    //发送请求
    xhr.send();
    //监听readyState状态
    xhr.onreadystatechange = function (){
        if(xhr.readyState == 4){ //响应处理完成
            if(xhr.status == 200){ //处理正确
                //获取响应数据
                var msg = xhr.responseText;
                //显示提示信息
                document.getElementById("errorzhanghao").innerHTML = msg;
            }
        }
    }
}

function clearerrorcontent(){
    document.getElementById("errorzhanghao").innerHTML = "";
}

function checkrepass(){
    var pass = document.getElementById("inputmima").value;
    var repass = document.getElementById("reinputmima").value;
    var errorrepas = document.getElementById("errorrepas");

    if (pass !== "" && repass !== "" && pass === repass){
        errorrepas.innerHTML = "密码一致";
        flagmima = true;
    }else {
        errorrepas.innerHTML = "密码不一致";
        flagmima = false;
    }
    //alert("密码判断:" + flagmima);
}

function checksubmit(){
    var errorzhanghao = document.getElementById("errorzhanghao").innerText;
    //alert(errorzhanghao + " " + "用户名已存在");
    if(errorzhanghao === "用户名已存在"){
        flagzhanghao = false;
        //alert("1 flase");
    }else {
        flagzhanghao = true;
        //alert("2 true");
    }

    return flagzhanghao && flagmima;
}