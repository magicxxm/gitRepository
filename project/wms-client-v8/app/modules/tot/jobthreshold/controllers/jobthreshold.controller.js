/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("jobthresholdCtl", function ($scope, $rootScope, $window, $state, commonService, totService) {
    $window.localStorage["currentItem"] = "jobthreshold";
   // $rootScope.obpCellTypeSource = totService.getDataSource({key: "getObpCellType", text: "name", value: "id"});
   // $rootScope.obpWallSource = totService.getDataSource({key: "getObpWall", text: "name", value: "id"});
   
    var columns = [
      {field: "thresholdA",headerTemplate: "<span translate='J_A'></span>"},
      {field: "thresholdB",headerTemplate: "<span translate='J_B'></span>"},
    ];
    $scope.jobthresholdGridOptions = commonService.gridMushiny({columns: columns, dataSource: totService.getGridDataSource("jobthreshold")});

  }).controller("jobthresholdUpdateCtl", function ($scope, $state, $stateParams, totService){

    totService.read("jobthreshold", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });

    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.update("jobthreshold", {
          "id": $scope.id,
          "thresholdA": $scope.thresholdA,
          "thresholdB": $scope.thresholdB,
        }, function () {
          $state.go("main.tot_jobthreshold");
        });
      }
    };

  });
})();