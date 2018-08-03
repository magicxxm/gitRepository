/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("obpStationTypeCtl", function ($scope, $rootScope, $window, $translate, $state, commonService, problemOutboundBaseService) {
    // ===================================================stationType====================================================
    $window.localStorage["currentItem"] = "obpStationType";
    $rootScope.typeSource = ["问题处理", "问题核实"];
    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.obpStationTypeRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {field: "type", headerTemplate: "<span>类型</span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.obpStationTypeGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: problemOutboundBaseService.getGridDataSource("obpStationType")
    });

    // =================================================stationTypePosition===============================================
    // 函数
    function stateEditor(container, options) {
      var source = problemOutboundBaseService.getDataSource({
        key: "getSelectionBySelectionKey", value: "selectionValue", text: "resourceKey",
        data: {selectionKey: "INVENTORY_STATE"}
      });
      problemOutboundBaseService.selectEditor(container, options, source);
    }

    // stationTypePosition-column
    var stationTypePositionColumns = [
      {
        field: "positionIndex",
        editor: problemOutboundBaseService.numberEditor,
        headerTemplate: "<span translate='POSITION_INDEX'></span>"
      },
      {
        field: "positionState",
        editor: stateEditor,
        headerTemplate: "<span translate='POSITION_STATE'></span>",
        template: function (item) {
          return item.positionState ? (typeof item.positionState === "string" ? $translate.instant(item.positionState) : item.positionState.resourceKey) : "";
        }
      }
    ];
    $rootScope.stationTypePositionGridOptions = problemOutboundBaseService.editGrid({
      height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 2 - 10 - 20 - 20 - 40),
      columns: stationTypePositionColumns
    });

  }).controller("obpStationTypeCreateCtl", function ($scope, $state, problemOutboundBaseService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var stationTypePositionGrid = $("#stationTypePositionGrid").data("kendoGrid"), datas = stationTypePositionGrid.dataSource.data();
        for (var i = 0, details = []; i < datas.length; i++) {
          var data = datas[i];
          details.push({
            "positionIndex": data.positionIndex,
            "positionState": data.positionState ? data.positionState.selectionValue : ""
          });
        }
        problemOutboundBaseService.create("obpStationType", {
          "name": $scope.name,
          "type": $scope.type,
          "description": $scope.description,
          "positions": details
        }, function () {
          $state.go("main.obp_station_type");
        });
      }
    };
  }).controller("obpStationTypeUpdateCtl", function ($scope, $state, $stateParams, problemOutboundBaseService) {
    problemOutboundBaseService.read("obpStationType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      var grid = $("#stationTypePositionGrid").data("kendoGrid");
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var stationTypePositionGrid = $("#stationTypePositionGrid").data("kendoGrid"), datas = stationTypePositionGrid.dataSource.data();
        for (var i = 0, details = []; i < datas.length; i++) {
          var data = datas[i];
          details.push({
            "id": data.id || null,
            "positionIndex": data.positionIndex,
            "positionState": data.positionState ? data.positionState.selectionValue : ""
          });
        }
        problemOutboundBaseService.update("obpStationType", {
          "id": $scope.id,
          "name": $scope.name,
          "type": $scope.type,
          "description": $scope.description,
          "positions": details
        }, function () {
          $state.go("main.obp_station_type");
        });
      }
    };
  }).controller("obpStationTypeReadCtl", function ($scope, $state, $stateParams, problemOutboundBaseService) {
    problemOutboundBaseService.read("obpStationType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      var grid = $("#stationTypePositionGrid").data("kendoGrid");
      grid.setOptions({"editable": false});
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
  });
})();