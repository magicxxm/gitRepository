package com.mingchun.mu.aricojf.platform.mina.message.robot.media.error;

/**
 *
 * 逻辑性异常
 *
 * Created by Laptop-6 on 2017/11/15.
 */
public enum LogicError {
    EXP_BATTERY_CAPACITY("电池电量异常", 0),
    EXP_UP_NO_VISION("顶升的时候检测不到上视觉", 1),
    EXP_SERVER_BREAK("服务器断开连接", 2),
    EXP_PIC_COMMUNICATION_BREAK("PIC通信断开连接", 3),
    EXP_VISION_DETECT_FAILURE("视觉检测失败", 4),
    EXP_WALK_ERROR_TAG("走错tag", 5),
    EXP_OCCUPY_NO_TAG("不在tag上", 6),
    EXP_CHARGING("充电出现异常", 7);
    private String name;
    private int index;
    LogicError(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (LogicError error: LogicError.values()) {
            if (error.getIndex() == index) {
                return error.name;
            }
        }
        return null;
    }
    // 获取枚举对象
    public static LogicError getEnumObject(int index) {
        for (LogicError error: LogicError.values()) {
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
