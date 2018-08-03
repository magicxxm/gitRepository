/**
 * Created by frank.zhou on 2017/04/25.
 */
(function() {
  "use strict";

  angular.module("myApp").service('adviceRequestService', function (commonService, MASTER_CONSTANT) {
    return {
      getItemData: function (clientId, cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getItemData+ "?clientId=" + clientId,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
        lock: function (id, cb) {
            commonService.ajaxMushiny({
                url: MASTER_CONSTANT.lock+ "?id=" + id,
                method: "PUT",
                success: function (datas) {
                    cb && cb(datas.data);
                }
            });
        }
    };
  });
})();