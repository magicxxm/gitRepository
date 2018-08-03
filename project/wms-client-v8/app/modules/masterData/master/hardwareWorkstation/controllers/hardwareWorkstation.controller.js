/**
 * Created by frank.zhou on 2017/05/26.
 */
(function(){
  "use strict";

  angular.module('myApp').controller("hardwareWorkstationCtl", function ($scope, commonService, masterService, hardwareWorkstationService){
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
    $scope.selectWorkstationOptions = getComboxOption({
      key: "getWorkstationList",
      change: function(){
        var workstationId = $scope.selectWorkstation.id;
        hardwareWorkstationService.getUnassignedHardwareByWorkstation(workstationId, function(unAssigned){
          getKendoList("listUnassignHardwares").setDataSource(new kendo.data.DataSource({data: unAssigned}));
        });
        hardwareWorkstationService.getAssignedHardwareByWorkstation(workstationId, function(assigned){
          getKendoList("listAssignHardwares").setDataSource(new kendo.data.DataSource({data: assigned}));
        });
      }
    });
    $scope.listAssignHardwares = masterService.getJsonForListView({data: [], template: null});
    $scope.listUnassignHardwares = masterService.getJsonForListView({data: [], template: null});

    // horizontal
    $scope.moveLeftAll = function(leftId, rightId){
      leftId == null && (leftId = "listUnassignHardwares");
      rightId == null && (rightId = "listAssignHardwares");
      var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
      for(var i = 0, items = listRight.dataItems(), len = items.length; i < len; i++){
        listRight.dataSource.remove(items[i]);
        listLeft.dataSource.insert(0, items[i]);
      }
      select(listLeft, len);
    };
    $scope.moveLeftSelect = function(leftId, rightId){
      leftId == null && (leftId = "listUnassignHardwares");
      rightId == null && (rightId = "listAssignHardwares");
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
      leftId == null && (leftId = "listUnassignHardwares");
      rightId == null && (rightId = "listAssignHardwares");
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
      leftId == null && (leftId = "listUnassignHardwares");
      rightId == null && (rightId = "listAssignHardwares");
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
      hardwareWorkstationService.save(requestKey, inputObj.id, items, function(){
        commonService.dialogMushiny($scope.window, {type: "success"});
      });
    };
  });
})();