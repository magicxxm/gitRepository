/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("moveToolCtl", function ($scope, $rootScope,$timeout, $window, $state, commonService, internalToolService, moveToolService) {
    $scope._goodsContent = false;
    $scope.goodsMoveCarts = false;
    $scope.isfirst = true;
    $scope.moveArray = [];
    $scope.moveModel = 1;

    $scope.moveModelclick = function (model) {
      $scope.moveModel = model;
      $scope.destinationContainerName = '';
      $scope.orgContainerName = "";
      clear();
      $scope.goodsMoveCarts = false;
      var arr = Object.keys($scope);
      $scope.isfirst = true;
      arr.map(
        function (data) {
          if (data.indexOf("Show") != -1) {
            if ($scope[data] == true) {
              $scope[data] = false;
            }
          }
        }
      );
    };
    //扫描商品事件
    $scope.scanningGoods = function (e) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        moveToolService.moveScanningItemData($scope.sourceId, $scope.scanningGoodsName, function (data) {
          $scope.errMessage = '';
          $scope.goodsMoveCarts = false;
          $scope.skuName = $scope.scanningGoodsName;
          $scope.itemId = data.itemData.id;
          $scope.skuId = data.itemData.skuNo;
          $scope.commodityName = data.itemData.name;
          $scope.volume = data.itemData.width + "mm *" + data.itemData.depth + "mm *" + data.itemData.height + "mm";
          $scope.weight = data.itemData.weight + "g";
          $scope.clientName = data.itemData.client.name;
          $scope.itemUnitName = data.itemData.itemUnit.name;
          $scope.scanningGoodsCount = data.amount;
          $("#destinationContainer").focus();
          $scope.destinationContainerShow = true;
          $scope._goodsContent = true;
          if (($scope.moveModel == 3 && !$scope.isfirst) || ($scope.moveModel == 2 && !$scope.isfirst)) {
            $("#goodsCount").focus();
            $scope.goodsCountShow = true;
          }
        }, errMessageFun);
      }
    };

    //扫描目标容器事件
    $scope.destinationContainer = function (e) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        if ($scope.destinationContainerName != $scope.orgContainerName) {
          moveToolService.moveScanningDestination($scope.sourceId, $scope.itemId, $scope.destinationContainerName, function (data) {
            $scope.errMessage = '';
            $scope.goodsMoveCarts = false;
            $scope.destinationContainerCount = data.itemDataAmount;
            $scope.destinationName = $scope.destinationContainerName;
            $scope.destinationId = data.id;
            $("#goodsCount").focus();
            $scope.goodsCountShow = true;
          }, errMessageFun);
        } else {
          $scope.errMessage = 'error';
        }
      }
    };
    //扫描原车事件
    $scope.orgContainer = function (e) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        moveToolService.moveScanningSource($scope.orgContainerName, function (data) {
          $scope.errMessage = '';
          $scope.goodsMoveCarts = false;
          $scope.orgContainerCount = data.itemDataAmount;
          $scope.orgName = $scope.orgContainerName;
          $scope.sourceId = data.id;
          $("#scanningGoods").focus();
          $scope.scanningGoodsShow = true;
        }, errMessageFun);
      }
    };
    //数量回车事件
    $scope.goodsCount = function (e) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $scope.amount = $scope.goodCounts;
        var data = {
          amount: $scope.amount,
          destinationId: $scope.destinationId,
          itemDataId: $scope.itemId,
          sourceId: $scope.sourceId
        };
        moveToolService.moveMeasuring(data, function (data) {
          $scope.errMessage = '';
          $scope.goodsMoveCarts = true;
          if ($scope.moveModel == 1 || $scope.moveModel == 3) {
            $scope.orgContainerCount -= $scope.amount;
            if ($scope.moveModel == 3) {
              $scope.destinationContainerCount = parseInt($scope.destinationContainerCount) + parseInt($scope.amount);
            }
            $scope.showType = 'scanningGoods';
            $("#scanningGoods").focus();
          } else if ($scope.moveModel == 2) {
            $scope.scanningGoodsShow = false;
            $scope.destinationContainerCount = parseInt($scope.destinationContainerCount) + parseInt($scope.amount);
            $("#orgContainer").focus();
          } else if ($scope.moveModel == 4) {
            $("#orgContainer").focus();
            var arr = Object.keys($scope);
            $scope.isfirst = true;
            arr.map(
              function (data) {
                if (data.indexOf("Show") != -1) {
                  if ($scope[data] == true) {
                    $scope[data] = false;
                  }
                }
              }
            );
            $scope.orgContainerShow = true;
          }
          $scope.isfirst = false;
          $scope.destinationContainerShow = false;
          $scope.successName = "已成功从" + $scope.orgName + "转移" + $scope.amount + "件商品至" + $scope.destinationName;
          $scope.goodsMoveCarts = true;
          $scope.moveArray.push({
            picture: '',
            orgName: $scope.orgName,
            destinationName: $scope.destinationName,
            skuName: $scope.skuId,
            skuDes: $scope.commodityName,
            count: $scope.goodCounts,
            client: $scope.clientName,
            type: $scope.itemUnitName,
            operatingTime: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date())
          });
          clear();
        }, errMessageFun);
      }
    };

    $scope.selectTransferRecords = function () {
      var moveGoodsSelectGridId = $("#moveGoodsSelectGrid").data("kendoGrid");
      moveGoodsSelectGridId.setOptions({
        height: 300,
      });
      $timeout(function () {
        moveGoodsSelectGridId.setOptions({
          dataSource: $scope.moveArray,
          columns: columns
        });
      })


      //  $scope.moveGoodsSelectGridOptions = internalToolService.reGrids($scope.moveArray, columns, 300);
      $('#moveToolId').parent().addClass("windowTitle");
      $scope.moveToolWindow.setOptions({
        width: 1200,
        height: 500,
        visible: false,
        actions: false
      });
      $scope.moveToolWindow.center();
      $scope.moveToolWindow.open();

    };
    var columns = [{
        field: "picture",
        width: 100,
        headerTemplate: "<span translate='PICTURE'></span>"
      },
      {
        field: "orgName",
        headerTemplate: "<span translate='原始容器'></span>"
      },
      {
        field: "destinationName",
        headerTemplate: "<span translate='目的容器'></span>"
      },
      {
        field: "skuName",
        headerTemplate: "<span translate='SKU'></span>"
      },
      {
        field: "skuDes",
        width: 200,
        headerTemplate: "<span translate='GOODS_NAME'></span>"
      },
      {
        field: "count",
        headerTemplate: "<span translate='COUNT'></span>"
      },
      ///222
      {
        field: "type",
        headerTemplate: "<span translate='TYPE'></span>"
      },
      ///12
      {
        field: "client",
        headerTemplate: "<span translate='CLIENT'></span>"
      },
      {
        field: "operatingTime",
        headerTemplate: "<span translate='操作时间'></span>"
      }
    ];
    //错误方法
    function errMessageFun(data) {
      $scope.errMessage = data.data.values[0];
    }

    function clear()

    {
      $scope.errMessage = '';
      $scope.goodCounts = '';
      $scope.scanningGoodsName = '';
      $scope.skuName = '';
      $scope.skuId = '';
      $scope.commodityName = '';
      $scope.volume = '';
      $scope.weight = '';
      $scope.clientName = '';
      $scope.scanningGoodsCount = '';
      if ($scope.moveModel == 1) $scope.goodsCountShow = false;

      if ($scope.moveModel != 1 && $scope.moveModel != 3) {
        $scope.sourceId = '';
        $scope.orgContainerName = "";
      }
      if ($scope.moveModel != 2 && $scope.moveModel != 3) {
        $scope.destinationId = '';
        $scope.destinationContainerName = '';
        $scope.destinationName = '';
      }

    }
  });
})();