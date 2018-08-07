package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.SystemProperty;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemPropertyRepository extends BaseRepository<SystemProperty, String>,SystemPropertyRepositoryCustom{

}
