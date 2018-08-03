package view;



import view.btnOper.ButtonActionHandler;
import view.left.LogPanel;
import view.left.PathConfigPanel;
import view.left.TopPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Laptop-6 on 2017/9/29.
 */
public class AutoPathView extends JFrame {

    private static AutoPathView instance = null;
    private synchronized static void initInstance(){
        if(instance == null){
            instance = new AutoPathView();
        }
    }
    public static AutoPathView getInstance() {
        if(instance == null){
            initInstance();
        }
        return instance;
    }

    private String title = "AGV路径配置自动下发工具@2.0  版权所有：苏州牧星智能科技有限公司（MUSHINY）";
    private JPanel contentPanel;
    private JPanel leftPane;
    private ButtonActionHandler buttonActionHandler;

    private AutoPathView(){
        this.setTitle(title);
        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        int frameHeight = (int)(ComponentConfig.MAIN_HEIGHT * 0.9);
        int frameWidth = (int)(frameHeight * 0.618);
        setSize(frameWidth, frameHeight);
        this.setLocationRelativeTo(null); // 居中


        contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        this.add(contentPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        layout.setConstraints(contentPanel, constraints);


        placeComponent(contentPanel);

        paintLeftPanel();

        buttonActionHandler = new ButtonActionHandler(this);


        panelRepaint();

    }

    // 三大面板布局
    private void placeComponent(JPanel contentPane) {
        GridBagLayout layout = new GridBagLayout();
        contentPane.setLayout(layout);

        leftPane = new JPanel();
//        leftPane.setPreferredSize(new Dimension(ComponentConfig.MAIN_WIDTH * 2 / 7, 0));
        leftPane.setBorder(BorderFactory.createLoweredBevelBorder());


        contentPane.add(leftPane);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        int inset = ComponentConfig.INSECT * 2 / 5;
        constraints.insets = new Insets(inset,inset,inset,inset);
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridwidth = 0;
        layout.setConstraints(leftPane, constraints);

    }


    private TopPanel topPanel; // 左上面板
    private PathConfigPanel pathConfigPanel; // 左上中面板
    private LogPanel logPanel; // 左下中面板


    // 左面板布局
    private void paintLeftPanel(){
        GridBagLayout layout = new GridBagLayout();
        leftPane.setLayout(layout);

        int leftHeight = ComponentConfig.MAIN_HEIGHT * 96 / 100;
        topPanel = new TopPanel();
        topPanel.setPreferredSize(new Dimension(0, leftHeight * 2 /17));
//        topPanel.setPreferredSize(new Dimension(0, leftHeight * 5 /34));
        leftPane.add(topPanel);
        pathConfigPanel = new PathConfigPanel();
        pathConfigPanel.setPreferredSize(new Dimension(0, leftHeight * 5 / 17));
        leftPane.add(pathConfigPanel);
        logPanel = new LogPanel();
        leftPane.add(logPanel);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        int inset = ComponentConfig.INSECT * 2 / 5;
        constraints.insets = new Insets(inset,inset,inset,inset);
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridheight = 3;
        constraints.gridwidth = 0;
        layout.setConstraints(topPanel, constraints);

        constraints.gridheight = 8;
        layout.setConstraints(pathConfigPanel, constraints);

        constraints.weighty = 1;
        layout.setConstraints(logPanel, constraints);


    }


    private void panelRepaint(){
        revalidate();
        repaint();
    }

    public void printLog(final String logInfo) {
        try{
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                    JTextArea logTextArea = AutoPathView.this.getLogPanel().getLogTextArea();
                    if(logTextArea != null){
                        if(logInfo != null && !("".equals(logInfo))){
                            logTextArea.append(logInfo);
                            logTextArea.append("\r\n");
                            logTextArea.paintImmediately(logTextArea.getBounds());
                        }
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void printLogAndClearLogBySize(String logInfo){
        JTextArea logTextArea = this.getLogPanel().getLogTextArea();
        long logSize = logTextArea.getText().length();
        if(logSize > 1024 * 1024){ // 日志超过1M需要清空
            this.clearLogs();
        }
        this.printLog(logInfo);
    }

    public void clearLogs() {
        this.getLogPanel().getLogTextArea().setText("");
    }




    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AutoPathView wcsTest = AutoPathView.getInstance();
//                autoPathView.authControl();
            }
        });

    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(JPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    public JPanel getLeftPane() {
        return leftPane;
    }

    public void setLeftPane(JPanel leftPane) {
        this.leftPane = leftPane;
    }

    public ButtonActionHandler getButtonActionHandler() {
        return buttonActionHandler;
    }

    public void setButtonActionHandler(ButtonActionHandler buttonActionHandler) {
        this.buttonActionHandler = buttonActionHandler;
    }

    public TopPanel getTopPanel() {
        return topPanel;
    }

    public void setTopPanel(TopPanel topPanel) {
        this.topPanel = topPanel;
    }

    public PathConfigPanel getPathConfigPanel() {
        return pathConfigPanel;
    }

    public void setPathConfigPanel(PathConfigPanel pathConfigPanel) {
        this.pathConfigPanel = pathConfigPanel;
    }

    public LogPanel getLogPanel() {
        return logPanel;
    }

    public void setLogPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
    }
}

