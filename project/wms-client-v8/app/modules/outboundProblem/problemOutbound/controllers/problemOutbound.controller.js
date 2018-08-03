/**
 * Created by thoma.bian on 2017/5/10.
 * Updated by frank.zhou on 2017/05/15.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("problemOutboundCtl", function ($window,$scope, $state, $rootScope, problemOutboundService) {
    $scope.outboundProblem = 'workStationPage';
    $scope.workingStation = false;
    $scope.problemCart = false;
    // 扫描工作站
    $scope.workStation = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode != 13) return;
      problemOutboundService.getOutboundProblemStation($scope.workstation, function(data){
        //console.log(data);
        $rootScope.obpStationId = data.obpStation.id;
        $rootScope.workStationId=data.obpStation.workStation.id;
        $rootScope.obpStationName = data.obpStation.name;
        $rootScope.sectionId=data.obpStation.workStation.sectionId;
        //保存工作站
        $window.localStorage["obpWorkstationName"] = $scope.workstation;
        $scope.outboundProblem = 'problemCarPage';
        setTimeout(function(){ $("#obp_wall").focus();}, 0);
      }, function(data){
          $scope.obProblemStation=$scope.workstation;
          $scope.workstation="";
       if (data.key == 'EX_OBPROBLEM_STATION_HAS_USED'){
           $scope.errorMessage="工作站被占用";
       }else{
           $scope.errorMessage="不是一个有效工作站";
       }
        $scope.workingStation = true;
      });
    };

    // 扫描问题处理车
    $scope.problemHandingCarts = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode != 13) return;
      problemOutboundService.getOutboundProblemHandingCar($scope.problemHandingCar, function(data){
        $rootScope.obpWallId = data.id;
        $rootScope.obpWallName = data.name;
        //保存车牌
        $window.localStorage["obProblemHandingCar"] = $scope.problemHandingCar;
        $state.go("main.problemOutboundShipment");
      }, function(){
        $scope.obProblemWall=$scope.problemHandingCar;
        $scope.problemHandingCar="";
        $scope.problemCart = true;
      });
    };

    // 初始化
    setTimeout(function(){ $("#obp_station").focus();}, 0);
  });
})();