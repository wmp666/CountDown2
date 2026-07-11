package com.wmp.publicTools.UITools

import com.wmp.publicTools.printLog.Log
import java.awt.Color
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.math.max
import kotlin.math.min

object SystemColor {


    fun getAccentColor(): Color {
        try {
            val os = System.getProperty("os.name").lowercase(Locale.getDefault())

            if (os.contains("win")) {
                // Windows系统
                val process = Runtime.getRuntime().exec(
                    arrayOf("reg", "query", "HKCU\\Software\\Microsoft\\Windows\\DWM\"", "/v", "AccentColor")
                )

                val reader = BufferedReader(
                    InputStreamReader(process.getInputStream())
                )

                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    if (line!!.contains("AccentColor")) {
                        val parts = line.trim { it <= ' ' }.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        var hex = parts[parts.size - 1]

                        if (hex.startsWith("0x")) hex = hex.substring(2)
                        val value = hex.toLong(16)

                        // 正确提取ARGB各通道值
                        //val alpha = ((value shr 24) and 0xFFL).toInt()
                        var red = (value and 0xFFL).toInt()
                        var green = ((value shr 8) and 0xFFL).toInt()
                        var blue = ((value shr 16) and 0xFFL).toInt()

                        // 确保所有值都在有效范围内
                        red = max(0, min(255, red))
                        green = max(0, min(255, green))
                        blue = max(0, min(255, blue))


                        return Color(red, green, blue)
                    }
                }
            }
        } catch (e: Exception) {
            Log.err.systemPrint(SystemColor::class.java, "获取系统主题设置失败", e)
        }
        return Color(0, 120, 215) // Windows默认蓝色
    }

    fun checkDarkMode(): Boolean {
        try {
            val os = System.getProperty("os.name").lowercase(Locale.getDefault())

            if (os.contains("win")) {
                val process = Runtime.getRuntime().exec(
                    arrayOf(
                        "reg",
                        "query",
                        "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize"
                    )
                )

                val reader = BufferedReader(
                    InputStreamReader(process.getInputStream())
                )

                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    if (line!!.contains("AppsUseLightTheme")) {
                        val parts = line.trim { it <= ' ' }.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        val value: String = parts[parts.size - 1]
                        return "0x0" == value || "0" == value
                    }
                }
            }
        } catch (e: Exception) {
            Log.err.systemPrint(SystemColor::class.java, "获取系统主题设置失败", e)
        }
        return false
    }

}
