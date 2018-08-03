/**
 * Created by thoma.bian on 2017/5/10.
 */

(function () {
    'use strict';
    angular.module('myApp').controller("icqaAndonCtl", function ($rootScope ,$timeout, ICQA_CONSTANT,$scope, $window,ICQABaseService,icqaAndonService,webSocketService) {
        $scope.page = "main";
        $scope.andonlogin = "登录";
        $scope.stopcallPOD = "停止分配POD";
        $scope.podSocket;
        // $scope.edit = false;   //标志位 处理方式 和处理人员不可编辑
        $("#noRemarksId").addClass("buttonColorGray");
        $("#releasePodId").addClass("buttonColorGray");
        $("#receiving-releasepod").addClass("buttonColorGray");
        $("#stopAllocationPod").addClass("buttonColorGray");
        $("#exitAndonId").addClass("buttonColorGray");
        $scope.andonAllSearch = true;  // 标志位搜索全部


        $scope.state = "";
        var columns = [
            {headerTemplate: "<span translate='选择'></span>", width: 35, template: "<input type=\"checkbox\"  ng-model='chk' id='dataItem.id' class='check-box' ng-checked = 'select_one' ng-click='selectOne(chk,dataItem.uid)'/>"},
            {field: "anDonMasterType.description", headerTemplate: "<span translate='问题类型'></span>", template: function(item){
                return item.problemDescription? item.problemDescription: item.anDonMasterType.description;
            }},
            {field: "storageLocation", headerTemplate: "<span translate='货位/扫描枪'></span>", template: function(item){
                return item.storageLocation? item.storageLocation.name:item.problemName;
            }},
            {field: "reportBy", headerTemplate: "<span translate='触发人员'></span>"},
            {field: "reportDate",headerTemplate: "<span translate='触发时间'></span>", template: function (item) {
                return item.reportDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.reportDate)) : "";
            }},
            {field: "anDonMasterPeSolve", headerTemplate: "<span translate='处理方式'></span>",
                template: function (item) {
                    return item.anDonMasterPeSolve ? item.anDonMasterPeSolve.name:"";
                },
                editor: function (container, options) {
                    $('<input id="anDonMasterPeSolveId" name="' + options.field + '"  />')
                        .appendTo(container)
                        .kendoComboBox({
                            dataTextField: "name",
                            dataValueField: "id"
                        });
                    icqaAndonService.selectAndonType(options.model.anDonMasterType.id,function(data){
                        var combox = $("#anDonMasterPeSolveId").data("kendoComboBox");
                        combox.setDataSource(new kendo.data.DataSource({data: data}));
                    });
                }
            },
            {field: "solveBy", headerTemplate: "<span translate='处理人员'></span>", editor: function (container, options) {
                $('<input id="solveById" name="' + options.field + '" class="k-textbox" />').appendTo(container);
            }
            }];
        $scope.option={"state":$scope.state,"anDonMasterType":"","seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
        // 选中全部
        $scope.selectAll = function () {
            if ($scope.andonAllSearch){   //搜索全部
                var grid = $('#icqaAndonGrid').data('kendoGrid');
                if ($scope.select_all) {
                    $scope.select_one = true;
                    grid.tbody.children('tr').addClass('k-state-selected');
                    //grid.select(grid.tbody.find(">tr"));
                } else {
                    $scope.select_one = false;
                    grid.tbody.children('tr').removeClass('k-state-selected');
                }
            }else {    // 搜索未备注项
                var grid = $("#icqaAndonGrid").data("kendoGrid");
                if ($scope.select_all) {
                    // $scope.icqaAndonNoRemarksSearch();
                    $("#allContentId").addClass("buttonColorGray");
                    $("#noRemarksId").removeClass("buttonColorGray");
                    $scope.andonAllSearch = false;
                    $scope.select_one = false;
                    $scope.select_all = false;
                    $scope.state ="undisposed";
                    $scope.option={"state":$scope.state,"anDonMasterType":"","seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
                    icqaAndonService.getAndonMasters($scope.option,function(data){
                        if ($scope.edit){
                            editorAndon(data);
                        }else {
                            notEditorAndon(data);
                        }
                        console.log(data.length);
                        console.log(data);
                        console.log(data[0]);
                        console.log(data[0].id);
                        for (var i = 0; i < data.length; i++) {
                            var grid = $("#icqaAndonGrid").data("kendoGrid");
                            var uid = grid.dataSource.at(i).uid;
                            grid.tbody.children("tr[data-uid='" + uid + "']").addClass('k-state-selected')
                            // grid.select("tr[data-uid='" + uid + "']").addClass('k-state-selected');
                            $scope.select_one = true;
                            $scope.select_all = true;

                        }
                    })

                }else {
                    $scope.select_one = false;
                    grid.tbody.children('tr').removeClass('k-state-selected');
                }
            }
        };
        //选中单个
        // $scope.chk = false;
        $scope.selectOne = function (val, uid) {
            var grid = $('#icqaAndonGrid').data('kendoGrid');
            if (val) {
                // grid.select("tr[data-uid='" + uid + "']");
                grid.tbody.children('tr[data-uid="' + uid + '"]').addClass('k-state-selected');
                // var rowData = grid.dataItem(grid.select());
                // selectInformationRow(rowData.id);
                // console.log("选中行的ID:"+rowData.id);

            } else {
                grid.tbody.children('tr[data-uid="' + uid + '"]').removeClass('k-state-selected');
            }
        };

        // // 变色
        // function selectInformationRow(id) {
        //     icqaAndonService.callPodAndon(id, function (data) {
        //         console.log("Andonpod",data);
        //         // var data = grid.getRowData();
        //         for (var i = 0; i < data.length; i++) {
        //             console.log("$scope.podNo:"+$scope.podNo);
        //             if ($scope.podNo != "" && data[i].podFace.startsWith($rootScope.podNo)){
        //                 grid.setRowData(id,{background:"#ffc000"});
        //                 return;
        //             }
        //         }
        //     });
        // }

        icqaAndonService.getAndonMasters($scope.option, function (data) {
            var grid = $('#icqaAndonGrid').data('kendoGrid');
            grid.setOptions(  ICQABaseService.editGrid({
                columns: columns,
                dataSource: {
                    data: data,
                    schema: {
                        model: {
                            id: "id",
                            fields: {
                                "anDonMasterType.description": {editable: false},
                                "storageLocation": {editable: false},
                                "reportBy": {editable: false},
                                "reportDate": {editable: false},
                                "anDonMasterPeSolve": {editable: false},
                                "solveBy": {editable: false}
                            }
                        }
                    }
                },
                height: $(document.body).height() - 210
            }))

        });
        $scope.icqaAndonAllSearch = function(){
            $("#noRemarksId").addClass("buttonColorGray");
            $("#allContentId").removeClass("buttonColorGray");
            $scope.state ="";
            $scope.andonAllSearch = true;
            $scope.select_one = false;
            $scope.select_all = false;
            $scope.option={"state":$scope.state,"anDonMasterType":"","seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
            icqaAndonService.getAndonMasters($scope.option,function(data){
                if ($scope.edit){
                    changeColor();
                    editorAndon(data);
                }else {
                    notEditorAndon(data);
                }

            })
        };

        function changeColor() {
            if ($scope.podNo != ""){
                var grid = $("#icqaAndonGrid").data("kendoGrid");    // 行样式
                var rows = grid.tbody.find("tr");
                console.log($scope.podNo);
                // var rows = grid.select();
                rows.each(function (i, row) {
                    var srcData = grid.dataItem(row);
                    if (srcData.storageLocation != undefined && srcData.storageLocation != null && srcData.storageLocation != ""){
                        if($scope.podNo != "" && srcData.storageLocation.name.startsWith($scope.podNo)){
                            $(row).css("background-color", "#ffc000");
                        }
                    }

                });
            }else{
                $scope.refreshPod();
            }

        }

        $scope.icqaAndonNoRemarksSearch = function(){
            $("#allContentId").addClass("buttonColorGray");
            $("#noRemarksId").removeClass("buttonColorGray");
            $scope.andonAllSearch = false;
            $scope.select_one = false;
            $scope.select_all = false;
            $scope.state ="undisposed";
            $scope.option={"state":$scope.state,"anDonMasterType":"","seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
            icqaAndonService.getAndonMasters($scope.option,function(data){
                if ($scope.edit){
                    editorAndon(data);
                }else {
                    notEditorAndon(data);
                }
            })
        };

        $scope.icqaAndonSearch = function(){
            $scope.option={"state":$scope.state,"anDonMasterType":"","seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
            icqaAndonService.getAndonMasters($scope.option,function(data){
                if ($scope.edit){
                    editorAndon(data);
                }else {
                    notEditorAndon(data);
                }
            })
        };
        $scope.icqaAndonKeyDown = function(e){
            if(!icqaAndonService.autoAddEvent(e)) return;
            $scope.option={"state":$scope.state,"anDonMasterType":"","seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
            icqaAndonService.getAndonMasters($scope.option,function(data){
                if ($scope.edit){
                    editorAndon(data);
                }else {
                    notEditorAndon(data);
                }
            })
        };

        function editorAndon(data){
            var grid = $("#icqaAndonGrid").data("kendoGrid");
            grid.setDataSource(new kendo.data.DataSource({data: data,
                schema: {
                    model: {
                        id: "id",
                        fields: {
                            "anDonMasterType.description": { editable: false},
                            "storageLocation":{editable:false},
                            "reportBy" :{ editable: false },
                            "reportDate" :{ editable: false},
                            "anDonMasterPeSolve": {editable: true},
                            "solveBy": {editable: true}
                        }
                    }
                },
                change:function(e) {
                    changeAndon(e);
                }
            }));
        }

        function notEditorAndon(data){
            var grid = $("#icqaAndonGrid").data("kendoGrid");
            grid.setDataSource(new kendo.data.DataSource({data: data,
                schema: {
                    model: {
                        id: "id",
                        fields: {
                            "anDonMasterType.description": { editable: false},
                            "storageLocation":{editable:false},
                            "reportBy" :{ editable: false },
                            "reportDate" :{ editable: false},
                            "anDonMasterPeSolve": {editable: false},
                            "solveBy": {editable: false}

                        }
                    }
                },
                change:function(e) {
                    changeAndon(e);
                }
            }));
        }

        function changeAndon(e){
            if (e.items.length > 0) {
                var anDonMasterPeSolve = e.items[0].anDonMasterPeSolve;

                var solveBy = e.items[0].solveBy;
                var dataMap = {"id": e.items[0].id, "state": ''};
                anDonMasterPeSolve && (dataMap.anDonMasterPeSolveId = anDonMasterPeSolve.id);
                solveBy && (dataMap.solveBy = solveBy);
                if(anDonMasterPeSolve && solveBy){
                    dataMap.state="disposed";
                }else {
                    dataMap.state="undisposed";
                }
                icqaAndonService.updateAndonMasters(dataMap, function (data) {

                },function () {
                    // $scope.remarks();
                    e.items[0].solveBy = "";
                    //   $("#solveById").val("");
                    $("#remarksId").parent().addClass("mySelect");
                    $scope.remarksWindow.setOptions({
                        width: 600,
                        height: 150,
                        visible: false,
                        actions: false
                    });
                    $scope.remarksWindow.center();
                    $scope.remarksWindow.open();
                });

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
            //   $("#tipwindow_span").html("此用户不存在");
        };
        //确定
        $scope.remarksWindowSure = function () {
            // $("#solveById").val("");
            $scope.remarksWindow.close();
            // e.items[0].solveBy = "";
            /*setTimeout(function () {$("#solveById").focus();
             },0  );
             e.items[0].solveBy = "";*/

            // $("#solveById").refresh();
            // $scope.icqaAndonAllSearch();//刷新
        }

        //点击登录
        $scope.loginWorkStation = function () {
            if($scope.andonlogin == "登录"){
                $scope.page = "workStationPage";
                setTimeout(function(){ $("#andonStation").focus();}, 0);
            }
            if ($scope.andonlogin == "呼叫POD"){

                $("#releasePodId").removeAttr("disabled");
                $("#stopAllocationPod").removeAttr("disabled");
                $("#receiving-releasepod").removeAttr("disabled");

                $("#releasePodId").removeClass("buttonColorGray");

                $("#receiving-releasepod").removeClass("buttonColorGray");
                $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});

                icqaAndonService.stopCallPod("start",$scope.workStationIds,function (data) {
                });
                var grid = $('#icqaAndonGrid').data('kendoGrid');
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
                console.log("选中行数:--->" + dataFiled.length);
                if(dataFiled.length > 0){
                    // 呼叫pod
                    icqaAndonService.callPodAndon(dataFiled,$scope.sectionId, function(data) {
                        console.log("呼叫POD------>" + data);
                        var callData = {
                            "pods":data,
                            "workStationId": $scope.workStationId
                        };
                        // icqaAndonService.callPodInterface({callData:JSON.stringify(callData)},function(){
                        icqaAndonService.callPodInterface(JSON.stringify(callData),function(){
                            getPodResult();
                            // $scope.icqaAndonSearch();
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
            icqaAndonService.getAndonStation($scope.workstation, function(data){
                $rootScope.workstationValue = data.name;
                $scope.workStationIds = data.workStation.id;
                $scope.workStationId = data.id;
                $scope.sectionId = data.workStation.sectionId;
                $scope.page = "main";
                $scope.andonlogin = "呼叫POD";


                icqaAndonService.yesOrNoFinsh($rootScope.workstationValue, function(data){
                    if (data){
                        $scope.stopcallPOD = "停止分配POD";
                        $("#releasePodId").removeAttr("disabled");
                        $("#stopAllocationPod").removeAttr("disabled");
                        $("#receiving-releasepod").removeAttr("disabled");

                        $("#releasePodId").removeClass("buttonColorGray");

                        $("#receiving-releasepod").removeClass("buttonColorGray");
                        $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});

                    }else {
                        $scope.stopcallPOD = "恢复分配POD";
                    }
                });


                // $scope.stopcallPOD = "停止分配POD";
                $scope.startDate = "";
                $scope.endDate = "";
                $scope.seekContent = "";
                $("#exitAndonId").removeAttr("disabled");

                $("#exitAndonId").removeClass("buttonColorGray");
                $scope.edit = true;  //标志位登陆之后可编辑
                // icqaAndonYesRoNotEdit();
                $scope.workingStation = false;
                $scope.option={"state":$scope.state,"anDonMasterType":"","seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
                icqaAndonService.getAndonMasters($scope.option, function (data) {
                    var grid = $('#icqaAndonGrid').data('kendoGrid');
                    grid.setOptions( ICQABaseService.editGrid({
                        columns: columns,
                        dataSource: {
                            data: data,
                            schema: {
                                model: {
                                    id: "id",
                                    fields: {
                                        "anDonMasterType.description": {editable: false},
                                        "storageLocation": {editable: false},
                                        "reportBy": {editable: false},
                                        "reportDate": {editable: false},
                                        "anDonMasterPeSolve": {editable: true},
                                        "solveBy": {editable: true}
                                    }
                                }
                            },
                            change: function (e) {
                                changeAndon(e);
                            }
                        },
                        height: $(document.body).height() - 210
                    }))
                });
            }, function(data){
                if(data.key == "ICQA_STOCKTAKING_WORKSTATION_SOMEONE"){
                    $scope.errorMessage = "已被占用";
                }else if(data.key == "ICQA_STOCKTAKING_NO_WORKSTATION"){
                    $scope.errorMessage = "不是一个有效工作站";
                }
                $scope.workingStation = true;
            });
        };
        // 停止分配pod
        $scope.stopAllocationPodGrid = function () {
            if ($scope.stopcallPOD == "停止分配POD"){
                icqaAndonService.stopCallPod("stop",$scope.workStationIds,function (data) {
                    $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
                    $scope.stopcallPOD = "恢复分配POD";
                })
            }else {
                icqaAndonService.stopCallPod("start",$scope.workStationIds,function (data) {
                    $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                    $scope.stopcallPOD = "停止分配POD";
                })
            }
            // var grid = $('#icqaAndonGrid').data('kendoGrid');
            // var rows = grid.select();
            // var dataFiled = "";
            // for (var i = 0; i < rows.length; i++) {
            //     var rowData = grid.dataItem(rows[i]);
            //     if (i == 0) {
            //         dataFiled = rowData.id;
            //     } else {
            //         dataFiled += "," + rowData.id;
            //     }
            // }
            // icqaAndonService.stopAllocationPod(dataFiled, function () {
            //     $("#stopAllocationPod").addClass("buttonColorGreen");
            //     $scope.stopcallPOD = "恢复分配POD";
            //     $scope.icqaAndonSearch();
            //     $scope.select_all = false;
            // })
        };
        //点击退出(解绑工作站)
        $scope.exitAndon = function(){
            icqaAndonService.yesOrNoFinsh($rootScope.workstationValue, function(data){
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
                }else{

                    $scope.page = "main";
                    icqaAndonService.exitAndonStation($rootScope.workstationValue, function(data){
                        $scope.andonlogin = "登录";
                        $scope.stopcallPOD = "停止分配POD";
                        $scope.startDate = "";
                        $scope.endDate = "";
                        $scope.seekContent = "";
                        $scope.select_one = "";
                        closeWebsocket($scope.podSocket,"pod");
                        // if(podSocket != undefined && podSocket != null && podSocket != ""){
                        //     podSocket.close();
                        // }
                        $scope.option={"state":$scope.state,"anDonMasterType":"","seek": $scope.seekContent,"startDate":$scope.startDate,"endDate":$scope.endDate};
                        $("#exitAndonId").attr("disabled", true);
                        $("#releasePodId").attr("disabled",true);
                        $("#receiving-releasepod").attr("disabled",true);
                        $("#stopAllocationPod").attr("disabled",true);
                        $("#stopAllocationPod").removeClass("buttonColorRed");
                        $("#stopAllocationPod").removeClass("buttonColorGray");
                        $("#stopAllocationPod").css({"backgroundColor": "#F7F7F7"});

                        icqaAndonService.getAndonMasters($scope.option, function (data) {
                            var grid = $('#icqaAndonGrid').data('kendoGrid');
                            grid.setOptions(  ICQABaseService.editGrid({
                                columns: columns,
                                dataSource: {
                                    data: data,
                                    schema: {
                                        model: {
                                            id: "id",
                                            fields: {
                                                "anDonMasterType.description": {editable: false},
                                                "storageLocation": {editable: false},
                                                "reportBy": {editable: false},
                                                "reportDate": {editable: false},
                                                "anDonMasterPeSolve": {editable: false},
                                                "solveBy": {editable: false}
                                            }
                                        }
                                    }
                                },
                                height: $(document.body).height() - 210
                            }))

                        });
                    })

                    // })
                }
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

                    var grid = $("#icqaAndonGrid").data("kendoGrid");    // 行样式
                    var rows = grid.tbody.find("tr");
                    console.log($scope.podNo);
                    // var rows = grid.select();
                    rows.each(function (i, row) {
                        var srcData = grid.dataItem(row);
                        if (srcData.storageLocation != undefined && srcData.storageLocation != null && srcData.storageLocation != ""){
                            if(data.pod != "" && srcData.storageLocation.name.startsWith(data.pod)){
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
        //     var url = ICQA_CONSTANT.podWebSocket+$scope.workStationIds;
        //     console.log("url:",url);
        //     podSocket = new WebSocket(url);
        //     //打开事件
        //     podSocket.onopen = function () {
        //         console.log("podSocket 已打开");
        //     };
        //
        //
        //     //获得消息事件
        //     podSocket.onmessage = function (msg) {
        //         console.log("podSocket 正在推送消息。。。");
        //         var data = JSON.parse(msg.data);
        //         if(data.pod != "success"){
        //             if (data.workstation == $scope.workStationIds) {
        //                 console.log("推送pod的信息：",data);
        //                 $scope.podNo = data.pod;
        //
        //                 var grid = $("#icqaAndonGrid").data("kendoGrid");    // 行样式
        //
        //                 var rows = grid.tbody.find("tr");
        //                 console.log($scope.podNo);
        //                 // var rows = grid.select();
        //                 rows.each(function (i, row) {
        //                     var srcData = grid.dataItem(row);
        //                     if (srcData.storageLocation != undefined && srcData.storageLocation != null && srcData.storageLocation != ""){
        //                         if(data.pod != "" && srcData.storageLocation.name.startsWith(data.pod)){
        //                             $(row).css("background-color", "#ffc000");
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


        function removeColor(){
            debugger;
            var grid = $("#icqaAndonGrid").data("kendoGrid");    // 行样式
            var rows = grid.tbody.find("tr");
            console.log("11111111111"+$scope.podNo);
            // rows.removeClass("background-color");
            // var rows = grid.select();
            rows.each(function (i, row) {
                var srcData = grid.dataItem(row);
                console.log("22222222222222"+$scope.podNo);
                if (srcData.storageLocation != undefined && srcData.storageLocation != null && srcData.storageLocation != ""){
                    if($scope.podNo != "" && srcData.storageLocation.name.startsWith($scope.podNo)){
                        console.log("3333333333333"+$scope.podNo);
                        $(row).css("background-color", "#FFFFFF");

                        // $("#icqaAndonGrid").data("kendoGrid").tbody.find("tr").removeClass("background-color")
                    }
                }


            });
            $("#icqaAndonGrid").data("kendoGrid").tbody.find("tr").css("color","black");
            $scope.podNo = "";

        }


        // 释放pod
        $scope.releasePodGrid = function () {
            // var grid = $('#icqaAndonGrid').data('kendoGrid');
            // var rows = grid.select();
            // var dataFiled = "";
            // for (var i = 0; i < rows.length; i++) {
            //     var rowData = grid.dataItem(rows[i]);
            //     if (i == 0) {
            //         dataFiled = rowData.id;
            //     } else {
            //         dataFiled += "," + rowData.id;
            //     }
            // }
            icqaAndonService.reservePod($scope.podNo,$scope.sectionId,"false",$scope.workStationIds,$scope.workStationId,function (data) {
                removeColor();
                // $scope.icqaAndonSearch();
                $scope.podNo = data.pod;
                console.log("释放前一个pod，获取新Pod--->"+data.pod);
                if ($scope.podNo != ""){
                    var grid = $("#icqaAndonGrid").data("kendoGrid");    // 行样式
                    var rows = grid.tbody.find("tr");
                    console.log($scope.podNo);
                    // var rows = grid.select();
                    rows.each(function (i, row) {
                        var srcData = grid.dataItem(row);
                        if (srcData.storageLocation != undefined && srcData.storageLocation != null && srcData.storageLocation != ""){
                            if(data.pod != "" && srcData.storageLocation.name.startsWith(data.pod)){
                                $(row).css("background-color", "#ffc000");
                            }
                        }

                    });
                }else{
                    $timeout($scope.refreshPod(),5000);
                }
                // $scope.select_all = false;
            },function () {
                removeColor();
                $timeout($scope.refreshPod(),5000);
            })
        };

        //刷新pod信息
        $scope.refreshPod = function () {
            // if($scope.podNo == null || $scope.podNo == "" || $scope.podNo == "undefined"){
            icqaAndonService.refreshPod($scope.sectionId,$scope.workStationIds,function (poddata) {
                console.log("刷新的pod信息：",poddata);
                if (poddata.pod != ""){
                    $scope.podNo = poddata.pod;
                    var grid = $("#icqaAndonGrid").data("kendoGrid");     // 行样式

                    var rows = grid.tbody.find("tr");
                    rows.each(function (i, row) {
                        var srcData = grid.dataItem(row);
                        if (srcData.storageLocation != undefined && srcData.storageLocation != null && srcData.storageLocation != ""){
                            if(poddata.pod != "" && srcData.storageLocation.name.startsWith(poddata.pod)){
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