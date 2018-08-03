/**
 * Created by frank.zhou on 2017/5/17.
 * Updated by frank.zhou on 2017/6/14.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("problemOutboundShipmentCtl", function ($scope, $rootScope, $state,$translate, problemOutboundService, problemOutboundBaseService,commonService) {
    // 错误弹窗
    $scope.errorWindow = function (options){
      $(options.id).parent().addClass("myWindow");
      var window = options.name;
      window.setOptions({width: options.width || 800, height: options.height || 250, visible: false, actions: ["close"]});
      window.center();
      options.open && window.bind("open", options.open);
      window.open();
    };

    // 查看问题格
    $scope.readProblemCell = function(){
      $rootScope.problemCellStatus = "read";
      $state.go("main.problemOutboundWall");
    };

    // 问题拣货商品分配
    $scope.problemPicker = function(){
      $("#assignGoodsTypeId").parent().addClass("windowTitle");
      $scope.assignGoodsTypeWindow.setOptions({width: 800, height: 200, visible: false, actions: ["close"]});
      $scope.assignGoodsTypeWindow.center();
      $scope.assignGoodsTypeWindow.open();
    };

    // 退出
    $scope.exitShipment = function(){
        problemOutboundService.verifySingOutObpStation($rootScope.obpStationId,function(data){
            if(data){
                var win = $("#mushinyWindow").data("kendoWindow");
                commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                    setTimeout(function(){
                        $("#warnContent").html($translate.instant("还有未到的POD,不能退出工作站!"));
                    }, 200);
                }});
                return ;
            }else{
                problemOutboundService.exitShipment($rootScope.obpStationId, function(){
                    $rootScope.obpStationName="";
                    $rootScope.obpStationId="";
                    $rootScope.sectionId="";
                    $rootScope.workStationId="";
                    $state.go("main.outbound_problem_disposal");
                });
            }
        });
    };

    // 生成拣货任务，按照拣货车分配
    $scope.accordingPickingTruckDistribution = function(){
      $state.go("main.problemOutboundPick");
    };

    // 分配到货位，按照拣货商品分配
    $scope.accordingPickingGoodsDistribution = function(){
      $state.go("main.problemOutboundGoods");
    };

   // 待处理问题
    $scope.processProblemMethod = function(){
      $scope.processProblem = true;
      $scope.closeProblem = false;
      $scope.gridId = 'problemOutboundUNScanedGrid';
      $rootScope.gridId = 'problemOutboundUNScanedGrid';
      $("#processProblemId").removeClass("buttonColorGray");
      $("#closeProblemId").addClass("buttonColorGray");
      $scope.refresh();
    };

    // 已处理完成
    $scope.closeProblemMethod = function(){
      $scope.closeProblem = true;
      $scope.processProblem = false;
      $scope.gridId = 'problemOutboundScanedGrid';
      $rootScope.gridId = 'problemOutboundScanedGrid';
      $("#processProblemId").addClass("buttonColorGray");
      $("#closeProblemId").removeClass("buttonColorGray");
      $scope.refresh();
    };

      // 获取已处理完成数量
      $scope.closeProblemMethodTwo = function(){
          $scope.closeProblem = false;
          $scope.processProblem = true;
          $("#processProblemId").removeClass("buttonColorGray");
          $("#closeProblemId").addClass("buttonColorGray");
          $scope.refreshTwo();
      };

      //进入页面默认获取正在处理信息和已处理数量
      $scope.refreshTwo=function() {
          problemOutboundService.getShipmentDealProblem({
              "obpStationId": $rootScope.obpStationId,
              "obpWallId": $rootScope.obpWallId,
              "shipmentNo": $scope.shipmentNo || "",
              "state": "solved"
          }, function (rs) {
              $scope.scanLength = rs.length;
              $scope.processProblemMethodTwo();
          });
      };

      $scope.processProblemMethodTwo=function(){
          problemOutboundService.getShipmentDealProblem({
              "obpStationId": $rootScope.obpStationId,
              "obpWallId": $rootScope.obpWallId,
              "shipmentNo": $scope.shipmentNo || "",
              "state": "unsolved"
          },function (data) {
              $scope.unScanLength = data.length;
              var grid = $("#problemOutboundUNScanedGrid").data("kendoGrid");
              grid.setDataSource(new kendo.data.DataSource({data:data}));
              //在进入订单详情界面需要获取$rootScope.gridId判断是已处理还是未处理
              $scope.gridId="problemOutboundUNScanedGrid";
              $rootScope.gridId=$scope.gridId;
              $scope.bindGridClick();
              $scope.printOrder();
          });
      };

    // 扫描
    $scope.questionOrder = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if(keycode != 13) return;
      $scope.refresh("scan");
    };

    // 查询
    $scope.refresh = function(){
      // $rootScope.shipmentNo = $scope.shipmentNo;
      var unScan = ($scope.gridId === "problemOutboundUNScanedGrid"); // 待处理
      problemOutboundService.getShipmentDealProblem({
        "obpStationId": $rootScope.obpStationId,
        "obpWallId": $rootScope.obpWallId,
        "shipmentNo": $scope.shipmentNo || "",
        "state": unScan? "unsolved": "solved"
      }, function(rs){
          $scope.shipmentNo="";//清空输入框
        // 问题
         if(rs.length == 1){
            $rootScope.shipmentNo = rs[0].shipmentNo;
            $scope.shipment = rs[0];
        }
        if(rs.length == 1 && rs[0].state === "EX_SCANNING_SHIPMENT_IS_NORMAL")
          $scope.errorWindow({id: "#dealWithProblemId", name: $scope.dealWithProblemWindow, height: 300});
        else if(rs.length == 1 && rs[0].state === "EX_SCANNING_SHIPMENT_NOT_BIND_CELL"){
          $rootScope.problemCellStatus = "bindCell";
          $state.go("main.problemOutboundWall");
        }
        // 客户删单
        else if(rs.length == 1 && rs[0].state == "DELETE_ORDER_CUSTOMER"){
          $scope.deleteOrders = "shelvesPlate";
          $scope.errorWindow({
            id:  "#orderHasDeletedId",
            name: $scope.orderHasDeletedWindow,
            height: 360,
            open: function(){
              $scope.deleteOrder="";//初始为空
              setTimeout(function(){ $("#obp_deleteOrder").focus();}, 600);
            }
          });
        }
        // 未找到(仅待处理)
        else if(!rs.length){
          $scope[unScan? "unScanLength": "scanLength"] = rs.length;
          // grid数据
          var grid = $("#" + $scope.gridId).data("kendoGrid");
          grid.setDataSource(new kendo.data.DataSource({data: []}));
        }
        // 正常
        else{
          $scope.shipment = rs[0];
          $scope[unScan? "unScanLength": "scanLength"] = rs.length;
          // grid数据
          var grid = $("#" + $scope.gridId).data("kendoGrid");
          grid.setDataSource(new kendo.data.DataSource({data: rs}));
          // 设置事件
          if($scope.gridId=='problemOutboundUNScanedGrid') {
              $scope.bindGridClick();
              $scope.printOrder();//打印订单
          }
          else if($scope.gridId=='problemOutboundScanedGrid') {
              $scope.gotoPacking();
              //$scope.gotoPack();
              $scope.printOrder();
              $scope.bindGridClick();
          }
        }
      },function(data){
          var win = $("#mushinyWindow").data("kendoWindow");
          commonService.dialogMushiny(win, {
              width: 320, height: 160, type: "warn", open: function () {
                  setTimeout(function () {$("#warnContent").html($translate.instant(data.key));}, 200);
                  win.bind("close",function(){
                      $scope.shipmentNo = ""; $rootScope.shipmentNo = "";
                      setTimeout(function(){ $("#obp_shipment").focus();}, 300);});
              }
          });
      });
    };

    // 根据shipmentNo选中跳转页面
    $scope.bindGridClick = function(){
      $("#"+ $scope.gridId+ " a").each(function () {
        $(this).bind("click", function () {
          var cell = $(this).attr("cell"), shipmentNo = $(this).attr("shipmentNo");
          $rootScope.shipmentNo = shipmentNo;
          if(cell) {
            $rootScope.obpCellName = cell;
            $state.go("main.problemOutboundDetail");
          }else{
            $rootScope.problemCellStatus = "bindCell";
            $state.go("main.problemOutboundWall");
          }
        });
      });
    };

    // 设置商品明细背景
    function setGoodsDetailsBackgound(datas){
      var grid = $("#orderDeletedGrid").data("kendoGrid");
      datas == null && (datas = grid.dataSource.data());
      for(var i = 0; i < datas.length; i++){
        var data = datas[i], bgColor = "";
        if(data.amountScaned == data.amount) bgColor = "#c5e0b4";
        else if(data.amountScaned > 0) bgColor = "#deebf7";
        bgColor != "" && grid.tbody.find("tr:eq("+ i+ ")").css("background", bgColor);
      }
    }

    // 检查扫描是否全部完成
    function checkScan(datas){
      for(var i = 0, scaned = true; i < datas.length; i++){
        var data = datas[i];
        if(data.amountScaned != data.amount){
          scaned = false;
          break;
        }
      }
      // 全部扫描完成
      if(scaned){
          $scope.lpNumber=false;
          $scope.goodMore = true;
      }
    }

      $scope.goMainProblem=function(){
         $scope.orderHasDeletedWindow.close();
         $scope.closeProblemMethodTwo();
      };

    // 刷新商品明细
    function refreshGoodsDetails(){
      var grid = $("#orderDeletedGrid").data("kendoGrid"), source = grid.dataSource;
      var row = source.at($scope.goodsDetailIndex);
      row.amountScaned+1 <= row.amount && row.set("amountScaned", row.amountScaned+1);
      setGoodsDetailsBackgound(source.data()); // 设置背景
      checkScan(source.data()); // 检查扫描完成否
    }

    // 客户删单-扫上架车牌
    $scope.clientDeleteOrder = function(e, stowContainer){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode != 13) return;
      $scope.customerShip=$rootScope.shipmentNo;
      $scope.deleteOrder = stowContainer;
        problemOutboundService.moveShelvesLicensePlate({
          "containerName": $scope.deleteOrder,
          "shipmentNo": $scope.customerShip,
          "itemNo":"",
          "serialNo": $scope.serialNumber || "",
          "useNotAfter": $scope.useNotAfter || "",
          "solveKey": "DELETE_ORDER_CUSTOMER"
        }, function(){
      $scope.deleteOrders = 'deleteGoodGrid';
      var columns = [
        {field: "skuNo", width:150,attributes:{style:"text-align:center"}, headerTemplate: "<span translate='商品条码'></span>"},
        {field: "itemName", width:300, attributes:{style:"text-align:left"},headerTemplate: "<span translate='商品名称'></span>"},
        {attributes:{style:"text-align:center"},headerTemplate: "<span translate='扫描数/总数'></span>", template: function(item){
          return item.amountScaned + "/"+ item.amount;
        }},
        {field: "amount",attributes:{style:"text-align:center"}, headerTemplate: "<span translate='总数量'></span>"},
        {attributes:{style:"text-align:center"},headerTemplate: "<span translate='备注'></span>", template: function(item){
          return item.amountScaned == item.amount? "扫描完成": "待扫描";
        }}
       ];
      $scope.orderDeletedGridGidOptions = {scrollable: true, columns: columns, height: 250};
      // 取明细
      problemOutboundService.getOrderGoodsDetails($rootScope.shipmentNo, function(datas){
        var grid = $("#orderDeletedGrid").data("kendoGrid");
        grid.setDataSource(new kendo.data.DataSource({data: datas}));
        setGoodsDetailsBackgound(datas);
       // $("#obp_deleteOrderGrid").focus();
       focusDeleteOrderGrid();
       $scope.lpNumber='one';
      });
     },function(data){
            var win = $("#mushinyWindow").data("kendoWindow");
            commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                setTimeout(function(){
                    $("#warnContent").html($translate.instant(data.key));
                }, 200);
                win.bind("close", function(){focusDeleteOrder(); });
            }});
        });
    };

    function focusDeleteOrderGrid(){
        $scope.deleteOrderGrid="";
        setTimeout(function(){ $("#obp_deleteOrderGrid").focus();}, 600);
    }

      function focusDeleteOrder(){
          $scope.deleteOrder="";
          setTimeout(function(){ $("#obp_deleteOrder").focus();}, 600);
          setTimeout(function(){ $("#obp_deleteOrder2").focus();}, 600);
          setTimeout(function(){ $("#obp_deleteOrder3").focus();}, 600);
      }

    // 逐一扫商品
    $scope.orderDeleteGrid = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode != 13) return;
      var grid = $("#orderDeletedGrid").data("kendoGrid"), datas = grid.dataSource.data();
      // 判断扫描商品是否存在
      for(var i = 0, targetItem = null; i < datas.length; i++){
        var data = datas[i];
        if(data.itemNo == $scope.deleteOrderGrid || data.skuNo == $scope.deleteOrderGrid){
          targetItem = data;
          $scope.deleteGoodsNo=$scope.deleteOrderGrid;
          $scope.goodsDetailIndex = i;
          break;
        }
      }
      // if(targetItem == null || targetItem.amountScaned == targetItem.amount) return;
      //找到和扫描完成
     if(targetItem!=null) {
         if(targetItem.amountScaned == targetItem.amount) return;
         $scope.goodsDetail = targetItem;
         if(targetItem.lotMandatory) $scope.openLotWin();
         else if(targetItem.serialRecordType === "ALWAYS_RECORD") $scope.openSerialWin();
         else $scope.scanGoods();
     }else{
         $scope.goodsDetail=null;
         $scope.scanGoods();
     }
    };

    // 商品有效期
    $scope.openLotWin = function(){
      var targetItem = $scope.goodsDetail, isProduce = (targetItem.lotType=="MANUFACTURE");
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

    // 商品有效期-确认
    $scope.commodityValidSure = function(){
        // 到期日
        var year = 0, month = 0, day = 0;
        var isProduce = ($scope.goodsDetail.lotType == "MANUFACTURE");
        year = isProduce ?$("#produce_year").val():$("#maturity_year").val();
        day = isProduce ? $("#produce_day").val():$("#maturity_day").val();
        month = isProduce ? $("#produce_month").val():$("#maturity_month").val();
        $scope.useNotAfterTest = year + "-" + problemOutboundBaseService.pad(month) + "-" + problemOutboundBaseService.pad(day);
        var date = new Date($scope.useNotAfterTest);
        if($scope.lotUnit=="月")
            date.setMonth(date.getMonth() + (parseInt($("#obp_months").val())|| 0));
        if($scope.lotUnit=="年")
            date.setFullYear(date.getFullYear() + (parseInt($("#obp_months").val())|| 0));
        if($scope.lotUnit=="日")
            date.setDate(date.getDate() + (parseInt($("#obp_months").val())|| 0));
      if(year>1000&&month>0&&day>0) {
      $scope.useNotAfter = date.getFullYear() + "-" + problemOutboundBaseService.pad(date.getMonth() + 1) + "-" + problemOutboundBaseService.pad(date.getDate());
      $scope.commodityValidWindow.close(); // 有效期/到期日
      //
      if($scope.goodsDetail.serialRecordType === "ALWAYS_RECORD") $scope.openSerialWin();
      else $scope.scanGoods();
      }
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

    // 序列号
    $scope.openSerialWin = function(){
      $scope.errorWindow({
        id: "#goodsNumberId",
        name: $scope.goodsNumberWindow,
        open: function () {
          $scope.serialNumber = ""; // 初始为空
          setTimeout(function(){ $("#serialNumberId").focus();}, 600);
        }
      });
    };

    // 扫描序列号
    $scope.serialNumbers = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if(keycode != 13) return;
      $scope.goodsNumberWindow.close();
      $scope.scanGoods();
    };

    // 扫描商品
    $scope.scanGoods = function(){
      problemOutboundService.moveShelvesLicensePlate({
        "containerName": $scope.deleteOrder,
        "shipmentNo": $scope.customerShip,
        "itemNo": $scope.goodsDetail?$scope.deleteGoodsNo:"1",//条码扫描错误的默认值
        "serialNo": $scope.serialNumber || "",
        "useNotAfter": $scope.useNotAfter || "",
        "solveKey": "DELETE_ORDER_CUSTOMER"
      }, function(){
        $scope.serialNumber = ""; $scope.useNotAfter = "";
        refreshGoodsDetails(); focusDeleteOrderGrid();
      },function(data){
          if(data.key == "EX_CONTAINER_SKU_DIFFERENT_CLIENT"){
              $scope.lpNumber = "two";
              focusDeleteOrder();
          }
          else if(data.key == "EX_CONTAINER_SKU_DIFFERENT_LOT"){
              $scope.lpNumber = "three";
              focusDeleteOrder();
          }else {
              var win = $("#mushinyWindow").data("kendoWindow");
              commonService.dialogMushiny(win, {
                  width: 320, height: 160, type: "warn", open: function () {
                      setTimeout(function () {
                          $("#warnContent").html($translate.instant(data.key));
                      }, 200);
                      win.bind("close", function () {focusDeleteOrderGrid();});
                  }
              });
          }
      });
    };

    // 正常订单转问题订单进行问题处理确认
    $scope.dealWithProblemSure = function(){
     problemOutboundService.dealWithProblem({
        "obpStationId": $rootScope.obpStationId,
        "obpWallId": $rootScope.obpWallId,
        "shipmentNo": $rootScope.shipmentNo || ""
      },function(){
        $scope.dealWithProblemWindow.close();
        $rootScope.problemCellStatus = "bindCell";
        $state.go("main.problemOutboundWall");
      });
    };
    //包装
      $scope.gotoPacking=function(){
        $("#"+$scope.gridId+" button").each(function () {
          $(this).bind("click", function () {
             var shipmentNo = $(this).attr("shipmentno");
             var cellName = $(this).attr("cellname");
             problemOutboundService.gotoPacking(shipmentNo,$rootScope.obpStationId,cellName,(function(){
               $scope.closeProblemMethod();
             }))
            });
        });
     };

      //测试包装
      $scope.gotoPack=function(){
        $("#"+$scope.gridId+" button").each(function () {
            $(this).bind("click", function () {
                var shipmentId=$(this).attr("shipmentno");
                var cellname=$(this).attr("cellname");
                $rootScope.shipmentId=shipmentId;
                $rootScope.cellname=cellname;
                $("#errorDiv").css("display","none");
                $scope.errorWindow({
                    id: "#gotoPackId",
                    name: $scope.gotoPackWindow,
                    height: 250,
                    open: function(){
                        $scope.locationContainer="";
                        setTimeout(function(){ $("#packStorageLocationId").focus();}, 600);
                    }
                });
             });
         });
      };
     //测试扫描车牌后送去包装
      $scope.relieveObpCell=function(e){
        var keycode = window.event ? e.keyCode : e.which;
        if(keycode != 13) return;
          problemOutboundService.relieveQuestionCell({
              "shipmentNo": $rootScope.shipmentId,
              "obpStationId": $rootScope.obpStationId,
              "cellName": $rootScope.cellname,
              "locationContainer": $scope.locationContainer,
              "solveKey":"RELEASE_CELL"
          },function(){
              $scope.locationContainer="";
              $scope.gotoPackWindow.close();
              $scope.closeProblemMethod();
          },function(data){
              $("#errorDiv").css("display","block");
              $scope.locationContainer="";
              if(data.key=='扫描对象不存在,请重新扫描')
                  $scope.message='车牌不存在,请重新扫描';
              else if(data.key=='容器已被锁定')
                  $scope.message='车牌被锁定,请重新扫描';
              else if(data.key=='容器里存在商品')
                  $scope.message='车牌内存在商品,请重新扫描';
              else {
                  var win = $("#mushinyWindow").data("kendoWindow");
                  commonService.dialogMushiny(win, {
                      width: 320, height: 160, type: "warn", open: function () {
                          setTimeout(function () {$("#warnContent").html($translate.instant(data.key));}, 200);
                          win.bind("close", function () {
                              $scope.locationContainer="";
                              setTimeout(function(){ $("#packStorageLocationId").focus();}, 600);});
                      }
                  });
              }
          });
      };
      //打印订单
      $scope.printOrder=function(){
          $("#"+$scope.gridId+" input").each(function () {
             $(this).bind("click", function () {
               var shipmentNo = $(this).attr("shipmentno");
                 problemOutboundService.printOrder(shipmentNo,"",(function(data){
                    console.log(data);
                 }));
              });
          });
      };
      //全部送去包装
      $scope.batchToPacking=function(){
          var grid = $("#problemOutboundScanedGrid").data("kendoGrid"), datas = grid.dataSource.data();
          for(var i = 0, items = []; i < datas.length; i++){
              var json={};
              var data = datas[i];
              if(data.cell == undefined) continue;
              json={"shipmentNo":data.shipmentNo,"cell":data.cell};
              items.push(json);
          }
          var datatemp={shipmentDatas:JSON.stringify(items),obpStationId:$rootScope.obpStationId};
          problemOutboundService.batchToPacking({datas:JSON.stringify(datatemp)},(function(){
              $scope.closeProblemMethod();
          }));
      };
    // ==============================================================初始化====================================================================
    $scope.shipmentNo = ""; $rootScope.shipmentNo = "";
    setTimeout(function(){ $("#obp_shipment").focus();}, 0);
    $scope.closeProblemMethodTwo();
    // 待处理问题
    var columnsLeft = [
      {field: "shipmentNo", headerTemplate: "<span translate='Shipment ID'></span>" , template: function (item) {
        return item.shipmentNo? "<a href='javascript:void(0)' cell='"+ item.cell+ "' shipmentNo='"+ item.shipmentNo+ "'>" + item.shipmentNo + "</a>": "";
      }},
      {field: "cell", headerTemplate: "<span translate='问题货格'></span>"},
      {field: "problemType", headerTemplate: "<span translate='问题类型'></span>"},
      {field: "reportBy", headerTemplate: "<span translate='触发人员'></span>"},
      {field: "jobType", headerTemplate: "<span translate='触发环节'></span>"},
      {field: "reportDate", headerTemplate: "<span translate='触发问题时间'></span>", template: function(item){
        return item.reportDate? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.reportDate)): "";
      }},
      {field: "obpsCreateDate", headerTemplate: "<span translate='OBPS添加问题时间'></span>", template: function(item){
        return item.obpsCreateDate? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.obpsCreateDate)): "";
      }},
      {field: "exSD", headerTemplate: "<span translate='预计发货时间点'></span>", template: function(item) {
        return item.exSD ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.exSD)) : "";
      }},
      {field: "timeCondition", headerTemplate: "<span translate='距离发货时间'></span>"},
      {field: "", headerTemplate: "<span translate='打印'></span>",template:function(item){
          return "<input type='button' value='打印订单' style='width:90px;height: 30px;background-color: #3f51b5;color:#fff;font-weight:500;font-size:16px;' shipmentno='"+item.shipmentNo+"'/>";
      }}
    ];
    $scope.problemSolvingOrderNumberGridOptions= {height: $rootScope.mainHeight- 83,scrollable: true, columns: columnsLeft};

    // 已处理完成
    var columnsRight = [
      {field: "shipmentNo", headerTemplate: "<span translate='Shipment ID'></span>",template:function(item){
          return item.shipmentNo? "<a href='javascript:void(0)' cell='"+ item.cell+ "' shipmentNo='"+ item.shipmentNo+ "'>" + item.shipmentNo + "</a>": "";
      }},
      {field: "cell", headerTemplate: "<span translate='问题货格'></span>", template: function(item){
          return item.cell? item.cell: "";
      }},
      {field: "problemType", headerTemplate: "<span translate='问题类型'></span>"},
      {field: "reportBy", headerTemplate: "<span translate='触发人员'></span>"},
      {field: "jobType", headerTemplate: "<span translate='触发环节'></span>"},
      {field: "reportDate", headerTemplate: "<span translate='触发问题时间'></span>", template: function(item){
        return item.reportDate? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.reportDate)): "";
      }},
      {field: "obpsCreateDate", headerTemplate: "<span translate='OBPS添加问题时间'></span>", template: function(item){
        return item.obpsCreateDate? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.obpsCreateDate)): "";
      }},
      {field: "solveDate", headerTemplate: "<span translate='OBPS处理时间'></span>", template: function(item) {
          return item.solveDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.solveDate)) : "";
      }},
      {field: "", headerTemplate: "<span translate='打印'></span>",template: function(item) {
          return "<input type='button' value='打印订单' style='width:90px;height: 30px;background-color: #3f51b5;color:#fff;font-weight:500;font-size:16px;' shipmentno='"+item.shipmentNo+"'/>";
      }},
      // {field: "",headerTemplate: "<span translate='包装'></span>",template: function(item) {
      //     return item.cell? "<button style='width:90px;height: 30px;background-color: #00cc00;color:#fff;font-weight:500;font-size:16px;' shipmentno='"+item.shipmentNo+"' cellname='"+item.cell+"'>送去包装</button>" : "";
      // }}
      {field: "",headerTemplate: "<div translate='全部送至包装' style='background-color: #00cc00;font-size:17px;height:23px;' ng-click='batchToPacking()'></div>",template: function(item) {
          return item.cell? "<button style='width:90px;height: 30px;background-color: #00cc00;color:#fff;font-weight:500;font-size:16px;' shipmentno='"+item.shipmentNo+"' cellname='"+item.cell+"'>送去包装</button>" : "";
      }}
    ];
    $scope.completedProcessingGridOptions = {height: $rootScope.mainHeight- 83, scrollable: true, columns: columnsRight};
  });
})();