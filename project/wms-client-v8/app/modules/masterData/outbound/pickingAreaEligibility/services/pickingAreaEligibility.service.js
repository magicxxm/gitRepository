/**
 * Created by frank.zhou on 2017/05/03.
 */
(function() {
  "use strict";

  angular.module("myApp").service('pickingAreaEligibilityService', function (commonService, MASTER_CONSTANT) {
    return {
      // 取pickingArea
      getPickingAreaByClient: function(clientId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getPickingAreaByClient+"?clientId="+ clientId,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配用户
      getUnassignedUserByPickingArea: function(pickingAreaId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUnassignedUserByPickingArea.replace("#id#", pickingAreaId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配用户
      getAssignedUserByPickingArea: function(pickingAreaId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getAssignedUserByPickingArea.replace("#id#", pickingAreaId),
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