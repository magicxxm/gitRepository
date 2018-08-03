/**
 * Created by thoma.bian on 2017/5/10.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("icqaSystemCreateCtl", function ($scope, $window, $rootScope, commonService, ICQABaseService, icqaSystemCreateService) {

    $scope.icqaCreatePost = false;
    $scope.amounts = [];
    $scope.stockStorageAmounts = false;
    $scope.count = false;

    icqaSystemCreateService.getZone($window.localStorage["clientId"], function (zones) {
      $scope.zoneAll = zones;
        // var zoneComboBox = $("#zoneId").data("kendoComboBox");
      var zoneComboBox = $("#zoneId").data("kendoMultiSelect");
      zoneComboBox.value("");
      zoneComboBox.setDataSource(new kendo.data.DataSource({data: zones}));
    });

    // icqaSystemCreateService.getStocktakingRules(function(data){
    //   var grid = $("#stocktakingRulesId").data("kendoComboBox");
    //   grid.setDataSource(new kendo.data.DataSource({data: data}));
    //
    // });
    // 点新增
    $scope.addStocktakingOrder = function () {
      $scope.stockStorageAmounts = false;
      icqaSystemCreateService.getStocktaking0rderUser($window.localStorage["clientId"], function (data) {
        $scope.userName = data;
      });
      var grid = $("#stocktakingGridId").data("kendoGrid");
      grid.addRow();
        // grid.dataSource.add({name : "itemNo",value : " "});
      // grid.dataSource.insert(0, {
      //   id: stocktakingRule.id,
      //   areaName: stocktakingRule.name,
      //   comparisonType: stocktakingRule.comparisonType
      // });
    };
    // 点删除
    $scope.deleteStocktaking = function () {
      $scope.stockStorageAmounts = true;
      var arr = [];
      var grid = $("#stocktakingGridId").data("kendoGrid");
      var selectedRows = grid.select();
      grid.removeRow(selectedRows);

      // var data = grid.dataSource.data();
      // for (var i = 0; i < data.length; i++) {
      //   arr.push({uid: data[i].uid, stockStorageAmount: data[i].stockStorageAmount});
      // }
      // $scope.stockStorageAmountArr = arr;
      // for (var j = 0; j < arr.length; j++) {
      //   if (j == 0) {
      //     $scope.stockStorageAmountSum = arr[j].stockStorageAmount;
      //   } else {
      //     $scope.stockStorageAmountSum += arr[j].stockStorageAmount;
      //   }
      // }
    };

      // // client
      // $rootScope.clientSource = ICQABaseService.getDataSource({key: "getClient", text: "name", value: "id"});
      // console.log($rootScope.clientSource);

      // var combobox = $("#comboClientId").data("kendoComboBox");
      // combobox.text($scope.client);

    var columns = [
      {field: "skuNo", headerTemplate: "<span translate='商品条码'></span>", editor: function (container, options) {
          $('<input id="itemNoId" name="' + options.field + '" class="k-textbox" />').appendTo(container);
      },template: function (item) {
          if (item.skuNo != undefined){
              icqaSystemCreateService.getItemData(item.skuNo,"",function(data){
                  console.log(data);
                  item.itemNo = data.itemNo;
              },function (data) {
                  if (data.key == "EX_STOCKTAKING_COMPLEMENT_CODER") {
                      $scope.itemNoError = "商品条码不唯一，请重新输入"
                  } else if (data.key == "EX_INFORMATION_OBJECT_ERROR"){
                      $scope.itemNoError = "商品条码不是有效条码，请重新输入";
                  }
                  $scope.remarks();
              })
          }
          return item.skuNo ? item.skuNo:"";
      }},
      {field: "itemNo", headerTemplate: "<span translate='唯一编码'></span>" },
      // {field: "client", headerTemplate: "<span translate='客户'></span>"},
      // class="k-textbox"


        {field: "client", headerTemplate: "<span translate='客户'></span>",
            editor: function (container, options) {
                $('<input id="clientId" name="' + options.field + '"  />')
                    .appendTo(container)
                    .kendoComboBox({
                        dataTextField: "name",
                        dataValueField: "id"
                    });
                icqaSystemCreateService.selectClientOptions(function(data){
                    var combox = $("#clientId").data("kendoComboBox");
                    combox.setDataSource(new kendo.data.DataSource({data: data}));
                });
            },
            template: function (item) {
                if (item.client){
                    icqaSystemCreateService.getItemDataNameAmount(zoneStr() ,item.itemNo, item.client.name, function(data){
                        console.log(data);
                        item.itemName = data.name;
                        item.binItemNoAmount = data.binAmount;
                        item.containerAmount = data.containerAmount;
                    });
                }
                $scope.client = item.client;
                return item.client ? item.client.name:"";
            }
        },

      {field: "itemName", headerTemplate: "<span translate='商品名称'></span>"},

      {field: "binItemNoAmount", headerTemplate: "<span translate='货位商品数量' ></span>"},
      {field: "containerAmount", headerTemplate: "<span translate='货筐商品数量' ></span>"}
    ];


      $scope.remarks = function () {
          $("#remarksId").parent().addClass("mySelect");
          $scope.remarksWindow.setOptions({
              width: 600,
              height: 150,
              visible: false,
              actions: false
          });
          $scope.remarksWindow.center();
          $scope.remarksWindow.open();
      };


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

    // 点保存
    $scope.saveIcqaCreate = function () {
      var grid = $("#stocktakingGridId").data("kendoGrid");
      var rule = grid.dataSource.data();
      console.log(rule);
      var arr = [];
      icqaSystemCreateService.saveStocktaking({
        "stocktakingNo": $scope.icqaName ,
        "stocktakingType":0 , // 默认静盘
        "state":90,
        "zone": zoneStr()
       },function(data){
          console.log(data);
          for (var i = 0; i < rule.length; i++) {  // 明细条数
              if (rule[i].itemNo != null){
                  arr.push({
                      "itemNo": rule[i].itemNo,
                      "skuNo":rule[i].skuNo,
                      "itemName":rule[i].itemName,
                      "state": "90",
                      "stocktakingId": data.id,
                      "clientId": rule[i].client.id,
                      "amountWMS": rule[i].binItemNoAmount
                      // "clientId":$window.localStorage["clientId"]
                  })
              }
          }
        icqaSystemCreateService.createStocktakingPosition(arr, function () {
            icqaSystemCreateService.createInventoryTask(data.id, function () {
                $scope.icqaName = "";
                $scope.zone = "";
                $scope.itemNo = "";
                $scope.skuNo = "";
                $scope.itemName = "";

                $("#stocktakingGridId").data("kendoGrid").dataSource.read();   // grid 清空对应的那行数据
            });

        });
      },function (data) {
         // 任务名称重名
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
    };
  })
})();
