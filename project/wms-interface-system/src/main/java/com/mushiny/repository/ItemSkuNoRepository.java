package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.ItemSkuNo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 123 on 2018/2/28.
 */
public interface ItemSkuNoRepository extends BaseRepository<ItemSkuNo,String>,ItemSkuNoRepositoryCustom {

    @Query("select i from ItemSkuNo i where i.itemNo = :itemNo ")
    List<ItemSkuNo> getByItemNo(@Param("itemNo")String itemNo);
}
