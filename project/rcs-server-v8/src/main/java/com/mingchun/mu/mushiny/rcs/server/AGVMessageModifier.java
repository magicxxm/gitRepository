
package com.mingchun.mu.mushiny.rcs.server;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 *
 * mingchun.mu@mushiny.com
 */
public class AGVMessageModifier {

    private static Logger LOG = LoggerFactory.getLogger(AGVMessageModifier.class.getName());

    /*public void modifyGlobalSeriesPathCheck(AGVMessage agvMessage){
        if (agvMessage.isRunGlobalSeriesPath()) {
            return;
        }
//        SeriesPath nowSeriesPath = getNextGlobalSeriesPath();
        // mingchun.mu@mushiny.com  -->  如果pod扫描错误  清空路径  不在执行
        SeriesPath nowSeriesPath = null;
        SeriesPath currentGlobalSeriesPath = agvMessage.getCurrentGlobalSeriesPath();
        if(currentGlobalSeriesPath != null && currentGlobalSeriesPath.getUpCellNode() != null){

            LOG.info(" AGV("+agvMessage.getID()+")  podID扫描正确条件：currentAddressCodeID="+currentAddressCodeID+", getUpCellNode="+currentGlobalSeriesPath.getUpCellNode().getAddressCodeID()+", podCodeID="+podCodeID+", currentGlobalSeriesPath.getUpPodID()="+currentGlobalSeriesPath.getUpPodID()+", currentGlobalSeriesPath="+currentGlobalSeriesPath);

            if(currentAddressCodeID == currentGlobalSeriesPath.getUpCellNode().getAddressCodeID()
                    && podCodeID == currentGlobalSeriesPath.getUpPodID()){
                nowSeriesPath = getNextGlobalSeriesPath();
                podCounts = 0;
                LOG.info(" AGV("+getID()+")  路径获取成功:"+nowSeriesPath);
            }else{
                if(podCounts < 20){
                    podCounts++;
                    return;
                }
                LOG.error("pod 扫描错误 停在此位置  ！！！");
                currentGlobalSeriesPath.setUpCellNode(null);
                seriesPathLinkedList.clear();
                podCounts = 0;
                return;
            }
        }else{
            nowSeriesPath = getNextGlobalSeriesPath();
        }
        // mingchun.mu@mushiny.com   -----------------


        if (nowSeriesPath != null) {
            if (globalPathTargetCellNode != null && currentGlobalSeriesPath!=null) {
//                LOG.info("从当前路径中移除终点（"+globalPathTargetCellNode.getAddressCodeID()+"） --> 当前路径："+currentGlobalSeriesPath);
                globalPathTargetCellNode.setNoInGlobalPath(currentGlobalSeriesPath);

                // mingchum.mu@mushiny.com 到达充电路径终点， 响应充电装 - 准备对接
                List<RobotAction> robotActionList = globalPathTargetCellNode.getRobotActionList(currentGlobalSeriesPath);
                for(RobotAction robotAction: robotActionList){
                    if(robotAction instanceof Charge30Action){
                        for(AGVListener agvListener: agvListenerList){
                            agvListener.onArrivedChargingPile(this, globalPathTargetCellNode.getAddressCodeID());
                        }
                        break;
                    }
                }


            }
            if(globalPathTargetSecondCellNode!=null && currentGlobalSeriesPath!=null) {

                if(RotateAreaManager.getInstance().getRotateAreaByCellNode(globalPathTargetSecondCellNode) != null){
//                    LOG.info("解锁旋转区的出口点（"+globalPathTargetSecondCellNode.getAddressCodeID()+"） --> 当前路径："+currentGlobalSeriesPath);
                    globalPathTargetSecondCellNode.setUnLocked_MapLock(currentGlobalSeriesPath);
                }
//                LOG.info("从当前路径中移除倒数第二点（"+globalPathTargetSecondCellNode.getAddressCodeID()+"） --> 当前路径："+currentGlobalSeriesPath);
                if (globalPathTargetAddressCodeID == currentAddressCodeID) {
                    if(this.equals(globalPathTargetSecondCellNode.getNowLockedAGV())){
                        globalPathTargetSecondCellNode.setUnLocked_MapLock(currentGlobalSeriesPath);
                    }
                }
                globalPathTargetSecondCellNode.setNoInGlobalPath(currentGlobalSeriesPath);


            }

            if(currentGlobalSeriesPath != null && currentGlobalSeriesPath.getPathList() != null && currentGlobalSeriesPath.getPathList().size() > 0){
                for(CellNode cellNode: currentGlobalSeriesPath.getPathList()){
                    cellNode.setNoInGlobalPath(currentGlobalSeriesPath);
                }
            }


            setCurrentGlobalPath(nowSeriesPath);
            isRunGlobalSeriesPath = true;
        }
    }*/


}