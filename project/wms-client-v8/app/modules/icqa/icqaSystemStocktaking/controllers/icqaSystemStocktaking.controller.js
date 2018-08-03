/**
 * Created by juping.li on 2017/8/30
 */
(function () {
    'use strict';
    angular.module('myApp').controller("icqaSystemStocktakingCtl", function ($scope, $window, $rootScope, commonService, ICQABaseService, icqaSystemStocktakingService) {

        $scope.icqaDetailPage = "ONE";

        $scope.inventoryStaff = false;
        $scope.stocktakingDetail = false;
        // $scope.state = "raw";
        $scope.personnelArr = [];
        $scope.newPersonnelArr = [];
        $scope.personnel = "";
        $scope.inventoryUser = false;
        $scope.inventoryUserToAssign = false;
        $scope.times = 1;

        $("#defaultOneInventoryId").removeClass("buttonColorGray");
        $("#defaultTwoInventoryId").addClass("buttonColorGray");
        $("#defaultThreeInventoryId").addClass("buttonColorGray");
        $("#defaultFourInventoryId").addClass("buttonColorGray");

        // 选中全部
        $scope.selectAll = function () {
            var grid = $('#stocktakingOrdersGrid').data('kendoGrid');
            if ($scope.select_all) {
                $scope.select_one = true;
                grid.tbody.children('tr').addClass('k-state-selected');
                //grid.select(grid.tbody.find(">tr"));
            } else {
                $scope.select_one = false;
                grid.tbody.children('tr').removeClass('k-state-selected');
            }
        };
        //选中单个
        $scope.chk = false;
        $scope.selectOne = function (val, uid) {
            var grid = $('#stocktakingOrdersGrid').data('kendoGrid');
            if (val) {
                grid.tbody.children('tr[data-uid="' + uid + '"]').addClass('k-state-selected');
                // grid.select("tr[data-uid='" + uid + "']");
            } else {
                grid.tbody.children('tr[data-uid="' + uid + '"]').removeClass('k-state-selected');
            }
        };

        var columnsSelect = [
            {headerTemplate: "<span translate='选择'></span>", width: 35, template: "<input type=\"checkbox\"  ng-model='chk' id='dataItem.id' class='check-box' ng-checked = 'select_one' ng-click='selectOne(chk,dataItem.uid)'/>"},
            {field: "stocktakingNo", headerTemplate: "<span translate='盘点编号'></span>"},
            // {field: "itemNo",headerTemplate:"<span translate='SKU_ID'></span>"},
            // {field: "itemName",headerTemplate:"<span translate='商品名称'></span>"},
            // // {field: "goodLocation",headerTemplate:"<span translate='盘点货位'></span>"},
            {field: "totalQty", headerTemplate: "<span translate='盘点总数'></span>"},
            {field: "okQty", headerTemplate: "<span translate='盘点完成数量'></span>"},
            {field: "ngdiffQty", headerTemplate: "<span translate='差异货位数量'></span>"},
            {field: "ngQty", headerTemplate: "<span translate='剩余差异货位数量'></span>"},
            {field: "dpmo", headerTemplate: "<span translate='差异货位‰'></span>",template: function (item) {
                return parseInt(item.dpmo);
            }
            },{
                field: "createdDate", headerTemplate: "<span translate='开始时间'></span>", template: function (item) {
                    if (item.createdDate) {
                        return kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.createdDate))
                    } else {
                        return "";
                    }
                }
            },
            {field: "createdByUser", headerTemplate: "<span translate='操作人'></span>"},
            {field: "state", headerTemplate: "<span translate='状态'></span>",template: function(item){
                var value = "";
                if(item.state < 100){
                    value =  "任务未生成";
                }else if(item.state == 100){
                    value =  "任务生成";
                }else if(item.state == 300){
                    value =  "盘点中";
                }else if(item.state == 700){
                    value =  "复盘";
                }else if(item.state == 900){
                    value =  "关闭";
                }
                return value;
            }
            },
            {field: "dfQty", headerTemplate: "<span translate='差异总数'></span>"},
            {
                field: "closeDate", headerTemplate: "<span translate='关闭时间'></span>", template: function (item) {
                if (item.closeDate) {
                    return kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.closeDate))
                } else {
                    return "";
                }
            }
            }
        ];
        inventoryCount("");
        icqaSystemStocktakingService.selectRoundOfInventory($scope.times, $scope.inventoryInformation, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
            console.log(data);
            $scope.stocktakingOrdersGridOption = ICQABaseService.grid(data, columnsSelect, $(document.body).height() - 240);
        });


        $scope.defaultOneInventory = function () {
            $scope.times = 1;
            inventoryCount("");
            $("#defaultOneInventoryId").removeClass("buttonColorGray");
            $("#defaultTwoInventoryId").addClass("buttonColorGray");
            $("#defaultThreeInventoryId").addClass("buttonColorGray");
            $("#defaultFourInventoryId").addClass("buttonColorGray");
            icqaSystemStocktakingService.selectRoundOfInventory($scope.times, $scope.inventoryInformation, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                console.log(data);
                var grid = $("#stocktakingOrdersGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: data}));
            });
        };
        $scope.defaultTwoInventory = function () {
            $scope.times = 2;
            inventoryCount("");
            $("#defaultOneInventoryId").addClass("buttonColorGray");
            $("#defaultTwoInventoryId").removeClass("buttonColorGray");
            $("#defaultThreeInventoryId").addClass("buttonColorGray");
            $("#defaultFourInventoryId").addClass("buttonColorGray");
            icqaSystemStocktakingService.selectRoundOfInventory($scope.times, $scope.inventoryInformation, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                var grid = $("#stocktakingOrdersGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: data}));
            });
        };

        $scope.defaultThreeInventory = function () {
            $scope.times = 3;
            inventoryCount("");
            $("#defaultOneInventoryId").addClass("buttonColorGray");
            $("#defaultTwoInventoryId").addClass("buttonColorGray");
            $("#defaultThreeInventoryId").removeClass("buttonColorGray");
            $("#defaultFourInventoryId").addClass("buttonColorGray");
            icqaSystemStocktakingService.selectRoundOfInventory($scope.times, $scope.inventoryInformation, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                var grid = $("#stocktakingOrdersGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: data}));
            });
        };

        $scope.defaultFourInventory = function(){
            $scope.times = 4;
            inventoryCount("");
            $("#defaultOneInventoryId").addClass("buttonColorGray");
            $("#defaultTwoInventoryId").addClass("buttonColorGray");
            $("#defaultThreeInventoryId").addClass("buttonColorGray");
            $("#defaultFourInventoryId").removeClass("buttonColorGray");
            icqaSystemStocktakingService.selectRoundOfInventory($scope.times, $scope.inventoryInformation, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                var grid = $("#stocktakingOrdersGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: data}));
            });
        };
        // 生成盘点任务
        $scope.inventoryTask = function(){
            var grid = $("#stocktakingOrdersGrid").data("kendoGrid");
            var rows = grid.select();
            var dataFiled = "";
            for (var i = 0; i < rows.length; i++) {
                var rowData = grid.dataItem(rows[i]);
                if (i == 0) {
                    dataFiled = rowData.id;
                } else {
                    dataFiled += "," + rowData.id;
                }
            }
            // if(rows.length > 0) {
            //     var rowData = grid.dataItem(rows[0]);
            //     // var data = [{
            //     //     "state": $scope.state,
            //     //     "stocktakingId": $scope.stocktakingId,
            //     //     "itemNo": rowData.itemNo,
            //     //     "itemName": rowData.itemName
            //     // }];
            // }
            if(rowData.state < 100 ){
                icqaSystemStocktakingService.createInventoryTask(dataFiled, function () {
                    $scope.select_all = false;
                    $scope.defaultOneInventory();
                    $scope.state = "100";
                });
            }else{
                $scope.stocktakingTitle = "盘点任务已生成";
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
            }
        }

        $scope.searchICQADetail = function () {
            icqaSystemStocktakingService.selectRoundOfInventory($scope.times, $scope.inventoryInformation, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                var grid = $("#stocktakingOrdersGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: data}));
            })
        };

        $scope.searchICQADetailTime = function () {
            icqaSystemStocktakingService.selectRoundOfInventory($scope.times, $scope.inventoryInformation, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                var grid = $("#stocktakingOrdersGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: data}));
            });
            inventoryCount("");
        };

        $scope.oneInventory = function () {
            $("#userNameId").data("kendoComboBox").value("");
            $scope.select_one_sku = false;
            $scope.select_all_sku = false;
            if ($scope.stocktakingId) {
                icqaSystemStocktakingService.getStocktaking0rderUser($window.localStorage["clientId"], function (data) {
                    $scope.usersSource = data;
                });
                inventoryCount($scope.stocktakingId);
                $scope.inventoryStaff = true;
                $scope.inventory = [];
                $scope.personnelArr = [];
                $scope.newPersonnelArr = [];
                $scope.times = 1;

                $("#oneInventoryId").removeClass("buttonColorGray");
                $("#twoInventoryId").addClass("buttonColorGray");
                $("#threeInventoryId").addClass("buttonColorGray");
                $("#fourInventoryId").addClass("buttonColorGray");
                //判断二轮三轮是否指定过人了
                icqaSystemStocktakingService.checkInventory($scope.stocktakingId, $scope.times,"system", function (v) {
                    if (v.length > 0) {
                        for (var i = 0; i < v.length; i++) {
                            $scope.newPersonnelArr.push({id: v[i].userDTO.id, username: v[i].userDTO.username});
                        }
                    }
                    //得到盘点中的一条数据
                    icqaSystemStocktakingService.selectRoundOfInventoryId($scope.times, $scope.stocktakingId, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                        var grid = $("#stocktakingSelectGridONEId").data("kendoGrid");
                        grid.setDataSource(new kendo.data.DataSource({data: data}));
                    });
                    //得到一轮盘点详情
                    $scope.getUserList($scope.stocktakingId, $scope.times);
                });
            } else {
                $("#stocktakingDetailsId").parent().addClass("mySelect");
                $scope.stocktakingDetailsWindow.setOptions({
                    width: 600,
                    height: 150,
                    visible: false,
                    actions: false
                });
                $scope.stocktakingDetailsWindow.center();
                $scope.stocktakingDetailsWindow.open();
            }
        };
        $scope.twoInventory = function () {
            $("#userNameId").data("kendoComboBox").value("");
            $scope.select_one_sku = false;
            $scope.select_all_sku = false;
            if ($scope.stocktakingId) {
                icqaSystemStocktakingService.getStocktaking0rderUser($window.localStorage["clientId"], function (data) {
                    $scope.usersSource = data;
                });
                $scope.times = 2;
                inventoryCount($scope.stocktakingId);
                $scope.inventoryStaff = true;
                $scope.inventory = [];
                $scope.personnelArr = [];
                $scope.newPersonnelArr = [];


                $("#oneInventoryId").addClass("buttonColorGray");
                $("#twoInventoryId").removeClass("buttonColorGray");
                $("#threeInventoryId").addClass("buttonColorGray");
                $("#fourInventoryId").addClass("buttonColorGray");


                //判断二轮三轮是否指定过人了
                icqaSystemStocktakingService.checkInventory($scope.stocktakingId, $scope.times,"system", function (v) {
                    if (v.length > 0) {
                        for (var i = 0; i < v.length; i++) {
                            $scope.newPersonnelArr.push({id: v[i].userDTO.id, username: v[i].userDTO.username});
                        }
                    }
                    //得到盘点中的一条数据
                    icqaSystemStocktakingService.selectRoundOfInventoryId($scope.times, $scope.stocktakingId, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                        var grid = $("#stocktakingSelectGridONEId").data("kendoGrid");
                        grid.setDataSource(new kendo.data.DataSource({data: data}));
                    });
                    $scope.getUserList($scope.stocktakingId, $scope.times);
                    //得到盘点详情
                    // icqaSystemStocktakingService.select0rdersByStocktakingIds($scope.stocktakingId, $scope.times, function (res) {
                    //     $scope.inventory = res;
                    //     var notInventory = 0;
                    //     for (var i = 0; i < $scope.inventory.length; i++) {
                    //         if ($scope.inventory[i].state == "未盘点") {
                    //             notInventory++;
                    //         }
                    //     }
                    //     if (notInventory == 0 && $scope.newPersonnelArr.length > 0) {
                    //         $scope.inventoryUserToAssign = true;
                    //         $("#userNameId").attr("readonly", "readonly");
                    //     } else {
                    //         $scope.inventoryUserToAssign = false;
                    //         $("#userNameId").removeAttr("readonly");
                    //     }
                    //     var grid = $("#stocktakingDetailsGridId").data("kendoGrid");
                    //     grid.setDataSource(new kendo.data.DataSource({data: res}));
                    // });

                });
            } else {
                $("#stocktakingDetailsId").parent().addClass("mySelect");
                $scope.stocktakingDetailsWindow.setOptions({
                    width: 600,
                    height: 150,
                    visible: false,
                    actions: false
                });
                $scope.stocktakingDetailsWindow.center();
                $scope.stocktakingDetailsWindow.open();
            }
        };

        $scope.threeInventory = function () {
            $("#userNameId").data("kendoComboBox").value("");
            $scope.select_one_sku = false;
            $scope.select_all_sku = false;
            if ($scope.stocktakingId) {
                icqaSystemStocktakingService.getStocktaking0rderUser($window.localStorage["clientId"], function (data) {
                    $scope.usersSource = data;
                });
                $scope.times = 3;
                inventoryCount($scope.stocktakingId);
                $scope.inventoryStaff = true;
                $scope.inventory = [];
                $scope.personnelArr = [];
                $scope.newPersonnelArr = [];


                $("#oneInventoryId").addClass("buttonColorGray");
                $("#twoInventoryId").addClass("buttonColorGray");
                $("#threeInventoryId").removeClass("buttonColorGray");
                $("#fourInventoryId").addClass("buttonColorGray");
                //判断二轮三轮是否指定过人了
                icqaSystemStocktakingService.checkInventory($scope.stocktakingId, $scope.times,"system", function (v) {
                    if (v.length > 0) {
                        for (var i = 0; i < v.length; i++) {
                            $scope.newPersonnelArr.push({id: v[i].userDTO.id, username: v[i].userDTO.username});
                        }
                    }
                    icqaSystemStocktakingService.selectRoundOfInventoryId($scope.times, $scope.stocktakingId, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                        var grid = $("#stocktakingSelectGridONEId").data("kendoGrid");
                        grid.setDataSource(new kendo.data.DataSource({data: data}));
                    });
                    $scope.getUserList($scope.stocktakingId, $scope.times);
                });
            } else {
                $("#stocktakingDetailsId").parent().addClass("mySelect");
                $scope.stocktakingDetailsWindow.setOptions({
                    width: 600,
                    height: 150,
                    visible: false,
                    actions: false
                });
                $scope.stocktakingDetailsWindow.center();
                $scope.stocktakingDetailsWindow.open();
            }
        };

        $scope.fourInventory = function(){
            $("#userNameId").data("kendoComboBox").value("");
            $scope.select_one_sku = false;
            $scope.select_all_sku = false;
            if ($scope.stocktakingId) {
                icqaSystemStocktakingService.getStocktaking0rderUser($window.localStorage["clientId"], function (data) {
                    $scope.usersSource = data;
                });
                $scope.times = 4;
                inventoryCount($scope.stocktakingId);
                $scope.inventoryStaff = true;
                $scope.inventory = [];
                $scope.personnelArr = [];
                $scope.newPersonnelArr = [];

                $("#oneInventoryId").addClass("buttonColorGray");
                $("#twoInventoryId").addClass("buttonColorGray");
                $("#threeInventoryId").addClass("buttonColorGray");
                $("#fourInventoryId").removeClass("buttonColorGray");
                //判断二轮，三轮是否指定过人了
                icqaSystemStocktakingService.checkInventory($scope.stocktakingId, $scope.times,"system", function (v) {
                    if (v.length > 0) {
                        for (var i = 0; i < v.length; i++) {
                            $scope.newPersonnelArr.push({id: v[i].userDTO.id, username: v[i].userDTO.username});
                        }
                    }
                    icqaSystemStocktakingService.selectRoundOfInventoryId($scope.times, $scope.stocktakingId, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                        var grid = $("#stocktakingSelectGridONEId").data("kendoGrid");
                        grid.setDataSource(new kendo.data.DataSource({data: data}));
                    });
                    $scope.getUserList($scope.stocktakingId, $scope.times);
                });
            } else {
                $("#stocktakingDetailsId").parent().addClass("mySelect");
                $scope.stocktakingDetailsWindow.setOptions({
                    width: 600,
                    height: 150,
                    visible: false,
                    actions: false
                });
                $scope.stocktakingDetailsWindow.center();
                $scope.stocktakingDetailsWindow.open();
            }
        };

        $scope.getUserList = function (stocktakingId,times) {
            //得到盘点详情
            var url = "icqa/systemStocktaking-orders/details-webs/page?stocktakingId="+ stocktakingId + "&times=" + times;
            $scope.stocktakingDetail = true;
            $scope.date = ICQABaseService.getGridDataSource1(url);
            var grid = $("#stocktakingDetailsGridId").data("kendoGrid");
            grid.setOptions(commonService.gridMushiny({
                    columns: columnsDetails,
                    dataSource: $scope.date,
                    height: $(document.body).height() - 390
                })
            )
            $scope.inventory = $scope.date;
            var notInventory = 0;
            for (var i = 0; i < $scope.inventory.length; i++) {
                if ($scope.inventory[i].state == "未盘点") {
                    notInventory++;
                }
            }
            if (notInventory == 0 && $scope.newPersonnelArr.length > 0) {
                $scope.inventoryUserToAssign = true;
                $("#userNameId").attr("readonly", "readonly");
            } else {
                $scope.inventoryUserToAssign = false;
                $("#userNameId").removeAttr("readonly");
            }

            // icqaSystemStocktakingService.select0rdersByStocktakingIds(stocktakingId, times, function (res) {
            //     $scope.stocktakingDetail = true;
            //     $scope.inventory = res;
            //     var notInventory = 0;
            //     for (var i = 0; i < $scope.inventory.length; i++) {
            //         if ($scope.inventory[i].state == "未盘点") {
            //             notInventory++;
            //         }
            //     }
            //     if (notInventory == 0 && $scope.newPersonnelArr.length > 0) {
            //         $scope.inventoryUserToAssign = true;
            //         $("#userNameId").attr("readonly", "readonly");
            //     } else {
            //         $scope.inventoryUserToAssign = false;
            //         $("#userNameId").removeAttr("readonly");
            //     }
            //     var grid = $("#stocktakingDetailsGridId").data("kendoGrid");
            //     grid.setDataSource(new kendo.data.DataSource({data: res}));
            // });
        }

        $scope.inventoryUsers = function (v) {
            if ($scope.newPersonnelArr.length > 0) {
                for (var i = 0; i < $scope.newPersonnelArr.length; i++) {
                    if (v.id == $scope.newPersonnelArr[i].id) {
                        $scope.inventoryUser = false;
                        break;
                    } else {
                        $scope.inventoryUser = true;
                    }
                }
            } else {
                $scope.personnelArr.push({id: v.id, username: v.username});
            }
            if($scope.inventoryUser) {
                $scope.personnelArr.push({id: v.id, username: v.username});
            }
        };

        $scope.deletePersonnelArr = function (id) {
            for (var i = 0; i < $scope.personnelArr.length; i++) {
                if (id == $scope.personnelArr[i].id) {
                    $scope.personnelArr.splice(i, 1);
                }
            }
            if ($scope.stocktakingId) {
                $scope.personnelArr = $scope.personnelArr.concat($scope.newPersonnelArr);
                $scope.newPersonnelArr = [];
                var temp = [];
                for (var i = 0; i < $scope.personnelArr.length; i++) {
                    temp.push({
                        "state": "90",
                        "systemStocktakingId": $scope.stocktakingId,
                        "times": $scope.times,
                        "userId": $scope.personnelArr[i].id,
                        "storageLocationId": $scope.personnelArr[i].storageLocationId,
                        "orderIndex": $scope.personnelArr[i].orderIndex,
                        "orderBy": "ASC"
                    })
                }
                console.log("sss1",temp);
                //删除盘点人员
                icqaSystemStocktakingService.saveStocktakingUser(temp, function () {
                    $scope.inventoryUser = false;
                    $("#inventoryPersonnelId").parent().addClass("mySelect");
                    $scope.assignUser = "盘点人员删除成功";
                    $scope.inventoryPersonnelWindow.setOptions({
                        width: 600,
                        height: 150,
                        visible: false,
                        actions: false
                    });
                    $scope.inventoryPersonnelWindow.center();
                    $scope.inventoryPersonnelWindow.open();
                })

                icqaSystemStocktakingService.deleteUsers($scope.stocktakingId, $scope.times,"system", function () {

                });
            }
        };

        $scope.backAll = function () {
            $scope.icqaDetailPage = "ONE";
            icqaSystemStocktakingService.selectRoundOfInventory($scope.times, $scope.inventoryInformation, $scope.createdDateOne, $scope.createdDateTwo, function (data) {
                console.log(data);
                var grid = $("#stocktakingOrdersGrid").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: data}));
            });
        };
        var columnsSelectOne = [
            {field: "stocktakingNo", width: "90px", headerTemplate: "<span translate='盘点编号'></span>"},
            {field: "itemNo",width: "100px",headerTemplate:"<span translate='SKU_ID'></span>"},
            {field: "itemName",width: "300px",headerTemplate:"<span translate='商品名称'></span>"},
            {field: "totalQty",width: "100px", headerTemplate: "<span translate='盘点总数'></span>"},
            {field: "okQty", width: "100px",headerTemplate: "<span translate='盘点完成数量'></span>"},
            {field: "ngdiffQty",width: "100px", headerTemplate: "<span translate='差异货位数量'></span>"},
            {field: "ngQty", width: "100px",headerTemplate: "<span translate='剩余差异货位数量'></span>"},
            {field: "dpmo",width: "100px", headerTemplate: "<span translate='差异货位‰'></span>",template: function (item) {
                return parseInt(item.dpmo);
            }
            },{
                field: "createdDate",width: "100px", headerTemplate: "<span translate='开始时间'></span>", template: function (item) {
                    if (item.createdDate) {
                        return kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.createdDate))
                    } else {
                        return "";
                    }
                }
            },
            {field: "createdByUser",width: "100px", headerTemplate: "<span translate='创建人'></span>"},
            // {field: "state", headerTemplate: "<span translate='状态'></span>",template: function(item){
            //     var value = "";
            //     if(item.state < 100){
            //         value =  "任务未生成";
            //     }else if(item.state == 100){
            //         value =  "任务生成";
            //     }else if(item.state == 300){
            //         value =  "盘点中";
            //     }else if(item.state == 700){
            //         value =  "复盘";
            //     }else if(item.state == 900){
            //         value =  "关闭";
            //     }
            //     return value;
            // }
            // },
            // {
            //     field: "closeDate",width: "100px", headerTemplate: "<span translate='关闭时间'></span>", template: function (item) {
            //     if (item.endedDate) {
            //         return kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.endedDate))
            //     } else {
            //         return "";
            //     }
            // }
            // }
        ];

        var columnsDetails = [
            {headerTemplate: "<span translate='选择'></span>", width: 35, template: "<input type=\"checkbox\"  ng-model='chk' id='dataItem.id' class='check-box' ng-checked = 'select_one_sku' ng-click='selectOneSku(chk,dataItem.uid)'/>"},
            {field: "itemNo", headerTemplate: "<span translate='SKU_ID'></span>"},
            {field: "itemName", headerTemplate: "<span translate='商品名称'></span>"},
            {field: "locationName", headerTemplate: "<span translate='货位'></span>"},
            // {field: "createdByUser", headerTemplate: "<span translate='盘点人员'></span>"},
            {field: "operatorId", headerTemplate: "<span translate='盘点人员'></span>"},
            {field: "state", headerTemplate: "<span translate='货位状态'></span>"},
            {
                field: "createDate", headerTemplate: "<span translate='创建时间'></span>", template: function (item) {
                if (item.createDate) {
                    return kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.createDate))
                } else {
                    return "";
                }
            }
            },
            {
                field: "countDate", headerTemplate: "<span translate='盘点时间'></span>", template: function (item) {
                if (item.countDate) {
                    return kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.countDate))
                } else {
                    return "";
                }
            }
            },
            {field: "stocktakingName", headerTemplate: "<span translate='盘点方式'></span>"}];
        $scope.stocktakingDetailsGridOption = ICQABaseService.grid([], columnsDetails, $(document.body).height() - 400);

        $scope.stocktakingSelectGridONEOption = ICQABaseService.grid([], columnsSelectOne, 300);

        // 选中全部
        $scope.selectAllSku = function () {
            var grid = $('#stocktakingDetailsGridId').data('kendoGrid');
            if ($scope.select_all_sku) {
                $scope.select_one_sku = true;
                grid.tbody.children('tr').addClass('k-state-selected');
                //grid.select(grid.tbody.find(">tr"));
            } else {
                $scope.select_one_sku = false;
                grid.tbody.children('tr').removeClass('k-state-selected');
            }
        }
        //选中单个
        $scope.chk = false;
        $scope.selectOneSku = function (val, uid) {
            var grid = $('#stocktakingDetailsGridId').data('kendoGrid');
            if (val) {
                grid.tbody.children('tr[data-uid="' + uid + '"]').addClass('k-state-selected');
                // grid.select("tr[data-uid='" + uid + "']");
            } else {
                grid.tbody.children('tr[data-uid="' + uid + '"]').removeClass('k-state-selected');
            }
        };
        // 手动关闭
        $scope.closeStocktakingSku = function () {
            var grid = $('#stocktakingDetailsGridId').data('kendoGrid');
            debugger;
            var rows = grid.select();
            var dataFiled = "";
            for (var i = 0; i < rows.length; i++) {
                var rowData = grid.dataItem(rows[i]);
                if (i == 0) {
                    dataFiled = rowData.id;
                } else {
                    dataFiled += "," + rowData.id;
                }
            }
            if (dataFiled.length > 0){
                var closeData = {"ids":dataFiled};
                console.log("手动关闭的数据" +closeData);
                icqaSystemStocktakingService.getCloseStocktaking(closeData,function (data){
                    icqaSystemStocktakingService.select0rdersByStocktakingIds($scope.stocktakingId, $scope.times, function (res) {
                        var grid = $("#stocktakingDetailsGridId").data("kendoGrid");
                        grid.setDataSource(new kendo.data.DataSource({data: res}));
                    });


                })
            }else{
                $scope.stocktakingTitle = "请选择需要关闭的数据";
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

            }
        }


        // $scope.stocktakingSelectGridONEOption = ICQABaseService.grid([], columnsSelectOne, $(document.body).height() - 635);

        $scope.myGridChange = function () {
            $scope.icqaDetailPage = "TWO";
            var grid = $("#stocktakingOrdersGrid").data("kendoGrid");
            var row = grid.select();
            var data = grid.dataItem(row);
            $scope.stocktakingId = data.id;
            if ($scope.times == 1) {
                $scope.oneInventory();
            } else if ($scope.times == 2) {
                $scope.twoInventory();
            } else if($scope.times == 3){
                $scope.threeInventory();
            } else {
                $scope.fourInventory();
            }
            inventoryCount($scope.stocktakingId);

            icqaSystemStocktakingService.selectRoundOfInventoryId($scope.times, $scope.stocktakingId, $scope.createdDateOne, $scope.createdDateTwo, function (data) {

                var grid = $("#stocktakingSelectGridONEId").data("kendoGrid");
                grid.setDataSource(new kendo.data.DataSource({data: data}));
            });
        };
        //得到一轮，二轮，三轮剩余数量
        function inventoryCount(id){
            if(id) {
                icqaSystemStocktakingService.selectInventoryCount(id, $scope.createdDateOne, $scope.createdDateTwo ,function (data) {
                    $scope.oneInventoryCountChild = data.amount1;
                    $scope.twoInventoryCountChild = data.amount2;
                    $scope.threeInventoryCountChild = data.amount3;
                    $scope.fourInventoryCountChild = data.amount4;
                });
            }else{
                icqaSystemStocktakingService.selectInventoryCount(id,$scope.createdDateOne, $scope.createdDateTwo, function (data) {
                    $scope.oneInventoryCount = data.amount1;
                    $scope.twoInventoryCount = data.amount2;
                    $scope.threeInventoryCount = data.amount3;
                    $scope.fourInventoryCount= data.amount4;
                });
            }
        }
        $scope.stocktakingUser = function () {
            var count = [];
            if ($scope.stocktakingId) {
                if ($scope.newPersonnelArr.length > 0) {
                    for (var i = 0; i < $scope.inventory.length; i++) {
                        if ($scope.inventory[i].state == "未盘点") {
                            count.push($scope.inventory[i]);
                        }
                    }
                    $scope.inventory = count;
                }
                $scope.personnelArr = $scope.personnelArr.concat($scope.newPersonnelArr);
                $scope.newPersonnelArr = [];
                var temp = [];
                if ($scope.inventory.length >= $scope.personnelArr.length) {
                    for (var i = 0; i < $scope.personnelArr.length; i++) {
                        temp.push({
                            "state": "90",
                            "systemStocktakingId": $scope.stocktakingId,
                            "times": $scope.times,
                            "userId": $scope.personnelArr[i].id,
                            "storageLocationId": $scope.personnelArr[i].storageLocationId,
                            "orderIndex": $scope.personnelArr[i].orderIndex,
                            "orderBy": "ASC"
                        })
                    }
                    console.log("sss1",temp);
                    //保存盘点人员
                    icqaSystemStocktakingService.saveStocktakingUser(temp, function () {
                        $scope.inventoryUser = false;
                        $("#inventoryPersonnelId").parent().addClass("mySelect");
                        $scope.assignUser = "盘点分配人员成功";
                        $scope.inventoryPersonnelWindow.setOptions({
                            width: 600,
                            height: 150,
                            visible: false,
                            actions: false
                        });
                        $scope.inventoryPersonnelWindow.center();
                        $scope.inventoryPersonnelWindow.open();
                    })

                    icqaSystemStocktakingService.deleteUsers($scope.stocktakingId, $scope.times,"system", function () {

                    });
                } else {
                    $("#inventoryCountId").parent().addClass("mySelect");
                    $scope.inventoryCountWindow.setOptions({
                        width: 600,
                        height: 150,
                        visible: false,
                        actions: false
                    });

                    $scope.inventoryCountWindow.center();
                    $scope.inventoryCountWindow.open();
                    return;
                }

            }
        };

        // $scope.stocktakingUser = function () {
        //     var count = [];
        //     if ($scope.stocktakingId) {
        //         if ($scope.newPersonnelArr.length > 0) {
        //             for (var i = 0; i < $scope.inventory.length; i++) {
        //                 if ($scope.inventory[i].state == "未盘点") {
        //                     count.push($scope.inventory[i]);
        //                 }
        //             }
        //             $scope.inventory = count;
        //         }
        //         $scope.personnelArr = $scope.personnelArr.concat($scope.newPersonnelArr);
        //         $scope.newPersonnelArr = [];
        //         var count = parseInt($scope.inventory.length) / parseInt($scope.personnelArr.length);
        //         var arr = [];
        //         var temp = [];
        //         var z = 0;
        //         if ($scope.inventory.length >= $scope.personnelArr.length) {
        //             for (var j = 0; j < $scope.personnelArr.length; j++) {
        //                 if (j == 0) {
        //                     arr.push(z);
        //                 } else {
        //                     z = parseInt(count) + z;
        //                     arr.push(z);
        //                 }
        //             }
        //             icqaSystemStocktakingService.deleteUsers($scope.stocktakingId, $scope.times,"system", function () {
        //
        //             });
        //         } else {
        //             $("#inventoryCountId").parent().addClass("mySelect");
        //             $scope.inventoryCountWindow.setOptions({
        //                 width: 600,
        //                 height: 150,
        //                 visible: false,
        //                 actions: false
        //             });
        //
        //             $scope.inventoryCountWindow.center();
        //             $scope.inventoryCountWindow.open();
        //             return;
        //         }
        //         for (var j = 0; j < $scope.inventory.length; j++) {
        //             for (var k = 0; k < arr.length; k++) {
        //                 if (j == arr[k]) {
        //                     $scope.personnelArr[k].storageLocationId = $scope.inventory[j].storageLocationId;
        //                     $scope.personnelArr[k].orderIndex = $scope.inventory[j].bayIndex;
        //                 }
        //             }
        //         }
        //
        //         for (var i = 0; i < $scope.personnelArr.length; i++) {
        //             temp.push({
        //                 "state": "90",
        //                 "systemStocktakingId": $scope.stocktakingId,
        //                 "times": $scope.times,
        //                 "userId": $scope.personnelArr[i].id,
        //                 "storageLocationId": $scope.personnelArr[i].storageLocationId,
        //                 "orderIndex": $scope.personnelArr[i].orderIndex,
        //                 "orderBy": "ASC"
        //             })
        //         }
        //         console.log("sss1",temp);
        //         //保存盘点人员
        //         icqaSystemStocktakingService.saveStocktakingUser(temp, function () {
        //             $scope.inventoryUser = false;
        //             $("#inventoryPersonnelId").parent().addClass("mySelect");
        //             $scope.inventoryPersonnelWindow.setOptions({
        //                 width: 600,
        //                 height: 150,
        //                 visible: false,
        //                 actions: false
        //             });
        //             $scope.inventoryPersonnelWindow.center();
        //             $scope.inventoryPersonnelWindow.open();
        //         })
        //     }
        // };
    })
})();
