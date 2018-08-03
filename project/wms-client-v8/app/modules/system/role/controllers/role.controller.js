/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  angular.module('myApp').controller("roleCtl", function ($scope, $rootScope, $window, Excel, commonService, systemService) {

    $window.localStorage["currentItem"] = "role";

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.roleRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.roleGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: systemService.getGridDataSource("role")
    });
    /************************导出*******************************************************/
      //列名
    var columnsName = ["名称", "描述"];
    //列宽
    var cellWidths1 = [200, 300];
    $scope.export = function () {
      Excel.exportToExcel("#roleGrid", "role", cellWidths1, columnsName);
    };
    $scope.downloadTemplate=function(){
      Excel.exportToExcelLine("#roleGrid", "角色模板", cellWidths1, columnsName);
    };
    /************************导出end*******************************************************/
  }).controller("roleCreateCtl", function ($scope, $state, systemService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.create("role", {
          "name": $scope.name,
          "description": $scope.description
        }, function () {
          $state.go("main.role");
        });
      }
    }

  }).controller("roleUpdateCtl", function ($scope, $state, $stateParams, systemService) {
    systemService.read("role", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.update("role", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description
        }, function () {
          $state.go("main.role");
        });
      }
    }
  }).controller("roleReadCtl", function ($scope, $stateParams, systemService) {
    systemService.read("role", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  })
})();