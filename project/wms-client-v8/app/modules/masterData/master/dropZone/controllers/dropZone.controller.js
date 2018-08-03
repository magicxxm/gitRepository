/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("dropZoneCtl", function ($scope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "dropZone";

    var columns = [
      {field: "name", template: "<a ui-sref='main.dropZoneRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "standardTime", headerTemplate: "<span translate='STANDARD_TIME'></span><span>(s)</span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.dropZoneGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("dropZone")});

  }).controller("dropZoneCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("dropZone", {
          "name": $scope.name,
          "standardTime": $scope.standardTime,
          "description" : $scope.description
        }, function () {
          $state.go("main.drop_zone");
        });
      }
    };
  }).controller("dropZoneUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("dropZone", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("dropZone", {
          "id": $scope.id,
          "name": $scope.name,
          "standardTime": $scope.standardTime,
          "description" : $scope.description
        }, function () {
          $state.go("main.drop_zone");
        });
      }
    };
  }).controller("dropZoneReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("dropZone", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();