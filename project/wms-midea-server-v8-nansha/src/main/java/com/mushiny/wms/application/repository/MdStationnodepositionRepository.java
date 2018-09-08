package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.MdStationnodeposition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/7/12.
 */
public interface MdStationnodepositionRepository extends JpaRepository<MdStationnodeposition, String> {

    @Query(" select n from MdStationnodeposition n where n.id=:positionId "
           )
    MdStationnodeposition getMdStationnodepositionById(@Param("positionId") String positionId);
    @Query(" select n from MdStationnodeposition n " +
            " left join n.stationnode sn  " +
            " left join n.node nod " +
            " where sn.type =:stationType and n.nodeType=1 ")
    List<MdStationnodeposition> getStationPositionByType(@Param("stationType") Integer stationType);

    @Query(" select n from MdStationnodeposition n " +
            " left join n.stationnode sn  " +
            " left join n.node nod " +
            " where sn.type =:stationType and nod.addressCodeId=:stationAddress and sn.name=:stationName and n.nodeType=:stationType")
    List<MdStationnodeposition> getStationPosition(@Param("stationType") Integer stationType,@Param("stationAddress") Integer stationAddress,@Param("stationName") String stationName);

    @Query(" select distinct n from MdStationnodeposition n " +
            " left join n.stationnode sn  " +
            " left join n.node nod " +
            " where sn.type =:stationType and nod.type=8 and sn.isCallPod=true and n.nodeType=1 and n.id not in (select distinct coalesce(t.mdNodePosition,'')from Trip t where t.tripState not in:tripState) and nod.addressCodeId not in (select distinct p.placeMark from Pod p  ) ")
    List<MdStationnodeposition> getCallPodStationPosition(@Param("stationType") Integer stationType ,@Param("tripState") List tripState);


    @Query(" select distinct n from MdStationnodeposition n " +
            " left join n.stationnode sn  " +
            " left join n.node nod " +
            " where sn.name=:stationName")
    List<MdStationnodeposition> getStationPositionByName(@Param("stationName") String stationName);

    @Query(" select n from MdStationnodeposition n " +
            " left join n.stationnode sn  " +
            " left join n.node nod " +
            " where sn.name=:lineCode and n.id not in (select coalesce(t.mdNodePosition,'') from Trip t where t.tripState not in:tripState ) and n.nodeType=1 and nod.addressCodeId not in (select distinct coalesce(p.placeMark,'')  from Pod p  )")
    List<MdStationnodeposition> getIdleStationPosition(@Param("tripState") List<String> tripState, @Param("lineCode") String lineCode);

}
