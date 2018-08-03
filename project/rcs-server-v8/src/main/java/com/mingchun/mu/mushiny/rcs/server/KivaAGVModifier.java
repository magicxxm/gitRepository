package com.mingchun.mu.mushiny.rcs.server;

import com.aricojf.platform.mina.common.MachineInterface;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Laptop-6 on 2017/9/20.
 *
 * minghcun.mu@mushiny.com
 *
 */
public class KivaAGVModifier {
    private static Logger LOG = LoggerFactory.getLogger(KivaAGVModifier.class.getName());

    public KivaAGVModifier() {
    }

    /**
     * 重新登陆检查  session断开重连检查 --> 都将产生新session
     *
     * 检测原来存在且处于AGV_STATUS_NO_CONNECTION状态的AGV,如果存在则合并AGV对象
     * 说明此AGV是新断开后的连接;如果没有检测到则说明是新连接.
     * 重连连接后的一些影响:
     * 重新连接后,如果AGV所在的最新地址码在断开之前下发锁格路径范围之内:则继续执行
     * 原来的路径,如果不在范围之内,则无法也不进行处理任何路径.


     此方法被调用于下情况:
     检测到心跳包\实时数据包


     * @param machine
     * @param addressCodeID
     */
    public synchronized void checkAGVReConnection(MachineInterface machine, long addressCodeID, IoSession session, KivaAGV kivaAGV) {
        /*if(kivaAGV.getCurrentAddressCodeID() == 0){  //不在地址码上也能重复登陆
            return;
        }*/
        /*if(kivaAGV.getAGVStatus() != AGVConfig.AGV_STATUS_NO_CONNECTION){  //？？？ 出现未合并就已赋予任务状态
            return;
        }*/
        if(kivaAGV.getSession().equals(session)){
            return;
        }
        boolean isSendSendedPath = false;// 是否继续发送之前的路径
        if(kivaAGV.getCurrentAddressCodeID() == addressCodeID){
            isSendSendedPath = true;
        }
        LOG.info("AGV("+kivaAGV.getID()+")重新连接并合并！");
        kivaAGV.setSession(session);// 更改现存agv的 session
        kivaAGV.setRtTimeout(false); // 实时包收到
        kivaAGV.setCurrentAddressCodeID(addressCodeID); //当前地址码设置
        LOG.info("位置改变发送:previousAddressCodeID="+kivaAGV.getPreviousAddressCodeID()+", currentAddressCodeID="+kivaAGV.getCurrentAddressCodeID());
        CellNode preCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(kivaAGV.getPreviousAddressCodeID());

        if(preCellNode != null){ // 前一个地址码不在地图上，不用触发为之改变解锁
            if(kivaAGV.equals(preCellNode.getNowLockedAGV())){
                kivaAGV.fireOnAGVPositionChange(kivaAGV.getPreviousAddressCodeID(), kivaAGV.getCurrentAddressCodeID()); // 位置改变发送
            }else{ // 重新登录锁住当前格子
                kivaAGV.fireOnAGVPositionChange(0, kivaAGV.getCurrentAddressCodeID()); // 位置改变发送
            }
        }
        kivaAGV.setAGVStatusOnTCPConnectionReOpen(); // 恢复agv之前的状态

        AGVManager.getInstance().removeAGVFromAGVList((KivaAGV) machine); // 移除重连agv
        kivaAGV.fireOnAGVRepeatConnection2RCS(); // 触发重连

        //确认最新的位置是否在"最后一次锁格路径范围之内",如果"在"则不预警,继续之前的状态;
        //如果"不在"
        //则上报WCS,且"清空"本AGV所有路径缓冲
        if (kivaAGV.getLastSendedSeriesPath() != null) {
            boolean isInLockedPath = false; // 是否在锁定路径中
            for (CellNode cell : kivaAGV.getLastSendedSeriesPath().getPathList()) {
                if (cell.getAddressCodeID() == kivaAGV.getCurrentAddressCodeID()) {
                    if(kivaAGV.getCurrentGlobalSeriesPath() != null){
                        kivaAGV.unlockCellFromSP();//原来是路径执行过程中,断开的->解锁
                        isInLockedPath = true;
                    }
                }
            }
            if (!isInLockedPath) {
                LOG.error("####AGV重新连接后，位置异常，上报WCS,RCS清除所有任务缓存！！");
                // 解锁之前的路径
                for(CellNode cellNode: kivaAGV.getLastSendedSeriesPath().getPathList()){
                    if(cellNode.getAddressCodeID() != kivaAGV.getCurrentAddressCodeID()){
                        if(kivaAGV.equals(cellNode.getNowLockedAGV())){
                            cellNode.setUnLocked();
                        }
                    }
                }
                kivaAGV.setStopSendSeriesPathFlag(false); // agv可以继续发送路径
                kivaAGV.clearRCSBufferPathCommand(); // 清除缓冲路径

                kivaAGV.fireOnAGVRequestWCSPath(); // 重连位置错误清空路径后，需要重新请求路径


                kivaAGV.setAGVStatus(AGVConfig.AGV_STATUS_REPEAT_CONNECTION_ADD_ERROR);
                kivaAGV.fireOnAGVRepeatConnection2RCS_PositionError();
            } else {
                kivaAGV.agvGlobalCheck();
                if(kivaAGV.getCurrentGlobalSeriesPath() != null && isSendSendedPath){ // kivaAGV.getCurrentGlobalSeriesPath()清空代表已经解锁，不用下发路径
                    LOG.warn("####AGV("+kivaAGV.getID()+")重新连接后，继续执行原来的任务 -- 原来任务："+kivaAGV.getLastSendedSeriesPath());
                    kivaAGV.sendSeriesPath(kivaAGV.getLastSendedSeriesPath());
                }
                kivaAGV.setAGVStatusOnTCPConnectionReOpen();
            }
        }
    }


    public void check(){}





}
