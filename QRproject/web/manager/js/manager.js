var loc = location.href;
var n1 = loc.length;//地址的总长度
var n2 = loc.indexOf("=");//取得=号的位置
var id = decodeURI(loc.substr(n2 + 1, n1 - n2));//从=号后面的内容
document.getElementById("title").innerHTML = "管理员" + id + "登录";

function getXhr() {
    var xhr = new XMLHttpRequest();
    console.log(xhr);
    return xhr;
}

var key = ["id", "name", "tel", "address", "kinds", "premission", "state"];

function update() {
    document.getElementById("list").innerHTML = "";
    console.log('调用异步任务建表');
    //发送异步请求进行校验
    var xhr = getXhr();
    //设置请求信息
    xhr.open("get", "http://47.242.148.245:27905/manager/selectAllqr", true);
    //发送请求
    xhr.send();
    //监听readyState状态
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) { //响应处理完成
            //处理正确
            //获取响应数据
            var msg = xhr.responseText;
            //显示提示信息
            msg = strToJson(msg);
            console.log(msg);

            var datas = msg;

            datas = datas["list"];

            var tbody = document.querySelector('tbody');
            for (var i = 0; i < datas.length; i++) { // 外面的for循环管行 tr
                // 创建 tr行
                var tr = document.createElement('tr');
                tbody.appendChild(tr);
                // 行里面创建单元格(跟数据有关系的7个单元格) td 单元格的数量取决于每个对象里面的属性个数  for循环遍历对象 datas[i]
                for (var j = 0; j < key.length; j++) {
                    var td = document.createElement('td');
                    if (datas[i][key[j]] === "null" || datas[i][key[j]] === null) {
                        td.innerHTML = "暂未分配";
                    }
                    else{
                        td.innerHTML = datas[i][key[j]];
                    }
                    console.log(key[j]);
                    tr.appendChild(td);
                }
                // 创建 修改 2个字的单元格 
                var td = document.createElement('td');
                td.innerHTML = '<a href="javascript:;" onclick="modifypermission(this)">修改 </a>';
                tr.appendChild(td);
            }
        }
    }
}

function strToJson(str) {
    return JSON.parse(str);
}

function modifypermission(temp) {
    console.log(temp.innerHTML);
    var qrid = temp.parentNode.parentNode.cells[0].innerHTML;
    var qrpermission = prompt("请输入新权限所有者", ""); // 弹出input框
    if (qrpermission === null) {
        alert("退出修改");
        return;
    }
    console.log(qrid + " " + qrpermission);

    console.log('调用异步任务修改权限');
    //发送异步请求进行校验
    var xhr = getXhr();
    //设置请求信息
    xhr.open("get", "http://47.242.148.245:27905/manager/updatepermission?id=" + qrid + "&permission=" + qrpermission, true);
    //发送请求
    xhr.send();
    //监听readyState状态
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) { //响应处理完成
            //处理正确
            //获取响应数据
            var msg = xhr.responseText;
            if (msg === "success") {
                alert("修改成功");
                var newpermission = temp.parentNode.parentNode.cells[5];
                newpermission.innerHTML = qrpermission;
            }
            else {
                alert("修改失败");
            }
        }
    }
}

console.log('调用异步任务建表');
//发送异步请求进行校验
var xhr = getXhr();
//设置请求信息
xhr.open("get", "http://47.242.148.245:27905/manager/selectAllqr", true);
//发送请求
xhr.send();
//监听readyState状态
xhr.onreadystatechange = function () {
    if (xhr.readyState == 4 && xhr.status == 200) { //响应处理完成
        //处理正确
        //获取响应数据
        var msg = xhr.responseText;
        //显示提示信息
        msg = strToJson(msg);
        console.log(msg);

        var datas = msg;

        datas = datas["list"];

        var tbody = document.querySelector('tbody');
        for (var i = 0; i < datas.length; i++) { // 外面的for循环管行 tr
            // 创建 tr行
            var tr = document.createElement('tr');
            tbody.appendChild(tr);
            // 行里面创建单元格(跟数据有关系的7个单元格) td 单元格的数量取决于每个对象里面的属性个数  for循环遍历对象 datas[i]
            for (var j = 0; j < key.length; j++) {
                var td = document.createElement('td');
                if (datas[i][key[j]] === "null" || datas[i][key[j]] === null) {
                    td.innerHTML = "暂未分配";
                }
                else{
                    td.innerHTML = datas[i][key[j]];
                }
                console.log(key[j] + " " + datas[i][key[j]]);
                tr.appendChild(td);
            }
            // 创建 修改 2个字的单元格 
            var td = document.createElement('td');
            td.innerHTML = '<a href="javascript:;" onclick="modifypermission(this)">修改 </a>';
            tr.appendChild(td);
        }
    }
}