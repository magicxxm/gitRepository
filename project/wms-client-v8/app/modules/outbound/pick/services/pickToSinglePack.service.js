/**
 * Created by xiong on 2017/4/14.
 */
(function () {
    "use strict";
    angular.module("myApp").service("pickToSinglePackService",function ($http,$window,commonService,OUTBOUND_CONSTANT) {
        return{
            //扫描工作站
            scanStation:function (stationName,succ,error) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.checkSingleStation+"?pickStationName="+ stationName,
                    success:function (datas) {
                        var info = datas.data.resultMessage;
                        var result = {};
                        if(info == 404){//工作站无效
                            result={msg:"none"};
                            succ(result);
                        }
                        if(info == 405){//工作站已被用户绑定
                            result={msg:"user",obj:datas.data.operatorName};
                            succ(result);
                        }
                        if(info == 406){//工作站绑定的有货筐
                            result={msg:"storage",obj:datas.data};
                            succ(result);
                        }
                        if(info == 1){//工作站扫描成功
                            result={msg:"success",obj:datas.data};
                            succ(result);
                        }
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            getOrderPosition:function (podName,sectionId,stationName,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.getSinglePickPack+'?podName='+podName+"&sectionId="+sectionId+"&stationName="+stationName,
                    success:function (datas) {
                        success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            confirmScanSKU:function (obj,success) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.confirmSinlePick,
                    data: obj,
                    method: "POST",
                    success:function (datas) {
                        success&&success(datas.data);
                    }
                })
            },
            //称重
            weigh:function (shipmentNo,weight,success,error) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.weighShipment+"?shipmentNo="+shipmentNo+"&weight="+weight,
                    success:function (datas) {
                        success && success(datas.data)
                    },error:function (datas) {
                        error &&  error(datas.data)
                    }
                })
            },
            //扫描订单
            scanSingleShipment:function (shipmentNo,success,error) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.scanSingleShipment+"?shipmentNo="+shipmentNo,
                    success:function (datas) {
                        success && success(datas.data)
                    },error:function (datas) {
                        error &&  error(datas.data)
                    }
                })
            },
            //包装完成
            packFinish:function (shipmentNo,boxName,type,success,error) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.singlePackFinish+"?shipmentNo="+shipmentNo+"&boxName="+boxName+"&type="+type,
                    success:function (datas) {
                        success && success(datas.data)
                    },error:function (datas) {
                        error &&  error(datas.data)
                    }
                })
            },
            //扫描订单
            checkPickedContainer:function (stationName,success,error) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.checkPickedContainer+"?stationName="+stationName,
                    success:function (datas) {
                        success && success(datas.data)
                    },error:function (datas) {
                        error &&  error(datas.data)
                    }
                })
            },
        }
    });
})();