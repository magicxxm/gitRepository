/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("attendanceCtl", function ($scope,attendanceService) {
    $scope.showAttInfo_gb = false;
    $scope.showAttInfo_lb = false;
    $scope.showError = false;
    setTimeout(function () {
      $("#employeeCode").focus();
    },0);
    // 扫描员工卡号
    $scope.scanEmployeeCode= function (e) {
      var keyCode=window.event ? e.keyCode : e.which;
      if(keyCode != 13) return;
      $scope.showAttInfo_gb = false;
      $scope.showAttInfo_lb = false;
      $scope.showError = false;
      attendanceService.checkEmployeeCode($scope.employeeCode,function (data) {
            $scope.totEmployeeName = data.employeeName;
            $scope.totClocktime = data.clockTime;
            if('CLOCK_IN' == data.clockType){
            $scope.totClockType = '上班';
             $scope.showAttInfo_gb = true;
             $scope.showAttInfo_lb = false;
            }else if('CLOCK_OFF' == data.clockType){
            $scope.totClockType = '下班';
            $scope.showAttInfo_lb = true;
            $scope.showAttInfo_gb = false;
            }else{}
        $scope.employeeCode="";
      }, errMessageFun);
    };

      //错误方法
      function errMessageFun(data) {
          $scope.errMessage = data.message+$scope.employeeCode+"不存在";
          $scope.employeeCode="";
          $scope.showError = true;
      }
  });
})();