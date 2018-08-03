/**
 * Created by frank.zhou on 2017/04/14.
 * Updated by frank.zhou on 2017/07/27.
 */
(function () {
    "use strict";
    /*angular
     .module("myApp")
     .constant("BACKEND_CONFIG", {
     andon: "http://192.168.1.203:11008/",
     icqa: "http://192.168.1.203:11005/",
     inbound: "http://192.168.1.203:11003/",
     inboundProblem: "http://192.168.1.203:11009/",
     internalTool: "http://192.168.1.203:11007/",
     login: "http://192.168.1.203:8001/",
     main: "http://192.168.1.203:11000/",
     masterData: "http://192.168.1.203:11002/",
     outbound: "http://192.168.1.203:11004/",
     outboundProblem: "http://192.168.1.203:11010/",
     report: "http://192.168.1.203:11006/",
     system: "http://192.168.1.203:11001/",
     tot: "http://192.168.1.203:11088/",
     wcs:"http://192.168.1.203:12011/",
     wcsPod:"http://192.168.1.203:12009/",
     callPod:"http://192.168.1.203:12001/",
     websocket:"ws://192.168.1.203:8008/",
     replenish:"http://192.168.1.203:11011/"

     });*/
    var pattern = /(http|https):\/\/(.+):[0-9]+/;
    var match = window.location.origin.match(pattern)
    if (!$.isEmptyObject(match)) {
        var ip=match[2];
        // var ip = "192.168.1.202";

    } else {
        var ip = "192.168.1.203";
    }
    angular
        .module("myApp")
        .constant("BACKEND_CONFIG", {
            comonIp: ip,
            andon: "http://" + ip + ":11008/",
            icqa: "http://" + ip + ":11005/",
            inbound: "http://" + ip + ":11003/",
            internalTool: "http://" + ip + ":11007/",
            inboundProblem: "http://" + ip + ":11009/",
            login: "http://" + ip + ":8001/",
            masterData: "http://" + ip + ":11002/",
            outbound: "http://" + ip + ":11004/",
            outboundProblem: "http://" + ip + ":11010/",
            report: "http://" + ip + ":11006/",
            system: "http://" + ip + ":11001/",
            transfer: "http://" + ip + ":11089/",
            tot: "http://" + ip + ":11088/",
            wcs: "http://" + ip + ":12011/",
            wcsPod: "http://" + ip + ":12009/",
            callPod: "http://" + ip + ":12001/",
            websocket: "ws://" + ip + ":8008/",
            replenish: "http://" + ip + ":11011/"

        });
})();