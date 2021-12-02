var imgsrc = ['https://www.csu.edu.cn/images/banner20201230_fayi.jpg', 'https://www.csu.edu.cn/images/banner20210528_wsbsdtsx.jpg',
    'https://www.csu.edu.cn/images/banner20210602_dj.png', 'https://www.csu.edu.cn/images/hyjs.jpg'];
let img = document.querySelector('#lunboimg>a>img');
let span = document.querySelectorAll('#selector>span');
let index = 1;
let timer;
changecolor(1, 0);
function show() {
    img.src = imgsrc[index];
    clearcolor();
    changecolor(index);
}

function clearcolor() {
    document.getElementById("0").style.backgroundColor = "#eceef4";
    document.getElementById("1").style.backgroundColor = "#eceef4";
    document.getElementById("2").style.backgroundColor = "#eceef4";
    document.getElementById("3").style.backgroundColor = "#eceef4";
}

function changecolor(i) {
    document.getElementById(i).style.backgroundColor = "#ff9900";
}

function autoPlay() {
    timer = setInterval(function () {
        if (index == 4) {
            index = 0;
        }
        show();
        index++;
    }, 3000);
}
autoPlay();

function ctrlPlay(i) {
    index = i;
    show();
}

function question() {
    document.getElementById("questext").style.display = "block";
    document.getElementById("quebutton").style.display = "none";
}

function closetext() {
    document.getElementById("questext").style.display = "none";
    document.getElementById("quebutton").style.display = "block";
}

var temp = 0;
function tanchu(flag) {
    var div1 = document.getElementById("div1");
    var div2 = document.getElementById("div2");
    var div3 = document.getElementById("div3");
    var div4 = document.getElementById("div4");
    var div5 = document.getElementById("div5");
    var div6 = document.getElementById("div6");
    var div7 = document.getElementById("div7");
    var jiantou = document.getElementById("jiantou");
    if (flag == "https://www.csu.edu.cn/images/i-q21.png") { //收回
        temp = 0;
        document.getElementById("jiantou").src = "https://www.csu.edu.cn/images/i-q21-1.png";
        div1.style.cssText = "width: 50px;margin-left: 89px;";
        div2.style.cssText = "width: 50px;margin-left: 89px;";
        div3.style.cssText = "width: 50px;margin-left: 89px;";
        div4.style.cssText = "width: 50px;margin-left: 89px;";
        div5.style.cssText = "width: 50px;margin-left: 89px;";
        div6.style.cssText = "width: 50px;margin-left: 89px;";
        div7.style.cssText = "width: 50px;margin-left: 89px;";
        jiantou.style.cssText = "margin-left: 89px";
    }
    else { //弹出
        temp = 1;
        document.getElementById("jiantou").src = "https://www.csu.edu.cn/images/i-q21.png";
        div1.style.cssText = "width: 140px;margin-left: 0px;";
        div2.style.cssText = "width: 140px;margin-left: 0px;";
        div3.style.cssText = "width: 140px;margin-left: 0px;";
        div4.style.cssText = "width: 140px;margin-left: 0px;";
        div5.style.cssText = "width: 140px;margin-left: 0px;";
        div6.style.cssText = "width: 140px;margin-left: 0px;";
        div7.style.cssText = "width: 140px;margin-left: 0px;";
        jiantou.style.cssText = "margin-left: 50px";
    }
}

var list = document.getElementsByClassName("frightdiv");
for (var i = 0; i < list.length; i++) {
    //为frightdiv注册鼠标进入事件
    list[i].onmouseover = function () {
        this.style.cssText = "background-color: #bc8d00;width: 140px;margin-left: 0px;";
    };
    //为frightdiv注册鼠标离开事件
    list[i].onmouseout = function () {
        if (temp == 1) {
            this.style.cssText = "background-color: #0651a2;width: 140px;margin-left: 0px;";
        }
        else if(temp == 0) {
            this.style.cssText = "background-color: #0651a2;margin-top: 2px;width: 50px;height: 50px;margin-left: 89px;overflow: hidden;padding-top: 1px;"
        }
    };
}

var edu = document.getElementById("edu");
var exper = document.getElementById("exper");
var dd1 = document.getElementById("dd1");
var dd2 = document.getElementById("dd2");
edu.onmousemove = function () {
    document.getElementById("jiantou1").src = "https://www.csu.edu.cn/images/i-down.png";
    console.log(document.getElementById("jiantou1").style.src);
}
edu.onmouseout = function () {
    document.getElementById("jiantou1").src = "https://www.csu.edu.cn/images/i-down1.png";
    console.log(document.getElementById("jiantou1").style.src);
}
dd1.onmousemove = function () {
    document.getElementById("jiantou1").src = "https://www.csu.edu.cn/images/i-down.png";
    console.log(document.getElementById("jiantou1").style.src);
}
dd1.onmouseout = function () {
    document.getElementById("jiantou1").src = "https://www.csu.edu.cn/images/i-down1.png";
    console.log(document.getElementById("jiantou1").style.src);
}

exper.onmousemove = function () {
    document.getElementById("jiantou2").src = "https://www.csu.edu.cn/images/i-down.png";
}
exper.onmouseout = function () {
    document.getElementById("jiantou2").src = "https://www.csu.edu.cn/images/i-down1.png";
}
dd2.onmousemove = function () {
    document.getElementById("jiantou2").src = "https://www.csu.edu.cn/images/i-down.png";
}
dd2.onmouseout = function () {
    document.getElementById("jiantou2").src = "https://www.csu.edu.cn/images/i-down1.png";
}