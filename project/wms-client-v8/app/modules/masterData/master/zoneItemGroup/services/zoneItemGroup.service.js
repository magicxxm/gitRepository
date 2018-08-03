/**
 * Created by frank.zhou on 2017/04/21.
 */
(function() {
  "use strict";

  angular.module("myApp").service('zoneItemGroupService', function (commonService, MASTER_CONSTANT) {
    return {
      // 取区域
      getZonesByClient: function(clientId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getZonesByClient+"?clientId="+ clientId,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配组
      getUnassignedItemGroupByZone: function(zoneId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUnassignedItemGroupByZone.replace("#id#", zoneId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配组
      getAssignedItemGroupByZone: function(zoneId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getAssignedItemGroupByZone.replace("#id#", zoneId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配区域
      getUnassignedZoneByItemGroup: function(itemGroupId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUnassignedZoneByItemGroup.replace("#id#", itemGroupId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配区域
      getAssignedZoneByItemGroup: function(itemGroupId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getAssignedZoneByItemGroup.replace("#id#", itemGroupId),
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