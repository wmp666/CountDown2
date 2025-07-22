package com.wmp.countdown.tools.uiTools;

import com.wmp.countdown.tools.printLog.Log;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CDColor {

    private static final Map<String, Color> COLOR_MAP = new HashMap<String, Color>();
    static {
        COLOR_MAP.put("蓝色", new Color(0x0090FF));
        COLOR_MAP.put("红色", new Color(0xFF0000));
        COLOR_MAP.put("绿色", new Color(0x00FF00));
        COLOR_MAP.put("黄色", new Color(0xFFFF00));
        COLOR_MAP.put("橙色", new Color(0xFFA500));
        COLOR_MAP.put("紫色", new Color(0x800080));
        COLOR_MAP.put("粉色", new Color(0xFFC0CB));
        COLOR_MAP.put("白色", Color.WHITE);
        COLOR_MAP.put("黑色", Color.BLACK);


    }

    private String textColorStr = "黑色";
    private String bgColorStr = "白色";
    private String mainColorStr = "蓝色";

    public CDColor() {

    }

    public CDColor(String textColorStr, String bgColorStr, String mainColorStr) {
        this.textColorStr = textColorStr;
        this.bgColorStr = bgColorStr;
        this.mainColorStr = mainColorStr;
    }

    public String getTextColorStr() {
        return textColorStr;
    }

    public void setTextColorStr(String textColorStr) {
        this.textColorStr = textColorStr;
    }

    public String getBgColorStr() {
        return bgColorStr;
    }

    public void setBgColorStr(String bgColorStr) {
        this.bgColorStr = bgColorStr;
    }

    public String getMainColorStr() {
        return mainColorStr;
    }

    public void setMainColorStr(String mainColorStr) {
        this.mainColorStr = mainColorStr;
    }

    public Color getTextColor() {
        try {
            return COLOR_MAP.get(textColorStr);
        } catch (Exception e) {
            Log.warn.print(this.getClass().getName(), "textColorStr is error:\n" + e.getMessage());
            return COLOR_MAP.get("黑色");
            //throw new RuntimeException(e);
        }
    }

    public Color getBgColor() {
        try {
            return COLOR_MAP.get(bgColorStr);
        } catch (Exception e) {
            Log.warn.print(this.getClass().getName(), "bgColorStr is error:\n" + e.getMessage());
            return COLOR_MAP.get("白色");
            //throw new RuntimeException(e);
        }
    }

    public Color getMainColor() {
        try {
            return COLOR_MAP.get(mainColorStr);
        } catch (Exception e) {
            Log.warn.print(this.getClass().getName(), "mainColorStr is error:\n" + e.getMessage());
            return COLOR_MAP.get("蓝色");
            //throw new RuntimeException(e);
        }
    }

    public void setAllColor(String textColorStr, String bgColorStr, String mainColorStr) {
        this.textColorStr = textColorStr;
        this.bgColorStr = bgColorStr;
        this.mainColorStr = mainColorStr;
    }

    public String[] getAllColorStyle() {
        return COLOR_MAP.keySet().toArray(new String[0]);
    }
}
