/**
 * Created by frank.zhou on 2017/03/13.
 */
(function() {
  "use strict";

  angular.module("myApp").service('zoneProcessPathService', function (commonService, OUTBOUND_CONSTANT) {
    return {
      // 取zoneProcessPath
      getZoneProcessPath: function (success) {
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.getZoneProcessPath,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      }
    };
  });
})();