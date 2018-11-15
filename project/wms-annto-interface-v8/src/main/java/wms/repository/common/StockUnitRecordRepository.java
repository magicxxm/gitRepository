package wms.repository.common;


import wms.common.respository.BaseRepository;
import wms.domain.common.StockUnitRecord;

/**
 * Created by 123 on 2017/5/4.
 */
public interface StockUnitRecordRepository extends BaseRepository<StockUnitRecord,String>,StockUnitRecordRepositoryCustom {
}
