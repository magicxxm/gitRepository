/**
 * Created by thoma.bian on 2017/5/10.
 * Updated by frank.zhou on 2017/05/10.
 */
(function(){
  "use strict";

  angular.module("myApp").service("directJobService", function(commonService, TOT_CONSTANT){
    return {
        getCascadingData: function (callback,typeTable) {
            var getString = "/"+typeTable;
            var key = TOT_CONSTANT.getDJobNames+getString;
            callback(key);
        },
        // 取数据源(特殊)
        getDataSourceMy: function (opts) {
            opts.value == null && (opts.value = "id");
            return {
                serverFiltering: false,
                transport: {
                    read: function(options){
                        commonService.ajaxSync({
                            url: opts.key,
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
        },
    };
  });
})();