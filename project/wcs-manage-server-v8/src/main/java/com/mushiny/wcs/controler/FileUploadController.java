package com.mushiny.wcs.controler;

import com.mushiny.wcs.Bean.Message;
import com.mushiny.wcs.business.ModuleBusiness;
import com.mushiny.wcs.business.ProjectBusiness;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
public class FileUploadController {
    @Autowired
    private ModuleBusiness moduleBusiness;
    @Autowired
    private ProjectBusiness projectBusiness;

    @RequestMapping(value = "/module-manage/uploadModule", method = RequestMethod.POST)
    public ResponseEntity<Message> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("uploadParams") String uploadParams) {
        Message result = new Message();
        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
            Map<String, String> tt = JSONUtil.jsonToMap(uploadParams);
            if (!CollectionUtils.isEmpty(tt)) {
                tt.put("moduleDir", "/home/mslab/wms_v8/" + tt.get("moduleName"));
            }
            moduleBusiness.uploadFile(file, tt, result);
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/module-manage/stop", method = RequestMethod.GET)
    public ResponseEntity<Message> uploadFile(@RequestParam("moduleName") String moduleName) {
        Message result = new Message();
        moduleBusiness.stop(moduleName, result);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/module-manage/getAllProject", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getAllProject() {
        List<Map<String, Object>> result = projectBusiness.getAllProject();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/module-manage/download")
    public ResponseEntity<byte[]> download(@RequestParam("fileName") String modules) {

        File file = moduleBusiness.downLoad(modules);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        byte[] data = new byte[0];
        try {
            data = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(data,
                headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/module-manage/compass", method = RequestMethod.GET)
    public ResponseEntity<Integer> compass(@RequestParam("modules") String modules) {
        Integer result = 1;
        Map<String, Object> tt = JSONUtil.jsonToMap(modules);
        String version = (String) tt.get("projectVersion");
        List<String> modu = (List<String>) tt.get("modules");
        File file = moduleBusiness.compass(version, modu);
        if (ObjectUtils.isEmpty(file)) {
            result = 0;
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/module-manage/getAllPackage", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, String>>> getAllPackage() {
        List<Map<String, String>> result = moduleBusiness.getAllPackage();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/module-manage/test", method = RequestMethod.GET)
    public ResponseEntity<Message> test(@RequestParam("moduleName") String moduleName) {
        Message result = new Message();
        moduleBusiness.test(moduleName, result);
        return ResponseEntity.ok(result);
    }


}
