function human2unix(year,month,day,hour,minute,second) {
    //功能：把日期转为unix时间戳
    var humanDate = formatDate(year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second);
    var unixTimeStamp=humanDate.getTime()/1000;
    return unixTimeStamp;
}

function unix2human(unixTimeStamp)   {
    //功能：把unix时间戳转成Y-m-d H:i:s格式的日期
    var now=new Date(unixTimeStamp*1000);
    var year=now.getFullYear();
    var month=now.getMonth()+1;
    month=month<10?"0"+month:month;
    var day=now.getDate();
    day=day<10?"0"+day:day;
    var hour=now.getHours();
    hour=hour<10?"0"+hour:hour;
    var minute=now.getMinutes();
    minute=minute<10?"0"+minute:minute;
    var second=now.getSeconds();
    second=second<10?"0"+second:second;
    return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
}

function formatDate(dateStr){
    var date = new Date(dateStr.replace(/-/g, '/'));
    return date;
}

function str2unix(str){
    var d = formatDate(str);
    var unixTimeStamp=d.getTime()/1000;
    return unixTimeStamp;
}

function parseQuery(q){
    var list = q.split('&');
    var result = {};
    for(var i=0; i<list.length; i++){
        var it = list[i];
        var pair = it.split('=');
        result[pair[0]] = pair[1];
    }

    return result;
}