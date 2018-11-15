/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  angular.module("myApp")

    .controller('moduleCtl', function ($window, $scope,$state,commonService, $websocket, $translate, $location){

        var wnd = $("#details")
            .kendoWindow({
                title: "提示窗口",
                modal: true,
                visible: false,
                resizable: false,
                width: 400,
                height:200
            }).data("kendoWindow");
        var checkedNodes = []
        $scope.goto=function () {
            $state.go("mainDisplay.installModule",{modules:angular.toJson(checkedNodes)});
        }
        var zipModule={projectVersion:"",
            modules:[]};
        $(".k-primary").kendoButton({  icon: "cancel"});
        $scope.compress=function()
        {
            commonService.ajaxSync({url:"module-manage/compass",data:{modules:angular.toJson(zipModule)},success:
                function(data) {

                    var succ="<div>生成安装包成功</div>"
                    var fail="<div>生成安装包失败</div>"
                    if(data==1)
                    {
                        wnd.content($(succ));
                        wnd.center().open();
                    }else{
                        wnd.content($(fail));
                        wnd.center().open();
                    }
                }
                })
        }
        var inline = new kendo.data.HierarchicalDataSource({
            transport: {
                read: {
                    url:  "module-manage/getAllProject",
                    dataType: "json"
                }
            },
            schema: {
                model: {
                    children: "subModules"
                }
            }
        });

       $("#treeview").kendoTreeView({
            animation: {
                expand: {
                    duration: 600
                }
            },
            dataSource: inline,
            dataTextField: [ "projectVersion", "moduleName" ],
            checkboxes: {
                checkChildren: true
            },

            check: onCheck
        });



        function checkedNodeIds(nodes, checkedNodes) {
            for (var i = 0; i < nodes.length; i++) {
                if (nodes[i].checked&&nodes[i].hasChildren) {
                    var children=nodes[i].children.view();
                    for(var k=0;k<children.length;k++)
                    {
                        if(children[k].checked)
                        {

                            var find=findElem(checkedNodes,"moduleName",children[k].moduleName)
                            if(find==-1)
                            {
                                checkedNodes.push({moduleName:children[k].moduleName,
                                    modulePort:children[k].modulePort,
                                    projectVersion:nodes[i].projectVersion,
                                    moduleVersion:nodes[i].projectVersion,
                                    projectName:nodes[i].projectName
                                })
                            }else{
                                checkedNodes[find].modulePort=children[k].modulePort;
                                checkedNodes[find].projectVersion=nodes[i].projectVersion;
                                checkedNodes[find].moduleVersion=nodes[i].projectVersion;
                                checkedNodes[find].projectName=nodes[i].projectName;
                            }
                            zipModule.projectVersion=nodes[i].projectVersion;
                            if(!zipModule.modules.includes(children[k].moduleName))
                            {
                                zipModule.modules.push(children[k].moduleName);
                            }


                        }
                    }
                }
            }
        }

        function findElem(arrayToSearch,attr,val){
            for (var i=0;i<arrayToSearch.length;i++){
                if(arrayToSearch[i][attr]==val){
                    return i;
                }
            }
            return -1;
        }
        // show checked node IDs on datasource change
        function onCheck() {

           var  treeView = $("#treeview").data("kendoTreeView");

            checkedNodeIds(treeView.dataSource.view(), checkedNodes);

        }

    });
})();