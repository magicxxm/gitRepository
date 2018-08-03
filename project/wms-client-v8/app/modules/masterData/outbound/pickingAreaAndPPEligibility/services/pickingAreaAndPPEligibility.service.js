/**
 * Created by HGF 2017/6/30.
 */
(function() {
  "use strict";

  angular.module("myApp").service('pickingAreaAndPPEligibilityService', function (commonService, MASTER_CONSTANT) {
    return {
      // 取user
      getUseByUserGroupId: function(userGroupId, cb){
        debugger;
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUseByUserGroupId+"?userGroupId="+ userGroupId,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配PP
      getUnassignedPPByUser: function(useId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUnassignedPPByUser.replace("#id#", useId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配PP
      getAssignedPPByUser: function(useId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getAssignedPPByUser.replace("#id#", useId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配区域
      getUnassignedAreaByClient: function(userId,clientId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUnassignedAreaByClient+"?id="+ userId+"&clientId="+clientId,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配区域

      getAssignedAreaByClient: function(userId,clientId,yi,cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getAssignedAreaByClient+"?userId="+ userId+"&clientId="+clientId+"&yi="+yi,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 保存
      save: function(key, id, items, cb){
        debugger;
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