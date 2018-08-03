(function () {
  "use strict";

  angular.module("myApp").service('campareBoxService', function (commonService, OUTBOUND_CONSTANT) {
    return{
        compareBoxType:function(data,success){
            commonService.ajaxMushiny({
                url: OUTBOUND_CONSTANT.getBoxDifference+ "?startDate="+data.startDate+"&endDate="+data.endDate+"&compare="+data.compare+"&seek="+data.seek,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        }
    }
  });
})();