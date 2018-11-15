package wms.business;

import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.constants.State;
import wms.crud.dto.CheckPositionDTO;
import wms.crud.dto.CheckUpdateDTO;
import wms.domain.*;
import wms.domain.common.Client;
import wms.domain.common.Warehouse;
import wms.repository.common.ClientRepository;
import wms.repository.common.ItemDataRepository;
import wms.repository.common.WarehouseRepository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 123 on 2017/8/16.
 */
@Component
public class CheckBusiness {
    private final Logger log = LoggerFactory.getLogger(CheckBusiness.class);

    private final EntityManager manager;
    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final ItemDataRepository itemDataRepository;

    @Autowired
    public CheckBusiness(EntityManager manager,
                         ClientRepository clientRepository,
                         WarehouseRepository warehouseRepository,
                         ItemDataRepository itemDataRepository){
        this.manager = manager;
        this.clientRepository = clientRepository;
        this.warehouseRepository = warehouseRepository;
        this.itemDataRepository = itemDataRepository;
    }

    public void saveToAnntoStocktaking(CheckUpdateDTO dto) {
        AnntoStocktaking anntoStocktaking = new AnntoStocktaking();
        anntoStocktaking.setOriginalCountId(dto.getOriginalCountId());
        anntoStocktaking.setCountType(dto.getCountType());
        anntoStocktaking.setRemark(dto.getRemark());
        anntoStocktaking.setWarehouseCode(dto.getWarehouseCode());

        JSONArray orderArray = JSONArray.fromObject(dto.getOrderItems());
        List<CheckPositionDTO> orderItems = (List<CheckPositionDTO>)JSONArray.toCollection(orderArray,CheckPositionDTO.class);
        for(int i = 0; i < orderItems.size(); i++) {
            AnntoStocktakingPosition anntoStocktakingPosition = new AnntoStocktakingPosition();
            anntoStocktakingPosition.setAnntoStocktaking(anntoStocktaking);
            anntoStocktakingPosition.setCompanyCode(orderItems.get(i).getCompanyCode());
            anntoStocktakingPosition.setLocationCode(orderItems.get(i).getLocationCode());
            anntoStocktakingPosition.setItemCode(orderItems.get(i).getItemCode());
            anntoStocktakingPosition.setItemName(orderItems.get(i).getItemName());
            anntoStocktakingPosition.setInventorySts(orderItems.get(i).getInventorySts());
            anntoStocktakingPosition.setSystemQty(orderItems.get(i).getSystemQTY());

            anntoStocktaking.getPositions().add(anntoStocktakingPosition);
        }
        manager.persist(anntoStocktaking);
    }

    public void saveToWmsSysStocktaking(CheckUpdateDTO dto) {

        SystemStocktaking systemStocktaking = new SystemStocktaking();
        systemStocktaking.setRemark(dto.getRemark());
        systemStocktaking.setState(State.RAW);
        systemStocktaking.setStockNo(dto.getOriginalCountId());
        systemStocktaking.setStockType(dto.getCountType());

        Warehouse warehouse = warehouseRepository.getByWarehouseNo(dto.getWarehouseCode());
        systemStocktaking.setWarehouseId(warehouse.getId());

        JSONArray orderArray = JSONArray.fromObject(dto.getOrderItems());
        List<CheckPositionDTO> orderItems = (List<CheckPositionDTO>)JSONArray.toCollection(orderArray,CheckPositionDTO.class);
        for (int i = 0;i < orderItems.size();i++){
            SystemStocktakingPosition position = new SystemStocktakingPosition();

            Client client = clientRepository.findByClientNo(orderItems.get(i).getCompanyCode());
//            ItemData itemData = itemDataRepository.getOneByItemNoAndClientId(orderItems.get(i).getItemCode(),client.getId());
            List<ItemData> itemDatas = itemDataRepository.getByItemNoAndClientId(orderItems.get(i).getItemCode(),client.getId());
            if(itemDatas.isEmpty()){
                log.info("wms 中没有这个商品。。。" + orderItems.get(i).getItemName());
                return;
            }
            ItemData itemData = itemDatas.get(0);
            if(itemData == null){
                log.info("itemCode = " + orderItems.get(i).getItemCode() + " 对应得商品 is null..."  );
                continue;
            }
            position.setAmountSystemAnnto(new BigDecimal(orderItems.get(i).getSystemQTY()));
            position.setInventorySts(orderItems.get(i).getInventorySts());
            position.setItemName(itemData.getName());
            position.setItemNo(itemData.getItemNo());
            position.setSkuNo(itemData.getSkuNo());
            position.setLocationCode(orderItems.get(i).getLocationCode());
            position.setState(State.RAW);
            position.setClientId(client.getId());
            position.setWarehouseId(warehouse.getId());

            systemStocktaking.addPosition(position);
        }

        manager.persist(systemStocktaking);
    }
}
