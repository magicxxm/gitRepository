/**
 * Created by frank.zhou on 2017/05/05.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebinStationTypeCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "rebinStationType";

    $rootScope.rebinWallTypeSource = masterService.getDataSource({key: "getRebinWallType", text: "name", value: "id"});

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.rebinStationTypeRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "rebinWallType.name",
        template: "<a ui-sref='main.rebin_wall_type'>#: rebinWallType.name # </a>",
        headerTemplate: "<span translate='REBIN_WALL_TYPE'></span>"
      },
      {
        width: 400, headerTemplate: "<span translate='REBIN_STATION'></span>", template: function (item) {
        var stations = item.rebinStations;
        for (var i = 0, datas = []; i < stations.length; i = i + 2) {
          var station = stations[i], next = stations[i + 1];
          var htmlStr = "<div style='margin:1px;'>";
          station && (htmlStr += "<div class='gridCellList'>" + station.name + "</div>");
          next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>" + next.name + "</div>");
          htmlStr += "</div>";
          datas.push(htmlStr);
        }
        return datas.join("");
      }
      },
      {
        width: 400, headerTemplate: "<span translate='REBIN_WALL'></span>", template: function (item) {
        var walls = item.rebinWalls;
        for (var i = 0, datas = []; i < walls.length; i = i + 2) {
          var wall = walls[i], next = walls[i + 1];
          var htmlStr = "<div style='margin:1px;'>";
          wall && (htmlStr += "<div class='gridCellList'>" + wall.name + "</div>");
          next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>" + next.name + "</div>");
          htmlStr += "</div>";
          datas.push(htmlStr);
        }
        return datas.join("");
      }
      },
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.rebinStationTypeGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("rebinStationType")
    });

  }).controller("rebinStationTypeCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("rebinStationType", {
          "name": $scope.name,
          "rebinWallTypeId": $scope.rebinWallType ? $scope.rebinWallType.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.rebin_station_type");
        });
      }
    };
  }).controller("rebinStationTypeUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("rebinStationType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("rebinStationType", {
          "id": $scope.id,
          "name": $scope.name,
          "rebinWallTypeId": $scope.rebinWallType ? $scope.rebinWallType.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.rebin_station_type");
        });
      }
    };
  }).controller("rebinStationTypeReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("rebinStationType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();