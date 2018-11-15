package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.ReplenishmentDTO;
import wms.domain.Replenishment;

@Component
public class ReplenishmentMapper implements BaseMapper<ReplenishmentDTO, Replenishment> {
    
    private final ApplicationContext applicationContext;

    @Autowired
    public ReplenishmentMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public ReplenishmentDTO toDTO(Replenishment entity) {
        if (entity == null) {
            return null;
        }
        ReplenishmentDTO dto = new ReplenishmentDTO(entity);
        dto.setOrderCode(entity.getOrdercode());
        dto.setReplenishmentMode(entity.getReplenishmentmode());
        dto.setStatus(entity.getStatus());
        dto.setRemark(entity.getRemark());
        dto.setOrderDate(entity.getOrderdate());
        dto.setFinishDate(entity.getFinishdate());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public Replenishment toEntity(ReplenishmentDTO dto) {
        if (dto == null) {
            return null;
        }
        Replenishment entity = new Replenishment();

        entity.setId(dto.getId());
        entity.setOrdercode(dto.getOrderCode());
        entity.setReplenishmentmode(dto.getReplenishmentMode());
        entity.setStatus(dto.getStatus());
        entity.setRemark(dto.getRemark());
        entity.setOrderdate(dto.getOrderDate());
        entity.setFinishdate(dto.getFinishDate());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReplenishmentDTO dto, Replenishment entity) {
    }
}
