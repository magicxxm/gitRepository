package wms.repository.common;



import wms.domain.common.PickingOrderPosition;
import wms.domain.common.StockUnit;
import wms.domain.common.StockUnitRecord;
import wms.domain.common.UnitLoad;

import java.math.BigDecimal;

public interface StockUnitRecordRepositoryCustom {

    /**
     * An amount of items has been created on StorageLocation
     *
     * @param amount
     * @param to
     * @param activityCode    (activityCode)
     * @param comment
     * @return
     */
    StockUnitRecord recordCreation(BigDecimal amount, StockUnit to, String recodeType, String recordTool);

    /**
     *
     * @param amount
     * @param su
     * @param old
     * @param dest
     * @param comment
     * @param operator
     * @return
     */
    StockUnitRecord recordTransfer(BigDecimal amount, StockUnit su, UnitLoad old, UnitLoad dest, String comment, String operator, String recodeType, String recordTool);


    StockUnitRecord createRecord(PickingOrderPosition pick, StockUnit toStock, BigDecimal amountDamaged, String recodeType);

    StockUnitRecord createRecord(PickingOrderPosition pick, StockUnit stockUnit);

}
