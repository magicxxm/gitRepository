package com.mushiny.wms.schedule.business;

import com.mushiny.wms.schedule.common.Parameter;
import com.mushiny.wms.schedule.domin.*;
import com.mushiny.wms.schedule.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScanningBusiness {

    @PersistenceContext
    private EntityManager entityManager;

    private final StowStationRepository stowStationRepository;
    private final UserRepository userRepository;
    private final Parameter parameter;
    private final WorkStationRepository workStationRepository;
    private final StorageLocationTypeRepository storageLocationTypeRepository;
    private final PickStationRepository pickStationRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final PackingStationRepository packingStationRepository;
    private static final Logger log= LoggerFactory.getLogger(ScanningBusiness.class);

    @Autowired
    public ScanningBusiness(StowStationRepository stowStationRepository,
                            UserRepository userRepository,
                            Parameter parameter,
                            WorkStationRepository workStationRepository,
                            StorageLocationTypeRepository storageLocationTypeRepository,
                            PickStationRepository pickStationRepository,
                            StockUnitRecordRepository stockUnitRecordRepository,
                            PackingStationRepository packingStationRepository) {
        this.stowStationRepository = stowStationRepository;
        this.userRepository = userRepository;
        this.parameter = parameter;
        this.workStationRepository = workStationRepository;
        this.storageLocationTypeRepository = storageLocationTypeRepository;
        this.pickStationRepository = pickStationRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.packingStationRepository = packingStationRepository;
    }

    public User getCurrentUser(){
        return  userRepository.getByName(parameter.getUserName());
    }

    public StowStation getStowLogicStation(){
        return stowStationRepository.getByName(parameter.getStowLogicStation());
    }

    public WorkStation getStowWorkStation(){
        return workStationRepository.getByName(parameter.getStowWorkStation());
    }

    public PackingStation getPackLogicStation(){
        return packingStationRepository.getByName(parameter.getPackLogicStation());
    }

    public WorkStation getPackWorkStation(){
        return workStationRepository.getByName(parameter.getPackWorkStation());
    }

    public void scanningStowStation(){
        StowStation stowStation = getStowLogicStation();
        User user = getCurrentUser();
        stowStation.setOperator(user);
        stowStationRepository.save(stowStation);
        WorkStation workStation=stowStation.getWorkStation();
        workStation.setCallPod(true);
        workStation.setOperator(user);
        workStation.setStationName(stowStation.getName());
        workStationRepository.save(workStation);
        //查询所有的BIN
        List<StorageLocationType> list=storageLocationTypeRepository.getByName("BIN");
        getByStorageLocationType(stowStation.getId(),list);
    }
    public void loginOutStowStation(){
        StowStation stowStation = getStowLogicStation();
        stowStation.setOperator(null);
        stowStationRepository.save(stowStation);
        WorkStation workStation=stowStation.getWorkStation();
        workStation.setOperator(null);
        workStation.setStationName(null);
        workStation.setCallPod(false);
        workStationRepository.save(workStation);
    }
    public void loginOutPickStation(){
        PickStation pickStation = getPickLogicStation();
        pickStation.setOperator(null);
        pickStationRepository.save(pickStation);
        WorkStation workStation=pickStation.getWorkStation();
        workStation.setOperator(null);
        workStation.setStationName(null);
        workStation.setCallPod(false);
        workStationRepository.save(workStation);
    }
    private void getByStorageLocationType(String id,List<StorageLocationType> li){
        String sql="select STORAGELOCATIONTYPE_ID from IB_STOWSTATIONBINTYPE where STOWSTATION_ID=:id";
        Query query=entityManager.createNativeQuery(sql);
        query.setParameter("id",id);
        List<String> list=query.getResultList();
        if(list.isEmpty()){
            for(StorageLocationType storageLocationType:li){
                String sql2="insert into IB_STOWSTATIONBINTYPE values(?,?)";
                Query query2=entityManager.createNativeQuery(sql2);
                query2.setParameter(1,storageLocationType.getId());
                query2.setParameter(2,id);
                query2.executeUpdate();
            }
        }
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime())+"已登录上架工作站-------------");
    }

    public PickStation getPickLogicStation(){
        return pickStationRepository.getByName(parameter.getPickLogicStation());
    }

    public WorkStation getPickWorkStation(){
        return workStationRepository.getByName(parameter.getPickWorkStation());
    }

    public void scanningPickStation(){
        PickStation pickStation = getPickLogicStation();
        User user = getCurrentUser();
        pickStation.setOperator(user);
        pickStationRepository.save(pickStation);
        WorkStation workStation=pickStation.getWorkStation();
        workStation.setCallPod(true);
        workStation.setOperator(user);
        workStation.setStationName(pickStation.getName());
        workStationRepository.save(workStation);
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime())+"已登录拣货工作站--------------------");
    }

    public void finishCallPod(WorkStation workStation){
        PickStation pickStation=getPickLogicStation();
        BigDecimal amount=stockUnitRecordRepository.getByDate(pickStation.getModifiedDate());
        if(amount.compareTo(new BigDecimal(parameter.getAmount()))>=0){
            workStation.setCallPod(false);
            workStationRepository.save(workStation);
        }
    }
    public void scanPackingStation(){
        PackingStation packStation = getPackLogicStation();
        User user = getCurrentUser();
        packStation.setOperator(user);
        packingStationRepository.save(packStation);
        WorkStation workStation=packStation.getWorkStation();
        workStation.setOperator(user);
        workStation.setStationName(packStation.getName());
        workStationRepository.save(workStation);
    }

    public void loginOutPackStation(){
        PackingStation packStation = getPackLogicStation();
        packStation.setOperator(null);
        packingStationRepository.save(packStation);
        WorkStation workStation=packStation.getWorkStation();
        workStation.setOperator(null);
        workStation.setStationName(null);
        workStation.setCallPod(false);
        workStationRepository.save(workStation);
    }
}
