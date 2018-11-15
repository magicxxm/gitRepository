package wms.crud.common.mapper;

/**
 * Created by PC-4 on 2017/8/4.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.common.exception.ApiException;
import wms.common.exception.ExceptionEnum;
import wms.crud.common.dto.StocktakingDTO;
import wms.domain.Stocktaking;

@Component
public class StocktakingMapper implements BaseMapper<StocktakingDTO, Stocktaking> {

    private final ApplicationContext applicationContext;

    @Autowired
    public StocktakingMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public StocktakingDTO toDTO(Stocktaking entity) {
        if (entity == null) {
            return null;
        }
        StocktakingDTO dto = new StocktakingDTO(entity);
        dto.setStocktakingNo(entity.getStocktakingNo());
        dto.setName(entity.getName());
        dto.setZone(entity.getZone());
        dto.setAmount(entity.getAmount());
        dto.setStocktakingType(entity.getStocktakingType());
        dto.setEnded(entity.getEnded());
        dto.setStarted(entity.getStarted());
        dto.setWarehouse(entity.getWarehouseId());
        return dto;
    }

    @Override
    public Stocktaking toEntity(StocktakingDTO dto) {
        if (dto == null) {
            return null;
        }
        Stocktaking entity = new Stocktaking();
        entity.setId(entity.getId());
        entity.setAdditionalContent(entity.getAdditionalContent());
        entity.setStocktakingNo(dto.getOriginalCountId());
        entity.setName(dto.getName());
        entity.setZone(dto.getZone());
        entity.setAmount(dto.getAmount());
        entity.setStocktakingType(dto.getStocktakingType());
        entity.setEnded(dto.getEnded());
        entity.setStarted(dto.getStarted());
        if (dto.getWarehouseId() == null) {
            entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StocktakingDTO dto, Stocktaking entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(entity.getId());
        entity.setAdditionalContent(entity.getAdditionalContent());
        if (dto.getStocktakingNo() != null) {
            entity.setStocktakingNo(dto.getStocktakingNo());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getZone() != null) {
            entity.setZone(dto.getZone());
        }
        if (dto.getAmount() != 0) {
            entity.setAmount(dto.getAmount());
        }
        if (dto.getStocktakingType() != null) {
            entity.setStocktakingType(dto.getStocktakingType());
        }
        if (dto.getEnded() != null) {
            entity.setEnded(dto.getEnded());
        }
        if (dto.getStarted() != null) {
            entity.setStarted(dto.getStarted());
        }
    }
}