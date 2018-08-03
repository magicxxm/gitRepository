/**
 * Created by frank.zhou on 2017/05/03.
 */
(function() {
  "use strict";

  angular.module("myApp").service('pickingProcessEligibilityService', function (commonService, MASTER_CONSTANT) {
    return {
      // 未分配用户
      getUnassignedUserByProcessPath: function(processPathId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUnassignedUserByProcessPath.replace("#id#", processPathId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配用户
      getAssignedUserByProcessPath: function(processPathId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getAssignedUserByProcessPath.replace("#id#", processPathId),
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