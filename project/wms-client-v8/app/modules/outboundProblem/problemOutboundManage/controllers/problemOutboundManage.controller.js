/**
 * Created by thoma.bian on 2017/5/10.
 */

(function () {
  'use strict';
  angular.module('myApp').controller("problemOutboundManageCtl", function ($window,$scope,$rootScope, problemOutboundManageService, problemOutboundBaseService) {

    $scope.problemsGrid = "problemsSolve";
    $scope.problemsGridSearch = "problemsSolveSearch";
    $scope.problemsGridTimeSearch = "problemsSolveTimeSearch";
    $scope.checkbox = true;
    $("#problemRecordsId").addClass("buttonColorGray");
    $("#problemClosesId").addClass("buttonColorGray");

    $scope.problemsSolving = function () {
      $rootScope.problemManage = 1;
      $scope.problemsGrid = "problemsSolve";
      $scope.problemsGridSearch = "problemsSolveSearch";
      $scope.problemsGridTimeSearch = "problemsSolveTimeSearch";
      $scope.checkbox = true;
      $("#problemsSolvingId").removeClass("buttonColorGray");
      $("#problemRecordsId").addClass("buttonColorGray");
      $("#problemClosesId").addClass("buttonColorGray");
      $scope.stateDateOpen = "";
      $scope.endDateOpen = "";
      $scope.itemNoLeft = "";
        problemOutboundUnsolveManageGrid( {"state":"unsolved","userName":"","seek":$scope.itemNoLeft,"startDate":$scope.stateDateOpen,"endDate":$scope.endDateOpen},"problemsSolvingGrid");
    };

    $scope.problemRecords = function () {
      $rootScope.problemManage = 2;
      $scope.problemsGrid = "problemsRecord";
      $scope.problemsGridSearch = "problemsRecordSearch";
      $scope.problemsGridTimeSearch = "problemsRecordTimeSearch";
      $scope.checkbox = false;
      $("#problemsSolvingId").addClass("buttonColorGray");
      $("#problemRecordsId").removeClass("buttonColorGray");
      $("#problemClosesId").addClass("buttonColorGray");
      $scope.stateDateProcess = "";
      $scope.endDateProcess = "";
      $scope.itemNoRight = "";
      problemOutboundManageGrid( {"state":"process","userName":"","seek":$scope.itemNoRight,"startDate":$scope.stateDateProcess,"endDate":$scope.endDateProcess},"problemRecordsGrid");
    };

    $scope.problemCloses = function () {
      $rootScope.problemManage = 3;
      $scope.problemsGrid = "problemsClose";
      $scope.problemsGridSearch = "problemsCloseSearch";
      $scope.problemsGridTimeSearch = "problemsCloseTimeSearch";
      $scope.checkbox = false;
      $("#problemsSolvingId").addClass("buttonColorGray");
      $("#problemRecordsId").addClass("buttonColorGray");
      $("#problemClosesId").removeClass("buttonColorGray");
      $scope.stateDateClose = "";
      $scope.endDateClose = "";
      $scope.itemNoClose = "";
      problemOutboundManageGrid( {"state":"close","userName":"","seek":$scope.itemNoClose,"startDate":$scope.stateDateClose,"endDate":$scope.endDateClose},"problemCloseGrid");
    };

    // 查询全部
    $scope.selectAll = function () {
      var grid = $('#problemsSolvingGrid').data('kendoGrid');
      if ($scope.select_all) {
        $scope.select_one = true;
        grid.tbody.children('tr').addClass('k-state-selected');
        //grid.select(grid.tbody.find(">tr"));
      } else {
        $scope.select_one = false;
        grid.tbody.children('tr').removeClass('k-state-selected');
      }
        console.log("选中条数------>:"+$('#problemsSolvingGrid').data('kendoGrid').select().length)
    };

    // 关闭选中的
    $scope.closeProblemsGrid = function () {
      var grid = $('#problemsSolvingGrid').data('kendoGrid');
      var rows = grid.select();
      var dataFiled = "";
      for (var i = 0; i < rows.length; i++) {
        var rowData = grid.dataItem(rows[i]);
        if (i == 0) {
          dataFiled = rowData.id;
        } else {
          dataFiled += "," + rowData.id;
        }
      }
      problemOutboundManageService.updateOutboundProblemList(dataFiled,"Closed", function () {
        $scope.searchGridLeft();
        $scope.select_all = false;
      })
    };

    $scope.chk = false;
    $scope.selectOne = function (val, uid) {
      var grid = $('#problemsSolvingGrid').data('kendoGrid');
      if (val) {
        // grid.select("tr[data-uid='" + uid + "']").addClass('k-state-selected');
          grid.tbody.children('tr[data-uid="' + uid + '"]').addClass('k-state-selected');
      } else {
        grid.tbody.children('tr[data-uid="' + uid + '"]').removeClass('k-state-selected');
      }
    };

    //outboundproblem  process  close 核实公共方法
    function problemOutboundManageGrid(value,gridId){
      problemOutboundManageService.getOutboundProblemManage(value, function (data) {
        var cargoRecordGridOptions = $("#"+gridId).data("kendoGrid");
          cargoRecordGridOptions.setDataSource(new kendo.data.DataSource({data: data}));
      })
    }
      // unsolve 核实公共方法
      function problemOutboundUnsolveManageGrid(value,gridId){
          problemOutboundManageService.getOutboundProblem(value, function (data) {
              var cargoRecordGridOptions = $("#"+gridId).data("kendoGrid");
              cargoRecordGridOptions.setDataSource(new kendo.data.DataSource({data: data}));
          })
      }



    var columnsLeft = [
      {width: 35, template: "<input type=\"checkbox\"  ng-model='chk' id='dataItem.id' class='check-box' ng-checked = 'select_one' ng-click='selectOne(chk,dataItem.uid)'/>"},
      {field: "problemType", width: "80px", headerTemplate: "<span translate='问题类型'></span>",
        template: function (item) {
          var value = "";
          if (item.problemType == "MORE") {
            value = "<span>多货</span>"
          } else {
            value = "<span>少货</span>"
          }
          return value;
        }
      },
        {field: "itemData.itemNo", headerTemplate: "<span translate='SKU'></span>" ,
            template: function (item) {
                return item.itemData ? item.itemData.itemNo : item.itemNo;
            }},
    {field: "problemStoragelocation", headerTemplate: "<span translate='容器'></span>"},
      {field: "amount", headerTemplate: "<span translate='数量'></span>"},
      {field: "reportBy", headerTemplate: "<span translate='操作人'></span>"},
      {
        field: "reportDate", headerTemplate: "<span translate='员工操作时间'></span>", template: function (item) {
        return item.reportDate? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.reportDate)) : "";
      }
      },
      {field: "solvedBy", headerTemplate: "<span translate='问题人员'></span>"},
      {field: "description", headerTemplate: "<span translate='备注信息'></span>"}];

    $scope.problemsSolvingGridOptions = problemOutboundBaseService.grid("", columnsLeft, $(document.body).height() - 300);
      problemOutboundUnsolveManageGrid( {"state":"unsolved","userName":"","seek":$scope.itemNoLeft,"startDate":$scope.stateDateOpen,"endDate":$scope.endDateOpen},"problemsSolvingGrid");

      $scope.searchGridLeftKeyDown = function (e) {
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode == 13) {
              $scope.searchGridLeft();
          }
      };

    $scope.searchGridLeft = function () {
        $scope.select_all = false;
        $scope.select_one = false;
        if ($scope.itemNoLeft == undefined) {
            $scope.itemNoLeft = "";
        }
        problemOutboundUnsolveManageGrid( {"state":"unsolved","userName":"","seek":$scope.itemNoLeft,"startDate":$scope.stateDateOpen,"endDate":$scope.endDateOpen},"problemsSolvingGrid");
    };



    $scope.searchOpen = function () {
        if ($scope.itemNoLeft == undefined) {
            $scope.itemNoLeft = "";
        }
        problemOutboundUnsolveManageGrid( {"state":"unsolved","userName":"","seek":$scope.itemNoLeft,"startDate":$scope.stateDateOpen,"endDate":$scope.endDateOpen},"problemsSolvingGrid");
    };



    var columnsRight = [
      {field: "obProblem.problemType", width: "80px", headerTemplate: "<span translate='问题类型'></span>",
        template: function (item) {
          var value = "";
          if (item.obProblem.problemType == "MORE") {
            value = "<span>多货</span>"
          } else {
            value = "<span>少货</span>"
          }
          return value;
          //return "<a ui-sref='main.problemOutboundManageDetail({id:dataItem.id,name:dataItem.storageLocation.name})'>" + value + "</a>";
        }
      },
      {field: "inboundProblemRule.description", headerTemplate: "<span translate='操作'></span>"},
      {field: "obProblem.itemData.itemNo", headerTemplate: "<span translate='SKU'></span>",
      template:function (item) {
              return item.obProblem.itemData ? item.obProblem.itemData.itemNo : item.obProblem.itemNo;
      }},
      {field: "obProblem.problemStoragelocation", headerTemplate: "<span translate='容器'></span>"},
      {field: "amount", headerTemplate: "<span translate='数量'></span>"},
      {field: "obProblem.reportBy", headerTemplate: "<span translate='操作人'></span>"},
      {
        field: "storageLocation.name", headerTemplate: "<span translate='货位名称'></span>", template: function (item) {
        return item.storageLocation ? item.storageLocation.name : "";
      }
      },
      {
        field: "obProblem.reportDate",
        headerTemplate: "<span translate='员工操作时间'></span>",
        template: function (item) {
          return item.obProblem? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.obProblem.reportDate)) : "";
        }
      },
      {field: "obProblem.solvedBy", headerTemplate: "<span translate='问题人员'></span>"},
      {field: "obProblem.rule", headerTemplate: "<span translate='备注信息'></span>"}];

     $scope.problemRecordsGridOptions = problemOutboundBaseService.grid("", columnsRight, $(document.body).height() - 300);
     problemOutboundManageGrid( {"state":"PROCESS","userName":"","seek":$scope.itemNoRight,"startDate":$scope.stateDateProcess,"endDate":$scope.endDateProcess},"problemRecordsGrid");


      $scope.searchGridRightKeyDown = function (e) {
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode == 13) {
              $scope.searchGridRight();
          }
      };

    $scope.searchGridRight = function () {
        if ($scope.itemNoRight == undefined) {
            $scope.itemNoRight = "";
        }
      problemOutboundManageGrid( {"state":"PROCESS","userName":"","seek":$scope.itemNoRight,"startDate":$scope.stateDateProcess,"endDate":$scope.endDateProcess},"problemRecordsGrid");
    };

    $scope.searchProcess = function () {
        if ($scope.itemNoRight == undefined) {
            $scope.itemNoRight = "";
        }
      problemOutboundManageGrid( {"state":"PROCESS","userName":"","seek":$scope.itemNoRight,"startDate":$scope.stateDateProcess,"endDate":$scope.endDateProcess},"problemRecordsGrid");
    };

    var columnsClose = [
      {field: "obProblem.problemType", width: "80px", headerTemplate: "<span translate='问题类型'></span>",
        template: function (item) {
          var value = "";
          if (item.obProblem.problemType == "MORE") {
            value = "<span>多货</span>"
          } else {
            value = "<span>少货</span>"
          }
          return value;
          return "<a ui-sref='main.problemOutboundManageDetail({id:dataItem.obProblem.id,name:dataItem.storageLocation.name})'>" + value + "</a>";
        }
      },
      {field: "inboundProblemRule.description", headerTemplate: "<span translate='操作'></span>",template:function(item){
         return item.inboundProblemRule? item.inboundProblemRule.description : "";
      }},
        {field: "obProblem.itemData.itemNo", headerTemplate: "<span translate='SKU'></span>",
            template: function (item) {
                return item.obProblem.itemData ? item.obProblem.itemData.itemNo : item.obProblem.itemNo;
            }},
      {field: "obProblem.problemStoragelocation", headerTemplate: "<span translate='容器'></span>"},
      {field: "amount", headerTemplate: "<span translate='数量'></span>"},
      {field: "obProblem.reportBy", headerTemplate: "<span translate='操作人'></span>"},
      {
        field: "storageLocation.name", headerTemplate: "<span translate='货位名称'></span>", template: function (item) {
        return item.storageLocation ? item.storageLocation.name : "";
      }
      },
      {
        field: "obProblem.reportDate",
        headerTemplate: "<span translate='员工操作时间'></span>",
        template: function (item) {
          return item.obProblem? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.obProblem.reportDate)) : "";
        }
      },
      {field: "obProblem.solvedBy", headerTemplate: "<span translate='问题人员'></span>"},
      {field: "obProblem.rule", headerTemplate: "<span translate='备注信息'></span>"}
    ];
   $scope.problemCloseGridOptions = problemOutboundBaseService.grid("", columnsClose, $(document.body).height() - 300);
   problemOutboundManageGrid( {"state":"ClOSE","userName":"","seek":$scope.itemNoClose,"startDate":$scope.stateDateClose,"endDate":$scope.endDateClose},"problemCloseGrid");

      $scope.searchGridCloseKeyDown = function (e) {
          var keycode = window.event ? e.keyCode : e.which;
          if (keycode == 13) {
              $scope.searchGridClose();
          }
      };

    $scope.searchGridClose = function () {
        if ($scope.itemNoClose == undefined) {
            $scope.itemNoClose = "";
        }
      problemOutboundManageGrid( {"state":"ClOSE","userName":"","seek":$scope.itemNoClose,"startDate":$scope.stateDateClose,"endDate":$scope.endDateClose},"problemCloseGrid");
    };

    $scope.searchClose = function () {
      problemOutboundManageGrid( {"state":"ClOSE","userName":"","seek":$scope.itemNoClose,"startDate":$scope.stateDateClose,"endDate":$scope.endDateClose},"problemCloseGrid");
    };

    if ($rootScope.problemManage == 2) {
      $scope.problemRecords();
    } else if ($rootScope.problemManage == 3) {
      $scope.problemCloses();
    }

  }).controller("problemOutboundManageDetailCtl", function ($scope, $state,  $stateParams, problemOutboundManageService, problemOutboundBaseService) {

    $scope.backProblemInbound = function () {
      $state.go("main.problem_Outbound_manage");
    };
    problemOutboundManageService.findOutboundProblem($stateParams.id,function(data){
      $scope.problemType = data.problemType;
      if ($scope.problemType == "MORE") {
        $scope.problemTypeValue = "多货";
      } else {
        $scope.problemTypeValue = "少货";
      }
      $scope.solveAmount = data.solveAmount;
      $scope.amount = data.amount - $scope.solveAmount;
      $scope.problemStoragelocation = data.problemStoragelocation;

      problemOutboundManageService.getGoodsInformation(data.id, function (data) {
        for (var k in data) $scope[k] = data[k];
      });

      var recordColumns = [
        {field: "storageLocation.name", width: 180, headerTemplate: "<span translate='上架货位历史'></span>",
          template: function (item) {
            setTimeout(function () {
              var grid = $("#adjustmentCargoSpaceGRID").data("kendoGrid");
              grid.tbody.find('tr').each(function () {
                if ($(this).find('td:first-child').text() == $stateParams.name) {
                  $(this).css("background", "#c5e0b4")
                }
              })
            }, 0);
            return  item.storageLocation.name;
          }
        },
        {field: "amount", headerTemplate: "<span translate='问题商品上架数量'></span>"},
        {field: "qaAmount", headerTemplate: "<span translate='货位问题商品剩余数量'></span>"},
        {field: "totalAmount", headerTemplate: "<span translate='货位商品总数'></span>"},
        {field: "client.name", headerTemplate: "<span translate='CLIENT'></span>",
          template: function (item) {
            return  item.client?item.client.name:"";
          }
        }
      ];
      problemOutboundManageService.getAndonInboundLocations({
        "anDonOutboundId": data.id
      }, function (datas) {
        $scope.adjustmentCargoSpaceGridOptions = problemOutboundBaseService.grid(datas, recordColumns, 240);
      });

    })
  })
})();

