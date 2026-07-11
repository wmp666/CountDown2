package com.wmp.publicTools.io

import com.formdev.flatlaf.util.SystemFileChooser
import com.wmp.publicTools.printLog.Log
import java.awt.Component
import java.io.File
import java.net.URISyntaxException
import javax.swing.JFileChooser

object GetPath {
    const val APPLICATION_PATH: Int = 1
    const val SOURCE_FILE_PATH: Int = 0


    /**
     * 获取应用程序路径
     * 
     * @param type 1: 应用程序路径 0: 源文件路径
     */
    fun getAppPath(type: Int): String {
        try {
            return if (type == SOURCE_FILE_PATH) programDirectory.path
            else programDirectory.getParent()
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    @get:Throws(URISyntaxException::class)
    private val programDirectory: File
        /**
         * 获取程序真实所在目录（兼容管理员权限模式）
         */
        get() {
            // 通过类加载器获取代码源位置
            val jarFile = File(
                GetPath::class.java
                    .protectionDomain
                    .codeSource
                    .location
                    .toURI()
            )

            // 如果是JAR文件，返回所在目录；如果是IDE运行，返回class文件目录
            return if (jarFile.isFile()) {
                jarFile.getParentFile()
            } else {
                // 开发环境中返回项目编译输出目录
                File(jarFile.path)
            }
        }

    @JvmStatic
    @Throws(RuntimeException::class)
    fun getFilePath(c: Component?, title: String?, fileType: String, fileName: String?): String? {
        val chooser = SystemFileChooser()
        chooser.dialogTitle = title

        //将文件过滤器设置为只显示.fileType 或 文件夹
        val fileTypes: Array<String?> =
            fileType.replace(".", "").split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        chooser.addChoosableFileFilter(SystemFileChooser.FileNameExtensionFilter(fileName, *fileTypes))

        //获取文件路径
        if (chooser.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {
            try {
                val filePath = chooser.selectedFile.absolutePath
                //获取文件名
                val chooseFileName = chooser.selectedFile.getName()
                //获取文件名后缀
                val fileSuffix = chooseFileName.substring(chooseFileName.lastIndexOf("."))
                //获取文件名前缀
                val filePrefix = chooseFileName.substring(0, chooseFileName.lastIndexOf("."))

                Log.info.print(
                    "文件选择器",
                    "文件路径：$filePath|文件名: $chooseFileName|文件后缀: $fileSuffix|文件前缀: $filePrefix"
                )
                return filePath
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
        }
        return null
    }

    @JvmStatic
    fun getDirectoryPath(c: Component?, title: String?): String? {
        val chooser = SystemFileChooser()
        chooser.dialogTitle = title
        chooser.isFileHidingEnabled = false
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
        //返回文件路径
        if (chooser.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) { // 如果点击了"确定"按钮
            try {
                val filePath = chooser.selectedFile.absolutePath
                println("文件路径：$filePath")
                return filePath
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
        }
        return null
    }
}
