package com.wmp.publicTools.printLog

import com.wmp.Main.isHasTheArg
import com.wmp.countdown.CTComponent.CTButton.CTIconButton
import com.wmp.countdown.CTComponent.CTButton.CTRoundTextButton
import com.wmp.countdown.CTComponent.CTButton.CTTextButton
import com.wmp.countdown.CTComponent.CTOptionPane
import com.wmp.countdown.CTComponent.Menu.CTMenu
import com.wmp.countdown.CTComponent.Menu.CTMenuItem
import com.wmp.countdown.CTComponent.Menu.CTPopupMenu
import com.wmp.countdown.frame.MainWindow
import com.wmp.countdown.importPanel.finalPanel.FinalPanel
import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.OpenInExp.open
import com.wmp.publicTools.UITools.CTColor
import com.wmp.publicTools.UITools.CTFont.getCTFont
import com.wmp.publicTools.UITools.CTFont.getDefaultFont
import com.wmp.publicTools.UITools.CTFontSizeStyle
import com.wmp.publicTools.UITools.GetIcon.getIcon
import com.wmp.publicTools.UITools.GetIcon.getImageIcon
import com.wmp.publicTools.appFileControl.IconControl
import com.wmp.publicTools.io.GetPath
import com.wmp.publicTools.io.IOForInfo.Companion.copyFile
import java.awt.*
import java.awt.event.ActionEvent
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer
import javax.swing.*
import javax.swing.Timer
import kotlin.system.exitProcess

object Log {
    val trayIcon: TrayIcon = TrayIcon(
        getImageIcon(Log::class.java.getResource("/image/icon/icon.png"), 48, 48, false)!!.getImage(),
        "CountDown"
    )
    val logInfList: LinkedList<String?> = LinkedList<String?>()
    private val textArea = JTextArea()

    @JvmField
    var info: InfoLogStyle = InfoLogStyle(LogStyle.INFO)

    @JvmField
    var warn: WarnLogStyle = WarnLogStyle(LogStyle.WARN)

    @JvmField
    var err: ErrorLogStyle = ErrorLogStyle(LogStyle.ERROR)

    private var isSaveLog = true

    var logFilePath: String? = null
    private val thread = Thread {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        logFilePath = CTInfo.DATA_PATH + "Log\\log_" + dateFormat.format(Date()) + ".log"
        val logFile = File(logFilePath!!)
        if (!logFile.exists()) {
            logFile.getParentFile().mkdirs()
        }
        try {
            val writer = BufferedWriter(
                Files.newBufferedWriter(
                    Paths.get(logFilePath!!),
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE_NEW
                )
            )
            while (true) {
                if (!isSaveLog) continue
                synchronized(logInfList) { // 恢复同步块
                    try {
                        val currentSize = logInfList.size

                        for (i in 0..<currentSize) {
                            val logInfo = logInfList.removeFirst()
                            writer.write(logInfo + "\n")
                        }
                        Thread.sleep(34) // 刷新间隔
                    } catch (e: InterruptedException) {
                        err.systemPrint(Log::class.java, "日志保存异常", e)
                    }
                }
            }
        } catch (e: IOException) {
            err.print("系统操作", "日志路径获取异常")
            throw RuntimeException(e)
        }
    }

    init {
        if (SystemTray.isSupported()) {
            trayIcon.setImageAutoSize(true)
            val systemTray = SystemTray.getSystemTray()
            try {
                systemTray.add(trayIcon)
            } catch (e: AWTException) {
                throw RuntimeException(e)
            }
        }

        thread.setDaemon(true)
        thread.start() // 确保启动线程
    }

    fun initTrayIcon() {
        ctPopupMenu
    }

    @JvmStatic
    val ctPopupMenu: CTPopupMenu
        get() {
            val popupMenu = CTPopupMenu()

            val refresh = CTMenuItem("刷新")
            refresh.setIcon(getImageIcon("通用.刷新", IconControl.COLOR_COLORFUL, 20, 20))
            refresh.setFont(getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL))
            refresh.addActionListener { _: ActionEvent? -> MainWindow.refreshPanel() }
            popupMenu.add(refresh)

