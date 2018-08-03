/**
 * Created by frank.zhou on 2017/04/18.
 */
(function() {
  "use strict";

  angular.module("myApp").service('userWarehouseService', function (commonService, SYSTEM_CONSTANT) {
    return {
      // 未分配仓库
      getUnassignedWarehouseByUser: function(userId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedWarehouseByUser.replace("#id#", userId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配仓库
      getAssignedWarehouseByUser: function(userId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedWarehouseByUser.replace("#id#", userId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配用户
      getUnassignedUserByWarehouse: function(warehouseId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedUserByWarehouse.replace("#id#", warehouseId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配用户
      getAssignedUserByWarehouse: function(warehouseId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedUserByWarehouse.replace("#id#", warehouseId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 保存
      save: function(key, id, items, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT[key].replace("#id#", id),
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