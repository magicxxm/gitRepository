package com.mushiny.jdbc.domain;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Created by Tank.li on 2017/6/13.
 */
public class InOutParam extends BaseParam {
    @Override
    public void registerParam(CallableStatement callableStatement) {
        try {
            callableStatement.setObject(this.getIndex(), this.getValue(), this.getType());
            callableStatement.registerOutParameter(this.getIndex(), this.getType());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
