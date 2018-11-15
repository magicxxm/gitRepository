package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.CustomerShipmentPosition;
import wms.domain.common.PickingOrderPosition;
import wms.domain.common.StockUnit;

import java.util.List;

public interface PickingOrderPositionRepository extends BaseRepository<PickingOrderPosition, String> {

    @Query("select p from PickingOrderPosition p where p.stockUnit = :stockUnit")
    List<PickingOrderPosition> getByStockUnit(@Param("stockUnit") StockUnit stockUnit);

    @Query("select po from PickingOrderPosition po where po.customerShipmentPosition=:customerShipmentPosition")
    List<PickingOrderPosition> getAllByCustomerShipmentPosition(@Param("customerShipmentPosition")CustomerShipmentPosition customerShipmentPosition);

}
