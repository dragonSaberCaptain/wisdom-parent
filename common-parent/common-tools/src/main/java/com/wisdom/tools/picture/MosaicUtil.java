package com.wisdom.tools.picture;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/1/29 9:41 星期日
 */
@Slf4j
@Data
@Accessors(chain = true)
public class MosaicUtil {
    /**
     * 给图片指定位置打马赛克
     *
     * @param x          图片要打码区域左上角的横坐标
     * @param y          图片要打码区域左上角的纵坐标
     * @param width      图片要打码区域的宽度
     * @param height     图片要打码区域的高度
     * @param mosaicSize 马赛克尺寸，即每个矩形的长宽
     */
    public static boolean mosaic(InputStream source, OutputStream target, int x, int y, int width, int height, int mosaicSize) throws IOException {
        //读取该图片
        BufferedImage bi = ImageIO.read(source);
        //复制一份画布，在新画布上作画，因为要用到原来画布的颜色信息
        BufferedImage spinImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        //马赛克块大小 不能大于图片宽度和高度
        if (mosaicSize > bi.getWidth() || mosaicSize > bi.getHeight() || mosaicSize <= 0) {
            System.err.println("马赛克尺寸设置不正确");
            return false;
        }
        //2. 设置各方向绘制的马赛克块个数
        int xcount = width / mosaicSize; // x方向绘制个数
        int ycount = height / mosaicSize; // y方向绘制个数
        if ((width % mosaicSize) != 0) {
            xcount++;
        }
        if ((height % mosaicSize) != 0) {
            ycount++;
        }
        //3. 绘制马赛克(在指定范围内绘制矩形并填充颜色)
        Graphics gs = spinImage.getGraphics();
        //将老画布内容画到新画布上
        gs.drawImage(bi, 0, 0, null);
        int xTmp = x;
        int yTmp = y;
        for (int i = 0; i < xcount; i++) {
            for (int j = 0; j < ycount; j++) {
                //马赛克矩形大小
                int mwidth = mosaicSize;
                int mheight = mosaicSize;
                if (i == xcount - 1) {   //横向最后一个比较特殊，可能不够一个size
                    mwidth = width - xTmp;
                }
                if (j == ycount - 1) {  //同理
                    mheight = height - yTmp;
                }
                //矩形颜色取中心像素点RGB值
                int centerX = xTmp;
                int centerY = yTmp;
                if (mwidth % 2 == 0) {
                    centerX += mwidth / 2;
                } else {
                    centerX += (mwidth - 1) / 2;
                }
                if (mheight % 2 == 0) {
                    centerY += mheight / 2;
                } else {
                    centerY += (mheight - 1) / 2;
                }
                Color color = new Color(bi.getRGB(centerX, centerY));
                gs.setColor(color);
                gs.fillRect(xTmp, yTmp, mwidth, mheight);
                yTmp += mosaicSize;// 计算下一个矩形的y坐标
            }
            yTmp = y;// 还原y坐标
            xTmp += mosaicSize;// 计算x坐标
        }
        gs.dispose();
        ImageIO.write(spinImage, "jpg", target); // 保存图片
        return true;
    }

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\admin\\Desktop\\mosaic\\1.jpg";
        File fileSrc = new File(filePath);
        int mosaicSize = 10;
        for (int i = 0; i < 10; i++) {
            FileInputStream fileInputStream = new FileInputStream(fileSrc);
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\admin\\Desktop\\mosaic\\" + (i + 1) + "_mosaic.jpg");
            mosaic(fileInputStream, fileOutputStream, 1350, 600, 400, 400, mosaicSize);
            mosaicSize += 10;
        }
    }
}
