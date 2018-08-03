/**
 * Created by thoma.bian on 2017/5/10.
 */

(function () {
  "use strict";

  angular.module("myApp").service('icqaCreateService', function (commonService, ICQA_CONSTANT) {
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
      createStocktaking0rder: function (zoneStr, data, cb) {
       commonService.ajaxMushiny({
          method: "POST",
          url: ICQA_CONSTANT.getStocktaking0rder + "?zoneList=" + zoneStr,
          data: data,
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
      getStocktakingRules: function (cb) {
        commonService.ajaxMushiny({
          url: ICQA_CONSTANT.getStocktakingRules ,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      saveStocktaking:function(data,cb,error){
        commonService.ajaxMushiny({
          method:"POST",
          data:data,
          url: ICQA_CONSTANT.saveStocktaking ,
          success: function (datas) {
            cb && cb(datas.data);
          },
          error:function (datas) {
              error&&error(datas.data);
          }

        });
      }
    };
  });
})();
