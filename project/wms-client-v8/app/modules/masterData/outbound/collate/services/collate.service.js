/**
 * Created by frank.zhou on 2017/03/07.
 */
(function() {
  "use strict";

  angular.module("myApp").service('collateService', function (commonService, OUTBOUND_CONSTANT) {
    return {
      // 查询collate
      getCollate: function(success){
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.getCollate,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 更新pp状态
      updateProcessPathStatus: function(data, success){
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.updateProcessPathStatus,
          method: "PUT",
          data: data,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 更新pp中collate属性
      updateProcessPathCollate: function(data, success){
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.updateProcessPathCollate,
          method: "PUT",
          data: data,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 取发货点信息
      getCollateExsdInfo: function(deliveryTime, success){
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.getCollateExsdInfo+ "?deliveryTime="+ deliveryTime,
          success: function(datas){
            success && success(datas.data);
          }
        });
      },
      // 取collateTemplate
      getCollateTemplate: function(success){
        commonService.ajaxMushiny({
          url: OUTBOUND_CONSTANT.getCollateTemplate,
          success: function(datas){
            success && success(datas.data);
          }
        });
      }
    };
  });
})();