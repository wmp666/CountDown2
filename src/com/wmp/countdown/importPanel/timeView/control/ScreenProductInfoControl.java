package com.wmp.countdown.importPanel.timeView.control;

import com.wmp.publicTools.CTInfo;
import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.publicTools.io.IOForInfo;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScreenProductInfoControl extends CTInfoControl<ScreenProductInfo> {
    @Override
    public File getInfoBasicFile() {
        return new File(CTInfo.DATA_PATH, "ScreenProduct");
    }

    @Override
    public void setInfo(ScreenProductInfo screenProductInfo) {
        try {
            IOForInfo ioForInfo = new IOForInfo(new File(getInfoBasicFile(), "background.json"));
            JSONObject jsonObject = new JSONObject(ioForInfo.getInfos());
            jsonObject.put("mainColor", screenProductInfo.mainColor());
            jsonObject.put("mainTheme", screenProductInfo.mainTheme());
            jsonObject.put("path", screenProductInfo.BGBasicPath());
            if(screenProductInfo.repaintTimer() > 0){
                jsonObject.put("repaintTimer", screenProductInfo.repaintTimer());
            }

            ioForInfo.setInfo(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ScreenProductInfo refreshInfo() {
        try {
            IOForInfo ioForInfo = new IOForInfo(new File(getInfoBasicFile(), "background.json"));
            JSONObject jsonObject = new JSONObject(ioForInfo.getInfos());
            ArrayList<String> pathList = null;
            String BGPath = jsonObject.optString("path");

            if (BGPath != null && BGPath.startsWith("BingBG")) {
                pathList = new ArrayList<>();
                if (jsonObject.getString("path").equals("BingBG")) {
                    pathList.addFirst("url:https://bing.img.run/1920x1080.php") ;
                } else if (jsonObject.getString("path").equals("BingBGRandom")) {
                    pathList.addFirst("url:https://bing.img.run/rand.php");
                }
            }else{
                pathList = getPathList(BGPath);
            }


            return new ScreenProductInfo(
                    jsonObject.optString("mainColor", "white"),
                    jsonObject.optString("mainTheme", "dark"),
                    jsonObject.optString("path"),
                    pathList,
                    jsonObject.optInt("repaintTimer", 60*60)
            );
        } catch (Exception _) {
        }


        return new ScreenProductInfo("white", "dark", null, null, 60*60);
    }

    private static ArrayList<String> getPathList(String BGPath) {
        ArrayList<String> pathList = null;
        if (BGPath != null && !BGPath.isEmpty()) {
            File file1 = new File(BGPath);
            if (file1.exists()) {
                pathList = new ArrayList<>();
                if (file1.isDirectory()) {
                    File[] files = file1.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile()) {
                                pathList.add(file.getAbsolutePath());
                            } else if (file.isDirectory()) {
                                pathList.addAll(getPathList(file.getAbsolutePath()));
                            }
                        }
                    }
                } else if (file1.isFile()) {
                    pathList.add(file1.getAbsolutePath());
                }
            }
        }
        return pathList;
    }
}
