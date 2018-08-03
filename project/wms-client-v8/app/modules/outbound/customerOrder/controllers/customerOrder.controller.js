/**
 * Created by xiaoning.xiong on 2017/05/28.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("customerOrderCtl", function ($scope, $window, $rootScope, commonService, outboundService, customerOrderService) {
        // ===================================================customerOrder====================================================
        $window.localStorage["currentItem"] = "customerOrder";

        // 列
        var columns = [
            {
                field: "orderNo",
                template: "<a ui-sref='main.customerOrderRead({id:dataItem.id})'>#: orderNo # </a>",
                headerTemplate: "<span translate='ORDER_NO'></span>"
            },
            {field: "sortCode", headerTemplate: "<span translate='SORT_CODE'></span>"},
            {
                field: "deliveryDate",
                headerTemplate: "<span translate='DELIVERY_DATE'></span>",
                template: function (dataItem) {
                    return dataItem.deliveryDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(dataItem.deliveryDate)) : "";
                }
            },
            {field: "customerName", headerTemplate: "<span translate='CUSTOMER_NAME'></span>"},
            {field: "customerNo", headerTemplate: "<span translate='CUSTOMER_NO'></span>"},
            //{field: "documentUrl", headerTemplate: "<span translate='DOCUMENT_URL'></span>"},
            //{field: "labelUrl", headerTemplate: "<span translate='LABEL_URL'></span>"},
            //{field: "priority", headerTemplate: "<span translate='PRIORITY'></span>"},
            //{field: "state", headerTemplate: "<span translate='STATE'></span>"},
            //{field: "storageLocation", headerTemplate: "<span translate='STORAGE_LOCATION'></span>", template: function(item){
            //  return item.storageLocation? item.storageLocation.name: "";
            //}},
            {field: "strategy.name",
                headerTemplate: "<span translate='ORDER_STRATEGY'></span>",
                template: function (dataItem) {
                    return dataItem.strategy ? dataItem.strategy.name : "";
                }}
        ];
        $scope.customerOrderGridOptions = commonService.gridMushiny({
            dataSource: outboundService.getGridDataSource("customerOrder"),
            columns: columns
        });

        //
        $rootScope.changeClient = function (clientId) {
            $scope.currentClientId = clientId;
            $scope.storageLocation = {};
            $scope.sortCode = {};
            /*customerOrderService.getExpectSendDelivery(clientId, function(datas){
             var comboBox = $("#sortCode").data("kendoComboBox");
             comboBox.value("");
             for(var i = 0; i < datas.length; i++){
             var data = datas[i];
             data["se"] = data.delivery+ "/"+ data.exsdTime;
             }
             comboBox.setDataSource(new kendo.data.DataSource({data: datas}));
             });*/
            customerOrderService.getOrderStrategy(clientId, function (datas) {
                var comboBox = $("#orderStrategy").data("kendoComboBox");
                comboBox.value("");
                comboBox.setDataSource(new kendo.data.DataSource({data: datas}));
            });
        };

        // =================================================customerOrderPosition================================================
        // 函数
        function itemDataEditor(container, options) {
            outboundService.selectEditor(container, options, {
                serverFiltering: false,
                transport: {
                    read: function (options) {
                        customerOrderService.getItemData($scope.currentClientId, function (datas) {
                            options.success(datas);
                        });
                    }
                }
            });
        }

        var customerOrderPositionColumns = [
            {
                field: "positionNo",
                width: 60,
                editor: outboundService.numberEditor,
                headerTemplate: "<span translate='POSITION_NO'></span>"
            },
            {
                field: "itemData",
                width: 180,
                headerTemplate: "<span translate='ITEM_DATA'></span>",
                editor: itemDataEditor,
                template: function (item) {
                    return item.itemData ? item.itemData.name : "";
                }
            },
            {
                field: "amount",
                width: 60,
                editor: outboundService.numberEditor,
                headerTemplate: "<span translate='AMOUNT'></span>"
            },
            //{field: "amountPicked", width: 60, editor: commonService.numberEditor, headerTemplate: "<span translate='AMOUNT_PICKED'></span>"},
            //{field: "orderIndex", width: 60, editor: commonService.numberEditor, headerTemplate: "<span translate='ORDER_INDEX'></span>"},
            //{field: "partitionAllowed", headerTemplate: "<span translate='PARTITION_ALLOWED'></span>"},
            {field: "serialNo", width: 80, headerTemplate: "<span translate='SERIAL_NO'></span>"}
            //{field: "state", width: 70, headerTemplate: "<span translate='STATE'></span>"}
        ];
        $rootScope.customerOrderPositionGridOptions = outboundService.editGrid({
            height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 6 - 15 - 20 - 20 - 40),
            columns: customerOrderPositionColumns
        });

    }).controller("customerOrderCreateCtl", function ($scope, $rootScope, $state, outboundService, customerOrderService) {
        $scope.client = {};
        /*$scope.selectStorageLocation = function(client){
         if(client == null) return;
         $rootScope.selectInWindow({
         title: "STORAGE_LOCATION",
         srcKey: "storageLocation",
         srcColumns: [
         {"field": "name", headerTemplate: "<span translate='NAME'></span>"},
         {field: "storageLocationType.name", headerTemplate: "<span translate='STORAGE_LOCATION_TYPE'></span>"},
         ],
         init: function(options){
         options.showClient = true;
         options.disableClient = true;
         $rootScope.client = client;
         },
         back: function(data){
         $scope.storageLocation = {id: data.id, name: data.name};
         }
         });
         };*/
        $scope.changeSortCode = function (sortCodeId) {
            $scope.currentSortCodeId = sortCodeId;
            //获取发货时间点
            customerOrderService.getDeliveryTime(sortCodeId, function (datas) {
                $scope.deliveryTime={};
                $scope.deliveryTimeData=[];
                for(var i=0;i<datas.length;i++){
                    $scope.deliveryTimeData.push(datas[i].timeDTO)
                }

                // console.log(datas);
                // var comboBox = $("#deliveryDate").data("kendoComboBox");
                // comboBox.value("");
                // /*for(var i = 0; i < datas.length; i++){
                //  var data = datas[i];
                //  data["se"] = data.delivery+ "/"+ data.exsdTime;
                //  }*/
                // comboBox.setDataSource(new kendo.data.DataSource({data: datas.timeDTO}));
            })
        };

        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                var customerOrderPositionGrid = $("#customerOrderPositionGRID").data("kendoGrid"), datas = customerOrderPositionGrid.dataSource.data();
                for (var i = 0, details = []; i < datas.length; i++) {
                    var data = datas[i];
                    details.push({
                        "positionNo": data.positionNo,
                        "serialNo": data.serialNo,
                        //"orderIndex": 0,//data.orderIndex,
                        "amount": data.amount,
                        "amountPicked": 0,
                        "state": 0,
                        "partitionAllowed": "false",
                        "itemDataId": data.itemData ? data.itemData.id : null,
                        "clientId": data.itemData.clientId
                    });
                }
                outboundService.create("customerOrder", {
                    "customerName": $scope.customerName,
                    "customerNo": $scope.customerNo,
                    "sortCode": $scope.sortCode ? $scope.sortCode.sortCode : "",
                    "deliveryDate": $scope.deliveryTime ? $scope.deliveryTime.time : "",
                    //"documentUrl": $scope.documentUrl,
                    //"externalNo": $scope.externalNo,
                    //"labelUrl": $scope.labelUrl,
                    "orderNo": $scope.orderNo,
                    "priority": 1,
                    "state": 0,
                    "destinationId": $scope.storageLocation ? $scope.storageLocation.id : null,
                    "strategyId": $scope.orderStrategy ? $scope.orderStrategy.id : null,
                    "clientId": $scope.client ? $scope.client.id : null,
                    "positions": details
                }, function () {
                    $state.go("main.customer_order");
                },function (data) {
                    $scope.errorWindow("errorId", $scope.errorWindows);
                    $scope.message = data.values[0];
                });
            }
        };
        // 错误弹窗
        $scope.errorWindow = function (windowId, windowName) {
            $("#" + windowId).parent().addClass("myWindow");
            windowName.setOptions({
                width: 400,
                height: 200,
                visible: false,
                closable: true,
                actions: false,
            });
            windowName.center();
            windowName.open();
        };
    }).controller("customerOrderUpdateCtl", function ($scope, $rootScope, $state, $stateParams, outboundService) {
        outboundService.read("customerOrder", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            $rootScope.changeClient($scope.client.id);
            var grid = $("#customerOrderPositionGRID").data("kendoGrid");
            grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
        });

        $scope.selectStorageLocation = function (client) {
            if (client == null) return;
            $rootScope.selectInWindow({
                title: "STORAGE_LOCATION",
                srcKey: "storageLocation",
                srcColumns: [
                    {"field": "name", headerTemplate: "<span translate='NAME'></span>"},
                    {
                        field: "storageLocationType.name",
                        headerTemplate: "<span translate='STORAGE_LOCATION_TYPE'></span>"
                    },
                ],
                init: function (options) {
                    options.showClient = true;
                    options.disableClient = true;
                    $rootScope.client = client;
                },
                back: function (data) {
                    $scope.storageLocation = {id: data.id, name: data.name};
                }
            });
        };

        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                var customerOrderPositionGrid = $("#customerOrderPositionGRID").data("kendoGrid"), datas = customerOrderPositionGrid.dataSource.data();
                for (var i = 0, details = []; i < datas.length; i++) {
                    var data = datas[i];
                    details.push({
                        "id": data.id || null,
                        "positionNo": data.positionNo,
                        "serialNo": data.serialNo,
                        "orderIndex": data.orderIndex,
                        "amount": data.amount,
                        "amountPicked": data.amountPicked,
                        "state": data.state,
                        "partitionAllowed": "false",
                        "itemDataId": data.itemData ? data.itemData.id : null,
                        "clientId": data.itemData.client.id
                    });
                }
                outboundService.update("customerOrder", {
                    "id": $scope.id,
                    "customerName": $scope.customerName,
                    "customerNo": $scope.customerNo,
                    "sortCode": $scope.sortCode ? $scope.sortCode.delivery : "",
                    "deliveryDate":  $scope.deliveryTime ? $scope.sortCode.time : "",
                    "documentUrl": $scope.documentUrl,
                    "externalNo": $scope.externalNo,
                    "labelUrl": $scope.labelUrl,
                    "orderNo": $scope.orderNo,
                    "priority": $scope.priority,
                    "state": $scope.state,
                    "destinationId": $scope.storageLocation ? $scope.storageLocation.id : null,
                    "strategyId": $scope.orderStrategy ? $scope.orderStrategy.id : null,
                    "clientId": $scope.client ? $scope.client.id : null,
                    "positions": details
                }, function () {
                    $state.go("main.customer_order");
                });
            }
        };
    }).controller("customerOrderReadCtl", function ($scope, $stateParams, outboundService) {
        outboundService.read("customerOrder", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            var grid = $("#customerOrderPositionGRID").data("kendoGrid");
            grid.setOptions({"editable": false});
            grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
        });
    });
})();