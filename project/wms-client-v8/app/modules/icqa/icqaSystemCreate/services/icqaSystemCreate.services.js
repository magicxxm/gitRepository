/**
 * Created by thoma.bian on 2017/5/10.
 */

(function () {
  "use strict";

  angular.module("myApp").service('icqaSystemCreateService', function (commonService, ICQA_CONSTANT) {
    return {
      // 取区域
      getStocktaking0rder: function (data, cb) {
        commonService.ajaxMushiny({
          url: ICQA_CONSTANT.getStocktaking0rder + "?areaName=" + data.areaName + "&parameter=" + data.parameter + "&zoneList=" + data.zoneList,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      getStocktaking0rderUser: function (clientId, cb) {
        commonService.ajaxMushiny({
          url: ICQA_CONSTANT.getStocktaking0rderUser + "?clientId=" + clientId,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },

      getZone: function (clientId, cb) {
            commonService.ajaxMushiny({
                url: ICQA_CONSTANT.getZone + "?clientId=" + clientId,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
      selectClientOptions: function (cb) {
          commonService.ajaxMushiny({
              url: ICQA_CONSTANT.getClient,
              success: function (datas) {
                  cb && cb(datas.data);
              }
          });
      },

      getItemData: function (skuNo,localtionName,cb,error) {
          commonService.ajaxMushiny({
              url: ICQA_CONSTANT.getItemData + "?itemNo=" + skuNo + "&localtionName=" +localtionName,
              success: function (datas) {
                  cb && cb(datas.data);
              },
              error:function (datas) {
                  error&&error(datas.data);
              }
          });
      },
        // SKU 盘点创建 得到商品名称和数量
        getItemDataNameAmount: function (zoneList,itemNo,client,cb) {
            commonService.ajaxMushiny({
                url: ICQA_CONSTANT.getItemDataNameAmount + "?zoneList=" + zoneList + "&itemNo=" +itemNo + "&client=" +client,
                success: function (datas) {
                   cb && cb(datas.data);
                }
            });


        },
      saveStocktaking:function(data,cb,error){
        commonService.ajaxMushiny({
          method:"POST",
          data:data,
          url: ICQA_CONSTANT.saveSkuStocktaking ,
          success: function (datas) {
            cb && cb(datas.data);
          },
          error:function (datas) {
              error&&error(datas.data);
          }

        });
      },
        createStocktakingPosition: function (data, cb) {
            commonService.ajaxMushiny({
                method: "POST",
                data: data,
                url: ICQA_CONSTANT.saveSkuStocktakingPosition,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        },
        // 生成商品所在的货位
        createInventoryTask: function (stocktakingIds, cb) {
            commonService.ajaxMushiny({
                method: "POST",
                url: ICQA_CONSTANT.inventoryTask + "?stocktakingIds="+stocktakingIds,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            })
        }
    };
  });
})();
