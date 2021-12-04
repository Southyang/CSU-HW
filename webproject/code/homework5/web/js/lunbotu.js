var imgsrc = ['http://localhost:8080/test_war_exploded/sources/background/background1.jpg',
    'http://localhost:8080/test_war_exploded/sources/background/background2.jpg',
    'http://localhost:8080/test_war_exploded/sources/background/background3.jpg',
    'http://localhost:8080/test_war_exploded/sources/background/background4.jpg',
    "http://localhost:8080/test_war_exploded/sources/background/background5.jpg",
    'http://localhost:8080/test_war_exploded/sources/background/background6.jpg'];
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