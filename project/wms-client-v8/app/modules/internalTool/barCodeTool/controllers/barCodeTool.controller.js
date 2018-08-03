/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("barCodeToolCtl", function ($scope, $rootScope, $state, $timeout, commonService, barCodeToolService, internalToolService,$translate) {
    $scope._goodsContent = false;
    $scope.barCodeSku = false;
    $scope.continueGoods = false;
    $scope.barCodeArr = [];
    focusScanSku();
    $scope.goodsBarCode = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        barCodeToolService.barcodeScanningSKU($scope.skuNo, function (data) {
          $scope.continueGoods = false;
          if (data.length == 0) return;
          if (data.length > 1) {

            //多种类
            $('#barCodeProductsId').parent().addClass("windowTitle");
            $scope.barCodeProductsWindow.setOptions({
              width: 1200,
              height: 500,
              visible: false,
              actions: false
            });
            $scope.barCodeProductsWindow.center();
            $scope.barCodeProductsWindow.open();
          } else {
            $scope._goodsContent = true;
            $scope.skuId = data[0].skuNo;
            $scope.itemNo = data[0].itemNo;
            $scope.goodName = data[0].name;
            $scope.width = data[0].width;
            $scope.depth = data[0].depth;
            $scope.height = data[0].height;
            $scope.weight = data[0].weight;
            $scope.type = data[0].itemUnit.unitType;
            $timeout(function () {
              $("#printQuantityInput").focus();
            })
          }
          $scope.barCodeSku = true;
          $scope.goodsBarCodeShow = true;
          var grid = $("#barCodeProductsGrid").data("kendoGrid");
          grid.setOptions({
            dataSource: data,
            columns: barCodeColumns,
            selectable: "multiple,row"
          });

          grid.bind("change", function () {
            var grid = $("#barCodeProductsGrid").data("kendoGrid");
            var row = grid.select();
            var item = grid.dataItem(row);
            $scope._goodsContent = true;
            $scope.skuId = item.skuNo;
            $scope.itemNo = item.itemNo;
            $scope.goodName = item.name;
            $scope.width = item.width;
            $scope.depth = item.depth;
            $scope.height = item.height;
            $scope.weigth = item.weigth;
            $scope.type = data.itemUnit.unitType;
            $timeout(function () {
              $scope.barCodeProductsWindow.close();
            }, 0);
          });
        },function(data){
            var win = $("#mushinyWindow").data("kendoWindow");
            commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                setTimeout(function(){
                    $("#warnContent").html($translate.instant(data.key));
                }, 200);
                win.bind("close", function(){ focusScanSku(); });
            }});

        });
      }
    };
    $scope.skuIdClick = function () {
      $("#printQuantityInput").focus();
      $scope.skuIdShow = true;
    };
    var barCodeColumns = [
      /*{
              field: "picture",
              headerTemplate: "<span translate='PICTURE'></span>"
            },*/
      {
        width: 150,
        field: "skuNo",
        headerTemplate: "<span translate='SKU_NO'></span>"
      },
      {
        width: 150,
        field: "itemNo",
        headerTemplate: "<span translate='SKU_ID'></span>"
      },
      /*      {
              field: "client",
              headerTemplate: "<span translate='CLIENT'></span>"
            },*/
      {
        field: "name",
        headerTemplate: "<span translate='GOODS_NAME'></span>"
      }
    ];

    function focusScanSku(){
      $scope.skuNo="";
      setTimeout(function(){ $("#scanGood").focus();}, 600);
    }

    $scope.barCodeProductsSure = function () {
      if ($scope.itemNo) {
        $scope.barCodeProductsWindow.close();
        $("#printQuantityInput").focus();
        $scope._goodsContent = true;
        $scope.barCodeSku = true;
      }
    };

    $scope.numberOfPrints = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
      //确认补打条码
      var json={"ip":"192.168.1.233","type":"tools","data":[{"goodsItemNo":$scope.itemNo,"goodsDescript":$scope.goodName}]};
      for(var i=1;i<$scope.barCodeCount;i++){
          json.data.push({"goodsItemNo":$scope.itemNo,"goodsDescript":$scope.goodName});
      }
      barCodeToolService.printSku(JSON.stringify(json),function(){
          $scope.barCodeArr.push({
              picture: '',
              sku: $scope.skuId,
              skuPrintId: $scope.itemNo,
              goodsName: $scope.goodName,
              count: $scope.barCodeCount,
              type: $scope.type,
              client: '',
              time: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date())
          });
          $scope.continueGoods = true;
      });
      }
    };

    $scope.nextGoods = function () {
      $scope.skuNo = '';
      $scope.skuId = '';
      $scope.barCodeCount = '';
      $scope._goodsContent = false;
      $scope.barCodeSku = false;
      $scope.continueGoods = false;
      //扫描商品获焦点
      $("#scanGood").focus();

      //做初始化
      var arr = Object.keys($scope);
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

    $scope.selectTransferRecords = function () {
      $('#barCodeId').parent().addClass("windowTitle");
      $scope.barCodeWindow.setOptions({
        width: 1200,
        height: 600,
        visible: false,
        actions: false
      });
      var barCodeSelectGridId = $("#barCodeSelectGrid").data("kendoGrid");
      barCodeSelectGridId.setOptions({
        height: 400
      });
      $timeout(function () {
        barCodeSelectGridId.setOptions({
          dataSource: $scope.barCodeArr,
          columns: columns
        });
      })

      $scope.barCodeWindow.center();
      $scope.barCodeWindow.open();
    };

    var columns = [{
        field: "picture",
        width: 100,
        headerTemplate: "<span translate='PICTURE'></span>"
      },
      {
        field: "sku",
        headerTemplate: "<span translate='SKU'></span>"
      },
      {
        field: "skuPrintId",
        width: 150,
        headerTemplate: "<span translate='SKU Print ID'></span>"
      },
      {
        field: "goodsName",
        width: 200,
        headerTemplate: "<span translate='GOODS_NAME'></span>"
      },
      {
        field: "count",
        headerTemplate: "<span translate='COUNT'></span>"
      },
      {
        field: "type",
        headerTemplate: "<span translate='TYPE'></span>"
      },
      /*    {
            field: "client",
            headerTemplate: "<span translate='CLIENT'></span>"
          },*/
      {
        field: "time",
        headerTemplate: "<span translate='操作时间'></span>"
      }
    ];
  });
})();