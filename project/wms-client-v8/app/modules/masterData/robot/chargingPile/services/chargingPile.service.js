/**
 * Created by frank.zhou on 2017/04/24.
 */
(function() {
  "use strict";

  angular.module("myApp").service('chargingPileService', function (commonService, MASTER_CONSTANT) {
    return {
      // getMapBySectionId: function(id, cb){
      //   commonService.ajaxMushiny({
      //     url: MASTER_CONSTANT.getMapBySectionId+"?sectionId="+ id,
      //     success: function(datas){
      //       cb && cb(datas.data);
      //     }
      //   });
      // }
    };
  });
})();