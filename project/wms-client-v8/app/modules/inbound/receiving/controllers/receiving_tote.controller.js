/**
 * Created by 123 on 2017/4/19.
 */
(function () {
    "use strict";
    angular.module("myApp").controller("receivingToteCtl",function ($window,$scope, $rootScope, $state, $stateParams,INBOUND_CONSTANT,receivingService,receiving_commonService,commonService) {
        $scope.podstatus = '0';
        $("#receiving_user").html($window.localStorage["name"]); // 当前用户
        var baseStyle = {style: "font-size:14px;"};
        var columns= [
            {field: "positionIndex", headerTemplate: "<span translate='POSITION_INDEX'></span>", attributes: baseStyle},
            {field: "desinationName", headerTemplate: "<span translate='POSITION_NO'></span>", attributes: baseStyle},
            {field: "receiveStorageName", headerTemplate: "<span translate='PICKCAR_NO'></span>", attributes: baseStyle},
            {field: "amount", headerTemplate: "<span translate='SKU_COUNT'></span>", attributes: baseStyle}
        ];
        $scope.scanstatus = '0';
        $scope.fullfinish = '0';
        $scope.sideshow = '1';
        $scope.isSureGoodsMore = false;
        var isOld = true;
        var storageIndex = null;
        var destinationId = null;
        var positionIndex = null;
        var receiveType = INBOUND_CONSTANT.GENUINE;
        var finishType = INBOUND_CONSTANT.ALL;
        var item = null;
        console.log("$rootScope.scan_product_content_DAMAGED-->"+$rootScope.scan_product_content_DAMAGED);
        if($rootScope.scan_product_content_DAMAGED===undefined)
            $rootScope.scan_product_content_DAMAGED=false;
        if($rootScope.scan_product_content_MEASURED===undefined)
            $rootScope.scan_product_content_MEASURED=false;
        if($rootScope.scan_product_content_TO_INVESTIGATE===undefined)
            $rootScope.scan_product_content_TO_INVESTIGATE=false;
        var scan_product_content_GENUINE= false;
        var scan_product_content_DAMAGED= $rootScope.scan_product_content_DAMAGED;
        var scan_product_content_MEASURED= $rootScope.scan_product_content_MEASURED;
        var scan_product_content_TO_INVESTIGATE= $rootScope.scan_product_content_TO_INVESTIGATE;
        $("#receiving-allmode").css({"backgroundColor":"#3f51b5"});
        $("#receiving-singlemode").css({"backgroundColor":"#6E6E6E"});
        var scan_DN = false;
        var scan_product_info= false;
        var scan_bin= false;
        var isSingle= false;
        var isAll= true;
        /*sd update*/
        var finishPositionIndex = 0;
        //是否测量
        var isMeasured= false;
        //是否待调查
        var isInvest = false;
        var isDamaged = false;
        var normalFull = null;
        var isLot = false;
        var isSn = false;
        $scope.user = $window.localStorage["name"];
        $scope.operateTime = '0.9小时';
        $scope.operateTotalCount = '250';
        $scope.operatePercentage = '260/小时';
        $scope.goal = '500';
        $scope.achieved = '83%';
        $scope.prePod = 'VirtualPod';
        $scope.preLocation = 'VirtualLocation';
        $scope.preDN = 'VirtualDN';
        var adviceNo = "";
        var storageid = "";
        var amount = "";
        //应收数量
        var dnAmount = null;
        //实收数量
        var receiveAmount = null;
        //用户可收最大数量
        var maxAmount = null;
        //正常可收数量
        var canReceiveAmount = null;
        var itemid = "";
        var sn = "";
        var useAfter = "";
        var upid='';
        var avaTimeType='';
        var TimeType='';
        var currentId='';
        var index = 0;
        var storages = new Array();
        $scope.callPod = function () {

        };
        //跳转收货页面
        $scope.toReceiving = function(){
            $state.go($scope.receivingCurrent==='single'? "main.receivingSingle": "main.receivingPallet");
        };
        //多货取消
        $scope.amountCancle = function(id){
            scan_product_info = false;
            $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
            $("#" + upid+"_span").text("");
            upid = '';
            checkContainerIsScan();
            $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
            receiving_commonService.CloseWindowByBtn(id);
            receiving_commonService.CloseWindowByBtn("keyboard_window");
        };
        //收货数量弹出框
        $scope.finish_keyboard = function (isKeyDown,e) {
            if(isKeyDown){
                if(!receiving_commonService.autoAddEvent(e)) return;
            }
            if($("#keyboard_inputer").val()===undefined||$("#keyboard_inputer").val()<1){
                $scope.keyboardStatus='0';
                return;
            }else{
                $scope.keyboardStatus='1';
            }
            amount = $("#keyboard_inputer").val();
            console.log("maxAmount--->"+maxAmount);
            if(!$scope.isSureGoodsMore&&parseInt(amount)>canReceiveAmount&&parseInt(amount)<=maxAmount){
                var options = {
                    width:600,
                    height:500,
                    title:INBOUND_CONSTANT.SUREGOODSMORE,
                    open:function () {
                        $scope.moregoods = '0';
                        $("#amountsku_sku").html($("#product_info_title").html());
                        $("#amountsku_skuName").html($("#product_info_text").html());
                        $("#amountsku_dnAmount").html(dnAmount);
                        $("#amountsku_receiveAmount").html(receiveAmount);
                        $("#amountsku_inputAmount").html(amount);
                        if((parseInt(amount)-maxAmount)>=0){
                            $("#amountsku_beyondMaxAmount").html((parseInt(amount)-maxAmount));
                        }else{
                            $("#amountsku_beyondMaxAmount").html("0");
                        }
                        $("#amountsku_beyondAmount").html(receiveAmount-dnAmount+(parseInt(amount)));
                        $("#amountsku_content").html("点击确定确认收货,点击取消请将商品放回原包装箱,重新扫描DN号码/商品条码收货");
                    }
                };
                receiving_commonService.receiving_tip_dialog("window_img_ok_cancel_amount_sku",options);
            }else if(!$scope.isSureGoodsMore&&parseInt(amount)>maxAmount){
                var options = {
                    width:600,
                    height:500,
                    title:INBOUND_CONSTANT.SUREGOODSMORE,
                    open:function () {
                        $scope.moregoods = '1';
                        $("#amountsku_sku").html($("#product_info_title").html());
                        $("#amountsku_skuName").html($("#product_info_text").html());
                        $("#amountsku_dnAmount").html(dnAmount);
                        $("#amountsku_receiveAmount").html(receiveAmount);
                        $("#amountsku_inputAmount").html(amount);
                        if((parseInt(amount)-maxAmount)>=0){
                            $("#amountsku_beyondMaxAmount").html((parseInt(amount)-maxAmount));
                        }else{
                            $("#amountsku_beyondMaxAmount").html("0");
                        }
                        $("#amountsku_beyondAmount").html(receiveAmount-dnAmount+(parseInt(amount)));
                        $("#amountsku_content").html("点击确认重新输入数量,点击取消请将商品放回原包装箱,重新扫描DN号码/商品条码收货");
                    }
                };
                receiving_commonService.receiving_tip_dialog("window_img_ok_cancel_amount_sku",options);
            }else{
                receivingService.finishReceive({
                        receiveStation:$rootScope.stationId,
                        receiveStationId:$rootScope.stationName,
                        adviceId:adviceNo,
                        sn:sn,
                        useNotAfter:useAfter,
                        itemId:itemid,
                        storageLocationId:storageid,
                        amount:amount,
                        receiveType:receiveType,
                        finishType:finishType,
                        toolName:INBOUND_CONSTANT.RECEIVETOTOTE,
                        finishPositionIndex:finishPositionIndex
                    },function () {
                        scan_product_info = false;
                        $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                        if(receiveType===INBOUND_CONSTANT.DAMAGED){
                            $("#scanbadcib").css({"backgroundColor":"#008B00"});
                            $("#receiving_status_span").css({"backgroundColor":"#FF0000"});
                            $("#scanbadcib").html(amount);
                            isDamaged = false;
                        }
                        if(receiveType===INBOUND_CONSTANT.MEASURED){
                            $("#scanmeasurecib").css({"backgroundColor":"#008B00"});
                            $("#scanmeasurecib").html('1');
                            $("#receiving_status_span").css({"backgroundColor":"#FFC125"});
                            isMeasured = false;
                        }
                        if(receiveType===INBOUND_CONSTANT.TO_INVESTIGATE){
                            $("#scanwaitcib").css({"backgroundColor":"#008B00"});
                            $("#scanwaitcib").html('1');
                            $("#receiving_status_span").css({"backgroundColor":"#00BFFF"});
                            isInvest = false;
                        }
                        if(receiveType===INBOUND_CONSTANT.GENUINE){
                            upid = receiving_commonService.findStorageLocationInTote(storageid,$rootScope.normalStorageList);
                            $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                            $("#"+upid+"_span").html(amount);
                            $("#"+upid+"_span").css({"paddingTop":"20%"});
                            $("#"+upid+"_div").css({"backgroundColor":"#008B00"});
                        }
                        $("#receiving_status_span").html("已成功收货"+amount+"件商品至</br>"+storageid);
                        reSetAllVar();
                        receiving_commonService.CloseWindowByBtn("window_img_ok_cancel_amount_sku");
                        var window = $("#keyboard_window").data("kendoWindow");
                        window.close();
                        focusOnReceiveInputer();
                    },
                    function (data) {
                        if (data.key === '-1' || data.key === '-2') {
                            showStorageFull(data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
                        } else {
                            if(isDamaged||isMeasured||isInvest){
                                showGeneralWindow(data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""), data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                            }else{
                                showGeneralWindow(data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""), data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                            }
                        }
                    });
            }
        };

        function focusOnReceiveInputer() {
            setTimeout(function () {
                $("#receiving-inputer").focus();
            },500);
        }

        //序列号无法扫描
        $scope.cantScanSN = function () {
            showGeneralWindow("功能待定","功能待定");
            return;
            if(!scan_product_content_DAMAGED||!scan_product_content_MEASURED||!scan_product_content_TO_INVESTIGATE){
                showGeneralWindow("请先绑定货筐","请先绑定残品/测量/待调查货筐");
                return;
            }
            $scope.product_info_con = '1';
            $scope.scanwaitcib = '0';
            if(!scan_DN){
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANDN);
                $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANDN);
                $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
            }else{
                $("#receiving_dn_span").css({"backgroundColor":"#EEEEE0"});
                $("#product_info_span").css({"backgroundColor":"#FFDEAD"});
                $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
            }
            scan_product_info = false;
            isInvest = true;
            receiveType = INBOUND_CONSTANT.TO_INVESTIGATE;
            receiving_commonService.CloseWindowByBtn("promenu_pop_window");
            focusOnReceiveInputer();
        };
        //有效期弹出框确定
        $scope.finish_avatime_keyboard = function () {
            if($("#avatime_pop_window_madeyear").val()===''||
                $("#avatime_pop_window_madeyear").val()===undefined||
                $("#avatime_pop_window_mademonth").val()===''||
                $("#avatime_pop_window_mademonth").val()===undefined||
                $("#avatime_pop_window_madeday").val()===''||
                $("#avatime_pop_window_madeday").val()===undefined){
                return;
            }else if($("#avatime_pop_window_madeyear").val()>9999||
                $("#avatime_pop_window_mademonth").val()>12||
                $("#avatime_pop_window_madeday").val()>31){
                return;
            }
            var date = $("#avatime_pop_window_madeyear").val()+"-"+
                $("#avatime_pop_window_mademonth").val()+"-"+
                $("#avatime_pop_window_madeday").val();
            var CurrentDate = receiving_commonService.getDate();
            console.log("currentDate-->"+CurrentDate);
            var cDate = new Date(CurrentDate.replace("-",",")).getTime() ;
            var inputDate = new Date(date.replace("-",",")).getTime();
            console.log("输入时间-->"+inputDate);
            console.log("当前时间"+cDate);
            var arriveTime = null;
            if(avaTimeType===INBOUND_CONSTANT.MANUFACTURE){//按照生产日期
                if(inputDate>=cDate){
                    var options = {
                        title:INBOUND_CONSTANT.CANTBEMANUTTIME,
                        open:function () {
                            $("#tipwindow_span").html(INBOUND_CONSTANT.CANTBEMANUTTIME);
                        }
                    };
                    receiving_commonService.receiving_tip_dialog("tipwindow",options);
                    return;
                }
                arriveTime = $("#avatime_pop_window_avatime").val();
                useAfter = receiving_commonService.getDateFormat(receiving_commonService.getDays(date,arriveTime,TimeType));
                if(useAfter<cDate){
                    useAfter = null;
                    var options = {
                        title:INBOUND_CONSTANT.SURETIME,
                        open:function () {
                            $("#tipwindow_span").html(INBOUND_CONSTANT.SURETIME);
                        }
                    };
                    receiving_commonService.receiving_tip_dialog("tipwindow",options);
                    return;
                }
            }else{
                if(inputDate<cDate){
                    var options = {
                        title:INBOUND_CONSTANT.CANTBECURRENTTIME,
                        open:function () {
                            $("#tipwindow_span").html(INBOUND_CONSTANT.CANTBECURRENTTIME);
                        }
                    };
                    receiving_commonService.receiving_tip_dialog("tipwindow",options);
                    return;
                }
                useAfter = receiving_commonService.getDateFormat(receiving_commonService.getDays(date,0,'DAY'));
            }
            receivingService.checkToteAvatime(itemid,useAfter,function () {
                var window = $("#avatime_pop_window").data("kendoWindow");
                window.close();
                isLot = false;
                if(isMeasured){
                    $('#receiving_tip').html(INBOUND_CONSTANT.SCANMEASURE);
                    $scope.scanmeasurecib = '0';
                }
                setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                checkContainerIsScan();
            },function (data) {
                useAfter = null;
                $("#avatime_tip").html(data.key);
                $scope.avatime_normal = '1';
                // showGeneralWindow("有效期检查失败",data.key);
            });
        };

        $scope.receivingscan = function (e) {
            if(!receiving_commonService.autoAddEvent(e)) return;
            var inputvalue = $("#receiving-inputer").val().trim()||$scope.tipvalue;
            console.log("inputvalue-->"+inputvalue);
            $("#receiving-inputer").val("");
            $scope.tipvalue = '';
            receiving_commonService.CloseWindowByBtn("newtipwindowwithinputer");
            if(!scan_product_content_DAMAGED){
                receivingService.scanStorageLocation(inputvalue,INBOUND_CONSTANT.DAMAGED,$rootScope.stationName,$rootScope.demagedDestinationId,$rootScope.demagedPositionIndex,function (data) {
                    if(data.status==='2'){//有商品,提示用户
                        $scope.scancontainerType = INBOUND_CONSTANT.DAMAGED;
                        storageid = inputvalue;
                        destinationId = $rootScope.demagedDestinationId;
                        positionIndex = $rootScope.demagedPositionIndex;
                        var options = {
                            width:600,
                            height:500,
                            title:INBOUND_CONSTANT.SUREUSELOCATION,
                            open:function () {
                                $scope.wimgstatus='hidden';
                                $("#win_content").html("当前货框:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
                            },
                            close:function () {
                                setTimeout(function () {
                                    $("#receiving-inputer").focus();
                                },500);
                            }
                        };
                        receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                    }else{
                        scan_product_content_DAMAGED = true;
                        checkContainerIsScan();
                    }
                },function (data) {
                    var options = {
                        title:INBOUND_CONSTANT.RESCANDAMAGED,
                        width:600,
                        height:400,
                        open:function () {
                            $scope.tipvalue = '';
                            $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
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
                });
            }else{
                if(!scan_product_content_MEASURED){
                    receivingService.scanStorageLocation(inputvalue,INBOUND_CONSTANT.MEASURED,$rootScope.stationName,$rootScope.measuredDestinationId,$rootScope.measuredPositionIndex,function (data) {
                        if(data.status==='2'){//有商品,提示用户
                            $scope.scancontainerType = INBOUND_CONSTANT.MEASURED;
                            storageid = inputvalue;
                            destinationId = $rootScope.measuredDestinationId;
                            positionIndex = $rootScope.measuredPositionIndex;
                            var options = {
                                width:600,
                                height:500,
                                title:INBOUND_CONSTANT.SUREUSELOCATION,
                                open:function () {
                                    $scope.wimgstatus='hidden';
                                    $("#win_content").html("当前货框:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
                                },
                                close:function () {
                                    $("#receiving-inputer").focus();
                                }
                            };
                            receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                        }else{
                            scan_product_content_MEASURED = true;
                            checkContainerIsScan();
                            setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                        }
                    },function (data) {
                        var options = {
                            title:INBOUND_CONSTANT.RESCANMEASURED,
                            width:600,
                            height:400,
                            open:function () {
                                $scope.tipvalue = '';
                                $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
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
                    });
                }else{
                    if(!scan_product_content_TO_INVESTIGATE){
                        receivingService.scanStorageLocation(inputvalue,INBOUND_CONSTANT.TO_INVESTIGATE,$rootScope.stationName,$rootScope.investDestinationId,$rootScope.investPositionIndex,function (data) {
                            if(data.status==='2'){//有商品,提示用户
                                storageid = inputvalue;
                                $scope.scancontainerType = INBOUND_CONSTANT.TO_INVESTIGATE;
                                destinationId = $rootScope.investDestinationId;
                                positionIndex = $rootScope.investPositionIndex;
                                var options = {
                                    width:600,
                                    height:500,
                                    title:INBOUND_CONSTANT.SUREUSELOCATION,
                                    open:function () {
                                        $scope.wimgstatus='hidden';
                                        $("#win_content").html("当前货框:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
                                    },
                                    close:function () {
                                        setTimeout(function () {
                                            $("#receiving-inputer").focus();
                                        },500);
                                    }
                                };
                                receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                            }else{
                                scan_product_content_TO_INVESTIGATE = true;
                                checkContainerIsScan();
                                setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                            }
                        },function (data) {
                            var options = {
                                title:INBOUND_CONSTANT.RESCANINVEST,
                                width:600,
                                height:400,
                                open:function () {
                                    $scope.tipvalue = '';
                                    $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
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
                        });
                    }else {
                        if (!scan_product_content_GENUINE) {
                            receivingService.scanStorageLocation(inputvalue,INBOUND_CONSTANT.GENUINE,$rootScope.stationName,$rootScope.normalStorageList[index].destinationId,$rootScope.normalStorageList[index].positionIndex,function (data) {
                                storageid = inputvalue;
                                if(data.status==='2'){//有商品,提示用户
                                    $scope.scancontainerType = INBOUND_CONSTANT.GENUINE;
                                    destinationId = $rootScope.normalStorageList[index].destinationId;
                                    positionIndex = $rootScope.normalStorageList[index].positionIndex;
                                    var options = {
                                        width:600,
                                        height:500,
                                        title:INBOUND_CONSTANT.SUREUSELOCATION,
                                        open:function () {
                                            $scope.wimgstatus='hidden';
                                            $("#win_content").html("当前货框:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
                                        },
                                        close:function () {
                                            $("#receiving-inputer").focus();
                                        }
                                    };
                                    receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                                }else{
                                    // $rootScope.normalStorageList[storageIndex].storageName = storageid;
                                    console.log("--->",$rootScope.normalStorageList);
                                    console.log(""+index+"/storageid--->"+storageid);
                                    $rootScope.normalStorageList[index].storageName = storageid;
                                    console.log("111--->",$rootScope.normalStorageList);
                                    checkContainerIsScan();
                                    setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
                                }
                            },function (data) {
                                var options = {
                                    title:INBOUND_CONSTANT.RESCANCONTAINER,
                                    width:600,
                                    height:400,
                                    open:function () {
                                        $scope.tipvalue = '';
                                        $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
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
                            });
                        } else {
                            if(isMeasured||isDamaged||isInvest){
                                checkContainerIsScan();
                            }

                            receivingService.scanIsDN(inputvalue, function (data) {
                                $scope.scanDn = data;
                                if ($scope.scanDn) {
                                    scan_DN = false;
                                    scan_product_info = false;
                                    scan_bin = false;
                                    //是否测量
                                    isMeasured = false;
                                    isInvest = false;
                                    isDamaged = false;
                                    isSingle = false;
                                    if(isAll){
                                        finishType = INBOUND_CONSTANT.ALL;
                                    }else{
                                        finishType = INBOUND_CONSTANT.SINGLE;
                                    }
                                    receiveType = INBOUND_CONSTANT.GENUINE;
                                    isLot = false;
                                    isSn = false;
                                    adviceNo = "";
                                    storageid = "";
                                    amount = "";
                                    itemid = "";
                                    sn = "";
                                    useAfter = "";
                                    $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
                                    $("#" + upid+"_span").text("");
                                    upid = '';
                                    item = null;
                                    $scope.scanDn = false;
                                    $scope.scanmeasurecib = '1';
                                    $("#receiving_status_span").html("");
                                    $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                                    console.log("ScanningDN...");
                                    receivingService.scanDN(inputvalue, function (data) {
                                        scan_DN = true;
                                        adviceNo = data.cls.request.adviceNo;
                                        $("#receiving_dn_span").html(adviceNo);
                                        $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                        $scope.product_info_con = '1';
                                        $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                        $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                        $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                                        checkContainerIsScan();
                                    }, function (data) {
                                        checkContainerIsScan();
                                        $("#receiving_tip").html(INBOUND_CONSTANT.RESCANDN);
                                        var options = {
                                            title:INBOUND_CONSTANT.RESCANDN,
                                            width:600,
                                            height:400,
                                            open:function () {
                                                $scope.tipvalue = '';
                                                $("#newtipwindow_span").html("DN号码:"+inputvalue+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
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
                                    });
                                }else{
                                    if (!scan_product_info) {
                                        itemid = inputvalue;
                                        receivingService.scanToteItem(adviceNo, itemid,$rootScope.stationName,INBOUND_CONSTANT.EACH_RECEIVE, function (data) {
                                            if(isMeasured){
                                                $scope.scanwaitcib = '1';
                                                $scope.scanbadcib = '1';
                                                $scope.scanmeasurecib = '1';
                                            }
                                            console.log("data--->",data);
                                            $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
                                            $("#"+upid+"_span").html('');
                                            $("#receiving_status_span").html("");
                                            scan_product_info = true;
                                            $scope.product_info_con = '0';
                                            $("#product_info_title").html("SKU:" + data.cls.itemData.skuNo);
                                            $("#product_info_text").html("商品名称:" + data.cls.itemData.name);
                                            item = data.cls.itemData;
                                            avaTimeType = data.cls.itemData.lotType;
                                            TimeType = data.cls.itemData.itemDataGlobal.lotUnit;
                                            dnAmount = data.cls.dnAmount;
                                            receiveAmount = data.cls.receiveAmount;
                                            maxAmount = data.cls.maxReceiveAmount;
                                            canReceiveAmount = data.cls.canReceiveAmount;
                                            if(!isDamaged&&!isMeasured&&!isInvest){
                                                receiving_commonService.findStorageLocationInTote(data.cls.toteIntroStorageDTOS[0].storageName,$rootScope.normalStorageList,function (updataid) {
                                                    console.log("upid--_->"+updataid);
                                                    finishPositionIndex = data.cls.toteIntroStorageDTOS[0].positionIndex;
                                                    upid = updataid;
                                                    $("#"+updataid+"_div").css({"backgroundColor":"#00BFFF"});
                                                });
                                            }
                                            if (data.status === '1'&&!isDamaged) {//测量商品
                                                finishPositionIndex = $rootScope.measuredPositionIndex;
                                                $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
                                                $("#" + upid+"_span").text("");
                                                console.log("测量商品");
                                                isMeasured = true;
                                                isSingle = true;
                                                $("#scanmeasurecib").css({"backgroundColor":"#FFC125"});
                                                $("#scanmeasurecib").html(INBOUND_CONSTANT.DAMAGED);
                                                finishType = INBOUND_CONSTANT.SINGLE;
                                                receiveType = INBOUND_CONSTANT.MEASURED;
                                            }
                                            checkContainerIsScan();
                                            checkType(data.cls.itemData);
                                        }, function (data) {
                                            $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
                                            $("#"+upid+"_span").html('');
                                            $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                            cleanStatus();
                                            checkContainerIsScan();
                                            var options = {
                                                title:INBOUND_CONSTANT.RESCANITEM,
                                                width:600,
                                                height:400,
                                                open:function () {
                                                    $scope.tipvalue = '';
                                                    if(data.key==='-4'){
                                                        $("#newtipwindow_span").html(data.values[0]);
                                                    }else{
                                                        $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                                    }
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
                                        });
                                    } else {
                                        //检查上架货位
                                        storageid = inputvalue;
                                        if(!isInvest&&isSn&&(sn==null||sn===''||sn===undefined)){
                                            popSnWindow();
                                            return;
                                        }
                                        if(isLot&&(useAfter===null||useAfter===''||useAfter===undefined)){
                                            popLotWindow();
                                            return;
                                        }
                                        if (isMeasured || isInvest || isDamaged) {
                                            console.log("测量检查货筐");
                                            receivingService.checkNotGenuisContainer(storageid, $rootScope.stationName, receiveType, useAfter, itemid, function (data) {
                                                console.log("测量开始收货");
                                                finishGoods();
                                            }, function (data) {//扫描非正品货筐错误
                                                if (data.key === '-1' || data.key === '-2') {
                                                    showStorageFull(data.values[0]);
                                                } else {
                                                    var options = {
                                                        title:INBOUND_CONSTANT.RESCANDN,
                                                        width:600,
                                                        height:400,
                                                        open:function () {
                                                            $scope.tipvalue = '';
                                                            $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
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
                                                }
                                            });
                                        } else {
                                            receivingService.checkContainer(storageid, itemid, useAfter, sn,$rootScope.stationName,INBOUND_CONSTANT.RECEIVETOTOTE,finishPositionIndex, function (data) {
                                                scan_bin = true;
                                                finishGoods();
                                            }, function (data) {
                                                storageid = "";
                                                if ((data.key.indexOf('已超过货位商品种类最大值,请重新扫描')!=-1)||
                                                    (data.key.indexOf('%')!=-1)||
                                                    (data.key.indexOf('已超过货位承载最大值,请重新扫描')!=-1)||
                                                    (data.key.indexOf('-1')!=-1)||
                                                    (data.key.indexOf('-2')!=-1)) {
                                                    var options = {
                                                        title:INBOUND_CONSTANT.RESCANCONTAINER,
                                                        width:600,
                                                        height:400,
                                                        open:function () {
                                                            $scope.tipvalue = '';
                                                            $("#ok_tipwindow_span").html(data.values[0]);

                                                        },
                                                        close:function () {
                                                            setTimeout(function () {
                                                                $("#receiving-inputer").focus();
                                                            },400);
                                                        }
                                                    };
                                                    receiving_commonService.receiving_tip_dialog("ok_tipwindow",options);
                                                } else {
                                                    var options = {
                                                        title: INBOUND_CONSTANT.RESCANCONTAINER,
                                                        width:600,
                                                        height:400,
                                                        open: function () {
                                                            $scope.tipvalue = '';
                                                            $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                                            setTimeout(function () {
                                                                $("#newtipwindow_inputer").focus();
                                                            },500);
                                                        },
                                                        close: function () {
                                                            setTimeout(function () {
                                                                $("#receiving-inputer").focus();
                                                            },500);
                                                        }
                                                    };
                                                    receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                                }
                                            });
                                        }
                                    }
                                }


                            })



                            /*if (receiving_commonService.isDN(inputvalue)) {
                                scan_DN = false;
                                scan_product_info = false;
                                scan_bin = false;
                                //是否测量
                                isMeasured = false;
                                isInvest = false;
                                isDamaged = false;
                                isSingle = false;
                                if(isAll){
                                    finishType = INBOUND_CONSTANT.ALL;
                                }else{
                                    finishType = INBOUND_CONSTANT.SINGLE;
                                }
                                receiveType = INBOUND_CONSTANT.GENUINE;
                                isLot = false;
                                isSn = false;
                                adviceNo = "";
                                storageid = "";
                                amount = "";
                                itemid = "";
                                sn = "";
                                useAfter = "";
                                $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
                                $("#" + upid+"_span").text("");
                                upid = '';
                                item = null;
                                $scope.scanmeasurecib = '1';
                                $("#receiving_status_span").html("");
                                $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                            }
                            if (!scan_DN) {
                                console.log("ScanningDN...");
                                receivingService.scanDN(inputvalue, function (data) {
                                    scan_DN = true;
                                    adviceNo = data.cls.request.adviceNo;
                                    $("#receiving_dn_span").html(adviceNo);
                                    $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                    $scope.product_info_con = '1';
                                    $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                    $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                                    checkContainerIsScan();
                                }, function (data) {
                                    checkContainerIsScan();
                                    $("#receiving_tip").html(INBOUND_CONSTANT.RESCANDN);
                                    var options = {
                                        title:INBOUND_CONSTANT.RESCANDN,
                                        width:600,
                                        height:400,
                                        open:function () {
                                            $scope.tipvalue = '';
                                            $("#newtipwindow_span").html("DN号码:"+inputvalue+data.message.replace("[","").replace("]","").replace("Unknown Error",""));
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
                                });
                            } else {
                                if (!scan_product_info) {
                                    itemid = inputvalue;
                                    receivingService.scanToteItem(adviceNo, itemid,$rootScope.stationName,INBOUND_CONSTANT.EACH_RECEIVE, function (data) {
                                        if(isMeasured){
                                            $scope.scanwaitcib = '1';
                                            $scope.scanbadcib = '1';
                                            $scope.scanmeasurecib = '1';
                                        }
                                        console.log("data--->",data);
                                        $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
                                        $("#"+upid+"_span").html('');
                                        $("#receiving_status_span").html("");
                                        scan_product_info = true;
                                        $scope.product_info_con = '0';
                                        $("#product_info_title").html("SKU:" + data.cls.itemData.skuNo);
                                        $("#product_info_text").html("商品名称:" + data.cls.itemData.name);
                                        item = data.cls.itemData;
                                        avaTimeType = data.cls.itemData.lotType;
                                        TimeType = data.cls.itemData.itemDataGlobal.lotUnit;
                                        dnAmount = data.cls.dnAmount;
                                        receiveAmount = data.cls.receiveAmount;
                                        maxAmount = data.cls.maxReceiveAmount;
                                        canReceiveAmount = data.cls.canReceiveAmount;
                                        if(!isDamaged&&!isMeasured&&!isInvest){
                                            receiving_commonService.findStorageLocationInTote(data.cls.toteIntroStorageDTOS[0].storageName,$rootScope.normalStorageList,function (updataid) {
                                                console.log("upid--_->"+updataid);
                                                finishPositionIndex = data.cls.toteIntroStorageDTOS[0].positionIndex;
                                                upid = updataid;
                                                $("#"+updataid+"_div").css({"backgroundColor":"#00BFFF"});
                                            });
                                        }
                                        if (data.status === '1'&&!isDamaged) {//测量商品
                                            finishPositionIndex = $rootScope.measuredPositionIndex;
                                            $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
                                            $("#" + upid+"_span").text("");
                                            console.log("测量商品");
                                            isMeasured = true;
                                            isSingle = true;
                                            $("#scanmeasurecib").css({"backgroundColor":"#FFC125"});
                                            $("#scanmeasurecib").html(INBOUND_CONSTANT.DAMAGED);
                                            finishType = INBOUND_CONSTANT.SINGLE;
                                            receiveType = INBOUND_CONSTANT.MEASURED;
                                        }
                                        checkContainerIsScan();
                                        checkType(data.cls.itemData);
                                    }, function (data) {
                                        $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
                                        $("#"+upid+"_span").html('');
                                        $("#receiving_tip").html(INBOUND_CONSTANT.RESCANITEM);
                                        cleanStatus();
                                        checkContainerIsScan();
                                        var options = {
                                            title:INBOUND_CONSTANT.RESCANITEM,
                                            width:600,
                                            height:400,
                                            open:function () {
                                                $scope.tipvalue = '';
                                                if(data.key==='-4'){
                                                    $("#newtipwindow_span").html(data.values[0]);
                                                }else{
                                                    $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                                }
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
                                    });
                                } else {
                                    //检查上架货位
                                    storageid = inputvalue;
                                    if(!isInvest&&isSn&&(sn==null||sn===''||sn===undefined)){
                                        popSnWindow();
                                        return;
                                    }
                                    if(isLot&&(useAfter===null||useAfter===''||useAfter===undefined)){
                                        popLotWindow();
                                        return;
                                    }
                                    if (isMeasured || isInvest || isDamaged) {
                                        console.log("测量检查货筐");
                                        receivingService.checkNotGenuisContainer(storageid, $rootScope.stationName, receiveType, useAfter, itemid, function (data) {
                                            console.log("测量开始收货");
                                            finishGoods();
                                        }, function (data) {//扫描非正品货筐错误
                                            if (data.key === '-1' || data.key === '-2') {
                                                showStorageFull(data.values[0]);
                                            } else {
                                                var options = {
                                                    title:INBOUND_CONSTANT.RESCANDN,
                                                    width:600,
                                                    height:400,
                                                    open:function () {
                                                        $scope.tipvalue = '';
                                                        $("#newtipwindow_span").html("货筐号码:"+inputvalue+data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
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
                                            }
                                        });
                                    } else {
                                        receivingService.checkContainer(storageid, itemid, useAfter, sn,$rootScope.stationName,INBOUND_CONSTANT.RECEIVETOTOTE,finishPositionIndex, function (data) {
                                            scan_bin = true;
                                            finishGoods();
                                        }, function (data) {
                                            storageid = "";
                                            if ((data.key.indexOf('已超过货位商品种类最大值,请重新扫描')!=-1)||
                                                (data.key.indexOf('%')!=-1)||
                                                (data.key.indexOf('已超过货位承载最大值,请重新扫描')!=-1)||
                                                (data.key.indexOf('-1')!=-1)||
                                                (data.key.indexOf('-2')!=-1)) {
                                                var options = {
                                                    title:INBOUND_CONSTANT.RESCANCONTAINER,
                                                    width:600,
                                                    height:400,
                                                    open:function () {
                                                        $scope.tipvalue = '';
                                                        $("#ok_tipwindow_span").html(data.values[0]);

                                                    },
                                                    close:function () {
                                                        setTimeout(function () {
                                                            $("#receiving-inputer").focus();
                                                        },400);
                                                    }
                                                };
                                                receiving_commonService.receiving_tip_dialog("ok_tipwindow",options);
                                            } else {
                                                var options = {
                                                    title: INBOUND_CONSTANT.RESCANCONTAINER,
                                                    width:600,
                                                    height:400,
                                                    open: function () {
                                                        $scope.tipvalue = '';
                                                        $("#newtipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                                                        setTimeout(function () {
                                                            $("#newtipwindow_inputer").focus();
                                                        },500);
                                                    },
                                                    close: function () {
                                                        setTimeout(function () {
                                                            $("#receiving-inputer").focus();
                                                        },500);
                                                    }
                                                };
                                                receiving_commonService.receiving_tip_dialog("newtipwindowwithinputer", options);
                                            }
                                        });
                                    }
                                }
                            }*/
                        }
                    }
                }
            }
        };
        //显示货位类型
        $scope.showBinTypeWindow = function () {
            var options = {
                title: INBOUND_CONSTANT.storagelocationtype,
                width:800,
                height:600,
                open: function () {
                    if($scope.assignpodinfo===undefined||$scope.assignpodinfo===''||$scope.assignpodinfo===null){
                        $("#receiving-stopassignpod").css({"backgroundColor":"#FF0000"});
                        $scope.assignpodinfo = INBOUND_CONSTANT.stopassignpod;
                    }
                    receivingService.getSelectedStorageType($rootScope.stationName,function (data) {
                        receiving_commonService.grid_BayTypeInPage(data.cls.allBinType,data.cls.selectedBinType,2);
                    });
                }
            };
            receiving_commonService.receiving_tip_dialog("showBinType_window", options);
        };
        $scope.stopAssignPod = function () {
            if($scope.assignpodinfo===INBOUND_CONSTANT.stopassignpod){
                $("#receiving-stopassignpod").css({"backgroundColor":"#6E6E6E"});
                $scope.assignpodinfo = INBOUND_CONSTANT.reassignpod;
            }else{
                $("#receiving-stopassignpod").css({"backgroundColor":"#FF0000"});
                $scope.assignpodinfo = INBOUND_CONSTANT.stopassignpod;
            }
        };
        //显示货筐已满窗口
        function showStorageFull(msg) {
            var options = {
                title: INBOUND_CONSTANT.EXCHANGESTORAGE,
                open: function () {
                    $("#ok_tipwindow_span").html(msg+","+INBOUND_CONSTANT.CLICKSTORAGEFULL);
                }
            };
            receiving_commonService.receiving_tip_dialog("ok_tipwindow", options);
        }
        //显示更换货筐窗口
        $scope.showChangeStoage = function (data) {
            normalFull = data;
            receiving_commonService.CloseWindowByBtn("promenu_pop_window");
            receiving_commonService.CloseWindowByBtn("ok_tipwindow");
            var options = {
                title: INBOUND_CONSTANT.SCANFULLSTORAGE,
                width:1000,
                height:600,
                open: function () {
                    gridStorageInfo();
                    $("#inputstoragewindow_span").html("请扫描已满货筐条码");
                    $("#window-storage-inputer").val("");
                    setTimeout(function () {
                        $("#window-storage-inputer").focus();
                    },500);
                },
                close:function () {
                    if(scan_product_content_MEASURED&&scan_product_content_TO_INVESTIGATE&&scan_product_content_DAMAGED&&scan_product_content_GENUINE&&normalFull){
                        normalFull = null;
                        focusOnReceiveInputer();
                        isOld = true;
                        return;
                    }
                    if(!scan_product_content_DAMAGED||!scan_product_content_MEASURED||!scan_product_content_TO_INVESTIGATE||!scan_product_content_GENUINE){
                        checkContainerIsScan();
                        isOld = true;
                    }
                    focusOnReceiveInputer();
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("scanstoragewindow", options);
        };
        //站台货筐信息`
        function gridStorageInfo() {
            $scope.singleReceivedGridOptions = {height: 200,columns: columns};

            receivingService.scanStation($rootScope.stationName,$rootScope.currentReceive,function (data) {
                console.log("data-->",data);
                $rootScope.receiveProcessDTOList = data.cls.receiveProcessDTOList;
                $scope.scanstatus='0';
                $("#singleReceivedGRID").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: data.cls.receiveProcessDTOList}));
            },function (data) {
                $scope.scanstatus = '1';
                $("#warnStation").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
            });
        }
        //现实通用提示窗口
        function showGeneralWindow(title,msg) {
            var options = {
                title:title,
                open:function () {
                    $("#tipwindow_span").html(msg);
                },
                close:function () {
                    focusOnReceiveInputer();
                }
            };
            receiving_commonService.receiving_tip_dialog("tipwindow",options);
        }
        //切换收货模式
        $scope.switchMode = function (modeData) {
            isAll = modeData;
            if(modeData){//bu是单件
                finishType = INBOUND_CONSTANT.ALL;
                $("#receiving-singlemode").css({"backgroundColor":"#6E6E6E"});
                $("#receiving-allmode").css({"backgroundColor":"#3f51b5"});
            }else{//是单件
                finishType = INBOUND_CONSTANT.SINGLE;
                $("#receiving-allmode").css({"backgroundColor":"#6E6E6E"});
                $("#receiving-singlemode").css({"backgroundColor":"#3f51b5"});
            }
            focusOnReceiveInputer();
        };
        //检查商品是否序列号/是否有效期/是否测量
        function checkType(itemData) {
            console.log("检查商品type");
            if(itemData.serialRecordType.toLowerCase()===INBOUND_CONSTANT.alwaysrecord.toLowerCase()){//是否序列号商品
                isSn = true;
                isSingle = true;
            }
            if(itemData.lotMandatory){//是否有效期商品
                isLot = true;
            }
            if (isSn) {
                popSnWindow();
            } else {
                if (isLot) {
                    popLotWindow();
                }
            }
        }
        //单件收货
        function SingleFinishGoods() {
            if(maxAmount>0){
                receivingService.finishReceive({
                    receiveStation:$rootScope.stationId,
                    receiveStationId:$rootScope.stationName,
                    adviceId:adviceNo,
                    sn:sn,
                    useNotAfter:useAfter,
                    itemId:itemid,
                    storageLocationId:storageid,
                    amount:1,
                    receiveType:receiveType,
                    finishType:finishType,
                    toolName:INBOUND_CONSTANT.RECEIVETOTOTE,
                    finishPositionIndex:finishPositionIndex
                },function () {
                    if(isMeasured||isDamaged||isInvest){
                        console.log("特殊商品");
                        console.log("isMeasured-->"+isMeasured);
                        if(isMeasured){
                            $("#scanmeasurecib").html("1");
                            $("#scanmeasurecib").css({"backgroundColor":"#008B00"});
                            isMeasured = false;
                            $("#receiving_status_span").css({"backgroundColor":"#FFC125"});
                        }
                        if(isInvest){
                            $("#scanwaitcib").html("1");
                            $("#scanwaitcib").css({"backgroundColor":"#008B00"});
                            isInvest = false;
                            $("#receiving_status_span").css({"backgroundColor":"#00BFFF"});
                        }
                        if(isDamaged){
                            $("#scanbadcib").html("1");
                            $("#scanbadcib").css({"backgroundColor":"#008B00"});
                            isDamaged = false;
                            $("#receiving_status_span").css({"backgroundColor":"#FF0000"});
                        }
                        // $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                    }else{
                        upid = receiving_commonService.findStorageLocationInTote(storageid,$rootScope.normalStorageList);
                        $("#"+upid+"_span").html('1');
                        $("#"+upid+"_div").css({"backgroundColor":"#008B00"});
                        $("#receiving_status_span").css({"backgroundColor":"#008B00"});
                    }
                    reSetAllVar();
                    $("#receiving_status_span").html("已成功收货1件商品至</br>"+storageid);
                    receiving_commonService.closePopWindow("keyboard_window");
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                    focusOnReceiveInputer();
                },function (data) {
                    if(isDamaged||isInvest||isMeasured){
                        if ((data.key.indexOf('已超过货位商品种类最大值,请重新扫描')!=-1)||
                            (data.key.indexOf('%')!=-1)||
                            (data.key.indexOf('已超过货位承载最大值,请重新扫描')!=-1)||
                            (data.key.indexOf('-1')!=-1)||
                            (data.key.indexOf('-2')!=-1)) {
                            showStorageFull(data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
                        } else {
                            showGeneralWindow("扫描货筐错误", data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
                        }
                    }else{
                        if((data.key.indexOf('已超过货位商品种类最大值,请重新扫描')!=-1)||
                            (data.key.indexOf('%')!=-1)||
                            (data.key.indexOf('已超过货位承载最大值,请重新扫描')!=-1)||
                            (data.key.indexOf('-1')!=-1)||
                            (data.key.indexOf('-2')!=-1)){
                            showStorageFull(data.message.replace("%","").replace("&","").replace("[","").replace("]","").replace("Unknown Error",""));
                        }else{
                            showGeneralWindow(INBOUND_CONSTANT.RESCANLOCATION, data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                        }
                    }
                });
            }else{
                showGeneralWindow("请确认是否要进行多货收货","商品数量超出当前用户可收商品数量最大值,请重新扫描商品进行收货");
                scan_product_info = false;
                $scope.scanmeasurecib = '1';
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
            }
        }
        //重置相关状态位
        function reSetAllVar() {
            isSingle = false;
            isSn = false;
            isLot = false;
            isDamaged = false;
            isMeasured = false;
            isInvest = false;
            receiveType = INBOUND_CONSTANT.GENUINE;
            if(isAll){
                finishType = INBOUND_CONSTANT.ALL;
            }else{
                finishType = INBOUND_CONSTANT.SINGLE;
            }
            scan_product_info = false;
            avaTimeType = null;
            TimeType = null;
            dnAmount = null;
            receiveAmount = null;
            maxAmount = null;
            item = null;
            itemid = null;
            sn = null;
            $scope.sn = '';
            useAfter = '';
            $scope.isSureGoodsMore = false;
        }
        //弹出有效期窗口
        function popLotWindow() {
            var options = {
                title:INBOUND_CONSTANT.INPUTAVATIME,
                width:800,
                height:600,
                open:function () {
                    $scope.avatime_normal = '0';
                    $("#avatime_tip").html('商品为有效期商品,请输入商品有效期');
                    $("#avatime_sku_name").html($("#product_info_title").html());
                    $("#avatime_pop_window_madeyear").val("");
                    $("#avatime_pop_window_mademonth").val("");
                    $("#avatime_pop_window_madeday").val("");
                    $("#avatime_pop_window_avatime").val("");
                    if(avaTimeType===INBOUND_CONSTANT.MANUFACTURE){
                        $("#avatime_year_span").html(INBOUND_CONSTANT.MANU_YEAR);
                        $("#avatime_mon_span").html(INBOUND_CONSTANT.MANU_MON);
                        $("#avatime_day_span").html(INBOUND_CONSTANT.MANU_DAY);
                        $scope.avatimevalue = '0';
                        $scope.TimeType = receiving_commonService.switchUnitLot(TimeType);
                    }else{
                        $("#avatime_year_span").html(INBOUND_CONSTANT.NOTMANU_YEAR);
                        $("#avatime_mon_span").html(INBOUND_CONSTANT.NOTMANU_MON);
                        $("#avatime_day_span").html(INBOUND_CONSTANT.NOTMANU_DAY);
                        $scope.avatimevalue = '1';
                    }
                    receiving_commonService.avatime_keyboard_fillGrid($("#avatime_pop_window_keyboard"),2,5,"avatime_pop","keyboard_layout_item");
                    setTimeout(function () {
                        $("#avatime_pop_window_madeyear").focus();
                    },500);
                },
                close:function () {
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANLOCATION);
                    focusOnReceiveInputer();
                }
            };
            receiving_commonService.receiving_tip_dialog("avatime_pop_window",options);
        }
        //有效期异常确定
        $scope.avaTimeInNormalOk = function () {
            $scope.avatime_normal = '0';
            receiving_commonService.CloseWindowByBtn("avatime_pop_window");
            scan_product_info = false;
            checkContainerIsScan();
            focusOnReceiveInputer();
        };
        //有效期异常确定
        $scope.avaTimeInNormalCancle = function () {
            $scope.avatime_normal = '0';
            $("#avatime_pop_window_madeyear").val('');
            $("#avatime_pop_window_mademonth").val('');
            $("#avatime_pop_window_madeday").val('');
            $("#avatime_pop_window_avatime").val('');
            setTimeout(function () {
                $('#avatime_pop_window_madeyear').focus();
            },50);
        };
        //弹出序列号扫描窗口
        function popSnWindow() {
            if(isInvest){
                return;
            }
            var options = {
                width:800,
                height:450,
                title: INBOUND_CONSTANT.SCANSERIALNO,
                open: function () {
                    $scope.cantSN='0';
                    $scope.snTip = '';
                    $scope.sn = '';
                    $("#window-receiving-inputer").val("");
                    $("#inputwindow_span").html(item.name);
                    setTimeout(function () {
                        $("#window-receiving-inputer").focus();
                    },500);
                },
                close:function () {
                    checkContainerIsScan();
                }
            };
            receiving_commonService.receiving_tip_dialog("scanwindow", options);
        }
        //序列号弹出框扫描
        $scope.windowScan = function (e) {
            if(!receiving_commonService.autoAddEvent(e)) return;
            sn = $scope.sn;
            receivingService.checkSN(itemid,sn,function () {
                var window = $("#scanwindow").data("kendoWindow");
                window.close();
                isSn = false;
                if(isInvest){
                    isInvest = false;
                    receiveType = INBOUND_CONSTANT.GENUINE;
                }
                if(isLot){
                    popLotWindow();
                }else{
                    $("#receiving-inputer").focus();
                }
                checkContainerIsScan();
            },function (data) {
                console.log("data.key-->"+data.key);
                $scope.sn = '';
                if(data.key===-1){
                    showGeneralWindow(data.values[1],data.values[0]);
                }else{
                    $scope.cantSN='1';
                    $scope.snTip = sn+INBOUND_CONSTANT.NOTSNRESCAN;
                }
            });
        };
        //序列号确定
        $scope.win_serok = function () {
            receiving_commonService.CloseWindowByBtn("scanwindow");
            $scope.scanwaitcib='0';
            isInvest = true;
            $("#scanwaitcib").html('1');
            $("#scanwaitcib").css({"backgroundColor":"#00BFFF"});
            $('#receiving_tip').html(INBOUND_CONSTANT.SCANINVESTAGETE);
            finishType = INBOUND_CONSTANT.SINGLE;
            receiveType = INBOUND_CONSTANT.TO_INVESTIGATE;
            isSingle = true;
            if(isDamaged){
                $scope.scanbadcib='1';
                isDamaged = false;
            }
            if(isMeasured){
                isMeasured = false;
                $scope.scanmeasurecib='1';
            }
            checkContainerIsScan();
        };
        //序列后窗口取消
        $scope.win_sercancle = function () {
            $scope.cantSN='0';
            $scope.snTip = '';
            setTimeout(function () {
                $("#window-receiving-inputer").focus();
            },500);
        };
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
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("chart-window",options);
        };
        function finishGoods() {
            if(isSingle){
                //检查用户收货数量是否符合
                console.log("单件开始收货");
                console.log("maxAmount--->"+maxAmount);
                SingleFinishGoods();
            }else{
                if(isAll||isDamaged){
                    receiving_commonService.receiving_tip_dialog_normal("keyboard_window",{
                        width:600,
                        title:"请输入收货数量",
                        open:function () {
                            $("#keyboard_inputer").val("");
                            receiving_commonService.keyboard_fillGrid($("#keyboard_keys"),2,5,"keyboard","keyboard_layout_item");
                            setTimeout(function () {
                                $("#keyboard_inputer").focus();
                            },500);
                        },
                        close:function () {
                            $("#keyboard_inputer").value = "";
                            focusOnReceiveInputer();
                        }
                    });
                }else{
                    console.log("单件开始收货");
                    SingleFinishGoods();
                }
            }
        }
        //确认使用当前货筐
        $scope.win_receivingok = function (type) {
            receivingService.bindStorageLocation(storageid,type,$rootScope.stationName,destinationId,positionIndex,function (data) {
                destinationId = null;
                positionIndex = null;
                isOld = true;
                if(type.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                    scan_product_content_DAMAGED = true;
                    $rootScope.receiveProcessDTOList[index].receiveStorageName = storageid;
                }
                if(type.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                    scan_product_content_MEASURED = true;
                    $rootScope.receiveProcessDTOList[index].receiveStorageName = storageid;
                }
                if(type.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                    scan_product_content_TO_INVESTIGATE = true;
                    $rootScope.receiveProcessDTOList[index].receiveStorageName = storageid;
                }
                if(type.toLowerCase()===INBOUND_CONSTANT.GENUINE.toLowerCase()){
                    $rootScope.normalStorageList[index].storageName = storageid;
                }
                if(isMeasured||isDamaged||isInvest){
                    finishGoods();
                }
                checkContainerIsScan();
                normalFull = null;
                $scope.scancontainerType = null;
                receiving_commonService.CloseWindowByBtn('scanstoragewindow');
                receiving_commonService.CloseWindowByBtn("window_img_ok_cancel");
            });
        };
        //不使用当前货筐
        $scope.win_receivingcancel = function (type) {
            if(type.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                scan_product_content_DAMAGED = false;
                $('#receiving_tip').html(INBOUND_CONSTANT.SCANDAMAGED);
            }
            if(type.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                scan_product_content_MEASURED = false;
                $('#receiving_tip').html(INBOUND_CONSTANT.SCANMEASURE);
            }
            if(type.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                scan_product_content_TO_INVESTIGATE = false;
                $('#receiving_tip').html(INBOUND_CONSTANT.SCANINVESTAGETE);
            }
            if(type.toLowerCase()===INBOUND_CONSTANT.GENUINE.toLowerCase()){
                scan_product_content_GENUINE = false;
                $('#receiving_tip').html(INBOUND_CONSTANT.SCANSTORAGELOCATION);
            }
            receiving_commonService.CloseWindowByBtn("window_img_ok_cancel");
            checkContainerIsScan();
            setTimeout(function () {
                $("#window-storage-inputer").focus();
            },500);
        };
        $scope.autoClose = function (e) {
            if(!receiving_commonService.autoAddEvent(e)) return;
            var window = $("#tipwindow").data("kendoWindow");
            window.close();
        };
        //显示问题菜单弹窗
        $scope.showProMenuWindow = function () {
            var options = {
                title:INBOUND_CONSTANT.SELECTPMENU,
                width:800,
                height:600,
                close:function () {
                    // receiving_commonService.avatime_keyboard_fillGrid($("#avatime_pop_window_keyboard"),2,5,"avatime_pop","keyboard_layout_item",0,"32");
                    focusOnReceiveInputer();
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("promenu_pop_window",options);
        };
        //结束收货弹窗
        $scope.finishReceiveWindow = function () {
            var options = {
                title:INBOUND_CONSTANT.FINISHMENU,
                width:600,
                height:400,
                open:function () {
                    $scope.exitflag = '0';
                    $scope.exitStationContent = INBOUND_CONSTANT.FINISHRECEIVECONTENT_TOTE;
                },
                close:function () {
                    $scope.exitStationContent = INBOUND_CONSTANT.FINISHRECEIVECONTENT_TOTE;
                    focusOnReceiveInputer();
                }
            };
            receiving_commonService.receiving_tip_dialog_normal("window_general_ok_cancel",options);
        };
        //改变退出工作站提示
        $scope.exitStationBefore = function () {
            $scope.exitflag = '1';
            $scope.exitStationContent = INBOUND_CONSTANT.SUREEXITANDFULL;
        };
        //结束收货确定(结满所有筐)
        $scope.exitStation = function(){
            receivingService.exitReceiveStation($rootScope.stationName,"YES",function(){
                    $scope.maxAmount=null;
                    $scope.stationId = null; // 工作站id
                    $scope.stationName = null; // 工作站name
                    $rootScope.stationId = null;
                    $rootScope.stationName = null;
                    $rootScope.locationTypeSize=null;
                    $rootScope.maxAmount=null;
                    $rootScope.processSize = null;
                    $rootScope.areaSize = null;
                    $rootScope.receiveProcessDTOList = null;
                    $rootScope.currentReceive = null;
                    receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                    $state.go("main.receiving");
                },
                function(data){
                    alert("结束收货失败,请重试");
                });
        };
        //退出工作站不满筐
        $scope.exitNotFull = function(){
            receivingService.exitReceiveStation($rootScope.stationName,"NO",function(){
                    $scope.maxAmount=null;
                    $scope.stationId = null; // 工作站id
                    $scope.stationName = null; // 工作站name
                    $rootScope.stationId = null;
                    $rootScope.stationName = null;
                    $rootScope.locationTypeSize=null;
                    $rootScope.maxAmount=null;
                    $rootScope.processSize = null;
                    $rootScope.areaSize = null;
                    $rootScope.receiveProcessDTOList = null;
                    $rootScope.currentReceive = null;
                    receiving_commonService.CloseWindowByBtn("window_general_ok_cancel");
                    $state.go("main.receiving");
                },
                function(data){
                    alert("结束收货失败,请重试");
                });
        };
        //结束收获取消
        $scope.closeGeneralWindow = function(){
            var window = $("#window_general_ok_cancel").data("kendoWindow");
            window.close();
        };
        //有效期输入框焦点函数
        $scope.avatimemethod = function (currentid) {
            currentId = currentid;
            receiving_commonService.getavatimeid(currentid);

        };
        $scope.delete_avavalue = function () {
            if(currentId===undefined||currentId===null){
                return;
            }
            $("#"+currentId).val("");
        };
        $scope.close = function(id){
            receivingService.getNewestReceiveAmount(adviceNo,itemid,function (data) {
                console.log("最新可收最大数量-->"+data);
                maxAmount = parseInt(data);
                receiving_commonService.CloseWindowByBtn(id);
            },function (data) {
                showGeneralWindow("提示","获取最新数量失败");
            });
        };
        $scope.startPod = function () {
            receiving_commonService.getLocationTypes(function (bindata,areadata) {
                receivingService.bindStorageLocationTypes({
                    "locationTypeDTOS":bindata,
                    "areaDTOS":areadata,
                    "stationid":$rootScope.stationId
                },function () {
                    $scope.fullfinish = '1';
                    $scope.podstatus = '0';
                    setTimeout(function(){ $("#receive-inputer").focus();}, 200);
                },function (data) {
                    if(data.key==='该工作站已绑定货位类型'){
                        $scope.fullfinish = '1';
                        $scope.podstatus = '0';
                        //聚焦pod输入框
                        setTimeout(function(){ $("#receive-inputer").focus();}, 200);
                    }
                });
            });
        };
        //初始化
        $scope.receivingCurrent = $stateParams.id; // 当前收货模式

        setTimeout(function(){ $("#receiving_station").focus();}, 200); // 首获焦
        //商品残损
        $scope.goodsDamage = function(){
            if(!scan_product_content_DAMAGED||!scan_product_content_MEASURED||!scan_product_content_TO_INVESTIGATE||!scan_product_content_GENUINE){
                showGeneralWindow("请先绑定货筐","请先绑定残品/测量/待调查/正品货筐");
                return;
            }
            $scope.product_info_con = '1';
            $scope.scanbadcib = '0';
            //$("#scanbadcib").html("1");
            $("#scanbadcib").css({"backgroundColor":"#FF0000"});
            $("#receiving_status_span").html("");
            $("#receiving_status_span").css({"backgroundColor":"#eeeee0"});

            $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
            $("#" + upid+"_span").text("");

            $("#product_info_title").html("");
            $("#product_info_text").html("");

            if(!scan_DN){
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANDN);
                $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANDN);
                $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});
            }else{
                receiveType = INBOUND_CONSTANT.DAMAGED;
                $("#receiving_dn_span").css({"backgroundColor":"#EEEEE0"});
                $("#product_info_span").css({"backgroundColor":"#FFDEAD"});
                $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
            }
            isDamaged = true;
            scan_product_info = false;
            receiveType = INBOUND_CONSTANT.DAMAGED;
            receiving_commonService.CloseWindowByBtn("promenu_pop_window");
            $("#receiving-inputer").focus();

        };
        // 扫描旧货筐
        $scope.scanOldContainer = function(e){
            if(!receiving_commonService.autoAddEvent(e)){
                return;
            }
            var storageName = $("#window-storage-inputer").val();
            $("#window-storage-inputer").val("");
            if(storageName===null||storageName===''||storageName===undefined){
                $("#inputstoragewindow_span").html("货筐条码无效");
                return;
            }
            if(isOld){
                receivingService.fullStorage(storageName,$rootScope.stationName,function (data) {
                    console.log("data-->"+JSON.stringify(data));
                    $("#inputstoragewindow_span").html("已成功满筐"+data.cls.storageType+",货筐条码:"+data.cls.storageName+",商品总数"+data.cls.stockAmount+"\n请扫描新的货筐");
                    gridStorageInfo();
                    destinationId = data.cls.destinationId;
                    positionIndex = data.cls.positionIndex;
                    $scope.scancontainerType = data.cls.storageType;
                    console.log("满筐类型->"+data.cls.storageType);
                    if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                        console.log("满残品");
                        $scope.scanbadcib = '0';
                        scan_product_content_DAMAGED = false;
                        index = receiving_commonService.findStorageLocationIndexInAllTote(storageName,$rootScope.receiveProcessDTOList);
                        $rootScope.receiveProcessDTOList[index].receiveStorageName = null;
                    }
                    if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                        console.log("满测量");
                        $scope.scanmeasurecib = '0';
                        scan_product_content_MEASURED = false;
                        index = receiving_commonService.findStorageLocationIndexInAllTote(storageName,$rootScope.receiveProcessDTOList);
                        $rootScope.receiveProcessDTOList[index].receiveStorageName = null;
                    }
                    if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                        console.log("满待调查");
                        $scope.scanwaitciban = '0';
                        scan_product_content_TO_INVESTIGATE = false;
                        index = receiving_commonService.findStorageLocationIndexInAllTote(storageName,$rootScope.receiveProcessDTOList);
                        $rootScope.receiveProcessDTOList[index].receiveStorageName = null;
                    }
                    if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.GENUINE.toLowerCase()){
                        console.log("满正品");
                        scan_product_content_GENUINE = false;
                        index = receiving_commonService.findStorageLocationIndexInTote(storageName,$rootScope.normalStorageList);
                        $rootScope.normalStorageList[index].storageName = null;
                    }
                    isOld = false;
                    setTimeout(function () {
                        $("#window-storage-inputer").focus();
                    },100);
                    gridStorageInfo();
                },function (data) {
                    $("#inputstoragewindow_span").html("满筐失败,请重新扫描");
                });
            }else{
                $("#window-storage-inputer").val("");
                receivingService.scanStorageLocation(storageName,$scope.scancontainerType,$rootScope.stationName,destinationId,positionIndex,function (data) {
                    console.log("扫描新货筐数据返回---》",data);
                    if(data.status==='2'){//有商品,提示用户
                        storageid = storageName;
                        var options = {
                            width:600,
                            height:500,
                            title:INBOUND_CONSTANT.SUREUSELOCATION,
                            open:function () {
                                $scope.wimgstatus='hidden';
                                $("#win_content").html("当前货框:"+data.cls.storagelocationName+",里面有"+data.cls.amount+"件商品，请重新确认是否继续使用当前货筐进行收货");
                            },
                            close:function () {
                                focusOnReceiveInputer();
                            }
                        };
                        receiving_commonService.receiving_tip_dialog("window_img_ok_cancel",options);
                    }else{
                        isOld = true;
                        storageid = storageName;
                        if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                            scan_product_content_DAMAGED = true;
                            $rootScope.receiveProcessDTOList[index].receiveStorageName = storageid;
                        }
                        if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                            scan_product_content_MEASURED = true;
                            $rootScope.receiveProcessDTOList[index].receiveStorageName = storageid;
                        }
                        if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                            scan_product_content_TO_INVESTIGATE = true;
                            $rootScope.receiveProcessDTOList[index].receiveStorageName = storageid;
                        }
                        if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.GENUINE.toLowerCase()){
                            scan_product_content_GENUINE = true;
                            $rootScope.normalStorageList[index].storageName = storageid;
                        }
                        if(isDamaged||isMeasured||isInvest||(itemid!==undefined&&itemid!==''&&itemid!==null&&!normalFull)){
                            if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.DAMAGED.toLowerCase()){
                                $scope.scanbadcib = '0';
                            }
                            if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.MEASURED.toLowerCase()){
                                $scope.scanmeasurecib = '0';
                            }
                            if($scope.scancontainerType.toLowerCase()===INBOUND_CONSTANT.TO_INVESTIGATE.toLowerCase()){
                                $scope.scanwaitciban = '0';
                            }
                            finishGoods();
                            //return;
                        }
                        destinationId = null;
                        positionIndex = null;
                        $scope.scancontainerType = null;
                        receiving_commonService.CloseWindowByBtn('scanstoragewindow');
                        checkContainerIsScan();
                    }
                },function (data) {
                    var options = {
                        title:INBOUND_CONSTANT.RESCANLOCATION,
                        open:function () {
                            $("#tipwindow_span").html(data.message.replace("[","").replace("]","").replace("Unknown Error",""));
                        },
                        close:function () {
                            $("#receiving-inputer").focus();
                        }
                    };
                    receiving_commonService.receiving_tip_dialog("tipwindow",options);
                });
            }
        };

        //有效期输入框会车焦点转移到
        $scope.avaTimeNextFocus = function (e,id) {
            if(!receiving_commonService.autoAddEvent(e)) return;
            switch(id){
                case 'avatime_pop_window_madeyear':{
                    setTimeout(function () {
                        $("#avatime_pop_window_mademonth").focus();
                    },50);
                }break;
                case 'avatime_pop_window_mademonth':{
                    setTimeout(function () {
                        $("#avatime_pop_window_madeday").focus();
                    },50);
                }break;
                case 'avatime_pop_window_madeday':{
                    if($scope.avatimevalue==='1'){
                        $scope.finish_avatime_keyboard();
                    }else{
                        setTimeout(function () {
                            $("#avatime_pop_window_avatime").focus();
                        },50);
                    }
                }break;
                case 'avatime_pop_window_avatime':{
                    $scope.finish_avatime_keyboard();
                }break;
            }
        };

        //清楚所有上次收货状态信息
        function cleanStatus() {
            if(!isDamaged&&!isInvest&&!isMeasured){
                console.log("清除状态");
                $scope.scanbadcib='1';
                $scope.scanmeasurecib='1';
                $scope.scanwaitcib='1';
                $("#receiving_status_span").html("");
                $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANLOCATION);
            }
            // $("#" + upid+"_div").css({"backgroundColor": "#8c8c8c"});
            // $("#" + upid+"_span").text("");
        }
        function checkContainerIsScan() {
            if(!scan_product_content_DAMAGED){
                $("#receiving_dn_span").html("");
                $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                $scope.product_info_con = 'hidden';
                $("#product_info_span").html("");
                $("#product_info_span").css({"backgroundColor": "#EEEEE0"});

                $("#receiving_status_span").html("");
                $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                $("#"+upid+"_div").children("span").html("");
                $("#"+upid+"_div").css({"backgroundColor":"#8c8c8c"});

                $scope.scanbadcib='0';
                $scope.scanmeasurecib='1';
                $scope.scanwaitcib='1';
                $("#receiving_tip").html(INBOUND_CONSTANT.SCANDAMAGED);
            }else{
                if(!scan_product_content_MEASURED){
                    $("#receiving_dn_span").html("");
                    $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                    $scope.product_info_con = 'hidden';
                    $("#product_info_span").html("");
                    $("#product_info_span").css({"backgroundColor": "#EEEEE0"});

                    $("#receiving_status_span").html("");
                    $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                    $("#"+upid+"_div").children("span").html("");
                    $("#"+upid+"_div").css({"backgroundColor":"#8c8c8c"});

                    $scope.scanmeasurecib='0';
                    $scope.scanbadcib='1';
                    $scope.scanwaitcib='1';
                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANMEASURE);
                }else{
                    if(!scan_product_content_TO_INVESTIGATE){
                        $("#receiving_dn_span").html("");
                        $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                        $scope.product_info_con = 'hidden';
                        $("#product_info_span").html("");
                        $("#product_info_span").css({"backgroundColor": "#EEEEE0"});

                        $("#receiving_status_span").html("");
                        $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                        $("#"+upid+"_div").children("span").html("");
                        $("#"+upid+"_div").css({"backgroundColor":"#8c8c8c"});

                        $scope.scanwaitcib='0';
                        $scope.scanbadcib='1';
                        $scope.scanmeasurecib='1';
                        $("#receiving_tip").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                    }else{
                        $scope.scanwaitcib='1';
                        $scope.scanbadcib='1';
                        $scope.scanmeasurecib='1';
                        if(!scan_product_content_GENUINE){
                            $("#receiving_dn_span").html("");
                            $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                            $scope.product_info_con = 'hidden';
                            $("#product_info_span").html("");
                            $("#product_info_span").css({"backgroundColor": "#EEEEE0"});

                            $("#receiving_status_span").html("");
                            $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});

                            $("#"+upid+"_div").children("span").html("");
                            $("#"+upid+"_div").css({"backgroundColor":"#8c8c8c"});

                            var length = $rootScope.maxAmount-3;
                            var flag = false;
                            for (var l=0;l<length;l++){
                                storageIndex = $rootScope.normalStorageList[l].positionIndex;
                                if($rootScope.normalStorageList[l].storageName===null||
                                    $rootScope.normalStorageList[l].storageName===undefined||
                                    $rootScope.normalStorageList[l].storageName===''){
                                    index = l;
                                    console.log("当前绑定索引---》"+l);
                                    $("#"+storageIndex+"_div").children("span").html("");
                                    $("#"+storageIndex+"_div").css({"backgroundColor":"#00BFFF"});
                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANSTORAGELOCATION);
                                    flag = true;
                                   break;
                                }else{
                                    $("#"+storageIndex+"_div").children("span").html("");
                                    $("#"+storageIndex+"_div").css({"backgroundColor":"#8c8c8c"});
                                }
                            }
                            if(!flag){
                                console.log("邦全了");
                                scan_product_content_GENUINE = true;
                                checkContainerIsScan();
                            }else{
                                return;
                            }
                        }else{
                            $scope.scanwaitcib='1';
                            $scope.scanbadcib='1';
                            $scope.scanmeasurecib='1';
                                if(!scan_DN){
                                    $scope.product_info_con = '1';
                                    $("#product_info_span").css({"backgroundColor": "#EEEEE0"});
                                    $("#product_info_span").html('');

                                    $("#receiving_tip").html(INBOUND_CONSTANT.SCANDN);
                                    $("#receiving_dn_span").html(INBOUND_CONSTANT.SCANDN);
                                    $("#receiving_dn_span").css({"backgroundColor": "#FFDEAD"});

                                }else{

                                    $("#receiving_status_span").html("");
                                    $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                                    if(!scan_product_info){
                                        if(isDamaged){
                                            $scope.scanbadcib='0';
                                            $scope.scanmeasurecib='1';
                                            $scope.scanwaitcib='1';
                                            $("#scanbadcib").html("");
                                            $("#scanbadcib").css({"backgroundColor":"#FF0000"});
                                        }else if(isMeasured){
                                            $scope.scanbadcib='1';
                                            $scope.scanmeasurecib='0';
                                            $scope.scanwaitcib='1';
                                            $("#scanmeasurecib").html("1");
                                            $("#scanmeasurecib").css({"backgroundColor":"#FFC125"});
                                        }else if(isInvest){
                                            $scope.scanbadcib='1';
                                            $scope.scanmeasurecib='1';
                                            $scope.scanwaitcib='0';
                                            $("#scanwaitcib").html("1");
                                            $("#scanwaitcib").css({"backgroundColor":"#00BFFF"});
                                        }
                                        $scope.product_info_con = '1';
                                        $("#product_info_span").html(INBOUND_CONSTANT.SCANITEMS);
                                        $("#product_info_span").css({"backgroundColor": "#FFDEAD"});
                                        $("#receiving_dn_span").html(adviceNo);
                                        $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                        $("#receiving_status_span").html("");
                                        $("#receiving_status_span").css({"backgroundColor": "#eeeee0"});
                                        $("#receiving_tip").html(INBOUND_CONSTANT.SCANITEM);
                                    }else{
                                        $("#receiving_dn_span").html(adviceNo);
                                        $("#receiving_dn_span").css({"backgroundColor": "#EEEEE0"});
                                        $scope.product_info_con = '0';
                                        if(isDamaged){
                                            $scope.scanbadcib='0';
                                            $scope.scanmeasurecib='1';
                                            $scope.scanwaitcib='1';
                                            $("#scanbadcib").css({"backgroundColor":"#FF0000"});
                                            $("#"+upid+"_div").children("span").html("");
                                            $("#"+upid+"_div").css({"backgroundColor":"#8c8c8c"});
                                            $("#scanbadcib").html("");
                                            $("#receiving_tip").html(INBOUND_CONSTANT.SCANSKUTODAMAGED);
                                        }else if(isMeasured){
                                            $scope.scanbadcib='1';
                                            $scope.scanmeasurecib='0';
                                            $scope.scanwaitcib='1';
                                            $("#scanmeasurecib").css({"backgroundColor":"#FFC125"});
                                            $("#scanmeasurecib").html("1");
                                            $("#receiving_tip").html(INBOUND_CONSTANT.SCANMEASURE);
                                            $("#"+upid+"_div").children("span").html("");
                                            $("#"+upid+"_div").css({"backgroundColor":"#8c8c8c"});
                                        }else if(isInvest){
                                            $scope.scanbadcib='1';
                                            $scope.scanmeasurecib='1';
                                            $scope.scanwaitcib='0';
                                            $("#scanwaitcib").css({"backgroundColor":"#00BFFF"});
                                            $("#scanwaitcib").html("1");
                                            $("#receiving_tip").html(INBOUND_CONSTANT.SCANINVESTAGETE);
                                            $("#"+upid+"_div").children("span").html("");
                                            $("#"+upid+"_div").css({"backgroundColor":"#8c8c8c"});
                                        }else{
                                            $("#receiving_tip").html(INBOUND_CONSTANT.SCANLOCATION);
                                        }
                                    }
                            }
                        }
                    }
                }
            }
            setTimeout(function(){ $("#receiving-inputer").focus();}, 200);
        }
        console.log("$rootScope.normalStorageList--->",$rootScope.normalStorageList);
        receiving_commonService.reveive_ToteFillGrid($("#receivetotote_grid"),($rootScope.maxAmount-3),3,"receivegrid","receiveToToteDiv",0,0,$rootScope.normalStorageList);
        checkContainerIsScan();
        setTimeout(function(){ $("#receiving_station").focus();}, 200); // 首获焦
    });
})();