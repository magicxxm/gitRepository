package com.mushiny.kiva.map;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * 地图旋转及翻转
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MapFlipFilter implements BufferedImageOp {

    /**
     * 水平翻转地图
     */
    public static final int FLIP_H = 1;

    /**
     * 垂直翻转地图
     */
    public static final int FLIP_V = 2;

    /**
     * 水平和垂直翻转地图
     */
    public static final int FLIP_HV = 3;

    /**
     * 顺时针旋转地图90度
     */
    public static final int FLIP_90CW = 4;

    /**
     * 逆时针旋转地图90度
     */
    public static final int FLIP_90CCW = 5;

    /**
     * 旋转地图180度
     */
    public static final int FLIP_180 = 6;
    private int operation;
    
     public MapFlipFilter() {
        this(FLIP_90CW);
    }
    public MapFlipFilter(int operation) {
        this.operation = operation;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int type = src.getType();
        WritableRaster srcRaster = src.getRaster();

        int[] inPixels = getRGB(src, 0, 0, width, height, null);

        int x = 0, y = 0;
        int w = width;
        int h = height;

        int newX = 0;
        int newY = 0;
        int newW = w;
        int newH = h;
        switch (getOperation()) {
            case FLIP_H:
                newX = width - (x + w);
                break;
            case FLIP_V:
                newY = height - (y + h);
                break;
            case FLIP_HV:
                newW = h;
                newH = w;
                newX = y;
                newY = x;
                break;
            case FLIP_90CW:
                newW = h;
                newH = w;
                newX = height - (y + h);
                newY = x;
                break;
            case FLIP_90CCW:
                newW = h;
                newH = w;
                newX = y;
                newY = width - (x + w);
                break;
            case FLIP_180:
                newX = width - (x + w);
                newY = height - (y + h);
                break;
        }

        int[] newPixels = new int[newW * newH];

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                int index = row * width + col;
                int newRow = row;
                int newCol = col;
                switch (getOperation()) {
                    case FLIP_H:
                        newCol = w - col - 1;
                        break;
                    case FLIP_V:
                        newRow = h - row - 1;
                        break;
                    case FLIP_HV:
                        newRow = col;
                        newCol = row;
                        break;
                    case FLIP_90CW:
                        newRow = col;
                        newCol = h - row - 1;
                        ;
                        break;
                    case FLIP_90CCW:
                        newRow = w - col - 1;
                        newCol = row;
                        break;
                    case FLIP_180:
                        newRow = h - row - 1;
                        newCol = w - col - 1;
                        break;
                }
                int newIndex = newRow * newW + newCol;
                newPixels[newIndex] = inPixels[index];
            }
        }
        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(newW, newH), dstCM.isAlphaPremultiplied(), null);
        }
        WritableRaster dstRaster = dst.getRaster();
        setRGB(dst, 0, 0, newW, newH, newPixels);
        return dst;
    }
    public int[] getRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            return (int[]) image.getRaster().getDataElements(x, y, width, height, pixels);
        }
        return image.getRGB(x, y, width, height, pixels, 0, width);
    }
    public void setRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            image.getRaster().setDataElements(x, y, width, height, pixels);
        } else {
            image.setRGB(x, y, width, height, pixels, 0, width);
        }
    }
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
        if (dstCM == null) {
            dstCM = src.getColorModel();
        }
        return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), dstCM.isAlphaPremultiplied(), null);
    }
    public Rectangle2D getBounds2D(BufferedImage src) {
        return new Rectangle(0, 0, src.getWidth(), src.getHeight());
    }
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = new Point2D.Double();
        }
        dstPt.setLocation(srcPt.getX(), srcPt.getY());
        return dstPt;
    }
    public RenderingHints getRenderingHints() {
        return null;
    }
    public int getOperation() {
        return operation;
    }
    public void setOperation(int operation) {
        this.operation = operation;
    }
}
