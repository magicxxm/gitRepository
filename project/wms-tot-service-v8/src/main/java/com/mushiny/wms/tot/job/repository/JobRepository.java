package com.mushiny.wms.tot.job.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.tot.job.domain.Job;
import com.netflix.ribbon.proxy.annotation.ClientProperties;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Created by Laptop-8 on 2017/6/8.
 */
public interface JobRepository extends BaseRepository<Job,String> {
    @Query(" select j from Job j where j.code = :code")
    Job getJob(@Param("code")String code);
    @Query(" select j from Job j where j.category in (select c.id from Jobcategory c where c.name = :name) order by j.code ")
    List<Job> getJobByCategory(@Param("name") String name);
}
