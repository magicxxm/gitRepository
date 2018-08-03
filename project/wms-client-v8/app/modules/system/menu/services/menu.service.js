/**
 * Created by frank.zhou on 2017/04/18.
 */
(function() {
  "use strict";

  angular.module("myApp").service('menuService', function (commonService, SYSTEM_CONSTANT) {
    return {
      // 取菜单
      getRootModules: function(cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getRootModules,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 取已分配权限
      getAssignedModules: function(parentId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedModules.replace("#parentId#", parentId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 取未分配权限
      getUnassignedModules: function(parentId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedModules.replace("#parentId#", parentId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 保存已分配权限
      saveAssignedModules: function(parentId, menus, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.saveAssignedModules.replace("#parentId#", parentId),
          method: "POST",
          data: angular.toJson(menus),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();