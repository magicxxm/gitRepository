/**
 * Created by HGF on 2017/6/30.
 */
(function () {
  "use strict";

  angular.module('myApp').controller("pickingAreaAndPPEligibilityCtl", function ($scope, commonService, masterService, pickingAreaAndPPEligibilityService) {
    // ==========================================================================================================
    $scope.useId;
    function getKendoList(id) {
      return $("#" + id).data("kendoListView");
    }

    function select(list, length) {
      var selectItems = list.element.children().filter(function (idx) {
        return idx < length;
      });
      list.select(selectItems);
    }

    function getComboxOption(options) {
      return {
        dataSource: options.key ? masterService.getDataSource({key: options.key, value: "id", text: "name"}) : [],
        dataTextField: "name",
        dataValueField: "id",
        filter: "contains",
        change: function () {
          options.change && options.change();
        }
      };
    }

    // input
    $scope.filterInput = function (event, id, value) {
      var keycode = window.event ? event.keyCode : event.which; // 获取按键编码
      if (keycode === 13) {
        var listView = getKendoList(id);
        listView.dataSource.filter({field: "name", operator: "contains", value: value});
      }
    };

    // ==========================================================================================================
    //获取userGroup；
    $scope.selectUseGroupOptions = getComboxOption({
      key: "getUserGroup",
      change: function () {
        var useGroupId = $scope.selectUseGroup.id;
        //根据userGroupId获取对应的use
        pickingAreaAndPPEligibilityService.getUseByUserGroupId(useGroupId, function (useData) {
          var EmployeeComboBox = $("#selectEmployee").data("kendoComboBox");
          var clientComboBox = $("#selectClient").data("kendoComboBox");
          EmployeeComboBox.value("");
          clientComboBox.value("");
          //设置员工
          EmployeeComboBox.setDataSource(new kendo.data.DataSource({data: useData}));
          //设置PP的默认值为空
          getKendoList("listUnassignPP").setDataSource(new kendo.data.DataSource({data: []}));
          getKendoList("listAssignPP").setDataSource(new kendo.data.DataSource({data: []}));
          getKendoList("listUnassignArea").setDataSource(new kendo.data.DataSource({data: []}));
          getKendoList("listAssignArea").setDataSource(new kendo.data.DataSource({data: []}));
        });
      }
    });
    //选择员工
    $scope.selectEmployeeOptions = getComboxOption({
      change: function () {
        var userId = $scope.selectEmployee.id;
        //根据员工的id获取pp的值
        pickingAreaAndPPEligibilityService.getUnassignedPPByUser(userId, function (unAssigned) {
          getKendoList("listUnassignPP").setDataSource(new kendo.data.DataSource({data: unAssigned}));
        });
        pickingAreaAndPPEligibilityService.getAssignedPPByUser(userId, function (assigned) {
          getKendoList("listAssignPP").setDataSource(new kendo.data.DataSource({data: assigned}));
        });
      }
    });
    //设置ng-model的值
    $scope.listAssignPP = masterService.getJsonForListView({data: [], template: null/*"#:userGroup.name#,#:name#"*/});
    $scope.listUnassignPP = masterService.getJsonForListView({data: [], template: null/*"#:userGroup?userGroup.name:''#,#:name#"*/});
    //获取客户 有员工的情况下才能获取区域
    $scope.selectClientOptions = getComboxOption({
      key: "getClient",
      change: function () {
        var clientId = $scope.selectClient.id;
        var userId= $scope.selectEmployee?$scope.selectEmployee.id:"";
        debugger;
        if (userId==="") {return;}else{
          pickingAreaAndPPEligibilityService.getUnassignedAreaByClient(userId, clientId, function (AreaNo) {
            getKendoList("listUnassignArea").setDataSource(new kendo.data.DataSource({data: AreaNo}));
          });
          pickingAreaAndPPEligibilityService.getAssignedAreaByClient(userId, clientId, "y", function (Area) {
            getKendoList("listAssignArea").setDataSource(new kendo.data.DataSource({data: Area}));
          });
        }
      }
    });
    //设置ng-model的值
    $scope.listAssignArea = masterService.getJsonForListView({data: [], template: null/*"#:userGroup.name#,#:name#"*/});
    $scope.listUnassignArea = masterService.getJsonForListView({
      data: [],
      template: null/*"#:userGroup?userGroup.name:''#,#:name#"*/
    });

    // horizontal  pp
    $scope.moveLeftAll = function (leftId, rightId) {
      leftId == null && (leftId = "listUnassignPP");
      rightId == null && (rightId = "listAssignPP");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      for (var i = 0, items = listRight.dataItems(), len = items.length; i < len; i++) {
        listRight.dataSource.remove(items[i]);
        listLeft.dataSource.insert(0, items[i]);
      }
      select(listLeft, len);
    };
    $scope.moveLeftSelect = function (leftId, rightId) {
      leftId == null && (leftId = "listUnassignPP");
      rightId == null && (rightId = "listAssignPP");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      var selections = listRight.select(), len = selections.length;
      for (var i = 0; i < len; i++) {
        var item = listRight.dataItem(selections[i]);
        listRight.dataSource.remove(item);
        listLeft.dataSource.insert(0, item);
      }
      select(listLeft, len);
    };
    $scope.moveRightSelect = function (leftId, rightId) {
      leftId == null && (leftId = "listUnassignPP");
      rightId == null && (rightId = "listAssignPP");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      var selections = listLeft.select(), len = selections.length;
      for (var i = 0; i < len; i++) {
        var item = listLeft.dataItem(selections[i]);
        listLeft.dataSource.remove(item);
        listRight.dataSource.insert(0, item);
      }
      select(listRight, len);
    };

    $scope.moveRightAll = function (leftId, rightId) {
      leftId == null && (leftId = "listUnassignPP");
      rightId == null && (rightId = "listAssignPP");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      for (var i = 0, items = listLeft.dataItems(), len = items.length; i < len; i++) {
        listLeft.dataSource.remove(items[i]);
        listRight.dataSource.insert(0, items[i]);
      }
      select(listRight, len);
    };
    // horizontal  Area
    $scope.moveLeftAll1 = function (leftId, rightId) {
      leftId == null && (leftId = "listUnassignArea");
      rightId == null && (rightId = "listAssignArea");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      for (var i = 0, items = listRight.dataItems(), len = items.length; i < len; i++) {
        listRight.dataSource.remove(items[i]);
        listLeft.dataSource.insert(0, items[i]);
      }
      select(listLeft, len);
    };
    $scope.moveLeftSelect1 = function (leftId, rightId) {
      leftId == null && (leftId = "listUnassignArea");
      rightId == null && (rightId = "listAssignArea");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      var selections = listRight.select(), len = selections.length;
      for (var i = 0; i < len; i++) {
        var item = listRight.dataItem(selections[i]);
        listRight.dataSource.remove(item);
        listLeft.dataSource.insert(0, item);
      }
      select(listLeft, len);
    };
    $scope.moveRightSelect1 = function (leftId, rightId) {
      leftId == null && (leftId = "listUnassignArea");
      rightId == null && (rightId = "listAssignArea");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      var selections = listLeft.select(), len = selections.length;
      for (var i = 0; i < len; i++) {
        var item = listLeft.dataItem(selections[i]);
        listLeft.dataSource.remove(item);
        listRight.dataSource.insert(0, item);
      }
      select(listRight, len);
    };
    $scope.moveRightAll1 = function (leftId, rightId) {
      leftId == null && (leftId = "listUnassignArea");
      rightId == null && (rightId = "listAssignArea");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      for (var i = 0, items = listLeft.dataItems(), len = items.length; i < len; i++) {
        listLeft.dataSource.remove(items[i]);
        listRight.dataSource.insert(0, items[i]);
      }
      select(listRight, len);
    };
    // 保存
    $scope.save = function (user) {
      var listViewPP = getKendoList("listAssignPP"), listItemsPP = listViewPP.dataItems();
      for (var i = 0, itemsPP = []; i < listItemsPP.length; i++) itemsPP.push(listItemsPP[i].id);
      var listViewArea = getKendoList("listAssignArea"), listItemsArea = listViewArea.dataItems();
      for (var i = 0, itemsArea = []; i < listItemsArea.length; i++) itemsArea.push(listItemsArea[i].id);
      pickingAreaAndPPEligibilityService.save("savePPToUser", user.id, itemsPP, function () {
        pickingAreaAndPPEligibilityService.save("saveAreaToUser", user.id, itemsArea, function () {
          commonService.dialogMushiny($scope.window, {type: "success"});
        });
      });
    };
  });
})();