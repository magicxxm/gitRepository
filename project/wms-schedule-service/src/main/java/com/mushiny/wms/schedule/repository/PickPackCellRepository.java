package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.PickPackCell;
import com.mushiny.wms.schedule.domin.PickPackWall;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 123 on 2017/5/3.
 */
public interface PickPackCellRepository extends BaseRepository<PickPackCell,String> {

    @Query("select p from PickPackCell p where p.pickPackWall=:pickPackWall order by p.orderIndex")
    List<PickPackCell> getByPickPackWall(@Param("pickPackWall") PickPackWall pickPackWall);

    @Query("select p from PickPackCell p where p.digitalabel2Id=:digitalLabel2")
    PickPackCell getcellName(@Param("digitalLabel2") String digitalLabel2);

    @Query("select p.digitalabel1Id from PickPackCell p where p.name = :name and p.warehouseId = :warehouseId")
    String getDigitalByCellName(@Param("name") String name, @Param("warehouseId") String warehouseId);
}
