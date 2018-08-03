/**
 * Created by frank.zhou on 2017/07/10.
 */
(function() {
  "use strict";

  angular.module("myApp").service('stowEligibilityService', function (commonService, MASTER_CONSTANT) {
    return {
      // 未分配权限
      getUnassignedStowThresholdByUser: function(userId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUnassignedStowThresholdByUser.replace("#id#", userId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配权限
      getAssignedStowThresholdByUser: function(userId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getAssignedStowThresholdByUser.replace("#id#", userId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 保存
      save: function(key, id, items, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT[key].replace("#id#", id),
          data: items,
          method: "POST",
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();