package com.mingchun.mu.aricojf.platform.mina.message.robot.media.error;

/**
 *
 * 底盘严重性异常
 *
 * Created by Laptop-6 on 2017/11/15.
 */
public enum SeriousError {
    PIC_RESET_FAILURE("PIC重置失敗", 0),
    RAM_SIZE_DIFINE_ERROR("RAM大小定義錯誤", 1),
    ROM_WR_FAILURE("ROM讀寫失敗", 2),
    EMERGENCY_SWITCH_TRIGGER("緊急開關觸發", 3),
    USB_UN_CONNECT("USB未連線", 4),
    EXP_LEFT_MOTOR("左馬達異常", 8),
    EXP_RIGHT_MOTOR("右馬達異常", 9),
    EXP_MOTOR_EMERGENCY("馬達-EMCY異常 ", 10),
    MOTOR_SDO_COMMUNICATION_FAILURE("馬達-SDO通訊失敗", 11),
    MOTOR_PDO1_COMMUNICATION_FAILURE("馬達-PDO1通訊失敗 ", 12),
    MOTOR_PDO2_COMMUNICATION_FAILURE("馬達-PDO2通訊失敗", 13),
    MOTOR_PDO3_COMMUNICATION_FAILURE("馬達-PDO3通訊失敗", 14),
    MOTOR_PDO4_COMMUNICATION_FAILURE("馬達-PDO4通訊失敗", 15);
    private String name;
    private int index;
    SeriousError(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (SeriousError error: SeriousError.values()) {
            if (error.getIndex() == index) {
                return error.name;
            }
        }
        return null;
    }
    // 获取枚举对象
    public static SeriousError getEnumObject(int index) {
        for (SeriousError error: SeriousError.values()) {
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
