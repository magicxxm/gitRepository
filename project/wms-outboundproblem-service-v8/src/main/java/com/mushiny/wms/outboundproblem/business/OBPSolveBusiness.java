package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.utils.ConversionUtil;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.outboundproblem.business.common.OutBoundProblemCommonBusiness;
import com.mushiny.wms.outboundproblem.business.dto.DeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.business.dto.SolveShipmentPositionDTO;
import com.mushiny.wms.outboundproblem.business.enums.*;
import com.mushiny.wms.outboundproblem.business.enums.StockUnitState;
import com.mushiny.wms.outboundproblem.business.utils.ShipmentState;
import com.mushiny.wms.outboundproblem.crud.dto.OBPCellDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPCellPositionDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPSolvePositionMapper;
import com.mushiny.wms.outboundproblem.domain.*;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.domain.enums.*;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.exception.common.BaseException;
import com.mushiny.wms.outboundproblem.query.OBPSolveQuery;
import com.mushiny.wms.outboundproblem.query.UnitLoadShipmentQuery;
import com.mushiny.wms.outboundproblem.repository.*;
import com.mushiny.wms.outboundproblem.repository.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OBPSolveBusiness {
    private final ApplicationContext applicationContext;
    private final OBPStationRepository obpStationRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final OBPSolveRepository obpSolveRepository;
    private final OBProblemRepository obProblemRepository;
    private final OBPWallRepository obpWallRepository;
    private final OBPSolveQuery obpSolveQuery;
    private final OBPCellRepository obpCellRepository;
    private final StockUnitRepository stockUnitRepository;
    private final OutBoundProblemCommonBusiness outBoundProblemCommonBusiness;
    private final StorageLocationRepository storageLocationRepository;
    private final StorageLocationTypeRepository storageLocationTypeRepository;
    private final ItemDataRepository itemDataRepository;
    private final OBPSolvePositionRepository obpSolvePositionRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final ItemDataSerialNumberRepository itemDataSerialNumberRepository;
    private final OBPShipmentSerialNoRepository obpShipmentSerialNoRepository;
    private final OBPSolvePositionMapper obpSolvePositionMapper;
    private final MoveGoodsBusiness moveGoodsBusiness;
    private final RebinUnitLoadRepository rebinUnitLoadRepository;
    private final PackingRequestRepository packingRequestRepository;
    private final BuildBusiness buildBusiness;
    private final UnitLoadShipmentQuery unitLoadShipmentQuery;
    private final CustomerShipmentRecordRepository customerShipmentRecordRepository;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
    private final LocationRepository locationRepository;
    private final Logger log= LoggerFactory.getLogger(OBPSolveBusiness.class);

    @Autowired
    public OBPSolveBusiness(ApplicationContext applicationContext,
                            OBPStationRepository obpStationRepository,
                            CustomerShipmentRepository customerShipmentRepository,
                            OBPSolveRepository obpSolveRepository,
                            OBProblemRepository obProblemRepository,
                            OBPWallRepository obpWallRepository,
                            OBPSolveQuery obpSolveQuery,
                            OBPCellRepository obpCellRepository,
                            StockUnitRepository stockUnitRepository,
                            OutBoundProblemCommonBusiness outBoundProblemCommonBusiness,
                            StorageLocationRepository storageLocationRepository,
                            StorageLocationTypeRepository storageLocationTypeRepository,
                            ItemDataRepository itemDataRepository,
                            OBPSolvePositionRepository obpSolvePositionRepository,
                            UnitLoadRepository unitLoadRepository,
                            ItemDataSerialNumberRepository itemDataSerialNumberRepository,
                            OBPShipmentSerialNoRepository obpShipmentSerialNoRepository,
                            OBPSolvePositionMapper obpSolvePositionMapper,
                            MoveGoodsBusiness moveGoodsBusiness,
                            RebinUnitLoadRepository rebinUnitLoadRepository,
                            PackingRequestRepository packingRequestRepository,
                            BuildBusiness buildBusiness,
                            UnitLoadShipmentQuery unitLoadShipmentQuery,
                            CustomerShipmentRecordRepository customerShipmentRecordRepository,
                            CustomerShipmentPositionRepository customerShipmentPositionRepository,
                            LocationRepository locationRepository) {
        this.applicationContext = applicationContext;
        this.obpStationRepository = obpStationRepository;
        this.customerShipmentRepository = customerShipmentRepository;
        this.obpSolveRepository = obpSolveRepository;
        this.obProblemRepository = obProblemRepository;
        this.obpWallRepository = obpWallRepository;
        this.obpSolveQuery = obpSolveQuery;
        this.obpCellRepository = obpCellRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.outBoundProblemCommonBusiness = outBoundProblemCommonBusiness;
        this.storageLocationRepository = storageLocationRepository;
        this.storageLocationTypeRepository = storageLocationTypeRepository;
        this.itemDataRepository = itemDataRepository;
        this.obpSolvePositionRepository = obpSolvePositionRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.itemDataSerialNumberRepository = itemDataSerialNumberRepository;
        this.obpShipmentSerialNoRepository = obpShipmentSerialNoRepository;
        this.obpSolvePositionMapper = obpSolvePositionMapper;
        this.moveGoodsBusiness = moveGoodsBusiness;
        this.rebinUnitLoadRepository = rebinUnitLoadRepository;
        this.packingRequestRepository = packingRequestRepository;
        this.buildBusiness = buildBusiness;
        this.unitLoadShipmentQuery = unitLoadShipmentQuery;
        this.customerShipmentRecordRepository = customerShipmentRecordRepository;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
        this.locationRepository = locationRepository;
    }


    //扫描订单或点搜索按钮，进入问题处理
    public List<OBPSolve> getOBPSolve(String obpStationId, String obpWallId, String shipmentNo, String state) {

        String warehouseId = applicationContext.getCurrentWarehouse();
        List<OBPSolve> solves = new ArrayList<>();
        if (state.equals(OBPSolveState.unsolved.toString()))
            solves = obpSolveQuery.queryProblem(warehouseId, obpStationId, obpWallId, shipmentNo, state);

        //在点击已处理完成时候，订单里
        if (state.equals(OBPSolveState.solved.toString())) {
            //一个订单里问题商品已经处理完成
            List<OBPSolve> list = new ArrayList<>();
            solves = obpSolveQuery.queryProblemByShipment(warehouseId, obpStationId, obpWallId, shipmentNo, state);
            list = obpSolveQuery.queryProblem(warehouseId, obpStationId, obpWallId, shipmentNo, OBPSolveState.unsolved.toString());
            List<OBPSolve> deletes = new ArrayList<>();
            if (!solves.isEmpty() && !list.isEmpty()) {
                for (OBPSolve solve : solves) {
                    for (OBPSolve li : list) {
                        if (solve.getCustomerShipment().getShipmentNo().equals(li.getCustomerShipment().getShipmentNo()))
                            deletes.add(solve);
                    }
                }
                if (!deletes.isEmpty())
                    solves.removeAll(deletes);
            }
        }
        List<OBPSolve> obpSolves = obpSolveRepository.getAllGoodsByShipmentNo(warehouseId, shipmentNo);
        List<OBPSolve> WallSolves = obpSolveRepository.getByStationAndWallAndHaveCell(warehouseId, obpStationId, obpWallId, state);
       // CustomerShipment customerShipment = customerShipmentRepository.getByShipmentNo(shipmentNo);
        //shipmentNo不为空，说明是扫描订单进入问题处理；
        if (!shipmentNo.isEmpty()) {
            CustomerShipment customerShipment=checkCustomerShipmentNo(shipmentNo);
            if (obpSolves != null && obpSolves.size() > 0) {
                for (int i = 0; i < obpSolves.size(); i++) {
                    if (!obpSolves.get(i).getObpWall().getId().equals(obpWallId))
                        throw new ApiException(OutBoundProblemException.EX_SHIPMENT_IS_SOLVING_AT_OTHER_WALL.getName(), shipmentNo);
                }
            }
            //如果没有处理过，判断该shipment是否有报问题处理
            List<OBProblem> problems = obProblemRepository.getByShipmentNo(warehouseId, shipmentNo);
            //判断订单之前是否已经处理过
            if (solves == null || solves.size() == 0) {
                OBPSolve solve = new OBPSolve();
                solve.setCustomerShipment(customerShipmentRepository.getByShipmentNo(shipmentNo)); //扫描的是容器，需要将shipmentNo传到前台
                List<OBPSolve> solvedProblem = obpSolveRepository.getProblemGoodsByShipment(applicationContext.getCurrentWarehouse(), shipmentNo);
                //如果有报问题处理，将问题商品移到问题解决表中,同时把该shipment的其它正常商品也添加到问题解决表中
                if (problems != null && problems.size() > 0) {
                    if (solvedProblem == null || solvedProblem.size() == 0) {
                        //将该shipment的问题商品移到问题解决表中
                        addProblemToOBPSolve(obpStationId, obpWallId, shipmentNo);
                        //将该shipment的所有商品添加到问题解决表中
                        addShipmentToOBPSolve(obpStationId, obpWallId, shipmentNo, null);

                        //返回一个“订单还未绑定问题处理格”的状态key给前端，提示需走绑定问题处理格流程（这里的状态值是一个特殊情况，数据库不会存储state该数据，只为前端流程跳转设计的）
                        solve.setState(OutBoundProblemException.EX_SCANNING_SHIPMENT_NOT_BIND_CELL.getName());
                    } else {
                        if (solvedProblem.get(0).getObpCell() == null) {
                            solve.setState(OutBoundProblemException.EX_SCANNING_SHIPMENT_NOT_BIND_CELL.getName());
                            //已处理好的订单再转成问题订单650
                            if (customerShipment.getState() < ShipmentState.SORTED && customerShipment.getState() > ShipmentState.REBINBUFFERED) {
                                solve.setState(OutBoundProblemException.EX_SCANNING_SHIPMENT_IS_NORMAL.getName());
                            }
                            if (solvedProblem.get(0).getState().equals(OBPSolveState.solved.toString()) && customerShipment.getState() == ShipmentState.PROBLEM) {
                                deleteObpSolve(shipmentNo, obpStationId, obpWallId);
                            }
                        } else {  //在已处理页面搜索未处理订单的异常判断
                            if (state.equals(OBPSolveState.unsolved.toString()))
                                throw new ApiException(OutBoundProblemException.EX_THIS_SHIPMENT_HAS_SOLVED.getName());
                            else
                                throw new ApiException(OutBoundProblemException.EX_THIS_SHIPMENT_SHOULD_SOLVED.getName());
                        }
                    }
                } else {
                    if (customerShipment != null && customerShipment.getState() == ShipmentState.CANCELED) {
                        customerDeleteShipment(obpStationId, obpWallId, shipmentNo);
                        solve.setState(SolveResoult.DELETE_ORDER_CUSTOMER.toString());
                    } else {
                        if (customerShipment != null && customerShipment.getState() < ShipmentState.SORTED && customerShipment.getState() > ShipmentState.REBINBUFFERED && obpSolves.size() == 0) {
                            solve.setState(OutBoundProblemException.EX_SCANNING_SHIPMENT_IS_NORMAL.getName());
                        } else if (obpSolves.size() > 0 && solvedProblem.size() == 0 && obpSolves.get(0).getObpCell() == null) {
                            solve.setState(OutBoundProblemException.EX_SCANNING_SHIPMENT_NOT_BIND_CELL.getName());
                        } else if (obpSolves.size() > 0 && solvedProblem.size() == 0 && obpSolves.get(0).getObpCell() != null) {
                            solve = obpSolves.get(0);
                        }
                    }
                }
                solves.add(solve);
            }
        } else {
            //当订单已放到CELL格中，但还没确定问题类型时，也需要显示到待处理中
            if (!WallSolves.isEmpty()) {
                for (OBPSolve solve : WallSolves) {
                    boolean flag = false;
                    if (!solves.isEmpty()) {
                        for (OBPSolve os : solves) {
                            if (os.getCustomerShipment().getShipmentNo().equals(solve.getCustomerShipment().getShipmentNo())) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (!flag) {
                        solves.add(solve);
                    }
                }
            }
        }

        //去除同一订单中同类型的问题
        List<OBPSolve> solveList = new ArrayList<>();
        if (!shipmentNo.isEmpty() && !solves.isEmpty() && solves.get(0).getState() != null) {
            solveList = solves;
        } else {
            if (!solves.isEmpty()) {
                solveList.add(solves.get(0));
                for (OBPSolve solve : solves) {
                    boolean exist = false;
                    for (OBPSolve obpSolve : solveList) {
                        if (solve.getObproblem() != null && obpSolve.getObproblem() != null) {
                            if (solve.getCustomerShipment().getShipmentNo().equals(obpSolve.getCustomerShipment().getShipmentNo())
                                    && solve.getObproblem().getProblemType().equals(obpSolve.getObproblem().getProblemType())) {
                                exist = true;
                                break;
                            }
                        }
                    }
                    if (!exist) {
                        if (solve.getObproblem() == null) {
                            if (solve.getCustomerShipment() != null && !solve.getCustomerShipment().getShipmentNo().equals(solveList.get(0).getCustomerShipment().getShipmentNo()))
                                solveList.add(solve);
                        } else
                            solveList.add(solve);

                    }
                }
            }
        }
        return solveList;

    }

    //绑定问题处理货格
    public List<OBPSolve> bindCell(String shipmentNo, String cellName) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        List<OBPSolve> solves = obpSolveRepository.getByShipmentNo(warehouseId, shipmentNo);
        if (solves == null || solves.isEmpty()) {
            throw new ApiException(ExceptionEnum.EX_OBJECT_NOT_FOUND.toString());
        }
        for (OBPSolve entity : solves) {
            entity.setObpCell(obpCellRepository.getByCellName(warehouseId, cellName));
            obpSolveRepository.save(entity);
        }

        OBPCell cell = obpCellRepository.getByCellName(applicationContext.getCurrentWarehouse(), cellName);
        cell.setState(OBPWallState.occupied.toString());
        obpCellRepository.save(cell);

        return solves;
    }

    //点击订单时查询商品明细
    public List<SolveShipmentPositionDTO> getSolveShipmentPositionByShipmentNo(String shipmentNo) {
        List<SolveShipmentPositionDTO> shipmentPositiones = new ArrayList<>();

        //查询出该订单下的所有商品
        List<OBPSolve> solves = obpSolveRepository.getAllGoodsByShipmentNo(applicationContext.getCurrentWarehouse(), shipmentNo);

        //查询出该订单下所有的有问题的商品
        List<OBPSolve> problemSolves = obpSolveRepository.getProblemGoodsByShipment(applicationContext.getCurrentWarehouse(), shipmentNo);

        if (solves == null || solves.isEmpty()) {
            throw new ApiException(ExceptionEnum.EX_OBJECT_NOT_FOUND.toString());
        }
        String shipmentId = solves.get(0).getCustomerShipment().getId();

        for (OBPSolve entity : solves) {
            SolveShipmentPositionDTO shipmentPosition = new SolveShipmentPositionDTO();
            //当shipmentPosition为有问题的商品时，shipmentPosition2则用来表示正常的商品
            SolveShipmentPositionDTO shipmentPosition2 = new SolveShipmentPositionDTO();
            String itemNo = entity.getItemData().getItemNo();
            shipmentPosition.setItemNo(itemNo);
            shipmentPosition.setItemName(entity.getItemData().getName());
            shipmentPosition.setAmount(entity.getAmountShipment());
            shipmentPosition.setAmountScaned(entity.getAmountScaned());
            shipmentPosition.setAmountDelete(entity.getAmount());
            shipmentPosition.setSolveId(entity.getId());
            shipmentPosition.setSkuNo(entity.getItemData().getSkuNo());
            shipmentPosition.setItemDataId(entity.getItemData().getId());
            shipmentPosition.setShipmentId(shipmentId);
            //是否需要输入有效期及有效期类型
            shipmentPosition.setLotMandatory(entity.getItemData().isLotMandatory());

            shipmentPosition.setLotType(entity.getItemData().getLotType());
            shipmentPosition.setLotUnit(entity.getItemData().getLotUnit());
            //是否要输入SN
            shipmentPosition.setSerialRecordType(entity.getItemData().getSerialRecordType());
            //若需输入SN，则设置SN；itemDate与serialNo是一对多的关系
            setSeriaNo(entity, shipmentPosition);

            shipmentPosition.setScaned(entity.getScaned());
            shipmentPosition.setWidth(entity.getItemData().getWidth());
            shipmentPosition.setHeight(entity.getItemData().getHeight());
            shipmentPosition.setDepth(entity.getItemData().getDepth());
            shipmentPosition.setWeight(entity.getItemData().getWeight());
            //判断该订单下是否存在问题商品，若存在就设置问题类型和数量
            OBPSolve solv = null;
            //问题商品扫描数
            BigDecimal amountScanedProblem = BigDecimal.ZERO;
            if (problemSolves != null && problemSolves.size() > 0) {
                solv = problemSolves.get(0);
                //同类商品的问题数
                for (OBPSolve solve : problemSolves) {
                    shipmentPosition = new SolveShipmentPositionDTO();
                    shipmentPosition.setItemNo(itemNo);
                    shipmentPosition.setItemName(entity.getItemData().getName());
                    shipmentPosition.setAmount(entity.getAmountShipment());
                    shipmentPosition.setAmountScaned(entity.getAmountScaned());
                    shipmentPosition.setAmountDelete(entity.getAmount());
                    shipmentPosition.setSolveId(entity.getId());
                    shipmentPosition.setSkuNo(entity.getItemData().getSkuNo());
                    shipmentPosition.setItemDataId(entity.getItemData().getId());
                    shipmentPosition.setShipmentId(shipmentId);
                    //是否需要输入有效期及有效期类型
                    shipmentPosition.setLotMandatory(entity.getItemData().isLotMandatory());

                    shipmentPosition.setLotType(entity.getItemData().getLotType());
                    shipmentPosition.setLotUnit(entity.getItemData().getLotUnit());
                    //是否要输入SN
                    shipmentPosition.setSerialRecordType(entity.getItemData().getSerialRecordType());
                    //若需输入SN，则设置SN；itemDate与serialNo是一对多的关系
                    setSeriaNo(entity, shipmentPosition);

                    shipmentPosition.setScaned(entity.getScaned());
                    shipmentPosition.setWidth(entity.getItemData().getWidth());
                    shipmentPosition.setHeight(entity.getItemData().getHeight());
                    shipmentPosition.setDepth(entity.getItemData().getDepth());
                    shipmentPosition.setWeight(entity.getItemData().getWeight());
                    shipmentPosition.setSkuNo(entity.getItemData().getSkuNo());

                    if (solve.getItemData().getItemNo().equals(itemNo)) {
                        shipmentPosition = new SolveShipmentPositionDTO();
                        shipmentPosition.setItemNo(itemNo);
                        shipmentPosition.setItemName(solve.getItemData().getName());
                        shipmentPosition.setSolveId(solve.getId());

                        //是否需要输入有效期及有效期类型
                        shipmentPosition.setLotMandatory(solve.getItemData().isLotMandatory());

                        shipmentPosition.setLotType(solve.getItemData().getLotType());
                        shipmentPosition.setLotUnit(solve.getItemData().getLotUnit());
                        //是否要输入SN
                        shipmentPosition.setSerialRecordType(solve.getItemData().getSerialRecordType());
                        //若需输入SN，则设置SN；itemDate与serialNo是一对多的关系
                        setSeriaNo(solve, shipmentPosition);

                        shipmentPosition.setScaned(solve.getScaned());
                        shipmentPosition.setWidth(solve.getItemData().getWidth());
                        shipmentPosition.setHeight(solve.getItemData().getHeight());
                        shipmentPosition.setDepth(solve.getItemData().getDepth());
                        shipmentPosition.setWeight(solve.getItemData().getWeight());
                        shipmentPosition.setSkuNo(solve.getItemData().getSkuNo());
                        shipmentPosition.setItemDataId(solve.getItemData().getId());
                        shipmentPosition.setShipmentId(shipmentId);

                        shipmentPosition.setProblemType(solve.getObproblem().getProblemType());
                        if (solve.getObproblem().getProblemType().equals(SolveResoult.PRINT_SKU_REPAIR.toString()))
                            shipmentPosition.setProblemType(ProblemType.UNABLE_SCAN_SKU.toString());
                        shipmentPosition.setAmountProblem(solve.getAmountProblem());
                        shipmentPosition.setAmountDelete(solv.getAmount());
                        shipmentPosition.setAmount(solve.getAmountProblem());
                        shipmentPosition.setAmountScaned(solve.getAmountScanedProblem());
                        shipmentPosition.setScaned(solve.getScaned());
                        //设置该商品的库存数，前端据此判断是否有库存
                        //需要判断当前登录的物理工作站所在的section
                        shipmentPosition.setStockUnitAmount(stockUnitRepository.sumByItemData(entity.getItemData()));

                        OBPSolvePosition solvePosition = obpSolvePositionRepository.getByShipmentNoAndItemNo(
                                applicationContext.getCurrentWarehouse(), solve.getId(), shipmentNo, solve.getItemData().getItemNo());
                        if (solvePosition != null && solvePosition.getSolveKey() != null) {
                            shipmentPosition.setSolveKey(solvePosition.getSolveKey());
                            shipmentPosition.setLocation(solvePosition.getLocation());
                            shipmentPosition.setDescription(solvePosition.getDescription());
                        }

                        //查询出该订单下所有的有问题的商品
                        amountScanedProblem = amountScanedProblem.add(solve.getAmountScanedProblem());
                    }
                    if (shipmentPosition.getProblemType() != null && shipmentPosition.getAmountProblem().compareTo(BigDecimal.ZERO) > 0)
                        shipmentPositiones.add(shipmentPosition);
                }
            }

            //如果问题类型为商品丢失或是商品残损或是条码无法扫描或是序列号无法扫描，将该类商品中的正品分开显示
            if (solv != null) {
                if (solv.getObproblem().getProblemType().equals(ProblemType.LOSE.toString()) ||
                        solv.getObproblem().getProblemType().equals(ProblemType.DAMAGED.toString()) ||
                        solv.getObproblem().getProblemType().equals(ProblemType.UNABLE_SCAN_SKU.toString()) ||
                        solv.getObproblem().getProblemType().equals(SolveResoult.PRINT_SKU_REPAIR.toString()) ||
                        solv.getObproblem().getProblemType().equals(ProblemType.UNABLE_SCAN_SN.toString())) {
                    shipmentPosition2.setItemNo(shipmentPosition.getItemNo());
                    shipmentPosition2.setItemName(shipmentPosition.getItemName());
                    //设置正常商品栏位，该商品的数量（订单下该类商品的总数-该类商品的问题总数）
                    BigDecimal amountProblem = BigDecimal.ZERO;
                    for (OBPSolve os : problemSolves) {
                        if (os.getItemData().getItemNo().equals(itemNo)) {
                            amountProblem = os.getAmountProblem().add(amountProblem);
                        }
                    }
                    shipmentPosition2.setAmount(entity.getAmountShipment().subtract(amountProblem));

                    shipmentPosition2.setAmountScaned(entity.getAmountScaned().subtract(amountScanedProblem));
                    shipmentPosition2.setSolveId(entity.getId());
                    //是否需要输入有效期及有效期类型
                    shipmentPosition2.setLotMandatory(entity.getItemData().isLotMandatory());

                    shipmentPosition2.setLotType(entity.getItemData().getLotType());
                    shipmentPosition2.setLotUnit(entity.getItemData().getLotUnit());
                    //是否要输入SN
                    shipmentPosition2.setSerialRecordType(entity.getItemData().getSerialRecordType());
                    //若需输入SN，则设置SN；itemDate与serialNo是一对多的关系
                    setSeriaNo(entity, shipmentPosition2);

                    shipmentPosition2.setScaned(entity.getScaned());
                    shipmentPosition2.setWidth(entity.getItemData().getWidth());
                    shipmentPosition2.setHeight(entity.getItemData().getHeight());
                    shipmentPosition2.setDepth(entity.getItemData().getDepth());
                    shipmentPosition2.setWeight(entity.getItemData().getWeight());
                    shipmentPosition2.setSkuNo(entity.getItemData().getSkuNo());
                    shipmentPosition2.setProblemType(null);
                    shipmentPosition2.setAmountProblem(BigDecimal.ZERO);
                    shipmentPosition2.setAmountDelete(entity.getAmount());
                    shipmentPosition2.setItemDataId(entity.getItemData().getId());
                    shipmentPosition2.setShipmentId(shipmentId);

                    if (shipmentPosition2.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                        shipmentPositiones.add(shipmentPosition2);
                    }
                }
            }
            //说明是正常订单
            if (!solves.isEmpty() && problemSolves.isEmpty()) {
                shipmentPositiones.add(shipmentPosition);
            }
        }
        return shipmentPositiones;
    }

    //扫描商品
    public SolveShipmentPositionDTO scanItemByShipmentAndItem(String toCellName, String shipmentNo, String itemNo) {
        SolveShipmentPositionDTO shipmentPosition = new SolveShipmentPositionDTO();
        //  shipmentPosition.setSerialRecordType(SerialNoRecordType.ALWAYS_RECORD.toString());
        List<ItemData> itemDatas = itemDataRepository.getByItemNoSkuNo(applicationContext.getCurrentWarehouse(), itemNo);
        //同一种SKU NO有多种商品
        if (itemDatas.size() > 1) {
            throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_MORE_THAN_ONE.getName(), itemNo);
        }
        if(itemDatas.size()==0){
            throw new ApiException(OutBoundProblemException.EX_IT_SKU_NOT_FOUND.getName(), itemNo);
        }
        OBPSolve  entity = obpSolveRepository.getGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), shipmentNo, itemDatas.get(0).getItemNo());
        List<OBPSolve> entityProblems = obpSolveRepository.getProblemGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), shipmentNo, itemDatas.get(0).getItemNo());
        if (entity == null) {
            throw new ApiException(OutBoundProblemException.EX_SKU_NOT_MATCH.getName(), itemNo);
        }
        OBPSolve entityProblem = null;
        if (entityProblems != null && !entityProblems.isEmpty()) {
            for (OBPSolve solve : entityProblems) {
                if (solve.getObproblem().getProblemType().equals(ProblemType.LOSE.toString())) {
                    continue;
                }
                if (solve.getScaned().equals(OBPSolveState.unscaned.toString())) {
                    entityProblem = solve;
                    break;
                }
            }
        }

        //此类商品的问题总数
        BigDecimal amountProblemItem = BigDecimal.ZERO;
        if (entityProblems != null && !entityProblems.isEmpty()) {
            for (OBPSolve solve : entityProblems) {
                amountProblemItem = amountProblemItem.add(solve.getAmountProblem());
            }
        }

        //判断扫描商品时，是否需要扫描SN码；如果不需要,则扫描次数直接加1;并生成相关记录（该环节扫描商品，不需要判断有效期）
        if (entity.getItemData().getSerialRecordType().equalsIgnoreCase(SerialNoRecordType.NO_RECORD.toString()) ||
                entity.getItemData().getSerialRecordType().isEmpty()) {
            shipmentPosition.setSerialRecordType(SerialNoRecordType.NO_RECORD.toString());
            shipmentPosition.setItemName(entity.getItemData().getName());
            //同类商品，同时含有正常和问题商品，每扫描一次，已扫描数递增
            if (entityProblem != null) {
                entity.setAmountScaned(entity.getAmountScaned().add(BigDecimal.ONE));

                if (entity.getAmountScaned().compareTo(entity.getAmountShipment()) > 0) {
                    entity.setAmountScaned(entity.getAmountShipment());
                    throw new ApiException(OutBoundProblemException.EX_THIS_ITEM_HAS_SCANED.getName());
                }
                //订单内某种商品数-某种商品问题总数=正常商品数
                BigDecimal num = entity.getAmountShipment().subtract(amountProblemItem);
                //扫描数>正常数，问题数已扫描数加1
                if (entity.getAmountScaned().compareTo(num) > 0) {
                    entityProblem.setAmountScanedProblem(entityProblem.getAmountScanedProblem().add(BigDecimal.ONE));
                }

                boolean result = updateScaned(entity, entityProblem);

                if (result) {
                    addRecord(shipmentNo, toCellName);
                } else {
                    if (entity.getAmountShipment().equals(entityProblem.getAmountProblem())
                            && entityProblem.getObproblem().getProblemType().equals(ProblemType.LOSE.toString()))
                        throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_HAS_LOSED.getName(), shipmentNo);
                }
                if (entityProblem.getAmountScanedProblem().compareTo(entityProblem.getAmountProblem()) == 0 && entityProblem.getObproblem().getProblemType().equals(ProblemType.UNABLE_SCAN_SKU.toString())) {
//                    OBProblem obProblem = obProblemRepository.getScanskuByItem(applicationContext.getCurrentWarehouse(), entityProblem.getCustomerShipment().getId(), entityProblem.getItemData().getId());
//                    obProblem.setAmount(BigDecimal.ZERO);
//                    obProblemRepository.save(obProblem);
                    entityProblem.setState(OBPSolveState.solved.toString());
                    obpSolveRepository.save(entityProblem);
                }
            }
            //扫描正常商品,或者只有一个问题且问题类型是丢失
            if ((entityProblem == null && entityProblems.isEmpty()) ||
                    (entityProblem == null && entityProblems.size() == 1 &&
                            entityProblems.get(0).getObproblem().getProblemType().equals(ProblemType.LOSE.toString()))) {
                //订单里这类商品都是丢失，丢失不能扫描
                OBPSolve obpSolve = obpSolveRepository.getGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), shipmentNo, itemDatas.get(0).getItemNo());
                if (!entityProblems.isEmpty() && entityProblems.get(0).getAmountProblem().compareTo(obpSolve.getAmountShipment()) == 0) {
                    throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_HAS_LOSED.getName());
                }
                //正常商品扫描数直接加1
                if (entityProblems.isEmpty()) {
                    entity.setAmountScaned(entity.getAmountScaned().add(BigDecimal.ONE));
                } else {
                    if (entityProblems.get(0).getAmountProblem().compareTo(entity.getAmountShipment().subtract(entity.getAmountScaned())) < 0) {
                        entity.setAmountScaned(entity.getAmountScaned().add(BigDecimal.ONE));
                    } else {  //错误的扫描了丢失的商品
                        throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_HAS_LOSED.getName());
                    }
                }
                if (entity.getAmountScaned().compareTo(entity.getAmountShipment()) > 0) {
                    entity.setAmountScaned(entity.getAmountShipment());
                    throw new ApiException(OutBoundProblemException.EX_THIS_ITEM_HAS_SCANED.getName()); //正常商品扫描完成后，如果错误的再扫描
                }
                boolean result = updateScaned(entity, null);

                if (result) {
                    addRecord(shipmentNo, toCellName);
                }
            }
        }
        //扫描商品时，需要扫描SN码
        else if (entity.getItemData().getSerialRecordType().equalsIgnoreCase(SerialNoRecordType.ALWAYS_RECORD.toString())) {
            shipmentPosition.setSerialRecordType(SerialNoRecordType.ALWAYS_RECORD.toString());
            //显示商品名称
            shipmentPosition.setItemName(entity.getItemData().getName());
            shipmentPosition.setSolveId(entity.getId());
            shipmentPosition.setItemNo(entity.getItemData().getItemNo());
            shipmentPosition.setLotType(entity.getItemData().getLotType());
            shipmentPosition.setLotMandatory(entity.getItemData().isLotMandatory());
            shipmentPosition.setAmount(entity.getAmountShipment());
            shipmentPosition.setLotUnit(entity.getItemData().getLotUnit());
            if (entityProblem != null && entityProblem.getObproblem().getProblemType().equals(ProblemType.UNABLE_SCAN_SN.toString())) {
                BigDecimal num = entity.getAmountShipment().subtract(amountProblemItem);
                //序列号正常的商品已扫描完，提示将问题商品转为待调查状态
                if (entity.getAmountScaned().compareTo(num) == 0) {
                    shipmentPosition.setProblemType(entityProblem.getObproblem().getProblemType());
                    shipmentPosition.setSerialRecordType(null);
                    shipmentPosition.setSolveId(entityProblem.getId());
                }
            }else if(entityProblem != null && !entityProblem.getObproblem().getProblemType().equals(ProblemType.UNABLE_SCAN_SN.toString())){
                BigDecimal num = entity.getAmountShipment().subtract(amountProblemItem);
                if(entity.getAmountScaned().compareTo(num) == 0){
                    shipmentPosition.setSolveId(entityProblem.getId());
                }
            }
            //丢失的不能扫描，获得丢失的问题数
            BigDecimal loseAmount = BigDecimal.ZERO;
            for (OBPSolve solv : entityProblems) {
                if (solv.getObproblem().getProblemType().equals(ProblemType.LOSE.toString())) {
                    loseAmount = loseAmount.add(solv.getAmountProblem());
                }
            }
            //有序列号问题商品扫描完成后不再提示扫描序列号
            if (entity.getAmountScaned().compareTo(entity.getAmountShipment().subtract(loseAmount)) == 0) {
                throw new ApiException(OutBoundProblemException.EX_THIS_ITEM_HAS_SCANED.getName());
            }
        }
        return shipmentPosition;
    }

    //shipment下所有商品扫描完，将商品移到cell格里，同时生成库存记录
    @SuppressWarnings("Duplicates")
    public void addRecord(String shipmentNo, String toCellName) {
        //判断shipment下所有商品是否已扫描完
        List<OBPSolve> solves = obpSolveRepository.getGoodsByShipment(applicationContext.getCurrentWarehouse(), shipmentNo);
        CustomerShipment customerShipment = customerShipmentRepository.getByShipmentNo(shipmentNo);
        String scanState = OBPSolveState.scaned.toString();
        for (OBPSolve solve : solves) {
            if (!solve.getScaned().equals(scanState)) {
                scanState = solve.getScaned();
                break;
            }
        }
        //如果所有商品扫描完，将容器中的商品移动obpcell中，添加库存记录
        if (scanState.equals(OBPSolveState.scaned.toString())) {
            //检测storagelocation中有没有当前cell，如果没有则根据cell创建一个新storagelocation
            StorageLocation toStorageLocation = storageLocationRepository.getAllStoragetypeByName(
                    applicationContext.getCurrentWarehouse(), toCellName);
            OBPCell obpCell = obpCellRepository.getByCellName(applicationContext.getCurrentWarehouse(), toCellName);
            if (toStorageLocation == null) {
                toStorageLocation = new StorageLocation();
                toStorageLocation.setName(toCellName);
                toStorageLocation.setxPos(obpCell.getxPos());
                toStorageLocation.setyPos(obpCell.getyPos());
                toStorageLocation.setzPos(obpCell.getzPos());
                toStorageLocation.setOrderIndex(storageLocationRepository.getMaxOrderIndex() + 1);
                //  toStorageLocation.setStorageLocationType(storageLocationTypeRepository.getInventoryContainer());
                toStorageLocation.setWarehouseId(applicationContext.getCurrentWarehouse());
                toStorageLocation= storageLocationRepository.save(toStorageLocation);
            }
            UnitLoad toUnitLoad = unitLoadRepository.movingGetByStorageLocation(toStorageLocation);
            if (toUnitLoad == null) {
                toUnitLoad = buildBusiness.buildUnitLoad(toStorageLocation);
            }
            UnitLoad unitLoad = unitLoadRepository.getByShipmentNo(shipmentNo);
            if (unitLoad != null) {
                //更新UNITLOAD_SHIPMENTNO表
                unitLoadShipmentQuery.updateUnitloadByShipment(toUnitLoad.getId(), customerShipment.getId());
            } else {
//                unitLoadShipmentQuery.insertUnitloadShipment(toUnitLoad.getId(), customerShipment.getId());
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName());
            }
            StorageLocation fromStorageLocation = unitLoad.getStorageLocation();
            if (fromStorageLocation == null)
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName());

            for (OBPSolve solve : solves) {
                List<OBProblem> problems = obProblemRepository.getByShipmentAndItemId(applicationContext.getCurrentWarehouse(), solve.getCustomerShipment().getId(), solve.getItemData().getId());
                BigDecimal problemAmount = BigDecimal.ZERO;
                for (OBProblem obProblem : problems) {
                    if (obProblem.getContainer() != null) {
                        StorageLocation storageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), obProblem.getContainer());
                        //如果商品已经放入残品车牌或者待调查车牌，则不需要更新CONTAINER字段
                        if (storageLocation != null && storageLocation.getStorageLocationType() != null && !storageLocation.getStorageLocationType().getInventoryState().equals("INVENTORY")) {
                            problemAmount = problemAmount.add(obProblem.getAmount());
                        } else {
                            obProblem.setContainer(obpCell.getName());
                            obProblemRepository.save(obProblem);
                        }
                    }
                    if (obProblem.getProblemType().equals(ProblemType.LOSE.toString())) {
                        problemAmount = problemAmount.add(obProblem.getAmount());
                    }
                }
                // List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoadAndItemData(unitLoad,solve.getItemData());
                moveGoodsBusiness.movingGoods(unitLoad, toUnitLoad, toStorageLocation, solve.getItemData(), solve.getAmountShipment().subtract(problemAmount),"moveToCell",null);
            }
        }
    }

    public CustomerShipment checkCustomerShipmentNo(String shipmentNo) {
        CustomerShipment customerShipment = customerShipmentRepository.getByShipmentNo(shipmentNo);
        if (customerShipment == null) {
            throw new ApiException(OutBoundProblemException.EX_SCANNING_SHIPMENT_NOT_FOUND.getName(), shipmentNo);
        }
        log.info("订单的状态是："+customerShipment.getState());
        if(customerShipment.getState() == ShipmentState.SORTED || customerShipment.getState() == ShipmentState.FINISHED
                || customerShipment.getState() == ShipmentState.POSTPROCESSED || customerShipment.getState() < ShipmentState.REBINED) {
            throw new ApiException(OutBoundProblemException.EX_SHIPMENT_CAN_NOT_DELETE.getName(), shipmentNo);
        }
        return customerShipment;
    }

    public void checkItemNo(String shipmentNo, String itemNo) {
        OBPSolve solve = obpSolveRepository.getGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), shipmentNo, itemNo);
        if (solve == null) {
            throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_NOT_FOUND.getName(), shipmentNo);
        }
    }

    public void checkSerialNo(String serialNo, String shipmentNo) {
        // SN为全球唯一
        OBPShipmentSerialNo shipmentSerialNo = obpShipmentSerialNoRepository.getSerialNo(
                applicationContext.getCurrentWarehouse(), serialNo, shipmentNo);
        if (shipmentSerialNo == null) {
            throw new ApiException(OutBoundProblemException.EX_SN_NOT_FOUND.getName(), serialNo);
        } else if (shipmentSerialNo.getScaned()) {
            throw new ApiException(OutBoundProblemException.EX_SN_ERROR.getName(), serialNo);
        }
    }

    //将所扫描的shipment下的问题商品添加到问题处理表中
    public void addProblemToOBPSolve(String obpStationId, String obpWallId, String shipmentNo) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        List<OBProblem> problems = obProblemRepository.getProblemByShipmentNo(warehouseId, shipmentNo);
        if (problems != null) {
            for (OBProblem obProblem : problems) {
                OBPSolve solve = obpSolveRepository.getByProblemAndShipmentAndStation(warehouseId, obProblem.getId(), shipmentNo, obpStationId);
                List<OBPSolve> obpSolves = obpSolveRepository.getGoodsByShipment(applicationContext.getCurrentWarehouse(), shipmentNo);
                if (solve == null) {
                    OBPSolve entity = new OBPSolve();
                    entity.setState(OBPSolveState.unsolved.toString());
                    entity.setAmount(BigDecimal.ZERO);
                    entity.setAmountProblem(obProblem.getAmount());
                    entity.setAmountShipment(BigDecimal.ZERO);
                    entity.setAmountScaned(BigDecimal.ZERO);
                    entity.setItemData(itemDataRepository.retrieve(obProblem.getItemDataId()));
                    entity.setObproblem(obProblem);
                    entity.setObpStation(obpStationRepository.retrieve(obpStationId));
                    entity.setObpWall(obpWallRepository.retrieve(obpWallId));
                    entity.setScaned(OBPSolveState.unscaned.toString());
                    entity.setCustomerShipment(customerShipmentRepository.retrieve(obProblem.getShipmentId()));
                    entity.setSolveBy(SecurityUtils.getCurrentUsername());
                    entity.setSolveDate(LocalDateTime.now());
                    entity.setClientId(obProblem.getClientId());
                    entity.setWarehouseId(obProblem.getWarehouseId());
                    //在点击商品丢失和条码无法扫描，添加cell
                    if (!obpSolves.isEmpty())
                        entity.setObpCell(obpSolves.get(0).getObpCell());

                    entity = obpSolveRepository.save(entity);

                    //如果问题类型为商品丢失，需默认在OBPSolvePosition表中添加一条相应的确认丢失的记录(前台需要根据solveKey来显示生成HotPick的图标)
                    if (obProblem.getProblemType().equals(ProblemType.LOSE.toString())) {
                        OBPSolvePosition solvePosition = obpSolvePositionRepository.getSolvePositionByShipmentNoAndSolve(
                                applicationContext.getCurrentWarehouse(), entity.getCustomerShipment().getShipmentNo(), entity.getId());
                        if (solvePosition == null) {
                            OBPSolvePositionDTO dto = new OBPSolvePositionDTO();
                            dto.setSolveId(entity.getId());
                            dto.setSolveKey(SolveResoult.GOODS_LOSS.toString());
                            dto.setItemNo(entity.getItemData().getItemNo());
                            dto.setState(OBPSolveState.unsolved.toString());
                            dto.setShipmentNo(entity.getCustomerShipment().getShipmentNo());
                            obpSolvePositionRepository.save(obpSolvePositionMapper.toEntity(dto));
                        }
                    }
                }
            }
        }
    }

    //将所扫描的shipment下的所有商品添加到问题处理表中
    public void addShipmentToOBPSolve(String obpStationId, String obpWallId, String shipmentNo, String solveKey) {
        List<java.lang.Object[]> lists = obpSolveQuery.queryShipment(shipmentNo);
        if (lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                OBPSolve entity = new OBPSolve();
                entity.setState(OBPSolveState.unsolved.toString());
                entity.setAmount(BigDecimal.ZERO);
                entity.setAmountProblem(BigDecimal.ZERO);
                String str = String.valueOf(lists.get(i)[2]);
                BigDecimal value = new BigDecimal(str);
                entity.setAmountShipment(value);
                //在强制删单时会有solveKey
                if (solveKey != null) {
                    OBProblem obProblem = obProblemRepository.getByShipmentItemLose(applicationContext.getCurrentWarehouse(), shipmentNo, String.valueOf(lists.get(i)[1]));
                    if (obProblem != null) {
                        //订单商品数-丢失的问题数
                        entity.setAmountShipment(value.subtract(obProblem.getAmount()));
                    }
                }
                entity.setAmountScaned(BigDecimal.ZERO);
                entity.setItemData(itemDataRepository.retrieve(String.valueOf(lists.get(i)[1])));
                if (obpStationId != null)
                    entity.setObpStation(obpStationRepository.retrieve(obpStationId));
                if (obpWallId != null)
                    entity.setObpWall(obpWallRepository.retrieve(obpWallId));
                entity.setObpCell(null);
                entity.setScaned(OBPSolveState.unscaned.toString());
                entity.setCustomerShipment(customerShipmentRepository.retrieve(String.valueOf(lists.get(i)[0])));
                entity.setSolveBy(SecurityUtils.getCurrentUsername());
                entity.setSolveDate(DateTimeUtil.getNowDateTime().toLocalDateTime());
                entity.setClientId(applicationContext.getCurrentClient());
                entity.setWarehouseId(applicationContext.getCurrentWarehouse());
                obpSolveRepository.save(entity);

                //将该shipment中含有SN号的商品添加到序列号对应表中，以供扫SN时记录已扫描情况
                if (entity.getItemData().getSerialRecordType().equals(SerialNoRecordType.ALWAYS_RECORD.toString())) {
                    // List<OBPShipmentSerialNo> serialNos=obpShipmentSerialNoRepository.getAllByShipment(applicationContext.getCurrentWarehouse(),entity.getCustomerShipment().getId());
                    //查询stockUnit表的序列号
                    UnitLoad unitLoad = unitLoadRepository.getByShipmentNo(entity.getCustomerShipment().getShipmentNo());
                    if (unitLoad == null)
                        throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.toString());

                    List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoadAndItemData(unitLoad, entity.getItemData());
                    for (StockUnit stockUnit : stockUnits) {
                        if(stockUnit.getSerialNo()!=null) {
                            OBPShipmentSerialNo shipmentSerialNo = new OBPShipmentSerialNo();
                            shipmentSerialNo.setShipmentId(entity.getCustomerShipment().getId());
                            shipmentSerialNo.setItemDataId(entity.getItemData().getId());
                            shipmentSerialNo.setSerialNo(stockUnit.getSerialNo());
                            shipmentSerialNo.setScaned(false);
                            //shipmentSerialNo.setState(null); //序列号状态(残损，待调查，正品)
                            shipmentSerialNo.setClientId(applicationContext.getCurrentClient());
                            shipmentSerialNo.setWarehouseId(applicationContext.getCurrentWarehouse());
                            obpShipmentSerialNoRepository.save(shipmentSerialNo);
                        }
                    }
                }
            }
        }
    }

    public OBPCellDTO getCellByWallId(String obpWallId) {
        OBPCellDTO cellDTO = new OBPCellDTO();
        if (obpWallId != null) {
            List<OBPCell> cells = obpCellRepository.getByWallId(applicationContext.getCurrentWarehouse(), obpWallId);
            if (cells.isEmpty()) {
                throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_NOT_FOUND.getName(), obpWallId);
            }
            OBPWall obpWall = obpWallRepository.retrieve(obpWallId);
            List<OBPCellPositionDTO> cellPositionDTOS = new ArrayList<>();
            int columns = obpWall.getNumberOfColumns();
            int rows = obpWall.getNumberOfRows();
            cellDTO.setNumberOfColumns(columns);
            cellDTO.setNumberOfRows(rows);
            for (OBPCell cell : cells) {
                OBPCellPositionDTO cellPositionDTO = new OBPCellPositionDTO();
                cellPositionDTO.setName(cell.getName());
                cellPositionDTO.setState(cell.getState());
                //判断问题处理格是否被占用
                if (cellPositionDTO.getState().equals(CellState.unoccupied.toString())) {
                    StorageLocation storageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), cell.getName());
                    if (storageLocation != null) {
                        UnitLoad unitLoad = unitLoadRepository.movingGetByStorageLocation(storageLocation);
                        if (unitLoad != null) {
                            BigDecimal amount = stockUnitRepository.sumByUnitLoad(unitLoad);
                            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                                cell.setState(CellState.occupied.toString());
                                obpCellRepository.save(cell);
                                cellPositionDTO.setState(cell.getState());
                            }
                        }
                    }
                }
                cellPositionDTO.setxPos(cell.getxPos());
                cellPositionDTO.setyPos(cell.getyPos());
                cellPositionDTO.setzPos(cell.getzPos());
                cellPositionDTOS.add(cellPositionDTO);
            }
            cellDTO.setObpCellPositions(cellPositionDTOS);
        }
        return cellDTO;
    }

    @SuppressWarnings("Duplicates")
    public void putDeleteGoodsToContainer(DeleteShipmentDTO deleteShipmentDTO) {

        CustomerShipment customerShipment = customerShipmentRepository.getByShipmentNo(deleteShipmentDTO.getShipmentNo());
        OBPSolvePosition solvePositionEntity;
        if (deleteShipmentDTO.getSolveKey().equals(SolveResoult.DELETE_ORDER_FORCE.toString())) {
            solvePositionEntity = obpSolvePositionRepository.getForceDeleteSolvePositionByShipmentNoAndSolveKey(
                    applicationContext.getCurrentWarehouse(), deleteShipmentDTO.getShipmentNo(), deleteShipmentDTO.getSolveKey());
            if (customerShipment.getState() == ShipmentState.DELETED && !customerShipment.getPickMode().equals("PICK_TO_PACK") && solvePositionEntity.getEntityLock() == 2) {
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_DELETE_ERROR.getName());
            }
        } else if (deleteShipmentDTO.getSolveKey().equals(SolveResoult.DELETE_ORDER_CUSTOMER.toString())) {
            if (customerShipment.getState() < ShipmentState.REBINED) {
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_DELETE_ERROR.getName());
            }
        }
        //判断容器是否符合放删单的商品
        StorageLocation toStorageLocation = outBoundProblemCommonBusiness.checkProcessContainer(deleteShipmentDTO.getContainerName(),
                InventoryState.INVENTORY.toString());
        if (!deleteShipmentDTO.getShipmentNo().isEmpty() && !deleteShipmentDTO.getItemNo().isEmpty()) {
            List<ItemData> itemDatas = itemDataRepository.getByItemNoSkuNo(applicationContext.getCurrentWarehouse(), deleteShipmentDTO.getItemNo());
            if (itemDatas.size() > 1) {
                throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_MORE_THAN_ONE.getName(), deleteShipmentDTO.getItemNo());
            }
            OBPSolve entity = null;
            if (!itemDatas.isEmpty()) {
                entity = obpSolveRepository.getGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(),
                        deleteShipmentDTO.getShipmentNo(), itemDatas.get(0).getItemNo());
            }
            if (entity == null) {
                throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_NOT_FOUND.getName(), deleteShipmentDTO.getShipmentNo());
            }
            //判断扫描商品时，是否需要扫描SN码;如果商品有SN，那么检查SN是否存在
            if (entity.getItemData().getSerialRecordType().equalsIgnoreCase(SerialNoRecordType.ALWAYS_RECORD.toString())) {
                checkSerialNo(deleteShipmentDTO.getSerialNo(), deleteShipmentDTO.getShipmentNo());
                //记录已扫描的SN
                OBPShipmentSerialNo shipmentSerialNo = obpShipmentSerialNoRepository.getSerialNo(
                        applicationContext.getCurrentWarehouse(), deleteShipmentDTO.getSerialNo(), deleteShipmentDTO.getShipmentNo());
                shipmentSerialNo.setScaned(true);
                obpShipmentSerialNoRepository.save(shipmentSerialNo);
            }
            // 容器中不能存在不同客户的相同商品
            outBoundProblemCommonBusiness.checkItemClient(toStorageLocation, entity.getItemData());

            UnitLoad unitLoad = Optional.ofNullable(unitLoadRepository.getByShipmentNo(deleteShipmentDTO.getShipmentNo()))
                    .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName()));
            StorageLocation fromStorageLocation = unitLoad.getStorageLocation();
            if (fromStorageLocation == null) {
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName());
            }
            //判断商品是否需要输入有效期
            Lot lot=null;
            if (entity.getItemData().isLotMandatory()) {
                lot=outBoundProblemCommonBusiness.checkItemLot(fromStorageLocation,toStorageLocation, entity.getItemData(),
                        ConversionUtil.toZonedDateTime(deleteShipmentDTO.getUseNotAfter()).toLocalDate(),
                        deleteShipmentDTO.getAmount(), InventoryState.INVENTORY.toString());
            }
            List<UnitLoad> unitLoadsList = unitLoadRepository.getByStorageLocation(toStorageLocation);
            UnitLoad toUnitLoad = null;
            if (unitLoadsList.isEmpty()) {
                toUnitLoad = buildBusiness.buildUnitLoad(toStorageLocation);
            } else {
                toUnitLoad = unitLoadsList.get(0);
            }
            //每扫描一次，已扫描数递增；如果是无库存删单，只需更新solvePosition表的已扫描数
            if (deleteShipmentDTO.getSolveKey().equalsIgnoreCase(SolveResoult.DELETE_ORDER_CUSTOMER.toString()) ||
                    deleteShipmentDTO.getSolveKey().equalsIgnoreCase(SolveResoult.DELETE_ORDER_FORCE.toString())) {

                entity.setAmountScanedProblem(entity.getAmountScanedProblem().add(BigDecimal.ONE));
                if (entity.getAmountScanedProblem().compareTo(entity.getAmountProblem()) > 0) {
                    entity.setAmountScanedProblem(entity.getAmountProblem());
                }
                entity.setAmountScaned(entity.getAmountScaned().add(BigDecimal.ONE));
                if (entity.getAmountScaned().compareTo(entity.getAmountShipment()) > 0) {
                    entity.setAmountScaned(entity.getAmountShipment());
                }
                updateScaned(entity, null);
            }
            //每扫描一件商品，生成库存记录
            if(deleteShipmentDTO.getSerialNo()!=null&&!deleteShipmentDTO.getSerialNo().isEmpty())
                 moveGoodsBusiness.movingGoodsToContainer(unitLoad, toUnitLoad, entity.getItemData(), BigDecimal.ONE,deleteShipmentDTO.getSerialNo(),lot);
            else
                moveGoodsBusiness.movingGoods(unitLoad, toUnitLoad, toStorageLocation, entity.getItemData(), BigDecimal.ONE,null,lot);

            OBPSolvePosition solvePosition = outBoundProblemCommonBusiness.checkAndSaveSolvePosition(entity, deleteShipmentDTO.getSolveKey(),
                    deleteShipmentDTO.getContainerName());

            //判断shipment下所有商品是否已扫描完
            List<OBPSolve> solves = obpSolveRepository.getGoodsByShipment(applicationContext.getCurrentWarehouse(), deleteShipmentDTO.getShipmentNo());
            if (solves == null)
                throw new ApiException(ExceptionEnum.EX_OBJECT_NOT_FOUND.toString());
            String scanState = OBPSolveState.scaned.toString();
            for (OBPSolve solve : solves) {
                if (!solve.getScaned().equals(scanState)) {
                    scanState = solve.getScaned();
                    break;
                }
            }
            //如果所有商品扫描完，将状态改为solved，同时添加库存记录
            if (deleteShipmentDTO.getSolveKey().equalsIgnoreCase(SolveResoult.DELETE_ORDER_CUSTOMER.toString()) ||
                    deleteShipmentDTO.getSolveKey().equalsIgnoreCase(SolveResoult.DELETE_ORDER_FORCE.toString())) {
                if (scanState.equals(OBPSolveState.scaned.toString())) {
                    solvePositionEntity = obpSolvePositionRepository.getForceDeleteSolvePositionByShipmentNoAndSolveKey(
                            applicationContext.getCurrentWarehouse(), deleteShipmentDTO.getShipmentNo(), deleteShipmentDTO.getSolveKey());
                    solvePositionEntity.setState(OBPSolveState.solved.toString());
                    obpSolvePositionRepository.save(solvePositionEntity);
                    for (OBPSolve solve : solves) {
                        solve.setState(OBPSolveState.solved.toString());
                        obpSolveRepository.save(solve);
                    }
                    if (!solvePosition.getLocationContainer().contains("/")) {
                        //更新UNITLOAD_SHIPMENT表
                        unitLoadShipmentQuery.updateUnitloadByShipment(toUnitLoad.getId(), customerShipment.getId());
                    } else { //同一个订单放在多个容器里
                        unitLoadShipmentQuery.deleteUnitloadShipment(customerShipment.getId());
                        String containers[] = solvePosition.getLocationContainer().split("/");
                        for (int i = 0; i < containers.length; i++) {
                            StorageLocation storageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), containers[i]);
                            UnitLoad load = unitLoadRepository.movingGetByStorageLocation(storageLocation);
                            unitLoadShipmentQuery.insertUnitloadShipment(load.getId(), customerShipment.getId());
                        }
                    }
                }
            } else if (deleteShipmentDTO.getSolveKey().equalsIgnoreCase(SolveResoult.OUT_OF_STOCK_DELETE_ORDER.toString())) {
                //无库存删单，更新solvePosition的已扫描数，当已扫描数=正常数，将订单状态改为solved
                solvePosition.setAmountScaned(solvePosition.getAmountScaned().add(BigDecimal.ONE));
                obpSolvePositionRepository.save(solvePosition);
                OBPSolve solve = obpSolveRepository.getByItemNoShipmentNo(applicationContext.getCurrentWarehouse(), itemDatas.get(0).getItemNo(), deleteShipmentDTO.getShipmentNo());
                solve.setAmountProblem(solve.getAmountProblem().add(BigDecimal.ONE));
                obpSolveRepository.save(solve);
                if (solvePosition.getAmountScaned().compareTo(deleteShipmentDTO.getAmountNormal()) == 0) {
                    solvePosition.setState(OBPSolveState.solved.toString());
                    obpSolvePositionRepository.save(solvePosition);
                    //将订单所有商品状态改为solved
                    List<OBPSolve> obpSolves = obpSolveRepository.getByShipmentNo(applicationContext.getCurrentWarehouse(), deleteShipmentDTO.getShipmentNo());
                    //修改问题格的状态为unoccupied
                    OBPCell cell = obpSolves.get(0).getObpCell();
                    cell.setState(CellState.unoccupied.toString());
                    obpCellRepository.save(cell);
                    for (OBPSolve obpSolve : obpSolves) {
                        obpSolve.setObpCell(null);
                        obpSolve.setState(OBPSolveState.solved.toString());
                        obpSolveRepository.save(obpSolve);
                    }
                    if (!solvePosition.getLocationContainer().contains("/")) {
                        unitLoadShipmentQuery.updateUnitloadByShipment(toUnitLoad.getId(), customerShipment.getId());
                    } else {
                        unitLoadShipmentQuery.deleteUnitloadShipment(customerShipment.getId());
                        String containers[] = solvePosition.getLocationContainer().split("/");
                        for (int i = 0; i < containers.length; i++) {
                            StorageLocation storageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), containers[i]);
                            UnitLoad load = unitLoadRepository.movingGetByStorageLocation(storageLocation);
                            unitLoadShipmentQuery.insertUnitloadShipment(load.getId(), customerShipment.getId());
                        }
                    }
                }
            }
        }
    }

    public boolean updateScaned(OBPSolve entity, OBPSolve entityProblem) {
        boolean result = false;
        if (entityProblem != null && !entityProblem.getObproblem().getProblemType().equals(ProblemType.LOSE.toString())) {
            if (entityProblem.getAmountScanedProblem().compareTo(entityProblem.getAmountProblem()) < 0) {
                obpSolveRepository.save(entityProblem);
                result = true;
            } else if (entityProblem.getAmountProblem().compareTo(BigDecimal.ZERO)>0&&entityProblem.getAmountScanedProblem().compareTo(entityProblem.getAmountProblem()) == 0) {
                if (!entityProblem.getScaned().equals(OBPSolveState.scaned.toString())) {
                    entityProblem.setScaned(OBPSolveState.scaned.toString());
                    obpSolveRepository.save(entityProblem);
                    result = true;
                }
            }
        }

        BigDecimal amountScanedDefault = entity.getAmountScaned();
//        if (entityProblem != null) {
//            if (entityProblem.getObproblem().getProblemType().equals(ProblemType.LOSE.toString()))
//                amountScanedDefault = amountScanedDefault.add(entityProblem.getAmountProblem());
//        }
        //获取订单里丢失商品数
        OBProblem obProblem = obProblemRepository.getByShipmentAndItemLose(applicationContext.getCurrentWarehouse(), entity.getCustomerShipment().getId(), entity.getItemData().getId());
        BigDecimal problemAmount = BigDecimal.ZERO;
        if (obProblem != null) {
            problemAmount = obProblem.getAmount();
        }
        //获取放入残品筐的商品数
        List<OBProblem> problems=obProblemRepository.getByShipmentIdAndType(applicationContext.getCurrentWarehouse(),entity.getCustomerShipment().getId(),entity.getItemData().getId());
        for(OBProblem problem:problems){
            problemAmount=problemAmount.add(problem.getSolveAmount());
        }
        if (amountScanedDefault.compareTo(entity.getAmountShipment()) < 0) {
            if (problemAmount.compareTo(BigDecimal.ZERO) > 0 && problemAmount.add(amountScanedDefault).compareTo(entity.getAmountShipment()) == 0) {
                entity.setScaned(OBPSolveState.scaned.toString());
                result = true;
            }
            obpSolveRepository.save(entity);
            //当订单的扫描数=商品总数，改状态为scaned
        } else if (amountScanedDefault.compareTo(entity.getAmountShipment()) == 0) {
            entity.setScaned(OBPSolveState.scaned.toString());
            obpSolveRepository.save(entity);
            result = true;
        }
        //获取订单里只有丢失的商品，没有正常的商品
        List<OBPSolve> obpSolves = obpSolveRepository.getAllGoodsByShipmentNo(applicationContext.getCurrentWarehouse(), entity.getCustomerShipment().getShipmentNo());
        for (OBPSolve obpSolve : obpSolves) {
            List<OBPSolve> solves = obpSolveRepository.getProblemGoodsByShipmentNo(applicationContext.getCurrentWarehouse(), entity.getCustomerShipment().getShipmentNo(), obpSolve.getItemData().getItemNo());
            if (solves.size() == 1 && solves.get(0).getObproblem().getProblemType().equals(ProblemType.LOSE.toString()) && solves.get(0).getAmountProblem().compareTo(obpSolve.getAmountShipment()) == 0) {
                obpSolve.setScaned(OBPSolveState.scaned.toString());
                obpSolveRepository.save(obpSolve);
            }
        }
        return result;
    }

    private SolveShipmentPositionDTO setSeriaNo(OBPSolve entity, SolveShipmentPositionDTO dto) {
        if (entity.getItemData().getSerialRecordType().equals(SerialNoRecordType.ALWAYS_RECORD.toString())) {
            List<ItemDataSerialNumber> serialNumbers = itemDataSerialNumberRepository.getByItemData(entity.getItemData().getId());
            if (!serialNumbers.isEmpty()) {
                int size = serialNumbers.size();
                List<String> serialNo = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    serialNo.add(serialNumbers.get(i).getSerialNo());
                }
                dto.setSerialNo(serialNo);
            }
        }
        return dto;
    }

    public String scanShipmentNo(String shipmentNo) {
        //问题处理时候，通过扫描容器查询到订单
        if (!shipmentNo.isEmpty()) {
            boolean result = shipmentNo.matches("[0-9]+");
            if (!result) {
                StorageLocation storageLocation = Optional.ofNullable(storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), shipmentNo))
                        .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_IT_STORAGE_LOCATION_NOT_FOUND.getName()));
                UnitLoad unitLoad = unitLoadRepository.movingGetByStorageLocation(storageLocation);
                if (unitLoad == null)
                    throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_NOT_FOUND.getName());
                List<Object> objects = unitLoadShipmentQuery.getShipmentNo(unitLoad.getId());
                if(objects.size()>1)
                    throw new ApiException(OutBoundProblemException.EX_CONTAINER_HAS_MORE_THAN_ONE_SHIPMENT.getName());
                CustomerShipment customerShipment = customerShipmentRepository.retrieve(String.valueOf(objects.get(0)));
                //CustomerShipment customerShipment= unitLoadRepository.getByLocation(storageLocation);
                if (customerShipment == null)
                    throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.toString());
                shipmentNo = customerShipment.getShipmentNo();
            }
        }
        return shipmentNo;
    }

    public void customerDeleteShipment(String stationId, String wallId, String shipmentNo) {

        //判断在删除订单之前，是否正在进行的问题处理，若存在，需先进行清除问题格操作
        List<OBPSolve> obpSolves = obpSolveRepository.getAllGoodsByShipmentNo(applicationContext.getCurrentWarehouse(), shipmentNo);
        if (!obpSolves.isEmpty()) {
            boolean flag=true;
            for(OBPSolve obpSolve:obpSolves){
                if(obpSolve.getScaned().equals(OBPSolveState.unscaned.toString())){
                    flag=false;
                    break;
                }
            }
            if(flag) throw new ApiException(OutBoundProblemException.EX_SHIPMENT_IS_SOLVED.getName(), shipmentNo);
        } else {
            //添加订单下的所有商品到obpSolve表中
            addShipmentToOBPSolve(stationId, wallId, shipmentNo, null);
        }
    }

    public void deleteObpSolve(String shipmentNo, String obpStationId, String obpWallId) {
        deleteSolveBusiness(shipmentNo);
        List<OBProblem> problems = obProblemRepository.getByShipment(applicationContext.getCurrentWarehouse(), shipmentNo);
        for (OBProblem problem : problems) {
            if (problem.getState().equals(OBPSolveState.solved.toString())) {
                obProblemRepository.delete(problem);
            }
        }
        addProblemToOBPSolve(obpStationId, obpWallId, shipmentNo);

        addShipmentToOBPSolve(obpStationId, obpWallId, shipmentNo, null);
    }

    public void deleteSolve(String shipmentNo, String obpStationId, String obpWallId) {
        deleteSolveBusiness(shipmentNo);
        addShipmentToOBPSolve(obpStationId, obpWallId, shipmentNo, null);
    }

    public void deleteSolveBusiness(String shipmentNo) {
        List<OBPSolve> solves = obpSolveRepository.getByShipmentNo(applicationContext.getCurrentWarehouse(), shipmentNo);
        List<OBPShipmentSerialNo> shipmentSerialNoList = obpShipmentSerialNoRepository.getAllByShipment(applicationContext.getCurrentWarehouse(),solves.get(0).getCustomerShipment().getId());
        for (OBPShipmentSerialNo serialNo : shipmentSerialNoList) {
             obpShipmentSerialNoRepository.delete(serialNo);
        }
        for (OBPSolve solve : solves) {
            List<OBPLocation> obpLocations = locationRepository.getBySolveId(applicationContext.getCurrentWarehouse(), solve.getId());
            for (OBPLocation obpLocation : obpLocations) {
                locationRepository.delete(obpLocation);
            }
            OBPSolvePosition solvePosition = obpSolvePositionRepository.getSolvePositionByShipmentNoAndSolve(
                    applicationContext.getCurrentWarehouse(), shipmentNo, solve.getId());
            if (solvePosition != null) {
                obpSolvePositionRepository.delete(solvePosition);
            }
            obpSolveRepository.delete(solve);
        }
    }
}
