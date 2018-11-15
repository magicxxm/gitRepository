package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.PickPackCell;


/**
 * Created by 123 on 2017/5/3.
 */
public interface PickPackCellRepository extends BaseRepository<PickPackCell,String> {

    @Query("select p from PickPackCell p where p.digitalabel2Id=:digitalLabel2")
    PickPackCell getcellName(@Param("digitalLabel2") String digitalLabel2);

}
