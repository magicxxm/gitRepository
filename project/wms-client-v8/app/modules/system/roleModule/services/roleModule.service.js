/**
 * Created by frank.zhou on 2017/04/18.
 */
(function() {
  "use strict";

  angular.module("myApp").service('roleModuleService', function (commonService, SYSTEM_CONSTANT) {
    return {
      // 未分配模块
      getUnassignedModuleByRole: function(roleId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedModuleByRole.replace("#id#", roleId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配模块
      getAssignedModuleByRole: function(roleId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedModuleByRole.replace("#id#", roleId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配角色
      getUnassignedRoleByModule: function(moduleId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getUnassignedRoleByModule.replace("#id#", moduleId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配角色
      getAssignedRoleByModule: function(moduleId, cb){
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT.getAssignedRoleByModule.replace("#id#", moduleId),
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