/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
  "use strict";

  angular.module("myApp")

    .controller('uploadFileCtl', function ($window, $scope, $websocket,$rootScope,commonService, $translate, $location){
        $scope.consoleDisplayFlag=false;
        $scope.projectName="牧星智能";
        $scope.projectVersion="v.1.0.0";
        $scope.moduleName="wcs-driveallocation-server-v8";
        $scope.moduleVersion="v.1.0.0";
        $scope.modulePort="12004:12004";
        $scope.moduleDir="/home/mslab/wms_v8/wcs-driveallocation-server-v8";
        $scope.moduleLog="/home/mslab/logs:/home/log";
        $("#editor").kendoEditor({

            tools: [
                {
                    name: "custom",
                    tooltip: "安装模块",
                    exec: function(e) {


                        var editor = $(this).data("kendoEditor");
                        commonService.install(editor,angular.toJson($scope.uploadFileParam))


                    }
                }
            ]
        });

        $("#module-file").kendoUpload({
            multiple: false,
            async: {
                saveUrl: "module-manage/uploadModule",

                autoUpload: false
            },
            success: function(e){
                if(e.response.result==1)
                {
                    $scope.$apply(function() {
                        $scope.consoleDisplayFlag=true;
                    });
                }
                else{
                    $scope.$apply(function() {
                        $scope.consoleDisplayFlag=false;
                    });
                }
            },
            upload:function(e)
            {
                $scope.uploadFileParam={projectName:$scope.projectName,
                    projectVersion:$scope.projectVersion,
                    moduleName:$scope.moduleName,
                    modulePort:$scope.modulePort,
                    moduleDir:$scope.moduleDir,
                    moduleLog:$scope.moduleLog

                }

                e.data={uploadParams:angular.toJson($scope.uploadFileParam)}
            },
            localization: {
                "select": "选择上传的项目",
                "uploadSelectedFiles":"上传",
                "headerStatusUploading": "上传中......",
                "statusUploaded": "上传成功",
                "statusFailed": "上传失败"

            }
        });
    });
})();