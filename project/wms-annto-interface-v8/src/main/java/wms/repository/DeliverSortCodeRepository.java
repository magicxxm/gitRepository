package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.DeliverySortCode;

/**
 * Created by 123 on 2017/6/13.
 */
public interface DeliverSortCodeRepository extends BaseRepository<DeliverySortCode,String> {

    @Query("select c from DeliverySortCode c where c.code = :code and c.warehouseId=:warehouse")
    DeliverySortCode getByCode(@Param("code") String code, @Param("warehouse") String warehouse);

}
