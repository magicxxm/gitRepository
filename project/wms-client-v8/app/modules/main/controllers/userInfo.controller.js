/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("userInfoCtl", function ($scope, mainService, commonService, systemService){
    $scope.warehouseSource = systemService.getDataSource({key: "getWarehouse", text: "name", value: "id"});
    $scope.clientSource =  systemService.getDataSource({key: "getClient", text: "name", value: "id"});
    $scope.languageSource = systemService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "LANGUAGE"}
    });
    mainService.getUser(function(data){
      for(var k in data) $scope[k] = (k==="locale"? systemService.toMap(data[k]): data[k]);
    });
    // 修改
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.update("user", {
          "id": $scope.id,
          "username": $scope.username,
          "name": $scope.name,
          "email": $scope.email,
          "phone": $scope.phone,
          "locale": $scope.locale.selectionValue,
          "clientId": $scope.client? $scope.client.id: null,
          "warehouseId": $scope.warehouse? $scope.warehouse.id: null
        }, function () {
          commonService.dialogMushiny($scope.window, {type: "update"});
        });
      }
    };
  }).controller("userPasswordCtl", function($scope,$state,mainService, commonService){
    $scope.validate = function (event) {
      event.preventDefault();
      if($scope.validator.validate()){
        var oldPassword = $scope.oldPassword, newPassword = $scope.newPassword, confirmPassword = $scope.confirmPassword;
        if(newPassword != confirmPassword){

        }else
          mainService.changePassword(oldPassword, newPassword, function(){
              $state.go("main.mainMenu");
          });
      }
    };
  });
})();