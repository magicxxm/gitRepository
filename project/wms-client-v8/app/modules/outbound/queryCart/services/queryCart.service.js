/**
 * Created by PC-2 on 2017/5/3.
 */
(function () {
  'use strict';

  angular.module("myApp").service("queryCartService",function (commonService, $httpParamSerializer, OUTBOUND_CONSTANT) {
    return{
      searchQueryCartData:function (searchOption,success) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.searchQueryCartData+"?searchDetail="+searchOption,
          success:function (datas) {
            success && success(datas.data)
          }
        })
      },
        getQueryCartData:function (sortCode,deliverTime,state,shippingDate,cartState,startTime,endTime,success) {
          console.log("endTime--->"+startTime+"/endTime-->"+endTime);
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.getQueryCartData+"?sortCode="+sortCode+"&deliverTime="+deliverTime+"&state="+state+"&shipDate="+shippingDate+"&cartState="+cartState+"&start="+startTime+"&end="+endTime,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        }
    }
  })
})();
