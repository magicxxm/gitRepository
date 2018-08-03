/**
 * Created by frank.zhou on 2017/05/09.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("sortingStationTypeCtl", function ($scope, $rootScope, $translate, $window, commonService, masterService) {
    // ===================================================stationType====================================================
    $window.localStorage["currentItem"] = "sortingStationType";

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.sortingStationTypeRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        width: 500, headerTemplate: "<span translate='SORTING_STATION'></span>", template: function (item) {
        var sortStations = item.sortStations;
        for (var i = 0, datas = []; i < sortStations.length; i = i + 2) {
          var station = sortStations[i], next = sortStations[i + 1];
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
    $scope.sortingStationTypeGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("sortingStationType")
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

  }).controller("sortingStationTypeCreateCtl", function ($scope, $state, masterService) {
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
        masterService.create("sortingStationType", {
          "name": $scope.name,
          "description": $scope.description,
          "positions": details
        }, function () {
          $state.go("main.sorting_station_type");
        });
      }
    };
  }).controller("sortingStationTypeUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("sortingStationType", $stateParams.id, function (data) {
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
            "positionState": data.positionState ? data.positionState.selectionValue :""
          });
        }
        masterService.update("sortingStationType", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "positions": details
        }, function () {
          $state.go("main.sorting_station_type");
        });
      }
    };
  }).controller("sortingStationTypeReadCtl", function ($scope, $stateParams, masterService) {
    masterService.read("sortingStationType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      var grid = $("#stationTypePositionGrid").data("kendoGrid");
      grid.setOptions({"editable": false});
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
  });
})();