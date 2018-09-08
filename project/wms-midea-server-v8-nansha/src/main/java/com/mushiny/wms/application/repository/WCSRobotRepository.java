package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.WCSRobot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WCSRobotRepository extends JpaRepository<WCSRobot, String> {

    @Query(" select r from WCSRobot r " +
            " where r.sectionId = :sectionId and r.robotId=:robotId"
    )
    WCSRobot getWCSRobotByRobotId(@Param("robotId") String robotId, @Param("sectionId") String sectionId);

    @Query(" select r from WCSRobot r " +
            " where r.sectionId = :sectionId and r.addressCodeId in (select c.placeMark from Charger c where c.entityLock = 0 and  c.chargerType=1 and c.sectionId = :sectionId  )"
    )
    List<WCSRobot> getWCSRobotByCharger(@Param("sectionId") String sectionId);


    @Query(" select r from WCSRobot r " +
            " where r.available = true" +
            " and r.status in (1,2,3) " +
            " and r.sectionId = :sectionId" +
            "  and r.addressCodeId <>0 and r.robotId not in (" +
            "   select coalesce(t.driveId,'') from Trip t" +
            "   where t.tripState in :tripStates and t.sectionId = :sectionId) ")
    List<WCSRobot> getAvailableDrives(@Param("tripStates") List<String> tripStates,
                                      @Param("sectionId") String sectionId);


    @Query(" select r from WCSRobot r " +
            " where r.available = true" +
            " and r.status in (1,2,3) " +
            " and r.sectionId = :sectionId" +
            "  and r.addressCodeId <>0 and r.robotId not in (" +
            "   select coalesce(t.driveId,'') from Trip t" +
            "   where t.tripState in :tripStates and t.sectionId = :sectionId) and r.addressCodeId in(select distinct coalesce(n.node.addressCodeId,'') from MdStationnodeposition n left join n.stationnode sn   left join n.node nod where sn.type in :stationType and nod.type=8 and n.nodeType=1)")
    List<WCSRobot> getDrives(@Param("tripStates") List<String> tripStates,
                                      @Param("sectionId") String sectionId,@Param("stationType") List<Integer> stationType);


    @Query(" select r from WCSRobot r " +
            " where r.available = true" +
            " and r.status in (1,2,3) " +
            " and r.sectionId = :sectionId and r.robotId=:robotId" +
            "  and r.addressCodeId <>0" +
            " and r.robotId not in (" +
            "   select coalesce(t.driveId,'') from Trip t " +
            "   where t.tripState <>'Finish')")
    List<WCSRobot> driverCanUse(@Param("robotId") String robotId,
                                      @Param("sectionId") String sectionId);
    @Query(" select r from WCSRobot r " +
            " where r.available = true" +
            " and r.status in (1,2,3) " +
            " and r.robotId = :robotId" +
            " and r.robotId not in (" +
            "   select coalesce(t.driveId,'') from Trip t" +
            "   where t.tripState in :tripStates and t.driveId = :robotId) ")
    WCSRobot getAvailableDrive(@Param("tripStates") List<String> tripStates,
                               @Param("robotId") String robotId);

    @Query("select r from WCSRobot r where r.addressCodeId = :addressCodeId and r.sectionId = :sectionId")
    WCSRobot getByAddressCodeId(@Param("addressCodeId") int addressCodeId,
                                @Param("sectionId") String sectionId);



    @Query("select r from WCSRobot r where (r.addressCodeId = :addressCodeId or r.targetAddrCodeId=:addressCodeId) and r.sectionId = :sectionId")
    List<WCSRobot> getSleepAddrRobots(@Param("addressCodeId") int addressCodeId,
                                      @Param("sectionId") String sectionId);
    @Query(" select r from WCSRobot r " +
            " where r.laveBattery < :maxLaveBattery " +
            " and r.laveBattery > :minLaveBattery and r.sectionId=:sectionId " +
            " and r.status in (1,2,3) " +
            " and r.addressCodeId not in (select c.placeMark from Charger c where c.sectionId = :sectionId)" +
            " and r.robotId not in (" +
            "   select coalesce(t.driveId,'') from Trip t" +
            "   where t.tripType = :tripType and t.tripState in :tripStates and t.sectionId = :sectionId) " +
            " order by r.laveBattery")
    List<WCSRobot> getRobotsCanCharged(@Param("tripType") String tripType,
                                       @Param("tripStates") List<String> tripStates,
                                       @Param("sectionId") String sectionId,
                                       @Param("minLaveBattery") int minLaveBattery,
                                       @Param("maxLaveBattery") int maxLaveBattery);

    @Query(" select r from WCSRobot r " +
            " where (r.laveBattery <= :laveBattery or r.voltage <= :voltage) and r.sectionId=:sectionId " +
            " and r.status in (1,2,3) " +
            " and r.addressCodeId not in (select c.placeMark from Charger c where c.sectionId = :sectionId)" +
            " and r.robotId not in (" +
            "   select coalesce(t.driveId,'') from Trip t" +
            "   where t.tripType = :tripType and t.tripState in :tripStates and t.sectionId = :sectionId) " +
            " order by r.laveBattery")
    List<WCSRobot> getRobotsMustCharged(@Param("tripType") String tripType,
                                        @Param("tripStates") List<String> tripStates,
                                        @Param("sectionId") String sectionId,
                                        @Param("voltage") int voltage,
                                        @Param("laveBattery") int laveBattery);

    @Query(" select r from WCSRobot r " +
            " where r.voltage <= :voltage and r.sectionId=:sectionId " +
            " and r.status in (1,2,3) " +
            " and r.addressCodeId not in (select c.placeMark from Charger c where c.sectionId = :sectionId)" +
            " and r.robotId not in (" +
            "   select coalesce(t.driveId,'') from Trip t" +
            "   where t.tripType = :tripType and t.tripState in :tripStates and t.sectionId = :sectionId) " +
            " order by r.voltage")
    List<WCSRobot> getRobotsVoltageMinValue(@Param("tripType") String tripType,
                                        @Param("tripStates") List<String> tripStates,
                                        @Param("sectionId") String sectionId,
                                        @Param("voltage") int voltage);

}
