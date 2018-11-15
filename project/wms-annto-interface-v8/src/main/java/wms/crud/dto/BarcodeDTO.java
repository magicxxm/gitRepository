package wms.crud.dto;

import wms.common.crud.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/10.
 */
public class BarcodeDTO extends BaseDTO{

    private String barcode;

    private String quantityUM = "EA";

    private ExtendFieldsDTO extendFields;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQuantityUM() {
        return quantityUM;
    }

    public void setQuantityUM(String quantityUM) {
        this.quantityUM = quantityUM;
    }

    public ExtendFieldsDTO getExtendFields() {
        return extendFields;
    }

    public void setExtendFields(ExtendFieldsDTO extendFields) {
        this.extendFields = extendFields;
    }
}
