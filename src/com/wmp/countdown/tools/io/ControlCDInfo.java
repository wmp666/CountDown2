package com.wmp.countdown.tools.io;

import com.wmp.countdown.tools.CDInfo;
import com.wmp.countdown.tools.printLog.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ControlCDInfo {
    public static void initCDInfo() {
        Log.info.print("ControlCDInfo", "正在获取数据");

        IOForInfo io = new IOForInfo(CDInfo.DATA_PATH + "data.json");
        try {
            String info = io.GetInfos();

            if (info.isEmpty() || info.equals("err")) {
                Log.info.print("ControlCDInfo", "数据为空,正在初始化数据");
                io.SetInfo("{}");
                info = "{}";
            }

            JSONObject jsonObject = new JSONObject(info);
            if (jsonObject.has("title"))
                CDInfo.title = jsonObject.getString("title");
            if (jsonObject.has("targetTime")) {
                String time = jsonObject.getString("targetTime");
                // 判断时间格式
                DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                dateFormat.parse(time);
                CDInfo.targetTime = time;
            }
            if (jsonObject.has("color")) {
                JSONObject color = jsonObject.getJSONObject("color");
                String textColor = "黑色";
                String bgColor = "白色";
                String mainColor = "蓝色";
                if (color.has("textColor")) textColor = color.getString("textColor");
                if (color.has("bgColor")) bgColor = color.getString("bgColor");
                if (color.has("mainColor")) mainColor = color.getString("mainColor");

                CDInfo.COLOR.setAllColor(color.getString("textColor"), color.getString("bgColor"), color.getString("mainColor"));
            }
            if (jsonObject.has("font")) {
                CDInfo.FONT.setFontName(jsonObject.getString("font"));
            }

            if (jsonObject.has("isCanExit")) {
                CDInfo.isCanExit = jsonObject.getBoolean("isCanExit");
            }

        } catch (IOException e) {
            Log.err.print("ControlCDInfo", "数据获取失败:\n" + e.getMessage());
            throw new RuntimeException(e);
        } catch (JSONException e) {
            Log.err.print("ControlCDInfo", "JSON数据加载失败:\n" + e.getMessage());
            throw new JSONException(e);
        }catch (ParseException e) {
            Log.err.print("ControlCDInfo", "时间格式错误:\n" + e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e){
            Log.err.print("ControlCDInfo", "出现未知错误导致失败:\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void saveCDInfo(String title, String targetTime , String textColorStr, String bgColorStr, String mainColorStr, String fontName, boolean isCanExit) {
        Log.info.print("ControlCDInfo", "正在保存数据");
        IOForInfo io = new IOForInfo(CDInfo.DATA_PATH + "data.json");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("targetTime", targetTime);

            {
                JSONObject color = new JSONObject();
                color.put("textColor", textColorStr);
                color.put("bgColor", bgColorStr);
                color.put("mainColor", mainColorStr);

                jsonObject.put("color", color);
            }

            jsonObject.put("font", fontName);

            jsonObject.put("isCanExit", isCanExit);

            io.SetInfo(jsonObject.toString());
        } catch (IOException e) {
            Log.err.print("ControlCDInfo", "数据保存失败:\n" + e.getMessage());
        }
    }
}
