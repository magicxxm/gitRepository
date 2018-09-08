package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.Pod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PodRepository extends JpaRepository<Pod, String> {
    @Query(" select n from Pod n " +
            " where n.id in :podId "
    )
    List<Pod>getPodById(@Param("podId") List<String> podId);

    @Query(" select n from Pod n " +
            " where n.id =:podId "
    )
    Pod getPodById(@Param("podId") String podId);


    @Query(" select n from Pod n " +
            " where n.podIndex = :podIndex  and n.sectionId=:sectionId"
    )
    Pod getByPodIndex(@Param("podIndex") int podIndex, @Param("sectionId") String sectionId);
    @Query(" select n from Pod n " +
            " where n.podIndex = :podIndex "
    )
    Pod getByPodIndex2(@Param("podIndex") int podIndex);
    @Query(" select n from Pod n " +
            " where n.name = :podName"
    )
    Pod getByPodName(@Param("podName") String podName);
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

    @Query(" select n from Pod n " +
            " where   n.placeMark=:placeMark and n.sectionId=:sectionId and n.state='Available' and n.id not in (select t.podId from Trip t where t.tripState <>'Finish' )"
    )
    Pod getWorkStationTrip( @Param("placeMark") Integer placeMark, @Param("sectionId") String sectionId);

    @Query(value = "SELECT MAX(p.POD_INDEX) FROM MD_POD p", nativeQuery = true)
    Integer getMaxIndexOfPod();
    @Query(" select n from Pod n " +
            " where n.sectionId=:sectionId and n.state='Available' and n.id not in (select coalesce(t.podId,'')  from Trip t where t.tripState <>'Finish' )" +
            " and n.placeMark in (select distinct nod.addressCodeId from MdStationnodeposition n " +
            " left join n.stationnode sn " +
            " left join n.node nod " +
            " where sn.type =:stationType  ) "
    )
    List<Pod> getPutBackPod(@Param("sectionId") String sectionId,@Param("stationType") Integer stationType);

    @Query(" select n from Pod n " +
            " where n.sectionId=:sectionId and n.state='Available' and n.id not in (select coalesce(t.podId,'')  from Trip t where t.tripState <>'Finish' )" +
            " and n.placeMark in (select distinct nod.addressCodeId from MdStationnodeposition n " +
            " left join n.stationnode sn " +
            " left join n.node nod " +
            " where sn.type in :stationType  ) "
    )
    List<Pod> getStationPod(@Param("sectionId") String sectionId,@Param("stationType") List<Integer> stationType);


}
