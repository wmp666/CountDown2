package com.wmp.publicTools.io

import com.wmp.publicTools.printLog.Log
import java.io.*
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class IOForInfo{
    private val file: File

    constructor(file: File) {
        this.file = file
    }

    constructor(file: String) {
        this.file = File(file)
    }

    constructor(file: URI) {
        this.file = File(file)
    }

    @Throws(IOException::class)
    fun getInfo(): Array<String> {
        val s = this.infos
        if (s == "err") {
            return arrayOf("err")
        }
        return s.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    }
    @Throws(IOException::class)
    fun setInfo(vararg infos: String) {
        if (!file.exists()) {
            if (!creativeFile(file)) {
                Log.err.print(IOForInfo::class.java, file.path + "文件无法创建")
                return
            }
        }

        try {
            OutputStreamWriter(
                FileOutputStream(file), StandardCharsets.UTF_8
            ).use { writer ->  // 明确指定编码
                Log.info.print("IOForInfo-设置数据", file.path + "文件开始写入")


                val inf = java.lang.String.join("\n", *infos)
                Log.info.print("IOForInfo-设置数据", "数据内容: $inf")
                writer.write(inf)
                writer.flush()
                BufferedReader(
                    InputStreamReader(
                        FileInputStream(file), StandardCharsets.UTF_8
                    )
                ).use { reader ->
                    val content = StringBuilder()
                    var line: String?
                    while ((reader.readLine().also { line = it }) != null) {
                        content.append(line)
                    }
                }
            }
        } catch (e: IOException) {
            Log.err.print(IOForInfo::class.java, file.path + "文件写入失败", e)
        }
    }


    @get:Throws(IOException::class)
    val infos: String
        get() {
            if (!file.exists()) {
                if (!creativeFile(file)) {
                    Log.err.print(IOForInfo::class.java, file.path + "文件无法创建")
                    return "err"
                }
            }


            try {
                BufferedReader(
                    InputStreamReader(
                        FileInputStream(file), StandardCharsets.UTF_8
                    )
                ).use { reader ->  // 明确指定编码
                    Log.info.print("IOForInfo-获取数据", file.path + "文件开始读取")
                    val content = StringBuilder()
                    var line: String?
                    while ((reader.readLine().also { line = it }) != null) {
                        content.append(line).append("\n")
                    }

                    var s = ""
                    if (!content.isEmpty()) {
                        //去除文字中的空格
                        s = content.deleteCharAt(content.length - 1).toString().trim { it <= ' ' }
                    }

                    val replace = s.replace("\n", "\\n")

                    Log.info.print("IOForInfo-获取数据", "数据内容: $replace")
                    Log.info.print("IOForInfo-获取数据", file.path + "文件读取完成")
                    reader.close()
                    return if (!s.isEmpty()) s else "err"
                }
            } catch (e: IOException) {
                Log.err.print(IOForInfo::class.java, file.path + "文件读取失败", e)
                return "err"
            }
        }

    @Throws(IOException::class)
    private fun creativeFile(file: File): Boolean {
        Log.info.print("IOForInfo-创建文件", file.path + "文件创建")
        file.getParentFile().mkdirs()
        return file.createNewFile()
    }

    override fun toString(): String {
        try {
            return "IOForInfo{" +
                    "file=" + file +
                    " Inf=" + this.infos +
                    '}'
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }


    companion object {
        @JvmStatic
        fun getInfo(file: String): Array<String>? {
            val infos: String? = getInfos(file)
            if (infos == "err") {
                return arrayOf("err")
            }
            return infos?.split("\n".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        }

        fun getInfo(file: URL): Array<String> {
            val infos: String? = getInfos(file)
            if (infos == "err") {
                return arrayOf("err")
            }
            return infos!!.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }

        @JvmStatic
        fun getInfos(file: String): String? {
            try {
                val file1 = File(file)
                if (!file1.exists()) return "err"
                return getInfos(file1.toURI().toURL())
            } catch (e: MalformedURLException) {
                Log.err.print(IOForInfo::class.java, file + "文件读取失败", e)
                return "err"
            }
        }

        @JvmStatic
        fun getInfos(file: URL): String? {
            try { // 明确指定编码
                val reader = BufferedReader(
                    InputStreamReader(
                        file.openStream(), StandardCharsets.UTF_8
                    )
                )
                val sb = StringBuilder()
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    if (line!!.startsWith("#")) {
                        continue
                    }
                    if (line.isEmpty()) {
                        continue
                    }
                    sb.append(line).append("\n")
                }
                Log.info.print("IOForInfo-获取数据", "数据内容: $sb")
                return sb.toString() // 读取第一行
            } catch (e: IOException) {
                Log.err.print(IOForInfo::class.java, file.path + "文件读取失败", e)
            }
            return null
        }

        @JvmStatic
        @JvmOverloads
        fun deleteDirectoryRecursively(path: Path, callback: Runnable? = null) {
            Log.info.print("删除文件", "删除")

            Log.info.loading.showDialog("文件删除", "正在删除文件...")

            val file = File(path.toUri())

            //  执行耗时操作
            try {
                if (!file.exists()) {
                    Log.err.print(IOForInfo::class.java, "目标不存在")
                }

                if (file.isDirectory()) {
                    Files.walk(file.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map { obj: Path? -> obj!!.toFile() }
                        .forEach { obj: File? -> obj!!.delete() }
                }

                if (file.delete() || !file.exists()) {
                    Log.info.message(null, "IOForInfo-删除文件", "删除文件/文件夹: $path")
                } else {
                    val errorType = if (file.canWrite()) "文件被占用" else "权限不足"
                    Log.err.print(IOForInfo::class.java, "删除失败$errorType")
                }
            } catch (e: Exception) {
                Log.err.print(IOForInfo::class.java, "删除失败", e)
            }

            Log.info.loading.closeDialog("文件删除")
            callback?.run()
        }

        @JvmStatic
        fun copyFile(source: Path, target: Path) {
            val id = Random().nextInt()
            Log.info.loading.showDialog("更新文件$id", "正在复制文件...")

            try {
                val sourceFile = source.toFile()
                val targetFile = target.toFile()

                if (!sourceFile.exists()) {
                    Log.info.loading.closeDialog("更新文件$id")
                    Log.err.print(IOForInfo::class.java, "源文件不存在")
                    return
                }


                if (!targetFile.exists()) {
                    targetFile.getParentFile().mkdirs()
                    targetFile.createNewFile()
                }


                val targetOut = FileOutputStream(targetFile)
                val sourceIn = FileInputStream(sourceFile)


                val temp = ByteArray(1024 * 10)
                var total2 = 0

                while (true) {
                    val i = sourceIn.read(temp)
                    if (i == -1) break
                    targetOut.write(temp, 0, i)
                    total2 += i

                    // 更新进度条
                    val finalTotal = ((total2 * 100).toFloat() / sourceFile.length())
                    Log.info.loading.updateDialog(
                        "更新文件$id",
                        String.format("正在拷贝文件... %02f%%", finalTotal),
                        finalTotal.toInt()
                    )
                }

                targetOut.close()
                sourceIn.close()
            } catch (e: IOException) {
                Log.err.print(IOForInfo::class.java, "文件复制失败", e)
            }

            Log.info.loading.closeDialog("更新文件$id")
        }
    }
}
