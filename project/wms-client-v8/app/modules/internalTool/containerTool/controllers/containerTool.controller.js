/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("containerToolCtl", function ($scope, $rootScope, $state, commonService, containerToolService, BACKEND_CONFIG, internalToolService,$translate) {

    $scope.totalAmount = 0;
    focusContainer();

    // ===================================================================================================================================
    var inventoryColumns = [{
        field: "storageLocationName",
        width: 100,
        headerTemplate: "<span translate='容器'></span>"
      },
      {
        field: "sku",
        width: 100,
        headerTemplate: "<span translate='SKU_NO'></span>"
      },
      {
        field: "itemNo",
        width: 100,
        headerTemplate: "<span translate='SKU_ID'></span>"
      },
      {
        field: "amount",
        width: 60,
        headerTemplate: "<span translate='COUNT'></span><span>({{totalAmount}})</span>"
      },
      {
        field: "inventoryState",
        width: 60,
        headerTemplate: "<span translate='STATE'></span>"
      },
      {
        field: "shipmentNo",
        width: 100,
        headerTemplate: "<span translate='SHIPMENT_NO'></span>"
      },
      {
        field: "itemUnitName",
        width: 60,
        headerTemplate: "<span translate='类型'></span>"
      },
      {
        field: "clientName",
        width: 60,
        headerTemplate: "<span translate='CLIENT'></span>"
      },
      {
        field: "itemDataName",
        width: 250,
        headerTemplate: "<span translate='GOODS_NAME'></span>",
        attributes: {
          style: "text-align:left"
        }
      }
    ];

    $scope.inventoryRecordsGridOptions = internalToolService.reGrids("", inventoryColumns, $(document.body).height() - 213);

    $scope.inventoryContainers = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $scope.inventorySearch();
      }
    };

    $scope.inventorySearch = function () {
      //
      containerToolService.getStorageRecords($scope.storageLocationName, function (data) {
        $scope.totalAmount = 0;
        for (var i = 0; i < data.length; i++) {
          $scope.totalAmount += data[i].amount;
        }
        var inventoryRecords = $("#inventoryRecordsGrid").data("kendoGrid");
        inventoryRecords.setOptions({
          dataSource: data,
          columns: inventoryColumns,
          selectable: BACKEND_CONFIG.selectModel
        });
      }, function (data) {
        $scope.totalAmount = 0;
        var inventoryRecords = $("#inventoryRecordsGrid").data("kendoGrid");
        inventoryRecords.setOptions({
          dataSource: [],
          columns: inventoryColumns
        });
          var win = $("#mushinyWindow").data("kendoWindow");
          commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
              setTimeout(function(){
                  $("#warnContent").html($translate.instant(data.key));
              }, 200);
              win.bind("close", function(){ focusContainer(); });
          }});
      });
      //
      var grid = $("#historyRecordsGrid").data("kendoGrid");
      grid.dataSource.filter({
        storageLocation: $scope.storageLocationName
      });
      $scope.setOptions(grid);
    };

    function focusContainer(){
        $scope.storageLocationName="";
        setTimeout(function(){ $("#containerId").focus();}, 600);
    }

    $scope.search = function () {
      var grid = $("#historyRecordsGrid").data("kendoGrid"),
        data = {
          storageLocation: $scope.storageLocationName
        };
      $scope.createdDate && (data["createdDate"] = $scope.createdDate);
      $scope.itemNo && (data["itemNo"] = $scope.itemNo);
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

    $scope.setOptions = function (grid) {
      grid.setOptions({
        dataSource: {
          transport: {
            read: function (options) {
              var sort = options.data.sort || [];
              sort && sort.length && (sort = sort[0].field + "," + sort[0].dir);
              // 过滤
              var filters = options.data.filter || {},
                data = filters.filters[0];
              data["page"] = options.data.page - 1;
              data["size"] = options.data.pageSize;
              data["sort"] = sort;
              //
              commonService.ajaxSync({
                url: "internal-tool/search-inventory/storage-historical-records",
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

    // ===================================================================================================================================
    var historyColumns = [{
        field: "createdDate",
        width: 100,
        headerTemplate: "<span translate='时间'></span>",
        template: function (dataItem) {
          return dataItem.createdDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(dataItem.createdDate)) : "";
        }
      },
      {
        field: "recordCode",
        width: 50,
        headerTemplate: "<span translate='操作代码'></span>"
      },
      {
        field: "itemNo",
        width: 120,
        headerTemplate: "<span translate='SKU_ID'></span>"
      },
      {
        field: "amount",
        width: 60,
        headerTemplate: "<span translate='COUNT'></span>"
      },
      {
        field: "operator",
        width: 100,
        headerTemplate: "<span translate='用户'></span>"
      },
      {
        field: "fromStorageLocation",
        width: 150,
        headerTemplate: "<span translate='原始容器'></span>"
      },
      {
        field: "toStorageLocation",
        width: 100,
        headerTemplate: "<span translate='目的容器'></span>"
      },
      {
        field: "recordType",
        width: 200,
        headerTemplate: "<span translate='操作'></span>"
      },
      {
        field: "recordTool",
        width: 100,
        headerTemplate: "<span translate='使用工具'></span>"
      }
    ];

    $scope.historyRecordsGridOptions = {
      height: $(document.body).height() - 265,
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

    // ===================================================================================================================================
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
      activate: function () {
        var grid = $("#historyRecordsGrid").data("kendoGrid");
        grid.setOptions({
          height: $(document.body).height() - 305
        });
      }
    };
  });
})();