function confirmJQM(text, callback) {
     var popupDialogId = 'popupDialog';
     $('<div data-role="popup" id="' + popupDialogId + '" data-confirmed="no" data-transition="pop" data-overlay-theme="b" data-theme="b" data-dismissible="false" style="max-width:500px;"> \
                         <div data-role="header" data-theme="b">\
                             <h1 style="margin:0 3%;text-align:left;">提示框</h1>\
                         </div>\
                         <div role="main" class="ui-content">\
                             <h5 class="ui-title">' + text + '</h5>\
                         </div>\
                         <div data-role="content" style="text-align:center;">\
                             <a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b optionConfirm" data-rel="back">确 定</a>\
                             <a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b optionCancel" data-rel="back" data-transition="flow">取 消</a>\
                         </div>\
                     </div>')
         .appendTo($.mobile.pageContainer);
     var popupDialogObj = $('#' + popupDialogId);
     popupDialogObj.trigger('create');
     popupDialogObj.popup({
         afterclose: function (event, ui) {
             popupDialogObj.find(".optionConfirm").first().off('click');
             var isConfirmed = popupDialogObj.attr('data-confirmed') === 'yes' ? true : false;
             $(event.target).remove();
             if (isConfirmed && callback) {
                 callback();
             }
         }
     });
     popupDialogObj.popup('open');
     popupDialogObj.find(".optionConfirm").first().on('click', function () {
         popupDialogObj.attr('data-confirmed', 'yes');
     });

}