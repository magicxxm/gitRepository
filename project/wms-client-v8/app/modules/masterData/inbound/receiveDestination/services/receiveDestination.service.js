/**
 * Created by frank.zhou on 2017/04/25.
 */
(function() {
  "use strict";

  angular.module("myApp").service('receiveDestinationService', function (commonService, MASTER_CONSTANT) {
    return {
      // 取area
      getArea: function(clientId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getArea+"?clientId="+ clientId,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();