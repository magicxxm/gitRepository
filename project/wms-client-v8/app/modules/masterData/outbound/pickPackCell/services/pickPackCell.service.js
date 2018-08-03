(function() {
    "use strict";

    angular.module("myApp").service('pickPackCellService', function (commonService,MASTER_CONSTANT) {
        return {
            // 批量修改电子标签
            updateMore: function(data, cb){
                commonService.ajaxMushiny({
                    url:MASTER_CONSTANT.batchUpdate,
                    data: data,
                    method: "PUT",
                    success: function(datas){
                        cb && cb(datas.data);
                    }
                });
            }
        };
    });
})();
