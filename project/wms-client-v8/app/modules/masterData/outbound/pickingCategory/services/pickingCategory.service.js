/**
 * Created by frank.zhou on 2017/05/03.
 */
(function() {
  "use strict";

  angular.module("myApp").service('pickingCategoryService', function (commonService, MASTER_CONSTANT) {
    return {
      // 取boxType
      getBoxType: function (clientId, success) {
        var data = null;
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getBoxType+ "?clientId="+ clientId,
          async: false,
          success: function (datas) {
            data = datas;
            success && success(datas);
          }
        });
        return data;
      },
      // 取itemGroup
      getItemGroup: function(success){
        var data = null;
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getItemGroup,
          async: false,
          success: function (datas) {
            data = datas;
            success && success(datas);
          }
        });
        return data;
      },
      // 取zone
      getZone: function(clientId, success){
          var data = null;
          commonService.ajaxMushiny({
              url: MASTER_CONSTANT.getZone+ "?clientId="+ clientId,
              async: false,
              success: function (datas) {
                  data = datas;
                  success && success(datas);
              }
          });
          return data;
      },
        getCarrier: function(success){
            var data = null;
            commonService.ajaxMushiny({
                url: MASTER_CONSTANT.getCarrier,
                async: false,
                success: function (datas) {
                    data = datas;
                    success && success(datas);
                }
            });
            return data;
        }
  };
  });
})();