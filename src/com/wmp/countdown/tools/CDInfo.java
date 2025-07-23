package com.wmp.countdown.tools;

import com.wmp.countdown.tools.printLog.Log;
import com.wmp.countdown.tools.uiTools.CDColor;
import com.wmp.countdown.tools.uiTools.CDFont;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class CDInfo {

    public static final String NAME = "Countdown";
    public static final String VERSION = "2.2.0";
    public static final String AUTHOR = "无名牌";

    public static final String DATA_PATH;
    public static final String TEMP_PATH;
    public static final String ICON_PATH = "/images/icon.png";

    public static String title = "";
    //public static Date targetTime = new Date();//"2025.12.01 00:00:00";
    public static String targetTime = "2025.12.01 00:00:00";

    public static final CDColor COLOR = new CDColor();

    public static final CDFont FONT = new CDFont();

    public static boolean isCanExit = true;

    static{
        //加载基础目录
        String path = System.getenv("LOCALAPPDATA");

        DATA_PATH = path + "\\CountDown\\";
        TEMP_PATH = path + "\\CountDownTemp\\";

        if ( !Files.exists(Paths.get(DATA_PATH))){
            try {
                Files.createDirectories(Paths.get(DATA_PATH ));
            } catch (IOException ex) {
                Log.err.print(null, "创建数据目录", "创建目录失败");
                throw new RuntimeException(ex);
            }
        }

        //初始化 COLOR FONT

    }

}
