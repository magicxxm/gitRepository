/**
 * Created by frank.zhou on 2017/04/25.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("goodsReceiptCtl", function ($scope, $window, commonService, masterService, goodsReceiptService) {

    $window.localStorage["currentItem"] = "goodsReceipt";
    var columns = [
      {field: "grNo", headerTemplate: "<span translate='GR_NO'></span>"},
      {
        field: "relatedAdvice.adviceNo",
        template: "<a ui-sref='main.goodsReceiptRead({id:dataItem.id})'>#: relatedAdvice.adviceNo # </a>",
        headerTemplate: "<span translate='ADVICE_NO'></span>"
      },
      {
        field: "receiptDate",
        template: function (dataItem) {
          return dataItem.receiptDate.substring(0, 10) +" " +dataItem.receiptDate.substring(11, 20);
        },
        headerTemplate: "<span translate='RECEIPT_DATE'></span>"
      },
      {field: "receiptState", headerTemplate: "<span translate='RECEIPT_STATE'></span>"},
      {
        field: "size",
        template: function (dataItem) {
          return dataItem.size + "/" + dataItem.relatedAdvice.size;
        },
        headerTemplate: "<span translate='RECEIPT_CASE'></span>"
      }
    ];
    $scope.goodsReceiptGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("goodsReceipt")
    });
    //关闭dn
    $scope.closedDN = function () {
      var grid = $("#goodsReceiptGrid").data("kendoGrid");
      var rows = grid.select();
      if (rows.length) {
        var rowData = grid.dataItem(rows[0]);
        goodsReceiptService.closedDn(rowData.id, function () {
          grid.dataSource.read();
        });
      }
    };
    //打开dn
    $scope.openDN = function () {
      var grid = $("#goodsReceiptGrid").data("kendoGrid");
      var rows = grid.select();
      if (rows.length) {
        var rowData = grid.dataItem(rows[0]);
        goodsReceiptService.openDn(rowData.id, function () {
          grid.dataSource.read();
        });
      }
    };
  }).controller("goodsReceiptActivateCtl", function ($scope, $state, goodsReceiptService) {
    $scope.doActivate = function () {
      goodsReceiptService.activateDN($scope.adviceNo, function () {
        $state.go("main.goods_receipt");
      });
    };
  }).controller("goodsReceiptReadCtl", function ($scope, $state, $stateParams, masterService) {
    var columns = [
      {field: "itemData.itemNo", width: 60, headerTemplate: "<span translate='ITEM_NO'></span>"},
      {field: "itemData.name", width: 100, headerTemplate: "<span translate='ITEM_DATA'></span>"},
      {field: "receiptAmount", width: 70, headerTemplate: "<span translate='RECEIPT_AMOUNT'></span>"},
      {field: "notifiedAmount", width: 70, headerTemplate: "<span translate='NOTIFIED_AMOUNT'></span>"},
      {field: "notifiedAmount",
        template:function (dataItem) {
          return dataItem.notifiedAmount-dataItem.receiptAmount;
        },
        width: 60, headerTemplate: "<span>差值</span>"},
    ];
    $scope.stockUnitGridOptions = {
      selectable: "row",
      sortable: true,
      scrollable: true,
      pageable: false,
      editable: false,
      columns: columns
    };
    var columns = [
      {field: "createdDate",
        template: function (dataItem) {
          return dataItem.createdDate.substring(0, 10) +" " +dataItem.createdDate.substring(11, 20);
        },
        width: 70, headerTemplate: "<span translate='RECEIPT_DATE'></span>"},
      {field: "stockUnit.itemData.itemNo", width: 60, headerTemplate: "<span translate='ITEM_NO'></span>"},
      {field: "stockUnit.itemData.name", width: 120, headerTemplate: "<span translate='ITEM_DATA'></span>"},
      {field: "amount", width: 30, headerTemplate: "<span translate='AMOUNT'></span>"},
      {field: "operator.username", width: 60, headerTemplate: "<span translate='OPERATOR'></span>"},
      {field: "storageLocation.name", width: 70, headerTemplate: "<span>Pod</span>"},
      {field: "unitLoad.stationName", width: 50, headerTemplate: "<span translate='WORKSTATION'></span>"}
    ];

    $scope.goodsReceiptPositionGridOptions = {
      selectable: "row",
      sortable: true,
      scrollable: true,
      pageable: false,
      editable: false,
      columns: columns
    };
    masterService.read("goodsReceipt", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      var grid = $("#goodsReceiptPositionGrid").data("kendoGrid");
      grid.setDataSource(new kendo.data.DataSource({data: data.positions,sort: {field: "createdDate", dir: "asc"}}));
      var grid1 = $("#stockUnitGRID").data("kendoGrid");
      grid1.setDataSource(new kendo.data.DataSource({data: data.relatedAdvice.positions}));
    });

  });
})();