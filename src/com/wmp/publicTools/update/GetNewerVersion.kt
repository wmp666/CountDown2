package com.wmp.publicTools.update

import com.wmp.Main.isHasTheArg
import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.io.DownloadURLFile
import com.wmp.publicTools.io.GetPath
import com.wmp.publicTools.io.IOForInfo.Companion.copyFile
import com.wmp.publicTools.io.IOForInfo.Companion.deleteDirectoryRecursively
import com.wmp.publicTools.printLog.Log
import com.wmp.publicTools.web.GetWebInf.getWebInf
import com.wmp.publicTools.web.SslUtils.ignoreSsl
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import java.awt.Desktop
import java.awt.Window
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Consumer
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingWorker
import kotlin.math.max

object GetNewerVersion {
    private const val NEW_VERSION = 1
    private const val TEST_VERSION = 2
    private const val NewVerFileUrl = "https://api.github.com/repos/wmp666/CountDown2/releases/latest"
    var newerVersion: Int = 1
    var importUpdate: Int = 2
    private var view: JPanel? = null

    /**
     * 
     * @param updateMode 更新模式
     * @return [版本,下载地址,sha265,更新提示]
     */
    private fun getLatestVersion(updateMode: Int): Array<String> {
        try {
            val info = arrayOf("", "", "", "")
            if (updateMode == NEW_VERSION) {
                // 获取原始JSON响应
                val json = getWebInf(NewVerFileUrl)

                // 使用JSONObject解析
                val release = JSONObject(json)
                info[0] = release.getString("tag_name")
                info[3] = release.getString("body").replace("\\r\\n", "\n")

                // 获取准确下载地址
                val assets = release.getJSONArray("assets")
                for (i in 0..<assets.length()) {
                    val asset = assets.getJSONObject(i)
                    if (asset.getString("name").endsWith(".jar")) {
                        info[1] = asset.getString("browser_download_url")
                        info[2] = asset.getString("digest").split(":".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[1]
                        break
                    }
                }
            } else {
                // 获取原始JSON响应
                val json = getWebInf("https://api.github.com/repos/wmp666/ClassTools_JDK25/releases")

                val newBate = AtomicReference("0.0.0")
                val newRelease = AtomicReference<JSONObject?>()
                // 使用JSONObject解析
                val releases = JSONArray(json)
                //获取最新测试版
                releases.forEach(Consumer { release: Any? ->
                    val JSONRelease = release as JSONObject
                    if (JSONRelease.getBoolean("prerelease")) {
                        if (isNewerVersion(JSONRelease.getString("tag_name"), newBate.get()!!) != 0) {
                            newBate.set(JSONRelease.getString("tag_name"))
                            newRelease.set(JSONRelease)
                        }
                    }
                })

                info[0] = newBate.get()
                info[3] = newRelease.get()!!.getString("body").replace("\\r\\n", "\n")

                // 获取准确下载地址
                val assets = newRelease.get()!!.getJSONArray("assets")
                for (i in 0..<assets.length()) {
                    val asset = assets.getJSONObject(i)
                    if (asset.getString("name").endsWith(".jar")) {
                        info[2] = asset.getString("digest").split(":".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[1]
                        info[1] = asset.getString("browser_download_url")
                        break
                    }
                }
            }
            if (info[1] == "null") {
                Log.err.print(null, GetNewerVersion::class.java, "无法获取下载地址")
            } else {
                Log.info.print(null, "获取新版本", "获取下载地址成功")
            }
            return info
        } catch (e: Exception) {
            Log.err.systemPrint(GetNewerVersion::class.java, "版本获取失败", e)
        }
        return arrayOf<String>("", "", "", "")
    }

    /**
     * 检查更新
     *
     * @param dialog 父窗口
     * @param panel 显示窗
     * @param showMessage 是否显示提示
     * @param inquireUpdateWay 是否询问更新方式
     * @param skipVersionChecking 是否跳过版本检查
     */
    @JvmStatic
    fun checkForUpdate(
        dialog: Window?,
        panel: JPanel?,
        showMessage: Boolean,
        inquireUpdateWay: Boolean = true,
        skipVersionChecking: Boolean = false
    ) {
        if (isHasTheArg("屏保:展示")) {
            Log.err.print(null, GetNewerVersion::class.java, "屏保状态,无法更新")
            return
        }

        val updateMode: Int

        if (inquireUpdateWay) {
            val s = Log.info.showChooseDialog(dialog, "检查更新", "选择更新版本", "最新版", "测试版")
            //updateMode = s.equals() ? NEW_VERSION : TEST_VERSION;
            if (s == "最新版") updateMode = NEW_VERSION
            else if (s == "测试版") updateMode = TEST_VERSION
            else {
                Log.err.print(null, GetNewerVersion::class.java, "未设置更新方式")
                return
            }
        } else {
            updateMode = NEW_VERSION
        }


        panel?.removeAll()

        object : SwingWorker<Void?, Void?>() {
            lateinit var latestInfo: Array<String>

            override fun doInBackground(): Void? {
                val temp = getLatestVersion(NEW_VERSION)
                if (CTInfo.appInfo.joinInsiderProgram || updateMode == TEST_VERSION) {
                    val testLatestInfo = getLatestVersion(TEST_VERSION)
                    if (isNewerVersion(testLatestInfo[0], temp[0]) != 0) latestInfo = testLatestInfo
                    else latestInfo = temp
                } else latestInfo = temp
                return null
            }

            override fun done() {
                val i =
                    if (skipVersionChecking) 1 else isNewerVersion(latestInfo[0], CTInfo.version)

                val updateThread = Thread {
                    Log.warn.message(
                        dialog,
                        "更新至 " + latestInfo[0],
                        "即将开始更新, 无论更新是否完成都将关闭程序, 没有提醒"
                    )
                    deleteDirectoryRecursively(Path.of(CTInfo.TEMP_PATH + "UpdateFile\\"))

                    DownloadURLFile.downloadWebFile(
                        dialog,
                        panel,
                        latestInfo[1],
                        CTInfo.TEMP_PATH + "UpdateFile\\",
                        latestInfo[2]
                    )

                    try {
                        copyFile(
                            Path.of(CTInfo.TEMP_PATH, "UpdateFile", "ClassTools.jar"),
                            Path.of(GetPath.getAppPath(GetPath.SOURCE_FILE_PATH), "ClassTools.jar")
                        )
                    } catch (e: Exception) {
                        Log.err.print(dialog, GetNewerVersion::class.java, "更新文件失败", e)
                    }
                    System.exit(0)
                }

                if (updateMode == NEW_VERSION) {
                    if (i == 1) {
                        Log.info.print("发现新版本", "发现新版本 " + latestInfo[0])
                        val result = Log.info.showChooseDialog(
                            dialog, "发现新版本",
                            "发现新版本 " + latestInfo[0] + "，是否下载？\n" + latestInfo[3]
                        )


                        if (result == JOptionPane.YES_OPTION) {
                            updateThread.start()
                        }
                    } else {
                        Log.info.print("获取新版本", "当前已是最新版本")
                    }
                } else {
                    if (i != 0) {
                        val result = Log.info.showChooseDialog(
                            dialog, "发现测试版本",
                            "发现测试版本 " + latestInfo[0] + "，是否下载？\n" + latestInfo[3]
                        )


                        if (result == JOptionPane.YES_OPTION) {
                            updateThread.start()
                        }
                    } else {
                        if (showMessage) {
                            Log.info.message(dialog, "获取测试版本", "无最新测试版本")
                        } else {
                            Log.info.print("获取测试版本", "无最新测试版本")
                        }
                    }
                }
            }
        }.execute() // 开始执行异步任务
    }

    /**
     * 比较版本号
     * 
     * @param remote 新版本号
     * @param local  旧版本号
     * @return 0:已是最新，1:新版本，2:重要更新
     */
    @JvmStatic
    fun isNewerVersion(remote: String, local: String): Int {
        // 实现版本号比较逻辑（需根据你的版本号格式调整）
        var remoteParts: Array<String?> = remote.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var localParts: Array<String?> = local.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val maxSize = max(remoteParts.size, localParts.size)
        remoteParts = remoteParts.copyOf(maxSize)
        localParts = localParts.copyOf(maxSize)

        for (i in 0..<maxSize) {
            val remotePart = (remoteParts[i] ?: "0").toInt()
            val localPart = (localParts[i] ?: "0").toInt()
            if (remotePart > localPart) { //有最新版
                return newerVersion
            } else if (remotePart < localPart) {
                return 0
            }
        }
        return 0
    }


    fun getSource(dialog: Window?, panel: JPanel?) {
        if (panel != null) {
            view = panel
            view!!.removeAll()
        }


        object : SwingWorker<Void?, Void?>() {
            var sourceURL: String? = null

            @Throws(Exception::class)
            override fun doInBackground(): Void? {
                sourceURL = GetNewerVersion.sourceURL
                return null
            }

            override fun done() {
                if (sourceURL == null) {
                    Log.err.print(dialog, GetNewerVersion::class.java, "无法获取下载地址")
                    return
                }

                val result = Log.info.showChooseDialog(dialog, "发现新版本", "检测到源代码文件，是否下载？")

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        GetNewerVersion.downloadSource(sourceURL!!)
                    } catch (e: Exception) {
                        Log.err.print(dialog, GetNewerVersion::class.java, "下载失败", e)
                    }
                }
            }
        }.execute()
    }

    private val sourceURL: String?
        get() {
            ignoreSsl()
            try {
                // 获取原始JSON响应
                val json = Jsoup.connect(NewVerFileUrl)
                    .header("Accept", "application/vnd.github.v3+json")
                    .timeout(10000)
                    .ignoreContentType(true)
                    .execute()
                    .body()

                // 使用JSONObject解析
                val release = JSONObject(json)


                // 获取准确下载地址
                return release.getString("zipball_url")
            } catch (e: Exception) {
                Log.err.print(GetNewerVersion::class.java, "信息解析失败", e)
            }
            return null
        }

    @Throws(URISyntaxException::class, IOException::class)
    private fun downloadSource(downloadUrl: String) {
        Desktop.getDesktop().browse(URI(downloadUrl))
    }
}
