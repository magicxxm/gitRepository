package com.mushiny.wms.application.business.common;

import com.mushiny.wms.application.config.RestTempConfig;
import com.mushiny.wms.application.domain.InvMitemLabel;
import com.mushiny.wms.application.domain.SfcMitem;
import com.mushiny.wms.application.repository.InvMitemLabelRepository;
import com.mushiny.wms.application.repository.SfcMitemRepository;
import com.mushiny.wms.common.utils.CommonUtil;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;


/**
 * Created by Administrator on 2018/7/6.
 */
@Component
@Transactional
public class InvMitemLabelBusiness {

    private Logger LOG = LoggerFactory.getLogger(InvMitemLabelBusiness.class.getName());
    @Autowired
    private RestTempConfig restTempConfig;

    private final InvMitemLabelRepository invMitemLabelRepository;
    @Value("${midea.webApi.INV_CODE}")
    private String INV_CODE;
    @Value("${midea.webApi.INV_ORG_ID}")
    private String INV_ORG_ID;
    @Autowired
    public InvMitemLabelBusiness(InvMitemLabelRepository invMitemLabelRepository) {

        this.invMitemLabelRepository = invMitemLabelRepository;
    }
    public List<InvMitemLabel>  saveSfcMitem(List<InvMitemLabel> invMitemLabels)
    {
        invMitemLabels = CommonUtil.removeSameItem(invMitemLabels, invMitemLabelRepository.findAll()); // 去重
        List<InvMitemLabel>  result=invMitemLabelRepository.save(invMitemLabels);
         return result;
    }





    public  List<InvMitemLabel> receive(String param)
    {
        Map tt= JSONUtil.jsonToMap(param);
        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("LAST_UPDATE_DATE", tt.get("LAST_UPDATE_DATE"));
        paramMap.put("INV_ORG_ID", INV_ORG_ID);
        paramMap.put("INV_CODE", INV_CODE);

        String result=restTempConfig.invMitemLabelSyn(paramMap);
        Map<String,Object> resultMap1=JSONUtil.jsonToMap(result);
        List<Map> resultMap=(List<Map>)resultMap1.get("ListData");
        List<InvMitemLabel> sfcMitems=new ArrayList<>();

        for(Map t:resultMap)
        {
            InvMitemLabel t2=  JSONUtil.mapToBean(t,new InvMitemLabel() );
            sfcMitems.add(t2);
        }
       return saveSfcMitem(sfcMitems);


    }

    @Scheduled(fixedDelay=1000*60*60, initialDelay = 2000)
    @Async
    public void execute()
    {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("INV_ORG_ID", INV_ORG_ID);
        paramMap.put("INV_CODE",INV_CODE);


        Date date=invMitemLabelRepository.getLastUpdateDate();
        String ss="2018-07-01";
        if(!ObjectUtils.isEmpty(date))
        {
            ss=DateTimeUtil.getDateFormat(date,"yyyy-MM-dd HH:mm:ss");
        }


        paramMap.put("LAST_UPDATE_DATE",ss);

        String result=restTempConfig.invMitemLabelSyn(paramMap);
        Map<String,Object> resultMap1=JSONUtil.jsonToMap(result);
        List<Map> resultMap=(List<Map>)resultMap1.get("ListData");
        List<InvMitemLabel> sfcMitems=new ArrayList<>();
        if(!CollectionUtils.isEmpty(resultMap))
        {
            for(Map t:resultMap)
            {
                InvMitemLabel t2=  JSONUtil.mapToBean(t,new InvMitemLabel() );
                sfcMitems.add(t2);
                break;

            }
            List<InvMitemLabel> lml=saveSfcMitem(sfcMitems);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Mo信息同步成功\n{}",JSONUtil.toJSon(lml));
            }
        }else{
            LOG.error("Mo信息同步失败,返回信息\n{}",JSONUtil.toJSon(result));
        }

    }


}
