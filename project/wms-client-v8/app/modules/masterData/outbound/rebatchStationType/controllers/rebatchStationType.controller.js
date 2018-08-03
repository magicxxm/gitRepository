/**
 * Created by frank.zhou on 2017/05/09.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebatchStationTypeCtl", function ($scope, $rootScope, $translate, $window, commonService, masterService) {
    // ===================================================stationType====================================================
    $window.localStorage["currentItem"] = "rebatchStationType";

    var columns = [
      {
        field: "name",
        width: 300,
        template: "<a ui-sref='main.rebatchStationTypeRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        width: 500, headerTemplate: "<span translate='REBATCH_STATION'></span>", template: function (item) {
        var rebatchStations = item.rebatchStations;
        for (var i = 0, datas = []; i < rebatchStations.length; i = i + 2) {
          var station = rebatchStations[i], next = rebatchStations[i + 1];
          var htmlStr = "<div style='margin:1px;'>";
          station && (htmlStr += "<div class='gridCellList'>" + station.name + "</div>");
          next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>" + next.name + "</div>");
          htmlStr += "</div>";
          datas.push(htmlStr);
        }
        return datas.join("");
      }
      },
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.rebatchStationTypeGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("rebatchStationType")
    });

    // =================================================stationTypePosition===============================================
    // 函数
    function stateEditor(container, options) {
      var source = masterService.getDataSource({
        key: "getSelectionBySelectionKey", value: "selectionValue", text: "resourceKey",
        data: {selectionKey: "INVENTORY_STATE"}
      });
      masterService.selectEditor(container, options, source);
    }

    // stationTypePosition-column
    var stationTypePositionColumns = [
      {
        field: "positionIndex",
        editor: masterService.numberEditor,
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
    $rootScope.stationTypePositionGridOptions = masterService.editGrid({
      height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 2 - 10 - 20 - 20 - 40),
      columns: stationTypePositionColumns
    });

  }).controller("rebatchStationTypeCreateCtl", function ($scope, $state, masterService) {
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
        masterService.create("rebatchStationType", {
          "name": $scope.name,
          "description": $scope.description,
          "positions": details
        }, function () {
          $state.go("main.rebatch_station_type");
        });
      }
    };
  }).controller("rebatchStationTypeUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("rebatchStationType", $stateParams.id, function (data) {
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
            "positionState": data.positionState.selectionValue ? data.positionState.selectionValue : data.positionState
          });
        }
        masterService.update("rebatchStationType", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "positions": details
        }, function () {
          $state.go("main.rebatch_station_type");
        });
      }
    };
  }).controller("rebatchStationTypeReadCtl", function ($scope, $stateParams, masterService) {
    masterService.read("rebatchStationType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      var grid = $("#stationTypePositionGrid").data("kendoGrid");
      grid.setOptions({"editable": false});
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
  });
})();