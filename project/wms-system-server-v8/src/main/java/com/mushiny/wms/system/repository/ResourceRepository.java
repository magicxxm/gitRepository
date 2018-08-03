package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.Resource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends BaseRepository<Resource, String> {

    Resource findByResourceKeyAndLocale(String resourceKey, String locale);

    List<Resource> findByLocaleOrderByResourceKey(String locale);

    @Query(" select dr from Resource dr " +
            " where not exists (" +
            " select 1 from Resource r " +
            " where r.resourceKey = dr.resourceKey " +
            " and r.resourceValue is not null " +
            " and r.locale = :locale) " +
            " and dr.locale = :defaultLocale " +
            " order by dr.resourceKey ")
    List<Resource> getByDefaultLocaleNotExistsLocale(@Param("locale") String locale,
                                                     @Param("defaultLocale") String defaultLocale);
}
