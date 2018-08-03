package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.business.enums.SerialNoRecordType;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataSerialNumber;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataSerialNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SerialNumberBusiness {

    private final ItemDataSerialNumberRepository itemDataSerialNumberRepository;

    @Autowired
    public SerialNumberBusiness(ItemDataSerialNumberRepository itemDataSerialNumberRepository) {
        this.itemDataSerialNumberRepository = itemDataSerialNumberRepository;
    }

    public void checkAndSaveSerialNo(ItemData itemData, String serialNo,BigDecimal amount){
        // SN为全球唯一
        if (!itemData.getSerialRecordType().equalsIgnoreCase(SerialNoRecordType.NO_RECORD.toString())) {
            if (amount.compareTo(BigDecimal.ONE) != 0) {
                throw new ApiException(InBoundException.EX_AMOUNT_ERROR.toString());
            }
            ItemDataSerialNumber serialNumber = itemDataSerialNumberRepository.getBySerialNo(serialNo);
            if(serialNumber != null){
                throw new ApiException(InBoundException.EX_SN_HAS_USED.toString(), serialNo);
            }
            serialNumber = new ItemDataSerialNumber();
            serialNumber.setSerialNo(serialNo);
            serialNumber.setItemData(itemData.getId().toString());
            serialNumber.setClientId(itemData.getClientId());
            serialNumber.setWarehouseId(itemData.getWarehouseId());
            itemDataSerialNumberRepository.save(serialNumber);
        }
    }
}
