package com.mushiny.jdbc.domain;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Created by Tank.li on 2017/6/13.
 */
public class InParam extends BaseParam {

    @Override
    public void registerParam(CallableStatement callableStatement) {
        try {
            callableStatement.setObject(this.getIndex(),this.getValue(),this.getType());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
