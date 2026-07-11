package com.wmp.countdown.infSet.panel.personalizationSets.control;

import com.wmp.publicTools.CTInfo;
import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.publicTools.io.IOForInfo;
import com.wmp.publicTools.printLog.Log;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class PPanelInfoControl extends CTInfoControl<PPanelInfo> {
    @Override
    public File getInfoBasicFile() {
        return new File(CTInfo.DATA_PATH, "CTPanelInfo.json");
    }

    @Override
    public void setInfo(PPanelInfo pPanelInfo) {
        IOForInfo io = new IOForInfo(getInfoBasicFile());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("disPanelList", pPanelInfo.disPanelList());
        jsonObject.put("runInBackground", pPanelInfo.runInBackgroundList());
        jsonObject.put("DPI", pPanelInfo.dpi());

        try {
            io.setInfo(jsonObject.toString(4));
        } catch (IOException e) {
            Log.err.print(PPanelInfoControl.class, "保存数据失败", e);
        }
    }

    @Override
    protected PPanelInfo refreshInfo() {
        IOForInfo io = new IOForInfo(getInfoBasicFile());
        try {
            String s = io.getInfos();
            if (!s.equals("err")) {
                JSONObject jsonObject = new JSONObject(s);
                return new PPanelInfo(jsonObject.getJSONArray("disPanelList").toList().toArray(new String[0]),
                        jsonObject.getJSONArray("runInBackground").toList().toArray(new String[0]),
                        jsonObject.getDouble("DPI"));
            }
        } catch (IOException e) {
            Log.err.print(PPanelInfoControl.class, "读取数据失败", e);
        }
        return new PPanelInfo(new String[0], new String[0], 1.0);
    }
}
