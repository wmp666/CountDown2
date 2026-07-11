package com.wmp.countdown.extraPanel.countdown;

import com.wmp.publicTools.CTInfo;
import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.publicTools.io.IOForInfo;
import com.wmp.publicTools.printLog.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 倒计时信息控制类,若没有相关数据,将返回<code>null</code>
 */
public class CDInfoControl extends CTInfoControl<CountDownInfos> {

    @Override
    public File getInfoBasicFile() {
        return new File(CTInfo.DATA_PATH, "CountDown.json");
    }

    @Override
    public void setInfo(CountDownInfos countDownInfos) {
        try {
            if (countDownInfos == null) return;
            JSONArray jsonArray = new JSONArray();
            for (CountDownInfo info : countDownInfos.list()) {
                if (info == null) continue;

                JSONObject json = new JSONObject();
                json.put("title", info.title());
                json.put("targetTime", info.targetTime());

                jsonArray.put(json);
            }
            IOForInfo io = new IOForInfo(getInfoBasicFile());

            io.setInfo(jsonArray.toString(4));
        } catch (IOException e) {
            Log.err.print(getClass(), "保存倒计时信息失败", e);
        }
    }
    @Override
    protected CountDownInfos refreshInfo() {
        try {
            IOForInfo io = new IOForInfo(getInfoBasicFile());
            String s = io.getInfos();
            if (!s.equals("err")) {
                if (s.startsWith("{")) {
                    JSONObject json = new JSONObject(s);
                    ArrayList<CountDownInfo> resultInfo = new ArrayList<>();
                    resultInfo.add(new CountDownInfo(json.getString("title"), json.getString("targetTime")));
                    return new CountDownInfos(resultInfo);
                } else {
                    JSONArray jsonArray = new JSONArray(s);
                    ArrayList<CountDownInfo> resultInfo = new ArrayList<>();
                    jsonArray.forEach(o -> {
                        if (o instanceof JSONObject json) {
                            resultInfo.add(new CountDownInfo(json.getString("title"), json.getString("targetTime")));
                        }
                    });
                    return new CountDownInfos(resultInfo);
                }
            }
        } catch (IOException e) {
            Log.err.systemPrint(getClass(), "获取倒计时信息失败", e);
        }
        return new CountDownInfos(new ArrayList<>(List.of(new CountDownInfo("数据出错", "9999.12.30 00:00:00"))));
    }
}
