/**
 * Created by frank.zhou on 2017/04/18.
 */
(function() {
  "use strict";

  angular.module("myApp").service('rfMenuService', function (commonService, SYSTEM_CONSTANT) {
    return {
      // 取menu
      getRfMenu: function(id, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getRfMenu+ "?moduleId="+ id,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 取菜单
      getRootModules: function(cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getRootRfModules,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 取已分配权限
      getAssignedModules: function(parentId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedRfModules.replace("#parentId#", parentId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 取未分配权限
      getUnassignedModules: function(parentId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedRfModules.replace("#parentId#", parentId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 保存已分配权限
      saveAssignedModules: function(parentId, menus, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.saveAssignedRfModules.replace("#parentId#", parentId),
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