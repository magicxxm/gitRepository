package test.scanPod;

import com.aricojf.platform.mina.message.robot.*;
import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseExceptionMessage;
import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseMessage;
import com.mingchun.mu.aricojf.platform.mina.message.robot.media.error.Robot2RCSMidiaErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Laptop-6 on 2017/10/11.
 */
public class ScanPodTest implements OnReceiveAGVAllMessageListener {

    private Logger LOG = LoggerFactory.getLogger(ScanPodTest.class.getName());



    @Override
    public void onReceivedAGVRTMessage(RobotRTMessage data) {

    }

    @Override
    public void onReceivedAGVErrorMessage(RobotErrorMessage data) {

    }

    @Override
    public void onReceivedAGVLoginMessage(RobotLoginRequestMessage data) {

    }

    @Override
    public void onReceivedAGVStatusMessage(RobotStatusMessage data) {

    }

    @Override
    public void onReceivedAGVHeartBeatMessage(RobotHeartBeatRequestMessage data) {

    }

    @Override
    public void onReceivedAGVActionCommandResponseMessage(Robot2RCSActionCommandResponseMessage data) {

    }

    @Override
    public void onReceiveRobot2RCSActionFinishedMessageListener(Robot2RCSActionFinishedMessage data) {
        data.toObject();
        if(data.getActedType() == 4){
            LOG.info("货架码："+data.getPodCodeID()+",\t货架A面朝向："+data.getPodAfaceToward()+",\t地址码："+data.getAddressCodeID());
        }
    }

    @Override
    public void OnReceiveRobot2RCSResponseMessage(Robot2RCSResponseMessage data) {

    }

    @Override
    public void OnReceiveRobot2RCSResponseExceptionMessage(Robot2RCSResponseExceptionMessage data) {

    }

    @Override
    public void onReceiveRobot2RCSMediaErrorMessageListener(Robot2RCSMidiaErrorMessage data) {

    }

    @Override
    public void onReceiveRobot2RCSResponseConfigMessageListener(RobotResponseConfigMessage data) {

    }
}
