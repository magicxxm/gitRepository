package com.mushiny.wms.tot.report.query.dto;

import java.io.Serializable;

/**
 * Created by Laptop-8 on 2017/6/29.
 * 员工打卡页面点员工姓名进去页面下方表格对应DTO
 */
public class CTimeDetailDTO  implements Serializable{
    private  String  message;//详情
    private  String  activityStartTime;//开始
    private  String  activityEndTime;//结束
    private  double  total;//时长（分钟）
    private  String  map;//甘特图
    private  String  actionType;//直接工作,普通间接，超级间接

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        if (activityStartTime!=null && activityStartTime.length()>=19)
            this.activityStartTime = activityStartTime.substring(0,19);
        else
            this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        if (activityStartTime!=null && activityStartTime.length()>=19)
            this.activityEndTime = activityEndTime.substring(0,19);
        else
            this.activityEndTime = activityEndTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
