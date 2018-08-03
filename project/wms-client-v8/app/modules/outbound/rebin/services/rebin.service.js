/**
 * Created by frank.zhou on 2017/01/17.
 */
(function () {
  "use strict";

  angular.module("myApp").service('rebinService', function (commonService, OUTBOUND_CONSTANT) {
    return {
      // 扫描工作站
      scanStation: function (data, success, error) {
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.scanStation + "?name=" + data,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      // 扫描批次号
      scanPickingOrder: function (number, rebinStationId, success,error) {
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.scanPickingOrder + "?number=" + number + "&rebinStationId=" + rebinStationId,
          success: function (datas) {
            success && success(datas.data);
          },
          error:function (datas) {
                error && error(datas.data);
          }
        });
      },
      // 扫描rebinWall
      scanReBinWall: function (name, requestId, success, error) {
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.scanReBinWall.replace(/{requestId}/, requestId) + "?name=" + name,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      //绑定rebinwall
      rebinWalls: function (data, success, error) {
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.rebinWalls.replace(/{requestId}/, data.requestId),
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
      // 扫描拣货车
      scanPickingContainer: function (containerName, rebinFromUnitLoadId, requestId, success, error) {
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.scanPickingContainer.replace(/{requestId}/, requestId) + "?containerName=" + containerName+"&rebinFromUnitLoadId="+rebinFromUnitLoadId,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      // 扫描商品
      scanGoods: function (data, success, error) {
        commonService.ajaxMushiny({
          method: "POST",
          data: data,
          url: OUTBOUND_CONSTANT.scanGoods.replace(/{requestId}/, data.id),
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },

        // 商品丢失
        confirmLoseItem: function (requestId, rebinFromUnitLoadId, success, error) {
            commonService.ajaxMushiny({
                url: OUTBOUND_CONSTANT.confirmLoseItem.replace(/{requestId}/, requestId)  +"?rebinFromUnitLoadId="+rebinFromUnitLoadId,
                success: function (datas) {
                    success && success(datas.data);
                },
                error: function (datas) {
                    error && error(datas.data);
                }
            });
        },
      //完成Rebin Position操作
      rebinPosition: function (data, requestId, success) {
        var rebinPosition = OUTBOUND_CONSTANT.rebinPosition.replace(/{requestId}/, requestId);
        rebinPosition = rebinPosition.replace(/{positionId}/, data.id);
        commonService.ajaxMushiny({
          data: data,
          method: "POST",
          url: rebinPosition,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },

        // rebin完成
        finishRebin: function (stationName, success) {
            commonService.ajaxMushiny({
                url: OUTBOUND_CONSTANT.finishRebin+"?stationName="+stationName,
                success: function (datas) {
                    success && success(datas.data);
                }
            });
        },
      //查看所有拣货车进度
      rebinfromUnitloads: function (data, success) {
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.rebinfromUnitloads.replace(/{requestId}/, data.requestId),
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      // 结束rebin
      rebinEnd: function (requestId, success, error) {
        commonService.ajaxMushiny({
          method: "POST",
          url: OUTBOUND_CONSTANT.rebinEnd.replace(/{requestId}/, requestId),
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      // 报问题
      reportProblem: function (data, success) {
        commonService.ajaxMushiny({
          method: "POST",
          url: OUTBOUND_CONSTANT.obproblem,
          data: data,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
        // 多货锁定货框
        getMoreItem: function (requestId,rebinFromUnitLoadId, success,error) {
            commonService.ajaxMushiny({
                url: OUTBOUND_CONSTANT.lockContainer.replace(/{requestId}/, requestId)  +"?rebinFromUnitLoadId="+rebinFromUnitLoadId,
                success: function (datas) {
                    success && success(datas.data);
                },
                error:function (data) {
                    error && error(datas.data);
                }
            });
        },
      // 取无法扫描商品
      getUnscanGoods: function (data,success) {
        commonService.ajaxMushiny({
           data: data,
          method: "POST",
          url: OUTBOUND_CONSTANT.rebinCannotscan.replace(/{requestId}/, data.id),
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      // 数量多货查询
      rebinMore: function (data,success) {
        commonService.ajaxMushiny({
           data: data,
          method: "POST",
          url: OUTBOUND_CONSTANT.rebinMore.replace(/{requestId}/, data.id),
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      // sku多货查询
      rebinSkumore: function (data,success) {
        commonService.ajaxMushiny({
           data: data,
          method: "POST",
          url: OUTBOUND_CONSTANT.rebinSkumore.replace(/{requestId}/, data.id),
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      // 商品丢失列表
      rebinLess: function (data,success) {
        commonService.ajaxMushiny({
           data: data,
          method: "POST",
          url: OUTBOUND_CONSTANT.rebinLess.replace(/{requestId}/, data.id),
          success: function (datas) {
            success && success(datas.data);
          }
        });
      }
      ,
      // 查询rebinwall 异常表格
      rebinResult: function (requestId,success) {
        commonService.ajaxMushiny({
          method: "POST",
          url: OUTBOUND_CONSTANT.rebinResult.replace(/{requestId}/, requestId),
          success: function (datas) {
            success && success(datas.data);
          }
        });
      }
    };
  });
})();