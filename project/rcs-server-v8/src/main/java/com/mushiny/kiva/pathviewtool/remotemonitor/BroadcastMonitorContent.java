/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.pathviewtool.remotemonitor;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class BroadcastMonitorContent implements Runnable {

    private BufferedImage viewArea;
    private DataOutputStream dos;
    private Robot robot;
    private Toolkit tk;
    private ZipOutputStream os;
    //  private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public BroadcastMonitorContent(DataOutputStream dos) {
        System.out.println("====client conneciton");
        this.dos = dos;
        try {
            robot = new Robot();
            tk = Toolkit.getDefaultToolkit();
        } catch (Exception e) {

        }
    }

    public void run() {
        while (true) {
            broadContent();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }
    }

    public void broadContent() {
        try {
            Rectangle r = new Rectangle(0, 0, (int) tk.getScreenSize().getWidth(), (int) tk.getScreenSize().getHeight());
            // Rectangle r = new Rectangle(0, 0, 200, 300);
            viewArea = robot.createScreenCapture(r);
            //      int width = viewArea.getWidth();
            //     int height = viewArea.getHeight();


            byte[] data = getBytes(viewArea);
//            dos.writeInt(width);
//            dos.writeInt(height);
            //dos.writeInt(data.length);
            dos.write(data);
            dos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }

    public BufferedImage getViewArea() {
        return viewArea;
    }
    public void setViewArea(BufferedImage viewArea) {
        this.viewArea = viewArea;
    }

}
