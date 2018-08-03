/**
 * Created by thoma.bian on 2017/5/10.
 */

(function() {
  "use strict";

  angular.module("myApp").service('problemOutboundService', function (commonService, $httpParamSerializer, PROBLEM_OUTBOUND) {
    return {
        // 扫描工作站
        getOutboundProblemStation: function (name, success, error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getOutboundProblemStation + "?name=" + name,
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
        // 扫描问题车
        getOutboundProblemHandingCar: function (name, success, error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getOutboundProblemHandingCar + "?name=" + name,
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
        // 扫描shipment
        getShipmentDealProblem: function (data, success,error) {
            var params = "";
            data.obpStationId != "" && (params += "?obpStationId=" + data.obpStationId);
            data.obpWallId != "" && (params += "&obpWallId=" + data.obpWallId);
            params += "&shipmentNo=" + data.shipmentNo;
            data.state != "" && (params += "&state=" + data.state);
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getShipmentDealProblem + params,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error&&error(datas.data);
                }
            });
        },
        // 退出
        exitShipment: function (obpStationId, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.exitShipment + "?obpStationId=" + obpStationId,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 送去包装
        gotoPacking: function (shipmentNo, obpStationId, cellName, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.gotoPacking + "?shipmentNo=" + shipmentNo + "&obpStationId=" + obpStationId + "&cellName=" + cellName,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 测试送去包装
        gotoPack: function (data, success, error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.gotoPack,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
        //全部送去包装
        batchToPacking: function (data,success) {
            commonService.ajaxMushiny({
               // url: PROBLEM_OUTBOUND.batchToPacking + "?dtos=" + "test"+"&obpStationId="+obpStationId,
                url: PROBLEM_OUTBOUND.batchToPacking ,
                params:data,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 问题处理格以放置商品
        problemCellPlaceGoods: function (wallId, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.problemCellPlaceGoods + "?wallId=" + wallId,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        //拣货回来扫描商品
        problemCellStorageLocation: function (wallId, podNo,location, success,error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.problemCellStorageLocation + "?wallId=" + wallId + "&podNo=" + podNo+"&location="+location,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error && error(datas.data);
                }
            });
        },
        // 绑定格子
        bindCell: function (shipmentNo, cellName, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.bindCell + "?shipmentNo=" + shipmentNo + "&cellName=" + cellName,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 获取shipmentNo
        getShipmentNoByCellName: function (stationId, wallId, cellName, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getShipmentNoByCellName + "?stationId=" + stationId + "&wallId=" + wallId + "&cellName=" + cellName,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 订单详情
        getOrderDetails: function (data, success) {
            var params = "";
            data.obpStationId != "" && (params += "?obpStationId=" + data.obpStationId);
            data.obpWallId != "" && (params += "&obpWallId=" + data.obpWallId);
            params += "&shipmentNo=" + data.shipmentNo;
            data.state != "" && (params += "&state=" + data.state);
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getOrderDetails + params,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 订单商品信息
        getOrderGoodsDetails: function (shipmentNo, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getOrderGoodsDetails + "?shipmentNo=" + shipmentNo,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        //打印订单
        printOrder: function (shipmentNo, solveKey, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.printOrder + "?shipmentNo=" + shipmentNo + "&solveKey=" + solveKey,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 保存商品记录(SN)
        saveGoodsBySN: function (data, success, error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.saveGoodsBySN + "?cellName=" + data.cellName + "&shipmentNo=" + data.shipmentNo + "&itemNo=" + data.itemNo + "&serialNo=" + data.serialNo,
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
        // 保存商品记录(非SN)
        saveGoodsInformation: function (data, success, error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.saveGoodsInformation + "?cellName=" + data.cellName + "&shipmentNo=" + data.shipmentNo + "&itemNo=" + data.itemNo,
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
        // 释放问题格
        releaseQuestionCell: function (data, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.releaseQuestionCell + "?" + $httpParamSerializer(data),
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 美的接口释放问题格
        relieveQuestionCell: function (data, success, error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.relieveQuestionCell,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
        // 商品转为正品
        saveGoodsToGenuine: function (data, success,error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.saveGoodsToGenuine,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error && error(datas.data);
            }
            });
        },
        // 确认残损
        damageConfirm: function (data, success,error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.damageConfirm,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error && error(datas.data);
                }
            });
        },
        // 放置商品到残品车
        damageGoods: function (data, success, error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.damageGoods + "?" + $httpParamSerializer(data),
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
        // 生成新拣货任务
        generateNewPickingTasks: function (data, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.generateNewPickingTasks,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },

        // 取分配货位
        getAssignedLocation: function (itemDataNo, sectionId, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getAssignedLocation + "?itemDataNo=" + itemDataNo + "&sectionId=" + sectionId,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 分配货位取货
        assignedPicking: function (data, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.assignedPicking,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        //根据货位获得pod信息
        callPodOutboundProblem: function (names, obpStationId, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.callPodOutboundProblem + "?names=" + names + "&obpStationId=" + obpStationId,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },

        refreshNextPod: function (sectionId, workStationId, success, error) {
            commonService.generalNet({
                url: PROBLEM_OUTBOUND.refreshNewPod + "?sectionId=" + sectionId + "&workStationId=" + workStationId,
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },

        getPodPosition:function(cellNames,workStationId,success){
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getPodPosition + "?cellNames="+cellNames+"&workStationId="+workStationId,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },

        // 拆单发货
        demolitionShip: function (data, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.demolitionShip + "?" + $httpParamSerializer(data),
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 删除订单
        deleteOrderSuccess: function (data, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.deleteOrderSuccess,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },

        // 删除订单后逐一扫描商品
        deleteOrderScanGoods: function (data, success,error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.deleteOrderScanGoods,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error && error(datas.data);
                }
            });
        },
        // 补打条码
        markUpBarcode: function (data, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.markUpBarcode,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        // 序列号无法扫描，转为待调查状态
        getInvestigated: function (data, success, error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.getInvestigated,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                }, error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
        // 客户删单
        moveShelvesLicensePlate: function (data, success,error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.moveShelvesLicensePlate,
                method: "POST",
                data: data,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error && error(datas.data);
                }
            });
        },
        // 进行问题处理
        dealWithProblem: function (data, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.dealWithProblem + "?obpStationId=" + data.obpStationId + "&obpWallId=" + data.obpWallId + "&shipmentNo=" + data.shipmentNo,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        //请扫描拣货车牌
        scanPickingLicensePlate: function (containerName, obpWallId, success,error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.scanPickingLicensePlate + "?containerName=" + containerName + "&obpWallId=" + obpWallId,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error && error(datas.data);
                }
            });
        },
        // 请检查并扫描商品
        checkScanGoods: function (containerName, obpWallId, itemNo, success,error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.checkScanGoods + "?containerName=" + containerName + "&obpWallId=" + obpWallId + "&itemNo=" + itemNo,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error && error(datas.data);
                }
            });
        },
        // 强制删单GRID
        forcedDeleteGrid: function (data, success, error) {
            if (data.startDate == undefined || data.startDate == "") {
                data.startDate = "";
            } else {
                data.startDate = data.startDate + "T00:00:00";
            }
            if (data.endDate == undefined || data.endDate == "") {
                data.endDate = "";
            } else {
                data.endDate = data.endDate + "T23:59:59";
            }
            if (data.shipmentNo == undefined) {
                data.shipmentNo = "";
            }
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.forcedDeleteGrid + "?startDate=" + data.startDate + "&endDate=" + data.endDate + "&shipmentNo=" + data.shipmentNo + "&state=" + data.state + "&obpStationId=" + data.obpStationId + "&obpWallId=" + data.obpWallId,
                success: function (datas) {
                    success && success(datas.data);
                },
                error:function(datas){
                    error && error(datas.data);
                }
            });
        },
        // 强制删单删除订单
        forcedDeleteOrder: function (data, success) {
            commonService.ajaxMushiny({
                method: 'PUT',
                url: PROBLEM_OUTBOUND.forcedDeleteOrder + "?shipmentNo=" + data.shipmentNo + "&deleteReason=" + data.deleteReason,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        //分配货位后点击货位分配商品
        scanGoodsStorageLocation: function (name, solveId, obpLocationId, obpWallId, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.scanGoodsStorageLocation + "?name=" + name + "&solveId=" + solveId + "&obpLocationId=" + obpLocationId + "&obpWallId=" + obpWallId,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        putForceDeleteGoodsToContainer: function (data, success,error) {
            if (data.serialNo == undefined) {
                data.serialNo = "";
            }
            commonService.ajaxMushiny({
                method: "POST",
                data: data,
                url: PROBLEM_OUTBOUND.putForceDeleteGoodsToContainer,
                success: function (datas) {
                    success && success(datas.data);
                },
                error:function(datas){
                    error&&error(datas.data);
                }
            });
        },
        verifySingOutObpStation: function (obpStationId, success) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.verifySingOutObpStation + "?obpStationId=" + obpStationId,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
        scanPickGoods:function (location, pickingGoods,success,error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.scanPickingGoods + "?location=" + location+"&itemNo="+pickingGoods,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error && error(datas.data);
                }
            });
        },
        //扫描拣货商品的序列号
        scanPickgGoodsSn:function (serialNo, location,itemNo, success,error) {
            commonService.ajaxMushiny({
                url: PROBLEM_OUTBOUND.scanPickgGoodsSerialNo + "?serialNo=" + serialNo + "&location=" + location+"&itemNo="+itemNo,
                success: function (datas) {
                    success && success(datas.data);
                },error:function(datas){
                    error && error(datas.data);
                }
            });
        },
      //生成拣货任务扫描拣货商品的序列号
      scanHotPickSn:function (pickingLicensePlate, obpWallId,checkGood,serialNumber, success,error) {
          commonService.ajaxMushiny({
              url: PROBLEM_OUTBOUND.hotPickGoodsSerialNo + "?containerName=" + pickingLicensePlate + "&obpWallId=" + obpWallId+"&itemNo="+checkGood+"&serialNo="+serialNumber,
              success: function (datas) {
                  success && success(datas.data);
              },error:function(datas){
                  error && error(datas.data);
              }
          });
      },
     printSku:function(data,success){
         commonService.generalNet({
             url: "http://192.168.1.115:12889/printService/print",
             data: data,
             method: "POST",
             success: function (datas) {
                 success && success(datas.data);
             }
         });
     }
    }
  });
})();