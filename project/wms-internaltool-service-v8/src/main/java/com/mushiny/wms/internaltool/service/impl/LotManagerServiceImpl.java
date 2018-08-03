package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.internaltool.query.LotmanagerQuery;
import com.mushiny.wms.internaltool.service.LotManagerService;
import com.mushiny.wms.internaltool.web.dto.LotManagerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 123 on 2017/11/10.
 */
@Service
@Transactional(readOnly = true)
public class LotManagerServiceImpl implements LotManagerService {

    private final ApplicationContext applicationContext;
    private final LotmanagerQuery lotmanagerQuery;

    public LotManagerServiceImpl(ApplicationContext applicationContext,
                                 LotmanagerQuery lotmanagerQuery){
        this.applicationContext = applicationContext;
        this.lotmanagerQuery = lotmanagerQuery;
    }

    @Override
    public List<LotManagerDTO> getAllStockUnits() {
        String warehouse = applicationContext.getCurrentWarehouse();
        return lotmanagerQuery.getStockInfo(warehouse);
    }

    @Override
    public Page<LotManagerDTO> getStockUnits(Pageable pageable) {
        String warehouse = applicationContext.getCurrentWarehouse();
        return lotmanagerQuery.getStock(warehouse,pageable);
    }

    @Override
    public List<LotManagerDTO> getByParam(String param) {
        String warehouse = applicationContext.getCurrentWarehouse();
        if(param != null && !"".equals(param) && !"undefined".equalsIgnoreCase(param) && !"null".equalsIgnoreCase(param)){
            return lotmanagerQuery.getByParamAndWarehouse(warehouse,param);
        }else {
            return lotmanagerQuery.getStockInfo(warehouse);
        }
    }
}
