package com.mushiny.wms.common.utils;

import com.mushiny.wms.common.entity.Client;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Laptop-9 on 2018/8/13.
 */
public class ExcelUtil {
    public static void exportExcel(HttpServletResponse response ,String[] headArray,
                                   List<Object[]> contentList, String fileName) throws Exception {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("库存");
        //设置列宽
        sheet.setDefaultColumnWidth((short) 18);
        //创建第一行的对象，第一行一般用于填充标题内容。从第二行开始一般是数据
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headArray.length; i++) {
            //创建单元格，每行多少数据就创建多少个单元格
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headArray[i]);
            //给单元格设置内容
            cell.setCellValue(text);
        }
        //遍历集合，将每个集合元素对象的每个值填充到单元格中
        for(int i=0;i<contentList.size();i++){
            Object[] object=contentList.get(i);
            //从第二行开始填充数据
            row = sheet.createRow(i+1);
            for(int j=0;j<object.length;j++){
                HSSFCell cell = row.createCell(j);
                if(object[j]!=null) {
                    HSSFRichTextString richString = new HSSFRichTextString(object[j].toString());
                    cell.setCellValue(richString);
                }else{
                    cell.setCellValue("");
                }
            }
        }
        // 设置输出的格式
        response.reset();
        response.setCharacterEncoding(getSystemFileCharset());
        response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("UTF-8"),"ISO8859-1")+".xls");
        response.setContentType("application/ms-excel;charset=UTF-8");
        //将文件下载到本地
//        FileOutputStream output=new FileOutputStream("d:\\库存.xls");
//        workbook.write(output);
//       output.close();

       //将workbook中的内容写入输出流中,供浏览器下载
       OutputStream output= response.getOutputStream();
       workbook.write(output);
       output.close();
    }

    //获取系统的字体
    public static String getSystemFileCharset(){
        Properties pro = System.getProperties();
        return pro.getProperty("file.encoding");
    }

    public static void getExcelTemplate(String fileName,String[] handers,String[] str1,String[] str2, HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //表头样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
        //字体样式
        HSSFFont font = workbook.createFont();
        //字体大小
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        HSSFSheet sheet = workbook.createSheet(fileName);
        //列宽
        sheet.setDefaultColumnWidth((short) 20);
        //列高
        sheet.setDefaultRowHeight((short)350);
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < handers.length; i++) {
            //创建单元格，每行多少数据就创建多少个单元格
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(handers[i]);
            //给单元格设置内容
            cell.setCellValue(text);
            cell.setCellStyle(style);
        }
        //设置第二列的1-65535行为下拉列表 创建第一个下拉框
        CellRangeAddressList regions1 = new CellRangeAddressList(1, 65535, 1, 1);
        //创建下拉列表数据
        DVConstraint constraint1 = DVConstraint.createExplicitListConstraint(str1);
        //绑定
        HSSFDataValidation dataValidation1 = new HSSFDataValidation(regions1, constraint1);
        //设置第三列的1-65535行为下拉列表 创建第二个下拉框
        CellRangeAddressList regions2 = new CellRangeAddressList(1, 65535, 2, 2);
        DVConstraint constraint2 = DVConstraint.createExplicitListConstraint(str2);
        HSSFDataValidation dataValidation2 = new HSSFDataValidation(regions2, constraint2);

        sheet.addValidationData(dataValidation1);
        sheet.addValidationData(dataValidation2);
       //写入文件
//        FileOutputStream fileOut;
//        try {
//            fileOut = new FileOutputStream("d:\\员工信息模板.xls");
//            workbook.write(fileOut);
//            fileOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        response.reset();
        response.setCharacterEncoding(getSystemFileCharset());
        try {
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("UTF-8"),"ISO8859-1")+".xls");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/ms-excel;charset=UTF-8");
        OutputStream output = response.getOutputStream();
        workbook.write(output);
        output.close();
    }

    //Excel是否是2003版
    public static boolean isExcel2003(String name){
        return name.matches("^.+\\.(?i)(xls)$");
    }

    //Excel是否是2007版x
    public static boolean isExcel2007(String name){
        return name.matches("^.+\\.(?i)(xlsx)$");
    }

    //以导入客户为例
    public static List<Client> createExcel(InputStream input, boolean isExcel2003) throws IOException {
        Workbook workbook = null;
        // 当excel是2003时,创建excel2003
        if (isExcel2003) {
            workbook = new HSSFWorkbook(input);
         // 当excel是2007时,创建excel2007
        } else {
            workbook = new XSSFWorkbook(input);
        }
        // 读取Excel里面客户的信息
        return readExcelValue(workbook);
    }

    public static List<Client> readExcelValue(Workbook workbook) {
        // 得到第一个shell
        Sheet sheet = workbook.getSheetAt(0);
        // 得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        int totalColumns = 0;
        if (totalRows > 1 && sheet.getRow(0) != null) {
            totalColumns = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < totalRows; i++) {
            Row row = sheet.getRow(1);
            if (row == null) {
                continue;
            }
            Client client = new Client();
            //遍历列
            for (int j = 0; j < totalColumns; j++) {
                Cell cell = row.getCell(j);
                if (cell != null) {
                    //判断每个cell 值的类型
                    if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                        String name = String.valueOf(cell.getNumericCellValue());
                        client.setClientNo(name);
                    } else {
                        client.setClientNo(cell.getStringCellValue());
                    }
                    //获取cell 的值 ，保存到数据库
                    clients.add(client);
                }
            }
        }
        return clients;
    }


}
