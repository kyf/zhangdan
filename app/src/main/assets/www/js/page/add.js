$(document).ready(function(){
    var JSM;
    var loadingfn = function(){
      $.mobile.loading('show', {
              text : '保存中...',
              theme : 'a',
              textVisible : true
          });
    }

    var title = $('#title'), datebt = $('#datebt'), paynumber = $('#paynumber'),
    note = $('#note'), submitbt = $('#submitbt');
    var CurrentDateLabel = $('#CurrentDateLabel');

    var d = new Date();
    var datestr = d.getFullYear() + '-' + (d.getMonth() > 8 ? (d.getMonth() + 1) : '0' + (d.getMonth() + 1)) + '-' + d.getDate() + 'T' + d.getHours() + ':' + d.getMinutes();

    datebt.get(0).value = datestr;
    title.keyup(function(){
        CurrentDateLabel.text(this.value);
    });



    if(typeof JavaScriptMethods != 'undefined'){
        JSM = JavaScriptMethods;

        submitbt.click(function(){
            var data = {
                title : title.val(),
                date : datebt.val().replace('T', ' ') + ':00',
                number : paynumber.val(),
                note : note.val()
            };

            if(data.title.trim() == ''){
                JSM.alert('请填写支出用途!');
                return;
            }

            if(data.date.trim() == ''){
                JSM.alert('请选择支出日期!');
                return;
            }

            if(data.number.trim() == ''){
                JSM.alert('请填写支出金额!');
                return;
            }

            loadingfn();
            JSM.addPay(data.title, str2unix(data.date) * 1000, data.number, data.note);
            history.go(-1);
        });

    }
});


