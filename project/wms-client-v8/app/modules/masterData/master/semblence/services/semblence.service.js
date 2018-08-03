/**
 * Created by preston.zhang on 2017/06/05.
 */
(function() {
  "use strict";

  angular.module("myApp").service('semblenceService', function (commonService, MASTER_CONSTANT) {
    return {
        //获取所有的semblance
        getSemblences:function (success,error) {
            commonService.ajaxMushiny({
                url:MASTER_CONSTANT.findSemblence,
                success:function (datas) {
                    success&&success(datas.data);
                }
            });
        },
      // 取client
      getClient: function(success,error){
        commonService.ajaxMushiny({
          url: MASTER_CONSTANT.getSemblenceClient,
          success: function(datas){
            success && success(datas.data);
          },
          error:function (datas){
            error&&error(datas.data);
          }
        });
      },
      //创建相似度
      createSemblence:function (databody,success,error) {
          commonService.ajaxMushiny({
              url:MASTER_CONSTANT.createSemblence,
              method:"POST",
              data:databody,
              success:function () {
                  success();
              }
          });
      },
      //更新相似度
      updateSemblence:function (databody,success,error) {
          commonService.ajaxMushiny({
              url:MASTER_CONSTANT.updateSemblence,
              method:"POST",
              data:databody,
              success:function () {
                  success();
              }
          });
      },
        //删除相似度
        deleteSemblence:function (id,success) {
            commonService.ajaxMushiny({
                url:MASTER_CONSTANT.deleteSemblence+"/"+id,
                method:"DELETE",
                success:function () {
                    success();
                }
            });
        }
    };
  });
})();