/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("pickPackWallCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "pickPackWall";

    $rootScope.pickPackWallTypeSource =  masterService.getDataSource({key: "getPickPackWallType", text: "name", value: "id"});
    $rootScope.labelControllerSource =  masterService.getDataSource({key: "getLabelController", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.pickPackWallRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "pickPackWallType.name",template: "<a ui-sref='main.pick_pack_wall_type'>#: pickPackWallType.name # </a>", headerTemplate: "<span translate='PICK_PACK_WALL_TYPE'></span>"},
      {field: "numberOfRows", headerTemplate: "<span translate='NUMBER_OF_ROWS'></span>"},
      {field: "numberOfColumns", headerTemplate: "<span translate='NUMBER_OF_COLUMNS'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"},
      {
            field: "storageLocationType",
            width: 120,

            headerTemplate: "<span translate='STORAGE_LOCATION_TYPE'></span>",
            template: function (dataItem) {
                return (dataItem.storageLocationType ? "<a ui-sref='main.storage_location_type'>" + dataItem.storageLocationType.name + "</a>" : "");
            }
      },
        {
            field: "area",
            width: 120,
            headerTemplate: "<span translate='AREA'></span>",
            template: function (dataItem) {
                return (dataItem.area ? "<a ui-sref='main.area'>" + dataItem.area.name + "</a>" : "");
            }
        }
    ];
    $scope.pickPackWallGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("pickPackWall")});

  }).controller("pickPackWallCreateCtl", function ($scope, $state, masterService, pickPackWallService){
    // 转数组
    $scope.toItems = function(number){
      return new Array(number);
    };

    // 选择pickPackWallType
    $scope.changeWallType = function(pickPackWallType){
      masterService.read("pickPackWallType", pickPackWallType.id, function(data){
        var pickPackFieldTypes = data.pickPackFieldTypes;
        $scope.pickPackFieldTypes = pickPackFieldTypes;
        for(var i = 0, width = 0, fieldTypeIds = []; i < pickPackFieldTypes.length; i++){
          var fieldType = pickPackFieldTypes[i];
          fieldTypeIds.push(fieldType.id);
          width += 120*fieldType.numberOfColumns;
        }
        $scope.frontTableWidth = width;
        $scope.backTableWidth = width;
        $scope.fieldTypeIds = fieldTypeIds;
      });
    };

    // 选择标签控制器
    $scope.changeLabelController = function(labelController){
      if(!labelController.length){
        $scope.digitalLabelSource = [];
        return;
      }
      // 标签控制器
      for(var i = 0, ids = []; i < labelController.length; i++) ids.push(labelController[i].id);
      // 取所有电子标签
      pickPackWallService.getDigitalLabelByLabel(ids, function(data){
        $scope.digitalLabelSource = {data: data, sort: {field: "name", dir: "asc"}}; // 数据源
        // 设置电子标签事件
        $scope.selectItems = []; // 已选项
        $scope.pickPackFieldTypes.forEach(function(fieldType){
         console.log(fieldType.name,"input[class~=front"+ fieldType.name+ "][data-role=combobox]");
          // 电子标签正面
          $("input[class~=front"+ fieldType.name+ "][data-role=combobox]").each(function(){ $scope.changeDigital(this); });
          // 电子标签反面
          $("input[class~=back"+ fieldType.name+ "][data-role=combobox]").each(function(){ $scope.changeDigital(this); });
        });
      });
    };
    //容器类型
      $scope.storageLocationTypeDataSource = masterService.getDataSource({
          key: "getStorageLocationType",
          text: "name",
          value: "id"
      });
      //所在区域
      $scope.areaDataSource = masterService.getDataSource({key: "getArea", text: "name", value: "id"});
    // 数据源过滤
    $scope.filterSource = function(){
      for(var i=0, filters = []; i < $scope.selectItems.length; i++) filters.push({"field": "name", "operator": "neq", "value": $scope.selectItems[i]});
      //
      $scope.pickPackFieldTypes.forEach(function(fieldType){
        // 电子标签正面
        $("input[class~=front"+ fieldType.name+ "][data-role=combobox]").each(function(){ $(this).data("kendoComboBox").dataSource.filter(filters);});
        // 电子标签反面
        $("input[class~=back"+ fieldType.name+ "][data-role=combobox]").each(function(){ $(this).data("kendoComboBox").dataSource.filter(filters);});
      });
    };

    // 选择电子标签
    $scope.changeDigital = function(evt){
      var comboBox = $(evt).data("kendoComboBox");
      comboBox.bind("change", function(e){
        var lastText = $(this).attr("lastText"), text = this.text();
        if(lastText != "" && lastText != null){
          var idx = $scope.selectItems.indexOf(lastText);
          idx >= 0 && $scope.selectItems.splice(idx, 1);
        }
        $scope.selectItems.push(text);
        $(this).attr("lastText", text);
        $scope.filterSource(); // 过滤
      });
    };

    // 保存
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        // 电子标签
        var digitalLabel1 = [], digitalLabel2 = [];
        $scope.pickPackFieldTypes.forEach(function(fieldType){
          // 电子标签正面
          $("input[class*=front][data-role=combobox]").each(function(){
            var comboBox = $(this).data("kendoComboBox"), value = comboBox.value();
            digitalLabel1.push(value!=""? value: null);
          });
          // 电子标签反面
          $("input[class*=back][data-role=combobox]").each(function(){
            var comboBox = $(this).data("kendoComboBox"), value = comboBox.value();
            digitalLabel2.push(value!=""? value: null);
          });
        });
        // 保存
        masterService.create("pickPackWall", {
          "name": $scope.name,
          "numberOfRows": $scope.numberOfRows,
          "numberOfColumns": $scope.numberOfColumns,
          "typeId": $scope.pickPackWallType? $scope.pickPackWallType.id: null,
          "description": $scope.description,
          "digitalLabel1": digitalLabel1,
          "digitalLabel2": digitalLabel2,
          "pickPackFieldTypeNames": $scope.fieldTypeIds,
          "areaId":$scope.area.id,
          "storageLocationTypeId":$scope.storageLocationType ? $scope.storageLocationType.id : null
         }, function () {
          $state.go("main.pick_pack_wall");
        });
      }
    };
  }).controller("pickPackWallUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("pickPackWall", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("pickPackWall", {
          "id": $scope.id,
          "name": $scope.name,
          "numberOfRows": $scope.numberOfRows,
          "numberOfColumns": $scope.numberOfColumns,
          "typeId": $scope.pickPackWallType? $scope.pickPackWallType.id: null,
          "description": $scope.description
        }, function () {
          $state.go("main.pick_pack_wall");
        });
      }
    };
  }).controller("pickPackWallReadCtl", function ($scope, $stateParams, masterService){
    masterService.read("pickPackWall", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();