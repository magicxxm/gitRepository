/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
  "use strict";

  angular.module("myApp").service('userService', function (commonService, SYSTEM_CONSTANT) {
    return {
      languageData: function (cb) {
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getSelectionBySelectionKey + "?selectionKey=" + "LANGUAGE",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      warehouseData: function (cb) {
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getWarehouse,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      clientData: function (cb) {
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getClient,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      userGroupData: function (cb) {
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUserGroup,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();