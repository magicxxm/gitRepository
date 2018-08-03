/**
 * Created by thoma.bian on 2017/5/10.
 */

(function () {
  "use strict";

  angular.module("myApp").service('icqaAllStocktakingService', function (commonService, ICQA_CONSTANT) {
    return {
      // 取区域
      selectRoundOfInventory: function (times, value, createdDateOne, createdDateTwo,type, cb) {
        if (value == undefined) {
          value = "";
        }
        if (createdDateOne) {
          createdDateOne = createdDateOne + "T00:00:00Z";
        } else {
          createdDateOne = "";
        }
        if (createdDateTwo) {
          createdDateTwo = createdDateTwo + "T00:00:00Z";
        } else {
          createdDateTwo = "";
        }
        commonService.ajaxMushiny({
          url: ICQA_CONSTANT.findSelectRoundOfInventory + "?times=" + times + "&search=" + value + "&date1=" + createdDateOne + "&date2=" + createdDateTwo + "&type=" +type,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },


      selectRoundOfInventoryId: function (times, id,createdDateOne,createdDateTwo, type, cb) {

        if (createdDateOne) {
          createdDateOne = createdDateOne + "T00:00:00Z";
        } else {
          createdDateOne = "";
        }
        if (createdDateTwo) {
          createdDateTwo = createdDateTwo + "T00:00:00Z";
        } else {
          createdDateTwo = "";
        }
        commonService.ajaxMushiny({
          url: ICQA_CONSTANT.selectRoundOfInventoryId + "?times=" + times + "&stocktakingId=" + id+"&date1=" + createdDateOne + "&date2=" + createdDateTwo + "&type=" +type,
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
      select0rdersByStocktakingIds: function (stocktakingId, times,type, cb) {
        commonService.ajaxMushiny({
          url: ICQA_CONSTANT.select0rdersByStocktakingIds + "?stocktakingId=" + stocktakingId + "&times=" + times + "&type=" +type,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      saveStocktakingUser: function (data, cb) {
        commonService.ajaxMushiny({
          url: ICQA_CONSTANT.saveStocktakingUser,
          method: "POST",
          data: data,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      checkInventory: function (stocktakingId, times,daily, cb) {
        commonService.ajaxMushiny({
          url: ICQA_CONSTANT.checkInventory + "?stocktakingId=" + stocktakingId + "&times=" + times + "&rule=" +daily,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      checkOneInventory: function (stocktakingId, times, cb) {
        commonService.ajaxMushiny({
          url: ICQA_CONSTANT.checkOneInventory + "?stocktakingId=" + stocktakingId + "&times=" + times,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      deleteUsers: function (stocktakingId, times,daily, cb) {
        commonService.ajaxMushiny({
          method: "DELETE",
          url: ICQA_CONSTANT.deleteUsers + "?stocktakingId=" + stocktakingId + "&times=" + times + "&rule=" + daily,
          success: function (datas) {
            cb && cb(datas.data);
          }
        })
      },
      selectInventoryCount: function (stocktakingId,createdDateOne,createdDateTwo,type,cb) {
        if (createdDateOne) {
          createdDateOne = createdDateOne + "T00:00:00Z";
        } else {
          createdDateOne = "";
        }
        if (createdDateTwo) {
          createdDateTwo = createdDateTwo + "T00:00:00Z";
        } else {
          createdDateTwo = "";
        }
          commonService.ajaxMushiny({
              url: ICQA_CONSTANT.selectInventoryCount + "?id=" + stocktakingId+"&date1=" + createdDateOne + "&date2=" + createdDateTwo + "&type=" +type,
              success: function (datas) {
                  cb && cb(datas.data);
              }
          });
      },
        // getCloseStocktaking: function (ids,cb) {
        //     commonService.ajaxMushiny({
        //         url: ICQA_CONSTANT.getCloseStocktaking + "?ids=" + ids ,
        //         success: function (datas) {
        //             cb && cb(datas.data);
        //         }
        //     });
        //
        // }
        getCloseStocktaking: function (data,cb) {
            commonService.ajaxMushiny({
                url: ICQA_CONSTANT.getCloseStocktaking ,
                method: "POST",
                data: data,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });

        },
        getNotStocktakingAmount: function(stocktakingId,times,cb){
            commonService.ajaxMushiny({
                url: ICQA_CONSTANT.getNotStocktakingAmount + "?stocktakingId=" + stocktakingId + "&times=" + times,
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        }
    };
  });
})();
