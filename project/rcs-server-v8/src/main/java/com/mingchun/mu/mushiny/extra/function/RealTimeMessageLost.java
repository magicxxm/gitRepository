package com.mingchun.mu.mushiny.extra.function;

import com.aricojf.platform.mina.message.robot.OnReceiveAGVRTMessageListener;
import com.aricojf.platform.mina.message.robot.RobotRTMessage;
import com.aricojf.platform.mina.server.ServerMessageService;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;
import mq.MQManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * 实时包丢失打印
 *
 * Created by Laptop-6 on 2017/12/15.
 */
public class RealTimeMessageLost implements OnReceiveAGVRTMessageListener {
    private static RealTimeMessageLost instance = null;
    private Logger LOG = LoggerFactory.getLogger(RealTimeMessageLost.class.getName());
    private Map<KivaAGV, List<Address>> lostAddressMap = new HashMap<>();

    private RealTimeMessageLost() {
        ServerMessageService.getInstance().registeAGVRTMessageListener(this);
    }

    private synchronized static void initInstance(){
        if(instance == null){
            instance = new RealTimeMessageLost();
        }
    }

    public static RealTimeMessageLost getInstance() {
        if(instance == null){
            initInstance();
        }
        return instance;
    }

    private List<AddressCodeIdLostListener> addressCodeIdLostListenerList = new CopyOnWriteArrayList<>();

    @Override
    public void onReceivedAGVRTMessage(RobotRTMessage data) {
        data.toObject();
        KivaAGV kivaAGV = AGVManager.getInstance().getAGVByID(data.getRobotID());
        List<Address> addressList = lostAddressMap.get(kivaAGV);
        if(kivaAGV != null && addressList != null && addressList.size() > 0){
            long addressCodeId = data.getAddressCodeID();
            List<Address> lostAddressList = new ArrayList<>(); // 丢码容器
            if(addressList.contains(addressCodeId)){
                for(int i = 0, len = addressList.size(); i < len; i++){
                    Address address = addressList.get(i);
                    if(addressCodeId == address.getAddressCodeId()){
                        addressList.remove(i); // 移除当前不丢失的地址码 - 之后跳出， list大小变化不会影响循环
                        break;
                    }
                    lostAddressList.add(address);
                }
            }
            if(lostAddressList.size() > 0){
                String lostStr = "";
                for(Address address : lostAddressList){
                    if(!"".equals(lostStr)){
                        lostStr += ",";
                    }
                    lostStr += address.getAddressCodeId();
                }
                String lostMsg = "AGV("+kivaAGV.getID()+")丢码:["+lostStr+"]";
                LOG.error(lostMsg);
                fireAddressCodeIdLostListener(kivaAGV.getID(), lostMsg);
                addressList.removeAll(lostAddressList);
            }
            if(kivaAGV.isTaskTimeout()){
                String lostAddr = "";
                for(int i = 0, len = addressList.size(); i < len; i++){
                    Address address = addressList.get(i);
                    if(!"".equals(lostAddr)){
                        lostAddr += ", " + address.getAddressCodeId();
                    }else{
                        lostAddr += address.getAddressCodeId();
                    }
                }
                String lostMsg = "AGV("+kivaAGV.getID()+")可能丢掉的码："+lostAddr;
                LOG.error(lostMsg);
                fireAddressCodeIdLostListener(kivaAGV.getID(), lostMsg);
            }
        }
    }

    public void putAGVGlobalSeriesPath(KivaAGV kivaAGV, SeriesPath currentGlobalSeriesPath){
        if(kivaAGV == null){
            return;
        }
        if(currentGlobalSeriesPath == null){
            return;
        }
        if(currentGlobalSeriesPath.getPathList() == null){
            return;
        }
        if(currentGlobalSeriesPath.getPathList().size() == 0){
            return;
        }
        List<CellNode> globalSeriesPath = currentGlobalSeriesPath.getPathList();
        List<Address> addressList = new ArrayList<>();
        for(CellNode cellNode : globalSeriesPath){
            Address address = new Address();
            address.setAddressCodeId(cellNode.getAddressCodeID());
            addressList.add(address);
        }
        lostAddressMap.put(kivaAGV, addressList);
    }

    public void registerAddressCodeIdLostListener(AddressCodeIdLostListener addressCodeIdLostListener){
        if(addressCodeIdLostListener == null){
            return;
        }
        addressCodeIdLostListenerList.add(addressCodeIdLostListener);
    }
    public void removeAddressCodeIdLostListener(AddressCodeIdLostListener addressCodeIdLostListener){
        if(addressCodeIdLostListener == null){
            return;
        }
        addressCodeIdLostListenerList.remove(addressCodeIdLostListener);
    }
    public void fireAddressCodeIdLostListener(long robotID, String lostMsg){
        if(addressCodeIdLostListenerList == null
                || addressCodeIdLostListenerList.size() == 0){
            return;
        }
        for(AddressCodeIdLostListener addressCodeIdLostListener : addressCodeIdLostListenerList){
            addressCodeIdLostListener.onAddressCodeIdLostListener(robotID, lostMsg);
        }
    }




}
