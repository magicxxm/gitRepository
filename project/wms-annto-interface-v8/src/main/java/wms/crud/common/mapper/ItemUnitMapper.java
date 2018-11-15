package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.crud.mapper.BaseMapper;
import wms.common.exception.ApiException;
import wms.common.exception.ExceptionEnum;
import wms.crud.common.dto.ItemUnitDTO;
import wms.domain.common.ItemUnit;
import wms.repository.common.ItemUnitRepository;

@Component
public class ItemUnitMapper implements BaseMapper<ItemUnitDTO, ItemUnit> {

    private final ItemUnitRepository itemUnitRepository;

    @Autowired
    public ItemUnitMapper(ItemUnitRepository itemUnitRepository) {
        this.itemUnitRepository = itemUnitRepository;
    }

    @Override
    public ItemUnitDTO toDTO(ItemUnit entity) {
        if (entity == null) {
            return null;
        }

        ItemUnitDTO dto = new ItemUnitDTO(entity);

        dto.setName(entity.getName());
        dto.setUnitType(entity.getUnitType());
        dto.setBaseFactor(entity.getBaseFactor());

        dto.setBaseUnit(toDTO(entity.getBaseUnit()));

        return dto;
    }

    @Override
    public ItemUnit toEntity(ItemUnitDTO dto) {
        if (dto == null) {
            return null;
        }

        ItemUnit entity = new ItemUnit();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setUnitType(dto.getUnitType());
        entity.setBaseFactor(dto.getBaseFactor());

        if (dto.getBaseUnitId() != null) {
            entity.setBaseUnit(itemUnitRepository.retrieve(dto.getBaseUnitId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ItemUnitDTO dto, ItemUnit entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setUnitType(dto.getUnitType());
        entity.setBaseFactor(dto.getBaseFactor());

        if (dto.getBaseUnitId() != null) {
            entity.setBaseUnit(itemUnitRepository.retrieve(dto.getBaseUnitId()));
        } else {
            entity.setBaseUnit(null);
        }
    }
}

