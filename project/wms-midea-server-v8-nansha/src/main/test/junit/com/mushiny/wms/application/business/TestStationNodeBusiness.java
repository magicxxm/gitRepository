package junit.com.mushiny.wms.application.business;

import com.mushiny.ServerApplication;
import com.mushiny.wms.application.business.common.StationNodeBusiness;
import com.mushiny.wms.application.domain.Stationnode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * @program: wms-midea-server
 * @description: 业务类测试
 * @author: mingchun.mu@mushiny.com
 * @create: 2018-07-10 19:40
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=ServerApplication.class)
@WebAppConfiguration
public class TestStationNodeBusiness {

    @Autowired
    private StationNodeBusiness stationNodeBusiness;

    @Test
    public void test(){
        List<Stationnode> list = stationNodeBusiness.getAllStationNode();
        System.out.println(" -- size  -- ="+list.size());
    }




}
