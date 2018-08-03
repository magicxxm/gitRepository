/**
 * Created by frank.zhou on 2017/04/25.
 * Updated by frank.zhou on 2017/05/02.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("receiveStationTypeCtl", function ($scope, $rootScope, $window, $translate, commonService, masterService) {
    // ===================================================stationType ====================================================
    $window.localStorage["currentItem"] = "receiveStationType";
    $rootScope.receiveStationTypeSource = ["Each Receive To Stow", "Pallet Receive To Stow", "Each Receive To Tote","pallet Receive","Each Receive"];

    // 列
    var columns = [
      {
        field: "name",
        width: 300,
        template: "<a ui-sref='main.receiveStationTypeRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        width: 500, headerTemplate: "<span translate='RECEIVE_STATION'></span>", template: function (item) {
        var receiveStations = item.receiveStations;
        for (var i = 0, datas = []; i < receiveStations.length; i = i + 2) {
          var station = receiveStations[i], next = receiveStations[i + 1];
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

    $scope.receiveStationTypeGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("receiveStationType")
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

    function receivingDestinationEditor(container, options) {
      var source = masterService.getDataSource({key: "getReceiveDestination", text: "name", value: "id"});
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
      },
      {
        field: "receiveDestination",
        headerTemplate: "<span translate='RECEIVE_DESTINATION'></span>",
        editor: receivingDestinationEditor,
        template: function (item) {
          return item.receiveDestination ? item.receiveDestination.name : "";
        }
      }
    ];

    $rootScope.stationTypePositionGridOptions = masterService.editGrid({
      height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 2 - 10 - 20 - 20 - 40),
      columns: stationTypePositionColumns
    });

  }).controller("receiveStationTypeCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();//方法阻止元素发生默认的行为
      if ($scope.validator.validate()) {
        var stationTypePositionGrid = $("#stationTypePositionGrid").data("kendoGrid"),
          datas = stationTypePositionGrid.dataSource.data();
        for (var i = 0, details = []; i < datas.length; i++) {
          var data = datas[i];
          details.push({
            "positionIndex": data.positionIndex,
            "positionState": data.positionState ? data.positionState.selectionValue : null,
            "receiveDestinationId": data.receiveDestination ? data.receiveDestination.id : null
          });
        }
        console.log("create", {
          "name": $scope.name,
          "receiveStationType": $scope.receiveStationType,
          "description": $scope.description,
          "positions": details
        });
        masterService.create("receiveStationType", {
          "name": $scope.name,
          "receiveStationType": $scope.receiveStationType,
          "description": $scope.description,
          "positions": details
        }, function () {
          $state.go("main.receive_station_type");
        });
      }
    };
  }).controller("receiveStationTypeUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("receiveStationType", $stateParams.id, function (data) {
      // console.log(data);
      for (var k in data) $scope[k] = data[k];
      var grid = $("#stationTypePositionGrid").data("kendoGrid");
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });

    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var stationTypePositionGrid = $("#stationTypePositionGrid").data("kendoGrid"),
          datas = stationTypePositionGrid.dataSource.data();
        for (var i = 0, details = []; i < datas.length; i++) {
          var data = datas[i];
          details.push({
            "id": data.id || null,
            "positionIndex": data.positionIndex,
            "positionState":  data.positionState.selectionValue ? data.positionState.selectionValue : data.positionState,
            "receiveDestinationId": data.receiveDestination ? data.receiveDestination.id : null
          });
        }
        masterService.update("receiveStationType", {
          "id": $scope.id,
          "name": $scope.name,
          "receiveStationType": $scope.receiveStationType,
          "description": $scope.description,
          "positions": details
        }, function () {
          $state.go("main.receive_station_type");
        });
      }
    };
  }).controller("receiveStationTypeReadCtl", function ($scope, $state, $stateParams, masterService) {

    masterService.read("receiveStationType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      var grid = $("#stationTypePositionGrid").data("kendoGrid");
      grid.setOptions({"editable": false});
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
  });
})();