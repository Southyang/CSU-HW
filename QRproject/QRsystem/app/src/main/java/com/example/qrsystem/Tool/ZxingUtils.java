package com.example.qrsystem.Tool;


import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Hashtable;

public class ZxingUtils {
    private static int IMAGE_HALFWIDTH = 50;//宽度值，影响中间图片大小
    private static BarcodeEncoder barcodeEncoder;
    static { barcodeEncoder = new BarcodeEncoder(); }
    /**
     * 生成二维码,默认500大小
     * @param contents 需要生成二维码的文字、网址等
     * @return bitmap
     */
    public static Bitmap createQRCode(String contents) {
        try {
            return barcodeEncoder.encodeBitmap(contents, BarcodeFormat.QR_CODE, 500, 500);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码
     * @param contents 需要生成二维码的文字、网址等
     * @param size 需要生成二维码的大小（）
     * @return bitmap
     */
    public static Bitmap createQRCode(String contents, int size) {
        try {
            return barcodeEncoder.encodeBitmap(contents, BarcodeFormat.QR_CODE, size, size);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成二维码
     * @param contents 需要生成二维码的文字、网址等
     * @param size 需要生成二维码的大小（）
     * @param whiteBorderScale 白边宽度比例，最低1，也就是二维码图片的1%白边
     * @return bitmap
     */
    public static Bitmap createQRCode(String contents, int size,int whiteBorderScale ) {
        try {
            HashMap<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, whiteBorderScale<0?1:whiteBorderScale);
            return barcodeEncoder.encodeBitmap(contents,BarcodeFormat.QR_CODE, size, size, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 有logo的二维码
     * @param contents
     * @param size
     * @param logo
     * @return
     */
    public static Bitmap createQRCode(String contents, int size,Bitmap logo) {
        return createQRCodeWithLogo(contents,size,logo,1);
    }
    public static Bitmap createQRCode(String contents, int size,Bitmap logo,int whiteBorderScale) {
        return createQRCodeWithLogo(contents,size,logo,whiteBorderScale);
    }

    /**
     * 生成带logo的二维码，logo默认为二维码的1/5
     *
     * @param contents 需要生成二维码的文字、网址等
     * @param size 需要生成二维码的大小（）
     * @param logo logo文件
     * @return bitmap
     */
    private static Bitmap createQRCodeWithLogo(String contents, int size, Bitmap logo,int whiteBorderScale) {
        try {
            IMAGE_HALFWIDTH = size/10;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            /*
             * 设置容错级别，默认为ErrorCorrectionLevel.L
             * 因为中间加入logo所以建议你把容错级别调至H,否则可能会出现识别不了
             */
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN,whiteBorderScale<0?1:whiteBorderScale);
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            int width = bitMatrix.getWidth();//矩阵高度
            int height = bitMatrix.getHeight();//矩阵宽度
            int halfW = width / 2;
            int halfH = height / 2;

            Matrix m = new Matrix();
            float sx = (float) 2 * IMAGE_HALFWIDTH / logo.getWidth();
            float sy = (float) 2 * IMAGE_HALFWIDTH / logo.getHeight();
            m.setScale(sx, sy);
            //设置缩放信息
            //将logo图片按martix设置的信息缩放
            logo = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), m, false);

            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                            && y > halfH - IMAGE_HALFWIDTH
                            && y < halfH + IMAGE_HALFWIDTH) {
                        //该位置用于存放图片信息
                        //记录图片每个像素信息
                        pixels[y * width + x] = logo.getPixel(x - halfW
                                + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                    } else {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * size + x] = 0xff000000;
                        } else {
                            pixels[y * size + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}

