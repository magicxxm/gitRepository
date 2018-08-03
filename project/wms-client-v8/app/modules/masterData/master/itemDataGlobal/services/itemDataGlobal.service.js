/**
 * Created by frank.zhou on 2017/04/21.
 */
(function() {
  "use strict";

  angular.module("myApp").service('itemDataGlobalService', function (commonService, MASTER_CONSTANT) {
    return {
      getItemGroup: function (cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getItemGroup,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      getItemUnit: function (cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getItemUnit,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      getSizeFilterRule:function (cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getSizeFilterRule,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      getSelectionBySelectionKey: function (data1,cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getSelectionBySelectionKey+"?selectionKey="+data1,
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      saveData: function (data1,cb) {
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.saveImportItemDataGlobal,
          data:data1,
          method: "POST",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      // 检查SKU
      checkSKU: function(skuNo, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.checkSKU+"?skuNo="+ skuNo,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();