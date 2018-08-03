/**
 * Created by frank.zhou on 2017/04/17.
 */
(function(){
  "use strict";

  angular
    .module("myApp")
    .constant("LOGIN_CONSTANT", {
      "getUserWarehouse": "uaa/login",
      "login": "uaa/oauth/token"
    });
})();