/**
 * Created by frank.zhou on 2017/04/21.
 * Updated by frank.zhou on 2017/04/27.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("itemDataGlobalCtl", function ($scope, $http, $window, $rootScope, $state, Excel, MASTER_CONSTANT, itemDataGlobalService, commonService, masterService) {

    $window.localStorage["currentItem"] = "itemDataGlobal";
    $scope.trueOfFalse = ["true", "false"];
    $rootScope.sizeFilterRuleData;
    $rootScope.size1;
    var isTrueSide;
    var isTrueVolume;
    var isTrueWeight;
    $rootScope.itemGroupSource = masterService.getDataSource({key: "getItemGroup", text: "name", value: "id"});
    $rootScope.handingUnitSource = masterService.getDataSource({key: "getItemUnit", text: "name", value: "id"});
    $rootScope.serialRecordTypeSource = masterService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "SERIAL_RECORD"}
    });
    $rootScope.lotTypeSource = masterService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "LOT_TYPE"}
    });
    $rootScope.lotUnitSource = masterService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "LOT_UNIT"}
    });
    /***************************首页表格********************************************************************/
    $rootScope.columns = [
      {
        field: "itemNo",
        width: 140,
        template: "<a ui-sref='main.itemDataGlobalRead({id:dataItem.id})'>#: itemNo # </a>",
        headerTemplate: "<span translate='ITEM_NO'></span>"
      },
      {field: "skuNo", width: 140, headerTemplate: "<span translate='SKU_NO'></span>"},
      {field: "name", width: 180, headerTemplate: "<span translate='NAME'></span>"},
      //{field: "description", width:100, headerTemplate: "<span translate='DESCRIPTION'></span>"},
      {field: "safetyStock", width: 100, headerTemplate: "<span translate='SAFETY_STOCK'></span>"},
      {
        field: "itemGroup.name",
        width: 100,
        template: "<a ui-sref='main.item_group'>#: itemGroup.name # </a>",
        headerTemplate: "<span translate='ITEM_GROUP'></span>"
      },

      {field: "lotMandatory", width: 70, headerTemplate: "<span translate='LOT_MANDATORY'></span>"},
      //{field: "lotType", width:70, headerTemplate: "<span translate='LOT_TYPE'></span>"},
      //{field: "lotUnit", width:70, headerTemplate: "<span translate='LOT_UNIT'></span>"},
      {field: "lotThreshold", width: 65, headerTemplate: "<span translate='LOT_THRESHOLD'></span>"},
      {
        field: "itemUnit.name",
        width: 100,
        template: "<a ui-sref='main.item_unit'>#: itemUnit.name # </a>",
        headerTemplate: "<span translate='HANDING_UNIT'></span>"
      },

      {field: "measured", width: 70, headerTemplate: "<span translate='MEASURED'></span>"},
      {field: "width", width: 100, headerTemplate: "<span translate='LENGTH'></span><span>(mm)</span>"},
      {field: "depth", width: 100, headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "height", width: 100, headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      //{field: "volume", width:100, headerTemplate: "<span translate='VOLUME'></span>"},
      {field: "size", width: 100, headerTemplate: "<span>Size</span>"},
      {field: "weight", width: 100, headerTemplate: "<span translate='WEIGHT'></span><span>(g)</span>"},

      {field: "multiplePart", width: 70, headerTemplate: "<span translate='MULTIPLE_PART'></span>"},
      {field: "multiplePartAmount", width: 65, headerTemplate: "<span translate='MULTIPLE_PART_AMOUNT'></span>"},
      {field: "preferOwnBox", width: 70, headerTemplate: "<span translate='PREFER_OWN_BOX'></span>"},
      {field: "preferBag", width: 70, headerTemplate: "<span translate='PREFER_BAG'></span>"},
      {field: "useBubbleFilm", width: 70, headerTemplate: "<span translate='USE_BUBBLE_FILM'></span>"}
    ];
    $scope.itemDataGlobalGridOptions = commonService.gridMushiny({
      columns: $rootScope.columns,
      dataSource: masterService.getGridDataSource("itemDataGlobal")
    });
    /*************************************************************************************************************/
    $rootScope.exportColumns = [
      {
        field: "itemNo",
        width: 140,
        template: "<a ui-sref='main.itemDataGlobalRead({id:dataItem.id})'>#: itemNo # </a>",
        headerTemplate: "<span translate='ITEM_NO'></span>"
      },
      {field: "skuNo", width: 140, headerTemplate: "<span translate='SKU_NO'></span>"},
      {field: "name", width: 180, headerTemplate: "<span translate='NAME'></span>"},
      {field: "safetyStock", width: 100, headerTemplate: "<span translate='SAFETY_STOCK'></span>"},
      {
        field: "itemGroup.name",
        width: 100,
        template: "<a ui-sref='main.item_group'>#: itemGroup.name # </a>",
        headerTemplate: "<span translate='ITEM_GROUP'></span>"
      },

      {field: "lotMandatory", width: 70, headerTemplate: "<span translate='LOT_MANDATORY'></span>"},
      {
        field: "lotType", width: 90, headerTemplate: "<span translate='LOT_TYPE'></span>",
        template: function (dataItem) {
          return (dataItem.lotType ? (dataItem.lotType == "MANUFACTURE" ? "按生产日期" : dataItem.lotType == "EXPIRATION" ? "按到期日期" : "") : "")
        }
      },
      {
        field: "lotUnit",
        width: 70,
        headerTemplate: "<span translate='LOT_UNIT'></span>",
        template: function (dataItem) {
          return (dataItem.lotUnit ? (dataItem.lotUnit == "YEAR" ? "年" : dataItem.lotUnit == "MONTH" ? "月" : dataItem.lotUnit == "DAY" ? "日" : "") : "")
        }
      },
      {field: "lotThreshold", width: 65, headerTemplate: "<span translate='LOT_THRESHOLD'></span>"},
      {
        field: "itemUnit.name",
        width: 100,
        template: "<a ui-sref='main.item_unit'>#: itemUnit.name # </a>",
        headerTemplate: "<span translate='HANDING_UNIT'></span>"
      },

      {field: "measured", width: 70, headerTemplate: "<span translate='MEASURED'></span>"},
      {field: "width", width: 100, headerTemplate: "<span translate='LENGTH'></span><span>(mm)</span>"},
      {field: "depth", width: 100, headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "height", width: 100, headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      {field: "volume", width:100, headerTemplate: "<span translate='VOLUME'></span><span>(mm3)</span>"},
      {field: "size", width: 100, headerTemplate: "<span>Size</span>"},
      {field: "weight", width: 100, headerTemplate: "<span translate='WEIGHT'></span><span>(g)</span>"},

      {field: "multiplePart", width: 70, headerTemplate: "<span translate='MULTIPLE_PART'></span>"},
      {field: "multiplePartAmount", width: 65, headerTemplate: "<span translate='MULTIPLE_PART_AMOUNT'></span>"},
      {field: "preferOwnBox", width: 70, headerTemplate: "<span translate='PREFER_OWN_BOX'></span>"},
      {field: "preferBag", width: 70, headerTemplate: "<span translate='PREFER_BAG'></span>"},
      {field: "useBubbleFilm", width: 70, headerTemplate: "<span translate='USE_BUBBLE_FILM'></span>"},
      {field: "description", width:100, headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    /*********************************计算size*****start*************************************************************/
    itemDataGlobalService.getSizeFilterRule(function (data) {
      $rootScope.sizeFilterRuleData = data;
    });
    //数据添加到数组
    function addToArray(nameArrays, nameArray, name, obj) {
      nameArrays.push(obj);
      if (nameArray.indexOf(name) === -1) {
        nameArray.push(name);
      }
    };
    //设置每个规则是否True
    function ruleIsTrue(ruleName) {
      if (ruleName === "最长边") {
        isTrueSide = false;
      } else if (ruleName === "体积") {
        isTrueVolume = false;
      } else {
        isTrueWeight = false;
      }
    }

    //设置是否
    function isTrue(ruleName, price1, nameArrayS) {
      if (nameArrayS.mode === "小于") {
        if (price1 >= nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      } else if (nameArrayS.mode === "小于等于") {
        if (price1 > nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      } else if (nameArrayS.mode === "大于") {
        if (price1 <= nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      } else if (nameArrayS.mode === "大于等于") {
        if (price1 < nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      } else {
        if (price1 != nameArrayS.price) {
          ruleIsTrue(ruleName)
        }
      }
    };
    //设置size
    function setSize(size1, nameArrayS, longestSide, volume, weight) {
      isTrueSide = true;
      isTrueVolume = true;
      isTrueWeight = true;
      for (var s = 0; s < nameArrayS.length; s++) {
        if (nameArrayS[s].rule === "最长边") {
          isTrue("最长边", longestSide, nameArrayS[s]);
        } else if (nameArrayS[s].rule === "体积") {
          isTrue("体积", volume, nameArrayS[s]);
        } else {
          isTrue("重量", weight, nameArrayS[s]);
        }
      }
      if (isTrueSide && isTrueVolume && isTrueWeight) {
        $rootScope.size1 = size1;
      }
    }

    $rootScope.calculatorSize = function (side1, side2, side3, weight) {
      var nameArray = [];//放name的数组，最多四条小，中，大，超大
      var nameArrayS = [];//小
      var nameArrayM = [];//中
      var nameArrayB = [];//大
      var nameArrayBB = [];//超大
      //如果最长边或者体积或者重量有任何一个为0，那么size为null
      if (side1 === 0 || side2 === 0 || side3 === 0 || weight === 0) {
        $rootScope.size1 = "小";
      } else {
        var longestSide = side1 > side2 ? (side1 > side3 ? side1 : side3) : ((side2 > side3 ? side2 : side3));
        var volume = side1 * side2 * side3;

        //有过滤规则
        if ($rootScope.sizeFilterRuleData != null && $rootScope.sizeFilterRuleData.length !== 0) {
          for (var i = 0; i < $rootScope.sizeFilterRuleData.length; i++) {
            var name = $rootScope.sizeFilterRuleData[i].name;
            if (name === "小") {
              addToArray(nameArrayS, nameArray, name, $rootScope.sizeFilterRuleData[i]);
            } else if (name === "中") {
              addToArray(nameArrayM, nameArray, name, $rootScope.sizeFilterRuleData[i]);
            } else if (name === "大") {
              addToArray(nameArrayB, nameArray, name, $rootScope.sizeFilterRuleData[i]);
            } else {
              addToArray(nameArrayBB, nameArray, name, $rootScope.sizeFilterRuleData[i]);
            }
          }
          for (var a = 0; a < nameArray.length; a++) {
            if (nameArray[a] === "小") {
              if ($rootScope.size1 == undefined) {
                setSize("小", nameArrayS, longestSide, volume, weight);
              }
            } else if (nameArray[a] === "中") {
              if ($rootScope.size1 == undefined) {
                setSize("中", nameArrayM, longestSide, volume, weight);
              }
            } else if (nameArray[a] === "大") {
              if ($rootScope.size1 == undefined) {
                setSize("大", nameArrayB, longestSide, volume, weight);
              }
            } else if (nameArray[a] === "超大") {
              if ($rootScope.size1 == undefined) {
                setSize("超大", nameArrayBB, longestSide, volume, weight);
              }
            }
          }
          //有过滤规则，没有过滤出来设置为超大
          if ($rootScope.size1 == undefined) {
            $rootScope.size1 = "超大";
          }
        } else {
          //没有过滤规则默认为超大
          $rootScope.size1 = "超大";
        }
      }
    };
    /*********************************导入选择下拉数据********************************************************/
    $scope.editItemGroup = function (container, options) {
      $('<input id="itemGroupId" name="' + options.field + '"  />')
        .appendTo(container)
        .kendoComboBox({
          dataTextField: "name",
          dataValueField: "id"
        });
      itemDataGlobalService.getItemGroup(function (data) {
        var combox = $("#itemGroupId").data("kendoComboBox");
        combox.setDataSource(new kendo.data.DataSource({data: data}));
      });
    };
    $scope.editTrueOfFalse = function (container, options) {
      var input = $('<input  name="' + options.field + '"  />');
      input.appendTo(container)
        .kendoComboBox({});
      var combox = input.data("kendoComboBox");
      combox.setDataSource(new kendo.data.DataSource({data: $scope.trueOfFalse}));
    };
    $scope.editOther = function (container, options) {
      var input = $('<input name="' + options.field + '"  />');
      input.appendTo(container)
        .kendoComboBox({
          dataTextField: "description",
          dataValueField: "id"
        });
      itemDataGlobalService.getSelectionBySelectionKey(options.field, function (data) {
        var combox = input.data("kendoComboBox");
        combox.setDataSource(new kendo.data.DataSource({data: data}));
      });
    };
    $scope.editItemUnit = function (container, options) {
      var input = $('<input name="' + options.field + '"  />');
      input.appendTo(container)
        .kendoComboBox({
          dataTextField: "name",
          dataValueField: "id"
        });
      itemDataGlobalService.getItemUnit(function (data) {
        var combox = input.data("kendoComboBox");
        combox.setDataSource(new kendo.data.DataSource({data: data}));
      });
    };
   $scope.downloadTemplate=function(){
      getSelect({key: "getItemGroup", text: "name", value: "id"});
      getSelect({key: "getItemUnit", text: "name", value: "id"});
      getSelect({key: "getSelectionBySelectionKey", value: "selectionValue", text: "resourceKey", data: {selectionKey: "SERIAL_RECORD"}});
      getSelect({key: "getSelectionBySelectionKey", value: "selectionValue", text: "resourceKey", data: {selectionKey: "LOT_TYPE"}});
      getSelect({key: "getSelectionBySelectionKey", value: "selectionValue", text: "resourceKey", data: {selectionKey: "LOT_UNIT"}});
      var columnName = ["货物分组","商品编号","名称","描述","安全库存","有效期商品","有效期类型","有效期单位",
          "商品距离到期日最小值","记录类型","序列号长度","商品单位", "测量","长(mm)","宽(mm)",
          "高(mm)","重量(g)","一套多件", "一套多件数量","自带包装","袋子优先","使用气垫膜"]; //22项
      var cellWidths = [100,100,200,100,100,100,100,100,150,100,100,100,100,100,100,100,100,100,100,100,100,100];
      exportToExcelLine("#itemDataGlobalGrid","SKU Global模板",cellWidths,columnName);
   };
   function exportToExcelLine(tableId, tableName, columnsWidths, columnsName) {
      var rows = [];
      var headCells = [];
      var cellWidths = [];
      for (var k = 0; k < columnsName.length; k++) {
          cellWidths.push({width: columnsWidths[k]});
          var cellValue = columnsName[k];
          if (cellValue != null) {
              headCells.push({value: cellValue});
          }
       }
       var headRow = {cells: headCells}; //标题行
       rows.push(headRow); //将标题行添加到rows集合中
       var rowInfo=[];
       for(var i = 0; i < columnsName.length; i++){
           var itemData="",serialRecord="",itemUnit="",lotType="",lotUnit="";
           if(i==0) {
              for (var j = 0; j < $scope.itemGlobalDatas.length; j++) {
                  itemData = itemData + $scope.itemGlobalDatas[j].name + "/";
              }
              rowInfo.push({value: itemData.substring(0,itemData.length-1)});
           } else if(i==6){
              for(var j=0;j<$scope.lotType.length;j++){
                  lotType=lotType+"按"+$scope.lotType[j].description+"/";
              }
              rowInfo.push({value:lotType.substring(0,lotType.length-1)});
           } else if(i==7){
              for(var j=0;j<$scope.lotUnit.length;j++){
                 lotUnit=lotUnit+$scope.lotUnit[j].description+"/";
              }
              rowInfo.push({value:lotUnit.substring(0,lotUnit.length-1)})
           } else if(i==9){
              for(var j=0;j<$scope.serialRecord.length;j++){
                  serialRecord=serialRecord+$scope.serialRecord[j].description+"/";
              }
                 rowInfo.push({value:serialRecord.substring(0,serialRecord.length-1)})
           } else if(i==11){
              for(var j=0;j<$scope.itemunits.length;j++){
                  itemUnit=itemUnit+$scope.itemunits[j].name+"/";
              }
              rowInfo.push({value:itemUnit.substring(0,itemUnit.length-1)})
           } else if(i==5||i==12||i==17||i==19||i==20||i==21){
               rowInfo.push({value:"true/false"});
           } else{
              rowInfo.push({value:""})
           }
       }
       rows.push({cells:rowInfo});
       //创建工作册
       var workbook = new kendo.ooxml.Workbook({
           sheets: [{
              columns: cellWidths,
              title: tableName,
              rows: rows
           }]
        });
       //保存为excel文件
       kendo.saveAs({dataURI: workbook.toDataURL(), fileName: tableName + '.xlsx'});
    }
    function getSelect(opts){
       masterService.getSelectData(opts,function(datas){
          if(opts.key=="getItemGroup") $scope.itemGlobalDatas=datas;
          if(opts.key=="getItemUnit") $scope.itemunits=datas;
          if(opts.data!=null&&opts.data.selectionKey=="SERIAL_RECORD") $scope.serialRecord=datas;
          if(opts.data!=null&&opts.data.selectionKey=="LOT_TYPE") $scope.lotType=datas;
          if(opts.data!=null&&opts.data.selectionKey=="LOT_UNIT") $scope.lotUnit=datas;
          });
      }
  }).controller("itemDataGlobalCreateCtl", function ($scope, $rootScope, $state, $translate, commonService, MASTER_CONSTANT, masterService, itemDataGlobalService,BACKEND_CONFIG) {
    $scope.lotMandatory = "true";
    $scope.measured = "true";
    $scope.multiplePart = "true";
    $scope.preferOwnBox = "true";
    $scope.preferBag = "true";
    $scope.useBubbleFilm = "true";
    /****************************导入弹窗按钮***************************************/

    // 检查sku
    $scope.checkSKU = function (skuNo) {
      itemDataGlobalService.checkSKU(skuNo, function (datas) {
        if (!datas.length) return;
        commonService.dialogMushiny($scope.window, {
          title: "<span style='font-size:12px;'>" + $translate.instant("SKU_HAS_GOODS") + "</span>",
          width: 470,
          height: 230,
          url: "modules/masterData/master/itemDataGlobal/templates/itemDataGlobal_sku.html",
          open: function () {
            var columns = [
              {field: "itemNo", headerTemplate: "<span translate='ITEM_NO'></span>"},
              {field: "name", headerTemplate: "<span translate='NAME'></span>"}
            ];
            $rootScope.skuListGridOptions = {
              selectable: "row",
              height: 195,
              sortable: true,
              columns: columns,
              dataSource: datas
            };
          }
        });
      });
    };

      // 保存
      $scope.validate = function (event) {
          event.preventDefault();
          if ($scope.validator.validate()) {
              $rootScope.size1 = undefined;
              $rootScope.calculatorSize($scope.height, $scope.width, $scope.depth, $scope.weight),//计算出来的size
                  masterService.create("itemDataGlobal", {
                      "skuNo": $scope.skuNo,
                      "name": $scope.name,
                      "description": $scope.description,
                      "safetyStock": $scope.safetyStock,
                      "itemGroupId": $scope.itemGroup ? $scope.itemGroup.id : null,

                      "lotMandatory": $scope.lotMandatory,
                      "lotType": $scope.lotType ? $scope.lotType.selectionValue : null,
                      "lotUnit": $scope.lotUnit ? $scope.lotUnit.selectionValue : null,
                      "lotThreshold": $scope.lotThreshold,
                      "serialRecordType": $scope.serialRecordType ? $scope.serialRecordType.selectionValue : "",
                      "serialRecordLength": $scope.serialRecordLength,
                      "handlingUnitId": $scope.itemUnit ? $scope.itemUnit.id : null,

                      "measured": $scope.measured,
                      "height": $scope.height,
                      "width": $scope.width,
                      "depth": $scope.depth,
                      "size": $rootScope.size1,
                      "volume": $scope.height * $scope.width * $scope.depth,
                      "weight": $scope.weight,

                      "multiplePart": $scope.multiplePart,
                      "multiplePartAmount": $scope.multiplePartAmount,
                      "preferOwnBox": $scope.preferOwnBox,
                      "preferBag": $scope.preferBag,
                      "useBubbleFilm": $scope.useBubbleFilm
                  }, function () {
                      $state.go("main.item_data_global");
                  });
          }
      };

      $("#itemDataFileId").kendoUpload({
          multiple: false,//支持多文件上传
          async: {
              saveUrl:BACKEND_CONFIG.masterData+"masterdata/item-data-globals/upload-skuGlobal",
              autoUpload: false
          },
          select:function(){
              $("#saveId").hide();
          },
          success: function(e){
              if(e.response.result==1) {
                  $scope.$apply(function() {
                      $state.go("main.item_data_global");
                  });
              } else{
                  $scope.$apply(function() {
                      var win = $("#mushinyWindow").data("kendoWindow");
                      commonService.dialogMushiny(win, {title:"上传文件",width: 320, height: 160,type: "warn", open: function(){
                          setTimeout(function(){
                              $("#warnContent").html(e.response.message)
                          }, 200);
                      }});
                  });
              }
          },
          upload:function(e) {
              if ($scope.validator.validate()) {
                  $rootScope.size1 = undefined;
                  $rootScope.calculatorSize($scope.height, $scope.width, $scope.depth, $scope.weight);
                  $scope.uploadFileParam = {
                      skuNo: $scope.skuNo,
                      name: $scope.name,
                      description: $scope.description,
                      safetyStock: $scope.safetyStock,
                      itemGroupId: $scope.itemGroup ? $scope.itemGroup.id : null,

                      lotMandatory: $scope.lotMandatory,
                      lotType: $scope.lotType ? $scope.lotType.selectionValue : null,
                      lotUnit: $scope.lotUnit ? $scope.lotUnit.selectionValue : null,
                      lotThreshold: $scope.lotThreshold,
                      serialRecordType: $scope.serialRecordType ? $scope.serialRecordType.selectionValue : "",
                      serialRecordLength: $scope.serialRecordLength,
                      handlingUnitId: $scope.itemUnit ? $scope.itemUnit.id : null,

                      measured: $scope.measured,
                      height: $scope.height,
                      width: $scope.width,
                      depth: $scope.depth,
                      size: $rootScope.size1,
                      volume: $scope.height * $scope.width * $scope.depth,
                      weight: $scope.weight,

                      multiplePart: $scope.multiplePart,
                      multiplePartAmount: $scope.multiplePartAmount,
                      preferOwnBox: $scope.preferOwnBox,
                      preferBag: $scope.preferBag,
                      useBubbleFilm: $scope.useBubbleFilm
                  }
              }
              e.data={uploadParams:angular.toJson($scope.uploadFileParam)}
          },
          localization: {
              "select":"选择图片",
              "uploadSelectedFiles":"保存",
              "headerStatusUploaded":"上传失败",
              "headerStatusUploading":"上传中"
          }
      });

  }).controller("itemDataGlobalUpdateCtl", function ($scope, $rootScope, $stateParams, $state, masterService,BACKEND_CONFIG,commonService) {
    masterService.read("itemDataGlobal", $stateParams.id, function (data) {
      for (var k in data) {
        if (data[k] === true) data[k] = "true";
        else if (data[k] === false) data[k] = "false";
        $scope[k] = (k === "serialRecordType" ? masterService.toMap(data[k]) : data[k]);
      }
      $scope.lotType = {selectionValue: data.lotType};
      $scope.lotUnit = {selectionValue: data.lotUnit};
    });

    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        $rootScope.size1 = undefined;
        $rootScope.calculatorSize($scope.height, $scope.width, $scope.depth, $scope.weight),//计算出来的size
          masterService.update("itemDataGlobal", {
            "id": $scope.id,
            "skuNo": $scope.skuNo,
            "name": $scope.name,
            "description": $scope.description,
            "safetyStock": $scope.safetyStock,
            "itemGroupId": $scope.itemGroup ? $scope.itemGroup.id : null,

            "lotMandatory": $scope.lotMandatory,
            "lotType": $scope.lotType ? $scope.lotType.selectionValue : null,
            "lotUnit": $scope.lotUnit ? $scope.lotUnit.selectionValue : null,
            "lotThreshold": $scope.lotThreshold,
            "serialRecordType": $scope.serialRecordType ? $scope.serialRecordType.selectionValue : null,
            "serialRecordLength": $scope.serialRecordLength,
            "handlingUnitId": $scope.itemUnit ? $scope.itemUnit.id : null,

            "measured": $scope.measured,
            "height": $scope.height,
            "width": $scope.width,
            "depth": $scope.depth,
            "size": $rootScope.size1,//计算出来的size
            "volume": $scope.height * $scope.width * $scope.depth,
            "weight": $scope.weight,

            "multiplePart": $scope.multiplePart,
            "multiplePartAmount": $scope.multiplePartAmount,
            "preferOwnBox": $scope.preferOwnBox,
            "preferBag": $scope.preferBag,
            "useBubbleFilm": $scope.useBubbleFilm
          }, function () {
            $state.go("main.item_data_global");
          });
      }
    };

    $("#itemDataFileId").kendoUpload({
        multiple: false,//支持多文件上传
        async: {
            saveUrl:BACKEND_CONFIG.masterData+"masterdata/item-data-globals/update-skuGlobal",
            autoUpload: false
        },
        select:function(){
           $("#updateId").hide();
        },
        success: function(e){
           if(e.response.result==1) {
               $scope.$apply(function() {
                   $state.go("main.item_data_global");
               });
           } else{
               $scope.$apply(function() {
                   var win = $("#mushinyWindow").data("kendoWindow");
                   commonService.dialogMushiny(win, {title:"上传文件",width: 320, height: 160,type: "warn", open: function(){
                       setTimeout(function(){
                           $("#warnContent").html(e.response.message)
                       }, 200);
                   }});
               });
           }
        },
        upload:function(e) {
            if ($scope.validator.validate()) {
                $rootScope.size1 = undefined;
                $rootScope.calculatorSize($scope.height, $scope.width, $scope.depth, $scope.weight);
                $scope.uploadFileParam = {
                    id: $scope.id,
                    skuNo: $scope.skuNo,
                    name: $scope.name,
                    description: $scope.description,
                    safetyStock: $scope.safetyStock,
                    itemGroupId: $scope.itemGroup ? $scope.itemGroup.id : null,
                    itemNo: $scope.itemNo,

                    lotMandatory: $scope.lotMandatory,
                    lotType: $scope.lotType ? $scope.lotType.selectionValue : null,
                    lotUnit: $scope.lotUnit ? $scope.lotUnit.selectionValue : null,
                    lotThreshold: $scope.lotThreshold,
                    serialRecordType: $scope.serialRecordType ? $scope.serialRecordType.selectionValue : "",
                    serialRecordLength: $scope.serialRecordLength,
                    handlingUnitId: $scope.itemUnit ? $scope.itemUnit.id : null,

                    measured: $scope.measured,
                    height: $scope.height,
                    width: $scope.width,
                    depth: $scope.depth,
                    size: $rootScope.size1,
                    volume: $scope.height * $scope.width * $scope.depth,
                    weight: $scope.weight,

                    multiplePart: $scope.multiplePart,
                    multiplePartAmount: $scope.multiplePartAmount,
                    preferOwnBox: $scope.preferOwnBox,
                    preferBag: $scope.preferBag,
                    useBubbleFilm: $scope.useBubbleFilm
                }
            }
            e.data={uploadParams:angular.toJson($scope.uploadFileParam)}
        },
        localization: {
            "select":"选择图片",
            "uploadSelectedFiles":"修改",
            "headerStatusUploaded":"上传失败",
            "headerStatusUploading":"上传中"
        }
    });

  }).controller("itemDataGlobalReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("itemDataGlobal", $stateParams.id, function (data) {
      for (var k in data) {
        if (data[k] === true) data[k] = "true";
        else if (data[k] === false) data[k] = "false";
        $scope[k] = (k === "serialRecordType" ? masterService.toMap(data[k]) : data[k]);
      }
      $scope.lotType = {selectionValue: data.lotType};
      $scope.lotUnit = {selectionValue: data.lotUnit};
    });
  }).controller("itemDataGlobalExportCtl", function ($scope, $rootScope, Excel, commonService, masterService) {
    $scope.itemDataGlobalExportGridOptions = commonService.gridMushiny({
      columns: $rootScope.exportColumns,
      dataSource: masterService.getGridDataExportSource("itemDataGlobal")
    });
    //列名
    var columnsName = ["唯一编码", "商品编码", "名称", "安全库存", "货位分组", "有效期商品","有效期类型", "有效期单位" ,"商品距离有效期最小值", "商品单位", "测量", "长（mm）",
      "宽（mm）", "高（mm）", "体积（mm3）","Size", "重量（g）", "一套多件", "一套多件数量", "自带包装", "袋子优先", "试用气垫膜","描述"];
    //列宽
    var cellWidths1 = [130, 130, 260, 90, 90, 100,100,100, 140, 90, 60, 70, 70, 70,70, 70, 70, 90, 110, 90, 90, 90,110];
    $rootScope.OKExport = function () {
      Excel.exportToExcel("#itemDataGlobalExportGrid", "itemDataGlobal", cellWidths1, columnsName);
    };
  });
  ;
})();