            val exit = CTMenuItem("关闭")
            exit.setIcon(getImageIcon("通用.关闭", IconControl.COLOR_COLORFUL, 20, 20))
            exit.setFont(getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL))
            exit.addActionListener { _: ActionEvent? -> exit(0) }
            popupMenu.add(exit)

            val hide = CTMenuItem("隐藏")
            hide.setIcon(getImageIcon("通用.删除", IconControl.COLOR_COLORFUL, 20, 20))
            hide.setFont(getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL))
            hide.addActionListener { _: ActionEvent? -> popupMenu.setVisible(false) }
            popupMenu.add(hide)
            return popupMenu
        }


    @JvmStatic
    fun exit(status: Int) {
        exitProcess(status)
    }

    private fun initBG(c: Container, window: JFrame, screenSize: Dimension) {
        //c.setBackground(Color.BLACK);
        (c as JPanel).setOpaque(false)


        val exitStrList = CTInfo.easterEggModeMap.getStringList(
            "关闭文字集", arrayOf(
                "愿此行，终抵群星",
                "我们终将重逢",
                "明天见",
                "为了与你重逢愿倾尽所有",
                "生命从夜中醒来\n却在触碰到光明的瞬间坠入永眠",
                "一起走向明天，我们不曾分离"
            )
        )
        var exitStr = exitStrList!![Random().nextInt(exitStrList.size)]
        if (exitStr.contains("\n")) {
            exitStr = "<html>" + exitStr.replace("\\n".toRegex(), "<br>") + "</html>"
        }

        //String result = "<html>" + exitStr.replaceAll("\\n", "<br>") + "</html>";
        val label = JLabel(exitStr) // 创建标签
        label.setForeground(Color.WHITE)
        label.setFont(getCTFont(Font.BOLD, CTFontSizeStyle.MORE_BIG))
        label.setHorizontalAlignment(SwingConstants.CENTER)
        label.setOpaque(false)

        c.add(label, BorderLayout.CENTER)

        val viewLabel = JLabel()
        //viewLabel.setBackground(Color.BLACK);
        run {
            //背景
            run {
                viewLabel.setBounds(0, 0, screenSize.width, screenSize.height)
                viewLabel.setIcon(
                    getIcon(
                        "系统.关闭页.$exitStr",
                        IconControl.COLOR_DEFAULT,
                        screenSize.width,
                        screenSize.height,
                        false
                    )
                )
            }
        }
        window.layeredPane.add(viewLabel, Integer.MIN_VALUE)

        window.layeredPane.repaint()
        viewLabel.revalidate()
        viewLabel.repaint()
    }

    fun systemPrint(style: LogStyle?, owner: String?, logInfo: String) {
        if (Objects.requireNonNull<LogStyle?>(style) == LogStyle.INFO) {
            CTOptionPane.showSystemStyleMessageDialog(TrayIcon.MessageType.INFO, owner, logInfo)
        }
        print(style!!, owner, logInfo, null, false)
    }

    @JvmOverloads
    fun print(style: LogStyle, owner: String?, logInfo: Any, c: Container?, showMessageDialog: Boolean = true) {
        val date = Date()
        val dateFormat: DateFormat = SimpleDateFormat("MM.dd HH:mm:ss")
        val dateStr = dateFormat.format(date)

        val info: String?
        when (style) {
            LogStyle.INFO -> {
                info = "[" + dateStr + "]" +
                        "[info]" +
                        "[" + owner + "]: " +
                        logInfo.toString().replace("\n", "[\\n]")
                println(info)
                logInfList.addLast(info)
            }

            LogStyle.WARN -> {
                info = "[" + dateStr + "]" +
                        "[warn]" +
                        "[" + owner + "] :" +
                        logInfo
                System.err.println(info)


                logInfList.addLast(info)
            }

            LogStyle.ERROR -> {
                info = "[" + dateStr + "]" +
                        "[error]" +
                        "[" + owner + "] :" +
                        logInfo
                System.err.println(info)
                logInfList.addLast(info)



            }
        }
    }

    @JvmOverloads
    fun showLogDialog(happenSystemErr: Boolean = false) {
        if (!happenSystemErr && isHasTheArg("屏保:展示")) {
            err.print(null, "系统", "屏保状态无法打开日志")
            return
        }

        info.loading.showDialog("log", "正在读取日志文件")

        textArea.text = ""
        try {
            val br = BufferedReader(Files.newBufferedReader(Path.of(logFilePath!!), StandardCharsets.UTF_8))
            while (br.ready()) {
                textArea.append(br.readLine() + "\n")
            }
            br.close()
        } catch (e: IOException) {
            textArea.text = "日志文件不存在"
            info.loading.closeDialog("log")
            err.print(Log::class.java, "读取日志文件失败", e)
        }
        info.loading.closeDialog("log")

        //dialog.removeAll();
        val dialog = JDialog(null as Frame?, false)
        dialog.setTitle("日志")
        dialog.setSize(500, 600)
        dialog.setLocationRelativeTo(null)
        dialog.layout = BorderLayout()
        dialog.contentPane.setBackground(CTColor.backColor)
        dialog.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE

        textArea.setFont(getDefaultFont(Font.PLAIN, CTFontSizeStyle.SMALL))
        textArea.isEditable = false

        val scrollPane = JScrollPane(textArea)
        scrollPane.setOpaque(false)
        scrollPane.getViewport().setOpaque(false)
        dialog.add(scrollPane, BorderLayout.CENTER)

        val buttonPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        buttonPanel.setOpaque(false)

        val closeButton = CTTextButton("关闭")
        closeButton.addActionListener { _: ActionEvent? ->
            dialog.dispose()
            if (happenSystemErr) {
                val i = CTOptionPane.showConfirmDialog(
                    dialog,
                    "系统",
                    "是否退出系统?",
                    null,
                    CTOptionPane.INFORMATION_MESSAGE,
                    true
                )

                if (i == CTOptionPane.YES_OPTION) exitProcess(-1)
            }
        }


        val clearButton = CTTextButton("清空")
        clearButton.addActionListener { _: ActionEvent? ->
            val i = info.showChooseDialog(dialog, "日志-清空", "是否清空并保存?")
            if (i == JOptionPane.YES_OPTION) {
                saveLog()
            }
            textArea.text = ""
            logInfList.clear()
        }

        val openButton = CTTextButton("打开所在位置")
        openButton.addActionListener { _: ActionEvent? ->
            if (!Files.exists(Paths.get(CTInfo.DATA_PATH + "Log\\"))) {
                try {
                    Files.createDirectories(Paths.get(CTInfo.DATA_PATH + "Log\\"))
                } catch (ex: IOException) {
                    throw RuntimeException(ex)
                }
            }
            open(CTInfo.DATA_PATH + "Log\\")
        }

        val saveButton = CTTextButton("保存至")
        saveButton.addActionListener { _: ActionEvent? -> saveLog() }
        buttonPanel.add(closeButton)
        buttonPanel.add(openButton)
        buttonPanel.add(clearButton)
        buttonPanel.add(saveButton)


        dialog.add(buttonPanel, BorderLayout.SOUTH)

        dialog.setAlwaysOnTop(true)
        dialog.isVisible = true
    }

    private fun saveLog(showMessageDialog: Boolean) {
        saveLog(GetPath.getDirectoryPath(null, "保存日志")!!, showMessageDialog)
    }

    private fun saveLog(path: String = GetPath.getDirectoryPath(null, "保存日志")!!, showMessageDialog: Boolean = true) {
        synchronized(logInfList) {
            try {
                copyFile(Paths.get(logFilePath!!), Paths.get(path, "Log.txt"))

                if (showMessageDialog) info.message(null, "Log", "日志保存成功")
            } catch (e: Exception) {
                err.print(Log::class.java, "日志保存失败", e)
                throw RuntimeException(e)
            }
        }
    }

    fun isSaveLog(b: Boolean) {
        isSaveLog = b
    }
}


