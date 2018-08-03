/**
 * Created by frank.zhou on 2017/04/18.
 */
(function(){
  "use strict";

  angular.module('myApp').controller("menuCtl", function ($scope, commonService, systemService, menuService){
    // ==========================================================================================================
    function getKendoList(id){
      return $("#"+ id).data("kendoListView");
    }

    function select(list, length){
      var selectItems = list.element.children().filter(function(idx){ return idx < length;});
      list.select(selectItems);
    }

    // ==========================================================================================================
    // root
    menuService.getRootModules(function(datas){
      $scope.listMenus = systemService.getJsonForListView({
        datas: datas,
        selectable: "single",
        change: function(){
          var rootList = getKendoList("listMenus"), curItem = rootList.dataItem(rootList.select()[0]);
          // assignModules
          menuService.getAssignedModules(curItem.id, function(assigned){
            getKendoList("listAssignModules").setDataSource(new kendo.data.DataSource({data: assigned}));
          });
          // unassignedModules
          menuService.getUnassignedModules(curItem.id, function(unassigned){
            getKendoList("listUnassignModules").setDataSource(new kendo.data.DataSource({data: unassigned}));
          });
        }
      });
    });
    // assigned
    $scope.listAssignModules = systemService.getJsonForListView({data: []});
    // unassigned
    $scope.listUnassignModules = systemService.getJsonForListView({data: []});

    // input
    $scope.filterInput = function(event, id, value){
      var keycode = window.event? event.keyCode: event.which; // 获取按键编码
      if(keycode === 13){
        var listView = getKendoList(id);
        listView.dataSource.filter({field: "name", operator: "contains", value: value});
      }
    };
    // vertical
    $scope.moveFirst = function(){
      var listLeft = getKendoList("listAssignModules"), selections = listLeft.select();
      var item = listLeft.dataItem(selections[0]);
      listLeft.dataSource.remove(item);
      listLeft.dataSource.insert(0, item);
      listLeft.select(listLeft.element.children().first());
    };
    $scope.moveUp = function(){
      var listLeft = getKendoList("listAssignModules"), selections = listLeft.select();
      var item = listLeft.dataItem(selections[0]), itemIdx = listLeft.dataSource.indexOf(item);
      if(itemIdx > 0){
        listLeft.dataSource.remove(item);
        listLeft.dataSource.insert(itemIdx-1, item);
        listLeft.select(listLeft.element.children().eq(itemIdx-1));
      }
    };
    $scope.moveDown = function(){
      var listLeft = getKendoList("listAssignModules"), selections = listLeft.select();
      var item = listLeft.dataItem(selections[0]), itemIdx = listLeft.dataSource.indexOf(item);
      if(itemIdx < listLeft.dataItems().length- 1){
        listLeft.dataSource.remove(item);
        listLeft.dataSource.insert(itemIdx+1, item);
        listLeft.select(listLeft.element.children().eq(itemIdx+1));
      }
    };
    $scope.moveLast = function(){
      var listLeft = getKendoList("listAssignModules"), selections = listLeft.select();
      var item = listLeft.dataItem(selections[0]);
      listLeft.dataSource.remove(item);
      listLeft.dataSource.add(item);
      listLeft.select(listLeft.element.children().last());
    };
    // horizontal
    $scope.moveLeftAll = function(){
      var listLeft = getKendoList("listAssignModules"), listRight = getKendoList("listUnassignModules");
      for(var i = 0, items = listRight.dataItems(), len = items.length; i < len; i++){
        listRight.dataSource.remove(items[i]);
        listLeft.dataSource.insert(0, items[i]);
      }
      select(listLeft, len);
    };
    $scope.moveLeftSelect = function(){
      var listLeft = getKendoList("listAssignModules"), listRight = getKendoList("listUnassignModules");
      var selections = listRight.select(), len = selections.length;
      for(var i = 0; i < len; i++){
        var item = listRight.dataItem(selections[i]);
        listRight.dataSource.remove(item);
        listLeft.dataSource.insert(0, item);
      }
      select(listLeft, len);
    };
    $scope.moveRightSelect = function(){
      var listLeft = getKendoList("listAssignModules"), listRight = getKendoList("listUnassignModules");
      var selections = listLeft.select(), len = selections.length;
      for(var i = 0; i < len; i++){
        var item = listLeft.dataItem(selections[i]);
        listLeft.dataSource.remove(item);
        listRight.dataSource.insert(0, item);
      }
      select(listRight, len);
    };
    $scope.moveRightAll = function(){
      var listLeft = getKendoList("listAssignModules"), listRight = getKendoList("listUnassignModules");
      for(var i = 0, items = listLeft.dataItems(), len = items.length; i < len; i++){
        listLeft.dataSource.remove(items[i]);
        listRight.dataSource.insert(0, items[i]);
      }
      select(listRight, len);
    };
    // 保存
    $scope.save = function(){
      var rootList = getKendoList("listMenus"), curItem = rootList.dataItem(rootList.select()[0]);
      var listLeft = getKendoList("listAssignModules"), listLeftItems = listLeft.dataItems();
      for(var i = 0, items = []; i < listLeftItems.length; i++){
        var item = listLeftItems[i];
        items.push({parentId: curItem.id, childId: item.id, orderIndex: i});
      }
      menuService.saveAssignedModules(curItem.id, items, function(){
        commonService.dialogMushiny($scope.window, {type: "success"});
      });
    };
  });
})();