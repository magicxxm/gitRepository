package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import wms.common.respository.BaseRepository;
import wms.domain.AnntoBox;

/**
 * Created by 123 on 2017/12/6.
 */
@Repository
public interface AnntoBoxRepository extends BaseRepository<AnntoBox,String> {

    @Query("select a from AnntoBox a where a.containerCode = :containerCode and a.warehouseCode = :warehouseCode and a.companyCode = :companyCode")
    AnntoBox getByContainerCodeAndWarehouseCode(@Param("containerCode")String containerCode,
                                                @Param("warehouseCode")String warehouseCode,
                                                @Param("companyCode")String companyCode);
}
