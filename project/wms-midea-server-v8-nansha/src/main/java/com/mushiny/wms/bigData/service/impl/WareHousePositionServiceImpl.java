package com.mushiny.wms.bigData.service.impl;

import com.mushiny.wms.application.domain.WmsWarehousePosition;

import com.mushiny.wms.application.domain.enums.AddressStatus;

import com.mushiny.wms.application.repository.WmsWarehousePositionRepository;
import com.mushiny.wms.application.service.BatchService;
import com.mushiny.wms.bigData.service.WareHousePositionService;
import com.mushiny.wms.common.utils.EntityManagerUtil;
import com.mushiny.wms.pathPlanning.business.PathCommonBusiness;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/11.
 */
@Service
@Transactional
public class WareHousePositionServiceImpl implements WareHousePositionService {
    private final EntityManagerUtil entityManagerUtil;
    @Autowired
    private PathCommonBusiness pathCommonBusiness;
    @Autowired
    private BatchService batchService;
    @Autowired
    private WmsWarehousePositionRepository wmsWarehousePositionRepository;
  @Autowired
    public WareHousePositionServiceImpl(EntityManagerUtil entityManagerUtil) {
        this.entityManagerUtil = entityManagerUtil;
    }


    private void initMap() {
        List<com.mushiny.wms.application.domain.Map> mapsTemp = pathCommonBusiness.getMapRepository().getAllActiveMap();
        if (!CollectionUtils.isEmpty(mapsTemp)) {

            for (com.mushiny.wms.application.domain.Map temp : mapsTemp) {


                doExecute(temp);


            }
        }


    }

    private void doExecute(com.mushiny.wms.application.domain.Map mapId )
    {
        String sql="SELECT " +
                " CONCAT(WD_NODE.ADDRESSCODEID,'')  as addresscodeid , CONCAT(MD_POD.POD_INDEX,'') as podIndex,MD_POD.STATE as podStatu, WCS_ROBOT.ROBOT_ID as robotId, WMS_INBOUND_INSTRUCT.MO_NAME as orderCode,WMS_INBOUND_INSTRUCT.MITEM_CODE as mitemCode, CONCAT(WMS_INBOUND_INSTRUCT.STOCK_QTY,'') as amount FROM WD_NODE\n" +
                "INNER JOIN WD_MAP ON WD_MAP.ID=WD_NODE.MAP_ID AND  WD_MAP.ACTIVE=1 AND WD_NODE.WAREHOUSE_ID=WD_MAP.WAREHOUSE_ID\n" +
                "LEFT JOIN MD_POD ON WD_NODE.ADDRESSCODEID=MD_POD.PLACEMARK  \n" +
                " LEFT JOIN WMS_INV_UNITLOAD ON WMS_INV_UNITLOAD.POD_INDEX=MD_POD.POD_INDEX and WMS_INV_UNITLOAD.ENTITY_LOCK=0  \n" +
                "LEFT JOIN WMS_INBOUND_INSTRUCT ON WMS_INV_UNITLOAD.INBOUND_INSTRUCT_ID=WMS_INBOUND_INSTRUCT.ID\n" +
                "LEFT JOIN WCS_ROBOT ON WD_NODE.ADDRESSCODEID=WCS_ROBOT.ADDRESSCODEID\n" +
                "WHERE WD_MAP.SECTION_ID=:sectionId AND WD_MAP.WAREHOUSE_ID=:wareHouseId ";

        Map param = new HashMap();
        param.put("sectionId", mapId.getSectionId());
        param.put("wareHouseId", mapId.getWarehouseId());
        List<WmsWarehousePosition> positions = entityManagerUtil.executeCondotionNativeQuery(sql,param, WmsWarehousePosition.class);

        List<WmsWarehousePosition> dataBase=wmsWarehousePositionRepository.findAll();
        List update=new ArrayList();
        List save=new ArrayList();
        for(WmsWarehousePosition wwp:positions)
        {

            if(ObjectUtils.isEmpty(wwp.getPodIndex()))
            {
                wwp.setNodeState(AddressStatus.EMPTY);
            }else if("Reserved".equalsIgnoreCase(wwp.getPodStatu()))
            {
                wwp.setNodeState(AddressStatus.RESERVED);
            }else if("Available".equalsIgnoreCase(wwp.getPodStatu()))
            {
                wwp.setNodeState(AddressStatus.AVALIABLE);
            }else if(!ObjectUtils.isEmpty(wwp.getRobotId()))
            {
                wwp.setNodeState(AddressStatus.OCCUPIED);

            }

            boolean find=false;
            WmsWarehousePosition findPo=null;
            for(WmsWarehousePosition db:dataBase)
            {
                if(db.getAddresscodeid().equals(wwp.getAddresscodeid())){
                    find=true;
                    findPo=db;
                    break;
                }
            }

            if(find)
            {
                if(!ObjectUtils.nullSafeEquals(findPo,wwp))
                {
                    BeanUtils.copyProperties(wwp,findPo,new String[]{"id","createdDate","createdBy","modifiedDate","modifiedBy","additionalContent","entityLock","version"});
                    update.add(findPo);
                }

            }else if(!ObjectUtils.isEmpty(wwp)){
                save.add(wwp);
            }

        }

        batchService.batchInsert(save);
        batchService.batchUpdate(update);

    }
    @Override
    public void execute() {

        initMap();

    }
}
