/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  angular.module("myApp")
    .provider("loginProvider", function($translateProvider, BACKEND_CONFIG){
      this.$get = function(){
        return {
          setLocale: function(lang){
            $.ajax({
              method: "GET",
              async: false,
              url: BACKEND_CONFIG.system+ "system/resources?locale="+ lang,
              success: function(resourceMap){
                $translateProvider.translations(lang, resourceMap);
              }
            });
          }
        };
      };
    })
    .controller('loginCtl', function ($window, $scope, loginService, loginProvider, $translate, $location, commonService){
      // =====================================================================================
      function login(warehouseId){
        loginService.doLogin($scope.username, $scope.password, warehouseId, function(data){
          $window.localStorage["accessToken"] = data["access_token"];
          $window.localStorage["warehouseId"] = warehouseId;
          $window.localStorage["authorities"] = data["authorities"];
          $location.path("/main");
        });
      }

      // =====================================================================================
      loginProvider.setLocale("CN");
      $translate.use("CN");
      // 验证
      $scope.validate = function (event){
        event.preventDefault();
        if($scope.validator.validate()){
          loginService.getUserWarehouse($scope.username, $scope.password, function(data){
            $window.localStorage["username"] = data.username;
            $window.localStorage["name"] = data.name;
            $window.localStorage["clientId"] = data.client.id;
            var warehouses = data.warehouses;
            if(!warehouses.length)
              commonService.dialogMushiny($scope.window, {url: "modules/login/templates/noWarehouseError.html"});
            else if(warehouses.length > 1){
              commonService.dialogMushiny($scope.window, {
                title: "<span style='font-size:12px;'>"+ $translate.instant("SELECT_USER_WAREHOUSE")+ "</span>",
                width: 280,
                height: 230,
                url: "modules/login/templates/selectWarehouse.html",
                open: function(win){
                  $scope.listWarehouses = loginService.getJsonForListView({datas: warehouses, selectable: "single"});
                  $scope.selectWarehouse = function(){
                    var list = $("#selectWarehouse").data("kendoListView");
                    var data = list.dataItem(list.select()[0]);
                    login(data.id);
                    win.close();
                  };
                }
              });
            }else
              login(warehouses[0].id);
          }, function(){
            commonService.dialogMushiny($scope.window, {
              url: "modules/login/templates/loginError.html"
            });
          });
        }
      }
    });
})();