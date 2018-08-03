package com.mushiny.wms.outboundproblem.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.business.OBPCellBusiness;
import com.mushiny.wms.outboundproblem.business.OBPSolveBusiness;
import com.mushiny.wms.outboundproblem.crud.dto.CellStorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPCellDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveDTO;
import com.mushiny.wms.outboundproblem.crud.dto.ReliveCellDTO;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPSolveMapper;
import com.mushiny.wms.outboundproblem.domain.OBPCell;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.enums.OBPSolveState;
import com.mushiny.wms.outboundproblem.domain.enums.OBPWallState;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.query.OBPSolveQuery;
import com.mushiny.wms.outboundproblem.repository.OBPCellRepository;
import com.mushiny.wms.outboundproblem.repository.OBPSolveRepository;
import com.mushiny.wms.outboundproblem.service.OBPCellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OBPCellServiceImpl implements OBPCellService {

    private final OBPSolveRepository obpSolveRepository;
    private final OBPSolveMapper obpSolveMapper;
    private final OBPSolveBusiness obpSolveBusiness;
    private final OBPCellRepository obpCellRepository;
    private final ApplicationContext applicationContext;
    private final OBPSolveQuery obpSolveQuery;
    private final OBPCellBusiness obpCellBusiness;

    @Autowired
    public OBPCellServiceImpl(OBPSolveRepository obpSolveRepository,
                              OBPSolveMapper obpSolveMapper,
                              OBPSolveBusiness obpSolveBusiness,
                              OBPCellRepository obpCellRepository,
                              ApplicationContext applicationContext,
                              OBPSolveQuery obpSolveQuery,
                              OBPCellBusiness obpCellBusiness) {
        this.obpSolveRepository = obpSolveRepository;
        this.obpSolveMapper = obpSolveMapper;
        this.obpSolveBusiness = obpSolveBusiness;
        this.obpCellRepository = obpCellRepository;
        this.applicationContext = applicationContext;
        this.obpSolveQuery = obpSolveQuery;
        this.obpCellBusiness = obpCellBusiness;
    }


    @Override
    public OBPCellDTO getCellByWallId(String obpWallId) {
        return obpSolveBusiness.getCellByWallId(obpWallId);
    }

    @Override
    public void bindCell(String shipmentNo, String cellName) {
        checkCell(cellName);
        obpSolveBusiness.bindCell(shipmentNo,cellName);
    }

    @Override
    public List<OBPSolveDTO> getProblemsByCell(String stationId, String wallId, String cellName) {
        List<OBPSolve> solves = obpSolveRepository.getByCell(applicationContext.getCurrentWarehouse(),wallId,cellName);
        if (solves == null || solves.isEmpty())
            throw new ApiException(ExceptionEnum.EX_OBJECT_NOT_FOUND.toString());
        String shipmentNo = solves.get(0).getCustomerShipment().getShipmentNo();
        String state = OBPSolveState.unsolved.toString();
        List<OBPSolve> entities = obpSolveQuery.queryProblem(applicationContext.getCurrentWarehouse(), stationId, wallId, shipmentNo, state);
        return obpSolveMapper.toDTOList(entities);
    }

    @Override
    public void unbindCell(String shipmentNo, String obpStationId, String cellName, String solveKey) {
        obpCellBusiness.unbindCell(shipmentNo,obpStationId,cellName,solveKey);
    }

    @Override
    public List<CellStorageLocationDTO> getStoragelocationByWallName(String obpWallId,String podNo,String location) {
        return obpCellBusiness.assignLocation(obpWallId,podNo,location);
    }

    @Override
    public void relieveCell(ReliveCellDTO reliveCellDTO) {
        obpCellBusiness.relieveCell(reliveCellDTO);
    }

    //检查问题处理格是否已被绑定
    private void checkCell(String cellName) {
        OBPCell cell = obpCellRepository.getByCellName(applicationContext.getCurrentWarehouse(), cellName);
        if (cell.getState().equalsIgnoreCase(OBPWallState.occupied.toString())) {
            throw new ApiException(OutBoundProblemException.EX_CELL_HAS_BIND.getName(), cellName);
        }
    }
}
