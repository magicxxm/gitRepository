/**
 * Created by thoma.bian on 2017/5/10.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("problemInboundManageCtl", function ($scope,$rootScope, problemInboundManageService, problemInboundBaseService) {

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
      $scope.searchGridLeft();
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
      $scope.searchGridRight();
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
      $scope.searchGridClose();
    };

    // 查询全部
    $scope.selectAll = function () {
      var grid = $('#problemsSolvingGRID').data('kendoGrid');
      if ($scope.select_all) {
        $scope.select_one = true;
        grid.tbody.children('tr').addClass('k-state-selected');
        //grid.select(grid.tbody.find(">tr"));
      } else {
        $scope.select_one = false;
        grid.tbody.children('tr').removeClass('k-state-selected');
      }
      console.log("选中条数------>:"+$('#problemsSolvingGRID').data('kendoGrid').select().length)
    };

    // 关闭选中的
    $scope.closeProblemsGrid = function () {
      var grid = $('#problemsSolvingGRID').data('kendoGrid');
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
      console.log("选中数据条数:---->"+dataFiled.length);
      problemInboundManageService.updateInboundProblemClose(dataFiled, "Closed", function () {
        $scope.searchGridLeft();
        $scope.select_all = false;
      })
    };

    $scope.chk = false;
    $scope.selectOne = function (val, uid) {
     var grid = $('#problemsSolvingGRID').data('kendoGrid');
      if (val) {
          grid.tbody.children('tr[data-uid="' + uid + '"]').addClass('k-state-selected');
        // grid.select("tr[data-uid='" + uid + "']");
      } else {
        grid.tbody.children('tr[data-uid="' + uid + '"]').removeClass('k-state-selected');
      }
    };
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
      {field: "problemStorageLocation", headerTemplate: "<span translate='容器'></span>"},
        {field: "amount", headerTemplate: "<span translate='数量'></span>",
            template: function (item) {
                return item.amount - item.solveAmount;
            }
        },
      // {field: "amount", headerTemplate: "<span translate='数量'></span>"},
      {field: "reportBy", headerTemplate: "<span translate='报问题人员'></span>"},
      {
        field: "createdDate", headerTemplate: "<span translate='员工操作时间'></span>", template: function (item) {
        return item.createdDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.createdDate)) : "";
      }
      },
      {field: "solveBy", headerTemplate: "<span translate='处理问题人员'></span>"},
      {field: "description", headerTemplate: "<span translate='备注信息'></span>"}];

    problemInboundManageService.getInboundProblem("OPEN", "", "", null, null, function (data) {
      $scope.problemsSolvingGridOptions = problemInboundBaseService.grid(data, columnsLeft, $(document.body).height() - 300);
    });

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
      problemInboundManageService.getInboundProblem("OPEN", "", $scope.itemNoLeft, null, null, function (data) {
        var problemsSolvingGrid = $("#problemsSolvingGRID").data("kendoGrid");
        problemsSolvingGrid.setDataSource(new kendo.data.DataSource({data: data}));
      });

    };

    $scope.searchOpen = function () {
      if ($scope.itemNoLeft == undefined) {
        $scope.itemNoLeft = "";
      }
      if ($scope.stateDateOpen == undefined || $scope.stateDateOpen == "") {
        $scope.stateDateOpen = null;
      }
      if ($scope.endDateOpen == undefined || $scope.endDateOpen == "") {
        $scope.endDateOpen = null;
      }
      problemInboundManageService.getInboundProblem("OPEN", "", $scope.itemNoLeft, $scope.stateDateOpen, $scope.endDateOpen, function (data) {
        var problemsSolvingGrid = $("#problemsSolvingGRID").data("kendoGrid");
        problemsSolvingGrid.setDataSource(new kendo.data.DataSource({data: data}));
      });
    };

    var columnsRight = [
      {field: "inboundProblem.problemType", width: "80px", headerTemplate: "<span translate='问题类型'></span>",
        template: function (item) {
         var value = "";
          if (item.inboundProblem.problemType == "MORE") {
            value = "<span>多货</span>"
          } else {
            value = "<span>少货</span>"
          }
          return  value ;
        }
      },
      {field: "inboundProblemRule.description", headerTemplate: "<span translate='操作'></span>"},
      {field: "inboundProblem.itemData.itemNo", headerTemplate: "<span translate='SKU'></span>",
        template: function (item) {
         return item.inboundProblem.itemData ? item.inboundProblem.itemData.itemNo : item.inboundProblem.itemNo;
      }},
      {field: "inboundProblem.problemStorageLocation", headerTemplate: "<span translate='容器'></span>"},
        // {field: "amount", headerTemplate: "<span translate='数量'></span>",
        //     template: function (item) {
        //         return item.amount - item.solveAmount;
        //     }
        // },
      {field: "amount", headerTemplate: "<span translate ='数量'></span>"},
      {field: "inboundProblem.reportBy", headerTemplate: "<span translate='报问题人员'></span>"},
      {
        field: "storageLocation.name", headerTemplate: "<span translate='货位名称'></span>", template: function (item) {
        return item.storageLocation ? item.storageLocation.name : "";
      }
      },
      {
          field: "inboundProblem.createdDate", headerTemplate: "<span translate='员工操作时间'></span>", template: function (item) {
          return item.inboundProblem.createdDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.inboundProblem.createdDate)) : "";
      }
      },
      {field: "inboundProblem.solveBy", headerTemplate: "<span translate='处理问题人员'></span>"},
      {field: "inboundProblem.description", headerTemplate: "<span translate='备注信息'></span>"}];

    problemInboundManageService.getInboundProblemRule("PROCESS", "", "", null, null, function (data) {
      $scope.problemRecordsGridOptions = problemInboundBaseService.grid(data, columnsRight, $(document.body).height() - 300);
    });

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
      problemInboundManageService.getInboundProblemRule("PROCESS", "", $scope.itemNoRight, null, null, function (data) {
        var problemRecordsGRID = $("#problemRecordsGRID").data("kendoGrid");
        problemRecordsGRID.setDataSource(new kendo.data.DataSource({data: data}));
      });
    };
    $scope.searchProcess = function () {
      if ($scope.itemNoRight == undefined) {
        $scope.itemNoRight = "";
      }
      if ($scope.stateDateProcess == undefined || $scope.stateDateProcess == "") {
        $scope.stateDateProcess = null;
      }
      if ($scope.endDateProcess == undefined || $scope.endDateProcess == "") {
        $scope.endDateProcess = null;
      }
      problemInboundManageService.getInboundProblemRule("PROCESS", "", $scope.itemNoRight, $scope.stateDateProcess, $scope.endDateProcess, function (data) {
        var problemRecordsGRID = $("#problemRecordsGRID").data("kendoGrid");
        problemRecordsGRID.setDataSource(new kendo.data.DataSource({data: data}));
      });
    };

    var columnsClose = [
      {field: "inboundProblem.problemType", width: "80px", headerTemplate: "<span translate='问题类型'></span>",
        template: function (item) {
          var value = "";
          if (item.inboundProblem.problemType == "MORE") {
            value = "<span>多货</span>"
          } else {
            value = "<span>少货</span>"
          }

          return "<a ui-sref='main.problemInboundManageDetail({id:dataItem.inboundProblem.id,name:dataItem.storageLocation.name})'>" + value + "</a>";
        }
      },
        {field: "inboundProblemRule.description", headerTemplate: "<span translate='操作'></span>",template:function(item){
            return item.inboundProblemRule? item.inboundProblemRule.description : "";
        }},
      {field: "inboundProblem.itemData.itemNo", headerTemplate: "<span translate='SKU'></span>",
        template: function (item) {
        return item.inboundProblem.itemData ? item.inboundProblem.itemData.itemNo : item.inboundProblem.itemNo;
      }},
      {field: "inboundProblem.problemStorageLocation", headerTemplate: "<span translate='容器'></span>"},
      // {field: "amount", headerTemplate: "<span translate='数量'></span>"},
        {field: "amount", headerTemplate: "<span translate='数量'></span>",
            template: function (item) {
                if(item.inboundProblemRule.name == "Closed"){
                    return item.inboundProblem.amount - item.inboundProblem.solveAmount;
                }else{
                    return item.amount;
                }
            }
        },
      {field: "inboundProblem.reportBy", headerTemplate: "<span translate='报问题人员'></span>"},
      {
        field: "storageLocation.name", headerTemplate: "<span translate='货位名称'></span>", template: function (item) {
        return item.storageLocation ? item.storageLocation.name : "";
      }
      },
      {
        field: "createdDate",
        headerTemplate: "<span translate='员工操作时间'></span>",
        template: function (item) {
          return item.createdDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.createdDate)) : "";
        }
      },
      {field: "inboundProblem.solveBy", headerTemplate: "<span translate='处理问题人员'></span>"},
      {field: "inboundProblem.description", headerTemplate: "<span translate='备注信息'></span>"}
    ];

    problemInboundManageService.getInboundProblemRule("ClOSE", "", "", null, null, function (data) {
      $scope.problemCloseGridOptions = problemInboundBaseService.grid(data, columnsClose, $(document.body).height() - 300);
    });

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
      problemInboundManageService.getInboundProblemRule("ClOSE", "", $scope.itemNoClose, null, null, function (data) {
        var problemCloseGRID = $("#problemCloseGRID").data("kendoGrid");
        problemCloseGRID.setDataSource(new kendo.data.DataSource({data: data}));
      });
    };

    $scope.searchClose = function () {
      if ($scope.itemNoRight == undefined) {
        $scope.itemNoRight = "";
      }
      if ($scope.stateDateClose == undefined || $scope.stateDateClose == "") {
        $scope.stateDateClose = null;
      }
      if ($scope.endDateClose == undefined || $scope.endDateClose == "") {
        $scope.endDateClose = null;
      }
      problemInboundManageService.getInboundProblemRule("ClOSE", "", $scope.itemNoRight, $scope.stateDateClose, $scope.endDateClose, function (data) {
        var problemCloseGRID = $("#problemCloseGRID").data("kendoGrid");
        problemCloseGRID.setDataSource(new kendo.data.DataSource({data: data}));
      });
    };

    if ($rootScope.problemManage == 2) {
      $scope.problemRecords();
    } else if ($rootScope.problemManage == 3) {
      $scope.problemCloses();
    }

  }).controller("problemInboundManageDetailCtl", function ($scope, $state,  $stateParams, problemInboundManageService, problemInboundBaseService) {

    $scope.backProblemInbound = function () {
      $state.go("main.inbound_problem_manage");
    };
    problemInboundManageService.findInboundProblem($stateParams.id,function(data){
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

      problemInboundManageService.getGoodsInformation({
        "inboundProblemId": data.id
      }, function (data) {
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
        {field: "problemAmount ", headerTemplate: "<span translate='货位问题商品当前数量'></span>"},
        {field: "storageLocationAmount ", headerTemplate: "<span translate='货位商品总数'></span>"},
        {field: "client", headerTemplate: "<span translate='CLIENT'></span>",
          template: function (item) {
            return  item.client?item.client:"";
          }
        }
      ];

      problemInboundManageService.getinboundProblemCheck({
        "inboundProblemId": data.id
      }, function (datas) {
        console.log(datas);
        $scope.adjustmentCargoSpaceGridOptions = problemInboundBaseService.grid(datas, recordColumns, 240);
      });

    })
  })
})();
