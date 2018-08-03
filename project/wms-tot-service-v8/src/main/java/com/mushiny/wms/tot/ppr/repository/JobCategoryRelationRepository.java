package com.mushiny.wms.tot.ppr.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.tot.ppr.domain.JobCategoryRelation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Laptop-8 on 2017/6/8.
 */
public interface JobCategoryRelationRepository extends BaseRepository<JobCategoryRelation,String> {
    @Query(" select j from JobCategoryRelation j order by j.reorder asc " )
    List<JobCategoryRelation> getJobCategoryRelations();

    @Query(" select j from JobCategoryRelation j where j.project =:project" )
    List<JobCategoryRelation> getRelations(@Param("project")String project);

    @Query("select j.project from JobCategoryRelation j where j.coreProcesses in " +
            "(select j.coreProcesses from JobCategoryRelation j where j.project =:project) group by j.project" )
    List<String> getJob(@Param("project")String project);
 /*
    @Query(" select j from Jobrelation j where j.jobcategoryName = :jobcategoryName or " +
            "(j.operation = :operation and j.tool = :tool)" )
    int deleteJobCategoryRelation(@Param("jobcategoryName") String jobcategoryName,
                                                        @Param("operation") String operation,
                                                        @Param("tool") String tool);
    @Query(" select j from Jobrelation j where j.jobcategoryName = :jobcategoryName or " +
            "(j.operation = :operation and j.tool = :tool)" )
    int upateJobCategoryRelation(@Param("jobcategoryName") String jobcategoryName,
                                          @Param("operation") String operation,
                                          @Param("tool") String tool);*/
}
