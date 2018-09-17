package com.mushiny.wms.test.web;

import com.mushiny.wms.test.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/test")
public class ExcelController {

    private final ExportService exportService;

    @Autowired
    public ExcelController(ExportService exportService) {
        this.exportService = exportService;
    }

    //导出数据为Excel
    @RequestMapping(value="/export-excel",
            method =  RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(HttpServletResponse response) throws Exception {
        exportService.exportExcel(response);
    }

    //生成带有下拉框的Excel
    @RequestMapping(value="/download-excel",
            method =  RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void createExcel(HttpServletResponse response) throws Exception {
        exportService.createExcel(response);
    }

    //导入Excel
    @RequestMapping(value="/import-excel",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        exportService.importExcel(file);
        return ResponseEntity.ok().build();
    }

    //文件上传
    @RequestMapping(value="/upload-file",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file){
        exportService.uploadFile(file);
        return ResponseEntity.ok().build();
    }

    //文件下载 例如下载某一张图片
    @RequestMapping(value="/download-file",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void downloadFile(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String id =request.getParameter("id");
        exportService.downloadFile(id,request,response);
    }
}
