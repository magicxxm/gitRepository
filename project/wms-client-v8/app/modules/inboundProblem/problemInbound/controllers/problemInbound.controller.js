/**
 * Created by thoma.bian on 2017/5/10.
 */
(function(){
    "use strict";

    angular.module('myApp').controller("problemInboundCtl", function ($timeout,$scope,$rootScope,PROBLEM_INBOUND, $window,webSocketService, inboundProblemService, problemInboundBaseService) {
        // 初始化
        setTimeout(function(){ $("#obp_station").focus();}, 0);
        $scope.podSocket;
        if($rootScope.page){
            $scope.page = "main";
        }else{
            $scope.page = "workStationPage";
        }

        $scope.selectedSign = "";
        if ($rootScope.podNo != ""){
            if ($scope.selectedSign == "all"){
                $scope.selectAll();
            }else if ($scope.selectedSign == "one"){
                if ($rootScope.id != ""){
                    $scope.selectInboundProblemRecord($rootScope.id);
                }
            }
        }



        // 扫描工作站
        $scope.workStation = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode != 13) return;
            inboundProblemService.getInboundProblemStation($scope.workstation, function(data){
                $rootScope.workstationValue = data.name;
                $rootScope.workStationIds = data.workStation.id;
                $rootScope.sectionId = data.workStation.sectionId;
                $rootScope.workStationId = data.id;

                $scope.workingStation = false;
                $scope.page = 'main';
                inboundProblemService.yesOrNoFinsh($rootScope.workstationValue, function(data){
                    if(data){   // 有pod
                        $rootScope.state = true;
                        $rootScope.stopcallPOD = "停止分配POD";
                        $("#releasePodId").removeAttr("disabled");
                        $("#receiving-releasepod").removeAttr("disabled");
                        $("#stopAllocationPod").removeAttr("disabled");

                        $("#releasePodId").removeClass("buttonColorGray");
                        $("#receiving-releasepod").removeClass("buttonColorGray");
                        $("#releasePodId").css({"backgroundColor": "#5c6bc0"});
                        $("#receiving-releasepod").css({"backgroundColor": "#5c6bc0"});
                        $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});

                    }else{    // 无pod
                        $rootScope.stopcallPOD = "停止分配POD";
                        $("#releasePodId").attr("disabled",true);
                        $("#receiving-releasepod").attr("disabled",true);
                        $("#stopAllocationPod").attr("disabled",true);
                    }
                })
            }, function(data){
                if(data.key == "IBP_WORKSTATION_SOMEONE"){
                    $scope.errorMessage = "已被占用";
                    // $scope.errorMessage = message.values[0];
                }else if(data.key == "EX_SERVER_ERROR"){
                    $scope.errorMessage = "不是一个有效工作站";
                }
                $scope.workingStation = true;
            });
        };

        // function stopPod() {
        //     if ($rootScope.stopcallPOD = "停止分配POD"){
        //         $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
        //     }else if ($rootScope.stopcallPOD = "恢复分配POD"){
        //         $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
        //     }
        // }
        //  setTimeout(getBackState, 0);
        // getBackState();
        $timeout(getBackState,0);

        // $scope.$on('destroy',function(){
        //     $timeout.cancel($scope.timer);
        // })




        //解绑工作站
        $scope.exitInboundProblem = function(){
            inboundProblemService.yesOrNoFinsh($rootScope.workstationValue, function(data){
                console.log("状态:",data);
                if (data){
                    $("#window_general_ok_cancel").parent().addClass("mySelect");
                    $scope.scanSerialNoWindow.setOptions({
                        width: 600,
                        height: 150,
                        visible: false,
                        actions: false
                    });
                    $scope.scanSerialNoWindow.center();
                    $scope.scanSerialNoWindow.open();
                    return;
                }else
                {
                    $rootScope.page=false;
                    $scope.page = "";
                    inboundProblemService.exitInboundProblemStation($rootScope.workstationValue, function(data){
                        // $state.go("main.inbound_problem_disposal");
                        $scope.page = "workStationPage";
                        $("#releasePodId").attr("disabled",true);
                        $("#receiving-releasepod").attr("disabled",true);
                        $("#stopAllocationPod").attr("disabled",true);
                        $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
                        $("#releasePodId").css({"backgroundColor": "#6E6E6E"});
                        $("#receiving-releasepod").css({"backgroundColor": "#6E6E6E"});
                        $rootScope.select_one = false;
                        $rootScope.select_all = false;
                        // $rootScope.chk = false;
                        $rootScope.workstationValue = "";
                        $rootScope.workStationIds = "";
                        $rootScope.sectionId = "";
                        $rootScope.workStationId = "";
                        $rootScope.state = false;
                        $rootScope.edit = false;
                        $rootScope.id = "";
                        closeWebsocket($scope.podSocket,"pod");
                        // $scope.podSocket.close();

                    })
                }
            })
        };

        //左边Grid
        var columnsLeft = [{field: "problemType", width: "40px", headerTemplate: "<span translate='问题类型'></span>",
            template: function (item) {
                var value = "";
                if (item.problemType == "MORE") {
                    value = "<span>多货</span>";
                } else {
                    value = "<span>少货</span>";
                }
                return "<a ui-sref='main.problemInboundRead({id:dataItem.id})'>" + value + "</a>";
            }
        },
            {field: "itemData.itemNo", width: "120px", headerTemplate: "<span translate='SKU'></span>",
                template: function (item) {
                    return item.itemData ? item.itemData.itemNo : item.itemNo;
                }},
            {field: "problemStorageLocation", width: "120px", headerTemplate: "<span translate='容器'></span>"},
            {field: "amount", width: "50px", headerTemplate: "<span translate='数量'></span>",
                template: function (item) {
                    return item.amount - item.solveAmount;
                }
            },
            {field: "reportBy", width: "110px", headerTemplate: "<span translate='操作人'></span>"},
            {field: "createdDate", width: 105, headerTemplate: "<span translate='员工操作时间'></span>",
                template: function (item) {
                    return item.createdDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.createdDate)) : "";
                }
            },
            {field: "solveBy", width: "110px", headerTemplate: "<span translate='问题人员'></span>"},
            {field: "description", headerTemplate: "<span translate='备注信息'></span>"}];

        inboundProblemService.getInboundProblem("OPEN","",$scope.itemNoLeft, function (data) {
            $scope.problemInboundLeftGridOptions = problemInboundBaseService.grid(data,columnsLeft, $(document.body).height() - 210);

        });

        $scope.searchInputLeft = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.searchGridLeft();
            }
        };

        $scope.searchGridLeft = function () {
            inboundProblemService.getInboundProblem("OPEN","", $scope.itemNoLeft, function (data) {
                var problemInboundLeftGrid = $("#problemInboundLeftGrid").data("kendoGrid");
                problemInboundLeftGrid.setOptions({"editable": false});
                problemInboundLeftGrid.setDataSource(new kendo.data.DataSource({data: data}));
            });
        };
        $scope.chk = false;
        $scope.selectOne = function (val, uid) {
            $scope.selectedSign = "one";
            var grid = $('#problemInboundRightGrid').data('kendoGrid');
            if (val) {
                // grid.select("tr[data-uid='" + uid + "']");
                grid.tbody.children('tr[data-uid="' + uid + '"]').addClass('k-state-selected');
                // $rootScope.chk = true;
                var rowData = grid.dataItem(grid.select());

                console.log("选中行的ID:"+rowData.id);
                if (grid.select().length == 1){
                    $scope.selectInboundProblemRecord(rowData.id);
                    $rootScope.id = rowData.id;
                    console.log("选中行的ID:"+rowData.id);
                }else if (grid.select().length > 1){

                    var rows = grid.select();
                    // var dataFiled = "";
                    for (var i = 0; i < rows.length; i++) {
                        var rowData = grid.dataItem(rows[i]);
                        console.log("选中行的id--->"+rowData.id);
                        $scope.selectInboundProblemRecord(rowData.id);
                    }

                    // $scope.selectAll();
                }

            } else {
                grid.tbody.children('tr[data-uid="' + uid + '"]').removeClass('k-state-selected');
                $rootScope.id = "";
                // $rootScope.chk = false;
            }
        };
        //右边Grid
        var columnsRight = [{width: 35, template: "<input type=\"checkbox\"  ng-model='chk' id='dataItem.id' class='check-box' ng-checked = 'select_one' ng-click='selectOne(chk,dataItem.uid)'/>"},
            {field: "problemType", width: "40px", headerTemplate: "<span translate='问题类型'></span>",
                template: function (item) {
                    var value = "";
                    if (item.problemType == "MORE") {
                        value = "<span>多货</span>"
                    } else {
                        value = "<span>少货</span>"
                    }
                    return "<a ui-sref='main.problemInboundRead({id:dataItem.id})'>" + value + "</a>";


                }
            },
            {field: "itemData", width: "120px", headerTemplate: "<span translate='SKU'></span>",
                template: function (item) {
                    return item.itemData ? item.itemData.itemNo : item.itemNo;
                }},
            {field: "problemStorageLocation", width: "120px", headerTemplate: "<span translate='容器'></span>"},
            {field: "amount", width: "50px", headerTemplate: "<span translate='数量'></span>",
                template: function (item) {
                    return item.amount - item.solveAmount;
                }
            },
            {field: "reportBy", width: "110px", headerTemplate: "<span translate='操作人'></span>"},
            {field: "createdDate", width: 105, headerTemplate: "<span translate='员工操作时间'></span>",
                template: function (item) {
                    return item.createdDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.createdDate)) : "";
                }
            },
            {field: "solveBy", width: "110px", headerTemplate: "<span translate='问题人员'></span>"}];

        inboundProblemService.getInboundProblem("PROCESS",$window.localStorage["username"],$scope.itemNoRight, function (data) {
            $scope.problemInboundRightGridOptions = problemInboundBaseService.grid(data,columnsRight, $(document.body).height() - 210);

        });

        $scope.searchInputRight = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.searchGridRight();
            }
        };
        $scope.sourceContainerKeyDown = function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                setTimeout(function () {$("#destinationContainerId").focus();
                },0  );
            }
        };
        $scope.destinationContainerKeyDown = function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                setTimeout(function () {$("#numId").focus();
                },0  );
            }
        };


        $scope.arr = "";
        $scope.searchGridRight = function () {
            inboundProblemService.getInboundProblem("PROCESS",$window.localStorage["username"],$scope.itemNoRight, function (data) {
                $scope.adjustment = true;
                if(data.length<=0){  $scope.adjustment = false;};
                if (data.length > 1 && data[0].itemNo == data[1].itemNo && (data[0].amount - data[0].solveAmount) == (data[1].amount - data[1].solveAmount) && data[0].itemData.clientId == data[1].itemData.clientId && data[0].problemType != data[1].problemType) {
                    if (data[0].problemType == "MORE"){
                        $scope.caseSourceSave = data[0].problemStorageLocation;
                        $scope.caseDestinationSave = data[1].problemStorageLocation;
                    }else {
                        $scope.caseSourceSave = data[1].problemStorageLocation;
                        $scope.caseDestinationSave = data[0].problemStorageLocation;
                    }
                    $scope.caseAmountSave = data[0].amount - data[0].solveAmount;
                    $scope.clientSave = data[0].itemData.clientId;
                    $scope.andCase1 = 1;
                    $("#problemInboundRightGrid").data("kendoGrid").setOptions({height: ($(document.body).height() - 280) / 2});
                    for (var i = 0; i < data.length; i++) {
                        if(data[i].itemData){
                            $scope.rowData = data[i];
                        }
                        if (i == 0) {
                            $scope.arr = data[i].id;
                        } else {
                            $scope.arr += "," + data[i].id;
                        }
                    }
                };
                if ($scope.itemNoRight == undefined || $scope.itemNoRight == ""){ $scope.adjustment = false; }
                var problemInboundRightGrid = $("#problemInboundRightGrid").data("kendoGrid");
                problemInboundRightGrid.setOptions({"editable": false});
                problemInboundRightGrid.setDataSource(new kendo.data.DataSource({data: data}));
            });
        };

        //左右移动
        $scope.moveProblemInbound = function (value) {
            var state = "", grid = "", username = "";
            if (value == "left") {
                $scope.adjustment = false;
                $scope.andCaseChildPage = "";
                state = "PROCESS";
                grid = $("#problemInboundLeftGrid").data("kendoGrid");
                username = $window.localStorage["username"]

            } else {
                $scope.adjustment = false;
                $scope.andCaseChildPage = "";
                state = "OPEN";
                grid = $("#problemInboundRightGrid").data("kendoGrid");
                username = $window.localStorage["username"];

            }
            var rows = grid.select();
            console.log("左右移动:"+rows);
            if (rows.length) {
                var rowData = grid.dataItem(rows[0]);
                var dataFiled = {};
                if (rowData.problemType == "MORE") {
                    dataFiled = {
                        "id": rowData.id,
                        "problemType": rowData.problemType,
                        "amount": rowData.amount,
                        "description":rowData.description,
                        "jobType": rowData.jobType,
                        "reportBy":rowData.reportBy,
                        "solveBy": username,
                        "createdDate ": rowData.createdDate,
                        "state": state,
                        "problemStorageLocation":rowData.problemStorageLocation,
                        "lotNo":rowData.lotNo,
                        "serialNo":rowData.serialNo,
                        "serialVersionUID":rowData.serialVersionUID,
                        "solveAmount":rowData.solveAmount,
                        "itemNo": rowData.itemNo,
                        "skuNo":rowData.skuNo,
                        "itemDataId": rowData.itemData.id

                    }
                } else {
                    dataFiled = {
                        "id": rowData.id,
                        "problemType": rowData.problemType,
                        "amount": rowData.amount,
                        "jobType": rowData.jobType,
                        "reportBy":rowData.reportBy,
                        "solveBy": username,
                        "createdDate": rowData.createdDate,
                        "state": state,
                        "problemStorageLocation":rowData.problemStorageLocation,
                        "lotNo":rowData.lotNo,
                        "serialNo":rowData.serialNo,
                        "serialVersionUID":rowData.serialVersionUID,
                        "solveAmount":rowData.solveAmount,
                        "itemDataId": rowData.itemData.id,
                        "itemNo": rowData.itemNo,
                        "skuNo":rowData.skuNo
                    }
                }
                inboundProblemService.updateInboundProblem(dataFiled, function () {
                    $scope.searchGridLeft();
                    $scope.searchGridRight();
                });
            }
        };
        var andCaseColumns = [{field: "containerName",width: "95px",  headerTemplate: "<span translate='容器'></span>"},
            {field: "amount", width: "50px", headerTemplate: "<span translate='数量'></span>",
                template: function (item) {
                    return item.amount;
                }
            },
            {field: "deliveryNote",headerTemplate: "<span translate='收货DN'></span>",
                template: function (item) {
                    return "<div style='word-wrap:break-word'>"+item.deliveryNote+"</div>"
                }},
            {field: "stationName",width: "90px", headerTemplate: "<span translate='收货站台'></span>"},
            {field: "createdBy",width: "70px", headerTemplate: "<span translate='收货人'></span>"},
            {field: "clientName",width: "70px", headerTemplate: "<span translate='客户'></span>"},
            {field: "createdDate", headerTemplate: "<span translate='时间'></span>",
                template: function (item) {
                    return item.createdDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.createdDate)) : "";
                }
            }];
        // 并案分析
        $scope.andCaseChild1 = function () {
            $scope.andCaseChildPage = 'CaseGrid';
            $scope.caseSource = "";
            $scope.caseDestination = "";
            $scope.caseAmount = "";
            $scope.client = "";
            inboundProblemService.getAnalysis($scope.arr, function (data) {
                $scope.andCaseGridOptions = problemInboundBaseService.grid(data, andCaseColumns, 180);
                console.log(data);
            })
        };
        // 并案调整
        $scope.andCaseChild2 = function () {
            $scope.andCaseChildPage = 'CaseContent';
            $scope.caseSource = $scope.caseSourceSave;
            $scope.caseDestination = $scope.caseDestinationSave;
            $scope.caseAmount = $scope.caseAmountSave;
            $scope.client = $scope.clientSave;
            console.log($scope.client);
            var combobox = $("#comboClientId").data("kendoComboBox");
            combobox.text($scope.client);
            setTimeout(function () {$("#sourceContainerId").focus();
            },0  );
        };

        // 对应调整点确定
        $scope.addCaseButtonSure = function(){
            inboundProblemService.getDestinationId($scope.caseSource,function(sourceData) {
                inboundProblemService.getDestinationId($scope.caseDestination, function (destinationData) {
                    inboundProblemService.moveGoods({
                        "sourceId":sourceData.id,
                        "destinationId": destinationData.id,
                        "itemDataId": $scope.rowData.itemData.id,
                        "amount": $scope.caseAmount
                    }, function (data) {
                        inboundProblemService.updateInboundProblemClose($scope.arr,"Adjustment",function(){
                            $("#problemInboundRightGrid").data("kendoGrid").setOptions({height: $(document.body).height() - 210});
                            $scope.searchGridRight();
                            $scope.andCaseInformation = true;
                            $scope.adjustment = false;
                            $scope.caseSource = "";
                            $scope.caseDestination = "";
                            $scope.caseAmount = "";
                            $scope.client = "";
                        });
                    });
                });
            });
            // inboundProblemService.getInboundDeal("Adjustment", function (data) {
            //     console.log(data);
            //     inboundProblemState({
            //         "inboundProblemId": $scope.inboundProblemId,
            //         "amount": $scope.amount,
            //         "inboundProblemRuleId": data.id,
            //         "state": 'CLOSE',
            //         "storageLocationId":$scope.storageLocationId,
            //         "solveBy":$window.localStorage["username"],
            //         "clientId": $scope.client.id
            //     });
            // });
        };

        $scope.emptyCase = function(){
            $scope.caseSource = "";
            $scope.caseDestination = "";
            $scope.caseAmount = "";
            $scope.client = "";
            $scope.source= "";
        };
        //点继续时
        $scope.continues = function(){
            $scope.searchGridRight();
            $scope.adjustment = false;
            $scope.andCase1 = "";
            $scope.andCaseChildPage = "";
            $scope.andCaseInformation = false;
            $("#problemInboundRightGrid").height($(document.body).height()-210);
        };

        // 查询全部
        $scope.selectAll = function () {
            $scope.selectedSign = "all";
            var grid = $('#problemInboundRightGrid').data('kendoGrid');
            if ($scope.select_all) {
                $rootScope.select_one = true;
                $rootScope.select_all = true;
                grid.tbody.children('tr').addClass('k-state-selected');

                var rows = grid.select();
                // var dataFiled = "";
                for (var i = 0; i < rows.length; i++) {
                    var rowData = grid.dataItem(rows[i]);
                    console.log("选中行的id--->"+rowData.id);
                    $scope.selectInboundProblemRecord(rowData.id);

                }
            } else {
                $rootScope.select_one = false;
                $rootScope.select_all = false;
                grid.tbody.children('tr').removeClass('k-state-selected');
            }
        };
        // 判断里面变色
        /*function selectInboundProblemRecord(id) {
            inboundProblemService.inboundProblemRecord({"inboundProblemId":id}, function (data) {
                console.log("货位",data);
                // var data = grid.getRowData();
                var grid = $('#problemInboundRightGrid').data('kendoGrid');
                for (var i = 0; i < data.length; i++) {
                    if (data[i].unexamined == "H") {
                        console.log("$rootScope.podNo:"+$rootScope.podNo);
                        if ($rootScope.podNo != "" && data[i].name.startsWith($rootScope.podNo)){
                            // grid.setRowData(id,{backgroundColor:"#ffc000"});
                            // document.getElementById("#id").style.backgroundColor == "#ffc000";
                            console.log("开始转换背景颜色。。。。");
                            var rows = grid.tbody.find("tr");
                            rows.each(function (i, row) {
                                console.log("row");
                                var srcData = grid.dataItem(row);
                                if (srcData.id == id){
                                    $(row).css("background-color", "#ffc000");
                                    return;
                                }
                            });

                        }
                    }
                }

            });
        }*/



        $scope.selectInboundProblemRecord = function(id) {
            inboundProblemService.inboundProblemRecord({"inboundProblemId":id}, function (data) {
                console.log("货位",data);
                // var data = grid.getRowData();
                var grid = $('#problemInboundRightGrid').data('kendoGrid');
                for (var i = 0; i < data.length; i++) {
                    if (data[i].unexamined == "H") {
                        console.log("$rootScope.podNo:"+$rootScope.podNo);
                        if ($rootScope.podNo != "" && data[i].name.startsWith($rootScope.podNo)){
                            // grid.setRowData(id,{backgroundColor:"#ffc000"});
                            // document.getElementById("#id").style.backgroundColor == "#ffc000";
                            console.log("开始转换背景颜色。。。。");
                            var rows = grid.tbody.find("tr");
                            rows.each(function (i, row) {
                                console.log("row");
                                var srcData = grid.dataItem(row);
                                if (srcData.id == id){
                                    $(row).css("background-color", "#ffc000");
                                    return;
                                }
                            });

                        }
                    }
                }

            });
        }
        // // 让外面变色
        //   function outSideChange(){
        //       var grid = $('#problemInboundRightGrid').data('kendoGrid');
        //       var uid = grid.dataSource.at(i).uid;
        //       grid.tbody.children("tr[data-uid='" + uid + "']").addClass('k-state-selected')
        //
        //
        //   }

        // 呼叫pod
        $scope.callPodGrid = function () {
            $rootScope.state = true;

            var grid = $('#problemInboundRightGrid').data('kendoGrid');
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
            if (dataFiled.length > 0) {
                getReleaseStart();
                inboundProblemService.callPodInboundProblem(dataFiled,$rootScope.sectionId, function (data) {
                    console.log("呼叫POD------>", data, $rootScope.workStationId);
                    var callData = {
                        "pods": data,
                        "workStationId": $rootScope.workStationId
                    };
                    // inboundProblemService.callPodInterface({callData: JSON.stringify(callData)}, function () {
                    inboundProblemService.callPodInterface(JSON.stringify(callData), function () {

                        var pod = getPodResult();//获取websocket推送过来的信息

                       // $scope.searchGridRight();
                        // $scope.select_all = false;
                    });
                });
            }else {
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
        };
        // 呼叫pod后初始化相关按钮
        function getReleaseStart() {

            $("#releasePodId").removeAttr("disabled");
            $("#receiving-releasepod").removeAttr("disabled");
            $("#stopAllocationPod").removeAttr("disabled");

            $("#releasePodId").removeClass("buttonColorGray");
            $("#receiving-releasepod").removeClass("buttonColorGray");
            $("#releasePodId").css({"backgroundColor": "#5c6bc0"});
            $("#receiving-releasepod").css({"backgroundColor": "#5c6bc0"});
            $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});

            inboundProblemService.stopCallPod("start",$rootScope.workStationIds,function (data) {
            });


        }

        // 定时器 返回时调用
        function getBackState() {
            // 只有返回才进
            if($rootScope.stopPod1){
                //返回后看是否可编辑
                if ($rootScope.edit){

                    $("#releasePodId").removeAttr("disabled");
                    $("#receiving-releasepod").removeAttr("disabled");
                    $("#stopAllocationPod").removeAttr("disabled");

                    $("#releasePodId").removeClass("buttonColorGray");
                    $("#receiving-releasepod").removeClass("buttonColorGray");
                    $("#releasePodId").css({"backgroundColor": "#5c6bc0"});
                    $("#receiving-releasepod").css({"backgroundColor": "#5c6bc0"});
                    $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                    inboundProblemService.workStationPodState($rootScope.workStationIds,function (data) {
                        console.log(data);
                        debugger;
                        if (data){
                            $rootScope.stopcallPOD = "停止分配POD";
                            $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                        }else {
                            $rootScope.stopcallPOD = "恢复分配POD";
                            $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
                        }
                    });
                }else {
                    $("#releasePodId").attr("disabled",true);
                    $("#receiving-releasepod").attr("disabled",true);
                    $("#stopAllocationPod").attr("disabled",true);
                }
            }
        }

        // 停止分配pod
        $scope.stopAllocationPodGrid = function () {
            debugger
            if ($rootScope.stopcallPOD == "停止分配POD"){
                inboundProblemService.stopCallPod("stop",$rootScope.workStationIds,function (data) {
                    $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
                    $rootScope.stopcallPOD = "恢复分配POD";
                })
            }else {
                inboundProblemService.stopCallPod("start",$rootScope.workStationIds,function (data) {
                    $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                    $rootScope.stopcallPOD = "停止分配POD";
                })
            }
        };

        // 释放pod
        $scope.releasePodGrid = function () {
            inboundProblemService.reservePod($rootScope.podNo,$rootScope.sectionId,"false",$rootScope.workStationIds,$rootScope.workStationId,function (data) {
                console.log("释放pod"+ $rootScope.podNo + "获取下一个pod:"+data.pod);
                $rootScope.podNo = data.pod;
                if ($rootScope.podNo!=""){
                    if ($scope.selectedSign == "all"){
                        $scope.selectAll();
                    }else if ($scope.selectedSign == "one"){
                        if ($rootScope.id != ""){
                            $scope.selectInboundProblemRecord($rootScope.id);
                        }

                    }


                }else {
                    $timeout($scope.refreshPod(),5000);
                    if ($rootScope.podNo == ""){
                        $scope.refreshPod();
                        if ($scope.selectedSign == "all"){
                            $scope.selectAll();
                        }else if ($scope.selectedSign == "one"){
                            if ($rootScope.id != ""){
                                $scope.selectInboundProblemRecord($rootScope.id);
                            }
                        }
                    }

                }
                $scope.searchGridRight();
                // $rootScope.select_all = false;
                // $rootScope.select_one = false;
            })
        };

        //刷新pod信息
        $scope.refreshPod = function () {
            $scope.selectedSign == "all"
            // if($rootScope.podNo == null || $rootScope.podNo == "" || $rootScope.podNo == "undefined"){
            inboundProblemService.refreshPod($rootScope.sectionId,$rootScope.workStationIds,function (poddata) {
                console.log("刷新的pod信息：",poddata);
                if ($rootScope.podNo!= ""){
                    $rootScope.podNo = poddata.pod;
                    if ($scope.selectedSign == "all"){
                        $scope.selectAll();
                    }else if ($scope.selectedSign == "one"){
                        if ($rootScope.id != ""){
                            $scope.selectInboundProblemRecord($rootScope.id);
                        }
                    }

                }
            });
        };

        //websocket 推送pod的结果
        function getPodResult()  {
            var option={
                "user":$rootScope.workStationIds,
                "url":"websocket/getPod/"+$rootScope.workStationIds,
                "onmessageCall":onmessagePod
            }
            $scope.podSocket=webSocketService.initSocket(option);

            console.log("获取到推送的pod信息：" + $scope.podSocket)
        }
        function onmessagePod(msg){
            //接收到消息后做的业务处理代码
            var date = new Date();
            console.log(date.toLocaleString()+"->$scope.podSocket 正在推送消息。。。");
            var data = JSON.parse(msg);
            if(data.pod != "success"){
                if (data.workstation == $rootScope.workStationIds) {
                    console.log(date.toLocaleString()+"->websocket推送pod的信息：",data);
                    $rootScope.podNo = data.pod ;
                    var grid = $('#problemInboundRightGrid').data('kendoGrid');
                    var rows = grid.tbody.find("tr");
                    // $rootScope.select_one = true;
                    // $rootScope.select_all = true;
                    grid.tbody.children('tr').addClass('k-state-selected');

                    // var rows = grid.select();
                    // var dataFiled = "";
                    for (var i = 0; i < rows.length; i++) {
                        var rowData = grid.dataItem(rows[i]);
                        console.log("选中行的id--->"+rowData.id);
                        $scope.selectInboundProblemRecord(rowData.id);

                    }
                }
            }
        };
        //关闭websocket
        function closeWebsocket(s,msg){
            var date = new Date();
            if(!($.isEmptyObject(s))){
                if(msg == "pod"){
                    console.log(date.toLocaleString()+"->Pod客户端主动关闭websocket连接");
                }
                s.close(3666,"客户端主动关闭websocket连接");
            }
        };

        //websocket 推送pod的结果
        // function getPodResult() {
        //     var url = PROBLEM_INBOUND.podWebSocket+$rootScope.workStationIds;
        //     console.log("url:",url);
        //     $scope.podSocket = new WebSocket(url);
        //     //打开事件
        //     $scope.podSocket.onopen = function () {
        //         console.log("podSocket 已打开");
        //     };
        //     //获得消息事件
        //     $scope.podSocket.onmessage = function (msg) {
        //         console.log("podSocket 正在推送消息。。。");
        //         var data = JSON.parse(msg.data);
        //         console.log("data:",data)
        //         if(data.pod != "success"){
        //             if (data.workstation == $rootScope.workStationIds) {
        //                 console.log("推送pod的信息：",data);
        //                 $rootScope.podNo = data.pod;
        //                 if ($scope.selectedSign == "all"){
        //                     $scope.selectAll();
        //                 }else if ($scope.selectedSign == "one"){
        //                     if ($rootScope.id != ""){
        //                         selectInboundProblemRecord($rootScope.id);
        //                     }
        //                 }
        //                 // var grid = $("#problemInboundRightGrid").data("kendoGrid");    // 行样式
        //                 // var rows = grid.tbody.find("tr");
        //                 // rows.each(function (i, row) {
        //                 //     var srcData = grid.dataItem(row);
        //                 //
        //                 //     if(data.pod != "" && srcData.name.startsWith(data.pod)){
        //                 //         $(row).css("background-color", "#ffc000");
        //                 //     }
        //                 // });
        //
        //             }
        //         }
        //     };
        //
        //     //关闭事件
        //     $scope.podSocket.onclose = function () {
        //         console.log("podSocket 关闭");
        //         // if($scope.podSocket.readyState != 1){
        //         //     $scope.podSocket = new WebSocket(url);
        //         //     if($scope.podSocket.readyState != 1){
        //         //         $scope.errorWindow("hardwareId1",$scope.hardwareWindows1);
        //         //     }
        //         // }
        //     };
        //     //发生了错误事件
        //     $scope.podSocket.onerror = function () {
        //         console.log("podSocket 发生了错误");
        //         $scope.podSocket = new WebSocket(url);
        //     }
        // }
        //刷新pod信息
        $scope.refreshesPod = function () {
            // if($rootScope.podNo == null || $rootScope.podNo == "" || $rootScope.podNo == "undefined"){
            inboundProblemService.refreshPod($rootScope.sectionId,$rootScope.workStationIds,function (poddata) {
                console.log("刷新的pod信息：",poddata);
                if ($rootScope.podNo!= ""){
                    $rootScope.podNo = poddata.pod;
                    // var grid = $("#cargoRecordGRID").data("kendoGrid");    // 行样式
                    var grid = $('#problemInboundRightGrid').data('kendoGrid');

                    var rows = grid.tbody.find("tr");
                    // $rootScope.select_one = true;
                    // $rootScope.select_all = true;
                    grid.tbody.children('tr').addClass('k-state-selected');

                    // var rows = grid.select();
                    // var dataFiled = "";
                    for (var i = 0; i < rows.length; i++) {
                        var rowData = grid.dataItem(rows[i]);
                        console.log("选中行的id--->"+rowData.id);
                        $scope.selectInboundProblemRecord(rowData.id);

                    }
                    // rows.each(function (i, row) {
                    //     var srcData = grid.dataItem(row);
                        // if(srcData.name.startsWith(poddata.pod)){
                        //     $(row).css("background-color", "#ffc000");
                        // }
                    // });
                }
            });
            // }
        };

    })
})();