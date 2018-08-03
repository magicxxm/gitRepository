package view.btnOper.oper;


import com.aricojf.platform.mina.common.MinaConfig;
import view.AutoPathView;
import view.PathConfigMainServer;

import java.awt.event.ActionEvent;

/**
 * Created by Laptop-6 on 2017/12/23.
 */
public class ConfigMapCommandAction extends DefaultButtonAction {

    public ConfigMapCommandAction(AutoPathView autoPathView) {
        super(autoPathView);
    }
    private PathConfigMainServer pathConfigMainServer = PathConfigMainServer.getInstance();
    @Override
    public void actionPerformed(ActionEvent e) {
        int row = 0;
        int col = 0;
        String rowText = autoPathView.getTopPanel().getRowText().getText();
        String colText = autoPathView.getTopPanel().getColText().getText();
        if(!intPattern.matcher(rowText).matches()){
            autoPathView.printLogAndClearLogBySize("请输入正确的‘行数’！");
            return;
        }
        if(!intPattern.matcher(colText).matches()){
            autoPathView.printLogAndClearLogBySize("请输入正确的‘列数’！");
            return;
        }
        row = Integer.parseInt(rowText);
        col = Integer.parseInt(colText);
        if(pathConfigMainServer.getKivaServer() != null){
            pathConfigMainServer.stopServer();
            autoPathView.printLogAndClearLogBySize("停止服务(ip="+ MinaConfig.HOST_NAME+", port="+MinaConfig.PORT+")！");
        }
        pathConfigMainServer.setRow(row);
        pathConfigMainServer.setCol(col);
        autoPathView.printLogAndClearLogBySize("加载地图配置（row="+row+", col="+col+"）！");
        pathConfigMainServer.startServer();
        autoPathView.printLogAndClearLogBySize("服务启动(ip="+ MinaConfig.HOST_NAME+", port="+MinaConfig.PORT+")！");
    }
}
