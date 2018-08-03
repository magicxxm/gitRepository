(function () {
    "use strict";

    angular.module("myApp").service('robotLaveBatteryService', function (commonService, MASTER_CONSTANT) {
        return {
            robotLaveBattery : function (id,success,error) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.getRobotBattery+"?robotId="+id,
                    success: function (datas) {
                        success && success(datas.data);
                    },
                    error:function (datas) {
                        error && error(datas.data);
                    }
                });
            }
        };
    });
})();
