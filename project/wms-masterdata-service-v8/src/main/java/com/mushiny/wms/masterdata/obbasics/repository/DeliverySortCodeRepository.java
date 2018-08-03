package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.DeliverySortCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Laptop-11 on 2017/6/9.
 */
public interface DeliverySortCodeRepository extends BaseRepository<DeliverySortCode,String> {
    @Query("select b from DeliverySortCode b " +
            " where b.warehouseId = :warehouse and b.code = :code")
    DeliverySortCode getByName(@Param("warehouse") String warehouse,
                      @Param("code") String code);
}
