function calendar(y) {
    var w = new Date(y, 0).getDay();
    var today = new Date();

    var html = '';

    for (m = 1; m <= 12; ++m) {
        html += '<table>';
        html += '<tr class="title"><th colspan="7">' + y + ' 年 ' + m + ' 月</th></tr>';
        html += '<tr><td style="color:red">日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td style="color:red">六</td></tr>';

        var max = new Date(y, m, 0).getDate();

        html += '<tr>';
        for (d = 1; d <= max; ++d) {
            if (w && d == 1) {
                html += '<td colspan="' + w + '"> </td>';
            }
            if(m == today.getMonth() && d == today.getDate()){
                html += '<td style="border-radius: 50%;height: 20px; width: 20px;display: inline-block;background: #238ff9;margin-top:5px">' + 
                '<span style="display: block;color: #FFFFFF;height: 20px;line-height: 20px;text-align: center">' + d + '</span> </td>';
            }
            else{
                html += '<td>' + d + '</td>';
            }
            if (w == 6 && d != max) {
                html += '</tr><tr>';
            } else if (d == max) {
                html += '</tr>';
            }
            w = (w + 1 > 6) ? 0 : w + 1;
        }
        html += '</table>';
    }
    return html;
}
