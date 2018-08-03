(function () {
  'use strict';

  angular.module('myApp').controller("transferStockInDifDetailsCtl", function ($scope,$window,$state,$translate,$timeout,transferStockInService) {
    $scope.transferDisplayDataClick=false;
    $scope.startDate='';
    $scope.endDate='';
    $scope.warehouseId=$window.localStorage["warehouseId"];
    $scope.clientId=$window.localStorage["clientId"];
    $scope.transferStockInDifDatas=[];
    $scope.transferStockInDifColumn= [{
      field: "transferExternDn",
      title: $translate.instant("transferStockIn_transferExternDn"),//外部调拨单号
      width: 100
    },
      {
        field: "transferStocInOrderDn",
        title: $translate.instant("transferStockIn_transferStocInOrderDn"),//调拨发货单号
        width: 100
      }
      ,
      {
        field: "transferStocInContainer",
        title: $translate.instant("transferStockIn_transferStocInContainer"),//容器号码
        width: 100
      }
      ,
      {
        field: "transferStocInSkuId",
        title: $translate.instant("transferStockIn_transferStocInSkuId"),//SKU ID
        width: 100
      }
      ,
      {
        field: "transferStocInGoodsName",
        title: $translate.instant("transferStockIn_transferStocInGoodsName"),//商品名称
        width: 100
      } ,
      {
        field: "transferStocInAmount",
        title: $translate.instant("transferStockIn_transferStocInAmount"),//数量
        width: 100
      } ,
      {
        field: "transferStocInType",
        title: $translate.instant("transferStockIn_transferStocInType"),//类型
        width: 100
      } ,
      {
        field: "transferStocInReporter",
        title: $translate.instant("transferStockIn_transferStocInReporter"),//上报人员
        width: 100
      } ,
      {
        field: "transferStocInHandler",
        title: $translate.instant("transferStockIn_transferStocInHandler"),//处理人员
        width: 100
      }
      ]

      $timeout(function () {
          $scope.initSearchParam();

          $("#startDatetimepicker").kendoDatePicker({
              format: "yyyy-MM-dd"

          });
          $("#endDatetimepicker").kendoDatePicker({
              format: "yyyy-MM-dd"
          });
          $scope.initGrid();

          $scope.submitStatistics();

      });
      //sumbit按钮事件
      $scope.submitStatistics = function () {
          $scope.initSearchParam();
        transferStockInService.getTransferInDiffDetail(function(data){
          $scope.transferStockInDifDatas=data;
          $('.transferDisplay').data("kendoGrid").setDataSource(new kendo.data.DataSource({
            data: $scope.transferStockInDifDatas
          }))



        },$scope.searchParam)


      };
      $scope.initGrid=function()
      {

        $(".transferDisplay").kendoGrid({
          allowCopy: true,
          columns: $scope.transferStockInDifColumn,
          dataSource:$scope.transferStockInDatas,
          height:200,
          selectable: "row",
          scrollable: true,
          sortable: true,
        });

      }




      $scope.initSearchParam=function()
      {
          $scope.searchParam={
              startDate:$scope.startDate,
              endDate:$scope.endDate,
              searchText:$scope.searchText,
              clientId:$scope.clientId,
              warehouseId:$scope.warehouseId}

      }



  });
})();