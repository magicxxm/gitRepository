package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.MovementPositionDTO;
import wms.domain.MovementPosition;
import wms.repository.common.MovementRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class MovementPositionMapper implements BaseMapper<MovementPositionDTO, MovementPosition> {

    private final ApplicationContext applicationContext;
    private final MovementMapper movementMapper;
    private final MovementRepository movementRepository;

    @Autowired
    public MovementPositionMapper(ApplicationContext applicationContext,
                                  MovementMapper movementMapper,
                                  MovementRepository movementRepository) {
        this.applicationContext = applicationContext;
        this.movementMapper = movementMapper;
        this.movementRepository = movementRepository;
    }

    @Override
    public MovementPositionDTO toDTO(MovementPosition entity) {
        if (entity == null) {
            return null;
        }
        MovementPositionDTO dto = new MovementPositionDTO(entity);
        dto.setItemCode(entity.getItemcode());
        dto.setItemName(entity.getItemname());
        dto.setAllocatedQty(entity.getAllocatedqty());
        dto.setInventorySts(entity.getInventorysts());
        dto.setBatch(entity.getBatch());
        dto.setLot(entity.getLot());
        dto.setManufactureDate(new SimpleDateFormat("yyyy-MM-dd").format(dto.getManufactureDate()));
        dto.setExpirationDate(new SimpleDateFormat("yyyy-MM-dd").format(dto.getExpirationDate()));
        dto.setFromloc(entity.getFromloc());
        dto.setToloc(entity.getToloc());
        dto.setFromZone(entity.getFromzone());
        dto.setToZone(entity.getTozone());
        dto.setFromLpn(entity.getFromlpn());
        dto.setToLpn(entity.getTolpn());
        dto.setMovement(movementMapper.toDTO(entity.getMovement()));
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public MovementPosition toEntity(MovementPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        MovementPosition entity = new MovementPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setCompanyCode(dto.getCompanyCode());
        entity.setItemcode(dto.getItemCode());
        entity.setItemname(dto.getItemName());
        entity.setAllocatedqty(dto.getAllocatedQty());
        entity.setInventorysts(dto.getInventorySts());
        entity.setBatch(dto.getBatch());
        entity.setLot(dto.getLot());
        try{
            entity.setManufacturedate(new SimpleDateFormat("yyyy-MM-dd").parse(dto.getManufactureDate()));
            entity.setExpirationdate(new SimpleDateFormat("yyyy-MM-dd").parse(dto.getExpirationDate()));
        }catch (ParseException e){
            System.out.print(e);
        }
        entity.setFromloc(dto.getFromloc());
        entity.setToloc(dto.getToloc());
        entity.setFromzone(dto.getFromZone());
        entity.setTozone(dto.getToZone());
        entity.setFromlpn(dto.getFromLpn());
        entity.setTolpn(dto.getToLpn());
        if (dto.getMovementID() != null) {
            entity.setMovement(movementRepository.findOne(dto.getMovementID()));
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(MovementPositionDTO dto, MovementPosition entity) {
    }
}
