package com.fast.starters.sms.utils;

import com.fast.starters.sms.model.support.CaptchaItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * 验证码工具类.
 *
 * @author wangjiannan
 */
@Slf4j
public class CaptchaUtils {
    // 默认的验证码大小
    public static final int WIDTH = 125;

    public static final int HEIGHT = 45;

    public static final int CODE_SIZE = 4;
    // 生成随机类
    private static final Random RANDOM = new Random();
    // 验证码随机字符数组
    private static final char[] charArray = "3456789ABCDEFGHJKMNPQRSTUVWXY".toCharArray();
    // 验证码字体
    private static final Font[] RANDOM_FONT = new Font[]{new Font("nyala", Font.BOLD, 38),
        new Font("Arial", Font.BOLD, 32),
        new Font("Bell MT", Font.BOLD, 32), new Font("Credit valley", Font.BOLD, 34),
        new Font("Impact", Font.BOLD, 32),
        new Font(Font.MONOSPACED, Font.BOLD, 40)};

    /**
     * 生成验证码字符串.
     *
     * @return string
     */
    public static String generateCode() {
        int count = CODE_SIZE;
        char[] buffer = new char[count];
        for (int i = 0; i < count; i++) {
            buffer[i] = charArray[RANDOM.nextInt(charArray.length)];
        }
        return new String(buffer);
    }

    ///**
    // * 生成验证码.
    // */
    //public static void generate(HttpServletResponse response, String code) {
    //    BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    //    response.setHeader("Pragma", "no-cache");
    //    response.setHeader("Cache-Control", "no-cache");
    //    response.setDateHeader("Expires", 0);
    //    response.setContentType("image/jpeg");
    //
    //    ServletOutputStream sos = null;
    //    try {
    //        drawGraphic(image, code);
    //        sos = response.getOutputStream();
    //        ImageIO.write(image, "JPEG", sos);
    //        sos.flush();
    //    } catch (Exception e) {
    //        throw new RuntimeException(e);
    //    } finally {
    //        IOUtils.closeQuietly(sos);
    //    }
    //}

    /**
     * 生成图片验证码、图片base64流.
     */
    public static CaptchaItem generateCaptchaItem() {
        String captchaText = CaptchaUtils.generateCode().toUpperCase();
        BufferedImage image = new BufferedImage(CaptchaUtils.WIDTH, CaptchaUtils.HEIGHT, BufferedImage.TYPE_INT_RGB);
        CaptchaUtils.drawGraphic(image, captchaText);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", outputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

        //返回，可选择流或者base64图.
        String imgBase64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        log.info("------image={},imgcode={}", "", captchaText);
        return new CaptchaItem(captchaText, imgBase64);
    }

    /**
     * 描述.
     *
     * @param image image
     * @param code  code
     */
    public static void drawGraphic(BufferedImage image, String code) {
        // 获取图形上下文
        Graphics2D gu = image.createGraphics();
        gu.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // 图形抗锯齿
        gu.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 字体抗锯齿
        gu.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 设定背景色，淡色
        gu.setColor(getRandColor(210, 250));
        gu.fillRect(0, 0, WIDTH, HEIGHT);

        // 画小字符背景
        Color color = null;
        for (int i = 0; i < 20; i++) {
            color = getRandColor(120, 200);
            gu.setColor(color);
            String rand = String.valueOf(charArray[RANDOM.nextInt(charArray.length)]);
            gu.drawString(rand, RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
            color = null;
        }
        // 取随机产生的认证码(4位数字)
        char[] buffer = code.toCharArray();
        for (int i = 0; i < buffer.length; i++) {
            char _code = buffer[i];
            // 旋转度数 最好小于45度
            int degree = RANDOM.nextInt(28);
            // 将认证码显示到图象中

            if (i % 2 == 0) {
                degree = degree * (-1);
            }
            // 定义坐标
            int xu = 22 * i;
            int yu = 21;
            gu.drawString("" + _code, xu + 8, yu + 10);
            // 旋转区域
            gu.rotate(Math.toRadians(degree), xu, yu);
            // 设定字体颜色
            color = getRandColor(20, 130);
            gu.setColor(color);
            // 设定字体，每次随机
            gu.setFont(RANDOM_FONT[RANDOM.nextInt(RANDOM_FONT.length)]);
            // 将认证码显示到图象中
            gu.drawString("" + _code, xu + 8, yu + 10);
            // 旋转之后，必须旋转回来
            gu.rotate(-Math.toRadians(degree), xu, yu);
        }
        // 图片中间曲线，使用上面缓存的color
        gu.setColor(color);
        // width是线宽,float型
        BasicStroke bs = new BasicStroke(3);
        gu.setStroke(bs);
        // 画出曲线
        QuadCurve2D.Double curve = new QuadCurve2D.Double(0d, RANDOM.nextInt(HEIGHT - 8) + 4,
            WIDTH / 2, HEIGHT / 2, WIDTH, RANDOM.nextInt(HEIGHT - 8) + 4);
        gu.draw(curve);
        // 销毁图像
        gu.dispose();
    }

    // 给定范围获得随机颜色
    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int ru = fc + RANDOM.nextInt(bc - fc);
        int gu = fc + RANDOM.nextInt(bc - fc);
        int bu = fc + RANDOM.nextInt(bc - fc);
        return new Color(ru, gu, bu);
    }
}
