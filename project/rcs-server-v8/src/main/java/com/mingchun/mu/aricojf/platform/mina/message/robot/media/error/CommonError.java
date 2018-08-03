package com.mingchun.mu.aricojf.platform.mina.message.robot.media.error;

/**
 *
 * 底盘一般性异常
 *
 * Created by Laptop-6 on 2017/11/15.
 */
public enum CommonError{
    EXP_MOTOR_UP("伺服頂升異常", 8),
    EXP_MOTOR_TURNPLATE("伺服轉盤異常", 9),
    EXP_UP_LIMIT_SWITCH("頂升-極限開關異常 ", 10),
    EXP_TURNPLATE_PHOTOELECTRICITY_SWITCH("轉盤-光電開關異常", 11),
    EXP_UP_INIT("頂升-初始化異常 ", 12),
    EXP_TURNPLATE_INIT("轉盤-初始化異常", 13),
    SERVER_MODS_UN_INIT("伺服模組-未初始化", 14),
    SERVER_MODS_BREAK("伺服模組-斷線", 15);
    private String name;
    private int index;
    CommonError(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (CommonError error: CommonError.values()) {
            if (error.getIndex() == index) {
                return error.name;
            }
        }
        return null;
    }
    // 获取枚举对象
    public static CommonError getEnumObject(int index) {
        for (CommonError error: CommonError.values()) {
            if (error.getIndex() == index) {
                return error;
            }
        }
        return null;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public int getErrorId(){
        return MediaErrorUtil.getErrorId(this.getIndex());
    }

    @Override
    public String toString() {
        return name;
    }
}
