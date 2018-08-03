/**
 * Created by frank.zhou on 2017/05/03.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("pickingAreaCtl", function ($scope, $window, $rootScope, commonService, masterService, pickingAreaService) {
    // ===================================================pickingArea====================================================
    $window.localStorage["currentItem"] = "pickingArea";

    // 列
    var columns = [
      {field: "name", template: "<a ui-sref='main.pickingAreaRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.pickingAreaGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("pickingArea")});
    //
    $rootScope.changeClient = function(clientId) {
      $scope.currentClientId = clientId;
    };

    // =================================================pickingAreaPosition================================================
    // 函数
    function zoneEditor(container, options) {
      masterService.selectEditor(container, options, {
        serverFiltering: false,
        transport: {
          read: function (options) {
            pickingAreaService.getZone($scope.currentClientId, function (datas) {
              options.success(datas);
            });
          }
        }
      });
    }

    // pickingAreaPosition-column
    var pickingAreaPositionColumns = [
      {field: "positionNo", editor: masterService.numberEditor, headerTemplate: "<span translate='POSITION_NO'></span>"},
      {field: "zone", headerTemplate: "<span translate='ZONE'></span>", editor: zoneEditor, template: function(item){
        return item.zone? item.zone.name: "";
      }}
    ];
    $rootScope.pickingAreaPositionGridOptions = masterService.editGrid({
      height: $rootScope.mainHeight- 20- 34*3- 20- 20- 40,
      columns: pickingAreaPositionColumns
    });

  }).controller("pickingAreaCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var pickingAreaPositionGrid = $("#pickingAreaPositionGrid").data("kendoGrid"), datas = pickingAreaPositionGrid.dataSource.data();
        for(var i = 0, details = []; i < datas.length; i++){
          var data = datas[i];
          details.push({
            "positionNo": data.positionNo,
            "zoneId": data.zone? data.zone.id: null,
            "clientId": data.zone.clientId
          });
        }
        masterService.create("pickingArea", {
          "name": $scope.name,
          "description": $scope.description,
          "clientId": $scope.client? $scope.client.id: null,
          "positions": details
        }, function () {
          $state.go("main.picking_area");
        });
      }
    };
  }).controller("pickingAreaUpdateCtl", function ($scope, $rootScope, $state, $stateParams, masterService) {
    masterService.read("pickingArea", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      $scope.client = {id: $scope.clientId};
      $rootScope.changeClient($scope.clientId);
      var grid = $("#pickingAreaPositionGrid").data("kendoGrid");
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });

    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var pickingAreaPositionGrid = $("#pickingAreaPositionGrid").data("kendoGrid"), datas = pickingAreaPositionGrid.dataSource.data();
        for(var i = 0, details = []; i < datas.length; i++){
          var data = datas[i];
          details.push({
            "id": data.id || null,
            "positionNo": data.positionNo,
            "zoneId": data.zone? data.zone.id: null,
            "clientId": data.zone.client.id
          });
        }
        masterService.update("pickingArea", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "clientId": $scope.client? $scope.client.id: null,
          "positions": details
        }, function () {
          $state.go("main.picking_area");
        });
      }
    };
  }).controller("pickingAreaReadCtl", function ($scope, $stateParams, masterService) {
    masterService.read("pickingArea", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      var grid = $("#pickingAreaPositionGrid").data("kendoGrid");
      grid.setOptions({"editable": false});
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
  });
})();