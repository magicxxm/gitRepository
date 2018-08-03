/**
 * Created by frank.zhou on 2017/05/03.
 */
(function() {
  "use strict";

  angular.module("myApp").service('pickingAreaService', function (commonService, MASTER_CONSTANT) {
    return {
      getZone: function (clientId, cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getZone+ "?clientId=" + clientId,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();