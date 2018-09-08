package com.mushiny.wms.application.business.common;

import com.mushiny.wms.application.config.RestTempConfig;
import com.mushiny.wms.application.domain.SfcMitem;
import com.mushiny.wms.application.repository.SfcMitemRepository;
import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.utils.CommonUtil;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2018/7/6.
 */
@Component
@Transactional
public class SfcMitemBusiness {

    private Logger LOG = LoggerFactory.getLogger(SfcMitemBusiness.class.getName());

    private RestTemplate restTemplate ;
    @Autowired
    private RestTempConfig restTempConfig;
    private final SfcMitemRepository sfcMitemRepository;
    @Value("${midea.webApi.INV_ORG_ID}")
    private String INV_ORG_ID;
    @Autowired
    public SfcMitemBusiness(SfcMitemRepository sfcMitemRepository) {
        this.restTemplate = new RestTemplate();
        this.sfcMitemRepository = sfcMitemRepository;
    }

    public List<SfcMitem> receive(String param)
    {
        Map tt=JSONUtil.jsonToMap(param);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("LAST_UPDATE_DATE", tt.get("LAST_UPDATE_DATE"));
        paramMap.put("INV_ORG_ID", INV_ORG_ID);

        String result=restTempConfig.sfcMitemSyn(paramMap);
        Map<String,Object> resultMap1=JSONUtil.jsonToMap(result);
        List<Map> resultMap=(List<Map>)resultMap1.get("ListData");
        List<SfcMitem> sfcMitems=new ArrayList<>();
        for(Map t:resultMap)
        {
            SfcMitem t2=  JSONUtil.mapToBean(t,new SfcMitem() );
            sfcMitems.add(t2);
        }
        return saveSfcMitem(sfcMitems);


    }
    @Scheduled(fixedDelay=1000*60*60, initialDelay = 2000)
    @Async
    public void  execute(){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("INV_ORG_ID", INV_ORG_ID);
        Date date=sfcMitemRepository.getLastUpdateDate();
        String ss="2018-06-01";
        if(!ObjectUtils.isEmpty(date))
        {
            ss=DateTimeUtil.getDateFormat(date,"yyyy-MM-dd HH:mm:ss");
        }


        paramMap.put("LAST_UPDATE_DATE",ss);
        String result=restTempConfig.sfcMitemSyn(paramMap);
        Map<String,Object> resultMap1=JSONUtil.jsonToMap(result);
        List<Map> resultMap=(List<Map>)resultMap1.get("ListData");
        List<SfcMitem> sfcMitems=new ArrayList<>();
        for(Map t:resultMap)
        {
            SfcMitem t2=  JSONUtil.mapToBean(t,new SfcMitem() );
            sfcMitems.add(t2);
        }
        List<SfcMitem> resultSfcMitem=saveSfcMitem(sfcMitems);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("物料信息同步成功\n{}",JSONUtil.toJSon(resultSfcMitem));
        }

    }
   /* @Scheduled(fixedDelay=4000, initialDelay = 2000)
    @RequestMapping("getForEntity/" + Constant.MIDEA_MITEM_SUBFIX)
    @Async*/
    public void mItems() {
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("LAST_UPDATE_DATE", Constant.LAST_UPDATE_DATE);
            paramMap.put("INV_ORG_ID", Constant.INV_ORG_ID);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(Constant.MIDEA_MITEM_URL+"?LAST_UPDATE_DATE="+Constant.LAST_UPDATE_DATE+"&INV_ORG_ID="+Constant.INV_ORG_ID, String.class, "");
            String jsonString = responseEntity.getBody();
            LOG.info(" < < < - - - 物料信息同步请求反馈结果(请求最后更新时间："+ CommonUtil.dateFormat(new Date(Constant.LAST_UPDATE_DATE)) +")。。。\n " + jsonString);
            Class<SfcMitem> sfcMitemClass = SfcMitem.class;
            List<SfcMitem> sfcMitems = CommonUtil.mideaListParamJsonModify(jsonString, SfcMitem.class);

            saveSfcMitem(sfcMitems);

            LOG.info(" 物料信息同步成功(请求最后更新时间："+ CommonUtil.dateFormat(new Date(Constant.LAST_UPDATE_DATE)) +")。。。 ");
        } catch (RestClientException e) {
            e.printStackTrace();
            LOG.info(" 物料信息同步失败。。。 ", e);
        }
    }


    public  List<SfcMitem> saveSfcMitem(List<SfcMitem> sfcMitems){
        sfcMitems = CommonUtil.removeSameItem(sfcMitems, sfcMitemRepository.findAll()); // 去重
       List<SfcMitem> result=sfcMitemRepository.save(sfcMitems);
        return result;
    }

    public Integer saveSfcMitem(SfcMitem sfcMitem){
        sfcMitemRepository.save(sfcMitem);
        return 1;
    }
}
