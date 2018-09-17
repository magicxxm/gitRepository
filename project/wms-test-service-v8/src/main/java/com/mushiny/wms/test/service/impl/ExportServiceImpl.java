package com.mushiny.wms.test.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.ExcelUtil;
import com.mushiny.wms.test.service.ExportService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Laptop-9 on 2018/8/13.
 */

@Service
public class ExportServiceImpl implements ExportService{

    private final static Logger log = LoggerFactory.getLogger(ExportServiceImpl.class);
    private Pattern imgPattern = Pattern.compile(".png|.jpg");

    @Override
    public void exportExcel(HttpServletResponse response) throws Exception {
        String[] headArray = {"商品编号","商品名称","状态","数量","仓库","客户"};
        List<Object[]> dtos = new ArrayList<>();
        ExcelUtil.exportExcel(response, headArray,dtos,"库存");
    }

    @Override
    public void createExcel(HttpServletResponse response) throws Exception {
        String fileName = "员工信息模板";
        //列标题
        List list = Arrays.asList("姓名","性别","民族");
        //将list转数组 test
        String[] test = (String[]) list.toArray(new String[0]);

        String[] handers = {"姓名","性别","民族"};
        //下拉框数据
        String[]  str1 = {"男","女","未知"};
        String[]  str2 = {"汉族","回族","藏族","苗族"};
        try {
            ExcelUtil.getExcelTemplate(fileName, handers, str1, str2, response);
        } catch (Exception e) {
            log.error("下载模板异常：" + e.getMessage());
        }
    }

    @Override
    public void importExcel(MultipartFile file) throws Exception {
        //获取文件名
        String name=file.getOriginalFilename();
        boolean isExcel2003=true;
        if(!ExcelUtil.isExcel2003(name) && ! ExcelUtil.isExcel2007(name)){
            throw new ApiException("不是Excel文件格式");
        }
        if(ExcelUtil.isExcel2007(name)){
            isExcel2003 = false;
        }
        //获取文件大小
        long size=file.getSize();
        if(size==0)
            throw new ApiException("文件为空");
        ExcelUtil.createExcel(file.getInputStream(),isExcel2003);
    }

    @Override
    public void uploadFile(MultipartFile file) {
        //获取文件名
        String name=file.getOriginalFilename();
        Matcher matcher = imgPattern.matcher(name);
        if(!matcher.matches()){
            throw new ApiException("文件格式错误");
        }
        //获取文件大小
        long size=file.getSize();
        if(size==0)
            throw new ApiException("文件为空");
        //设置文件上传目录
         String path = "D:" + File.separator + "image" + File.separator;
         File uploadFile = new File(path);
         if(!uploadFile.getParentFile().exists()){
             uploadFile.getParentFile().mkdirs();
         }
        try {
            FileUtils.touch(uploadFile);
            //获取文件输出流
            FileOutputStream fs =new FileOutputStream(path);
            //缓冲输出流
            BufferedOutputStream bos = new BufferedOutputStream(fs);
            FileCopyUtils.copy(file.getInputStream(),bos);
            log.info("文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadFile(String id, HttpServletRequest request,HttpServletResponse response) throws IOException {
        //文件路径 可配置
        String path="...."+"\\"+id+".jpg";
        response.reset();
        response.setContentType("image/jpg;charset=GB2312");
        //输出流
        OutputStream os = response.getOutputStream();
        //文件输入流
        FileInputStream fs =new FileInputStream(path);
        //输入缓冲流
        BufferedInputStream bis =new BufferedInputStream(fs);
        //输出缓冲流
        BufferedOutputStream bos =new BufferedOutputStream(os);
        byte data[] = new byte[2048];
        int size = bis.read(data);
        while(size!=-1){
            bos.write(data,0,size);
            size = bis.read(data);
        }
        bis.close();
        bos.flush();
        bos.close();
        os.close();
        fs.close();
    }
}
