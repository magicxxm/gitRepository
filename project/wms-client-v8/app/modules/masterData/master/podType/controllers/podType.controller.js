/**
 * Created by frank.zhou on 2017/04/24.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("podTypeCtl", function ($scope, $rootScope, $window, $translate, commonService, masterService) {
    // ===================================================podType====================================================
    $window.localStorage["currentItem"] = "podType";
    $rootScope.i;
    $rootScope.j;
    // 列
    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.podTypeRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {field: "numberOfRows", headerTemplate: "<span translate='NUMBER_OF_ROWS'></span>"},
      {field: "height", headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      {field: "width", headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "depth", headerTemplate: "<span translate='DEPTH'></span><span>(mm)</span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.podTypeGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("podType")
    });

    // =================================================podTypePosition===============================================
    // podTypePosition--函数
    function binTypeEditor(container, options) {
      masterService.selectEditor(container, options, masterService.getDataSource({
        key: "getStorageLocationType",
        text: "name",
        value: "id"
      }));
    }

    function dropZoneEditor(container, options) {
      masterService.selectEditor(container, options, masterService.getDataSource({
        key: "getDropZone",
        text: "name",
        value: "id"
      }));
    }

    function faceEditor(container, options) {
      var source = masterService.getDataSource({
        key: "getSelectionBySelectionKey",
        text: "resourceKey",
        value: "selectionValue",
        data: {selectionKey: "FACE"}
      });
      masterService.selectEditor(container, options, source);
    }

    // bayTypePosition--体
    var podTypePositionColumns = [
      {
        field: "positionNo",
        width: 120,
        headerTemplate: "<span translate='POSITION_NO'></span>",
        template: function (item) {
          $rootScope.i = $rootScope.i + 1;
          return item.positionNo?item.positionNo:$rootScope.i - 1 === $rootScope.j ? 1 : $rootScope.i;
        }
      },
      {
        field: "storageLocationType",
        width: 150,
        editor: binTypeEditor,
        headerTemplate: "<span translate='STORAGE_LOCATION_TYPE'></span>",
        template: function (item) {
          return item.storageLocationType ? item.storageLocationType.name : "";
        }
      },
      {
        field: "level",
        width: 60,
        editor: masterService.numberEditor,
        headerTemplate: "<span translate='LEVEL'></span>"
      },
      {
        field: "numberOfColumns",
        width: 60,
        editor: masterService.numberEditor,
        headerTemplate: "<span translate='NUMBER_OF_COLUMNS'></span>"
      },
      {
        field: "dropZone",
        width: 120,
        editor: dropZoneEditor,
        headerTemplate: "<span translate='DROP_ZONE'></span>",
        template: function (item) {
          return item.dropZone ? item.dropZone.name : "";
        }
      },
      {
        field: "face",
        width: 100,
        editor: faceEditor,
        headerTemplate: "<span translate='FACE'></span>",
        template: function (item) {
          return item.face ? (item.face.resourceKey || $translate.instant(item.face)) : "";
        }
      },
      {field: "color", width: 100, headerTemplate: "<span translate='COLOR'></span>"}];

    $rootScope.podTypePositionGridOptions = masterService.editGrid({
      height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 6 - 15 - 20 - 20 - 40),
      columns: podTypePositionColumns
    });

  }).controller("podTypeCreateCtl", function ($scope, $rootScope, $state, masterService) {
    var row = 0;
    // 新增明细
    $scope.createDetail = function () {
      $rootScope.i = 0;
      var grid = $("#podTypePositionGrid").data("kendoGrid");
      for (var i = 0; i < $scope.numberOfRows; i++) {
        if ($scope.numberOfRows > row) {
          $rootScope.j=$scope.numberOfRows;
          $rootScope.i = 0;
          grid.addRow();
          row++;
        }
      }
      ;
    };
    // 删除明细
    $scope.deleteDetail = function () {
      var grid = $("#podTypePositionGrid").data("kendoGrid");
      if (row > 0) {
        $rootScope.i = 0;
        grid.removeRow(grid.select());
        row--;
      }
    };
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var podTypePositionGrid = $("#podTypePositionGrid").data("kendoGrid"), datas = podTypePositionGrid.dataSource.data();
        for (var i = 0, details = []; i < datas.length; i++) {
          var data = datas[i];
          details.push({
            "positionNo": i + 1,
            "storageLocationTypeId": data.storageLocationType ? data.storageLocationType.id : null,
            "level": data.level,
            "numberOfColumns": data.numberOfColumns,
            "dropZoneId": data.dropZone ? data.dropZone.id : null,
            "face": data.face ? data.face.selectionValue : null,
            "color": data.color
          });
        }
        masterService.create("podType", {
          "name": $scope.name,
          "description": $scope.description,
          "numberOfRows": $scope.numberOfRows,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "positions": details
        }, function () {
          $state.go("main.pod_type");
        });
      }
    };
  }).controller("podTypeUpdateCtl", function ($scope, $state, $translate, $stateParams, masterService) {
    masterService.read("podType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      var podTypePositionGrid = $("#podTypePositionGrid").data("kendoGrid");
      podTypePositionGrid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var podTypePositionGrid = $("#podTypePositionGrid").data("kendoGrid"), datas = podTypePositionGrid.dataSource.data();
        for (var i = 0, details = []; i < datas.length; i++) {
          var data = datas[i];
          details.push({
            "id": data.id || null,
            "positionNo": i + 1,
            "storageLocationTypeId": data.storageLocationType ? data.storageLocationType.id : null,
            "level": data.level,
            "numberOfColumns": data.numberOfColumns,
            "dropZoneId": data.dropZone ? data.dropZone.id : null,
            "face": data.face ? (data.face.selectionValue || $translate.instant(data.face)) : null,
            "color": data.color
          });
        }
        masterService.update("podType", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "numberOfRows": $scope.numberOfRows,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "positions": details
        }, function () {
          $state.go("main.pod_type");
        });
      }
    };
  }).controller("podTypeReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("podType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      var podTypePositionGrid = $("#podTypePositionGrid").data("kendoGrid");
      podTypePositionGrid.setOptions({"editable": false});
      podTypePositionGrid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
  });
})();