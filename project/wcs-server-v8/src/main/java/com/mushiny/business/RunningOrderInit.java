package com.mushiny.business;

import com.mushiny.beans.enums.TripStatus;
import com.mushiny.comm.CommonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/21.
 */
@Component
@org.springframework.core.annotation.Order(value = 1)
public class RunningOrderInit implements CommandLineRunner {
    private final static Logger logger = LoggerFactory.getLogger(RunningOrderInit.class);
    private Map processRobots = new HashMap();
    @Autowired
    private JdbcRepository jdbcRepository;
    private int restoreProcessTrip2Available(){
        String getProcessTrip_sql = "select * from RCS_TRIP where trip_state=? or trip_state=?";
        String updateProcessTrip_sql = "update RCS_TRIP set trip_state=? where ID=?";
        String getProcessTripPosition_sql = "select * from RCS_TRIPPOSITION where TRIP_ID=? and tripposition_state=?";
        String updateProcessTripPosition_sql = "update RCS_TRIPPOSITION set tripposition_state=? where ID=?";
        List<Map> rows = this.jdbcRepository.queryBySql(getProcessTrip_sql.toUpperCase(), TripStatus.PROCESS, TripStatus.LEAVING);
        if (rows!=null && rows.size()>0){
            logger.debug("上一次未完成的调度单有："+rows.size()+"条");
            List<Map> positionRows;
            for (int i = 0; i < rows.size(); i++) {
                Map map =  rows.get(i);
                String tripId = CommonUtils.parseString("ID", map);
                String robotId = CommonUtils.parseString("DRIVE_ID", map);
                String tripType = CommonUtils.parseString("TRIP_TYPE", map);
                processRobots.put(robotId, map);
                this.jdbcRepository.updateBySql(updateProcessTrip_sql.toUpperCase(), TripStatus.AVAILABLE, tripId);
                logger.debug("更新RCS_TRIP状态Process-->Available成功,tripId:"+tripId);
                positionRows = this.jdbcRepository.queryBySql(getProcessTripPosition_sql.toUpperCase(), tripId, TripStatus.PROCESS);
                if (positionRows!=null && positionRows.size()>0){
                    logger.debug("调度单tripId:"+tripId+"中正在进行的明细有："+positionRows.size()+"条");
                    for (int j = 0; j < positionRows.size(); j++) {
                        Map positionMap =  positionRows.get(j);
                        String positionId = CommonUtils.parseString("ID", positionMap);
                        this.jdbcRepository.updateBySql(updateProcessTripPosition_sql.toUpperCase(), TripStatus.AVAILABLE, positionId);
                        logger.debug("更新RCS_TRIPPOSITION状态Process-->Available成功,tripPositionId:"+positionId);
                    }
                }
            }
        }else{
            logger.debug("重启wcs后，未扫描到上一次正在执行中的调度单");
        }
        return 1;
    }

    private void clearRobots(){
        Map delMap = new HashMap();
        delMap.put("1","1");
        int delNum = jdbcRepository.deleteRecords("WCS_ROBOT", delMap);
        logger.debug("启动WCS，清除所有小车共："+delNum);
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.debug("启动WCS，准备清除数据库所有小车...");
        clearRobots();
        restoreProcessTrip2Available();
        logger.debug("启动WCS，已经清除数据库所有小车，并还原所有Process调度单为Available...");
    }
}
