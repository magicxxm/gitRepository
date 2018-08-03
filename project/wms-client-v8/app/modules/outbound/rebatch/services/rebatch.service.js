/**
 * Created by bian on 2016/11/30.
 */

(function() {
  "use strict";

  angular.module("myApp").service('reBatchService', function (commonService,OUTBOUND_CONSTANT,BACKEND_CONFIG) {
    return {
      // 所有slot的信息
      getReBatchInfo: function(success){
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.getReBatchInfo,
          success: function(datas){
              success && success(datas.data)
          }
        });
      },

        //退出工作站
        exitClick:function (name,success,error) {
            commonService.ajaxMushiny({
                url:OUTBOUND_CONSTANT.exitClick+"?stationName="+name,
                success:function (datas) {
                    success && success(datas.data);
                },
                error:function (datas) {
                    error && error(datas.data);
                }
            });
        },
      //扫描工作站
      scanReBatchStations:function (name,success,error) {
        commonService.ajaxMushiny({
          url:OUTBOUND_CONSTANT.scanReBatchStations+"?name="+name,
          success:function (datas) {
              success && success(datas.data);
          },
          error:function (datas) {
              error && error(datas.data);
          }
        });
      },

      //扫描容器
      scanReBatchContainer:function (containerName,reBachStationName,success,error) {
          commonService.ajaxMushiny({
            url:OUTBOUND_CONSTANT.scanReBatchContainer+"?containerName="+containerName+"&stationName="+reBachStationName,
            success:function (datas) {
                success && success(datas.data)
            },
            error:function (datas) {
                error && error(datas.data)
            }
          })
      },

      //扫描RebatchSlotdoReBatch
      scanReBatchSlot:function (positionId,data,success) {
          var url=OUTBOUND_CONSTANT.scanReBatchSlot.replace(/{positionId}/,positionId);
          commonService.ajaxMushiny({
            url:url,
            method:"POST",
            data:data,
            success:function (datas) {
                success && success(datas.data)
            }
          })
      },

      //确认Rebatch
      doReBatch:function (requestId,stationName,success,error) {
        var url=OUTBOUND_CONSTANT.doReBatch.replace(/{requestId}/,requestId)+"?stationName="+stationName;
        commonService.ajaxMushiny({
          url:url,
          success:function () {
              success && success()
          },
          error:function () {
              error && error()
          }
        })
      }
    };
  });
})();
