/**
 * Created by thoma.bian on 2017/5/10.
 */
(function(){
    "use strict";

    angular.module('myApp').controller("problemOutboundVerifyReadCtl", function ($timeout,$scope,$rootScope,PROBLEM_OUTBOUND,$state, $window, $stateParams, outboundProblemVerifyService, problemOutboundBaseService) {

        $scope.content='cargoRecordNumber';
        $scope.products = false;
        $scope.errorCargoSpace = false;
        $scope.errorCargoSpaceLocation = false;
        $scope.cargoRecordPage = 'recordNumber';
        $scope.goodsCountContent = "";

        $("#allOutboundCargoSpaceId").addClass("buttonColorGray");
        $("#notOutboundSelectCargoSpaceId").removeClass("buttonColorGray");

        setTimeout(function () {$("#cargoSpaceId").focus();}, 0);

        $scope.backProblemOutboundVerify = function () {
            $rootScope.page = true;
            $rootScope.stopPod1 = true;

            if ($rootScope.state){
                $rootScope.edit = true;

            } else {
                $rootScope.edit = false;
            }
            $state.go("main.outbound_problem_verify");

        };

        //Rebin车记录
        var carColumns=[{field: "id", width:"50px", headerTemplate: "<span translate='序号'></span>"},
            {field: "wall",headerTemplate: "<span translate='Rebin车牌'></span>"},
            // {field: "cell",  headerTemplate: "<span translate='Rebin格'></span>"},
            {field: "amount", headerTemplate: "<span translate='问题商品数量'></span>"},
            {field: "rebinAmount", headerTemplate: "<span translate='Rebin格商品总数'></span>"}];


        //显示所有货位
        $scope.allCargoSpaces = function () {
            $scope.argoRecordGridShow = "allCargoSpacePage";
            $("#allOutboundCargoSpaceId").removeClass("buttonColorGray");
            $("#notOutboundSelectCargoSpaceId").addClass("buttonColorGray");
            var cargoRecordGridOptions = $("#cargoOutboundRecordGrid").data("kendoGrid");
            cargoRecordGridOptions.setDataSource(new kendo.data.DataSource({data: $scope.allData}));
        };

        //显示未查货位
        $scope.notSelectCargoSpaces = function () {
            $scope.argoRecordGridShow = "notSelectCargoSpacePage";
            $("#allOutboundCargoSpaceId").addClass("buttonColorGray");
            $("#notOutboundSelectCargoSpaceId").removeClass("buttonColorGray");
            var cargoRecordGridOptions = $("#cargoOutboundRecordGrid").data("kendoGrid");
            cargoRecordGridOptions.setDataSource(new kendo.data.DataSource({data: $scope.selectData}));
        };

        //显示商品信息
        outboundProblemVerifyService.findOutboundProblem($stateParams.id,function(data){
            if (data.client) {
                $scope.LessclientId = data.client.id;
            }
            $scope.problemType = data.problemType;
            $scope.rowData = data;
            if ($scope.problemType == "MORE") {
                $scope.problemTypeValue = "多货";
                $scope.location='more';
            } else {
                $scope.problemTypeValue = "少货";
                $scope.location='less';
            }
            $scope.outboundProblemId = data.id;
            $scope.jobType = data.jobType;
            $scope.solveAmount = data.solveAmount;
            $scope.amount = data.amount - $scope.solveAmount;
            $scope.problemStoragelocation = data.problemStoragelocation;
            $scope.itemNo = data.itemNo;



            //商品信息
            outboundProblemVerifyService.getGoodsInformation(data.id, function (data) {
                console.log(data);
                //是否是有效期商品 true  or   false
                $scope.skuData = data.lotMandatory;
                for (var k in data) $scope[k] = data[k];
            });
            $scope.rebinCarRecordsGridOptions  = problemOutboundBaseService.grid("", carColumns, $(document.body).height() - 210-250);
            //Rebin车记录
            outboundProblemVerifyService.getRebinCarRecords($scope.problemStoragelocation,$scope.itemNo,$scope.jobType, function(data) {
                var allRebin=[],num = 0;
                for (var i = 0; i < data.length; i++){
                    num++;
                    allRebin.push({"id": num, "wall": data[i].wall, "amount": data[i].amount, "rebinAmount": data[i].rebinAmount
                    });
                }
                $scope.allRebin = allRebin;
                var rebinCarRecords = $("#rebinCarRecordsGrid").data("kendoGrid");
                rebinCarRecords.setDataSource(new kendo.data.DataSource({data: allRebin}));
            });
            //拣货货位记录
            var recordColumns = [{field: "id", width: "50px", headerTemplate: "<span translate='序号'></span>"},
                {field: "name", width: 180, headerTemplate: "<span translate='拣货货位记录'></span>"},
                {field: "amount", headerTemplate: "<span translate='问题商品拣货数量'></span>"},
                {field: "actualAmount", headerTemplate: "<span translate='货位问题商品剩余数量'></span>"},
                {field: "totalAmount", headerTemplate: "<span translate='货位商品总数'></span>"},
                {field: "clientName", headerTemplate: "<span translate='CLIENT'></span>"},
                {field: "unexamined", headerTemplate: "<span translate='unexamined'></span>",
                    template: function (item) {
                        var unexamined = item.unexamined;
                        var htmlStr = "<div>" + unexamined + "</div>";
                        setTimeout(function () {
                            var cargoRecordGridOptions = $("#cargoOutboundRecordGrid").data("kendoGrid");
                            cargoRecordGridOptions.tbody.find("tr").each(function () {
                                var td = $(this).find("td:last-child");
                                if (td.text() == "NG") {
                                    $(this).css("background", "#92D050");
                                }
                                cargoRecordGridOptions.hideColumn("unexamined");
                            }, 0)

                        });
                        return htmlStr;
                    }
                }
            ];
            $scope.cargoRecordGridOptions = problemOutboundBaseService.grid([], recordColumns, 240);
            problemOutboundRecords(data.id);
        });

        //record 记录
        function outbountProblemState(data) {
            outboundProblemVerifyService.getOutbountProblemState(data, function () {
            });
        }

        //拣货货位记录数据查询
        function problemOutboundRecords(id) {
            var selectData = [], allData = [], number = 0;
            outboundProblemVerifyService.outboundProblemRecord(id,$scope.jobType , function (data) {
                $scope.storageLocationNumb = 0;
                if (data.length == 0) {
                    var cargoRecordGridOptions = $("#cargoOutboundRecordGrid").data("kendoGrid");
                    cargoRecordGridOptions.hideColumn("unexamined");
                }
                for (var i = 0; i < data.length; i++) {
                    number++;
                    allData.push({"id": number, "name": data[i].name, "amount": data[i].amount, "actualAmount": data[i].actualAmount, "totalAmount": data[i].totalAmount, "storageLocationId": data[i].storageLocationId, "itemDataId": data[i].itemDataId, "clientName": data[i].clientName, "clientId": data[i].clientId, "unexamined": data[i].unexamined
                    });
                    if (data[i].unexamined == "H") {
                        $scope.storageLocationNumb++;
                        selectData.push({"id": number, "name": data[i].name, "amount": data[i].amount, "actualAmount": data[i].actualAmount, "totalAmount": data[i].totalAmount, "storageLocationId": data[i].storageLocationId, "itemDataId": data[i].itemDataId, "clientName": data[i].clientName, "clientId": data[i].clientId, "unexamined": data[i].unexamined
                        });
                    }
                }
                $scope.selectData = selectData;
                $scope.allData = allData;

                var cargoRecordGridOptions = $("#cargoOutboundRecordGrid").data("kendoGrid");
                cargoRecordGridOptions.setDataSource(new kendo.data.DataSource({data: selectData}));

                if ($rootScope.podNo!=""){
                    var grid = $("#cargoOutboundRecordGrid").data("kendoGrid");    // 行样式

                    var rows = grid.tbody.find("tr");
                    rows.each(function (i, row) {
                        var srcData = grid.dataItem(row);
                        if($rootScope.podNo != "" && srcData.name.startsWith($rootScope.podNo)){
                            $(row).css("background-color", "#ffc000");
                        }
                    });

                }else {
                    getPodResult();
                }

            });
        }

        function getRuleState(value) {
            outboundProblemVerifyService.getRule(value, function (data) {
                $scope.dataRuleId = data.id;
            });
        }

        //找到多货少货位置
        $scope.findLocation = function() {
            var rebinColumns=[
                {field: "wall",headerTemplate: "<span translate='Rebin车牌'></span>"},
                // {field: "cell",  headerTemplate: "<span translate='Rebin格'></span>"},
                {field: "amount", headerTemplate: "<span translate='问题商品数量'></span>"},
                {field: "rebinAmount", headerTemplate: "<span translate='Rebin格商品总数'></span>"},
                {field: "actualAmount", headerTemplate: "<span translate='Rebin格实际问题商品数量'></span>"}
            ];

            var grid = $("#rebinCarRecordsGrid").data("kendoGrid");
            var rows = grid.select();
            if(rows.length > 0) {
                var rowData = grid.dataItem(rows[0]);
                var datas = [{
                    "wall": rowData.wall,
                    // "cell": rowData.cell,
                    "amount": rowData.amount,
                    "rebinAmount": rowData.rebinAmount,
                    "itemSku": rowData.itemSku,
                    "itemNo": rowData.itemNo

                }];
                var grid = $('#rebinOutboundCarRecordsGrid').data('kendoGrid');
                grid.setOptions(problemOutboundBaseService.editGrid({
                    columns: rebinColumns,
                    height: 180,
                    dataSource: {
                        data: datas,
                        schema: {
                            model: {
                                id: "id",
                                fields: {
                                    "wall": {editable: false},
                                    // "cell": {editable: false},
                                    "amount": {editable: false},
                                    "rebinAmount": {editable: false}
                                }
                            }
                        }
                    }
                }));
                $("#rebinQuestionGoodsCountId").parent().addClass("windowTitle");
                $scope.rebinQuestionGoodsCountWindow.setOptions({
                    width: 800,
                    height: 300,
                    visible: false,
                    actions: false
                });
                $scope.rebinQuestionGoodsCountWindow.center();
                $scope.rebinQuestionGoodsCountWindow.open();
            }
        };

        //找到多货少货位置确认按钮
        $scope.rebinQuestionGoodsCountSure = function(){
            debugger;
            var grid = $('#rebinOutboundCarRecordsGrid').data('kendoGrid');
            var data = grid.dataSource.data()[0];
            $scope.actualAmountFiled = data.actualAmount;
            $scope.totalAmountFiled = data.rebinAmount;
            // $scope.numberPoor = data.amount-data.actualAmount;
            $scope.wall= data.wall;
            // $scope.cell= data.cell;
            $scope.itemSku = data.itemSku;

            if ($scope.problemType == "MORE") {
                $scope.numberPoor = data.amount - data.actualAmount;
                if ($scope.numberPoor > 0) {
                    getFindOverageGoods();
                }
            }else {
                $scope.numberPoor = data.actualAmount-data.amount;
                if($scope.numberPoor>0) {
                    getFindLossGoods();
                }
            }
        };
        // //多货找到位置
        // function getFindOverageGoods(){
        //     // outboundProblemVerifyService.getDestinationId ($scope.wall+ $scope.cell,function(data) {
        //     outboundProblemVerifyService.getDestinationId ($scope.wall,function(data) {
        //         $scope.storageLocationId = data.id;
        //         outboundProblemVerifyService.findOverageGoods({
        //             "amount": $scope.numberPoor,
        //             "storageLocation": data.id,
        //             "itemSku": $scope.itemNo,
        //             "fromName": $scope.problemStoragelocation,
        //             "jobType":$scope.jobType
        //         }, function () {
        //             outboundProblemVerifyService.problemProductsNumber({
        //                 "amount": $scope.numberPoor,
        //                 "problemId": $scope.outboundProblemId,
        //                 "storageLocationId": $scope.storageLocationId,
        //                 "state": "NG",
        //                 "clientId": $scope.clientFiled,
        //                 "itemDataAmount": $scope.numberPoor,
        //                 "storageLocationAmount": $scope.totalAmountFiled,
        //                 "problemAmount": $scope.actualAmountFiled
        //             }, function (data) {
        //                 outboundProblemVerifyService.getRule("More_FindBin", function (data) {
        //                     outbountProblemState({
        //                         "problemId": $scope.outboundProblemId,
        //                         "amount": $scope.amount,
        //                         "inboundProblemRuleId": data.id,
        //                         "state": 'CLOSE',
        //                         "storageLocationId":$scope.storageLocationId,
        //                         "solveBy":$window.localStorage["username"],
        //                         "clientId": $scope.clientFiled
        //                     });
        //                 });
        //                 $scope.amount = $scope.amount - $scope.numberPoor;
        //                 $scope.solveAmount = parseInt($scope.solveAmount) + parseInt($scope.numberPoor);
        //                 updateOutboundProblemMethod($scope.rowData, $scope.solveAmount, '', '');
        //                 if ($scope.rowData.amount - $scope.solveAmount == 0) {
        //                     // $scope.allPage = $scope.wall + $scope.cell;
        //                     $scope.allPage = $scope.wall ;
        //                     $scope.amount =  $scope.rowData.amount - $scope.solveAmount;
        //                     $scope.cargoRecordPage = 'recordNumberSuccess';
        //                     updateOutboundProblemMethod($scope.rowData, $scope.solveAmount, 'close', '');
        //                     $scope.rebinQuestionGoodsCountWindow.close();
        //                 } else {
        //                     $scope.goodsCountContent = 'rebinCarCell';
        //                     $scope.rebinQuestionGoodsCountWindow.close();
        //                 }
        //             })
        //         })
        //     });
        //
        // }
        // //少货找到位置
        // function getFindLossGoods(){
        //     debugger
        //     outboundProblemVerifyService.getDestinationId ($scope.wall,function(data) {
        //         $scope.storageLocationId = data.id;
        //
        //         outboundProblemVerifyService.findLossGoods({
        //             "amount": $scope.numberPoor,
        //             "fromName": $scope.problemStoragelocation,
        //             // "toName":$scope.wall+ $scope.cell,
        //             "toName":$scope.wall,
        //             "itemSku": $scope.itemNo,
        //             "jobType":$scope.jobType
        //         }, function () {
        //             outboundProblemVerifyService.problemProductsNumber({
        //                 "amount": $scope.numberPoor,
        //                 "problemId": $scope.outboundProblemId,
        //                 "storageLocationId": $scope.storageLocationId,
        //                 "state": "NG",
        //                 "clientId": $scope.clientFiled,
        //                 "itemDataAmount": $scope.numberPoor,
        //                 "storageLocationAmount": $scope.totalAmountFiled,
        //                 "problemAmount": $scope.actualAmountFiled
        //             }, function () {
        //                 outboundProblemVerifyService.getRule("Less_FindBin", function (data) {
        //                     outbountProblemState({
        //                         "problemId": $scope.outboundProblemId,
        //                         "amount": $scope.numberPoor,
        //                         "inboundProblemRuleId": data.id,
        //                         "state": 'CLOSE',
        //                         "storageLocationId":$scope.storageLocationId,
        //                         "solveBy":$window.localStorage["username"],
        //                         "clientId": $scope.clientFiled
        //                     });
        //                     $scope.solveAmount = parseInt($scope.solveAmount) + parseInt($scope.numberPoor);
        //                     $scope.amount =  $scope.rowData.amount - $scope.numberPoor;
        //                     updateOutboundProblemMethod($scope.rowData, $scope.solveAmount, '', '');
        //                     console.log("solveAmount--->"+$scope.solveAmount+"/amount-->"+$scope.amount+"111--->"+$scope.rowData.amount);
        //
        //                 });
        //                 if ($scope.rowData.amount - $scope.solveAmount == 0) {
        //                     // $scope.allPage = $scope.wall + $scope.cell;
        //                     $scope.allPage = $scope.wall;
        //                     updateOutboundProblemMethod($scope.rowData,$scope.solveAmount, 'close', '');
        //                     $scope.cargoRecordPage = 'recordNumberSuccess';
        //                     $scope.rebinQuestionGoodsCountWindow.close();
        //                 } else {
        //                     $scope.goodsCountContent = 'rebinCarCell';
        //                     $scope.rebinQuestionGoodsCountWindow.close();
        //                 }
        //             })
        //         })
        //     })
        // }

        function updateOutboundProblemMethod(rowData, solveAmount, value, arr) {
            var data = {}, state = "unsolved";
            if (value != "") {
                state = value;
            } else {
                state = rowData.state;
            }
            if ($scope.amount == 0 || $scope.amount < 0) {
                state = "CLOSE";
            }
            if (rowData.problemType == "MORE") {
                data = {
                    "id": rowData.id,
                    "problemType": rowData.problemType,
                    "amount": rowData.amount,
                    "solveAmount": solveAmount,
                    "jobType": rowData.jobType,
                    "state": state,
                    "container":rowData.container,
                    "problemStoragelocation": rowData.problemStoragelocation,
                    "reportBy":rowData.reportBy,
                    "itemNo": rowData.itemNo,
                    "modifiedDate":rowData.modifiedDate,
                    "solvedBy": rowData.solvedBy,
                    "client":rowData.client,
                    "clientId":rowData.clientId,
                    "dealState": arr.id,
                    "description": arr.reason,
                    "reportDate":rowData.reportDate,
                    "rule":rowData.inboundProblemRule
                }
            } else {
                data = {
                    "id": rowData.id,
                    "problemType": rowData.problemType,
                    "amount": rowData.amount,
                    "jobType": rowData.jobType,
                    "state": state,
                    "container":rowData.container,
                    "problemStoragelocation": rowData.problemStoragelocation,
                    "modifiedDate":rowData.modifiedDate,
                    "reportBy":rowData.reportBy,
                    "solvedBy": $window.localStorage["username"],
                    "client":rowData.client,
                    "clientId":rowData.clientId,
                    "itemNo": rowData.itemNo,
                    // "itemDataId": rowData.itemData.id,
                    "solveAmount": solveAmount,
                    "reportDate":rowData.reportDate,
                    // "description": rowData.description,
                    "rule":rowData.inboundProblemRule,
                    "description": arr.reason
                }
            }
            outboundProblemVerifyService.updateOutboundProblemVerify(data, function (v) {
                $scope.rrr = true;
                $scope.products = false;
                $scope.errorCargoSpace = false;
                $scope.productsNumber = "";
                $scope.cargoSpace = "";
            });
        }

        $scope.checkCargoSpace = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                for (var i = 0; i < $scope.selectData.length; i++) {
                    var data = $scope.selectData[i];
                    if (data.name == $scope.cargoSpace) {
                        $scope.checkName = data.name;
                        $scope.goodsCountContent = 0;
                        $scope.products = true;
                        $scope.errorCargoSpace = false;
                        $scope.errorCargoSpaceLocation = false;
                        $scope.itemDataId = data.itemDataId;
                        $scope.storageLocationId = data.storageLocationId;
                        $scope.amountFiled = data.amount;
                        $scope.actualAmountFiled = data.actualAmount;
                        $scope.totalAmountFiled = data.totalAmount;
                        $scope.clientFiled = data.clientId;
                        var grid = $("#cargoOutboundRecordGrid").data("kendoGrid");
                        var uid = grid.dataSource.at(i).uid;
                        grid.select("tr[data-uid='" + uid + "']");
                    }
                }
                if ($scope.checkName != this.cargoSpace) {
                        $scope.errorCargoSpaceLocation = true;
                        $scope.products = false;
                        $scope.errorCargoSpace = false;
                }else if($rootScope.podNo != "" && !this.cargoSpace.startsWith($rootScope.podNo)){
                        //   if(data.pod != "" && srcData.name.startsWith(data.pod)){
                        // if ($rootScope.podNo != this.cargoSpace){
                        $scope.errorCargoSpace = true;
                        $scope.products = false;
                        $scope.errorCargoSpaceLocation = false;
                } else {
                    setTimeout(function () {$("#problemProductsNumber").focus();}, 0);
                }
            }
        };

        $scope.problemProductsNumber = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                debugger;
                if ($scope.actualAmountFiled == $scope.productsNumber) {
                    setTimeout(function () {$("#cargoSpaceId").focus();}, 0);
                    outboundProblemVerifyService.problemProductsNumber({
                        "amount": $scope.amountFiled,
                        "problemId": $scope.outboundProblemId,
                        "storageLocationId": $scope.storageLocationId,
                        "state": "OK",
                        "clientId": $scope.clientFiled,
                        "itemDataAmount": $scope.amountFiled,
                        "storageLocationAmount": $scope.totalAmountFiled,
                        "problemAmount": $scope.actualAmountFiled
                    }, function (data) {
                        problemOutboundRecords($scope.outboundProblemId);
                        $scope.products = false;
                        $scope.errorCargoSpace = false;
                        $scope.productsNumber = "";
                        $scope.cargoSpace = "";
                    })
                }
                else if ($scope.actualAmountFiled > $scope.productsNumber && $scope.problemType == "MORE") {
                    $scope.goodsCountContent = 'goodsCountMore';
                    $scope.numberPoor = $scope.actualAmountFiled - this.productsNumber;
                    setTimeout(function () {$("#recargoSpaceNameId").focus();}, 0);
                    $scope.recargoSpace = $scope.cargoSpace;
                    $scope.allPage =  $scope.recargoSpace;
                }
                else if ($scope.actualAmountFiled < $scope.productsNumber && $scope.problemType == "LOSE") {
                    lossMethod();
                }
            }
        };
        // 多货处理
        $scope.reCheckCargoSpace = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                outboundProblemVerifyService.getDestinationId ($scope.cargoSpace,function(data) {
                    $scope.storageLocationId=data.id;
                });
                if ($scope.recargoSpace == $scope.cargoSpace) {
                    var data = {
                        "amount": $scope.numberPoor,
                        "storageLocation":  $scope.storageLocationId,
                        "itemDataId": $scope.itemDataId,
                        "jobType":$scope.jobType
                    };
                    outboundProblemVerifyService.getStowingOverage(data, function () {
                        outboundProblemVerifyService.getRule("More_FindBin", function (data) {
                            // $scope.dataRuleId = data.id;
                            outbountProblemState({
                                "problemId": $scope.outboundProblemId,
                                "amount": $scope.numberPoor,
                                "inboundProblemRuleId": data.id,
                                "state": 'CLOSE',
                                "storageLocationId":$scope.storageLocationId,
                                "solveBy":$window.localStorage["username"],
                                "clientId": $scope.clientFiled
                            });
                            $scope.amount = $scope.amount - $scope.numberPoor;
                        });
                        outboundProblemVerifyService.problemProductsNumber({
                            "amount": $scope.amountFiled,
                            "problemId": $scope.outboundProblemId,
                            "storageLocationId": $scope.storageLocationId,
                            "state": "OK",
                            "clientId": $scope.clientFiled,
                            "itemDataAmount": $scope.amountFiled,
                            "storageLocationAmount": $scope.totalAmountFiled,
                            "problemAmount": $scope.actualAmountFiled
                        }, function (data) {
                            problemOutboundRecords($scope.outboundProblemId);
                            $scope.solveAmount = parseInt($scope.solveAmount) + parseInt($scope.numberPoor);
                            updateOutboundProblemMethod($scope.rowData, $scope.solveAmount, '', '');
                            if ($scope.rowData.amount - $scope.solveAmount == 0) {
                                $scope.cargoRecordPage = 'recordNumberSuccess';
                            } else {
                                $scope.goodsCountContent = 'goodCountSuccess';
                            }

                        });
                        // getRuleState("More_FindBin");
                    })
                }else {
                    outboundProblemVerifyService.problemProductsNumber({
                        "amount": $scope.amountFiled,
                        "problemId": $scope.outboundProblemId,
                        "storageLocationId": $scope.storageLocationId,
                        "state": "NG",
                        "clientId": $scope.clientFiled,
                        "itemDataAmount": $scope.amountFiled,
                        "storageLocationAmount": $scope.totalAmountFiled,
                        "problemAmount": $scope.actualAmountFiled - $scope.numberPoor
                    }, function (data) {
                        $scope.solveAmount = $scope.numberPoor;
                        updateOutboundProblemMethod($scope.rowData, $scope.solveAmount, '','');
                        if ($scope.rowData.amount - $scope.solveAmount == 0) {
                            $scope.cargoRecordPage = 'recordNumberSuccess';
                        } else {
                            $scope.goodsCountContent = 'goodCountSuccess';
                        }
                        // outboundProblemVerifyService.getDestinationId ($scope.recargoSpace,function(data){
                        outboundProblemVerifyService.moveGoods({
                                "sourceId":$scope.outboundProblemId,
                                "destinationId": $scope.storageLocationId,
                                "itemDataId": $scope.itemDataId,
                                "amount": $scope.numberPoor
                            }, function (data) {
                                problemOutboundRecords($scope.inboundProblemId);
                                $scope.amount = $scope.amount - $scope.numberPoor;
                        })

                    })
                }
            }
        };

        //少货处理
        function lossMethod(){
            outboundProblemVerifyService.getDestinationId ($scope.cargoSpace,function(data) {
                $scope.storageLocationId=data.id;
            });
            outboundProblemVerifyService.getStowingLoss({
                "amount": $scope.productsNumber - $scope.actualAmountFiled,
                "fromName": $scope.problemStoragelocation,
                "toName":$scope.cargoSpace,
                "itemDataId": $scope.itemDataId,
                "jobType":$scope.jobType
            }, function () {
                outboundProblemVerifyService.problemProductsNumber({
                    "amount": $scope.amountFiled,
                    "problemId": $scope.outboundProblemId,
                    "storageLocationId": $scope.storageLocationId,
                    "state": "NG",
                    "clientId": $scope.clientFiled,
                    "itemDataAmount": $scope.amountFiled,
                    "storageLocationAmount": $scope.totalAmountFiled,
                    "problemAmount": $scope.productsNumber - $scope.actualAmountFiled
                }, function (data) {
                    problemOutboundRecords($scope.outboundProblemId);
                    $scope.solveAmount = parseInt($scope.solveAmount) + parseInt($scope.productsNumber - $scope.actualAmountFiled);
                    $scope.recargoSpace = $scope.cargoSpace;
                    $scope.allPage =  $scope.recargoSpace;
                    $scope.numberPoor = $scope.productsNumber - $scope.actualAmountFiled;
                    $scope.amount = $scope.amount - $scope.numberPoor;
                    updateOutboundProblemMethod($scope.rowData, $scope.solveAmount, '', '');

                    outboundProblemVerifyService.getRule("Less_FindBin", function (data) {
                        outbountProblemState({
                            "problemId": $scope.outboundProblemId,
                            "amount": $scope.numberPoor,
                            "inboundProblemRuleId": data.id,
                            "state": 'CLOSE',
                            "storageLocationId":$scope.storageLocationId,
                            "solveBy":$window.localStorage["username"],
                            "clientId": $scope.clientFiled
                        });
                    });
                    if ($scope.rowData.amount - $scope.solveAmount == 0) {
                        $scope.cargoRecordPage = 'recordNumberSuccess';
                    } else {
                        $scope.goodsCountContent = 'goodCountSuccess';
                    }
                    $scope.products = false;
                    $scope.productsNumber = "";
                    $scope.cargoSpace = "";
                })
            });
        };
        $scope.maturityYearKeyDown = function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                setTimeout(function () {$("#maturityMonth").focus();
                },0  );
            }
        };

        $scope.maturityMonthKeyDown = function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                setTimeout(function () {$("#maturityDay").focus();
                },0  );
            }
        };

        $scope.maturityDayKeyDown = function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.goodsMaturityDateSure();
                //setTimeout(goodsMaturityDateSure, 0);
                // setTimeout(function () {$("#destinationContainerId").focus();
                // },0  );
            }
        };

        $scope.produceYearKeyDown = function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                setTimeout(function () {$("#produceMonth").focus();
                },0  );
            }
        };
        $scope.produceMonthKeyDown = function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                setTimeout(function () {$("#produceDay").focus();
                },0  );
            }
        };

        $scope.produceDayKeyDown = function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                setTimeout(function () {$("#produceDatas").focus();
                },0  );
            }
        };

        $scope.produceDatasKeyDown = function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.goodsMaturityDateSure();
                // setTimeout(goodsMaturityDateSure, 0);
            }
        };

        // //盘盈//盘亏
        // $scope.produceDate = function () {
        //   $scope.goodsMaturity = "produce";
        //   $("#expirationDate").addClass("buttonColorGray");
        //   $scope.flag = 1;
        // };
        //
        // $scope.maturityDate = function () {
        //   $scope.goodsMaturity = "maturity";
        //   $("#productionDate").addClass("buttonColorGray");
        //   $scope.flag = 2;
        // };
        //点击确认无法找到对应货位时
        $scope.problemGoodsNumber = function () {
            if ($scope.problemType == "MORE") {
                if ($scope.skuData == true) {
                    //到期日
                    if ($scope.lotType == "EXPIRATION") {
                        $scope.goodsMaturity = 'maturity';
                    } else {
                        $scope.goodsMaturity = 'produce';
                        $scope.month = parseInt($scope.months) + parseInt($scope.month || 0);
                        while ($scope.month > 12) {
                            $scope.year++;
                            $scope.month -= 12;
                        }
                    }
                    $("#goodsMaturityDateId").parent().addClass("mySelect");
                    $scope.goodsMaturityDateWindow.setOptions({
                        width: 700,
                        height: 350,
                        visible: false,
                        actions: false,
                        activate:function () {
                            if ($scope.goodsMaturity == 'maturity'){
                                $("#maturityYear").focus();
                            }else {
                                $("#produceYear").focus();
                            }
                        }
                    });
                    $scope.goodsMaturityDateWindow.center();
                    $scope.goodsMaturityDateWindow.open();
                } else {
                    $scope.goodsMaturityDateSure();
                }
            } else {
                $scope.goodsMaturityDateSure();
            }
        };
        //有效期确认
        $scope.goodsMaturityDateSure = function () {
            var window, windowId;
            if ($scope.problemType == "MORE") {
                windowId = $("#diskSurplusGoodsId");
                window = $scope.diskSurplusGoodsWindow;
            } else {
                windowId = $("#diskDeficitGoodsId");
                window = $scope.diskDeficitGoodsWindow;
            }
            $scope.goodsMaturityDateWindow.close();
            windowId.parent().addClass("myWindow");
            window.setOptions({
                width: 800,
                height: 200,
                visible: false,
                actions: false
            });
            window.center();
            window.open();
            // if ($scope.flag == 1) {
            //   $scope.month = parseInt($scope.months) + parseInt($scope.month || 0);
            //   while ($scope.month > 12) {
            //     $scope.year++;
            //     $scope.month -= 12;
            //   }
            // }
            $scope.useNotAfter = $scope.year + "-" + pad($scope.month) + "-" + pad($scope.day) ;
        };
        //是否盘盈确认
        $scope.diskSurplusGoodsSure = function () {
            $scope.diskSurplusGoodsWindow.close();
            $scope.cargoRecordPage = 'recordNumberContent';
            $scope.source = $scope.problemStoragelocation;
        };

        //盘盈确认
        $scope.overageSure = function (client) {
            // outboundProblemVerifyService.getRule("More_Overage", function (data) {
            //     $scope.dataRuleId = data.id;
            // });
            // getRuleState("More_Overage");
            debugger
            outboundProblemVerifyService.getRule("More_Overage", function (data) {
                outboundProblemVerifyService.getDestinationId ($scope.problemStoragelocation,function(sourceData) {
                    outbountProblemState({
                        "problemId": $scope.outboundProblemId,
                        "amount": $scope.amount,
                        "inboundProblemRuleId": data.id,
                        "state": 'CLOSE',
                        "storageLocationId": sourceData.id,
                        "solveBy": $window.localStorage["username"],
                        "clientId": client.id
                    });
                });
            });
            if ($scope.useNotAfter.indexOf("undefined") >= 0) {
                $scope.useNotAfter = "";
            }
            outboundProblemVerifyService.getDestinationId($scope.destination, function (data) {
                var arr = {
                    "destinationId": data.id,
                    // "clientId": client,
                    "clientId": client ? client.id : null,
                    "itemNo": $scope.itemNo,
                    "amount": $scope.amount,
                    "inventoryState": "",
                    "useNotAfter": $scope.useNotAfter || "",
                    "adjustReason": 'Ibp_Overage',
                    "thoseResponsible": $window.localStorage["username"],
                    "problemDestination": ""
                };
                outboundProblemVerifyService.overageGoods(arr, function (data) {
                    updateOutboundProblemMethod($scope.rowData, '', 'close', '');
                    $scope.cargoRecordPage = 'recordNumberOverage';
                });
            })

        };

        //盘亏确认
        $scope.diskDeficitGoodsSure = function () {
            getRuleState("Less_Loss");
            outboundProblemVerifyService.getDestinationId ($scope.problemStoragelocation,function(data){
                outbountProblemState({
                    "problemId": $scope.outboundProblemId,
                    "amount": $scope.amount,
                    "inboundProblemRuleId": $scope.dataRuleId,
                    "state": 'CLOSE',
                    "storageLocationId":data.id,
                    "solveBy":$window.localStorage["username"],
                    "clientId": $scope.LessclientId
                });
                outboundProblemVerifyService.lossGoods({
                    "sourceId": data.id,
                    "itemDataId": $scope.rowData.itemNo,
                    "amount": $scope.amount,
                    "adjustReason": 'Ibp_Loss',
                    "thoseResponsible": $window.localStorage["username"],
                    "problemDestination": ""
                }, function () {
                    $scope.diskDeficitGoodsWindow.close();
                    updateOutboundProblemMethod($scope.rowData, '', 'close', '');
                    $scope.cargoRecordPage = 'recordNumberLoss';
                });
            });
        };

        $scope.nextGoods = function () {
            $rootScope.page = true;
            $state.go("main.outbound_problem_verify");
        };

        function pad(str) {
            str = str + "";
            var pad = "00";
            return (pad.length > str.length ? pad.substring(0, pad.length - str.length) + str : str);
        }

        $scope.overageCancel = function () {
            $scope.products = false;
            $scope.errorCargoSpace = false;
            $scope.cargoRecordPage = 'recordNumber';
            $scope.argoRecordGrid = "notSelectCargoSpace";
        };

        //websocket 推送pod的结果
        // $scope.podSocket=null;//webSocket
        function getPodResult() {
            var url = PROBLEM_OUTBOUND.podWebSocket+$rootScope.workStationIds;
            console.log("url:",url);
            $scope.podSocket = new WebSocket(url);
            //打开事件
            $scope.podSocket.onopen = function () {
                console.log("podSocket 已打开");
            };
            //获得消息事件
            $scope.podSocket.onmessage = function (msg) {
                console.log("podSocket 正在推送消息。。。");
                var data = JSON.parse(msg.data);
                console.log("data:",data)
                if(data.pod != "success"){
                    if (data.workstation == $rootScope.workStationIds) {
                        console.log("推送pod的信息：",data);
                        $rootScope.podNo = data.pod;
                        var grid = $("#cargoRecordGRID").data("kendoGrid");    // 行样式

                        var rows = grid.tbody.find("tr");
                        rows.each(function (i, row) {
                            var srcData = grid.dataItem(row);
                            if(data.pod != "" && srcData.name.startsWith(data.pod)){
                                $(row).css("background-color", "#ffc000");
                            }
                        });

                    }
                }
            };

            //关闭事件
            $scope.podSocket.onclose = function () {
                console.log("podSocket 关闭");
                if($scope.podSocket.readyState != 1){
                    $scope.podSocket = new WebSocket(url);
                    if($scope.podSocket.readyState != 1){
                        $scope.errorWindow("hardwareId1",$scope.hardwareWindows1);
                    }
                }
            };
            //发生了错误事件
            $scope.podSocket.onerror = function () {
                console.log("podSocket 发生了错误");
                $scope.podSocket = new WebSocket(url);
            }
        }
        // 释放Pod
        $scope.releasePodGrid = function () {
            outboundProblemVerifyService.reservePod($rootScope.podNo,$rootScope.sectionId,"false",$rootScope.workStationIds,$rootScope.workStationId,function (data) {
             //   $scope.select_all = false;
                console.log("释放pod",data);
                // $rootScope.select_one = false;
                // $rootScope.select_all = false;
                $rootScope.podNo = data.pod;
                if ($rootScope.podNo!=""){
                    var grid = $("#cargoOutboundRecordGrid").data("kendoGrid");    // 行样式
                    var rows = grid.tbody.find("tr");
                    rows.each(function (i, row) {
                        var srcData = grid.dataItem(row);
                        if(srcData.name.startsWith($rootScope.podNo)){
                            $(row).css("background-color", "#ffc000");
                        }
                    });

                }else {
                    setTimeout($scope.refreshPod(),5000);
                    // $timeout($scope.refreshPod(),5000);
                    if ($rootScope.podNo == ""){
                        getPodResult();
                    }
                }
            })

        };


        //刷新pod信息
        $scope.refreshPod = function () {
            if($rootScope.podNo == null || $rootScope.podNo == "" || $rootScope.podNo == "undefined"){
                outboundProblemVerifyService.refreshPod($rootScope.sectionId,$rootScope.workStationIds,function (poddata) {
                    console.log("刷新的pod信息：",poddata);
                    if ($rootScope.podNo!= ""){
                        $rootScope.podNo = poddata.pod;
                        var grid = $("#cargoOutboundRecordGrid").data("kendoGrid");    // 行样式

                        var rows = grid.tbody.find("tr");
                        rows.each(function (i, row) {
                            var srcData = grid.dataItem(row);
                            if(srcData.name.startsWith(poddata.pod)){
                                $(row).css("background-color", "#ffc000");
                            }
                        });
                    }
                });
            }
        };
        //备注
        $scope.remarks = function () {

            if ($scope.problemTypeValue == "多货"){
                $("#remarksId").parent().addClass("mySelect");
                $scope.remarksWindow.setOptions({
                    width: 800,
                    height: 280,
                    visible: false,
                    actions: false
                });
                $scope.remarksWindow.center();
                $scope.remarksWindow.open();
            }else if($scope.problemTypeValue == "少货"){
                $("#remarksLessId").parent().addClass("mySelect");
                $scope.remarksWindowLess.setOptions({
                    width: 800,
                    height: 280,
                    visible: false,
                    actions: false
                });
                $scope.remarksWindowLess.center();
                $scope.remarksWindowLess.open();
            }
        };

        //备注确认
        $scope.remarksWindowSure = function () {
            if ($scope.problemType == "MORE") {
                if ($scope.reasonId == 1) {
                    $scope.reason = $("#reasonOneId").html().substring(2);
                }
                if ($scope.reasonId == 2) {
                    $scope.reason = $("#reasonTwoId").html().substring(2);
                }
                var arr = {
                    id: $scope.reasonId,
                    reason: $scope.reason
                };
                if ($scope.reasonId) {
                    updateOutboundProblemMethod($scope.rowData, '', 'unsolved', arr);
                }
                $scope.remarksWindow.close();
            }else{
                if ($scope.reasonId == 1) {
                    $scope.reason = $("#reasonOneLessId").html().substring(2);
                }
                var arr = {
                    id: $scope.reasonId,
                    reason: $scope.reason
                };
                if ($scope.reasonId) {
                    updateOutboundProblemMethod($scope.rowData, '', 'unsolved', arr);
                }
                $scope.remarksWindowLess.close();
            }



        }

    })
})();