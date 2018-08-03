/**
 * Created by PC-2 on 2017/5/4.
 */
(function () {
  'use strict';

    angular.module("myApp").service("deliveryShipmentsDetailService",function (commonService,$httpParamSerializer,OUTBOUND_CONSTANT) {
      return{
        //获取所以信息
        getDeliveryShipmentsDetailData:function (sortCode,startTime,endTime,exSD,sendDate,State,isJustUnOut,shipmentState,success) {
          commonService.ajaxMushiny({
            url:OUTBOUND_CONSTANT.getDeliveryShipmentsDetailData+"?sortCode="+sortCode+"&startTime="+startTime+"&endTime="+endTime+"&isUnOut="+isJustUnOut+"&state="+State+"&exSD="+exSD+"&sendDate="+sendDate+"&shipmentState="+shipmentState,
            success:function (datas) {
              success && success(datas.data)
            }
          })
        },
          searchDeliveryShipmentsDetailData:function (startTime,endTime,exsd,isJustUnOut,searchDetail,sortCode,success) {
              commonService.ajaxMushiny({
                  url:OUTBOUND_CONSTANT.searchDeliveryShipmentsDetailData+"?sortCode="+sortCode+"&startTime="+startTime+"&endTime="+endTime+"&isJustUnOut="+isJustUnOut+"&exSD="+exsd+"&searchDetail="+searchDetail,
                  success:function (datas) {
                      success && success(datas.data)
                  }
              })
          },
          getExsds:function (success) {
              commonService.ajaxMushiny({
                  url:OUTBOUND_CONSTANT.getAllExSd,
                  success:function (datas) {
                      success&&success(datas.data);
                  }
              });
          }
      }
  })
})();