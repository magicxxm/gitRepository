(function () {
  'use strict';
  angular.module('myApp').controller("totStatisticsCtl", function ($scope,$state,$timeout, $window, commonService, totStatisticsService,totService,totStatisticsDetailService) {

      $scope.timeType=1;
      $scope.dayDate=totStatisticsDetailService.format('yyyy-MM-dd',new Date());
      $scope.startDate='';
      $scope.endDate='';
      $scope.employeeName='';
      $scope.employeeCode='';
      $scope.warehouseId='';
      $scope.clientId='';

      $scope.turnPage = function(dataItem) {
          totStatisticsDetailService.employeeCode=dataItem.employeeCode;
          totStatisticsDetailService.employeeName=dataItem.employeeName
          totStatisticsDetailService.warehouseId=$scope.warehouseId
          totStatisticsDetailService.clientId=$scope.clientId
          totStatisticsDetailService.dayDate=$scope.dayDate;
          totStatisticsDetailService.timeType=$scope.timeType;
          totStatisticsDetailService.startDate=$scope.startDate;
          totStatisticsDetailService.endDate=$scope.endDate;
          $state.go("main.totStatistics_ctimedetail.totClockdetail");
      }
      function statisticColumns() {
          var staColumn = [];
          staColumn.push({
              field: "employeeCode", headerTemplate: "<span translate='EMPLOYEE_CODE'></span>"
          });

          staColumn.push({
              field: "employeeName",
              template: "<a style='cursor:pointer' ng-click='turnPage(dataItem)'>#: employeeName # </a>",
              headerTemplate: "<span translate='TOT_EMPLOYEE_NAME'></span>"
          });
          staColumn.push({
              field: "ecWorkTime", headerTemplate: "<span translate='EC_WORKTIME'></span>"
          });
          staColumn.push({
              field: "totalClockTime", headerTemplate: "<span translate='TOTAL_CLOCKTIME'></span>"
          });
          staColumn.push({
              field: "ecWorkRate", headerTemplate: "<span translate='EC_WORKRATE'></span>"
          });

          return staColumn;
      }

      $timeout(function () {
          $('#dayDatetimepicker').data('kendoDatePicker').bind("close", function(e) {
              $scope.initDateParam();
          })
          $("#startDatetimepicker").kendoDateTimePicker({
              format: "yyyy-MM-dd HH:mm:ss",
              close:function()
              {
                  $scope.initDateParam();
              }
          });
          $("#endDatetimepicker").kendoDateTimePicker({
              format: "yyyy-MM-dd HH:mm:ss",
              close:function()
              {
                  $scope.initDateParam();
              }
          });
          $scope.initGrid();
          totStatisticsService.getWarehouse(function (data1) {
              $scope.warehouseId = data1[0];
              totStatisticsService.getClientByCurrentWarehouse(function (data2) {
                  $scope.clientId = data2[0];
                  $scope.submitStatistics();
              })
          });
      });
      //sumbit按钮事件
      $scope.submitStatistics = function () {
          $scope.warehouseId=currentWarehouse.value();
          $scope.clientId=currentClient.value();
          $scope.initSearchParam();
          console.info($scope.searchParam);
          totStatisticsService.getTotStatisticsData(function (data) {
              var totStatisticsDataGridId = $("#totStatisticsDataGrid").data("kendoGrid");
              totStatisticsDataGridId.setOptions({
                  dataSource: data,
                  columns: statisticColumns()
              });
          },$scope.searchParam);
      };
      $scope.initGrid=function()
      {
          $scope.totStatisticsDataGridOptions = totService.reGrids([], statisticColumns(), $(document.body).height() - 192);
      }

      //  $(document).ready(function() {
      $("#selectWarehouse").kendoComboBox({
          dataSource: totService.getDataSource({key: "getWarehouse"}),
          dataTextField: "name",
          dataValueField: "id",
          filter: "contains",
          suggest: true,
          index: 0
      });

      $("#selectClientInfos").kendoComboBox({
          dataSource: totService.getDataSource({key: "getClientByCurrentWarehouse"}),
          dataTextField: "name",
          dataValueField: "id",
          filter: "contains",
          suggest: true,
          index: 0
      });
      var currentWarehouse =$("#selectWarehouse").data("kendoComboBox")
      var currentClient = $("#selectClientInfos").data("kendoComboBox")
      // });

      $scope.dayDatecheck = function () {
          if($scope.timeType==1)
          {
              $scope.startDate='';
              $scope.endDate='';
              totStatisticsDetailService.startDate='';
              totStatisticsDetailService.endDate='';
              totStatisticsDetailService.timeType=1
          }else{
              $scope.dayDate='';
              totStatisticsDetailService.dayDate='';
              totStatisticsDetailService.timeType=2

          }

      }
      $scope.initDateParam=function()
      {
          if($scope.timeType==1)
          {
              totStatisticsDetailService.dayDate= $scope.dayDate;
              totStatisticsDetailService.startDate='';
              totStatisticsDetailService.endDate='';
          }else{
              totStatisticsDetailService.dayDate='';
              totStatisticsDetailService.startDate=$scope.startDate;
              totStatisticsDetailService.endDate=$scope.endDate;
          }
      }

      $scope.initSearchParam=function()
      {
          $scope.searchParam={userName:$scope.employeeCode,
              startDate:$scope.startDate,
              endDate:$scope.endDate,
              dayDate:$scope.dayDate,
              clientId:$scope.clientId,
              warehouseId:$scope.warehouseId}
      }
  });
})();