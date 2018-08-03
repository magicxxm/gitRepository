/**
 * Created by frank.zhou on 2016/11/18.
 */
(function() {
  "use strict";

  angular.module("myApp").service('receivingService', function (commonService, INBOUND_CONSTANT) {
    return {
        /**
         * 获取在途的pod
         * @param workStationId
         * @param sectionId
         * @param success
         */
        getPodInPath:function (workStationId,sectionId,success) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.getPodInPath+"?workStationId="+workStationId+"&sectionId="+sectionId,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },
        releasePod:function (sectionId,workStationId,podName,force,logicStationId,callback,error) {
            commonService.generalNet({
                url:INBOUND_CONSTANT.releasePod+"?sectionId="+sectionId+"&workStationId="+workStationId+"&podName="+podName+"&force=false"+"&logicStationId="+logicStationId,
                success:function (datas) {
                    callback&&callback(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        refreshPod:function (sectionId,workStationId,callback) {
            commonService.generalNet({
                url:INBOUND_CONSTANT.refreshNewPod+"?sectionId="+sectionId+"&workStationId="+workStationId,
                success:function (datas) {
                    callback&&callback(datas.data);
                }
            });
        },
        //收货呼叫pod
        receiveCallPod:function (stationId,success) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.receiveCallPod+"?stationId="+stationId,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },
        //上架呼叫pod
        stowCallPod:function (stationId,success) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.stowCallPod+"?stationId="+stationId,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },
        //收货工作站停止或恢复叫pod
        receivestoporcallpod:function (stationName,flag,success) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.receivestoporcallpod+"?stationName="+stationName+"&flag="+flag,
                method: "POST",
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },
        //上架工作站停止或恢复叫pod
        stowstoporcallpod:function (stationName,flag,success) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.stowstoporcallpod+"?stationName="+stationName+"&flag="+flag,
                method: "POST",
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },
      // 扫描工作站
      scanStation: function(name,type, success, error){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.scanStation+ "?name="+ name+"&type="+type,
          success: function(datas){
            success && success(datas.data);
          },
          error:function (datas) {
              error && error(datas.data);
          }
        });
      },
        // 扫描工作站
        findgridStorageInfo: function(name,type, success, error){
            commonService.ajaxMushiny({
                url: INBOUND_CONSTANT.findgridStorageInfo+ "?name="+ name+"&type="+type,
                success: function(datas){
                    success && success(datas.data);
                },
                error:function (datas) {
                    error && error(datas.data);
                }
            });
        },
      autoFullStorageLocations: function(stationid, success, error){
          commonService.ajaxMushiny({
              url: INBOUND_CONSTANT.deleteallprocess+ "?StationId="+ stationid,
              method: "DELETE",
              success: function(datas){
                  success();
              },
              error:function (datas) {
                  error && error(datas.data);
              }
          });
      },
      //获取所有货位类型
      getStorageLocationTypes:function(success){
          commonService.ajaxMushiny({
              url:INBOUND_CONSTANT.getlocationtypes,
              success:function (datas) {
                  success&&success(datas.data);
              }
          });
      },
        //绑定receiverStation货位
        bindStorageLocationTypes:function (arraydata,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.bindlocationtypes,
                method: "POST",
                data:arraydata,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //scanstowStation
        scanStowStation:function (stowStationName,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scanstowstation+"?stationName="+stowStationName,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },

        findstowgridStorageInfo:function (stowStationName,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.findstowgridStorageInfo+"?stationName="+stowStationName,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        autoFullStowLocation: function(stationid, success, error){
            commonService.ajaxMushiny({
                url: INBOUND_CONSTANT.autofullstowlocation+ "?stationName="+ stationid,
                method: "DELETE",
                success: function(datas){
                    success();
                },
                error:function (datas) {
                    error && error(datas.data);
                }
            });
        },
        //scanstowContainer
        scanStowContainer:function (slid,scanType,stationName,positionIndex,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scanstowcontainer+"?stationName="+stationName+"&slid="+slid+"&scanType="+scanType+"&positionIndex="+positionIndex,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        unbindStowContainer:function (slid,stationName,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.unbindstowcontainer+"?slid="+slid+"&stationName="+stationName,
                method:"DELETE",
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        bindStowContainer:function (slid,stationName,type,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.bindstowcontainer+"?slid="+slid+"&stationName="+stationName+"&type="+type,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },

        scanIsDN:function (slid,success) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scanisDN+"?slid="+slid,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },

        scanIsCiper:function (slid,success) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scanisciper+"?slid="+slid,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },

        //scanstowCiper
        scanStowCiper:function (slid,scanType,stationName,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scanstowciper+"?stationName="+stationName+"&slid="+slid+"&scanType="+scanType,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //绑定StowStation货位
        bindStorageLocationTypesToStow:function (arraydata,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.bindlocationtypestostow,
                method: "POST",
                data:arraydata,
                success:function (datas) {
                    success&&success(datas);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //报告多货
        reportGoodsMore:function (moredata,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.getStowingOverage,
                method:"POST",
                data:moredata,
                success:function (datas) {
                    success&&success(datas.data);
                },error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //检查多货
        checkGoodsMore:function (locationName,itemid,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.checkIsMore+"?locationName="+locationName+"&itemDataId="+itemid,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //报告少货
        reportGoodsLess:function (storageid,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.getStowingOverage+"?storageLocationName="+storageid,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //扫描pod并获取pod信息
        getPodInfo:function (podid,storagetype,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.getpodinfo+"?podid="+podid+"&type="+storagetype,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //扫描货筐
        scanStorageLocation:function (slid,scantype,stationname,destinationId,positionIndex,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scanstoragelocation+"?slid="+slid+"&scanType="+scantype+"&stationName="+stationname+"&destinationId="+destinationId+"&positionIndex="+positionIndex,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //绑定货筐
        bindStorageLocation:function (slid,scantype,stationname,destinationId,positionIndex,success) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.bindstoragelocation+"?slid="+slid+"&scanType="+scantype+"&stationName="+stationname+"&destinationId="+destinationId+"&positionIndex="+positionIndex,
                success:function (datas) {
                    success&&success(datas.data);
                }
            })
        },
        //扫描DN
        scanDN:function (dn,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scandn+"?DN="+dn,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //扫描商品
        scanItem:function (requestId,itemId,stationName,podId,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scanitem+"?requestid="+requestId+"&itemid="+itemId+"&stationName="+stationName+"&podId="+podId,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //扫描商品
        scanToteItem:function (requestId,itemId,stationName,type,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scantoteitem+"?requestid="+requestId+"&itemid="+itemId+"&stationName="+stationName+"&type="+type,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //扫描商品
        scanStowItem:function (itemId,ciper,podId,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scanstowitem+"?itemid="+itemId+"&ciperid="+ciper+"&podId="+podId,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //检查SN
        checkSN:function (itemid,sn,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.checksn+"?itemId="+itemid+"&SN="+sn,
                success:function () {
                    success();
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //检查有效期
        checkAvaTime:function (itemId,avatime,podId,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.checkavatime+"?itemId="+itemId+"&avaTime="+avatime+"&podId="+podId,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        checkToteAvatime:function (itemId,useAfter,success,error) {
          commonService.ajaxMushiny({
              url:INBOUND_CONSTANT.checkToteAvatime+"?itemId="+itemId+"&avaTime="+useAfter,
              success:function (datas) {
                  success&&success(datas.data);
              },
              error:function (datas) {
                  error&&error(datas.data);
              }
          });
        },
        //检查有效期
        checkNotGenuineLocation:function (storageid,stationName,storageType,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scannotgenuine+"?storageId="+storageid+"&stationName="+stationName+"&storageType="+storageType,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //检查上架货位
        checkBin:function (storageid,itemId,useAfter,podid,stationName,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.checkbin+"?storageid="+storageid+"&itemid="+itemId+"&podid="+podid+"&useAfter="+useAfter+"&stationName="+stationName,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //满筐
        fullStorage:function (storageid,stationName,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.fullstoragelocation+"?stationId="+stationName+"&storageLocationId="+storageid,
                method: "DELETE",
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //检查stow上架货位
        checkStowBin:function (storageid,itemId,podid,ciperid,stationName,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.checkstowbin+"?storageid="+storageid+"&itemid="+itemId+"&podid="+podid+"&ciperid="+ciperid+"&stationName="+stationName,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //检查stow上架货筐
        checkStowContainer:function (storageid,itemId,ciperid,stationName,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.checkstowcontainer+"?storageid="+storageid+"&itemid="+itemId+"&ciperid="+ciperid+"&stationName="+stationName,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //检查上架货筐
        checkContainer:function (storageid,itemId,useAfter,sn,stationName,type,positionIndex,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.checkcontainer+"?storageid="+storageid+"&itemid="+itemId+"&stationName="+stationName+"&useAfter="+useAfter+"&sn="+sn+"&type="+type+"&finishPositionIndex="+positionIndex,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        checkNotGenuisContainer:function (storageid,stationName,type,useAfter,itemid,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.scannotgenuine+"?storageid="+storageid+"&stationName="+stationName+"&storageType="+type+"&useAfter="+useAfter+"&itemid="+itemid,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        checkDNProblem:function (itemid,dn,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.checkDnProblem+"?itemid="+itemid+"&DN="+dn,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //检查非正品货位
        checkPallet:function (storageid,itemId,type,useAfter,success,error) {
            console.log("storageid-->"+storageid+"/itemid-->"+itemId+"/useafter-->"+useAfter+"/type-->"+type);
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.checkcontainer+"?storageid="+storageid+"&itemid="+itemId+"&type="+type+"&useAfter="+useAfter,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //上架商品
        finishReceive:function (databody,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.finishreceive,
                method:"POST",
                data:databody,
                success:function () {
                    success();
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //上架商品
        finishStow:function (databody,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.finishstow,
                method:"POST",
                data:databody,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        finishInnormalReceive:function (databody,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.finishreceive,
                method:"POST",
                data:databody,
                success:function () {
                    success();
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //获取暗灯列表项
        getReportLight:function (success) {
          commonService.ajaxMushiny({
              url:INBOUND_CONSTANT.getreportmenu,
              success:function (datas) {
                  success(datas.data);
              }
          });
        },
        //获取所有已选货位类型
        getSelectedStorageType:function (stationName,success) {
          commonService.ajaxMushiny({
              url:INBOUND_CONSTANT.getselectedbintype+"?stationName="+stationName,
              success:function (datas) {
                  success&&success(datas.data);
              }
          });
        },
        getStowSelectedStorageType:function (stationName,success) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.getstowselectedbintype+"?stationName="+stationName,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },
        getstoragelocationId:function (name,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.getstoragelocationId+"?stName="+name,
                success:function (datas) {
                    success&&success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
        //报告暗灯
        createReportLight:function (databody,success,error) {
            commonService.ajaxMushiny({
                url:INBOUND_CONSTANT.postreportlight,
                method:"POST",
                data:databody,
                success:function () {
                    success();
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            });
        },
      //扫描目的地
      scanDestination: function(stationId, name, success, error){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.scanDestination+ "?receivingStationId="+ stationId+ "&name="+ name,
          success: function(datas){
            success && success(datas.data);
          },
          error: function(datas){
            error && error(datas.data);
          }
        });
      },
      // 扫描车牌
      scanContainer: function(name, success, error){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.scanContainer+ "?name="+ name,
          success: function(datas){
            success && success(datas.data);
          },
          error: function(datas){
            error && error(datas.data);
          }
        });
      },
      // 获取目的地车牌信息
      getReceivingContainer: function(receivingStationId, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.getReceivingContainer+ "?receivingStationId="+ receivingStationId,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 保存目的地车牌信息
      saveReceivingContainer: function(data, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.saveReceivingContainer,
          method: "POST",
          data: data,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 修改车牌信息
      replaceContainer: function(oldContainerId, newContainerId, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.replaceContainer+ "?oldContainerId="+ oldContainerId+ "&newContainerId="+ newContainerId,
          method: "PUT",
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 删除目的地车牌信息
      deleteReceivingContainer: function(stationId, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.deleteReceivingContainer+ "?receivingStationId="+ stationId,
          method: "DELETE",
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 扫描收货单号
      scanReceiving: function(adviceNo, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.scanReceiving+ "?adviceNo="+ adviceNo,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 扫描商品
      scanItemData: function(adviceId, itemNo, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.scanItemData+ "?adviceId="+ adviceId+ "&itemNo="+ itemNo,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 筛选目得地
      screenReceivingDestination: function(itemDataId, receivingType, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.screenReceivingDestination+ "?itemDataId="+ itemDataId+ "&receivingType="+ receivingType,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
        // 检查小车是否不同客户同商品
        getNewestReceiveAmount: function(requestid, itemDataId, success, error){
            var options = {
                url: INBOUND_CONSTANT.getnewestreceiveamount+ "?requestid="+ requestid+ "&itemid="+ itemDataId,
                success: function(datas){
                    success && success(datas.data);
                },
                error:function (datas) {
                    error&&error(datas.data);
                }
            };
            commonService.ajaxMushiny(options);
        },
      // 检查小车是否不同客户同商品
      checkContainers: function(containerId, itemDataId, useNotAfter, success, error){
        useNotAfter != "" && (useNotAfter = "&useNotAfter="+ useNotAfter);
        var options = {
          url: INBOUND_CONSTANT.checkContainer+ "?itemDataId="+ itemDataId+ "&containerId="+ containerId+ useNotAfter,
          success: function(datas){
            success && success(datas.data);
          }
        };
        error && (options.error = function(datas){
          error(datas.data);
        });
        commonService.ajaxMushiny(options);
      },
      // 测量
      receivingGoodsToCubiScan: function(data, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.receivingGoodsToCubiScan,
          method: "POST",
          data: data,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // stockUnit
      receivingGoodsToStockUnit: function(data, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.receivingGoodsToStockUnit,
          method: "POST",
          data: data,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      exitReceiveStation: function(stationName,type,success,error){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.exitReceiveStation+"?stationName="+stationName+"&type="+type,
          method: "DELETE",
          success: function(){
            success();
          },
          error:function(datas){
            error&&error(datas.data);
          }
        });
      },
        exitStowStationBefore: function(stationName,exitType,ciperid,success,error){
            commonService.ajaxMushiny({
                url: INBOUND_CONSTANT.exitStowStationBefore+"?stationName="+stationName+"&exitType="+exitType+"&ciperid="+ciperid,
                method: "DELETE",
                success: function(datas){
                    success&&success(datas.data);
                },
                error:function(datas){
                    error&&error(datas.data);
                }
            });
        },
        exitStowStation: function(stationName,exitType,ciperid,success,error){
            commonService.ajaxMushiny({
                url: INBOUND_CONSTANT.exitStowStation+"?stationName="+stationName+"&exitType="+exitType+"&ciperid="+ciperid,
                method: "DELETE",
                success: function(datas){
                    success&&success(datas.data);
                },
                error:function(datas){
                    error&&error(datas.data);
                }
            });
        },
        exitStowUI: function(stationName,fullStorageType,success,error){
            commonService.ajaxMushiny({
                url: INBOUND_CONSTANT.exitStowUI+"?stationName="+stationName+"&fullStorageType="+fullStorageType,
                method: "DELETE",
                success: function(datas){
                    success&&success(datas.data);
                },
                error:function(datas){
                    error&&error(datas.data);
                }
            });
        },
      // 商品残损
      receivingGoodsToDamage: function(data, success){
        commonService.ajaxMushiny({
          url: INBOUND_CONSTANT.receivingGoodsToDamage,
          method: "POST",
          data: data,
          success: function(datas){
            success && success(datas.data);
          }
        });
      }


    };
  });
})();