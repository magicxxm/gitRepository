package com.mushiny.wms.tot.jobcategory.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.tot.jobcategory.domain.Jobcategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Laptop-8 on 2017/6/8.
 */
public interface JobcategoryRepository extends BaseRepository<Jobcategory,String> {
    @Query(" select j from Jobcategory j where j.jobType = :jobType " )
    List<Jobcategory> findJobcategoryListByJobType(@Param("jobType")String jobType);

    @Query(" select j from Jobcategory j order by j.name" )
    List<Jobcategory> findJobcategoryList();

    @Query(" select j from Jobcategory j where j.code = :code " )
    Jobcategory findJobcategoryByCode(@Param("code")String code);

    @Query(" select j.name from Jobcategory j where j.jobType = :jobType " )
    List<String> getJobcategoryListByJobType(@Param("jobType")String jobType);

    @Query(" select j.id from Jobcategory j where j.name = :name " )
    String findIdByName(@Param("name")String name);
}
