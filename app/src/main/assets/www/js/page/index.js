$(document).ready(function(){

    var loadingfn = function(){
      $.mobile.loading('show', {
              text : '加载中...',
              theme : 'a',
              textVisible : true
          });
    }

    var showDatafn = function(){
                           var dataset = $('#listview');
                           var PayLabel = $('#PayLabel')
                           var date = new Date();
                           var begin = human2unix(JSM.getCurrentYear(), JSM.getCurrentMonth(), 1, 0, 0, 0) * 1000;
                           var end = human2unix(JSM.getCurrentYear(), JSM.getCurrentMonth() + 1, 1, 0, 0, 0) * 1000;
                           var data = JSM.getDataFromNative(begin, end);

                           if(data == ""){
                                $.mobile.loading('hide');
                                JSM.alert("历史数据为空");
                                return;
                           }

                           data = JSON.parse(data);
                           var total = 0;
                           $.each(data, function(index, it){
                                total += parseInt(it.number, 10);
                           });
                           PayLabel.text(total);
                           $.mobile.loading('hide');
                       }

    loadingfn();
    if(typeof JavaScriptMethods != 'undefined'){
        var JSM = JavaScriptMethods;
        if(JSM.isFirst() && JSM.getSizeSMS() > 0){
            $.mobile.loading('hide');
            setTimeout(function(){
                confirmJQM('第一次使用可以导入短信数据，是否导入?', function(){
                    loadingfn();
                    JSM.importSMS();
                    showDatafn();
                });
            }, 300);
            JSM.updateIsFirst();
        }else{
            showDatafn();
        }
    }
});


