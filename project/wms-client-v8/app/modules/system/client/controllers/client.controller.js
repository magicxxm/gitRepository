/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("clientCtl", function ($scope, $rootScope,$window, Excel, commonService, systemService) {

    $window.localStorage["currentItem"] = "client";

    var columns = [
      {
        field: "clientNo",
        template: "<a ui-sref='main.clientRead({id:dataItem.id})'>#: clientNo # </a>",
        headerTemplate: "<span translate='CLIENT_NO'></span>"
      },
      {field: "name", headerTemplate: "<span translate='NAME'></span>"},
      {field: "email", headerTemplate: "<span translate='EMAIL'></span>"},
      {field: "phone", headerTemplate: "<span translate='PHONE'></span>"},
      {field: "fax", headerTemplate: "<span translate='FAX'></span>"}
    ];
    $scope.clientGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: systemService.getGridDataSource('client')
    });

    /*****************************导出**********************************************************/
      //列名
    var columnsName = ["客户编号", "名称", "邮箱", "电话", "传真"];
    //列宽
    var cellWidths1 = [130, 150, 200, 200, 150];
    $scope.export = function () {
      Excel.exportToExcel("#clientGrid", "client", cellWidths1, columnsName);
    };
    $scope.downloadTemplate=function(){
      Excel.exportToExcelLine("#clientGrid", "client模板", cellWidths1, columnsName);
    };
    /*******************************导出end********************************************************/
  }).controller("clientCreateCtl", function ($scope, $state, systemService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.create("client", {
          "clientNo": $scope.clientNo,
          "name": $scope.name,
          "email": $scope.email,
          "phone": $scope.phone,
          "fax": $scope.fax
        }, function () {
          $state.go("main.client");
        });
      }
    }
  }).controller("clientUpdateCtl", function ($scope, $state, $stateParams, systemService) {
    systemService.read("client", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.update("client", {
          "id": $scope.id,
          "clientNo": $scope.clientNo,
          "name": $scope.name,
          "email": $scope.email,
          "phone": $scope.phone,
          "fax": $scope.fax
        }, function () {
          $state.go("main.client");
        });
      }
    }
  }).controller("clientReadCtl", function ($scope, $stateParams, systemService) {
    systemService.read("client", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  })
})();