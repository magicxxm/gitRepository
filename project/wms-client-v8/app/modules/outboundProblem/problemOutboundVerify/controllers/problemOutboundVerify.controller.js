/**
 * Created by thoma.bian on 2017/5/10.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("problemOutboundVerifyCtl", function ($scope,$rootScope,$timeout,PROBLEM_OUTBOUND,$window,outboundProblemVerifyService,problemOutboundBaseService,BACKEND_CONFIG) {

    $scope.arr = "";
      $scope.selectedSign = "";
      if ($rootScope.podNo != ""){
          if ($scope.selectedSign == "all"){
              $scope.selectAll();
          }else if ($scope.selectedSign == "one"){
              if ($rootScope.id != ""){
                  selectOutboundProblemRecord($rootScope.id);
              }

          }
      }

      setTimeout(function(){ $("#obp_station").focus();}, 0);
    if($rootScope.page){
      $scope.outboundProblemVerify = "Home";
    }else{
      $scope.outboundProblemVerify = "workStationPage";
    }

      $("#releasePodId").attr("disabled",true);
      $("#stopAllocationPod").attr("disabled",true);

      $rootScope.stopcallPOD = "停止分配POD";
      $timeout(getBackState,0);
      // 绑定工作站
      $scope.workStation = function (e) {
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode != 13) return;
          outboundProblemVerifyService.getOutboundProblemStation($scope.workstation, function(data){
            console.log(data);
              $rootScope.workStationId = data.id; //逻辑工作站Id
              $rootScope.workstationValue = data.name;
              $rootScope.workStationIds = data.workStation.id; //物理工作站Id
              $rootScope.sectionId = data.workStation.sectionId;
              $scope.outboundProblemVerify = 'Home';
              $scope.workingStation = false;
              $scope.page = 'main';
          }, function(data){
              if(data.key == "OBP_WORKSTATION_SOMEONE"){
                  $scope.errorMessage = "已被占用";
              }else if(data.key == "EX_SERVER_ERROR"){
                  $scope.errorMessage = "不是一个有效工作站";
              }else if (data.key == "OBP_DEAL_WORKSTATION"){
                  $scope.errorMessage = "不是OUTBOUND问题核实工作站";
              }


              $scope.workingStation = true;
          });
      };

      //解绑工作站
      $scope.exitOnboundProblem = function(){
          outboundProblemVerifyService.yesOrNoFinsh($rootScope.workstationValue, function(data){
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
              }else {
                  $rootScope.page=false;
                  $scope.outboundProblemVerify = "";
                  outboundProblemVerifyService.exitOnboundProblemStation($rootScope.workstationValue, function(data){
                      // $state.go("main.inbound_problem_disposal");
                      $scope.outboundProblemVerify = "workStationPage";
                      $("#releasePodId").attr("disabled",true);
                      $("#stopAllocationPod").attr("disabled",true);
                      $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
                      $("#releasePodId").css({"backgroundColor": "#6E6E6E"});
                      $rootScope.select_one = false;
                      $rootScope.select_all = false;

                      $rootScope.workStationId = "";
                      $rootScope.workstationValue = "";
                      $rootScope.workStationIds = "";
                      $rootScope.sectionId = "";
                      $rootScope.state = false;
                      $rootScope.edit = false;
                      $scope.podSocket.close();

                  })
              }
          })

      };

    // 扫描工作站
    // $scope.workStation = function (e) {
    //   var keycode = window.event ? e.keyCode : e.which;
    //   if (keycode != 13) return;
    //   outboundProblemVerifyService.getOutboundProblemStation($scope.workstation, function(data){
    //     $rootScope.obpStationId = data.obpStation.id;
    //     $rootScope.workstationValue = data.obpStation.name;
    //     $scope.outboundProblemVerify = 'Home';
    //     setTimeout(function(){ $("#obp_wall").focus();}, 0);
    //   }, function(){
    //     $scope.workingStation = true;
    //   });
    // };
    //   //解绑工作站
    //   $scope.exitOnboundProblem = function(){
    //       $rootScope.page=false;
    //       $scope.page = "";
    //       outboundProblemVerifyService.exitOnboundProblemStation($rootScope.obpStationId, function(data){
    //           // $state.go("main.inbound_problem_disposal");
    //           $scope.page = "workStationPage";
    //       })
    //   };

    //outboundproblem核实公共方法
     function problemOutboundVerifyGrid(value,gridId){
         debugger;
      outboundProblemVerifyService.getOutboundProblem(value, function (data) {
        if(value.state == "process") {
            $scope.adjustment = true;
            if(data.length<=0){  $scope.adjustment = false;};
           if (data.length > 1 && data[0].itemNo == data[1].itemNo && (data[0].amount - data[0].solveAmount) == (data[1].amount - data[1].solveAmount) && data[0].itemData.clientId == data[1].itemData.clientId && data[0].problemType != data[1].problemType) {
               if (data[0].problemType == "MORE"){
                   $scope.caseSourceSave = data[0].problemStoragelocation;
                   $scope.caseDestinationSave = data[1].problemStoragelocation;
               }else {
                   $scope.caseSourceSave = data[1].problemStoragelocation;
                   $scope.caseDestinationSave = data[0].problemStoragelocation;
               }
               $scope.caseAmountSave = data[0].amount - data[0].solveAmount;
               $scope.clientSave = data[0].itemData.clientId;
             $scope.andCase1 = 1;
             $("#problemOutboundVerifyRightGrid").data("kendoGrid").setOptions({height: ($(document.body).height() - 280) / 2});
             $scope.rowData = data[0];
             for (var i = 0; i < data.length; i++) {
               if (i == 0) {
                 $scope.arr = data[i].id;
               } else {
                 $scope.arr += "," + data[i].id;
               }
             }
           }
         }
          if ($scope.itemNoRight == undefined || $scope.itemNoRight == ""){ $scope.adjustment = false; }
          var grid = $("#"+gridId).data("kendoGrid");
         grid.setDataSource(new kendo.data.DataSource({data: data}));
       })
     }

    //左边Grid
    var columnsLeft = [{field: "problemType", width: "40px", headerTemplate: "<span translate='问题类型'></span>",
      template: function (item) {
        var value = "";
        if (item.problemType == "MORE") {
          value = "<span>多货</span>";
        } else {
          value = "<span>少货</span>";
        }
        return "<a ui-sref='main.problemOutboundVerifyRead({id:dataItem.id})'>" + value + "</a>";
      }
    },
      {field: "itemData", width: "90px", headerTemplate: "<span translate='SKU'></span>",  template: function (item) {
          return item.itemData ? "<div style='word-wrap:break-word'>"+item.itemData.itemNo+"</div>" : "<div style='word-wrap:break-word'>"+item.itemNo+"</div>";
      }},
        {field: "problemStoragelocation", width: "90px", headerTemplate: "<span translate='容器'></span>" ,template: function (item) {
            return item.problemStoragelocation ?  "<div style='word-wrap:break-word'>"+item.problemStoragelocation+"</div>" : "";
        }},
      {field: "amount", width: "50px", headerTemplate: "<span translate='数量'></span>",
        template: function (item) {
          return item.amount - item.solveAmount;
        }
      },
      {field: "reportBy", width: "60px", headerTemplate: "<span translate='操作人'></span>",template: function (item) {
          return item.reportBy ? "<div style='word-wrap:break-word'>"+item.reportBy+"</div>" : ""}},
      {field: "reportDate", width: 105, headerTemplate: "<span translate='员工操作时间'></span>",
        template: function (item) {
          return item.reportDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.reportDate)) : "";
        }
      },
     {field: "jobType",width: "70px", headerTemplate: "<span translate='操作环节'></span>"},
     {field: "clientId",width: "60px", headerTemplate: "<span translate='客户'></span>",template: function (item) {
          return item.clientId ? "<div style='word-wrap:break-word'>"+item.clientId+"</div>" : ""}},
      {field: "solvedBy", width: "90px", headerTemplate: "<span translate='问题人员'></span>",template: function (item) {
          return item.solvedBy ? "<div style='word-wrap:break-word'>"+item.solvedBy+"</div>" : "";
      }}];

    problemOutboundVerifyGrid({"state":"unsolved","userName":"","seek":$scope.itemNoLeft},"problemOutboundVerifyLeftGird");
    $scope.problemOutboundVerifyLeftGridOptions = problemOutboundBaseService.grid("",columnsLeft, $(document.body).height() - 210);

    //左边Grid INPUT 回车
    $scope.searchInputLeft = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        problemOutboundVerifyGrid({"state":"unsolved","userName":"","seek":$scope.itemNoLeft},"problemOutboundVerifyLeftGird");
      }
    };
      $scope.sourceContainerKeyDown = function(e){
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode == 13) {
              setTimeout(function () {$("#destinationContainerId").focus();
              },0  );
          }
      };
      $scope.destinationContainerKeyDown = function(e){
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode == 13) {
              setTimeout(function () {$("#numId").focus();
              },0  );
          }
      };

    //左边Grid INPUT 搜索
    $scope.searchGridLeft = function () {
      problemOutboundVerifyGrid({"state":"unsolved","userName":"","seek":$scope.itemNoLeft},"problemOutboundVerifyLeftGird");
    };
      $scope.chk = false;
      $scope.selectOne = function (val, uid) {
          $scope.selectedSign = "one";
          var grid = $('#problemOutboundVerifyRightGrid').data('kendoGrid');
          if (val) {
              // grid.select("tr[data-uid='" + uid + "']");
              grid.tbody.children('tr[data-uid="' + uid + '"]').addClass('k-state-selected');
              var rowData = grid.dataItem(grid.select());
              if (grid.select().length == 1){
                  $rootScope.id = rowData.id;
                  selectOutboundProblemRecord(rowData.id);
                  console.log("选中行的ID:"+rowData.id);
              }else if (grid.select().length > 1){
                  $scope.selectAll();
              }
          } else {
              grid.tbody.children('tr[data-uid="' + uid + '"]').removeClass('k-state-selected');
              $rootScope.id = "";
          }
      };
    //右边Grid
    var columnsRight = [{width: 35, template: "<input type=\"checkbox\"  ng-model='chk' id='dataItem.id' class='check-box' ng-checked = 'select_one' ng-click='selectOne(chk,dataItem.uid)'/>"},
        {field: "problemType", width: "40px", headerTemplate: "<span translate='问题类型'></span>",
      template: function (item) {
        var value = "";
        if (item.problemType == "MORE") {
          value = "<span>多货</span>"
        } else {
          value = "<span>少货</span>"
        }
        return "<a ui-sref='main.problemOutboundVerifyRead({id:dataItem.id})'>" + value + "</a>";
      }
    },
        {field: "itemData", width: "90px", headerTemplate: "<span translate='SKU'></span>",  template: function (item) {
            return item.itemData ? "<div style='word-wrap:break-word'>"+item.itemData.itemNo+"</div>" : "<div style='word-wrap:break-word'>"+item.itemNo+"</div>";
        }},
        {field: "problemStoragelocation", width: "90px", headerTemplate: "<span translate='容器'></span>" ,template: function (item) {
            return item.problemStoragelocation ? "<div style='word-wrap:break-word'>"+item.problemStoragelocation +"</div>" : "";
        }},
      {field: "amount", width: "50px", headerTemplate: "<span translate='数量'></span>",
        template: function (item) {
          return item.amount - item.solveAmount;
        }
      },
      {field: "reportBy", width: "60px", headerTemplate: "<span translate='操作人'></span>",
          template: function (item) {
              return item.reportBy ? "<div style='word-wrap:break-word'>"+item.reportBy+"</div>" : ""}},
      {field: "reportDate", width: 105, headerTemplate: "<span translate='员工操作时间'></span>",
        template: function (item) {
          return item.reportDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.reportDate)) : "";
        }
      },
      {field: "jobType",width: "70px",headerTemplate: "<span translate='操作环节'></span>"},
        {field: "clientId",width: "60px", headerTemplate: "<span translate='客户'></span>",template: function (item) {
            return item.clientId ? "<div style='word-wrap:break-word'>"+item.clientId+"</div>" : ""}},
        {field: "solvedBy", width: "90px", headerTemplate: "<span translate='问题人员'></span>",template: function (item) {
          return item.solvedBy ? "<div style='word-wrap:break-word'>"+item.solvedBy+"</div>" : "";
      }}];

    $scope.problemOutboundVerifyRightGridOptions = problemOutboundBaseService.grid("",columnsRight, $(document.body).height() - 210);
    problemOutboundVerifyGrid({"state":"process","userName":$window.localStorage["username"],"seek":$scope.itemNoRight},"problemOutboundVerifyRightGrid");

    //右边Grid INPUT 回车
    $scope.searchInputRight = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        problemOutboundVerifyGrid({"state":"process","userName":$window.localStorage["username"],"seek":$scope.itemNoRight},"problemOutboundVerifyRightGrid");
      }
    };

    //右边Grid INPUT 搜索
    $scope.searchGridRight = function () {
      problemOutboundVerifyGrid({"state":"process","userName":$window.localStorage["username"],"seek":$scope.itemNoRight},"problemOutboundVerifyRightGrid");
    };


    //左右移动
    $scope.moveProblemOutboundVerify = function (value) {
      var state = "", grid = "", username = "";
      if (value == "left") {
        state = "process";
        grid = $("#problemOutboundVerifyLeftGird").data("kendoGrid");
        username = $window.localStorage["username"];
          $scope.adjustment = false;
      } else {
        state = "unsolved";
        grid = $("#problemOutboundVerifyRightGrid").data("kendoGrid");
        username = $window.localStorage["username"];
          $scope.adjustment = false;
      }
      var rows = grid.select();
      if (rows.length) {
        var rowData = grid.dataItem(rows[0]);
        var dataFiled = {};
          dataFiled = {
            "id": rowData.id,
            "problemType": rowData.problemType,
            "amount": rowData.amount,
            "solveAmount": rowData.solveAmount,
            "jobType": rowData.jobType,
            "state": state,
            "container":rowData.container,
            "problemStoragelocation": rowData.problemStoragelocation,
            "reportBy":rowData.reportBy,
            "itemNo": rowData.itemNo,
            "createdDate":rowData.createdDate,
            "solvedBy": username,
            "client":rowData.client,
            "clientId":rowData.clientId,
            "description":rowData.description,
            "reportDate":rowData.reportDate
        }
        outboundProblemVerifyService.updateOutboundProblemVerify(dataFiled, function () {
          $scope.searchGridLeft();
          $scope.searchGridRight();
        });
      }
    };
    var andCaseColumns = [{field: "problemStoragelocation",  headerTemplate: "<span translate='容器'></span>"},
      {field: "amount", width:80, headerTemplate: "<span translate='数量'></span>"},
        {field: "batchId", headerTemplate: "<span translate='Batch ID'></span>"},
      {field: "stationName", headerTemplate: "<span translate='站台'></span>"},
      {field: "rebinUser", headerTemplate: "<span translate='操作人'></span>"},
      {field: "customer", headerTemplate: "<span translate='客户'></span>"},
      {field: "rebinDate", headerTemplate: "<span translate='时间'></span>",
          template: function (item) {
              return item.rebinDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.rebinDate)) : "";
          }
      }];

    $scope.andCaseChild1 = function () {
      $scope.andCaseChildPage = 'CaseGrid';
        $scope.caseSource = "";
        $scope.caseDestination = "";
        $scope.caseAmount = "";
        $scope.client = "";
      outboundProblemVerifyService.getAnalysis($scope.arr, function (data) {
        $scope.andCaseGridOptions = problemOutboundBaseService.grid(data, andCaseColumns,180);
      })
    };

    $scope.andCaseChild2 = function () {

      $scope.andCaseChildPage = 'CaseContent';
        $scope.caseSource = $scope.caseSourceSave;
        $scope.caseDestination = $scope.caseDestinationSave;
        $scope.caseAmount = $scope.caseAmountSave;
        $scope.client = $scope.clientSave;
        console.log($scope.client);
        var combobox = $("#comboClientId").data("kendoComboBox");
        combobox.text($scope.client);
        // combobox.trigger("change");
        setTimeout(function () {$("#sourceContainerId").focus();
        },0  );

    };

    $scope.addCaseButtonSure = function(){
      outboundProblemVerifyService.getDestinationId ($scope.caseSource,function(sourceData) {
        outboundProblemVerifyService.getDestinationId($scope.caseDestination, function (data) {
          outboundProblemVerifyService.moveGoods({
            "sourceId": sourceData.id,
            "destinationId": data.id,
            "itemDataId": $scope.rowData.itemData.id,
            "amount": $scope.caseAmount
          }, function (data) {
             outboundProblemVerifyService.updateOutboundProblemList($scope.arr,"Adjustment",function(){
              $("#problemOutboundVerifyRightGrid").data("kendoGrid").setOptions({height: $(document.body).height() - 210});
              $scope.searchGridRight();
              $scope.andCaseInformation = true;
              $scope.adjustment = false;
             $scope.caseSource = "";
             $scope.caseDestination = "";
             $scope.caseAmount = "";
             $scope.client = "";
           });
          })
        })
      })
    };

    $scope.emptyCase = function(){
      $scope.caseSource = "";
      $scope.client = "";
      $scope.caseDestination= "";
      $scope.source= "";
      $scope.caseAmount= "";
    };

    $scope.continues = function(){
      $scope.searchGridRight();
      $scope.andCase1 = "";
      $scope.andCaseChildPage = "";
      $scope.andCaseInformation = false;
      $scope.adjustment = false;
      $("#problemOutboundVerifyRightGrid").height($(document.body).height()-210);
    };
      // 查询全部
      $scope.selectAll = function () {
          $scope.selectedSign = "all";
          var grid = $('#problemOutboundVerifyRightGrid').data('kendoGrid');
          if ($scope.select_all) {
              $rootScope.select_one = true;
              $rootScope.select_all = true;
              grid.tbody.children('tr').addClass('k-state-selected');

              var rows = grid.select();
              // var dataFiled = "";
              for (var i = 0; i < rows.length; i++) {
                  var rowData = grid.dataItem(rows[i]);
                  console.log("选中行的id--->"+rowData.id);
                  selectOutboundProblemRecord(rowData.id);

              }
          } else {
              $rootScope.select_one = false;
              $rootScope.select_all = false;
              grid.tbody.children('tr').removeClass('k-state-selected');
          }
      };
      // 变色
      function selectOutboundProblemRecord(id) {
          outboundProblemVerifyService.outboundProblemRecord(id,$scope.jobType, function (data) {
              var grid = $('#problemOutboundVerifyRightGrid').data('kendoGrid');
              console.log("货位",data);
              // var data1 = grid.getRowData();
              for (var i = 0; i < data.length; i++) {
                  if (data[i].unexamined == "H") {
                      console.log("$rootScope.podNo:"+$rootScope.podNo);
                      if ($rootScope.podNo != "" && data[i].name.startsWith($rootScope.podNo)){

                          var rows = grid.tbody.find("tr");
                          rows.each(function (i, row) {
                              var srcData = grid.dataItem(row);
                              if (srcData.id == id){
                                  $(row).css("background-color", "#ffc000");
                                  return;
                              }
                          });
                      }
                  }
              }

          });
      }
      // 定时器 返回时调用
      function getBackState() {
          // 只有返回才进
          if($rootScope.stopPod1){
              //返回后看是否可编辑
              if ($rootScope.edit){

                  $("#releasePodId").removeAttr("disabled");
                  $("#stopAllocationPod").removeAttr("disabled");

                  $("#releasePodId").removeClass("buttonColorGray");
                  $("#releasePodId").css({"backgroundColor": "#5c6bc0"});
                  $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                  outboundProblemVerifyService.workStationPodState($rootScope.workStationIds,function (data) {
                      console.log(data);
                      debugger;
                      if (data){
                          $rootScope.stopcallPOD = "停止分配POD";
                          $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                      }else {
                          $rootScope.stopcallPOD = "恢复分配POD";
                          $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
                      }
                  });
              }else {
                  $("#releasePodId").attr("disabled",true);
                  $("#stopAllocationPod").attr("disabled",true);
              }
          }
      }

      // 呼叫pod后初始化相关按钮
      function getReleaseStart() {

          $("#releasePodId").removeAttr("disabled");
          $("#stopAllocationPod").removeAttr("disabled");

          $("#releasePodId").removeClass("buttonColorGray");
          $("#releasePodId").css({"backgroundColor": "#5c6bc0"});
          $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});

          outboundProblemVerifyService.stopCallPod("start",$rootScope.workStationId,function (data) {
          });
      }

      // 呼叫pod
      $scope.callPodGrid = function () {
          $rootScope.state = true;
          getReleaseStart();
          var grid = $('#problemOutboundVerifyRightGrid').data('kendoGrid');
          var rows = grid.select();
          var dataFiled = "";
          for (var i = 0; i < rows.length; i++) {
              var rowData = grid.dataItem(rows[i]);
              if (i == 0) {
                  dataFiled = rowData.id;
                  $scope.jobType = rowData.jobType;
              } else {
                  dataFiled += "," + rowData.id;
              }
           }
           if (dataFiled.length > 0){
              outboundProblemVerifyService.callPodInboundProblem(dataFiled,$rootScope.sectionId,$scope.jobType, function (data) {
                  var callData = {
                      "pods": data,
                      "workStationId": $scope.workStationId
                  };
                  outboundProblemVerifyService.callPodInterface(JSON.stringify(callData), function () {
                      getPodResult();
                      $scope.searchGridRight();
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
      };

      // 停止分配pod
      $scope.stopAllocationPodGrid = function () {
          if ($rootScope.stopcallPOD == "停止分配POD"){
              outboundProblemVerifyService.stopCallPod("stop",$rootScope.workStationId,function (data) {
                  $("#stopAllocationPod").css({"backgroundColor": "#6E6E6E"});
                  $rootScope.stopcallPOD = "恢复分配POD";
              })
          }else {
              outboundProblemVerifyService.stopCallPod("start",$rootScope.workStationId,function (data) {
                  $("#stopAllocationPod").css({"backgroundColor": "#FF0000"});
                  $rootScope.stopcallPOD = "停止分配POD";
              })
          }
      };
      //websocket 推送pod的结果
      var podSocket;//webSocket
      function getPodResult() {
         // var url = PROBLEM_OUTBOUND.podWebSocket+$scope.workStationIds;
          var url = BACKEND_CONFIG.websocket+"websocket/getPod/"+$scope.workStationIds;
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
                  if (data.workstation == $scope.workStationIds) {
                      console.log("推送pod的信息：",data);
                      $rootScope.podNo = data.pod;
                      if ($scope.selectedSign == "all"){
                          $scope.selectAll();
                      }else if ($scope.selectedSign == "one"){
                          if ($rootScope.id != ""){
                              selectOutboundProblemRecord($rootScope.id);
                          }
                      }

                  }
              }
          };
          //关闭事件
          podSocket.onclose = function () {
              console.log("podSocket 关闭");
              // if(podSocket.readyState != 1){
              //     podSocket = new WebSocket(url);
              //     if(podSocket.readyState != 1){
              //         $scope.errorWindow("hardwareId1",$scope.hardwareWindows1);
              //     }
              // }
          };
          //发生了错误事件
          podSocket.onerror = function () {
              console.log("podSocket 发生了错误");
              podSocket = new WebSocket(url);
          }
      }

      // 释放pod
      $scope.releasePodGrid = function () {
          outboundProblemVerifyService.reservePod($rootScope.podNo,$rootScope.sectionId,"false",$rootScope.workStationIds,$rootScope.workStationId,function (data) {
              console.log("释放pod"+ $rootScope.podNo + "获取下一个pod:"+data.pod);
              $rootScope.podNo = data.pod;
              if ($rootScope.podNo!=""){
                  if ($scope.selectedSign == "all"){
                      $scope.selectAll();
                  }else if ($scope.selectedSign == "one"){
                      if ($rootScope.id != ""){
                          selectOutboundProblemRecord($rootScope.id);
                      }
                  }

              }else {
                  $timeout($scope.refreshPod(),5000);
                  if ($rootScope.podNo == ""){
                      $scope.refreshPod();
                      if ($scope.selectedSign == "all"){
                          $scope.selectAll();
                      }else if ($scope.selectedSign == "one"){
                          if ($rootScope.id != ""){
                              selectOutboundProblemRecord($rootScope.id);
                          }
                      }
                  }
              }
              $scope.searchGridRight();
              // $scope.select_all = false;
          },function(){
              $timeout($scope.refreshPod(),5000);
          })
      };

      //刷新pod信息
      $scope.refreshPod = function () {
          // if($rootScope.podNo == null || $rootScope.podNo == "" || $rootScope.podNo == "undefined"){
          outboundProblemVerifyService.refreshPod($rootScope.sectionId,$rootScope.workStationIds,function (poddata) {
              console.log("刷新的pod信息：",poddata);
              if ($rootScope.podNo!= ""){
                  $rootScope.podNo = poddata.pod;
                  if ($scope.selectedSign == "all"){
                      $scope.selectAll();
                  }else if ($scope.selectedSign == "one"){
                      if ($rootScope.id != ""){
                          selectOutboundProblemRecord($rootScope.id);
                      }

                  }

              }
          });
      };

  })
})();

