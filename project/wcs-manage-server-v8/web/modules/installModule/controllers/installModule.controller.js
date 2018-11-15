/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  angular.module("myApp")

    .controller('installModuleCtl', function ($window, $scope, commonService, $stateParams,BACKEND_CONFIG, $location){
        $scope.consoleDisplayFlag=false;
        $scope.datas=angular.fromJson($stateParams.modules)
         //   [{moduleName:"driver",moduleVersion:"31231313"},{moduleName:"stow",moduleVersion:"4432432"}]

        var grid = $("#grid").kendoGrid({
            dataSource: {

                data:$scope.datas },

            pageable: false,
            height: "40%",
            columns: [
                { field: "moduleName", title: "模块名称", width: "140px" },
                { field: "moduleVersion", title: "模块版本", width: "140px" },
                { command: [
                    { text: "启动", click: showDetails },{text:"测试",click:test}
                    ,{text:"停止",click:stop}
                    ], title: " ", width: "180px" }]
        }).data("kendoGrid");
        $("#editor").kendoEditor({
            tools: [
                {
                    name: "custom",
                    tooltip: "清空log",
                    exec: function(e) {
                        $scope.editor.value("")
                    }
                }
            ]
        });

        $scope.editor = $("#editor").data("kendoEditor");
    function stop(e)
    {
        e.preventDefault();
        var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
        var succ="<div>停止模块"+dataItem.moduleName+"成功</div>"
        var fail="<div>停止模块"+dataItem.moduleName+"失败</div>"
        commonService.ajaxSync({url:"module-manage/stop",data:{moduleName:dataItem.moduleName},success:
        function(data)
        {
            if(data.result==1)
            {
                wnd.content($(succ));
                wnd.center().open();
            }
            else{
                wnd.content($(fail+data.message));
                wnd.center().open();
            }
           if($scope.editor)
           {
               $scope.editor.exec("inserthtml", { value: "<p>"+data.message+"</p>" });
           }


        },error:
        function(){
            wnd.content($(fail));
            wnd.center().open();
        }
        })

    }


        function test(e) {
            e.preventDefault();
            var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
            var tr=$(e.currentTarget).closest("tr");

            commonService.doLogin("admin","123456","DEFAULT",function(data)
                {
                    $window.localStorage["accessToken"] = data.access_token;
                    $window.localStorage["warehouseId"]="DEFAULT"
                    //   url: dataItem.moduleName
                    var succ="<div>模块安装"+dataItem.moduleName+"成功</div>"
                    var fail="<div>模块安装"+dataItem.moduleName+"失败</div>"
                    commonService.ajaxSync(
                        {
                        url: "module-manage/test", data:{moduleName:dataItem.moduleName},success: function (data) {
                            if(data.result==1)
                            {
                                tr.css("background-color","#3f51b5");
                                wnd.content($(succ));
                                wnd.center().open();
                            }else{
                                tr.css("background-color","#F5EB2D");
                                wnd.content($(fail));
                                wnd.center().open();
                            }
                            if($scope.editor)
                            {
                                $scope.editor.exec("inserthtml", { value: "<p>"+data.message+"</p>" });
                            }

                        },
                            error:function(){
                                tr.css("background-color","#F5EB2D");
                                wnd.content($(fail));
                                wnd.center().open();
                            }
                    })
                }
            )
        }
        var wnd = $("#details")
            .kendoWindow({
                title: "提示窗口",
                modal: true,
                visible: false,
                resizable: false,
                width: 400,
                height:200
            }).data("kendoWindow");
        function showDetails(e) {
            e.preventDefault();

            var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

            var param={projectName:dataItem.projectName,
                projectVersion:dataItem.projectVersion,
                moduleName:dataItem.moduleName,
                moduleVersion:dataItem.moduleVersion,
                modulePort:dataItem.modulePort

            }
            $scope.$apply(function() {
                $scope.consoleDisplayFlag=true;
            });

            commonService.install($scope.editor,param);

        }


    });
})();