package view.left;




import view.ComponentConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Laptop-6 on 2017/11/16.
 */
public class TopPanel extends JPanel {
    private String title = "地图信息";
    public TopPanel() {
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



    private JTextField rowText;
    private JTextField colText;
    private JButton configCommand;

    private JLabel rowLabel;
    private JLabel colLabel;
    private void placeComponents(JPanel contentPanel) {
        GridBagLayout layout = new GridBagLayout();
        contentPanel.setLayout(layout);

        rowLabel = new JLabel("行数:");
        rowLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(rowLabel);
        rowText = new JTextField("5");
        rowText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(rowText);
        colLabel = new JLabel("列数:");
        colLabel.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(colLabel);
        colText = new JTextField("3");
        colText.setFont(ComponentConfig.COMMON_FONT);
        contentPanel.add(colText);

        // 动作命令按钮
        configCommand = new JButton("配置地图 & 启动服务");
        contentPanel.add(configCommand);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        int inset = ComponentConfig.INSECT * 2 / 5;
        constraints.insets = new Insets(inset,inset,inset,inset);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weighty = 0;
        layout.setConstraints(rowLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        layout.setConstraints(rowText, constraints);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0;
        layout.setConstraints(colLabel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 0;
        constraints.weightx = 1;
        layout.setConstraints(colText, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1;
        constraints.gridwidth = 0;
        layout.setConstraints(configCommand, constraints);

    }

    public JTextField getRowText() {
        return rowText;
    }

    public void setRowText(JTextField rowText) {
        this.rowText = rowText;
    }

    public JTextField getColText() {
        return colText;
    }

    public void setColText(JTextField colText) {
        this.colText = colText;
    }

    public JButton getConfigCommand() {
        return configCommand;
    }

    public void setConfigCommand(JButton configCommand) {
        this.configCommand = configCommand;
    }

    public JLabel getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(JLabel rowLabel) {
        this.rowLabel = rowLabel;
    }

    public JLabel getColLabel() {
        return colLabel;
    }

    public void setColLabel(JLabel colLabel) {
        this.colLabel = colLabel;
    }
}
