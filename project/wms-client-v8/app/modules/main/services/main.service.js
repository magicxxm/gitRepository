/**
 * Created frank.zhou on 2017/04/17.
 */
(function(){
  "use strict";

  angular.module("myApp").service("mainService", function(commonService, $httpParamSerializer, MAIN_CONSTANT){
    return {
      // 取菜单
      getMainMenu: function(cb){
        commonService.ajaxMushiny({
          url: MAIN_CONSTANT.getMenus,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 取当前用户
      getUser: function(cb){
        commonService.ajaxMushiny({
          url: MAIN_CONSTANT.getUser,
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      },
      // 修改密码
      changePassword: function(password, newPassword, cb){
        commonService.ajaxMushiny({
          url: MAIN_CONSTANT.changePassword,
          method: "PUT",
          contentType: "application/x-www-form-urlencoded;charset=utf-8",
          data: $httpParamSerializer({
            password: password,
            newPassword: newPassword
          }),
          success: function(datas){
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();