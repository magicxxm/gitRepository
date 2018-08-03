/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("jobrecordCtl", function ($scope,jobrecordService) {
    $scope.showJobInfo = false;
    $scope.showError = false;
    setTimeout(function () {
      $("#employeeCode").focus();
    },0);

      //检查用户
      $scope.keyupCheckEmployeeCode = function (e) {
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode == 13) {
              $scope.showJobInfo = false;
              jobrecordService.validitySource($scope.employeeCode, function (data) {
                  $scope.showError = false;
                  $("#jobCode").focus();
              }, errMessageFun);
          }
      };

      //错误方法
      function errMessageFun(data) {
          if (data.message=="工卡号码") {
              $scope.errMessage = data.message + $scope.employeeCode + "不存在";
              $scope.employeeCode = "";
          }
          else {
              $scope.errMessage = data.message+$scope.jobCode+"不存在";
              $scope.jobCode = "";
          }
          $scope.showError = true;
      }

    // 扫描员工卡号和间接条码
    $scope.scanEmployeeCodeAndJobCode= function (e) {
      var keyCode=window.event ? e.keyCode : e.which;
      if(keyCode != 13) return;
      $scope.showJobInfo = false;
      jobrecordService.checkEmployeeCodeAndJobCode($scope.employeeCode,$scope.jobCode,function (data) {
        $scope.errMessage = '';
        $scope.totEmployeeName = data.employeeName;
        $scope.totJobName = data.jobName;
        $scope.totClocktime = data.recordTime;
        $scope.totJobDescription = data.description;
        $scope.showError = false;
        $scope.showJobInfo = true;
        $scope.employeeCode="";
        $scope.jobCode="";
        setTimeout(function () {
              $("#employeeCode").focus();
            },0);
      }, errMessageFun);
    };
  });
})();