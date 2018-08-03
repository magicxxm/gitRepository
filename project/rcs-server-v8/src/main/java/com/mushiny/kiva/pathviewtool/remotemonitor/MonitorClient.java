/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.pathviewtool.remotemonitor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MonitorClient extends JFrame {

    // private String host = "192.168.2.237";
    private String host = "localhost";
    private DataInputStream dis;
    private BufferedImage viewArea;
    private ViewPanel viewPanel;
    byte[] b;
    ImageInputStream imageInputstream;

    public MonitorClient() {
        setTitle("AGV远程监控工具");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        viewPanel = new ViewPanel();
        getContentPane().add(viewPanel, BorderLayout.CENTER);
        pack();
    }

    public void connectionMonitorServer() {
        try {
            Socket server = new Socket(host, 8877);
            InputStream is = server.getInputStream();
            dis = new DataInputStream(is);
            while (true) {
                //  int width = dis.readInt();
                //  int height = dis.readInt();
                // setSize(new Dimension(width, height));
                //int len = dis.readInt();
                //System.out.println("width="+width+",height="+height+",data size="+len);
                //System.out.println("-----数据大小="+(len/1024/1024));
                //b = new byte[len];
                //dis.read(b);
                //imageInputstream = new MemoryCacheImageInputStream(dis);
                //imageInputstream = new MemoryCacheImageInputStream(new ByteArrayInputStream(b));
                viewArea = ImageIO.read(dis);
                viewPanel.updateView(viewArea);
                System.out.println("---------收到监控数据。。。");
            }
        } catch (Exception e) {
        }
    }

    class ViewPanel extends JPanel {

        private BufferedImage bi;

        public void paintComponent(Graphics g) {
            if (bi != null) {
                g.drawImage(bi, 0, 0, null);
            }
        }

        public Dimension getPreferredSize() {
            Dimension dimension = new Dimension();
            dimension.setSize(1024, 768);
            return dimension;
        }

        public void updateView(BufferedImage bi) {
            this.bi = bi;
            revalidate();
            repaint();
        }
    }

    /*public static void main(String[] args) {
        MonitorClient client = new MonitorClient();
        client.setVisible(true);
        client.connectionMonitorServer();
    }*/
}
