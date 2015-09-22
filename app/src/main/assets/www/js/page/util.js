function human2unix(year,month,day,hour,minute,second) {
    //功能：把日期转为unix时间戳
    var humanDate = new Date(Date.UTC(year, (month-1), day, hour, minute, second));
    var unixTimeStamp=humanDate.getTime()/1000 - 8*60*60;
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
    var list = dateStr.split(' ');
    var d = list[0];
    var t = list[1];
    var ds = d.split('-');
    var ts = t.split(':');
    var date = new Date(Date.UTC(ds[0], (ds[1]-1), ds[2], ts[0], ts[1], ts[2]));
    return date;
}

function str2unix(str){
    var d = formatDate(str);
    var unixTimeStamp=d.getTime()/1000 - 8*60*60;
    return unixTimeStamp;
}