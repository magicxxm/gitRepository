package com.mushiny;

import com.mushiny.business.WebApiBusiness;
import com.mushiny.mq.ISender;
import com.mushiny.mq.MessageSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tank.li on 2017/7/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WcsApplication.class)
public class MqTest {
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private WebApiBusiness webApiBusiness;
    //@Test
    public void testSend(){
        Map data = new HashMap();
        data.put("robotID",1);
        data.put("time",System.currentTimeMillis());
        data.put("sectionID","1");
        data.put("password",1l);
        data.put("addressCodeID",1l);
        data.put("addressCodeInfoX",1);
        data.put("addressCodeInfoY",1);
        data.put("addressCodeInfoTheta",90);
        data.put("laveBattery",90);
        data.put("podCodeID",10001);

        messageSender.sendMsg(ISender.EXCHANGE,"AGV_REPEAT_CONNECT2_RCS_POSITION_ERROR",data);
        System.out.println("消息发送:"+data);


    }

    @Test
    public void testWebApi(){
        ///path-planning/heavy-drive/path?warehouseId=DEFAULT&sectionId=ec229eb7-7e2b-43a8-b1c7-91bd807e91cf&sourceVertex=34&targetVertex=505
        //System.out.println(webApiBusiness.getHeavyPath("DEFAULT","ec229eb7-7e2b-43a8-b1c7-91bd807e91cf",34,505));
        System.out.println(webApiBusiness.getEmptyPath("DEFAULT","ec229eb7-7e2b-43a8-b1c7-91bd807e91cf",17,33));
    }

    /*  机器ID	Long	robotID
        sectionID	Long	sectionID
        IP	String	ip
        登录时间(秒)-时间戳	Long	loginTime
        密码	Long	password
        机器型号	Short	robotType
        硬件版本	Short	hardwareVersion
        软件版本	Short	softwareVersion
        出厂日期	Long	manufactureDate
        累计时长	Long	TotalTime
        最近维修时间	Long	recentFixDate
        剩余电量	int	laveBattery
        地址码ID	Long	addressCodeID
        地址码坐标X 	Short	addressCodeInfoX
        地址码坐标Y	Short	addressCodeInfoY
        地址码偏移角	Float	addressCodeInfoTheta
        货架ID	Long	podCodeID
        货架x坐标	Short	podCodeInfoX
        货架y坐标	Short	podCodeInfoY
        货架偏移角	Float	podCodeInfoTheta
        所选货架ID	Long	selectedPodCodeID
        举升状态（0：放下1：举起）	Short	upStatus
        充电状态（0：未充电 1：充电 ）	Short	chargeStatus
        错误ID	int	errorID
        信号质量	Short	signalQuality
        入侵检测次数	Int	InvasionDetection
        冷启动	Int	coolReset
        热启动	int	hotReset*/
}
