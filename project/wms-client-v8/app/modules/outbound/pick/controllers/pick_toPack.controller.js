(function () {
    'use strict';

    angular.module('myApp').controller("pickToPackCtl", function ($scope, $stateParams, $interval, $rootScope, $state, $window,
                                                                  FLOOR_COLOR,BACKEND_CONFIG, OUTBOUND_CONSTANT, pickToPackService,
                                                                  webSocketService,outboundService, $timeout) {
        $scope.wallHeight = $("#wallHeight").height();//pickpackwall父div的高度
        $scope.podheight = $("#podheight").height();//pod父div的高度
        $scope.serialNoType = "INVENTORY";//初始化定义扫描序列号状态
        $scope.operation = "PICK TO PACK";
        //$scope.comfirmBnt = false;
        var scanDamagedStep = 1;//当前扫描残品筐的操作步骤
        var scanUnscanStep = 1;//当前扫描无法扫描筐的操作步骤
        var scanInventeorStep = 1;//当前扫描待调查筐的操作步骤
        var inputAmount = 1;//当前输入商品数的步骤
        var clickBinButton = 0;//获取当前点击的是要扫描哪个货位
        var bindStorageStep = 0;//继续使用现有货框时的判断
        var scanNewStorageStep = 0;//货框已满判断
        var putProblemStep = 0;//放置问题商品类型判断
        var lightResult = 0;//拍灯状态
        var unScanLight1 = 0;//无法扫描的灯标签扫描的状态
        var socket;//webSocket
        var podSocket;//webSocket
        var podValue = 0;
        var date = new Date();
        
        /////////////////////////////////////////////////////////////外部函数///////////////////////////////////////////////////////
        // 扫描工作站
        $scope.scanStation = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            $scope.stationNo = $scope.pickPackStationName;//工作站条码
            pickToPackService.scanStation($scope.stationNo, function (data) {
                if (data.msg == "none") {
                    $scope.message = "条码 " + $scope.stationNo + " 不是一个有效工作站，请重新扫描";
                    $scope.messageOperate = "show";
                    $scope.pickPackStationName = "";
                }
                if (data.msg == "user") {
                    $scope.message = "工作站已分配给员工" + data.obj + "，请重新扫描";
                    $scope.messageOperate = "show";
                    $scope.pickPackStationName = "";
                }
                if (data.msg == "storage") {
                    $scope.workStationId = data.obj.workStationId;
                    $scope.logicStationId = data.obj.logicStationId;
                    $scope.sectionId = data.obj.sectionId;
                    getLightResult();  //继续使用已绑定货框时监听拍灯消息
                    //获取已绑定货框的pickingUnitLoadId
                    //获取工作站货筐对应的电子标签
                    $scope.messageOperate = "";//隐藏错误信息条码
                    $scope.storages = data.obj.pickingUnitLoadResults;
                    $scope.count = data.obj.pickingUnitLoadResults.length;
                    $scope.damagedLight = data.obj.damagedLightId;
                    $scope.unScanLight = data.obj.canNotScanLightId;
                    $scope.toReserachLight = data.obj.toInvestigateId;
                    $scope.damagedPid = data.obj.damagedPickId;
                    $scope.canNotScanPId = data.obj.genuinePickId;


                    $scope.toInvestigatePId = data.obj.invetigatePickId;
                    $scope.chooseTypeWindow("pickStyleId", $scope.pickStyleWindows);

                }
                if (data.msg == "success") {
                    //获取工作站货筐对应的电子标签
                    $scope.workStationId = data.obj.workStationId;
                    $scope.logicStationId = data.obj.logicStationId;
                    $scope.sectionId = data.obj.sectionId;
                    $scope.messageOperate = "";//隐藏错误信息条码
                    getLightResult();  //重新绑定货框时监听拍灯信息
                    $scope.damagedLight = data.obj.damagedLightId;
                    $scope.unScanLight = data.obj.canNotScanLightId;
                    $scope.toReserachLight = data.obj.toInvestigateId;
                    $scope.pickPackStationName = "";
                    $scope.pickPackOperate = "scanPickPackCar"; // 继续扫描pickpack_car
                    $rootScope.pickPackMain = {};
                    $rootScope.pickPackContinue = false;
                    setTimeout(function () {
                        $("#pickPack_pickPackCar").focus();
                    }, 100);

                }
            },function (data) {
                if(data.key == "PICK_STATION_ALREADY_BINDED"){//实际工作站被绑定
                    $scope.message = data.values[0];
                    $scope.messageOperate = "show";
                    $scope.pickPackStationName = "";
                }
            });
        };
        //自动满筐所有车牌重新绑定
        $scope.reused = function () {
            pickToPackService.fullAllStorage($scope.stationNo);
            $scope.pickStyleWindows.close();
            $scope.messageOperate = "";//隐藏错误信息条码
            $scope.pickPackOperate = "scanPickPackCar";
            setTimeout(function () {
                $("#pickPack_pickPackCar").focus();
            }, 100);
        }
        //继续使用当前货框
        $scope.continueUse = function () {
            //获取已绑定的货框信息
            var storages = $scope.storages;
            var size = storages.length;
            if (size == 3) {
                bindStorageStep = 1;//3个货框位置都有，直接到扫描pod
            }
            if (size == 2) {
                var type1 = storages[0].type;
                var type2 = storages[1].type;
                if ((type1 == "DAMAGE" && type2 == "PENDING") || (type1 == "PENDING" && type2 == "DAMAGE")) {
                    bindStorageStep = 2;//进入主页面后扫描无法扫描货框，扫描完后，直接扫pod
                }
                if ((type1 == "INVENTORY" && type2 == "DAMAGE") || (type2 == "INVENTORY" && type1 == "DAMAGE")) {
                    bindStorageStep = 3;//进入主页面后扫描待调查货框，扫描完后，直接扫pod
                }
                if ((type1 == "INVENTORY" && type2 == "PENDING") || (type2 == "INVENTORY" && type1 == "PENDING")) {
                    bindStorageStep = 4;//进入主页面后扫描残品货框，扫描完后，直接扫pod
                }
            }
            if (size == 1) {
                var type1 = storages[0].type;
                if (type1 == "INVENTORY") {
                    bindStorageStep = 5;//进入主页面后扫描残品货框,再扫描待调查货框，扫描完后，直接扫pod
                }
                if (type1 == "DAMAGE") {
                    bindStorageStep = 6;//进入主页面后扫描无法扫描货框,再扫描待调查货框，扫描完后，直接扫pod
                }
                if (type1 == "PENDING") {
                    bindStorageStep = 7;//进入主页面后扫描残品货框,再扫描无法扫描货框，扫描完后，直接扫pod
                }

            }
            $scope.pickStyleWindows.close();
            $scope.messageOperate = "";//隐藏错误信息条码
            $scope.pickPackOperate = "scanPickPackCar";
            setTimeout(function () {
                $("#pickPack_pickPackCar").focus();
            }, 100);
        }
        //扫描Pick-Pack Wall
        $scope.scanPickPackCar = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var pickPackNo = $scope.pickPackCarNo;
            var obj = {"pickPackWallName":$scope.pickPackCarNo,"pickStationName":$scope.stationNo};
            pickToPackService.scanPickPackCar(obj, function (data) {
                if (data.msg == "success") {
                    if (bindStorageStep == 1) {
                        $scope.messageOperate = "";
                        initMainPage();
                        $scope.pickPackWallInfo = data.obj;
                        initPickPackWall($scope.pickPackWallInfo);
                    }
                    if (bindStorageStep == 2 || bindStorageStep == 6) {
                        $scope.messageOperate = "";
                        initMainPage2();
                        $scope.pickPackWallInfo = data.obj;
                        initPickPackWall($scope.pickPackWallInfo);
                    }
                    if (bindStorageStep == 3) {
                        $scope.messageOperate = "";
                        initMainPage3();
                        $scope.pickPackWallInfo = data.obj;
                        initPickPackWall($scope.pickPackWallInfo);
                    }
                    if (bindStorageStep == 0 || bindStorageStep == 4 || bindStorageStep == 5 || bindStorageStep == 7) {
                        $scope.messageOperate = "";
                        initMainPage1();
                        $scope.pickPackWallInfo = data.obj;
                        initPickPackWall($scope.pickPackWallInfo);
                    }
                }
                if (data.msg == "none") {
                    $scope.message = "条码" + pickPackNo + "，不是一个有效的Pick Pack车，请重新扫描";
                    $scope.messageOperate = "show";
                    $scope.pickPackCarNo = "";
                }
                if (data.msg == "notsysytem") {
                    $scope.message = "扫描的Pick Pack wall并不是系统绑定的Pick Pack wall，请重新扫描";
                    $scope.messageOperate = "show";
                    $scope.pickPackCarNo = "";
                }
            });
        }
        //扫描残品筐
        $scope.scanCanPinBasket = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var damage = $scope.canPinBasket.toUpperCase();
            pickToPackService.scanCanPinBasket($scope.canPinBasket, $scope.stationNo, function (data) {
                $scope.reScanCanPinWindows.close();
                $scope.damagedPid = data.pickUnitLoadId;
                //残品筐扫描成功,进入桉灯
                $scope.windowMessage = "请按动残品货筐上方暗灯";
                $("#canPinId").css("background", "#00B050");
                $scope.basketOperate = "scanCanPinBasketDigital";
                setTimeout(function () {
                    $("#canPinBasketDigitalId").focus();
                }, 100);
                $scope.clickDamageLight = $interval(function () {
                    if (lightResult == 1) {
                        clearLight();
                        $interval.cancel($scope.clickDamageLight);
                        if (scanDamagedStep == 1) {
                            initScanDamaged();
                        }
                        if (scanDamagedStep == 2) {
                            $("#canPinId").css("background", "#D9D9D9");
                            $scope.basketOperate = "sku";
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            setTimeout(function () {
                                $("#skuId").focus()
                            }, 200);
                            lightResult = 0;
                        }
                    }
                }, 100)
            }, function (data) {
                if (data.key == "GOODS_EXIST_IN_LOCATION") {
                    //残品筐里有商品
                    openDamagedWindow(data, damage);
                }
                if (data.key == "UNITLOAD_ALREADY_BINDED_TO_STATION") {
                    //残品筐已被绑定
                    openDamagedScanWindow(data, damage);
                }
                if (data.key == "NO_SUCH_UNITLOAD") {
                    //无效的残品筐
                    openDamagedNoneWindow(damage);
                }
                if (data.key == "UNITLOAD_IS_LOCKED") {
                    openDamagedLockWindow(damage);
                }
            });
        }
        //扫描残品框上方的灯的标签
        $scope.scanCanPinBasketDigitalName = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var canpinDigital = $scope.canPinBasketDigitalName.toUpperCase();
            $scope.scanDigitalWindows.close();
            pickToPackService.scanDigitalName(canpinDigital,function (data) {
                if($scope.damagedLight == data.digitalId){
                    clearLight();
                    if (scanDamagedStep == 1) {
                        lightResult = 1;
                        initScanDamaged();
                    }
                    if (scanDamagedStep == 2) {
                        $("#canPinId").css("background", "#D9D9D9");
                        $scope.basketOperate = "sku";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        setTimeout(function () {
                            $("#skuId").focus()
                        }, 200);
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalOperate = "scanCanPinBasketDigital1";
                    $scope.digitalErrorMsg = "扫描的位置标签"+canpinDigital+"不匹配，请重新扫描";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#canPinBasketDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                setTimeout(function () {$("#canPinBasketDigitalId1").focus();}, 200);
                $scope.digitalOperate = "scanCanPinBasketDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#canPinBasketDigitalId1").focus();});
            });
            $scope.canPinBasketDigitalName = "";
        }
        function initScanDamaged() {
            //残品筐扫描成功,进入桉灯
            $scope.windowMessage = "请按动残品货筐上方暗灯";
            $("#canPinId").css("background", "#00B050");
            $scope.basketOperate = "rescanCanPinBasketDigital";
            setTimeout(function () {
                $("#reScancanPinBasketDigitalId").focus();
            }, 100);
            $scope.clickLight3 = $interval(function () {
                if (lightResult == 1 ) {
                    lightResult = 0;
                    clearLight();
                    $interval.cancel($scope.clickLight3);
                    if (bindStorageStep == 0 || bindStorageStep == 7) {
                        $scope.basketOperate = "unScanSKUBasket";
                        $scope.canPinBasket = "";
                        $scope.windowMessage = "请扫描商品无法扫描货筐，并将货筐放于指定位置";
                        setTimeout(function () {
                            $("#unScanSKUBasketId").focus();
                        }, 200);
                        $("#canPinId").css("background", "#D9D9D9");
                        $("#unScanId").css("background", "#FFC000");
                        sendMessage($scope.unScanLight);
                    } else if (bindStorageStep == 4) {
                        if(podValue == 0){
                            getPodResult();
                            podValue = 1;
                        }
                        $("#canPinId").css("background", "#D9D9D9");
                        $scope.basketOperate = "pod";
                        $scope.windowMessage = "等待Pod进入";
                        setTimeout(function () {
                            $("#podId").focus();
                        }, 200);
                    } else if (bindStorageStep == 5) {
                        $("#canPinId").css("background", "#D9D9D9");
                        $("#toReserachId").css("background", "#33CCFF");
                        $scope.basketOperate = "toReserachBasket";
                        $scope.unScanSKUBasket = "";
                        $scope.windowMessage = "请扫描待调查货筐，并将货筐放于指定位置";
                        setTimeout(function () {
                            $("#toReserachBasketId").focus();
                        }, 200);
                        sendMessage($scope.toReserachLight);
                    } else if (scanNewStorageStep == 0) {
                        $("#unScanId").css("background", "#D9D9D9");
                        $scope.skuOperate = "skuNo";
                        $scope.basketOperate = "sku";
                        $scope.pickNoInput = "";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        setTimeout(function () {
                            $("#skuId").focus();
                        }, 200);
                    }
                }
            }, 100)
        }

        //扫描新的残品框上方的位置标签
        $scope.rescanCanPinBasketDigitalName = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var newCanpinDigital = $scope.newCanPinBasketDigitalName.toUpperCase();
            $scope.scanDigitalWindows.close();
            pickToPackService.scanDigitalName(newCanpinDigital,function (data) {
                if($scope.damagedLight == data.digitalId){
                    clearLight();
                    if (bindStorageStep == 0 || bindStorageStep == 7) {
                        $scope.basketOperate = "unScanSKUBasket";
                        $scope.canPinBasket = "";
                        $scope.windowMessage = "请扫描商品无法扫描货筐，并将货筐放于指定位置";
                        setTimeout(function () {
                            $("#unScanSKUBasketId").focus();
                        }, 200);
                        $("#canPinId").css("background", "#D9D9D9");
                        $("#unScanId").css("background", "#FFC000");
                        sendMessage($scope.unScanLight);
                    } else if (bindStorageStep == 4) {
                        if(podValue == 0){
                            getPodResult();
                            podValue = 1;
                        }
                        $("#canPinId").css("background", "#D9D9D9");
                        $scope.basketOperate = "pod";
                        $scope.windowMessage = "等待Pod进入";
                        setTimeout(function () {
                            $("#podId").focus();
                        }, 200);

                    } else if (bindStorageStep == 5) {
                        $("#canPinId").css("background", "#D9D9D9");
                        $("#toReserachId").css("background", "#33CCFF");
                        $scope.basketOperate = "toReserachBasket";
                        $scope.unScanSKUBasket = "";
                        $scope.windowMessage = "请扫描待调查货筐，并将货筐放于指定位置";
                        setTimeout(function () {
                            $("#toReserachBasketId").focus();
                        }, 200);
                        sendMessage($scope.toReserachLight);
                    } else if (scanNewStorageStep == 0) {
                        $("#unScanId").css("background", "#D9D9D9");
                        $scope.skuOperate = "skuNo";
                        $scope.basketOperate = "sku";
                        $scope.pickNoInput = "";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        setTimeout(function () {
                            $("#skuId").focus();
                        }, 200);
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalOperate = "rescanCanPinBasketDigital";
                    setTimeout(function () {$("#canPinBasketDigitalId1").focus();}, 200);
                    $scope.digitalErrorMsg = "扫描的位置标签"+newCanpinDigital+"不匹配，请重新扫描";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows);
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "rescanCanPinBasketDigital";
                setTimeout(function () {$("#canPinBasketDigitalId1").focus();}, 200);
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows);
            });
            $scope.newCanPinBasketDigitalName = "";
        }

        //扫描无法扫描货筐
        $scope.scanUnScanSKUBasket = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var unScanSKUBasket = $scope.unScanSKUBasket.toUpperCase();
            pickToPackService.scanUnScanSKUBasket(unScanSKUBasket, $scope.stationNo, function (data) {
                $scope.reUnScanWindows.close();
                $scope.canNotScanPId = data.pickUnitLoadId;
                //无法扫描商品货筐扫描成功,进入桉灯
                $scope.windowMessage = "请按动无法扫描商品货筐上方暗灯";
                $("#unScanId").css("background", "#00B050");
                $scope.basketOperate = "scanUnScanSKUBasketDigital";
                setTimeout(function () {
                    $("#unScanSKUBasketDigitalId").focus();
                }, 100);
                $scope.clickInventoryLight = $interval(function () {
                    if (lightResult == 1) {
                        clearLight();
                        $interval.cancel($scope.clickInventoryLight);
                        if (scanUnscanStep == 1) {
                            initScanGenuine();
                        }
                        if (scanUnscanStep == 2) {
                            $("#unScanId").css("background", "#D9D9D9");
                            $scope.basketOperate = "sku";
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            setTimeout(function () {
                                $("#skuId").focus()
                            }, 200);
                            lightResult = 0;
                        }
                    }
                }, 100);
            }, function (data) {
                if (data.key == "GOODS_EXIST_IN_LOCATION") {
                    //无法扫描商品货筐里有商品
                    openUnScanWindow(data, unScanSKUBasket);
                }
                if (data.key == "UNITLOAD_ALREADY_BINDED_TO_STATION") {
                    //无法扫描商品货筐已被绑定
                    openUnScanBindWindow(data, unScanSKUBasket);
                }
                if (data.key == "NO_SUCH_UNITLOAD") {
                    //无效的无法扫描商品货筐
                    openUnScanNoneWindow(unScanSKUBasket);
                }
                if (data.key == "UNITLOAD_IS_LOCKED") {
                    openUnScanLockWindow(unScanSKUBasket);
                }
            })
        }
        //无法扫描框扫描位置标签信息
        $scope.scanUnScanSKUBasketDigitalName = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var unscanDigital = $scope.unScanSKUBasketDigitalName.toUpperCase();
            $scope.scanDigitalWindows.close();
            pickToPackService.scanDigitalName(unscanDigital,function (data) {
                if($scope.unScanLight == data.digitalId){
                    lightResult = 0;
                    clearLight();
                    if (scanUnscanStep == 1) {
                        unScanLight1 = 1;
                        initScanGenuine();
                    }
                    if (scanUnscanStep == 2) {
                        $("#unScanId").css("background", "#D9D9D9");
                        $scope.basketOperate = "sku";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        setTimeout(function () {
                            $("#skuId").focus()
                        }, 200);
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalErrorMsg = "扫描的位置标签"+unscanDigital+"不匹配，请重新扫描";
                    $scope.digitalOperate = "scanUnScanSKUBasketDigital1";
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
        function initScanGenuine() {
            //无法扫描商品货筐扫描成功,进入桉灯
            $scope.windowMessage = "请按动无法扫描商品货筐上方暗灯";
            $("#unScanId").css("background", "#00B050");
            $scope.basketOperate = "rescanUnScanSKUBasketDigital";
            setTimeout(function () {
                $("#reunScanSKUBasketDigitalId").focus();
            }, 100);
            $scope.clickInventoryLight3 = $interval(function () {
                if (lightResult == 1 || unScanLight1 == 1 ) {
                    lightResult = 0;
                    clearLight();
                    unScanLight1 = 0;
                    $interval.cancel($scope.clickInventoryLight3);
                    if (bindStorageStep == 0 || bindStorageStep == 6) {
                        $("#unScanId").css("background", "#D9D9D9");
                        $("#toReserachId").css("background", "#33CCFF");
                        $scope.basketOperate = "toReserachBasket";
                        $scope.unScanSKUBasket = "";
                        $scope.windowMessage = "请扫描待调查货筐，并将货筐放于指定位置";
                        setTimeout(function () {
                            $("#toReserachBasketId").focus();
                        }, 200);
                        sendMessage($scope.toReserachLight);
                    } else if (bindStorageStep == 2) {
                        if(podValue == 0){
                            getPodResult();
                            podValue = 1;
                        }
                        $("#unScanId").css("background", "#D9D9D9");
                        $scope.basketOperate = "pod";
                        $scope.windowMessage = "等待Pod进入";
                        setTimeout(function () {
                            $("#podId").focus();
                        }, 200);

                    } else if (scanNewStorageStep == 0) {
                        $("#unScanId").css("background", "#D9D9D9");
                        $scope.skuOperate = "skuNo";
                        $scope.basketOperate = "sku";
                        $scope.pickNoInput = "";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        setTimeout(function () {
                            $("#skuId").focus()
                        }, 200);
                    }
                }
            }, 100);
        }
        //新的无法扫描框扫描位置标签信息
        $scope.rescanUnScanSKUBasketDigitalName = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var newUnscanDigital = $scope.newUnScanSKUBasketDigitalName.toUpperCase();
            $scope.scanDigitalWindows.close();
            pickToPackService.scanDigitalName(newUnscanDigital,function (data) {
                if($scope.unScanLight == data.digitalId){
                    clearLight();
                    if (bindStorageStep == 0 || bindStorageStep == 6) {
                        $("#unScanId").css("background", "#D9D9D9");
                        $("#toReserachId").css("background", "#33CCFF");
                        $scope.basketOperate = "toReserachBasket";
                        $scope.unScanSKUBasket = "";
                        $scope.windowMessage = "请扫描待调查货筐，并将货筐放于指定位置";
                        setTimeout(function () {
                            $("#toReserachBasketId").focus();
                        }, 200);
                        sendMessage($scope.toReserachLight);
                    } else if (bindStorageStep == 2) {
                        if(podValue == 0){
                            getPodResult();
                            podValue = 1;
                        }
                        $("#unScanId").css("background", "#D9D9D9");
                        $scope.basketOperate = "pod";
                        $scope.windowMessage = "等待Pod进入";
                        setTimeout(function () {
                            $("#podId").focus();
                        }, 200);

                    } else if (scanNewStorageStep == 0) {
                        $("#unScanId").css("background", "#D9D9D9");
                        $scope.skuOperate = "skuNo";
                        $scope.basketOperate = "sku";
                        $scope.pickNoInput = "";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        setTimeout(function () {
                            $("#skuId").focus()
                        }, 200);
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalErrorMsg = "扫描的位置标签"+newUnscanDigital+"不匹配，请重新扫描";
                    $scope.digitalOperate = "rescanUnScanSKUBasketDigital1";
                    setTimeout(function () {$("#reunScanSKUBasketDigitalId1").focus();}, 200);
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows);
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "rescanUnScanSKUBasketDigital1";
                setTimeout(function () {$("#reunScanSKUBasketDigitalId1").focus();}, 200);
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows);
            });
            $scope.newUnScanSKUBasketDigitalName = "";
        }

        //扫描待调查货筐
        $scope.scanToReserachBasket = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var toReserachBasket = $scope.toReserachBasket.toUpperCase();
            pickToPackService.scanToReserachBasket(toReserachBasket, $scope.stationNo, function (data) {
                $scope.toReserachWindows.close();
                $scope.toInvestigatePId = data.pickUnitLoadId;
                //待调查商品货筐扫描成功,进入桉灯
                $scope.windowMessage = "请按动待调查商品货筐上方暗灯";
                $("#toReserachId").css("background", "#00B050");
                $scope.basketOperate = "scanToReserachBasketDigital";
                setTimeout(function () {
                    $("#toReserachBasketDigitalId").focus();
                }, 100);
                $scope.clickPendingLight= $interval(function () {
                    if (lightResult == 1) {
                        lightResult = 0;
                        clearLight();
                        $interval.cancel($scope.clickPendingLight);
                        if (scanInventeorStep == 1) {
                            if(podValue == 0){
                                getPodResult();
                                podValue = 1;
                            }
                            $("#toReserachId").css("background", "#D9D9D9");
                            $scope.toReserachBasket = "";
                            $scope.basketOperate = "pod";
                            $scope.windowMessage = "等待Pod进入";
                            setTimeout(function () {
                                $("#podId").focus();
                            }, 200);

                        } else if (scanInventeorStep == 2) {
                            $("#toReserachId").css("background", "#D9D9D9");
                            $scope.basketOperate = "sku";
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            setTimeout(function () {
                                $("#skuId").focus()
                            }, 200);
                            scanInventeorStep = 1
                        } else if (scanNewStorageStep == 0) {
                            $("#unScanId").css("background", "#D9D9D9");
                            $scope.skuOperate = "skuNo";
                            $scope.basketOperate = "sku";
                            $scope.pickNoInput = "";
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            setTimeout(function () {
                                $("#skuId").focus()}, 200);
                        }
                    }
                }, 100)
            }, function (data) {
                if (data.key == "GOODS_EXIST_IN_LOCATION") {
                    //待调查商品货筐里有商品
                    openToReaservhWindow(data, toReserachBasket);
                }
                if (data.key == "UNITLOAD_ALREADY_BINDED_TO_STATION") {
                    //待调查商品货筐已被绑定
                    openToReaservhBindWindow(data, toReserachBasket);
                }
                if (data.key == "NO_SUCH_UNITLOAD") {
                    //无效的待调查商品货筐
                    openToReaservhNoneWindow(toReserachBasket);
                }
                if (data.key == "UNITLOAD_IS_LOCKED") {
                    openToReaservhLockWindow(toReserachBasket);
                }
            })
        }
        //扫描带调查货框的位置标签
        $scope.scanToReserachBasketDigitalName = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var reserachDigtial = $scope.toReserachBasketDigitalName;
            $scope.scanDigitalWindows.close();
            pickToPackService.scanDigitalName(reserachDigtial,function (data) {
                if($scope.toReserachLight == data.digitalId){
                    clearLight();
                    if (scanInventeorStep == 1) {
                        if(podValue == 0){
                            getPodResult();
                            podValue = 1;
                        }
                        $("#toReserachId").css("background", "#D9D9D9");
                        $scope.toReserachBasket = "";
                        $scope.basketOperate = "pod";
                        $scope.windowMessage = "等待Pod进入";
                        setTimeout(function () {
                            $("#podId").focus();
                        }, 200);

                    } else if (scanInventeorStep == 2) {
                        $("#toReserachId").css("background", "#D9D9D9");
                        $scope.basketOperate = "sku";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        setTimeout(function () {
                            $("#skuId").focus()
                        }, 200);
                        scanInventeorStep = 1
                    } else if (scanNewStorageStep == 0) {
                        $("#unScanId").css("background", "#D9D9D9");
                        $scope.skuOperate = "skuNo";
                        $scope.basketOperate = "sku";
                        $scope.pickNoInput = "";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        setTimeout(function () {
                            $("#skuId").focus()}, 200);
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalErrorMsg = "扫描的位置标签"+reserachDigtial+"不匹配，请重新扫描";
                    $scope.digitalOperate = "scanToReserachBasketDigital1";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#toReserachBasketDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "scanToReserachBasketDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#toReserachBasketDigitalId1").focus();});
            });
            $scope.toReserachBasketDigitalName = "";
        }
        //扫描pod
        $scope.scanPod = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            date = new Date();
            console.log(date.toLocaleString()+"->**********进入扫描pod方法");
            $scope.getOrderPosition($scope.podNo,$scope.sectionId, $scope.stationNo);//获取取货单信息

        }

        //获取取货单信息
        $scope.getOrderPosition = function (podNo, sectionId, stationNo) {
            //获取取货单信息
            pickToPackService.getOrderPosition(podNo, sectionId, stationNo, function (data) {
                if(data.message == "noPod"){
                    $scope.podMessage = "POD";
                    $scope.podRows = [];
                    initMainCallPod();
                }else if(data.message == "nextPod"){
                    //$scope.refreshNewPod();
                   // $scope.getOrderPosition(data.socketPodName,$scope.sectionId, $scope.stationNo);
                    callNewPod(data.socketPodName);
                }else{
                    getPickOrder(data);
                }
            })
        }

        function  getPickOrder(data) {
            //每次获取拣货单之前重新画pod
            pickToPackService.scanPod(data.socketPodName, function (data1) {
                console.log(date.toLocaleString()+"->获取拣货单成功。。。");
                $scope.basketOperate = "sku";
                $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                setTimeout(function () {$("#skuId").focus()}, 200);
                $scope.podInfo = data1;
                initPod(data1);//初始化pod
                $scope.podNo = data.socketPodName;
                $scope.podMessage = data.socketPodName;

                var changeChar = function (char) {
                    return Math.abs(65 - char.charCodeAt() + $scope.size) - 1
                }
                $scope.pickId = data.id;//拣货单id
                $scope.pickNo = data.amount;
                $scope.shipmentId = data.customerShipmentPositionDTO.customerShipment.id;
                $scope.skuNo = "SKU: " + data.itemDataDTO.itemNo;//商品skuNo
                $scope.skuName = data.itemDataDTO.name;

                /*  var item = data.itemDataDTO.itemNo;
                 if(item == "711209561729769"){
                 $scope.skuImg = "xuebi";
                 }
                 if(item == "129714641165855"){
                 $scope.skuImg = "kele";
                 }
                 if(item == "517036749820530"){
                 $scope.skuImg = "meinianda";
                 }
                 if(item == "235270135112427"){
                 $scope.skuImg = "shangquan";
                 }*/


                $scope.serialNoCode = data.itemDataDTO.serialRecordType;//商品序列号是否需要扫描 ALWAYS_RECORD需要扫描
                var fieldIndex = data.pickPackCellDTO.fieldIndex;
                $scope.pickPackCellName = data.pickPackCellDTO.name;
                $scope.pickPackWallId = data.pickPackCellDTO.pickPackWallDTO.id;
                $scope.pickPackCellLight2 = data.pickPackCellDTO.digitalabel2Id;//包装的灯
                $scope.pickPackCellLight = data.pickPackCellDTO.digitalabel1Id;//对应Pick Pack Cell的电子标签  拣货的灯
                $scope.fromLocationName = data.pickFromLocationName;
                $scope.fromLocationNameId = data.pickFromStorageLoactionId;

                $scope.podHierarchy = data.pickFromLocationName.substring(9, 10).toUpperCase(); //ABC  层级
                $scope.podNumber = data.pickFromLocationName.substring(10); //123 排列

                $scope.podHeight = parseInt(changeChar($scope.podHierarchy));
                $scope.podWidth = parseInt($scope.podNumber) - 1;

                $scope.podColor = FLOOR_COLOR[$scope.podHierarchy];
                $scope.podTypeName = data.pod.podType.name;
                //pod格子高亮
                $scope.podRows[$scope.podHeight].color = $scope.podColor; //行颜色
                $scope.podRows[$scope.podHeight].item[$scope.podWidth].name = data.pickFromLocationName.substring(9);
                $scope.podRows[$scope.podHeight].item[$scope.podWidth].choice = true;

                $scope.binName = data.pickFromLocationName.substring(9);//记录商品所有货位的位置信息
                //pickPackWall格亮
                $scope.pickrow = data.pickPackCellDTO.xPos;
                $scope.pickcolumn = data.pickPackCellDTO.yPos;
                if (fieldIndex == 1) {
                    $scope.pickPackWall1Rows[$scope.pickrow - 1].item[$scope.pickcolumn - 1].name = data.amount;
                    $scope.pickPackWall1Rows[$scope.pickrow - 1].item[$scope.pickcolumn - 1].choice = true;
                }
                if (fieldIndex == 2) {
                    $scope.pickPackWall2Rows[$scope.pickrow - 1].item[$scope.pickcolumn - 1].name = data.amount;
                    $scope.pickPackWall2Rows[$scope.pickrow - 1].item[$scope.pickcolumn - 1].choice = true;
                }
                if (fieldIndex == 3) {
                    $scope.pickPackWall3Rows[$scope.pickrow - 1].item[$scope.pickcolumn - 1].name = data.amount;
                    $scope.pickPackWall3Rows[$scope.pickrow - 1].item[$scope.pickcolumn - 1].choice = true;
                }
                //商品出现问题时需要的信息
                if (data.lotPicked != null) {
                    $scope.lotNo = data.lotPicked.lotNo;
                }
            });
        }

        //获取取货单信息
      /*  $scope.getOrderPosition = function (podNo, sectionId, stationNo) {
            //获取取货单信息
            pickToPackService.getOrderPosition(podNo, sectionId, stationNo, function (data) {
                console.log("获取拣货单成功。。。");

            }, function (data) {
                if (data.key == "EX_NO_PICKINGORDERPOSITION") {
                    console.log("没有拣货POD:",data);
                    $scope.podMessage = "POD";
                    $scope.podRows = [];
                    initPickPackWall($scope.pickPackWallInfo);
                    initMainCallPod();
                }
                if (data.key == "EX_CELL_HAS_LOCKED") {
                    $scope.pickingOrderPostion = "cell";
                    $scope.cellName = data.values[0];
                    $scope.podMsg = podNo;
                    $scope.errorWindow("reScanPodId", $scope.reScanPodWindows);
                }
                if (data.key == "EX_CAR_STOP") {
                    $scope.pickingOrderPostion = "carStop";
                    $scope.podMsg = podNo;
                    $scope.errorWindow("reScanPodId", $scope.reScanPodWindows);
                }
            })
        }*/
        //pod中没有拣货任务
        $scope.continueScanPod = function () {
            $scope.reScanPodWindows.close();
            $scope.podInfo = "";
            initPod($scope.podInfo);//初始化pod
            initMainCallPod();
            $scope.podRows = [];//清除pod的样子
            $("#toReserachId").css("background", "#D9D9D9");
            $("#unScanId").css("background", "#D9D9D9");
            $("#canPinId").css("background", "#D9D9D9");
        }
        //扫描商品
        $scope.scanSKU = function (e) {//判断商品存不存在，匹配否//若成功，判断商品需要拣的个数$scope.pickNo
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            $scope.scanItemErrorWindows.close();
            var obj = {
                "pickId": $scope.pickId,
                "itemNo": $scope.itemNo,
                "amountPicked": "",
                "type": ""
            };
            pickToPackService.scanSKU(obj, function (data) {
                checkItemSuccess();
                $scope.itemNo = "";
            }, function (data) {
                if (data.key == "NO_ITEMDATA_WITH_ITEMNUMER") {
                    $scope.itemMsg = data.values[0];
                    $scope.itemNo = "";
                    $scope.openWindow({
                        windowId: "scanItem",
                        windowClass: "myWindow",
                        windowName: $scope.scanItemErrorWindows,
                        width: 700,
                        height: 260,
                        closeable: false,
                        visible: true,
                        activate: function () {
                            $("#scanItemInput").focus();
                        },
                    })
                }
                if (data.key == "ITEM_IS_NOT_MATCH") {
                    $scope.itemMsg = data.values[0];
                    $scope.itemNo = "";
                    $scope.openWindow({
                        windowId: "scanItem",
                        windowClass: "myWindow",
                        windowName: $scope.scanItemErrorWindows,
                        width: 700,
                        height: 260,
                        closeable: false,
                        visible: true,
                        activate: function () {
                            $("#scanItemInput").focus();
                        },
                    })
                }
                if (data.key == "TOO_MANY_ITEM_EXIST") {
                    $scope.itemMsg = data.values[0];
                    $scope.itemNo = "";
                    $scope.openWindow({
                        windowId: "scanItem",
                        windowClass: "myWindow",
                        windowName: $scope.scanItemErrorWindows,
                        width: 700,
                        height: 260,
                        closeable: false,
                        visible: true,
                        activate: function () {
                            $("#scanItemInput").focus();
                        },
                    })
                }
            })

        }
        function checkItemSuccess() {
            if ($scope.pickNo == 1) {
                if ($scope.serialNoCode == "ALWAYS_RECORD") {
                    $scope.basketOperate = "seriesNo";
                    $scope.windowMessage = "请扫描商品的序列号"
                    $scope.seriesWindow("seriesNoId", $scope.seriesNoWindows, function () {
                        $("#seriesNumberId").focus();
                    });
                } else {
                    sendMessage($scope.pickPackCellLight);
                    confirmItemData($scope.pickNo);
                }
            }
            if ($scope.pickNo > 1) {
                sendMessage($scope.pickPackCellLight);
                $scope.amount = $scope.pickNo;
                $scope.skuOperate = "skuNoInput";
                $scope.basketOperate = "skuNoInput";
                $scope.windowMessage = "请输入商品数量";
                $scope.comfirmBnt = false;
            }
        }

        function confirmItemData(amount) {
            var obj = {
                "pickId": $scope.pickId,
                "itemNo": $scope.itemNo,
                "amountPicked": amount,
                "type": "",
                "operation":$scope.operation
            };
            pickToPackService.confirmScanSKU(obj, function (data) {
                $scope.shipmentState = data.shipmentState;
                putItem();
            });
        }

        //扫描商品序列号
        $scope.scanSeriesNo = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            $scope.rescanSeriesNoWindows.close();
            pickToPackService.scanSeriesNo($scope.pickId, $scope.seriesNo, $scope.serialNoType,$scope.operation, function (data) {
                $scope.seriesNo = "";
                $scope.seriesNoWindows.close();

                if ("DAMAGE" == $scope.serialNoType && $scope.serialStep == "DAMAGE") {  //商品残损但序列号可以扫描
                    var obj = {
                        "pickId": $scope.pickId,
                        "itemNo": $scope.skuDamagedNo,
                        "amountPicked": 1,
                        "type": "DAMAGE",
                        "operation":$scope.operation
                    };
                    $scope.serialStep = "";
                    handleProblemItem(obj);
                }

                if ("INVENTORY" == $scope.serialNoType && $scope.serialStep == "DAMAGE") {  //商品无法扫描但序列号可以扫描
                    var obj = {
                        "pickId": $scope.pickId,
                        "itemNo": $scope.skuDamagedNo,
                        "amountPicked": 1,
                        "type": "INVENTORY",
                        "operation":$scope.operation
                    };
                    $scope.serialStep = "";
                    handleProblemItem(obj);
                }
                if ("INVENTORY" == $scope.serialNoType) {
                    sendMessage($scope.pickPackCellLight);
                    $scope.basketOperate = "scanDigital";
                    $scope.windowMessage = "请将商品放到指定的Pick-Pack Wall,并按动Pick-Pack Wall上方暗灯";
                    setTimeout(function () {
                        $("#digitalId").focus();
                    }, 100);
                   // getPickOrderDetail();
                    pickToPackService.getOrderPosition($scope.podNo, $scope.sectionId, $scope.stationNo, function (data) {
                        if(data.message == "noPod"){
                            $scope.podDetail = "noPod";
                            closeWebsocket(podSocket,"pod");
                        }
                        $scope.clickSKUlightlight = $interval(function () {
                            if (lightResult == 1) {
                                lightResult = 0;
                                clearLight();
                                $interval.cancel($scope.clickSKUlightlight);
                                // initOrderPosition();
                                if (data.shipmentState == 600) {
                                    console.log(date.toLocaleString()+"->扫描序列号订单完成shipmentId-->digitalId",$scope.shipmentId,$scope.pickPackCellLight2);
                                    sendMessage($scope.pickPackCellLight2,$scope.shipmentId,$scope.pickPackWallId);//让pack包装的cell格子的灯亮
                                }
                                getlightOffPosition(data.socketPodName);
                            }
                        }, 100);
                    })

                }
            }, function (data) {
                $scope.seriesNoWindows.close();
                $scope.seriesNumber = $scope.seriesNo;
                $scope.seriesNo = "";
                $scope.openWindow({
                    windowId: "newSeriesNoId",
                    windowName: $scope.rescanSeriesNoWindows,
                    windowClass: "myWindow",
                    width: 700,
                    height: 300,
                    closeable: true,
                    activate: function () {
                        $("#newSeriesNumberId").focus();
                    },
                })
            })
        }
        //确认无法扫描序列号
        $scope.confirmUnableScanSN = function () {
            $scope.rescanSeriesNoWindows.close();
            $scope.seriesNo = "";
            var itemNo = $scope.itemNo;
            if (inputAmount == 2) {
                itemNo = $scope.skuDamagedNo;
            }
            var obj = {
                "pickId": $scope.pickId,
                "itemNo": itemNo,
                "amountPicked": "",
                "type": "PENDING",
            };
            pickToPackService.checkDamagedItem(obj, function (data) {
                sendMessage($scope.damagedLight,"exception");
                sendMessage($scope.unScanLight,"exception");
                $("#canPinId").css("background", "#D9D9D9");
                $("#unScanId").css("background", "#D9D9D9");
                $("#toReserachId").css("background", "#33CCFF");
                sendMessage($scope.toReserachLight);
                inputAmount = 4;
                var type = "PENDING";
                //checkProblemItemSuccess(type);
                var obj = {
                    pickId: $scope.pickId,
                    itemNo: itemNo,
                    amountPicked: 1,
                    type: type,
                    operation:$scope.operation
                };
                handleProblemItem(obj);

            }, function (data) {
                if (data.key == "SAME_ITEMNAME_DIFFERENT_LOT") {
                    putProblemStep = 1;
                    $scope.errorWindow("changeBakId", $scope.changeBakWindows);
                    $scope.BasketFullMessaage = "货筐号码：" + data.values[0] + "中存在相同名称不同有效期商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
                if (data.key == "SAME_ITEM_DIFFERENT_CLIENT") {
                    putProblemStep = 1;
                    $scope.errorWindow("changeBakId", $scope.changeBakWindows);
                    $scope.BasketFullMessaage = "货筐号码： " + data.values[0] + "中存在相同名称不同客户商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
            })
        }
        //扫描序列号错误，取消
        $scope.cancelScanSN = function () {
            $scope.rescanSeriesNoWindows.close();
            $scope.seriesNo = "";
            $scope.basketOperate = "sku";
            $scope.itemNo = "";
            $scope.skuOperate = "skuNo";
            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
            setTimeout(function () {
                $("#skuId").focus();
            }, 200);
        }
        //选择拣货数量
        $scope.bind = function (x) {
            if($scope.amount < x){
                $scope.pickNoInput = "";
            }else{
                $scope.pickNoInput = x;
            }
        }
        //输入拣货数量后确定
        $scope.confirmPick = function () {
            if ($scope.pickNo > $scope.pickNoInput && $scope.pickNoInput != "") {
                $scope.openWindow({
                    windowId: "isInputNoId",
                    windowClass: "myWindow",
                    windowName: $scope.isInputNoWindows,
                    width: 700,
                    height: 260,
                    closeable: false,
                    visible: true,
                })
            } else if($scope.pickNo < $scope.pickNoInput){
                $scope.inputSuccessAmount = "1";
                $scope.openWindow({
                    windowId: "inputNoId",
                    windowClass: "myWindow",
                    windowName: $scope.inputNoWindows,
                    width: 700,
                    height: 200,
                    closeable: false,
                    visible: true,
                })
            } else if($scope.pickNoInput == "" || $scope.pickNoInput == undefined){
                $scope.inputSuccessAmount = "0";
                $scope.openWindow({
                    windowId: "inputNoId",
                    windowClass: "myWindow",
                    windowName: $scope.inputNoWindows,
                    width: 700,
                    height: 200,
                    closeable: false,
                    visible: true,
                })
            }else {
                confirmInputItemData();
            }
        }
        //当输入数量小于需求数量时，确认
        $scope.confirmInput = function () {
            $scope.isInputNoWindows.close();
            confirmInputItemData();
        }
        //当输入数量小于需求数量时，取消
        $scope.cancelInput = function () {
            $scope.isInputNoWindows.close();
        }
        //当输入拣货数量时，确认拣货
        function confirmInputItemData() {
            if (inputAmount == 1) {//正品确认
                confirmItemData($scope.pickNoInput);
            }
            if (inputAmount == 2) {//报残商品确认
                var obj = {
                    "pickId": $scope.pickId,
                    "itemNo": $scope.skuDamagedNo,
                    "amountPicked": $scope.pickNoInput,
                    "type": "DAMAGE",
                    "operation":$scope.operation
                };
                handleProblemItem(obj);
            }
            if (inputAmount == 3) {//商品无法扫描
                var obj = {
                    "pickId": $scope.pickId,
                    "itemNo": "",
                    "amountPicked": $scope.pickNoInput,
                    "type": "INVENTORY",
                    "operation":$scope.operation
                };
                handleProblemItem(obj);
            }
        }

        //商品残损按钮
        $scope.skuDamaged = function () {
            $scope.skuDamagedNo = "";
            $scope.basketOperate = "skuDamaged";
            $scope.windowMessage = "请扫描残损商品";
            setTimeout(function () {
                $("#skuDamagedId").focus();
            }, 200);
        }
        //扫描残损商品
        $scope.scanskuDamaged = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var obj = {
                "pickId": $scope.pickId,
                "itemNo": $scope.skuDamagedNo,
                "amountPicked": "",
                "type": "DAMAGE"
            };
            pickToPackService.checkDamagedItem(obj, function (data) {
                $("#canPinId").css("background", "red");
                sendMessage($scope.damagedLight);//打开对应残品货筐的灯
                inputAmount = 2;
                var type = "DAMAGE";
                checkProblemItemSuccess(type);
            }, function (data) {
                if (data.key == "SAME_ITEMNAME_DIFFERENT_LOT") {
                    putProblemStep = 2;
                    $scope.errorWindow("changeBakId", $scope.changeBakWindows);
                    $scope.BasketFullMessaage = "货筐号码：" + data.values[0] + "中存在相同名称不同有效期商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
                if (data.key == "SAME_ITEM_DIFFERENT_CLIENT") {
                    putProblemStep = 2;
                    $scope.errorWindow("changeBakId", $scope.changeBakWindows);
                    $scope.BasketFullMessaage = "货筐号码： " + data.values[0] + "中存在相同名称不同客户商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
                if (data.key == "NO_ITEMDATA_WITH_ITEMNUMER") {
                    $scope.damagedItemMsg = data.values[0];
                    $scope.openWindow({
                        windowId: "scanDamagedItemId",
                        windowClass: "myWindow",
                        windowName: $scope.scanDamagedItemErrorWindows,
                        width: 700,
                        height: 260,
                        closeable: false,
                        visible: true,
                    })
                }
                if (data.key == "ITEM_IS_NOT_MATCH") {
                    $scope.damagedItemMsg = data.values[0];
                    $scope.openWindow({
                        windowId: "scanDamagedItemId",
                        windowClass: "myWindow",
                        windowName: $scope.scanDamagedItemErrorWindows,
                        width: 700,
                        height: 260,
                        closeable: false,
                        visible: true,
                    })
                }
                if (data.key == "TOO_MANY_ITEM_EXIST") {
                    $scope.itemMsg = data.values[0];
                    $scope.itemNo = "";
                    $scope.openWindow({
                        windowId: "scanItem",
                        windowClass: "myWindow",
                        windowName: $scope.scanItemErrorWindows,
                        width: 700,
                        height: 260,
                        closeable: false,
                        visible: true,
                        activate: function () {
                            $("#scanItemInput").focus();
                        },
                    })
                }
            })

        }
        function checkProblemItemSuccess(type) {
            if ($scope.pickNo == 1) {
                if ($scope.serialNoCode == "ALWAYS_RECORD") {
                    //$scope.serialNoType = "DAMAGE";//扫描序列号变为残品序列号状态
                    $scope.serialNoType = type;
                    $scope.serialStep = "DAMAGE";
                    $scope.basketOperate = "seriesNo";
                    $scope.windowMessage = "请扫描商品的序列号"
                    $scope.seriesWindow("seriesNoId", $scope.seriesNoWindows, function () {
                        $("#seriesNumberId").focus();
                    });
                } else {
                    var obj = {
                        pickId: $scope.pickId,
                        itemNo: $scope.skuDamagedNo,
                        amountPicked: 1,
                        type: type,
                        operation:$scope.operation
                    };
                    handleProblemItem(obj);
                }
            }
            if ($scope.pickNo > 1) {
                $scope.pickNoInput = "";
                $scope.amount = $scope.pickNo;
                $scope.skuOperate = "skuNoInput";
                $scope.basketOperate = "skuNoInput";
                $scope.windowMessage = "请输入商品数量";
                $scope.comfirmBnt = false;
            }
        }

        //继续报残按钮
        $scope.continueBaoCan = function () {
            $scope.scanDamagedItemErrorWindows.close();
            var type = "DAMAGE";
            checkProblemItemSuccess(type);
        }
        $scope.cancelBaoCan = function () {
            $scope.scanDamagedItemErrorWindows.close();
            $scope.itemNo = "";
            $scope.skuNo = "";
            $scope.skuName = "";
            $scope.skuOperate = "skuNo";
            $scope.basketOperate = "sku";
            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
            setTimeout(function () {
                $("#skuId").focus(), 200
            });
        }
        //问题商品处理
        function handleProblemItem(obj) {
            pickToPackService.handleProblemItem(obj, function (data) {
                //调用问题处理接口
                putCanPin(obj.type);//放问题商品
            },function (data) {
                if(obj.type == "DAMAGE"){
                    putProblemStep = 2;
                }
                if(obj.type == "INVENTORY"){
                    putProblemStep = 3;
                }
                if(obj.type == "PENDING"){
                    putProblemStep = 1;
                }
                if (data.key == "SAME_ITEMNAME_DIFFERENT_LOT") {
                    $scope.errorWindow("changeBakId", $scope.changeBakWindows);
                    $scope.BasketFullMessaage = "货筐号码：" + data.values[0] + "中存在相同名称不同有效期商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
                if (data.key == "SAME_ITEM_DIFFERENT_CLIENT") {
                    $scope.errorWindow("changeBakId", $scope.changeBakWindows);
                    $scope.BasketFullMessaage = "货筐号码： " + data.values[0] + "中存在相同名称不同客户商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
            })
        }

        //货筐已满按钮事件
        $scope.BasketFull = function () {
            $scope.changeBakWindows.close();
            scanNewStorageStep = 1;
            //获取该站台3个筐目前的信息
            pickToPackService.getBakInfo($scope.stationNo, function (data) {
                $scope.bakInfo = data.pickingUnitLoadResults;
            });
            $scope.scanFullBakWindow("scanFullId", $scope.scanFullWindows, function () {
                $("#fullBasketId").focus()
            });
            $scope.basketOperate = "windowMessage";
            $scope.windowMessage = "请扫描已满货筐";
        }
        //扫描已满货筐
        $scope.scanfullBasket = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            pickToPackService.scanfullBasket($scope.fullBasket, $scope.stationNo, function (data) {
                $scope.storageTypeName = data.storageName;
                $scope.scanFullWindows.close();
                if (data.storageName == "DAMAGE") {
                    $scope.newBasketNo = "已成功满筐在残品货筐位置，货筐条码：" + $scope.fullBasket + "，商品总数" + data.sum + "件,请扫描新的货筐放在残品货筐位置";
                    $("#canPinId").css("background", "#FF0000");
                    sendMessage($scope.damagedLight);//打开对应残品货筐的灯
                }
                if (data.storageName == "INVENTORY") {
                    $scope.newBasketNo = "已成功满筐在无法扫描货筐位置，货筐条码：" + $scope.fullBasket + "，商品总数" + data.sum + "件,请扫描新的货筐放在无法扫描货筐位置";
                    $("#unScanId").css("background", "#FFC000");
                    sendMessage($scope.unScanLight);//打开对应无法扫描货筐的灯
                }
                if (data.storageName == "PENDING") {
                    $scope.newBasketNo = "已成功满筐在待调查货筐位置，货筐条码：" + $scope.fullBasket + "，商品总数" + data.sum + "件,请扫描新的货筐放在待调查货筐位置";
                    $("#toReserachId").css("background", "#33CCFF");
                    sendMessage($scope.toReserachLight);//打开对应待调查货筐的灯
                }
                $scope.openWindow({
                    windowId: "newbakId",
                    windowName: $scope.newbakWindows,
                    windowClass: "blueWindow",
                    width: 700,
                    height: 300,
                    closeable: true,
                    activate: function () {
                        $("#newbasketId").focus();
                    },
                    title: "请扫描新的货筐",
                });
                $scope.basketOperate = "windowMessage";
                $scope.windowMessage = "请扫描新的货筐";
                $scope.fullBasket = "";
            })
        }
        //扫描新的货筐
        $scope.scannewbasketNo = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var info = $scope.newbasketNo;
            $scope.newbasketNo = "";
            $scope.newbakWindows.close();
            pickToPackService.scanNewBasket(info, $scope.storageTypeName, $scope.stationNo, function (data) {
                $scope.errorWindow("newbakDigitalId", $scope.newbakDigitalWindows,function (){$("#canpinDigitalId").focus()});
                if($scope.storageTypeName == "DAMAGE") {
                    //$scope.newBasketNo = "已成功扫描车牌" + $scope.newbasketNo + "在残品货筐位置;请按动货筐上方暗灯";
                    $scope.newBasketName = "已成功扫描车牌" + info + "在残品货筐位置;请按动货筐上方暗灯";
                    $scope.digitalStep = "canpin";
                    $("#canPinId").css("background","#00B050");
                }
                if($scope.storageTypeName == "INVENTORY") {
                    //$scope.newBasketNo = "已成功扫描车牌" + $scope.newbasketNo + "在无法扫描货筐位置;请按动货筐上方暗灯";
                    $scope.newBasketName = "已成功扫描车牌" + info + "在无法扫描货筐位置;请按动货筐上方暗灯";
                    $scope.digitalStep = "unScan";
                    $("#unScanId").css("background","#00B050");
                }
                if($scope.storageTypeName == "PENDING") {
                    //$scope.newBasketNo = "已成功扫描车牌" + $scope.newbasketNo + "在待调查货筐位置;请按动货筐上方暗灯";
                    $scope.newBasketName = "已成功扫描车牌" + info + "在待调查货筐位置;请按动货筐上方暗灯";
                    $scope.digitalStep = "reserve";
                    $("#toReserachId").css("background","#00B050");
                }
                $scope.clickNewBasketLight = $interval(function () {
                    if(lightResult == 1){
                        lightResult = 0;
                        $interval.cancel($scope.clickNewBasketLight);
                        $scope.newbakDigitalWindows.close();
                        clearLight();
                        $scope.newbakWindows.close();
                        $scope.newPickUnitLoadid = data.pickUnitLoadId;
                        if (scanNewStorageStep == 1) {
                            var itemNo = $scope.itemNo;
                            if (inputAmount == 2) {//商品报残时
                                itemNo = $scope.skuDamagedNo;
                            }
                            //inputAmount = 4;
                            var type = "";
                            if (putProblemStep == 1) {//待调查，序列号无法扫描
                                type = "PENDING";
                                //$scope.windowMessage="请按动货筐上方暗灯";
                                // $("#toReserachId").css("background", "#33CCFF");
                                // sendMessage($scope.toReserachLight);//打开对应待调查货筐的灯
                                $scope.scanBasketStep = "newBasket";
                            }
                            if (putProblemStep == 2) {//商品报残
                                type = "DAMAGE";
                                //$scope.windowMessage="请按动货筐上方暗灯";
                                // $("#canPinId").css("background", "#FF0000");
                                // sendMessage($scope.damagedLight);//打开对应残品货筐的灯
                                $scope.scanBasketStep = "newBasket";
                            }
                            if (putProblemStep == 3) {//商品无法扫描
                                type = "INVENTORY";
                                //$scope.windowMessage="请按动货筐上方暗灯";
                                // $("#unScanId").css("background", "#FFC000");
                                // sendMessage($scope.unScanLight);//打开对应无法扫描货筐的灯
                                $scope.scanBasketStep = "newBasket";
                            }
                            var obj = {
                                pickId: $scope.pickId,
                                itemNo: itemNo,
                                amountPicked: 1,
                                type: type,
                                operation:$scope.operation
                            };
                            handleProblemItem(obj);
                        } else {
                            $("#canPinId").css("background", "#D9D9D9");
                            $("#unScanId").css("background", "#D9D9D9");
                            $("#toReserachId").css("background", "#D9D9D9");
                            $scope.basketOperate = "sku";
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            $scope.fullBasketBnt = false;
                            setTimeout(function () {
                                $("#skuId").focus(), 200
                            });
                        }
                    }
                },100)
/*
                $scope.clickNewBasketLight = $interval(function () {
                    if(lightResult == 1){
                        lightResult = 0;
                        $interval.cancel($scope.clickNewBasketLight);
                        $scope.newbakDigitalWindows.close();
                        clearLight();
                        $scope.newbakWindows.close();
                        $scope.newPickUnitLoadid = data.pickUnitLoadId;
                        if (scanNewStorageStep == 1) {
                            var itemNo = $scope.itemNo;
                            if (inputAmount == 2) {//商品报残时
                                itemNo = $scope.skuDamagedNo;
                            }
                            //inputAmount = 4;
                            var type = "";
                            if (putProblemStep == 1) {//待调查，序列号无法扫描
                                type = "PENDING";
                                $("#toReserachId").css("background", "#33CCFF");
                                $scope.windowMessage="请按动货筐上方暗灯";
                                sendMessage($scope.toReserachLight);//打开对应待调查货筐的灯
                            }
                            if (putProblemStep == 2) {//商品报残
                                type = "DAMAGE";
                                $("#canPinId").css("background", "#FF0000");
                                $scope.windowMessage="请按动货筐上方暗灯";
                                sendMessage($scope.damagedLight);//打开对应残品货筐的灯
                            }
                            if (putProblemStep == 3) {//商品无法扫描
                                type = "INVENTORY";
                                $("#unScanId").css("background", "#FFC000");
                                $scope.windowMessage="请按动货筐上方暗灯";
                                sendMessage($scope.unScanLight);//打开对应无法扫描货筐的灯
                            }
                            var obj = {
                                pickId: $scope.pickId,
                                itemNo: itemNo,
                                amountPicked: 1,
                                type: type,
                                operation:$scope.operation
                            };
                            handleProblemItem(obj);
                        } else {
                            $("#canPinId").css("background", "#D9D9D9");
                            $("#unScanId").css("background", "#D9D9D9");
                            $("#toReserachId").css("background", "#D9D9D9");
                            $scope.basketOperate = "sku";
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            setTimeout(function () {
                                $("#skuId").focus(), 200
                            });
                        }
                    }
                },100)*/
            }, function (data) {
                $scope.newbakWindows.close();
                if (data.key == "GOODS_EXIST_IN_LOCATION") {
                    if ($scope.storageTypeName == "DAMAGE") {
                        //残品筐里有商品
                        openDamagedWindow(data, info);
                        scanDamagedStep = 2;
                    }
                    if ($scope.storageTypeName == "INVENTORY") {
                        openUnScanWindow(data, info);
                        scanUnscanStep = 2;
                    }
                    if ($scope.storageTypeName == "PENDING") {
                        openToReaservhWindow(data, info);
                        scanInventeorStep = 2;
                    }
                }
                if (data.key == "UNITLOAD_ALREADY_BINDED_TO_STATION") {
                    if ($scope.storageTypeName == "DAMAGE") {
                        //残品筐已被绑定
                        openDamagedScanWindow(data, info);
                        scanDamagedStep = 2;
                    }
                    if ($scope.storageTypeName == "INVENTORY") {
                        openUnScanBindWindow(data, info);
                        scanUnscanStep = 2;
                    }
                    if ($scope.storageTypeName == "PENDING") {
                        openToReaservhBindWindow(data, info);
                        scanInventeorStep = 2;
                    }
                }
                if (data.key == "NO_SUCH_UNITLOAD") {
                    if ($scope.storageTypeName == "DAMAGE") {
                        //无效的残品筐
                        openDamagedNoneWindow(info);
                        scanDamagedStep = 2;
                    }
                    if ($scope.storageTypeName == "INVENTORY") {
                        openUnScanNoneWindow(info);
                        scanUnscanStep = 2;
                    }
                    if ($scope.storageTypeName == "PENDING") {
                        openToReaservhNoneWindow(info);
                        scanInventeorStep = 2;
                    }
                }
                if (data.key == "UNITLOAD_IS_LOCKED") {
                    if ($scope.storageTypeName == "DAMAGE") {
                        //无效的残品筐
                        openDamagedLockWindow(info);
                        scanDamagedStep = 2;
                    }
                    if ($scope.storageTypeName == "INVENTORY") {
                        openUnScanLockWindow(info);
                        scanUnscanStep = 2;
                    }
                    if ($scope.storageTypeName == "PENDING") {
                        openToReaservhLockWindow(info);
                        scanInventeorStep = 2;
                    }
                }
            });
        }
        //扫描新框后安灯
        $scope.scanNewBasketDigital = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var newBaketDigitalNo = $scope.newBaketDigitalNo;
            $scope.scanDigitalWindows.close();
            $scope.newbakDigitalWindows.close();
            pickToPackService.scanDigitalName(newBaketDigitalNo,function (data) {
                if(($scope.digitalStep == "canpin" && $scope.damagedLight == data.digitalId) || ($scope.digitalStep == "unScan" && $scope.unScanLight == data.digitalId) || ($scope.digitalStep == "reserve" && $scope.toReserachLight == data.digitalId)){
                    clearLight();
                    $scope.newPickUnitLoadid = data.pickUnitLoadId;
                    if (scanNewStorageStep == 1) {
                        var itemNo = $scope.itemNo;
                        if (inputAmount == 2) {//商品报残时
                            itemNo = $scope.skuDamagedNo;
                        }
                        //inputAmount = 4;
                        var type = "";
                        if (putProblemStep == 1) {//待调查，序列号无法扫描
                            type = "PENDING";
                            //$scope.windowMessage="请按动货筐上方暗灯";
                            // $("#toReserachId").css("background", "#33CCFF");
                            // sendMessage($scope.toReserachLight);//打开对应待调查货筐的灯
                            $scope.scanBasketStep = "newBasket";
                        }
                        if (putProblemStep == 2) {//商品报残
                            type = "DAMAGE";
                            //$scope.windowMessage="请按动货筐上方暗灯";
                            // $("#canPinId").css("background", "#FF0000");
                            // sendMessage($scope.damagedLight);//打开对应残品货筐的灯
                            $scope.scanBasketStep = "newBasket";
                        }
                        if (putProblemStep == 3) {//商品无法扫描
                            type = "INVENTORY";
                            //$scope.windowMessage="请按动货筐上方暗灯";
                            // $("#unScanId").css("background", "#FFC000");
                            // sendMessage($scope.unScanLight);//打开对应无法扫描货筐的灯
                            $scope.scanBasketStep = "newBasket";
                        }
                        var obj = {
                            pickId: $scope.pickId,
                            itemNo: itemNo,
                            amountPicked: 1,
                            type: type,
                            operation:$scope.operation
                        };
                        handleProblemItem(obj);
                    } else {
                        $("#canPinId").css("background", "#D9D9D9");
                        $("#unScanId").css("background", "#D9D9D9");
                        $("#toReserachId").css("background", "#D9D9D9");
                        $scope.basketOperate = "sku";
                        $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                        $scope.fullBasketBnt = false;
                        setTimeout(function () {
                            $("#skuId").focus(), 200
                        });
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalErrorMsg = "扫描的位置标签"+newBaketDigitalNo+"不匹配，请重新扫描";
                    $scope.digitalOperate = "newBaseketDigital";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function (){$("#newBaseketDigitaId").focus()});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "newBaseketDigital";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function (){$("#newBaseketDigitaId").focus()});
            });
            $scope.newBaketDigitalNo = "";
        }
        //待调查商品货筐确定，进入桉灯
        $scope.continueToReserach = function () {
            $scope.reserachWindows.close();
            //绑定货筐
            pickToPackService.bindStorage($scope.toReserachBasketNumber, $scope.stationNo, "PENDING", function (data) {
                $scope.toInvestigatePId = data.pickUnitLoadId;
                $scope.windowMessage = "请按动待调查商品货筐上方暗灯";
                $("#toReserachId").css("background","#00B050");
                $scope.basketOperate = "rescanToReserachBasketDigital";
                setTimeout(function () {
                    $("#reToReserachBasketDigitalId").focus()
                }, 200);
                $scope.clickPendingLight1 = $interval(function () {
                     if (lightResult == 1) {
                        lightResult = 0;
                         clearLight();
                         $interval.cancel($scope.clickPendingLight1);
                        if (putProblemStep == 0) {
                            if (scanInventeorStep == 1) {
                                $("#toReserachId").css("background", "#D9D9D9");
                                $scope.toReserachBasket = "";
                                $scope.basketOperate = "pod";
                                $scope.windowMessage = "等待Pod进入";
                                setTimeout(function () {
                                    $("#podId").focus();
                                }, 200);
                                if(podValue == 0){
                                    getPodResult();
                                    podValue = 1;
                                }
                            }
                            if (scanInventeorStep == 2) {
                                $("#toReserachId").css("background", "#D9D9D9");
                                $scope.basketOperate = "sku";
                                $("#toReserachId").css("background", "#D9D9D9");
                                $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                                setTimeout(function () {
                                    $("#skuId").focus()
                                }, 200);
                            }
                        }
                        if (putProblemStep == 1) {
                            var obj = {
                                pickId: $scope.pickId,
                                itemNo: $scope.itemNo,
                                amountPicked: 1,
                                type: "PENDING",
                                operation:$scope.operation
                            };
                            $scope.scanBasketStep = "newBasket";
                            handleProblemItem(obj);
                        }
                    }
                });
            });
        }
        //扫描新的带调查框的位置标签
        $scope.rescanToReserachBasketDigitalName = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var newReserachDigital = $scope.newReserachBasketDigitalName;
            $scope.scanDigitalWindows.close();
            pickToPackService.scanDigitalName(newReserachDigital,function (data) {
                if($scope.toReserachLight == data.digitalId){
                    clearLight();
                    if (putProblemStep == 0) {
                        if (scanInventeorStep == 1) {
                            $("#toReserachId").css("background", "#D9D9D9");
                            $scope.toReserachBasket = "";
                            $scope.basketOperate = "pod";
                            $scope.windowMessage = "等待Pod进入";
                            setTimeout(function () {
                                $("#podId").focus();
                            }, 200);
                            if(podValue == 0){
                                getPodResult();
                                podValue = 1;
                            }
                        }
                        if (scanInventeorStep == 2) {
                            $("#toReserachId").css("background", "#D9D9D9");
                            $scope.basketOperate = "sku";
                            $("#toReserachId").css("background", "#D9D9D9");
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            setTimeout(function () {
                                $("#skuId").focus()
                            }, 200);
                        }
                    }
                    if (putProblemStep == 1) {
                        var obj = {
                            pickId: $scope.pickId,
                            itemNo: $scope.itemNo,
                            amountPicked: 1,
                            type: "PENDING",
                            operation:$scope.operation
                        };
                        $scope.scanBasketStep = "newBasket";
                        handleProblemItem(obj);
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalErrorMsg = "扫描的位置标签"+newReserachDigital+"不匹配，请重新扫描";
                    $scope.digitalOperate = "rescanToReserachBasketDigital1";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#reToReserachBasketDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "rescanToReserachBasketDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#reToReserachBasketDigitalId1").focus();});
            });
            $scope.newReserachBasketDigitalName = "";
        }
        //无法扫描商品货筐确定，进入桉灯
        $scope.continueUnScan = function () {
            $scope.unScanWindows.close();
            //绑定货筐
            pickToPackService.bindStorage($scope.unScanBasketNumber, $scope.stationNo, "INVENTORY", function (data) {
                $scope.canNotScanPId = data.pickUnitLoadId;
                $scope.windowMessage = "请按动无法扫描商品货筐上方暗灯";
                $("#unScanId").css("background", "#00B050");
                $scope.basketOperate = "notScanItemInBasketDigital";
                setTimeout(function () {
                    $("#notScanItemInBasketDigitalId").focus();
                }, 100);
                $scope.clickInventoryLight2 = $interval(function () {
                    if (lightResult == 1) {
                        clearLight();
                        $interval.cancel($scope.clickInventoryLight2);
                        if (putProblemStep == 0) {
                            if (scanUnscanStep == 1) {
                                initScanGenuine();
                            }
                            if (scanUnscanStep == 2) {
                                $("#unScanId").css("background", "#D9D9D9");
                                $scope.basketOperate = "sku";
                                $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                                setTimeout(function () {
                                    $("#skuId").focus()
                                }, 200);
                                lightResult = 0;
                            }
                        }
                        if (putProblemStep == 3) {
                            var amount = 1;
                            if ($scope.pickNoInput != null && $scope.pickNoInput != "") {
                                amount = $scope.pickNoInput;
                            }
                            var obj = {
                                pickId: $scope.pickId,
                                itemNo: "",
                                amountPicked: amount,
                                type: "INVENTORY",
                                operation:$scope.operation
                            };
                            lightResult = 0;
                            $scope.scanBasketStep = "newBasket";
                            handleProblemItem(obj);
                        }
                    }
                });
            });
        }
        //无法扫描有商品时确定，扫描位置标签
        $scope.scanNotScanItemInBasketDigital = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var notScanItemInBasketDigitalNo = $scope.notScanItemInBasketDigitalNo;
            $scope.scanDigitalWindows.close();
            pickToPackService.scanDigitalName(notScanItemInBasketDigitalNo,function (data) {
                if($scope.unScanLight == data.digitalId){
                    clearLight();
                    if (putProblemStep == 0) {
                        if (scanUnscanStep == 1) {
                            unScanLight1 = 1;
                            initScanGenuine();
                        }
                        if (scanUnscanStep == 2) {
                            $("#unScanId").css("background", "#D9D9D9");
                            $scope.basketOperate = "sku";
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            setTimeout(function () {
                                $("#skuId").focus()
                            }, 200);
                        }
                    }
                    if (putProblemStep == 3) {
                        var amount = 1;
                        if ($scope.pickNoInput != null && $scope.pickNoInput != "") {
                            amount = $scope.pickNoInput;
                        }
                        var obj = {
                            pickId: $scope.pickId,
                            itemNo: "",
                            amountPicked: amount,
                            type: "INVENTORY",
                            operation:$scope.operation
                        };
                        $scope.scanBasketStep = "newBasket";
                        handleProblemItem(obj);
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalErrorMsg = "扫描的位置标签"+notScanItemInBasketDigitalNo+"不匹配，请重新扫描";
                    $scope.digitalOperate = "notScanItemInBasketDigital1";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#notScanItemInBasketDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "notScanItemInBasketDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#notScanItemInBasketDigitalId1").focus();});
            });
            $scope.notScanItemInBasketDigitalNo = "";
        }
        //残品货筐确定，进入桉灯
        $scope.continueCanPin = function () {
            $scope.canPinWindows.close();
            //绑定货筐
            pickToPackService.bindStorage($scope.canPinBasketNumber, $scope.stationNo, "DAMAGE", function (data) {
                $scope.damagedPid = data.pickUnitLoadId;
                $scope.windowMessage = "请按动残品货筐上方暗灯";
                $("#canPinId").css("background", "#00B050");
                $scope.basketOperate = "canpinItemInBasketDigital";
                setTimeout(function () {
                    $("#canpinItemInBasketDigitalId").focus()
                }, 200);
                $scope.clickDamageLight2 = $interval(function () {
                    if (lightResult == 1) {
                        clearLight();
                        $interval.cancel($scope.clickDamageLight2);
                        if (putProblemStep == 0) {
                            if (scanDamagedStep == 1) {
                                initScanDamaged();
                            }
                            if (scanDamagedStep == 2) {
                                $("#canPinId").css("background", "#D9D9D9");
                                $scope.basketOperate = "sku";
                                $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                                setTimeout(function () {
                                    $("#skuId").focus()
                                }, 200);
                                lightResult = 0;
                            }
                        }
                        if (putProblemStep == 2) {
                            var amount = 1;
                            if ($scope.pickNoInput != null && $scope.pickNoInput != "") {
                                amount = $scope.pickNoInput;
                            }
                            var obj = {
                                pickId: $scope.pickId,
                                itemNo: "",
                                amountPicked: amount,
                                type: "DAMAGE",
                                operation:$scope.operation
                            };
                            lightResult = 0;
                            $scope.scanBasketStep = "newBasket";
                            handleProblemItem(obj);
                        }
                    }
                });
            });
        }
        //残品货框有商品时，确定后扫描位置标签
        $scope.scanCanpinItemInBasketDigital = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var canPinItemInBasketDigital = $scope.canPinItemBasketDigitalNo;
            $scope.scanDigitalWindows.close();
            pickToPackService.scanDigitalName(canPinItemInBasketDigital,function (data) {
                if($scope.damagedLight == data.digitalId){
                    clearLight();
                    if (putProblemStep == 0) {
                        if (scanDamagedStep == 1) {
                            lightResult = 1;
                            initScanDamaged();
                        }
                        if (scanDamagedStep == 2) {
                            $("#canPinId").css("background", "#D9D9D9");
                            $scope.basketOperate = "sku";
                            $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
                            setTimeout(function () {
                                $("#skuId").focus()
                            }, 200);
                        }
                    }
                    if (putProblemStep == 2) {
                        var amount = 1;
                        if ($scope.pickNoInput != null && $scope.pickNoInput != "") {
                            amount = $scope.pickNoInput;
                        }
                        var obj = {
                            pickId: $scope.pickId,
                            itemNo: "",
                            amountPicked: amount,
                            type: "DAMAGE",
                            operation:$scope.operation
                        };
                        $scope.scanBasketStep = "newBasket";
                        handleProblemItem(obj);
                    }
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalErrorMsg = "扫描的位置标签"+canPinItemInBasketDigital+"不匹配，请重新扫描";
                    $scope.digitalOperate = "canpinItemInBasketDigital1";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#canpinItemInBasketDigitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "canpinItemInBasketDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#canpinItemInBasketDigitalId1").focus();});
            });
            $scope.canPinItemBasketDigitalNo = "";
        }
        //残品货筐请求确认时取消，重新扫描货筐
        $scope.cancel = function () {
            $scope.canPinWindows.close();
            $scope.canPinBasket = "";
            $scope.windowMessage = "请重新扫描残品货筐";
            setTimeout(function () {
                $("#canPinBasketId").focus();
            }, 200);
        }
        //无法扫描商品货筐请求确认时取消，重新扫描货筐
        $scope.unScanclose = function () {
            $scope.unScanWindows.close();
            $scope.unScanSKUBasket = "";
            $scope.windowMessage = "请重新扫描无法扫描商品货筐";
            setTimeout(function () {
                $("#unScanSKUBasketId").focus();
            }, 200);
        }
        //待调查商品货筐请求确认时取消，重新扫描货筐
        $scope.toReserachclose = function () {
            $scope.reserachWindows.close();
            $scope.toReserachBasket = "";
            $scope.windowMessage = "请重新扫描待调查商品货筐";
            setTimeout(function () {
                $("#toReserachBasketId").focus();
            }, 200);
        }
        //////////////////////////////////////////////////////////////问题处理按钮//////////////////////////////////////
        //商品丢失
        $scope.skuMiss = function () {
            $scope.scanBinOperate = "1";
            $scope.bin = "";
            $scope.qusetionWindows.close();
            $scope.openWindow({
                windowId: "scanHuoWeiId",
                windowName: $scope.scanHuoWeiWindows,
                windowClass: "blueWindow",
                width: 700,
                height: 300,
                closeable: true,
                activate: function () {
                    $("#huoWeiId").focus();
                },
            })

        }
        //货筐已满
        $scope.unitLoadFull = function () {
            $scope.qusetionWindows.close();
            //获取该站台3个筐目前的信息
            pickToPackService.getBakInfo($scope.stationNo, function (data) {
                $scope.bakInfo = data.pickingUnitLoadResults;
            });
            $scope.scanFullBakWindow("scanFullId", $scope.scanFullWindows, function () {
                $("#fullBasketId").focus()
            }, function () {
                // $scope.skuNo="";
                $timeout(function () {
                    $("#skuId").focus();
                })
                $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
            });
            $scope.windowMessage = "请扫描已满货筐";
        }
        //商品无法扫描
        $scope.canNotScan = function () {
            $scope.qusetionWindows.close();
            var obj = {
                "pickId": $scope.pickId,
                "itemNo": "",
                "amountPicked": $scope.pickNo,
                "type": "INVENTORY"
            };
            pickToPackService.checkDamagedItem(obj, function (data) {
                var type = "INVENTORY";
                inputAmount = 3;
                $("#unScanId").css("background", "#FFC000");
                sendMessage($scope.unScanLight);//打开对应无法扫描货筐的灯
                checkProblemItemSuccess(type);

            }, function (data) {
                if (data.key == "SAME_ITEMNAME_DIFFERENT_LOT") {
                    putProblemStep = 3;
                    $scope.errorWindow("changeBakId", $scope.changeBakWindows);
                    $scope.BasketFullMessaage = "货筐号码：" + data.values[0] + "中存在相同名称不同有效期商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
                if (data.key == "SAME_ITEM_DIFFERENT_CLIENT") {
                    putProblemStep = 3;
                    $scope.errorWindow("changeBakId", $scope.changeBakWindows);
                    $scope.BasketFullMessaage = "货筐号码： " + data.values[0] + "中存在相同名称不同客户商品，请点击“货筐已满”结满当前货筐，扫描空的货筐后再放置商品";
                }
            })
        }
        //报告暗灯
        $scope.clickLight = function () {
            $scope.qusetionWindows.close();
            //获取暗灯列表
            outboundService.getAndonList(function (data) {
                var andonList = [];
                $scope.andonSize = data.length;
                for (var k = 0; k < $scope.andonSize; k++) {
                    if (data[k].name == "扫描枪存在问题" || data[k].name == "商品丢失" || data[k].name == "商品需要录入有效期") {
                        continue;
                    }
                    andonList.push(data[k]);
                }
                var size = andonList.length;
                $scope.div1 = [];
                $scope.div2 = [];
                for (var i = 0; i < size; i++) {
                    andonList[i]["no"] = i + 1;
                    if (i < size / 2) {
                        $scope.div1.push(andonList[i]);
                    } else {
                        $scope.div2.push(andonList[i]);
                    }
                }
                $scope.openWindow({
                    windowId: "lightMenuId",
                    windowName: $scope.lightWindows,
                    windowClass: "blueWindow",
                    width: 800,
                    height: 500,
                    closeable: true,
                    title: "请选择暗灯菜单"
                })
            });
        }
        //信息查询
        $scope.infoCheck = function () {
            $scope.qusetionWindows.close();
            $scope.openWindow({
                windowId: "informationInquiryId",
                windowName: $scope.informationInquiryWindow,
                windowClass: "blueWindow",
                width: 700,
                height: 350,
                closeable: true,
            })
            $scope.userName = $window.localStorage['username'];
            $scope.operationTime = "";
            $scope.totalOperating = "";
            $scope.operationalEfficiency = "";
            $scope.target = "";
            $scope.conclude = "";
            $scope.onAPickPackWall = $scope.pickPackCarNo;
            $scope.onAPod = $scope.podNo;
            $scope.onAPallet = $scope.fromLocationName;
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
        }

        //检查所有的订单是否完成
        $scope.checkShipmemt = function () {
            $scope.stockUnitlossWindows.close();
            $scope.qusetionWindows.close();
            pickToPackService.checkShipment($scope.logicStationId,function (data) {
                var shipmentList = data;
                $scope.shipmentArr = "";
                for(var i = 0; i<shipmentList.length; i++){
                    var data1 = shipmentList[i];
                    sendMessage(data1.digitalId,data1.shipmentId,data1.pickPackWallId);
                    $scope.shipmentArr += data1.shipmentNo + ",";
                }
                $scope.stockUnitState = "loss";
                $scope.stockShipmentState = $scope.shipmentArr.substring(0,$scope.shipmentArr.length-1)+"订单中的商品库存不够，已转交包装";
                $scope.errorWindow("stockUnitlossId", $scope.stockUnitlossWindows);

            },function (data) {
                $scope.stockUnitState = "loss";
                $scope.stockShipmentState = data.values[0];
                $scope.errorWindow("stockUnitlossId", $scope.stockUnitlossWindows);
            });
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////报告暗灯////////////////////////////////////////////////////////
        //报告暗灯按钮点击事件
        $scope.clickAndon = function (andonType) {
            $scope.andonType = andonType;
            if (andonType.name == "货位条码无法扫描") {
                $scope.lightWindows.close();
                if($scope.podTypeName != "POD_TYPE_PALLET"){ //不是托盘货位
                    $scope.openWindow({
                        windowId: "scanOtherBinId",
                        windowName: $scope.scanOtherBinWindows,
                        windowClass: "blueWindow",
                        width: 800,
                        height: 300,
                        title: "请选择需要扫描的货位位置",
                        closeable: true,
                    })
                }else{//是托盘货位 直接报暗灯
                    outboundService.getStorage($scope.fromLocationName, function (data) {
                        callAnDon($scope.andonType, data.id);
                    })
                }
            } else {
                $scope.lightWindows.setOptions({
                    title: "请扫描货位条码"
                })
                $scope.scanBinOperate = "scanBinNoOperate";
                $timeout(function () {
                    $("#scanBinNumberId").focus();
                })
            }
        }
        //扫描正上方货位按钮
        $scope.scanUpBin = function () {
            clickBinButton = 1;
            $scope.scanOtherLocationOperate = "";
            $scope.scanOtherBinOperate = "scanOtherBinNoOperate";
            $scope.scanOtherBinWindows.setOptions({
                title: "请扫描货位条码"
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
                title: "请扫描货位条码"
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
                title: "请扫描货位条码"
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
                title: "请扫描货位条码"
            })
            $timeout(function () {
                $("#scanOtherBinNumberId").focus();
            })
        }
        //暗灯1-8，扫描货位上报暗灯
        $scope.scanBinNumber = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var storageLocaltion = $scope.binNumber.toUpperCase();
            //获取storageLocaltionId
            outboundService.getStorage(storageLocaltion, function (data) {
                callAnDon($scope.andonType, data.id);
            })
        }
        //货位无法扫描，扫描其他货位
        $scope.scanOtherBinNumber = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var storageName = $scope.otherBinNumber.toUpperCase();
            var level = storageName.substring(9, 10);
            var coloum = storageName.substring(10);
            if (clickBinButton == 1) {//扫描正上方货位
                level = String.fromCharCode(storageName.substring(9, 10).charCodeAt() - 1);
                storageName = storageName.substring(0, 9) + level + storageName.substring(10);
            }
            if (clickBinButton == 2) {//扫描正左方货位
                coloum = parseInt(coloum) + 1;
                if (coloum < 9) {
                    storageName = storageName.substring(0, 10) + "0" + coloum;
                } else {
                    storageName = storageName.substring(0, 10) + coloum;
                }
            }
            if (clickBinButton == 3) {//扫描正下方货位
                level = String.fromCharCode(storageName.substring(9, 10).charCodeAt() + 1);
                storageName = storageName.substring(0, 9) + level + storageName.substring(10);
            }
            if (clickBinButton == 4) {//扫描正右方货位
                coloum = parseInt(coloum) - 1;
                if (coloum < 9) {
                    storageName = storageName.substring(0, 10) + "0" + coloum;
                } else {
                    storageName = storageName.substring(0, 10) + coloum;
                }
            }
            outboundService.getStorage(storageName, function (data) {
                callAnDon($scope.andonType, data.id);
                $scope.otherBinNumber = "";
                $scope.scanOtherBinOperate = "";
                $scope.scanOtherBinWindows.close();
            }, function (data) {
                if (data.key == "EX_ANDON_MASTER_LOCATION_NAME_NOT_NULL") {
                    $scope.scanOtherLocationOperate = "1";
                    $scope.otherBinNumber = "";
                    $scope.scanOtherBinOperate = "scanOtherBinNoOperate";
                    $scope.scanOtherBinWindows.setOptions({
                        title: "请重新扫描货位条码"
                    })
                    $timeout(function () {
                        $("#scanOtherBinNumberId").focus();
                    })
                }
            })
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //确认是否结束拣货
        $scope.exit = function () {
          /*  $scope.isFinishWindows.close();
            $scope.openWindow({
                windowId: "isFullId",
                windowName: $scope.isFullAllBakWindows,
                windowClass: "blueWindow",
                width: 700,
                height: 260,
                closeable: false,
                visible: true,
            })
            pickToPackService.stopWorking($scope.stationNo);//解除用户与工作站绑定*/
            $scope.isFinishWindows.close();
            $scope.qusetionWindows.close();
            pickToPackService.stopWorking($scope.stationNo,function (data) {
                /*if(podSocket != undefined && podSocket != null && podSocket != ""){
                    podSocket.close(3666,"主动退出");
                }
                if(socket != undefined && socket != null && socket != ""){
                    socket.close(3666,"主动退出");
                }*/
                closeWebsocket(podSocket,"pod");
                closeWebsocket(socket,"digital");
                $scope.openWindow({
                    windowId: "isFullId",
                    windowName: $scope.isFullAllBakWindows,
                    windowClass: "blueWindow",
                    width: 700,
                    height: 260,
                    closeable: false,
                    visible: true,
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
        $scope.notExit = function () {
            $scope.isFinishWindows.close();
        }
        //确认结满所有货筐
        $scope.fullAll = function () {
            pickToPackService.fullAllStorage($scope.stationNo);//结满工作站下所有货筐
            $scope.isFullAllBakWindows.close();
            $state.go("main.pick");
        }
        $scope.notFullAll = function () {
            $scope.isFullAllBakWindows.close();
            $state.go("main.pick");
        }
        //货位扫描
        $scope.scanHuoWei = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            pickToPackService.scanHuoWei($scope.bin.toUpperCase(), $scope.pickId, function (data) {
                $scope.scanHuoWeiWindows.close();
                $scope.bin = "";
                $scope.openWindow({
                    windowId: "scanEachSKUId",
                    windowName: $scope.scanEachSKUWindows,
                    windowClass: "blueWindow",
                    width: 700,
                    height: 400,
                    closeable: true,
                    activate: function () {
                        $("#eachSKUId").focus();
                    },
                })
            }, function (data) {
                if (data.key == "WRONG_STORAGELOCATION") {
                    $scope.bin = "";
                    $scope.scanBinOperate = "2";
                }
            })
        }
        //货位无法扫描
        $scope.canNotScanBin = function () {
            $scope.scanHuoWeiWindows.close();
            $scope.bin = "";
            $scope.openWindow({
                windowId: "scanEachSKUId",
                windowName: $scope.scanEachSKUWindows,
                windowClass: "blueWindow",
                width: 700,
                height: 400,
                closeable: true,
                activate: function () {
                    $("#eachSKUId").focus();
                },
            })
            callAnDon({
                name: "货位条码无法扫描",
                id: "5901d872-f295-11e6-8e32-0242ac110014"
            }, $scope.fromLocationNameId)

        }
        //逐一扫描货位里的商品
        $scope.scanEachSKU = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var obj = {
                "pickId": $scope.pickId,
                "itemNo": $scope.eachSKUNo,
                "amountPicked": "",
                "type": ""
            };
            $scope.scanEachSKUWindows.close();
            $scope.scanEverySKUWindows.close();
            pickToPackService.scanSKU(obj, function (data) {
                sendMessage($scope.pickPackCellLight);
                checkItemSuccess();
            }, function (data) {
                $scope.eachSKUNumber = $scope.eachSKUNo;
                $scope.eachSKUNo = "";
                $scope.openWindow({
                    windowId: "scanEverySKUId",
                    windowName: $scope.scanEverySKUWindows,
                    windowClass: "myWindow",
                    width: 700,
                    height: 400,
                    closeable: true,
                    activate: function () {
                        $("#everySKUId").focus();
                    },
                })
            })
        }
        //商品扫描错误，继续每一件扫描
        $scope.scanEverySKU = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var obj = {
                "pickId": $scope.pickId,
                "itemNo": $scope.everySKUNo,
                "amountPicked": "",
                "type": ""
            };
            $scope.scanEverySKUWindows.close();
            $scope.scanEachSKUWindows.close();
            pickToPackService.scanSKU(obj, function (data) {
                checkItemSuccess();
            }, function (data) {
                $scope.eachSKUNumber = $scope.everySKUNo;
                $scope.everySKUNo = "";
                $scope.openWindow({
                    windowId: "scanEverySKUId",
                    windowName: $scope.scanEverySKUWindows,
                    windowClass: "myWindow",
                    width: 700,
                    height: 400,
                    closeable: true,
                    activate: function () {
                        $("#everySKUId").focus();
                    },
                })
            })
        }
        //货位为空，留个接口，功能和已扫描完所有商品按钮一样
        $scope.positionEmpry = function () {
            $scope.scanEachSKUWindows.close();
            $scope.bin = "";
            //取消该拣货单，跳转下一个拣货任务
            pickToPackService.haveScanedAllSKU($scope.pickId,$scope.operation, function (data) {
                initOrderPosition();
            });
        }
        //已扫描完所有商品
        $scope.haveScanedAllSKU = function () {
            $scope.scanEverySKUWindows.close();
            $scope.eachSKUNo = "";
            //取消该拣货单，跳转下一个拣货任务
            pickToPackService.haveScanedAllSKU($scope.pickId,$scope.operation, function (data) {
                initOrderPosition();
            });
        }
        //根据位置标签查找灯的信息
        $scope.scanDigitalName = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            $scope.cellLightName = $scope.digitalName;
            $scope.scanDigitalWindows.close();
            pickToPackService.scanpickPackCellDigital($scope.cellLightName,function (data) {
                if($scope.pickPackCellLight == data.digitalId){

                    $scope.comfirmBnt = false;
                    if ($scope.shipmentState == 600) {
                        console.log(date.toLocaleString()+"->订单完成，给包装发送消息shipmentId-->digitalId：",$scope.shipmentId,$scope.pickPackCellLight2);
                        sendMessage($scope.pickPackCellLight2,$scope.shipmentId,$scope.pickPackWallId);//让pack包装的cell格子的灯亮
                    }

                    //拍灯后，获取拣货单
                    //initOrderPosition();
                    getlightOffPosition($scope.nextPodNo);

                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalErrorMsg = "扫描的pickPackCell标签:"+$scope.cellLightName+"不匹配，请重新扫描";
                    $scope.digitalOperate = "scanDigital1";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#digitalId1").focus();});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "scanDigital1";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function () {$("#digitalId1").focus();});
            });
            $scope.digitalName = "";
        }
        ///////////////////////////////////////////////////////////////内部函数////////////////////////////////////////////////////
        //取商品，获取下一个拣货任务
        function putItem() {
            $scope.basketOperate = "scanDigital";
            $scope.windowMessage = "请将商品放到指定的Pick-Pack Wall,并按动Pick-Pack Wall上方暗灯";
            setTimeout(function () {
                $("#digitalId").focus();
            }, 100);
            $scope.comfirmBnt = true;
            //查询拣货单，若没有拣货单则释放pod，获取到新的pod，若有拣货单则不做操作
            //getPickOrderDetail();
            pickToPackService.getOrderPosition($scope.podNo, $scope.sectionId, $scope.stationNo, function (data) {
                if(data.message == "noPod"){
                    $scope.podDetail = "noPod";
                    closeWebsocket(podSocket,"pod");
                }else{
                    $scope.nextPodNo = data.socketPodName;
                }
                $scope.clickPickPackWallLight = $interval(function () {
                    if (lightResult == 1) {
                        lightResult = 0;
                        clearLight();
                        $interval.cancel($scope.clickPendingLight);
                        $interval.cancel($scope.clickPickPackWallLight);
                        $scope.comfirmBnt = false;
                        if ($scope.shipmentState == 600) {
                            console.log(date.toLocaleString()+"->订单完成，给包装发送消息shipmentId-->digitalId：",$scope.shipmentId,$scope.pickPackCellLight2);
                            sendMessage($scope.pickPackCellLight2,$scope.shipmentId,$scope.pickPackWallId);//让pack包装的cell格子的灯亮
                        }

                        //拍灯后，获取拣货单
                        //initOrderPosition();
                        getlightOffPosition(data.socketPodName);
                    }
                }, 100);
            })

        }

        function  getPickOrderDetail() {
                //获取取货单信息
            pickToPackService.getOrderPosition($scope.podNo, $scope.sectionId, $scope.stationNo, function (data) {
                if(data.message == "noPod"){
                    $scope.podDetail = "noPod";
                    closeWebsocket(podSocket,"pod");
                }
            })
        }
        function getlightOffPosition(podNo) {
            $scope.nextPodNo = "";
            $scope.nextPodMsg = "";
            if($scope.podDetail == "noPod"){  //若后没有pod了，则清除页面
                $scope.podDetail = "";
                $scope.podMessage = "POD";
                $scope.podRows = [];
                initPickPackWall($scope.pickPackWallInfo);
                initMainCallPod();
                getPodResult();
            }else {
                $scope.podDetail = "";
                //$scope.refreshNewPod();
                callNewPod(podNo);
            }
        }


        //清除拣货页面信息，重新获取拣货单
        function initOrderPosition() {
            console.log(date.toLocaleString()+"->进入清除页面，重新获取拣货单。。。");
            /*  //清除原有信息
              $scope.itemNo = "";
              $scope.skuNo = "";
              $scope.skuName = "";
              $scope.skuOperate = "skuNo";
              $scope.basketOperate = "sku";
              $scope.pickNoInput = "";
              $scope.pickNo = "";
             // $scope.skuImg = "";
              $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
              $scope.podRows = [];
              $scope.podMessage = "POD";
              getIsCallPod();
              setTimeout(function () {
                  $("#skuId").focus()
              }, 200);*/

            $scope.pickPackStation = {};
            $scope.pickpackcar = {};
            $scope.pickPackOperate = {};
            $scope.pickPackMain = "pickPackMain";
            $scope.skuOperate = "skuNo";
            $scope.skuNo = "";
            $scope.skuName = "";
            $scope.basketOperate = "pod";
            //$scope.podNo = "";
            $scope.pickNoInput = "";
            $scope.pickNo = "";
            $scope.windowMessage = "等待Pod进入";
            $scope.podMessage = "POD";
            setTimeout(function () {
                $("#podId").focus();
            }, 200);
            if(podValue == 0){
                getPodResult();
                podValue = 1;
            }
            initPickPackWall($scope.pickPackWallInfo);
            getIsCallPod();

            $scope.getOrderPosition($scope.podNo, $scope.sectionId,$scope.stationNo);//获取取货单信息
        }

        //初始化pod
        function initPod(data) {
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

        // 错误弹窗
        $scope.errorWindow = function (windowId, windowName, focusInput) {
            $("#" + windowId).parent().addClass("myWindow");
            windowName.setOptions({
                width: 700,
                height: 250,
                visible: false,
                closable: true,
                actions: ["Close"],
                activate: focusInput,
            });
            windowName.center();
            windowName.open();
        }
        $scope.chooseTypeWindow = function (windowId, windowName) {
            $("#" + windowId).parent().addClass("myWindow");
            windowName.setOptions({
                width: 900,
                height: 400,
                visible: false,
                closable: true,
                actions: ["Close"],
            });
            windowName.center();
            windowName.open();
        }
        // 扫描已满货筐弹窗
        $scope.scanFullBakWindow = function (windowId, windowName, focusInput, closefunction) {
            $("#" + windowId).parent().addClass("myWindow");
            windowName.setOptions({
                width: 700,
                height: 400,
                closeable: true,
                visible: false,
                actions: ["Close"],
                activate: focusInput,
                close: closefunction,
            });
            windowName.center();
            windowName.open();
        }
        //扫描序列号弹窗
        $scope.seriesWindow = function (windowId, windowName, focusInput) {
            $("#" + windowId).parent().addClass("myWindow");
            windowName.setOptions({
                width: 700,
                height: 260,
                visible: false,
                actions: ["Close"],
                closeable: true,
                activate: focusInput,
            });
            windowName.center();
            windowName.open();
        }
        //问题处理弹窗
        $scope.problemShow = function () {
            $("#qusetionMenuId").parent().addClass("blueWindow");
            $scope.qusetionWindows.setOptions({
                width: 700,
                height: 420,
                closeable: true,
                actions: ["Close"],
                visible: false,
            });
            $scope.qusetionWindows.center();
            $scope.qusetionWindows.open();
        }
        //残品货筐扫描报错时弹窗
        function openDamagedWindow(data, damage) {
            $scope.reScanCanPinWindows.close();
            //残品筐里有商品
            $scope.canPinBasketNumber = damage;
            $scope.basketOperate = "scanCanPinBasket";
            $scope.windowMessage = "请确认是否使用当前残品货筐";
            $scope.damagedSkuNo = data.values[0];
            $scope.errorWindow("canPinBakId", $scope.canPinWindows, function () {
                $("#recanPinBasketId").focus();
            });
        }

        function openDamagedScanWindow(data, canPinBasket) {
            $scope.canPinWindows.close();
            //残品筐已被绑定
            $scope.basketOperate = "scanCanPinBasket";
            $scope.windowMessage = "请重新扫描残品货筐";
            $scope.stationName = (data.values)[0];
            $scope.canPinBasketNo = canPinBasket;
            $scope.canPinBasket = "";
            $scope.errorWindow("recanPinBakId", $scope.reScanCanPinWindows, function () {
                $("#recanPinBasketId").focus();
            });
            $scope.reScanCanPinOperate = "yiBangDing";
        }

        function openDamagedNoneWindow(canPinBasket) {
            $scope.canPinWindows.close();
            //无效的残品筐
            $scope.basketOperate = "scanCanPinBasket";
            $scope.windowMessage = "请重新扫描残品货筐";
            $scope.canPinBasketNo = canPinBasket;
            $scope.canPinBasket = "";
            $scope.errorWindow("recanPinBakId", $scope.reScanCanPinWindows, function () {
                $("#recanPinBasketId").focus();
            });
            $scope.reScanCanPinOperate = "wuXiao";
        }

        function openDamagedLockWindow(canPinBasket) {
            $scope.basketOperate = "scanCanPinBasket";
            $scope.windowMessage = "请重新扫描残品货筐";
            $scope.canPinBasketNo = canPinBasket;
            $scope.canPinBasket = "";
            $scope.errorWindow("recanPinBakId", $scope.reScanCanPinWindows, function () {
                $("#recanPinBasketId").focus();
            });
            $scope.reScanCanPinOperate = "locked";
        }

        //无法扫描货筐扫描报错时弹窗
        function openUnScanWindow(data, inventory) {
            $scope.reUnScanWindows.close();
            //无法扫描商品货筐里有商品
            $scope.unScanBasketNumber = inventory;
            $scope.basketOperate = "unScanSKUBasket";
            $scope.unScanSkuNo = data.values[0];
            $scope.windowMessage = "请确认是否使用当前无法扫描商品货筐";
            $scope.errorWindow("unScanBakId", $scope.unScanWindows);
        }

        function openUnScanBindWindow(data, unScanSKUBasket) {
            $scope.unScanWindows.close();
            $scope.basketOperate = "unScanSKUBasket";
            $scope.windowMessage = "请重新扫描无法扫描商品货筐";
            $scope.unScanSKUBasketNo = unScanSKUBasket;
            $scope.unScanSKUBasket = "";
            $scope.stationName = (data.values)[0];
            $scope.errorWindow("reUnScanBakId", $scope.reUnScanWindows, function () {
                $("#reunScanSKUBasketId").focus();
            });
            $scope.reUnScanOperate = "yiBangDing";
        }

        function openUnScanNoneWindow(unScanSKUBasket) {
            $scope.unScanWindows.close();
            $scope.basketOperate = "unScanSKUBasket";
            $scope.windowMessage = "请重新扫描无法扫描商品货筐";
            $scope.unScanSKUBasketNo = unScanSKUBasket;
            $scope.unScanSKUBasket = "";
            $scope.errorWindow("reUnScanBakId", $scope.reUnScanWindows, function () {
                $("#reunScanSKUBasketId").focus();
            });
            $scope.reUnScanOperate = "wuXiao";
        }

        function openUnScanLockWindow(unScanSKUBasket) {
            $scope.basketOperate = "unScanSKUBasket";
            $scope.windowMessage = "请重新扫描无法扫描商品货筐";
            $scope.unScanSKUBasketNo = unScanSKUBasket;
            $scope.unScanSKUBasket = "";
            $scope.errorWindow("reUnScanBakId", $scope.reUnScanWindows, function () {
                $("#reunScanSKUBasketId").focus();
            });
            $scope.reUnScanOperate = "locked";
        }

        //待调查货筐扫描报错时弹窗
        function openToReaservhWindow(data, pending) {
            $scope.toReserachWindows.close();
            $scope.toReserachBasketNumber = pending;
            $scope.basketOperate = "toReserachBasket";
            $scope.toReserachSkuNo = data.values[0];
            $scope.windowMessage = "请确认是否使用当前待调查商品货筐";
            $scope.errorWindow("reserachId", $scope.reserachWindows);
        }

        function openToReaservhBindWindow(data, toReserachBasket) {
            $scope.reserachWindows.close();
            $scope.basketOperate = "toReserachBasket";
            $scope.stationName = (data.values)[0];
            $scope.windowMessage = "请重新扫描待调查商品货筐";
            $scope.toReserachBasketNo = toReserachBasket;
            $scope.toReserachBasket = "";
            $scope.errorWindow("toReserachBakId", $scope.toReserachWindows, function () {
                $("#retoReserachBasketId").focus();
            });
            $scope.toReserachOperate = "yiBangDing";
        }

        function openToReaservhNoneWindow(toReserachBasket) {
            $scope.reserachWindows.close();
            $scope.basketOperate = "toReserachBasket";
            $scope.windowMessage = "请重新扫描待调查商品货筐";
            $scope.toReserachBasketNo = toReserachBasket;
            $scope.toReserachBasket = "";
            $scope.errorWindow("toReserachBakId", $scope.toReserachWindows, function () {
                $("#retoReserachBasketId").focus();
            });
            $scope.toReserachOperate = "wuXiao";
        }

        function openToReaservhLockWindow(toReserachBasket) {
            $scope.reserachWindows.close();
            $scope.basketOperate = "toReserachBasket";
            $scope.windowMessage = "请重新扫描待调查商品货筐";
            $scope.toReserachBasketNo = toReserachBasket;
            $scope.toReserachBasket = "";
            $scope.errorWindow("toReserachBakId", $scope.toReserachWindows, function () {
                $("#retoReserachBasketId").focus();
            });
            $scope.toReserachOperate = "locked";
        }

        //打开窗口
        $scope.openWindow = function (options) {
            $("#" + options.windowId).parent().addClass(options.windowClass);
            options.windowName.setOptions({
                width: options.width,
                height: options.height,
                closeable: options.closeable,
                visible: true,
                actions: ["Close"],
                title: options.title,
                activate: options.activate,
            });
            options.windowName.center();
            options.windowName.open();
        }
        //outbound问题处理
        function callProblem(data) {
            $scope.problemData = {
                "problemType": data.problemType,
                "amount": data.amount,
                "jobType": "PICK",
                "reportBy": $window.localStorage['username'],
                "reportDate": data.reportDate,
                "problemStoragelocation": $scope.problemStoragelocation,
                "container": data.storageName,
                "lotNo": $scope.lotNo,
                "serialNo": $scope.serialNo,
                "skuNo": $scope.skuNo,
                "itemNo": $scope.itemNo,
                "itemDataId": $scope.itemDataId,
                "shipmentId": data.shipmentId
            }
            pickToPackService.callProblem($scope.problemData, function (data) {
                putCanPin();//放问题商品
            });
        }

        //报告暗灯
        function callAnDon(data, storageLocaltion) {

            var anDonData = {
                "storageLocationId": storageLocaltion,
                "problemName": data.name,
                "anDonMasterTypeId": data.id,
                "reportBy": $window.localStorage['username'],
                "state": "undisposed",
                "clientId": $window.localStorage["clientId"],
                "warehouseId": $window.localStorage["warehouseId"]
            }
            outboundService.callAnDon(anDonData, function (data) {
                $scope.binNumber = "";
                $scope.scanBinOperate = "";
                $scope.lightWindows.close();
            });
        }

        //放问题商品
        function putCanPin(type) {
            $scope.basketOperate = "problemItemDigital";
            setTimeout(function () {
                $("#problemItemDigitalId").focus()
            },100);
            if($scope.scanBasketStep != "newBasket") {
                if (type == "DAMAGE") {
                    $scope.windowMessage = "请将商品放置到残品货筐中，并按动残品货筐上方暗灯";
                    $("#canPinId").css("background", "#00B050");
                    $scope.problemItem = "canPin";
                } else if (type == "INVENTORY") {
                    $("#unScanId").css("background", "#00B050");
                    $scope.windowMessage = "请将商品放置到无法扫描货筐中，并按动无法扫描货筐上方暗灯";
                    $scope.problemItem = "unScan";
                } else if (type == "PENDING") {
                    $("#toReserachId").css("background", "#00B050");
                    $scope.problemItem = "toReserach";
                    $scope.windowMessage = "请将商品放置到待调查货筐中，并按动待调查货筐上方暗灯";
                }
            }

            $scope.comfirmBnt = true;
            //getPickOrderDetail();
            pickToPackService.getOrderPosition($scope.podNo, $scope.sectionId, $scope.stationNo, function (data) {
                if (data.message == "noPod") {
                    $scope.podDetail = "noPod";
                    closeWebsocket(podSocket, "pod");
                }else{
                    $scope.nextPodMsg = data.socketPodName;
                }
                if($scope.scanBasketStep == "newBasket"){  //扫描新框之前已经亮灯拍灯，此时不需要拍灯
                    $scope.comfirmBnt = false;
                    $("#canPinId").css("background", "#D9D9D9");
                    $("#unScanId").css("background", "#D9D9D9");
                    $("#toReserachId").css("background", "#D9D9D9");
                    putProblemStep = 0;
                    $scope.scanBasketStep = "";
                    scanNewStorageStep = 0;
                    // $scope.refreshNewPod();
                    getlightOffPosition(data.socketPodName);
                }else{
                    $scope.clickPutSKUlight = $interval(function () {
                        if (lightResult == 1) {
                            clearLight();
                            $interval.cancel($scope.clickPutSKUlight);
                            lightResult = 0;
                            $scope.comfirmBnt = false;
                            $("#canPinId").css("background", "#D9D9D9");
                            $("#unScanId").css("background", "#D9D9D9");
                            $("#toReserachId").css("background", "#D9D9D9");
                            //initOrderPosition();
                            getlightOffPosition(data.socketPodName);
                        }
                    }, 100);
                }
            })

            /*$("#canPinId").css("background","#D9D9D9");
             $("#unScanId").css("background","#D9D9D9");
             $("#toReserachId").css("background","#D9D9D9");
             $scope.windowMessage = "请从指定货位取出商品，检查并扫描商品";
             setTimeout(function () {$("#skuId").focus(),200});
             initOrderPosition();*/
        }

        //扫描问题商品后扫描位置标签
        $scope.scanProblemItemDigital = function (e) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13) return;
            var problemItemDigitalName = $scope.problemItemDigitalName;
            $scope.scanDigitalWindows.close();
            pickToPackService.scanDigitalName(problemItemDigitalName,function (data) {
                if(($scope.problemItem == "canPin" && $scope.damagedLight == data.digitalId) || ($scope.problemItem == "unScan" && $scope.unScanLight == data.digitalId) || ($scope.problemItem == "toReserach" && $scope.toReserachLight == data.digitalId)){
                    clearLight();
                    $scope.comfirmBnt = false;
                    $("#canPinId").css("background", "#D9D9D9");
                    $("#unScanId").css("background", "#D9D9D9");
                    $("#toReserachId").css("background", "#D9D9D9");
                    //initOrderPosition();
                    getlightOffPosition($scope.nextPodMsg);
                }else{
                    $scope.pickingOrderPostion = "digital";
                    $scope.digitalErrorMsg = "扫描的位置标签"+problemItemDigitalName+"不匹配，请重新扫描";
                    $scope.digitalOperate = "rescanproblemItemDigital";
                    $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function (){$("#problemItemDigitalId1").focus()});
                }
            },function (data) {
                $scope.pickingOrderPostion = "digital";
                $scope.digitalErrorMsg = data.values[0];
                $scope.digitalOperate = "rescanproblemItemDigital";
                $scope.errorWindow("scanDigitalId", $scope.scanDigitalWindows,function (){$("#problemItemDigitalId1").focus()});
            });
            $scope.problemItemDigitalName = "";
        }
        // 初始化页面
        function initPage() {
            if ($rootScope.pickPackContinue) {
                $scope.pickPackOperate = "scanPickPackCar"; // 继续扫描pickpack_car
                $rootScope.pickpackcar = {};
                $rootScope.pickPackMain = {};
                $rootScope.pickPackContinue = false;
                setTimeout(function () {
                    $("#pickPack_pickPackCar").focus();
                }, 100);
            } else {
                $rootScope.pickPackContinue = false;
                $rootScope.pickPackStation = {};
                $rootScope.pickPackMain = {};
                $rootScope.pickpackcar = {};
                $scope.pickPackOperate = "scanStation"; // 初始扫描工作站
                setTimeout(function () {
                    $("#pickPack_station").focus();
                }, 200);
            }
        }

        //初始化主页面,直接扫描pod
        function initMainPage() {
            $scope.pickPackStation = {};
            $scope.pickpackcar = {};
            $scope.pickPackOperate = {};
            $scope.pickPackMain = "pickPackMain";
            $scope.skuOperate = "skuNo";
            $scope.skuNo = "";
            $scope.skuName = "";
            $scope.basketOperate = "pod";
            /*$scope.butPod = "停止分配Pod";
            $scope.butcolor = "red";*/
            stop_PickOrder();
            getIsCallPod();
            $scope.podNo = "";
            $scope.pickNo = "";
            $scope.windowMessage = "等待Pod进入";
            $scope.podMessage = "POD";
            setTimeout(function () {
                $("#podId").focus();
            }, 200);
            if(podValue == 0){
                getPodResult();
                podValue = 1;
            }
        }
        function initMainCallPod() {
            $scope.pickPackStation = {};
            $scope.pickpackcar = {};
            $scope.skuNo = "";
            $scope.skuName = "";
            $scope.pickPackOperate = {};
           // $scope.skuImg = "";
            $scope.pickPackMain = "pickPackMain";
            $scope.skuOperate = "skuNo";
            $scope.basketOperate = "pod";
            /*$scope.butPod = "停止分配Pod";
            $scope.butcolor = "red";*/
            getIsCallPod();
            $scope.podNo = "";
            $scope.pickNo = "";
            $scope.windowMessage = "等待Pod进入";
            setTimeout(function () {
                $("#podId").focus();
            }, 200);
        }

        //初始化主页面,直接扫描残品货框
        function initMainPage1() {
            $scope.pickPackStation = {};
            $scope.pickpackcar = {};
            $scope.pickPackOperate = {};
            $scope.pickPackMain = "pickPackMain";
            $scope.basketOperate = "scanCanPinBasket";
            $scope.skuOperate = "skuNo";
            /*$scope.butPod = "停止分配Pod";
            $scope.butcolor = "red";*/
            stop_PickOrder();
            getIsCallPod();
            $scope.podMessage = "POD";
            $scope.windowMessage = "请扫描残品货筐，并将货筐放于指定位置";
            $("#canPinId").css("background", "#FF0000");
            setTimeout(function () {
                $("#canPinBasketId").focus();
            }, 200);
            sendMessage($scope.damagedLight);
        }

        //初始化主页面,直接扫描无法扫描框
        function initMainPage2() {
            $scope.pickPackStation = {};
            $scope.pickpackcar = {};
            $scope.pickPackOperate = {};
            $scope.pickPackMain = "pickPackMain";
            $scope.skuOperate = "skuNo";
            /*$scope.butPod = "停止分配Pod";
            $scope.butcolor = "red";*/
            stop_PickOrder();
            getIsCallPod();
            $scope.podMessage = "POD";
            $("#unScanId").css("background", "#FFC000");
            $scope.basketOperate = "unScanSKUBasket";
            $scope.windowMessage = "请扫描商品无法扫描货筐，并将货筐放于指定位置";
            setTimeout(function () {
                $("#unScanSKUBasketId").focus();
            }, 200);
            //调灯亮的接口
            sendMessage($scope.unScanLight);
        }

        //初始化主页面,直接扫描待调查货框
        function initMainPage3() {
            $scope.pickPackStation = {};
            $scope.pickpackcar = {};
            $scope.pickPackOperate = {};
            $scope.pickPackMain = "pickPackMain";
            $scope.skuOperate = "skuNo";
            /*$scope.butPod = "停止分配Pod";
            $scope.butcolor = "red";*/
            stop_PickOrder();
            getIsCallPod();
            $scope.podMessage = "POD";
            $("#toReserachId").css("background", "#33CCFF");
            $scope.basketOperate = "toReserachBasket";
            $scope.windowMessage = "请扫描待调查货筐，并将货筐放于指定位置";
            setTimeout(function () {
                $("#toReserachBasketId").focus();
            }, 200);
            //发送灯亮命令
            sendMessage($scope.toReserachLight);
        }

        //初始化Pick Pack Wall
        function initPickPackWall(data) {
            var length = data.length;
            if (length == 1) {
                //var fileIndex = data[0].fieldIndex;
                var fileIndex = data[0].orderIndex;
                if (fileIndex == 1) {
                    initLeft(data[0].pickPackFieldTypeDTO);
                    $scope.pickPackWallWidth = $scope.numberOfColumns1;
                }
                if (fileIndex == 2) {
                    initMiddle(data[0].pickPackFieldTypeDTO);
                    $scope.pickPackWallWidth = $scope.numberOfColumns2;
                }
                if (fileIndex == 3) {
                    initRight(data[0].pickPackFieldTypeDTO);
                    $scope.pickPackWallWidth = $scope.numberOfColumns3;
                }

            }
            if (length == 2) {
                // var fileIndex1 = data[0].fieldIndex;
                // var fileIndex2 = data[1].fieldIndex;
                var fileIndex1 = data[0].orderIndex;
                var fileIndex2 = data[1].orderIndex;
                if (fileIndex1 == 1 && fileIndex2 == 2) {
                    initLeft(data[0].pickPackFieldTypeDTO);
                    initMiddle(data[1].pickPackFieldTypeDTO);
                    $scope.pickPackWallWidth = $scope.numberOfColumns1 + $scope.numberOfColumns2;
                }
                if (fileIndex1 == 1 && fileIndex2 == 3) {
                    initLeft(data[0].pickPackFieldTypeDTO);
                    initRight(data[1].pickPackFieldTypeDTO);
                    $scope.pickPackWallWidth = $scope.numberOfColumns1 + $scope.numberOfColumns3;
                }
                if (fileIndex1 == 2 && fileIndex2 == 3) {
                    initMiddle(data[0].pickPackFieldTypeDTO);
                    initRight(data[1].pickPackFieldTypeDTO);
                    $scope.pickPackWallWidth = $scope.numberOfColumns2 + $scope.numberOfColumns3;
                }

            }
            if (length == 3) {
                //初始化左侧格子
                initLeft(data[0].pickPackFieldTypeDTO);
                //初始化中间格子
                initMiddle(data[1].pickPackFieldTypeDTO);
                //初始化右边格子
                initRight(data[2].pickPackFieldTypeDTO);
                $scope.pickPackWallWidth = $scope.numberOfColumns1 + $scope.numberOfColumns2 + $scope.numberOfColumns3;
            }
        }

        function initLeft(data) {
            $scope.pickPackWall1Rows = [], $scope.pickPackWall1Columns = [];
            $scope.numberOfRows1 = data.numberOfRows;
            $scope.numberOfColumns1 = data.numberOfColumns;
            $scope.cellHeight1 = ($scope.wallHeight - ($scope.numberOfRows1 + 1) * 10) / $scope.numberOfRows1;
            for (var i = 0; i < data.numberOfColumns; i++) $scope.pickPackWall1Columns.push({
                name: "",
                choice: false
            });
            for (var i = 0; i < data.numberOfRows; i++) {
                $scope.pickPackWall1Rows[i] = angular.copy({
                    item: $scope.pickPackWall1Columns,
                    color: "#c1c1c1",
                });
            }
            var data = $scope.pickPackWall1Rows;
            angular.forEach(data, function (data) {
                data.color = "#c1c1c1";
                angular.forEach(data.item, function (data) {
                    data.name = "";
                    data.choice = false;
                })
            });
        }

        function initMiddle(data) {
            $scope.pickPackWall2Rows = [], $scope.pickPackWall2Columns = [];
            $scope.numberOfRows2 = data.numberOfRows;
            $scope.numberOfColumns2 = data.numberOfColumns;
            $scope.cellHeight2 = ($scope.wallHeight - ($scope.numberOfRows2 + 1) * 10) / $scope.numberOfRows2;
            for (var i = 0; i < data.numberOfColumns; i++) $scope.pickPackWall2Columns.push({
                name: "",
                choice: false
            });
            for (var i = 0; i < data.numberOfRows; i++) $scope.pickPackWall2Rows[i] = angular.copy({
                item: $scope.pickPackWall2Columns,
                color: "#c1c1c1"
            });
            var data = $scope.pickPackWall2Rows;
            angular.forEach(data, function (data) {
                data.color = "#c1c1c1";
                angular.forEach(data.item, function (data) {
                    data.name = "";
                    data.choice = false;
                })
            });
        }

        function initRight(data) {
            $scope.pickPackWall3Rows = [], $scope.pickPackWall3Columns = [];
            $scope.numberOfRows3 = data.numberOfRows;
            $scope.numberOfColumns3 = data.numberOfColumns;
            $scope.cellHeight3 = ($scope.wallHeight - ($scope.numberOfRows3 + 1) * 10) / $scope.numberOfRows3;
            for (var i = 0; i < data.numberOfColumns; i++) $scope.pickPackWall3Columns.push({
                name: "",
                choice: false
            });
            for (var i = 0; i < data.numberOfRows; i++) $scope.pickPackWall3Rows[i] = angular.copy({
                item: $scope.pickPackWall3Columns,
                color: "#c1c1c1"
            });
            var data = $scope.pickPackWall3Rows;
            angular.forEach(data, function (data) {
                data.color = "#c1c1c1";
                angular.forEach(data.item, function (data) {
                    data.name = "";
                    data.choice = false;
                })
            });
        }

        function clearLight() {
            $interval.cancel($scope.clickDamageLight);
            $interval.cancel($scope.clickLight3);
            $interval.cancel($scope.clickInventoryLight);
            $interval.cancel($scope.clickPendingLight);
            $interval.cancel($scope.clickSKUlightlight);
            $interval.cancel($scope.clickNewBasketLight);
            $interval.cancel($scope.clickPendingLight1);
            $interval.cancel($scope.clickInventoryLight2);
            $interval.cancel($scope.clickDamageLight2);
            $interval.cancel($scope.clickPickPackWallLight);
            $interval.cancel($scope.clickPutSKUlight);
            $interval.cancel($scope.clickInventoryLight3);
        }

        $scope.assignPod = function () {
            if ($scope.butPod === "停止分配Pod") {
                outboundService.callPod("stop", $scope.workStationId, function (data) {
                    $scope.butPod = "恢复分配Pod";
                    $scope.butcolor = "green";
                })
            } else {
                outboundService.callPod("start", $scope.workStationId, function (data) {
                    $scope.butPod = "停止分配Pod";
                    $scope.butcolor = "red";
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
        $scope.stopPickOrder = function(){
            if($scope.stopOrderBnt === "停止分配批次"){
                $scope.pickOrderStateMsg = "确认停止分配批次后，不再给此工作站分配拣货任务";
                $scope.errorWindow("assginPickOrderWinId",$scope.assginPickOrderWindows);
            } else {
                outboundService.stopPickOrder($scope.logicStationId,"start",function (data) {
                    $scope.stopOrderBnt = "停止分配批次";
                    $scope.stopBntcolor = "red";
                })
            }
        }

        function stop_PickOrder(){
             outboundService.getCallPickOrder($scope.stationNo,function(data){
                if(data.isCallPickOrder){
                    $scope.stopBntcolor = "red";
                    $scope.stopOrderBnt = "停止分配批次";
                }else{
                    $scope.stopBntcolor = "green";
                    $scope.stopOrderBnt = "恢复分配批次";
                }
             })
        }
        function getIsCallPod() {
            outboundService.getCallPod($scope.workStationId,function (data) {
                if(data.isCallPod){
                    $scope.butPod = "停止分配Pod";
                    $scope.butcolor = "red";
                }else {
                    $scope.butPod = "恢复分配Pod";
                    $scope.butcolor = "green";
                }
            });
        }
        //释放pod确认
        $scope.comfirmReservePod = function () {
            $scope.reservePodName = $scope.podNo;
            $scope.errorWindow("reservePodWindowId", $scope.reservePodWindows);
        }
        //释放pod
        $scope.reservePod = function () {
            $scope.reservePodWindows.close();
            outboundService.reservePod($scope.podNo,$scope.sectionId,"false",$scope.workStationId,$scope.logicStationId,function (data) {
                callNewPod(data.pod);
            })
        }
        //刷新重新获取pod
        $scope.refreshNewPod = function () {
            outboundService.refreshPod($scope.sectionId,$scope.workStationId,function (data) {
                console.log(date.toLocaleString()+"->刷新获取pod SUCCESS");
                callNewPod(data.pod);
            },function (data) {
                console.log(date.toLocaleString()+"->刷新获取pod error");
            })
        }
       /* function callNewPod(data) {
            if(data.pod != "" && data.pod != null && data.pod != undefined){//当获取到pod信息时，进行下一步，获取拣货单
                console.log(date.toLocaleString()+"->刷新或推送的pod信息:",data);
                $scope.podNo = data.pod;
                initOrderPosition();
            }else{//没有获取到pod信息时，初始化界面，继续获取pod
                console.log(date.toLocaleString()+"->工作站此时没有pod:",data);
                $scope.podMessage = "POD";
                $scope.podRows = [];
                initPickPackWall($scope.pickPackWallInfo);
                initMainCallPod();
            }
        }*/

        function callNewPod(pod) {
            if(pod != "" && pod != null && pod != undefined){//当获取到pod信息时，进行下一步，获取拣货单
                console.log(date.toLocaleString()+"->刷新或推送的pod信息:",pod);
                $scope.podNo = pod;
                initOrderPosition();
            }else{//没有获取到pod信息时，初始化界面，继续获取pod
                console.log(date.toLocaleString()+"->工作站此时没有pod:",pod);
                $scope.podMessage = "POD";
                $scope.podRows = [];
                initPickPackWall($scope.pickPackWallInfo);
                initMainCallPod();
            }
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
               /!* if(socket.readyState != 1){
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
        }

        //websocket 推送pod的结果
        function getPodResult() {
           // var url = OUTBOUND_CONSTANT.podWebSocket+$scope.workStationId;
            var url = BACKEND_CONFIG.websocket+"websocket/getPod/"+$scope.workStationId;
            console.log("url:",url);
            podSocket = new WebSocket(url);
            //打开事件
            podSocket.onopen = function () {
                console.log("podSocket 已打开");
            };
            //获得消息事件
            podSocket.onmessage = function (msg) {
                console.log("podSocket 正在推送消息。。。");
                var data = JSON.parse(msg.data);
                if(data.pod != "success"){
                    if (data.workstation == $scope.workStationId) {
                        console.log("推送pod的信息：",data);
                        callNewPod(data);
                    }
                }
            };
            //关闭事件
            podSocket.onclose = function () {
                console.log("podSocket 关闭");
            };
            //发生了错误事件
            podSocket.onerror = function () {
                console.log("podSocket 发生了错误");
                podSocket = new WebSocket(url);
            }
        }
*/
        function getLightResult() {
            var option={
                "user":$scope.workStationId,
                "url":"websocket/ws/"+$scope.workStationId,
                "onmessageCall":onmessageCall
            }
           // if($.isEmptyObject(socket)){
                socket=webSocketService.initSocket(option);
           // }
        }
        function onmessageCall(msg){
            //接收到消息后做的业务处理代码
            if(msg != "Success"){
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
            console.log(date.toLocaleString()+"->podSocket 正在推送消息。。。");
            var data = JSON.parse(msg);
            if(data.pod != "success"){
                if (data.workstation == $scope.workStationId) {
                    console.log(date.toLocaleString()+"->websocket推送pod的信息：",data);
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
        function sendMessage(data,data1,data2) {
            var obj = "";
            if(data2 == undefined && data1 == undefined){
                obj = {"url": "/light/onOff", "labelId": data, "onOffFlag": "true","shootLight":"true","workStationId":$scope.workStationId};
                console.log(date.toLocaleString()+"->当前亮的灯："+ data);
            }else if(data1 == "exception"){
                obj = {"url": "/light/onOff", "labelId": data, "onOffFlag": "false","shootLight":"true","workStationId":$scope.workStationId};
                console.log(date.toLocaleString()+"->当前亮的灯："+ data);
            }else{
                obj = {"DIGITALLABEL2":data,"SHIPMENT_ID":data1, "PICKPACKWALL_ID":data2}
            }

            var msg = JSON.stringify(obj);
            socket.send(msg);
        }

        function init() {
            initPage();
        }

        //初始化
        init();
    })
})();