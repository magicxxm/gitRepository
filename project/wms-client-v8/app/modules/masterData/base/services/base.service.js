/**
 * Created by frank.zhou on 2017/04/20.
 */
(function () {
    "use strict";

    angular.module("myApp").service("masterService", function ($translate, commonService, MASTER_CONSTANT) {
        return {
            // 首字母大写
            toUpper: function (string) {
                return string.substring(0, 1).toUpperCase() + string.substring(1);
            },
            //
            toMap: function (value, key) {
                return {selectionValue: value, resourceKey: $translate.instant(key || value)};
            },
            // 取数据源
            getDataSource: function (opts) {
                opts.value == null && (opts.value = "id");
                return {
                    serverFiltering: false,
                    transport: {
                        read: function (options) {
                            commonService.ajaxSync({
                                url: MASTER_CONSTANT[opts.key],
                                async: true,
                                data: opts.data,
                                success: function (result) {
                                    console.log("111", result);
                                    options.success(result);
                                }
                            });
                        }
                    },
                    schema: {
                        data: function (datas) {
                            for (var i = 0, items = []; i < datas.length; i++) {
                                var data = datas[i], itemMap = {id: data.id};
                                itemMap[opts.text] = (opts.text === "resourceKey" ? $translate.instant(data[opts.text]) : data[opts.text]);
                                itemMap[opts.value] = data[opts.value];
                                items.push(angular.extend(data, itemMap));
                            }
                            return items;
                        }
                    }
                };
            },
            // list数据源
            getListDataSource: function (key) {
                key = this.toUpper(key);
                return {
                    transport: {
                        read: function (options) {
                            // 排序
                            var sort = options.data.sort || [];
                            sort && sort.length && (sort = sort[0].field + "," + sort[0].dir);
                            // 过滤
                            var filter = options.data.filter || {}, filterItems = filter.filters || [];
                            for (var i = 0, filterStr = ""; i < filterItems.length; i++) {
                                var filterItem = filterItems[i];
                                filterStr += filterItem.field + "==" + (filterItem.operator == "contains" ? "*" + filterItem.value + "*" : filterItem.value);
                                i != filterItems.length - 1 && (filterStr += ";");
                            }
                            // 取数据
                            commonService.ajaxSync({
                                url: MASTER_CONSTANT["find" + key],
                                async: true,
                                data: {
                                    sort: sort,
                                    search: filterStr
                                },
                                success: function (result) {
                                    options.success(result);
                                }
                            });
                        }
                    },
                    schema: {
                        parse: function (datas) {
                            for (var i = 0; i < datas.length; i++) !datas[i].parentItemGroup && (datas[i].parentItemGroup = {id: null});
                            return datas;
                        },
                        model: {
                            id: "id",
                            fields: {
                                parentId: {field: "parentItemGroup.id", nullable: true}
                            }
                        }
                    },
                    serverFiltering: true,
                    serverSorting: true
                };
            },
            // grid数据源
            getGridDataSource: function (key) {
                key = this.toUpper(key);
                return {
                    transport: {
                        read: function (options) {
                            // 排序
                            var sort = options.data.sort || [];
                            sort && sort.length && (sort = sort[0].field + "," + sort[0].dir);
                            // 过滤
                            var filter = options.data.filter || {}, filterItems = filter.filters || [];
                            for (var i = 0, filterStr = ""; i < filterItems.length; i++) {
                                var filterItem = filterItems[i];
                                filterStr += filterItem.field + "==" + (filterItem.operator == "contains" ? "*" + filterItem.value + "*" : filterItem.value);
                                i != filterItems.length - 1 && (filterStr += ";");
                            }
                            // 取数据
                            commonService.ajaxSync({
                                url: MASTER_CONSTANT["find" + key],
                                async: true,
                                data: {
                                    page: options.data.page - 1,
                                    size: options.data.pageSize,
                                    sort: sort,
                                    search: filterStr
                                },
                                success: function (result) {
                                    options.success(result);
                                }
                            });
                        }
                    },
                    filter: {field: "entityLock", operator: "eq", value: "0"},
                    schema: {
                        data: function (response) {
                            return response.content;
                        },
                        total: function (response) {
                            return response.totalElements;
                        }
                    },
                    serverAggregates: true,
                    serverPaging: true,
                    serverFiltering: true,
                    serverSorting: true
                };
            },
            getGridDataExportSource: function (key) {
                key = this.toUpper(key);
                return {
                    transport: {
                        read: function (options) {
                            // 取数据
                            commonService.ajaxSync({
                                url: MASTER_CONSTANT["find" + key],
                                async: true,
                                data: {
                                    page: options.data.page - 1,

                                    size: 1000
                                    // search: filterStr
                                },
                                success: function (result) {
                                    console.log(result);
                                    options.success(result);
                                }
                            });
                        }
                    },
                    filter: {field: "entityLock", operator: "eq", value: "0"},
                    schema: {
                        data: function (response) {
                            return response.content;
                        },
                        total: function (response) {
                            return response.totalElements;
                        }
                    },
                    serverAggregates: true,
                    serverPaging: true,
                    serverFiltering: true,
                    serverSorting: true
                };
            },
            // listView
            getJsonForListView: function (options) {
                options.template == null && (options.template = "#:name#");
                return {
                    dataSource: {data: options.datas},
                    selectable: options.selectable || "multiple",
                    template: "<div style='height:20px;vertical-align:middle;padding:4px;'>" + options.template + "</div>",
                    change: function () {
                        options.change && options.change(this);
                    }
                };
            },
            editGrid: function (options) {
                return {
                    dataSource: options.dataSource,
                    selectable: options.selectModel || "row",
                    height: options.height || 300,
                    sortable: true,
                    scrollable: true,
                    pageable: false,
                    editable: options.editable != null ? options.editable : {
                        confirmation: false,
                        mode: options.mode || "incell"
                    },
                    columns: options.columns
                };
            },
            // 列表编辑器
            selectEditor: function (container, options, dataSource) {
                // 创建input
                var requireStr = "";
                if (["storageLocationType", "dropZone", "receiveDestination"].indexOf(options.field) >= 0)
                    requireStr = "required validationMessage=\"{{'WARNING_NAME' | translate }}\"";
                var input = $('<input ' + requireStr + ' name="' + options.field + '"/>');
                input.appendTo(container);
                // 输入控件
                if (["face", "positionState"].indexOf(options.field) >= 0)
                    input.kendoComboBox({
                        dataTextField: "resourceKey",
                        dataValueField: "selectionValue",
                        filter: "contains",
                        dataSource: dataSource
                    });
                else if (options.field === "operator")
                    input.kendoComboBox({filter: "contains", dataSource: dataSource});
                else if (options.field === "compKey") {
                    if (options.model.receiveCategoryRule.decisionKey === "ZONE_TYPE") input.kendoComboBox({
                        filter: "contains",
                        dataSource: dataSource
                    });
                    else input.kendoMultiSelect({
                        dataTextField: "name",
                        dataValueField: "id",
                        filter: "contains",
                        dataSource: dataSource
                    });
                } else if (options.field === "value")
                    input.kendoMultiSelect({
                        dataTextField: "name",
                        dataValueField: "id",
                        filter: "contains",
                        dataSource: dataSource
                    });
                else
                    input.kendoComboBox({
                        dataTextField: "name",
                        dataValueField: "id",
                        filter: "contains",
                        dataSource: dataSource
                    });
                // 提示信息
                var tooltipElement = $('<span class="k-invalid-msg" data-for="' + options.field + '"></span>');
                tooltipElement.appendTo(container);
            },
            // 数值编辑器
            numberEditor: function (container, options) {
                var input = $("<input type='number' class='k-textbox' name='" + options.field + "'/>");
                input.appendTo(container);
                var tooltipElement = $('<span class="k-invalid-msg" data-for="' + options.field + '"></span>');
                tooltipElement.appendTo(container);
            },
            // 补位
            pad: function (str) {
                str = str + "";
                var pad = "00";
                return (pad.length > str.length ? pad.substring(0, pad.length - str.length) + str : str)
            },
            // 转化字符为数字
            transStrToNum: function (str) {
                str = str.toUpperCase(); // 转换为大写
                return (str.length - 1) * 26 + str.substring(str.length - 1).charCodeAt(0) - "A".charCodeAt(0);
            },
            // 转化数字为字符
            transNumToStr: function (num) {
                for (var i = 0, preStr = ""; i < Math.floor(num / 26); i++) preStr += "A";
                return (preStr + String.fromCharCode(num % 26 + "A".charCodeAt(0)));
            },
            // 查询
            find: function (key, options, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT["find" + this.toUpper(key)],
                    method: "GET",
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            // 新增
            create: function (key, data, cb) {

                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT["create" + this.toUpper(key)],
                    method: "POST",
                    data: data,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });

            },
            // 查询单笔
            read: function (key, id, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT["read" + this.toUpper(key)].replace("#id#", id),
                    method: "GET",
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            // 修改
            update: function (key, data, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT["update" + this.toUpper(key)],
                    method: "PUT",
                    data: data,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            // 删除单表
            deleteOne: function (key, id, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT["delete" + this.toUpper(key)].replace("#id#", id),
                    method: "DELETE",
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            // 删除多笔
            deleteMore: function (key, ids, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT["delete" + this.toUpper(key)],
                    method: "DELETE",
                    data: {
                        ids: ids
                    },
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            getSelectData: function(opts,cb){
                commonService.ajaxSync({
                    url: MASTER_CONSTANT[opts.key],
                    async: false,
                    data: opts.data,
                    success: function (datas) {
                        cb && cb(datas);
                    }
                });
            }
        };
    });
})();