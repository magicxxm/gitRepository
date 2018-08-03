package view.btnOper;


import view.AutoPathView;
import view.btnOper.oper.*;

/**
 * Created by Laptop-6 on 2017/11/17.
 */
public class ButtonActionHandler {
    private AutoPathView autoPathView;
    public ButtonActionHandler(AutoPathView wcsTest) {
        this.autoPathView = wcsTest;
        init();
    }

    private void init(){
        this.autoPathView.getTopPanel().getConfigCommand().addActionListener(new ConfigMapCommandAction(autoPathView));
        this.autoPathView.getPathConfigPanel().getConfigCommand().addActionListener(new PathConfigCommandAction(autoPathView));
        this.autoPathView.getPathConfigPanel().getReadCommand().addActionListener(new ReadPathConfigCommandAction(autoPathView));
        this.autoPathView.getPathConfigPanel().getClearCommand().addActionListener(new ClearPathConfigCommandAction(autoPathView));
        this.autoPathView.getPathConfigPanel().getSendCommand().addActionListener(new SendPathCommandAction(autoPathView));
        this.autoPathView.getPathConfigPanel().getAutoCircleSendCommand().addActionListener(new AutoCircleSendCommandAction(autoPathView));
        this.autoPathView.getPathConfigPanel().getEndSendCommand().addActionListener(new EndSendCommandAction(autoPathView));
        this.autoPathView.getPathConfigPanel().getChargingCommand().addActionListener(new ChargingCommandAction(autoPathView));
    }





    public AutoPathView getAutoPathView() {
        return autoPathView;
    }

    public void setAutoPathView(AutoPathView autoPathView) {
        this.autoPathView = autoPathView;
    }
}
