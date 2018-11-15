/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  angular.module("myApp")

    .controller('downLoadFileCtl', function ($window, $scope, $websocket,$rootScope,commonService, $translate, $location){
        $scope.datas=[];
        commonService.ajaxSync({url:"module-manage/getAllPackage",success:
            function(data) {
                $scope.datas=data;
                var grid = $("#grid").kendoGrid({
                    dataSource: {

                        data:$scope.datas },

                    pageable: false,
                    height: "40%",
                    columns: [
                        { field: "projectVersion", title: "安装包版本", width: "140px" },
                        { field: "projectName", title: "安装包", width: "140px" },

                        { command: [
                            { text: "下载", click: download }
                        ], title: " ", width: "180px" }]
                }).data("kendoGrid");
            }
        })



        function download(e)
        {

                try{
                    e.preventDefault();
                    var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
                    var url="module-manage/download?fileName="+dataItem.projectVersion+"/"+dataItem.projectName;
                    var elemIF = document.createElement("iframe");
                    elemIF.src = url;
                    elemIF.style.display = "none";
                    document.body.appendChild(elemIF);
                }catch(e){

                }

        }
    });
})();