/**
 * Created by thoma.bian on 2017/5/10.
 * Updated by frank.zhou on 2017/7/05.
 */

(function () {
  'use strict';

  angular.module('myApp').controller("problemOutboundDetailCtl", function ($scope, $rootScope,$state, $translate, commonService, problemOutboundService, problemOutboundBaseService) {
    // ============================================================================================================================================
    // 错误弹窗
    $scope.errorWindow = function (options){
      $(options.id).parent().addClass("myWindow");
      var window = options.name;
      window.setOptions({width: options.width || 800, height: options.height || 250, visible: false, actions: ["close"]});
      window.center();
      options.open && window.bind("open", options.open);
      window.open();
    };

    // 扫描框获焦点
    function focusItemNo(){
      $scope.itemNo = "";
      setTimeout(function(){ $("#obp_itemNo").focus();}, 300);
    }

    // shipment信息
    function getOrderDetails(){
        var ifSolved="unsolved";
        if($rootScope.gridId == 'problemOutboundScanedGrid') ifSolved="solved";
      problemOutboundService.getOrderDetails({
        "obpStationId": $rootScope.obpStationId,
        "obpWallId": $rootScope.obpWallId,
        "shipmentNo": $rootScope.shipmentNo,
        "state": ifSolved
      }, function(datas){
        var grid = $("#outProblemGrid").data("kendoGrid");
        grid.setDataSource(new kendo.data.DataSource({data: datas}));
      });
    }

    // 获取商品明细
    function getOrderGoodsDetails(){
      problemOutboundService.getOrderGoodsDetails($rootScope.shipmentNo, function(datas){
        $scope.orderGoodsDetails = datas;
        var grid = $("#outGoodsProblemGrid").data("kendoGrid");
        grid.setDataSource(new kendo.data.DataSource({data: datas}));
        setGoodsDetailsBackgound(datas)
        checkScan(datas);
        focusItemNo();
      });
    }

    // 设置商品明细背景
    function setGoodsDetailsBackgound(datas, gridId){
      gridId == null && (gridId = "outGoodsProblemGrid");
      var grid = $("#"+ gridId).data("kendoGrid");
      datas == null && (datas = grid.dataSource.data());
      for(var i = 0; i < datas.length; i++){
        var data = datas[i], bgColor = "";
        if(data.problemType == "DAMAGED" && data.solveKey == "") bgColor = "#f0ad4e";
        else if(data.amountScaned == data.amount) bgColor = "#c5e0b4";
        else if(data.amountScaned > 0) bgColor = "#deebf7";
        bgColor != "" && grid.tbody.find("tr:eq("+ i+ ")").css("background", bgColor);
      }
    }

    // 检查扫描是否全部完成
    function checkScan(datas){
      for(var i = 0, scaned = true, hasStockUnitZero = false,hasNotConfirmDamaged=false; i < datas.length; i++){
        var data = datas[i];
        if(data.solveKey == "CONFIRM_DAMAGED" && data.stockUnitAmount <= 0){ hasStockUnitZero=true; continue;}
        if(data.solveKey == "DAMAGED_TO_NORMAL") {hasNotConfirmDamaged=true;break;} //残损转正常但是还未确认残损
        if(!(data.amountScaned == data.amount && (data.solveKey?( data.solveKey=="DAMAGED_TO_NORMAL" ||data.solveKey=="PRINT_SKU_REPAIR"): data.problemType!="DAMAGED"))){
          scaned = false;
          break;
        }
      }
      // 全部扫描完成
      if(scaned){
        if(hasStockUnitZero){ $scope.clearTheProblem = false; $scope.demolitionDelivery= true;}
        else if($rootScope.gridId == 'problemOutboundScanedGrid') $scope.orderProcessing = 'orderFinish';
        else if(hasNotConfirmDamaged) $scope.clearTheProblem = true;
        else if(!hasStockUnitZero) $scope.orderProcessing = "orderSuccess";
      }else {
          $scope.orderProcessing = 'processing';
          if ($rootScope.gridId == 'problemOutboundScanedGrid') $scope.orderProcessing = 'orderFinish'; //订单处理完成
      }
    }

    // 刷新商品明细
    function refreshGoodsDetails(){
      getOrderGoodsDetails();
    }

    // 打印订单
    $scope.printOrder = function(){
        problemOutboundService.printOrder($rootScope.shipmentNo,"",function(data){
          console.log("data...",data);
        });
    };

    // 清除问题处理格
    $scope.clearProblemCell = function(key, callback){
      problemOutboundService.releaseQuestionCell({
        "shipmentNo": $rootScope.shipmentNo,
        "obpStationId": $rootScope.obpStationId,
        "cellName": $rootScope.obpCellName,
        "solveKey": key
      }, function(){
        if(callback) callback();
        else $state.go("main.problemOutboundShipment");
      });
    };

    $scope.clearRelieveProblemCell= function() {
       $scope.clearCellError=false;
       $scope.errorWindow({
           id: "#clearCellId",
           name: $scope.clearCellWindow,
           open: function () {
           setTimeout(function () {$("#clearStorageLoationId").focus();}, 600); // 清除扫车牌
          }
       });
    };

    $scope.releaseRelieveProblemCell= function() {
        $scope.releaseCellError=false;
        $scope.errorWindow({
            id: "#releaseCellId",
            name: $scope.releaseCellWindow,
            open: function () {
            setTimeout(function () {$("#releaseStorageLoationId").focus();}, 600); // 包装扫车牌
        }
        });
   };

    //测试清除问题格和释放问题格
   $scope.relieveCell=function(e,key,callback){
       var keycode = window.event ? e.keyCode : e.which;
       if(keycode != 13) return;
       problemOutboundService.relieveQuestionCell({
           "shipmentNo": $rootScope.shipmentNo,
           "obpStationId": $rootScope.obpStationId,
           "cellName": $rootScope.obpCellName,
           "solveKey": key,
           "locationContainer":$scope.locationContainer
       }, function(){
           $scope.locationContainer="";
           if(callback) callback();
           else
             $state.go("main.problemOutboundShipment");
       },function(data){
           $scope.locationContainer="";
           if(key=='CLEARANCE_CELL'){
               $scope.clearCellError=true;
           }else if(key='RELEASE_CELL'){
               $scope.releaseCellError=true;
           }
           if(data.key=='扫描对象不存在,请重新扫描')
               $scope.message='车牌不存在,请重新扫描';
           else if(data.key=='容器已被锁定')
               $scope.message='车牌被锁定,请重新扫描';
           else if(data.key=='容器里存在商品')
               $scope.message='车牌内存在商品,请重新扫描';
           else{
               var win = $("#mushinyWindow").data("kendoWindow");
               commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                   setTimeout(function(){
                       $("#warnContent").html($translate.instant(data.key));
                   }, 200);
                   win.bind("close", function () {
                   $scope.locationContainer="";
                   setTimeout(function(){ $("#clearStorageLoationId").focus();}, 600);});
               }});
           }
       });
   };

    // 返回主页
    $scope.backHome = function(){
        $state.go("main.problemOutboundShipment");
    };

    // 扫描商品
    $scope.scanGoods = function(e){
      $rootScope.itemNo=$scope.itemNo;
      var keycode = window.event? e.keyCode: e.which;
      if(keycode != 13) return;
      problemOutboundService.saveGoodsInformation({
        "cellName": $rootScope.obpCellName,
        "shipmentNo": $rootScope.shipmentNo,
        "itemNo": $scope.itemNo
      }, function(dataItem){
        refreshGoodsDetails(); // 刷新
        $scope.goodsDetail = dataItem;
        if(dataItem.problemType == "UNABLE_SCAN_SN"){
          $scope.goodsDetailClick=dataItem;
          $scope.errorWindow({id: "#goodsInvestigate", name: $scope.goodsInvestigateWindow, height: 300,open:function(){
             $scope.serialNumber = ""; // 扫序列号（有按钮）
             setTimeout(function(){ $("#serialNumbersId").focus();}, 600);
          }});
        }else if(dataItem.serialRecordType === "ALWAYS_RECORD"){
          $scope.goodsDetailClick=dataItem; //解决正常商品转序列号问题
          $scope.operation = "serialNumber";
          $scope.scanSn=true;
          $scope.noScanSn=false;
          $scope.errorWindow({id: "#goodsNumberId", name: $scope.goodsNumberWindow, open: function () {
            $scope.serialNumber = ""; // 初始为空
            $scope.investigated = false;
            setTimeout(function(){ $("#serialNumberId").focus();}, 600);
          }});
        }
      }, function(data){
        var win = $("#mushinyWindow").data("kendoWindow");
        commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
          setTimeout(function(){ $("#warnContent").html($translate.instant(data.key));}, 200);
          win.bind("close", function(){ focusItemNo(); });
        }});
      });
    };

    // 扫描序列号
    $scope.serialNumbers = function (e, item) {
      var keycode = window.event ? e.keyCode : e.which;
      if(keycode != 13) return;
      problemOutboundService.saveGoodsBySN({
        "cellName": $rootScope.obpCellName,
        "shipmentNo": $rootScope.shipmentNo,
       // "itemNo":$rootScope.itemNo,
        "itemNo":$scope.goodsDetailClick.itemNo,
        "serialNo": $scope.serialNumber
      }, function() {
          $scope.goodsNumberWindow.close();
          $scope.goodsInvestigateWindow.close();
          $scope.operation = "checkGoods";
          refreshGoodsDetails();
      },function(data) {
         $scope.serialNumber="";
         if(data.key=="商品序列号错误"||data.key=="此序列号已被扫描") {
           $scope.noScanSn=true;
        }else{
             var win = $("#mushinyWindow").data("kendoWindow");
             commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                 setTimeout(function(){
                     $("#warnContent").html($translate.instant(data.key));
                 }, 200);
                 win.bind("close", function(){ $scope.serialNumber = "";
                 setTimeout(function(){ $("#serialNumberId").focus();}, 600);  });
             }});
         }
      });
    };

    // 序列号无法扫描，转为待调查状态
    $scope.investigateTheStatus = function(key){
      var problemResource = ($scope.goodsDetail.problemType != null? "PROBLEM": "NORMAL");
      if(problemResource=="PROBLEM"){
      $scope.goodsInvestigateWindow.close();
      if($scope.goodsDetail.lotMandatory){
          $scope.useNotAfterKey = "TO_BE_INVESTIGATED";
          $scope.openLotWindow();
      }else{
        $scope.useNotAfter = "";
        $scope.investigatedCar="";
        $scope.errorWindow({id: "#investigationCartsId", name: $scope.investigationCartsWindow, open: function() {
          setTimeout(function (){ $("#obp_investigatedCar").focus(); }, 600);
        }});
      }
      }else{
        //$scope.checkProblemAmount("UNABLE_SCAN_SN");
         $scope.goodsToInvestigated(problemResource,1);
      }
    };

    // 请扫描待调查车牌
    $scope.investigationCart = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if(keycode != 13) return;
      problemOutboundService.getInvestigated({
        "solveId": $scope.goodsDetailClick.solveId,
        "itemNo": $scope.goodsDetailClick.itemNo,
        "shipmentNo": $rootScope.shipmentNo,
        "locationContainer":$scope.investigatedCar,
        "useNotAfter": $scope.useNotAfter,
        "solveKey": "TO_BE_INVESTIGATED"
      }, function(){
        $scope.investigationCartsWindow.close();
        //$scope.goodsDetailClick.set("solveKey", "TO_BE_INVESTIGATED");
          getOrderGoodsDetails();
      }, function(data) {
        if (data.key == "EX_CONTAINER_SKU_DIFFERENT_CLIENT") {
            $scope.investErrorKey = "errorClient";
            investigationCarts();
        } else if (data.key == "EX_CONTAINER_SKU_DIFFERENT_LOT") {
            $scope.investErrorKey = "errorTime";
            investigationCarts();
        }else if(data.key =="有效期错误"){
            var win = $("#mushinyWindow").data("kendoWindow");
            commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                setTimeout(function(){
                    $("#warnContent").html($translate.instant(data.key));
                }, 200);
                win.bind("close", function(){
                    $scope.residualLicensePlateWindow.close();
                    $scope.useNotAfterKey="TO_BE_INVESTIGATED";
                    $scope.openLotWindow();
                });
            }});
        } else {
            var win = $("#mushinyWindow").data("kendoWindow");
            commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                setTimeout(function(){
                    $("#warnContent").html($translate.instant(data.key));
                }, 200);
                win.bind("close", function(){ investigationCarts(); });
            }});
        }
      });
    };

    function investigationCarts(){
        $scope.investigatedCar="";
        setTimeout(function (){ $("#obp_investigatedCar").focus(); }, 600);
    }

      $scope.confirmTime=function(e,id){
          var keycode = window.event ? e.keyCode : e.which;
          if(keycode!=13)  return;
              switch (id) {
                  case "maturity_year":
                      setTimeout(function (){ $("#maturity_month").focus(); }, 300);
                      break;
                  case "maturity_month":
                      setTimeout(function (){ $("#maturity_day").focus(); }, 300);
                      break;
                  case "produce_year":
                      setTimeout(function (){ $("#produce_month").focus(); }, 300);
                      break;
                  case "produce_month":
                      setTimeout(function (){ $("#produce_day").focus(); }, 300);
                      break;
                  case "produce_day":
                      setTimeout(function (){ $("#obp_months").focus(); }, 300);
                      break;
              }
      };

    // 商品有效期-确认
    $scope.commodityValidSure = function(){
        // 到期日
        var year = 0, month = 0, day = 0;
        var isProduce = ($scope.goodsDetailClick.lotType == "MANUFACTURE");
        // year = isProduce ? $scope.produce_year : $scope.maturity_year;
        year = isProduce ?$("#produce_year").val():$("#maturity_year").val();
        day = isProduce ? $("#produce_day").val():$("#maturity_day").val();
        month= isProduce ? $("#produce_month").val():$("#maturity_month").val();
        $scope.useNotAfterTest = year + "-" + problemOutboundBaseService.pad(month) + "-" + problemOutboundBaseService.pad(day);
        var date = new Date($scope.useNotAfterTest);
        if($scope.lotUnit=="月")
            date.setMonth(date.getMonth() + (parseInt($("#obp_months").val())|| 0));
        if($scope.lotUnit=="年")
            date.setFullYear(date.getFullYear() + (parseInt($("#obp_months").val())|| 0));
        if($scope.lotUnit=="日")
            date.setDate(date.getDate() + (parseInt($("#obp_months").val())|| 0));
      // month = parseInt(isProduce ? parseInt($("#produce_month").val()) + months : parseInt($("#maturity_month").val()));
      // while (month > 12){ year++; month -= 12;}
      if(year>1000&&month>0&&day>0) {
      $scope.useNotAfter = date.getFullYear() + "-" + problemOutboundBaseService.pad(date.getMonth() + 1) + "-" + problemOutboundBaseService.pad(date.getDate());
      $scope.commodityValidWindow.close(); // 有效期/到期日
      //
      if($scope.useNotAfterKey == "TO_BE_INVESTIGATED"){
        $scope.errorWindow({id: "#investigationCartsId", name: $scope.investigationCartsWindow, open: function() {
          $scope.investigatedCar=""; //清空
          setTimeout(function (){ $("#obp_investigatedCar").focus(); }, 600);
        }});
      }else if($scope.useNotAfterKey == "CONFIRM_DAMAGED"){
             $scope.errorWindow({id: "#residualLicensePlateId", name: $scope.residualLicensePlateWindow, height: 300, open: function(){
             $scope.damageCart="";
             setTimeout(function (){ $("#obp_damageCart").focus();}, 600);
        }});
      }else if($scope.useNotAfterKey =="OUT_OF_STOCK_DELETE_ORDER"){
          if($scope.goodsDetail.serialRecordType === "ALWAYS_RECORD"){
              var grid = $("#demolitionDeleteGridx").data("kendoGrid"), datas = grid.dataSource.data();
              $scope.openSerialWindow($scope.goodsDetailClick,datas);
          }else {
              var grid = $("#demolitionDeleteGridx").data("kendoGrid"), datas = grid.dataSource.data();
              checkSuccess($scope.goodsDetailClick,datas);
          }
      }
      }
    };

    //日期点击赋值
    $scope.numberValue = function(v){
       if($scope.inputPos == 'year') {
          $("#produce_year").val($("#produce_year").val() + v);
          $("#maturity_year").val($("#maturity_year").val() + v);
        }else if($scope.inputPos == 'month'){
          $("#produce_month").val($("#produce_month").val() + v);
          $("#maturity_month").val($("#maturity_month").val() + v);
        }else if($scope.inputPos == 'day'){
          $("#produce_day").val($("#produce_day").val() + v);
          $("#maturity_day").val($("#maturity_day").val() + v);
        }else if($scope.inputPos == 'months'){
          $("#obp_months").val($("#obp_months").val() + v);
        }
     };

     //日期光标位置
     $scope.inputPosition = function(value){
       $scope.inputPos = value;
     };

      //清空日期
      $scope.emptyContentData = function(){
          $("#produce_year").val(""); $("#produce_month").val(""); $("#produce_day").val(""); $("#obp_months").val("");
          $("#maturity_year").val(""); $("#maturity_month").val(""); $("#maturity_day").val("");
            setTimeout(function (){ $("#produce_year").focus();}, 600);
            setTimeout(function (){ $("#maturity_year").focus();}, 600);
      };

      // 选择数字
    $scope.selectNumber = function(text){

    };

    // 输入数量（确认残损，商品丢失，转正品，条码无法扫描）
    $scope.checkProblemAmount = function(key){
      var problemResource = ($scope.goodsDetail.problemType != null? "PROBLEM": "NORMAL");
      if(($scope.goodsDetail.serialRecordType!="ALWAYS_RECORD" || key == "GOODS_LOSS" || key == "PRINT_SKU_REPAIR" )&&$scope.goodsDetail.amount > 1) {
          $scope.errorWindow({
              id: "#problemAmountId",
              name: $scope.problemAmountWindow,
              width: 600,
              height: 300,
              open: function () {
                  $scope.problemAmount = 1;
                  $scope.problemTypeWindow.close();
                  $scope.problemTypeNoScanWindow.close();
                  $scope.problemTypeScanDoneWindow.close();
                  $scope.goodsNumberWindow.close();
                  setTimeout(function () {
                      $("#obp_problemAmount").focus();
                  }, 600);
                  // 确认方法
                  $scope.problemAmountSure = function (amount) {
                      if ($scope.validator.validate()) {
                          $scope.problemAmountWindow.close();
                          if (key == "CONFIRM_DAMAGED") $scope.confirmDamage(problemResource, amount,"");
                          else if (key == "GOODS_LOSS") $scope.goodsLost(problemResource, amount);
                          else if (key == "DAMAGED_TO_NORMAL") $scope.goodsToGenuine(problemResource, amount,"");
                          else if (key == "PRINT_SKU_REPAIR") $scope.barcodeNotScanned(problemResource, amount);
                         // else if (key == "UNABLE_SCAN_SN") $scope.goodsToInvestigated(problemResource, amount);
                      }
                  };
              }
          });
      }else{
        var amount=0;
        if(key == "CONFIRM_DAMAGED") {
            if($scope.goodsDetail.amount > 1)
               $scope.checkSerialRecordType(key,problemResource, 1);
            else
               $scope.checkSerialRecordType(key,problemResource, amount);
        }
        else if(key == "GOODS_LOSS") $scope.goodsLost(problemResource, amount);
        else if(key == "DAMAGED_TO_NORMAL"){
            if($scope.goodsDetail.amount > 1)
                $scope.checkSerialRecordType(key,problemResource, 1);
            else
                $scope.checkSerialRecordType(key,problemResource, amount);
        }
        else if(key == "PRINT_SKU_REPAIR") $scope.barcodeNotScanned(problemResource, amount);
      }
    };

    // 确认残损/商品残损
    $scope.confirmDamage = function(problemResource, amount,serialNo){
      problemOutboundService.damageConfirm({
        "solveId": $scope.goodsDetail.solveId,
        "itemNo": $scope.goodsDetail.itemNo,
        "shipmentNo": $rootScope.shipmentNo,
        "solveKey": "CONFIRM_DAMAGED",
        "problemResources": problemResource || "",
        "amountConfirmProblem": amount || 0,
        "serialNo":serialNo
      }, function(){
         $scope.goodsSerialWindow.close();
        //refreshGoodsDetails();//确认残损刷新商品详情
        $scope.differentValidityPeriod = false;
        $scope.differentClient = false;
        $scope.problemTypeWindow.close();
        $scope.problemTypeScanDoneWindow.close();
        if($scope.goodsDetailClick.lotMandatory){
            $scope.useNotAfterKey="CONFIRM_DAMAGED";
            $scope.serialNo=serialNo;
            $scope.openLotWindow();
        }else{
          $scope.useNotAfter = "";
          $scope.errorWindow({id: "#residualLicensePlateId", name: $scope.residualLicensePlateWindow, height: 300, open: function(){
            setTimeout(function (){ $("#obp_damageCart").focus();}, 600);
            $scope.damageCart = ""; $scope.serialNo=serialNo;
          }});
        }
      },function(data){
          var win = $("#mushinyWindow").data("kendoWindow");
          commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
              setTimeout(function(){
                  $("#warnContent").html($translate.instant(data.key));
              }, 200);
              win.bind("close", function(){ $scope.goodsSerialNumber = ""; setTimeout(function(){ $("#goodsSnId").focus();}, 600); });
          }});
      });
    };

    $scope.checkSerialRecordType=function(solveKey,problemResource,amount){
        if($scope.goodsDetailClick.serialRecordType=="ALWAYS_RECORD"){
             $scope.openSNWindow(solveKey,problemResource,amount)
        } else{
            if(solveKey=="CONFIRM_DAMAGED")
                $scope.confirmDamage(problemResource, amount,"");
            else if(solveKey=="DAMAGED_TO_NORMAL")
                $scope.goodsToGenuine(problemResource, amount,"");
        }
   };

    // 扫描残品车
    $scope.damageCarts = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if(keycode != 13) return;
      problemOutboundService.damageGoods({
        // "cellName": $rootScope.obpCellName,
        "containerName": $scope.damageCart,
        "shipmentNo": $rootScope.shipmentNo,
        "itemNo": $scope.goodsDetail.itemNo,
        "useNotAfter": $scope.useNotAfter,
        "serialNo":$scope.serialNo
      }, function(){
        $scope.residualLicensePlateWindow.close(); // 残品弹窗
        getOrderGoodsDetails();
      }, function(data){
        if(data.key == "EX_CONTAINER_SKU_DIFFERENT_LOT"){
          $scope.differentValidityPeriod= true;
          $scope.differentClient= false;
            focusDamageCarts();
        }else if(data.key == "EX_CONTAINER_SKU_DIFFERENT_CLIENT") {
            $scope.differentValidityPeriod = false;
            $scope.differentClient = true;
            focusDamageCarts();
        }else if(data.key =="有效期错误"){
            var win = $("#mushinyWindow").data("kendoWindow");
            commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                setTimeout(function(){
                    $("#warnContent").html($translate.instant(data.key));
                }, 200);
                win.bind("close", function(){
                    $scope.useNotAfterKey = "CONFIRM_DAMAGED";
                    $scope.openLotWindow();
                });
            }});
        } else {
          var win = $("#mushinyWindow").data("kendoWindow");
          commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
            setTimeout(function(){
              $("#warnContent").html($translate.instant(data.key));
            }, 200);
            win.bind("close", function(){ focusDamageCarts(); });
          }});
        }
      });
    };
     function focusDamageCarts(){
         $scope.damageCart = "";
         setTimeout(function(){ $("#obp_damageCart").focus();}, 600);
     }
    //正常商品转为待调查状态
      $scope.goodsToInvestigated = function(problemResource, amount){
        problemOutboundService.damageConfirm({
           "solveId": $scope.goodsDetailClick.solveId,
           "itemNo": $scope.goodsDetailClick.itemNo,
           "shipmentNo": $rootScope.shipmentNo,
           "solveKey": "UNABLE_SCAN_SN",
           "problemResources": problemResource || "",
           "amountConfirmProblem": amount || 1
        }, function(){
          refreshGoodsDetails();//刷新订单详情
          $scope.goodsNumberWindow.close();
          if($scope.goodsDetailClick.lotMandatory){
              $scope.useNotAfterKey = "TO_BE_INVESTIGATED";
              $scope.openLotWindow();
          } else{
              $scope.useNotAfter = "";
              $scope.investigatedCar="";
              $scope.errorWindow({id: "#investigationCartsId", name: $scope.investigationCartsWindow, open: function() {
                 setTimeout(function (){ $("#obp_investigatedCar").focus(); }, 600);
             }});
           }
       });
    };

    // 商品丢失
    $scope.goodsLost = function(problemResource, amount){
      var ifScaned="";
      if($scope.goodsDetailClick.amount>0&&
          $scope.goodsDetailClick.amount==$scope.goodsDetailClick.amountScaned){
          ifScaned="true";
      }
      problemOutboundService.damageConfirm({
        "solveId": $scope.goodsDetail.solveId,
        "itemNo": $scope.goodsDetail.itemNo,
        "shipmentNo": $rootScope.shipmentNo,
        "solveKey": "GOODS_LOSS",
        "problemResources": problemResource || "",
        "amountConfirmProblem": amount || 0,
        "ifScaned":ifScaned
      }, function() {
          $scope.problemTypeWindow.close();
          $scope.problemTypeNoScanWindow.close();
          $scope.problemTypeScanDoneWindow.close();
          getOrderGoodsDetails();
      },function(data){
          var win = $("#mushinyWindow").data("kendoWindow");
          commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function() {
              setTimeout(function () {
                  $("#warnContent").html($translate.instant(data.key));
              }, 200);
          }})
        });
    };

    // 商品转为正品
    $scope.goodsToGenuine = function(problemResource, amount,serialNo){
      problemOutboundService.saveGoodsToGenuine({
        "solveId": $scope.goodsDetail.solveId,
        "itemNo": $scope.goodsDetail.itemNo,
        "shipmentNo": $rootScope.shipmentNo,
        "solveKey": "DAMAGED_TO_NORMAL",
        "problemResources": problemResource || "",
        "amountConfirmProblem": amount || 0,
        "serialNo":serialNo
      },function(){
        $scope.goodsSerialWindow.close();
        $scope.problemTypeWindow.close();
        getOrderGoodsDetails();
      },function(data){
          var win = $("#mushinyWindow").data("kendoWindow");
          commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
              setTimeout(function(){
                  $("#warnContent").html($translate.instant(data.key));
              }, 200);
              win.bind("close", function(){ $scope.goodsSerialNumber = ""; setTimeout(function(){ $("#goodsSnId").focus();}, 600); });
          }});
      });
    };

      // 生成拣货任务
      $scope.confirmPickingTask =  function() {
          console.log("goodsDetail:",$scope.goodsDetail);
          problemOutboundService.generateNewPickingTasks({
              "solveId": $scope.goodsDetail.solveId,
              "itemNo": $scope.goodsDetail.itemNo,
              "shipmentNo": $rootScope.shipmentNo,
              "solveKey": "HAS_HOT_PICK",
              "itemDataId":$scope.goodsDetail.itemDataId,
              "amountHotPick":$scope.goodsDetail.amount
          },function(){
              $scope.pickingTaskWindow.close();
              getOrderGoodsDetails();
          });
      };

    // 分配货位取货
    $scope.distributionPickingTask = function(){
      $scope.pickingTaskWindow.close();
      $scope.errorWindow({id: "#confirmDistributionId", name: $scope.confirmDistributionWindow, height: 300});
      var columns = [
        {field: "",width: 35, template: "<input type=\"checkbox\"  ng-model='chk' id='checkId' class='check-box' style='zoom:150%' checked=false  ng-click='selectOne(chk,dataItem.uid)'/>"},
        {field: "name", width: 130, headerTemplate: "<span translate='位置'></span>"},
        {field: "amount", width:70, headerTemplate: "<span translate='数量'></span>"},
        {field:"输入数量",width:70,template:"<input id='confirmAmountId' type='number' style='width:50px' min='0' value='0' max='{{ goodsDetail.amount }}' class='k-textbox'>"}
      ];
      $scope.confirmDistributionGidOptions = {selectable: "row", scrollable: true, columns: columns, height: 200};
      // 加载数据
      problemOutboundService.getAssignedLocation($scope.goodsDetailClick.itemNo,$rootScope.sectionId,function(datas){
        var grid = $("#confirmDistributionGrid").data("kendoGrid");
        grid.setDataSource(new kendo.data.DataSource({data: datas}));
      });
    };

      $scope.selectOne=function(val,uid){
          var grid = $("#confirmDistributionGrid").data("kendoGrid");
          if(val){
              grid.tbody.children('tr[data-uid="' + uid + '"]').addClass('k-state-selected');
          }else{
              grid.tbody.children('tr[data-uid="' + uid + '"]').removeClass('k-state-selected');
          }
      };

      // 分配货位取货--确定
      $scope.confirmDistributionSure = function(){
          var grid = $("#confirmDistributionGrid").data("kendoGrid"), row= grid.select();
          if(!row.length) return;
          var amount=$scope.goodsDetailClick.amount; //需要拣货的数量
          var amounts="",amountProblem=0;
          $("input[id='confirmAmountId']").each(function(){
              if($(this).val()!=""&&$(this).val()!=0){
                  amounts+=$(this).val()+"/";
                  amountProblem+=parseInt($(this).val()); //前台判断用户输入的数量是否正确
              }
          });
          if(amountProblem!=amount){
              var win = $("#mushinyWindow").data("kendoWindow");
              commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                  setTimeout(function(){
                      $("#warnContent").html($translate.instant("货位或数量有误，请重新选择!"));
                  }, 200);
              }});
              return ;
          }
          for(var i=0,datas=[],items="";i<row.length;i++){
              datas.push(grid.dataItem(row[i]).name);
              items+=grid.dataItem(row[i]).name+"/";
          }
          if(items.length>0)  items=items.substr(0,items.length-1);
          if(amounts.length>0)  amounts=amounts.substr(0,amounts.length-1);
          $scope.location=items;  //货位名称
          $scope.amounts=amounts;  //货位对应的数量
            assignedPicking();
      };

      function assignedPicking(){
          problemOutboundService.assignedPicking({
              "solveId": $scope.goodsDetailClick.solveId,
              "itemNo": $scope.goodsDetailClick.itemNo,
              "shipmentNo": $rootScope.shipmentNo,
              "location": $scope.location,
              "solveKey": "ASSIGNED_LOCATION",
              "description":$scope.amounts
          }, function(){
              $scope.confirmDistributionWindow.close();
              getOrderGoodsDetails();
          });
      }

      // 获取拆单明细
    function getDemolitionDatas(){
      var grid = $("#outGoodsProblemGrid").data("kendoGrid"), datas = grid.dataSource.data();
      for(var i = 0, items = []; i < datas.length; i++) {
          var data = datas[i];
          // if(data.solveKey == "CONFIRM_DAMAGED" && data.stockUnitAmount <= 0) continue;
          //如果同一种商品既有丢失库存为0又有条码无法扫描
          if (data.stockUnitAmount!=null&&data.stockUnitAmount <= 0) {
              if (data.problemType != "UNABLE_SCAN_SKU") continue;
           //   else  data.set("amountProblem", 0);
          }
          items.push(data);
      }
      console.log("拆单:",items);
      return items;
    }

    // 拆单发货
    $scope.demolitionShip = function(){
      $scope.errorWindow({id: "#demolitionGoodsDetailsId", name: $scope.demolitionGoodsDetailsWindow, height: 350});
      // 拆单后商品明细
      var columns = [
        {field: "skuNo", width:150,attributes:{style:"text-align:center"}, headerTemplate: "<span translate='商品条码'></span>"},
        {field: "itemName", width:300,attributes:{style:"text-align:left"}, headerTemplate: "<span translate='商品名称'></span>"},
        {field: "amount", width: 100,attributes:{style:"text-align:center"}, headerTemplate: "<span translate='总数量'></span>"}
      ];
      $scope.demolitionGoodsDetailsGidOptions = {scrollable: true, dataSource: getDemolitionDatas(), columns: columns, height: 240};
    };

    // 拆单发货--确定
    $scope.demolitionGoodsDetailsSure = function(){
      problemOutboundService.demolitionShip({
        "shipmentNo": $rootScope.shipmentNo,
        "solveKey": "DISMANTLE_SHIPMENT"
      }, function(data){
        $scope.demolitionGoodsDetailsWindow.close();
        $scope.orderProcessing="orderSuccess";
      });
    };

    // 删除订单
    $scope.deleteOrder = function(){
      $scope.errorWindow({id: "#demolitionDeleteId", name: $scope.demolitionDeleteWindow, height: 400});
      $scope.demolitionDelete = true;
      $scope.licensePlateNumber = false;
      // 商品明细
      var columns = [
        {field: "skuNo", width:150,attributes:{style:"text-align:center"} ,headerTemplate: "<span translate='商品条码'></span>"},
        {field: "itemName", width:300, attributes:{style:"text-align:left"},headerTemplate: "<span translate='商品名称'></span>"},
        {field: "amount",width:100,attributes:{style:"text-align:center"},headerTemplate: "<span translate='总数量'></span>"}
      ];
      $scope.demolitionDeleteGidOptions = {scrollable: true, dataSource: getDemolitionDatas(), columns: columns, height: 240};
    };

    // 删除订单--确认
    $scope.demolitionDeleteSure =  function(){
        var grid= $("#outGoodsProblemGrid").data("kendoGrid"), datas = grid.dataSource.data();
        for(var solveId="",i=0;i<datas.length;i++){
            if(datas[i].stockUnitAmount!=null&&datas[i].stockUnitAmount<=0) solveId=datas[i].solveId;
        }
      problemOutboundService.deleteOrderSuccess({
        "shipmentNo": $rootScope.shipmentNo,
        "solveKey": "OUT_OF_STOCK_DELETE_ORDER",
        "solveId":solveId
      }, function(){
        $scope.plateNumber=""; //初始为空
        $scope.demolitionDelete = false;
        $scope.licensePlateNumber = true;
        $scope.lpNumber = 'one';
        setTimeout(function(){ $("#obp_plateNumber").focus();}, 300);
      });
    };

    // 扫描上架车牌
    $scope.plateNumbers = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if(keycode != 13) return;
        var grid = $("#demolitionDeleteGrid").data("kendoGrid"), datas = grid.dataSource.data();
        //需要放到上架车牌的正常商品数量
        for(var i = 0,amoutNormal=0; i < datas.length; i++){
            amoutNormal+=datas[i].amount;
        }
        $scope.amoutNormal=amoutNormal;
      problemOutboundService.deleteOrderScanGoods({
        "shipmentNo": $rootScope.shipmentNo,
        "containerName": $scope.plateNumber,
        "solveKey": "OUT_OF_STOCK_DELETE_ORDER",
        "itemNo":"",
        "amountNormal":$scope.amoutNormal
      }, function(){
        //$scope.demolitionDeleteWindow.set("title", "请逐一扫描订单内的商品");
          $scope.demolitionDeleteWindow.close();
          $scope.licensePlateNumberx = true;
          $scope.scanWinGoods="";  //清空上次扫描商品输入框
          $scope.errorWindow({id: "#demolitionDeleteIdx", name: $scope.demolitionDeleteWindowx, height: 400, open:function(){
              $scope.lpNumber = 'two';
              setTimeout(function(){ $("#obp_scanWinGoods").focus();}, 600);
          }
          });
          var columnsItem = [
              {field: "skuNo", width:150,attributes:{style:"text-align:center"} ,headerTemplate: "<span translate='商品条码'></span>"},
              {field: "itemName", width:300, attributes:{style:"text-align:left"},headerTemplate: "<span translate='商品名称'></span>"},
              {width:100,attributes:{style:"text-align:center"},headerTemplate: "<span translate='扫描数/总数量'></span>",template:function (item){
                  return  item.amountDelete+"/"+item.amount;
               }}
          ];
        $scope.demolitionDeleteGidOptionsx = {scrollable: true, dataSource: getDemolitionDatas(), columns: columnsItem, height: 240};
       // setTimeout(function(){ $("#obp_scanWinGoods").focus();}, 300);
      },function(data){
          var win = $("#mushinyWindow").data("kendoWindow");
          commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
              setTimeout(function(){
                  $("#warnContent").html($translate.instant(data.key));
              }, 200);
              win.bind("close", function(){ focusObpPlate(); });
          }});
      });
    };

    function focusObpPlate(){
        $scope.plateNumber="";
        setTimeout(function(){ $("#obp_plateNumber").focus();}, 600);
    }

      function focusObpItemNo(){
          $scope.scanWinGoods="";
          setTimeout(function(){ $("#obp_scanWinGoods").focus();}, 600);
      }

    // 逐一扫描商品
    $scope.scanWindowGoods = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if(keycode != 13) return;
      var grid = $("#demolitionDeleteGridx").data("kendoGrid"), datas = grid.dataSource.data();
      // 判断扫描商品是否存在
      for(var i = 0, targetItem = null; i < datas.length; i++){
        var data = datas[i];
        if(data.itemNo === $scope.scanWinGoods||data.skuNo === $scope.scanWinGoods){
            targetItem = data;
            $scope.scanGoodsNo=$scope.scanWinGoods;
            break;
        }
      }
        console.log("targetItem",targetItem);
        $scope.goodsDetail=targetItem;
        $scope.goodsDetailClick=targetItem;
        if(targetItem!=null) {
            if(targetItem.amountDelete == targetItem.amount) return;
            if (targetItem.lotMandatory) {
                $scope.openLotWindow();
                $scope.useNotAfterKey = "OUT_OF_STOCK_DELETE_ORDER";
            }
            else if (targetItem.serialRecordType === "ALWAYS_RECORD") $scope.openSerialWindow(targetItem,datas);
            else checkSuccess(targetItem, datas);
        }else{
            checkSuccess(targetItem, datas);
        }
    };

      // 商品有效期
      $scope.openLotWindow = function(){
          var isProduce = ($scope.goodsDetail.lotType=="MANUFACTURE");
          $scope.goodDate = (isProduce? 'produce': "maturity");
          if(isProduce){
              if($scope.goodsDetail.lotUnit.toUpperCase()=="MONTH")
                  $scope.lotUnit="月";
              if($scope.goodsDetail.lotUnit.toUpperCase()=="YEAR")
                  $scope.lotUnit="年";
              if($scope.goodsDetail.lotUnit.toUpperCase()=="DAY")
                  $scope.lotUnit="日";
          }
          $scope.errorWindow({
              id: "#commodityValidId",
              name: $scope.commodityValidWindow,
              height: 530,
              open: function(){
                  $scope.produce_year = ""; $scope.produce_month = ""; $scope.produce_day = ""; $scope.obp_months = "";
                  $scope.maturity_year = ""; $scope.maturity_month = ""; $scope.maturity_day = "";
                  $("#maturity_year").val(""); $("#maturity_month").val(""); $("#maturity_day").val("");
                  $("#produce_year").val(""); $("#produce_month").val(""); $("#produce_day").val(""); $("#obp_months").val("");
                  $scope.useNotAfter = "";
                  setTimeout(function(){ $(isProduce? "#produce_year": "#maturity_year").focus();}, 600);
              }
          });
      };

      // （无库存删单）序列号
      $scope.openSerialWindow = function(data,items){
          $scope.errorWindow({
              id: "#goodsSerialNoId",
              name: $scope.goodsSerialWindow,
              open: function () {
                  $scope.goodsSerialNumber = ""; // 初始为空
                  setTimeout(function(){ $("#goodsSnId").focus();}, 600);
                  $scope.goodsData=data;  $scope.goodsDatas=items;
                  $scope.key="OUT_OF_STOCK_DELETE_ORDER";
              }
          });
      };

      // （确认残损，残品转正品）序列号弹窗
      $scope.openSNWindow = function(key,problemResource,amount){
          $scope.errorWindow({
              id: "#goodsSerialNoId",
              name: $scope.goodsSerialWindow,
              height:300,
              open: function () {
                  $scope.goodsSerialNumber = ""; // 初始为空
                  setTimeout(function(){ $("#goodsSnId").focus();}, 600);
                  $scope.key=key; $scope.problemResource=problemResource;
                  $scope.confirmAmount=amount;
              }
          });
      };

      // 扫描序列号
      $scope.goodsSerialNumbers = function (e) {
          var keycode = window.event ? e.keyCode : e.which;
          if(keycode != 13) return;
          // $scope.goodsSerialWindow.close();
          if($scope.key=="CONFIRM_DAMAGED"){
             $scope.confirmDamage($scope.problemResource,$scope.confirmAmount,$scope.goodsSerialNumber);
          }else if($scope.key=="DAMAGED_TO_NORMAL"){
             $scope.goodsToGenuine($scope.problemResource,$scope.confirmAmount,$scope.goodsSerialNumber);
          } else if($scope.key=="OUT_OF_STOCK_DELETE_ORDER"){
              $scope.goodsSerialWindow.close();
              checkSuccess($scope.goodsData,$scope.goodsDatas);
          }
      };

      function checkSuccess(data,datas){
      problemOutboundService.deleteOrderScanGoods({
         "containerName": $scope.plateNumber,
         "shipmentNo": $rootScope.shipmentNo,
         "itemNo": data?$scope.scanGoodsNo:"1",
         "solveKey": "OUT_OF_STOCK_DELETE_ORDER",
         "useNotAfter": $scope.useNotAfter, // 缺详细计算
         "amountNormal":$scope.amoutNormal,
         "serialNo":$scope.goodsSerialNumber
      }, function(){
          $scope.goodsSerialWindow.close();
          $scope.scanWinGoods="";
         // targetItem.amountScaned+1 <= targetItem.amount && targetItem.set("amountScaned", targetItem.amountScaned+1);
          var scanAmount = data.get("amountDelete")+1 ; //用amountDelete表示扫描数是业务需要
          if (scanAmount - data.amount>0) scanAmount = data.amount ;
          data.set("amountDelete", scanAmount);
          var scan=0,num=0;
          for(var i=0;i<datas.length;i++){
              scan+=datas[i].amountDelete;
              num+=datas[i].amount;
           }
           if(scan==num) {
              $scope.licensePlateNumberx = false;
              $scope.goodsMore = true;
           }
      }, function(data){
          if(data.key == "EX_CONTAINER_SKU_DIFFERENT_CLIENT"){
              $scope.lpNumber = "three";
              $scope.plateNumber="";
              setTimeout(function(){ $("#obp_plateNumber2").focus();}, 600);
          } else if(data.key == "EX_CONTAINER_SKU_DIFFERENT_LOT"){
              $scope.lpNumber = "four";
              $scope.plateNumber="";
              setTimeout(function(){ $("#obp_plateNumber3").focus();}, 600);
          }else{
              var win = $("#mushinyWindow").data("kendoWindow");
              commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                  setTimeout(function(){
                      $("#warnContent").html($translate.instant(data.key));
                  }, 200);
                  win.bind("close", function(){ focusObpItemNo(); });
              }});
          }
      });
    }

    // 条码无法扫描
    $scope.barcodeNotScanned = function(problemResource, amount){
      $scope.printCodeAmount=amount;
      if(amount===0)
          $scope.printCodeAmount=1;
      $scope.problemTypeWindow.close();
      $scope.problemTypeNoScanWindow.close();
      $scope.problemTypeScanDoneWindow.close();
      var ifScaned="";
      if($scope.goodsDetailClick.amount>0&&
          $scope.goodsDetailClick.amount==$scope.goodsDetailClick.amountScaned){
          ifScaned="true";
      }
      problemOutboundService.markUpBarcode({
        "shipmentNo": $rootScope.shipmentNo,
        "solveId": $scope.goodsDetailClick.solveId,
        "itemNo": $scope.goodsDetailClick.itemNo,
        "solveKey": "UNABLE_SCAN_SKU",
        "problemResources": problemResource || "",
        "amountConfirmProblem": amount || 0,
        "ifScaned":ifScaned
      }, function() {
        $scope.errorWindow({id: "#newPickingId", name: $scope.newPickingWindow, height: 300});
      });
    };

    // 补打条码
    $scope.fillTheBarCode = function(){
      problemOutboundService.markUpBarcode({
        "shipmentNo": $rootScope.shipmentNo,
        "solveId": $scope.goodsDetailClick.solveId,
        "itemNo": $scope.goodsDetailClick.itemNo,
        "solveKey": "PRINT_SKU_REPAIR"
      }, function(data) {
        console.log("补打条码-------",data);
        var json={"ip":"192.168.1.233","type":"tools","data":[{"goodsItemNo":data.itemNo,"goodsDescript":data.name}]};
        for(var i = 1; i < $scope.printCodeAmount; i++){
            json.data.push({"goodsItemNo":data.itemNo,"goodsDescript":data.name});
         }
        problemOutboundService.printSku(JSON.stringify(json),(function(){
            $scope.newPickingWindow.close();
            getOrderGoodsDetails();
        }));
      });
    };

    // =================================================================初始化===================================================================
    $scope.orderProcessing = 'processing';
    $scope.clearTheProblem = true;
    $scope.problemTypeButton = "noScan";
    $scope.operation = "checkGoods";
    focusItemNo();

    // shipment
    var columns = [
      {field: "shipmentNo", headerTemplate: "<span translate='订单号码'></span>"},
      {field: "cell", headerTemplate: "<span translate='问题货格'></span>"},
      {field: "problemType", headerTemplate: "<span translate='问题类型'></span>",template:function(item){
          return item.problemType?item.problemType.split(" ")[0]:"";
      }},
      {field: "reportBy", headerTemplate: "<span translate='触发人员'></span>"},
      {field: "jobType", headerTemplate: "<span translate='触发环节'></span>"},
      {field: "reportDate", headerTemplate: "<span translate='触发问题时间'></span>", template: function(item){
        return item.reportDate? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.reportDate)): "";
      }},
      {field: "obpsCreateDate", width:180, headerTemplate: "<span translate='OBPS添加问题时间'></span>", template: function(item){
        return item.obpsCreateDate? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.obpsCreateDate)): "";
      }},
      {field: "exSD", headerTemplate: "<span translate='预计发货时间点'></span>", template: function(item){
        return item.exSD? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.exSD)): "";
      }},
      {field: "problemStorageLocation", headerTemplate: "<span translate='触发容器'></span>"},
      {field: "timeCondition", headerTemplate: "<span translate='距离发货时间'></span>", template: function(item){
        return item.timeCondition? item.timeCondition: "";
      }}
    ];
    $scope.outProblemGridOptions = {scrollable: true, columns: columns, height: 60};
    getOrderDetails();

    // 商品明细
    var columnsGoods = [
      {field: "skuNo", width:150, headerTemplate: "<span translate='商品条码'></span>"},
      {field: "itemName",width:300,attributes:{style:"text-align:left"}, headerTemplate: "<span translate='商品名称'></span>"},
      {headerTemplate: "<span translate='扫描数/总数'></span>", template: function(item){
        return item.amountScaned + "/"+ item.amount;
      }},
      {field: "amount", headerTemplate: "<span translate='总数量'></span>"},
      {headerTemplate: "<span translate='备注'></span>", template: function(item) {
        var text = "待扫描";
        if (item.problemType == "DAMAGED") {
          if (item.solveKey == "CONFIRM_DAMAGED") text = "<div>已确认残损</div>";
          else if (["HAS_HOT_PICK", "ASSIGNED_LOCATION"].indexOf(item.solveKey) >= 0) text = "";
          else text = "<div>报残" + item.amountProblem + "件</div>";
          text += (item.amountScaned == item.amount ? "<div>扫描完成</div>" : "");
        } else if (item.problemType == "LOSE") {
          if (item.solveKey == "GOODS_LOSS" || item.solveKey=="OUT_OF_STOCK_DELETE_ORDER") {
              var src = (item.stockUnitAmount> 0 ? "image/activeFire.png" : "image/noStock.png");
              var content = (item.stockUnitAmount> 0 ? "生成新拣货任务" : "库存为零");
              text = "<img src='" + src + "' width='32' height='32' /><div>" + content + "</div>";
              if(content=="库存为零") {$scope.clearTheProblem = false; $scope.demolitionDelivery= true;}
          }
          else if (item.solveKey == "HAS_HOT_PICK")
            text = "<img src='image/inactiveFire.png' width='32' height='32' /><div>已生成拣货任务</div>";
          else if (item.solveKey == "ASSIGNED_LOCATION") {
              if (item.location.indexOf("/") >= 0) {
                  text = "<img src='image/inactiveFire.png' width='32' height='32' /><div>已分配货位</div><div>";
                  for (var i = 0; i < item.location.split("/").length; i++) {
                      text += item.location.split("/")[i] + "<br>";
                  }
                  text += "</div>";
              } else text = "<img src='image/inactiveFire.png' width='32' height='32' /><div>已分配货位</div><div>" + item.location + "</div>";
          }
          else
            text = "<div>丢失" + item.amountProblem + "件</div>";
            text += (item.amountScaned == item.amount? "<div>扫描完成</div>" : "");
        } else if (item.problemType == "UNABLE_SCAN_SKU") {
          if (item.solveKey == "PRINT_SKU_REPAIR") text = "<div>条码已补打</div>";
          else text = "条码无法扫描" + item.amountProblem + "件";
          text += (item.amountScaned == item.amount ? "<div>扫描完成</div>" : "");
        } else if (item.problemType == "UNABLE_SCAN_SN"){
          if(item.solveKey == "TO_BE_INVESTIGATED"||item.solveKey=="OUT_OF_STOCK_DELETE_ORDER")
            if(item.stockUnitAmount>0) {
               text = "<img src='image/activeFire.png' width='32' height='32' /><div>生成新拣货任务</div>";
            }else{
               text = "<img src='image/noStock.png' width='32' height='32' /><div>库存为零</div>";
               $scope.clearTheProblem = false; $scope.demolitionDelivery= true;
            }
          else if(item.solveKey=="HAS_HOT_PICK")
            text = "<img src='image/inactiveFire.png' width='32' height='32' /><div>已生成拣货任务</div>";
          else if (item.solveKey == "ASSIGNED_LOCATION") {
              if (item.location.indexOf("/") >= 0){
                  text = "<img src='image/inactiveFire.png' width='32' height='32' /><div>已分配货位</div><div>";
                  for (var i = 0; i < item.location.split("/").length; i++) {
                      text += item.location.split("/")[i]  + "<br>";
                  }
                  text += "</div>";
              } else text = "<img src='image/inactiveFire.png' width='32' height='32' /><div>已分配货位</div><div>" + item.location + "</div>";
          }else
              text = "<div>序列号无法扫描" + item.amountProblem + "件</div>";
              text += (item.amountScaned == item.amount ? "<div>扫描完成</div>" : "");
        }else if(item.amountScaned == item.amount)
          text = "扫描完成";
        else if(item.amountScaned > 0)
          text = "正在扫描";
        return text;
      }},
      {headerTemplate: "<span translate='操作'></span>", template: function(item){
        var text = "";
        if(item.problemType == "DAMAGED"){
          if (item.solveKey == "CONFIRM_DAMAGED"){
            var src = (item.stockUnitAmount > 0? "image/activeFire.png": "image/noStock.png");
            var content = (item.stockUnitAmount > 0? "生成新拣货任务" : "库存为零");
            text = "<img src='"+ src+ "' width='32' height='32' /><div>" + content+ "</div>";
          }else if (item.solveKey == "HAS_HOT_PICK")
            text = "<img src='image/inactiveFire.png' width='32' height='32' /><div>已生成拣货任务</div>";
          else if (item.solveKey == "ASSIGNED_LOCATION") {
              if (item.location.indexOf("/") >= 0){
                  text = "<img src='image/inactiveFire.png' width='32' height='32' /><div>已分配货位</div><div>";
                  for (var i = 0; i < item.location.split("/").length; i++) {
                      text += item.location.split("/")[i] +"<br>";
                  }
                  text += "</div>";
              }else text = "<img src='image/inactiveFire.png' width='32' height='32' /><div>已分配货位</div><div>" + item.location + "</div>";
          }
        }
        return text;
      }}
    ];
    $scope.outGoodsProblemOptions = {scrollable: true, columns: columnsGoods, height: $(document.body).height() - 320};
    // 取数据
    getOrderGoodsDetails();
    // 设置事件
    $("#outGoodsProblemGrid").on("click", "td", function(){
      var grid = $("#outGoodsProblemGrid").data("kendoGrid"), obj = $(this), dataItem = grid.dataItem(obj.closest("tr"));
      $scope.goodsDetail = dataItem; $scope.goodsDetailClick = dataItem;
      setTimeout(function(){
        if(dataItem.get("amountScaned") == dataItem.get("amount")){
          if(dataItem.problemType == "DAMAGED"){
            $scope.errorWindow({id: "#problemTypeId", name: $scope.problemTypeWindow, height: 300}); // 选问题类型（初始）
            $scope.orderProcessing = "processing";
          }else
            $scope.errorWindow({id: "#problemTypeScanDoneId", name: $scope.problemTypeScanDoneWindow, height: 300}); // 选问题类型（扫描完成）
        }else if(obj.closest("td").find("img").length){
          if(dataItem.problemType == "DAMAGED" && dataItem.solveKey == "CONFIRM_DAMAGED" && dataItem.stockUnitAmount > 0)
            $scope.errorWindow({id: "#pickingTaskId", name: $scope.pickingTaskWindow, height: 300});
          else if(dataItem.problemType == "LOSE" && dataItem.solveKey == "GOODS_LOSS"){
              if(dataItem.stockUnitAmount>0) $scope.errorWindow({id: "#pickingTaskId", name: $scope.pickingTaskWindow, height: 300});
          }
          else if(dataItem.problemType == "UNABLE_SCAN_SN" && dataItem.solveKey == "TO_BE_INVESTIGATED")
            $scope.errorWindow({id: "#pickingTaskId", name: $scope.pickingTaskWindow, height: 300});
        }else if(["CONFIRM_DAMAGED", "HAS_HOT_PICK", "ASSIGNED_LOCATION","GOODS_LOSS"].indexOf(dataItem.solveKey) < 0)
          $scope.errorWindow({id: "#problemTypeNoScanId", name: $scope.problemTypeNoScanWindow, height: 300}); // 选问题类型（未扫描）
      }, 240);
    });
  });
})();