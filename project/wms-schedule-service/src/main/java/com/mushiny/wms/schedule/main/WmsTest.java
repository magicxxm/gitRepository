package com.mushiny.wms.schedule.main;

import com.mushiny.wms.schedule.service.PackService;
import com.mushiny.wms.schedule.service.PickingOrderPositionService;
import com.mushiny.wms.schedule.service.StowGoodsService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WmsTest {

    private static final org.slf4j.Logger log= LoggerFactory.getLogger(WmsTest.class);
    private final StowGoodsService stowGoodsService;
    private final PickingOrderPositionService pickingOrderPositionService;
    private final PackService packService;

    @Autowired
    public WmsTest(StowGoodsService stowGoodsService,
                   PickingOrderPositionService pickingOrderPositionService,
                   PackService packService) {
        this.stowGoodsService = stowGoodsService;
        this.pickingOrderPositionService = pickingOrderPositionService;
        this.packService = packService;
    }
    //登录上架工作站
   // @Scheduled(fixedDelay = 900000000)
    public void loginStowStation(){
        log.info("进入登录上架工作站---------------");
        stowGoodsService.loginStation();
    }
    //上架
    @Scheduled(fixedDelay = 10000)
    public void stowGoods() {
        log.info("进入上架定时任务------------------");
        stowGoodsService.stow();
    }
    //创建订单
    //@Scheduled(fixedDelay = 600000)
    public void createCustomerOrder(){
        log.info("进入创建订单---------------");
        pickingOrderPositionService.createOrder();
    }
    //拆分订单
   // @Scheduled(fixedDelay = 10000)
    public void startSplit() {
        log.info("进入拆分订单---------------");
        pickingOrderPositionService.splitCustomerOrder();
    }
    //登录拣货工作站
    //@Scheduled(fixedDelay = 900000000)
    public void loginPickStation(){
        log.info("进入登录拣货工作站---------------");
        pickingOrderPositionService.loginPickStation();
    }
    //拣货
    @Scheduled(fixedDelay = 10000)
    public void picking(){
        log.info("进入拣货---------------");
        pickingOrderPositionService.picking();
    }
    //包装
    @Scheduled(fixedDelay = 10000)
    public void pack(){
        log.info("进入包装---------------");
        packService.pack();
    }
}
