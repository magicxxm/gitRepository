package com.mushiny.wcs.application.repository;

import com.mushiny.wcs.application.domain.Pod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PodRepository extends JpaRepository<Pod, String> {
    @Query(" select n from Pod n " +
            " where n.placeMark = :placeMark  and (n.state='Available' or n.state='Reserved') and n.sectionId=:sectionId"
    )
    List<Pod> getByAddressCodeId(@Param("placeMark") int placeMark, @Param("sectionId") String sectionId);

    @Query(" select n from Pod n " +
            " where n.placeMark=:placeMark and n.id in (select t.podId from Trip t where t.id=:tripId)"
    )
    List<Pod> getByTripId(@Param("tripId") String tripId, @Param("placeMark") int placeMark);

    @Query(" select n from Pod n " +
            " where   n.tarAddrcodeId=:placeMark and n.sectionId=:sectionId and n.id in (select t.podId from Trip t where t.tripState in :tripState )"
    )
    List<Pod> getByTripTarAddrId(@Param("tripState") List<String> tripState, @Param("placeMark") String placeMark, @Param("sectionId") String sectionId);


}
