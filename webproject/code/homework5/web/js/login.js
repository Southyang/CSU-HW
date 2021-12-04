var imgsrc = ['./sources/background/background1.jpg', './sources/background/background2.jpg',
    './sources/background/background3.jpg', './sources/background/background4.jpg',
    "./sources/background/background5.jpg", './sources/background/background6.jpg'];
let lunboimg = document.getElementById("lunboimg");
let index = 1;
let timer;
function show() {
    lunboimg.src = imgsrc[index];
}

function autoPlay() {
    timer = setInterval(function () {
        if (index === 6) {
            index = 0;
        }
        show();
        index++;
    }, 3000);
}
autoPlay();

function choosezhanghao(){
    document.getElementById("biaodan").style.display = "block";
    document.getElementById("zhanghao").style.color = "deepskyblue";
    document.getElementById("zhanghaohengxian").style.background = "deepskyblue";
    document.getElementById("youxiangbiaodan").style.display = "none";
    document.getElementById("youxiang").style.color = "grey";
    document.getElementById("youxianghengxian").style.background = "grey";
}

function chooseyouxiang(){
    document.getElementById("biaodan").style.display = "none";
    document.getElementById("zhanghao").style.color = "grey";
    document.getElementById("zhanghaohengxian").style.background = "grey";
    document.getElementById("youxiangbiaodan").style.display = "block";
    document.getElementById("youxiang").style.color = "deepskyblue";
    document.getElementById("youxianghengxian").style.background = "deepskyblue";
}

function clearLoginMsg(){
    var errorcontent = document.getElementById("errorinput");
    errorcontent.innerHTML = "";
}

function getXhr(){
    var xhr = new XMLHttpRequest();
    console.log(xhr);
    return xhr;
}

function getdongtaima(){
    var youxiang = document.getElementById("inputyouxiang");
    //发送异步请求进行校验
    var xhr = getXhr();
    console.log(inputyouxiang.value);
    //设置请求信息
    xhr.open("get" , "./Getdongtaima?youxiang=" + inputyouxiang.value , true);
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
