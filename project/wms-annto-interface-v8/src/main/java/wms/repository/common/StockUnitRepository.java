package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.ItemData;
import wms.domain.common.StockUnit;
import wms.domain.common.UnitLoad;

import java.math.BigDecimal;
import java.util.List;

public interface StockUnitRepository extends BaseRepository<StockUnit, String> {

    @Query(" select count (distinct s.itemData) from StockUnit s " +
            " where s.unitLoad = :unitLoad")
    long countByUnitLoad(@Param("unitLoad") UnitLoad unitLoad);

    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
            " where s.unitLoad = :unitLoad ")
    BigDecimal sumByUnitLoad(@Param("unitLoad") UnitLoad unitLoad);

    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
            " left join s.itemData i " +
            " where i.itemNo = :sku or i.skuNo = :sku and i.warehouseId = :warehouseId")
    BigDecimal sumBySKU(@Param("sku") String sku,
                        @Param("warehouseId") String warehouseId);

    @Query(" select coalesce(sum (s.amount),0) from StockUnit s " +
            " where s.unitLoad = :unitLoad and s.itemData = :itemData ")
    BigDecimal sumByUnitLoadAndItemData(@Param("unitLoad") UnitLoad unitLoad,
                                        @Param("itemData") ItemData itemData);

    @Query(" select s from StockUnit s where s.unitLoad = :unitLoad")
    List<StockUnit> getByUnitLoad(@Param("unitLoad") UnitLoad unitLoad);

    @Query(" select s from StockUnit s " +
            " left join s.itemData i " +
            " where (i.itemNo = :sku or i.skuNo = :sku) and s.amount <> 0 and s.warehouseId = :warehouseId")
    List<StockUnit> getBySKU(@Param("sku") String sku,
                             @Param("warehouseId") String warehouseId);

    @Query(" select s from StockUnit s " +
            " left join s.itemData i " +
            " where s.unitLoad = :unitLoad " +
            " and (i.itemNo = :sku or i.skuNo = :sku)")
    List<StockUnit> getByUnitLoadAndSKU(@Param("unitLoad") UnitLoad unitLoad,
                                        @Param("sku") String sku);

    @Query(" select s from StockUnit s " +
            " where s.unitLoad = :unitLoad and s.itemData = :itemData ")
    List<StockUnit> getByUnitLoadAndItemData(@Param("unitLoad") UnitLoad unitLoad,
                                             @Param("itemData") ItemData itemData);

    @Query(" select s from StockUnit s " +
            " left join s.itemData i " +
            " where s.unitLoad = :unitLoad " +
            " and i.itemNo = :itemNo " +
            " and i.clientId <> :clientId")
    StockUnit getByUnequalClientAndItemNo(@Param("unitLoad") UnitLoad unitLoad,
                                          @Param("itemNo") String itemNo,
                                          @Param("clientId") String clientId);
}
