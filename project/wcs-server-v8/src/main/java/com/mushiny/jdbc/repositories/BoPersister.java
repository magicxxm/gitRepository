package com.mushiny.jdbc.repositories;

import com.mushiny.beans.BaseObject;
import com.mushiny.comm.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tank.li on 2017/9/23.
 */
public class BoPersister extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(BoPersister.class);

    private JdbcRepository jdbcRepository;

    public JdbcRepository getJdbcRepository() {
        return jdbcRepository;
    }

    public void setJdbcRepository(JdbcRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    public void persist(BaseObject baseObject) {
        if (baseObject.getDelCon() != null && !baseObject.getDelCon().isEmpty()) {
            //做删除操作
            jdbcRepository.deleteRecords(baseObject.getTable(), baseObject.getDelCon());
        } else if(baseObject.getId() == null //?
                && (baseObject.getCon()==null || baseObject.getCon().isEmpty())){
            //新增操作
            jdbcRepository.insertBusinessObject(baseObject);
        }else {
            //做更新操作
            Map newValue = baseObject.getNewValue();
            if (newValue == null || newValue.isEmpty()) {
                //logger.debug("未做更新操作，表"+baseObject.getTable()+"的状态是空:"+newValue);
                return;
            }
            CommonUtils.modifyUselessInfo(newValue);
            Map con = new HashMap();
            con.put(baseObject.getIdName(), baseObject.getId());
            jdbcRepository.updateRecords(baseObject.getTable(), newValue, con);
            baseObject.clearKV();
            logger.debug("BO:" + baseObject.getTable() + "状态已刷新到数据库!" + baseObject.getActiveThread()+" "
                    + baseObject.getIdName() + ":" + baseObject.getId());
        }
    }

    @Override
    public void run() {
        BaseObject baseObject = null;
        while((baseObject = jdbcRepository.getBo())!=null){
            if (baseObject.getCon() == null) {
                try {
                    persist(baseObject);
                } catch (Throwable e) {
                    e.printStackTrace();
                    logger.error("persist(baseObject) fail:" + baseObject.getActiveThread()+" "
                            +baseObject.getTable() + " "+baseObject.getId()+" "+baseObject.getKv(),e);
                }
            }else {
                //按条件批量更新
                try {
                    this.jdbcRepository.updateRecords(baseObject.getTable(),
                            baseObject.getNewValue(),baseObject.getCon());
                } catch (Throwable e) {
                    e.printStackTrace();
                    logger.error("updateRecords fail:" + baseObject.getActiveThread()+" "
                            +baseObject.getTable() + " "+baseObject.getNewValue()+" "+baseObject.getCon(),e);
                }
            }
        }
    }
}
