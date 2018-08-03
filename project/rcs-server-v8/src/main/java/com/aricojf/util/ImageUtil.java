/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.util;

/**
 * 图像工具包
 *
 * @author aricochen
 */
public class ImageUtil {

//    //等比例缩放
//    public static BufferedImage scaleImage(final BufferedImage source, int targetW, int targetH) {
//        long begin1 = System.currentTimeMillis();
//        // targetW，targetH分别表示目标长和宽  
//        int type = source.getType();
//        BufferedImage target = null;
//        double sx = (double) targetW / source.getWidth();
//        double sy = (double) targetH / source.getHeight();
//        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放  
//        // 则将下面的if else语句注释即可  
//        if (sx < sy) {
//            sx = sy;
//            targetW = (int) (sx * source.getWidth());
//        } else {
//            sy = sx;
//            targetH = (int) (sy * source.getHeight());
//        }
//        if (type == BufferedImage.TYPE_CUSTOM) { // handmade  
//            ColorModel cm = source.getColorModel();
//            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
//                    targetH);
//            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
//            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
//        } else {
//            target = new BufferedImage(targetW, targetH, type);
//        }
//        ScaleFilter scaleFilter = new ScaleFilter(targetW, targetH);
//        scaleFilter.filter(source, target);
//        long end = System.currentTimeMillis();
//        System.out.println("图像进行缩放所使用的时间:" + (end - begin1));
//        return target;
//    }
//
//    //图片高质量缩放类[外国人写的]
//    public static void resize(File originalFile, File resizedFile,
//            int newWidth, float quality) throws IOException {
//        if (quality > 1) {
//            throw new IllegalArgumentException(
//                    "Quality has to be between 0 and 1");
//        }
//
//        ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
//        Image i = ii.getImage();
//        Image resizedImage = null;
//
//        int iWidth = i.getWidth(null);
//        int iHeight = i.getHeight(null);
//
//        if (iWidth > iHeight) {
//            resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight)
//                    / iWidth, Image.SCALE_SMOOTH);
//        } else {
//            resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight,
//                    newWidth, Image.SCALE_SMOOTH);
//        }
//
//        // This code ensures that all the pixels in the image are loaded.  
//        Image temp = new ImageIcon(resizedImage).getImage();
//
//        // Create the buffered image.  
//        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
//                temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
//
//        // Copy image to buffered image.  
//        Graphics g = bufferedImage.createGraphics();
//
//        // Clear background and paint the image.  
//        g.setColor(Color.white);
//        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
//        g.drawImage(temp, 0, 0, null);
//        g.dispose();
//
//        // Soften.  
//        float softenFactor = 0.05f;
//        float[] softenArray = {0, softenFactor, 0, softenFactor,
//            1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
//        Kernel kernel = new Kernel(3, 3, softenArray);
//        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
//        bufferedImage = cOp.filter(bufferedImage, null);
//
//        // Write the jpeg to a file.  
//        FileOutputStream out = new FileOutputStream(resizedFile);
//
//        // Encodes image as a JPEG data stream  
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//
//        JPEGEncodeParam param = encoder
//                .getDefaultJPEGEncodeParam(bufferedImage);
//
//        param.setQuality(quality, true);
//
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(bufferedImage);
//    } // Example usage  
//
//    /**
//     * 以JPEG编码保存图片
//     * @param dpi 分辨率
//     * @param image_to_save 要处理的图像图片
//     * @param JPEGcompression 压缩比
//     * @param fos 文件输出流
//     * @throws IOException
//     * 注意本方法引用地址:http://www.aichengxu.com/view/70782
//     */
//    public static void saveAsJPEG(Integer dpi, BufferedImage image_to_save, float JPEGcompression, FileOutputStream fos) throws IOException {
//
//	//useful documentation at http://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html 	      
//        //useful example program at http://johnbokma.com/java/obtaining-image-metadata.html to output JPEG data
//	//old jpeg class
//        //com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder  =  com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);
//        //com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam  =  jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);
//        // Image writer
//        //JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpeg").next();
//        ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpg").next();
//        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
//        imageWriter.setOutput(ios);
//        //and metadata
//        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image_to_save), null);
//        if (dpi != null && !dpi.equals("")) {
//	     //old metadata
//            //jpegEncodeParam.setDensityUnit(com.sun.image.codec.jpeg.JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
//            //jpegEncodeParam.setXDensity(dpi);
//            //jpegEncodeParam.setYDensity(dpi);
//            //new metadata
//            Element tree = (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
//            Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
//            jfif.setAttribute("Xdensity", Integer.toString(dpi));
//            jfif.setAttribute("Ydensity", Integer.toString(dpi));
//        }
//
//        if (JPEGcompression >= 0 && JPEGcompression <= 1f) {
//	    //old compression
//            //jpegEncodeParam.setQuality(JPEGcompression,false);
//            // new Compression
//            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
//            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
//            jpegParams.setCompressionQuality(JPEGcompression);
//        }
//
//	//old write and clean
//        //jpegEncoder.encode(image_to_save, jpegEncodeParam);
//        //new Write and clean up
//        imageWriter.write(imageMetaData, new IIOImage(image_to_save, null, null), null);
//        ios.close();
//        imageWriter.dispose();
//    }
}
