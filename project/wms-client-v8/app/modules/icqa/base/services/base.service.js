/**
 * Created by thoma.bian on 2017/5/10.
 * Updated by frank.zhou on 2017/5/22.
 */

(function(){
  "use strict";

  angular.module("myApp").service("ICQABaseService", function($translate, commonService, ICQA_CONSTANT){

    function grid(url,columns,height){
      return {
        selectable: "row",
        dataSource: url,
        height: height || $(document.body).height() - 191,
        sortable: true,
        scrollable: true,
        columns: columns
      };
    }
    function grids(options,cb){
    var menu = false;
      if(cb){
        menu= {columns: true, sortable: false}
      }else{
        menu = false
      }
      return {
        dataSource: options.dataSource,
        selectable: "row",
        height: options.height || $(document.body).height() - 191,
        sortable: true,
        scrollable: true,
        columns: options.columns,
        editable: true,
        columnMenu:menu,
        dataBound: function(e) {
          cb && cb(e);
        }
        };
    }

    // 首字母大写
    function toUpper(string){
      return string.substring(0, 1).toUpperCase()+ string.substring(1);
    }
    //
    function toMap(value){
      return {selectionValue: value, resourceKey: $translate.instant(value)};
    }
    // 取数据源
    function getDataSource(opts) {
      debugger;
      opts.value == null && (opts.value = "id");
      return {
        serverFiltering: false,
        transport: {
          read: function(options){
            commonService.ajaxSync({
              url: ICQA_CONSTANT[opts.key],
              async: true,
              data: opts.data,
              success: function(result){
                 options.success(result);
              }
            });
          }
        },
        schema: {
          data: function(datas){
            for(var i = 0, items = []; i < datas.length; i++){
              var data = datas[i], itemMap = {id: data.id};
              itemMap[opts.text] = (opts.text === "resourceKey"? $translate.instant(data[opts.text]): data[opts.text]);
              itemMap[opts.value] = data[opts.value];
              items.push(angular.extend(data, itemMap));
            }
            return items;
          }
        }
      };
    }
    // grid数据源
    function getGridDataSource(key){
      key = toUpper(key);
      return {
        transport: {
          read: function(options){
            // 排序
            var sort = options.data.sort || [];
            sort && sort.length && (sort = sort[0].field+ ","+ sort[0].dir);
            // 过滤
            var filter = options.data.filter || {}, filterItems = filter.filters || [];
            for(var i = 0, filterStr = ""; i < filterItems.length; i++){
              var filterItem = filterItems[i];
              filterStr += filterItem.field+ "=="+ (filterItem.operator == "contains"? "*"+ filterItem.value+ "*": filterItem.value);
              i != filterItems.length-1 && (filterStr += ";");
            }
            // 取数据
            commonService.ajaxSync({
              url: ICQA_CONSTANT["find"+ key],
              async: true,
              data: {
                page: options.data.page-1,
                size: options.data.pageSize,
                sort: sort,
                search: filterStr
              },
              success: function(result){
                console.log(result)
                options.success(result);

              }

            });
          }
        },
        filter: {field: "entityLock", operator: "eq", value: "0"},
        schema: {
          data: function(response){
            return response.content;
          },
          total: function(response){
            return response.totalElements;
          }
        },
        serverAggregates: true,
        serverPaging: true,
        serverFiltering: true,
        serverSorting: true
      };
    }

      // grid数据源
      function getGridDataSource1(url){
        debugger;
          // key = toUpper(key);
          return {
              transport: {
                  read: function(options){
                      // 排序
                      var sort = options.data.sort || [];
                      sort && sort.length && (sort = sort[0].field+ ","+ sort[0].dir);
                      // 过滤
                      var filter = options.data.filter || {}, filterItems = filter.filters || [];
                      for(var i = 0, filterStr = ""; i < filterItems.length; i++){
                          var filterItem = filterItems[i];
                          filterStr += filterItem.field+ "=="+ (filterItem.operator == "contains"? "*"+ filterItem.value+ "*": filterItem.value);
                          i != filterItems.length-1 && (filterStr += ";");
                      }
                      // 取数据
                      commonService.ajaxSync({
                          url: url,
                          async: true,
                          data: {
                              page: options.data.page-1,
                              size: options.data.pageSize,
                              sort: sort,
                              search: filterStr
                          },
                          success: function(result){
                              console.log(result)
                              options.success(result);

                          }

                      });
                  }
              },
              filter: {field: "entityLock", operator: "eq", value: "0"},
              schema: {
                  data: function(response){
                      return response.content;
                  },
                  total: function(response){
                      return response.totalElements;
                  }
              },
              serverAggregates: true,
              serverPaging: true,
              serverFiltering: true,
              serverSorting: true
          };
      }


    // grid请求(edit)
    function editGrid(options,cb){
      var menu = false;
      if(cb){
        menu= {columns: true, sortable: false}
      }else{
        menu = false
      }
      return {
        dataSource: options.dataSource,
        selectable: options.selectModel || "row",
        height: options.height || 300,
        sortable: true,
        scrollable: true,
        pageable: false,
        columnMenu:menu,
        editable: options.editable!= null? options.editable: {confirmation: false, mode: options.mode || "incell"},
        columns: options.columns,
        dataBound: function(e) {
          cb && cb(e);
        }
      };
    }
    // 列表编辑器
    function selectEditor(container, options, dataSource){
      // 创建input
      var requireStr = "";
      if(["storageLocationType", "dropZone", "receiveDestination"].indexOf(options.field) >= 0)
        requireStr = "required validationMessage=\"{{'WARNING_NAME' | translate }}\"";
      var input = $('<input '+ requireStr+ ' name="' + options.field + '"/>');
      input.appendTo(container);
      // 输入控件
      if(["face", "positionState"].indexOf(options.field) >= 0)
        input.kendoComboBox({dataTextField: "resourceKey", dataValueField: "selectionValue", filter: "contains", dataSource: dataSource});
      else if(options.field === "operator")
        input.kendoComboBox({filter: "contains", dataSource: dataSource});
      else if(options.field === "compKey"){
        if(options.model.receiveCategoryRule.decisionKey === "ZONE_TYPE") input.kendoComboBox({filter: "contains", dataSource: dataSource});
        else input.kendoMultiSelect({dataTextField: "name", dataValueField: "id", filter: "contains", dataSource: dataSource});
      }else if(options.field === "value")
        input.kendoMultiSelect({dataTextField: "name", dataValueField: "id", filter: "contains", dataSource: dataSource});
      else
        input.kendoComboBox({dataTextField: "name", dataValueField: "id", filter: "contains", dataSource: dataSource});
      // 提示信息
      var tooltipElement = $('<span class="k-invalid-msg" data-for="'+ options.field + '"></span>');
      tooltipElement.appendTo(container);
    }
    // 数值编辑器
    function numberEditor(container, options){
      var input = $("<input type='number' class='k-textbox' name='" + options.field + "'/>");
      input.appendTo(container);
      var tooltipElement = $('<span class="k-invalid-msg" data-for="'+ options.field + '"></span>');
      tooltipElement.appendTo(container);
    }

    // 查询
    function find(key, options, cb){
      commonService.ajaxMushiny({
        url: ICQA_CONSTANT["find"+ toUpper(key)],
        method: "GET",
        success: function(datas){
          cb && cb(datas.data);
        }
      });
    }
    // 新增
    function create(key, data, cb){
      commonService.ajaxMushiny({
        url: ICQA_CONSTANT["create"+ toUpper(key)],
        method: "POST",
        data: data,
        success: function(datas){
          cb && cb(datas.data);
        }
      });
    }
    // 查询单笔
    function read(key, id, cb){
      commonService.ajaxMushiny({
        url: ICQA_CONSTANT["read"+ toUpper(key)].replace("#id#", id),
        method: "GET",
        success: function(datas){
          cb && cb(datas.data);
        }
      });
    }
    // 修改
    function update(key, data, cb){
      commonService.ajaxMushiny({
        url: ICQA_CONSTANT["update"+ toUpper(key)],
        method: "PUT",
        data: data,
        success: function(datas){
          cb && cb(datas.data);
        }
      });
    }
    // 删除单表
    function deleteOne(key, id, cb){
      commonService.ajaxMushiny({
        url: ICQA_CONSTANT["delete"+ toUpper(key)].replace("#id#", id),
        method: "DELETE",
        success: function(datas){
          cb && cb(datas.data);
        }
      });
    }

      function getSelect(opts,cb){
          commonService.ajaxSync({
              url: ICQA_CONSTANT[opts.key],
              async: false,
              data: opts.data,
              success: function (datas) {
                  cb && cb(datas);
              }
          });
      }

      // getSelect: function(opts,cb){
      //     commonService.ajaxSync({
      //         url: ICQA_CONSTANT[opts.key],
      //         async: false,
      //         data: opts.data,
      //         success: function (datas) {
      //             cb && cb(datas);
      //         }
      //     });
    //  }


    return {
        getGridDataSource1:getGridDataSource1,
      grid: grid,
      grids:grids,
      toUpper: toUpper,
      toMap: toMap,
      getDataSource: getDataSource,
      getGridDataSource: getGridDataSource,
      editGrid:editGrid,
      numberEditor:numberEditor,
      selectEditor:selectEditor,
      find: find,
      create: create,
      read: read,
      update: update,
      deleteOne: deleteOne,
        getSelect: getSelect
    };
  });
})();


