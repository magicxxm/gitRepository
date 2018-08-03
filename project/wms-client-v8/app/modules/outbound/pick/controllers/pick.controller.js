/**
 * Created by frank.zhou on 2017/01/17.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("pickCtl", function ($window) {
        $window.localStorage["currentItem"] = "pick";
        $("#pick_user").html($window.localStorage["name"]); // 当前用户
    })

})();