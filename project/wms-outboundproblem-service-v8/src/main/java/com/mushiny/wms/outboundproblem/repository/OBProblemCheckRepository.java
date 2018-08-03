package com.mushiny.wms.outboundproblem.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.OBProblem;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;
import com.mushiny.wms.outboundproblem.domain.common.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OBProblemCheckRepository extends BaseRepository<OBProblemCheck, String> {


    @Query(" select o from OBProblem o where o.container = :container")
    List<OBProblem> getByContainer(@Param("container") String container);

    @Query(" select o from OBProblemCheck o where o.problemStoragelocation = :storagelocation and o.itemNo = :itemdataSku and o.state <> 'CLOSE' ")
    List<OBProblemCheck> getByStorageLocationName(@Param("storagelocation") String storagelocation, @Param("itemdataSku") String itemdataSku);



    @Query(" select o from OBProblemCheck o where o.problemStoragelocation = :storagelocation and o.itemData = :itemData " )
    OBProblemCheck getProblem(@Param("storagelocation") String storagelocation, @Param("itemData") String itemData);


    @Query("SELECT u.name FROM User u where u.id =:userId ")
    String getByUserIds(@Param("userId") String userId);


    @Query("select s from StorageLocation s where s.name =:storageLocationName ")
    StorageLocation getByProblemStorageLocationName(@Param("storageLocationName") String storageLocationName);

    @Query("select s from ItemData s where s.itemNo =:itemdataSku ")
    ItemData getByItemData(@Param("itemdataSku") String itemdataSku);

    @Query("SELECT count(a.id) FROM OBProblemCheck a " +
            " WHERE a.state <> 'CLOSE' " +
            " AND a.problemStoragelocation = :problemStorageLocation " +
            " AND a.id <> :id ")
    Integer countIdByContainer(@Param("problemStorageLocation") String problemStorageLocation,
                               @Param("id") String id);

}
