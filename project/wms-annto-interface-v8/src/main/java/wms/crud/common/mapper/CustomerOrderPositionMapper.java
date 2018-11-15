package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.CustomerOrderPositionDTO;
import wms.domain.CustomerOrderPosition;
import wms.repository.CustomerOrderRepository;
import wms.repository.common.ItemDataRepository;

/**
 * Created by PC-4 on 2017/8/4.
 */
@Component
public class CustomerOrderPositionMapper implements BaseMapper<CustomerOrderPositionDTO, CustomerOrderPosition> {

    private final ItemDataRepository itemDataRepository;
    private final ItemDataMapper itemDataMapper;
    private final CustomerOrderMapper customerOrderMapper;
    private final CustomerOrderRepository customerOrderRepository;
    private final ApplicationContext applicationContext;

    @Autowired
    public CustomerOrderPositionMapper(ItemDataRepository itemDataRepository,
                                       ItemDataMapper itemDataMapper,
                                       ApplicationContext applicationContext,
                                       CustomerOrderMapper customerOrderMapper,
                                       CustomerOrderRepository customerOrderRepository) {
        this.itemDataRepository = itemDataRepository;
        this.itemDataMapper = itemDataMapper;
        this.customerOrderMapper = customerOrderMapper;
        this.customerOrderRepository = customerOrderRepository;
        this.applicationContext = applicationContext;
    }
    @Override
    public CustomerOrderPositionDTO toDTO(CustomerOrderPosition entity) {
        if (entity == null) {
            return null;
        }
        CustomerOrderPositionDTO dto = new CustomerOrderPositionDTO(entity);
        dto.setAmount(entity.getAmount());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setPositionNo(entity.getPositionNo());
        dto.setState(entity.getState());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setItemData(itemDataMapper.toDTO(entity.getItemData()));
        dto.setCustomerOrder(customerOrderMapper.toDTO(entity.getCustomerOrder()));

        return dto;
    }

    @Override
    public CustomerOrderPosition toEntity(CustomerOrderPositionDTO dto) {
        if(dto == null){
            return null;
        }
        CustomerOrderPosition entity = new CustomerOrderPosition();
        if(dto.getItemDataId() != null){
            entity.setItemData(itemDataRepository.findOne(dto.getItemDataId()));
        }
        entity.setAmount(dto.getAmount());
        if(dto.getOrderId() != null){
            entity.setCustomerOrder(customerOrderRepository.findOne(dto.getOrderId()));
        }
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setPositionNo(dto.getPositionNo());
        entity.setState(dto.getState());

        entity.setClientId(applicationContext.getCurrentClient());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(CustomerOrderPositionDTO dto, CustomerOrderPosition entity) {
    }
}