/**
 * Created by frank.zhou on 2016/11/15.
 */
(function() {
  "use strict";

  angular.module("myApp").service('bayService', function (commonService, MASTER_CONSTANT) {
    return {
      // 取区域
      getZone: function(clientId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getZone+"?clientId="+ clientId,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 取area
      getArea: function(clientId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getArea+"?clientId="+ clientId,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // // 取热销度
      // getItemSellingDegree: function(clientId, cb){
      //   commonService.ajaxMushiny({
      //     url: MASTER_DATA_CONSTANT.getItemSellingDegree+"?clientId="+ clientId,
      //     success: function(datas){
      //       cb && cb(datas.data);
      //     }
      //   });
      // }
    };
  });
})();