package com.wmp.publicTools.appFileControl.appInfoControl

import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.appFileControl.CTInfoControl
import com.wmp.publicTools.io.IOForInfo
import com.wmp.publicTools.printLog.Log
import org.json.JSONObject
import java.io.File
import java.io.IOException

class AppInfoControl : CTInfoControl<AppInfo?>() {
    override val infoBasicFile: File
        get() = File(CTInfo.DATA_PATH, "appFileInfo.json")

    override fun setInfo(appInfo: AppInfo?) {
        val jsonObject = JSONObject()

        if (appInfo?.messageShowTime != -1) {
            jsonObject.put("SSMDWaitTime", appInfo?.messageShowTime)
        }
        jsonObject.put("joinInsiderProgram", appInfo?.joinInsiderProgram)

        val io = IOForInfo(infoBasicFile)
        try {
            io.setInfo(jsonObject.toString())
        } catch (e: IOException) {
            Log.err.print(javaClass, "保存应用信息失败", e)
        }
    }

    override fun refreshInfo(): AppInfo {
        try {
            val io = IOForInfo(infoBasicFile)
            val info = io.infos
            if (info != "err") {
                val jsonObject = JSONObject(info)
                return AppInfo(
                    jsonObject.optInt("SSMDWaitTime", 5),
                    jsonObject.optBoolean("joinInsiderProgram", false)
                )
            }
        } catch (e: IOException) {
            Log.err.print(javaClass, "获取应用信息失败", e)
        }
        return AppInfo(5, false)
    }
}
