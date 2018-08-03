/**
 * Created by feiyu.pan on 2017/5/3.
 */
(function () {
  "use strict";

  angular.module("myApp").service('deliverySystemService', function (commonService, $httpParamSerializer, OUTBOUND_CONSTANT) {
    return{
      getPageData:function (startTime,endTime,showUnOut,isShowZeroShip,success,error) {
        commonService.ajaxMushiny({
            url:OUTBOUND_CONSTANT.getDeliverPageData+"?startTime="+startTime+"&endTime="+endTime+"&showUnOut="+showUnOut+"&isShowZeroShip="+isShowZeroShip,
            success:function (datas) {
                success&&success(datas.data);
            },
            error:function (datas) {
                error&&error(datas.data);
            }
        })
      },
      //获取所有sortCode信息
      getDeliverySystemData:function (searchOption,startTime,endTime,state,isShowZeroShip,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.getDeliverySystemData+"?startTime="+startTime+"&endTime="+endTime+"&isJustUnOut="+state+"&searchDetail="+searchOption+"&isShowZeroShip="+isShowZeroShip,
          success:function (datas) {
            success && success(datas.data)
          },
            error:function (datas) {
                error&&error(datas.data);
            }
        })
      },
      //获取所以未绑定门信息
      getDockDoor:function (success) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.getDockDoor,
          success:function (datas) {
            success && success(datas.data);
          }
        })
      },
      //绑定发货门
      bindDockDoor:function (doorNo,sortCode,deliverTime,success) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.bindDockDoor+"?doorNo="+doorNo+"&sortCode="+sortCode+"&deliverTime="+deliverTime,
          success:function (datas) {
            success && success(datas.data);
          }
        })
      },
      //重新装载
      reload:function (sortCode,success) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.reload+"?sortCode="+sortCode,
          success:function (datas) {
            success && success(datas.data);
          }
        })
      },
      //改变状态
      changeState:function (sortCode,deliverTime,state,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.changeState+"?sortCode="+sortCode+"&deliverTime="+deliverTime+"&state="+state,
          success:function (datas) {
            success && success(datas.data);
          },
            error:function (datas) {
                error&&error(datas.data);
            }
        })
      },
      //改变状态
      toinsertRequst:function (sortCode,deliverTime) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.toinsertRequst+"?sortCode="+sortCode+"&deliverTime="+deliverTime,
        })
      },
      //获取打印交接单信息
      getSortCodePrintInfo:function (sortCode,state,sendDate,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.getSortCodePrintInfo+"?sortCode="+sortCode+"&state="+state+"&sendDate="+sendDate,
          success:function (datas) {
            success && success(datas.data);
          },
            error:function (datas) {
                error&&error(datas.data);
            }
        })
      },
      //打印
      print:function (sortCode,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.print+"?sortCode="+sortCode,
          success:function (datas) {
            success && success(datas.data);
          },
            error:function (datas) {
                error&&error(datas.data);
            }
        })
      },
        rePrint:function (sortCode,goodsOutNo,success,error) {
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.rePrint+"?sortCode="+sortCode+"&goodsOutNo="+goodsOutNo,
                success:function (datas) {
                    success && success(datas.data);
                }
            })
        }
    }
  });
})();