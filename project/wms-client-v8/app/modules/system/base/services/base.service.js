/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  angular.module("myApp").service("systemService", function ($translate, commonService, SYSTEM_CONSTANT) {
    return {
      // 首字母大写
      toUpper: function (string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
      },
      //
      toMap: function (value) {
        return {selectionValue: value, resourceKey: $translate.instant(value)};
      },
      // 取数据源
      getDataSource: function (opts) {
        opts.value == null && (opts.value = "id");
        return {
          serverFiltering: false,
          transport: {
            read: function (options) {
              commonService.ajaxSync({
                url: SYSTEM_CONSTANT[opts.key],
                async: true,
                data: opts.data,
                success: function (result) {
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
                items.push(itemMap);
              }
              return items;
            }
          }
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
                url: SYSTEM_CONSTANT["find" + key],
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
      // 查询
      find: function (key, options, cb) {
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT["find" + this.toUpper(key)],
          method: "GET",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      // 新增
      create: function (key, data, cb) {
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT["create" + this.toUpper(key)],
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
          url: SYSTEM_CONSTANT["read" + this.toUpper(key)].replace("#id#", id),
          method: "GET",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      // 修改
      update: function (key, data, cb) {
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT["update" + this.toUpper(key)],
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
          url: SYSTEM_CONSTANT["delete" + this.toUpper(key)].replace("#id#", id),
          method: "DELETE",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      // 删除多笔
      deleteMore: function (key, ids, cb) {
        commonService.ajaxMushiny({
          url: SYSTEM_CONSTANT["delete" + this.toUpper(key)],
          method: "DELETE",
          data: {
            ids: ids
          },
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      // 数值编辑器
      numberEditor: function (container, options) {
        var input = $("<input type='number' class='k-textbox' name='" + options.field + "'/>");
        input.appendTo(container);
        var tooltipElement = $('<span class="k-invalid-msg" data-for="' + options.field + '"></span>');
        tooltipElement.appendTo(container);
      },
      editGrid: function (options) {
        return {
          dataSource: options.dataSource,
          selectable: options.selectModel || "row",
          height: options.height || $(document.body).height()-150,
          scrollable: true,
          pageable: false,
          editable: options.editable != null ? options.editable : {confirmation: false, mode: options.mode || "incell"},
          columns: options.columns
        };
      }
    };
  });
})();