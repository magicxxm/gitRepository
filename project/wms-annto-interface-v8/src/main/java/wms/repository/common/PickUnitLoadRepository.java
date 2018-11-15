package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.PickingUnitLoad;
import wms.domain.common.UnitLoad;

import java.util.List;

/**
 * Created by 123 on 2017/4/27.
 */
public interface PickUnitLoadRepository  extends BaseRepository<PickingUnitLoad, String> {
    //根据工作站查询该工作站下绑定的所有货筐
    @Query("SELECT pu FROM  PickingUnitLoad pu  WHERE pu.unitLoad = :unitLoad and pu.state=600")
    PickingUnitLoad getPickingUnitLoadsByUnitLoad(@Param("unitLoad") UnitLoad unitLoad);
}
