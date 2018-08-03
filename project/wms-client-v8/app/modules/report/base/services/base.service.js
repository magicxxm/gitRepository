/**
 * Created by zhihan.dong on 2017/04/17.
 */
(function () {
  "use strict";

  angular.module("myApp").service("reportService", function ($translate, commonService, REPORT_CONSTANT, BACKEND_CONFIG) {
    return {
      // 首字母大写
      toUpper: function (string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
      },
      //动态字段设定
       setColumns: function (data, path) {
        var columnKey = Object.keys(data);
        var columns = [];
        columnKey.map(function (x) {
          if (typeof data[x] == "object") {
            var col = {};
            col.title = x;
            col.columns = this.setColumns(data[x], path ? path + '.' + x : x);
            columns.push(col);
          } else {
            columns.push({
              headerTemplate: '<span translate=' + x + '>'+x+'</span>',
              field: path ? path + '.' + x : x
            });
          }
        }.bind(this));
        return columns;
      },
      //
      toMap: function (value, key) {
        return {
          selectionValue: value,
          resourceKey: $translate.instant(key || value)
        };
      },
      reGrids: function reGrids(url, columns, height) {
        return {
          dataSource:url,
          height:height || ($(document.body).height() - 191),
          sortable:true,
          scrollable:true,
          editable:false,
          columns:columns,
          selectable:"row",

        };
      },

     
      // grid请求(edit)
      editGrid: function (options) {
        return {
          dataSource: options.dataSource,
          selectable: BACKEND_CONFIG.selectModel,
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
      // 取数据源
      getDataSource: function (opts) {
        opts.value == null && (opts.value = "id");
        return {
          serverFiltering: false,
          transport: {
            read: function (options) {
              commonService.ajaxSync({
                url: REPORT_CONSTANT[opts.key],
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
                var data = datas[i],
                  itemMap = {
                    id: data.id
                  };
                itemMap[opts.text] = (opts.text === "resourceKey" ? $translate.instant(data[opts.text]) : data[opts.text]);
                itemMap[opts.value] = data[opts.value];
                items.push(angular.extend(data, itemMap));
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
              var filter = options.data.filter || {},
                filterItems = filter.filters || [];
              for (var i = 0, filterStr = ""; i < filterItems.length; i++) {
                var filterItem = filterItems[i];
                filterStr += filterItem.field + "==" + (filterItem.operator == "contains" ? "*" + filterItem.value + "*" : filterItem.value);
                i != filterItems.length - 1 && (filterStr += ";");
              }
              // data
              var data = {
                page: options.data.page - 1,
                size: options.data.pageSize,
                sort: sort
              };
              filterStr != "" && (data["search"] = filterStr);
              // 取数据
              commonService.ajaxSync({
                url: REPORT_CONSTANT["find" + key],
                async: true,
                data: data,
                success: function (result) {
                  options.success(result);
                }
              });
            }
          },
          filter: REPORT_CONSTANT["find" + key].indexOf("wms") >= 0 ? {} : {
            field: "entityLock",
            operator: "eq",
            value: "0"
          },
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
      // 查询
      find: function (key, options, cb) {
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT["find" + this.toUpper(key)],
          method: "GET",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      // 新增
      create: function (key, data, cb) {
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT["create" + this.toUpper(key)],
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
          url: REPORT_CONSTANT["read" + this.toUpper(key)].replace("#id#", id),
          method: "GET",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      // 修改
      update: function (key, data, cb) {
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT["update" + this.toUpper(key)],
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
          url: REPORT_CONSTANT["delete" + this.toUpper(key)].replace("#id#", id),
          method: "DELETE",
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      },
      // 删除多笔
      deleteMore: function (key, ids, cb) {
        commonService.ajaxMushiny({
          url: REPORT_CONSTANT["delete" + this.toUpper(key)],
          method: "DELETE",
          data: {
            ids: ids
          },
          success: function (datas) {
            cb && cb(datas.data);
          }
        });
      }
    };
  });
})();