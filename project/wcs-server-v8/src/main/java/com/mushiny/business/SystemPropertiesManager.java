package com.mushiny.business;

import com.mushiny.comm.CommonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tank.li on 2017/7/25.
 */
@Component
@org.springframework.core.annotation.Order(value = 0)
public class SystemPropertiesManager implements CommandLineRunner{

    private final static Logger logger = LoggerFactory.getLogger(RobotManager.class);

    @Autowired
    private JdbcRepository jdbcRepository;

    private Map<SysKey,String> properties = new ConcurrentHashMap<>();

    public String getProperty(String key,String wareHouseId){
        return properties.get(new SysKey(wareHouseId,key));
    }

    @Override
    public void run(String... strings) throws Exception {
        loadSystemProperties();
    }
    @Scheduled(fixedDelay = 100000l)
    public void loadSystemProperties() {
        /*SYSTEM_KEY` varchar(255) NOT NULL,
          `SYSTEM_VALUE` varchar(255) NOT NULL,
          `WAREHOUSE_ID` varchar(255) NOT NULL,*/
        logger.debug("加载系统参数信息.....");
        List<Map> rows = this.jdbcRepository.queryByKey("SystemPropertiesManager.loadSystemProperty");
        for (int i = 0; i < rows.size(); i++) {
            Map row = rows.get(i);
            String WAREHOUSE_ID = CommonUtils.parseString("WAREHOUSE_ID",row);
            String SYSTEM_KEY = CommonUtils.parseString("SYSTEM_KEY",row);
            String SYSTEM_VALUE = CommonUtils.parseString("SYSTEM_VALUE",row);
            properties.put(new SysKey(WAREHOUSE_ID,SYSTEM_KEY), SYSTEM_VALUE);
        }
        logger.debug("加载系统参数信息结束:"+properties);
    }

    private class SysKey{
        private String wareHouseId;
        private String key;

        public SysKey(String wareHouseId, String key) {
            this.wareHouseId = wareHouseId;
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SysKey sysKey = (SysKey) o;

            if (wareHouseId != null ? !wareHouseId.equals(sysKey.wareHouseId) : sysKey.wareHouseId != null)
                return false;
            return key != null ? key.equals(sysKey.key) : sysKey.key == null;
        }

        @Override
        public int hashCode() {
            int result = wareHouseId != null ? wareHouseId.hashCode() : 0;
            result = 31 * result + (key != null ? key.hashCode() : 0);
            return result;
        }
    }
}
