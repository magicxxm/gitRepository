/**
 * Created by frank.zhou on 2017/04/06.
 */
(function() {
  "use strict";

  angular.module("myApp").service('collateLivePickerService', function (commonService, OUTBOUND_CONSTANT) {
    return {
      // 查询collate
      getLivePicker: function (processPathId, success) {
        var params = "";
        processPathId != null && (params += "?processPathId="+ processPathId);
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.getLivePicker+ params,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      }
    };
  });
})();