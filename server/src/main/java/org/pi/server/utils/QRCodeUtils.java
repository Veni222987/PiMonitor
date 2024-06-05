package org.pi.server.utils;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import javax.imageio.ImageIO;

import cn.hutool.core.codec.Base64;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author HTT
 */
@Component
public class QRCodeUtils {
    /**
     * 编码格式
     */
    private static final String CHARSET = "utf-8";

    /**
     * 二维码后缀名
     */
    private static final String FORMAT_NAME = "JPG";

    /**
     * 二维码尺寸
     */
    private static final int QRCODE_SIZE = 300;

    /**
     * 插入图宽度
     */
    private static final int WIDTH = 60;

    /**
     * 插入图高度
     */
    private static final int HEIGHT = 60;

    /**
     * 插入图片
     * @param source 文件流
     * @param imgPath 图片路径
     * @param needCompress 是否压缩图片
     * @throws Exception
     */
    private void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            throw new Exception(imgPath+"图片文件不存在");
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩LOGO
        if (needCompress) {
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 创建带图片的二维码核心方法（如果图片路径为空，不会生成图片）
     * @param content 二维码内容
     * @param imgPath 图片路径
     * @param needCompress 是否压缩图片
     * @return
     * @throws Exception
     */
    private BufferedImage createEwm(String content, String imgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable(16);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (StringUtils.isEmpty(imgPath)) {
            return image;
        }
        // 插入图片
        insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 创建自定义颜色和图片的二维码核心方法（如果图片路径为空，不会生成图片）
     * @param content 二维码内容
     * @param imgPath 图片路径
     * @param needCompress 是否压缩图片
     * @param frontColor 前景色
     * @param backgroundColor 背景色
     * @return
     * @throws Exception
     */
    private BufferedImage createEwm(String content, String imgPath, boolean needCompress,int frontColor,int backgroundColor) throws Exception {
        Hashtable hints = new Hashtable(16);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? frontColor : backgroundColor);
            }
        }
        if (StringUtils.isEmpty(imgPath)) {
            return image;
        }
        // 插入图片
        insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 生成带图片的二维码保存为文件（如果图片路径为空，不会生成图片）
     * @param content 二维码内容
     * @param imgPath 图片路径
     * @param destPath 存放路径
     * @param needCompress 是否压缩图片
     * @throws Exception
     */
    private void generate(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = createEwm(content, imgPath, needCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    /**
     * 创建自定义颜色和图片的二维码保存为文件（如果图片路径为空，不会生成图片）
     * @param content 二维码内容
     * @param imgPath 图片路径（路径为空，则只生成基础的二维码）
     * @param destPath 存放路径
     * @param needCompress 是否压缩图片
     * @param frontColor 前景色
     * @param backgroundColor 背景色
     * 例举一些16进制的颜色代码
     * 0x000000 黑
     * 0xff0000 亮红
     * 0x00ff00 亮绿
     * 0xffff00 亮黄
     * 0x0000ff 亮蓝
     * 0xff00ff 亮紫
     * 0x00ffff 亮浅蓝
     * 0xffffff 白
     * 0xc6c6c6 亮灰
     * 0x848484 暗灰
     * @throws Exception
     */
    private void generate(String content, String imgPath, String destPath, boolean needCompress,int frontColor,int backgroundColor) throws Exception {
        BufferedImage image = createEwm(content, imgPath, needCompress,frontColor,backgroundColor);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    /**
     * 生成带有图片的二维码并返回Base64（如果图片路径为空，不会生成图片）
     * @param content 二维码内容
     * @param imgPath 图片路径
     * @param needCompress 是否压缩图片
     * @return
     * @throws Exception
     */
    private String generateBase64(String content, String imgPath, boolean needCompress) throws Exception {
        if (!StringUtils.isEmpty(content)) {
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 0);
            BufferedImage bufferedImage = createEwm(content, imgPath, needCompress);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, FORMAT_NAME, os);
            String base64 = Base64.encode(os.toByteArray());
            os.flush();
            os.close();
            return "data:image/png;base64," + base64;
        }
        return "";
    }

    /**
     * 生成带有自定义颜色和图片的二维码并返回Base64（如果图片路径为空，不会生成图片）
     * @param content 二维码内容
     * @param imgPath 图片路径
     * @param needCompress 是否压缩图片
     * @param frontColor 前景色
     * @param backgroundColor 背景色
     * @return
     * @throws Exception
     */
    private String generateBase64(String content, String imgPath, boolean needCompress,int frontColor,int backgroundColor) throws Exception {
        if (!StringUtils.isEmpty(content)) {
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 0);
            BufferedImage bufferedImage = createEwm(content, imgPath, needCompress,frontColor,backgroundColor);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, FORMAT_NAME, os);
            String base64 = Base64.encode(os.toByteArray());
            os.flush();
            os.close();
            return "data:image/png;base64," + base64;
        }
        return "";
    }

    /**
     * 创建多级目录
     * @param destPath
     */
    private void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 根据文件解析二维码
     * @param file
     * @return
     * @throws Exception
     */
    private String analysis(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        HashMap hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /*******************************************************以下是创建二维码提供的方法封装*******************************************************/

    /******************************另存为文件版本******************************/

    /**
     * 创建基础的二维码
     * @param content 二维码内容
     * @param destPath 存放路径
     * @throws Exception
     */
    public void create(String content, String destPath) throws Exception {
        generate(content, null, destPath, false);
    }

    /**
     * 创建带颜色基础的二维码
     * @param content 二维码内容
     * @param destPath 存放路径
     * @throws Exception
     */
    public void create(String content, String destPath,int frontColor,int backgroundColor) throws Exception {
        generate(content, null, destPath, false,frontColor,backgroundColor);
    }

    /**
     * 创建带图片的二维码
     * @param content 二维码内容
     * @param imgPath 图片路径
     * @param destPath 存放路径
     * @param needCompress 是否压缩图片
     * @throws Exception
     */
    public void create(String content, String imgPath, String destPath,boolean needCompress) throws Exception {
        generate(content, imgPath, destPath, needCompress);
    }

    /**
     * 创建带有自定义颜色和图片的二维码
     * @param content 二维码内容
     * @param imgPath 图片路径
     * @param destPath 存放路径
     * @param needCompress 是否压缩图片
     * @param frontColor 前景色
     * @param backgroundColor 背景色
     * @throws Exception
     */
    public void create(String content, String imgPath, String destPath,boolean needCompress,int frontColor,int backgroundColor) throws Exception {
        generate(content, imgPath, destPath, needCompress,frontColor,backgroundColor);
    }

    /******************************另存为文件版本******************************/

    /******************************Base64版本******************************/

    /**
     * 创建基础的二维码并返回Base64
     * @param content
     * @throws Exception
     */
    public String create(String content) throws Exception {
        return generateBase64(content,null,false);
    }

    /**
     * 创建带颜色基础的二维码并返回Base64
     * @param content
     * @throws Exception
     */
    public String create(String content,int frontColor,int backgroundColor) throws Exception {
        return generateBase64(content,null,false,frontColor,backgroundColor);
    }

    /**
     * 创建带图片的二维码并返回Base64
     * @param content 二维码内容
     * @param imgPath 图片路径
     * @param needCompress 是否压缩图片
     * @throws Exception
     */
    public String create(String content, String imgPath,boolean needCompress) throws Exception {
        return generateBase64(content, imgPath, needCompress);
    }

    /**
     * 创建带有自定义颜色和图片的二维码并返回Base64
     * @param content 二维码内容
     * @param imgPath 图片路径
     * @param needCompress 是否压缩图片
     * @param frontColor 前景色
     * @param backgroundColor 背景色
     * @throws Exception
     */
    public String create(String content, String imgPath,boolean needCompress,int frontColor,int backgroundColor) throws Exception {
        return generateBase64(content, imgPath, needCompress,frontColor,backgroundColor);
    }

    /******************************Base64版本******************************/

    /**
     * 根据文件路径解析二维码
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public String decode(String path) throws Exception {
        File file = new File(path);
        if(!file.exists()){
            throw new Exception("文件不存在！");
        }
        return analysis(file);
    }

}