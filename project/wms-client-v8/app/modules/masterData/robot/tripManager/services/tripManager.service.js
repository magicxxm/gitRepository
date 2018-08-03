(function () {
    "use strict";

    angular.module("myApp").service('tripManagerService', function (commonService) {
        return {
            exportTrip:function(url,cb){
                commonService.ajaxMushiny({
                    url: url,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            }
        };
    });
})();
