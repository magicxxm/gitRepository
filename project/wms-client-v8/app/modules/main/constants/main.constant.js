/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  angular
    .module("myApp")
    .constant("MAIN_CONSTANT", {
      //"getMenus": "modules/main/data/mainMenu.json",
      "getMenus": "system/menus",
      "getUser": "system/users",
      "changePassword": "system/users/change-password"
    });
})();
