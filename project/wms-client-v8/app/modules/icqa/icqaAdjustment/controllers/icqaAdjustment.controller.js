/**
 * Created by thoma.bian on 2017/5/10.
 */
(function () {
    'use strict';
    angular.module('myApp').controller("icqaAdjustmentCtl", function ($timeout,$rootScope,ICQA_CONSTANT, $scope, $window,icqaAdjustmentService,ICQABaseService,webSocketService) {
        $scope.edit = false;
        $scope.page = "main";
        $scope.andonlogin = "登录";
        $scope.stopcallPOD = "停止分配POD";
        $scope.podSocket;
        $scope.adjustmentAllSearch = true;  // 标志位搜索全部
        $("#noRemarksId").addClass("buttonColorGray");
        $("#releasePodId").addClass("buttonColorGray");
        $("#receiving-releasepod").addClass("buttonColorGray");
        $("#stopAllocationPod").addClass("buttonColorGray");
        $("#exitLoginId").addClass("buttonColorGray");
        $scope.remark = "";
        var columns = [
            {headerTemplate: "<span translate='选择'></span>", width: 35, template: "<input type=\"checkbox\"  ng-model='chk' id='dataItem.id' class='check-box' ng-checked = 'select_one' ng-click='selectOne(chk,dataItem.uid)'/>"},
            {field: "操作时间", headerTemplate: "<span translate='操作时间'></span>", template: function (item) {
                return item.modifiedDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.modifiedDate)) : "";
            }},
            {field: "问题类型", headerTemplate: "<span translate='问题类型'></span>",
                template: function (item) {
                    return item.recordType ? item.recordType : "";
                }
            },
            {field: "客户", headerTemplate: "<span translate='客户'></span>",template:function(item){
                return item.client? item.client.name: "";
            }},
            {field: "SKU",headerTemplate: "<span translate='SKU'></span>",template: function (item) {
                return item.itemNo ? "<div style='word-wrap:break-word'>"+item.itemNo+"</div>" : ""}},
            {field: "商品名称", headerTemplate: "<span translate='GOODS_NAME'></span>",template: function (item) {
                return item.itemDataGlobal ? item.itemDataGlobal.name:"";
            }},
            // {field: "f", headerTemplate: "<span translate='触发问题工具'></span>"},
            {field: "操作工具", headerTemplate: "<span translate='操作工具'></span>",template: function (item) {
                return item.recordTool ? item.recordTool:""}},
            // {field: "fromStorageLocation", headerTemplate: "<span translate='原始容器'></span>",hidden:true},

            {field: "原始容器", headerTemplate: "<span translate='原始容器'></span>",template: function (item) {
                return item.fromStorageLocation ? "<div style='word-wrap:break-word'>"+item.fromStorageLocation+"</div>" : ""}},

            // {field: "toStorageLocation", headerTemplate: "<span translate='目的容器'></span>",hidden:true},

            {field: "目的容器", headerTemplate: "<span translate='目的容器'></span>",template: function (item) {
                return item.toStorageLocation ? "<div style='word-wrap:break-word'>"+item.toStorageLocation+"</div>" : ""}},

            {field: "数量", headerTemplate: "<span translate='数量'></span>",template: function (item) {
                return item.amount ? item.amount: "";
            }},
            {field: "操作人", headerTemplate: "<span translate='操作人'></span>",template: function (item) {
                return item.operator ? item.operator: "";
            }},
            {field: "thoseResponsible", headerTemplate: "<span translate='责任人'></span>",menu:false,editor: function (container, options) {
                $('<input id="thoseResponsibleId" name="' + options.field + '" class="k-textbox"  />').appendTo(container);

            }},
            {field: "adjustReason", headerTemplate: "<span translate='原因'></span>",menu:false, editor: function (container, options) {
                $('<input  id="adjustReasonId" name="' + options.field + '" class="k-textbox"  />').appendTo(container);
            }
            },
            {field: "reasonName", headerTemplate: "<span translate='分析人员'></span>",menu:false, editor: function (container, options) {
                $('<input  id="reasonNameId" name="' + options.field + '" class="k-textbox" />').appendTo(container);
            }
            },
            {field: "problemDestination", headerTemplate: "<span translate='调整分析'></span>",menu:false, editor: function (container, options) {
                $('<input   id="problemDestinationId" name="' + options.field + '" class="k-textbox" />').appendTo(container);
            }},
            {field: "other", headerTemplate: "<span translate='选择项目'></span>",menu:false}];
        $scope.option = {"remark":"","seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};

        // 选中全部
        $scope.selectAll = function () {
            var grid = $('#icqaAdjustmentGrid').data('kendoGrid');
            if ($scope.adjustmentAllSearch){
                if ($scope.select_all) {
                    $scope.select_one = true;
                    grid.tbody.children('tr').addClass('k-state-selected');
                    //grid.select(grid.tbody.find(">tr"));
                } else {
                    $scope.select_one = false;
                    grid.tbody.children('tr').removeClass('k-state-selected');
                }
            }else {
                if ($scope.select_all) {
                    $("#allContentId").addClass("buttonColorGray");
                    $("#noRemarksId").removeClass("buttonColorGray");
                    $scope.remark ="undisposed";
                    $scope.adjustmentAllSearch = false;
                    $scope.option={"remark":$scope.remark,"seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
                    icqaAdjustmentService.getItemAdjust($scope.option,function(data){
                        if($scope.edit) {
                            editorIcqaAdjustment(data);
                        }else {
                            editorIcqaAdjustmentNotEdit(data);
                        }

                        console.log(data.length);
                        console.log(data);
                        console.log(data[0]);
                        console.log(data[0].id);
                        for (var i = 0; i < data.length; i++) {
                            var uid = grid.dataSource.at(i).uid;
                            grid.tbody.children("tr[data-uid='" + uid + "']").addClass('k-state-selected')
                            // grid.select("tr[data-uid='" + uid + "']").addClass('k-state-selected');
                            $scope.select_one = true;
                            $scope.select_all = true;
                        }
                    });
                }else {
                    $scope.select_one = false;
                    grid.tbody.children('tr').removeClass('k-state-selected');
                }
            }
        };
        //选中单个
        $scope.chk = false;
        $scope.selectOne = function (val, uid) {
            var grid = $('#icqaAdjustmentGrid').data('kendoGrid');
            if (val) {
                grid.tbody.children('tr[data-uid="' + uid + '"]').addClass('k-state-selected');
                // grid.select("tr[data-uid='" + uid + "']");
            } else {
                grid.tbody.children('tr[data-uid="' + uid + '"]').removeClass('k-state-selected');
            }
        };
        function editOrNot() {
            if($scope.edit){
                icqaAdjustmentService.getItemAdjust($scope.option,function(data){
                    var grid = $("#icqaAdjustmentGrid").data("kendoGrid");
                    grid.setOptions( ICQABaseService.editGrid({
                            columns: columns,
                            height: $(document.body).height() - 210,
                            dataSource: {
                                data: data,
                                schema: {
                                    model: {
                                        sort: "",
                                        id: "id",
                                        fields: {
                                            "操作时间": {editable: false},
                                            "问题类型": {editable: false},
                                            "客户": {editable: false},
                                            "SKU": {editable: false},
                                            "商品名称": {editable: false},
                                            "操作工具": {editable: false},
                                            "原始容器": {editable: false},
                                            "目的容器": {editable: false},
                                            "操作人": {editable: false},
                                            "数量": {editable: false},
                                            "thoseResponsible": {editable: true},
                                            "adjustReason":{editable: true},
                                            "reasonName":{editable: true},
                                            "problemDestination":{editable: true},
                                            "other": {editable: false}
                                        }
                                    }
                                },
                                change:function(e) {
                                    changeAdjustment(e);
                                }
                            }
                        },function(){
                            remove();
                        })
                    )
                });
            }else {
                icqaAdjustmentService.getItemAdjust($scope.option,function(data){

                    var grid = $("#icqaAdjustmentGrid").data("kendoGrid");
                    grid.setOptions( ICQABaseService.editGrid({
                            columns: columns,
                            height: $(document.body).height() - 210,
                            dataSource: {
                                data: data,
                                schema: {
                                    model: {
                                        sort: "",
                                        id: "id",
                                        fields: {
                                            "操作时间": {editable: false},
                                            "问题类型": {editable: false},
                                            "客户": {editable: false},
                                            "SKU": {editable: false},
                                            "商品名称": {editable: false},
                                            "操作工具": {editable: false},
                                            "原始容器": {editable: false},
                                            "目的容器": {editable: false},
                                            "操作人": {editable: false},
                                            "数量": {editable: false},
                                            "thoseResponsible": {editable: false},
                                            "adjustReason":{editable: false},
                                            "reasonName":{editable: false},
                                            "problemDestination":{editable: false},
                                            "other": {editable: false}
                                        }
                                    }
                                },
                                change: function(){
                                    remove();
                                }
                            }
                        },function(){
                            remove();
                        })
                    )
                });

            }
        }
        // 移除最后一列的'选择项目'图标
        function remove() {
            var grid = $("#icqaAdjustmentGrid").data("kendoGrid");
            grid.thead.find("[data-field='操作时间']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='问题类型']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='客户']>.k-header-column-menu").remove();
            //   grid.thead.find("[data-field='sku']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='SKU']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='商品名称']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='操作工具']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='原始容器']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='目的容器']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='thoseResponsible']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='操作人']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='数量']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='adjustReason']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='reasonName']>.k-header-column-menu").remove();
            grid.thead.find("[data-field='problemDestination']>.k-header-column-menu").remove();

        }
        $scope.icqaAdjustemntAllSearch = function(){
            $("#noRemarksId").addClass("buttonColorGray");
            $("#allContentId").removeClass("buttonColorGray");
            $scope.remark ="";
            $scope.adjustmentAllSearch = true;
            $scope.option={"remark":$scope.remark,"seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
            icqaAdjustmentService.getItemAdjust($scope.option,function(data){
                if($scope.edit){
                    editorIcqaAdjustment(data);
                }else {
                    editorIcqaAdjustmentNotEdit(data);
                }
            });
        };
        $scope.icqaAdjustemntNoRemarksSearch = function(){
            $("#allContentId").addClass("buttonColorGray");
            $("#noRemarksId").removeClass("buttonColorGray");
            $scope.remark ="undisposed";
            $scope.adjustmentAllSearch = false;
            $scope.option={"remark":$scope.remark,"seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
            icqaAdjustmentService.getItemAdjust($scope.option,function(data){
                if($scope.edit) {
                    editorIcqaAdjustment(data);
                }else {
                    editorIcqaAdjustmentNotEdit(data);
                }
            });
        };

        $scope.icqaAdjustmentSearch = function(){
            $scope.option={"remark":$scope.remark,"seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
            icqaAdjustmentService.getItemAdjust($scope.option,function(data){
                if($scope.edit) {
                    editorIcqaAdjustment(data);
                }else {
                    editorIcqaAdjustmentNotEdit(data);
                }
            })
        };
        $scope.icqaAdjustmentKeyDown = function(e){
            if(!icqaAdjustmentService.autoAddEvent(e)) return;
            $scope.option={"remark":$scope.remark,"seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
            icqaAdjustmentService.getItemAdjust($scope.option,function(data){
                if($scope.edit) {
                    editorIcqaAdjustment(data);
                }else {
                    editorIcqaAdjustmentNotEdit(data);
                }
            })
        };

        function editorIcqaAdjustment(data){
            var grid = $("#icqaAdjustmentGrid").data("kendoGrid");
            grid.setDataSource(new kendo.data.DataSource({
                data: data,
                schema: {
                    model: {
                        id: "id",
                        fields: {
                            "操作时间": {editable: false},
                            "问题类型": {editable: false},
                            "客户": {editable: false},
                            "SKU": {editable: false},
                            "商品名称": {editable: false},
                            "操作工具": {editable: false},
                            "原始容器": {editable: false},
                            "目的容器": {editable: false},
                            "操作人": {editable: false},
                            "数量": {editable: false},
                            "thoseResponsible": {editable: true},
                            "adjustReason":{editable: true},
                            "reasonName":{editable: true},
                            "problemDestination":{editable: true},
                            "other": {editable: false}
                        }
                    }
                },
                change:function(e){
                    changeAdjustment(e)
                }
            }));
        }

        function editorIcqaAdjustmentNotEdit(data){
            var grid = $("#icqaAdjustmentGrid").data("kendoGrid");
            grid.setDataSource(new kendo.data.DataSource({
                data: data,
                schema: {
                    model: {
                        id: "id",
                        fields: {
                            "操作时间": {editable: false},
                            "问题类型": {editable: false},
                            "客户": {editable: false},
                            "SKU": {editable: false},
                            "商品名称": {editable: false},
                            "操作工具": {editable: false},
                            "原始容器": {editable: false},
                            "目的容器": {editable: false},
                            "操作人": {editable: false},
                            "数量": {editable: false},
                            "thoseResponsible": {editable: false},
                            "adjustReason":{editable: false},
                            "reasonName":{editable: false},
                            "problemDestination":{editable: false},
                            "other": {editable: false}
                        }
                    }
                },
                change:function(e){
                    changeAdjustment(e)
                }
            }));
        }

        function changeAdjustment(e){
            var dataMap;
            if($("#reasonNameId").attr("name")!=undefined || $("#problemDestinationId").attr("name")!= undefined || $("#adjustReasonId").attr("name")!=undefined || $("#thoseResponsibleId").attr("name")!=undefined){
                if($("#thoseResponsibleId").attr("name")=="thoseResponsible"){
                    dataMap = {"id": e.items[0].id, "thoseResponsible":e.items[0].thoseResponsible, "adjustReason":"", "problemDestination":"", "reasonName":""};
                } if($("#adjustReasonId").attr("name")=="adjustReason"){
                    dataMap = {"id": e.items[0].id, "thoseResponsible":"", "adjustReason":e.items[0].adjustReason, "problemDestination":"", "reasonName":""};
                }if($("#problemDestinationId").attr("name")=="problemDestination"){
                    dataMap = {"id": e.items[0].id, "thoseResponsible":"", "adjustReason":"", "problemDestination":e.items[0].problemDestination, "reasonName":""};
                } if($("#reasonNameId").attr("name")=="reasonName"){
                    dataMap = {"id": e.items[0].id, "thoseResponsible":"", "adjustReason":"", "problemDestination":"", "reasonName":e.items[0].reasonName};
                }
                icqaAdjustmentService.updateAdjustment(dataMap, function (data) {
                    $scope.changeColor();
                },function() {
                    $scope.changeColor();
                    // $scope.remarks();

                    if (data.key == "EX_ANDON_MASTER_USERNAME_NOT_EXIST") {
                        e.items[0].reasonName = "";
                        e.items[0].thoseResponsible = "";
                        $("#remarksId").parent().addClass("mySelect");
                        $scope.remarksWindow.setOptions({
                            width: 600,
                            height: 150,
                            visible: false,
                            actions: false
                        });
                        $scope.remarksWindow.center();
                        $scope.remarksWindow.open();
                    }
                });
            }
        }
        // 变色
        $scope.changeColor = function() {
            if ($scope.podNo != "") {
                var grid = $("#icqaAdjustmentGrid").data("kendoGrid");    // 行样式
                var rows = grid.tbody.find("tr");
                rows.each(function (i, row) {
                    var srcData = grid.dataItem(row);
                    if ($scope.podNo != "" && srcData.fromStorageLocation != undefined) {
                        if (srcData.fromStorageLocation.startsWith($scope.podNo)) {
                            $(row).css("background-color", "#ffc000");

                        }
                    }
                    if ($scope.podNo != "" && srcData.toStorageLocation != undefined) {
                        if (srcData.toStorageLocation.startsWith($scope.podNo)) {
                            $(row).css("background-color", "#ffc000");
                        }
                    }
                });
            }else {
                $timeout($scope.refreshPod(),5000);
                // $scope.refreshPod();
                // getPodResult();
            }
        }

        //提示用户不存在
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

        //确定
        $scope.remarksWindowSure = function () {
            $scope.remarksWindow.close();
            // $("#reasonNameId").val("");
            // $("#thoseResponsibleId").val("");
            // $("#reasonNameId").refresh();

            // $scope.icqaAdjustemntAllSearch();//刷新
        }

        //点击登录 (呼叫pod)
        $scope.loginWorkStation = function () {
            if($scope.andonlogin == "登录"){
                $scope.page = "workStationPage";
                setTimeout(function(){ $("#receiving_station").focus();}, 0);
            }
            if ($scope.andonlogin == "呼叫POD"){

                var grid = $('#icqaAdjustmentGrid').data('kendoGrid');
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
                if(dataFiled.length > 0){
                    $("#releasePodId").removeAttr("disabled");
                    $("#stopAllocationPod").removeAttr("disabled");
                    $("#receiving-releasepod").removeAttr("disabled");


                    $("#releasePodId").removeClass("buttonColorGray");
                    $("#receiving-releasepod").removeClass("buttonColorGray");
                    $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});

                    icqaAdjustmentService.stopCallPod("start",$scope.workStationIds,function (data) {
                    });
                    //对应调整过滤pod
                    icqaAdjustmentService.callPodIcqaAdjustment(dataFiled,$scope.sectionId, function (data) {
                        console.log(data);
                        var callData = {
                            "pods":data,
                            "workStationId": $scope.workStationId
                        };
                        // icqaAdjustmentService.callPodInterface({callData:JSON.stringify(callData)},function(){
                        icqaAdjustmentService.callPodInterface(JSON.stringify(callData),function(){
                            getPodResult();
                            // $scope.icqaAdjustmentSearch();
                            // $scope.select_all = false;
                        });

                    })
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
            }
        };

        // 扫描工作站
        $scope.workStation = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode != 13) return;
            icqaAdjustmentService.getIcqaAdjustmentStation($scope.workstation, function(data){
                $rootScope.workstationValue = data.name;
                $scope.workStationIds = data.workStation.id;
                $scope.sectionId = data.workStation.sectionId;
                $scope.workStationId = data.id;
                $scope.page = "main";
                $scope.andonlogin = "呼叫POD";


                icqaAdjustmentService.yesOrNoFinsh($rootScope.workstationValue, function(data) {
                    if (data){
                        // 不能直接退出
                        $scope.stopcallPOD = "停止分配POD";
                        $("#releasePodId").removeAttr("disabled");
                        $("#stopAllocationPod").removeAttr("disabled");
                        $("#receiving-releasepod").removeAttr("disabled");


                        $("#releasePodId").removeClass("buttonColorGray");
                        $("#receiving-releasepod").removeClass("buttonColorGray");
                        $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                    }else {
                        // 和初次进入一样
                        $scope.stopcallPOD = "恢复分配POD";

                    }
                });


                // $scope.stopcallPOD = "停止分配POD";
                $scope.startDate = "";
                $scope.endDate = "";
                $scope.seekContent = "";
                $scope.option={"remark":$scope.remark,"seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
                $("#exitLoginId").removeAttr("disabled");

                $("#exitLoginId").removeClass("buttonColorGray");
                $scope.edit = true;  //标志位登陆之后可编辑
                // $scope.edit = false;
                editOrNot();
                $scope.workingStation = false;

            }, function(data){
                if(data.key == "ICQA_STOCKTAKING_WORKSTATION_SOMEONE"){
                    $scope.errorMessage = "已被占用";
                }else if(data.key == "ICQA_STOCKTAKING_NO_WORKSTATION"){
                    $scope.errorMessage = "不是一个有效工作站";
                }
                $scope.workingStation = true;
            });
        };
        //点击退出(解绑工作站)
        $scope.exitAndon = function(){
            icqaAdjustmentService.yesOrNoFinsh($rootScope.workstationValue, function(data){
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

                    $scope.page = "main";
                    icqaAdjustmentService.exitIcqaAdjustmentStation($rootScope.workstationValue, function(data){
                        $scope.andonlogin = "登录";
                        $scope.stopcallPOD = "停止分配POD";
                        $scope.startDate = "";
                        $scope.endDate = "";
                        $scope.seekContent = "";
                        closeWebsocket($scope.podSocket,"pod");
                        // if(podSocket != undefined && podSocket != null && podSocket != ""){
                        //     podSocket.close();
                        // }
                        $scope.option={"remark":$scope.remark,"seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
                        $("#exitLoginId").attr("disabled", true);
                        $("#releasePodId").attr("disabled",true);
                        $("#receiving-releasepod").attr("disabled",true);
                        $("#stopAllocationPod").attr("disabled",true);
                        $("#stopAllocationPod").removeClass("buttonColorRed");
                        $("#stopAllocationPod").removeClass("buttonColorGray");
                        $("#stopAllocationPod").css({"backgroundColor": "#F7F7F7"});
                        $scope.edit = false;
                        editOrNot();
                        // alert("处理方式和处理人员不可编辑");

                    })
                }
            })

        };
        // 停止分配pod
        $scope.stopAllocationPodGrid = function () {
            if ($scope.stopcallPOD == "停止分配POD"){
                icqaAdjustmentService.stopCallPod("stop",$scope.workStationIds,function (data) {
                    $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
                    $scope.stopcallPOD = "恢复分配POD";
                })
            }else {
                icqaAdjustmentService.stopCallPod("start",$scope.workStationIds,function (data) {
                    $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                    $scope.stopcallPOD = "停止分配POD";
                })
            }
        };
        function removeColor(){
            var grid = $("#icqaAdjustmentGrid").data("kendoGrid");    // 行样式
            var rows = grid.tbody.find("tr");
            rows.each(function (i, row) {
                var srcData = grid.dataItem(row);
                console.log($scope.podNo);
                if ($scope.podNo != "" && srcData.fromStorageLocation != undefined) {
                    if (srcData.fromStorageLocation.startsWith($scope.podNo)) {
                        console.log("移除颜色--->" + $scope.podNo);
                        $(row).css("background-color", "#FFFFFF");

                    }
                }
                if ($scope.podNo != "" && srcData.toStorageLocation != undefined) {
                    if (srcData.toStorageLocation.startsWith($scope.podNo)) {
                        console.log("移除颜色--->" + $scope.podNo);
                        $(row).css("background-color", "#FFFFFF");
                    }
                }
            });
            $("#icqaAdjustmentGrid").data("kendoGrid").tbody.find("tr").css("color","black");

        }



        // 释放pod
        $scope.releasePodGrid = function () {
            icqaAdjustmentService.reservePod($scope.podNo,$scope.sectionId,"false",$scope.workStationIds,$scope.workStationId,function (data) {
                removeColor();
                $scope.podNo = "";
                // $(row).removeClass("background-color");
                $scope.podNo = data.pod;
                // $scope.icqaAdjustmentSearch();
                console.log("释放前一个pod，获取新Pod--->" +data.pod);
                $scope.changeColor();
                // if ($scope.podNo != "") {
                //     var grid = $("#icqaAdjustmentGrid").data("kendoGrid");    // 行样式
                //     var rows = grid.tbody.find("tr");
                //     rows.each(function (i, row) {
                //         var srcData = grid.dataItem(row);
                //         console.log($scope.podNo);
                //         if ($scope.podNo != "" && srcData.fromStorageLocation != undefined) {
                //             if (srcData.fromStorageLocation.startsWith($scope.podNo)) {
                //                 console.log("变色pod--->" + $scope.podNo);
                //                 $(row).css("background-color", "#ffc000");
                //
                //             }
                //
                //         }
                //         if ($scope.podNo != "" && srcData.toStorageLocation != undefined) {
                //             if (srcData.toStorageLocation.startsWith($scope.podNo)) {
                //                 console.log("变色pod--->" + $scope.podNo);
                //                 $(row).css("background-color", "#ffc000");
                //             }
                //         }
                //     });
                // }
                // $timeout($scope.refreshPod(),5000);
                // $scope.refreshPod();
                /*  $scope.podNo = data.pod;

                 }else {
                 $scope.refreshPod();
                 }*/
                $scope.select_all = false;
            },function () {
                removeColor();
                $timeout($scope.refreshPod(),5000);
            })

        };

        //websocket 推送pod的结果
        function getPodResult() {
            var option={
                "user":$scope.workStationIds,
                "url":"websocket/getPod/"+$scope.workStationIds,
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
                if (data.workstation == $scope.workStationIds) {
                    console.log(date.toLocaleString()+"->websocket推送pod的信息：",data);
                    $scope.podNo = data.pod ;

                    var grid = $("#icqaAdjustmentGrid").data("kendoGrid");    // 行样式

                    var rows = grid.tbody.find("tr");
                    rows.each(function (i, row) {
                        var srcData = grid.dataItem(row);
                        if (data.pod != "" && srcData.fromStorageLocation != undefined){
                            if(srcData.fromStorageLocation.startsWith(data.pod)){
                                $(row).css("background-color", "#ffc000");
                            }

                        }
                        if (data.pod != "" && srcData.toStorageLocation != undefined){
                            if (srcData.toStorageLocation.startsWith(data.pod)){
                                $(row).css("background-color", "#ffc000");

                            }
                        }

                    });

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

        // //websocket 推送pod的结果
        // var podSocket;//webSocket
        // function getPodResult() {
        //     debugger;
        //     var url = ICQA_CONSTANT.podWebSocket+$scope.workStationIds;
        //     console.log("url:",url);
        //     podSocket = new WebSocket(url);
        //     //打开事件
        //     podSocket.onopen = function () {
        //         console.log("podSocket 已打开");
        //     };
        //     //获得消息事件
        //     podSocket.onmessage = function (msg) {
        //         console.log("podSocket 正在推送消息。。。");
        //         var data = JSON.parse(msg.data);
        //         if(data.pod != "success"){
        //             if (data.workstation == $scope.workStationIds) {
        //                 console.log("推送pod的信息：",data);
        //                 $scope.podNo = data.pod;
        //
        //                 var grid = $("#icqaAdjustmentGrid").data("kendoGrid");    // 行样式
        //
        //                 var rows = grid.tbody.find("tr");
        //                 rows.each(function (i, row) {
        //                     var srcData = grid.dataItem(row);
        //                     if (data.pod != "" && srcData.fromStorageLocation != undefined){
        //                         if(srcData.fromStorageLocation.startsWith(data.pod)){
        //                             $(row).css("background-color", "#ffc000");
        //
        //                             // var selIDs=$("#icqaAdjustmentGrid").getGridParam("selarrrow");
        //                             // var strValue = $("#icqaAdjustmentGrid").jqGrid("getCell",$(row),"thoseResponsible");
        //                             // // $("#grid").jqGrid("setColProp",{editType:"text",formatter:"text"});
        //                             // $("#icqaAdjustmentGrid").jqGrid("strValue",{editable:true});
        //
        //                         }
        //
        //                     }
        //                     if (data.pod != "" && srcData.toStorageLocation != undefined){
        //                         if (srcData.toStorageLocation.startsWith(data.pod)){
        //                             $(row).css("background-color", "#ffc000");
        //
        //                         }
        //                     }
        //
        //                 });
        //             }
        //         }
        //     };
        //     //关闭事件
        //     podSocket.onclose = function () {
        //         console.log("podSocket 关闭");
        //         // if(podSocket.readyState != 1){
        //         //     podSocket = new WebSocket(url);
        //         //     if(podSocket.readyState != 1){
        //         //         $scope.errorWindow("hardwareId1",$scope.hardwareWindows1);
        //         //     }
        //         // }
        //     };
        //     //发生了错误事件
        //     podSocket.onerror = function () {
        //         console.log("podSocket 发生了错误");
        //         podSocket = new WebSocket(url);
        //     }
        // }

        editOrNot();
        //刷新pod信息
        $scope.refreshPod = function () {
            // if($scope.podNo == null || $scope.podNo == "" || $scope.podNo == "undefined"){

            icqaAdjustmentService.refreshPod($scope.sectionId,$scope.workStationIds,function (poddata) {
                console.log("刷新的pod信息：",poddata);
                $scope.podNo = poddata.pod;
                console.log("7777777777---：",$scope.podNo);
                if ($scope.podNo != null){
                    console.log("-----------" +$scope.podNo);
                    var grid = $("#icqaAdjustmentGrid").data("kendoGrid");    // 行样式
                    var rows = grid.tbody.find("tr");
                    rows.each(function (i, row) {
                        var srcData = grid.dataItem(row);
                        if ($scope.podNo != "" && srcData.fromStorageLocation != undefined){
                            if(srcData.fromStorageLocation.startsWith($scope.podNo)){
                                $(row).css("background-color", "#ffc000");
                            }
                        }
                        if ($scope.podNo != "" && srcData.toStorageLocation != undefined){
                            if (srcData.toStorageLocation.startsWith($scope.podNo)){
                                $(row).css("background-color", "#ffc000");
                            }

                        }
                    });
                }
            });
            // }
        };
        // getPodResult();

    })
})();
