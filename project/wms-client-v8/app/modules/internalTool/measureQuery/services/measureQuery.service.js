/**
 * Created by PC-2 on 2017/5/11.
 */
(function () {
  "use strict";
  angular.module("myApp").service("measureQueryService", function (commonService, $httpParamSerializer, INTERNAL_TOOL_CONSTANT) {
    return {
      //获取测量查询信息
      getMeasureQuery: function (success, searchTerm, startDate, endDate) {
        if (!searchTerm) searchTerm = '';
        if (!startDate) startDate = '';
        if (!endDate) endDate = '';
        var date = new Date(endDate);
        date.setDate(date.getDate() + 1);
        endDate = endDate ? kendo.format("{0:yyyy-MM-dd}", date) : "";
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.getMeasureQuery + "?searchTerm=" + searchTerm + "&startDate=" + startDate + "&endDate=" + endDate,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      //获取商品信息
      getGoodsDetail: function (sku, success) {
        success({
          skuNo: "123456789",
          skuId: "MSA00000001",
          skuName: "Dettol 滴露 健康沐浴露薄荷冰爽935g+935g 超值特惠两瓶装 特卖",
          depth: 200,
          width: 150,
          height: 70,
          weight: "1.9g"
        });
        return success;
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.getGoodsDetail + "sukNo=" + sku,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      //获取商品尺寸更改记录
      getSizeChangeRecordGrid: function (sku, success) {
        success([{
          "operator": "admin"
        }]);
        return success;
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.getSizeChangeRecordGrid + "sku=" + sku,
          success: function (datas) {
            success && success(datas.data)
          }
        });
      }
    };
  });
})();