/**
 * Created by preston.zhang on 2017/8/18.
 */
(function () {
    "use strict";
    angular.module("myApp").constant("TRANSOUT_CONSTANTS",{
        "getTransOutConfigList":"transout/transoutconfig",
        "ReadTransOutConfig":"transout/transoutconfig/",
        "CreateTransOutConfig":"transout/transoutconfig/",
        "UpdateTransOutConfig":"transout/transoutconfig/",
        "DeleteTransOutConfig":"transout/transoutconfig/",
        "importTransOutConfigData":"transout/transoutconfig/import/importData",
        "exportTransOutConfigData":"transout/transoutconfig/export/exportData",
        "transOutDetail":"transout/transoutdetail",
        "transOutMainPage":"transout/mainpage/"
    });
})();