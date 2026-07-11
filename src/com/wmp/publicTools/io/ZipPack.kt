package com.wmp.publicTools.io

import com.wmp.publicTools.printLog.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object ZipPack {
    @JvmStatic
    fun unzip(zipFilePath: String?, destDir: String?): Thread? {
        //new File(destDir).delete();
        //生成一个弹窗显示解压进度
        Log.info.print("ZipPack-解压", "正在解压...")

        try {
            if (zipFilePath == null || !File(zipFilePath).exists()) {
                Log.err.print(ZipPack::class.java, "找不到压缩包!")
                return null
            }
        } catch (e: Exception) {
            Log.err.print(ZipPack::class.java, "找不到压缩包!", e)

            return null
        }
        val id = Random().nextInt()

        Log.info.loading.showDialog("ZipPack-解压$id", "正在解压...")

        val thread = Thread {
            try {
                val zipInputStream = ZipInputStream(FileInputStream(zipFilePath))
                // 解压缩文件
                unzipFiles(zipInputStream, destDir, id)

                Log.info.print("ZipPack-解压", "解压完成!")
            } catch (e: IOException) {
                Log.err.print(ZipPack::class.java, "解压失败！", e)
                throw RuntimeException(e)
            }
            Log.info.loading.closeDialog("ZipPack-解压$id")
        }
        thread.start()


        return thread
    }

    @Throws(IOException::class)
    private fun unzipFiles(zipInputStream: ZipInputStream, outputFolder: String?, id: Int) {
        val buffer = ByteArray(1024)
        var entry: ZipEntry?

        // 遍历压缩文件中的每个文件
        while (true) {
            try {
                entry = zipInputStream.getNextEntry()
                if (entry == null) {
                    break
                }
            } catch (e: IllegalArgumentException) {
                Log.warn.print(ZipPack::class.java.toString(), "文件名解码错误:\n$e")
                // 跳过这个损坏的条目
                continue
            }
            // 处理文件
            val fileName = entry.getName()
            val outputFile = File("$outputFolder/$fileName")

            Log.info.print("ZipPack-解压", "解压文件: $outputFile")
            Log.info.loading.updateDialog("ZipPack-解压$id", "解压文件: $outputFile")
            // 创建文件夹
            if (entry.isDirectory) {
                outputFile.mkdirs()
            } else {
                // 创建文件并写入内容
                File(outputFile.getParent()).mkdirs()
                FileOutputStream(outputFile).use { fileOutputStream ->
                    var bytesRead: Int
                    while ((zipInputStream.read(buffer).also { bytesRead = it }) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead)
                    }
                }
            }

            zipInputStream.closeEntry()
        }
    }

    @JvmStatic
    fun createZip(outputPath: String?, dataPath: String, zipName: String?, vararg ZipFiles: String?) {
        Log.info.print("ZipPack-压缩", "开始压缩...")
        Log.info.print("ZipPack-压缩", "压缩:$dataPath->$outputPath")
        if (ZipFiles.isNotEmpty()) {
            Log.info.print("ZipPack-压缩", "要打包的文件:" + ZipFiles.contentToString())
        } else Log.info.print("ZipPack-压缩", "要打包的文件:全部")

        val id = Random().nextInt()

        Log.info.loading.showDialog("ZipPack-压缩$id", "正在压缩...")


        // String zipName = zipName;
        Thread {
            // 在后台线程执行压缩操作
            try {
                ZipOutputStream(
                    FileOutputStream(outputPath + File.separator + zipName)
                ).use { zos ->
                    addFolderToZip(File(dataPath), "", zos, id, *ZipFiles)
                    // 压缩完成后更新UI
                    Log.info.print("ZipPack-压缩", "压缩完成!")
                }
            } catch (e: IOException) {
                Log.err.print(ZipPack::class.java, "压缩失败！", e)
            }
            Log.info.loading.closeDialog("ZipPack-压缩$id")
        }.start()
    }

    // 优化的压缩方法
    @Throws(IOException::class)
    private fun addFolderToZip(
        folder: File,
        parentPath: String?,
        zos: ZipOutputStream,
        id: Int,
        vararg ZipFiles: String?
    ) {
        for (file in Objects.requireNonNull(folder.listFiles())) {
            if (ZipFiles.isNotEmpty()) {
                val b = listOf(*ZipFiles).contains(file.getName())
                if (!b) {
                    // 跳过不压缩的文件
                    continue
                }
            }

            val entryName = parentPath + file.getName()

            Log.info.print("ZipPack-压缩", "压缩文件: $entryName")
            if (file.isDirectory()) {
                // 处理目录时添加"/"后缀
                zos.putNextEntry(ZipEntry("$entryName/"))
                zos.closeEntry()
                addFolderToZip(file, "$entryName/", zos, id)
            } else {
                Log.info.loading.updateDialog("ZipPack-压缩$id", "压缩文件: $entryName")
                FileInputStream(file).use { fis ->
                    val entry = ZipEntry(entryName)
                    zos.putNextEntry(entry)

                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while ((fis.read(buffer).also { bytesRead = it }) != -1) {
                        zos.write(buffer, 0, bytesRead)
                    }
                    zos.closeEntry()
                }
            }
        }
    }
}
