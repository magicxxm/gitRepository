package view.btnOper.oper;


import view.AutoPathView;
import view.PathConfigMainServer;
import view.bean.ViewAGVPathContainer;

import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * Created by Laptop-6 on 2017/12/23.
 */
public class ClearPathConfigCommandAction extends DefaultButtonAction {
    public ClearPathConfigCommandAction(AutoPathView autoPathView) {
        super(autoPathView);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        long robotId;
        String robotIdText = autoPathView.getPathConfigPanel().getRobotIdText().getText().trim();
        if(!intPattern.matcher(robotIdText).matches()){
            autoPathView.printLogAndClearLogBySize("请输入正确的‘小车ID’！");
            return;
        }
        robotId = Long.parseLong(robotIdText);
        Map<Long, ViewAGVPathContainer> agvPathContainerMap = PathConfigMainServer.getInstance().getAgvPathContainerMap();
        agvPathContainerMap.put(robotId, null);
        autoPathView.printLogAndClearLogBySize("已将小车ID="+robotId+"的路径清空！");
    }

}
