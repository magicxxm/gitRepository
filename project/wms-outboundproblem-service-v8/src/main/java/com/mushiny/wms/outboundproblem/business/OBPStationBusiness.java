package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.outboundproblem.domain.OBPStation;
import com.mushiny.wms.outboundproblem.domain.common.User;
import com.mushiny.wms.outboundproblem.domain.common.WorkStation;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.repository.OBPStationRepository;
import com.mushiny.wms.outboundproblem.repository.common.UserRepository;
import com.mushiny.wms.outboundproblem.repository.common.WorkStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OBPStationBusiness {
    private final ApplicationContext applicationContext;
    private final OBPStationRepository obpStationRepository;
    private final UserRepository userRepository;
    private final WorkStationRepository workStationRepository;

    @Autowired
    public OBPStationBusiness(ApplicationContext applicationContext,
                              OBPStationRepository obpStationRepository,
                              UserRepository userRepository, WorkStationRepository workStationRepository) {
        this.applicationContext = applicationContext;
        this.obpStationRepository = obpStationRepository;
        this.userRepository = userRepository;
        this.workStationRepository = workStationRepository;
    }


    public OBPStation getOBPStation(String stationName) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        OBPStation obpStation = Optional
                .ofNullable(obpStationRepository.getByName(warehouseId, stationName))
                .orElseThrow(() -> new ApiException(OutBoundProblemException
                        .EX_SCANNING_OBJECT_NOT_FOUND.getName(), stationName));
        //如果工作站存在，判断工作站类型是不是问题处理工作站
        if(!obpStation.getObpStationType().getType().equals("问题处理")){
             throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_NOT_FOUND.getName(),stationName);
        }
        // 判断工作站是否被删除
        if (!obpStation.getEntityLock().equals(Constant.NOT_LOCKED)) {
            throw new ApiException(OutBoundProblemException
                    .EX_RECEIVING_STATION_HAS_DELETED.getName(), stationName);
        }

        User currentUser = userRepository.retrieve(applicationContext.getCurrentUser());
        //工作站有人
        if(obpStation.getWorkStation().getOperator()!=null&&obpStation.getWorkStation().getStationName()!=null){
            //判断物理工作站是否被占用
            if (!obpStation.getWorkStation().getOperator().equals(currentUser)||
                    !obpStation.getWorkStation().getStationName().equalsIgnoreCase(stationName)) {
                throw new ApiException(OutBoundProblemException
                        .EX_OBPROBLEM_STATION_HAS_USED.getName(), stationName);
            }
            obpStation.setOperator(currentUser);
            obpStationRepository.save(obpStation);
         //当前工作站没人，保存当前用户
        }else{
            WorkStation workStation=obpStation.getWorkStation();
            workStation.setOperator(currentUser);
            workStation.setStationName(stationName);
            workStationRepository.save(workStation);
            obpStation.setOperator(currentUser);
            obpStationRepository.save(obpStation);
        }
        return obpStation;
    }
}
