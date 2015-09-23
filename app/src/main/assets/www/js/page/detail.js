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

        dataset.empty();
        $.each(data, function(index, it){
                var tpl = '<div class="detail_panel" myid='+it.id+' style="background-color:white;margin:10px;padding:10px;border:1px solid #999;border-radius:5px;">\
                                               <div style="font-size:20px;color:black;line-height:30px;height:30px;">'+it.title+'</div>\
                                               <div style="font-size:15px;color:#ccc;line-height:25px;height:25px;">'+it.date+'</div>\
                                               <div style="font-size:18px;color:red;line-height:25px;height:25px;">￥'+it.number+'元</div>\
                                               <div style="font-size:15px;color:#999;line-height:25px;">'+it.note+'</div>\
                                           </div>';

             dataset.append(tpl);
        });
        $.mobile.loading('hide');
    }

    loadingfn();
    if(typeof JavaScriptMethods != 'undefined'){
        JSM = JavaScriptMethods;
        var CurrentDateLabel = $('#CurrentDateLabel');
        CurrentDateLabel.text(JSM.getCurrentDate());
        loadDatafn();

        $('body').on('taphold', '.detail_panel', function(event){
            event.stopPropagation();
            oplist($(this).attr('myid'), function(){
                loadingfn();
                loadDatafn();
            });

        })
    }
});


