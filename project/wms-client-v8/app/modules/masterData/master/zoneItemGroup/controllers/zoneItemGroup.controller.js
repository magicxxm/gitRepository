/**
 * Created by frank.zhou on 2017/04/21.
 */
(function(){
  "use strict";

  angular.module('myApp').controller("zoneItemGroupCtl", function ($scope, commonService, masterService, zoneItemGroupService){
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
        dataSource: options.key? masterService.getDataSource({key: options.key, value: "id", text: "name"}): [],
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
    $scope.selectClientOptions = getComboxOption({
      key: "getClient",
      change: function () {
        var clientId = $scope.client.id;
        zoneItemGroupService.getZonesByClient(clientId, function(zones){
          getKendoList("listUnassignItemGroups").setDataSource(new kendo.data.DataSource({data: []}));
          getKendoList("listAssignItemGroups").setDataSource(new kendo.data.DataSource({data: []}));
          var zoneComboBox = $("#selectZoneByClient").data("kendoComboBox");
          zoneComboBox.value("");
          zoneComboBox.setDataSource(new kendo.data.DataSource({data: zones}));
        });
      }
    });
    $scope.selectZoneOptions = getComboxOption({
      change: function(){
        var zoneId = $scope.selectZone.id;
        zoneItemGroupService.getUnassignedItemGroupByZone(zoneId, function(unAssigned){
          getKendoList("listUnassignItemGroups").setDataSource(new kendo.data.DataSource({data: unAssigned}));
        });
        zoneItemGroupService.getAssignedItemGroupByZone(zoneId, function(assigned){
          getKendoList("listAssignItemGroups").setDataSource(new kendo.data.DataSource({data: assigned}));
        });
      }
    });
    //$scope.selectItemGroupOptions = getComboxOption({
    //  key: "getSelectItemGroup", change: function(){
    //    var itemGroupId = $scope.selectItemGroup.id;
    //    zoneItemGroupService.getUnassignedZoneByItemGroup(itemGroupId, function(unAssigned){
    //      getKendoList("listUnassignZones").setDataSource(new kendo.data.DataSource({data: unAssigned}));
    //    });
    //    zoneItemGroupService.getAssignedZoneByItemGroup(itemGroupId, function(assigned){
    //      getKendoList("listAssignZones").setDataSource(new kendo.data.DataSource({data: assigned}));
    //    });
    //  }
    //});
    // assigned
    //$scope.listAssignZones = commonService.getJsonForListView({data: []});
    $scope.listAssignItemGroups = masterService.getJsonForListView({data: [], template: null});
    // unassigned
    //$scope.listUnassignZones = commonService.getJsonForListView({data: []});
    $scope.listUnassignItemGroups = masterService.getJsonForListView({data: [], template: null});

    // horizontal
    $scope.moveLeftAll = function(leftId, rightId){
      leftId == null && (leftId = "listUnassignItemGroups");
      rightId == null && (rightId = "listAssignItemGroups");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      for(var i = 0, items = listRight.dataItems(), len = items.length; i < len; i++){
        listRight.dataSource.remove(items[i]);
        listLeft.dataSource.insert(0, items[i]);
      }
      select(listLeft, len);
    };
    $scope.moveLeftSelect = function(leftId, rightId){
      leftId == null && (leftId = "listUnassignItemGroups");
      rightId == null && (rightId = "listAssignItemGroups");
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
      leftId == null && (leftId = "listUnassignItemGroups");
      rightId == null && (rightId = "listAssignItemGroups");
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
      leftId == null && (leftId = "listUnassignItemGroups");
      rightId == null && (rightId = "listAssignItemGroups");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      for(var i = 0, items = listLeft.dataItems(), len = items.length; i < len; i++){
        listLeft.dataSource.remove(items[i]);
        listRight.dataSource.insert(0, items[i]);
      }
      select(listRight, len);
    };

    // 保存
    $scope.save = function(inputObj, listViewId, requestKey){
      var listView = getKendoList(listViewId), listItems = listView.dataItems();
      for(var i = 0, items = []; i < listItems.length; i++) items.push(listItems[i].id);
      zoneItemGroupService.save(requestKey, inputObj.id, items, function(){
        commonService.dialogMushiny($scope.window, {type: "success"});
      });
    };
  });
})();