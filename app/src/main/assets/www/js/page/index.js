$(document).ready(function(){

    $.mobile.loading('show', {
            text : 'loading ...',
            theme : 'a',
            textVisible : true
        });

    if(typeof JavaScriptMethods != 'undefined'){
        var JSM = JavaScriptMethods;
        if(JSM.isFirst()){
            $.mobile.loading('hide');
            setTimeout(function(){
                confirmJQM('第一次使用可以导入短信数据，是否导入?', function(){
                    JSM.importSMS();
                });
            }, 100);

        }else{
             var dataset = $('#listview');
             var data = JSM.getDataFromNative();
             if(data == ""){
                 JSM.alert("历史数据为空");
                 return;
             }
             data = JSON.parse(data);

             $.each(data, function(index, it){
                 dataset.append('<li>[' + it.number + "] : " + it.body + '</li>');
             });
             dataset.listview('refresh');
             $.mobile.loading('hide');
        }
    }
});


