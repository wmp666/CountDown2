package com.wmp.publicTools.web

import com.wmp.publicTools.printLog.Log
import org.jsoup.Jsoup
import java.util.*

object GetWebInf {
    @JvmStatic
    fun getWebInf(apiUrl: String) = getWebInf(apiUrl, true)

    @JvmStatic
    fun getWebInf(apiUrl: String, showDialog: Boolean): String {
        val id = Random().nextInt()
        SslUtils.ignoreSsl()
        if (showDialog) {
            Log.info.loading.showDialog("网络信息获取-$id", "正在获取Web信息...")
        }
        try {
            // 获取原始JSON响应
            val webInf = Jsoup.connect(apiUrl)
                .header("Accept", "application/vnd.github.v3+json") //.timeout(60000)
                .ignoreContentType(true)
                .execute()
                .body()

            Log.info.print("GetWebInf", "获取Web信息成功")
            Log.info.print("GetWebInf", "信息: $webInf")
            if (showDialog) {
                Log.info.loading.closeDialog("网络信息获取-$id")
            }
            return webInf
        } catch (e: Exception) {
            Log.err.systemPrint(GetWebInf::class.java, "获取Web信息失败", e)
        }
        if (showDialog) {
            Log.info.loading.closeDialog("网络信息获取-$id")
        }
        return ""
    }
}
