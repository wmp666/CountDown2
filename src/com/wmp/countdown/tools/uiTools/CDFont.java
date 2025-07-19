package com.wmp.countdown.tools.uiTools;

import com.wmp.countdown.tools.printLog.Log;

import java.awt.*;
import java.util.Arrays;

public class CDFont {

    private static String fontName = "微软雅黑";

    private static int BigBigSize = 100;
    private static int moreBigSize = 60;
    private static int bigSize = 24;
    private static int normalSize = 19;
    private static int smallSize = 15;
    private static int moreSmallSize = 12;

    public static Font getCDFont(int fontStyle, CDFontSizeStyle sizeStyle) {
        int size = 0;
        switch (sizeStyle) {
            case BIG_BIG -> size = BigBigSize;
            case MORE_BIG -> size = moreBigSize;
            case BIG -> size = bigSize;
            case NORMAL -> size = normalSize;
            case SMALL -> size = smallSize;
            case MORE_SMALL -> size = moreSmallSize;
        }//12 14/-15-/16 18/(-19-/)20 -23-/24/25
        return new Font(fontName, fontStyle, size);
    }

    public static String[] getAllFontName() {
        //获取所有字体
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();//获取本地图形环境
        String[] fontNames = ge.getAvailableFontFamilyNames();
        Log.info.print("fontNames", "所有字体:" + Arrays.toString(fontNames));
        return fontNames;
    }

    public static String getFontName() {
        return fontName;
    }

    public static void setFontName(String fontName) {
        //获取所有字体
        String[] fontNames = getAllFontName();
        //判断是否存在该字体
        boolean isExist = Arrays.asList(fontNames).contains(fontName);
        if (!isExist) {
            Log.err.print("fontNames", "不存在该字体:" + fontName);
            return;
        }
        CDFont.fontName = fontName;
    }

    public static void setSize(int bigBigSize, int moreBigSize, int bigSize, int normalSize, int smallSize, int moreSmallSize) {
        CDFont.BigBigSize = bigBigSize;
        CDFont.moreBigSize = moreBigSize;
        CDFont.bigSize = bigSize;
        CDFont.normalSize = normalSize;
        CDFont.smallSize = smallSize;
        CDFont.moreSmallSize = moreSmallSize;
    }

    /**
     * 获取字体大小
     *
     * @return 大  中  小  更小
     */
    public static int[] getSize() {
        return new int[]{BigBigSize, moreBigSize, bigSize, normalSize, smallSize, moreSmallSize};
    }
}
