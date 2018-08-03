package view.btnOper.oper;


import view.AutoPathView;
import view.PathConfigMainServer;

import java.awt.event.ActionEvent;

/**
 * Created by Laptop-6 on 2017/12/23.
 */
public class EndSendCommandAction extends DefaultButtonAction {
    public EndSendCommandAction(AutoPathView autoPathView) {
        super(autoPathView);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long robotId = 0;
        String robotIdText = autoPathView.getPathConfigPanel().getRobotIdText().getText().trim();
        if(!intPattern.matcher(robotIdText).matches()){
            autoPathView.printLogAndClearLogBySize("请输入正确的‘小车ID’！");
            return;
        }
        robotId = Long.parseLong(robotIdText);
        if(PathConfigMainServer.getInstance().getAgvPathContainerMap() != null
                && PathConfigMainServer.getInstance().getAgvPathContainerMap().get(robotId) != null){
            PathConfigMainServer.getInstance().getAgvPathContainerMap().get(robotId).setAutoSendFlag(false); // 停止下次循环路径自动发送
            autoPathView.printLogAndClearLogBySize("已经停止（小车ID="+robotId+"）下次循环路径自动发送！");
        }else{
            autoPathView.printLogAndClearLogBySize("（小车ID="+robotId+"）下次循环路径自动发送");
        }
    }

}
