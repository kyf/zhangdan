$(document).ready(function(){
    var JSM;
    var loadingfn = function(){
      $.mobile.loading('show', {
              text : '加载中...',
              theme : 'a',
              textVisible : true
          });
    }

    var loadDatafn = function(){
        var dataset = $('#listview');
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

        $.each(data, function(index, it){

        });

        dataset.highcharts({                   //图表展示容器，与div的id保持一致
                chart: {
                    type: 'column'                         //指定图表的类型，默认是折线图（line）
                },
                title: {
                    text: '本月消费统计'      //指定图表标题
                },
                xAxis: {
                    categories: [1, 2, 3, 4, 5, 6, 7]   //指定x轴分组
                },
                yAxis: {
                    title: {
                        text: '消费金额'                  //指定y轴的标题
                    }
                },
                series: [{
                    data: [500, 270, 316, 270, 590, 109, 2000]
                }]
            });

        $.mobile.loading('hide');
    }

    loadingfn();
    if(typeof JavaScriptMethods != 'undefined'){
        JSM = JavaScriptMethods;
        var CurrentDateLabel = $('#CurrentDateLabel');
        CurrentDateLabel.text(JSM.getCurrentDate());
        loadDatafn();
    }
});


