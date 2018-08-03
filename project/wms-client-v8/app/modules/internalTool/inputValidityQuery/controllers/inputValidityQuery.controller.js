/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("inputValidityQueryCtl", function ($scope, $rootScope, $state, $window, $timeout, commonService, internalToolService, inputValidityQueryService) {
    $scope.skuNo = "";
    setTimeout(function(){$("#termId").focus();},600);
    var columns = [{
        field: "sku",
        headerTemplate: "<span translate='SKU_NO'></span>"
      },
      {
        field: "itemNo",
        headerTemplate: "<span translate='SKU_ID'></span>"
      },
      {
        field: "itemDateName",
        width: 200,
        headerTemplate: "<span translate='GOODS_NAME'></span>"
      },
      {
        field: "fromStorageLocation",
        headerTemplate: "<span translate='原始容器'></span>"
      },
      {
        field: "amount",
        headerTemplate: "<span translate='数量'></span>"
      },
      {
        field: "fromUseNotAfter",
        headerTemplate: "<span translate='原到期日期'></span>"
      },
      {
        field: "toUseNotAfter",
        headerTemplate: "<span translate='新到期日期'></span>"
      },
      {
        field: "client.name",
        headerTemplate: "<span translate='客户'></span>"
      },
      {
        field: "modifiedBy",
        headerTemplate: "<span translate='操作员'></span>"
      },
      {
        field: "modifiedDate",
        headerTemplate: "<span translate='操作时间'></span>",
        template: function (dataItem) {
          return dataItem.modifiedDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(dataItem.modifiedDate)) : "";
        }
      }
    ];
    $scope.searchKeyup = function (e) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $scope.search();
      };
    }
    $scope.search = function () {
      inputValidityQueryService.getBySearchTerm(function (data) {
        var inputValidityQueryGrid = $("#inputValidityQueryGrid").data("kendoGrid");
        //按照时间排序
        data.sort(function (a,b) {
          var timeS=new Date(a.modifiedDate);
          var timeE=new Date(b.modifiedDate);
          if(timeS>timeE)
          return -1;
          else
          return 1;
        })
        inputValidityQueryGrid.setOptions({
          dataSource: data,
          height: $(document.body).height() - 206,
          columns: columns,
          sortable: true,
          selectable: "multiple,row",
          change: function () {
            var grid = $("#inputValidityQueryGrid").data("kendoGrid");
            var row = grid.select();
            var item = grid.dataItem(row);
            $scope.popup();
            inputValidityQueryService.getBySearchTerm(function (data) {
              $scope.skuNo = data[0].sku;
              $scope.skuId = data[0].itemNo;
              $scope.skuName = data[0].itemDateName;
              inputValidityQueryService.getItemData(function (data) {
                $scope.volume = data.width + "*" + data.depth + "*" + data.height + "mm";
                $scope.weight = data.weight + "g";
              }, data["0"].itemNo, data["0"].client.id, data["0"].warehouseId)

              var validityChangeRecordGrid = $("#validityChangeRecordGrid").data("kendoGrid");
              var datafilter = new kendo.data.DataSource({
                data: data
              });
              datafilter.filter({
                field: "fromStorageLocation",
                operator: "startswith",
                value: item.fromStorageLocation
              });
              validityChangeRecordGrid.setDataSource(datafilter);
              validityChangeRecordGrid.setOptions({
                columns: validityChangeRecordColumns,
                height: $("#validityChangeRecord").height() - 42.5
              });
            }, item.itemNo);
          }
        });

      }, $scope.term, $scope.startTime, $scope.endTime);
    };
    var validityChangeRecordColumns = [{
        field: "modifiedDate",
        headerTemplate: "<span translate='修改时间'></span>",
        template: function (dataItem) {
          return dataItem.modifiedDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(dataItem.modifiedDate)) : "";
        }
      },
      {
        field: "fromStorageLocation",
        headerTemplate: "<span translate='容器'></span>"
      },
      {
        field: "fromUseNotAfter",
        headerTemplate: "<span translate='原到期日期'></span>",
        template: function (dataItem) {
          return dataItem.fromUseNotAfter ? kendo.format("{0:yyyy-MM-dd}", kendo.parseDate(dataItem.fromUseNotAfter)) : "";
        }
      },
      {
        field: "toUseNotAfter",
        headerTemplate: "<span translate='新到期日期'></span>",
        template: function (dataItem) {
          return dataItem.toUseNotAfter ? kendo.format("{0:yyyy-MM-dd}", kendo.parseDate(dataItem.toUseNotAfter)) : "";
        }
      },
      {
        field: "modifiedBy",
        headerTemplate: "<span translate='操作人员'></span>"
      },
      {
        field: "recordTool",
        headerTemplate: "<span translate='使用工具'></span>"
      }
    ];
    //$scope.validityChangeRecordOptions = internalToolService.reGrids("", validityChangeRecordColumns);
    $scope.popup = function () {
      $scope.inputValidityQueryWindow.setOptions({
        width: $(document.body).width() * 0.95,
        height: $(document.body).height() * 0.98,
        closable: true
      });
      $scope.inputValidityQueryWindow.center();
      $scope.inputValidityQueryWindow.open();
    };
    $scope.search();
  });
})();