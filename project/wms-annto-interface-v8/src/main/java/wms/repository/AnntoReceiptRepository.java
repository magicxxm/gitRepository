package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.AnntoReceipt;

public interface AnntoReceiptRepository extends BaseRepository<AnntoReceipt, String> {

    @Query("select a from AnntoReceipt a where a.code=:code") // and a.warehouseCode=:warehouseCode
    AnntoReceipt getByCode(@Param("code")String code);//,@Param("warehouseCode")String warehouseCode
}
