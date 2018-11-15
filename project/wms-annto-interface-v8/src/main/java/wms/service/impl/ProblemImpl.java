package wms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.business.dto.ProblemDTO;
import wms.common.crud.AccessDTO;
import wms.common.exception.ApiException;
import wms.crud.common.mapper.OBProblemMapper;
import wms.domain.ItemData;
import wms.domain.ItemDataGlobal;
import wms.domain.OBProblem;
import wms.domain.common.CustomerShipment;
import wms.domain.common.Lot;
import wms.repository.CustomerShipmentRepository;
import wms.repository.OBProblemRepository;
import wms.repository.common.ItemDataGlobalRepository;
import wms.repository.common.ItemDataRepository;
import wms.repository.common.LotRepository;
import wms.service.Problem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ProblemImpl implements Problem {

    private final CustomerShipmentRepository customerShipmentRepository;
    private final OBProblemRepository obProblemRepository;
    private final OBProblemMapper obProblemMapper;
    private final ItemDataRepository itemDataRepository;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final LotRepository lotRepository;

    @Autowired
    public ProblemImpl(CustomerShipmentRepository customerShipmentRepository,
                       OBProblemRepository obProblemRepository,
                       OBProblemMapper obProblemMapper,
                       ItemDataRepository itemDataRepository,
                       ItemDataGlobalRepository itemDataGlobalRepository,
                       LotRepository lotRepository) {
        this.customerShipmentRepository = customerShipmentRepository;
        this.obProblemRepository = obProblemRepository;
        this.obProblemMapper = obProblemMapper;
        this.itemDataRepository = itemDataRepository;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    public AccessDTO createProblem(ProblemDTO problemDTO) {
        AccessDTO accessDTO = new AccessDTO();

        //报多货问题时将会无法知道当前商品是属于哪个订单，所以需要过滤掉
        if (!problemDTO.getProblemType().equals("MORE")) {
            //更新订单状态
            CustomerShipment customerShipment = customerShipmentRepository.getByShipmentNo(problemDTO.getShipmentCode());
            if (customerShipment != null) {
                customerShipment.setState(1100);
            } else {
                accessDTO.setCode("1");
                accessDTO.setMsg("订单错误，报问题失败");
                return accessDTO;
            }
            customerShipmentRepository.save(customerShipment);
            problemDTO.setShipmentId(customerShipment.getId());
        }
        //少货和其他问题
        ItemDataGlobal itemDataGlobal = itemDataGlobalRepository.getByItemDataNo(problemDTO.getItemCode());
        ItemData itemData = itemDataRepository.getByItemDataGlobal(itemDataGlobal.getId()).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expirationDate = null;
        try {
            expirationDate = sdf.parse(problemDTO.getExpirationDate());
        } catch (ParseException e) {
            //e.printStackTrace();
            accessDTO.setCode("1");
            accessDTO.setMsg("报问题失败");
            return accessDTO;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(problemDTO.getReportDate(), df);
        Lot lot = lotRepository.getByLotDate(expirationDate, itemData);
        problemDTO.setItemDataId(itemData.getId());
        problemDTO.setLotNo(lot.getLotNo());
        obProblemRepository.save(obProblemMapper.toEntity(problemDTO, ldt));
        accessDTO.setCode("0");
        accessDTO.setMsg("报问题成功");
        return accessDTO;
    }
}
