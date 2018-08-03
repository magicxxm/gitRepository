/**
 * Created by frank.zhou on 2017/01/10.
 */
(function() {
  "use strict";

  angular.module("myApp").service('customerOrderService', function (commonService, MASTER_CONSTANT,OUTBOUND_CONSTANT) {
    return {
      /*getExpectSendDelivery: function (clientId, cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getExpectSendDelivery+ "?clientId=" + clientId,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },*/
     getDeliveryTime:function (sortCodeId,cb) {
         commonService.ajaxMushiny({
             url: OUTBOUND_CONSTANT.getDeliveryTimeBySortCode+ "?sortCodeId=" + sortCodeId,
             success: function (datas) {
                 cb && cb(datas.data);
             }
         });
     },
     getOrderStrategy: function (clientId, cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getOrderStrategy+ "?clientId=" + clientId,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      getItemData: function (clientId, cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getItemData+ "?clientId=" + clientId,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();