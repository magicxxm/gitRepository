package view.btnOper.oper;


import com.mushiny.kiva.map.MapManager;
import view.AutoPathView;
import view.PathConfigMainServer;

import java.awt.event.ActionEvent;

/**
 * Created by Laptop-6 on 2017/12/23.
 */
public class AutoCircleSendCommandAction extends DefaultButtonAction {
    public AutoCircleSendCommandAction(AutoPathView autoPathView) {
        super(autoPathView);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(MapManager.getInstance().getMap() == null){
            autoPathView.printLogAndClearLogBySize("请先配置地图 & 启动服务！");
            return;
        }
        long robotId = 0;
        String robotIdText = autoPathView.getPathConfigPanel().getRobotIdText().getText().trim();
        if(!intPattern.matcher(robotIdText).matches()){
            autoPathView.printLogAndClearLogBySize("请输入正确的‘小车ID’！");
            return;
        }
        robotId = Long.parseLong(robotIdText);
        PathConfigMainServer pathConfigMainServer = PathConfigMainServer.getInstance();
        if(pathConfigMainServer.getAgvPathContainerMap() != null){
            if(pathConfigMainServer.getAgvPathContainerMap().get(robotId) != null){
                pathConfigMainServer.getAgvPathContainerMap().get(robotId).setAutoSendFlag(true);
                pathConfigMainServer.getAgvPathContainerMap().get(robotId).setExecutingPathFlag(0);
                pathConfigMainServer.sendPath(pathConfigMainServer.getAgvPathContainerMap().get(robotId).getPathList().get(0));
                autoPathView.printLogAndClearLogBySize("开始（给小车ID="+robotId+"）自动循环下发配置路径！");
            }else{
                autoPathView.printLogAndClearLogBySize("请先（给小车ID="+robotId+"）配置自动下发路径！");
            }
        }
    }

}
