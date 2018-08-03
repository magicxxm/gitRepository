/**
 * Created by PC-7 on 2017/3/14.
 */


(function () {
  "use strict";

  angular.module("myApp").service('skuToolService', function (commonService, INTERNAL_TOOL_CONSTANT) {
    return {
      getByItemDataNo: function (sku, success, error) {
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.getByItemDataNo + "?sku=" + sku,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      getItemPurchasingRecords: function (sku, success,error) {
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.getItemPurchasingRecords + "?sku=" + sku,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      getItemAdjustRecords: function (sku, success,error) {
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.getItemAdjustRecords + "?sku=" + sku,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      },
      getItemRecords: function (sku, success,error) {
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.getItemRecords + "?sku=" + sku,
          success: function (datas) {
            success && success(datas.data);
          },
          error: function (datas) {
            error && error(datas.data);
          }
        });
      }
    };
  });
})();
