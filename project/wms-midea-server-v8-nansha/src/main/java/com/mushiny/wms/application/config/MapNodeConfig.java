package com.mushiny.wms.application.config;

import com.mushiny.wms.application.business.dto.SectionMapNode;
import com.mushiny.wms.application.domain.MapNode;
import com.mushiny.wms.application.domain.enums.MapNodeClassValue;
import com.mushiny.wms.application.repository.MapNodeRepository;
import com.mushiny.wms.application.repository.MapRepository;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/17.
 */
@Component
@Order(1)
public class MapNodeConfig implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapNodeConfig.class);
    public static Map<String, List<SectionMapNode>> sectionMapNodes = new HashMap<>();
    @Autowired
    private MapNodeRepository mapNodeRepository;
    @Autowired
    private MapRepository mapRepository;

    private void init(List<SectionMapNode> sectionMapNodes, com.mushiny.wms.application.domain.Map map, MapNode mapNode, List<MapNode> mapNodes) {
        SectionMapNode sectionMapNode = new SectionMapNode();
        sectionMapNode.setCurrentMapNode(mapNode);
        sectionMapNode.setMap(map);
        final String classGroup = mapNode.getClassGroup();
        final String mapId = map.getId();
        if (!StringUtils.isEmpty(classGroup)) {
            final int classValue = mapNode.getClassValue();
            if (classValue == MapNodeClassValue.INSIZE) {
                sectionMapNode.setInAddress(true);

                sectionMapNode.setOutSizeMapNodes(searchOutsizeMapNode(mapId, mapNodes, classGroup, MapNodeClassValue.OUTSIZE));

            } else if (classValue == MapNodeClassValue.OUTSIZE) {

                sectionMapNode.setInAddress(false);
                sectionMapNode.setInSizeMapNodes(searchOutsizeMapNode(mapId, mapNodes, classGroup, MapNodeClassValue.INSIZE));

            } else {
                throw new ApiException(ExceptionEnum.EX_MAP_CLASSVALUE_NOT_LEGAL.name());
            }

        }
        sectionMapNodes.add(sectionMapNode);


    }

    private List<MapNode> searchOutsizeMapNode(String mapId, List<MapNode> mapNodes, String classGroup, int classValue) {
        List<MapNode> result = new ArrayList<>();
        for (MapNode temp : mapNodes) {
            if ((!StringUtils.isEmpty(temp.getClassGroup())) &&
                    temp.getClassGroup().equals(classGroup) &&
                    temp.getClassValue() == classValue &&
                    temp.getMapId().equals(mapId))

            {
                result.add(temp);
                break;
            }
        }
        return result;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.debug("开始加载地图.....");
        List<MapNode> mapNodes = mapNodeRepository.findAll();
        List<com.mushiny.wms.application.domain.Map> maps = mapRepository.findAllMap();
        for (com.mushiny.wms.application.domain.Map map : maps) {

            for (MapNode mapNode : mapNodes) {
                final String mapId = map.getId();
                final String mapNodeMapId = mapNode.getMapId();
                final String sectionId = map.getSectionId();
                if (mapId.equals(mapNodeMapId)) {
                    if (!sectionMapNodes.containsKey(sectionId)) {
                        List<SectionMapNode> sectionMapNodeTemp = new ArrayList<>();
                        init(sectionMapNodeTemp, map, mapNode, mapNodes);
                        sectionMapNodes.put(sectionId, sectionMapNodeTemp);

                    } else {
                        init(sectionMapNodes.get(sectionId), map, mapNode, mapNodes);


                    }
                }
            }

        }
        LOGGER.debug("地图信息\n{}", JSONUtil.toJSon(sectionMapNodes));

    }

    public static SectionMapNode findSectionMapNode(int addrCode, String sectionId) {
        SectionMapNode result = null;
        List<SectionMapNode> sectionMapNodesTemp = sectionMapNodes.get(sectionId);
        if (!CollectionUtils.isEmpty(sectionMapNodes) && !CollectionUtils.isEmpty(sectionMapNodesTemp)) {

            for (SectionMapNode sectionMapNode : sectionMapNodesTemp)

            {
                if (sectionMapNode.getCurrentMapNode().getAddressCodeId() == addrCode) {
                    result = sectionMapNode;
                    break;
                }
            }
        }

        return result;

    }


}
