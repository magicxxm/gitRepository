package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.Sequence;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2018/2/8.
 */
public interface SequenceRepository extends BaseRepository<Sequence,String> {

    @Query("select s from Sequence s where s.sequenceNo = :sequenceNo")
    Sequence getBySequenceNo(@Param("sequenceNo")String number);
}
