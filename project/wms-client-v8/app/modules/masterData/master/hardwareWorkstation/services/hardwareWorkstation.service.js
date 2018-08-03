/**
 * Created by frank.zhou on 2017/05/26.
 */
(function() {
  "use strict";

  angular.module("myApp").service('hardwareWorkstationService', function (commonService, MASTER_CONSTANT) {
    return {
      // 取工作站
      getWorkstationList: function(cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getWorkstationList,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 未分配硬件
      getUnassignedHardwareByWorkstation: function(workstationId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getUnassignedHardwareByWorkstation.replace("#id#", workstationId),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 已分配硬件
      getAssignedHardwareByWorkstation: function(workstationId, cb){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getAssignedHardwareByWorkstation.replace("#id#", workstationId),
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