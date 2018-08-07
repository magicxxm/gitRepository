package com.mushiny.service.impl;

import com.mushiny.business.CommanBusiness;
import com.mushiny.common.crud.AccessDTO;
import com.mushiny.model.Client;
import com.mushiny.model.Sequence;
import com.mushiny.model.Warehouse;
import com.mushiny.repository.ClientRepository;
import com.mushiny.repository.SequenceRepository;
import com.mushiny.repository.WarehouseRepository;
import com.mushiny.service.SequenceService;
import com.mushiny.utils.StringUtil;
import com.mushiny.web.dto.ChuanMaInfo;
import com.mushiny.web.dto.ChuanMaInfoPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * Created by 123 on 2018/2/8.
 */
@Service
@Transactional
public class SequenceServiceImpl implements SequenceService {
    private final Logger log = LoggerFactory.getLogger(SequenceServiceImpl.class);

    private final EntityManager manager;
    private final SequenceRepository sequenceRepository;
    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;
    private final CommanBusiness commanBusiness;

    public SequenceServiceImpl(EntityManager manager, SequenceRepository sequenceRepository,
                               ClientRepository clientRepository,
                               CommanBusiness commanBusiness,
                               WarehouseRepository warehouseRepository) {
        this.manager = manager;
        this.commanBusiness = commanBusiness;
        this.sequenceRepository = sequenceRepository;
        this.clientRepository = clientRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public AccessDTO createSequence(ChuanMaInfo chuanMaInfo) {
        AccessDTO accessDTO = new AccessDTO();

        for (ChuanMaInfoPosition p : chuanMaInfo.getPositions()) {
            Sequence sequence = null;
            sequence = sequenceRepository.getBySequenceNo(p.getSequenceNo());
            if (sequence != null) {
                log.info("更新串码信息。。。。");
                accessDTO = updateSequence(sequence, p);
            } else {
                log.info("创建串码信息。。。。");
                accessDTO = generateSequence(p);
            }
        }
        return accessDTO;
    }

    private AccessDTO updateSequence(Sequence s, ChuanMaInfoPosition p) {
        AccessDTO accessDTO = new AccessDTO();
        s.setSequenceNo(p.getSequenceNo());
        s.setState(StringUtil.stringToint(p.getState()));
        s.setItemNo(p.getItemNo());
        Client client = clientRepository.getByClientNo(p.getClientNo());
        Warehouse warehouse = warehouseRepository.getByWarehouseNo(p.getWarehouseNo());

        if (warehouse == null) {
            log.info("串码 sequence ： " + p.getSequenceNo() + " 对应的仓库为空。。");
            accessDTO.setCode("1");
            accessDTO.setMsg("串码 sequence ： " + p.getSequenceNo() + " 对应的仓库为空。。");
            return accessDTO;
        }

        if (client == null) {
            log.info("新增客户： " + p.getClientNo());
            client = commanBusiness.generateClient(p.getClientNo());
        }

        s.setClientId(client.getId());
        s.setWarehouseId(warehouse.getId());
        return accessDTO;
    }

    private AccessDTO generateSequence(ChuanMaInfoPosition chuanMaInfo) {
        AccessDTO accessDTO = new AccessDTO();

        Sequence sequence = new Sequence();
        sequence.setItemNo(chuanMaInfo.getItemNo());
        sequence.setSequenceNo(chuanMaInfo.getSequenceNo());
        sequence.setState(StringUtil.stringToint(chuanMaInfo.getState()));

        Warehouse warehouse = warehouseRepository.getByWarehouseNo(chuanMaInfo.getWarehouseNo());
        if (warehouse == null) {
            log.info("串码 sequence ： " + chuanMaInfo.getSequenceNo() + " 对应的仓库为空。。");
            accessDTO.setCode("1");
            accessDTO.setMsg("串码 sequence ： " + chuanMaInfo.getSequenceNo() + " 对应的仓库为空。。");
            return accessDTO;
        }

        Client client = clientRepository.getByClientNo(chuanMaInfo.getClientNo());
        if (client == null) {
            log.info("新增客户： " + chuanMaInfo.getClientNo());
            client = commanBusiness.generateClient(chuanMaInfo.getClientNo());
        }

        sequence.setClientId(client.getId());
        sequence.setWarehouseId(warehouse.getId());

        manager.persist(sequence);

        return accessDTO;
    }
}
