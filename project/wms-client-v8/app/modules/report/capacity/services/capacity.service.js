/**
 * Created by zhihan.dong on 2017/04/17.
 * Updated by zhihan.dong on 2017/04/18.
 */
(function () {
  "use strict";
  angular.module("myApp").service('capacityService', function (commonService, REPORT_CONSTANT) {
    return {
      queryCapcityPods: function (cb) {
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.queryCapcityPods,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      queryCapacitySide: function (podName,cb) {
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.queryCapcitySides + "?podName="+podName,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      queryCapacityTotal: function (cb) {
 
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.capcityTotal,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      queryCapacityBin: function (podName,cb) {
 
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.queryCapcityBins + "?podName="+podName,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();