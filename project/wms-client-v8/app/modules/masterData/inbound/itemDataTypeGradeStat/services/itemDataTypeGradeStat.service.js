/**
 * Created by frank.zhou on 2017/05/25.
 */
(function () {
    "use strict";

    angular.module("myApp").service('itemDataTypeGradeStatService', function (commonService, MASTER_CONSTANT) {
        return {
            // 保存刷新多少天的数据
            sureDay: function (dayNumber, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.saveDayNumber.replace("#dayNumber#", dayNumber),
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            // 保存每天什么时间刷新
            sureRefreshTime: function (refreshTime, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.sureRefreshTime.replace("#refreshTime#", refreshTime),
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
            // 获取刷新时间
            getRefreshTime: function (cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.getRefreshTime,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },
        };
    });
})();