/**
 * Created by bian on 2016/12/5.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("measureToolCtl", function ($scope, $rootScope, $state, $timeout, commonService, measureService) {
    $scope.sourceErr = '';
    $scope.goodsErr = '';
    var arr = [];
    $rootScope.sizeFilterRuleData;
    $rootScope.size1;
    var isTrueSide;
    var isTrueVolume;
    var isTrueWeight;
    $timeout(function () {
      $("#sourceId").focus();
    }, 100);
    //测量
    $scope.measureToolmeasure = function () {
      measureService.measureToolmeasure($scope.measureName, function (data) {
        $scope.width = data.data.length;
        $scope.depth = data.data.width;
        $scope.height = data.data.height;
        $scope.weight = data.data.weight;
        $timeout(function () {
          $("#destinationId").focus();
        }, 100);
      });
    }
    //扫描原始容器
    $scope.sources = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        measureService.scanningSource($scope.source, function (data) {
          $timeout(function () {
            $("#itemNoId").focus();
          }, 100);
          $scope.sourceId = data.id;
          $scope.sourceErr = '';
          $scope.errMessage = "";
        }, function (data) {
          $scope.goodsErr = 'err';
          $scope.errMessage = data.message;
          $scope.source = "";
          $timeout(function () {
            $("#sourceId").focus();
          }, 100);
        });
      }
    };

    $scope.itemNos = function (e) {
      var keycode = window.event ? e.keyCode : e.which;

      if (keycode == 13) {
        measureService.scanningItemData($scope.itemNo, $scope.sourceId, function (data) {
          $scope.goodName = data.itemData.name;
          $scope.itemNOk = $scope.itemNo;
          $scope.itemId = data.itemData.id;
          $scope.itemUnitName = data.itemData.itemUnit.name;
          $timeout(function () {
            $("#widthId").focus();
          }, 100);
          $scope.goodsErr = '';
          $scope.errMessage = "";
        }, function (data) {
          $scope.errMessage = data.message;
          $scope.goodsErr = 'err';
          $scope.itemNo = "";
          $timeout(function () {
            $("#itemNoId").focus();
          }, 100);
        });

      }
    };


    $scope.widths = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $timeout(function () {
          $("#depthId").focus();
        }, 100);
      }
    };

    $scope.depths = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $timeout(function () {
          $("#heightId").focus();
        }, 100);
      }
    };

    $scope.heights = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $timeout(function () {
          $("#weightId").focus();
        }, 100);
      }
    };

    $scope.weights = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        $timeout(function () {
          $("#destinationId").focus();
        }, 100);
      }
    };
    /*********************************计算size*****start*************************************************************/
    measureService.getSizeFilterRule(function (data) {
      $rootScope.sizeFilterRuleData = data;
      // console.log($rootScope.sizeFilterRuleData);
    });
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
        $rootScope.size1 = "";
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
    $scope.destinations = function (e) {
      var keycode = window.event ? e.keyCode : e.which;
      if (keycode == 13) {
        measureService.scanningDestination($scope.sourceId, $scope.itemId, $scope.destination, function (data) {
          $rootScope.size1 = undefined;
          $rootScope.calculatorSize($scope.width, $scope.depth, $scope.height, $scope.weight),//计算出来的size
            $scope.errMessage = "";
          measureService.saveMeasure({
            sourceId: $scope.sourceId,
            itemDataId: $scope.itemId,
            width: $scope.width,
            depth: $scope.depth,
            height: $scope.height,
            size: $rootScope.size1,
            weight: $scope.weight,
            destinationId: data.id
          }, function (data) {
            var date = new Date();
            arr.push({
              itemNo: $scope.itemNOk,
              width: $scope.width,
              depth: $scope.depth,
              height: $scope.height,
              weight: $scope.weight,
              name: $scope.goodName,
              itemUnitName: $scope.itemUnitName,
              operatingTime: kendo.format("{0:yyyy-MM-dd HH:mm:ss}", date)
            });
            $timeout(function () {
              $("#sourceId").focus();
            }, 100);
            $scope.source = "";
            $scope.itemNo = "";
            $scope.width = "";
            $scope.depth = "";
            $scope.height = "";
            $scope.weight = "";
            $scope.destination = "";
            $('#saveMeasureId').parent().addClass("windowTitle");
            $scope.measureSaveWindow.setOptions({
              width: 600,
              height: 150,
              visible: false,
              actions: false,
              activate: function () {
                $timeout(function () {

                  $("#saveMeasureSureId").focus();

                })
              },
              close: function () {
                $timeout(function () {
                  $("#sourceId").focus();
                }, 500)

              }
            });

            $scope.measureSaveWindow.center();
            $scope.measureSaveWindow.open();

          }, function (data) {
            $scope.goodsErr = 'err';
            $scope.errMessage = data.message;
            $scope.destination = "";
          });
        }, function (data) {
          $scope.goodsErr = 'err';
          $scope.errMessage = data.message;
          $scope.destination = "";
        });
      }
    };

    $scope.saveMeasureSure = function () {
      $scope.measureSaveWindow.close();
    };

    var columns = [
      // {field: "picture", width:100, headerTemplate: "<span translate='PICTURE'></span>"},
      {
        field: "itemNo",
        width: 80,
        headerTemplate: "<span translate='SKU'></span>"
      },
      {
        field: "width",
        width: 30,
        headerTemplate: "<span translate='LENGTH'></span><span>(mm)</span>"
      },
      {
        field: "depth",
        width: 30,
        headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"
      },
      {
        field: "height",
        width: 30,
        headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"
      },
      {
        field: "weight",
        width: 30,
        headerTemplate: "<span translate='WEIGHT'></span><span>(g)</span>"
      },
      {
        field: "name",
        width: 250,
        headerTemplate: "<span translate='GOODS_NAME'></span>",
        attributes: {
          style: "text-align:left"
        }
      },
      // {field: "count",width:"80px",headerTemplate: "<span translate='COUNT'></span>" },
      {
        field: "itemUnitName",
        width: 30,
        headerTemplate: "<span translate='HANDING_UNIT'></span>"
      },
      {
        field: "operatingTime",
        width: 80,
        headerTemplate: "<span translate='操作时间'></span>"
      }
    ];


    $scope.selectTransferRecords = function () {
      var width = 1200,
        height = 500;
      var url = "modules/internalTool/base/templates/readInWindow.html";
      commonService.dialogMushiny($scope.window, {
        title: "查看测量记录", //"<span>"+ $translate.instant("READ_"+ options.title)+ "</span>",
        width: width,
        height: height,
        url: url,
        open: function () {
          $rootScope.readGridOptions = {
            selectable: "row",
            dataSource: arr,
            height: 385,
            sortable: true,
            columns: columns
          };
          $("#mushinyWindow_wnd_title").css("font-size", "25px");
          $("#mushinyWindow_wnd_title").css("text-align", "center");
          $("#mushinyWindow_wnd_title").parent().css("z-index", "99");
          $("#mushinyWindow_wnd_title").parent().css("height", "30px");
        }
      });
    };
  });

})();