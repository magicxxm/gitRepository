package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.Stationnode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/7/10 0010.
 */
public interface StationnodeRepository extends JpaRepository<Stationnode, String> {

    @Query(value = "SELECT \n" +
            "  t.ID,\n" +
            "  t.NAME,\n" +
            "  t.TYPE,\n" +
            "  t.IS_CALL_POD,\n" +
            "  GROUP_CONCAT(n.ADDRESSCODEID) ADDRS,\n" +
            "  p.ID MAP_ID,\n" +
            "  p.NAME MAP_NAME,\n" +
            "  s.ID SECTION_ID,\n" +
            "  s.NAME SECTION_NAME,\n" +
            "  s.RCS_SECTIONID, \n" +
            "  s.WAREHOUSE_ID \n" +
            "FROM\n" +
            "  MD_STATIONNODE t \n" +
            "  LEFT JOIN MD_STATIONNODEPOSITION t2 \n" +
            "    ON t.ID = t2.STATIONNODE_ID \n" +
            "  LEFT JOIN WD_NODE n \n" +
            "    ON n.ID = t2.NODE_ID \n" +
            "  LEFT JOIN WD_MAP p \n" +
            "    ON n.MAP_ID = p.ID \n" +
            "  LEFT JOIN WD_SECTION s \n" +
            "    ON s.ID = p.SECTION_ID \n" +
            "GROUP BY t.ID,\n" +
            "  t.NAME,\n" +
            "  t.TYPE,\n" +
            "  t.IS_CALL_POD,\n" +
            "  p.ID,\n" +
            "  p.NAME,\n" +
            "  s.ID,\n" +
            "  s.NAME,\n" +
            "  s.RCS_SECTIONID", nativeQuery = true)
    List<Stationnode> getAllStationNode();


    /**
     *
     * @param stationName
     * @param sectionId
     * @return
     */

    @Modifying
    @Query(value = " update MD_STATIONNODE w set w.IS_CALL_POD=:isCall" +
            " where w.NAME = :stationName  and w.SECTION_ID = :sectionId", nativeQuery = true
    )
    Integer workstationCall(@Param("stationName") String stationName, @Param("isCall") Integer isCall, @Param("sectionId") String sectionId) ;

    @Query("select s from Stationnode s where s.type=2 and s.name like 'PZZ1XT%'")
    List<Stationnode> getAllOutStation();
}
