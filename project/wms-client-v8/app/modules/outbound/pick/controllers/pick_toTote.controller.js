/**
 * Created by frank.zhou on 2017/01/17.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("pickToToteCtl", function ($scope, $stateParams, $rootScope, $state, $interval,
                                                                  FLOOR_COLOR,OUTBOUND_CONSTANT,BACKEND_CONFIG,pickToToteService,
                                                                  webSocketService,$window,outboundService,$timeout) {
        $scope.podheight = $("#podheight").height();//pod父div的高度
        var scanDamagedStep = 1;//当前扫描残品筐的操作步骤
        var scanUnscanStep = 1;//当前扫描无法扫描筐的操作步骤
        var scanInventeorStep = 1;//当前扫描待调查筐的操作步骤
        var inputAmount = 1;//当前输入商品数的步骤
        var clickBinButton = 0;//获取当前点击的是要扫描哪个货位
        var bindStorageStep = 0;//继续使用现有货框时的判断
        var scanNewStorageStep = 0;//货框已满判断
        var putProblemStep = 0;//放置问题商品类型判断
        var canNotScanPickStationPositionId = "";//无法扫描筐物理位置id
        var cannotScanDigitallabelId = "";//无法扫描电子标签id
        var damagedPickStationPositionId = "";//残品框物理位置id
        var damagedDigitallabelId = "";//残品电子标签id
        var damageBasket = "";//残品框name
        var unScanBasketNo = "";//无法扫描框name
        var lightResult = 0;//拍灯状态
        var socket;//webSocket
        var podSocket;
        var podValue = 0;
        var pageStateStep = "waitPod";
        $scope.pickNoInput = "";
        $scope.damagedItemInput = "";
        var date = new Date();
        /////////////////////////////////////////////////////////////外部函数///////////////////////////////////////////////////////
        // 扫描工作站
        $scope.scanStation = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            $scope.stationNo = $scope.pickPackStationName;
            pickToToteService.scanStation($scope.stationNo, function (data) {
                if(data.msg=="none"){
                    $scope.message = "条码 "+$scope.stationNo+" 不是一个有效工作站，请重新扫描";
                    $scope.messageOperate="show";
                    $scope.pickPackStationName = "";
                }
                if(data.msg =="user"){
                    $scope.message = "工作站已分配给员工"+data.obj+"，请重新扫描";
                    $scope.messageOperate="show";
                    $scope.pickPackStationName = "";
                }
                if(data.msg == "storage"){
                    $scope.storages = data.obj.pickingUnitLoadResults;
                    $scope.workStationId = data.obj.workStationId;
                    $scope.logicStationId = data.obj.logicStationId;
                    $scope.sectionId = data.obj.sectionId;
                    getLightResult();
                    $scope.amountStorage = $scope.storages.length;
                    $scope.amountPosition = data.obj.amountPosition;
                    $scope.orderIndex = data.obj.orderIndexList;
                    $scope.stopAssignOrderStep = data.obj.stopAssignOrder;

                    for(var i= 0 ;i<$scope.amountStorage; i++){
                        if($scope.storages[i].positionIndex == 21){
                            $scope.damagedPickUnitLoadId = $scope.storages[i].id;
                            damagedPickStationPositionId = $scope.storages[i].pickStationPositionId;
                            damagedDigitallabelId = data.obj.damagedLightId;
                        }
                        if($scope.storages[i].positionIndex == 22){
                            $scope.canNotScanpickUnitLoadId = $scope.storages[i].id;
                            canNotScanPickStationPositionId = $scope.storages[i].pickStationPositionId;
                            cannotScanDigitallabelId = data.obj.unScanLightId;
                        }
                    }
                    $scope.chooseTypeWindow("pickStyleId",$scope.pickStyleWindows);

                }
                if(data.msg =="success"){
                    $scope.workStationId = data.obj.workStationId;
                    getLightResult();
                    $scope.amountPosition = data.obj.amountPosition;
                    $scope.orderIndex = data.obj.orderIndexList;
                    $scope.logicStationId = data.obj.logicStationId;
                    $scope.sectionId = data.obj.sectionId;
                    $scope.stopAssignOrderStep = data.obj.stopAssignOrder;
                    initMainPage();//初始化主页面
                    checkProStorage($scope.stationNo);//先检查2个问题货筐是否绑定
                }
            },function (data) {
                if(data.key == "PICK_STATION_ALREADY_BINDED") {//实际工作站被绑定
                    $scope.message = data.values[0];
                    $scope.messageOperate = "show";
                    $scope.pickPackStationName = "";
                }
            });
        };
        //自动满筐所有车牌重新绑定
        $scope.reused = function () {
            $scope.pickStyleWindows.close();
            pickToToteService.fullAllStorage($scope.stationNo,function (data) {
                initMainPage();//初始化主页面
                checkProStorage($scope.stationNo);//先检查2个问题货筐是否绑定
            });
        }
        //继续使用当前货框
        $scope.continueUse = function () {
            $scope.pickStyleWindows.close();
            initMainPage();
            checkProStorage($scope.stationNo);//先检查2个问题货筐是否绑定
        }
        //扫描残品筐
        $scope.scanDamagedBasket  = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            $scope.reScanCanPinWindows.close();
            var canPinBasket = $scope.canPinBasket;
            damageBasket = $scope.canPinBasket;
            var obj = {
                "pickStationPositionId":damagedPickStationPositionId,
                "stationName":$scope.stationNo,
                "storageName":damageBasket,
                "pickOrderId":"",
                "type":"DAMAGE"
            };
            pickToToteService.scanCanPinBasket(obj,function(data){
                $scope.damagedPickUnitLoadId = data.pickUnitLoadId;
                //残品筐扫描成功,进入桉灯
                $scope.windowMessage = "请按动残品货筐上方暗灯或扫描货筐上方标签";
                $("#canPinId").css("background","#00B050");
                $scope.basketOperate = "DamagedBasketDigital";
                setTimeout(function () {
                    $("#DamagedBasketDigitalId").focus();
                }, 100);
                // console.log("扫描残品框："+canPinBasket);
                //拍灯
                $scope.clickDamageLight = $interval(function () {
                    if(lightResult == 1){
                        lightResult = 0;
                        $scope.scanDigitalWindows.close();
                        $interval.cancel($scope.clickDamageLight);
                        clearLight();
                        if(scanNewStorageStep == 1){
                            scanNewStorageStep = 0;
                            if(scanDamagedStep == 2){//残品框绑定成功后，给残品框放残品时，发现有效期不同等等异常，判断拣货单的数量
                                var obj1 = {
                                    "pickIds": $scope.pickIds,
                                    "pickUnitLoadId": $scope.damagedPickUnitLoadId,
                                    "itemNo": $scope.skuDamagedNo,
                                    "amountPicked": 1,
                                    "pickingOrderPositionState":"Damage"
                                };
                                //  $scope.newBasketStep = "newBasket";
                                checkDamageAmount(obj1);
                            }
                        }
                        if(scanDamagedStep == 1){//当第一次扫描残品框时，去检查是否绑定无法扫描货框
                            $("#canPinId").css("background","#D9D9D9");
                            if(scanNewStorageStep == 0){
                                checkProStorage($scope.stationNo);
                            }else{
                                toWaitpodOrWaitscan();
                                /*if(pageStateStep == "waitPod"){
                                 clearPodPage();
                                 }else {
                                 $("#canPinId").css("background", "#D9D9D9");
                                 $scope.skuNo = "";
                                 $timeout(function () {
                                 $("#skuId").focus();
                                 })
                                 $scope.basketOperate = "sku";
                                 $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                                 //  scanNewStorageStep = 1;//恢复初始值

                                 $scope.checkOrderBnt = true;
                                 $scope.skuMissBnt = false;
                                 $scope.canNotBnt = false;
                                 $scope.damageBnt = false;

                                 pageStateStep = "checkItem";
                                 }*/
                            }
                        }
                    }
                },100);
            },function (data) {
                if(data.key=="GOODS_EXIST_IN_LOCATION"){
                    openDamagedWindow(data,canPinBasket);
                }
                if(data.key == "UNITLOAD_ALREADY_BINDED_TO_STATION"){
                    openDamagedScanWindow(data,canPinBasket);
                }
                if(data.key == "NO_SUCH_UNITLOAD"){
                    openDamagedNoneWindow(canPinBasket);
                }
                if(data.key == "UNITLOAD_IS_LOCKED"){
                    openDamagedLockWindow(canPinBasket);
                }
            });
        }
        //扫描残品筐上方的灯的标签
        $scope.scanDamagedBasketDigital  = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var canPinBasketDigitalName = $scope.canPinBasketDigitalName;
            $scope.scanDigitalWindows.close();
            pickToToteService.scanDigitalName(canPinBasketDigitalName,function (data) {
                if(damagedDigitallabelId == data.digitalId || $scope.oldPositionLightId == data.digitalId){
                    sendMessage2(data.digitalId);
                    $interval.cancel($scope.clickDamageLight);
                    $interval.cancel($scope.clickDamageLight1);
                    clearLight();
                    if(scanNewStorageStep == 1){
                        scanNewStorageStep = 0;
                        if(scanDamagedStep == 2){//残品框绑定成功后，给残品框放残品时，发现有效期不同等等异常，判断拣货单的数量
                            var obj1 = {
                                "pickIds": $scope.pickIds,
                                "pickUnitLoadId": $scope.damagedPickUnitLoadId,
                                "itemNo": $scope.skuDamagedNo,
                                "amountPicked": 1,
                                "pickingOrderPositionState":"Damage"
                            };
                            //  $scope.newBasketStep = "newBasket";
                            checkDamageAmount(obj1);
                        }
                    }
                    if(scanDamagedStep == 1){//当第一次扫描残品框时，去检查是否绑定无法扫描货框
                        $("#canPinId").css("background","#D9D9D9");
                        if(scanNewStorageStep == 0){
                            checkProStorage($scope.stationNo);
                        }else{
                            toWaitpodOrWaitscan();
                            /*if(pageStateStep == "waitPod"){
                             clearPodPage();
                             }else {
                             $("#canPinId").css("background", "#D9D9D9");
                             $scope.skuNo = "";
                             $timeout(function () {
                             $("#skuId").focus();
                             })
                             $scope.basketOperate = "sku";
                             $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                             //  scanNewStorageStep = 1;//恢复初始值

                             $scope.checkOrderBnt = true;
                             $scope.skuMissBnt = false;
                             $scope.canNotBnt = false;
                             $scope.damageBnt = false;

                             pageStateStep = "checkItem";
                             }*/
                        }
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalOperate = "scanDamagedBasketDigital1";
                    $scope.digitalErrorMsg = "扫描的位置标签"+canPinBasketDigitalName+"不匹配，请重新扫描";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#damagedBasketDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "scanDamagedBasketDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#damagedBasketDigitalId1").focus();});
            });
            $scope.canPinBasketDigitalName = "";
        }
        //扫描无法扫描货筐
        $scope.scanUnScanSKUBasket = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            $scope.reUnScanWindows.close();
            var unScanSKUBasket = $scope.unScanSKUBasket;
            unScanBasketNo = $scope.unScanSKUBasket;
            var obj = {
                "pickStationPositionId":canNotScanPickStationPositionId,
                "stationName":$scope.stationNo,
                "storageName":$scope.unScanSKUBasket,
                "pickOrderId":"",
                "type":"INVENTORY"
            };
            pickToToteService.scanUnScanSKUBasket(obj,function (data) {
                $scope.canNotScanpickUnitLoadId = data.pickUnitLoadId;
                //无法扫描商品货筐扫描成功,进入桉灯
                $scope.windowMessage = "请按动无法扫描商品货筐上方暗灯或者扫描货筐上方标签";
                $("#unScanId").css("background","#00B050");
                $scope.basketOperate = "scanUnScanSKUBasketDigital";
                setTimeout(function () {
                    $("#unScanSKUBasketDigitalId").focus();
                }, 100);
                //拍灯，获取返回信息'
                $scope.clickCannotScanLight = $interval(function () {
                    if(lightResult == 1){
                        lightResult = 0;
                        $scope.scanDigitalWindows.close();
                        $interval.cancel($scope.clickCannotScanLight);
                        clearLight();
                        if(scanNewStorageStep == 1){
                            scanNewStorageStep = 0;//恢复初始值
                            /* if(scanUnscanStep == 1){//第一次扫描货框时，进入扫描pod
                             $("#unScanId").css("background","#D9D9D9");
                             $scope.reUnScanWindows.close();
                             // $scope.canNotScanpickUnitLoadId = data.pickUnitLoadId;
                             checkProStorage($scope.stationNo);
                             }*/
                            if(scanUnscanStep == 2){
                                var obj1 = {
                                    "pickIds": $scope.pickIds,
                                    "pickUnitLoadId": $scope.canNotScanpickUnitLoadId,
                                    "itemNo": "",
                                    "amountPicked": 1,
                                    "pickingOrderPositionState":"NotScan"
                                };
                                // $scope.scanBasketStep = "newBasket";
                                inputNotScanItemAmount(obj1);
                            }
                        }
                        if(scanUnscanStep == 1){//第一次扫描货框时，进入扫描pod
                            $("#unScanId").css("background","#D9D9D9");
                            if(scanNewStorageStep == 0){
                                $scope.reUnScanWindows.close();
                                // $scope.canNotScanpickUnitLoadId = data.pickUnitLoadId;
                                checkProStorage($scope.stationNo);
                            }else{
                                toWaitpodOrWaitscan();
                                /* if(pageStateStep == "waitPod"){
                                 clearPodPage();
                                 }else{
                                 $("#unScanId").css("background","#D9D9D9");
                                 $scope.skuNo="";
                                 $scope.basketOperate = "sku";
                                 $timeout(function () {$("#skuId").focus();})
                                 $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                                 //  scanNewStorageStep = 1;//恢复初始值

                                 $scope.checkOrderBnt = true;
                                 $scope.skuMissBnt = false;
                                 $scope.canNotBnt = false;
                                 $scope.damageBnt = false;

                                 pageStateStep = "checkItem";
                                 }*/

                            }
                        }
                    }
                },100);
            },function (data) {
                if (data.key == "GOODS_EXIST_IN_LOCATION") {
                    //无法扫描商品货筐里有商品
                    openUnScanWindow(data,unScanSKUBasket);
                }
                if (data.key == "UNITLOAD_ALREADY_BINDED_TO_STATION") {
                    //无法扫描商品货筐已被绑定
                    openUnScanBindWindow(data,unScanSKUBasket);
                }
                if (data.key == "NO_SUCH_UNITLOAD") {
                    //无效的无法扫描商品货筐
                    openUnScanNoneWindow(unScanSKUBasket);
                }
                if(data.key == "UNITLOAD_IS_LOCKED"){
                    openUnScanLockWindow(unScanSKUBasket);
                }
            })
        }
        //扫描无法扫描货筐上方的灯的标签
        $scope.scanUnScanSKUBasketDigital  = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var unScanSKUBasketDigitalName = $scope.unScanSKUBasketDigitalName;
            $scope.scanDigitalWindows.close();
            pickToToteService.scanDigitalName(unScanSKUBasketDigitalName,function (data) {
                if(cannotScanDigitallabelId == data.digitalId || $scope.oldPositionLightId == data.digitalId){
                    sendMessage2(data.digitalId);
                    $interval.cancel($scope.clickCannotScanLight);
                    $interval.cancel($scope.clickCannotScanLight1);
                    clearLight();
                    if(scanNewStorageStep == 1){
                        scanNewStorageStep = 0;//恢复初始值
                        /* if(scanUnscanStep == 1){//第一次扫描货框时，进入扫描pod
                         $("#unScanId").css("background","#D9D9D9");
                         $scope.reUnScanWindows.close();
                         // $scope.canNotScanpickUnitLoadId = data.pickUnitLoadId;
                         checkProStorage($scope.stationNo);
                         }*/
                        if(scanUnscanStep == 2){
                            var obj1 = {
                                "pickIds": $scope.pickIds,
                                "pickUnitLoadId": $scope.canNotScanpickUnitLoadId,
                                "itemNo": "",
                                "amountPicked": 1,
                                "pickingOrderPositionState":"NotScan"
                            };
                            // $scope.scanBasketStep = "newBasket";
                            inputNotScanItemAmount(obj1);
                        }
                    }
                    if(scanUnscanStep == 1){//第一次扫描货框时，进入扫描pod
                        $("#unScanId").css("background","#D9D9D9");
                        if(scanNewStorageStep == 0){
                            $scope.reUnScanWindows.close();
                            // $scope.canNotScanpickUnitLoadId = data.pickUnitLoadId;
                            checkProStorage($scope.stationNo);
                        }else{
                            toWaitpodOrWaitscan();
                            /*if(pageStateStep == "waitPod"){
                             clearPodPage();
                             }else{
                             $("#unScanId").css("background","#D9D9D9");
                             $scope.skuNo="";
                             $scope.basketOperate = "sku";
                             $timeout(function () {$("#skuId").focus();})
                             $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                             //  scanNewStorageStep = 1;//恢复初始值

                             $scope.checkOrderBnt = true;
                             $scope.skuMissBnt = false;
                             $scope.canNotBnt = false;
                             $scope.damageBnt = false;

                             pageStateStep = "checkItem";
                             }*/
                        }
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalOperate = "scanUnScanSKUBasketDigital1";
                    $scope.digitalErrorMsg = "扫描的位置标签"+unScanSKUBasketDigitalName+"不匹配，请重新扫描";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#unScanSKUBasketDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "scanUnScanSKUBasketDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#unScanSKUBasketDigitalId1").focus();});
            });
            $scope.unScanSKUBasketDigitalName = "";
        }
        //扫描空拣货货筐
        $scope.scanemptyBasket = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            //关闭错误弹窗
            $scope.emptyWindows.close();
            var empty = $scope.emptyBasket;
            var obj = {
                "pickStationPositionId":$scope.genuinePickStationPositionId,
                "stationName":$scope.stationNo,
                "storageName":$scope.emptyBasket,
                "pickOrderId":$scope.pickOrderId,
                "type":"INVENTORY"
            };
            pickToToteService.scanEmptyStorage(obj,function (data) {
                $scope.emptyBasket = "";
                // 空拣货货筐扫描成功,进入桉灯
                $scope.windowMessage = "请按动空拣货货筐上方暗灯或者扫描货筐上方标签";
                $("#empty"+$scope.index).css("background", "#00B050");
                $scope.basketOperate = "inventoryBasketDigital";
                setTimeout(function () {
                    $("#inventoryBasketDigitalId").focus();
                }, 100);
                // 拍灯，获取返回信息
                $scope.clickNewBasketLight = $interval(function () {
                    if(lightResult == 1){
                        lightResult = 0;
                        $interval.cancel($scope.clickNewBasketLight);
                        $scope.scanDigitalWindows.close();
                        clearLight();
                        if(scanNewStorageStep == 0){//扫描空拣货框，获取拣货单
                            $("#empty"+$scope.index).css("background","#D9D9D9");
                            $scope.pickUnitLoadId = data.pickUnitLoadId;
                            //获取拣货单信息
                            getOrder($scope.podNo,$scope.stationNo);
                        }
                        if(scanNewStorageStep == 1){ //重新绑定后，继续再之前扫描商品的页面操作
                            $("#empty"+$scope.index).css("background","#D9D9D9");
                            setTimeout(function () {$("#skuId").focus()}, 200);
                            $scope.pickUnitLoadId = data.pickUnitLoadId;
                            $scope.basketOperate = "sku";
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            scanNewStorageStep = 1;

                            $scope.skuMissBnt = false;
                            $scope.canNotBnt = false;
                            $scope.damageBnt = false;
                            $scope.checkOrderBnt = true;

                            pageStateStep = "checkItem";
                        }
                    }
                },100);
            },function (data) {
                if (data.key == "GOODS_EXIST_IN_LOCATION") {
                    //空拣货货筐里有商品
                    openEmptyWindow(data,empty);
                }else  if (data.key == "UNITLOAD_ALREADY_BINDED_TO_STATION") {
                    //空拣货货筐已被绑定
                    openEmptyBindWindow(data,empty);
                }else if (data.key == "NO_SUCH_UNITLOAD") {
                    //无效的空拣货货筐
                    openEmptyNoneWindow(empty);
                }else if(data.key == "UNITLOAD_IS_LOCKED"){
                    openEmptyLockWindow(empty);
                }else{
                    openEmptyBasketTypeWindow(data.values[0]);
                }
            });
        }
        //扫描空拣货货筐上方的灯的标签
        $scope.scanInventoryBasketDigital  = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var inventoryBasketDigitalName = $scope.inventoryBasketDigitalName;
            $scope.scanDigitalWindows.close();
            pickToToteService.scanDigitalName(inventoryBasketDigitalName,function (data) {
                if($scope.digitalLabelId == data.digitalId){
                    sendMessage2(data.digitalId);
                    $interval.cancel($scope.clickNewBasketLight);
                    clearLight();
                    var empty = $scope.emptyBasket;
                    $scope.emptyBasket = "";
                    if(scanNewStorageStep == 0){//扫描空拣货框，获取拣货单
                        //  scanNewStorageStep = 0;
                        $("#empty"+$scope.index).css("background","#D9D9D9");
                        $scope.pickUnitLoadId = data.pickUnitLoadId;
                        //获取拣货单信息
                        getOrder($scope.podNo,$scope.stationNo);
                    }
                    if(scanNewStorageStep == 1){ //重新绑定后，继续再之前扫描商品的页面操作
                        $("#empty"+$scope.index).css("background","#D9D9D9");
                        setTimeout(function () {$("#skuId").focus()}, 200);
                        $scope.pickUnitLoadId = data.pickUnitLoadId;
                        $scope.basketOperate = "sku";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        // scanNewStorageStep = 1;

                        $scope.skuMissBnt = false;
                        $scope.canNotBnt = false;
                        $scope.damageBnt = false;
                        $scope.checkOrderBnt = true;

                        pageStateStep = "checkItem";
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalOperate = "inventoryBasketDigital1";
                    $scope.digitalErrorMsg = "扫描的位置标签"+inventoryBasketDigitalName+"不匹配，请重新扫描";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#inventoryBasketDigital1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "inventoryBasketDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#inventoryBasketDigital1").focus();});
            });
            $scope.inventoryBasketDigitalName = "";
        }
        //扫描pod
        $scope.scanPod = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            $scope.podName = $scope.podNo;
            pickToToteService.scanPod($scope.podNo, function (data) {
                // $scope.podInfo = data;
                //initPod(data);
                checkOrder($scope.podNo,$scope.stationNo);
                //getOrder($scope.podNo,$scope.stationNo);
            },function (data) {
                $scope.podException = data.values[0];
                $scope.podNo = "";
                $scope.errorWindow("podErrorId",$scope.podErrorWindows);
            });
        }
        $scope.confirmPodError = function () {
            $scope.podErrorWindows.close();
            setTimeout(function () {
                $("#podId").focus();
            }, 200);
        }

        //扫描正品商品
        $scope.scanSKU = function (e) {
            var keyCode = window.event ? e.keyCode : e.wich;
            if (keyCode != 13) return;
            var obj = {
                "pickIds": $scope.pickIds,
                "pickUnitLoadId": $scope.pickUnitLoadId,
                "itemNo": $scope.skuNo,
                "amountPicked": ""
            };

            pickToToteService.scanSKU(obj, function (data) {
                $scope.scanItemErrorWindows.close();
                var obj1 = {
                    "pickIds": $scope.pickIds,
                    "pickUnitLoadId": $scope.pickUnitLoadId,
                    "itemNo": $scope.skuNo,
                    "amountPicked": 1
                };
                sendMessage($scope.orderDigitalLabelId);//打开放置商品的灯
                console.log("亮灯的位置是："+$scope.i);
                $("#empty"+$scope.i).css("background","#33CCFF");
                $("#empty"+$scope.i).text($scope.pickAmount);
                checkInventoryItemAmount(obj1);
            },function (data) {
                $scope.scanItemErrorWindows.close();
                $scope.skuNo = "";
                if(data.key == "NO_ITEMDATA_WITH_ITEMNUMER"){
                    $scope.itemMsg = data.values[0];
                    $scope.openWindow({
                        windowId:"scanItem",
                        windowClass:"myWindow",
                        windowName:$scope.scanItemErrorWindows,
                        width:700,
                        height:260,
                        closeable:false,
                        visible:true,
                        activate:function () {$("#scanItemInput").focus();}
                    })
                }
                if(data.key == "ITEM_IS_NOT_MATCH"){
                    $scope.itemMsg = data.values[0];
                    $scope.openWindow({
                        windowId:"scanItem",
                        windowClass:"myWindow",
                        windowName:$scope.scanItemErrorWindows,
                        width:700,
                        height:260,
                        closeable:false,
                        visible:true,
                        activate:function () {$("#scanItemInput").focus();}
                    })
                }
                if(data.key == "TOO_MANY_ITEM_EXIST"){
                    $scope.itemMsg = data.values[0];
                    $scope.itemNo= "";
                    $scope.openWindow({
                        windowId:"scanItem",
                        windowClass:"myWindow",
                        windowName:$scope.scanItemErrorWindows,
                        width:700,
                        height:260,
                        closeable:false,
                        visible:true,
                        activate:function () {$("#scanItemInput").focus();}
                    })
                }
            })
        }
        //正品商品的数量判断
        function checkInventoryItemAmount(obj) {
            var amount = $scope.pickAmount;//拣货单需要拣货的数量
            if(amount == 1) {//需要拣货数量为1，直接到后台确认拣货
                pickToToteService.confirmPickAmount(obj, function (data) {
                    $scope.batchState = data.state;
                    $scope.batchName = data.pickOrderNo;
                    putSKU(data);
                })
            }
            if(amount > 1){//数量大于1的，需要显示选择数量界面，当确认输入数量后，再提交后台确认
                $scope.basketOperate = "amountInput";
                $scope.windowMessage = "请输入拣货数量";
                $scope.skuOperate= "skuNoInput";
                $scope.amount = amount;
                $scope.comfirmAmountBnt = false;
            }
        }
        //输入正品数量后确认
        $scope.confirmInputAmount = function () {
            //判断拣货数量和输入的数量
            $scope.pickNo = $scope.pickAmount;
            if($scope.pickNoInput != "" && $scope.pickAmount > $scope.pickNoInput ){
                $scope.openWindow({
                    windowId:"isInputNoId",
                    windowClass:"myWindow",
                    windowName:$scope.isInputNoWindows,
                    width:700,
                    height:260,
                    closeable:false,
                    visible:true,
                })
            }else if($scope.pickAmount < $scope.pickNoInput){
                $scope.inputSuccessAmount = "1";
                $scope.openWindow({
                    windowId:"inputNoId",
                    windowClass:"myWindow",
                    windowName:$scope.inputNoWindows,
                    width:700,
                    height:200,
                    closeable:false,
                    visible:true,
                })
            }else if($scope.pickNoInput == "" || $scope.pickNoInput == undefined){
                $scope.inputSuccessAmount = "0";
                $scope.openWindow({
                    windowId:"inputNoId",
                    windowClass:"myWindow",
                    windowName:$scope.inputNoWindows,
                    width:700,
                    height:200,
                    closeable:false,
                    visible:true,
                })
            }else{
                inputItemDataAmount();
            }
        }
        //输入正品数量拣商品
        function inputItemDataAmount() {
            var obj = {
                "pickIds": $scope.pickIds,
                "pickUnitLoadId": $scope.pickUnitLoadId,
                "itemNo": $scope.skuNo,
                "amountPicked": $scope.pickNoInput
            };
            $("#empty"+$scope.i).text($scope.pickNoInput);
            pickToToteService.confirmPickAmount(obj, function (data) {
                $scope.batchState = data.state;
                $scope.batchName = data.pickOrderNo;
                putSKU(data);
                $scope.pickNoInput = "";
            })
        }
        //当输入数量小于需求数量时，确认
        $scope.confirmInput = function () {
            $scope.isInputNoWindows.close();
            inputItemDataAmount();
            $scope.skuNo = "";
        }
        //当输入数量小于需求数量时，取消
        $scope.cancelInput = function () {
            $scope.pickNoInput = "";
            $scope.pickNo = "";
            $scope.isInputNoWindows.close();
        }
        //选择拣货数量
        $scope.bind = function (x) {
            if($scope.amount < x){
                $scope.pickNoInput = "";
            }else{
                $scope.pickNoInput += x;
            }
        }
        //扫描残品商品
        $scope.scanskuDamaged = function (e) {
            var keyCode = window.event ? e.keyCode:e.which;
            if(keyCode != 13) return;
            var obj = {
                "pickIds": $scope.pickIds,
                "pickUnitLoadId": $scope.damagedPickUnitLoadId,
                "itemNo": $scope.skuDamagedNo,
                "amountPicked": ""
            };
            $scope.problemPickUnitLoadId = $scope.damagedPickUnitLoadId;
            $scope.changeBakWindows.close();
            $scope.scanDamageItemErrorWindows.close();
            pickToToteService.scanskuDamaged(obj,function (data) {
                var obj1 = {
                    "pickIds": $scope.pickIds,
                    "pickUnitLoadId": $scope.damagedPickUnitLoadId,
                    "itemNo": $scope.skuDamagedNo,
                    "amountPicked": 1,
                    "pickingOrderPositionState":"Damage"
                };
                $("#empty"+$scope.i).css("background","#D9D9D9");
                $("#empty"+$scope.i).text("");
                $("#canPinId").css("background","#FF0000");
                $("#unScanId").css("background","#D9D9D9");
                sendMessage(damagedDigitallabelId);//打开对应的灯
                checkDamageAmount(obj1);
            },function (data) {
                $scope.skuDamagedNo = "";
                $scope.scanDamageItemErrorWindows.close();
                if(data.key == "SAME_ITEM_DIFFERENT_CLIENT"){
                    $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                    $scope.BasketFullMessaage="货筐号码： "+data.values[0]+"中存在相同名称不同客户商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
                if(data.key == "SAME_ITEMNAME_DIFFERENT_LOT"){
                    $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                    $scope.BasketFullMessaage="货筐号码： "+data.values[0]+"中存在相同名称不同有效期商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
                if(data.key == "NO_ITEMDATA_WITH_ITEMNUMER"){
                    $scope.damageItemMsg = data.values[0];
                    $scope.openWindow({
                        windowId:"scanDamageItem",
                        windowClass:"myWindow",
                        windowName:$scope.scanDamageItemErrorWindows,
                        width:700,
                        height:260,
                        closeable:false,
                        visible:true,
                        activate:function () {$("#scanDamageItemInput").focus();},
                    })
                }
                if(data.key == "ITEM_IS_NOT_MATCH"){
                    $scope.damageItemMsg = data.values[0];
                    $scope.openWindow({
                        windowId:"scanDamageItem",
                        windowClass:"myWindow",
                        windowName:$scope.scanDamageItemErrorWindows,
                        width:700,
                        height:260,
                        closeable:false,
                        visible:true,
                        activate:function () {$("#scanDamageItemInput").focus();},
                    })
                }
                if(data.key == "TOO_MANY_ITEM_EXIST"){
                    $scope.damageItemMsg = data.values[0];
                    $scope.openWindow({
                        windowId:"scanDamageItem",
                        windowClass:"myWindow",
                        windowName:$scope.scanDamageItemErrorWindows,
                        width:700,
                        height:260,
                        closeable:false,
                        visible:true,
                        activate:function () {$("#scanDamageItemInput").focus();},
                    })
                }
            })
        }
        //判断要不要输入拣货数量
        function checkDamageAmount(obj1) {
            var amount = $scope.pickAmount;
            if(amount == 1) {//需要拣货数量为1，直接到后台确认拣货
                pickToToteService.confirmDamagedAmount(obj1, function (data) {
                    $("#unScanId").css("background","#D9D9D9");
                    $("#canPinId").css("background","#D9D9D9");
                    putCanPin(amount,"damage");//放残品
                },function (data) {
                    if(data.key == "SAME_ITEM_DIFFERENT_CLIENT"){
                        $scope.newBasketStep = "newBasket";
                        $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                        $scope.BasketFullMessaage="货筐号码： "+data.values[0]+"中存在相同名称不同客户商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                    }
                    if(data.key == "SAME_ITEMNAME_DIFFERENT_LOT"){
                        $scope.newBasketStep = "newBasket";
                        $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                        $scope.BasketFullMessaage="货筐号码： "+data.values[0]+"中存在相同名称不同有效期商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                    }
                })
            }
            if(amount > 1){
                $scope.damageAmount = amount;
                $scope.basketOperate = "amountInput";
                $scope.windowMessage = "请输入残品数量";
                $scope.damagedItemInput= "";
                $scope.comfirmDamageAmountBnt = false;
                /*$scope.openWindow({
                 windowId:"damagedItemId",
                 windowClass:"myWindow",
                 windowName:$scope.damagedItemAmountWindows,
                 width:700,
                 height:300,
                 title:"请输入残品数量",
                 // closeable:false,
                 actions:[],
                 })*/
                $scope.damageAmountWindow("damagedItemId",$scope.damagedItemAmountWindows,"请输入残品数量",function(){$("#damagedItemInput").focus()});
            }
        }
        //输入残品数量
        $scope.inputDamagedAmount = function (x) {
            if($scope.damageAmount < x){
                $scope.damagedItemInput = "";
            }else{
                $scope.damagedItemInput += x;
            }
        }
        //输入残品数量，确认
        $scope.confirmDamagedInput = function () {
            if($scope.damageAmount < $scope.damagedItemInput){
                $
                $scope.inputSuccessAmount = "2";
                $scope.openWindow({
                    windowId: "inputNoId",
                    windowClass: "myWindow",
                    windowName: $scope.inputNoWindows,
                    width: 700,
                    height: 260,
                    closeable: false,
                    visible: true,
                })
            }else if($scope.damagedItemInput == "" || $scope.damagedItemInput == undefined){
                $scope.inputSuccessAmount = "0";
                $scope.openWindow({
                    windowId: "inputNoId",
                    windowClass: "myWindow",
                    windowName: $scope.inputNoWindows,
                    width: 700,
                    height: 260,
                    closeable: false,
                    visible: true,
                })
            }else {
                $scope.damagedItemAmountWindows.close();
                $scope.inputNoWindows.close();
                if($scope.skuDamagedNo == "" || $scope.skuDamagedNo == null || $scope.skuDamagedNo == "undefined" ){ //无法扫描
                    var obj = {
                        "pickIds": $scope.pickIds,
                        "pickUnitLoadId": $scope.canNotScanpickUnitLoadId,
                        "itemNo": $scope.skuDamagedNo,
                        "amountPicked": $scope.damagedItemInput,
                        "pickingOrderPositionState":"NotScan"
                    };
                }else{
                    var obj = {
                        "pickIds": $scope.pickIds,
                        "pickUnitLoadId": $scope.damagedPickUnitLoadId,
                        "itemNo": $scope.skuDamagedNo,
                        "amountPicked": $scope.damagedItemInput,
                        "pickingOrderPositionState":"Damage"
                    };
                }

                pickToToteService.confirmDamagedAmount(obj, function (data) {
                    $scope.damagedItemAmountWindows.close();
                    $("#unScanId").css("background","#D9D9D9");
                    $("#canPinId").css("background","#D9D9D9");
                    if($scope.skuDamagedNo != "" && $scope.skuDamagedNo != null){
                        putCanPin($scope.damagedItemInput,"damage");
                    }else{
                        putCanPin($scope.damagedItemInput,"canNotScan");
                    }
                    $scope.skuDamagedNo = "";
                },function (data) {
                    if(data.key == "SAME_ITEM_DIFFERENT_CLIENT"){
                        $scope.newBasketStep = "newBasket";
                        $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                        $scope.BasketFullMessaage="货筐号码： "+data.values[0]+"中存在相同名称不同客户商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                    }
                    if(data.key == "SAME_ITEMNAME_DIFFERENT_LOT"){
                        $scope.newBasketStep = "newBasket";
                        $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                        $scope.BasketFullMessaage="货筐号码： "+data.values[0]+"中存在相同名称不同有效期商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                    }
                })
            }
        }
        //货筐已满按钮事件
        $scope.BasketFull = function () {
            $scope.changeBakWindows.close();
            //获取该站台货筐目前的信息
            /* pickToToteService.getBakInfo($scope.stationNo,function (data) {
             $scope.bakInfo=data.pickingUnitLoadResults;
             });*/
            scanNewStorageStep = 1;
            pickToToteService.getBakInfo($scope.pickPackStationName,"full",function (data) {
                $scope.bakInfo=data.pickingUnitLoadResults;
                //去掉正品貨框的滿框
                /* var unitLoads = [];
                 var dataResults = data.pickingUnitLoadResults;
                 for (var i = 0; i<dataResults.length; i++){
                 if(dataResults[i].type == "INVENTORY" && dataResults[i].positionIndex != 22){
                 continue;
                 }else{
                 unitLoads.push(dataResults[i]);
                 }
                 }
                 $scope.bakInfo = unitLoads;*/
            });
            // $scope.windowMessage="请扫描已满货筐";
            $scope.scanFullBakWindow("scanFullId",$scope.scanFullWindows,function(){$("#fullBasketId").focus()});
        }
        //扫描已满货筐
        $scope.scanfullBasket = function(e){
            var keyCode = window.event ? e.keyCode:e.which;
            if(keyCode != 13) return;
            pickToToteService.scanfullBasket($scope.fullBasket,$scope.stationNo,function (data) {
                $scope.scanfullBasketWindows.close();
                $scope.newbasketNo = "";
                $scope.storageTypeName = data.storageName;
                $scope.positionIndex = data.positionIndex;
                $scope.oldPickStationPositionId = data.pickStationPostionId;
                $scope.oldPositionLightId = data.digitalLabelId;
                $scope.scanFullWindows.close();
                if($scope.positionIndex == 21) {
                    $scope.orderId = "";
                    $scope.stoarageType = "DAMAGE";
                    $("#canPinId").css("background","red");
                    sendMessage(damagedDigitallabelId);
                    $scope.newBasketNo = "已成功满筐在残品货筐位置，货筐条码：" + $scope.fullBasket + "，商品总数" + data.sum + "件,请扫描新的货筐放在残品货筐位置";
                }
                if($scope.positionIndex == 22){
                    $scope.orderId = "";
                    $scope.stoarageType = "INVENTORY";
                    $("#unScanId").css("background","#33CCFF");
                    sendMessage(cannotScanDigitallabelId);
                    $scope.newBasketNo = "已成功满筐在无法扫描货筐位置，货筐条码：" + $scope.fullBasket + "，商品总数" + data.sum + "件,请扫描新的货筐放在无法扫描货筐位置";
                }
                if(data.storageName == "INVENTORY" && $scope.positionIndex != 22){
                    $scope.orderId = data.pickOrderId;
                    $scope.stoarageType = "INVENTORY";
                    $("#empty"+$scope.positionIndex).css("background","#33CCFF");
                    sendMessage($scope.oldPositionLightId);
                    $scope.newBasketNo = "已成功满筐"+data.positionIndex+"货筐位置，货筐条码：" + $scope.fullBasket + "，商品总数" + data.sum
                        + "件,请扫描新的货筐放在"+data.positionIndex+"货筐位置";
                }
                $scope.openWindow({
                    windowId:"newbakId",
                    windowName:$scope.newbakWindows,
                    windowClass:"blueWindow",
                    width:700,
                    height:300,
                    closeable:true,
                    activate:function () {$("#newbasketId").focus();},
                    title:"请扫描新的货筐",
                });
                $scope.windowMessage="请扫描新的货筐";
            },function (data) {
                $scope.fullBasket = "";
                $scope.scanFullWindows.close();
                $scope.scanfullBasketWindows.close();
                $scope.scanFullBasketErrorMsg = data.values[0];
                $scope.openWindow({
                    windowId:"scanfullBasketWinId",
                    windowName:$scope.scanfullBasketWindows,
                    windowClass:"blueWindow",
                    width:700,
                    height:300,
                    closeable:true,
                    activate:function () {$("#fullNewBasketId").focus();},
                    // title:"请扫描新的货筐",
                });
            })
        }
        //扫描新的货筐
        $scope.scannewbasketNo = function (e) {
            var keyCode = window.event ? e.keyCode:e.which;
            if(keyCode != 13) return;
            var info = $scope.newbasketNo;
            var obj = {
                "pickStationPositionId":$scope.oldPickStationPositionId,
                "stationName":$scope.stationNo,
                "storageName":info,
                "pickOrderId":$scope.orderId,
                "type":$scope.stoarageType
            };
            $scope.fullBasket = "";
            pickToToteService.scanNewBasket(obj,function (data) {
                // $scope.newbasketNo = "";
                $scope.newbakWindows.close();
                //按灯操作
                if($scope.positionIndex == 21) {
                    $scope.newBasketNo = "已成功扫描车牌" + $scope.newbasketNo + "在残品货筐位置;请按动货筐上方暗灯或者扫描货筐上方标签";
                    $scope.damagedPickUnitLoadId = data.pickUnitLoadId;
                    $("#canPinId").css("background","#00B050");
                }else if($scope.positionIndex == 22) {
                    $scope.newBasketNo = "已成功扫描车牌" + $scope.newbasketNo + "在无法扫描货筐位置;请按动货筐上方暗灯或者扫描货筐上方标签";
                    $scope.canNotScanpickUnitLoadId = data.pickUnitLoadId;
                    $("#unScanId").css("background","#00B050");
                }else{
                    $scope.newBasketNo = "已成功扫描车牌" + $scope.newbasketNo + "在"+$scope.positionIndex+"位置;请按动货筐上方暗灯或者扫描货筐上方标签";
                    $scope.pickUnitLoadId = data.pickUnitLoadId;
                    $("#empty"+$scope.positionIndex).css("background","#00B050");
                }
                $scope.newbakWindows.setOptions({
                    title:"请按动货筐上方暗灯或者扫描货筐上方标签",
                });
                $scope.basketOperate = "windowMessage";
                $scope.windowMessage="请按动货筐上方暗灯或者扫描货筐上方标签";
                $scope.basketOperate = "newBasketDigital";
                setTimeout(function () {
                    $("#newBasketDigitalId").focus();
                }, 100);
                $scope.clickNewLight = $interval(function () {
                    if(lightResult == 1){
                        lightResult = 0;
                        $interval.cancel($scope.clickNewLight);
                        $scope.scanDigitalWindows.close();
                        clearLight();
                        if(scanNewStorageStep == 1){
                            // $scope.newBasketStep = "newBasket";
                            if ($scope.positionIndex == 21) {
                                $scope.type = "DAMAGE";
                                var obj1 = {
                                    "pickIds": $scope.pickIds,
                                    "pickUnitLoadId": $scope.damagedPickUnitLoadId,
                                    "itemNo": $scope.skuDamagedNo,
                                    "amountPicked": 1,
                                    "pickingOrderPositionState":"Damage"
                                };
                                checkDamageAmount(obj1);
                            } else if($scope.positionIndex == 22){
                                $scope.type = "INVENTORY";
                                var obj1 = {
                                    "pickIds": $scope.pickIds,
                                    "pickUnitLoadId": $scope.canNotScanpickUnitLoadId,
                                    "itemNo": "",
                                    "amountPicked": 1,
                                    "pickingOrderPositionState":"NotScan"
                                };
                                inputNotScanItemAmount(obj1);
                            }
                        }else{
                            $("#canPinId").css("background","#D9D9D9");
                            $("#unScanId").css("background","#D9D9D9");
                            $("#empty"+$scope.positionIndex).css("background","#D9D9D9");
                            clearPodPage();
                            /* if(pageStateStep == "waitPod"){
                             clearPodPage();
                             }else{
                             $("#canPinId").css("background","#D9D9D9");
                             $("#unScanId").css("background","#D9D9D9");
                             //$("#empty"+$scope.positionIndex).css("background","#33CCFF");
                             $("#empty"+$scope.positionIndex).css("background","#D9D9D9");
                             $scope.basketOperate = "sku";
                             $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                             setTimeout(function () {
                             $("#skuId").focus()
                             }, 200);

                             $scope.skuMissBnt = false;
                             $scope.canNotBnt = false;
                             $scope.damageBnt = false;
                             $scope.checkOrderBnt = true;
                             }*/
                        }
                    }
                },100);
            },function (data) {
                $scope.newbakWindows.close();
                if(data.key=="GOODS_EXIST_IN_LOCATION"){
                    if($scope.positionIndex == 21) {
                        //残品筐里有商品
                        openDamagedWindow(data,info);
                        damageBasket = $scope.newbasketNo;
                        scanDamagedStep = 2;
                    }else if($scope.positionIndex == 22) {
                        openUnScanWindow(data,info);
                        unScanBasketNo = $scope.newbasketNo;
                        scanUnscanStep = 2;
                    }else{
                        openEmptyWindow(data,info);
                        scanInventeorStep = 2;
                    }
                }
                if(data.key == "UNITLOAD_ALREADY_BINDED_TO_STATION"){
                    if($scope.positionIndex == 21) {
                        //残品筐已被绑定
                        openDamagedScanWindow(data,info);
                        scanDamagedStep = 2;
                    }else if($scope.positionIndex == 22) {
                        openUnScanBindWindow(data,info);
                        scanUnscanStep = 2;
                    }else{
                        openEmptyBindWindow(data,info);
                        scanInventeorStep = 2;
                    }
                }
                if(data.key == "NO_SUCH_UNITLOAD"){
                    if($scope.positionIndex == 21) {
                        //无效的残品筐
                        openDamagedNoneWindow(info);
                        scanDamagedStep = 2;
                    }else if($scope.positionIndex == 22) {
                        openUnScanNoneWindow(info);
                        scanUnscanStep = 2;
                    }else{
                        openEmptyNoneWindow(info);
                        scanInventeorStep = 2;
                    }
                }
                if(data.key == "UNITLOAD_IS_LOCKED"){
                    if($scope.positionIndex == 21) {
                        //无效的残品筐
                        openDamagedLockWindow(info);
                        scanDamagedStep = 2;
                    }else if($scope.positionIndex == 22) {
                        openUnScanLockWindow(info);
                        scanUnscanStep = 2;
                    }else{
                        openEmptyLockWindow(info);
                        scanInventeorStep = 2;
                    }
                }
            });
        }
        //扫描新货筐上方的灯的标签
        $scope.scannewBasketDigital  = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var newBasketDigitalName = $scope.newBasketDigitalName;
            $scope.scanDigitalWindows.close();
            pickToToteService.scanDigitalName(newBasketDigitalName,function (data) {
                if($scope.oldPositionLightId == data.digitalId){
                    sendMessage2(data.digitalId);
                    $interval.cancel($scope.clickNewLight);
                    clearLight();
                    if(scanNewStorageStep == 1){
                        //$scope.newBasketStep = "newBasket";
                        if ($scope.positionIndex == 21) {
                            $scope.type = "DAMAGE";
                            var obj1 = {
                                "pickIds": $scope.pickIds,
                                "pickUnitLoadId": $scope.damagedPickUnitLoadId,
                                "itemNo": $scope.skuDamagedNo,
                                "amountPicked": 1,
                                "pickingOrderPositionState":"Damage"
                            };
                            checkDamageAmount(obj1);
                        } else if($scope.positionIndex == 22){
                            $scope.type = "INVENTORY";
                            var obj1 = {
                                "pickIds": $scope.pickIds,
                                "pickUnitLoadId": $scope.canNotScanpickUnitLoadId,
                                "itemNo": "",
                                "amountPicked": 1,
                                "pickingOrderPositionState":"NotScan"
                            };
                            inputNotScanItemAmount(obj1);
                        }
                    }else{
                        $("#canPinId").css("background","#D9D9D9");
                        $("#unScanId").css("background","#D9D9D9");
                        $("#empty"+$scope.positionIndex).css("background","#D9D9D9");
                        clearPodPage();
                        /*if(pageStateStep == "waitPod"){
                         clearPodPage();
                         }else{
                         $("#canPinId").css("background","#D9D9D9");
                         $("#unScanId").css("background","#D9D9D9");
                         //$("#empty"+$scope.positionIndex).css("background","#33CCFF");
                         $("#empty"+$scope.positionIndex).css("background","#D9D9D9");
                         $scope.basketOperate = "sku";
                         $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                         setTimeout(function () {
                         $("#skuId").focus()
                         }, 200);

                         $scope.skuMissBnt = false;
                         $scope.canNotBnt = false;
                         $scope.damageBnt = false;
                         $scope.checkOrderBnt = true;
                         }*/
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalOperate = "newBasketDigital1";
                    $scope.digitalErrorMsg = "扫描的位置标签"+newBasketDigitalName+"不匹配，请重新扫描";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#newBasketDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "newBasketDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#newBasketDigitalId1").focus();});
            });
            $scope.newBasketDigitalName = "";
        }
        //无法扫描商品货筐确定，进入桉灯
        $scope.continueUnScan = function () {
            $scope.reUnScanWindows.close();
            $scope.unScanWindows.close();
            //绑定货筐
            var obj = {
                "pickStationPositionId":canNotScanPickStationPositionId,
                "stationName":$scope.stationNo,
                "storageName":unScanBasketNo,
                "pickOrderId":"",
                "type":"INVENTORY"
            };
            pickToToteService.bindStorage(obj,function (data) {
                $("#unScanId").css("background","#00B050");
                $scope.windowMessage = "请按动无法扫描商品货筐上方暗灯或者扫描货筐上方标签";
                $scope.basketOperate = "scanUnScanSKUBasketDigital";
                setTimeout(function () {
                    $("#unScanSKUBasketDigitalId").focus();
                }, 100);
                $scope.canNotScanpickUnitLoadId = data.pickUnitLoadId;
                //按灯
                $scope.clickCannotScanLight1 = $interval(function () {
                    if(lightResult == 1){
                        lightResult = 0;
                        $interval.cancel($scope.clickCannotScanLight1);
                        $scope.scanDigitalWindows.close();
                        clearLight();
                        if(scanNewStorageStep == 1){
                            scanNewStorageStep = 0;//恢复初始值
                            if(scanUnscanStep == 1){//第一次扫描货框时，进入扫描pod
                                $("#unScanId").css("background","#D9D9D9");
                                $scope.reUnScanWindows.close();
                                checkProStorage($scope.stationNo);
                            }
                            if(scanUnscanStep == 2){
                                var obj1 = {
                                    "pickIds": $scope.pickIds,
                                    "pickUnitLoadId": $scope.canNotScanpickUnitLoadId,
                                    "itemNo": "",
                                    "amountPicked": 1,
                                    "pickingOrderPositionState":"NotScan"
                                };
                                //$scope.scanBasketStep = "newBasket";
                                inputNotScanItemAmount(obj1);
                            }
                        }
                        if(scanNewStorageStep == 0){
                            clearPodPage();
                            $("#unScanId").css("background","#D9D9D9");
                            /* if(pageStateStep == "waitPod"){
                             clearPodPage();
                             }else{
                             $scope.skuNo="";
                             $scope.basketOperate = "sku";
                             $timeout(function () {$("#skuId").focus();})
                             $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                             //  scanNewStorageStep = 1;//恢复初始值

                             $scope.skuMissBnt = false;
                             $scope.canNotBnt = false;
                             $scope.damageBnt = false;
                             $scope.checkOrderBnt = true;

                             pageStateStep = "checkItem";
                             }*/

                        }
                    }
                },100);
            });
        }
        //残品货筐有商品确定，进入桉灯
        $scope.continueCanPin = function(){
            $scope.reScanCanPinWindows.close();
            $scope.canPinWindows.close();
            //绑定货筐
            var obj = {
                "pickStationPositionId":damagedPickStationPositionId,
                "stationName":$scope.stationNo,
                "storageName":damageBasket,
                "pickOrderId":"",
                "type":"DAMAGE"
            };
            pickToToteService.bindStorage(obj,function (data) {
                $("#canPinId").css("background","#00B050");
                $scope.windowMessage = "请按动残品货筐上方暗灯或者扫描货筐上方标签";
                $scope.basketOperate = "DamagedBasketDigital";
                setTimeout(function () {
                    $("#DamagedBasketDigitalId").focus();
                }, 100);
                $scope.damagedPickUnitLoadId = data.pickUnitLoadId;
                $scope.clickDamageLight1 = $interval(function () {
                    if(lightResult == 1){
                        lightResult = 0;
                        $interval.cancel($scope.clickDamageLight1);
                        $scope.scanDigitalWindows.close();
                        clearLight();
                        if(scanNewStorageStep == 1){
                            scanNewStorageStep = 0;//恢复初始值
                            if(scanDamagedStep == 1){//当第一次扫描残品框时，去检查是否绑定无法扫描货框
                                $("#canPinId").css("background","#D9D9D9");
                                checkProStorage($scope.stationNo);
                            }
                            if(scanDamagedStep == 2){//残品框绑定成功后，给残品框放残品时，发现有效期不同等等异常，判断拣货单的数量
                                var obj1 = {
                                    "pickIds": $scope.pickIds,
                                    "pickUnitLoadId": $scope.damagedPickUnitLoadId,
                                    "itemNo": $scope.skuDamagedNo,
                                    "amountPicked": 1,
                                    "pickingOrderPositionState":"Damage"
                                };
                                // $scope.newBasketStep = "newBasket";
                                checkDamageAmount(obj1);
                            }
                        }
                        if(scanNewStorageStep == 0){
                            clearPodPage();
                            $("#canPinId").css("background","#D9D9D9");
                            /*if(pageStateStep == "waitPod"){
                             clearPodPage();
                             }else{
                             $("#canPinId").css("background","#D9D9D9");
                             $scope.skuNo="";
                             $scope.basketOperate = "sku";
                             $timeout(function () {$("#skuId").focus();})
                             $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                             //scanNewStorageStep = 1;//恢复初始值

                             $scope.skuMissBnt = false;
                             $scope.canNotBnt = false;
                             $scope.damageBnt = false;
                             $scope.checkOrderBnt = true;

                             pageStateStep = "checkItem";
                             }*/

                        }
                    }
                },100);
            });
        }
        //残品货筐请求确认时取消，重新扫描货筐
        $scope.cancel = function () {
            $scope.windowMessage = "请重新扫描残品货筐";
            $scope.canPinWindows.close();
            $scope.canPinBasket = "";
            $scope.basketOperate = "scanCanPinBasket";
            setTimeout(function () {
                $("#canPinBasketId").focus();
            }, 200);
        }
        //无法扫描商品货筐请求确认时取消，重新扫描货筐
        $scope.unScanclose = function () {
            $scope.windowMessage = "请重新扫描无法扫描商品货筐";
            $scope.unScanWindows.close();
            $scope.unScanSKUBasket = "";
            $scope.basketOperate = "unScanSKUBasket";
            setTimeout(function () {
                $("#unScanSKUBasketId").focus();
            }, 200);
        }
        //////////////////////////////////////////////////////////////问题处理按钮//////////////////////////////////////
        //商品残损按钮
        $scope.skuDamaged = function () {
            //清除货框显示信息
            $("#empty"+$scope.i).css("background","#D9D9D9");
            $("#empty"+$scope.i).text("");
            $scope.qusetionWindows.close();
            $scope.skuDamagedNo="";
            $scope.basketOperate="skuDamaged";
            $scope.windowMessage="请扫描残损商品";
            setTimeout(function () {
                $("#skuDamagedId").focus();
            },200);
            // $("#canPinId").css("background","#FF0000");
            // sendMessage(damagedDigitallabelId);//打开对应的灯
        }
        //商品丢失
        $scope.skuMiss = function () {
            $scope.scanBinOperate="1";
            $scope.qusetionWindows.close();
            $scope.bin = "";
            $scope.openWindow({
                windowId:"scanHuoWeiId",
                windowName:$scope.scanHuoWeiWindows,
                windowClass:"blueWindow",
                width:700,
                height:300,
                closeable:true,
                activate:function () {$("#huoWeiId").focus();},
            })

        }
        //商品无法扫描
        $scope.canNotScan = function () {
            var obj = {
                pickIds: $scope.pickIds,
                pickUnitLoadId:$scope.canNotScanpickUnitLoadId,
                itemNo: "",
                amountPicked: $scope.pickAmount
            };
            var amount = $scope.pickAmount;
            $scope.qusetionWindows.close();
            $scope.changeBakWindows.close();
            pickToToteService.scanskuDamaged(obj,function (data) {
                $("#empty"+$scope.i).css("background","#D9D9D9");
                $("#empty"+$scope.i).text("");
                $("#canPinId").css("background","#D9D9D9");
                $("#unScanId").css("background","#33CCFF");
                sendMessage(cannotScanDigitallabelId);
                var obj1 = {
                    "pickIds": $scope.pickIds,
                    "pickUnitLoadId": $scope.canNotScanpickUnitLoadId,
                    "itemNo": "",
                    "amountPicked": 1,
                    "pickingOrderPositionState":"NotScan"
                };
                $scope.problemPickUnitLoadId = $scope.canNotScanpickUnitLoadId;
                inputNotScanItemAmount(obj1);
            },function (data) {
                if(data.key == "SAME_ITEM_DIFFERENT_CLIENT"){
                    $scope.newBasketStep = "newBasket";
                    $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                    $scope.BasketFullMessaage="货筐号码： "+data.values[0]+"中存在相同名称不同客户商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
                if(data.key == "SAME_ITEMNAME_DIFFERENT_LOT"){
                    $scope.newBasketStep = "newBasket";
                    $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                    $scope.BasketFullMessaage="货筐号码："+data.values[0]+"中存在相同名称不同有效期商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
            })
        }
        //商品无法扫描的数量输入
        function inputNotScanItemAmount(obj) {
            var amount = $scope.pickAmount;
            if(amount == 1) {//需要拣货数量为1，直接到后台确认拣货
                pickToToteService.confirmDamagedAmount(obj, function (data) {
                    putCanPin($scope.pickAmount,"canNotScan");
                },function (data) {
                    if(data.key == "SAME_ITEM_DIFFERENT_CLIENT"){
                        $scope.newBasketStep = "newBasket";
                        $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                        $scope.BasketFullMessaage="货筐号码： "+data.values[0]+"中存在相同名称不同客户商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                    }
                    if(data.key == "SAME_ITEMNAME_DIFFERENT_LOT"){
                        $scope.newBasketStep = "newBasket";
                        $scope.errorWindow("changeBakId",$scope.changeBakWindows);
                        $scope.BasketFullMessaage="货筐号码： "+data.values[0]+"中存在相同名称不同有效期商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                    }
                })
            }
            if(amount > 1){
                $scope.damageAmount = amount;
                $scope.basketOperate = "amountInput";
                $scope.windowMessage = "请输入无法扫描商品数量";
                $scope.damagedItemInput= "";
                /*$scope.openWindow({
                 windowId:"damagedItemId",
                 windowClass:"myWindow",
                 windowName:$scope.damagedItemAmountWindows,
                 width:700,
                 height:300,
                 title:"请输入无法扫描商品数量",
                 // closeable:false,
                 actions:[],
                 })*/

                $scope.damageAmountWindow("damagedItemId",$scope.damagedItemAmountWindows,"请输入无法扫描商品数量",function(){$("#damagedItemInput").focus()});
            }
        }
        //货筐已满
        $scope.unitLoadFull = function () {
            //获取该站台货筐筐目前的信息
            $scope.qusetionWindows.close();
            //   scanNewStorageStep = 1;
            pickToToteService.getBakInfo($scope.pickPackStationName,"full",function (data) {
                $scope.bakInfo=data.pickingUnitLoadResults;
                //去掉正品貨框的滿框
                /* var unitLoads = [];
                 var dataResults = data.pickingUnitLoadResults;
                 for (var i = 0; i<dataResults.length; i++){
                 if(dataResults[i].type == "INVENTORY" && dataResults[i].positionIndex != 22){
                 continue;
                 }else{
                 unitLoads.push(dataResults[i]);
                 }
                 }
                 $scope.bakInfo = unitLoads;*/
            });
            // $scope.windowMessage="请扫描已满货筐";
            $scope.fullBasket = "";
            $scope.scanFullBakWindow("scanFullId",$scope.scanFullWindows,function(){$("#fullBasketId").focus()},function () {
                inputfocus();
            });
        }
        //报告暗灯
        $scope.clickLight = function () {
            $scope.qusetionWindows.close();
            //获取暗灯列表
            outboundService.getAndonList(function (data) {
                var andonList = [];
                $scope.andonSize = data.length;
                for(var k = 0;k < $scope.andonSize;k++){
                    if(data[k].name == "扫描枪存在问题" || data[k].name == "商品丢失" || data[k].name == "商品需要录入有效期" ){
                        continue;
                    }
                    andonList.push(data[k]);
                }
                var size = andonList.length;
                $scope.div1 = [];
                $scope.div2 = [];
                for(var i = 0;i <size;i++){
                    andonList[i]["no"]=i + 1;
                    if(i < size/2){
                        $scope.div1.push(andonList[i]);
                    }else{
                        $scope.div2.push(andonList[i]);
                    }
                }
                $scope.binNumber = "";
                $scope.openWindow({
                    windowId:"lightMenuId",
                    windowName:$scope.lightWindows,
                    windowClass:"blueWindow",
                    width:800,
                    height:500,
                    closeable:true,
                    title:"请选择暗灯菜单"
                })
            });
        }
        //信息查询
        $scope.infoCheck = function () {
            $scope.userName = $window.localStorage['username'];
            $scope.operationTime = "";
            $scope.totalOperating = "";
            $scope.operationalEfficiency = "";
            $scope.target = "";
            $scope.conclude = "";
            $scope.onAPod = $scope.podName;
            $scope.onAPallet = $scope.locationName;
            $scope.qusetionWindows.close();
            $scope.openWindow({
                windowId:"informationInquiryId",
                windowName:$scope.informationInquiryWindow,
                windowClass:"blueWindow",
                width:700,
                height:350,
                closeable:true,
            });
        }
        //停止工作
        $scope.finishWork = function () {
            $scope.qusetionWindows.close();
            $scope.openWindow({
                windowId: "isFinishId",
                windowName: $scope.isFinishWindows,
                windowClass: "blueWindow",
                width: 700,
                height: 260,
                closeable: true,
                visible: true,
            })
            /*pickToToteService.stopWorking($scope.stationNo,function (data) {
             $scope.qusetionWindows.close();
             $scope.openWindow({
             windowId: "isFinishId",
             windowName: $scope.isFinishWindows,
             windowClass: "blueWindow",
             width: 700,
             height: 260,
             closeable: true,
             visible: true,
             })
             },function (data) {
             $scope.qusetionWindows.close();
             var podNames = data.values[0];
             $scope.podNameInfo = podNames.join();
             /!*for(var i = 0; i< podNames.length; i++){
             $scope.podNameInfo+=podNames[i]+',';
             }*!/
             $scope.openWindow({
             windowId: "isExistPodId",
             windowName: $scope.isExistPodWindows,
             windowClass: "blueWindow",
             width: 700,
             height: 260,
             closeable: true,
             visible: true,
             })
             });*/
        }
        //检查批次是否完成
        $scope.checkUnfinishOrder = function () {
            $scope.qusetionWindows.close();
            $scope.checkOrderWindows.close();
            pickToToteService.getfinishOrder($scope.logicStationId,function (data) {
                if(data != "" && data != undefined){
                    var position = "";
                    var shipmentOrderNo = "";
                    var digitalArr = [];
                    for(var i = 0;i<data.length;i++){
                        position += data[i].positionIndex+",";
                        shipmentOrderNo += data[i].shipmentNo +",";
                        if(data[i].digitalId != "" && data[i].digitalId != null && data[i].digitalId != undefined){
                            sendMessage1(data[i].digitalId)
                        }
                    }
                    $scope.checkFinishOrderMsg = $scope.pickPackStationName+"工作站下的"+shipmentOrderNo+"批次已完成，"+position+"号位置的货框已解满"
                }else {
                    $scope.checkFinishOrderMsg = "订单未完成，请等待。。。";
                }
                $scope.errorWindow("checkOrderId",$scope.checkOrderWindows);
            },function (data) {
                $scope.checkFinishOrderMsg = data.values[0];
                $scope.errorWindow("checkOrderId",$scope.checkOrderWindows);
            });
        }
        ///////////////////////////////////////////////////////////////////////////////////////
        // ///////////////////////
        //报告暗灯按钮点击事件
        $scope.clickAndon = function(andonType) {
            $scope.andonType = andonType;
            if(andonType.name == "货位条码无法扫描"){
                $scope.lightWindows.close();
                if($scope.storageLocationType == "托盘货位"){
                    outboundService.getStorage($scope.pickFromStoragelocation,function (data) {
                        callAnDon($scope.andonType,data.id);
                    })
                }else {
                    $scope.openWindow({
                        windowId: "scanOtherBinId",
                        windowName: $scope.scanOtherBinWindows,
                        windowClass: "blueWindow",
                        width: 800,
                        height: 300,
                        title: "请选择需要扫描的货位位置",
                        closeable: true,
                    })
                }
            }else{
                $scope.lightWindows.setOptions({
                    title:"请扫描货位条码"
                })
                $scope.scanBinOperate = "scanBinNoOperate";
                $timeout(function () {
                    $("#scanBinNumberId").focus();
                })
            }
        }
        //扫描正上方货位按钮
        $scope.scanUpBin = function (){
            clickBinButton = 1;
            $scope.scanOtherLocationOperate = "";
            $scope.scanOtherBinOperate = "scanOtherBinNoOperate";
            $scope.scanOtherBinWindows.setOptions({
                title:"请扫描货位条码"
            })
            $timeout(function () {
                $("#scanOtherBinNumberId").focus();
            })
        }
        //扫描正左方货位按钮
        $scope.scanLeftBin = function () {
            clickBinButton = 2;
            $scope.scanOtherLocationOperate = "";
            $scope.scanOtherBinOperate = "scanOtherBinNoOperate";
            $scope.scanOtherBinWindows.setOptions({
                title:"请扫描货位条码"
            })
            $timeout(function () {
                $("#scanOtherBinNumberId").focus();
            })
        }
        //扫描正下方货位按钮
        $scope.scanDownBin = function () {
            clickBinButton = 3;
            $scope.scanOtherLocationOperate = "";
            $scope.scanOtherBinOperate = "scanOtherBinNoOperate";
            $scope.scanOtherBinWindows.setOptions({
                title:"请扫描货位条码"
            })
            $timeout(function () {
                $("#scanOtherBinNumberId").focus();
            })
        }
        //扫描正右方货位按钮
        $scope.scanBinRight = function () {
            clickBinButton = 4;
            $scope.scanOtherLocationOperate = "";
            $scope.scanOtherBinOperate = "scanOtherBinNoOperate";
            $scope.scanOtherBinWindows.setOptions({
                title:"请扫描货位条码"
            })
            $timeout(function () {
                $("#scanOtherBinNumberId").focus();
            })
        }
        //暗灯1-8，扫描货位上报暗灯
        $scope.scanBinNumber = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var storageLocaltion = $scope.binNumber;
            //获取storageLocaltionId
            outboundService.getStorage(storageLocaltion,function (data) {
                callAnDon($scope.andonType,data.id);
            },function (data) {
                $scope.andonException = data.values[0];
                $scope.binNumber = "";
                $scope.errorWindow("andonId", $scope.andonExceptionWindows);
            })
        }
        //货位无法扫描，扫描其他货位
        $scope.scanOtherBinNumber = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var storageName = $scope.otherBinNumber;
            var level = storageName.substring(9,10);
            var coloum = storageName.substring(10);
            if(clickBinButton == 1){//扫描正上方货位
                level  = String.fromCharCode(storageName.substring(9,10).charCodeAt() - 1);
                storageName = storageName.substring(0,9)+level+storageName.substring(10);
            }
            if(clickBinButton == 2){//扫描正左方货位
                coloum = parseInt(coloum) +  1;
                if(coloum < 9){
                    storageName = storageName.substring(0,10)+"0"+coloum;
                }else {
                    storageName = storageName.substring(0,10)+coloum;
                }
            }
            if(clickBinButton == 3){//扫描正下方货位
                level  = String.fromCharCode(storageName.substring(9,10).charCodeAt() + 1);
                storageName = storageName.substring(0,9)+level+storageName.substring(10);
            }
            if(clickBinButton == 4){//扫描正右方货位
                coloum = parseInt(coloum) -  1;
                if(coloum < 9){
                    storageName = storageName.substring(0,10)+"0"+coloum;
                }else {
                    storageName = storageName.substring(0,10)+coloum;
                }
            }
            outboundService.getStorage(storageName,function (data) {
                callAnDon($scope.andonType,data.id);
                $scope.otherBinNumber = "";
                $scope.scanOtherBinOperate = "";
                $scope.scanOtherBinWindows.close();
            },function (data) {
                if(data.key == "EX_ANDON_MASTER_LOCATION_NAME_NOT_NULL"){
                    $scope.scanOtherLocationOperate = "1";
                    $scope.otherBinNumber = "";
                    $scope.scanOtherBinOperate = "scanOtherBinNoOperate";
                    $scope.scanOtherBinWindows.setOptions({
                        title:"请重新扫描货位条码"
                    })
                    $timeout(function () {
                        $("#scanOtherBinNumberId").focus();
                    })
                }else{
                    $scope.binNumber = "";
                    $scope.andonException = data.values[0];
                    $scope.errorWindow("andonId", $scope.andonExceptionWindows);
                }
            })
        }
        //确认是否结束拣货
        $scope.exit = function () {
            /*  $scope.isFinishWindows.close();
             $scope.openWindow({
             windowId:"isFullId",
             windowName:$scope.isFullAllBakWindows,
             windowClass:"blueWindow",
             width:700,
             height:260,
             closeable:false,
             visible:true,
             })
             pickToToteService.stopWorking($scope.stationNo);//解除用户与工作站绑定*/
            $scope.isFinishWindows.close();
            $scope.qusetionWindows.close();
            pickToToteService.stopWorking($scope.stationNo,function (data) {
                closeWebsocket(podSocket,"pod");
                closeWebsocket(socket,"digital");
                $scope.openWindow({
                    windowId:"isFullId",
                    windowName:$scope.isFullAllBakWindows,
                    windowClass:"blueWindow",
                    width:700,
                    height:260,
                    closeable:false,
                    visible:true,
                })
            },function (data) {
                var podNames = data.values[0];
                $scope.podNameInfo = podNames.join();
                $scope.openWindow({
                    windowId: "isExistPodId",
                    windowName: $scope.isExistPodWindows,
                    windowClass: "blueWindow",
                    width: 700,
                    height: 260,
                    closeable: true,
                    visible: true,
                })
            });

        }
        //确认结满所有货筐
        $scope.fullAll = function () {
            pickToToteService.fullAllStorage($scope.stationNo);
            $scope.isFullAllBakWindows.close();
            $state.go("main.pick");
        }
        $scope.notFullAll = function () {
            $scope.isFullAllBakWindows.close();
            $state.go("main.pick");
        }
        //货位无法扫描
        $scope.canNotScanBin = function () {
            $scope.scanHuoWeiWindows.close();
            $scope.bin = "";
            $scope.openWindow({
                windowId:"scanEachSKUId",
                windowName:$scope.scanEachSKUWindows,
                windowClass:"blueWindow",
                width:700,
                height:400,
                closeable:true,
                activate:function () {$("#eachSKUId").focus();},
            })
            callAnDon({
                name:"货位条码无法扫描",
                id:"5901d872-f295-11e6-8e32-0242ac110014"
            },$scope.fromLocationNameId)

        }
        //货位扫描
        $scope.scanHuoWei = function (e) {
            var keyCode = window.event ? e.keyCode:e.which;
            if(keyCode != 13) return;
            pickToToteService.scanHuoWei($scope.bin,$scope.pickIds,function (data) {
                $scope.scanHuoWeiWindows.close();
                $scope.openWindow({
                    windowId:"scanEachSKUId",
                    windowName:$scope.scanEachSKUWindows,
                    windowClass:"blueWindow",
                    width:700,
                    height:400,
                    closeable:true,
                    activate:function () {$("#eachSKUId").focus();},
                })
            },function (data) {
                if(data.key == "WRONG_STORAGELOCATION"){
                    $scope.bin = "";
                    $scope.scanBinOperate="2";
                }
            })
        }
        //逐一扫描货位里的商品
        $scope.scanEachSKU = function (e) {
            var keyCode = window.event ? e.keyCode:e.which;
            if(keyCode != 13) return;
            var obj = {
                "pickIds": $scope.pickIds,
                "pickUnitLoadId": $scope.pickUnitLoadId,
                "itemNo": $scope.eachSKUNo,
                "amountPicked": ""
            };
            pickToToteService.scanSKU(obj, function (data) {
                $scope.scanEachSKUWindows.close();
                var obj1 = {
                    "pickIds": $scope.pickIds,
                    "pickUnitLoadId": $scope.pickUnitLoadId,
                    "itemNo": $scope.eachSKUNo,
                    "amountPicked":1
                };
                sendMessage($scope.orderDigitalLabelId);//打开放置商品的灯
                console.log("亮灯的位置是："+$scope.i);
                $("#empty"+$scope.i).css("background","#33CCFF");
                $("#empty"+$scope.i).text($scope.pickAmount);
                checkInventoryItemAmount(obj1)
            },function (data) {
                $scope.scanEachSKUWindows.close();
                $scope.scanEverySKUWindows.close();
                $scope.eachSKUNumber = $scope.eachSKUNo;
                $scope.eachSKUNo = "";
                $scope.openWindow({
                    windowId:"scanEverySKUId",
                    windowName:$scope.scanEverySKUWindows,
                    windowClass:"myWindow",
                    width:700,
                    height:400,
                    closeable:true,
                    activate:function () {$("#everySKUId").focus();},
                })
            })
        }
        //货位为空，留个接口，功能和已扫描完所有商品按钮一样
        $scope.positionEmpry = function () {
            $scope.scanEachSKUWindows.close();
            $scope.bin = "";
            //取消该拣货单，跳转下一个拣货任务
            var obj = {
                "pickIds": $scope.pickIds,
                "pickUnitLoadId": "",
                "itemNo": $scope.sku,
                "amountPicked": $scope.pickAmount
            };
            pickToToteService.haveScanedAllSKU(obj,function (data) {
                clearPage();
                checkOrder($scope.podNo,$scope.stationNo);
            });
        }
        //商品扫描错误，继续每一件扫描
        $scope.scanEverySKU = function (e) {
            var keyCode = window.event ? e.keyCode:e.which;
            if(keyCode != 13) return;
            var obj = {
                "pickIds": $scope.pickIds,
                "pickUnitLoadId": $scope.pickUnitLoadId,
                "itemNo": $scope.everySKUNo,
                "amountPicked": ""
            };
            pickToToteService.scanSKU(obj, function (data) {
                $scope.scanEverySKUWindows.close();
                var obj1 = {
                    "pickIds": $scope.pickIds,
                    "pickUnitLoadId": $scope.pickUnitLoadId,
                    "itemNo": $scope.everySKUNo,
                    "amountPicked":1
                };
                sendMessage($scope.orderDigitalLabelId);//打开放置商品的灯
                console.log("亮灯的位置是："+$scope.i);
                $("#empty"+$scope.i).css("background","#33CCFF");
                $("#empty"+$scope.i).text($scope.pickAmount);
                checkInventoryItemAmount(obj1);
            },function (data) {
                $scope.scanEachSKUWindows.close();
                $scope.scanEverySKUWindows.close();
                $scope.eachSKUNumber = $scope.everySKUNo;
                $scope.everySKUNo = "";
                $scope.openWindow({
                    windowId:"scanEverySKUId",
                    windowName:$scope.scanEverySKUWindows,
                    windowClass:"myWindow",
                    width:700,
                    height:400,
                    closeable:true,
                    activate:function () {$("#everySKUId").focus();},
                })
            })
        }
        //已扫描完所有商品
        $scope.haveScanedAllSKU = function () {
            $scope.scanEverySKUWindows.close();
            $scope.eachSKUNo="";
            //取消该拣货单，跳转下一个拣货任务
            var obj = {
                "pickIds": $scope.pickIds,
                "pickUnitLoadId": "",
                "itemNo": $scope.sku,
                "amountPicked": $scope.pickAmount
            };
            pickToToteService.haveScanedAllSKU(obj,function (data) {
                clearPage();
                checkOrder($scope.podNo,$scope.stationNo);
            });
        }
        ///////////////////////////////////////////////////////////////内部函数////////////////////////////////////////////////////
        //检查 2 个问题筐是否绑定
        function checkProStorage(stationName) {
            pickToToteService.checkProStorage(stationName,function (data) {
                if(data.msg == "DAMAGED_PICKUNITLOAD_NOT_EXIST"){
                    damagedPickStationPositionId = data.data.pickStationPositionId;
                    damagedDigitallabelId = data.data.digitalLabelId;
                    $scope.basketOperate = "scanCanPinBasket";
                    $scope.windowMessage = "请扫描残品货筐，并将货筐放于指定位置";
                    $("#canPinId").css("background","red");
                    setTimeout(function () {
                        $("#canPinBasketId").focus();
                    }, 200);
                    //调灯亮的接口
                    sendMessage(damagedDigitallabelId);

                    $scope.skuMissBnt = true;
                    $scope.canNotBnt = true;
                    $scope.damageBnt = true;

                    $scope.refreshBnt = true;

                }
                if(data.msg== "UNSCAN_PICKUNITLOAD_NOT_EXIST"){
                    canNotScanPickStationPositionId = data.data.pickStationPositionId;
                    cannotScanDigitallabelId = data.data.digitalLabelId;
                    $scope.basketOperate = "unScanSKUBasket";
                    $scope.windowMessage = "请扫描无法扫描货筐，并将货筐放于指定位置";
                    $("#unScanId").css("background","#33CCFF");
                    setTimeout(function () {
                        $("#unScanSKUBasketId").focus();
                    }, 200);
                    //调灯亮的接口
                    sendMessage(cannotScanDigitallabelId);

                    $scope.skuMissBnt = true;
                    $scope.canNotBnt = true;
                    $scope.damageBnt = true;

                    $scope.refreshBnt = true;
                }
                if(data.msg == "SUCCESS"){
                    $scope.pickPackMain = "pickPackMain";
                    $scope.messageOperate = {};
                    $scope.pickPackOperate = {};
                    $scope.skuOperate = "skuNo";
                    $scope.basketOperate = "pod";
                    $scope.windowMessage = "等待Pod...";
                    $scope.lotDate = "";
                    // $scope.fullContainerBnt = true;
                    pageStateStep = "waitPod";
                    $scope.skuMissBnt = true;
                    $scope.canNotBnt = true;
                    $scope.checkOrderBnt = false;
                    $scope.damageBnt = true;

                    $scope.refreshBnt = false;


                    if(podValue == 0){
                        getPodResult();
                        podValue = 1;
                    }
                    setTimeout(function () {
                        $("#podId").focus();
                    }, 200);
                }
            })
        }
        //检查batch对应得空拣货货筐是否绑定
        function checkOrder(podName,stationName) {
            pickToToteService.checkBatch(podName,stationName,function (data) {
                    if(data.data.message == "noPod"){
                        clearAllPage();
                    }else if(data.data.message == "nextPod"){
                        //$scope.refreshNewPod();
                        //checkOrder(data.pod,$scope.stationNo);
                        console.log("245callnew"+data.data.socketPodName);
                        callNewPod(data.data.socketPodName)
                    }else{
                        $scope.podNo = data.data.socketPodName;
                        if(data.msg == "PICKUNITLOAD_NOT_EXIST"){
                            $scope.genuinePickStationPositionId = data.data.pickStationPositionId;
                            $scope.index = data.data.positionIndex;
                            //$scope.index = data.data.orderIndex;
                            console.log("绑空拣货框的位置是："+$scope.index);
                            $scope.digitalLabelId = data.data.digitalLabelId;
                            $scope.basketOperate = "emptyBasket";
                            $scope.pickOrderId = data.data.pickOrderId,
                                $scope.windowMessage = "请扫描空拣货货筐，放置在指定位置";
                            $("#empty"+$scope.index).css("background","#33CCFF");
                            setTimeout(function () {
                                $("#emptyBasketId").focus();
                            }, 200);
                            //调灯亮的接口
                            sendMessage($scope.digitalLabelId)
                        }
                        if(data.msg == "SUCCESS"){
                            //获取拣货单信息
                            //var storageName = data.data.storageName;
                            getOrder($scope.podNo,stationName);
                        }
                    }

                }/*,function (data) {//没有拣货单了
                 if(data.key == "EX_NO_PICKINGORDERPOSITION"){
                 //openWindow();
                 clearAllPage();
                 }
                 if (data.key == "EX_CAR_STOP") {
                 $scope.pickingOrderPostion = "carStop";
                 $scope.podMsg = podName;
                 $scope.errorWindow("reScanPodId", $scope.reScanPodWindows);
                 }
                 }*/
            )
            // getOrder(podName,stationName);
        }
        function clearnextPodPage(pod) {
            if($scope.sku != undefined && $scope.sku != "" && $scope.sku != null){
                $("#empty"+$scope.i).text("");
                $("#empty"+$scope.i).css("background","#D9D9D9");
                $scope.sku = "";
                $scope.fromSkuNo = "";
                $scope.lotDate = "";
                $scope.toSkuNo = "";
                $scope.skuName = "";
                $scope.pickAmount = "";
            }
            initPod(pod);
        }

        //获取拣货任务
        function getOrder(podName,stationName) {
            pickToToteService.getOrderPosition(podName,stationName,function (data) {
                pickToToteService.scanPod(podName, function (data2) {
                    // $scope.fullContainerBnt = false;
                    console.log("获取拣货单信息，批次是："+data.pickOrderNo);
                    $scope.podInfo = data2;
                    initPod(data2);
                    $scope.podNo = podName;
                    $scope.podMessage = podName;
                    lightPodCell(data.pickfromLocationName);
                    //设置页面信息
                    $scope.basketOperate = "sku";
                    $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                    pageStateStep = "checkItem";
                    //$scope.stoageLocationName = storageLocationName;
                    $scope.storageLocationType = data.storageLocationType;

                    var lot = data.lotDate ? data.lotDate : "";
                    $scope.lotDate = "有效期："+ lot;

                    $scope.sku = "商品编码: " + data.itemData.itemNo;
                   // var skuNo = data.skuNoMessage;
                    var skuNo = data.itemData.skuNo;
                    $scope.fromSkuNo = "商品条码: " + skuNo.substr(0,skuNo.length-4);
                    $scope.toSkuNo = skuNo.substr(skuNo.length-4);
                    //$scope.skuNo = data.itemData.skuNo;
                    $scope.skuName = "商品名称：" + data.itemData.name;
                    $scope.pickAmount = data.amountRequest;
                    $scope.i = data.positionIndex;
                    //$scope.i = data.orderIndex;
                    $scope.binName = data.pickfromLocationName.substring(9);//记录商品所有货位的位置信息
                    //打开灯
                    $scope.orderDigitalLabelId = data.digitalLabelId;

                    //需要向后台返回的数据
                    $scope.pickIds = data.pickIdList;
                    $scope.pickUnitLoadId = data.pickUnitLoadId;
                    $scope.shipmentId = data.shipmentId;//商品所属的订单
                    //商品出现问题时需要的信息
                    $scope.itemNo = data.itemData.itemNo;
                    $scope.itemDataId = data.itemData.id;
                    $scope.pickFromStoragelocation = data.pickfromLocationName;
                    $scope.fromLocationNameId = data.pickFromStorageLocationId;
                    setTimeout(function () {
                        $("#skuId").focus()
                    }, 200);

                    $scope.skuMissBnt = false;
                    $scope.canNotBnt = false;
                    $scope.damageBnt = false;
                    $scope.checkOrderBnt = true;
                },function (data) {
                    $scope.podException = data.values[0];
                    $scope.podNo = "";
                    $scope.errorWindow("podErrorId",$scope.podErrorWindows);
                })
            },function (data) {
                if (data.key == "EX_NO_PICKINGORDERPOSITION") {
                    clearAllPage();
                }
                if (data.key == "EX_CAR_STOP") {
                    $scope.pickingOrderPostion = "carStop";
                    $scope.podMsg = podName;
                    $scope.errorWindow("reScanPodId", $scope.reScanPodWindows);
                }
            })
        }
        //点亮pod格子
        function lightPodCell(locationName) {
            var changeChar = function (char) {
                return Math.abs(65 - char.charCodeAt() + $scope.size) - 1
            }
            $scope.locationName = locationName;
            $scope.podHierarchy = locationName.substring(9,10); //ABC  层级
            $scope.podSlot = locationName.substring(9);
            $scope.podNumber = locationName.substring(10);//123 排列
            $scope.podHeight = parseInt(changeChar($scope.podHierarchy));
            $scope.podWidth = parseInt($scope.podNumber) - 1;
            $scope.podColor = FLOOR_COLOR[$scope.podHierarchy];
            $scope.podRows[$scope.podHeight].color = $scope.podColor; //行颜色
            $scope.podRows[$scope.podHeight].item[$scope.podWidth].name = locationName.substring(9);
            $scope.podRows[$scope.podHeight].item[$scope.podWidth].choice = true;
        }
        //放商品，拍灯
        function  putSKU(data) {
            //按灯操作
            $scope.windowMessage = "请将商品放入指定货筐，并按动货筐上方暗灯或者扫描货筐上方标签";
            $scope.comfirmAmountBnt = true;
            $("#empty"+$scope.i).css("background","green");
            $scope.basketOperate = "scanSKUDigital";
            $scope.batchName = data.pickOrderNo;
            setTimeout(function () {
                $("#scanSKUDigitalId").focus();
            }, 100);
            //按钮不可用
            $scope.problemBnt = true;
            //  $scope.fullContainerBnt = true;
            //$scope.refreshBnt = true;

            //放车
            //getPickOrderDetail();
            pickToToteService.checkBatch($scope.podNo,$scope.stationNo, function (data) {
                if(data.data.message == "noPod"){
                    $scope.podDetail = "noPod";
                    /*  if(podSocket != undefined && podSocket != null && podSocket != ""){
                     podSocket.close(3666,"主动退出");
                     }*/
                    closeWebsocket(podSocket,"pod");
                }else {
                    $scope.nextPod = data.data.socketPodName;
                }
                $scope.clickOrderLight = $interval(function () {
                    if(lightResult == 1){
                        lightResult = 0;
                        $scope.scanDigitalWindows.close();
                        $interval.cancel($scope.clickOrderLight);
                        clearLight();
                        $scope.comfirmAmountBnt = false;
                        $scope.problemBnt = false;
                        //  $scope.fullContainerBnt = false;
                        if($scope.batchState == "FINISH"){
                            $scope.finishOrdermessage = $scope.batchName+"订单拣货完成，已解绑"+$scope.i+"位置货框,请按灯后移走货筐";
                            $scope.finishOrderPod = data.data.socketPodName;
                            sendMessage1($scope.orderDigitalLabelId);
                            $scope.errorWindow("finishOrderId",$scope.finishOrderWindows);
                        }else{
                            clearPage();
                            console.log("345567callnew"+data.data.socketPodName);
                            getlightOffPosition(data.data.socketPodName);
                        }
                    }
                },100);
            })
        }

        $scope.getPagePosition = function() {
            $scope.finishOrderWindows.close();
            clearAllPage();
            console.log("245callnew"+$scope.finishOrderPod);
            getlightOffPosition($scope.finishOrderPod);
        }

        function  getPickOrderDetail() {
            //获取取货单信息
            pickToToteService.checkBatch($scope.podNo,$scope.stationNo, function (data) {
                if(data.data.message == "noPod"){
                    $scope.podDetail = "noPod";
                    /*  if(podSocket != undefined && podSocket != null && podSocket != ""){
                     podSocket.close(3666,"主动退出");
                     }*/
                    closeWebsocket(podSocket,"pod");
                }

            })
        }
        function getlightOffPosition(pod) {
            if($scope.podDetail == "noPod"){  //若后没有pod了，则清除页面
                $scope.podDetail = "";
                // $scope.fullContainerBnt = true;
                clearAllPage();
                getPodResult();
            }else{
                $scope.podDetail = "";
                //$scope.refreshNewPod();
                console.log("123释放"+pod);
                callNewPod(pod)
            }
            $scope.nextPodMsg = "";
            $scope.nextPod = "";
        }



        //放商品时扫描正品框上方的灯的标签
        $scope.scanSKUDigital  = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var skuDigitalName = $scope.skuDigitalName;
            $scope.scanDigitalWindows.close();
            pickToToteService.scanDigitalName(skuDigitalName,function (data) {
                if($scope.orderDigitalLabelId == data.digitalId){
                    sendMessage2(data.digitalId);
                    clearLight();
                    var state = $scope.batchState;
                    $scope.comfirmAmountBnt = false;
                    $scope.problemBnt = false;
                    //  $scope.fullContainerBnt = false;
                    if($scope.batchState == "FINISH"){
                        $scope.finishOrdermessage = $scope.batchName+"订单拣货完成，已解绑"+$scope.i+"位置货框，请按灯后移走货筐";
                        sendMessage1($scope.orderDigitalLabelId);
                        $scope.errorWindow("finishOrderId",$scope.finishOrderWindows)
                    }else{
                        clearPage();
                        console.log("24234sgg5callnew"+$scope.nextPod);
                        getlightOffPosition($scope.nextPod);
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalOperate = "scanSKUDigital1";
                    $scope.digitalErrorMsg = "扫描的位置标签"+skuDigitalName+"不匹配，请重新扫描";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#scanSKUDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "scanSKUDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#scanSKUDigitalId1").focus();});
            });
            $scope.skuDigitalName = "";
        }
        //提示结满货框的弹窗确定
        /*  $scope.finishPickOrder = function () {
         $scope.finishBatchWindows.close();
         clearPage();
         checkOrder($scope.podNo,$scope.stationNo);
         }*/
        //清理页面信息
        function clearPage() {
            $("#empty"+$scope.i).css("background","#D9D9D9");
            $("#empty"+$scope.i).text("");
            $scope.skuOperate = "skuNo";
            $scope.pickAmount="";
            $scope.sku = "";
            $scope.skuName = "";
            $scope.fromSkuNo = "";
            $scope.lotDate = "";
            $scope.toSkuNo = "";
            $scope.pickNoInput = "";
            $scope.skuNo = "";

            initPod($scope.podInfo);//清理pod

            $("#canPinId").css("background","#D9D9D9");
            $("#unScanId").css("background","#D9D9D9");
            $("#canPinId").text("");
            $("#unScanId").text("");
            $scope.damagedItemInput = "";
        }
        //初始化pod
        function initPod(data) {
            /* $scope.podRows = [], $scope.podColumns = [];
             $scope.size = data.levels.length;
             $scope.heightPod = ($scope.podheight -10*($scope.size))/$scope.size;//每层高度
             $scope.widthPod = Math.min.apply(null,data.columns);//取列集合中最小的列数
             var column = data.columns;
             for (var j=0;j<$scope.size;j++){
             for (var i = 0; i < column[j];i++) $scope.podColumns.push({
             name: "",
             choice: false
             });
             $scope.podRows[j] = angular.copy({
             item: $scope.podColumns,
             color: "#c1c1c1"
             });
             $scope.podColumns = [];
             }*/
            if(data == "" || data == null || data == undefined){
                $scope.podRows = [];
                $scope.podMessage = "POD";
            }else{
                $scope.podMessage = $scope.podNo;
                var podHeightTotal = 0;
                var perPodHeight = 0;
                var podHeightArr = [];
                $scope.heightPod = [];
                var podTypePositions = data.podTypePositionDTO;
                for(var a = 0;a<podTypePositions.length;a++){
                    podHeightTotal += podTypePositions[a].storageLocationType.height;
                }
                for(var b = 0;b<podTypePositions.length;b++){
                    perPodHeight = podTypePositions[b].storageLocationType.height/podHeightTotal;
                    podHeightArr.push(perPodHeight);
                }
                $scope.podRows = [], $scope.podColumns = [];
                $scope.size = data.levels.length;//行数
                $scope.widthPod = Math.min.apply(null, data.columns);//取列集合中最小的列数
                $scope.podColumn = Math.max.apply(null, data.columns);//取列集合中最大的列数
                if($scope.podColumn == 1){
                    $scope.podDivWidht = 80;
                }else{
                    $scope.podDivWidht = 90;
                }
                //$scope.heightPod = ($scope.podheight - 10 * ($scope.size)) / $scope.size;//每层高度
                $scope.heightPodSum = $scope.podheight - 10 * ($scope.size);//pod的总的显示区域
                for(var c = 0;c<podHeightArr.length;c++){
                    var height = $scope.heightPodSum * podHeightArr[c];
                    $scope.heightPod.push(height);
                }
                var column = data.columns;
                for (var j = 0; j < $scope.size; j++) {
                    for (var i = 0; i < column[j]; i++) $scope.podColumns.push({
                        name: "",
                        choice: false
                    });
                    $scope.podRows[j] = angular.copy({
                        item: $scope.podColumns,
                        color: "#c1c1c1",
                        h:$scope.heightPod[j]
                    });
                    $scope.podColumns = [];
                }
            }
        }
        //残品货筐扫描报错时弹窗
        function openDamagedWindow(data,canPinBasket) {
            //残品筐里有商品
            $scope.windowMessage = "请确认是否使用当前残品货筐";
            $scope.damagedAmount = data.values[0];
            $scope.canPinBasketNo = canPinBasket;
            $scope.errorWindow("canPinBakId",$scope.canPinWindows);
        }
        function openDamagedScanWindow(data,canPinBasket){
            //残品筐已被绑定
            $scope.basketOperate="scanCanPinBasket";
            $scope.windowMessage = "请重新扫描残品货筐";
            $scope.stationName=data.values[0];
            $scope.canPinBasketNo = canPinBasket;
            $scope.canPinBasket="";
            $scope.errorWindow("recanPinBakId",$scope.reScanCanPinWindows,function () {
                $("#recanPinBasketId").focus();
            });
            $scope.reScanCanPinOperate = "yiBangDing";
        }
        function openDamagedNoneWindow(canPinBasket) {
            //无效的残品筐
            $scope.basketOperate="scanCanPinBasket";
            $scope.windowMessage = "请重新扫描残品货筐";
            $scope.canPinBasketNo = canPinBasket;
            $scope.canPinBasket = "";
            $scope.errorWindow("recanPinBakId",$scope.reScanCanPinWindows,function () {
                $("#recanPinBasketId").focus();
            });
            $scope.reScanCanPinOperate = "wuXiao";
        }
        function openDamagedLockWindow(canPinBasket) {
            $scope.basketOperate="scanCanPinBasket";
            $scope.windowMessage = "请重新扫描残品货筐";
            $scope.canPinBasketNo = canPinBasket;
            $scope.canPinBasket = "";
            $scope.errorWindow("recanPinBakId",$scope.reScanCanPinWindows,function () {
                $("#recanPinBasketId").focus();
            });
            $scope.reScanCanPinOperate = "locked";
        }
        //无法扫描货筐扫描报错时弹窗
        function openUnScanWindow(data,unScanBasket) {
            //无法扫描商品货筐里有商品
            $scope.unScanSKUBasketNo = unScanBasket;
            $scope.unScanSkuNo = data.values[0];
            $scope.windowMessage = "请确认是否使用当前无法扫描商品货筐";
            $scope.errorWindow("unScanBakId", $scope.unScanWindows);
        }
        function openUnScanBindWindow(data,unScanSKUBasket) {
            $scope.basketOperate = "unScanSKUBasket";
            $scope.windowMessage = "请重新扫描无法扫描商品货筐";
            $scope.unScanSKUBasketNo = unScanSKUBasket;
            $scope.unScanSKUBasket = "";
            $scope.stationName = data.values[0];
            $scope.errorWindow("reUnScanBakId", $scope.reUnScanWindows,function () {
                $("#reunScanSKUBasketId").focus();
            });
            $scope.reUnScanOperate = "yiBangDing";
        }
        function openUnScanNoneWindow(unScanSKUBasket) {
            $scope.basketOperate = "unScanSKUBasket";
            $scope.windowMessage = "请重新扫描无法扫描商品货筐";
            $scope.unScanSKUBasketNo = unScanSKUBasket;
            $scope.unScanSKUBasket = "";
            $scope.errorWindow("reUnScanBakId", $scope.reUnScanWindows,function () {
                $("#reunScanSKUBasketId").focus();
            });
            $scope.reUnScanOperate = "wuXiao";
        }
        function openUnScanLockWindow(unScanSKUBasket) {
            $scope.basketOperate = "unScanSKUBasket";
            $scope.windowMessage = "请重新扫描无法扫描商品货筐";
            $scope.unScanSKUBasketNo = unScanSKUBasket;
            $scope.unScanSKUBasket = "";
            $scope.errorWindow("reUnScanBakId", $scope.reUnScanWindows,function () {
                $("#reunScanSKUBasketId").focus();
            });
            $scope.reUnScanOperate = "locked";
        }
        //空拣货货筐扫描报错时弹窗
        function openEmptyWindow(data,empty) {
            $scope.basketOperate = "emptyBasket";
            $scope.emptySkuNo = data.values[0];
            $scope.windowMessage = "请重新扫描空拣货货筐";
            $scope.emptyBasketNo = empty;
            $scope.emptyBasket = "";
            $scope.errorWindow("emptyBakId", $scope.emptyWindows, function () {
                $("#emptyStorageId").focus();
            });
            $scope.emptyOperate = "existSKU";
        }
        function openEmptyBindWindow(data,empty) {
            $scope.basketOperate = "emptyBasket";
            $scope.windowMessage = "请重新扫描空拣货货筐";
            $scope.stationName = data.values[0];
            $scope.emptyBasketNo = empty;
            $scope.emptyBasket = "";
            $scope.errorWindow("emptyBakId", $scope.emptyWindows, function () {
                $("#emptyStorageId").focus();
            });
            $scope.emptyOperate = "yiBangDing";
        }
        function openEmptyNoneWindow(empty) {
            $scope.basketOperate = "emptyBasket";
            $scope.windowMessage = "请重新扫描空拣货货筐";
            $scope.emptyBasketNo = empty;
            $scope.emptyBasket = "";
            $scope.errorWindow("emptyBakId", $scope.emptyWindows, function () {
                $("#emptyStorageId").focus();
            });
            $scope.emptyOperate = "wuXiao";
        }
        function openEmptyBasketTypeWindow(data) {
            $scope.basketOperate = "emptyBasket";
            $scope.windowMessage = "请重新扫描空拣货货筐";
            $scope.emptyBasketType = data;
            $scope.emptyBasket = "";
            $scope.errorWindow("emptyBakId", $scope.emptyWindows, function () {
                $("#emptyStorageId").focus();
            });
            $scope.emptyOperate = "noMatch";
        }
        function openEmptyLockWindow(empty) {
            $scope.basketOperate = "emptyBasket";
            $scope.windowMessage = "请重新扫描空拣货货筐";
            $scope.emptyBasketNo = empty;
            $scope.emptyBasket = "";
            $scope.errorWindow("emptyBakId", $scope.emptyWindows, function () {
                $("#emptyStorageId").focus();
            });
            $scope.emptyOperate = "locked";
        }
        //pod中没有拣货单时弹窗
        function openWindow() {
            $scope.podName = $scope.podName;
            $scope.errorWindow("reScanPodId",$scope.reScanPodWindows);

        }
        //pod中没有拣货单时的确定按钮，跳转到pod重新扫描
        $scope.continueScanPod = function () {
            $scope.reScanPodWindows.close();
            initMainPage();
            $scope.podRows = [];//清除pod的样子
            $scope.podNo = "";
            $("#empty"+$scope.i).text("");
            $("#empty"+$scope.i).css("background","#D9D9D9");
            $scope.skuNo="";
            $scope.sku="";
            $scope.fromSkuNo = "";
            $scope.lotDate = "";
            $scope.toSkuNo = "";
            $scope.skuName="";
            $scope.pickNoInput="";
            $scope.pickAmount = "";
            $scope.windowMessage = "等待Pod...";
            $scope.lotDate = "";
            pageStateStep = "waitPod";
            $scope.skuMissBnt = true;
            $scope.canNotBnt = true;
            $scope.damageBnt = true;
            $scope.checkOrderBnt = false;


            // $scope.fullContainerBnt = true;
            if(podValue == 0){
                getPodResult();
                podValue = 1;
            }
            $scope.basketOperate = "pod";
            setTimeout(function () {
                $("#podId").focus();
            }, 200);
        }
        // 错误弹窗
        $scope.errorWindow = function (windowId, windowName,focusInput) {
            $("#" + windowId).parent().addClass("myWindow");
            windowName.setOptions({
                width: 700,
                height: 260,
                visible: false,
                closeable:true,
                //actions: false,
                activate:focusInput,
            });
            windowName.center();
            windowName.open();
        }

        $scope.damageAmountWindow = function (windowId, windowName,title,focusInput) {
            $("#" + windowId).parent().addClass("myWindow");
            windowName.setOptions({
                width: 700,
                height: 400,
                closeable:false,
                actions:[],
                visible: false,
                activate:focusInput,
                title:title
            });
            windowName.center();
            windowName.open();
        }
        // 选择拣货模式弹窗弹窗
        $scope.chooseTypeWindow = function (windowId, windowName) {
            $("#" + windowId).parent().addClass("myWindow");
            windowName.setOptions({
                width: 900,
                height: 400,
                visible: false,
                closeable:true,
                //actions: false,
            });
            windowName.center();
            windowName.open();
        }
        // 扫描已满货筐弹窗
        $scope.scanFullBakWindow = function (windowId, windowName,focusInput,closefunction) {
            $("#" + windowId).parent().addClass("blueWindow");
            windowName.setOptions({
                width: 700,
                height: 400,
                closeable:false,
                // actions:[],
                visible: false,
                activate:focusInput,
            });
            windowName.center();
            windowName.open();
        }
        //扫描序列号弹窗
        $scope.seriesWindow = function (windowId, windowName,focusInput) {
            $("#"+windowId).parent().addClass("myWindow");
            windowName .setOptions({
                width: 700,
                height: 260,
                visible: false,
                closeable:true,
                activate:focusInput,
            });
            windowName.center();
            windowName.open();
        }
        //问题处理弹窗
        $scope.problemShow = function () {
            $("#qusetionMenuId").parent().addClass("blueWindow");
            $scope.qusetionWindows.setOptions({
                width: 700,
                height: 500,
                closeable:true,
                visible: false,
            });
            $scope.qusetionWindows.center();
            $scope.qusetionWindows.open();
        }
        //打开窗口
        $scope.openWindow = function(options){
            $("#" + options.windowId).parent().addClass(options.windowClass);
            options.windowName.setOptions({
                width:options.width,
                height:options.height,
                closeable:options.closeable,
                visible:true,
                title:options.title,
                activate:options.activate,

            });
            options.windowName.center();
            options.windowName.open();
        }
        //放残品
        function putCanPin(amount,type) {
            if($scope.newBasketStep != "newBasket"){
                if(type == "damage"){
                    $scope.windowMessage = "请将商品放置到残品货筐中，并按动残品货筐上方暗灯或者扫描货筐上方标签";
                    $("#canPinId").css("background","#FF0000");
                    $("#canPinId").text(amount);
                }else{
                    $scope.windowMessage = "请将商品放置到无法扫描货筐中，并按动无法扫描货筐上方暗灯或者扫描货筐上方标签";
                    $("#unScanId").css("background","#33CCFF");
                    $("#unScanId").text(amount);
                }
            }

            $scope.basketOperate = "scanProblemSKUDigital";
            $scope.comfirmDamageAmountBnt = true;
            setTimeout(function () {
                $("#scanProblemSKUDigitalId").focus();
            }, 100);
            $scope.problemBnt = true;
            //  $scope.fullContainerBnt = true;
            //$scope.refreshBnt = true;
            //getPickOrderDetail();
            pickToToteService.checkBatch($scope.podNo,$scope.stationNo, function (data) {
                if(data.data.message == "noPod"){
                    $scope.podDetail = "noPod";
                    closeWebsocket(podSocket,"pod");
                }else{
                    $scope.nextPodMsg = data.data.socketPodName;
                }
                // //按灯操作
                if($scope.newBasketStep == "newBasket"){
                    $scope.newBasketStep = "";
                    lightExceptionDigital(data.data.socketPodName);
                }else{
                    $scope.clickCanPinLight = $interval(function () {
                        if(lightResult == 1){
                            lightResult = 0;
                            $scope.scanDigitalWindows.close();
                            $interval.cancel($scope.clickCanPinLight);
                            clearLight();
                            lightExceptionDigital(data.data.socketPodName);
                        }
                    },100);
                }
            })
        }
        function lightExceptionDigital(pod) {
            clearPage();
            $scope.problemBnt = false;
            // $scope.fullContainerBnt = false;
            //$scope.refreshBnt = false;
            $scope.comfirmDamageAmountBnt = false;
            $("#canPinId").css("background","#D9D9D9");
            $("#unScanId").css("background","#D9D9D9");
            $("#canPinId").text("");
            $("#unScanId").text("");
            $scope.damagedItemInput = "";
            console.log("242345callnew"+pod);
            getlightOffPosition(pod);

            //checkOrder($scope.podNo,$scope.stationNo);

        }
        //放问题商品时扫描正品框上方的灯的标签
        $scope.scanProblemSKUDigital  = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var ProblemSkuDigitalName = $scope.ProblemSkuDigitalName;
            $scope.scanDigitalWindows.close();
            pickToToteService.scanDigitalName(ProblemSkuDigitalName,function (data) {
                if(cannotScanDigitallabelId == data.digitalId || damagedDigitallabelId == data.digitalId){
                    sendMessage2(data.digitalId);
                    clearLight();
                    lightExceptionDigital($scope.nextPodMsg);
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalOperate = "scanProblemSKUDigital1";
                    $scope.digitalErrorMsg = "扫描的位置标签"+ProblemSkuDigitalName+"不匹配，请重新扫描";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#scanProblemSKUDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "scanProblemSKUDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#scanProblemSKUDigitalId1").focus();});
            });
            $scope.ProblemSkuDigitalName = "";
        }
        //报告暗灯
        function callAnDon(data,storageLocaltion){
            var anDonData = {
                "storageLocationId":storageLocaltion,
                "problemName":data.name,
                "anDonMasterTypeId":data.id,
                "reportBy":$window.localStorage['username'],
                "state":"undisposed",
                "clientId":$window.localStorage["clientId"],
                "warehouseId":$window.localStorage["warehouseId"]
            }
            outboundService.callAnDon(anDonData,function (data) {
                $scope.binNumber = "";
                $scope.scanBinOperate = "";
                $scope.lightWindows.close();
            });
        }
        //outbound问题处理
        function callProblem(data) {
            $scope.problemData = {
                "problemType":data.problemType,
                "amount":data.mount,
                "jobType":"Pick",
                "reportBy":$window.localStorage['username'],
                "reportDate":data.reportDate,
                "problemStoragelocation":$scope.problemStoragelocation,
                "container":"",
                "lotNo":$scope.lotNo,
                "serialNo":$scope.serialNo,
                "skuNo":$scope.skuNo,
                "itemNo":$scope.itemNo,
                "itemDataId":$scope.itemDataId,
                "shipmentId":$scope.shipmentId
            }
            pickToPackService.callProblem($scope.problemData);
        }
        // 初始化页面
        function initPage() {
            $rootScope.pickPackContinue = false;
            $rootScope.pickPackStation = {};
            $rootScope.pickPackMain = {};
            $rootScope.pickpackcar = {};
            $scope.pickPackOperate = "scanStation"; // 初始扫描工作站
            setTimeout(function () {
                $("#pickPack_station").focus();
            }, 200);
        }
        //初始化主页面
        function initMainPage() {
            $scope.pickPackMain = "pickPackMain";
            $scope.messageOperate = {};
            $scope.pickPackOperate = {};
            $scope.skuOperate = "skuNo";
            $scope.podMessage = "POD";
            stop_PickOrder();
            getIsCallPod();
        }


        function getIsCallPod() {
            outboundService.getCallPod($scope.workStationId,function (data) {
                if(data.isCallPod){
                    $scope.butPod = "停止呼叫Pod";
                    $scope.butcolor = "red";
                }else {
                    $scope.butPod = "恢复呼叫Pod";
                    $scope.butcolor = "green";
                }
            });
        }
        $scope.assignPod = function () {
            if($scope.butPod === "停止呼叫Pod"){
                $scope.podStateMsg = "确认停止呼叫货架后，不再给此工作站分配货架";
                $scope.errorWindow("assginPodWinId",$scope.assginPodWindows);
            }else {
                outboundService.callPod("start",$scope.workStationId,function (data) {
                    $scope.butPod = "停止呼叫Pod";
                    $scope.butcolor = "red";
                })
            }
        }

        $scope.confirmAssginPod = function () {
            $scope.assginPodWindows.close();
            if($scope.butPod === "停止呼叫Pod"){
                outboundService.callPod("stop",$scope.workStationId,function (data) {
                    $scope.butPod = "恢复呼叫Pod";
                    $scope.butcolor = "green";
                })
            }

        }


        $scope.confirmAssginPickOrder = function () {
            $scope.assginPickOrderWindows.close();
            if($scope.stopOrderBnt === "停止分配批次"){
                outboundService.stopPickOrder($scope.logicStationId,"stop",function (data) {
                    $scope.stopOrderBnt = "恢复分配批次";
                    $scope.stopBntcolor = "green";
                })
            }
        }

        $scope.stopPickOrder = function () {
            if($scope.stopOrderBnt === "停止分配批次"){
                /*outboundService.stopPickOrder($scope.logicStationId,"stop",function (data) {
                 $scope.stopOrderBnt = "恢复分配批次";
                 $scope.stopBntcolor = "green";
                 })*/
                $scope.pickOrderStateMsg = "确认停止分配批次后，不再给此工作站分配拣货任务";
                $scope.errorWindow("assginPickOrderWinId",$scope.assginPickOrderWindows);
            }else {
                outboundService.stopPickOrder($scope.logicStationId,"start",function (data) {
                    $scope.stopOrderBnt = "停止分配批次";
                    $scope.stopBntcolor = "red";
                })
            }
        }

        function stop_PickOrder() {
            outboundService.getCallPickOrder($scope.stationNo,function (data) {
                if(data.isCallPickOrder){
                    $scope.stopBntcolor = "red";
                    $scope.stopOrderBnt = "停止分配批次";
                }else{
                    $scope.stopBntcolor = "green";
                    $scope.stopOrderBnt = "恢复分配批次";
                }
            })
        }
        //websocket 推送pod的结果
        /*       function getPodResult() {
         //var url = OUTBOUND_CONSTANT.podWebSocket+$scope.workStationId;
         var url = BACKEND_CONFIG.websocket+"websocket/getPod/"+$scope.workStationId;
         console.log("url:",url);
         podSocket = new WebSocket(url);
         //打开事件
         podSocket.onopen = function () {
         console.log("podSocket 已打开");
         };
         //获得消息事件
         podSocket.onmessage = function (msg) {
         var data = JSON.parse(msg.data);
         console.log("推送pod的信息：",data);
         if(data.pod != "success"){
         if (data.workstation == $scope.workStationId) {
         callNewPod(data);
         }
         }
         };
         //关闭事件
         podSocket.onclose = function () {
         console.log("podSocket 关闭");
         /!*if(podSocket.readyState != 1){
         podSocket = new WebSocket(url);
         if(podSocket.readyState != 1){
         $scope.errorWindow("hardwareId1",$scope.hardwareWindows1);
         }
         }*!/
         };
         //发生了错误事件
         podSocket.onerror = function () {
         console.log("podSocket 发生了错误");
         podSocket = new WebSocket(url);
         }
         }*/
        /*function callNewPod(data) {
         console.log("dataPod****:",data);
         if(data.pod != "" && data.pod != null && data.pod != undefined){//当获取到pod信息时，进行下一步，获取拣货单
         //$scope.podShow = "";
         $scope.podName = data.pod;
         $scope.podNo = data.pod;
         pickToToteService.scanPod(data.pod, function (data1) {
         $scope.podInfo = data1;
         initPod(data1);//初始化pod
         checkOrder(data.pod,$scope.stationNo);
         });
         //getOrder(data.pod,$scope.stationNo);//获取取货单信息

         /!* $scope.basketOperate = "sku";
         $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
         setTimeout(function () {
         $("#skuId").focus()
         }, 200);*!/
         }else{//没有获取到pod信息时，初始化界面，继续获取pod
         clearAllPage();
         }
         }*/

        function callNewPod(pod) {
            console.log("dataPod****:",pod);
            if(pod != "" && pod != null && pod != undefined){//当获取到pod信息时，进行下一步，获取拣货单
                //$scope.podShow = "";
                $scope.podName = pod;
                $scope.podNo = pod;
                pickToToteService.scanPod(pod, function (data1) {
                    $scope.podInfo = data1;
                    initPod(data1);//初始化pod
                    checkOrder(pod,$scope.stationNo);
                },function (data) {
                    $scope.podException = data.values[0];
                    $scope.podNo = "";
                    $scope.errorWindow("podErrorId",$scope.podErrorWindows);
                });
                //getOrder(data.pod,$scope.stationNo);//获取取货单信息

                /* $scope.basketOperate = "sku";
                 $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                 setTimeout(function () {
                 $("#skuId").focus()
                 }, 200);*/
            }else{//没有获取到pod信息时，初始化界面，继续获取pod
                clearAllPage();
            }
        }
        //释放pod
        $scope.reservePod = function () {
            outboundService.reservePod($scope.podNo,$scope.sectionId,"false",$scope.workStationId,$scope.logicStationId,function (data) {
                callNewPod(data.pod);
            })
        }
        //刷新重新获取pod
        $scope.refreshNewPod = function () {
            outboundService.refreshPod($scope.sectionId,$scope.workStationId,function (data) {
                clearLight();
                console.log($scope.sectionId,$scope.workStationId);
                console.log("刷新获取pod SUCCESS");
                clearAllPage();
                callNewPod(data.pod);
            },function (data) {
                console.log("刷新获取pod error");
            })
        }

        function clearAllPage(){
            initMainPage();
            $scope.podNo = "";
            $scope.lotDate = "";
            $("#empty"+$scope.i).text("");
            $("#empty"+$scope.i).css("background","#D9D9D9");
            $("#canPinId").css("background","#D9D9D9");
            $("#unScanId").css("background","#D9D9D9");
            $("#canPinId").text("");
            $("#unScanId").text("");
            $scope.skuNo="";
            $scope.sku="";
            $scope.fromSkuNo = "";
            $scope.toSkuNo = "";
            $scope.skuName="";
            $scope.pickNoInput="";
            $scope.pickAmount = "";
            $scope.basketOperate = "pod";
            $scope.windowMessage = "等待Pod...";
            pageStateStep = "waitPod";
            $scope.checkOrderBnt = false;
            $scope.skuMissBnt = true;
            $scope.canNotBnt = true;
            $scope.damageBnt = true;

            $scope.podRows = [];
            if(podValue == 0){
                getPodResult();
                podValue = 1;
            }
            setTimeout(function () {
                $("#podId").focus();
            }, 200);
        }

        //websocket 获取按灯后的结果
        /*  function getLightResult() {
         //var url = OUTBOUND_CONSTANT.webSocket+$scope.workStationId;
         var url = BACKEND_CONFIG.websocket+"websocket/ws/"+$scope.workStationId;
         console.log("url:",url);
         socket = new WebSocket(url);
         //打开事件
         socket.onopen = function () {
         console.log("Socket 已打开");
         };
         //获得消息事件
         socket.onmessage = function (msg) {
         var data = JSON.parse(msg.data);
         console.log("拍灯返回信息：",data);
         if (data.cmd == "1") {
         lightResult = 1;
         }
         };
         //关闭事件
         socket.onclose = function () {
         console.log("Socket已关闭");
         /!*if(socket.readyState != 1){
         socket = new WebSocket(url);
         if(socket.readyState != 1){
         $scope.errorWindow("hardwareId",$scope.hardwareWindows);
         }
         }*!/
         };
         //发生了错误事件
         socket.onerror = function () {
         console.log("发生了错误");
         socket = new WebSocket(url);
         }
         }*/
        function getLightResult() {
            var option={
                "user":$scope.workStationId,
                "url":"websocket/ws/"+$scope.workStationId,
                "onmessageCall":onmessageCall
            }
            socket=webSocketService.initSocket(option);
        }
        function onmessageCall(msg){
            //接收到消息后做的业务处理代码
            if(msg != "Success") {
                console.log(date.toLocaleString()+"->拍灯返回信息：",msg);
                var data = JSON.parse(msg);
                if (data.cmd == "1") {
                    lightResult = 1;
                }
            }
        };

        //websocket 推送pod的结果
        function getPodResult() {
            var option={
                "user":$scope.workStationId,
                "url":"websocket/getPod/"+$scope.workStationId,
                "onmessageCall":onmessagePod
            }
            podSocket=webSocketService.initSocket(option);
        }
        function onmessagePod(msg){
            //接收到消息后做的业务处理代码
            console.log("podSocket 正在推送消息。。。");
            var data = JSON.parse(msg);
            if(data.pod != "success"){
                if (data.workstation == $scope.workStationId) {
                    console.log("websocket推送pod的信息：",data);
                    callNewPod(data.pod);
                }
            }
        };
        //关闭websocket
        function closeWebsocket(s,msg){
            if(!($.isEmptyObject(s))){
                if(msg == "digital"){
                    console.log(date.toLocaleString()+"->拍灯客户端主动关闭websocket连接");
                }
                if(msg == "pod"){
                    console.log(date.toLocaleString()+"->Pod客户端主动关闭websocket连接");
                }
                s.close(3666,"客户端主动关闭websocket连接");
            }
        };
        //websocket发送亮灯命令
        function sendMessage(data) {
            var obj =  {"url": "/light/onOff", "labelId": data, "onOffFlag": "true","shootLight":"true","workStationId":$scope.workStationId};
            console.log(date.toLocaleString()+"->当前亮的灯："+ data);
            var msg = JSON.stringify(obj);
            socket.send(msg);
        }
        function sendMessage1(data) {
            var obj =  {"url": "/light/flashColorNumber", "labelId": data, "color":"GREEN","blinkFlag":"0.5","number":0, "shootLight":"true","workStationId":$scope.workStationId};
            console.log(date.toLocaleString()+"->当前闪烁的灯："+ data);
            var msg = JSON.stringify(obj);
            socket.send(msg);
        }
        function sendMessage2(data) {
            // var obj =  {"url": "/light/flashColorNumber", "labelId": data, "color":"GREEN","blinkFlag":"0.5","number":0, "shootLight":"true","workStationId":$scope.workStationId};
            var  obj = {"url": "/light/onOff", "labelId": data, "onOffFlag": "false","shootLight":"true","workStationId":$scope.workStationId};
            console.log(date.toLocaleString()+"->当前关闭的灯："+ data);
            var msg = JSON.stringify(obj);
            socket.send(msg);
        }
        function inputfocus() {
            if($scope.basketOperate == "scanCanPinBasket"){
                $scope.windowMessage = "请扫描残品货筐，并将货筐放于指定位置";
                setTimeout(function () {
                    $("#canPinBasketId").focus();
                }, 200);
            }else if($scope.basketOperate == "unScanSKUBasket"){
                $scope.windowMessage = "请扫描无法扫描货筐，并将货筐放于指定位置";
                setTimeout(function () {
                    $("#unScanSKUBasketId").focus();
                }, 200);
            }else if($scope.basketOperate == "emptyBasket"){
                $scope.windowMessage = "请扫描空拣货货筐，放置在指定位置";
                setTimeout(function () {
                    $("#emptyBasketId").focus();
                }, 200);
            }else if($scope.basketOperate == "pod"){
                $scope.windowMessage = "等待Pod...";
                //    $scope.fullContainerBnt = true;
                pageStateStep = "waitPod";
                $scope.skuMissBnt = true;
                $scope.canNotBnt = true;
                $scope.damageBnt = true;
                $scope.checkOrderBnt = false;
                $scope.lotDate = "";

                setTimeout(function () {
                    $("#podId").focus();
                }, 200);
            }else if($scope.basketOperate == "sku"){
                $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                setTimeout(function () {
                    $("#skuId").focus()
                }, 200);
                $scope.skuMissBnt = false;
                $scope.canNotBnt = false;
                $scope.damageBnt = false;
                $scope.checkOrderBnt = true;

                pageStateStep = "checkItem";
            }else if($scope.basketOperate == "skuDamaged"){
                $scope.windowMessage="请扫描残损商品";
                setTimeout(function () {
                    $("#skuDamagedId").focus();
                },200);
            }
        }
        function clearLight(){
            $interval.cancel($scope.clickDamageLight);
            $interval.cancel($scope.clickDamageLight1);
            $interval.cancel($scope.clickCannotScanLight);
            $interval.cancel($scope.clickCannotScanLight1);
            $interval.cancel($scope.clickNewBasketLight);
            $interval.cancel($scope.clickNewLight);
            $interval.cancel($scope.clickOrderLight);
            $interval.cancel($scope.clickCanPinLight);
        }

        function toWaitpodOrWaitscan() { //页面跳转到等待pod页面还是等待扫描商品界面
            if(pageStateStep == "waitPod"){
                clearPodPage();
            }else {
                $scope.skuNo = "";
                $timeout(function () {
                    $("#skuId").focus();
                })
                $scope.basketOperate = "sku";
                $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                //  scanNewStorageStep = 1;//恢复初始值

                $scope.checkOrderBnt = true;
                $scope.skuMissBnt = false;
                $scope.canNotBnt = false;
                $scope.damageBnt = false;

                pageStateStep = "checkItem";
            }
        }


        function clearPodPage(){
            $scope.lotDate = "";
            $("#empty"+$scope.i).text("");
            $("#empty"+$scope.i).css("background","#D9D9D9");
            $("#canPinId").css("background","#D9D9D9");
            $("#unScanId").css("background","#D9D9D9");
            $("#canPinId").text("");
            $("#unScanId").text("");
            $scope.windowMessage = "等待Pod...";
            //    $scope.fullContainerBnt = true;
            pageStateStep = "waitPod";
            $scope.skuMissBnt = true;
            $scope.canNotBnt = true;
            $scope.damageBnt = true;
            $scope.checkOrderBnt = false;

            $scope.basketOperate = "pod";
            setTimeout(function () {
                $("#podId").focus();
            }, 200);
        }



        function init() {
            initPage();
        }
        //初始化v
        init();
    })
})();