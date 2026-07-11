package com.wmp.publicTools

import com.wmp.Main
import com.wmp.Main.getTheArgNextArg
import com.wmp.countdown.CTComponent.CTPanel.CTViewPanel
import com.wmp.countdown.frame.MainWindow
import com.wmp.countdown.infSet.panel.personalizationSets.control.PBasicInfoControl
import com.wmp.countdown.infSet.panel.personalizationSets.control.PPanelInfoControl
import com.wmp.publicTools.EasterEgg.EasterEggModeMap
import com.wmp.publicTools.UITools.CTColor
import com.wmp.publicTools.UITools.CTFont
import com.wmp.publicTools.appFileControl.IconControl
import com.wmp.publicTools.appFileControl.appInfoControl.AppInfo
import com.wmp.publicTools.appFileControl.appInfoControl.AppInfoControl
import com.wmp.publicTools.printLog.Log
import java.awt.Taskbar
import java.awt.TrayIcon
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.function.Consumer
import javax.swing.UIManager
import javax.swing.plaf.ColorUIResource

open class CTInfo {
    companion object {
        //禁用的组件和按钮
        @JvmField
        val disPanelList: ArrayList<String?> = ArrayList()

        @JvmField
        val disButList: ArrayList<String?> = ArrayList()

        //控件数据
        const val arch: Int = 15
        const val arcw: Int = 15

        @JvmField
        var dpi: Double = 1.0

        @JvmField
        var isButtonUseMainColor: Boolean = false

        //数据位置
        @JvmField
        var DATA_PATH: String = "null"

        @JvmField
        var TEMP_PATH: String = "null"

        @JvmField
        var APP_INFO_PATH: String = "null"

        //基础数据
        @JvmField
        var appName: String = "倒计时"
        var author: String = "无名牌"

        @JvmField
        var iconPath: String = "/image/icon/icon.png"

        //其他
        @JvmField
        var easterEggModeMap
                : EasterEggModeMap = EasterEggModeMap(
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            null,
            null,
            "light",
            true,
            null,
            "版本",
            "作者",
            "软件名称",
            "图标路径",
            "提示窗标题",
            "提示窗是否使用图标",
            "背景色",
            "文字色",
            "主题色",
            "主题模式",
            "是否可以退出",
            "彩蛋启动运行"
        )

        @JvmField
        var canExit: Boolean = true
        var StartUpdate: Boolean = true

        @JvmField
        var version: String = Main.VERSION

        @JvmField
        var appInfo: AppInfo = AppInfo(5, false)

        @JvmStatic
        fun init(getCTAppInfoNewerVersion: Boolean) {
            if (MainWindow.mainWindow != null && Taskbar.isTaskbarSupported()) {
                val taskbar = Taskbar.getTaskbar()

                if (taskbar.isSupported(Taskbar.Feature.USER_ATTENTION_WINDOW)) {
                    taskbar.requestWindowUserAttention(MainWindow.mainWindow)
                }

                if (taskbar.isSupported(Taskbar.Feature.PROGRESS_STATE_WINDOW)) {
                    taskbar.setWindowProgressState(MainWindow.mainWindow, Taskbar.State.INDETERMINATE)
                }
            }

            initCTRunImportInfo()


            initCTBasicInfo(getCTAppInfoNewerVersion)


            if (MainWindow.mainWindow != null && Taskbar.isTaskbarSupported()) {
                val taskbar = Taskbar.getTaskbar()

                if (taskbar.isSupported(Taskbar.Feature.PROGRESS_STATE_WINDOW)) {
                    taskbar.setWindowProgressState(MainWindow.mainWindow, Taskbar.State.OFF)
                }
            }
        }

        private fun initCTRunImportInfo() {
            dpi = 1.0
            disButList.clear()
            disPanelList.clear()

            //加载基础目录
            var path = System.getenv("LOCALAPPDATA")
            val s = getTheArgNextArg("BasicDataPath")
            if (s == null) {
                if (path != null && !path.isEmpty()) {
                    val file = File(path, "\\CountDown\\basicDataPath.txt")
                    if (file.exists() && file.isFile()) {
                        try {
                            path = File(Files.readString(file.toPath(), StandardCharsets.UTF_8)).getAbsolutePath()
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                }
            } else path = s

            DATA_PATH = "$path\\CountDown\\"
            TEMP_PATH = "$path\\CountDownTemp\\"

            iconPath = "/image/icon/icon.png"

            iconPath = easterEggModeMap.getString("图标路径", iconPath)

            initCTInfo()
        }

        private fun initCTBasicInfo(getNewerVersion: Boolean) {
            appInfo = AppInfoControl().getInfo()!!

            if (appInfo.joinInsiderProgram) {
                val versionArr = IntArray(5)
                val versionStr: Array<String?> =
                    version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in versionStr.indices) {
                    versionArr[i] = versionStr[i]!!.toInt()
                }
                val result = versionArr.copyOf(5)
                version = "${result[0]}.${result[1]}.${result[2]}.${result[3]}.${result[4]}"

                Log.trayIcon.displayMessage(
                    "提示",
                    "您加入了测试计划,可能遇到大量为未经验证的更新,请关注",
                    TrayIcon.MessageType.WARNING
                )
            }

            //设置默认字体
            val textColorUIResource = ColorUIResource(CTColor.textColor)
            val backColorUIResource = ColorUIResource(CTColor.backColor)
            val keys = UIManager.getDefaults().keys()
            while (keys.hasMoreElements()) {
                val key = keys.nextElement()
                val value = UIManager.get(key)
                if (value is ColorUIResource) {
                    if (key.toString().endsWith("background")) UIManager.put(key, backColorUIResource)
                    else if (key.toString().endsWith("foreground")) UIManager.put(key, textColorUIResource)
                }
            }

            IconControl.init(getNewerVersion)
            Log.initTrayIcon()
        }

        private fun initCTInfo() {
            val basicInfo = PBasicInfoControl().getInfo()
            val panelInfo = PPanelInfoControl().getInfo()

            //基础数据

            //颜色数据

            val themeColor = CTColor.getThemeColor(basicInfo.mainTheme)
            CTColor.setColorList(
                easterEggModeMap.getColor("主题色", CTColor.getParticularColor(basicInfo.mainColor)),
                easterEggModeMap.getColor("背景色", themeColor[0]),
                easterEggModeMap.getColor("文字色", themeColor[1]),
                easterEggModeMap.getString("主题模式", basicInfo.mainTheme)
            )
            isButtonUseMainColor = basicInfo.buttonColor
            //字体数据
            CTFont.setFontName(basicInfo.fontName)
            //界面数据
            disButList.addAll(basicInfo.disposeButton)
            canExit = basicInfo.canExit
            StartUpdate = basicInfo.startUpdate
            if (basicInfo.isSaveLog) {
                Log.isSaveLog(true)
            }

            //组件数据
            MainWindow.allPanelList.forEach(Consumer { panel: CTViewPanel<*>? ->
                panel!!.isIgnoreState = panelInfo.runInBackgroundList.contains(panel.getID())
            })
            disPanelList.addAll(panelInfo.disPanelList)
            dpi = panelInfo.dpi
        }

        init {
            initCTRunImportInfo()
        }
    }
}
