/**
 * Created by frank.zhou on 2017/04/20.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("transferCtl", function ($rootScope,$scope,$window,$state,$stateParams,$http,transOutService,commonService) {
    console.log("base.js");
    $rootScope.create = function () {
      var currentItem = $window.localStorage["currentItem"];
      if (currentItem == null) return;
      $state.go("main." + currentItem + "Create");
    };
    $rootScope.update = function () {
      var currentItem = $window.localStorage["currentItem"];
      console.log("current"+currentItem);
      if (currentItem == null) return;
      var grid = $("#" + currentItem + "Grid").data(currentItem === "" ? "kendoTreeList" : "kendoGrid");
      var rows = grid.select();
      if (rows.length) {
        var rowData = grid.dataItem(rows[0]);
        $state.go("main." + currentItem + "Update", {id: rowData.id});
      }
    };
    $rootScope.delete = function () {
      var currentItem = $window.localStorage["currentItem"];
      if (currentItem == null) return;
      var grid = $("#" + currentItem + "Grid").data(currentItem === "transferStockOutConfig" ? "kendoGrid":"kendoTreeList");
      var rows = grid.select();
      if (rows.length) {
        commonService.dialogMushiny($scope.window, {
          open: function (win) {
            $rootScope.deleteSure = function () {
              var rowData = grid.dataItem(rows[0]);
                transOutService.DeleteTransOutConfig(rowData.id,function () {
                    grid.dataSource.read(); // 刷新表格
                    win.close();
                });
            };
          }
        });
      }
    };

  });
})();