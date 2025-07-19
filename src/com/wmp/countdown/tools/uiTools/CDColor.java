package com.wmp.countdown.tools.uiTools;

import com.wmp.countdown.tools.printLog.Log;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CDColor {

    private static final Map<String, Color> COLOR_MAP = new HashMap<String, Color>();
    static {
        COLOR_MAP.put("blue", new Color(0x0090FF));
        COLOR_MAP.put("red", new Color(0xFF0000));
        COLOR_MAP.put("green", new Color(0x00FF00));
        COLOR_MAP.put("yellow", new Color(0xFFFF00));
        COLOR_MAP.put("orange", new Color(0xFFA500));
        COLOR_MAP.put("purple", new Color(0x800080));
        COLOR_MAP.put("pink", new Color(0xFFC0CB));
        COLOR_MAP.put("white", Color.WHITE);
        COLOR_MAP.put("black", Color.BLACK);


    }

    private String textColorStr = "black";
    private String bgColorStr = "white";
    private String mainColorStr = "blue";

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
            Log.err.print(this.getClass().getName(), "textColorStr is error:\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Color getBgColor() {
        try {
            return COLOR_MAP.get(bgColorStr);
        } catch (Exception e) {
            Log.err.print(this.getClass().getName(), "bgColorStr is error:\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Color getMainColor() {
        try {
            return COLOR_MAP.get(mainColorStr);
        } catch (Exception e) {
            Log.err.print(this.getClass().getName(), "mainColorStr is error:\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
