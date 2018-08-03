/**
 * Created by frank.zhou on 2017/01/17.
 */
(function() {
  "use strict";

  angular.module("myApp").service('zoneBatchService', function (commonService, OUTBOUND_CONSTANT) {
    return {
      // 取zoneBatch
      getZoneBatch: function(condition,success){
          commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.getZoneBatch+condition,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      //
      getExpectSendDelivery: function (clientId, cb) {
          commonService.ajaxMushiny({
              url: OUTBOUND_CONSTANT.getAllDeliveryTime,//+ "?clientId="+clientId,
              success: function (datas) {
                  cb && cb(datas.data);
              }
          });
      }
    };
  });
})();