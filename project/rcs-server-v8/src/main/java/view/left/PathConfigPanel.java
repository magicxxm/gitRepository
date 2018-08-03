package view.left;




import view.ComponentConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Laptop-6 on 2017/11/16.
 */
public class PathConfigPanel extends JPanel {
    private String title = "路径配置";
    public PathConfigPanel() {
        this.setBorder(BorderFactory.createTitledBorder(title));
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        JPanel contentPanel = new JPanel();
        this.add(contentPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        layout.setConstraints(contentPanel, constraints);
        placeComponents(contentPanel);
    }



    private JTextField pathText;
    private JTextField robotIdText;
    private JTextField upAddressCodeIdText;
    private JTextField downAddressCodeIdText;
    private JTextField stopAddressCodeIdText;
    private JTextField stopTimeText;
    private JTextField chargerTowardText;
    private JTextField podCodeIdText;
    private JTextField rotationAddressCodeIdText;

    private JButton configCommand;
    private JButton readCommand;
    private JButton clearCommand;
    private JButton sendCommand;
    private JButton autoCircleSendCommand;
    private JButton endSendCommand;
    private JButton chargingCommand;


    private JLabel pathLabel;
    private JLabel robotIdLabel;
    private JLabel upAddressCodeIdLabel;
    private JLabel downAddressCodeIdLabel;
    private JLabel stopAddressCodeIdLabel;
    private JLabel stopTimeLabel;
    private JLabel podCodeIdLabel;
    private JLabel rotationAddressCodeIdLabel;

    private JLabel chargerTowardLabel;

    private void placeComponents(JPanel contentPanel) {
        GridBagLayout layout = new GridBagLayout();
        contentPanel.setLayout(layout);

        //  --------------------------------------------------
        pathLabel = new JLabel("路径:");
        pathLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(pathLabel);
        pathText = new JTextField("13,14,15,12,9,6,3,2,1,4,7,10,13");
        pathText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(pathText);
        robotIdLabel = new JLabel("小车ID:");
        robotIdLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(robotIdLabel);
        robotIdText = new JTextField("25");
        robotIdText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(robotIdText);
        //  --------------------------------------------------

        //  --------------------------------------------------
        upAddressCodeIdLabel = new JLabel("举升点:");
        upAddressCodeIdLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(upAddressCodeIdLabel);
        upAddressCodeIdText = new JTextField("13");
        upAddressCodeIdText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(upAddressCodeIdText);

        downAddressCodeIdLabel = new JLabel("下降点:");
        downAddressCodeIdLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(downAddressCodeIdLabel);
        downAddressCodeIdText = new JTextField("13");
        downAddressCodeIdText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(downAddressCodeIdText);
        //  --------------------------------------------------

        //  --------------------------------------------------
        stopAddressCodeIdLabel = new JLabel("暂停点:");
        stopAddressCodeIdLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(stopAddressCodeIdLabel);
        stopAddressCodeIdText = new JTextField("0");
        stopAddressCodeIdText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(stopAddressCodeIdText);

        stopTimeLabel = new JLabel("暂停时间(s):");
        stopTimeLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(stopTimeLabel);
        stopTimeText = new JTextField("0");
        stopTimeText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(stopTimeText);

        chargerTowardLabel = new JLabel("旋转角度:");
        chargerTowardLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(chargerTowardLabel);
        chargerTowardText = new JTextField("180");
        chargerTowardText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(chargerTowardText);

        podCodeIdLabel = new JLabel("货架码:");
        podCodeIdLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(podCodeIdLabel);
        podCodeIdText = new JTextField("0");
        podCodeIdText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(podCodeIdText);


        rotationAddressCodeIdLabel = new JLabel("旋转点:");
        rotationAddressCodeIdLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(rotationAddressCodeIdLabel);
        rotationAddressCodeIdText = new JTextField("2");
        rotationAddressCodeIdText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(rotationAddressCodeIdText);
        //  --------------------------------------------------


        // 动作命令按钮
        configCommand = new JButton("配置");
        contentPanel.add(configCommand);
        readCommand = new JButton("回读");
        contentPanel.add(readCommand);
        clearCommand = new JButton("清空配置");
        contentPanel.add(clearCommand);
        autoCircleSendCommand = new JButton("自动循环发送");
        contentPanel.add(autoCircleSendCommand);
        sendCommand = new JButton("发送");
        contentPanel.add(sendCommand);
        endSendCommand = new JButton("结束循环路径发送");
        contentPanel.add(endSendCommand);
        chargingCommand = new JButton("充电");
        contentPanel.add(chargingCommand);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        int inset = ComponentConfig.INSECT * 2 / 5;
        constraints.insets = new Insets(inset,inset,inset,inset);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weighty = 0;
        layout.setConstraints(pathLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.gridwidth = 0;
        layout.setConstraints(pathText, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        layout.setConstraints(robotIdLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        layout.setConstraints(robotIdText, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        layout.setConstraints(upAddressCodeIdLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        layout.setConstraints(upAddressCodeIdText, constraints);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0;
        layout.setConstraints(downAddressCodeIdLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 0;
        constraints.weightx = 1;
        layout.setConstraints(downAddressCodeIdText, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        layout.setConstraints(stopAddressCodeIdLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        layout.setConstraints(stopAddressCodeIdText, constraints);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0;
        layout.setConstraints(stopTimeLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        layout.setConstraints(stopTimeText, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0;
        layout.setConstraints(chargerTowardLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 0;
        constraints.weightx = 1;
        layout.setConstraints(chargerTowardText, constraints);


        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        layout.setConstraints(podCodeIdLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        layout.setConstraints(podCodeIdText, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        layout.setConstraints(rotationAddressCodeIdLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 0;
        constraints.weightx = 1;
        layout.setConstraints(rotationAddressCodeIdText, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.gridwidth = 2;
        layout.setConstraints(configCommand, constraints);
        constraints.gridwidth = 1;
        layout.setConstraints(readCommand, constraints);
        constraints.gridwidth = 0;
        layout.setConstraints(clearCommand, constraints);


        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.gridwidth = 2;
        layout.setConstraints(autoCircleSendCommand, constraints);
        constraints.gridwidth = 1;
        layout.setConstraints(sendCommand, constraints);
        constraints.gridwidth = 0;
        layout.setConstraints(endSendCommand, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.gridwidth = 2;
        layout.setConstraints(chargingCommand, constraints);



    }

    public JTextField getPathText() {
        return pathText;
    }

    public void setPathText(JTextField pathText) {
        this.pathText = pathText;
    }

    public JTextField getRobotIdText() {
        return robotIdText;
    }

    public void setRobotIdText(JTextField robotIdText) {
        this.robotIdText = robotIdText;
    }

    public JTextField getUpAddressCodeIdText() {
        return upAddressCodeIdText;
    }

    public void setUpAddressCodeIdText(JTextField upAddressCodeIdText) {
        this.upAddressCodeIdText = upAddressCodeIdText;
    }

    public JTextField getDownAddressCodeIdText() {
        return downAddressCodeIdText;
    }

    public void setDownAddressCodeIdText(JTextField downAddressCodeIdText) {
        this.downAddressCodeIdText = downAddressCodeIdText;
    }

    public JTextField getStopAddressCodeIdText() {
        return stopAddressCodeIdText;
    }

    public void setStopAddressCodeIdText(JTextField stopAddressCodeIdText) {
        this.stopAddressCodeIdText = stopAddressCodeIdText;
    }

    public JTextField getStopTimeText() {
        return stopTimeText;
    }

    public void setStopTimeText(JTextField stopTimeText) {
        this.stopTimeText = stopTimeText;
    }

    public JButton getConfigCommand() {
        return configCommand;
    }

    public void setConfigCommand(JButton configCommand) {
        this.configCommand = configCommand;
    }

    public JButton getReadCommand() {
        return readCommand;
    }

    public void setReadCommand(JButton readCommand) {
        this.readCommand = readCommand;
    }

    public JLabel getPathLabel() {
        return pathLabel;
    }

    public void setPathLabel(JLabel pathLabel) {
        this.pathLabel = pathLabel;
    }

    public JLabel getRobotIdLabel() {
        return robotIdLabel;
    }

    public void setRobotIdLabel(JLabel robotIdLabel) {
        this.robotIdLabel = robotIdLabel;
    }

    public JLabel getUpAddressCodeIdLabel() {
        return upAddressCodeIdLabel;
    }

    public void setUpAddressCodeIdLabel(JLabel upAddressCodeIdLabel) {
        this.upAddressCodeIdLabel = upAddressCodeIdLabel;
    }

    public JLabel getDownAddressCodeIdLabel() {
        return downAddressCodeIdLabel;
    }

    public void setDownAddressCodeIdLabel(JLabel downAddressCodeIdLabel) {
        this.downAddressCodeIdLabel = downAddressCodeIdLabel;
    }

    public JLabel getStopAddressCodeIdLabel() {
        return stopAddressCodeIdLabel;
    }

    public void setStopAddressCodeIdLabel(JLabel stopAddressCodeIdLabel) {
        this.stopAddressCodeIdLabel = stopAddressCodeIdLabel;
    }

    public JLabel getStopTimeLabel() {
        return stopTimeLabel;
    }

    public void setStopTimeLabel(JLabel stopTimeLabel) {
        this.stopTimeLabel = stopTimeLabel;
    }

    public JButton getClearCommand() {
        return clearCommand;
    }

    public void setClearCommand(JButton clearCommand) {
        this.clearCommand = clearCommand;
    }

    public JButton getSendCommand() {
        return sendCommand;
    }

    public void setSendCommand(JButton sendCommand) {
        this.sendCommand = sendCommand;
    }

    public JButton getAutoCircleSendCommand() {
        return autoCircleSendCommand;
    }

    public void setAutoCircleSendCommand(JButton autoCircleSendCommand) {
        this.autoCircleSendCommand = autoCircleSendCommand;
    }

    public JButton getEndSendCommand() {
        return endSendCommand;
    }

    public void setEndSendCommand(JButton endSendCommand) {
        this.endSendCommand = endSendCommand;
    }

    public JTextField getChargerTowardText() {
        return chargerTowardText;
    }

    public void setChargerTowardText(JTextField chargerTowardText) {
        this.chargerTowardText = chargerTowardText;
    }

    public JLabel getChargerTowardLabel() {
        return chargerTowardLabel;
    }

    public void setChargerTowardLabel(JLabel chargerTowardLabel) {
        this.chargerTowardLabel = chargerTowardLabel;
    }

    public JButton getChargingCommand() {
        return chargingCommand;
    }

    public void setChargingCommand(JButton chargingCommand) {
        this.chargingCommand = chargingCommand;
    }

    public JTextField getPodCodeIdText() {
        return podCodeIdText;
    }

    public void setPodCodeIdText(JTextField podCodeIdText) {
        this.podCodeIdText = podCodeIdText;
    }

    public JTextField getRotationAddressCodeIdText() {
        return rotationAddressCodeIdText;
    }

    public void setRotationAddressCodeIdText(JTextField rotationAddressCodeIdText) {
        this.rotationAddressCodeIdText = rotationAddressCodeIdText;
    }

    public JLabel getPodCodeIdLabel() {
        return podCodeIdLabel;
    }

    public void setPodCodeIdLabel(JLabel podCodeIdLabel) {
        this.podCodeIdLabel = podCodeIdLabel;
    }

    public JLabel getRotationAddressCodeIdLabel() {
        return rotationAddressCodeIdLabel;
    }

    public void setRotationAddressCodeIdLabel(JLabel rotationAddressCodeIdLabel) {
        this.rotationAddressCodeIdLabel = rotationAddressCodeIdLabel;
    }
}
