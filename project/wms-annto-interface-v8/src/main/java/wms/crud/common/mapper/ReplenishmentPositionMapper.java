package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.ReplenishmentPositionDTO;
import wms.domain.ReplenishmentPosition;
import wms.repository.common.ClientRepository;
import wms.repository.common.ReplenishmentRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Component
public class ReplenishmentPositionMapper implements BaseMapper<ReplenishmentPositionDTO, ReplenishmentPosition> {

    private final ApplicationContext applicationContext;
    private final ReplenishmentMapper replenishmentMapper;
    private final ReplenishmentRepository replenishmentRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ReplenishmentPositionMapper(ApplicationContext applicationContext,
                                       ReplenishmentMapper replenishmentMapper,
                                       ReplenishmentRepository replenishmentRepository,
                                       ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.replenishmentMapper = replenishmentMapper;
        this.replenishmentRepository = replenishmentRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public ReplenishmentPositionDTO toDTO(ReplenishmentPosition entity) {
        if (entity == null) {
            return null;
        }
        ReplenishmentPositionDTO dto = new ReplenishmentPositionDTO(entity);
        dto.setItemCode(entity.getItemcode());
        dto.setItemName(entity.getItemname());
        dto.setAllocatedQty(entity.getAllocatedqty());
        dto.setInventorySts(entity.getInventorysts());
        dto.setBatch(entity.getBatch());
        dto.setLot(entity.getLot());
        dto.setManufactureDate(new SimpleDateFormat("yyyy-MM-dd").format(entity.getManufacturedate()));
        dto.setExpirationDate(new SimpleDateFormat("yyyy-MM-dd").format(entity.getExpirationdate()));
        dto.setFromloc(entity.getFromloc());
        dto.setToloc(entity.getToloc());
        dto.setFromZone(entity.getFromzone());
        dto.setToZone(entity.getTozone());
        dto.setFromLpn(entity.getFromlpn());
        dto.setToLpn(entity.getTolpn());
        dto.setReplenishment(replenishmentMapper.toDTO(entity.getReplenishment()));
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public ReplenishmentPosition toEntity(ReplenishmentPositionDTO dto){
        if (dto == null) {
            return null;
        }
        ReplenishmentPosition entity = new ReplenishmentPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setItemcode(dto.getItemCode());
        entity.setItemname(dto.getItemName());
        entity.setAllocatedqty(dto.getAllocatedQty());
        entity.setInventorysts(dto.getInventorySts());
        entity.setBatch(dto.getBatch());
        entity.setLot(dto.getLot());
        try {
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
        if (dto.getReplenishmentID() != null) {
            entity.setReplenishment(replenishmentRepository.retrieve(dto.getReplenishmentID()));
        }
//        entity.setClient(clientRepository.findByClientNo(dto.getCompanyCode()).getId());
        entity.setCompanyCode(dto.getCompanyCode());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReplenishmentPositionDTO dto, ReplenishmentPosition entity) {
    }
}
