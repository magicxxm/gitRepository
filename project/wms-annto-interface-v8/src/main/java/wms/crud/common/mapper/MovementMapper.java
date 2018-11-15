package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.MovementDTO;
import wms.domain.Movement;

@Component
public class MovementMapper implements BaseMapper<MovementDTO, Movement> {
    
    private final ApplicationContext applicationContext;

    @Autowired
    public MovementMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public MovementDTO toDTO(Movement entity) {
        if (entity == null) {
            return null;
        }
        MovementDTO dto = new MovementDTO(entity);
        dto.setOrderCode(entity.getOrdercode());
        dto.setStatus(entity.getStatus());
        dto.setRemark(entity.getRemark());
        dto.setOrderDate(entity.getOrderdate());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public Movement toEntity(MovementDTO dto) {
        if (dto == null) {
            return null;
        }
        Movement entity = new Movement();

        entity.setId(dto.getId());
        entity.setOrdercode(dto.getOrderCode());
        entity.setStatus(dto.getStatus());
        entity.setRemark(dto.getRemark());
        if(dto.getOrderDate() == null){
            entity.setOrderdate("");
        }else {
            entity.setOrderdate(dto.getOrderDate());
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(MovementDTO dto, Movement entity) {
    }
}
