package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.AdviceRequest;
import org.springframework.stereotype.Repository;

/**
 * Created by 123 on 2018/2/2.
 */
@Repository
public interface AdviceRepository extends BaseRepository<AdviceRequest,String> {
}
