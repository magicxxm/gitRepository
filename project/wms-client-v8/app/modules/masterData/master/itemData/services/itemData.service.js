/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
  "use strict";

  angular.module("myApp").service('itemDataService', function (commonService, MASTER_CONSTANT) {
    return {
      itemGroupData: function (clientId,cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getItemDataGlobal+"?clientId="+ clientId,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      clientData: function (cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getClient,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      saveData: function (data1,cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.saveImportItemData,
          data:data1,
          method: "POST",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();