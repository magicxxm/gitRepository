package view.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/13 0013.
 */
public class PathBean {
    private List pathList;
    private long robotId;
    private long upAddressCodeId;
    private long downAddressCodeId;
    private long waitTime;
    private int rotateTheta;
    private int podCodeId;

    public List getPathList() {
        return pathList;
    }

    public void setPathList(List pathList) {
        this.pathList = pathList;
    }

    public long getRobotId() {
        return robotId;
    }

    public void setRobotId(long robotId) {
        this.robotId = robotId;
    }

    public long getUpAddressCodeId() {
        return upAddressCodeId;
    }

    public void setUpAddressCodeId(long upAddressCodeId) {
        this.upAddressCodeId = upAddressCodeId;
    }

    public long getDownAddressCodeId() {
        return downAddressCodeId;
    }

    public void setDownAddressCodeId(long downAddressCodeId) {
        this.downAddressCodeId = downAddressCodeId;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public int getRotateTheta() {
        return rotateTheta;
    }

    public void setRotateTheta(int rotateTheta) {
        this.rotateTheta = rotateTheta;
    }

    public int getPodCodeId() {
        return podCodeId;
    }

    public void setPodCodeId(int podCodeId) {
        this.podCodeId = podCodeId;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("路径=");
        stringBuilder.append(pathList != null ? pathList.toString() : "");
        stringBuilder.append(",");
        stringBuilder.append("小车ID=");
        stringBuilder.append(robotId);
        stringBuilder.append(",");
        stringBuilder.append("上升点=");
        stringBuilder.append(upAddressCodeId);
        stringBuilder.append(",");
        stringBuilder.append("货架码=");
        stringBuilder.append(podCodeId);
        stringBuilder.append(",");
        stringBuilder.append("旋转角度=");
        stringBuilder.append(rotateTheta);
        stringBuilder.append(",");
        stringBuilder.append("下降点=");
        stringBuilder.append(downAddressCodeId);
        stringBuilder.append(",");
        stringBuilder.append("等待时间(ms)=");
        stringBuilder.append(waitTime);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
