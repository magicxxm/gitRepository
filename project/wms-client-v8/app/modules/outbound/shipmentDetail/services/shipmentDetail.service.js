/**
 * Created by prestonmax on 2017/7/26.
 */
(function () {
    'use strict';

    angular.module("myApp").service("shipmentDetailService",function (commonService, $httpParamSerializer,OUTBOUND_CONSTANT) {
        return{
            getShipmentInformation:function (searchOption,success) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.getShipmentInformation+"?shipmentid="+searchOption,
                    success:function (datas) {
                        success && success(datas.data);
                    }
                });
            }
        }
    })
})();
