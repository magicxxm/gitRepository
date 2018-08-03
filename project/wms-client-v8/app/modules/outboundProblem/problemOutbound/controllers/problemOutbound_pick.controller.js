/**
 * Created by thoma.bian on 2017/5/10.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("problemOutboundPickCtl", function ($scope, $sce, $state, problemOutboundService,commonService,$translate) {
      // 错误弹窗
      $scope.errorWindow = function (options){
          $(options.id).parent().addClass("myWindow");
          var window = options.name;
          window.setOptions({width: options.width || 800, height: options.height || 250, visible: false, actions: ["close"]});
          window.center();
          options.open && window.bind("open", options.open);
          window.open();
      };
    $scope.checkGoodsBack = true;
    $scope.checkGoodsEnd = false;

    $scope.pickingShow = 'licensePlates';
    $scope.goodsContent = false;

    $scope.obpPick = 'allWallContent';

      function focusPickPlate(){
          $scope.pickingLicensePlate = "";
          setTimeout(function(){ $("#obp_pickPlate").focus();}, 300);
      }
      function focusPickItemNo(){
          $scope.checkGood = "";
          setTimeout(function(){ $("#obp_pickItemNo").focus();}, 300);
      }
      function focusSerialNo(){
          $scope.serialNumber="";
          setTimeout(function(){ $("#serialNumbersId").focus();}, 600);
      }
    // 放置有货 每行颜色
    problemOutboundService.problemCellPlaceGoods($scope.obpWallId, function(datas){
       focusPickPlate();
      //
      for(var i = 0, items = []; i < datas.numberOfRows; i++){
        items[i] = datas.obpCellPositions.slice(i*datas.numberOfColumns, (i+1)*datas.numberOfColumns);
      }
      //
      var colors = ["#DE291E","#FFA12D","#F4E800","#2FB135","#4892DD","#7A378B","#F18CD2","#512C1A","#014631","#9292CE","#C3C7C8","#7ADDD8"];
      for(var i = 0, htmls = []; i < datas.numberOfRows; i++){
        htmls.push("<tr>");
        for(var cells = items[i], j = cells.length -1 ; j >= 0; j--){
          var cell = cells[j];
          htmls.push("<td style='background:"+ (cell.state == "occupied"? colors[cell.yPos-1]: "#d9d9d9")+ ";cursor:pointer;height:45px;'>"+ cell.name.split("-")[1]+"</td>");
        }
        htmls.push("</tr>");
      }
      $scope.pickWallContent = $sce.trustAsHtml(htmls.join(""));
    });

   //请扫描拣货车牌
    $scope.pickingLicensePlates = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        problemOutboundService.scanPickingLicensePlate($scope.pickingLicensePlate,$scope.obpWallId,function(datas){
          focusPickItemNo();
          $scope.obpPick = 'pickCarContent';
          $scope.pickingShow = 'checkGoodsNumber';
          //
          for(var i = 0, items = []; i < datas.numberOfRows; i++){
            items[i] = datas.obpCellPositions.slice(i*datas.numberOfColumns, (i+1)*datas.numberOfColumns);
          }
          //
          var colors = ["#DE291E","#FFA12D","#F4E800","#2FB135","#4892DD","#7A378B","#F18CD2","#512C1A","#014631","#9292CE","#C3C7C8","#7ADDD8"];
          for(var i = 0, htmls = []; i < datas.numberOfRows; i++){
            htmls.push("<tr>");
            for(var cells = items[i], j = cells.length -1 ; j >= 0; j--){
              var cell = cells[j];
               htmls.push("<td style='background:"+ (cell.goodsInCell == true? colors[cell.yPos-1]: "#d9d9d9")+ ";cursor:pointer;height:45px'>"+ cell.name.split("-")[1]+ "<br>"+(cell.amountProblem>0?cell.amountScanedProblem+"/"+cell.amountProblem:"")+"</td>");
            }
            htmls.push("</tr>");
          }
          $scope.pickWallContent = $sce.trustAsHtml(htmls.join(""));
         },function(data){
            var win = $("#mushinyWindow").data("kendoWindow");
            commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                setTimeout(function(){$("#warnContent").html($translate.instant(data.key));}, 200);
                win.bind("close", function(){ focusPickPlate(); });
            }});
        });
      }
    };

    //请检查并扫描商品
    $scope.checkGoods = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $scope.obpPick='pickGoodsContent';
        problemOutboundService.checkScanGoods($scope.pickingLicensePlate,$scope.obpWallId,$scope.checkGood,function(datas) {
          if(datas.serialRecordType=="NO_RECORD") {
              var colors = ["#DE291E","#FFA12D","#F4E800","#2FB135","#4892DD","#7A378B","#F18CD2","#512C1A","#014631","#9292CE","#C3C7C8","#7ADDD8"];
              $("#mydiv").css("background-color", colors[datas.inCellyPos - 1]);
              $scope.goodsContent = true;
              $scope.pickingPicture = true;
              $scope.obpCellName = datas.inCellName;
              $scope.checkGood = "";
              for (var i = 0, items = []; i < datas.numberOfRows; i++) {
                  items[i] = datas.obpCellPositions.slice(i * datas.numberOfColumns, (i + 1) * datas.numberOfColumns);
              }
              for (var i = 0, htmls = []; i < datas.numberOfRows; i++) {
                  htmls.push("<tr>");
                  for (var cells = items[i], j = cells.length - 1; j >= 0; j--) {
                      var cell = cells[j];
                      var color = "";
                      if (cell.goodsInCell) {
                          if (cell.amountScanedProblem == cell.amountProblem) {
                              color = "#92d050";
                          } else {
                              color = colors[cell.yPos - 1];
                          }
                      } else {
                          color = "#d9d9d9";
                      }
                      htmls.push("<td style='background:" + color + ";cursor:pointer;height:45px'>" + cell.name.split("-")[1] + "<br>" + (cell.amountProblem > 0 ? cell.amountScanedProblem + "/" + cell.amountProblem : "") + "</td>");
                  }
                  htmls.push("</tr>");
              }
              // console.log(datas);
              if (datas.scaned == "scaned") {
                  $scope.checkGoodsBack = false;
                  $scope.checkGoodsEnd = true;
              }
              $scope.pickWallContent = $sce.trustAsHtml(htmls.join(""));
              focusPickItemNo();
          }else if(datas.serialRecordType=="ALWAYS_RECORD"){
              $scope.errorWindow({
                  id: "#scanGoodsSnWindowId",
                  name: $scope.scanGoodsSnWindow,
                  height: 260,
                  open: function () {
                      focusSerialNo();
                      $scope.itemDataName=datas.itemName;
                      $scope.itemDataNo=datas.itemNo;
                  }
              });
          }
         },function(data){
            var win = $("#mushinyWindow").data("kendoWindow");
            commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                setTimeout(function(){$("#warnContent").html($translate.instant(data.key));}, 200);
                win.bind("close", function(){ focusPickItemNo(); });
            }});
        })
      }
    };

      $scope.serialNumbers=function(e) {
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode != 13) return;
          problemOutboundService.scanHotPickSn($scope.pickingLicensePlate, $scope.obpWallId, $scope.itemDataNo, $scope.serialNumber, function (datas) {
              $scope.scanGoodsSnWindow.close();
              var colors = ["#DE291E","#FFA12D","#F4E800","#2FB135","#4892DD","#7A378B","#F18CD2","#512C1A","#014631","#9292CE","#C3C7C8","#7ADDD8"];
              $("#mydiv").css("background-color", colors[datas.inCellyPos - 1]);
              $scope.goodsContent = true;
              $scope.pickingPicture = true;
              $scope.obpCellName = datas.inCellName;
              $scope.checkGood = "";
              for (var i = 0, items = []; i < datas.numberOfRows; i++) {
                  items[i] = datas.obpCellPositions.slice(i * datas.numberOfColumns, (i + 1) * datas.numberOfColumns);
              }
              for (var i = 0, htmls = []; i < datas.numberOfRows; i++) {
                  htmls.push("<tr>");
                  for (var cells = items[i], j = cells.length - 1; j >= 0; j--) {
                      var cell = cells[j];
                      var color = "";
                      if (cell.goodsInCell) {
                          if (cell.amountScanedProblem == cell.amountProblem) {
                              color = "#92d050";
                          } else {
                              color = colors[cell.yPos - 1];
                          }
                      } else {
                          color = "#d9d9d9";
                      }
                      htmls.push("<td style='background:" + color + ";cursor:pointer;height:45px'>" + cell.name.split("-")[1] + "<br>" + (cell.amountProblem > 0 ? cell.amountScanedProblem + "/" + cell.amountProblem : "") + "</td>");
                  }
                  htmls.push("</tr>");
              }
              // console.log(datas);
              if (datas.scaned == "scaned") {
                  $scope.checkGoodsBack = false;
                  $scope.checkGoodsEnd = true;
              }
              $scope.pickWallContent = $sce.trustAsHtml(htmls.join(""));
              focusPickItemNo();
          }, function (data) {
              var win = $("#mushinyWindow").data("kendoWindow");
              commonService.dialogMushiny(win, {
                  width: 320, height: 160, type: "warn", open: function () {
                      setTimeout(function () {
                          $("#warnContent").html($translate.instant(data.key));
                      }, 200);
                      win.bind("close", function () {focusSerialNo();});
                  }
              });
          });
      };

    //返回
    $scope.returnBack = function(){
      $state.go("main.problemOutboundShipment");
    };

    //结束
    $scope.checkGoodsEnds = function(){
      $("#assignmentEndsId").parent().addClass("windowTitle");
      $scope.assignmentEndsWindow.setOptions({
        width:800,
        height: 200,
        visible: false,
        actions: false
      });
      $scope.assignmentEndsWindow.center();
      $scope.assignmentEndsWindow.open();
    };

    //结束确定
    $scope.assignmentEndsSure = function(){
      $scope.assignmentEndsWindow.close();
      $state.go("main.problemOutboundShipment");
    }

  }).controller("problemOutboundGoodsCtl", function ($scope, $rootScope, $state, $sce, problemOutboundService,outboundProblemVerifyService,PROBLEM_OUTBOUND,commonService,$translate,BACKEND_CONFIG) {
      // 错误弹窗
      $scope.errorWindow = function (options){
          $(options.id).parent().addClass("myWindow");
          var window = options.name;
          window.setOptions({width: options.width || 800, height: options.height || 250, visible: false, actions: ["close"]});
          window.center();
          options.open && window.bind("open", options.open);
          window.open();
      };
    $scope.scanItemNo = false;
    $scope.showItemName=false;
    $scope.obpCallPod = true;

     function focusLocation(){
         $scope.obpLocation = "";
         setTimeout(function(){ $("#obp_location").focus();}, 300);
     }
    function focusItemNo(){
       $scope.pickingGoods ="";
       setTimeout(function(){ $("#obp_itemNo").focus();}, 300);
    }
      function focusSerialNo(){
          $scope.serialNumber="";
          setTimeout(function(){ $("#serialNumbersId").focus();}, 600);
      }

      // 放置有货 每行颜色,初始化拣货货位
    problemOutboundService.problemCellStorageLocation($rootScope.obpWallId,"","", function(datas){
        $scope.podContent=true;
        $scope.stopcallPOD="停止分配POD";
        $("#releasePodId").attr("disabled",true);
        $("#stopAllocationPod").attr("disabled",true);
        var htmls = [];
        htmls.push("<tr><td style='background:#d9d9d9;width:5%;height:30px;cursor:pointer;'>"+"选择"+ "</td>");
        htmls.push("<td style='background:#d9d9d9;width:8%;height:30px;cursor:pointer;'>"+"问题格"+ "</td>");
        for(var x=0;x<5;x++){
            htmls.push("<td style='background:#d9d9d9;width:15%;height:30px;cursor:pointer;'>"+"货位"+ "</td>");
        }
        htmls.push("</tr>");
        console.log(datas);
        for(var i = 0; i < datas.length; i++){
            var data = datas[i];
            if(data.callPod){
                htmls.push("<tr><td style='background:#d9d9d9;'><input type=\"checkbox\" cellName='"+data.cellName+"' style='zoom:180%' ng-model='chk'/></td>");
            }else{
                htmls.push("<tr><td style='background:#d9d9d9;width:5%;height:30px;cursor:pointer;'></td>");
            }
            htmls.push("<td style='background:#d9d9d9;width:8%;height:30px;cursor:pointer;'>" + data.cellName + "</td>");
           for(var storageLocations = datas[i].storageLocationPositions, j = 0 ; j <= 4; j++){
               var a=storageLocations[j]==undefined?"":storageLocations[j].storageLocationName;
               var amountScanedProblem=storageLocations[j]==undefined?"":storageLocations[j].amountScanedProblem;
               var amountProblem=storageLocations[j]==undefined?"":storageLocations[j].amountProblem;
               htmls.push("<td style='background:#d9d9d9;width:15%;cursor:pointer;'>"+a+"<br>"+(amountProblem>0?(amountScanedProblem+"/"+amountProblem):"")+"</td>");
            }
              htmls.push("</tr>");
        }
        $scope.goodsWallContent = $sce.trustAsHtml(htmls.join(""));
    });

    //返回
    $scope.returnBack = function(){
      $state.go("main.problemOutboundShipment");
    };

      $scope.callPod=function() {
          var cellNames=[];
         $("#obp_goodsWallContent input").each(function(){
             if($(this).is(':checked')){
                var cell=$(this).attr("cellName");
                cell=$rootScope.obpWallName+"-"+cell;
                cellNames.push(cell);
             }
         });
          problemOutboundService.getPodPosition(cellNames,$rootScope.workStationId,function(data){
              console.log("呼叫pod信息:",data);
              var callData = {
                  "pods": data,
                  "workStationId": $rootScope.obpStationId
              };
              outboundProblemVerifyService.callPodInterface({callData:JSON.stringify(callData)},function(){
                  getReleaseStart();
                  getPodResult();
                  $scope.scan_finish=false;
                  $scope.podWillComing=true;
              });
              // getReleaseStart();
              // $scope.obpCallPod=false;
              // $scope.scanLocation=true;
              // $scope.podComing=true;
              // $scope.podName="P0000015";
              // $rootScope.podNo="P0000015";
              // getComingPod($rootScope.obpWallId,"P0000015","");
          });
      };
          //呼叫pod后改变按钮
          function getReleaseStart(){
              $("#releasePodId").removeAttr("disabled");
              $("#stopAllocationPod").removeAttr("disabled");

              $("#releasePodId").removeClass("buttonColorGray");
              $("#releasePodId").css({"backgroundColor": "#3f51b5"});
              $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
          }

          // 停止分配pod
          $scope.stopAllocationPodGrid = function () {
              if ($scope.stopcallPOD == "停止分配POD") {
                  outboundProblemVerifyService.stopCallPod("stop", $rootScope.obpStationId, function (data) {
                      $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
                      $scope.stopcallPOD = "恢复分配POD";
                  })
              } else {
                  outboundProblemVerifyService.stopCallPod("start", $rootScope.obpStationId, function (data) {
                      $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                      $scope.stopcallPOD = "停止分配POD";
                  })
              }
          };

          var podSocket;//webSocket
          function getPodResult() {
            //  var url = PROBLEM_OUTBOUND.podWebSocket+$rootScope.workStationId;
              var url = BACKEND_CONFIG.websocket+"websocket/getPod/"+$rootScope.workStationId;
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
                      if (data.workstation == $rootScope.workStationId) {
                          console.log("推送pod的信息：",data);
                          // $scope.podNo = data.pod;
                          $rootScope.podNo = data.pod;
                          $scope.podName=data.pod;
                          getComingPod($rootScope.obpWallId,$rootScope.podNo,"");
                      }
                  }
              };
              //关闭事件
              podSocket.onclose = function () {
                  console.log("podSocket 关闭");
                  if(podSocket.readyState != 1){
                      podSocket = new WebSocket(url);
                      if(podSocket.readyState != 1){
                          $scope.errorWindow("hardwareId1",$scope.hardwareWindows1);
                      }
                  }
              };
              //发生了错误事件
              podSocket.onerror = function () {
                  console.log("podSocket 发生了错误");
                  podSocket = new WebSocket(url);
              }
          }

          //刷新pod信息
          $scope.refreshPod = function () {
             problemOutboundService.refreshNextPod($rootScope.sectionId,$rootScope.workStationId,function (data) {
                 console.log("刷新的pod信息：",data);
                 callNewPod(data);
                 if(data.pod!=null&&data.pod!=""&&data.pod!=undefined)
                     getReleaseStart();
              },function(){
                   console.log("刷新pod信息error!");
             });
          };

          // 释放pod
          $scope.releasePodGrid = function () {
              if($rootScope.podNo == null || $rootScope.podNo == "" || $rootScope.podNo == undefined) {
                  problemOutboundService.refreshNextPod($rootScope.sectionId, $rootScope.workStationId, function (data) {
                      console.log("刷新的pod信息：", data);
                      if (data.pod != null && data.pod != "" && data.pod != undefined) {
                          $rootScope.podNo = data.pod;
                      }
                  });
              }
              outboundProblemVerifyService.reservePod($rootScope.podNo,$rootScope.sectionId,"false",$rootScope.workStationId,$rootScope.obpStationId,function (data) {
                  console.log("pod释放成功");
                  $rootScope.podNo="";
                  $scope.podComing=false;
                  $scope.scan_finish=false;
                  $scope.podWillComing=true;
                  console.log("释放POD后推送的POD信息:"+data);
                  callNewPod(data);
                  //$scope.refreshPod();
              });
          };

          function callNewPod(data){
              if(data.pod!=null&&data.pod!=""&&data.pod!=undefined) {
                  $rootScope.podNo = data.pod;
                  $scope.obpCallPod=false;
                  $scope.scan_finish=false;
                  $scope.podComing=true;
                  $scope.podName=$rootScope.podNo;
                  getComingPod($rootScope.obpWallId,$rootScope.podNo,"");
              }else{
                  console.log("没有获取到推送pod信息!");
              }
          }

          function getComingPod(wallId,podNo,location){
              problemOutboundService.problemCellStorageLocation(wallId,podNo,location,function(datas){
                  console.log("获取到datas:",datas);
                  if(location!=""&&location!=null){
                      if(datas[0].nextPod){
                          $scope.scanItemNo=false;
                          $scope.showItemName=false;
                          $scope.podComing=false;
                          $scope.scan_finish=true;
                      }else {
                          $scope.itemName = datas[0].itemName;
                          if($scope.itemName==""||$scope.itemName==null){
                              $scope.scanItemNo=false;
                              $scope.showItemName=false;
                              $scope.scanLocation=true;
                              focusLocation();
                          }else {
                              $scope.pickingGoods = "";
                              $scope.scanLocation = false;
                              $scope.scanItemNo = true;
                              $scope.showItemName = true;
                              focusItemNo();
                          }
                      }
                  }else{
                      $scope.obpCallPod=false;
                      $scope.scan_finish=false;
                      $scope.podWillComing=false;
                      $scope.scanLocation=true;
                      $scope.podComing=true;
                      focusLocation();
                  }
                  var colors = ["#DE291E","#FFA12D","#F4E800","#2FB135","#4892DD","#7A378B","#F18CD2","#512C1A","#014631","#9292CE","#C3C7C8","#7ADDD8"];
                  var cols=["#33ffff","#d9d9d9","#92d050","#FFA500"];
                  //商品在货位里的颜色蓝色[0] 初始为灰色[1]，扫描完成是绿色[2],正在处理的货位是橙色[3]
                  var htmls = [];
                  htmls.push("<tr><td style='background:#d9d9d9;width:5%;height:30px;cursor:pointer;'>"+"选择"+ "</td>");
                  htmls.push("<td style='background:#d9d9d9;width:8%;height:30px;cursor:pointer;'>"+"问题格"+ "</td>");
                  for(var x=0;x<5;x++){
                      htmls.push("<td style='background:#d9d9d9;width:15%;height:30px;cursor:pointer;'>"+"货位"+ "</td>");
                  }
                  htmls.push("</tr>");
                  for(var i = 0; i < datas.length; i++){
                      var data = datas[i];
                      if(data.callPod){
                          htmls.push("<tr><td style='background:#d9d9d9;'><input type=\"checkbox\" cellName='"+data.cellName+"' style='zoom:180%' ng-model='chk'/></td>");
                      }else{
                          htmls.push("<tr><td style='background:#d9d9d9;width:5%;height:30px;cursor:pointer;'></td>");
                      }
                      if(data.thisCell){
                          htmls.push("<td style='background:"+colors[data.yPos - 1]+";width:8%;cursor:pointer;font-weight:500;color:white;'>" + data.cellName + "</td>");
                      }else{
                          htmls.push("<td style='background:"+cols[1]+";width:8%;cursor:pointer;'>" + data.cellName + "</td>");
                      }
                      for(var storageLocations = datas[i].storageLocationPositions, j = 0 ; j <5; j++){
                          var a=storageLocations[j]==undefined?"":storageLocations[j].storageLocationName;
                          var amountScanedProblem=storageLocations[j]==undefined?"":storageLocations[j].amountScanedProblem;
                          var amountProblem=storageLocations[j]==undefined?"":storageLocations[j].amountProblem;
                          var color=cols[1];
                          if(storageLocations[j]!=undefined&&storageLocations[j].goodsInStorageLocation){//在货位里
                              color=cols[0];
                              if(location==a) color=cols[3];
                              // if(amountProblem==amountScanedProblem) color=cols[2];
                          }
                          if(amountProblem>0&&amountProblem==amountScanedProblem){color=cols[2];}
                          htmls.push("<td style='background:"+color+ ";width:15%;cursor:pointer;' color='"+color+"'>"+a+"<br>"+(amountProblem>0?(amountScanedProblem+"/"+amountProblem):"")+"</td>");
                      }
                      htmls.push("</tr>");
                  }
                  $scope.goodsWallContent = $sce.trustAsHtml(htmls.join(""));
              },function(data){
                  var win = $("#mushinyWindow").data("kendoWindow");
                  commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                      setTimeout(function(){
                          $("#warnContent").html($translate.instant(data.key));
                      }, 200);
                      win.bind("close", function(){ focusLocation(); });
                  }});
              });
          }
          $scope.scanObpLocation=function(e){
              var keycode = window.event ? e.keyCode : e.which;
              if(keycode != 13) return;
                  $scope.location = $scope.obpLocation;
                  getComingPod($rootScope.obpWallId, $rootScope.podNo, $scope.obpLocation);
                 // $scope.scanLocation = false;
                 // $scope.scanItemNo = true;
                 // $scope.showItemName = true;
          };
          $scope.scanPickGoods=function(e){
              var keycode = window.event ? e.keyCode : e.which;
              if(keycode != 13) return;
              problemOutboundService.scanPickGoods($scope.location,$scope.pickingGoods,function(data){
                  if(data.serialRecordType=="NO_RECORD"){
                      getComingPod($rootScope.obpWallId,$rootScope.podNo,$scope.location);
                  }else if(data.serialRecordType=="ALWAYS_RECORD"){
                      $scope.errorWindow({
                          id: "#scanGoodsSnWindowId",
                          name: $scope.scanGoodsSnWindow,
                          height: 260,
                          open:function(){
                              focusSerialNo();
                              $scope.itemDataName=data.itemName;
                              $scope.itemDataNo=data.itemNo;
                          }
                      });
                  }
              },function(data){
                  var win = $("#mushinyWindow").data("kendoWindow");
                  commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                      setTimeout(function(){
                          $("#warnContent").html($translate.instant(data.key));
                      }, 200);
                      win.bind("close", function(){ focusItemNo(); });
                  }});
              });
          };

      $scope.serialNumbers=function(e){
          var keycode = window.event ? e.keyCode : e.which;
          if(keycode != 13) return;
          problemOutboundService.scanPickgGoodsSn($scope.serialNumber,$scope.location,$scope.itemDataNo,function(){
              $scope.scanGoodsSnWindow.close();
              getComingPod($rootScope.obpWallId,$rootScope.podNo,$scope.location);
          },function(data){
              var win = $("#mushinyWindow").data("kendoWindow");
              commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                  setTimeout(function(){
                      $("#warnContent").html($translate.instant(data.key));
                  }, 200);
                  win.bind("close", function(){ focusSerialNo(); });
              }});
          });
      }
  })
})();