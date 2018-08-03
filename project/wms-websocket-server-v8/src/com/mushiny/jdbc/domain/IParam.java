package com.mushiny.jdbc.domain;

import java.sql.CallableStatement;

/**
 * Created by Tank.li on 2017/6/13.
 */
public interface IParam {
    /**
     * 索引值
     *
     * @param index
     */
    void setIndex(int index);

    /**
     * 参数
     *
     * @param value
     */
    void setValue(Object value);

    /**
     * 返回值
     *
     * @return
     */
    Object getValue();

    /**
     * 返回索引
     *
     * @return
     */
    int getIndex();
    //注册参数//入参与返回参数注册方式不一样

    /**
     * 设置参数
     *
     * @param callableStatement
     */
    void registerParam(CallableStatement callableStatement);

}
