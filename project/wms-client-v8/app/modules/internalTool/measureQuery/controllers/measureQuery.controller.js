/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("measureQueryCtl", function ($scope, $window, $rootScope, $state, commonService, internalToolService, measureQueryService) {

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

        headerTemplate: "<span translate='原尺寸长*宽*高（mm）'></span>",
        template: function (item) {
          return "<span>" + item.fromWidth + "*" + item.fromDepth + "*" + item.fromHeight + "</span>";
        }
      },
      {
        field: "fromWeight",
        headerTemplate: "<span translate='原重量（g）'></span>"
      },
      {
        headerTemplate: "<span translate='新尺寸长*宽*高（mm）'></span>",
        template: function (item) {
          return "<span>" + item.toWidth + "*" + item.toDepth + "*" + item.toHeight + "</span>";
        }
      },
      {
        field: "toWeight",
        headerTemplate: "<span translate='新重量（g）'></span>"
      },
      {
        field: "client.name",
        headerTemplate: "<span translate='CLIENT'></span>"
      },
      {
        field: "modifiedBy",
        headerTemplate: "<span translate='OPERATOR'></span>"
      },
      {
          field: "modifiedDate",
        headerTemplate: "<span translate='操作时间'></span>",
        template: function (dataItem) {
          return dataItem.modifiedDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(dataItem.modifiedDate)) : "";
        }
      }
    ];



    //查询
    function searchs() {
      measureQueryService.getMeasureQuery(function (data) {
        var measureQueryGrid = $("#measureQueryGrid").data("kendoGrid");
        data.sort(function (a, b) {
          var timeS = new Date(a.modifiedDate);
          var timeE = new Date(b.modifiedDate);
          if (timeS > timeE)
            return -1;
          else
            return 1;
        })
        measureQueryGrid.setOptions({
          dataSource: data,
          height: $(document.body).height() - 206,
          columns: columns,
          sortable: true,
          selectable: "multiple,row",
          change: function () {
            var grid = $("#measureQueryGrid").data("kendoGrid");
            var row = grid.select();
            var item = grid.dataItem(row);
            $scope.popup();
            measureQueryService.getMeasureQuery(function (data) {
              $scope.skuNo = data[0].sku;
              $scope.skuId = data[0].itemNo;
              $scope.skuName = data[0].itemDateName;
              $scope.volume = data[0].toDepth + "*" + data[0].toWidth + "*" + data[0].toHeight + "mm";
              $scope.weight = data[0].toWeight + "g";
              //measureQueryService.getSizeChangeRecordGrid(item.skuNo, function (data) {
              var validityChangeRecordGrid = $("#sizeChangeRecordGrid").data("kendoGrid");
              validityChangeRecordGrid.setOptions({
                columns: sizeChangeRecordColumns,
                dataSource: data,
                height: $("#sizeChangeRecord").height() - 42.5
              });
              //});
            }, item.itemNo);

          }
        });
      }, $scope.term, $scope.startTime, $scope.endTime);
    }


    $scope.search = function (e) {
      searchs();
    }
    $scope.searchKeyup = function (e) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        searchs();
      };
    }

    $scope.search();
    var sizeChangeRecordColumns = [{
        field: "modifiedDate",
        headerTemplate: "<span translate='修改时间'></span>",
        template: function (dataItem) {
          return dataItem.modifiedDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(dataItem.modifiedDate)) : "";
        }
      },
      {
        field: "toWidth",
        headerTemplate: "<span translate='LENGTH'></span><sapn>(mm)</sapn>"
      },
      {
        field: "toDepth",
        headerTemplate: "<span translate='WIDTH'></span><sapn>(mm)</sapn>"
      },
      {
        field: "toHeight",
        headerTemplate: "<span translate='HEIGHT'></span><sapn>(mm)</sapn>"
      },
      {
        field: "volume",
        headerTemplate: "<span translate='VOLUME'></span><sapn>(mm3)</sapn>",
        template: function (item) {
          return "<span>" + item.toWidth * item.toDepth * item.toHeight + "</span>";
        }
      },
      {
        field: "toWeight",
        headerTemplate: "<span translate='WEIGHT'></span><span>(g)</span>"
      },
      {
        field: "modifiedBy",
        headerTemplate: "<span translate='OPERATOR'></span>"
      },
      {
        headerTemplate: "<span translate='WAREHOUSE'></span>",
        template: function (item) {
          return "<span>" + item.warehouse.name + "</span>";
        }
      }
    ];

    $scope.sizeChangeRecordOptions = internalToolService.reGrids("", sizeChangeRecordColumns);

    $scope.popup = function () {
      $scope.measureQueryWindow.setOptions({
        width: $(document.body).width() * 0.95,
        height: $(document.body).height() * 0.95,
        closable: true
      });
      $scope.measureQueryWindow.center();
      $scope.measureQueryWindow.open();
    };
  });
})();