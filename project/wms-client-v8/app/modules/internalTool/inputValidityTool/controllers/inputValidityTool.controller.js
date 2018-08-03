/**
 * Created by zhihan.dong on 2017/04/24.
 * updated by zhihan.dong on 2017/05/04.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("inputValidityToolCtl", function ($scope, $timeout, $window, $rootScope, $state, $translate, commonService, internalToolService, inputValidityToolService) {
    $scope.inputValidityPage = 1;
    $scope._goodsContent = false;
    //$scope.dateModel = false;
    $scope.produceDate = false;
    $scope.maturityDate = false;
    $scope.array = [];
    $scope.year = "";
    $scope.month = "";
    $scope.day = "";
    $scope.dateUnits = "";
    $scope.inputValidityModel = 1;
    $scope.regular = /((^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(10|12|0?[13578])([-\/\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(11|0?[469])([-\/\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(0?2)([-\/\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([3579][26]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][13579][26])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][13579][26])([-\/\._])(0?2)([-\/\._])(29)$))/;

    $scope.moveModelclick = function (model) {
      $scope.inputValidityModel = model;
      clear();

    };
    //扫描商品
    $scope.scanGoods = function (e) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        inputValidityToolService.validityScanningItemData($scope.sourceId, $scope.scanningGoodsName, function (data) {
          $scope.showType = 'scan';
          if (data.itemData.lotMandatory) {
            $scope.errMessage = '';
            $scope.scanningGoods = data.amount;
            $scope.dateSelectShow = true;
            $scope.itemDataId = data.itemData.id;
            $scope.skuId = data.itemData.itemNo;
            $scope.commodityName = data.itemData.name;
            $scope.volume = data.itemData.width + "mm *" + data.itemData.depth + "mm *" + data.itemData.height + "mm";
            $scope.weight = data.itemData.weight + "g";
            $scope.clientName = data.itemData.client.name;
            if(data.lot != null){
                $scope.expirationDate = data.lot.useNotAfter; //到期日期
                $scope.lotId = data.lot.id;
            }else{
                $scope.expirationDate = "请录入！";
            }
            $scope.dateModel = data.itemData.lotType;
            $scope.dateModel = $scope.dateModel.toLowerCase();
            $scope.unitName = data.itemData.itemUnit.name;
            $scope.goodName = data.itemData.name;
            $scope.lotUnit = data.itemData.lotUnit;
            $scope.dateUnit = $translate.instant($scope.lotUnit);
            if ($scope.dateModel == "manufacture") {
              $timeout(function () {
                $("#maturityYear").focus();
              }, 0);
            } else if ($scope.dateModel == "expiration") {
              $timeout(function () {
                $("#productionYear").focus();
              }, 100);
            } else {
              $scope.dateModel = "manufacture";
            }
            $scope._goodsContent = true;
          } else {
            $scope.errMessage = 'lotMandatory is false'
          }
        }, errMessageFun);
      }
    };
    //原始容器
    $scope.orgContainer = function (e) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        inputValidityToolService.validityScanningSource($scope.orgContainerName, function (data) {
          $scope.errMessage = '';
          $scope.orgContainerCount = data.itemDataAmount;
          $scope.sourceId = data.id;
          $scope.sourceName = data.name;
          $scope.scanningGoodsShow = true;
          $("#scanningGoods").focus();
        }, errMessageFun);
      }
    };
    //检查日期
    $scope.checkDate = function (e, year, month, day, dateUnits) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode != 13) return;
      $scope.useNotAfter = year + "-" + pad(month) + "-" + pad(day);
      $scope.manufacture = $scope.useNotAfter;
      if ($scope.regular.test($scope.useNotAfter)) {
        var date = new Date($scope.useNotAfter);
        if ($scope.lotUnit.toLowerCase() == "year") {
          date.setFullYear(date.getFullYear() + (dateUnits || 0));
        } else if ($scope.lotUnit.toLowerCase() == "month") {
          date.setMonth(date.getMonth() + (dateUnits || 0));
        } else if ($scope.lotUnit.toLowerCase() == "day") {
          date.setDate(date.getDate() + (dateUnits || 0));
        }
        var now = new Date();
        var useNotAfterTest = new Date(date.getFullYear() + "-" + pad(date.getMonth() + 1) + "-" + pad(date.getDate()));
        if (now > useNotAfterTest) {
          $scope.errMessage = "到期日小于当前日期";
          return;
        } else
          $scope.useNotAfter = date.getFullYear() + "-" + pad(date.getMonth() + 1) + "-" + pad(date.getDate());
      } else {
        $scope.year = "";
        $scope.month = "";
        $scope.day = "";
        $scope.dateUnits = "";
        $('#productionYear').focus();
      }
      $scope.entering(e);
    };
    //补位
    function pad(str) {
      str = str + "";
      var pad = "00";
      return (pad.length > str.length ? pad.substring(0, pad.length - str.length) + str : str);
    }
    $scope.entering = function (e) {
      var data = {
        itemDataId: $scope.itemDataId,
        lotId: $scope.lotId,
        sourceId: $scope.sourceId,
        useNotAfter: $scope.useNotAfter
      };

      inputValidityToolService.validityEntering(data, function () {
        $scope.errMessage = '';
        if ($scope.inputValidityModel == 1) {
          ///初始化第一步
          inputValidityToolService.validityScanningSource($scope.orgContainerName, function (data) {
            $scope.errMessage = '';
            $scope.orgContainerCount = data.itemDataAmount;
            $scope.sourceId = data.id;
            $scope.sourceName = data.name;
            $scope.scanningGoodsShow = true;
            $("#scanningGoods").focus();
          }, errMessageFun);
          ///
          $scope.dateSelectShow = false;
          $scope.dateModel = "";
          $scope.orgContainer(e);
        } else if ($scope.inputValidityModel == 2) {
          $scope.moveModelclick();
          $("#orgContainer").focus();
        }
        $scope.array.push({
          picture: '',
          source: $scope.sourceName,
          orgExpirationDate: $scope.expirationDate,
          useNotAfter: $scope.useNotAfter,
          goodsName: $scope.commodityName,
          count: $scope.scanningGoods,
          type: $scope.unitName,
          SKU: $scope.skuId,
          client: $scope.clientName,
          time: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", new Date())
        });
        $scope.useNotAfterSuccess = $scope.useNotAfter;
        $scope.showType = 'dateSucess';
        clear();
      }, errMessageFun);
    };

    $scope.inputValidityDateFocus = function (e, type) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $('#' + type).focus();
      }
    };

    $scope.dateUpdateType = function (e) {
      var keycode = $window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        if ($scope.updateDateType == 1) {
          $scope.dateModel = 'produceDate';
          $("#productionYear").focus();
        }
        if ($scope.updateDateType == 2) {
          $scope.dateModel = 'maturityDate';
          $("#maturityDate").focus();
        }
      }
    };
    $scope.selectTransferRecords = function () {
      $('#inputValidityId').parent().addClass("windowTitle");
      $scope.inputValidityWindow.setOptions({
        width: 1200,
        height: 500,
        visible: false,
        actions: false
      });
      $scope.inputValidityWindow.center();
      $scope.inputValidityWindow.open();
      var inputValidityQueryGrid = $("#inputValidityGrid").data("kendoGrid");
      inputValidityQueryGrid.setOptions({
        height: 300,
        columns: columns
      });
      $timeout(function () {
        inputValidityQueryGrid.setOptions({
          dataSource: $scope.array
        });
      })

      // $scope.moveGoodsSelectGridOptions = internalToolService.reGrids($scope.array, columns, 300);



    };
    var columns = [{
        field: "picture",
        width: 100,
        headerTemplate: "<span translate='PICTURE'></span>"
      },
      {
        field: "source",
        headerTemplate: "<span translate='原始容器'></span>"
      },
      {
        field: "orgExpirationDate",
        headerTemplate: "<span translate='原有效期'></span>"
      },
      {
        field: "useNotAfter",
        headerTemplate: "<span translate='更改有效期'></span>"
      },
      {
        field: "SKU",
        width: 150,
        headerTemplate: "<span translate='SKU'></span>"
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
      {
        field: "client",
        headerTemplate: "<span translate='CLIENT'></span>"
      },
      {
        field: "time",
        headerTemplate: "<span translate='操作时间'></span>"
      }
    ];
    //错误方法
    function errMessageFun(data) {
      $scope.errMessage = data.message;
    }

    function clear() {
      $scope.year = '';
      $scope.month = '';
      $scope.day = '';
      $scope.dateUnits = '';
      $scope.expiredYear = '';
      $scope.expiredMonth = '';
      $scope.expiredDay = '';

      $scope.scanningGoodsName = '';
      $scope.scanningGoods = '';
      $scope.dateSelectShow = '';
      $scope.itemDataId = '';
      $scope.skuId = '';
      $scope.commodityName = '';
      $scope.volume = '';
      $scope.weight = '';
      $scope.clientName = '';
      $scope.expirationDate = '';
      $scope.dateModel = '';
      $scope.lotId = '';
      $scope.unitName = '';
      $scope.goodName = '';
      $scope.lotUnit = '';
      $scope.dateUnit = '';
      $scope.orgContainerCount = '';
      $scope.sourceId = '';
      $scope.sourceName = '';
      $scope.scanningGoodsShow = '';
      $scope._goodsContent = '';
      if ($scope.inputValidityModel != 1) $scope.orgContainerName = '';
      $scope.dateSelectShow = '';
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
    }

  });
})();