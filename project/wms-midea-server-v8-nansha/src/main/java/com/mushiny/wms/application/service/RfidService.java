package com.mushiny.wms.application.service;

/**
 * Created by Administrator on 2018/7/6.
 */
public interface RfidService {

     /**
      * 保存RFID pod 信息
      * @param pods
      * @return
      */
     Integer saveRfidInfo(String pods);

     /**
      * 通过工作站按默认sectionID呼叫空货架
      * @param pods
      * @return
      */
     Integer saveRfidInfoByStationName(String pods);
}
