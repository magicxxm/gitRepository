package view.left;


import javax.swing.*;
import java.awt.*;

/**
 * Created by Laptop-6 on 2017/11/16.
 */
public class LogPanel extends JPanel {
    private String title = "日志信息";
    public LogPanel() {
        this.setBorder(BorderFactory.createTitledBorder(title));
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        JPanel contentPanel = new JPanel();
        this.add(contentPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 0;
        constraints.gridheight = 0;
        layout.setConstraints(contentPanel, constraints);
        placeComponents(contentPanel);
    }


    private JTextArea logTextArea;
    private void placeComponents(JPanel contentPanel) {
        GridBagLayout layout = new GridBagLayout();
        contentPanel.setLayout(layout);
        logTextArea = new JTextArea();
        logTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPanel.add(scrollPane);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridwidth = 0;
        constraints.gridheight = 0;

        layout.setConstraints(scrollPane, constraints);

    }

    public JTextArea getLogTextArea() {
        return logTextArea;
    }

    public void setLogTextArea(JTextArea logTextArea) {
        this.logTextArea = logTextArea;
    }
}
