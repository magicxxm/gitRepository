/**
 * Created by xiong on 2017/4/14.
 */
(function () {
    "use strict";
    angular.module("myApp").service("pickToPackService",function ($http,$window,commonService,OUTBOUND_CONSTANT) {
        return{
            //扫描工作站
            scanStation:function (stationName,succ,error) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.checkStation+"?pickStationName="+ stationName,
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
            //扫描PickPackWall
            scanPickPackCar:function (obj,success) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.findPickPackWall,
                    method:"POST",
                    data:obj,
                    success:function (datas) {
                        var info = datas.data.resultMessage;
                        if(info == 404){//pickpackwall无效
                            var result = {msg:"none"};
                            success(result);
                        }
                        if(info == 500){//不是系统绑定的pickpackwall
                            var result = {msg:"notsysytem"};
                            success(result);
                        }
                        if(info == 1){
                            var result={msg:"success",obj:datas.data.typePositionDTOs};/*datas.data.pickPackFieldTypeDTOS*/
                            success(result);
                        }
                    }
                })
            },
            //扫描残品筐
            scanCanPinBasket:function (storageName,stationName,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.checkStorageLocation+'?storageName='+storageName+'&stationName='+stationName+"&type=DAMAGE",
                    success:function (datas) {
                        success&&success(datas.data);
                    },error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //扫描无法扫描货筐
            scanUnScanSKUBasket:function (storageName,stationName,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.checkStorageLocation+'?storageName='+storageName+'&stationName='+stationName+"&type=INVENTORY",
                    success:function (datas) {
                        success&&success(datas.data);
                    },error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //扫描待调查货筐
            scanToReserachBasket:function (storageName,stationName,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.checkStorageLocation+'?storageName='+storageName+'&stationName='+stationName+"&type=PENDING",
                    success:function (datas) {
                        success&&success(datas.data);
                    },error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //绑定货筐
            bindStorage:function (storageName,stationName,type,success) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.bindStorageLocation + '?storageName=' + storageName + '&stationName=' + stationName + "&type="+type,
                    success: function (datas) {
                        success&&success(datas.data);
                    }
                })
            },
            //扫描pod
            scanPod:function (podName,success) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.findPod+'?podName='+podName,
                    success:function (datas) {
                        success&&success(datas.data);
                    }
                })
            },
            //获取orderposition
            getOrderPosition:function (podName,sectionId,stationName,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.getOrderPosition+'?podName='+podName+"&sectionId="+sectionId+"&stationName="+stationName,
                    success:function (datas) {
                        success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //扫描商品
            scanSKU:function (obj,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.scanSKU,
                    data:obj,
                    method:"POST",
                    success:function (datas) {
                        success&&success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                })

            },
            //确认扫描商品，完成拣货
            confirmScanSKU:function (obj,success) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.confirmScanSKU,
                    data: obj,
                    method: "POST",
                    success:function (datas) {
                        success&&success(datas.data);
                    }
                })
            },
            //扫描商品序列号
            scanSeriesNo:function (pickId,serialNo,serialNoType,operation,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.scanSerialNo+"?pickId="+pickId+"&serialNo="+serialNo+"&serialNoType="+serialNoType+"&operation="+operation,
                    success:function (datas) {
                        success&&success(datas.data);
                    },error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //扫描残损商品
            checkDamagedItem:function (obj,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.checkDamagedItem,
                    data:obj,
                    method:"POST",
                    success:function (datas) {
                        success&&success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //检查订单是否不能完成
            checkShipment:function (logicStationId,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.checkShipment+"?stationId="+logicStationId,
                    success:function (datas) {
                        success&&success(datas.data);
                    },error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //问题商品处理
            handleProblemItem:function (obj,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.scanProItem,
                    data:obj,
                    method:"POST",
                    success:function (datas) {
                        success&&success(datas.data);
                    },error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //获取站台当前3个筐的信息
            getBakInfo:function (data,success) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.getAllstorageInfo + "?stationName="+data,
                    success: function (datas) {
                        success(datas.data);
                    }
                });
             },
            //扫描已满货筐
            scanfullBasket:function (storagename,stationname,success) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.fullstorage + "?storageName="+storagename+"&pickStationName="+stationname,
                    success: function (datas) {
                        success(datas.data);
                    }
                });
            },
            //扫描新的货筐
            scanNewBasket:function (storagename,storageType,stationName,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.checkStorageLocation+'?storageName='+storagename+'&stationName='+stationName+"&type="+storageType,
                    success:function (datas) {
                        success&&success(datas.data);
                    },error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //扫描货位
            scanHuoWei:function (binName,pickId,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.scanBin + "?binName="+binName+"&pickId="+pickId,
                    success: function (datas) {
                        success&&success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                });
            },
            //逐一扫描货位的商品
            scanEachSKU:function (obj,success,error) {
                commonService.ajaxMushiny({
                    url: OUTBOUND_CONSTANT.scanSKU,
                    data:obj,
                    method:"POST",
                    success:function (datas) {
                        success&&success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //已扫描完所有商品
            haveScanedAllSKU:function (data,operation,success) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.haveScanedAll+"?pickId="+data+"&operation="+operation,
                    success:function (datas) {
                        success&&success(datas.data);
                    }
                })
            },
            //扫描工作站位置标签获取灯的信息
            scanDigitalName:function (positionNo,success,error) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.scanPosition+"?positionNo="+positionNo,
                    success:function (datas) {
                        success&&success(datas.data);
                    },
                   error:function (datas) {
                       error&&error(datas.data);
                   }
                })
            },
            //扫描灯的标签
            scanpickPackCellDigital:function (cellName,success,error) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.scanpickPackCellDigital+"?cellName="+cellName,
                    success:function (datas) {
                        success&&success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //结满工作站所有货筐
            fullAllStorage:function (stationName,success) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.fullAllStorage+"?stationName="+stationName,
                })
            },

            //问题处理接口
            callProblem:function (data,success) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.obproblem,
                    data:data,
                    method:"POST",
                    success:function (datas) {
                        success&&success(datas.data);
                    }
                })
            },
            //根据暗灯name查询暗灯对象
            getAnDeng:function (name,success) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.obproblem,
                    success:function (datas) {
                        success&&success(datas.data);
                    }
                })
            },
            //停止工作
            stopWorking:function (stationName,success,error) {
                commonService.ajaxMushiny({
                    url:OUTBOUND_CONSTANT.stopWorking+"?stationName="+stationName,
                    success:function (datas) {
                        success&&success(datas.data);
                    },
                    error:function (datas) {
                        error&&error(datas.data);
                    }
                })
            },
            //打开电子标签
            openLight:function (data) {
                $http({
                    url:"http://192.168.1.92:9090/light/onOff?labelId="+data+"&onOffFlag=true",
                })
            },
            //控制颜色打开灯
            openLightWithColor:function (data,color) {
                $http({
                    url:"http://192.168.1.88:9090/light/onOffColor?labelId="+data+"&onOffFlag=true&color="+color,
                })
            },
            //改变灯的颜色
            changeLightColor:function (labelId,color) {
                $http({
                    url:"http://192.168.1.88:9090/light/changeTagColor?labelId="+labelId+"&color="+color,
                })
            },
        }
    });
})();