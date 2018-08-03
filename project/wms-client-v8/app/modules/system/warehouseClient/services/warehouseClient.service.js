/**
 * Created by frank.zhou on 2017/04/18.
 */
(function() {
  "use strict";

  angular.module("myApp").service('warehouseClientService', function (commonService, SYSTEM_CONSTANT) {
    return {
      // 未分配客户
      getUnassignedClientByWarehouse: function(warehouseId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedClientByWarehouse.replace("#id#", warehouseId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配客户
      getAssignedClientByWarehouse: function(warehouseId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedClientByWarehouse.replace("#id#", warehouseId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配仓库
      getUnassignedWarehouseByClient: function(clientId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedWarehouseByClient.replace("#id#", clientId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配仓库
      getAssignedWarehouseByClient: function(clientId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedWarehouseByClient.replace("#id#", clientId),
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