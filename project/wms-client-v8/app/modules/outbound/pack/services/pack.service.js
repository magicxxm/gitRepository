/**
 * Created by feiyu.pan on 2017/4/19.
 */
(function () {
  "use strict";

  angular.module("myApp").service('packService', function ($http,commonService, $httpParamSerializer, OUTBOUND_CONSTANT) {
    return{
      //获得商品列表
      getGoods:function (pickPackCell,stationName,scan,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.getGoods+"?pickPackCellName="+pickPackCell+"&stationName="+stationName+"&scan="+scan,
          success:function (datas) {
            success && success(datas.data)
          },
          error:function (datas) {
              error && error(datas.data);
          }
        })
      },

        //获得商品列表
        getGoodsbByShipment:function (shipment,stationName,scan,success,error) {
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.getGoodsbByShipment+"?shipment="+shipment+"&stationName="+stationName+"&scan="+scan,
                success:function (datas) {
                    success && success(datas.data)
                },
                error:function (datas) {
                    error && error(datas.data);
                }
            })
        },
      //检查包装工作站
      checkPackStation:function (stationName,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.checkPackStation+"?stationName="+stationName,
          success:function (datas) {
            success && success(datas.data)
          },error:function (datas) {
            error && error(datas.data)
          }
        })
      },
      //检查pickPackWall
      checkPickPackWall:function (pickPackWallName,workStationId,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.checkPickPackWall+"?pickPackWallName="+pickPackWallName+"&workStationId="+workStationId,
          success:function (datas) {
            success && success(datas.data)
          },error:function (datas) {
            error && error(datas.data)
          }
        })
      },
        //查询是否有已拣完货的订单
        getDigitalShipment:function (pickPackWallName,station,success,error) {
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.getDigitalShipment+"?pickPackWallName="+pickPackWallName+"&station="+station,
                success:function (datas) {
                    success && success(datas.data)
                },error:function (datas) {
                    error && error(datas.data)
                }
            })
        },
        //删除已拍灯的订单
        updateDigitalShipment:function (shipment,station,success,error) {
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.updateDigitalShipment+"?shipment="+shipment+"&station="+station,
                success:function (datas) {
                    success && success(datas.data)
                },error:function (datas) {
                    error && error(datas.data)
                }
            })
        },
        //通过灯获取cellname
        getCellName:function (digitalLabel,success,error) {
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.getCellName+"?digitalLabel="+digitalLabel,
                success:function (datas) {
                    success && success(datas.data)
                },error:function (datas) {
                    error && error(datas.data)
                }
            })
        },
      //称重
      weigh:function (shipmentNo,weight,success) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.weigh+"?shipmentNo="+shipmentNo+"&weight="+weight,
          success:function (datas) {
            success && success(datas.data)
          }
        })
      },
      //检查商品是否存在
      checkItem:function (itemNo,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.checkItem+"?itemNo="+itemNo,
          success:function (datas) {
            success && success(datas.data)
          },error:function (datas) {
            error && error(datas.data)
          }
        })
      },
      //包装
      packing:function (data,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.packing,
          method:"POST",
          data:data,
          success:function (datas) {
            success && success(datas.data)
          },error:function (datas) {
            error && error(datas.data)
          }
        })
      },
      //扫描订单号
      scanShimpment:function (shipment,storageName,success,error) {
         commonService.ajaxMushiny({
            url:OUTBOUND_CONSTANT.scanShimpment+"?shipment="+shipment+"&storageName="+storageName,
            success:function (datas) {
               success && success(datas.data)
            },error:function (datas) {
               error && error(datas.data)
            }
         })
        },
        //扫描商品
        checkScanItem:function (itemNo,reBinCellName,shipmentId,success,error) {
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.checkScanItem+"?itemNo="+itemNo+"&storageName="+reBinCellName+"&shipmentId="+shipmentId,
                success:function (datas) {
                    success && success(datas.data)
                },error:function (datas) {
                    error && error(datas.data)
                }
            })
        },
      //包装结束
      packFinish:function (shipmentNo,boxName,cellName,type,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.packFinish+"?shipmentNo="+shipmentNo+"&boxName="+boxName+"&cellName="+cellName+"&type="+type,
          success:function (datas) {
            success && success(datas.data)
          },error:function (datas) {
            error &&  error(datas.data)
          }
        })
      },
      //检查问题货框
      checkProblemContainer:function (itemNo,storageLocationName,type,cellName,stateType,itemDataId,lossAmount,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.checkProblemContainer+"?shipmentNo="+itemNo+"&storageLocationName="+storageLocationName+"&type="+type+"&cellName="+cellName+"&stateType="+stateType+"&itemDataId="+itemDataId+"&lossAmount="+lossAmount,
          success:function (datas) {
            success && success(datas.data)
          },error:function (datas) {
            error && error(datas.data)
          }
        })
      },
        //检查问题订单
      checkProblemShipment:function (shipmentNo,success,error) {
          commonService.ajaxMushiny({
              url:OUTBOUND_CONSTANT.checkProblemShipment+"?shipmentNo="+shipmentNo,
              success:function (datas) {
                  success && success(datas.data)
              },error:function (datas) {
                  error && error(datas.data)
              }
          })
      },
      //检查正品容器
      checkDeleteContainer:function (shipmentNo,storageLocationName,reBinCellName,stateType,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.checkDeleteContainer+"?shipmentNo="+shipmentNo+"&storageLocationName="+storageLocationName+"&cellName="+reBinCellName+"&stateType="+stateType,
          success:function (datas) {
            success && success(datas.data)
          },error:function (datas) {
            error && error(datas.data)
          }
        })
      },
      //提交问题
      submitQuestion:function (data,success) {
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.submitQuestion,
          method:"POST",
          data:data,
          success: function (datas) {
            success && success(datas.data)
          }
        })
      },

        //设置cell格子的entitylock为1
        getMoreItem:function (cellName,success) {
            commonService.ajaxMushiny({
                url: OUTBOUND_CONSTANT.getMoreItem+"?cellName="+cellName,
                success: function (datas) {
                    success && success(datas.data)
                }
            })
        },
      //停止包装
      stopPack:function (stationName,success) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.stopPack+"?stationName="+stationName,
          success:function (datas) {
            success && success(datas.data)
          }
        })
      },
      //获取重量
      getWeight:function (id,success) {
         $http({
           url:"http://192.168.1.201:10093/outWeight/"+id,
         }).then(function (datas) {
             success&&success(datas.data);
         })
      },
      //获取按灯的格子
      getPickPackCellName:function (success) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.getPickPackCellName,
          async:false,
          success:function (datas) {
            success && success(datas)
          }
        })
      },
      informationInquiry:function (success) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.informationInquiry,
          success:function (datas) {
            success && success(datas.data)
          }
        })
      },

      getDigitalLabel:function (pickPackCellName,stationName,success,error) {
          commonService.ajaxMushiny({
              url:OUTBOUND_CONSTANT.getDigitalLabel+"?pickPackWallName="+pickPackCellName+"&stationName="+stationName,
              success:function (datas) {
                  success && success(datas.data)
              },error:function (datas) {
                  error && error(datas.data)
              }
          })
      }
    }
  });
})();