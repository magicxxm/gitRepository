/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
  "use strict";

  angular.module("myApp").service('sizeFilterRuleService', function (commonService, MASTER_CONSTANT) {
    return {

      getSizeFilterRule: function (cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getSizeFilterRule,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      getItemDataGlobal: function (cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getItemDataGlobal,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      upDateSize: function (data1,cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.updateItemDataGlobalSize,
          data:data1,
          method: "PUT",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
    };
  });
})();