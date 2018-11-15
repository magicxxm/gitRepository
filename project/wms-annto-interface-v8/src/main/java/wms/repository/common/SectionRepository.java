package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.WdSection;

import java.util.List;

/**
 * Created by 123 on 2017/8/11.
 */
public interface SectionRepository extends BaseRepository<WdSection,String> {

    @Query("select s from WdSection s where s.warehouseId=:warehouseId")
    List<WdSection> getByWarehouseId(@Param("warehouseId") String warehouseId);
}
