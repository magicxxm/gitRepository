package com.mushiny.business;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.model.*;
import com.mushiny.repository.ClientRepository;
import com.mushiny.repository.ItemDataRepository;
import com.mushiny.repository.UnitLoadRepository;
import com.mushiny.repository.WarehouseRepository;
import com.mushiny.web.dto.AdviceReceiptDTO;
import com.mushiny.web.dto.AdviceReceiptPositionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 123 on 2018/2/2.
 */
@Component
public class AdviceBusiness {
    private final Logger log = LoggerFactory.getLogger(AdviceBusiness.class);

    private final ItemDataRepository itemDataRepository;
    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;
    private final EntityManager manager;
    private final UnitLoadRepository unitLoadRepository;

    @Autowired
    public AdviceBusiness(ItemDataRepository itemDataRepository,
                          ClientRepository clientRepository,
                          WarehouseRepository warehouseRepository,
                          UnitLoadRepository unitLoadRepository,
                          EntityManager manager){
        this.itemDataRepository = itemDataRepository;
        this.clientRepository = clientRepository;
        this.warehouseRepository = warehouseRepository;
        this.manager = manager;
        this.unitLoadRepository = unitLoadRepository;
    }

    public AccessDTO createAdvice(AdviceReceiptDTO dto){
        AccessDTO accessDTO = new AccessDTO();


        return accessDTO;
    }


}
