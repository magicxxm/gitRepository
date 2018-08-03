/**
 * Created by frank.zhou on 2017/04/26.
 */
(function() {
  "use strict";

  angular.module("myApp").service('receiveEligibilityService', function (commonService, MASTER_CONSTANT) {
    return {
      // 未分配权限
      getUnassignedThresholdByUser: function(userId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUnassignedThresholdByUser.replace("#id#", userId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配权限
      getAssignedThresholdByUser: function(userId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getAssignedThresholdByUser.replace("#id#", userId),
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