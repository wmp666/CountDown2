package com.wmp.publicTools.io

import com.wmp.publicTools.printLog.Log
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object ResourceLocalizer {
    // 将内置文件复制到指定目录
    @JvmStatic
    fun copyEmbeddedFile(outputPath: String, inputPath: String, fileName: String) {
        val file = File(outputPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        try {
            ResourceLocalizer::class.java.getResourceAsStream(inputPath + fileName).use { `is` ->  // 获取资源流
                if (`is` == null) {
                    Log.err.print(
                        ResourceLocalizer::class.java,
                        "内置文件[" + ResourceLocalizer::class.java.getResource(inputPath + fileName) + "]未找到"
                    )
                    return
                }

                Files.createDirectories(Paths.get(inputPath, "video"))
                Files.copy(
                    `is`,
                    Paths.get(outputPath, fileName),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        } catch (e: IOException) {
            Log.err.print(ResourceLocalizer::class.java, "文件[$fileName]本地化失败", e)
        }
    }

    fun copyWebFile(outputPath: String, webPath: String?, fileName: String?) {
        val file = File(outputPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        val targetFile = File("$outputPath/$fileName")
        if (targetFile.exists()) {
            return
        }
        Log.info.print("ResourceLocalizer-下载文件", "开始下载文件:$webPath")
        DownloadURLFile.downloadWebFile(null, null, webPath, outputPath)
    }
}
