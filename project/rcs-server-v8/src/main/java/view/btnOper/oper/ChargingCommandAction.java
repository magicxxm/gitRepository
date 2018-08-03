package view.btnOper.oper;


import com.mushiny.kiva.map.MapManager;
import view.AutoPathView;
import view.PathConfigMainServer;
import view.bean.PathBean;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Laptop-6 on 2017/12/23.
 */
public class ChargingCommandAction extends DefaultButtonAction {

    private Pattern pathPattern;
    private String pathRex = "^(\\s*[1-9][0-9]*\\s*,)+(\\s*[1-9][0-9]*\\s*)$"; // 路径匹配正则表达式

    public ChargingCommandAction(AutoPathView autoPathView) {
        super(autoPathView);
        init();
    }
    private void init(){
        pathPattern = Pattern.compile(pathRex);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(MapManager.getInstance().getMap() == null){
            autoPathView.printLogAndClearLogBySize("请先配置地图 & 启动服务！");
            return;
        }
        long robotId;
        long upAddressCodeId = 0;
        long downAddressCodeId = 0;
        long stopAddressCodeId = 0;
        long waitTime = 0;
        int chargerToward = 0;
        String pathText = autoPathView.getPathConfigPanel().getPathText().getText().trim();
        String robotIdText = autoPathView.getPathConfigPanel().getRobotIdText().getText().trim();
        String upAddressCodeIdText = autoPathView.getPathConfigPanel().getUpAddressCodeIdText().getText().trim();
        String downAddressCodeIdText = autoPathView.getPathConfigPanel().getUpAddressCodeIdText().getText().trim();
        String stopAddressCodeIdText = autoPathView.getPathConfigPanel().getStopAddressCodeIdText().getText().trim();
        String waitTimeText = autoPathView.getPathConfigPanel().getStopTimeText().getText().trim();
        String chargerTowardText = autoPathView.getPathConfigPanel().getChargerTowardText().getText().trim();
        if(!pathPattern.matcher(pathText).matches()){
            autoPathView.printLogAndClearLogBySize("输入路径格式不正确，正确实例: 1,2,3,4！");
            return;
        }
        if(!intPattern.matcher(robotIdText).matches()){
            autoPathView.printLogAndClearLogBySize("请输入正确的‘小车ID’！");
            return;
        }
        if(!intPattern.matcher(upAddressCodeIdText).matches()){
            upAddressCodeId = 0;
        }else{
            upAddressCodeId = Long.parseLong(upAddressCodeIdText);
        }
        if(!intPattern.matcher(downAddressCodeIdText).matches()){
            downAddressCodeId = 0;
        }else{
            downAddressCodeId = Long.parseLong(downAddressCodeIdText);
        }
        if(!intPattern.matcher(stopAddressCodeIdText).matches()){
            stopAddressCodeId = 0;
        }else{
            stopAddressCodeId = Long.parseLong(stopAddressCodeIdText);
        }
        if(!intPattern.matcher(waitTimeText).matches()){
            waitTime = 0;
        }else{
            waitTime = Long.parseLong(waitTimeText);
        }
        try {
            chargerToward = Integer.parseInt(chargerTowardText.trim());
        } catch (NumberFormatException e1) {
            chargerToward = -1;
            e1.printStackTrace();
        }
        robotId = Long.parseLong(robotIdText);
        PathBean pathBean = new PathBean();
        pathBean.setRobotId(robotId);
        pathBean.setUpAddressCodeId(upAddressCodeId);
        pathBean.setDownAddressCodeId(downAddressCodeId);
        pathBean.setRotateTheta(chargerToward);
        List<Long> pathList = new LinkedList<>();
        String[] pathArr = pathText.trim().split(",");
        for(String addr : pathArr){
            pathList.add(Long.parseLong(addr.trim()));
        }
        pathBean.setPathList(pathList);
        PathConfigMainServer.getInstance().sendChargingPath(pathBean);
        autoPathView.printLogAndClearLogBySize("发送充电路径:"+pathBean);
    }

}
