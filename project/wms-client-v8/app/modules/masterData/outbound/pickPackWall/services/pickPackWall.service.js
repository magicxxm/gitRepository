/**
 * Created by frank.zhou on 2017/05/25.
 */
(function() {
  "use strict";

  angular.module("myApp").service('pickPackWallService', function (commonService, MASTER_CONSTANT) {
    return {
      // 取电子标签
      getDigitalLabelByLabel: function(labelIds, cb){

        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getDigitalLabelByLabel.replace("#ids#", labelIds),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();