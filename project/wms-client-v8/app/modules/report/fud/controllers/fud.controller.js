/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("fudCtl", function ($scope, $window, commonService, fudService, reportService) {

    //
    var legacyColumns = [{
        field: "skuNo",
        width: 120,
        headerTemplate: "<span translate='SKU_NO'></span>"
      },
      {
        field: "skuId",
        width: 120,
        headerTemplate: "<span translate='SKU_ID'></span>"
      },
      {
        field: "clientName",
        width: 100,
        headerTemplate: "<span translate='CLIENT'></span>"
      },
      {
        field: "state",
        width: 60,
        headerTemplate: "<span translate='STATE'></span>"
      },
      {
        field: "amount",
        width: 60,
        headerTemplate: "<span translate='数量'></span>"
      },
      {
        field: "containerName",
        width: 100,
        headerTemplate: "<span translate='容器号码'></span>"
      },
      {
        field: "activityCode",
        width: 150,
        headerTemplate: "<span translate='容器最后操作'></span>"
      },
      {
        field: "modifiedDate",
        width: 130,
        headerTemplate: "<span translate='商品进入容器时间'></span>",
        template: function (item) {
          return item.modifiedDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.modifiedDate)) : "";
        }
      },
      {
        field: "totalTime",
        width: 100,
        headerTemplate: "<span translate='累计时长(日)'></span>"
      },
      {
        field: "skuName",
        width: 250,
        headerTemplate: "<span translate='商品名称'></span>"
      }
    ];
 
    $scope.getLegacy = function () {
      fudService.getLegacyData(function (data) {
        $scope.legacyDataGridOptions = reportService.reGrids(data, legacyColumns, $(document.body).height() - 192);
      });
    }

    //
    var fudColumns = [{
        field: "skuNo",
        width: 150,
        headerTemplate: "<span translate='SKU_NO'></span>"
      },
      {
        field: "skuId",
        width: 150,
        headerTemplate: "<span translate='SKU_ID'></span>"
      },
      {
        field: "clientName",
        width: 100,
        headerTemplate: "<span translate='CLIENT'></span>"
      },
      {
        field: "state",
        width: 60,
        headerTemplate: "<span translate='STATE'></span>"
      },
      {
        field: "amount",
        width: 60,
        headerTemplate: "<span translate='数量'></span>"
      },
      {
        field: "containerName",
        width: 100,
        headerTemplate: "<span translate='容器号码'></span>"
      },
      {
        field: "activityCode",
        width: 100,
        headerTemplate: "<span translate='容器最后操作'></span>"
      },
      {
        field: "modifiedDate",
        width: 130,
        headerTemplate: "<span translate='商品进入容器时间'></span>",
        template: function (item) {
          return item.modifiedDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.modifiedDate)) : "";
        }
      },
      {
        field: "totalTime",
        width: 100,
        headerTemplate: "<span translate='累计时长(日)'></span>"
      },
      {
        field: "overTime",
        width: 120,
        headerTemplate: "<span translate='影响客户订单时长'></span>"
      },
      {
        field: "skuName",
        width: 250,
        headerTemplate: "<span translate='商品名称'></span>"
      }
    ];

    $scope.getLegacyFud = function () {
      fudService.getFud(function (data) {
        $scope.fudGridOptions = reportService.reGrids(data, fudColumns, $(document.body).height() - 192);
      });
    }

    //
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
      }
    };

    $scope.getLegacy();
    $scope.getLegacyFud();

  });
})();