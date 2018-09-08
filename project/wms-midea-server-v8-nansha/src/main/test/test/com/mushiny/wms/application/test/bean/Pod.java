package test.com.mushiny.wms.application.test.bean;

/**
 * @program: wms-midea-server
 * @description: Pod 实体类
 * @author: mingchun.mu@mushiny.com
 * @create: 2018-07-10 10:21
 **/
public class Pod {
    private int id;
    private boolean isInBoundStation; // 料车在入库工作站处

    public Pod(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isInBoundStation() {
        return isInBoundStation;
    }

    public void setInBoundStation(boolean inBoundStation) {
        isInBoundStation = inBoundStation;
    }

    public boolean equals(Pod pod) {
        if(pod == null){
            return false;
        }
        if(this.id == pod.getId()){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Pod{" +
                "id='" + id + '\'' +
                ", isInBoundStation=" + isInBoundStation +
                '}';
    }
}
