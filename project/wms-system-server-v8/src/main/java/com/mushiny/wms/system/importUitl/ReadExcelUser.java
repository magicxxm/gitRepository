package com.mushiny.wms.system.importUitl;

import com.mushiny.wms.system.crud.dto.UserDTO;
import com.mushiny.wms.system.repository.ClientRepository;
import com.mushiny.wms.system.repository.UserGroupRepository;
import com.mushiny.wms.system.repository.WarehouseRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hgf on 2017/8/4.
 */
public class ReadExcelUser {
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;
    private WarehouseRepository warehouseRepository;
    private ClientRepository clientRepository;
    private UserGroupRepository userGroupRepository;

    //构造方法
    public ReadExcelUser(WarehouseRepository warehouseRepository, ClientRepository clientRepository, UserGroupRepository userGroupRepository) {
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.userGroupRepository = userGroupRepository;
    }

    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param
     * @return
     */
    public List<UserDTO> getExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();//获取文件名
        if (!validateExcel(fileName)) {// 验证文件名是否合格
            return null;
        }
        boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
        if (isExcel2007(fileName)) {
            isExcel2003 = false;
        }
        List<UserDTO> userList = null;
        try {
            userList = createExcel(mFile.getInputStream(), isExcel2003);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<UserDTO> createExcel(InputStream is, boolean isExcel2003) {
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
        List<UserDTO> userList = readExcelValue(wb);// 读取Excel里面客户的信息

        return userList;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    public List<UserDTO> readExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 2 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<UserDTO> userList = new ArrayList<UserDTO>();
        // 循环Excel行数
        for (int r = 2; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            UserDTO user = new UserDTO();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        user.setWarehouseId(warehouseRepository.findIdByName(0,cell.getStringCellValue().trim()));
                    } else if (c == 1) {
                        user.setClientId(clientRepository.findIdByName(0,cell.getStringCellValue().trim()));
                    }else if (c == 2) {
                        user.setUsername(cell.getStringCellValue().trim());
                    }else if (c == 3) {
                        user.setPassword(cell.getStringCellValue().trim());
                    }else if (c == 4) {
                        user.setName(cell.getStringCellValue()!=null?cell.getStringCellValue().trim():"");
                    }else if (c == 5) {
                        user.setEmail(cell.getStringCellValue()!=null?cell.getStringCellValue().trim():"");
                    }else if (c == 6) {
                        user.setPhone(cell.getStringCellValue()!=null?cell.getStringCellValue().trim():"");
                    }else if (c == 7) {
                        user.setLocale(cell.getStringCellValue().trim()=="英文"?"EN":"CN");
                    }else if (c == 8) {
                        user.setUserGroupId(userGroupRepository.findIdByName(0,cell.getStringCellValue().trim()));
                    }
                }
            }
            //  添加到list
            userList.add(user);
        }
        return userList;
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
