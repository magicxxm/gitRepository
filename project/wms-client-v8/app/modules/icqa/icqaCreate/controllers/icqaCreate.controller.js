/**
 * Created by thoma.bian on 2017/5/10.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("icqaCreateCtl", function ($scope, $window, $rootScope, commonService, ICQABaseService, icqaCreateService) {

    $scope.icqaCreatePost = false;
    $scope.amounts = [];
    $scope.stockStorageAmountSum = 0;
    $scope.stockStorageAmounts = false;
    $scope.stockStorageAmountArr = [];
    $scope.count = false;
    // $scope.dayNumber = true;

    icqaCreateService.getZone($window.localStorage["clientId"], function (zones) {
      $scope.zoneAll = zones;
      var zoneComboBox = $("#zoneId").data("kendoMultiSelect");
      zoneComboBox.value("");
      zoneComboBox.setDataSource(new kendo.data.DataSource({data: zones}));
    });

    icqaCreateService.getStocktakingRules(function(data){
      var grid = $("#stocktakingRulesId").data("kendoComboBox");
      grid.setDataSource(new kendo.data.DataSource({data: data}));

    });
    $rootScope.stocktakingTypeSource = ["日常盘点","系统盘点"];

    // var typeComboBox = $("#typeId").data("kendoComboBox");
    // typeComboBox.value("日常盘点");

    //用户下拉框
    $scope.addStocktakingOrder = function (stocktakingRule) {
      $scope.stockStorageAmounts = false;
      icqaCreateService.getStocktaking0rderUser($window.localStorage["clientId"], function (data) {
        $scope.userName = data;
      });
      if (stocktakingRule.name == "任意天内操作次数大于" || stocktakingRule.name == "任意天内操作人员"){
          // $scope.dayNumber = true;
          if ($scope.day != undefined){
              stocktakingRule.name = parseInt($scope.day) + stocktakingRule.name.substring(2);
          }else {
              $scope.stocktakingRule = "";
              $scope.title = "请先选择自定义天数";
              $("#inventoryTypeId").parent().addClass("mySelect");
              $scope.inventoryTypeWindow.setOptions({
                  width: 600,
                  height: 150,
                  visible: false,
                  actions: false
              });
              $scope.inventoryTypeWindow.center();
              $scope.inventoryTypeWindow.open();
              return;
          }
      }
      var grid = $("#stocktakingGridId").data("kendoGrid");
      grid.dataSource.insert(0, {
        id: stocktakingRule.id,
        areaName: stocktakingRule.name,
        comparisonType: stocktakingRule.comparisonType
      });
    };

    $scope.deleteStocktaking = function () {
      $scope.stockStorageAmounts = true;
      $scope.stockStorageAmountSum = 0;
      var arr = [];
      var grid = $("#stocktakingGridId").data("kendoGrid");
      var selectedRows = grid.select();
      grid.removeRow(selectedRows);

      var data = grid.dataSource.data();
      for (var i = 0; i < data.length; i++) {
        arr.push({uid: data[i].uid, stockStorageAmount: data[i].stockStorageAmount});
      }
      $scope.stockStorageAmountArr = arr;
      for (var j = 0; j < arr.length; j++) {
        if (j == 0) {
          $scope.stockStorageAmountSum = arr[j].stockStorageAmount;
        } else {
          $scope.stockStorageAmountSum += arr[j].stockStorageAmount;
        }
      }
    };

    var columns = [
      {field: "areaName", headerTemplate: "<span translate='项目'></span>"},
      {
        field: "parameter", editor: function (container, options) {
        if (options.model.comparisonType === "DDL_ZONE") {
          var zone = [];
          if ($scope.zone == "" || $scope.zone == undefined || $scope.zone == null) {
            if ($scope.zoneAll.length > 0) {
              for (var i = 0; i < $scope.zoneAll.length; i++) {
                var z = $scope.zoneAll[i];
                zone.push({id: z.id, name: z.name});
              }
            }
          }
          if ($scope.zone) {
            if ($scope.zone.length > 0) {
              for (var i = 0; i < $scope.zone.length; i++) {
                var z = $scope.zone[i];
                zone.push({id: z.id, name: z.name});
              }
            }
          }
          ICQABaseService.selectEditor(container, options, zone);

        } else if (options.model.comparisonType === "DDL_USER") {
          var user = [];
          if ($scope.userName) {
              for (var i = 0; i < $scope.userName.length; i++) {
                  var  u= $scope.userName[i];
                  user.push({id: u.id, name:u.username});
              }
            ICQABaseService.selectEditor(container, options, user);
          }
        } else{
          $('<input type="number"  name="' + options.field + '" class="k-textbox" />').appendTo(container);
        }
      },
        template: function (item) {
          var parameter;
          var parameters;
          if (item.parameter) {
            if (item.comparisonType === "DDL_ZONE") {
              parameter = item.parameter.name;
              parameters = parameter;

            } else if (item.comparisonType === "DDL_USER") {
              parameter = item.parameter.name;
              parameters = item.parameter.name;
            } else {
              parameter = item.parameter;
              parameters = item.parameter;
            }
            icqaCreateService.getStocktaking0rder({
              comparisonType: item.comparisonType,
              areaName: item.areaName,
              parameter: parameter,
              zoneList: zoneStr()
            }, function (v) {
              item.totalCount = v;
            });
          }
          return parameters ? parameters : "";
        }, headerTemplate: "<span translate='参数'></span>"
      },


      {field: "totalCount", headerTemplate: "<span translate='涉及货位总数'></span>"},
      {
        field: "percentage", template: function (item) {
        if (item.percentage) {
          var str = item.percentage.replace("%", "");
          str = str / 100;
            item.stockStorageAmount = parseInt($scope.icqaAmount * ((str * 100)/100));
          // item.stockStorageAmount = parseInt($scope.icqaAmount * str);
          if (item.stockStorageAmount) {
            if ($scope.stockStorageAmountArr.length > 0) {
              for (var i = 0; i < $scope.stockStorageAmountArr.length; i++) {
                var stockStorageAmountArr = $scope.stockStorageAmountArr[i];
                if (stockStorageAmountArr.uid == item.uid) {
                  $scope.count = false;
                  stockStorageAmountArr.stockStorageAmount = item.stockStorageAmount;
                  break;
                } else {
                  $scope.count = true;
                }
              }
            } else {
              $scope.stockStorageAmountArr.push({uid: item.uid, stockStorageAmount: item.stockStorageAmount});
            }
            if ($scope.count == true) {
              $scope.stockStorageAmountArr.push({uid: item.uid, stockStorageAmount: item.stockStorageAmount});
            }
            for (var j = 0; j < $scope.stockStorageAmountArr.length; j++) {
              if (j == 0) {
                $scope.stockStorageAmountSum = $scope.stockStorageAmountArr[j].stockStorageAmount;
              } else {
                $scope.stockStorageAmountSum += $scope.stockStorageAmountArr[j].stockStorageAmount;
              }

            }
          }
        }
        if (item.stockStorageAmount > item.totalCount || $scope.stockStorageAmountSum > $scope.icqaAmount) {
          item.stockStorageAmount = 0;
          $("#stocktakingId").parent().addClass("mySelect");
          $scope.stocktakingWindow.setOptions({
            width: 600,
            height: 150,
            visible: false,
            actions: false
          });
          $scope.stocktakingWindow.center();
          $scope.stocktakingWindow.open();
        }
        return item.percentage ? item.percentage : "";
      }, headerTemplate: "<span translate='占盘点总数百分比'></span>"
      },
      {field: "stockStorageAmount", headerTemplate: "<span >盘点货位数量:{{stockStorageAmountSum}}</span>"}
    ];

    $scope.stocktakingGridOption = ICQABaseService.editGrid({
      columns: columns,
      height: $(document.body).height() - 210
    });

    function zoneStr() {
      var str = "";
      if ($scope.zone == "" || $scope.zone == undefined || $scope.zone == null) {
        if ($scope.zoneAll.length > 0) {
          for (var i = 0; i < $scope.zoneAll.length; i++) {
            if (i == 0) {
              str = $scope.zoneAll[i].name;
            } else {
              str += "," + $scope.zoneAll[i].name;
            }
          }
        }
      } else {
        if ($scope.zone.length > 0) {
          for (var j = 0; j < $scope.zone.length; j++) {
            if (j == 0) {
              str = $scope.zone[j].name;
            } else {
              str += "," + $scope.zone[j].name;
            }
          }
        }
      }
      return str;
    }

    $scope.saveIcqaCreate = function () {
      var grid = $("#stocktakingGridId").data("kendoGrid");
      var rule = grid.dataSource.data();
      console.log(rule);

      var arr = [];
      var parameter;
        if($scope.icqaName == "" || $scope.icqaName == undefined || $scope.icqaName == "undefined"){
            $scope.title = "盘点名称不能为空";
            $("#inventoryTypeId").parent().addClass("mySelect");
            $scope.inventoryTypeWindow.setOptions({
                width: 600,
                height: 150,
                visible: false,
                actions: false
            });
            $scope.inventoryTypeWindow.center();
            $scope.inventoryTypeWindow.open();
            return;
        }
      console.log($scope.stocktakingType);
      if ($scope.stocktakingType != null && $scope.stocktakingType != ""){
          icqaCreateService.saveStocktaking({
              "name": $scope.icqaName,
              "amount": $scope.icqaAmount,
              "type":$scope.stocktakingType,
              "zone": zoneStr()
          },function(data){
              for (var i = 0; i < rule.length; i++) {
                  if (rule[i].comparisonType == "TEXT") {
                      parameter = rule[i].parameter;
                  } else if (rule[i].comparisonType == "DDL_USER") {
                      parameter = rule[i].parameter.name;
                  } else {
                      parameter = rule[i].parameter.name;
                  }
                  arr.push({
                      "state": "RAW",
                      "stocktakingId": data.id,
                      "areaName": rule[i].areaName,
                      "parameter": parameter,
                      "stockStorageAmount": rule[i].stockStorageAmount,
                      "stocktakingRuleId": rule[i].id,
                      "type":$scope.stocktakingType
                  })
              }
              icqaCreateService.createStocktaking0rder(zoneStr(), arr, function () {
                  $scope.icqaName = "";
                  $scope.icqaAmount = "";
                  $scope.zone = "";
                  $scope.stocktakingRule = "";
                  $scope.stocktakingType = "";
                  $scope.day = "";

                  var typeComboBox = $("#typeId").data("kendoComboBox");
                  typeComboBox.value("");

                  $scope.stockStorageAmountSum = 0;
                  $scope.stockStorageAmountArr = [];
                  $("#stocktakingGridId").data("kendoGrid").dataSource.read();
              });
          },function (data) {
              $("#inventoryTaskId").parent().addClass("mySelect");
              $scope.inventoryTaskWindow.setOptions({
                  width: 600,
                  height: 150,
                  visible: false,
                  actions: false
              });
              $scope.inventoryTaskWindow.center();
              $scope.inventoryTaskWindow.open();
              return;

          });
      }else {
          $scope.title = "请选择盘点方式";
          $("#inventoryTypeId").parent().addClass("mySelect");
          $scope.inventoryTypeWindow.setOptions({
              width: 600,
              height: 150,
              visible: false,
              actions: false
          });
          $scope.inventoryTypeWindow.center();
          $scope.inventoryTypeWindow.open();
          return;
      }


    };
  })
})();
