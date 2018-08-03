/**
 * Created by thoma.bian on 2017/5/10.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("problemOutboundForcedCtl", function ($scope, $state,$rootScope, problemOutboundService,problemOutboundBaseService,commonService,$translate) {

    $scope.shipmentNo = "";
    $scope.forcedDelete = 'ONE';
    $scope.deleteListContent='deleteList';
    $scope.state = "unsolved";
    $scope.selectTwo = "NG";

    $scope.option = {"startDate":$scope.startDate,"endDate":$scope.startDate,"shipmentNo":$scope.shipmentNo,"state": $scope.state,"obpStationId":$rootScope.obpStationId,"obpWallId":$scope.obpWallId};

    setTimeout(function(){ $("#obp_forcedDelete").focus();}, 300);
    $scope.solvingGrid = function(){
      $scope.option["shipmentNo"] = "";
      $scope.option["state"] = "unsolved";
      $scope.option["startDate"] = "";
      $scope.option["endDate"] = "";
      $("#solvingButtonId").css({"background":"#ed7d31","border":"1px solid #ed7d31"});
      $("#deleteButtonId").css({"background":"gray","border":"1px solid gray"});
      defaultShipment($scope.option);
    };

    $scope.deleteGrid = function(){
      $scope.option["shipmentNo"] = "";
      $scope.option["state"] = "solved";
      $scope.option["startDate"] = "";
      $scope.option["endDate"] = "";
      $("#solvingButtonId").css({"background":"gray","border":"1px solid gray"});
      $("#deleteButtonId").css({"background":"#ed7d31","border":"1px solid #ed7d31"});
      defaultShipment($scope.option);
    };

    function defaultShipment(data){
     problemOutboundService.forcedDeleteGrid(data,function(res) {
        if ($scope.option["state"] == 'unsolved') {
           $scope.solvingCount = res.length;
        }else if($scope.option["state"]=='solved'){
           $scope.solvedCount = res.length;
        }
       $scope.startDate="";
       $scope.endDate="";
       var grid = $("#problemForciblyDeleteListGrid").data("kendoGrid");
       grid.setDataSource(new kendo.data.DataSource({data: res}));

         $("#problemForciblyDeleteListGrid  a").each(function () {
             $(this).bind("click", function () {
                 var shipmentNo = $(this).attr("shipmentno");
                   $scope.selectTwo = "OK";
                   $scope.option["shipmentNo"] =shipmentNo ;
                   deleteShipment($scope.option);
                   setTimeout(function(){ $("#deleteContainer").focus();}, 300);
              });
         });
     });
    }

    function deleteShipment(data){
      problemOutboundService.forcedDeleteGrid(data,function(res){
        var v = res[0];
        $scope.shipmentGoods = [];
        setTimeout(function(){ $("#deleteContainer").focus();}, 300);
        //清空扫车牌输入框
        $scope.goodsToContainer="";
        //清空扫商品输入框
        $scope.checkGoodContent="";
        if(v) {
            $scope.shipmentNo = v.shipmentNo;
        if(v.state=='solved') {
            $scope.deleteListSuccess = false;
            $scope.deleteListContent = 'deleteSuccess';
        }else{
            if(v.shipmentState==1000){
                $scope.deleteListSuccess = false;
                $scope.deleteListContent = 'problemCar';
            }else{
                $scope.deleteListSuccess = true;
                $scope.deleteListContent='deleteList';
            }
        }
       if(v.solveShipmentPositions.length>0 && $scope.selectTwo == "OK"){
          $scope.forcedDelete = 'TWO';
          $scope.shipmentDate =[];
          $scope.shipmentGoods = [];
          $scope.shipmentDate = [{"shipmentNo": v.shipmentNo,"exSD":v.exSD,"timeCondition":v.timeCondition,"container":v.container}];
          for(var i = 0; i < v.solveShipmentPositions.length; i++){
            var goods = v.solveShipmentPositions[i];
            $scope.shipmentGoods.push({"number":i+1,"itemNo":goods.itemNo,"skuNo":goods.skuNo,"itemName":goods.itemName,"amountScaned":goods.amountScaned,"amount":goods.amount,"scaned":goods.scaned,"serialRecordType":goods.serialRecordType,"serialNo":goods.serialNo,"lotMandatory":goods.lotMandatory,"lotType":goods.lotType,"count":goods.count,"lotUnit":goods.lotUnit});
          }
           //
          var shipmentDateGrid = $("#shipmentGridId").data("kendoGrid");
          shipmentDateGrid.setDataSource(new kendo.data.DataSource({data: $scope.shipmentDate}));
           //
           var shipmentGoodsGrid = $("#outGoodsProblemGridId").data("kendoGrid");
           shipmentGoodsGrid.setDataSource(new kendo.data.DataSource({data:  $scope.shipmentGoods}));
           for(var j = 0; j <$scope.shipmentGoods.length; j++){
             var data = $scope.shipmentGoods[j];
             setBackgound(data,j,shipmentGoodsGrid);
           }
         }
        }
      },function(data){
          var win = $("#mushinyWindow").data("kendoWindow");
          commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
              setTimeout(function(){
                  $("#warnContent").html($translate.instant(data.key));
              }, 200);
              win.bind("close", function(){
                  $scope.shipmentNo=""; setTimeout(function(){$("#obp_forcedDelete").focus();},300) });
          }});
          });
    }

    var columns = [
      {field: "shipmentNo",attributes:{style:"text-align:center"},headerTemplate: "<span translate='订单号码'></span>", template: function (item) {
         return "<a class='shipment' href='javascript:void(0)' shipmentno='"+item.shipmentNo+"'>" + item.shipmentNo + "</a>";
      }},
      {field: "exSD",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='预计发货时间'></span>", template: function (item) {
          return item.exSD ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.exSD)) : "";
        }
      },
      {field: "solveDate",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='删单时间'></span>"},
      {field: "description",attributes:{style:"text-align:center"}, width: 400,headerTemplate: "<span translate='删单原因'></span>"},
      {field: "solveBy",attributes:{style:"text-align:center"},headerTemplate: "<span translate='删单人员'></span>"}];
    $scope.problemForciblyDeleteListGridOptions  = problemOutboundBaseService.grid("", columns,$(document.body).height() - 250);
      $scope.option["state"]="solved";
      solvingAndSolved($scope.option);

    function solvingAndSolved(data){
          problemOutboundService.forcedDeleteGrid(data,function(res) {
              if(data.state=="solved"){
                  $scope.solvedCount = res.length;
                  data.state="unsolved";
                  problemOutboundService.forcedDeleteGrid(data,function(res) {
                      $scope.solvingCount = res.length;
                      $scope.startDate="";
                      $scope.endDate="";
                      var grid = $("#problemForciblyDeleteListGrid").data("kendoGrid");
                      grid.setDataSource(new kendo.data.DataSource({data: res}));

                      $("#problemForciblyDeleteListGrid  a").each(function () {
                          $(this).bind("click", function () {
                              var shipmentNo = $(this).attr("shipmentno");
                              $scope.selectTwo = "OK";
                              $scope.option["shipmentNo"] =shipmentNo ;
                              deleteShipment($scope.option);
                              setTimeout(function(){ $("#deleteContainer").focus();}, 300);
                          });
                      });
                  });
              }
          });
    }
    $scope.deleteListInput = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $scope.selectTwo = "OK";
        $scope.option["startDate"] =$scope.startDate;
        $scope.option["endDate"] =$scope.endDate;
        $scope.option["shipmentNo"] = $scope.shipmentNo;
        deleteShipment($scope.option);
      }
    };

    //时间搜索
    $scope.dateSearch = function(){
      $scope.option["startDate"] =$scope.startDate;
      $scope.option["endDate"] =$scope.endDate;
      $scope.option["shipmentNo"] = $scope.shipmentNo;
     // deleteShipment($scope.option);
     defaultShipment($scope.option);
    };

    //shipmentNo搜索
    $scope.shipmentNoSearch = function(){
      $scope.selectTwo = "OK";
      $scope.option["startDate"] =$scope.startDate;
      $scope.option["endDate"] =$scope.endDate;
      $scope.option["shipmentNo"] = $scope.shipmentNo;
      deleteShipment($scope.option);
    };

    var shipmentColumns = [
      {field: "shipmentNo",attributes:{style:"text-align:center"},headerTemplate: "<span translate='订单号码'></span>"},
      {field: "exSD",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='预计发货时间'></span>", template: function(item){
        return item.exSD? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.exSD)): "";
      }},
      {field: "timeCondition",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='距离发货时间'></span>", template: function(item){
        return item.timeCondition? item.timeCondition: "";
      }},
      {field: "container ",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='问题车牌'></span>"}];
    $scope.shipmentGridOptions =  problemOutboundBaseService.grid("",shipmentColumns,80);

    var goodsColumns = [
      {field: "number",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='编号'></span>"},
      {field: "skuNo",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='商品条码'></span>"},
      {field: "itemName",attributes:{style:"text-align:left"}, width: 300, headerTemplate: "<span translate='GOODS_NAME'></span>"},
      {field: "amountScaned",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='扫描数/总数'></span>", template: function(item){
         return item.amountScaned + "/"+ item.amount;
      }},
      {field: "amount",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='总数量'></span>"},
      {field: "scaned",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='备注'></span>", template: function (item) {
        var text = "待扫描";
        if (item.amountScaned == item.amount) {
          text = "扫描完成";
        }else if (item.amountScaned > 0) {
          text = "正在扫描";
        }
        return text;
       }}
    ];
    $scope.outGoodsProblemOptions = problemOutboundBaseService.grid("", goodsColumns,350);

    //删除订单
    $scope.deleteList = function(){
      $("#deleteListId").parent().addClass("myWindow");
      $scope.deleteListWindow.setOptions({
        width: 700,
        height: 270,
        visible: false,
        actions: false
      });
        var grid = $("#outGoodsProblemGridId").data("kendoGrid");
        var datas = grid.dataSource.data();
        $scope.itemAmount=0;
        for (var i = 0; i < datas.length; i++) {
            $scope.itemAmount += datas[i].amount;
        }
      $scope.deleteListWindow.center();
      $scope.deleteListWindow.open();
      $scope.deleteReason="";
     // setTimeout(function(){$("#simple-textarea").focus();}, 300);
    };

    //删除订单确认
    $scope.deleteListSure = function(){
      problemOutboundService.forcedDeleteOrder({"shipmentNo":$scope.shipmentNo,"deleteReason":$scope.deleteReason},function(){
        $scope.deleteListSuccess = false;
        $scope.deleteListWindow.close();
        $scope.deleteListContent = 'problemCar';
          setTimeout(function(){ $("#deleteContainer").focus();}, 300);
      });
    };

    //请扫描问题车牌用于放置删单商品
    $scope.deleteCars = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode != 13)  return;
        problemOutboundService.putForceDeleteGoodsToContainer({"containerName":$scope.goodsToContainer,"shipmentNo":$scope.shipmentNo,"itemNo":"","serialNo":"","useNotAfter":"","amount":"","solveKey":"DELETE_ORDER_FORCE"},function(data){
          $scope.deleteListContent = 'checkGoodsContent';
          $scope.obpContainer=$scope.goodsToContainer;
            setTimeout(function(){ $("#deleteGoods").focus();}, 300);
            $scope.shipmentDate[0]["container"]=$scope.goodsToContainer;
            var shipmentDateGrid = $("#shipmentGridId").data("kendoGrid");
            shipmentDateGrid.setDataSource(new kendo.data.DataSource({data: $scope.shipmentDate}));
        },function(data){
            var win = $("#mushinyWindow").data("kendoWindow");
            commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                setTimeout(function(){
                    $("#warnContent").html($translate.instant(data.key));
                }, 200);
                win.bind("close", function(){
                    $scope.goodsToContainer=""; setTimeout(function(){$("#deleteContainer").focus();},300) });
            }});
        });
    };

    $scope.checkGoodsContents = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        var grid = $("#outGoodsProblemGridId").data("kendoGrid");
        var gridCount = true;
        var datas = grid.dataSource.data();
        var itemN="";
        for (var i = 0; i < datas.length; i++) {
          var data = datas[i];
          //商品是否存在
          if (data.itemNo == $scope.checkGoodContent||data.skuNo == $scope.checkGoodContent) {
              $scope.serialRecordType = grid.dataSource.at(i).get("serialRecordType");
              $scope.serialNo = grid.dataSource.at(i).get("serialNo");
              $scope.lotMandatory = grid.dataSource.at(i).get("lotMandatory");
              $scope.lotType = grid.dataSource.at(i).get("lotType");
              $scope.count = grid.dataSource.at(i).get("count");
              $scope.amount = grid.dataSource.at(i).get("amount");
              $scope.itemNo = grid.dataSource.at(i).get("itemNo");
              $scope.lotUnits = grid.dataSource.at(i).get("lotUnit");
              itemN = $scope.itemNo;
              $scope.goodsDetail = data;
              if (data.skuNo == $scope.checkGoodContent) {
                  $scope.itemNo = grid.dataSource.at(i).get("skuNo");
                  itemN = $scope.itemNo;
              }
              $scope.saveGoods = {
                  "containerName": $scope.obpContainer,
                  "shipmentNo": $scope.shipmentNo,
                  "serialNo": $scope.serialNo,
                  "itemNo": $scope.itemNo,
                  "useNotAfter": "",
                  "amount": $scope.amount,
                  "solveKey": "DELETE_ORDER_FORCE"
              };
              if (data.amount > 0 && data.amountScaned == data.amount) {
                  gridCount = false;
              }
              if (data.amountScaned > data.amount) {
                  gridCount = false;
              }
              break;
          }
        }
          //商品条码错误弹窗
          if(itemN=="") {
             $scope.checkGoodContent="";
             setTimeout(function(){ $("#deleteGoods").focus();}, 300);
             $scope.scanErrorWindow("#scanErrorWindowId", $scope.scanErrorWindows, 200);
             return ;
          }
          if(gridCount == true) {
            if ($scope.lotMandatory == true) {
            if ($scope.lotType == "EXPIRATION") {
              $scope.maturity_year=""; $scope.maturity_month=""; $scope.maturity_day="";
              $("#maturity_year").val(""); $("#maturity_month").val(""); $("#maturity_day").val("");
              $scope.goodDate = 'maturity';
              setTimeout(function(){$("#maturity_year").focus();}, 600);
            } else {
                if($scope.lotUnits.toUpperCase()=="MONTH")
                    $scope.lotUnit="月";
                if($scope.lotUnits.toUpperCase()=="YEAR")
                    $scope.lotUnit="年";
                if($scope.lotUnits.toUpperCase()=="DAY")
                    $scope.lotUnit="日";
                $scope.produce_year=""; $scope.produce_month=""; $scope.produce_day=""; $scope.obp_months="";
                $("#produce_year").val(""); $("#produce_month").val(""); $("#produce_day").val("");  $("#obp_months").val("");
                setTimeout(function(){$("#produce_year").focus();}, 600);
                $scope.goodDate = 'produce';
            }
            $scope.errorWindow("#commodityValidId", $scope.commodityValidWindow, 530);
          }else if($scope.serialRecordType == "ALWAYS_RECORD") {
                $scope.serialNumber="";
                $scope.errorWindow("#goodsNumberId", $scope.goodsNumberWindow);
                setTimeout(function(){$("#serialNumberId").focus();},600);
          }else checkSuccess();
        }
      }
    };

    //扫描序列号
    $scope.serialNumbers = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $scope.saveGoods["serialNo"] = $scope.serialNumber;
          checkSuccess();
          $scope.goodsNumberWindow.close();
      }
    };

    function checkSuccess(){
       problemOutboundService.putForceDeleteGoodsToContainer($scope.saveGoods, function () {
       var grid = $("#outGoodsProblemGridId").data("kendoGrid");
         var datas = grid.dataSource.data();
         //订单内商品数
         var numShipment=0;
         //订单商品已扫描数
         var numScaned=0;
         for (var j = 0; j < datas.length; j++) {
             var data = datas[j];
             if (data.itemNo == $scope.checkGoodContent || data.skuNo == $scope.checkGoodContent) {
                 var scanAmount = grid.dataSource.at(j).get("amountScaned") + 1;
                 if (scanAmount > data.amount) scanAmount = data.amount;
                 grid.dataSource.at(j).set("amountScaned", scanAmount);
                 setBackgound(data, j, grid);
             }
             numShipment += datas[j].amount;
             numScaned += datas[j].amountScaned;
         }
           if (numShipment!=0&&numScaned!=0&&numShipment == numScaned) {
               $scope.deleteListContent = 'problemCar' == false;
               $scope.deleteListContent = 'deleteFinished';
           }
         //清空扫商品输入框
         $scope.checkGoodContent="";
           setTimeout(function(){ $("#deleteGoods").focus();}, 300);
       },function(data){
           if(data.key=="EX_CONTAINER_SKU_DIFFERENT_LOT"){
               data.key="扫描车牌内存在相同名称不同有效期商品，请重新扫描车牌";
           }else if(data.key=="EX_CONTAINER_SKU_DIFFERENT_CLIENT"){
               data.key="扫描车牌内存在相同名称不同客户商品，请重新扫描车牌";
           }
           var win = $("#mushinyWindow").data("kendoWindow");
           commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
               setTimeout(function(){
                   $("#warnContent").html($translate.instant(data.key));
               }, 200);
               if(data.key.indexOf("请重新扫描车牌")>=0){
                   win.bind("close", function(){
                       $scope.deleteListContent = 'problemCar';
                       $scope.checkGoodContent="";
                       $scope.goodsToContainer=""; setTimeout(function(){$("#deleteContainer").focus();},300) });
               }else{
                   win.bind("close", function(){
                       $scope.deleteListContent="checkGoodsContent";
                       $scope.checkGoodContent=""; setTimeout(function(){$("#deleteGoods").focus();},300) });
               }
           }});
       })
    }

    function setBackgound(data,i,grid){
        var bgColor = "";
        if(data.amountScaned == data.amount) bgColor = "#c5e0b4";
        else if(data.amountScaned > 0) bgColor = "#deebf7";
        bgColor != "" && grid.tbody.find("tr:eq("+ i+ ")").css("background", bgColor);
    }

    $scope.deleteOrder = function(){
      $scope.shipmentNo="";
      $scope.startDate="";
      $scope.endDate="";
      $scope.forcedDelete = 'ONE';
      $scope.option["shipmentNo"] = "";
      $scope.option["startDate"] = "";
      $scope.option["endDate"] = "";
     // setTimeout(function(){ $("#obp_forcedDelete").focus();}, 600);
      defaultShipment($scope.option);
     };

     //日期确定按钮
    $scope.commodityValidSure = function(){
      // if($scope.count>0){
      //   $scope.errorWindow("#goodsCountId", $scope.goodsCountWindow,350);
      //   setTimeout(function(){ $("#goodsCountPosition").focus();}, 0);
      // }else{
       // checkSuccess();
      //}
        var year = 0, month = 0, day = 0;
        if($scope.goodDate == 'produce'){
            year = $("#produce_year").val();
            day = $("#produce_day").val();
            month = $("#produce_month").val();
            $scope.useNotAfterTest = year + "-" + problemOutboundBaseService.pad(month) + "-" + problemOutboundBaseService.pad(day);
            var date = new Date($scope.useNotAfterTest);
            if($scope.lotUnit=="月")
                date.setMonth(date.getMonth() + (parseInt($("#obp_months").val())|| 0));
            if($scope.lotUnit=="年")
                date.setFullYear(date.getFullYear() + (parseInt($("#obp_months").val())|| 0));
            if($scope.lotUnit=="日")
                date.setDate(date.getDate() + (parseInt($("#obp_months").val())|| 0));
        }else{
            year = $("#maturity_year").val();
            day = $("#maturity_day").val();
            month = $("#maturity_month").val();
            $scope.useNotAfterTest = year + "-" + problemOutboundBaseService.pad(month) + "-" + problemOutboundBaseService.pad(day);
            var date = new Date($scope.useNotAfterTest);
        }
        if(year>1000&&month>0&&day>0) {
          $scope.useNotAfter = date.getFullYear() + "-" + problemOutboundBaseService.pad(date.getMonth() + 1) + "-" + problemOutboundBaseService.pad(date.getDate());
          $scope.saveGoods["useNotAfter"] = $scope.useNotAfter;
          $scope.commodityValidWindow.close();
          if($scope.serialRecordType == "ALWAYS_RECORD"){
              $scope.serialNumber = "";
              $scope.errorWindow("#goodsNumberId", $scope.goodsNumberWindow);
              setTimeout(function () {$("#serialNumberId").focus();}, 600);
          }else checkSuccess();
      }
    };

    //数量大于0确定按钮
    $scope.goodsCountSure = function(){
      $scope.goodsCountWindow.close();
      checkSuccess();
    };

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

      //日期数量点击赋值
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
      }else if($scope.inputPos == 'count'){
        $("#goodsCountPosition").val($("#goodsCountPosition").val() + v);
      }
    };

    //日期光标位置
    $scope.inputPosition = function(value){
      $scope.inputPos = value;
    };

    //数量光标位置
    $scope.countPosition = function(){
      $scope.inputPos= 'count';
    };

    //清空日期
    $scope.emptyContentData = function(){
      $("#produce_year").val(""); $("#produce_month").val(""); $("#produce_day").val(""); $("#obp_months").val("");
      $("#maturity_year").val(""); $("#maturity_month").val(""); $("#maturity_day").val("");
        setTimeout(function (){ $("#produce_year").focus();}, 600);
        setTimeout(function (){ $("#maturity_year").focus();}, 600);
    };

    //清空数量
    $scope.emptyContentCount = function(){
      $("#goodsCountPosition").val("");
    };

    //错误弹窗
    $scope.errorWindow = function (windowId, windowName,height) {
      var heightValue = 250;
      if(height){
        heightValue = height;
      }
      $(windowId).parent().addClass("myWindow");
      windowName.setOptions({
        width:750,
        height: heightValue,
        visible: false,
        actions: false
      });
      windowName.center();
      windowName.open();
    };

    $scope.scanClose=function(){
        $scope.scanErrorWindows.close();
        setTimeout(function(){ $("#deleteGoods").focus();}, 300);
    };

    //扫描商品条码错误弹窗
    $scope.scanErrorWindow = function (windowId, windowName,height) {
       var heightValue = 120;
       if(height){
           heightValue = height;
        }
        windowName.setOptions({
           width:350,
           height: heightValue,
           visible: false,
           actions: false
        });
        windowName.center();
        windowName.open();
    };

  })
})();