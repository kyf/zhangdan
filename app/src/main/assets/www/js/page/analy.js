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

        var categories = [], datalist = [];

        $.each(new Array(31), function(i){
            categories.push(i + 1);
            datalist.push(0);
        });

        $.each(data, function(index, it){
            var d = formatDate(it.date);
            var day = d.getDate();
            datalist[day] = datalist[day] + parseFloat(it.number);
            datalist[day] = Math.round(datalist[day] * 100) / 100;
        });

        dataset.highcharts({
                                   chart: {
                                       height:575,
                                       type: 'bar'
                                   },
                                   title: {
                                       text: '本月消费统计图'
                                   },
                                   xAxis: {
                                       categories: categories
                                   },
                                   yAxis: {
                                       min: 0,
                                       title: {
                                           text: '金额(元)',
                                           align: 'high'
                                       },
                                       labels: {
                                           overflow: 'justify'
                                       }
                                   },
                                   tooltip: {
                                       valueSuffix: ' 元'
                                   },
                                   plotOptions: {
                                       bar: {
                                           dataLabels: {
                                               enabled: true
                                           }
                                       }
                                   },
                                   legend: {
                                       layout: 'horizontal',
                                       align: 'right',
                                       verticalAlign: 'top',
                                       x: -40,
                                       y: 100,
                                       enabled:false,
                                       floating: true,
                                       borderWidth: 1,
                                       backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
                                       shadow: true
                                   },
                                   credits: {
                                       enabled: false
                                   },
                                   series: [{
                                       name: '消费额度',
                                       data: datalist
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


