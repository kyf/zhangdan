$(document).ready(function(){
    var JSM;
    var CurrentDateLabel = $('#CurrentDateLabel');
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
                                total += parseFloat(it.number);
                           });

                           total = Math.round(total * 100)/100;
                           PayLabel.text(total + "");
                           $.mobile.loading('hide');
                       }

    var changeDate = function(flag){
        loadingfn();
        JSM.changeDate(flag);
        showDatafn();
        CurrentDateLabel.text(JSM.getCurrentDate());
    }

    loadingfn();
    if(typeof JavaScriptMethods != 'undefined'){
        JSM = JavaScriptMethods;
        CurrentDateLabel.text(JSM.getCurrentDate());
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

        var NextBt = $('#NextBt'), PrevBt = $('#PrevBt');homebt = $('#home');
        NextBt.click(function(){
            changeDate(1);
        });
        PrevBt.click(function(){
            changeDate(0);
        });

        var content = $('#content');
        content.on("swipeleft",function(){
             changeDate(1);
        });

        content.on("swiperight",function(){
             changeDate(0);
        });

        var syncbt = $('#sync');
        syncbt.on("click", function(){
            JSM.loading("正在同步数据...");
            JSM.sync();
            homebt.click();
            JSM.dismiss();
        });
    }
});


