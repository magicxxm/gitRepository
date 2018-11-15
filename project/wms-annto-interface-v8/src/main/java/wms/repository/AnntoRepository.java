package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.AnntoItem;

/**
 * Created by 123 on 2017/8/10.
 */
public interface AnntoRepository extends BaseRepository<AnntoItem,String> {

    @Query("select a from AnntoItem a where a.code=:code")
    AnntoItem getByCode(@Param("code")String code);
}
