/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
    "use strict";

    angular.module('myApp').controller("pickPackCellTypeBoxTypeCtl", function ($scope, commonService, masterService, pickPackCellTypeBoxTypeService) {
        // ==========================================================================================================
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
                dataSource: options.key ? masterService.getDataSource({
                    key: options.key,
                    value: "id",
                    text: "name"
                }) : [],
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
        // ===========客户下拉数据===============================================================================================
        var clientId;
        $scope.selectClientOptions = getComboxOption({
            key: "getClient",
            change: function () {
                var clientId = $scope.selectClient.id;
                pickPackCellTypeBoxTypeService.getBoxTypeByClient(clientId, function (boxTypes) {
                    var boxTypeComboBox = $("#selectBoxType").data("kendoComboBox");
                    boxTypeComboBox.value("");
                    boxTypeComboBox.setDataSource(new kendo.data.DataSource({data: boxTypes}));
                });
            }
        });
        //箱子下拉数据  这块选择的id是箱子的id
        $scope.selectBoxTypeOptions = getComboxOption({
            change: function () {
                var clientId = $scope.selectClient.id;
                var boxTypeId = $scope.selectBoxType.id;
                pickPackCellTypeBoxTypeService.getUnassignedPickPackCellTypeByBoxType(boxTypeId,clientId, function (unAssigned) {
                    getKendoList("listUnassignPickPackCellTypes").setDataSource(new kendo.data.DataSource({data: unAssigned}));
                });
                pickPackCellTypeBoxTypeService.getAssignedPickPackCellTypeByBoxType(boxTypeId,clientId, function (assigned) {
                    getKendoList("listAssignPickPackCellTypes").setDataSource(new kendo.data.DataSource({data: assigned}));
                });
            }
        });
        $scope.listAssignPickPackCellTypes = masterService.getJsonForListView({
            data: [],
            template: null/*"#:userGroup.name#,#:name#"*/
        });
        $scope.listUnassignPickPackCellTypes = masterService.getJsonForListView({
            data: [],
            template: null/*"#:userGroup?userGroup.name:''#,#:name#"*/
        });

        // horizontal
        $scope.moveLeftAll = function (leftId, rightId) {
            leftId == null && (leftId = "listUnassignPickPackCellTypes");
            rightId == null && (rightId = "listAssignPickPackCellTypes");
            var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
            for (var i = 0, items = listRight.dataItems(), len = items.length; i < len; i++) {
                listRight.dataSource.remove(items[i]);
                listLeft.dataSource.insert(0, items[i]);
            }
            select(listLeft, len);
        };
        $scope.moveLeftSelect = function (leftId, rightId) {
            leftId == null && (leftId = "listUnassignPickPackCellTypes");
            rightId == null && (rightId = "listAssignPickPackCellTypes");
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
            leftId == null && (leftId = "listUnassignPickPackCellTypes");
            rightId == null && (rightId = "listAssignPickPackCellTypes");
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
            leftId == null && (leftId = "listUnassignPickPackCellTypes");
            rightId == null && (rightId = "listAssignPickPackCellTypes");
            var listLeft = getKendoList(leftId), listRight = getKendoList(rightId);
            for (var i = 0, items = listLeft.dataItems(), len = items.length; i < len; i++) {
                listLeft.dataSource.remove(items[i]);
                listRight.dataSource.insert(0, items[i]);
            }
            select(listRight, len);
        };

        // 保存
        $scope.save = function (inputObj, listViewId, requestKey) {
            var listView = getKendoList(listViewId), listItems = listView.dataItems();
            for (var i = 0, items = []; i < listItems.length; i++) items.push(listItems[i].id);
            pickPackCellTypeBoxTypeService.save(requestKey, inputObj.id, items, function () {
                commonService.dialogMushiny($scope.window, {type: "success"});
            });
        };
    });
})();