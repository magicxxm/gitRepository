/**
 * Created by PC-2 on 2017/5/3.
 */
(function () {
  "use strict";

  angular.module("myApp").service('cartQueryShipmentService', function (commonService, $httpParamSerializer, OUTBOUND_CONSTANT) {
    return{
      searchCartQueryShipmentData:function (searchOption,success) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.searchCartQueryShipmentData+"?searchDetail="+searchOption,
          success:function (datas) {
            success && success(datas.data)
          }
        })
      },
        getCartQueryShipmentData:function (cartName,success) {
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.getCartQueryShipmentData+"?ciperNo="+cartName,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        }
    }
  });
})();