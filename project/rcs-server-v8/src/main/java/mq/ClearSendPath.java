package mq;

import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;
import com.mushiny.rcs.wcs.WCSSeriesPath;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Laptop-6 on 2017/10/23.
 */
public class ClearSendPath {


    public void test(){
        sendPath(1, 0, 0, true, 90, "8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24", 89);
    }



    public void sendPath(long robotID, long podUpAddress, long podDownAddress, boolean isRotatePod, int rotateTheta, String seriesPath, int podCodeID){
        String[] seriesPath_ = seriesPath.split(",");
        List<Long> seriesPathList = new LinkedList<>();
        for(String temp: seriesPath_){
            seriesPathList.add(Long.parseLong(temp.trim()));
        }
    }



    public void sendPath(long robotID, long podUpAddress, long podDownAddress, boolean isRotatePod, int rotateTheta, List<Long> seriesPath, int podCodeID){
        Long robotid = robotID;
        long upOrDownPod = podUpAddress;
        Long podAddressCodeID = podDownAddress;
        KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotid);
        if (null != curAGV)
        {
            curAGV.clearBufferSP();
            WCSSeriesPath wcsSeriesPath = new WCSSeriesPath(upOrDownPod, podAddressCodeID, isRotatePod, rotateTheta, new LinkedList<>(seriesPath));
            wcsSeriesPath.setPodCodeID(podCodeID);// 设置路径中小车举升或下降的podID
            wcsSeriesPath.setAgvID(curAGV.getID());
            if (null == wcsSeriesPath){
            }
            else{
                if (wcsSeriesPath.checkWCSSeriesPath())                {
                    curAGV.putWCSSeriesPath(wcsSeriesPath);
                }
                else {
                }
            }
        }
    }







}
