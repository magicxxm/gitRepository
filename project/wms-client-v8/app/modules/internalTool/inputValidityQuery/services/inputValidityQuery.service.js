/**
 * Created by PC-2 on 2017/5/12.
 */
(function () {
  'use strict';
  angular.module("myApp").service("inputValidityQueryService", function (commonService, $httpParamSerializer, INTERNAL_TOOL_CONSTANT) {
    return {
      //获取有效期录入信息
      getBySearchTerm: function (success, searchTerm, startDate, endDate) {
        if (!searchTerm) searchTerm = '';
        if (!startDate) startDate = '';
        if (!endDate) endDate = '';
        var date=new Date(endDate);
        date.setDate(date.getDate()+ 1);
       endDate = endDate ? kendo.format("{0:yyyy-MM-dd}", date ) : "";
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.getBySearchTerm + "?searchTerm=" + searchTerm + "&startDate=" + startDate + "&endDate=" + endDate,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      },
      getItemData: function (success, itemNo, clientId, warehouseId) {
        if (!itemNo) itemNo = '';
        if (!clientId) clientId = '';
        if (!warehouseId) warehouseId = '';
        commonService.ajaxMushiny({
          url: INTERNAL_TOOL_CONSTANT.publicGetItemData + "?itemNo=" + itemNo + "&clientId=" + clientId + "&warehouseId=" + warehouseId,
          success: function (datas) {
            success && success(datas.data);
          }
        });
      }
    };
  });
})();