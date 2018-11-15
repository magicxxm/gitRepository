package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.common.exception.ApiException;
import wms.common.exception.ExceptionEnum;
import wms.crud.common.dto.CustomerOrderDTO;
import wms.domain.CustomerOrder;
import wms.repository.OrderStrategyRepository;
import wms.repository.common.ClientRepository;
import wms.repository.common.WarehouseRepository;

/**
 * Created by PC-4 on 2017/8/4.
 */
@Component
public class CustomerOrderMapper implements BaseMapper<CustomerOrderDTO,CustomerOrder> {
    private final OrderStrategyRepository orderStrategyRepository;
    private final OrderStrategyMapper orderStrategyMapper;
    private final ApplicationContext applicationContext;
//    private final ClientMapper clientMapper;
//    private final WarehouseMapper warehouseMapper;
    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public CustomerOrderMapper(OrderStrategyRepository orderStrategyRepository,
                               OrderStrategyMapper orderStrategyMapper,
                               ApplicationContext applicationContext,
//                               ClientMapper clientMapper,
                               ClientRepository clientRepository,
                               WarehouseRepository warehouseRepository
//                               WarehouseMapper warehouseMapper
    ){
        this.orderStrategyRepository = orderStrategyRepository;
        this.orderStrategyMapper = orderStrategyMapper;
        this.applicationContext = applicationContext;
//        this.clientMapper = clientMapper;
//        this.warehouseMapper = warehouseMapper;
        this.clientRepository = clientRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public CustomerOrderDTO toDTO(CustomerOrder entity) {
        if(entity == null){
            return  null;
        }
        CustomerOrderDTO dto = new CustomerOrderDTO(entity);
        dto.setCustomerName(entity.getCustomerName());
        dto.setCustomerNo(entity.getCustomerNo());
        dto.setDeliveryDate(entity.getDeliveryDate());
        dto.setOrderNo(entity.getOrderNo());
        dto.setPriority(entity.getPriority());
        dto.setSortCode(entity.getSortCode());
        dto.setState(entity.getState());
        dto.setStrategy(orderStrategyMapper.toDTO(entity.getStrategy()));

//        dto.setClientDTO(clientMapper.toDTO(clientRepository.retrieve(entity.getClientId())));
//        dto.setWarehouseDTO(warehouseMapper.toDTO(warehouseRepository.retrieve(entity.getWarehouseId())));
        return dto;
    }
    @Override
    public CustomerOrder toEntity(CustomerOrderDTO dto) {
        if(dto == null){
            return null;
        }
        CustomerOrder entity = new CustomerOrder();
        entity.setCustomerName(dto.getCustomerName());
        entity.setCustomerNo(dto.getCustomerNo());
        entity.setDeliveryDate(dto.getDeliveryDate());
        entity.setOrderNo(dto.getCode());
        entity.setPriority(dto.getPriority());
        entity.setSortCode(dto.getSortCode());
        entity.setState(dto.getState());
        if(dto.getStrategyId() != null){
            entity.setStrategy(orderStrategyRepository.retrieve(dto.getStrategyId()));
        }

        entity.setClientId(clientRepository.findByClientNo(dto.getCompanyCode()).getId());
        entity.setWarehouseId(warehouseRepository.getByWarehouseNo(dto.getWarehouseCode()).getId());
        return entity;
    }
    @Override
    public void updateEntityFromDTO(CustomerOrderDTO dto, CustomerOrder entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setCustomerName(dto.getCustomerName());
        entity.setCustomerNo(dto.getCustomerNo());
        entity.setDeliveryDate(dto.getDeliveryDate());
        entity.setSortCode(dto.getSortCode());
        entity.setOrderNo(dto.getOrderNo());
        entity.setPriority(dto.getPriority());
        entity.setState(dto.getState());
        if (dto.getStrategyId() != null) {
            entity.setStrategy(orderStrategyRepository.retrieve(dto.getStrategyId()));
        } else {
            entity.setStrategy(null);
        }
    }
}
