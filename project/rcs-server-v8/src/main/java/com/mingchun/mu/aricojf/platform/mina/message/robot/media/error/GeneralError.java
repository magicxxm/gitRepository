package com.mingchun.mu.aricojf.platform.mina.message.robot.media.error;

/**
 *
 * 底盘一般性异常
 *
 * Created by Laptop-6 on 2017/11/15.
 */
public enum  GeneralError {
    EXP_GYROSCOPE("陀螺儀異常", 0),
    EXP_PATH_DATA_BANK("路徑資料庫異常", 1),
    BUMPER_0_TRIGGER("保險桿0觸發", 2),
    BUMPER_1_TRIGGER("保險桿1觸發", 3),
    BUMPER_2_TRIGGER("保險桿2觸發", 4),
    BUMPER_3_TRIGGER("保險桿3觸發", 5),
    EXP_FRONT_RADAR("雷射(前)異常", 6),
    EXP_BACK_RADAR("雷射(後)異常", 7),
    MANUAL_SWITCH_TRIGGER("手動開關觸發", 8),
    EXP_HEARBEAT("心跳機制失敗", 9),
    NODE_TWO_LOSS("節點2遺失", 10),
    EXP_ANGLE_CONFIRM("角度確認異常", 11),
    TRACE_OVERFLOW("軌跡追蹤溢出", 12),
    NODE_NO_MATCH_PATH("節點與路徑不穩合", 13),
    ACCELERATE_SHOCK_DETECTION("加速規衝擊檢測", 14);
    private String name;
    private int index;
    GeneralError(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (GeneralError error: GeneralError.values()) {
            if (error.getIndex() == index) {
                return error.name;
            }
        }
        return null;
    }
    // 获取枚举对象
    public static GeneralError getEnumObject(int index) {
        for (GeneralError error: GeneralError.values()) {
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
