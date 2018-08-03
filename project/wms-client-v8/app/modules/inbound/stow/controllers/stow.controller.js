/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
    "use strict";

    angular.module('myApp').controller("stowCtl", function ($window, $scope, $rootScope, $state, $stateParams,webSocketService, receivingService, INBOUND_CONSTANT, receiving_commonService, mySocket) {
        $window.localStorage["currentItem"] = "stow";
        $("#receiving_user").html($window.localStorage["name"]); // 当前用户
        var headerStyle = {style:"font-size:16px;line-height:2;color:white;height:35px;background-color:'#ef7421';overflow:hidden"};
        var baseStyle = {style: "font-size:16px;height:25px;background-color:light;overflow:hidden"};
        var columns= [
            {field: "positionIndex", headerTemplate: "<span translate='NO'></span>", attributes: baseStyle,headerAttributes:headerStyle},
            {field: "receiveStorageName", headerTemplate: "<span translate='PICKCAR_NO'></span>", attributes: baseStyle,headerAttributes:headerStyle},
            {field: "amount", headerTemplate: "<span translate='SKU_COUNT'></span>", attributes: baseStyle,headerAttributes:headerStyle}
        ];
        $("#receiving-singlemode").css({"backgroundColor":"#6E6E6E"});
        $("#receiving-allmode").css({"backgroundColor":"#3f51b5"});
        $("#tipDiv").hide();
        $scope.fullfinish = '1';
        $scope.podstatus = '1';
        $scope.scanhead = '0';
        $scope.exitType = 'NO';
        $scope.user = $window.localStorage["name"];
        $scope.operateTime = '0.9小时';
        $scope.operateTotalCount = '250';
        $scope.operatePercentage = '260/小时';
        $scope.goal = '500';
        $scope.achieved = '83%';
        $scope.preCiper = 'VirtualPod';
        $scope.preLocation = 'VirtualLocation';
        $scope.isNotIntro = false;
        var scan_DAMAGED = false;
        var scan_ciper = false;
        var scan_pod = false;
        var isDamaged = false;
        var scan_product_info = false;
        var isSingle = false;
        var isAll = true;
        var isOld = true;
        var ciperFinish = false;
        var scan_bin = false;
        var finishType = INBOUND_CONSTANT.ALL;
        var stowType = INBOUND_CONSTANT.GENUINE;
        var thisid;
        var exitThreshold = null;
        var ciperCount = null;
        var damageAmount = null;
        var goodsMoreAmount = null;
        var positionIndex = null;
        var inputvalue = '';
        var ciper = '';
        var upid = null;
        var itemid = '';
        var useNotAfter='';
        var storageid = '';
        var podid = '';
        var podName = '';
        var amount = '';
        var array = null;
        var receiveType = INBOUND_CONSTANT.GENUINE;
        var maxAmount = null;
        var isGoodsMore = false;
        var isMore = false;
        var isExit = false;
        var normalFull = false;
        var item = null;
        var currentId='';
        var goodsMoreItemid = null;
        var stockUnits = null;
        var stationState = INBOUND_CONSTANT.GENUINE;
        $scope.finishState = '停止上架';
        var storages = [];
        var introStorages = [];


        $scope.isCiperflag = false;


        /*sd add start*/
        var podSocket;//webSocket
        /*sd add end*/
        $scope.podInfo = 'Pod';
        $scope.podShow = '1';
        $scope.mySocket = mySocket;
        $scope.webSocketBuilder = mySocket.webSocketBuilder;


        var websocketClient = null;
        //连接websocket
        /*function getPodResult() {
         var option = {
         "user": $scope.workStationId,
         "url": "websocket/getPod/" + $scope.workStationId,
         "onmessageCall": onmessageCall
         }
         if ($.isEmptyObject(websocketClient)) {
         websocketClient = webSocketService.initSocket(option)
         }
         }*/
        //接收到消息后做的业务处理代码
        function onmessageCall(msg){
            console.log("推送pod:",msg);
            var data = JSON.parse(msg);
            if(data.pod != "success"){
                if (data.workstation == $rootScope.workStationId) {
                    console.log("推送pod的信息：",data);
                    console.log("onMessage.pod-->"+data.pod);
                    if(podid===''||podid===null||podid===undefined){
                        $scope.getPodInfo(data.pod);
                    }
                }
            }
        }
        //关闭websocket
        function closeWebsocket(){
            if(!($.isEmptyObject(websocketClient))){
                console.log("客户端主动关闭websocket连接");
                websocketClient.close(3666,"客户端主动关闭websocket连接");
            }
        };

        $scope.buildWebSocketConnect = function () {
            var option = {
                "user": $scope.workStationId,
                "url": "websocket/getPod/" + $scope.workStationId,
                "onmessageCall": onmessageCall
            }
            if ($.isEmptyObject(websocketClient)) {
                websocketClient = webSocketService.initSocket(option)
            }
        };

        $scope.toReceiving = function () {
            if ($rootScope.locationTypeSize === 0) {
                $scope.fullfinish = '0';
                $scope.podstatus = '1';
                $scope.scanhead = '1';
                receivingService.getStorageLocationTypes(function (data) {
                    //填充数据
                    receiving_commonService.grid_BayType(data.cls.storageLocationDTOList, data.cls.binTypeColumn.column, data.cls.binTypeColumn.row);
                });
            } else {
                $scope.podstatus = '0';
                $scope.fullfinish = '1';
                $scope.scanhead = '1';
                $scope.status = 'init';
                $scope.buildWebSocketConnect();
                checkContainerIsScan();
            }
        };

        // 扫描工作站
        $scope.scanStation = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            receivingService.scanStowStation($scope.station, function (data) {
                scan_DAMAGED = false;
                scan_ciper = false;
                $("#tipDiv").hide();
                console.log("scanStowStation--->",data);
                $scope.maxAmount = data.transValue;
                $scope.stationId = data.cls.receiveStationId; // 工作站id
                $scope.stationName = data.cls.receiveStationName; // 工作站name
                $rootScope.stationId = $scope.stationId;
                $rootScope.stationName = $scope.stationName;
                $rootScope.sectionId = data.cls.sectionId;
                $rootScope.workStationId = data.cls.workStationId;
                $rootScope.locationTypeSize = data.cls.locationTypeSize;
                $rootScope.maxAmount = data.cls.maxAmount;
                $rootScope.processSize = data.cls.processSize;
                $rootScope.receiveProcessDTOList = data.cls.receiveProcessDTOList;
                $rootScope.normalStorageList = new Array();
                $rootScope.stowstoporcallpod = data.cls.callingPodFlag;
                var length = receiving_commonService.getObjCount($rootScope.receiveProcessDTOList);
                array = new Array();
                if (data.cls.stationState==='1') {
                    stationState = INBOUND_CONSTANT.GENUINE;
                }else{
                    stationState = INBOUND_CONSTANT.DAMAGED;
                    scan_DAMAGED = true;
                }
                for (var i = 0; i < length; i++) {
                    if(stationState===INBOUND_CONSTANT.GENUINE&&parseInt($rootScope.receiveProcessDTOList[i].positionIndex) === 1&&($rootScope.receiveProcessDTOList[i].receiveStorageName!=null||$rootScope.receiveProcessDTOList[i].receiveStorageName!=undefined||$rootScope.receiveProcessDTOList[i].receiveStorageName!='')){
                        scan_DAMAGED = true;
                        $rootScope.demagedPositionIndex = $rootScope.receiveProcessDTOList[i].positionIndex;
                        console.log("$rootScope.receiveProcessDTOList[i].positionIndex--->"+$rootScope.receiveProcessDTOList[i].positionIndex);
                        console.log("$rootScope.receiveProcessDTOList[i].positionIndex--->"+$rootScope.demagedPositionIndex);
                    }
                    if ($rootScope.receiveProcessDTOList[i].receiveStorageName !== null &&
                        $rootScope.receiveProcessDTOList[i].receiveStorageName !== '' &&
                        $rootScope.receiveProcessDTOList[i].receiveStorageName !== undefined) {
                        array.push($rootScope.receiveProcessDTOList[i]);
                    }
                }
                if (array.length === 0) {
                    if ($rootScope.locationTypeSize === 0) {
                        $scope.fullfinish = '0';
                        $scope.podstatus = '1';
                        $scope.scanhead = '1';
                        receivingService.getStorageLocationTypes(function (data) {
                            //填充数据
                            receiving_commonService.grid_BayType(data.cls.storageLocationDTOList, data.cls.binTypeColumn.column, data.cls.binTypeColumn.row);
                        });
                    } else {
                        $scope.podstatus = '0';
                        $scope.fullfinish = '1';
                        $scope.scanhead = '1';
                        $scope.buildWebSocketConnect();
                        checkContainerIsScan();
                    }
                } else {
                    $scope.status = 'max';
                    $scope.scanhead = '1';
                    $scope.receivingButton = '!confirm';
                    $("#receivedGRID").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: array}));
                }
            }, function (data) {
                $scope.scanstatus = '1';
                $("#warnStation").html(data.key||data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                $scope.LOGINSTATE = data.key||data.message.replace("[","").replace("]","").replace("Unknown Error","");
                $("#tipDiv").fadeIn(500);
            });
        };




        $scope.getPodInfo = function (pod) {
            // receiving_commonService.CloseWindowByBtn("reloadPod_Window");
            receivingService.getPodInfo(pod, INBOUND_CONSTANT.BIN, function (datas) {
                if (Number(datas.status) < 0 || datas.cls.totalRow === 0) {//pod信息不合法
                    $scope.podShow ='1';
                    scan_pod = false;
                } else {
                    console.log("pod--->",datas);
                    $scope.podShow ='0';
                    scan_pod = true;
                    podid = pod;
                    $scope.Pod = pod;
                    $scope.podInfo = pod;
                    podName = datas.cls.pod.name;
                    checkContainerIsScan();
                    storages = datas.cls.storageLocations;
                    $("#receiving_pod_layout").html("");
                    //层级货位类型
                    var levelTypes = [];
                    var totalHeight = 0;
                    for(var key = datas.cls.columnMap.length-1;key>=0;key--){
                        var count = storages.length-1;
                        if(key<datas.cls.columnMap.length-1){
                            for(var l=key+1;l<datas.cls.columnMap.length;l++){
                                count -=datas.cls.columnMap[l];
                            }
                        }
                        totalHeight+=storages[count].storageLocationType.height;
                    }
                    for(var key = datas.cls.columnMap.length-1;key>=0;key--){
                        var count = storages.length-1;
                        if(key<datas.cls.columnMap.length-1){
                            for(var l=key+1;l<datas.cls.columnMap.length;l++){
                                count -=datas.cls.columnMap[l];
                            }
                        }
                        levelTypes.push(parseFloat(storages[count].storageLocationType.height/totalHeight));
                    }
                    receiving_commonService.fillGrid(document.getElementById("receiving_pod_layout"), datas.cls.totalRow, "receiving_pod_layout", "receiving_pod_layout_item", datas.cls.columnMap,levelTypes);
                    setTimeout(function () {
                        //重新查找推荐pod
                        console.log("重新查找推荐pod");
                        if(itemid!==undefined&&itemid!==''&&itemid!==null){
                            receivingService.scanStowItem(itemid, ciper,podid, function (data) {
                                if(data.cls.introStorages===undefined||data.cls.introStorages===null||data.cls.introStorages===''||data.cls.introStorages.length===0){
                                    $scope.isNotIntro = true;
                                    //showNoIntroStorage();
                                    showGeneralWindow("提示","该POD无推荐货位，请扫描车牌将商品放回原处，或者释放POD");
                                }else{
                                    $scope.isNotIntro = false;
                                    introStorages = data.cls.introStorages;
                                    receiving_commonService.colorIntroStorage(introStorages,storages);
                                }
                            });
                        }
                        focusOnReceiveInputer();
                    }, 500);
                }
            },function () {
                $scope.podShow ='1';
                scan_pod = false;
            });
        };
        //自动满筐所有车牌
        $scope.deleteReceivingContainer = function (e) {
            $scope.status = 'init';
            $scope.receivingMode = 'init';
            $scope.receivingButton = 'init';
            receivingService.autoFullStowLocation($rootScope.stationName, function (data) {
                if(stationState===INBOUND_CONSTANT.GENUINE){
                    scan_DAMAGED = false;
                }
                scan_ciper = false;
                ciper = null;
                if ($rootScope.locationTypeSize === 0) {
                    $scope.fullfinish = '0';
                    $scope.podstatus = '1';
                    $scope.scanhead = '1';
                    receivingService.getStorageLocationTypes(function (data) {
                        //填充数据
                        receiving_commonService.grid_BayType(data.cls.storageLocationDTOList, data.cls.binTypeColumn.column, data.cls.binTypeColumn.row);
                    });
                } else {
                    $scope.podstatus = '0';
                    $scope.fullfinish = '1';
                    $scope.scanhead = '1';
                    $scope.buildWebSocketConnect();
                    checkContainerIsScan();
                }
            }, function (data) {
                alert("autoFull error");
            });
        };
        $scope.startPod = function () {
            receiving_commonService.getLocationTypes(function (data) {
                console.log("data-->" + data);
                receivingService.bindStorageLocationTypesToStow({
                    "locationTypeDTOS": data,
                    "stationid": $rootScope.stationId
                }, function (data) {
                    $scope.fullfinish = '1';
                    $scope.podstatus = '0';
                    $scope.scanhead = '1';
                    $scope.status = 'init';
                    $scope.buildWebSocketConnect();
                    checkContainerIsScan();
                    $("#").html(INBOUND_CONSTANT.SCANDAMAGED);
                    setTimeout(function () {
                        $("#receive-inputer").focus();
                    }, 200);
                }, function (data) {
                    console.log("bindTypeerror");
                    if (data.key === '该工作站已绑定货位类型') {
                        $scope.fullfinish = '1';
                        $scope.podstatus = '0';
                        $scope.scanhead = '1';
                        $scope.status = 'init';
                        $("#").html(INBOUND_CONSTANT.SCANDAMAGED);
                        //聚焦pod输入框
                        setTimeout(function () {
                            $("#receive-inputer").focus();
                        }, 200);
                    }
                });
            });
        };

        //多货取消
        $scope.amountCancle = function (id) {
            scan_product_info = false;
            $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
            $scope.product_info_con = '1';
            $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
            $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
            receiving_commonService.CloseWindowByBtn(id);
            receiving_commonService.CloseWindowByBtn("keyboard_window");
        };
        //收货数量弹出框
        $scope.finish_keyboard = function (isKeyDown,e) {
            if(isKeyDown){
                if(!receiving_commonService.autoAddEvent(e)) return;
            }
            if ($("#keyboard_inputer").val() === undefined || $("#keyboard_inputer").val() < 1) {
                $scope.keyboardStatus = '0';
                return;
            } else {
                $scope.keyboardStatus = '1';
            }
            amount = $("#keyboard_inputer").val();
            receivingService.finishStow({
                    receiveStationId: $rootScope.stationName,
                    itemid: itemid,
                    storageId: storageid,
                    amount: amount,
                    ciperName: ciper,
                    stowType: receiveType,
                    finishType: finishType,
                    stationName:$rootScope.stationName,
                    podid: podid,
                    toolName: INBOUND_CONSTANT.STOW
                }, function (data) {
                    $scope.remainder = parseInt($scope.remainder)-amount;
                        scan_product_info = false;
                        $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                        if (isGoodsMore) {
                            if(isDamaged){
                                $("#receiving_status_span").html("已成功上架" + amount + "件残品至</br>" + storageid + "<br>标记" + $scope.goodsMoreAmount + "件多货");
                            }else{
                                $("#receiving_status_span").html("已成功上架" + amount + "件商品至</br>" + storageid + "<br>标记" + $scope.goodsMoreAmount + "件多货");
                            }
                        } else {
                            if(isDamaged){
                                $("#receiving_status_span").html("已成功上架" + amount + "件残品至</br>" + storageid);
                            }else{
                                $("#receiving_status_span").html("已成功上架" + amount + "件商品至</br>" + storageid);
                            }
                        }
                        if (receiveType === INBOUND_CONSTANT.DAMAGED) {
                            $("#scanbadcib").css({"backgroundColor": "#008B00"});
                            $("#receiving_status_span").css({"backgroundColor": "#FF0000"});
                            $("#scanbadcib").html(amount);
                            isDamaged = false;
                        }
                        if (receiveType === INBOUND_CONSTANT.GENUINE) {
                            upid = receiving_commonService.findStorageLocation(storageid, storages);
                            console.log("upid-->" + upid);
                            $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                            $("#" + upid).css({"backgroundColor": "#008B00"});
                            $("#" + upid).children("span").text(amount);
                        }
                        if(parseInt(data.cls)<=0){
                            $scope.ciperTotalAmount = '0';
                            $scope.remainder = '0';
                            $scope.ciperAmount = '0';
                            receiving_commonService.receiving_tip_dialog("goods_general_ok_cancel", {
                                title: INBOUND_CONSTANT.SURECIPERCLOSE,
                                width: 600,
                                height: 400,
                                open: function () {
                                    $("#goods_content").html("此承载单元中剩余0件商品，是否关闭？");
                                },
                                close:function () {
                                    focusOnReceiveInputer();
                                }
                            });
                        }
                        reSetAllVar();
                        receiving_commonService.CloseWindowByBtn("window_img_ok_cancel_amount_sku");
                        var window = $("#keyboard_window").data("kendoWindow");
                        window.close();
                    focusOnReceiveInputer();
                },
                function (data) {
                    isGoodsMore = false;
                    if (data.key === '-6') {
                        receiving_commonService.receiving_tip_dialog("window_stow_goods_more_ok_cancel", {
                            title: INBOUND_CONSTANT.SUREGOODSMORE,
                            width: 600,
                            height: 500,
                            open: function () {
                                $scope.ciperAmount = data.values[1];
                                $scope.inputAmount = data.values[2];
                                $scope.goodsMoreAmount = data.values[3];
                            }
                        });
                    }else if(data.key === '-3') {
                        var options = {
                            title: INBOUND_CONSTANT.RESCANLOCATION,
                            width: 600,
                            height: 400,
                            open: function () {
                                $scope.tipvalue = '';
                                $("#newtipwindow_span").html(data.values[0]);
                                setTimeout(function () {
                                    $("#newtipwindow_inputer").focus();
                                }, 500);
                            },
                            close: function () {
                                setTimeout(function () {
                                    $("#receiving-inputer").focus();
                                }, 500);
                            }
                        };
                        receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);

                        receiving_commonService.CloseWindowByBtn("window_img_ok_cancel_amount_sku");
                        var window = $("#keyboard_window").data("kendoWindow");
                        window.close();
                        focusOnReceiveInputer();
                    } else {
                        if (isDamaged) {
                            if (data.key === '-1' || data.key === '-2') {
                                showStorageFull(data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
                            } else {
                                showGeneralWindow("扫描货筐错误", data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
                            }
                        } else {
                            showGeneralWindow(INBOUND_CONSTANT.RESCANLOCATION, data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
                        }
                    }
                });
        };
        //刷新pod信息
        $scope.refreshPod = function () {
            $("#receiving_pod_layout").html("");
            $scope.Pod = 'Pod';
            scan_pod = false;
            receivingService.refreshPod($rootScope.sectionId,$rootScope.workStationId,function (poddata) {
                if(poddata===null||poddata===undefined||poddata===''||poddata.pod===''||poddata.pod===null||poddata.pod===undefined){
                    $scope.podShow='1';
                    focusOnReceiveInputer();
                    return;
                }
                $scope.getPodInfo(poddata.pod);
                focusOnReceiveInputer();
            });
        };
        //释放pod
        $scope.releasePod = function () {
            if(!scan_DAMAGED){
                showGeneralWindow("请先绑定残品货筐","请先绑定残品货筐");
                return;
            }else{
                receiving_commonService.CloseWindowByBtn("releasePodWindow");
                if(podid===''||podid===undefined||podid===null){
                    podid='P0000000C';
                }
                receivingService.releasePod($rootScope.sectionId,$rootScope.workStationId,podid,false,$rootScope.stationId,function (data) {
                    $("#receiving_pod_layout").html('');
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANPOD);
                    scan_pod = false;
                    $("#receiving-inputer").focus();
                    podid = '';
                    $scope.podInfo = 'Pod';
                    if(data===null||data===''||data===undefined||data.pod===''||data.pod===undefined||data.pod===null){
                        $scope.podShow ='1';
                        return ;
                    }
                    $scope.getPodInfo(data.pod);
                    if(isDamaged){
                        $scope.product_info_con = 'hidden';
                        $("#product_info_span").css({"backgroundColor": "#EEEEE0"});
                        $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                        $scope.scanbadcib='1';
                    }else{
                        if(!scan_ciper){
                            $("#receiving_dn_span").html('');
                            $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                        }else{
                            if(!scan_product_info){
                                $scope.product_info_con = 'hidden';
                                $("#product_info_span").css({"backgroundColor": "#EEEEE0"});
                                $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                            }else{
                                $("#receiving_status_span").html("");
                                $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                            }
                        }
                    }
                });
            }
        };

        $scope.receivingscan = function (e) {
            if (!receiving_commonService.autoAddEvent(e)) return;
            var inputvalue = $("#receiving-inputer").val().trim()||$scope.tipvalue;
            if(inputvalue===null||inputvalue===''||inputvalue===undefined)return;
            console.log("inputvalue-->" + inputvalue);
            $("#receiving-inputer").val("");
            $scope.tipvalue = '';
            receiving_commonService.CloseWindowByBtn("newtipwindowwithinputer");
            if (!scan_DAMAGED) {//绑残品框
                receivingService.scanStowContainer(inputvalue, INBOUND_CONSTANT.DAMAGED, $rootScope.stationName, 1, function (data) {
                    if (data.status === '2') {//有商品,提示用户
                        $scope.scancontainerType = INBOUND_CONSTANT.DAMAGED;
                        storageid = inputvalue;
                        var options = {
                            width: 600,
                            height: 500,
                            title: INBOUND_CONSTANT.SUREUSELOCATION,
                            open: function () {
                                $scope.wimgstatus = 'hidden';
                                $("#win_content").html("当前货框:" + data.cls.storagelocationName + ",里面有" + data.cls.amount + "件商品，请重新确认是否继续使用当前货筐进行收货");
                            },
                            close: function () {
                                focusOnReceiveInputer();
                            }
                        };
                        receiving_commonService.receiving_tip_dialog("window_img_ok_cancel", options);
                    } else {
                        if (isDamaged&&!normalFull) {
                            finishGoods();
                        }
                        $rootScope.demagedPositionIndex = 1;
                        $scope.scanbadcib = '1';
                        scan_DAMAGED = true;
                        checkContainerIsScan();
                    }
                }, function (data) {
                    var options = {
                        title:INBOUND_CONSTANT.RESCANDAMAGED,
                        width:600,
                        height:400,
                        open:function () {
                            $scope.tipvalue = '';
                            $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("Unknown Error",""));
                            setTimeout(function () {
                                $("#newtipwindow_inputer").focus();
                            },500);
                        },
                        close:function () {
                            focusOnReceiveInputer();
                        }
                    };
                    receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                });
            } else {
                if (!scan_pod) {
                    showGeneralWindow("提示","Pod尚未就绪,请稍后操作／刷新Pod获取最新Pod信息");
                    /* if(inputvalue!==''||inputvalue!==null||inputvalue==undefined){
                     $scope.getPodInfo(inputvalue);
                     }*/
                } else {
                    console.log("isDamaged--->"+isDamaged);
                    if (!isDamaged) {//isDamaged = true为残品
                        //自动识别是否是车牌
                        receivingService.scanIsCiper(inputvalue, function (data) {
                            //是车牌true,不是false
                            $scope.scanDn = data;
                            if ($scope.scanDn) {

                                /*if($scope.isCiperflag){
                                 receiving_commonService.receiving_tip_dialog("goods_general_ok_cancel", {
                                 title: INBOUND_CONSTANT.SURECIPERCLOSE,
                                 width: 600,
                                 height: 500,
                                 open: function () {
                                 $("#goods_content").html("此承载单元中剩余0件商品，是否关闭？");
                                 },
                                 close:function () {
                                 focusOnReceiveInputer();
                                 }
                                 });
                                 }
                                 $scope.isCiperflag=true;*/
                                console.log("监测到车牌--->" + inputvalue);
                                scan_ciper = false;
                                ciperFinish = false;
                                $scope.ciperAmount = ' ';
                                $scope.remainder = '';
                                $scope.ciperTotalAmount = '';
                                scan_product_info = false;//扫描商品
                                scan_bin = false;
                                isDamaged = false;
                                isSingle = false;//单一收获？
                                $scope.scanDn = false;
                                $scope.scanbadcib = '1';
                                if (isAll) {
                                    finishType = INBOUND_CONSTANT.ALL;
                                }
                                receiveType = INBOUND_CONSTANT.GENUINE;
                                ciper = "";
                                storageid = "";
                                amount = "";
                                itemid = "";
                               // itemuuid = '';
                                useNotAfter='';
                                item = null;
                                $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                                $("#" + upid).children("span").text("");
                                upid = '';
                                $("#receiving_status_span").html("");
                                $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                                checkContainerIsScan();
                                receiving_commonService.backPodStorageColor(storages);

                                receivingService.scanStowCiper(inputvalue, receiveType, $rootScope.stationName, function (data) {
                                    ciper = inputvalue;
                                    console.log("stowciper--->",data);
                                    scan_ciper = true;
                                    ciperFinish = false;
                                    isExit = false;
                                    $scope.finishState = '停止上架';
                                    ciper = data.cls.storageLocation.name;
                                    maxAmount = data.cls.amount;
                                    $scope.ciperTotalAmount = maxAmount;
                                    $scope.remainder = maxAmount;
                                    $scope.product_info_con = '1';
                                    $("#receiving_dn_span").html(ciper);
                                    $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                    $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                    $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                                    $("#receiving_status_span").html("");
                                    $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                                    checkContainerIsScan();
                                },
                                    function (data) {
                                    // ciper = null;
                                    scan_ciper = false;
                                    if(data.key==='-1'){
                                        isExit = false;
                                        $scope.finishState = '停止上架';
                                        ciper = inputvalue;
                                        $("#receiving_dn_span").html(inputvalue);
                                        $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                        $scope.ciperTotalAmount = '';
                                        $scope.remainder = '';
                                        $("#receiving_tip").html(INBOUND_CONSTANT.RESCANCIPERORSTOPUP);
                                        var options = {
                                            title:INBOUND_CONSTANT.RESCANCIPERORSTOPUP,
                                            width:600,
                                            height:400,
                                            open:function () {
                                                $scope.tipvalue = '';
                                                $("#newtipwindow_span").html(data.values[0]);
                                                setTimeout(function () {
                                                    $("#newtipwindow_inputer").focus();
                                                },200);
                                            },
                                            close:function () {
                                                focusOnReceiveInputer();
                                            }
                                        };
                                        receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);

                                    }else{
                                        $("#receiving_dn_span").html(INBOUND_CONSTANT.RESCANCIPER);
                                        $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                                        $scope.ciperTotalAmount = '';
                                        $scope.remainder = '';
                                        $("#receiving_tip").html(INBOUND_CONSTANT.RESCANCIPER);
                                        var options = {
                                            title:INBOUND_CONSTANT.RESCANCIPER,
                                            width:600,
                                            height:400,
                                            open:function () {
                                                $scope.tipvalue = '';
                                                $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                                setTimeout(function () {
                                                    $("#newtipwindow_inputer").focus();
                                                },500);
                                            },
                                            close:function () {
                                                focusOnReceiveInputer();
                                            }
                                        };
                                        receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                                    }
                                });
                            }
                            else{
                                if (!scan_product_info) {
                                    itemid = inputvalue;
                                    receiving_commonService.backPodStorageColor(storages);
                                    receivingService.scanStowItem(itemid, ciper,podid, function (data) {
                                        if(data.cls.introStorages===undefined||data.cls.introStorages===null||data.cls.introStorages===''||data.cls.introStorages.length==0){
                                            showNoIntroStorage();
                                        }else{
                                            console.log("data--->", data);
                                            $("#receiving_status_span").html("");
                                            scan_product_info = true;
                                            console.log("data--->",data);
                                            item = data.cls.itemData;
                                            itemid = item.itemNo;
                                            introStorages = data.cls.introStorages;
                                            maxAmount = data.transValue;
                                            $scope.product_info_con = '0';
                                            $("#product_info_title").html("SKU:" + item.skuNo);
                                            $("#product_info_text").html("商品名称:" + item.name);
                                            setTimeout(function () {
                                                $("#receiving-inputer").focus();
                                            }, 200);
                                            cleanStatus();
                                            checkContainerIsScan();
                                            receiving_commonService.colorIntroStorage(introStorages,storages);
                                        }
                                    }, function (data) {
                                        item = data.values[3];
                                        console.log("data--->",data);
                                        cleanStatus();
                                        checkContainerIsScan();
                                        if (data.key === '-6') {//多货
                                            item = data.values[3];
                                            goodsMoreItemid = data.values[3].id;
                                            $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                            var options = {
                                                title: INBOUND_CONSTANT.SUREGOODSMORE,
                                                open: function () {
                                                    $("#win_goodsmore_content").html(data.values[0]+"<br>SKU:"+data.values[1]+"<br>商品名称:"+data.values[2]);
                                                },
                                                close: function () {
                                                    focusOnReceiveInputer();
                                                }
                                            };
                                            receiving_commonService.receiving_tip_dialog("window_goodsmore_ok_cancel", options);
                                        } else {
                                            $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                            var options = {
                                                title:INBOUND_CONSTANT.RESCANITEM,
                                                width:600,
                                                height:400,
                                                open:function () {
                                                    $scope.tipvalue = '';
                                                    $("#newtipwindow_span").html(data.message.replace("Unknown Error",""));
                                                    setTimeout(function () {
                                                        $("#newtipwindow_inputer").focus();
                                                    },500);
                                                },
                                                close:function () {
                                                    focusOnReceiveInputer();
                                                }
                                            };
                                            receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                                        }
                                    });
                                } else {
                                    //检查上架货位
                                    if (isDamaged) {
                                        console.log("残品检查货筐");
                                        receivingService.checkStowContainer(inputvalue, itemid, ciper, $rootScope.stationName, function (data) {
                                            storageid = inputvalue;
                                            console.log("残品开始收货");
                                            finishGoods();
                                        }, function (data) {//扫描非正品货筐错误
                                            if (data.key === '-1' || data.key === '-2') {
                                                showStorageFull(data.values[0]);
                                            } else {
                                                var options = {
                                                    title:INBOUND_CONSTANT.RESCANCONTAINER,
                                                    width:600,
                                                    height:400,
                                                    open:function () {
                                                        $scope.tipvalue = '';
                                                        $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                                        setTimeout(function () {
                                                            $("#newtipwindow_inputer").focus();
                                                        },500);
                                                    },
                                                    close:function () {
                                                        focusOnReceiveInputer();
                                                    }
                                                };
                                                receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                                            }
                                        });
                                    }
                                    else {
                                        if($scope.isNotIntro){
                                            showNoIntroStorage();
                                        }
                                        receivingService.checkStowBin(inputvalue, itemid, podid, ciper, $rootScope.stationName, function (data) {
                                            storageid = inputvalue;
                                            scan_bin = true;
                                            receiving_commonService.backPodStorageColor(storages);
                                            finishGoods();
                                        }, function (data) {
                                            storageid = "";
                                            if (data.key === '-5') {
                                                var options = {
                                                    title:INBOUND_CONSTANT.RESCANLOCATION,
                                                    width:600,
                                                    height:400,
                                                    open:function () {
                                                        $scope.tipvalue = '';
                                                        $("#newtipwindow_span").html("<h3>" + data.message.key + "</h3><br>" + data.message.message);
                                                        setTimeout(function () {
                                                            $("#newtipwindow_inputer").focus();
                                                        },500);
                                                    },
                                                    close:function () {
                                                        setTimeout(function () {
                                                            $("#receiving-inputer").focus();
                                                        },500);
                                                    }
                                                };
                                                receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                                            } else {
                                                if (data.key === '-2') {
                                                    $("#newtipwindow_span").html(data.values[0]);
                                                }else if(data.key === '-1'){
                                                    $("#newtipwindow_span").html(data.values[0]);
                                                }else if(data.key === '-3'){
                                                    $("#newtipwindow_span").html(data.values[0]);
                                                }else {
                                                    if(data.key.indexOf("%")!=-1){
                                                        $("#newtipwindow_span").html("<h3 style='text-align: center;font-size: 20px;position: absolute;top: 0;left: 42%;'>"+data.values[0]+"</h3></br><p style='font-size: 16px;margin-top: 5%;text-align: center'>"+data.key.substr(data.key.indexOf("&")+1,data.key.length-1)+"</p>");
                                                    }else{
                                                        //$("#newtipwindow_span").html(data.values[0]);
                                                        $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                                    }
                                                }
                                                var options = {
                                                    title: INBOUND_CONSTANT.RESCANLOCATION,
                                                    open: function () {
                                                        $scope.tipvalue = '';
                                                        setTimeout(function () {
                                                            $("#newtipwindow_inputer").focus();
                                                        },500);
                                                    },
                                                    close: function () {
                                                        focusOnReceiveInputer();
                                                    }
                                                };
                                                receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                            }
                                        });
                                    }
                                }

                            }

                        });
                    }
                }
            }
        };


       /* $scope.receivingscan = function (e) {
            if (!receiving_commonService.autoAddEvent(e)) return;
            var inputvalue = $("#receiving-inputer").val().trim()||$scope.tipvalue;
            if(inputvalue===null||inputvalue===''||inputvalue===undefined)return;
            console.log("inputvalue-->" + inputvalue);
            $("#receiving-inputer").val("");
            $scope.tipvalue = '';
            receiving_commonService.CloseWindowByBtn("newtipwindowwithinputer");
            if (!scan_DAMAGED) {//绑残品框
                receivingService.scanStowContainer(inputvalue, INBOUND_CONSTANT.DAMAGED, $rootScope.stationName, 1, function (data) {
                    if (data.status === '2') {//有商品,提示用户
                        $scope.scancontainerType = INBOUND_CONSTANT.DAMAGED;
                        storageid = inputvalue;
                        var options = {
                            width: 600,
                            height: 500,
                            title: INBOUND_CONSTANT.SUREUSELOCATION,
                            open: function () {
                                $scope.wimgstatus = 'hidden';
                                $("#win_content").html("当前货框:" + data.cls.storagelocationName + ",里面有" + data.cls.amount + "件商品，请重新确认是否继续使用当前货筐进行收货");
                            },
                            close: function () {
                                focusOnReceiveInputer();
                            }
                        };
                        receiving_commonService.receiving_tip_dialog("window_img_ok_cancel", options);
                    } else {
                        if (isDamaged&&!normalFull) {
                            finishGoods();
                        }
                        $rootScope.demagedPositionIndex = 1;
                        $scope.scanbadcib = '1';
                        scan_DAMAGED = true;
                        checkContainerIsScan();
                    }
                }, function (data) {
                    var options = {
                        title:INBOUND_CONSTANT.RESCANDAMAGED,
                        width:600,
                        height:400,
                        open:function () {
                            $scope.tipvalue = '';
                            $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("Unknown Error",""));
                            setTimeout(function () {
                                $("#newtipwindow_inputer").focus();
                            },500);
                        },
                        close:function () {
                            focusOnReceiveInputer();
                        }
                    };
                    receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                });
            } else {
                if (!scan_pod) {
                    //showGeneralWindow("提示","Pod尚未就绪,请稍后操作／刷新Pod获取最新Pod信息");
                    if(inputvalue!==''||inputvalue!==null||inputvalue==undefined){
                        $scope.getPodInfo(inputvalue);
                    }
                } else {
                    console.log("isDamaged--->"+isDamaged);
                    if (!isDamaged) {//isDamaged = true为残品
                        //自动识别是否是车牌
                        if (receiving_commonService.isCiper(inputvalue)) {
                            console.log("监测到车牌--->"+inputvalue);
                            scan_ciper = false;
                            ciperFinish = false;
                            $scope.ciperAmount = ' ';
                            $scope.remainder = '';
                            $scope.ciperTotalAmount = '';
                            scan_product_info = false;//扫描商品
                            scan_bin = false;
                            isDamaged = false;
                            isSingle = false;//单一收获？
                            $scope.scanbadcib = '1';
                            if (isAll) {
                                finishType = INBOUND_CONSTANT.ALL;
                            }
                            receiveType = INBOUND_CONSTANT.GENUINE;
                            ciper = "";
                            storageid = "";
                            amount = "";
                            itemid = "";
                            item = null;
                            $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                            $("#" + upid).children("span").text("");
                            upid = '';
                            $("#receiving_status_span").html("");
                            $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                            checkContainerIsScan();
                            receiving_commonService.backPodStorageColor(storages);
                        }
                    }
                    if (!scan_ciper) {
                        console.log("ScanningDN...");
                        receivingService.scanStowCiper(inputvalue, receiveType, $rootScope.stationName, function (data) {
                            ciper = inputvalue;
                            console.log("stowciper--->",data);
                            scan_ciper = true;
                            ciperFinish = false;
                            isExit = false;
                            $scope.finishState = '停止上架';
                            ciper = data.cls.storageLocation.name;
                            maxAmount = data.cls.amount;
                            $scope.ciperTotalAmount = maxAmount;
                            $scope.remainder = maxAmount;
                            $scope.product_info_con = '1';
                            $("#receiving_dn_span").html(ciper);
                            $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                            $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                            $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                            $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                            $("#receiving_status_span").html("");
                            $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                            checkContainerIsScan();
                        }, function (data) {
                            // ciper = null;
                            scan_ciper = false;
                            if(data.key==='-1'){
                                isExit = false;
                                $scope.finishState = '停止上架';
                                ciper = inputvalue;
                                $("#receiving_dn_span").html(inputvalue);
                                $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                $scope.ciperTotalAmount = '';
                                $scope.remainder = '';
                                $("#receiving_tip").html(INBOUND_CONSTANT.RESCANCIPERORSTOPUP);
                                var options = {
                                    title:INBOUND_CONSTANT.RESCANCIPERORSTOPUP,
                                    width:600,
                                    height:400,
                                    open:function () {
                                        $scope.tipvalue = '';
                                        $("#newtipwindow_span").html(data.values[0]);
                                        setTimeout(function () {
                                            $("#newtipwindow_inputer").focus();
                                        },200);
                                    },
                                    close:function () {
                                        focusOnReceiveInputer();
                                    }
                                };
                                receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);

                            }else{
                                $("#receiving_dn_span").html(INBOUND_CONSTANT.RESCANCIPER);
                                $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                                $scope.ciperTotalAmount = '';
                                $scope.remainder = '';
                                $("#receiving_tip").html(INBOUND_CONSTANT.RESCANCIPER);
                                var options = {
                                    title:INBOUND_CONSTANT.RESCANCIPER,
                                    width:600,
                                    height:400,
                                    open:function () {
                                        $scope.tipvalue = '';
                                        $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                        setTimeout(function () {
                                            $("#newtipwindow_inputer").focus();
                                        },500);
                                    },
                                    close:function () {
                                        focusOnReceiveInputer();
                                    }
                                };
                                receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                            }
                        });
                    }
                    else {
                        if (!scan_product_info) {
                            itemid = inputvalue;
                            receiving_commonService.backPodStorageColor(storages);
                            receivingService.scanStowItem(itemid, ciper,podid, function (data) {
                                if(data.cls.introStorages===undefined||data.cls.introStorages===null||data.cls.introStorages===''||data.cls.introStorages.length==0){
                                    showNoIntroStorage();
                                }else{
                                    console.log("data--->", data);
                                    $("#receiving_status_span").html("");
                                    scan_product_info = true;
                                    console.log("data--->",data);
                                    item = data.cls.itemData;
                                    itemid = item.itemNo;
                                    introStorages = data.cls.introStorages;
                                    maxAmount = data.transValue;
                                    $scope.product_info_con = '0';
                                    $("#product_info_title").html("SKU:" + item.skuNo);
                                    $("#product_info_text").html("商品名称:" + item.name);
                                    setTimeout(function () {
                                        $("#receiving-inputer").focus();
                                    }, 200);
                                    cleanStatus();
                                    checkContainerIsScan();
                                    receiving_commonService.colorIntroStorage(introStorages,storages);
                                }
                            }, function (data) {
                                item = data.values[3];
                                console.log("data--->",data);
                                cleanStatus();
                                checkContainerIsScan();
                                if (data.key === '-6') {//多货
                                    item = data.values[3];
                                    goodsMoreItemid = data.values[3].id;
                                    $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                    var options = {
                                        title: INBOUND_CONSTANT.SUREGOODSMORE,
                                        open: function () {
                                            $("#win_goodsmore_content").html(data.values[0]+"<br>SKU:"+data.values[1]+"<br>商品名称:"+data.values[2]);
                                        },
                                        close: function () {
                                            focusOnReceiveInputer();
                                        }
                                    };
                                    receiving_commonService.receiving_tip_dialog("window_goodsmore_ok_cancel", options);
                                } else {
                                    $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                    var options = {
                                        title:INBOUND_CONSTANT.RESCANITEM,
                                        width:600,
                                        height:400,
                                        open:function () {
                                            $scope.tipvalue = '';
                                            $("#newtipwindow_span").html(data.message.replace("Unknown Error",""));
                                            setTimeout(function () {
                                                $("#newtipwindow_inputer").focus();
                                            },500);
                                        },
                                        close:function () {
                                            focusOnReceiveInputer();
                                        }
                                    };
                                    receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                                }
                            });
                        }
                        else {
                            //检查上架货位
                            if (isDamaged) {
                                console.log("残品检查货筐");
                                receivingService.checkStowContainer(inputvalue, itemid, ciper, $rootScope.stationName, function (data) {
                                    storageid = inputvalue;
                                    console.log("残品开始收货");
                                    finishGoods();
                                }, function (data) {//扫描非正品货筐错误
                                    if (data.key === '-1' || data.key === '-2') {
                                        showStorageFull(data.values[0]);
                                    } else {
                                        var options = {
                                            title:INBOUND_CONSTANT.RESCANCONTAINER,
                                            width:600,
                                            height:400,
                                            open:function () {
                                                $scope.tipvalue = '';
                                                $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                                setTimeout(function () {
                                                    $("#newtipwindow_inputer").focus();
                                                },500);
                                            },
                                            close:function () {
                                                focusOnReceiveInputer();
                                            }
                                        };
                                        receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                                    }
                                });
                            }
                            else {
                                if($scope.isNotIntro){
                                    showNoIntroStorage();
                                }
                                receivingService.checkStowBin(inputvalue, itemid, podid, ciper, $rootScope.stationName, function (data) {
                                    storageid = inputvalue;
                                    scan_bin = true;
                                    receiving_commonService.backPodStorageColor(storages);
                                    finishGoods();
                                }, function (data) {
                                    storageid = "";
                                    if (data.key === '-5') {
                                        var options = {
                                            title:INBOUND_CONSTANT.RESCANLOCATION,
                                            width:600,
                                            height:400,
                                            open:function () {
                                                $scope.tipvalue = '';
                                                $("#newtipwindow_span").html("<h3>" + data.message.key + "</h3><br>" + data.message.message);
                                                setTimeout(function () {
                                                    $("#newtipwindow_inputer").focus();
                                                },500);
                                            },
                                            close:function () {
                                                setTimeout(function () {
                                                    $("#receiving-inputer").focus();
                                                },500);
                                            }
                                        };
                                        receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer",options);
                                    } else {
                                        if (data.key === '-2') {
                                            $("#newtipwindow_span").html(data.values[0]);
                                        } else {
                                            if(data.key.indexOf("%")!=-1){
                                                $("#newtipwindow_span").html("<h3 style='text-align: center;font-size: 20px;position: absolute;top: 0;left: 42%;'>"+data.values[0]+"</h3></br><p style='font-size: 16px;margin-top: 5%;text-align: center'>"+data.key.substr(data.key.indexOf("&")+1,data.key.length-1)+"</p>");
                                            }else{
                                                $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                            }
                                        }
                                        var options = {
                                            title: INBOUND_CONSTANT.RESCANLOCATION,
                                            open: function () {
                                                $scope.tipvalue = '';
                                                setTimeout(function () {
                                                    $("#newtipwindow_inputer").focus();
                                                },500);
                                            },
                                            close: function () {
                                                focusOnReceiveInputer();
                                            }
                                        };
                                        receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        };
*/

        //显示无推荐货位弹窗
        function showNoIntroStorage() {
            receiving_commonService.receiving_tip_dialog("releasePodWindow",{
                title:"无推荐货位",
                width:600,
                height:500,
                open:function () {
                    $scope.sureDnTip = INBOUND_CONSTANT.NOINTROSTORAGES;
                    $scope.sureDn = '';
                    setTimeout(function () {
                        $("#sureDn").focus();
                    });
                },
                close:function () {
                    focusOnReceiveInputer();
                }
            });
        }

        $scope.scanDnSureItem = function (e) {
            if(!receiving_commonService.autoAddEvent(e)) return;
            receivingService.checkDNProblem(itemid,$scope.sureDn,function () {
                receiving_commonService.CloseWindowByBtn('releasePodWindow');
                itemid = '';
                introStorages = [];
                scan_product_info = false;
                $scope.product_info_con = '1';
                $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                checkContainerIsScan();
            },function () {
                $scope.sureDnTip = 'DN扫描错误,请重新扫描DN';
            });
        };
        //显示货位类型
        $scope.showBinTypeWindow = function () {
            var options = {
                title: INBOUND_CONSTANT.storagelocationtype,
                width: 850,
                height: 550,
                open: function () {
                    $("#show_bin_grid").html("");
                    if($rootScope.stowstoporcallpod){
                        $("#receiving-stopassignpod").css({"backgroundColor":"#FF0000"});
                        $scope.assignpodinfo = INBOUND_CONSTANT.stopassignpod;
                    }else{
                        $("#receiving-stopassignpod").css({"backgroundColor":"#B3EE3A"});
                        $scope.assignpodinfo = INBOUND_CONSTANT.reassignpod;
                    }
                    receivingService.getPodInPath($rootScope.workStationId,$rootScope.sectionId,function (data) {
                        if(data.status==='0') {
                            $scope.podTotal = data.cls.length;
                        }
                        else {
                            $scope.podTotal = 0;
                        }
                    });
                    receivingService.getStowSelectedStorageType($rootScope.stationName, function (data) {
                        receiving_commonService.grid_BayTypeInPage(data.cls.allBinType, data.cls.selectedBinType, 2);
                    });
                },
                close:function () {
                    focusOnReceiveInputer();
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("showBinType_window", options);
        };
        $scope.stopAssignPod = function () {
            if($scope.assignpodinfo===INBOUND_CONSTANT.stopassignpod){
                receivingService.stowstoporcallpod($rootScope.stationName,false,function (data) {
                    $("#receiving-stopassignpod").css({"backgroundColor":"#B3EE3A"});
                    $scope.assignpodinfo = INBOUND_CONSTANT.reassignpod;
                    $rootScope.stowstoporcallpod = false;
                });
            }else{
                receivingService.stowstoporcallpod($rootScope.stationName,true,function (data) {
                    $("#receiving-stopassignpod").css({"backgroundColor":"#FF0000"});
                    $scope.assignpodinfo = INBOUND_CONSTANT.stopassignpod;
                    $rootScope.stowstoporcallpod = true;
                });
            }
        };
        //显示货筐已满窗口
        function showStorageFull(msg) {
            var options = {
                title: INBOUND_CONSTANT.EXCHANGESTORAGE,
                open: function () {
                    $("#ok_tipwindow_span").html(msg + "," + INBOUND_CONSTANT.CLICKSTORAGEFULL);
                }
            };
            receiving_commonService.receiving_tip_dialog("ok_tipwindow", options);
        }

        //显示更换货筐窗口
        $scope.showChangeStoage = function (data) {
            receiving_commonService.CloseWindowByBtn("promenu_pop_window");
            receiving_commonService.CloseWindowByBtn("ok_tipwindow");
            normalFull = data;
            var options = {
                title: INBOUND_CONSTANT.SCANFULLSTORAGE,
                width: 1000,
                height: 600,
                open: function () {
                    gridStorageInfo();
                    $("#inputstoragewindow_span").html("请扫描已满货筐条码");
                    $("#window-storage-inputer").val("");
                    setTimeout(function () {
                        $("#window-storage-inputer").focus();
                    },500);
                },
                close:function () {
                    if(scan_DAMAGED&&normalFull){
                        normalFull = false;
                        isOld = true;
                        return;
                    }
                    if(!scan_DAMAGED){
                        isOld = true;
                        checkContainerIsScan();
                    }
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("scanstoragewindow", options);
        };
        //站台货筐信息
        function gridStorageInfo() {
            $("#storagewindowtip").html(INBOUND_CONSTANT.CURRENTSTATIONLOADING);
            $scope.singleReceivedGridOptions = {height: 200, columns: columns};
            receivingService.findstowgridStorageInfo($rootScope.stationName, function (data) {
                $("#storagewindowtip").html(INBOUND_CONSTANT.CURRENTSTATION);
                console.log("data-->", data);
                $rootScope.receiveProcessDTOList = data.cls.receiveProcessDTOList;
                $scope.scanstatus = '0';
                $("#singleReceivedGRID").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: data.cls.receiveProcessDTOList}));
            }, function (data) {
                $scope.scanstatus = '1';
                $("#warnStation").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
            });
        }
        //现实通用提示窗口
        function showGeneralWindow(title, msg) {
            var options = {
                title: title,
                open: function () {
                    $("#tipwindow_span").html(msg);
                },
                close: function () {
                    focusOnReceiveInputer();
                }
            };
            receiving_commonService.receiving_tip_dialog("tipwindow", options);
        }
        //切换收货模式
        $scope.switchMode = function (modeData) {
            isAll = modeData;
            if (modeData) {//bu是单件
                finishType = INBOUND_CONSTANT.ALL;
                $("#receiving-singlemode").css({"backgroundColor": "#6E6E6E"});
                $("#receiving-allmode").css({"backgroundColor": "#3f51b5"});
            } else {//是单件
                finishType = INBOUND_CONSTANT.SINGLE;
                $("#receiving-allmode").css({"backgroundColor": "#6E6E6E"});
                $("#receiving-singlemode").css({"backgroundColor": "#3f51b5"});
            }
            focusOnReceiveInputer();
        };
        //单件收货
        function SingleFinishGoods() {
            receivingService.finishStow({
                receiveStationId: $rootScope.stationName,
                itemid: itemid,
                storageId: storageid,
                amount: 1,
                ciperName: ciper,
                stowType: receiveType,
                finishType: finishType,
                podid: podid,
                stationName:$rootScope.stationName,
                toolName: INBOUND_CONSTANT.STOW
            }, function (data) {
                $scope.remainder = parseInt($scope.remainder)-1;
                    if (isDamaged) {
                        console.log("特殊商品");
                        if (isDamaged) {
                            $("#scanbadcib").html("1");
                            $("#scanbadcib").css({"backgroundColor": "#008B00"});
                            isDamaged = false;
                            $("#receiving_status_span").css({"backgroundColor": "#FF0000"});
                        }
                        // $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                    } else {
                        upid = receiving_commonService.findStorageLocation(storageid, storages);
                        $("#" + upid).css({"backgroundColor": "#008B00"});
                        $("#" + upid).children("span").text(1);
                        $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                    }
                    $("#receiving_status_span").html("已成功上架<span style='font-size: 36px;font-weight: bold'>1</span>件商品至</br>" + storageid);
                    if(parseInt(data.cls)<=0){
                        $scope.remainder = '0';
                        $scope.ciperAmount = '0';
                        $scope.ciperTotalAmount = '0';

                        receiving_commonService.receiving_tip_dialog("goods_general_ok_cancel", {
                            title: INBOUND_CONSTANT.SURECIPERCLOSE,
                            width: 600,
                            height: 500,
                            open: function () {
                                $("#goods_content").html("此承载单元中剩余0件商品，是否关闭？");
                            },
                            close:function () {
                                focusOnReceiveInputer();
                            }
                        });
                    }
                    reSetAllVar();
                    receiving_commonService.closePopWindow("keyboard_window");
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                focusOnReceiveInputer();
            }, function (data) {
                if (data.key === '-6') {
                    receiving_commonService.receiving_tip_dialog("window_stow_goods_more_ok_cancel", {
                        title: INBOUND_CONSTANT.SUREGOODSMORE,
                        width: 600,
                        height: 500,
                        open: function () {
                            $scope.ciperAmount = data.values[1];
                            $scope.inputAmount = data.values[2];
                            $scope.goodsMoreAmount = data.values[3];
                        }
                    });
                } else {
                    if (isDamaged) {
                        if (data.key === '-1' || data.key === '-2') {
                            showStorageFull(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                        } else {
                            showGeneralWindow("扫描货筐错误", data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                        }
                    } else {
                        showGeneralWindow(INBOUND_CONSTANT.RESCANLOCATION, data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                    }
                }
            });
        }

        //重置相关状态位
        function reSetAllVar() {
            isSingle = false;
            isDamaged = false;
            isGoodsMore = false;
            isMore = false;
            receiveType = INBOUND_CONSTANT.GENUINE;
            scan_product_info = false;
            maxAmount = null;
            itemid = "";
            useNotAfter='';
            item = null;
            $scope.isSureGoodsMore = false;
        }

        function finishGoods() {
            if (isSingle) {
                //检查用户收货数量是否符合
                console.log("单件开始收货");
                console.log("maxAmount--->" + maxAmount);
                SingleFinishGoods();
            } else {
                if (isAll || isDamaged) {
                    var ti = null;
                    if(isDamaged){
                        ti = "请输入残品数量";
                    }else{
                        ti = "请输入上架数量";
                    }

                    upid = receiving_commonService.findStorageLocation(storageid, storages);
                    $("#" + upid).css({"backgroundColor": "#008B00"});

                    receiving_commonService.receiving_tip_dialog_normal("keyboard_window", {
                        width: 600,
                        title: ti,
                        open: function () {
                            $("#keyboard_inputer").val("");
                            receiving_commonService.keyboard_fillGrid($("#keyboard_keys"), 2, 5, "keyboard", "keyboard_layout_item",function () {
                                setTimeout(function () {
                                    $("#keyboard_inputer").focus();
                                },500);
                            });
                        },
                        close: function () {
                            $("#keyboard_inputer").value = "";
                            focusOnReceiveInputer();
                        }
                    });
                } else {
                    console.log("单件开始收货");
                    SingleFinishGoods();
                }
            }
        };

        //有效期输入框焦点函数
        $scope.avatimemethod = function (currentid) {
            currentId = currentid;
            receiving_commonService.getavatimeid(currentid);

        };

        $scope.sureGoodsMore = function () {
            isGoodsMore = true;
            isMore = true;
            amount = $scope.ciperAmount;
            console.log("amount-->"+amount+"/ciperamount-->"+$scope.ciperAmount);
            receivingService.finishStow({
                receiveStationId: $rootScope.stationName,
                itemid: itemid,
                storageId: storageid,
                amount: amount,
                ciperName: ciper,
                stowType: receiveType,
                finishType: finishType,
                podid: podid,
                stationName:$rootScope.stationName,
                toolName: INBOUND_CONSTANT.STOW
            }, function (outerdata) {
                $scope.remainder = parseInt($scope.remainder)-amount;
                receiving_commonService.closePopWindow("keyboard_window");
                receivingService.checkGoodsMore(ciper,item.id,function (data) {
                    console.log("检查多货返回-->",data);
                    if(data===undefined||data===null||parseInt(data)===0){
                        receivingService.reportGoodsMore({
                            "amount": $scope.goodsMoreAmount,
                            "problemType": "MORE",
                            "reportBy": $window.localStorage["username"],
                            "problemStorageLocation": ciper,
                            "jobType": "STOW OVERAGE",
                            "itemNo": item.itemNo,
                            "skuNo":item.skuNo,
                            "itemDataId": item.id,
                            "state": "OPEN",
                            "warehouseId": $window.localStorage["warehouseId"]
                        }, function (data) {
                                isGoodsMore = false;
                                $scope.product_info_con = '0';
                                $("#product_info_title").html("SKU:" + item.skuNo);
                                $("#product_info_date").html("有效期:" + useNotAfter);
                                $("#product_info_text").html("商品名称:" + item.name);
                                if (isDamaged) {
                                    console.log("特殊商品");
                                    $("#scanbadcib").html(amount);
                                    $("#scanbadcib").css({"backgroundColor": "#008B00"});
                                    isDamaged = false;
                                    $("#receiving_status_span").css({"backgroundColor": "#FF0000"});
                                    $("#receiving_status_span").html("已成功上架"+amount+"件残品至"+storageid+"</br>登记"+$scope.goodsMoreAmount+"件多货至</br>" + ciper);
                                    // $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                                } else {
                                    upid = receiving_commonService.findStorageLocation(storageid, storages);
                                    $("#" + upid).css({"backgroundColor": "#008B00"});
                                    $("#" + upid).children("span").text(amount);
                                    $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                                    $("#receiving_status_span").html("已成功上架<span style='font-size: 36px;font-weight: bold'>"+amount+"</span>件商品至"+storageid+"</br>登记<span style='font-size: 36px;font-weight: bold'>"+$scope.goodsMoreAmount+"</span>件多货至</br>" + ciper);
                                }
                                if(parseInt(outerdata.cls)<=0){
                                    $scope.remainder = '0';
                                    $scope.ciperAmount = '0';
                                    $scope.ciperTotalAmount = '0';

                                }
                                reSetAllVar();
                                receiving_commonService.closePopWindow("keyboard_window");
                                $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                                focusOnReceiveInputer();
                        },function (data) {
                            isGoodsMore = false;
                            $scope.product_info_con = '0';
                            $("#product_info_title").html("SKU:" + item.skuNo);
                            $("#product_info_date").html("有效期:" + useNotAfter);
                            $("#product_info_text").html("商品名称:" + item.name);
                            if (isDamaged) {
                                console.log("特殊商品");
                                $("#scanbadcib").html(amount);
                                $("#scanbadcib").css({"backgroundColor": "#008B00"});
                                isDamaged = false;
                                $("#receiving_status_span").css({"backgroundColor": "#FF0000"});
                                $("#receiving_status_span").html("已成功上架"+amount+"件残品至"+storageid+"</br>登记多货失败</br>");
                                // $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                            } else {
                                upid = receiving_commonService.findStorageLocation(storageid, storages);
                                $("#" + upid).css({"backgroundColor": "#008B00"});
                                $("#" + upid).children("span").text(amount);
                                $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                                $("#receiving_status_span").html("已成功上架<span style='font-size: 36px;font-weight: bold'>"+amount+"</span>件商品至"+storageid+"</br>登记多货失败</br>");
                            }
                            if(parseInt(outerdata.cls)<=0){
                                $scope.remainder = '0';
                                $scope.ciperAmount = '0';
                                $scope.ciperTotalAmount = '0';
                            }
                            reSetAllVar();
                            receiving_commonService.closePopWindow("keyboard_window");
                            $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                        });
                    }else{
                        receiving_commonService.CloseWindowByBtn("window_stow_goods_more_ok_cancel");
                        var options = {
                            title: "已标记过该商品",
                            width: 800,
                            height: 600,
                            open: function () {
                                $("#win_goodsmore_more_content").html("该商品:"+itemid+"在该容器中已被标记过"+data+"次,是否要再次标记？");
                            },
                            close:function () {
                                focusOnReceiveInputer();
                            }
                        };
                        receiving_commonService.receiving_tip_dialog_normal("window_goodsmore_more_ok_cancel", options);
                    }
                },function (data) {
                    isGoodsMore = false;
                    $scope.product_info_con = '0';
                    $("#product_info_title").html("SKU:" + item.skuNo);
                    $("#product_info_date").html("有效期:" + useNotAfter);
                    $("#product_info_text").html("商品名称:" + item.name);
                    if (isDamaged) {
                        console.log("特殊商品");
                        $("#scanbadcib").html(amount);
                        $("#scanbadcib").css({"backgroundColor": "#008B00"});
                        isDamaged = false;
                        $("#receiving_status_span").css({"backgroundColor": "#FF0000"});
                        $("#receiving_status_span").html("已成功上架"+amount+"件残品至"+storageid+"</br>检查多货失败</br>");
                        // $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                    } else {
                        upid = receiving_commonService.findStorageLocation(storageid, storages);
                        $("#" + upid).css({"backgroundColor": "#008B00"});
                        $("#" + upid).children("span").text(amount);
                        $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                        $("#receiving_status_span").html("已成功上架"+amount+"件残品至"+storageid+"</br>检查多货失败</br>");
                    }
                    if(parseInt(outerdata.cls)<=0){
                        $scope.remainder = '0';
                        $scope.ciperAmount = '0';
                        $scope.ciperTotalAmount = '0';
                    }
                    reSetAllVar();
                    receiving_commonService.closePopWindow("keyboard_window");
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                });
            }, function (data) {
                if (data.key === '-6') {
                    receiving_commonService.receiving_tip_dialog("window_stow_goods_more_ok_cancel", {
                        title: INBOUND_CONSTANT.SUREGOODSMORE,
                        width: 600,
                        height: 500,
                        open: function () {
                            $scope.ciperAmount = data.values[1];
                            $scope.inputAmount = data.values[2];
                            $scope.goodsMoreAmount = data.values[3];
                        }
                    });
                } else {
                    if (isDamaged) {
                        if (data.key === '-1' || data.key === '-2') {
                            showStorageFull(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                        } else {
                            showGeneralWindow("扫描货筐错误", data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                        }
                    } else {
                        showGeneralWindow(INBOUND_CONSTANT.RESCANLOCATION, data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                    }
                }
            });
            receiving_commonService.CloseWindowByBtn("window_stow_goods_more_ok_cancel");
        }
        //确认使用当前货筐
        $scope.win_receivingok = function (type) {

            receivingService.bindStowContainer(storageid, $rootScope.stationName, type, function (data) {
                $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                $("#" + upid).children("span").text("");
                isOld = true;
                scan_DAMAGED = true;
                if (type.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()) {
                    checkContainerIsScan();
                    $scope.scanbadcib = '1';
                }
                if (isDamaged&&!normalFull) {
                    finishGoods();
                }
                $rootScope.demagedPositionIndex = 1;
                normalFull = false;
                checkContainerIsScan();
                $scope.scancontainerType = null;
                positionIndex = null;
                receiving_commonService.CloseWindowByBtn('scanstoragewindow');
                receiving_commonService.CloseWindowByBtn("window_img_ok_cancel");
            });
        };
        //不使用当前货筐
        $scope.win_receivingcancel = function (type) {
            if (type.toLowerCase() === INBOUND_CONSTANT.DAMAGED.toLowerCase()) {
                scan_DAMAGED = false;
                $('#receiving_tip').html(INBOUND_CONSTANT.SCANDAMAGED);
            }
            receiving_commonService.CloseWindowByBtn("window_img_ok_cancel");
        };
        $scope.autoClose = function (e) {
            if (!receiving_commonService.autoAddEvent(e)) return;
            var window = $("#tipwindow").data("kendoWindow");
            window.close();
        };
        //显示问题菜单弹窗
        $scope.showProMenuWindow = function () {
            var options = {
                title: INBOUND_CONSTANT.SELECTPMENU,
                width: 800,
                height: 600,
                close: function () {
                    setTimeout(function () {
                        $("#receiving-inputer").focus();
                    }, 200);
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("promenu_pop_window", options);
        };
        //结束收货弹窗
        $scope.finishReceiveWindow = function () {
            console.log("isExit--->",isExit);
            if(isExit){
                receivingService.getPodInPath($rootScope.workStationId,$rootScope.sectionId,function (data) {
                    if(data.status==='0'){
                        var podInfo = '';
                        for(var i=0;i<data.cls.length;i++){
                            podInfo+=data.cls[i]+',';
                        }
                        showGeneralWindow("提示","当前工作站尚有在途Pod,请等待全部到站并释放完毕再进行退出.</br>"+podInfo);
                    }else{
                        var options = {
                            title: INBOUND_CONSTANT.FINISHMENU,
                            width: 600,
                            height: 400,
                            open: function () {
                                $scope.exitflag = '0';
                                $scope.exitStationContent = INBOUND_CONSTANT.FINISHRECEIVECONTENT_STOW;
                            },
                            close: function () {
                                $scope.exitStationContent = INBOUND_CONSTANT.FINISHRECEIVECONTENT_STOW;
                            }
                        };
                        receiving_commonService.receiving_tip_dialog_normal("window_general_ok_cancel", options);
                    }
                });
            }else{
                receivingService.exitStowStationBefore($rootScope.stationName, "NO", ciper, function (data) {
                    receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                    receiving_commonService.CloseWindowByBtn("window_exitstation_ok_cancel");
                    receiving_commonService.CloseWindowByBtn("goods_general_ok_cancel");
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANCIPER);
                    $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANCIPER);
                    $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                    $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                    if(ciper===null||ciper===''||ciper===undefined){
                        $("#receiving_status_span").html("请扫描上架车牌继续上架或点击结束上架直接退出");
                    }else{
                        $("#receiving_status_span").html("成功关闭车牌<br>请扫描上架车牌继续上架或点击结束上架直接退出");
                    }
                    console.log("data--->",data);
                    ciper = null;
                    scan_ciper = false;
                    isExit = true;
                    $scope.finishState = '结束上架';
                    $scope.ciperAmount = '';
                    $scope.remainder = '';
                    $scope.ciperTotalAmount = '0';
                    checkContainerIsScan();
                }, function (data) {
                    console.log("data--->",data);
                    //有商品
                    if (data.key === '-1') {
                        var options = {
                            title: "上架结束",
                            width: 600,
                            height: 400,
                            open: function () {
                                exitThreshold = data.values[1];
                                ciperCount = data.values[0];
                                damageAmount = data.values[2];
                                goodsMoreAmount = data.values[3];
                                stockUnits = data.values[4];
                                $scope.exitstationflag = '0';
                                $scope.exitStationContentNumTip = '已扫描车牌' + ciper + "中尚有" + data.values[0] + "件商品,请确认操作";
                            },
                            close: function () {
                                $scope.exitStationContentNumTip = '';
                            }
                        };
                        receiving_commonService.receiving_tip_dialog("window_exitstation_ok_cancel", options);
                    } else {
                        showGeneralWindow("结束收货失败", data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                    }
                });
            }
        };
        //改变退出工作站提示
        $scope.exitStationBefore = function () {
            $scope.exitflag = '1';
            $scope.exitStationContent = INBOUND_CONSTANT.SUREEXITANDFULL;
        }
        $scope.exitBefore = function () {
            // isAllFullStorage = "YES";
            console.log();
            if($scope.exitType==='YES'&&(ciperCount>=exitThreshold)){
                $scope.exitStationContentNumTip = '车牌剩余数据'+ciperCount+"件,大于等于设置数据"+exitThreshold+",无法强制退出\n点击确认,将问题车牌交至问题处理区,由问题组人员进行处理\n点击取消,结束当前操作";
                $scope.exitstationflag = '1';
            }else {
                receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                receiving_commonService.CloseWindowByBtn("window_exitstation_ok_cancel");
                if($scope.exitType==='YES'){
                    receivingService.reportGoodsLess(ciper,function (datass) {
                        /*receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                        receiving_commonService.CloseWindowByBtn("window_exitstation_ok_cancel");*/
                        receivingService.exitStowStation($rootScope.stationName, $scope.exitType, ciper, function (data) {
                            console.log("exitstation.data--->", data);
                            $("#receiving_tip").html(INBOUND_CONSTANT.SCANCIPER);
                            $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANCIPER);
                            $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                            $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                            if (data.cls.state === null || data.cls.state === '' || data.cls.state === undefined||data.cls.state==='-1') {
                                if($scope.exitType==='NO'){
                                    $("#receiving_status_span").html("成功选择中途休息模式<br>休息完成后请扫描上架车牌继续上架或点击结束上架直接退出");
                                }else{
                                    $("#receiving_status_span").html("请扫描上架车牌继续上架或点击结束上架直接退出");
                                }
                            } else {
                                console.log("$scope.exitType--->"+$scope.exitType+"/$scope.exitType.TYPE--->"+typeof $scope.exitType);
                                if($scope.exitType==='NO'){
                                    $("#receiving_status_span").html("成功选择中途休息模式<br>休息完成后请扫描上架车牌继续上架或点击结束上架直接退出");
                                }else{
                                    $("#receiving_status_span").html("成功选择强制退出模式<br>此车牌共登记" + data.cls.damageAmount + "件残品," + data.cls.goodsMoreAmount + "件多货,"+parseInt(data.cls.goodsLessAmount)+ "件少货,请将以上商品连同问题车辆交至问题处理区<br>请扫描上架车牌继续上架或点击结束上架直接退出");
                                }
                            }
                            $scope.ciperAmount = '';
                            $scope.remainder = '';
                            $scope.ciperTotalAmount = '';
                            ciper = null;
                            scan_ciper = false;
                            isExit = true;
                            $scope.finishState = '结束上架';
                            checkContainerIsScan();
                        }, function (data) {
                            //有商品
                            if (data.key === '-1') {
                                var options = {
                                    title: "上架结束",
                                    width: 600,
                                    height: 400,
                                    open: function () {
                                        exitThreshold = data.values[1];
                                        ciperCount = data.values[0];
                                        damageAmount = data.values[2];
                                        goodsMoreAmount = data.values[3];
                                        $scope.exitstationflag = '0';
                                        $scope.exitStationContentNumTip = '已扫描车牌' + ciper + "中尚有" + data.values[0] + "件商品,请确认操作";
                                    },
                                    close: function () {
                                        $scope.exitStationContentNumTip = '';
                                    }
                                };
                                receiving_commonService.receiving_tip_dialog("window_exitstation_ok_cancel", options);
                            } else {
                                ciperCount = 0;
                                showGeneralWindow("结束收货失败", "登记少货成功："+datass+",停止收货失败"+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                            }
                        });
                    }, function (data) {

                        receiving_commonService.receiving_tip_dialog("goodsless_tips_cancel", {
                            title: INBOUND_CONSTANT.GOODSLESSTIPS,
                            width: 600,
                            height: 400,
                            open: function () {
                                $("#goodslesscontent").html("登记少货失败,请将此车牌中途休息后交由问题人员处理");
                            },
                            close:function () {
                                focusOnReceiveInputer();
                            }
                        });
                        /*receivingService.exitStowStation($rootScope.stationName, $scope.exitType, ciper, function (data) {
                            console.log("exitstation.data--->", data);
                            $("#receiving_tip").html(INBOUND_CONSTANT.SCANCIPER);
                            $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANCIPER);
                            $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                            $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                            if (data.cls.state === null || data.cls.state === '' || data.cls.state === undefined||data.cls.state==='-1') {
                                if($scope.exitType==='NO'){
                                    $("#receiving_status_span").html("成功选择中途休息模式<br>休息完成后请扫描上架车牌继续上架或点击结束上架直接退出");
                                }else{
                                    $("#receiving_status_span").html("请扫描上架车牌继续上架或点击结束上架直接退出");
                                }
                            } else {
                                console.log("$scope.exitType--->"+$scope.exitType+"/$scope.exitType.TYPE--->"+typeof $scope.exitType);
                                if($scope.exitType==='NO'){
                                    $("#receiving_status_span").html("成功选择中途休息模式(本次上报少货失败)<br>休息完成后请扫描上架车牌继续上架或点击结束上架直接退出");
                                }else{
                                    $("#receiving_status_span").html("成功选择强制退出模式(本次上报少货失败)<br>此车牌共登记" + data.cls.damageAmount + "件残品," + data.cls.goodsMoreAmount + "件多货,"+parseInt(data.cls.goodsLessAmount)+ "件少货,请将以上商品连同问题车辆交至问题处理区<br>请扫描上架车牌继续上架或点击结束上架直接退出");
                                }
                            }
                            $scope.ciperAmount = '';
                            $scope.remainder = '';
                            $scope.ciperTotalAmount = '';
                            ciper = null;
                            scan_ciper = false;
                            isExit = true;
                            $scope.finishState = '结束上架';
                            checkContainerIsScan();
                        }, function (data) {
                            //有商品
                            if (data.key === '-1') {
                                var options = {
                                    title: "上架结束",
                                    width: 600,
                                    height: 400,
                                    open: function () {
                                        exitThreshold = data.values[1];
                                        ciperCount = data.values[0];
                                        damageAmount = data.values[2];
                                        goodsMoreAmount = data.values[3];
                                        $scope.exitstationflag = '0';
                                        $scope.exitStationContentNumTip = '已扫描车牌' + ciper + "中尚有" + data.values[0] + "件商品,请确认操作";
                                    },
                                    close: function () {
                                        $scope.exitStationContentNumTip = '';
                                    }
                                };
                                receiving_commonService.receiving_tip_dialog("window_exitstation_ok_cancel", options);
                            } else {
                                showGeneralWindow("提示", "上报少货失败,停止收货失败:"+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                            }
                        });*/
                    });
                }else{
                    receivingService.exitStowStation($rootScope.stationName, $scope.exitType, ciper, function (data) {
                        console.log("exitstation.data--->", data);
                        /*receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                        receiving_commonService.CloseWindowByBtn("window_exitstation_ok_cancel");*/
                        $("#receiving_tip").html(INBOUND_CONSTANT.SCANCIPER);
                        $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANCIPER);
                        $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                        $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                        if (data.cls.state === null || data.cls.state === '' || data.cls.state === undefined||data.cls.state==='-1') {
                            if($scope.exitType==='NO'){
                                $("#receiving_status_span").html("成功选择中途休息模式<br>休息完成后请扫描上架车牌继续上架或点击结束上架直接退出");
                            }else{
                                $("#receiving_status_span").html("请扫描上架车牌继续上架或点击结束上架直接退出");
                            }
                        } else {
                            console.log("$scope.exitType--->"+$scope.exitType+"/$scope.exitType.TYPE--->"+typeof $scope.exitType);
                            if($scope.exitType==='NO'){
                                $("#receiving_status_span").html("成功选择中途休息模式<br>休息完成后请扫描上架车牌继续上架或点击结束上架直接退出");
                            }else{
                                $("#receiving_status_span").html("成功选择强制退出模式<br>此车牌共登记<span style='font-size: 36px;font-weight: bold'>"  + data.cls.goodsMoreAmount + "</span>件多货,<span style='font-size: 36px;font-weight: bold'>"+parseInt(data.cls.goodsLessAmount)+ "</span>件少货,请将以上商品连同问题车辆交至问题处理区<br>请扫描上架车牌继续上架或点击结束上架直接退出");
                            }
                        }
                        $scope.ciperAmount = '';
                        $scope.remainder = '';
                        $scope.ciperTotalAmount = '';
                        ciper = null;
                        scan_ciper = false;
                        isExit = true;
                        $scope.finishState = '结束上架';
                        checkContainerIsScan();
                    }, function (data) {
                        //有商品
                        if (data.key === '-1') {
                            var options = {
                                title: "上架结束",
                                width: 600,
                                height: 400,
                                open: function () {
                                    exitThreshold = data.values[1];
                                    ciperCount = data.values[0];
                                    damageAmount = data.values[2];
                                    goodsMoreAmount = data.values[3];
                                    $scope.exitstationflag = '0';
                                    $scope.exitStationContentNumTip = '已扫描车牌' + ciper + "中尚有" + data.values[0] + "件商品,请确认操作";
                                },
                                close: function () {
                                    $scope.exitStationContentNumTip = '';
                                }
                            };
                            receiving_commonService.receiving_tip_dialog("window_exitstation_ok_cancel", options);
                        } else {
                            showGeneralWindow("结束收货失败", data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                        }
                    });
                }
            }
        }
        $scope.exitCancel = function () {
            isAllFullStorage = "NO";
            $scope.exitflag = '0';
            $scope.exitstationflag = '0';
            receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
            // receiving_commonService.CloseWindowByBtn("window_exitstation_ok_cancel");gulp
        }
        var isAllFullStorage = "NO";
        //结束收货确定(结满所有筐)
        $scope.exitStation = function () {
            isAllFullStorage = "YES";
            receivingService.exitStowUI($rootScope.stationName,isAllFullStorage,function (data) {
                $state.go("main.mainMenu");
                var splitter = $("#mainSplitter").data("kendoSplitter");
                splitter && splitter.expand(".k-pane:first");
            },function (data) {
                showGeneralWindow("结束收货失败","结束收货失败");
            });
        };
        //退出工作站不满筐
        $scope.exitNotFull = function () {
            isAllFullStorage = "NO";
            console.log("退出不结满");
            receivingService.exitStowUI($rootScope.stationName,isAllFullStorage,function (data) {
                $state.go("main.mainMenu");
                var splitter = $("#mainSplitter").data("kendoSplitter");
                splitter && splitter.expand(".k-pane:first");
            },function (data) {
                showGeneralWindow("结束收货失败","结束收货失败");
            });
        };
        $scope.exit = function () {
            ciper = null;
            scan_ciper = false;
            checkContainerIsScan();
            receivingService.exitStowStationBefore($rootScope.stationName, "YES", ciper, function (data) {
                ciper = null;
                scan_ciper = false;
                isExit = true;
                $scope.finishState = '结束上架';
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANCIPER);
                $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANCIPER);
                $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                if(data.cls.state === null || data.cls.state === '' || data.cls.state === undefined||data.cls.state==='-1'){
                    $("#receiving_status_span").html("请扫描上架车牌继续上架或点击结束上架直接退出");
                }else{
                    if($scope.exitType==='NO'){
                        $("#receiving_status_span").html("成功关闭车牌<br>此车牌共登记<span style='font-size: 36px;font-weight: bold'>"  + data.cls.goodsMoreAmount + "</span>件多货,<span style='font-size: 36px;font-weight: bold'>"+parseInt(data.cls.goodsLessAmount)+ "</span>件少货,请将以上商品连同问题车辆交至问题处理区<br>请扫描上架车牌继续上架或点击结束上架直接退出");
                    }else{
                        $("#receiving_status_span").html("成功关闭车牌<br>此车牌共登记<span style='font-size: 36px;font-weight: bold'>"  + data.cls.goodsMoreAmount + "</span>件多货,<span style='font-size: 36px;font-weight: bold'>"+parseInt(data.cls.goodsLessAmount+ciperCount)+ "</span>件少货,请将以上商品连同问题车辆交至问题处理区<br>请扫描上架车牌继续上架或点击结束上架直接退出");
                    }
                }
                $scope.ciperAmount = '';
                $scope.remainder = '';
                $scope.ciperTotalAmount = '';
                receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                receiving_commonService.CloseWindowByBtn("window_exitstation_ok_cancel");
                checkContainerIsScan();
            });
        }
        //结束收获取消
        $scope.closeGeneralWindow = function () {
            var window = $("#window_general_ok_cancel").data("kendoWindow");
            window.close();
        }

        $scope.closegoodsWindow = function () {
            var window = $("#goods_general_ok_cancel").data("kendoWindow");
            window.close();
        }
        $scope.closegoodslesstipWindow = function () {
            var window = $("#goodsless_tips_cancel").data("kendoWindow");
            window.close();
        }
        //无法强制退出并且上报少货
        $scope.cantExitAndReportLessGoods = function () {
            receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
            receiving_commonService.CloseWindowByBtn("window_exitstation_ok_cancel");
            receivingService.exitStowStation($rootScope.stationName, $scope.exitType, ciper, function (data) {
                console.log("exitstation.data--->", data);
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANCIPER);
                $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANCIPER);
                $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                if (data.cls.state === null || data.cls.state === '' || data.cls.state === undefined||data.cls.state==='-1') {
                    if($scope.exitType==='NO'){
                        $("#receiving_status_span").html("成功选择中途休息模式<br>休息完成后请扫描上架车牌继续上架或点击结束上架直接退出");
                    }else{
                        $("#receiving_status_span").html("请扫描上架车牌继续上架或点击结束上架直接退出");
                    }
                } else {
                    console.log("$scope.exitType--->"+$scope.exitType+"/$scope.exitType.TYPE--->"+typeof $scope.exitType);
                    if (data.cls.state === null || data.cls.state === '' || data.cls.state === undefined || data.cls.state === '-1') {
                        $("#receiving_status_span").html("车牌剩余数据过多,无法选择强制退出模式</br>请将以上商品和车牌交至问题处理区</br>扫描车牌开始新的车牌上架或点击结束上架退出上架系统");
                    } else {
                        $("#receiving_status_span").html("车牌剩余数据过多,无法选择强制退出模式</br><br>此车牌共登记" + data.cls.damageAmount + "件残品," + data.cls.goodsMoreAmount + "件多货," + parseInt(data.cls.goodsLessAmount) + "件少货,请将以上商品连同问题车辆交至问题处理区<br>请扫描上架车牌继续上架或点击结束上架直接退出");
                    }
                }
                $scope.ciperAmount = '';
                $scope.remainder = '';
                $scope.ciperTotalAmount = '';
                ciper = null;
                scan_ciper = false;
                isExit = true;
                $scope.finishState = '结束上架';
                checkContainerIsScan();
            }, function (data) {
                //有商品
                if (data.key === '-1') {
                    var options = {
                        title: "上架结束",
                        width: 600,
                        height: 400,
                        open: function () {
                            exitThreshold = data.values[1];
                            ciperCount = data.values[0];
                            damageAmount = data.values[2];
                            goodsMoreAmount = data.values[3];
                            $scope.exitstationflag = '0';
                            $scope.exitStationContentNumTip = '已扫描车牌' + ciper + "中尚有" + data.values[0] + "件商品,请确认操作";
                        },
                        close: function () {
                            $scope.exitStationContentNumTip = '';
                        }
                    };
                    receiving_commonService.receiving_tip_dialog("window_exitstation_ok_cancel", options);
                } else {
                    ciperCount = 0;
                    showGeneralWindow("停止收货失败", ",停止收货失败"+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                }
            });
        }
        //点击报告暗灯
        var reportTypeId = '';
        var side = '';
        $scope.clickReportLight = function () {
            if(podid===null||podid===undefined||podid===''){
                showGeneralWindow("请先扫描Pod","请先扫描Pod");
                return;
            }
            receiving_commonService.CloseWindowByBtn("promenu_pop_window");
            var options = {
                title:"选择暗灯菜单",
                width:1000,
                height:650,
                open:function () {
                    $scope.badStorageState = '1';
                    $("#badStorageState").css({"display":"none"});
                    $scope.scanbadstorageworn = '';
                    //获取暗灯菜单
                    receivingService.getReportLight(function (data) {
                        for(var l=0;l<data.length;l++){
                            if(data[l]!=undefined&&(data[l].name==="扫描枪存在问题"||
                                data[l].name==="商品需要录入有效期")){
                                receiving_commonService.removeByValue(data,data[l]);
                            }
                            if(data[l].name==="商品丢失"){
                                receiving_commonService.removeByValue(data,data[l]);
                            }
                        }
                        receiving_commonService.grid_ReportMenu(data,2,function (typeid) {
                            //获取点击div的id
                            var typeName = $("#"+typeid).children("span").html();
                            reportTypeId = typeid;
                            if(typeName===INBOUND_CONSTANT.storagecantscan){
                                $scope.scansideinputer = '';
                                receiving_commonService.CloseWindowByBtn("report_light_pop_window");
                                showScanStorageSide();
                            }else{
                                // showBadStorage();
                                $scope.badStorageState = '0';
                                $("#badStorageState").css({"display":"block"});
                                setTimeout(function () {
                                    $("#scan-badstorage-inputer").focus();
                                },500);
                            }
                        });
                    });
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("report_light_pop_window",options);
        };
        //显示货位无法扫描窗口
        function showScanStorageSide() {
            var options = {
                title: INBOUND_CONSTANT.SELECTSCANLOCATION,
                width: 800,
                height: 600,
                open: function () {
                    $("#scan_side_window_grid").html("");
                    $scope.scansideinputer = '';
                    $("#scan_side_inputer_div").css({"display": "none"});
                    receiving_commonService.grid_SideStorage([
                        {
                            "id": "up",
                            "name": INBOUND_CONSTANT.scanup
                        },
                        {
                            "id": "down",
                            "name": INBOUND_CONSTANT.scandown
                        },
                        {
                            "id": "left",
                            "name": INBOUND_CONSTANT.scanleft
                        },
                        {
                            "id": "right",
                            "name": INBOUND_CONSTANT.scanright
                        }], 2, function (sideid) {
                        $("#scan_side_inputer_div").css({"display": "block"});
                        $("#scan_side_window_wnd_title").html(INBOUND_CONSTANT.SCANSTORAGE);
                        side = sideid;
                        setTimeout(function () {
                            $("#scan-side-inputer").focus();
                        },500);
                    });
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("scan_side_window", options);
        };
        //扫描周边货位方法
        $scope.sideStorageScan = function (e) {
            if(!receiving_commonService.autoAddEvent(e)){
                setTimeout(function(){ $("#scan-side-inputer").focus();}, 500);
                return;
            }
            var flag = receiving_pod_layout;
            if($scope.scansideinputer===''||$scope.scansideinputer===undefined){
                $scope.scansideworn = INBOUND_CONSTANT.storagecantscan;
                $scope.scansideinputer = '';
                setTimeout(function(){ $("#scan-side-inputer").focus();}, 500);
                return;
            }
            var storage = receiving_commonService.findSideStorageLocation($scope.scansideinputer,storages,side);
            console.log("storage--->"+storage);
            if(storage===null||
                storage===undefined){
                $scope.scansideworn = "货位号码:"+$scope.scansideinputer+"无效";
                $scope.scansideinputer = '';
                setTimeout(function(){ $("#scan-side-inputer").focus();}, 500);
                return;
            }
            $scope.scansideworn = '';
            receivingService.createReportLight({
                "storageLocationId":storage,
                "problemName":$("#"+reportTypeId).children("span").html().replace($("#"+reportTypeId).children("span").html().substring(0,1),"").replace(".",""),
                "anDonMasterTypeId":reportTypeId,
                "state":"undisposed",
                "reportBy":$window.localStorage["username"],
                "clientId":$window.localStorage["clientId"],
                "warehouseId":$window.localStorage["warehouseId"]
            },function () {
                receiving_commonService.CloseWindowByBtn("report_light_pop_window");
                receiving_commonService.CloseWindowByBtn('scan_side_window');
                setTimeout(function(){ $("#receiving-inputer").focus();}, 500);
            });
        };
        $scope.closeWindow = function (id) {
            if(isMore){
                reSetAllVar();
                $("#receiving_status_span").html("已成功上架<span style='font-size: 36px;font-weight: bold'>"+amount+"</span>件商品至</br>" + storageid);
                receiving_commonService.closePopWindow("keyboard_window");
            }
            receiving_commonService.CloseWindowByBtn(id);
        }
        //扫描商品多货确定
        $scope.scanItemGoodsMore = function () {
            isGoodsMore = true;
            receiving_commonService.CloseWindowByBtn("window_goodsmore_ok_cancel");
            receiving_commonService.receiving_tip_dialog_normal("goods_more_keyboard_window", {
                width: 600,
                title: "请输入多货数量",
                open: function () {
                    $scope.goodsMoreAmount = '';
                    $("#goods_more_keyboard_inputer").focus();
                    receiving_commonService.keyboard_fillGrid($("#goods_more_keyboard_keys"), 2, 5, "keyboard", "keyboard_layout_item",function () {
                        setTimeout(function () {
                            $("#goods_more_keyboard_inputer").focus();
                        },500);
                    });
                },
                close: function () {
                    $("#goods_more_keyboard_inputer").val("");
                    focusOnReceiveInputer();
                }
            });
        }
        //多货数量弹出框确定
        $scope.goodsMoreFinish = function () {
            /*sd update start*/
            $scope.goods_more_keyboardStatus = '1';
            /*sd update end*/
            if ($("#goods_more_keyboard_inputer").val() === '' ||
                $("#goods_more_keyboard_inputer").val() === undefined ||
                $("#goods_more_keyboard_inputer").val() === null) {
                $scope.goods_more_keyboardStatus = '0';
                return;
            }
            $scope.goodsMoreAmount = $("#goods_more_keyboard_inputer").val();
            receiving_commonService.CloseWindowByBtn('goods_more_keyboard_window');
            receiving_commonService.CloseWindowByBtn('window_stow_goods_more_ok_cancel');
            reportGoodsMore();
        }
        $scope.goodsmoreOk = function () {
            receivingService.reportGoodsMore({
                "amount": $scope.goodsMoreAmount,
                "problemType": "MORE",
                "reportBy": $window.localStorage["username"],
                "problemStorageLocation": ciper,
                "jobType": "STOW OVERAGE",
                "itemNo": item.itemNo,
                "skuNo":item.skuNo,
                "itemDataId": item.id,
                "state": "OPEN",
                "warehouseId": $window.localStorage["warehouseId"]
            }, function (data) {
                $scope.product_info_con = '0';
                $("#product_info_title").html("SKU:" + item.skuNo);
                $("#product_info_date").html("有效期:" + useNotAfter);
                $("#product_info_text").html("商品名称:" + item.name);
                if(isMore){
                        $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                        upid = receiving_commonService.findStorageLocation(storageid, storages);
                        $("#" + upid).css({"backgroundColor": "#008B00"});
                        $("#" + upid).children("span").text(amount);
                        $("#receiving_status_span").css({"backgroundColor": "#008B00"});
                        $("#receiving_status_span").html("已成功上架<span style='font-size: 36px;font-weight: bold'>"+amount+"</span>件商品至"+storageid+"</br>登记<span style='font-size: 36px;font-weight: bold'>"+$scope.goodsMoreAmount+"</span>件多货至</br>" + ciper);
                        scan_product_info = false;
                }else{
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                    $("#receiving_status_span").css({"backgroundColor": "#FF7F24"});
                    $("#receiving_status_span").html("已成功登记<span style='font-size: 36px;font-weight: bold'>"+$scope.goodsMoreAmount+"</span>件多货至</br>" + ciper);
                }
                isMore = false;
                isGoodsMore = false;
                if(parseInt($scope.remainder)<=0){
                    $scope.remainder = '0';
                    $scope.ciperAmount = '0';
                    $scope.ciperTotalAmount = '0';
                }
                receiving_commonService.CloseWindowByBtn("keyboard_window");
                receiving_commonService.CloseWindowByBtn("window_goodsmore_more_ok_cancel");
            },function (data) {
                showGeneralWindow(INBOUND_CONSTANT.REPORTGOODSMOREFAIL,INBOUND_CONSTANT.REPORTGOODSMOREFAIL);
            });
        };
        /*少货*/
        function reportGoodsLess() {
            receivingService.reportGoodsLess(ciper,function (data) {
                receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                receiving_commonService.CloseWindowByBtn("window_exitstation_ok_cancel");
                console.log("批量少货报告完成");
            }, function (data) {
               showGeneralWindow("报告少货失败","报告少货失败");
            });
        }
        /*多货*/
        function reportGoodsMore() {
            receivingService.checkGoodsMore(ciper,item.id,function (data) {
                console.log("检查多货返回-->",data);
                if(data===undefined||data===null||parseInt(data)<=0){
                    receivingService.reportGoodsMore({
                        "amount": $scope.goodsMoreAmount,
                        "problemType": "MORE",
                        "reportBy": $window.localStorage["username"],
                        "problemStorageLocation": ciper,
                        "jobType": "STOW OVERAGE",
                        "itemNo": item.itemNo,
                        "skuNo": item.skuNo,
                        "itemDataId": item.id,
                        "state": "OPEN",
                        "warehouseId": $window.localStorage["warehouseId"]
                    }, function (data) {
                        isGoodsMore = false;
                        $scope.product_info_con = '0';
                        $("#product_info_title").html("SKU:" + item.skuNo);
                        $("#product_info_date").html("有效期:" + useNotAfter);
                        $("#product_info_text").html("商品名称:" + item.name);
                        $("#receiving_status_span").css({"backgroundColor": "#FF7F24"});
                        $("#receiving_status_span").html("已成功登记<span style='font-size: 36px;font-weight: bold'>"+$scope.goodsMoreAmount+"</span>件多货至</br>" + ciper);
                    },function (data) {
                        isGoodsMore = false;
                        showGeneralWindow("检查多货失败","检查多货失败");
                    });
                }else{
                    var options = {
                        title: "已标记过该商品",
                        width: 800,
                        height: 600,
                        open: function () {
                            $("#win_goodsmore_more_content").html("该商品:"+itemid+"在该容器中已被标记过"+data+"次,是否要再次标记？");
                        }
                    };
                    receiving_commonService.receiving_tip_dialog_normal("window_goodsmore_more_ok_cancel", options);
                }
            });
        }

        //显示问题货位方法
        function showBadStorage() {
            var options = {
                title: "请扫描货位条码",
                width: 800,
                height: 600,
                close: function () {
                    $scope.scanbadstorageinputer = '';
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("scan_badstorage_window", options);
        }

        //扫描问题货位方法
        $scope.scanBadStorage = function (e) {
            if(!receiving_commonService.autoAddEvent(e)){
                return;
            }
            //提交暗灯
            if($scope.scanbadstorageinputer===''||$scope.scanbadstorageinputer===undefined){
                $scope.scanbadstorageworn = INBOUND_CONSTANT.storagecantscan;
                return;
            }
            receivingService.getstoragelocationId($scope.scanbadstorageinputer,function (data) {
                receivingService.createReportLight({
                    "storageLocationId":data.id,
                    "problemName":$("#"+reportTypeId).children("span").html().replace($("#"+reportTypeId).children("span").html().substring(0,1),"").replace(".",""),
                    "anDonMasterTypeId":reportTypeId,
                    "state":"undisposed",
                    "reportBy":$window.localStorage["username"],
                    "clientId":$window.localStorage["clientId"],
                    "warehouseId":$window.localStorage["warehouseId"]
                },function (data) {
                    // receiving_commonService.CloseWindowByBtn('scan_badstorage_window');
                    $scope.scanbadstorageinputer = '';
                    receiving_commonService.CloseWindowByBtn("report_light_pop_window");
                },function (data) {
                    $scope.scanbadstorageworn = $scope.scanbadstorageinputer+":"+data.key||data.message.replace("[","").replace("]","").replace("Unknown Error","");
                    $scope.scanbadstorageinputer = '';
                    setTimeout(function () {
                        $("#scan-badstorage-inputer").focus();
                    },500);
                });
            },function (data) {
                $scope.scanbadstorageworn = $scope.scanbadstorageinputer+":"+data.key||data.message.replace("[","").replace("]","").replace("Unknown Error","");
                $scope.scanbadstorageinputer = '';
                setTimeout(function () {
                    $("#scan-badstorage-inputer").focus();
                },500);
            });
        };
        $scope.delete_avavalue = function () {
            if (currentId === undefined || currentId === null) {
                return;
            }
            $("#" + currentId).val("");
        }
        $scope.close = function (id) {
            receivingService.getNewestReceiveAmount(adviceNo, itemid, function (data) {
                console.log("最新可收最大数量-->" + data);
                maxAmount = parseInt(data);
                receiving_commonService.CloseWindowByBtn(id);
            }, function (data) {
                showGeneralWindow("提示", "获取最新数量失败");
            });
        }
        $scope.startPodInPage = function () {
            receiving_commonService.getLocationTypesInPage(function (data) {
                receivingService.bindStorageLocationTypesToStow({
                    "locationTypeDTOS": data,
                    "stationid": $rootScope.stationId
                }, function () {
                    receiving_commonService.CloseWindowByBtn("showBinType_window");
                    setTimeout(function () {
                        $("#receive-inputer").focus();
                    }, 200);
                }, function (data) {
                    showGeneralWindow("绑定货位类型失败", "绑定货位类型失败");
                });
            });
        }

        //商品残损
        $scope.goodsDamage = function () {
            console.log("$rootScope.demagedPositionIndex--->"+$rootScope.demagedPositionIndex);
            if(scan_DAMAGED&&($rootScope.demagedPositionIndex===null||$rootScope.demagedPositionIndex===undefined||$rootScope.demagedPositionIndex==='')){
                showGeneralWindow("提示","此工作站没有绑定残品货筐无法报残");
                return;
            }
            if (!scan_DAMAGED) {
                showGeneralWindow("请先绑定货筐", "请先绑定残品货筐");
                return;
            }
            if (ciper===null||ciper===''||ciper===undefined) {
                showGeneralWindow("请先绑定车牌", "请先绑定车牌");
                return;
            }
            $scope.product_info_con = '1';
            $scope.scanbadcib = '0';
            $("#scanbadcib").html("1");
            $("#scanbadcib").css({"backgroundColor": "#FF0000"});
            $("#receiving_status_span").html("");
            $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

            $("#" + upid).css({"backgroundColor": "#8c8c8c"});
            $("#" + upid).children("span").text("");

            $("#product_info_title").html("");
            $("#product_info_text").html("");

            receiveType = INBOUND_CONSTANT.DAMAGED;
            $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
            $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
            $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
            $("#receiving_tip").html(INBOUND_CONSTANT.SCANDAMAGEITEMS);

            isDamaged = true;//商品残损
            scan_product_info = false;//扫描商品
            scan_pod = true;//不扫pod
            receiveType = INBOUND_CONSTANT.DAMAGED;//类型残损
            receiving_commonService.CloseWindowByBtn("promenu_pop_window");
            $("#receiving-inputer").focus();

        };
        // 扫描旧货筐
        $scope.scanOldContainer = function (e) {
            if (!receiving_commonService.autoAddEvent(e)) {
                return;
            }
            var storageName = $("#window-storage-inputer").val();
            if (storageName === '' || storageName === undefined) {
                $("#inputstoragewindow_span").html("货筐条码无效");
                return;
            }
            if (isOld) {
                receivingService.unbindStowContainer(storageName, $rootScope.stationName, function (data) {
                    console.log("满筐data-->",data);
                    $("#inputstoragewindow_span").html("已成功满筐" + data.cls.storageType + ",货筐条码:" + data.cls.storageName + ",商品总数" + data.cls.stockAmount + "\n请扫描新的货筐");
                    $scope.scanbadcib = '0';
                    gridStorageInfo();
                    positionIndex = data.cls.positionIndex;
                    $rootScope.demagedPositionIndex = positionIndex;
                    $scope.scancontainerType = data.cls.storageType;
                    $("#window-storage-inputer").val("");
                    isOld = false;
                    scan_DAMAGED = false;
                    $("#window-storage-inputer").focus();
                }, function (data) {
                    console.log("满筐失败");
                    if(data.key==='-1'){
                        $("#inputstoragewindow_span").html(data.values[0]);
                    }else{
                        $("#inputstoragewindow_span").html("满筐失败,请重新扫描");
                    }
                    setTimeout(function () {
                        $("#window-storage-inputer").focus();
                    },500);
                });
            } else {
                receivingService.scanStowContainer(storageName,  $scope.scancontainerType,$rootScope.stationName,positionIndex, function (data) {
                    console.log("扫描新货筐数据返回---》", data);
                    if (data.status === '2') {//有商品,提示用户
                        storageid = storageName;
                       // upid = "scanbadcib";
                        var options = {
                            width: 600,
                            height: 500,
                            title: INBOUND_CONSTANT.SUREUSELOCATION,
                            open: function () {
                                $scope.wimgstatus = 'hidden';
                                $("#win_content").html("当前货框:" + data.cls.storagelocationName + ",里面有" + data.cls.amount + "件商品，请重新确认是否继续使用当前货筐进行收货");
                            },
                            close: function () {
                                $("#receiving-inputer").focus();
                            }
                        };
                        receiving_commonService.receiving_tip_dialog("window_img_ok_cancel", options);
                    } else {
                        isOld = true;
                        scan_DAMAGED = true;
                        normalFull = false;
                         //storageid = storageName;
                        receiving_commonService.CloseWindowByBtn('scanstoragewindow');
                        if (isDamaged) {
                            storageid = storageName;
                            if ($scope.scancontainerType.toLowerCase() === INBOUND_CONSTANT.DAMAGED.toLowerCase()) {
                                $scope.scanbadcib = '0';
                            }
                            finishGoods();
                            return;
                        }
                        positionIndex = null;
                        $scope.scancontainerType = null;
                    }
                }, function (data) {

                    setTimeout(function () {
                        $("#window-storage-inputer").focus();
                    },500);
                });
            }
        };

        function focusOnReceiveInputer() {
            setTimeout(function () {
                $("#receiving-inputer").focus();
            },150);
        }

        //清楚所有上次收货状态信息
        function cleanStatus() {
            if (!isDamaged) {
                console.log("清除状态");
                $scope.scanbadcib = '1';
                $("#receiving_status_span").html("");
                $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANLOCATIONORDN);
            }
            $("#" + upid).css({"backgroundColor": "#8c8c8c"});
            $("#" + upid).children("span").text("");
        }

        function checkContainerIsScan() {
            console.log("isExit--->"+isExit+"/scanCiper--->"+scan_ciper+"/ciperFinish--->"+ciperFinish);
            if(isExit&&scan_DAMAGED){
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANCIPER);
                $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANCIPER);
                $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                $scope.product_info_con = 'hidden';
                $("#product_info_span").html("");
                $("#product_info_span").css({"backgroundColor": "#EEEEE0"});
                $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                $("#" + upid).children("span").text("");
                return;
            }
            if (!scan_DAMAGED) {
                console.log("--->damage");
                if(!isDamaged){
                    $("#receiving_dn_span").html("");
                    $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                    $scope.product_info_con = 'hidden';
                    $("#product_info_span").html("");
                    $("#product_info_span").css({"backgroundColor": "#EEEEE0"});

                    $("#receiving_status_span").html("");
                    $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                    $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                    $("#" + upid).children("span").text("");

                    $("#scanbadcib").html(INBOUND_CONSTANT.SCANDAMAGED);
                }else{
                    $("#scanbadcib").html('1');
                }
                $scope.scanbadcib = '0';
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANDAMAGED);
            } else {
                $scope.scanbadcib = '1';
                $("#receiving_dn_span").html(ciper);
                if (!scan_pod) {
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANPOD);
                } else {
                    if (!scan_ciper) {
                        if(!ciperFinish){
                            $scope.product_info_con = 'hidden';
                            $("#product_info_span").html("");
                            $("#product_info_span").css({"backgroundColor": "#EEEEE0"});
                        }
                        $("#receiving_tip").html(INBOUND_CONSTANT.SCANCIPER);
                        $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANCIPER);
                        $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
                    } else {
                        $("#receiving_dn_span").html(ciper);
                        if (!scan_product_info) {
                            if (podid == undefined || podid == null||podid==='') {
                                showGeneralWindow("请先扫描Pod", "请先扫描Pod条码");
                                scan_pod = false;
                                checkContainerIsScan();
                                return;
                            }
                            if (isDamaged) {
                                $scope.scanbadcib = '0';
                                $("#receiving_tip").html(INBOUND_CONSTANT.SCANDAMAGEITEMS);
                            } else {
                                $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEMSORCIPER);
                            }
                            $("#" + upid).css({"backgroundColor": "#8c8c8c"});
                            $("#" + upid).children("span").text("");
                            $scope.product_info_con = '1';
                            $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                            $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                            $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                            $("#receiving_status_span").html("");
                            $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                        } else {
                            if (isDamaged) {
                                $scope.scanbadcib = '0';
                                $("#receiving_tip").html(INBOUND_CONSTANT.SCANSKUTODAMAGED);
                            } else {
                                $("#receiving_tip").html(INBOUND_CONSTANT.SCANLOCATIONORDN);
                            }
                        }
                    }
                }

            }
            focusOnReceiveInputer();
        }
        $scope.showChartWindow = function () {
            $("#chart").kendoChart({
                title: {
                    text: "当前用户工作情况记录图表"
                },
                legend: {
                    position: "bottom"
                },
                chartArea: {
                    background: ""
                },
                seriesDefaults: {
                    type: "line",
                    style: "smooth"
                },
                series: [{
                    name: "目标情况",
                    data: [100, 100, 100, 100, 100, 100, 100, 100, 100, 100,100,100,100]
                },{
                    name: "实际情况",
                    data: [80, 82, 82, 83, 85, 90, 91, 90, 92, 91,94,93,96]
                }],
                valueAxis: {
                    labels: {
                        format: "{0}%"
                    },
                    line: {
                        visible: false
                    },
                    axisCrossingValue: -10
                },
                categoryAxis: {
                    categories: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13],
                    majorGridLines: {
                        visible: false
                    },
                    labels: {
                        rotation: "auto"
                    }
                },
                tooltip: {
                    visible: true,
                    format: "{0}%",
                    template: "#= series.name #: #= value #"
                }
            });
            // $(document).ready(createChart);
            // $(document).bind("kendo:skinChange", createChart);
            var options = {
                title:"信息查询",
                width:1000,
                height:550,
                open:function () {
                    receiving_commonService.CloseWindowByBtn("promenu_pop_window");
                },
                close:function () {
                    focusOnReceiveInputer();
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("chart-window",options);
        };
        // 初始化
        $scope.contentWidth = ($rootScope.mainWidth + 222 - 1200) / 2;
        $("#receiving_receive").focus();

        $scope.receivedGridOptions = {selectable: "row", height: 260, sortable: true, columns: columns};
        setTimeout(function () {
            $("#receiving_station").focus();
        }, 200); // 首获焦
    });
})();