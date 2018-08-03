package com.mushiny.jdbc.domain;

/**
 * Created by Tank.li on 2017/6/13.
 */
public abstract class BaseParam implements IParam {

    //定义的java.sql.Types.***类型，为空的话，默认返回字符串
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //无参数构造函数 默认为字符串
    public BaseParam() {
        this.type = java.sql.Types.CHAR;
    }

    /**
     * 构造不同的返回类型，java.sql.Types定义了格式
     *
     * @param type java.sql.Types 里头定义的类型格式
     */
    public BaseParam(int type) {
        this.type = type;
    }

    private int index;
    private Object value;

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "index:"+index+" value:"+value;
    }
}
