package com.mushiny.wms.masterdata.mdbasics.util;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataGlobalDTO;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemGroupRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemUnitRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hgf on 2017/8/4.
 */
public class ReadExcelItemDataGlobal {
    private final ItemGroupRepository itemGroupRepository;
    private final ItemUnitRepository itemUnitRepository;
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;

    //构造方法
    public ReadExcelItemDataGlobal(ItemGroupRepository itemGroupRepository, ItemUnitRepository itemUnitRepository) {
        this.itemGroupRepository = itemGroupRepository;
        this.itemUnitRepository = itemUnitRepository;
    }

    //获取总行数
    public int getTotalRows() {
        return totalRows;
    }

    //获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    //获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }

    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param
     * @return
     */
    public List<ItemDataGlobalDTO> getExcelInfo(MultipartFile mFile){
        String fileName = mFile.getOriginalFilename();//获取文件名
        if (!validateExcel(fileName)) {// 验证文件名是否合格
            return null;
        }
        boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
        if (isExcel2007(fileName)) {
            isExcel2003 = false;
        }
        List<ItemDataGlobalDTO> itemDataGlobalList = null;
        try {
            itemDataGlobalList = createExcel(mFile.getInputStream(), isExcel2003);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemDataGlobalList;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<ItemDataGlobalDTO> createExcel(InputStream is, boolean isExcel2003){
        Workbook wb = null;
        if (isExcel2003) {// 当excel是2003时,创建excel2003
            try {
                wb = new HSSFWorkbook(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {// 当excel是2007时,创建excel2007
            try {
                wb = new XSSFWorkbook(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<ItemDataGlobalDTO> itemDataGlobalList = readExcelValue(wb);// 读取Excel里面客户的信息

        return itemDataGlobalList;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    public List<ItemDataGlobalDTO> readExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 2 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<ItemDataGlobalDTO> itemDataGlobalList = new ArrayList<ItemDataGlobalDTO>();
        // 循环Excel行数
        for (int r = 2; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            ItemDataGlobalDTO itemDataGlobal = new ItemDataGlobalDTO();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        itemDataGlobal.setItemGroupId(itemGroupRepository.getIdByName(cell.getStringCellValue().trim()));
                    } else if (c == 1) {
                        itemDataGlobal.setSkuNo(cell.getStringCellValue().trim());
                    } else if (c == 2) {
                        itemDataGlobal.setName(cell.getStringCellValue().trim());
                    } else if (c == 3) {
                        itemDataGlobal.setDescription(cell.getStringCellValue() == null ? "" : cell.getStringCellValue().trim());
                    } else if (c == 4) {
                        itemDataGlobal.setSafetyStock(Integer.parseInt(cell.getStringCellValue() == null ? "0" : cell.getStringCellValue().trim()));
                    } else if (c == 5) {
                        itemDataGlobal.setLotMandatory(Boolean.parseBoolean(cell.getStringCellValue() == null ? "true" : cell.getStringCellValue().trim()));
                    } else if (c == 6) {
                        itemDataGlobal.setLotType(cell.getStringCellValue() == null ? "" : cell.getStringCellValue().trim() .equals("按生产日期")  ? "MANUFACTURE" : cell.getStringCellValue().trim().equals("按到期日期") ? "EXPIRATION" : cell.getStringCellValue().trim().equals("无有效期但需要先进先出")?"PC":"");
                    } else if (c == 7) {
                        itemDataGlobal.setLotUnit(cell.getStringCellValue() == null ? "" : cell.getStringCellValue().trim() .equals("年") ? "YEAR" : cell.getStringCellValue().trim() .equals("月" )? "MONTH" : cell.getStringCellValue().trim().equals("日")  ? "DAY" : "");
                    } else if (c == 8) {
                        itemDataGlobal.setLotThreshold(Integer.parseInt(cell.getStringCellValue() == null ? "0" : cell.getStringCellValue().trim()));
                    } else if (c == 9) {
                        itemDataGlobal.setSerialRecordType(cell.getStringCellValue() == null ? "" : cell.getStringCellValue().trim() .equals("不记录")  ? "NO_RECORD" : cell.getStringCellValue().trim().equals("记录")  ? "ALWAYS_RECORD" : cell.getStringCellValue().trim().equals("出货后记录") ? "GOODS_OUT_RECORD" : "");
                    } else if (c == 10) {
                        itemDataGlobal.setSerialRecordLength(Integer.parseInt(cell.getStringCellValue() == null ? "0" : cell.getStringCellValue().trim()));
                    } else if (c == 11) {
                        itemDataGlobal.setHandlingUnitId(itemUnitRepository.getIdByName(cell.getStringCellValue()));
                    } else if (c == 12) {
                        itemDataGlobal.setMeasured(Boolean.parseBoolean(cell.getStringCellValue() == null ? "true" : cell.getStringCellValue().trim()));
                    } else if (c == 13) {
                        itemDataGlobal.setWidth(new BigDecimal(Integer.parseInt(cell.getStringCellValue() == null ? "0" : cell.getStringCellValue().trim())));
                    } else if (c == 14) {
                        itemDataGlobal.setDepth(new BigDecimal(Integer.parseInt(cell.getStringCellValue() == null ? "0" : cell.getStringCellValue().trim())));
                    } else if (c == 15) {
                        itemDataGlobal.setHeight(new BigDecimal(Integer.parseInt(cell.getStringCellValue() == null ? "0" : cell.getStringCellValue().trim())));
                    } else if (c == 16) {
                        itemDataGlobal.setWeight(new BigDecimal(Integer.parseInt(cell.getStringCellValue() == null ? "0" : cell.getStringCellValue().trim())));
                    } else if (c == 17) {
                        itemDataGlobal.setMultiplePart(Boolean.parseBoolean(cell.getStringCellValue() == null ? "true" : cell.getStringCellValue().trim()));
                    } else if (c == 18) {
                        itemDataGlobal.setMultiplePartAmount(Integer.parseInt(cell.getStringCellValue() == null ? "0" : cell.getStringCellValue().trim()));
                    } else if (c == 19) {
                        itemDataGlobal.setPreferOwnBox(Boolean.parseBoolean(cell.getStringCellValue() == null ? "true" : cell.getStringCellValue().trim()));
                    } else if (c == 20) {
                        itemDataGlobal.setPreferBag(Boolean.parseBoolean(cell.getStringCellValue() == null ? "true" : cell.getStringCellValue().trim()));
                    } else if (c == 21) {
                        itemDataGlobal.setUseBubbleFilm(Boolean.parseBoolean(cell.getStringCellValue() == null ? "true" : cell.getStringCellValue().trim()));
                    }
                }
            }
            //  添加到list
            itemDataGlobalList.add(itemDataGlobal);
        }
        return itemDataGlobalList;
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
}
