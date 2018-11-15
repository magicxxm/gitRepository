package wms.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.crud.AccessDTO;
import wms.constants.State;
import wms.crud.common.dto.MovementDTO;
import wms.crud.common.dto.MovementPositionDTO;
import wms.crud.common.mapper.MovementMapper;
import wms.crud.common.mapper.MovementPositionMapper;
import wms.domain.CustomerOrder;
import wms.domain.CustomerOrderPosition;
import wms.domain.ItemData;
import wms.domain.Movement;
import wms.domain.common.*;
import wms.repository.common.ClientRepository;
import wms.repository.common.ItemDataRepository;
import wms.repository.common.MovementRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 123 on 2017/8/16.
 */
@Component
public class TransferBusiness {
    private Logger log = LoggerFactory.getLogger(TransferBusiness.class);

    private final MovementMapper movementMapper;
    private final MovementPositionMapper movementPositionMapper;
    private final MovementRepository movementRepository;
    private final ClientRepository clientRepository;
    private final ItemDataRepository itemDataRepository;
    private final EntityManager manager;

    @Autowired
    public TransferBusiness(MovementMapper movementMapper,
                            MovementPositionMapper movementPositionMapper,
                            MovementRepository movementRepository,
                            ClientRepository clientRepository,
                            ItemDataRepository itemDataRepository,
                            EntityManager manager){
        this.movementMapper = movementMapper;
        this.movementPositionMapper = movementPositionMapper;
        this.movementRepository = movementRepository;
        this.clientRepository = clientRepository;
        this.itemDataRepository = itemDataRepository;
        this.manager = manager;
    }

    public AccessDTO checkStockUnit(MovementDTO dto) {
        log.info("开始查询调拨单中商品在本仓库中是否有库存。。。");
        AccessDTO accessDTO = new AccessDTO();
        List<MovementPositionDTO> orderItems = dto.getOrderItems();
        if(orderItems.size() > 0){
            boolean hasStockUnit = true;
            for (MovementPositionDTO m:orderItems){
                Client client = clientRepository.findByClientNo(m.getCompanyCode());
                String itemNo = m.getItemCode();
                ItemData item = itemDataRepository.getOneByItemNoAndClientId(itemNo,client.getId());
                int amount = m.getAllocatedQty();
                String state = "Inventory";
                hasStockUnit = getStockUnit(item.getId(),amount,client.getId(),state);
                if(!hasStockUnit){
                    accessDTO.setCode("1");
                    accessDTO.setMsg("商品 "+m.getItemName()+" 在仓库中库存不够");
                    return accessDTO;
                }
            }
        }

        return accessDTO;

    }

    private boolean getStockUnit(String itemId, int amount, String clientId,String state) {
        boolean hasStockUnit = true;
        String sql = "SELECT S.ITEMDATA_ID,SUM(S.AMOUNT),SUM(S.RESERVED_AMOUNT) " +
                "FROM INV_STOCKUNIT S " +
                "WHERE S.ITEMDATA_ID='"+itemId+"' " +
                "AND S.STATE = '"+state+"' " +
                "AND S.CLIENT_ID = '"+clientId+"' " +
                "GROUP BY S.ITEMDATA_ID " +
                "HAVING (SUM(S.AMOUNT) - SUM(S.RESERVED_AMOUNT)) > "+amount;

        Query query = manager.createNativeQuery(sql);
        List result = query.getResultList();
        if(result.size() == 0){
            hasStockUnit = false;
        }

        return hasStockUnit;
    }

    public AccessDTO createAnntoTransfer(MovementDTO dto) {
        AccessDTO accessDTO = new AccessDTO();
        Movement entity = movementMapper.toEntity(dto);
        for (MovementPositionDTO movementPositionDTO : dto.getOrderItems()) {
            entity.addPosition(movementPositionMapper.toEntity(movementPositionDTO));
        }
        movementRepository.save(entity);
        return accessDTO;
    }


    public void createTransferShipment(MovementDTO dto) {
        log.info("开始生成调拨单。。。");

        //生成TransferShipment
        TransferShipment shipment = new TransferShipment();
        shipment.setState(State.RAW);
        shipment.setShipmentNo(dto.getOrderCode());

        /**
         * 美的接口没有传过来sortCode , deliveryTime
         */
        shipment.setDeliveryDate(null);// 发货点
        shipment.setSortCode("");//sortCode

        shipment.setPickMode("PICK_TO_TOTE");
         //shipment.setClientId();
        // shipment.setWarehouseId();

        List<MovementPositionDTO> orderItems = dto.getOrderItems();
        if(orderItems.size() > 0){
            for(int i = 0;i < orderItems.size();i++){
                TransferShipmentPosition position = new TransferShipmentPosition();
                Client client = clientRepository.findByClientNo(orderItems.get(i).getCompanyCode());
                position.setAmount(new BigDecimal(orderItems.get(i).getAllocatedQty()));
                position.setItemData(itemDataRepository.getOneByItemNoAndClientId(orderItems.get(i).getItemCode(),client.getId()));
                position.setOrderIndex(i+1);
                position.setState(State.RAW);
                position.setPositionNo(i+1);
                //shipment.setClientId();
                //shipment.setWarehouseId();

                shipment.getPositions().add(position);
            }
        }
        manager.persist(shipment);
    }
}
