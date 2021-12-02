var imgsrc = ['../sources/background/background1.jpg', '../sources/background/background2.jpg',
    '../sources/background/background3.jpg', '../sources/background/background4.jpg',
    "../sources/background/background5.jpg", '../sources/background/background6.jpg'];
let lunboimg = document.getElementById("lunboimg");
let index = 1;
let timer;
function show() {
    lunboimg.src = imgsrc[index];
}

function autoPlay() {
    timer = setInterval(function () {
        if (index == 6) {
            index = 0;
        }
        show();
        index++;
    }, 3000);
}
autoPlay();