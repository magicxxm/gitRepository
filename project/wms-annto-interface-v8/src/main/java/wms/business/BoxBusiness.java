package wms.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import wms.crud.dto.BoxDTO;
import wms.domain.AnntoBox;
import wms.domain.common.BoxType;
import wms.domain.common.Client;
import wms.domain.common.Warehouse;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/12/6.
 */
@Component
public class BoxBusiness {

    private final Logger log = LoggerFactory.getLogger(BoxBusiness.class);

    private final EntityManager manager;

    public BoxBusiness(EntityManager manager){
        this.manager = manager;
    }

    public AnntoBox createAnntoBox(BoxDTO boxDTO) {
        AnntoBox anntoBox = new AnntoBox();
        anntoBox.setCompanyCode(boxDTO.getCompanyCode());
        anntoBox.setContainerCode(boxDTO.getContainerCode());
        anntoBox.setContainerName(boxDTO.getContainerName());
        anntoBox.setContainerRemark(boxDTO.getContainerRemark());
        anntoBox.setContainerType(boxDTO.getContainerType());
        anntoBox.setHeight(boxDTO.getHeight());
        anntoBox.setLength(boxDTO.getLength());
        anntoBox.setWarehouseCode(boxDTO.getWarehouseCode());
        anntoBox.setWeight(boxDTO.getWeight());
        anntoBox.setWidth(boxDTO.getWidth());

        manager.persist(anntoBox);

        return anntoBox;
    }

    public AnntoBox updateAnntoBox(AnntoBox anntoBox, BoxDTO boxDTO) {

        anntoBox.setCompanyCode(boxDTO.getCompanyCode());
        anntoBox.setContainerName(boxDTO.getContainerName());
        anntoBox.setContainerRemark(boxDTO.getContainerRemark());
        anntoBox.setContainerType(boxDTO.getContainerType());
        anntoBox.setHeight(boxDTO.getHeight());
        anntoBox.setLength(boxDTO.getLength());
        anntoBox.setWarehouseCode(boxDTO.getWarehouseCode());
        anntoBox.setWeight(boxDTO.getWeight());
        anntoBox.setWidth(boxDTO.getWidth());

        return anntoBox;
    }

    public BoxType createBoxType(BoxDTO boxDTO, Warehouse warehouse, Client client) {

        BoxType boxType = new BoxType();

        BigDecimal height = new BigDecimal(boxDTO.getHeight());
        BigDecimal width = new BigDecimal(boxDTO.getLength());
        BigDecimal depth = new BigDecimal(boxDTO.getWeight());

        boxType.setDepth(depth);
        if(boxDTO.getContainerRemark() == null || "".equals(boxDTO.getContainerRemark())){
            boxType.setDescription(boxDTO.getContainerName());
        }else {
            boxType.setDescription(boxDTO.getContainerRemark());
        }
        boxType.setHeight(height);
        boxType.setWeight(new BigDecimal(boxDTO.getWeight()));
        boxType.setWidth(width);
        boxType.setName(boxDTO.getContainerCode());//箱型名称
//        boxType.setBoxCode(boxDTO.getContainerCode());//箱型编码
        boxType.setTypeGroup(boxDTO.getContainerType());
        boxType.setVolume(width.multiply(height).multiply(depth));
        boxType.setClientId(client.getId());
        boxType.setWarehouseId(warehouse.getId());

        manager.persist(boxType);

        return boxType;
    }

    public BoxType updateBoxType(BoxType box, BoxDTO boxDTO) {

        BigDecimal height = new BigDecimal(boxDTO.getHeight());
        BigDecimal width = new BigDecimal(boxDTO.getLength());
        BigDecimal depth = new BigDecimal(boxDTO.getWeight());

        box.setDepth(depth);
        if(boxDTO.getContainerRemark() != null){
            box.setDescription(boxDTO.getContainerRemark());
        }else {
            box.setDescription(boxDTO.getContainerRemark());
        }
        box.setHeight(height);
        box.setWeight(new BigDecimal(boxDTO.getWeight()));
        box.setWidth(width);
        box.setTypeGroup(boxDTO.getContainerType());
        box.setVolume(width.multiply(height).multiply(depth));

        return box;
    }
}
