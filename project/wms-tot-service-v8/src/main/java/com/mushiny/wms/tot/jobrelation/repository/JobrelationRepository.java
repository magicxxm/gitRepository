package com.mushiny.wms.tot.jobrelation.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.tot.jobrelation.domain.Jobrelation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Laptop-8 on 2017/6/8.
 */
public interface JobrelationRepository extends BaseRepository<Jobrelation,String> {
    @Query(" select j from Jobrelation j where j.jobcategoryName = :jobcategoryName or " +
            "(j.operation = :operation and j.tool = :tool)" )
    Jobrelation findByJobcategoryNameOrOperationAndTool(@Param("jobcategoryName") String jobcategoryName,
                                                              @Param("operation") String operation,
                                                              @Param("tool") String tool);
}
