package com.wmp.countdown.tools;

import com.wmp.countdown.tools.uiTools.CDColor;

public class CDInfo {

    public static final String NAME = "Countdown";
    public static final String VERSION = "2.0.0";
    public static final String AUTHOR = "WMP";

    public static final String DATA_PATH;
    public static final String TEMP_PATH;
    public static final String ICON_PATH = "/images/icon.png";

    public static String title = "";
    public static String targetTime = "2025.12.01 00:00:00";

    public static final CDColor COLOR = new CDColor();

    static{
        //加载基础目录
        String path = System.getenv("LOCALAPPDATA");

        DATA_PATH = path + "\\CountDown\\";
        TEMP_PATH = path + "\\CountDownTemp\\";



    }

}
