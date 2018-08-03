package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.Semblence;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by prestonmax on 2017/6/11.
 */
public interface SemblenceRepository extends BaseRepository<Semblence,String> {

    @Query("select s from Semblence s where s.entityLock=0")
    Semblence getSemblence();
}
