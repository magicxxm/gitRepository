/**
 * Created by frank.zhou on 2017/05/02.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("callPodCtl", function ($scope, $rootScope, $window, masterService, callPodService, commonService) {
        // ===================================================workstation====================================================
        function getLocalStorage(key,defaultValue) {
            return $window.localStorage[key] || defaultValue
        }
        $window.localStorage["currentItem"] = "callPodCtl";
        $scope.podName= getLocalStorage("IcqapodName","");
        $scope.face=getLocalStorage("Icqaface","").split(',');
        $scope.stationName=getLocalStorage("IcqastationName","");
        $scope.faceStr = "";
        $scope.pod = getLocalStorage("Icqapod","");
        $scope.stationId = getLocalStorage("IcqastationId","");
        $scope.workStationId = getLocalStorage("IcqaworkStationId","");
        $scope.sectionId = getLocalStorage("IcqasectionId","");
        $("#multiselect").kendoMultiSelect();

        var multiselect = $("#multiselect").data("kendoMultiSelect");

        multiselect.value($scope.face);
        //multiselect.trigger("change");
        $scope.$watch('podName',function (newValue,oldValue) {
            if (newValue === oldValue) { return; }
            $window.localStorage["IcqapodName"]=newValue;
        },true);
        $scope.$watch('sectionId',function (newValue,oldValue) {
            if (newValue === oldValue) { return; }
            $window.localStorage["IcqasectionId"]=newValue;
        },true);
        $scope.$watch('face',function (newValue,oldValue) {
            if (newValue === oldValue) { return; }
            $window.localStorage["Icqaface"]=newValue;
        },true);

        $scope.$watch('stationName',function (newValue,oldValue) {
            if (newValue === oldValue) { return; }
            $window.localStorage["IcqastationName"]=newValue;
        },true);
        $scope.$watch('pod',function (newValue,oldValue) {
            if (newValue === oldValue) { return; }
            $window.localStorage["Icqapod"]=newValue;
        },true);
        $scope.$watch('stationId',function (newValue,oldValue) {

            if (newValue === oldValue) { return; }
            $window.localStorage["IcqastationId"]=newValue;
        },true);
        $scope.$watch('workStationId',function (newValue,oldValue) {

            if (newValue === oldValue) { return; }
            $window.localStorage["IcqaworkStationId"]=newValue;
        },true);


        $scope.validate = function (event) {
            $scope.faceStr="";
            if(event.which!=13)
            {
                if ($scope.face == "" || $scope.face == "undefined"){
                    $scope.faceStr = "A"+","+ "C";
                }else{
                    for(var i=0;i<$scope.face.length;i++){
                        $scope.faceStr+=$scope.face[i]+",";
                    }
                }
                // if( $scope.face.length!=0){
                //     for(var i=0;i<$scope.face.length;i++){
                //         $scope.faceStr+=$scope.face[i]+",";
                //     }
                // }
                event.preventDefault();
                callPodService.callPod($scope.podName,$scope.faceStr,$scope.stationName, function (data) {
                    var win = $("#mushinyWindow").data("kendoWindow");
                    commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                        setTimeout(function(){
                            $("#warnContent").html(data.message)
                        }, 200);
                    }});
                    if(data.state!='fail')
                    {
                        $scope.pod = data.pod;
                        $scope.stationId = data.stationId;
                        $scope.workStationId = data.workStationId;
                        $scope.sectionId = data.sectionId;
                    }

                });
            }


        };

        $scope.clearPod=function(e)
        {
            e.preventDefault();
            $window.localStorage["IcqapodName"]='';
            $window.localStorage["IcqasectionId"]='';
            $window.localStorage["Icqaface"]='';
            $window.localStorage["IcqastationName"]='';
            $window.localStorage["Icqapod"]='';
            $window.localStorage["IcqastationId"]='';
            $window.localStorage["IcqaworkStationId"]='';
            $scope.podName= getLocalStorage("IcqapodName","");
            $scope.face=getLocalStorage("Icqaface","").split(',');
            $scope.stationName=getLocalStorage("IcqastationName","");
            $scope.pod = getLocalStorage("Icqapod","");
            $scope.stationId = getLocalStorage("IcqastationId","");
            $scope.workStationId = getLocalStorage("IcqaworkStationId","");
            $scope.sectionId = getLocalStorage("IcqasectionId","");
            var multiselect = $("#multiselect").data("kendoMultiSelect");

            multiselect.value($scope.face);
          //  multiselect.trigger("change");
        }

       $scope.freePod = function (e) {
         e.preventDefault();
         if(!$.isEmptyObject($scope.sectionId)&&!$.isEmptyObject($scope.workStationId))
         {
             callPodService.reservePod($scope.pod,$scope.sectionId,"false",$scope.workStationId,$scope.stationId,function (data) {
                 var win = $("#mushinyWindow").data("kendoWindow");
                 commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                     setTimeout(function(){
                         $("#warnContent").html("释放pod成功")
                     }, 200);
                 }});
             },function (data) {
                 var win = $("#mushinyWindow").data("kendoWindow");
                 commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                     setTimeout(function(){
                         $("#warnContent").html("释放pod失败")
                     }, 200);
                 }});
             })
         }else{
             var win = $("#mushinyWindow").data("kendoWindow");
             commonService.dialogMushiny(win, {width: 320, height: 160, type: "warn", open: function(){
                 setTimeout(function(){
                     $("#warnContent").html("释放pod失败")
                 }, 200);
             }});
         }

         }
    })
    ;
})();