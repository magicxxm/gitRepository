/**
 * Created by preston.zhang on 2017/8/18.
 */
(function () {
    "use strict";
    angular.module("myApp").constant("TRANSFERIN_CONSTANTS",{
      // transferIn
      "addTransferOrderPosition":"transferIn/addTransferOrderPosition",
      "getTransferInOrders":"transferIn/getTransferOrders",
      "getConditionOrders":"transferIn/getConditionOrders",
      "activeTransferOrders":"transferIn/activeTransferOrders",
      "closeTransferOrders":"transferIn/closeTransferOrders",
      "addTransferOrders":"transferIn/addTransferOrders",
      "getTransferInContainerSurvey":"transferIn/getTransferInContainerSurvey",
      "getTransferInContainerDetail":"transferIn/getTransferInContainerDetail",
      "getTransferInProConSurvey":"transferIn/getTransferInProConSurvey",
      "getTransferInProConDetail":"transferIn/getTransferInProConDetail",
      "getTransferInDiffDetail":"transferIn/getTransferInDiffDetail",
      "deleteTransferOrders":"transferIn/deleteTransferOrders"

    });
})();