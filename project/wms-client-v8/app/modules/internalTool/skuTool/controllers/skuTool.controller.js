/**
 * Created by PC-7 on 2017/3/1.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("skuToolCtl", function ($scope, $rootScope, $state, commonService,
                                                             skuToolService,BACKEND_CONFIG,internalToolService) {

    $scope.name = "";
    $scope.itemUnitName = "";
    $scope.itemGroupName = "";
    $scope.skuNo = "";
    $scope.description = "";
    $scope.description = "";
    $scope.size = "";
    $scope.weight = "";
    $scope.page = 0;
    $scope.sizes = 50;
    $scope.purchaseTotalNum = 0;
    $scope.inventoryTotalNum = 0;

    setTimeout(function(){ $("#skuId").focus();}, 600);

    $scope.goodsSku = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $scope.goodsSKUSearch();
      }
    };
    $scope.goodsSKUSearch = function () {
      $scope.name = "";
      $scope.itemUnitName = "";
      $scope.itemGroupName = "";
      $scope.skuNo = "";
      $scope.description = "";
      $scope.description = "";
      $scope.size = "";
      $scope.weight = "";
      $scope.itemNub = "";
      //查询商品信息
      skuToolService.getByItemDataNo($scope.itemNo, function (v) {
        $scope.itemNub = v.itemNo;
        $scope.name = v.name;
        $scope.itemUnitName = v.itemUnit.name;
        $scope.itemGroupName = v.itemGroup.name;
        $scope.skuNo = v.skuNo;
        $scope.description = v.description;
        $scope.description = v.description;
        $scope.size = v.width + "*" + v.depth + "*" + v.height;
        $scope.weight = v.weight;
      },function (data) {
          $scope.itemNo = "";
          $scope.itemMsg = data.values[0];
          $scope.openWindow({
              windowId: "scanItem",
              windowClass: "myWindow",
              windowName: $scope.scanItemErrorWindows,
              width: 500,
              height: 160,
              closeable: true,
              visible: true,
          })
        var grid = $("#purchaseOrderGrid").data("kendoGrid");
        grid.setDataSource(new kendo.data.DataSource({data: []}));
        var inventoryRecordsGrid = $("#inventoryRecordsGrid").data("kendoGrid");
        inventoryRecordsGrid.setOptions({columns: inventoryColumns, dataSource: []});
        var adjustRecordsGrid = $("#adjustRecordsGrid").data("kendoGrid");
        adjustRecordsGrid.setDataSource(new kendo.data.DataSource({data: []}));
      });
      //查询采购订单
      skuToolService.getItemPurchasingRecords($scope.itemNo, function (data) {
        var grid = $("#purchaseOrderGrid").data("kendoGrid");
        grid.setOptions({ dataSource: data,selectable: BACKEND_CONFIG.selectModel});
      });
      //查询库存记录
      skuToolService.getItemRecords($scope.itemNo, function (data) {
        $scope.inventoryTotalNum = 0;
        for (var i = 0; i < data.length; i++) {
          $scope.inventoryTotalNum += data[i].amount;
        }
        data.sort(function (a,b) {
          if(a.storageLocationName.toUpperCase().indexOf("PC")!=-1) return 1;
          if(b.storageLocationName.toUpperCase().indexOf("PC")!=-1) return -1;
          if(a.storageLocationName.toUpperCase().indexOf("PACKED")!=-1)return 1;
          if(b.storageLocationName.toUpperCase().indexOf("PACKED")!=-1)return -1;
          return  a.storageLocationName  < b.storageLocationName ? -1 : 1;
        });
        var grid = $("#inventoryRecordsGrid").data("kendoGrid");
        grid.setOptions({columns: inventoryColumns, dataSource: data,selectable: BACKEND_CONFIG.selectModel});
      });
      //查询调整记录
      skuToolService.getItemAdjustRecords($scope.itemNo, function (data) {
        var grid = $("#adjustRecordsGrid").data("kendoGrid");
        console.log(data.content)
        grid.setOptions({ dataSource: data.content,selectable: BACKEND_CONFIG.selectModel});
      });
      var grid = $("#historyRecordsGrid").data("kendoGrid");
      grid.dataSource.filter({itemNo: $scope.itemNo});
      $scope.setOptions(grid);
    };

    $scope.search = function () {
      var grid = $("#historyRecordsGrid").data("kendoGrid"), data = {itemNo: $scope.itemNo};
      $scope.createdDate && (data["createdDate"] = $scope.createdDate);
      $scope.username && (data["username"] = $scope.username);
      $scope.fromStorageLocation && (data["fromStorageLocation"] = $scope.fromStorageLocation);
      $scope.toStorageLocation && (data["toStorageLocation"] = $scope.toStorageLocation);
      $scope.recordCode && (data["recordCode"] = $scope.recordCode);
      $scope.recordTool && (data["recordTool"] = $scope.recordTool);
       $scope.recordType && (data["recordType"] = $scope.recordType);
      grid.dataSource.filter(data);
      grid.dataSource.sort();
      $scope.setOptions(grid);
    };

    var purchaseColumns = [
      {field: "skuNo", headerTemplate: "<span translate=' SKU_NO'></span>"},
      {field: "itemNo", headerTemplate: "<span translate=' SKU_ID'></span>"},
      //{field: "itemData.skuNo",headerTemplate: "<span translate='MS SKU Barcode'></span>"},
      {field: "dn", headerTemplate: "<span translate='采购单号'></span>"},
      {field: "amount", headerTemplate: "<span translate='COUNT'></span>"},
      {field: "clientName", headerTemplate: "<span translate='CLIENT'></span>"},
      {field: "expectedDelivery", headerTemplate: "<span translate='收货时间'></span>"}];

    $scope.purchaseOrderGridOptions = internalToolService.reGrids("", purchaseColumns, $(document.body).height() - 213);


    var inventoryColumns = [
      {field: "storageLocationName", headerTemplate: "<span translate='容器'></span>"},
      {field: "sku", headerTemplate: "<span translate='SKU_NO'></span>"},
      {field: "itemNo", headerTemplate: "<span translate='SKU_ID'></span>"},
      // {field: "",headerTemplate: "<span translate='SMS SKU Barcode'></span>"},
      {field: "amount", headerTemplate: "<span translate='COUNT'></span><span>({{inventoryTotalNum}})</span>"},
      {field: "inventoryState", headerTemplate: "<span translate='STATE'></span>"},
      {field: "shipmentNo", headerTemplate: "<span translate='SHIPMENT_NO'></span>"},
      {field: "clientName", headerTemplate: "<span translate='CLIENT'></span>"},
      {field: "useNotAfter", headerTemplate: "<span translate='到期日期'></span>"}];

    $scope.inventoryRecordsGridOptions = internalToolService.reGrids("", inventoryColumns, $(document.body).height() - 213);


    var adjustColumns = [
      {field: "createdDate", headerTemplate: "<span translate='时间'></span>", template: function (dataItem) {
          return dataItem.createdDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(dataItem.createdDate)) : "";
        }
      },
      {field: "recordCode", headerTemplate: "<span translate='操作代码'></span>"},
      {field: "itemNo", headerTemplate: "<span translate='SKU_ID'></span>"},
      {field: "amount", headerTemplate: "<span translate='COUNT'></span>"},
      {field: "operator", headerTemplate: "<span translate='操作人'></span>"},
      {field: "fromStorageLocation", headerTemplate: "<span translate='原始容器'></span>"},
      {field: "toStorageLocation", headerTemplate: "<span translate='目的容器'></span>"},
      {field: "client.name", headerTemplate: "<span translate='CLIENT'></span>"},
      {field: "recordType", headerTemplate: "<span translate='操作'></span>"},
      {field: "recordTool", headerTemplate:"<span translate='使用工具'></span>"}];

    $scope.adjustRecordsGridOptions = internalToolService.reGrids("", adjustColumns, $(document.body).height() - 213);


    var historyColumns = [
      {field: "createdDate", width: 150, headerTemplate: "<span translate='时间'></span>", template: function (dataItem) {
          return dataItem.createdDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(dataItem.createdDate)) : "";
        }
      },
      {field: "recordCode", width: 50, headerTemplate: "<span translate='操作代码'></span>"},
      {field: "itemNo", width: 100, headerTemplate: "<span translate='SKU_ID'></span>"},
      {field: "amount", width: 50, headerTemplate: "<span translate='COUNT'></span>"},
      {field: "operator", width: 100, headerTemplate: "<span translate='用户'></span>"},
      {field: "fromStorageLocation", width: 150, headerTemplate: "<span translate='原始容器'></span>"},
      {field: "toStorageLocation", width: 150, headerTemplate: "<span translate='目的容器'></span>"},
      {field: "recordType",width: 200, headerTemplate: "<span translate='操作'></span>"},
      {field: "recordTool",width: 100, headerTemplate:"<span translate='使用工具'></span>"}];

    //查询历史记录
    $scope.setOptions = function (grid) {
      grid.setOptions({
        dataSource: {
          transport: {
            read: function (options) {
              var sort = options.data.sort || [];
              sort && sort.length && (sort = sort[0].field + "," + sort[0].dir);
              // 过滤
              var filters = options.data.filter || {}, data = filters.filters[0];
              data["page"] = options.data.page - 1;
              data["size"] = options.data.pageSize;
              data["sort"] = sort;
              //
              commonService.ajaxSync({
                url: "internal-tool/search-inventory/item-historical-records",
                async: true,
                data: data,
                success: function (result) {
                  options.success(result);
                }
              });
            }
          },
          schema: {
            data: function (response) {
              return response.content;
            },
            total: function (response) {
              return response.totalElements;
            }
          },
          serverFiltering: true,
          serverPaging: true,
          serverSorting: true
        }
      });
    };

    $scope.historyRecordsGridOptions = {
      height: $(document.body).height() - 275,
      columns: historyColumns,
      scrollable: true,
      sortable: true,
      pageable: {
        pageSize: 50,
        pageSizes: [50, 100, 200],
        previousNext: true,
        numeric: true,
        input: false,
        info: true
      }
    };
    $scope.tabOpation = {
      animation: {
        close: {
          duration: 100,
          effects: "fadeOut"
        },
        // fade-in new tab over 500 milliseconds
        open: {
          duration: 100,
          effects: "fadeIn"
        }
      },
      activate:function(){
        var grid = $("#historyRecordsGrid").data("kendoGrid");
        grid.setOptions({height:$(document.body).height() - 305});
      }
    };

      //打开窗口
    $scope.openWindow = function (options) {
      $("#" + options.windowId).parent().addClass(options.windowClass);
        options.windowName.setOptions({
            width: options.width,
            height: options.height,
            closeable: options.closeable,
            visible: true,
            title: options.title,
        });
        options.windowName.center();
        options.windowName.open();
    };
  });
})();
