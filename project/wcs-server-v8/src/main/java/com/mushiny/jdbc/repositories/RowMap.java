package com.mushiny.jdbc.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tank.li on 2017/6/13.
 */
public class RowMap implements RowMapper<Map> {
    private final static Logger logger = LoggerFactory.getLogger(RowMap.class);
    @Override
    public Map mapRow(ResultSet resultSet, int i) throws SQLException {
        Map data = new HashMap();
        int colCount = resultSet.getMetaData().getColumnCount();
        for (int j = 0; j < colCount; j++) {
            String key = resultSet.getMetaData().getColumnName(j+1);
            String label = resultSet.getMetaData().getColumnLabel(j+1);
            data.put(key.toUpperCase(),resultSet.getObject(key));
            data.put(label.toUpperCase(),resultSet.getObject(label));
        }
        //logger.debug("index:"+i,"row="+data);
        return data;
    }
}
