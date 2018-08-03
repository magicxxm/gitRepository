/**
 * Created by frank.zhou on 2017/05/05.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebinWallTypeCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "rebinWallType";

    $rootScope.rebinCellTypeSource = masterService.getDataSource({key: "getRebinCellType", text: "name", value: "id"});

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.rebinWallTypeRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        width: 350, headerTemplate: "<span translate='REBIN_CELL_TYPE'></span>", template: function (item) {
        var rebinCellTypes = item.rebinCellTypes || [];
        for (var i = 0, datas = []; i < rebinCellTypes.length; i = i + 2) {
          var rebinCellType = rebinCellTypes[i], next = rebinCellTypes[i + 1];
          var htmlStr = "<div style='margin:1px;'>";
          rebinCellType && (htmlStr += "<div class='gridCellList'>" + rebinCellType.name + "</div>");
          next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>" + next.name + "</div>");
          htmlStr += "</div>";
          datas.push(htmlStr);
        }
        return datas.join("");
      }
      },
      {field: "numberOfRows", headerTemplate: "<span translate='NUMBER_OF_ROWS'></span>"},
      {field: "numberOfColumns", headerTemplate: "<span translate='NUMBER_OF_COLUMNS'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.rebinWallTypeGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("rebinWallType")
    });
    // 创建position
    $rootScope.createTable = function (rows, columns, isRead) {
      isRead == null && (isRead = false);
      for (var i = rows, htmls = []; i > 0; i--) {
        var pos = masterService.transNumToStr(i - 1); // A、B、C...
        htmls.push("<tr height='30'>");
        for (var j = 1; j <= columns; j++) {
          var id = pos + "_" + j + "_" + i;
          htmls.push("<td>");
          htmls.push("<input id='" + id + "' min='0' type='number' class='k-textbox wallTypePosition' style='width:100%;margin-left:0px;'" + (isRead ? " readonly" : "") + " />");
          htmls.push("</td>");
        }
        htmls.push("</tr>");
      }
      $("#rebinWallTypePosition").html(htmls.join(""));
      //绑定明细input的改变事件
      $("input[class~=wallTypePosition]").bind("change", function (e) {
        $scope.cell=$(this);//当前的格子
        var text=$scope.cell.val();//当前填写的数
        var number = [];//明细中已经填写的数据
        $("input[class~=wallTypePosition]").each(function () {
          var text1 = $(this).val();//每个格子填写的数
          if (text1 === null || text1 === "") {
          } else {
            number.push(text1);
          }
          var flag=0;
          for(var i=0;i<number.length;i++){
            if(number[i]===text){
              flag++;
              if(flag===2){
                $scope.cell.val("");
              }
            }
          }
        });
      });
    };

  }).controller("rebinWallTypeCreateCtl", function ($scope, $state, commonService, masterService) {
    // 取position
    $scope.getPosition = function () {
      var positions = [];
      $("input[class~=wallTypePosition]").each(function () {
        var id = $(this).attr("id"), ids = id.split("_");
        var pos = ids[0], x = ids[1], y = ids[2];
        positions.push({
          "name": pos + masterService.pad(x),
          "xPos": parseInt(x),
          "yPos": parseInt(y),
          "zPos": 1,
          "orderIndex": parseInt($(this).val()),
          "rebinCellTypeId": $scope.rebinCellType ? $scope.rebinCellType.id : null
        });

      });
      return positions;
    };

    // 保存
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("rebinWallType", {
          "name": $scope.name,
          "numberOfRows": $scope.numberOfRows,
          "numberOfColumns": $scope.numberOfColumns,
          "description": $scope.description,
          "positions": $scope.getPosition()
        }, function () {
          $state.go("main.rebin_wall_type");
        });
      }
    };
  }).controller("rebinWallTypeUpdateCtl", function ($scope, $rootScope, $state, $stateParams, commonService, masterService) {
    masterService.read("rebinWallType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      $scope.rebinCellType = data.positions[0].rebinCellType;
      $rootScope.createTable(data.numberOfRows, data.numberOfColumns); // 创建表格
      for (var i = 0, positions = data.positions; i < positions.length; i++) {
        var pos = positions[i], x = pos.xPos, y = pos.yPos;
        var id = masterService.transNumToStr(y - 1) + "_" + x + "_" + y;
        $("#" + id).val(pos.orderIndex);
      }
    });

    // 取position
    $scope.getPosition = function () {
      var positions = [];
      $("input[class~=wallTypePosition]").each(function () {
        var id = $(this).attr("id"), ids = id.split("_");
        var pos = ids[0], x = ids[1], y = ids[2];
        positions.push({
          "name": pos + masterService.pad(x),
          "xPos": parseInt(x),
          "yPos": parseInt(y),
          "zPos": 1,
          "orderIndex": parseInt($(this).val()),
          "rebinCellTypeId": $scope.rebinCellType ? $scope.rebinCellType.id : null
        });
      });
      return positions;
    };

    // 修改
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("rebinWallType", {
          "id": $scope.id,
          "name": $scope.name,
          "numberOfRows": $scope.numberOfRows,
          "numberOfColumns": $scope.numberOfColumns,
          "description": $scope.description,
          "positions": $scope.getPosition()
        }, function () {
          $state.go("main.rebin_wall_type");
        });
      }
    };
  }).controller("rebinWallTypeReadCtl", function ($scope, $rootScope, $stateParams, masterService) {
    masterService.read("rebinWallType", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
      $rootScope.createTable(data.numberOfRows, data.numberOfColumns, true); // 创建表格
      for (var i = 0, positions = data.positions; i < positions.length; i++) {
        var pos = positions[i], x = pos.xPos, y = pos.yPos;
        var id = masterService.transNumToStr(y - 1) + "_" + x + "_" + y;
        $("#" + id).val(pos.orderIndex);
      }
    });
  });
})();