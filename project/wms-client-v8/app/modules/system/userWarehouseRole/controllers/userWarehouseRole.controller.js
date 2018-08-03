/**
 * Created by frank.zhou on 2017/04/18.
 */
(function(){
  "use strict";

  angular.module('myApp').controller("userWarehouseRoleCtl", function ($scope, commonService, systemService, userWarehouseRoleService){
    // ==========================================================================================================
    function getKendoList(id){
      return $("#"+ id).data("kendoListView");
    }

    function select(list, length){
      var selectItems = list.element.children().filter(function(idx){ return idx < length;});
      list.select(selectItems);
    }

    function getComboxOption(options){
      return {
        dataSource: options.key? systemService.getDataSource({key: options.key, value: "id", text: "name"}): [],
        dataTextField: "name",
        dataValueField: "id",
        filter: "contains",
        change: function(){
          options.change && options.change();
        }
      };
    }

    // input
    $scope.filterInput = function(event, id, value){
      var keycode = window.event? event.keyCode: event.which; // 获取按键编码
      if(keycode === 13){
        var listView = getKendoList(id);
        listView.dataSource.filter({field: "name", operator: "contains", value: value});
      }
    };

    // ==========================================================================================================
    $scope.listAssignRoles = systemService.getJsonForListView({data: []});
    $scope.listUnassignRoles = systemService.getJsonForListView({data: []});
    $scope.selectUserOptions = getComboxOption({
      change: function(){
        var warehouseId = $scope.selectWarehouseUp.id, userId = $scope.selectUserByWarehouse.id;
        userWarehouseRoleService.getUnassignedRoleByWarehouseUser(warehouseId, userId, function(unAssigned){
          getKendoList("listUnassignRoles").setDataSource(new kendo.data.DataSource({data: unAssigned}));
        });
        userWarehouseRoleService.getAssignedRoleByWarehouseUser(warehouseId, userId, function(assigned){
          getKendoList("listAssignRoles").setDataSource(new kendo.data.DataSource({data: assigned}));
        });
      }
    });
    $scope.selectWarehouseUpOptions = getComboxOption({
      key: "getWarehouseInRole",
      change: function(){
        var warehouseId = $scope.selectWarehouseUp.id;
        userWarehouseRoleService.getUserByWarehouseInRole(warehouseId, function(users){
          var userComboBox = $("#selectUserByWarehouse").data("kendoComboBox");
          userComboBox.value("");
          userComboBox.setDataSource(new kendo.data.DataSource({data: users}));
        });
      }
    });

    //
    $scope.listAssignUsers = systemService.getJsonForListView({data: [], template: null/*"#:userGroup.name#,#:name#"*/});
    $scope.listUnassignUsers = systemService.getJsonForListView({data: [], template: null/*"#:userGroup?userGroup.name:''#,#:name#"*/});
    $scope.selectRoleOptions = getComboxOption({
      change: function() {
        var warehouseId = $scope.selectWarehouseDown.id, roleId = $scope.selectRoleByWarehouse.id;
        userWarehouseRoleService.getUnassignedUserByWarehouseRole(warehouseId, roleId, function (unAssigned) {
          getKendoList("listUnassignUsers").setDataSource(new kendo.data.DataSource({data: unAssigned}));
        });
        userWarehouseRoleService.getAssignedUserByWarehouseRole(warehouseId, roleId, function (assigned) {
          getKendoList("listAssignUsers").setDataSource(new kendo.data.DataSource({data: assigned}));
        });
      }
    });
    $scope.selectWarehouseDownOptions = getComboxOption({
      key: "getWarehouseInRole",
      change: function () {
        var warehouseId = $scope.selectWarehouseDown.id;
        userWarehouseRoleService.getRoleByWarehouse(warehouseId, function(roles){
          var roleComboBox = $("#selectRoleByWarehouse").data("kendoComboBox");
          roleComboBox.value("");
          roleComboBox.setDataSource(new kendo.data.DataSource({data: roles}));
        });
      }
    });

    // horizontal
    $scope.moveLeftAll = function(leftId, rightId){
      leftId == null && (leftId = "listUnassignRoles");
      rightId == null && (rightId = "listAssignRoles");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      for(var i = 0, items = listRight.dataItems(), len = items.length; i < len; i++){
        listRight.dataSource.remove(items[i]);
        listLeft.dataSource.insert(0, items[i]);
      }
      select(listLeft, len);
    };
    $scope.moveLeftSelect = function(leftId, rightId){
      leftId == null && (leftId = "listUnassignRoles");
      rightId == null && (rightId = "listAssignRoles");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      var selections = listRight.select(), len = selections.length;
      for(var i = 0; i < len; i++){
        var item = listRight.dataItem(selections[i]);
        listRight.dataSource.remove(item);
        listLeft.dataSource.insert(0, item);
      }
      select(listLeft, len);
    };
    $scope.moveRightSelect = function(leftId, rightId){
      leftId == null && (leftId = "listUnassignRoles");
      rightId == null && (rightId = "listAssignRoles");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      var selections = listLeft.select(), len = selections.length;
      for(var i = 0; i < len; i++){
        var item = listLeft.dataItem(selections[i]);
        listLeft.dataSource.remove(item);
        listRight.dataSource.insert(0, item);
      }
      select(listRight, len);
    };
    $scope.moveRightAll = function(leftId, rightId){
      leftId == null && (leftId = "listUnassignRoles");
      rightId == null && (rightId = "listAssignRoles");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      for(var i = 0, items = listLeft.dataItems(), len = items.length; i < len; i++){
        listLeft.dataSource.remove(items[i]);
        listRight.dataSource.insert(0, items[i]);
      }
      select(listRight, len);
    };

    // 保存
    $scope.save = function(requestKey, listViewId, firstObj, secondObj){
      var listView = getKendoList(listViewId), listItems = listView.dataItems();
      for(var i = 0, items = []; i < listItems.length; i++) items.push(listItems[i].id);
      userWarehouseRoleService[requestKey](firstObj.id, secondObj.id, items, function(){
        commonService.dialogMushiny($scope.window, {type: "success"});
      });
    };
  });
})();