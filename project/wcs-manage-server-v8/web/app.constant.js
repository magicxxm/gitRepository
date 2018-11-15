/**
 * Created by frank.zhou on 2017/04/14.
 * Updated by frank.zhou on 2017/07/27.
 */
(function(){
  "use strict";
  angular
    .module("myApp")
    .constant("BACKEND_CONFIG", {
        webSocket:"ws://192.168.1.249:8124/",
        main:"http://192.168.1.249:8124/",
        path:"http://192.168.1.249:12003/",
        login: "http://192.168.1.249:8001/",
        driver:"http://192.168.1.249:12004/"
    });
})();