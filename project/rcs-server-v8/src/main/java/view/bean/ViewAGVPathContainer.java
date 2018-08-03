package view.bean;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2018/5/13 0013.
 */
public class ViewAGVPathContainer {
    private long robotId;
    private volatile int executingPathFlag;
    private volatile boolean autoSendFlag;
    private final List<PathBean> pathList = new CopyOnWriteArrayList();
    public void clearPathConfig(){
        pathList.clear();
    }
    public void addPath(PathBean pathBean){
        pathList.add(pathBean);
    }
    public void removePath(int idx){
        pathList.remove(idx);
    }

    public long getRobotId() {
        return robotId;
    }

    public void setRobotId(long robotId) {
        this.robotId = robotId;
    }

    public int getExecutingPathFlag() {
        return executingPathFlag;
    }

    public void setExecutingPathFlag(int executingPathFlag) {
        this.executingPathFlag = executingPathFlag;
    }

    public List<PathBean> getPathList() {
        return pathList;
    }

    public boolean isAutoSendFlag() {
        return autoSendFlag;
    }

    public void setAutoSendFlag(boolean autoSendFlag) {
        this.autoSendFlag = autoSendFlag;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        stringBuilder.append("\t小车ID=");
        stringBuilder.append(robotId);
        stringBuilder.append(",\n");
        stringBuilder.append("\t当前执行或即将执行第");
        stringBuilder.append(executingPathFlag+1);
        stringBuilder.append("路径,\n");
        stringBuilder.append("\t所有路径情况：");
        stringBuilder.append("[\n");
        int count = 0;
        for(PathBean pathBean : pathList){
            count ++;
            stringBuilder.append("\t\t第");
            stringBuilder.append(count);
            stringBuilder.append("条：");
            stringBuilder.append(pathBean.toString());
            stringBuilder.append("\n");
        }
        stringBuilder.append("\t]\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
