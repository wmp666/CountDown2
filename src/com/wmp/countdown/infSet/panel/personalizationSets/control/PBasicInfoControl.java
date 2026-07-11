package com.wmp.countdown.infSet.panel.personalizationSets.control;

import com.wmp.publicTools.CTInfo;
import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.publicTools.io.IOForInfo;
import com.wmp.publicTools.printLog.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class PBasicInfoControl extends CTInfoControl<PBasicInfo> {
    @Override
    public File getInfoBasicFile() {
        return new File(CTInfo.DATA_PATH, "setUp.json");
    }

    @Override
    public void setInfo(PBasicInfo pBasicInfo) {
        IOForInfo io = new IOForInfo(getInfoBasicFile());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("canExit", pBasicInfo.canExit())
                .put("StartUpdate", pBasicInfo.startUpdate())
                .put("buttonColor", pBasicInfo.buttonColor())
                .put("mainColor", pBasicInfo.mainColor())
                .put("FontName", pBasicInfo.fontName())
                .put("mainTheme", pBasicInfo.mainTheme())
                .put("disposeButton", pBasicInfo.disposeButton())
                .put("isSaveLog", pBasicInfo.isSaveLog());
        try {
            io.setInfo(jsonObject.toString(4));
        } catch (IOException e) {
            Log.err.print(PBasicInfoControl.class, "保存数据失败", e);
        }
    }

    @Override
    protected PBasicInfo refreshInfo() {
        IOForInfo io = new IOForInfo(getInfoBasicFile());
        try {
            String s = io.getInfos();
            if (!s.equals("err")) {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.optJSONArray("disposeButton");
                String[] disposeButtons = jsonArray == null?new String[0]:jsonArray.toList().toArray(new String[0]);
                return new PBasicInfo(jsonObject.optBoolean("canExit", true),
                        jsonObject.optBoolean("StartUpdate",  true),
                        jsonObject.optBoolean("isSaveLog",  true),
                        jsonObject.optString("mainColor", "blue"),
                        jsonObject.optString("mainTheme", "light"),
                        jsonObject.optBoolean("buttonColor", false),
                        jsonObject.optString("FontName", "微软雅黑"),
                        disposeButtons
                        );
            }
        } catch (Exception e) {
            Log.err.systemPrint(PBasicInfoControl.class, "读取数据失败", e);
        }
        return new PBasicInfo(true, true, true, "blue", "light", false, "微软雅黑", new String[0]);
    }
}
