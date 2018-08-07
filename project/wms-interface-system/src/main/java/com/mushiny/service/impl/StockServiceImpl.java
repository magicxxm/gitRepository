package com.mushiny.service.impl;

import com.mushiny.business.StockBusiness;
import com.mushiny.common.crud.AccessDTO;
import com.mushiny.model.SystemStocktaking;
import com.mushiny.model.Warehouse;
import com.mushiny.repository.ClientRepository;
import com.mushiny.repository.WarehouseRepository;
import com.mushiny.service.StockService;
import com.mushiny.web.dto.StocktakingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 123 on 2018/2/2.
 */
@Service
@Transactional
public class StockServiceImpl implements StockService {
    private final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final StockBusiness stockBusiness;

    @Autowired
    public StockServiceImpl(WarehouseRepository warehouseRepository, ClientRepository clientRepository,
                            StockBusiness stockBusiness) {
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.stockBusiness = stockBusiness;
    }

    @Override
    public AccessDTO createStock(StocktakingDTO stocktakingDTO) {
        log.info("开始同步盘点单信息");
        AccessDTO accessDTO = new AccessDTO();
        if(stocktakingDTO.getWarehouseNo() == null || "".equalsIgnoreCase(stocktakingDTO.getWarehouseNo())){
            log.info("盘点单：" + stocktakingDTO.getStockNo() +" 对应的仓库信息为空。。。");
            accessDTO.setMsg("盘点单：" + stocktakingDTO.getStockNo() +" 对应的仓库信息为空。。。");
            accessDTO.setCode("1");
            return accessDTO;
        }
        Warehouse warehouse = warehouseRepository.getByWarehouseNo(stocktakingDTO.getWarehouseNo());

        SystemStocktaking stocktaking = stockBusiness.createStock(stocktakingDTO,warehouse);

        return accessDTO;
    }
}
