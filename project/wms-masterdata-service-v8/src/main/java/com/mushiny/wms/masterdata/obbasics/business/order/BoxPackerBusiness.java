package com.mushiny.wms.masterdata.obbasics.business.order;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.obbasics.business.order.util.boxpacker.Container;
import com.mushiny.wms.masterdata.obbasics.business.order.util.boxpacker.Dimension;
import com.mushiny.wms.masterdata.obbasics.business.order.util.boxpacker.Item;
import com.mushiny.wms.masterdata.obbasics.business.order.util.boxpacker.Packager;
import com.mushiny.wms.masterdata.obbasics.domain.BoxType;
//import com.mushiny.wms.masterdata.obbasics.domain.CustomerOrder;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.BoxTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class BoxPackerBusiness {

    private final BoxTypeRepository boxTypeRepository;

    @Autowired
    public BoxPackerBusiness(BoxTypeRepository boxTypeRepository) {
        this.boxTypeRepository = boxTypeRepository;
    }

//    public String getBox(CustomerOrder customerOrder, ItemData itemData) {
//        List<ItemData> itemDataList = new ArrayList<>();
//        itemDataList.add(itemData);
//        return getBox(customerOrder, itemDataList);
//    }
//
//    public String getBox(CustomerOrder customerOrder, List<ItemData> itemDataList) {
//        // 按体积从小到大排序
//        List<BoxType> boxAsc =
//                boxTypeRepository.getAllBoxAsc(customerOrder.getWarehouse(), customerOrder.getClient());
//        if (boxAsc == null || boxAsc.isEmpty()) {
//            throw new ApiException(OutBoundException
//                    .EX_CLIENT_NOT_FOUND_BOX.toString(), customerOrder.getClient().getName());
//        }
//        // 获取箱子
//        return getBoxType(itemDataList, boxAsc);
//    }
//
//    public String getBag(CustomerOrder customerOrder, ItemData itemData) {
//        List<ItemData> itemDataList = new ArrayList<>();
//        itemDataList.add(itemData);
//        return getBag(customerOrder, itemDataList);
//    }
//
//    public String getBag(CustomerOrder customerOrder, List<ItemData> itemDataList) {
//        // 按体积从小到大排序
//        List<BoxType> bagAsc =
//                boxTypeRepository.getAllBagAsc(customerOrder.getWarehouse(), customerOrder.getClient());
//        if (bagAsc == null || bagAsc.isEmpty()) {
//            throw new ApiException(OutBoundException
//                    .EX_CLIENT_NOT_FOUND_BOX.toString(), customerOrder.getClient().getName());
//        }
//        // 获取袋子
//        return getBoxType(itemDataList, bagAsc);
//    }

    // boxTypeList按体积从小到大排序
    private String getBoxType(List<ItemData> itemDataList, List<BoxType> boxTypeListAsc) {
        // 处理说有的SKU
        List<Item> items = new ArrayList<>();
        for (ItemData itemData : itemDataList) {
            Item item = getItem(itemData);
            items.add(item);
        }
        // 获取箱子
        List<Dimension> containers = new ArrayList<>();
        // 最大容器是否放的下，放不下直接返回null
        Container container = getMaxContainer(boxTypeListAsc.get(boxTypeListAsc.size() - 1), items);
        if (container == null) {
            return null;
        }
        // 放的下,取最优容器
        for (BoxType boxType : boxTypeListAsc) {
            Dimension dimension = getDimension(boxType);
            containers.add(dimension);
        }
        Packager packager = new Packager(containers);
        // match to container
        Container match = packager.pack(items);
        return match.getName();
    }


    private Container getMaxContainer(BoxType maxBoxType, List<Item> items) {
        List<Dimension> containers = new ArrayList<>();
        containers.add(getDimension(maxBoxType));
        Packager packager = new Packager(containers);
        // match to container
        Container match = packager.pack(items);
        return match;
    }

    private Item getItem(ItemData itemData) {
        return new Item(
                itemData.getId(),
                toInt(itemData.getWidth()),
                toInt(itemData.getDepth()),
                toInt(itemData.getHeight()));
    }

    private Dimension getDimension(BoxType boxType) {
        return new Dimension(
                boxType.getId(),
                toInt(boxType.getWidth()),
                toInt(boxType.getDepth()),
                toInt(boxType.getHeight()));
    }

    private int toInt(BigDecimal bigDecimal) {
        return bigDecimal.setScale(0, BigDecimal.ROUND_CEILING).intValue();
    }
}
