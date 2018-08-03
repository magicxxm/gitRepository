/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */


(function () {
  "use strict";

  angular.module("myApp").service('containerToolService', function (commonService, INTERNAL_TOOL_CONSTANT) {
    return {
      //
      getStorageRecords : function (storageLocationName, success,error) {
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.storageRecords  + "?storageLocationName=" + storageLocationName,
          success: function (datas) {
            success && success(datas.data);
          },
          error:function (datas) {
            error && error(datas.data);
          }
        });
      }
    };
  });
})();