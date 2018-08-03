/**
 * Created by frank.zhou on 2017/04/25.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("adviceRequestCtl", function ($scope, $window, $rootScope, commonService, masterService, adviceRequestService) {
    // ===================================================adviceRequest====================================================
    $window.localStorage["currentItem"] = "adviceRequest";

    // 列
    var columns = [
      {
        field: "adviceNo",
        template: "<a ui-sref='main.adviceRequestRead({id:dataItem.id})'>#: adviceNo # </a>",
        headerTemplate: "<span translate='ADVICE_NO'></span>"
      },
      {
        field: "expectedDelivery", template: function (dataItem) {
        return dataItem.expectedDelivery.substring(0, 19);
      }, headerTemplate: "<span translate='EXPECTED_DELIVERY'></span>"
      },
      {field: "adviceState", headerTemplate: "<span translate='ADVICE_STATE'></span>"},
      {field: "size", headerTemplate: "<span translate='AMOUNT'></span>"}

    ];
    $scope.adviceRequestGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("adviceRequest")
    });
    //
    $rootScope.changeClient = function (clientId) {
      $scope.currentClientId = clientId;
    };
    //删除送货单
    $rootScope.delete1 = function () {
      var grid = $("#adviceRequestGrid").data("kendoGrid");
      var rows = grid.select();
      if (rows.length) {
        var rowData = grid.dataItem(rows[0]);
        var adviceState = rowData.adviceState;
        if (adviceState !== "Fulfilled") {
          commonService.dialogMushiny($scope.window, {
            open: function (win) {
              $rootScope.deleteSure = function () {
                masterService.deleteOne("adviceRequest", rowData.id, function () {
                  win.close();
                  grid.dataSource.read(); // 刷新表格
                });
              };
            }
          });
        }else{
          dialogMushiny($scope.window, {width: 320, height: 160, type: "warn", open: function(){
            setTimeout(function(){
              $("#warnContent").html($translate.instant(datas.data.message))
            }, 200);
          }});
        }
        ;
      }
      ;
    };
    // =================================================adviceRequestPosition================================================
    // 函数
    function itemDataEditor(container, options) {
      masterService.selectEditor(container, options, {
        serverFiltering: false,
        transport: {
          read: function (options) {
            adviceRequestService.getItemData($scope.currentClientId, function (datas) {
              options.success(datas);
            });
          }
        }
      });
    }

    // adviceRequestPosition-column
    var adviceRequestPositionColumns = [
      {
        field: "positionNo",
        editor: masterService.numberEditor,
        headerTemplate: "<span translate='POSITION_NO'></span>"
      },
      {
        field: "notifiedAmount",
        editor: masterService.numberEditor,
        headerTemplate: "<span translate='NOTIFIED_AMOUNT'></span>"
      },
      {
        field: "itemData",
        headerTemplate: "<span translate='ITEM_DATA'></span>",
        editor: itemDataEditor,
        template: function (item) {
          return item.itemData ? item.itemData.name : "";
        }
      }
    ];
    $rootScope.adviceRequestPositionGridOptions = masterService.editGrid({
      height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 2 - 10 - 20 - 20 - 40),
      columns: adviceRequestPositionColumns
    });
    //锁定DN
    $scope.lock = function () {
      var grid = $("#adviceRequestGrid").data("kendoGrid");
      var rows = grid.select();
      if (rows.length) {
        var rowData = grid.dataItem(rows[0]);
        adviceRequestService.lock(rowData.id, function () {
          $state.go("main.advice_request");
        });
      }
    }
  }).controller("adviceRequestCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        var adviceRequestPositionGrid = $("#adviceRequestPositionGrid").data("kendoGrid"),
          datas = adviceRequestPositionGrid.dataSource.data();
        for (var i = 0, details = []; i < datas.length; i++) {
          var data = datas[i];
          details.push({
            "positionNo": data.positionNo,
            "notifiedAmount": data.notifiedAmount,
            "itemDataId": data.itemData ? data.itemData.id : null,
            "clientId": data.itemData.clientId
          });
        }
        masterService.create("adviceRequest", {
          "expectedDelivery": $scope.expectedDelivery,
          "clientId": $scope.client ? $scope.client.id : null,
          "positions": details
        }, function () {
          $state.go("main.advice_request");
        });
      }
    };
  }).controller("adviceRequestReadCtl", function ($scope, $stateParams, masterService) {
    masterService.read("adviceRequest", $stateParams.id, function (data) {
      $("#expectedDelivery").val(data.expectedDelivery.substring(0, 19));
      for (var k in data) $scope[k] = data[k];
      var grid = $("#adviceRequestPositionGrid").data("kendoGrid");
      grid.setOptions({"editable": false});
      grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
    });
  });
})();