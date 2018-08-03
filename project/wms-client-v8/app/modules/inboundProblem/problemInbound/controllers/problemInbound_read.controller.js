/**
 * Created by thoma.bian on 2017/5/10.
 */
(function(){
  "use strict";

  angular.module('myApp').controller("problemInboundReadCtl", function ($scope,$timeout,$rootScope, PROBLEM_INBOUND,$state, $window, $stateParams, inboundProblemService, problemInboundBaseService) {
    $scope.flag = $stateParams.params;

    $scope.products = false;
    $scope.errorCargoSpace = false;
    $scope.errorCargoSpaceLocation = false;


      //
      // var cargoRecordGridOptions = $("#cargoRecordGRID").data("kendoGrid");
      // $scope.rows = cargoRecordGridOptions.tbody.find("tr");

      // if ($scope.rows.length <= 0){
      //     $scope.seeLocation = false;
      // }else {
      //     $scope.seeLocation = true;
      // }


    $scope.cargoRecordPage = "recordNumber";
    $scope.argoRecordGridShow = "notSelectCargoSpacePage";
    $scope.storageLocationNumb = 0;
    $scope.goodsCountContent = "";

      // if ($stateParams.params) {
      //     var params = angular.fromJson($stateParams.params);
      // }
      // if (params) {
      //     $rootScope.stopcallPOD = params.stopcallPOD;
      // }

    $("#allCargoSpaceId").addClass("buttonColorGray");
    $("#expirationDate").addClass("buttonColorGray");

    setTimeout(function () {$("#cargoSpaceId").focus();}, 0);

    $scope.backProblemInbound = function () {
      $rootScope.page = true;
      $rootScope.stopPod1 = true;
        // $scope.stopPod1 = true;
      if ($rootScope.state){  //是否可编辑
          $rootScope.edit = true;
          // $scope.edit = true;
      } else {
          $rootScope.edit = false;
          // $scope.edit = false;
      }
        $state.go("main.inbound_problem_disposal")
      // $state.go("main.inbound_problem_disposal", {
      //     params: angular.toJson({
      //         edit: $scope.edit,
      //         stopPod1: $scope.stopPod1
      //     })
      // })
      // inboundProblemService.workStationPodState($rootScope.workStationIds,function (data) {
      //     console.log(data);
      // })
    }
    //点击显示所有货位时
    $scope.allCargoSpaces = function () {
        $scope.seeLocation = false;
      $scope.argoRecordGridShow = "allCargoSpacePage";
      $("#allCargoSpaceId").removeClass("buttonColorGray");
      $("#notSelectCargoSpaceId").addClass("buttonColorGray");
      var cargoRecordGridOptions = $("#cargoRecordGRID").data("kendoGrid");
      cargoRecordGridOptions.setDataSource(new kendo.data.DataSource({data: $scope.allData}));
    };
    //点击显示未查货位
    $scope.notSelectCargoSpaces = function () {
        $scope.seeLocation = true;
      $scope.argoRecordGridShow = "notSelectCargoSpacePage";
      $("#allCargoSpaceId").addClass("buttonColorGray");
      $("#notSelectCargoSpaceId").removeClass("buttonColorGray");
      var cargoRecordGridOptions = $("#cargoRecordGRID").data("kendoGrid");
      cargoRecordGridOptions.setDataSource(new kendo.data.DataSource({data: $scope.selectData}));

      var rows = cargoRecordGridOptions.tbody.find("tr");

      if (rows.length <= 0){
          $scope.seeLocation = false;
      }
    };

    inboundProblemService.findInboundProblem($stateParams.id,function(data){
     $scope.rowData = data;
     if (data.client) {
        $scope.LessclientId = data.client.id;
      }
      $scope.problemType = data.problemType;
      if ($scope.problemType == "MORE") {
        $scope.problemTypeValue = "多货";
      } else {
        $scope.problemTypeValue = "少货";
      }
      $scope.inboundProblemId = data.id;
      $scope.solveAmount = data.solveAmount;
      $scope.amount = data.amount - $scope.solveAmount;
      $scope.problemStorageLocation = data.problemStorageLocation;
      //商品信息
      inboundProblemService.getGoodsInformation({"inboundProblemId": data.id
      }, function (data) {
        $scope.skuData = data.lotMandatory;
        for (var k in data) $scope[k] = data[k];
      });

    var recordColumns = [{field: "id", width: "50px", headerTemplate: "<span translate='序号'></span>"},
      {field: "name", width: 180, headerTemplate: "<span translate='上架货位历史'></span>",
          template: function (item) {
              var name = item.name;
              console.log("name:",name);
              console.log("$rootScope.podNo:",$rootScope.podNo);
              var htmlStr = "<div>" + name + "</div>";
              // this.parent().css("background", "#ffc000");
              if ($rootScope.podNo != "" && name.indexOf($rootScope.podNo) >= 0){
                  $(this).css("background", "#ffc000");
              }
              // setTimeout(function () {
              //     var cargoRecordGridOptions = $("#cargoRecordGRID").data("kendoGrid");
              //     cargoRecordGridOptions.tbody.find("tr").each(function () {
              //         var td = $(this).find("td:last-child");
              //         console.log("name1:",name);
              //         console.log("$rootScope.podNo:",$rootScope.podNo);
              //         if (name.indexOf($rootScope.podNo) >= 0 ){
              //             $(this).css("background", "#ffc000");
              //         }
              //     })
              // },0);
              return htmlStr;
          }
      },
      {field: "amount", headerTemplate: "<span translate='问题商品上架数量'></span>"},
      {field: "actualAmount", headerTemplate: "<span translate='货位问题商品当前数量'></span>"},
      {field: "totalAmount", headerTemplate: "<span translate='货位商品总数'></span>"},
      {field: "clientName", headerTemplate: "<span translate='CLIENT'></span>"},
      {field: "unexamined", headerTemplate: "<span translate='unexamined'></span>",
        template: function (item) {
          var unexamined = item.unexamined;
          var htmlStr = "<div>" + unexamined + "</div>";
          setTimeout(function () {
            var cargoRecordGridOptions = $("#cargoRecordGRID").data("kendoGrid");
            cargoRecordGridOptions.tbody.find("tr").each(function () {
              var td = $(this).find("td:last-child");
              if (td.text() == "NG") {
                $(this).css("background", "#92D050");
              }
              cargoRecordGridOptions.hideColumn("unexamined");
            }, 0)

          });
          return htmlStr;
        }
      }
    ];
    $scope.cargoRecordGridOptions = problemInboundBaseService.grid([], recordColumns, 240);
    problemInboundRecords(data.id);


  });
  function problemInboundRecords(inboundProblemId) {
    var selectData = [], allData = [], number = 0;
    inboundProblemService.inboundProblemRecord({"inboundProblemId": inboundProblemId
    }, function (data) {
      $scope.storageLocationNumb = 0;
      if (data.length == 0) {
          $scope.seeLocation = false;
          $("#cargoSpaceId").attr("disabled", true);
          $("#problemProductsNumber").attr("disabled",true);
        var cargoRecordGridOptions = $("#cargoRecordGRID").data("kendoGrid");
        cargoRecordGridOptions.hideColumn("unexamined");

      }else{
          $scope.seeLocation = true;
      }
      // var data = [{"id": 1, "name": '1-1-A001B05', "amount": 3, "actualAmount": 4, "totalAmount":13, "storageLocationId": '00227a79-4536-4376-a3c4-498607c86e63', "itemDataId": 1, "clientName": 'system', "clientId": 'SYSTEM', "unexamined":"H"},
      //   {"id": 2, "name": '1-1-A001B06', "amount": 4, "actualAmount": 2, "totalAmount": 10, "storageLocationId": '002fc1a2-3892-4789-b9c4-dd99f988cfd1S', "itemDataId": 1, "clientName": 'system', "clientId": 'SYSTEM', "unexamined": "H"}];
      for (var i = 0; i < data.length; i++) {
        number++;
        allData.push({"id": number, "name": data[i].name, "amount": data[i].amount, "actualAmount": data[i].actualAmount, "totalAmount": data[i].totalAmount, "storageLocationId": data[i].storageLocationId, "itemDataId": data[i].itemDataId, "clientName": data[i].clientName, "clientId": data[i].clientId, "unexamined": data[i].unexamined
        });
        if (data[i].unexamined == "H") {
          $scope.storageLocationNumb++;
          selectData.push({"id": number, "name": data[i].name, "amount": data[i].amount, "actualAmount": data[i].actualAmount, "totalAmount": data[i].totalAmount, "storageLocationId": data[i].storageLocationId, "itemDataId": data[i].itemDataId, "clientName": data[i].clientName, "clientId": data[i].clientId, "unexamined": data[i].unexamined
          });
        }
      }
      $scope.selectData = selectData;
      $scope.allData = allData;
      // for(var i=0;i<selectData;i++){
      //     $scope.podNames = selectData[i].name;
      //     console.log(podNames);
      // }


      var cargoRecordGridOptions = $("#cargoRecordGRID").data("kendoGrid");
      cargoRecordGridOptions.setDataSource(new kendo.data.DataSource({data: selectData}));

      if ($rootScope.podNo!=""){
          var grid = $("#cargoRecordGRID").data("kendoGrid");    // 行样式

          var rows = grid.tbody.find("tr");
          rows.each(function (i, row) {
              var srcData = grid.dataItem(row);
              if($rootScope.podNo != "" && srcData.name.startsWith($rootScope.podNo)){
                  $(row).css("background-color", "#ffc000");
              }
          });

      }else {
          debugger;
          getPodResult();
      }

    });
  }

  //record 记录
  function inboundProblemState(data) {
    inboundProblemService.getInboudProblemState(data, function () {
    });
  }

  function updateInboundProblemMethod(rowData, solveAmount, value, arr) {
    var data = {}, state = "OPEN";
      if (value != "") {
        state = value;
      } else {
        state = rowData.state;
      }
      if ($scope.amount == 0 || $scope.amount < 0) {
        state = "CLOSE";
      }
      if (rowData.problemType == "MORE") {
        data = {
          "id": rowData.id,
          "problemType": rowData.problemType,
          "amount": rowData.amount,
          "jobType": rowData.jobType,
          "reportBy":rowData.reportBy,
          "solveBy": rowData.solveBy,
            "reportDate ":rowData.reportDate,
          // "reportDate ":$window.localStorage["username"],
          "state": state,
          "problemStorageLocation":rowData.problemStorageLocation,
          "lotNo":rowData.lotNo,
          "serialNo":rowData.serialNo,
          "serialVersionUID":rowData.serialVersionUID,
          "solveAmount":solveAmount,
          "itemNo": rowData.itemNo,
          "dealState": arr.id,
          "description": arr.reason
        }
      } else {
        data = {
          "id": rowData.id,
          "problemType": rowData.problemType,
          "amount": rowData.amount,
          "jobType": rowData.jobType,
          "reportBy":rowData.reportBy,
          "solveBy": rowData.solveBy,
            "reportDate ":rowData.reportDate,
          // "reportDate ":$window.localStorage["username"],
          "state": state,
          "problemStorageLocation":rowData.problemStorageLocation,
          "lotNo":rowData.lotNo,
          "serialNo":rowData.serialNo,
          "serialVersionUID":rowData.serialVersionUID,
          "solveAmount":solveAmount,
          "itemDataId": rowData.itemData.id,
            "description": arr.reason

        }
      }
    inboundProblemService.updateInboundProblem(data, function (v) {
      $scope.rrr = true;
      $scope.products = false;
      $scope.errorCargoSpace = false;
      $scope.errorCargoSpaceLocation = false;
      $scope.productsNumber = "";
      $scope.cargoSpace = "";
    });
  }

  //检查货位
  $scope.checkCargoSpace = function (e) {

    var keycode = window.event ? e.keyCode : e.which;
    if (keycode == 13) {
      for (var i = 0; i < $scope.selectData.length; i++) {
        var data = $scope.selectData[i];
          this.cargoSpace = this.cargoSpace.toUpperCase();
        if (data.name == this.cargoSpace) {
          $scope.checkName = data.name;
          $scope.goodsCountContent = 0;
          $scope.products = true;
          $scope.errorCargoSpace = false;
            $scope.errorCargoSpaceLocation = false;
          $scope.itemDataId = data.itemDataId;
          $scope.storageLocationId = data.storageLocationId;
          $scope.amountFiled = data.amount;
          $scope.problemAmountFiled = data.actualAmount;
          $scope.storageLocationAmountFiled = data.totalAmount;
          $scope.clientFiled = data.clientId;
          var grid = $("#cargoRecordGRID").data("kendoGrid");
          var uid = grid.dataSource.at(i).uid;
          grid.select("tr[data-uid='" + uid + "']");
        }
      }
      //$scope.checkName  grid 中的 name 列值
        // this.cargoSpace 扫描的货位
      if ($scope.checkName != this.cargoSpace) {
          $scope.errorCargoSpaceLocation = true;
          $scope.products = false;
          $scope.errorCargoSpace = false;
      }
      else if($rootScope.podNo != "" && !this.cargoSpace.startsWith($rootScope.podNo)){
        //   if(data.pod != "" && srcData.name.startsWith(data.pod)){
        // if ($rootScope.podNo != this.cargoSpace){
        $scope.errorCargoSpace = true;
        $scope.products = false;
          $scope.errorCargoSpaceLocation = false;
        // }
      }else if($rootScope.podNo == ""){
          $scope.errorCargoSpace = true;
          $scope.products = false;
          $scope.errorCargoSpaceLocation = false;
      }
      else {
        setTimeout(function () {$("#problemProductsNumber").focus();}, 0);
      }
    }
  };

  $scope.problemProductsNumber = function (e) {
    var keycode = window.event ? e.keyCode : e.which;
    if (keycode == 13) {
      if ($scope.problemAmountFiled == this.productsNumber) {

          setTimeout(function () {$("#cargoSpaceId").focus();}, 0);
        inboundProblemService.problemProductsNumber({
          "amount": $scope.amountFiled,
          "inboundProblemId": $scope.inboundProblemId,
          "storageLocationId": $scope.storageLocationId,
          "state": "OK",
          "clientId": $scope.clientFiled,
          "itemDataAmount": $scope.amountFiled,
          "storageLocationAmount": $scope.storageLocationAmountFiled,
           "problemAmount": $scope.problemAmountFiled
        }, function (data) {
          problemInboundRecords($scope.inboundProblemId);
          $scope.products = false;
          $scope.errorCargoSpace = false;
          $scope.errorCargoSpaceLocation = false;
          $scope.productsNumber = "";
          $scope.cargoSpace = "";
        })
      }
      else if ($scope.problemAmountFiled > this.productsNumber && $scope.problemType == "MORE") {
        $scope.goodsCountContent = 'goodsCountMore';
        $scope.numberPoor = $scope.problemAmountFiled - this.productsNumber;
        setTimeout(function () {$("#recargoSpaceNameId").focus();}, 0);
        $scope.recargoSpace = $scope.cargoSpace;
      }
      else if ($scope.problemAmountFiled < this.productsNumber && (parseInt($scope.problemAmountFiled) + parseInt($scope.amount)) >= this.productsNumber && $scope.problemType == "LESS") {
        lossMethod();
      }else if (((parseInt($scope.problemAmountFiled) + parseInt($scope.amount)) < this.productsNumber) && $scope.problemType == "LESS"){
          // 数量输入有误
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
        $timeout($scope.refreshPod(),8000);
    }

  };

  // 多货处理
  $scope.reCheckCargoSpace = function (e) {
    var keycode = window.event ? e.keyCode : e.which;
    if (keycode == 13) {
      inboundProblemService.getDestinationId($scope.cargoSpace,function(data) {
        $scope.storageLocationId = data.id;
      });
      if ($scope.recargoSpace == $scope.cargoSpace) {
        var data = {
          "amount": $scope.numberPoor,
          "storageLocationId": $scope.storageLocationId,
          "itemDataId": $scope.itemDataId,
          "problemStorageLocation": $scope.problemStorageLocation
        };
       inboundProblemService.getStowingOverage(data, function () {
          inboundProblemService.problemProductsNumber({
            "amount": $scope.amountFiled,
            "inboundProblemId": $scope.inboundProblemId,
            "storageLocationId": $scope.storageLocationId,
            "state": "OK",
            "clientId": $scope.clientFiled,
            "itemDataAmount": $scope.amountFiled,
            "storageLocationAmount": $scope.storageLocationAmountFiled,
            "problemAmount": $scope.problemAmountFiled
          }, function (data) {
            problemInboundRecords($scope.inboundProblemId);
            $scope.solveAmount = parseInt($scope.solveAmount) + parseInt($scope.numberPoor);
            updateInboundProblemMethod($scope.rowData, $scope.solveAmount, '', '');
            if ($scope.rowData.amount - $scope.solveAmount == 0) {
              $scope.cargoRecordPage = 'recordNumberSuccess';
            } else {
              $scope.goodsCountContent = 'goodCountSuccess';
            }

          });
          inboundProblemService.getInboundDeal("More_FindBin", function (data) {
              inboundProblemState({
                  "inboundProblemId": $scope.inboundProblemId,
                  // "amount": $scope.amount,
                  "amount": $scope.numberPoor,
                  "inboundProblemRuleId": data.id,
                  "state": 'CLOSE',
                  "storageLocationId":$scope.storageLocationId,
                  "solveBy":$window.localStorage["username"],
                  "clientId": $scope.clientFiled
              });
          });
          $scope.amount = $scope.amount - $scope.numberPoor;

        })
      } else {
        inboundProblemService.problemProductsNumber({
          // "amount": $scope.problemAmountFiled - $scope.numberPoor,
          "amount": $scope.amountFiled,
          "inboundProblemId": $scope.inboundProblemId,
          "storageLocationId": $scope.storageLocationId,
          "state": "NG",
          "clientId": $scope.clientFiled,
            "itemDataAmount": $scope.amountFiled,
            "storageLocationAmount": $scope.storageLocationAmountFiled,
            "problemAmount": $scope.problemAmountFiled - $scope.numberPoor
        }, function (data) {
          $scope.solveAmount = $scope.numberPoor;
          updateInboundProblemMethod($scope.rowData, $scope.solveAmount, '');
          if ($scope.rowData.amount - $scope.solveAmount == 0) {
            $scope.cargoRecordPage = 'recordNumberSuccess';
          } else {
            $scope.goodsCountContent = 'goodCountSuccess';
          }
          inboundProblemService.getDestinationId ($scope.recargoSpace,function(data){
            inboundProblemService.moveGoods({
              "sourceId":$scope.inboundProblemId,
              "destinationId": data.id,
              "itemDataId": $scope.itemDataId,
              "amount": $scope.numberPoor
            }, function (data) {
              problemInboundRecords($scope.inboundProblemId);
              $scope.amount = $scope.amount - $scope.numberPoor;
            })
          });

        })
      }
    }
  };

  //少货处理
  function lossMethod(){
   inboundProblemService.getStowingLoss({
      "amount": $scope.productsNumber - $scope.problemAmountFiled,
      "toName": $scope.cargoSpace,
      "itemDataId": $scope.itemDataId,
      "fromName": $scope.problemStorageLocation
     }, function () {
      inboundProblemService.problemProductsNumber({
        "amount": $scope.amountFiled,
        "inboundProblemId": $scope.inboundProblemId,
        "storageLocationId": $scope.storageLocationId,
        "state": "NG",
        "clientId": $scope.clientFiled,
        "itemDataAmount": $scope.amountFiled,
        "storageLocationAmount": $scope.totalAmountFiled,
        "problemAmount": $scope.productsNumber - $scope.problemAmountFiled
      }, function (data) {
        problemInboundRecords($scope.inboundProblemId);
        $scope.solveAmount = parseInt($scope.solveAmount) + parseInt($scope.productsNumber - $scope.problemAmountFiled);
        $scope.recargoSpace = $scope.cargoSpace;
        $scope.numberPoor = $scope.productsNumber - $scope.problemAmountFiled;
        $scope.amount = $scope.amount - $scope.numberPoor;
        updateInboundProblemMethod($scope.rowData, $scope.solveAmount, '', '');
        if ($scope.rowData.amount - $scope.solveAmount == 0) {
          $scope.cargoRecordPage = 'recordNumberSuccess';
        } else {
          $scope.goodsCountContent = 'goodCountSuccess';
        }
        inboundProblemService.getInboundDeal("Less_FindBin", function (data) {
            inboundProblemState({
                "inboundProblemId": $scope.inboundProblemId,
                "amount": $scope.numberPoor,
                "inboundProblemRuleId": data.id,
                "state": 'CLOSE',
                "storageLocationId":$scope.storageLocationId,
                "solveBy":$window.localStorage["username"],
                "clientId": $scope.clientFiled
            });
        });

        $scope.products = false;
        $scope.productsNumber = "";
        $scope.cargoSpace = "";
      })
    });
  };

  //盘盈//盘亏
  // $scope.produceDate = function () {
  //   $scope.goodsMaturity = "produce";
  //   $("#expirationDate").addClass("buttonColorGray");
  //   $scope.flag = 1;
  // };
  //
  // $scope.maturityDate = function () {
  //   $scope.goodsMaturity = "maturity";
  //   $("#productionDate").addClass("buttonColorGray");
  //   $scope.flag = 2;
  // };

  $scope.problemGoodsNumber = function () {
    if ($scope.problemType == "MORE") {
      if ($scope.skuData == true) {
        if ($scope.lotType == "EXPIRATION") {
          $scope.goodsMaturity = 'maturity';
        } else {
          $scope.goodsMaturity = 'produce';
          $scope.month = parseInt($scope.months) + parseInt($scope.month || 0);
          while ($scope.month > 12) {
            $scope.year++;
            $scope.month -= 12;
          }
        }
        $("#goodsMaturityDateId").parent().addClass("mySelect");
        $scope.goodsMaturityDateWindow.setOptions({
          width: 700,
          height: 350,
          visible: false,
          actions: false,
          activate:function () {
              if ($scope.goodsMaturity == 'maturity'){
                  $("#maturityYear").focus();
              }else {
                  $("#produceYear").focus();
              }
          }
        });
        $scope.goodsMaturityDateWindow.center();
        $scope.goodsMaturityDateWindow.open();
      } else {
        $scope.goodsMaturityDateSure();
      }
    } else {
      $scope.goodsMaturityDateSure();
    }
  };

  $scope.goodsMaturityDateSure = function () {
    var window, windowId;
    if ($scope.problemType == "MORE") {
      windowId = $("#diskSurplusGoodsId");
      window = $scope.diskSurplusGoodsWindow;
    } else {
      windowId = $("#diskDeficitGoodsId");
      window = $scope.diskDeficitGoodsWindow;
    }
    $scope.goodsMaturityDateWindow.close();
    windowId.parent().addClass("myWindow");
    window.setOptions({
      width: 800,
      height: 200,
      visible: false,
      actions: false
    });
    window.center();
    window.open();
    $scope.useNotAfter = $scope.year + "-" + pad($scope.month) + "-" + pad($scope.day)+" 00:00:00";
  };

  $scope.diskSurplusGoodsSure = function () {
    $scope.diskSurplusGoodsWindow.close();
    $scope.source = $scope.problemStorageLocation;
    $scope.cargoRecordPage = 'recordNumberContent';
  };

  //盘盈确认
  $scope.overageSure = function () {
    inboundProblemService.getInboundDeal("More_Overage", function (data) {
          inboundProblemService.getDestinationId($scope.problemStorageLocation,function(sourceData) {
            $scope.storageLocationId = sourceData.id;
            inboundProblemState({
                "inboundProblemId": $scope.inboundProblemId,
                "amount": $scope.amount,
                "inboundProblemRuleId": data.id,
                "state": 'CLOSE',
                "storageLocationId":$scope.storageLocationId,
                "solveBy":$window.localStorage["username"],
                "clientId": $scope.client.id
            });
          });
      });
    if($scope.useNotAfter.indexOf("undefined")>=0){
      $scope.useNotAfter = "";
    }
    inboundProblemService.getDestinationId ($scope.destination,function(data) {
      var arr = {
        "destinationId": data.id,
        "clientId": $scope.client.id,
        "itemNo": $scope.itemNo,
        "amount": $scope.amount,
        "inventoryState": "",
        "useNotAfter": $scope.useNotAfter || "",
        "adjustReason": 'Ibp_Overage',
        "thoseResponsible": $window.localStorage["username"],
        "problemDestination": ""
      };

      inboundProblemService.overageGoods(arr, function (data) {
        updateInboundProblemMethod($scope.rowData, '', 'CLOSE', '');
        $scope.cargoRecordPage = 'recordNumberOverage';
      });
    })
  };

  //盘亏
  $scope.diskDeficitGoodsSure = function () {
    inboundProblemService.getInboundDeal("Less_Loss", function (data) {
        inboundProblemService.getDestinationId($scope.problemStorageLocation,function(sourceData) {
            $scope.storageLocationId = sourceData.id;
          inboundProblemState({
              "inboundProblemId": $scope.inboundProblemId,
              "amount": $scope.amount,
              "inboundProblemRuleId": data.id,
              "state": 'CLOSE',
              "storageLocationId":$scope.storageLocationId,
              "solveBy":$window.localStorage["username"],
              "clientId": $scope.LessclientId
          });
        });
    });
    $scope.diskDeficitGoodsWindow.close();
    inboundProblemService.getDestinationId ($scope.problemStorageLocation,function(data) {
      inboundProblemService.lossGoods({
        "sourceId": data.id,
        "itemDataId": $scope.rowData.itemData.id,
        "amount": $scope.amount,
        "adjustReason": 'Ibp_Loss',
        "thoseResponsible": $window.localStorage["username"],
        "problemDestination": ""
      }, function () {
        updateInboundProblemMethod($scope.rowData, '', 'CLOSE', '');
        $scope.cargoRecordPage = 'recordNumberLoss';
      })
    });
  };

  $scope.nextGoods = function () {
    $rootScope.page = true;
    $state.go("main.inbound_problem_disposal");
   };

  function pad(str) {
    str = str + "";
    var pad = "00";
    return (pad.length > str.length ? pad.substring(0, pad.length - str.length) + str : str);
  }

  $scope.overageCancel = function () {
    $scope.products = false;
    $scope.errorCargoSpace = false;
    $scope.errorCargoSpaceLocation = false;
    $scope.cargoRecordPage = 'recordNumber';
    $scope.argoRecordGrid = "notSelectCargoSpace";
  };

  $scope.maturityYearKeyDown = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          setTimeout(function () {$("#maturityMonth").focus();
          },0  );
      }
  };

  $scope.maturityMonthKeyDown = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          setTimeout(function () {$("#maturityDay").focus();
          },0  );
      }
  };

  $scope.maturityDayKeyDown = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.goodsMaturityDateSure();
          // setTimeout(function () {$("#destinationContainerId").focus();
          // },0  );
      }
  };

  $scope.produceYearKeyDown = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          setTimeout(function () {$("#produceMonth").focus();
          },0  );
      }
  };
  $scope.produceMonthKeyDown = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          setTimeout(function () {$("#produceDay").focus();
          },0  );
      }
  };

  $scope.produceDayKeyDown = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          setTimeout(function () {$("#produceDatas").focus();
          },0  );
      }
  };

  $scope.produceDatasKeyDown = function(e){
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
          $scope.goodsMaturityDateSure();
      }
  };
  //备注
  $scope.remarks = function () {
    if ($scope.problemTypeValue == "多货"){
      $("#remarksId").parent().addClass("mySelect");
      $scope.remarksWindow.setOptions({
        width: 800,
        height: 280,
        visible: false,
        actions: false
      });
      $scope.remarksWindow.center();
      $scope.remarksWindow.open();
    }else if($scope.problemTypeValue == "少货"){
        $("#remarksLessId").parent().addClass("mySelect");
        $scope.remarksWindowLess.setOptions({
            width: 800,
            height: 280,
            visible: false,
            actions: false
        });
        $scope.remarksWindowLess.center();
        $scope.remarksWindowLess.open();
    }
  };

  //备注确认
  $scope.remarksWindowSure = function () {
    if ($scope.problemType == "MORE") {
      if ($scope.reasonId == 1) {
        $scope.reason = $("#reasonOneId").html().substring(2);
      }
      if ($scope.reasonId == 2) {
        $scope.reason = $("#reasonTwoId").html().substring(2);
      }
      var arr = {
        id: $scope.reasonId,
        reason: $scope.reason
      };
      if ($scope.reasonId) {
        updateInboundProblemMethod($scope.rowData, '', 'OPEN', arr);
      }
        $scope.remarksWindow.close();
    }else{
        if ($scope.reasonId == 1) {
            $scope.reason = $("#reasonOneLessId").html().substring(2);
        }
        var arr = {
            id: $scope.reasonId,
            reason: $scope.reason
        };
        if ($scope.reasonId) {
            updateInboundProblemMethod($scope.rowData, '', 'OPEN', arr);
        }
        $scope.remarksWindowLess.close();
    }

  }
  // 释放pod
  $scope.releasePodGrid = function () {

    inboundProblemService.reservePod($rootScope.podNo,$rootScope.sectionId,"false",$rootScope.workStationIds,$rootScope.workStationId,function (data) {
        console.log("释放pod"+ $rootScope.podNo + "获取下一个pod:"+data.pod);
        // $rootScope.select_one = false;
        // $rootScope.select_all = false;
        $rootScope.podNo = data.pod;
        if ($rootScope.podNo!=""){
            var grid = $("#cargoRecordGRID").data("kendoGrid");    // 行样式
            var rows = grid.tbody.find("tr");
            rows.each(function (i, row) {
                var srcData = grid.dataItem(row);
                if(data.pod != "" && srcData.name.startsWith(data.pod)){
                    $(row).css("background-color", "#ffc000");
                }
            });
        }else {
            $timeout($scope.refreshPod(),5000);
            if ($rootScope.podNo == ""){
                $scope.refreshPod();
            }

        }
        // $rootScope.select_one = false;
        // $rootScope.select_all = false;
        // $rootScope.podNo = data.pod;
        // if ($rootScope.podNo!=""){
        //     getPodResult();
        // }

        // getColor(data.pod);

    })
  };
  // function getColor(pod) {
  //   var arr;
  //   debugger
  //   arr = $scope.podNames;
  //   for (var i = 0;i<arr.length;i++){
  //       if (i.indexOf(pod) >= 0 ){
  //           $(this).css("background", "#ffc000");
  //       }
  //   }
  // //     // var cargoRecordGridOptions = $("#cargoRecordGRID").data("kendoGrid");
  // //     forEach (var podNames : podName){
  // //       if (podName.indexOf(pod) >= 0 ){
  // //           $(this).css("background", "#ffc000");
  // //       }
  // //     }
  // //     // cargoRecordGridOptions.tbody.find("tr").each(function () {
  // //     //     var td = $(this).find("td:last-child");
  // //     //     if (td.text().indexOf(pod) >= 0 ){
  // //     //         $(this).css("background", "#ffc000");
  // //     //     }
  // //     // })
  // }

  //websocket 推送pod的结果
  // $scope.podSocket=null;//webSocket
  function getPodResult() {
      var url = PROBLEM_INBOUND.podWebSocket+$rootScope.workStationIds;
      console.log("url:",url);
      $scope.podSocket = new WebSocket(url);
      //打开事件
      $scope.podSocket.onopen = function () {
          console.log("podSocket 已打开");
      };
      //获得消息事件
      $scope.podSocket.onmessage = function (msg) {
          console.log("podSocket 正在推送消息。。。");
          var data = JSON.parse(msg.data);
          console.log("data:",data)
          if(data.pod != "success"){
              if (data.workstation == $rootScope.workStationIds) {
                  console.log("推送pod的信息：",data);
                  $rootScope.podNo = data.pod;
                  var grid = $("#cargoRecordGRID").data("kendoGrid");    // 行样式

                  var rows = grid.tbody.find("tr");
                  rows.each(function (i, row) {
                      var srcData = grid.dataItem(row);
                      if(data.pod != "" && srcData.name.startsWith(data.pod)){
                          $(row).css("background-color", "#ffc000");
                      }
                  });

              }
          }
      };

      //关闭事件
      $scope.podSocket.onclose = function () {
          console.log("podSocket 关闭");
          // if($scope.podSocket.readyState != 1){
          //     $scope.podSocket = new WebSocket(url);
          //     if($scope.podSocket.readyState != 1){
          //         $scope.errorWindow("hardwareId1",$scope.hardwareWindows1);
          //     }
          // }
      };
      //发生了错误事件
      $scope.podSocket.onerror = function () {
          console.log("podSocket 发生了错误");
          $scope.podSocket = new WebSocket(url);
      }
  }

      //刷新pod信息
      $scope.refreshPod = function () {
          // if($rootScope.podNo == null || $rootScope.podNo == "" || $rootScope.podNo == "undefined"){
              inboundProblemService.refreshPod($rootScope.sectionId,$rootScope.workStationIds,function (poddata) {
                  console.log("刷新的pod信息：",poddata);
                  if ($rootScope.podNo!= ""){
                    $rootScope.podNo = poddata.pod;
                    var grid = $("#cargoRecordGRID").data("kendoGrid");    // 行样式

                    var rows = grid.tbody.find("tr");
                    rows.each(function (i, row) {
                        var srcData = grid.dataItem(row);
                        if(srcData.name.startsWith(poddata.pod)){
                            $(row).css("background-color", "#ffc000");
                        }
                    });
                  }
              });
          // }
      };


  })
})();
