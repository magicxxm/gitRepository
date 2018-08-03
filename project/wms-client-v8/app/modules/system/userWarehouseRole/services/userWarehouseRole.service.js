/**
 * Created by frank.zhou on 2017/04/18.
 */
(function() {
  "use strict";

  angular.module("myApp").service('userWarehouseRoleService', function (commonService, SYSTEM_CONSTANT) {
    return {
      // 取用户
      getUserByWarehouseInRole: function(warehouseId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUserByWarehouseInRole.replace("#id#", warehouseId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配角色
      getUnassignedRoleByWarehouseUser: function(warehouseId, userId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedRoleByWarehouseUser.replace("#warehouseId#", warehouseId).replace("#userId#", userId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配角色
      getAssignedRoleByWarehouseUser: function(warehouseId, userId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedRoleByWarehouseUser.replace("#warehouseId#", warehouseId).replace("#userId#", userId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 取角色
      getRoleByWarehouse: function(warehouseId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getRoleByWarehouse.replace("#id#", warehouseId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配用户
      getUnassignedUserByWarehouseRole: function(warehouseId, roleId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedUserByWarehouseRole.replace("#warehouseId#", warehouseId).replace("#roleId#", roleId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配用户
      getAssignedUserByWarehouseRole: function(warehouseId, roleId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedUserByWarehouseRole.replace("#warehouseId#", warehouseId).replace("#roleId#", roleId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 保存角色
      saveRolesByWarehouseUser: function(warehouseId, userId, items, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.saveRolesByWarehouseUser.replace("#warehouseId#", warehouseId).replace("#userId#", userId),
          data: items,
          method: "POST",
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 保存用户
      saveUsersByWarehouseRole: function(warehouseId, roleId, items, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.saveUsersByWarehouseRole.replace("#warehouseId#", warehouseId).replace("#roleId#", roleId),
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