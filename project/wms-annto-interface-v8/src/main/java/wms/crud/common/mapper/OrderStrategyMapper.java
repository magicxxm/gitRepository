package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.OrderStrategyDTO;
import wms.domain.common.OrderStrategy;

/**
 * Created by PC-4 on 2017/8/4.
 */
@Component
public class OrderStrategyMapper implements BaseMapper<OrderStrategyDTO,OrderStrategy> {

//    private final StorageLocationMapper storageLocationMapper;

    @Autowired
    public OrderStrategyMapper(){
    }

    @Override
    public OrderStrategyDTO toDTO(OrderStrategy entity) {
        if(entity == null){
            return null;
        }
        OrderStrategyDTO dto = new OrderStrategyDTO();
        dto.setCreateFollowupPicks(entity.isCreateFollowupPicks());
        dto.setCreateShippingOrder(entity.isCreateShippingOrder());
        dto.setManualCreationIndex(entity.isManualCreationIndex());
        dto.setName(entity.getName());
        dto.setPreferMatchingStock(entity.isPreferMatchingStock());
        dto.setPreferUnopened(entity.isPreferUnopened());
//        dto.setStorageLocation(storageLocationMapper.toDTO(entity.getStorageLocation()));
        dto.setUseLockedLot(entity.isUseLockedLot());
        dto.setUseLockedStock(entity.isUseLockedStock());
        return dto;
    }

    @Override
    public OrderStrategy toEntity(OrderStrategyDTO dto) {
        return null;
    }

    @Override
    public void updateEntityFromDTO(OrderStrategyDTO dto, OrderStrategy entity) {
    }
}
