/**
 * Created by feiyu.pan on 2017/4/24.
 * Updated by feiyu.pan on 2017/5/31
 */
(function () {
  'use strict';

  angular.module('myApp').controller('shipmentDetailCtl', function ($scope,$stateParams,shipmentDetailService,receiving_commonService) {
    $scope.CustomerShipmentId=$stateParams.shipment;//获取shipmentId
    if($stateParams.shipment!=undefined){
        $scope.CustomerShipmentId=$stateParams.shipment;
    }
    //搜索
    $scope.search=function () {
      if($scope.CustomerShipmentId!==""&&$scope.CustomerShipmentId!==undefined&&$scope.CustomerShipmentId!==null) {
        shipmentDetailService.getShipmentInformation($scope.CustomerShipmentId,function (data) {
          $scope.shipmentId=data.smallShipmentDTOS.shipmentId;
          $scope.exSD=data.smallShipmentDTOS.exSD;
          $scope.shipmentCreated=data.smallShipmentDTOS.createTime;
          $scope.shipmentCondition=data.smallShipmentDTOS.condition;
          $scope.batchId=data.smallShipmentDTOS.batchId;
          $scope.batchCreateTime=data.smallShipmentDTOS.batchCreateTime;
          $scope.backSortCode = data.smallShipmentDTOS.sortCode;
          var goodsDetailGrid=$("#goodsDetailGrid").data("kendoGrid");
          goodsDetailGrid.setDataSource(new kendo.data.DataSource({data:data.itemDTOList}));
          var grid=$("#shipmentStateGrid").data("kendoGrid");
          grid.setDataSource(new kendo.data.DataSource({data:data.shipmentStateDTOS}));
        });
      }
    };
    $scope.search();
    var goodsDetailColumns = [
      {field: "skuId", headerTemplate: "<span translate='SKU ID'></span>"},
      {field: "skuNo", headerTemplate: "<span translate='SKU NO.'></span>"},
      {field: "quantity", headerTemplate: "<span translate='Quantity'></span>"},
      {field: "location", headerTemplate: "<span translate='Location'></span>"},
      {field: "title", headerTemplate: "<span translate='Title'></span>",attributes:{style:"text-align:left"}}];
    $scope.goodsDetailGridOptions = {dataSource: "", columns: goodsDetailColumns,scrollable:false};
    var shipmentStateColumns = [
      {field: "time", headerTemplate: "<span  translate='时间'></span>",template:function (item) {
        return kendo.format("{0:yyyy/MM/dd HH:mm:ss}",kendo.parseDate(item.time));
      }},
      {headerTemplate: "<span translate='状态'></span>", columns:[
        {field:"state",headerTemplate:"<sp  an style='display:none;'></span>",attributes:{style:"text-align:left"}},
        {field:"stateNum",headerTemplate:"<span style='display:none;'></span>"}]},
      {field: "station", headerTemplate: "<span translate='站台'></span>",attributes:{style:"text-align:left"}},
      {field: "operator", headerTemplate: "<span translate='操作人员'></span>"}];
    $scope.shipmentStateGridOptions = {dataSource: "", columns: shipmentStateColumns,scrollable:false,dataBound:function () {
      $("#shipmentStateGrid th[data-index=2]").css("padding","0");
        $("#shipmentStateGrid th[data-index=2]").css("border","0");
        $("#shipmentStateGrid th[data-index=1]").css("border","0");
        $("#shipmentStateGrid th[data-index=1]").css("padding","0");
    }};
  })
})();