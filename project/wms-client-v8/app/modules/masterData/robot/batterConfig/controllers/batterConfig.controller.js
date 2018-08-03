/**
 * Created by bian on 2016/9/27.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("batterConfigCtl", function ($scope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "batterConfig";

    var columns = [
      {field: "name", template: "<a ui-sref='main.batterConfigRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "startNumber", template: "<span>#: startNumber*100 #</span>%~<span>#: endNumber*100 #</span>%", headerTemplate: "<span translate='电量表示范围'></span>"}
     ];
    $scope.batterConfigGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("batterConfig")});

  }).controller("batterConfigCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("batterConfig", {
          "name": $scope.name,
          "startNumber": $scope.startNumber>1?1:$scope.startNumber,
          "endNumber": $scope.endNumber>1?1:$scope.endNumber
        }, function () {
           $state.go("main.batter_config");
        });
      }
    };
  }).controller("batterConfigUpdateCtl", function ($scope,$stateParams, $state, masterService){
    masterService.read("batterConfig", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("batterConfig", {
            "id": $scope.id,
            "name": $scope.name,
            "startNumber": $scope.startNumber>1?1:$scope.startNumber,
            "endNumber": $scope.endNumber>1?1:$scope.endNumber
          }, function () {
          $state.go("main.batter_config");
        });
      }
    };
  }).controller("batterConfigReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("batterConfig", $stateParams.id, function(data){
      $("#startEndNum").val(data.startNumber*100+"%~"+data.endNumber*100+"%");
      for(var k in data) $scope[k] = data[k];
    });
  });
})();