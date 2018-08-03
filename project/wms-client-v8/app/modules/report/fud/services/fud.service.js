/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  "use strict";

  angular.module("myApp").service('fudService', function (commonService, REPORT_CONSTANT) {
    return {
      // 取workflow
      getLegacyData: function (success) {
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.getLegacyData,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      getFud: function (success) {
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT.getFud,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      }
    };
  });
})();
