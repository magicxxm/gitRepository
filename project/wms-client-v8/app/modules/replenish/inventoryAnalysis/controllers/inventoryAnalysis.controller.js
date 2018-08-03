/**
 * Created by frank.zhou on 2017/04/25.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("inventoryAnalysisCtl", function ($scope, $rootScope, $window, $timeout, commonService,
                                                                       replenishService,inventoryAnalysisService) {
    $window.localStorage["currentItem"] = "inventoryAnalysis";

    //列
    var columns = [
      {field: "itemData.itemNo", headerTemplate: "<span translate='ITEM_NO'></span>"},
      {field: "itemData.skuNo", headerTemplate: "<span translate='SKU_NO'></span>"},
      {field: "itemData.name", headerTemplate: "<span translate='GOODS_NAME'></span>"},
      {field: "clientId", width: 120, headerTemplate: "<span translate='CLIENT'></span>"},
      {field: "level",  headerTemplate: "<span translate='GOODS_LEVEL'></span>"},
      {field: "onpodAmount", width: 190, headerTemplate: "<span translate='ONPOD_INVENTORY'></span>"},
      {field: "availableAmount", width: 200, headerTemplate: "<span translate='AVAILABLE_INVENTORY'></span>"},
      {field: "bufferfudAmount", width: 180, headerTemplate: "<span translate='BUFFER_ZONE_FUD'></span>"},
      {field: "maxDoc", headerTemplate: "<span translate='Max DOC'></span>"},
      {field: "replenishDoc", headerTemplate: "<span translate='Replen DOC'></span>"},
      {field: "safetyDoc", headerTemplate: "<span translate='Safety DOC'></span>"}
    ];
    $scope.inventoryAnalysisGridOptions = commonService.gridMushiny({
        columns: columns, dataSource: replenishService.getGridDataSource("inventoryAnalysis")});

      $timeout(function() {
          $("#startDaypicker").kendoDatePicker({
              start: "year",
              depth: "month",
              format: "MM-dd"
          });
          $("#startTimepicker").kendoTimePicker({
              format: "HH"
          });
          $("#endDaypicker").kendoDatePicker({
              start: "year",
              depth: "month",
              format: "MM-dd"
          });
          $("#endTimepicker").kendoTimePicker({
              format: "HH"
          });

          inventoryAnalysisService.getTimeConfig(function (data) {
              $scope.id = data["id"];
              if (data["interval"]!=0) {
                  $scope.interval = data["interval"];
                  $scope.startDay = data["startDate"];
                  $scope.endDay = data["endDate"];
                  if (!(data["startTime"] ==0 && data["endTime"]==0)) {
                      $scope.startTime = data["startTime"];
                      $scope.endTime = data["endTime"];
                  }
                  $scope.message = "补货计算中"
              }
              else {
                  $scope.message = "停止补货"
              }
          });
      });

      $scope.start = function () {
          $scope.param={interval:$scope.interval,
                        startDate:$scope.startDay,
                        startTime:$scope.startTime,
                        endDate:$scope.endDay,
                        endTime:$scope.endTime,
                        id:$scope.id}
          console.log("参数-->",$scope.param);
          inventoryAnalysisService.updateTimeDetail(function (data) {
              console.info(data);
              inventoryAnalysisService.startCron(function (data) {
                  console.log("start success")
                  $scope.message = "补货计算中"
              },$scope.param);
          },$scope.param);

      }

      $scope.stop = function () {
          inventoryAnalysisService.updateStopTime(function (data) {
              console.info(data);
              inventoryAnalysisService.stopCron(function (data) {
                  console.log("stop success")
                  $scope.message = "停止补货"
              });
          },$scope.id);

      }
  })
    .controller("inventoryAnalysisReadCtl", function ($scope, $stateParams, replenishService) {
        replenishService.read("inventoryAnalysis", $stateParams.id, function(data){
          for(var k in data) $scope[k] = data[k];
        });
    });
})();