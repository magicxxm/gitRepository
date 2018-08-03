/**
 * Created by frank.zhou on 2017/01/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("customerShipmentCtl", function ($scope, $window, $rootScope, commonService, outboundService) {
    // ===================================================customerShipment====================================================
    $window.localStorage["currentItem"] = "customerShipment";

    // 列
    var columns = [
      {field: "shipmentNo", template: "<a ui-sref='main.customerShipmentRead({id:dataItem.id})'>#: shipmentNo # </a>", width: 200, headerTemplate: "<span translate='SHIPMENT_NO'></span>"},
      {field: "customerOrder", headerTemplate: "<span translate='ORDER_NO'></span>", template: function(item){
        return item.customerOrder? item.customerOrder.orderNo: "";
      }},
      {field: "sortCode", headerTemplate: "<span translate='SORT_CODE'></span>"},
      {field: "deliveryDate", headerTemplate: "<span translate='DELIVERY_DATE'></span>", template: function(dataItem){
        return dataItem.deliveryDate?  kendo.format("{0:yyyy-MM-dd HH:mm:ss}",kendo.parseDate(dataItem.deliveryDate)): "";
      }},
      {field: "customerName", headerTemplate: "<span translate='CUSTOMER_NAME'></span>"},
      {field: "customerNo", headerTemplate: "<span translate='CUSTOMER_NO'></span>"},
      {field: "priority", headerTemplate: "<span translate='PRIORITY'></span>"},
      {field: "state", headerTemplate: "<span translate='STATE'></span>"},
      {field: "boxType.name", headerTemplate: "<span translate='BOX_TYPE'></span>", template: function(item){
        return item.boxType? item.boxType.name: "";
      }},
      {field: "orderStrategy.name", headerTemplate: "<span translate='ORDER_STRATEGY'></span>"}
    ];
    $scope.customerShipmentGridOptions = commonService.gridMushiny({dataSource:outboundService.getGridDataSource("customerShipment"), columns:columns});

    // =================================================customerShipmentPosition================================================
    var customerShipmentPositionColumns = [
      {field: "positionNo", width: 60, headerTemplate: "<span translate='POSITION_NO'></span>"},
      {field: "itemData", width: 200, headerTemplate: "<span translate='ITEM_DATA'></span>", template: function(item){
        return item.itemData? item.itemData.name: "";
      }},
      {field: "amount", width: 60, headerTemplate: "<span translate='AMOUNT'></span>"},
      {field: "amountPicked", width: 60, headerTemplate: "<span translate='AMOUNT_PICKED'></span>"},
      {field: "orderIndex", width: 60, headerTemplate: "<span translate='ORDER_INDEX'></span>"},
      {field: "partitionAllowed", width: 60, headerTemplate: "<span translate='PARTITION_ALLOWED'></span>"},
      {field: "serialNo", width: 80, headerTemplate: "<span translate='SERIAL_NO'></span>"},
      {field: "state", width: 70, headerTemplate: "<span translate='STATE'></span>"}
    ];
    $rootScope.customerShipmentPositionGridOptions = outboundService.editGrid({
      height: Math.max(300, $rootScope.mainHeight- 20- 34*11- 15- 20- 20- 40),
      columns: customerShipmentPositionColumns
    });

  }).controller("customerShipmentReadCtl", function ($scope, $stateParams, outboundService) {
    outboundService.read("customerShipment", $stateParams.id, function(data){
      console.log(data);
      for(var k in data) $scope[k] = data[k];
      var grid = $("#customerShipmentPositionGRID").data("kendoGrid");
      grid.setOptions({"editable": false});
      grid.setDataSource(new kendo.data.DataSource({data: data.positionsDTO}));
    });
  });
})();