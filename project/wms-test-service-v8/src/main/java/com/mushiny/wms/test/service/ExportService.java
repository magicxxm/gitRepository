package com.mushiny.wms.test.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Laptop-9 on 2018/8/13.
 */
public interface ExportService {

    void exportExcel(HttpServletResponse response) throws Exception;

    void createExcel(HttpServletResponse response) throws Exception;

    void importExcel(MultipartFile file) throws Exception;

    void uploadFile(MultipartFile file);

    void downloadFile(String id,HttpServletRequest request,HttpServletResponse response) throws IOException;
}
