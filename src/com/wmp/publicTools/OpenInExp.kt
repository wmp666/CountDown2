package com.wmp.publicTools

import com.wmp.publicTools.printLog.Log
import java.awt.Desktop
import java.io.File
import java.io.IOException

object OpenInExp {
    @JvmStatic
    fun open(path: String) {
        try {
            // 获取可靠的项目工作目录
            val targetDir = File(path)

            // 校验父目录有效性
            if (!targetDir.exists()) {
                Log.err.print(OpenInExp::class.java, "不存在的位置")
                throw IOException("Parent directory does not exist")
            }
            // 使用跨平台方式打开文件管理器
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(targetDir)
            } else {
                // 兼容回退方案
                Runtime.getRuntime().exec(arrayOf<String>("cmd", "/c", "start", targetDir.getAbsolutePath()))
            }
        } catch (ex: IOException) {
            Log.err.print(OpenInExp::class.java, "错误", ex)
        }
    }
}
