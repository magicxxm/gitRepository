/**
 * Created by zhihan.dong on 2017/04/17.
 * Updated by zhihan.dong on 2017/04/18.
 */
(function () {
  "use strict";
  angular.module("myApp").service('pickQueryService', function (commonService, REPORT_CONSTANT) {
    return {
      querypickTypeArea: function (cb, ppName, zoneName) {
  
                var getString = '?';
        if(ppName)getString = getString + "ppName=" + ppName;
        if(zoneName)getString = getString + "&zoneName=" + zoneName;
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.pickArea +getString,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      querypickTypeExsd: function (cb, ppName, deliveryDate) {    
        var getString = '?';
        if(ppName)getString = getString + "ppName=" + ppName;
        if(deliveryDate && deliveryDate!="null")getString = getString + "&deliveryDate=" + deliveryDate;
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.pickExsd + getString,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
    };
  });
})();