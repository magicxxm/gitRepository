/**
 * Created by frank.zhou on 2017/04/24.
 */
(function () {
    "use strict";

    angular.module("myApp").service('podService', function (commonService, MASTER_CONSTANT) {
        return {
            // 取区域
            getZone: function (clientId, sectionId, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.getZone + "?clientId=" + clientId + "&sectionId=" + sectionId,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },

            getPlaceMark: function (id, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.getPodPlaceMark + "?id=" + id,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });

            },
            // 取area
            getArea: function (clientId, cb) {
                commonService.ajaxMushiny({
                    url: MASTER_CONSTANT.getArea + "?clientId=" + clientId,
                    success: function (datas) {
                        cb && cb(datas.data);
                    }
                });
            },

        };
    });
})();