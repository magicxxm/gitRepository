package com.mushiny.business;

import com.mushiny.constants.State;
import com.mushiny.model.*;
import com.mushiny.repository.ClientRepository;
import com.mushiny.repository.ItemDataRepository;
import com.mushiny.web.dto.StocktakingDTO;
import com.mushiny.web.dto.StocktakingPositionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by 123 on 2018/2/2.
 */
@Component
public class StockBusiness {
    private final Logger logger = LoggerFactory.getLogger(StockBusiness.class);

    private final EntityManager manager;
    private final ItemDataRepository itemDataRepository;
    private final ClientRepository clientRepository;


    @Autowired
    public StockBusiness(EntityManager manager,
                         ItemDataRepository itemDataRepository,
                         ClientRepository clientRepository) {
        this.manager = manager;
        this.itemDataRepository = itemDataRepository;
        this.clientRepository = clientRepository;
    }

    public SystemStocktaking createStock(StocktakingDTO stocktakingDTO, Warehouse warehouse) {
        SystemStocktaking stocktaking = new SystemStocktaking();
        stocktaking.setState(State.STOCK_NEW);
        stocktaking.setStockNo(stocktakingDTO.getStockNo());
//        stocktaking.setStockType(stocktakingDTO.getStockType());
        stocktaking.setWarehouseId(warehouse.getId());

        List<StocktakingPositionDTO> positionDTOS = stocktakingDTO.getPositionDTOS();
        for (int i = 0;i < positionDTOS.size();i++){
            SystemStocktakingPosition position = new SystemStocktakingPosition();
            Client client = clientRepository.getByClientNo(positionDTOS.get(i).getClientNo());

            ItemData itemData = itemDataRepository.getByItemNoAndClientId(positionDTOS.get(i).getItemNo(),client.getId());
            position.setItemName(itemData.getName());
            position.setItemNo(itemData.getItemNo());
            position.setSkuNo(itemData.getSkuNo());
            position.setClientId(client.getId());
            position.setWarehouseId(warehouse.getId());

            stocktaking.addPosition(position);
        }

        manager.persist(stocktaking);

        return stocktaking;
    }
}
