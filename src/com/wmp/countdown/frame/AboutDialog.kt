package com.wmp.countdown.frame

import com.formdev.flatlaf.FlatLaf
import com.nlf.calendar.Lunar
import com.wmp.countdown.CTComponent.CTButton.*
import com.wmp.countdown.CTComponent.*
import com.wmp.countdown.CTComponent.Menu.*
import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.OpenInExp
import com.wmp.publicTools.UITools.CTColor
import com.wmp.publicTools.UITools.CTFont
import com.wmp.publicTools.UITools.CTFontSizeStyle
import com.wmp.publicTools.UITools.GetIcon
import com.wmp.publicTools.appFileControl.IconControl
import com.wmp.publicTools.io.GetPath
import com.wmp.publicTools.printLog.Log
import com.wmp.publicTools.update.GetNewerVersion
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel

class AboutDialog : JDialog() {
    init {
        this.setTitle("关于")
        this.setSize((300 * CTInfo.dpi).toInt(), (400 * CTInfo.dpi).toInt())
        this.setLayout(BorderLayout())
        this.setLocationRelativeTo(null)
        this.setModal(true)
        this.setResizable(false)
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE)
        this.contentPane.setBackground(CTColor.backColor)


        val icon = JLabel(GetIcon.getIcon("系统.图标", IconControl.COLOR_DEFAULT, 100, 100))


        val info = JLabel(
            ("<html>"
                    + "程序名: " + CTInfo.appName + "<br><br>"
                    + "作者: " + CTInfo.author + "<br><br>"
                    + "版本: " + CTInfo.version
                    + "</html>")
        )
        info.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL))
        info.setForeground(CTColor.textColor)

        //view = new JPanel();
        val infos = JPanel()
        infos.setOpaque(false)
        infos.setLayout(BorderLayout())
        infos.add(icon, BorderLayout.WEST)
        infos.add(info, BorderLayout.CENTER)

        val getNew = CTIconButton(
            "检查更新",
            "通用.网络.更新", IconControl.COLOR_COLORFUL,
            { GetNewerVersion.checkForUpdate(this, view, true) })
        getNew.setBackground(CTColor.backColor)

        val update = JPanel(GridBagLayout())
        update.setOpaque(false)
        run {
            val gbc = GridBagConstraints()
            gbc.gridx = 0
            gbc.gridy = 0
            gbc.insets = Insets(5, 5, 5, 5)
            update.add(view, gbc)
            gbc.gridy++
            update.add(getNew.toRoundTextButton(), gbc)
        }

        this.add(update, BorderLayout.SOUTH)
        this.add(infos, BorderLayout.CENTER)

        initMenuBar()
    }

    private fun initMenuBar() {
        val menuBar = CTMenuBar()
        this.jMenuBar = menuBar

        val menu = CTMenu("转到")

        val chat = CTMenu("社交")

        val weChat = CTMenuItem("微信")
        weChat.setIcon(GetIcon.getIcon("关于.微信", 20, 20))
        weChat.addActionListener({ _: ActionEvent? ->
            Log.info.message(
                this,
                "关于-个人信息",
                "微信: w13607088913"
            )
        }
        )

        val qq = CTMenuItem("QQ")
        qq.setIcon(GetIcon.getIcon("关于.QQ", 20, 20))
        qq.addActionListener({_ ->
            Log.info.message(
                this,
                "关于-个人信息",
                "QQ: 2134868121"
            )
        })

        val bilibili = CTMenuItem("哔哩哔哩")
        bilibili.setIcon(GetIcon.getIcon("关于.哔哩哔哩", 20, 20))
        bilibili.addActionListener({_ ->
            try {
                Desktop.getDesktop().browse(URI("https://space.bilibili.com/1075810224/"))
            } catch (ex: Exception) {
                Log.err.print(javaClass, "网页打开失败", ex)
            }
        })

        chat.add(weChat)
        chat.add(qq)
        chat.add(bilibili)

        val github = CTMenu("Github")
        github.setIcon(GetIcon.getIcon("关于.Github", 20, 20))

        val authorGithub = CTMenuItem("作者")
        authorGithub.setIcon(GetIcon.getIcon("关于.Github", 20, 20))
        authorGithub.addActionListener({_ ->
            try {
                Desktop.getDesktop().browse(URI("https://github.com/wmp666"))
            } catch (ex: Exception) {
                Log.err.print(javaClass, "网页打开失败", ex)
            }
        })

        val repo = CTMenuItem("仓库")
        repo.setIcon(GetIcon.getIcon("关于.Github", 20, 20))
        repo.addActionListener({_ ->
            try {
                Desktop.getDesktop().browse(URI("https://github.com/wmp666/ClassTools"))
            } catch (ex: Exception) {
                Log.err.print(javaClass, "网页打开失败", ex)
            }
        })

        github.add(authorGithub)
        github.add(repo)

        val appPath = CTMenuItem("程序路径")
        appPath.setIcon(GetIcon.getIcon("通用.文件.文件夹", 20, 20))
        appPath.addActionListener({_ -> OpenInExp.open(GetPath.getAppPath(GetPath.APPLICATION_PATH)) })

        val dataPath = CTMenuItem("数据路径")
        dataPath.setIcon(GetIcon.getIcon("通用.文件.文件夹", 20, 20))
        dataPath.addActionListener({_ -> OpenInExp.open(CTInfo.DATA_PATH) })

        menu.add(chat)
        menu.add(github)
        menu.addSeparator()
        menu.add(appPath)
        menu.add(dataPath)

        menuBar.add(menu)

        // 在现有菜单中添加
        val downloadMenu = CTMenu("下载")

        //获取源代码
        val getSource = CTMenuItem("获取源代码")
        getSource.addActionListener({ _ -> GetNewerVersion.getSource(this, view) })

        val checkUpdate = CTMenuItem("检查更新")
        checkUpdate.setIcon(GetIcon.getIcon("通用.网络.更新", 20, 20))
        checkUpdate.addActionListener({_ ->
            GetNewerVersion.checkForUpdate(
                this,
                view,
                true
            )
        })

        val getLatest = CTMenuItem("强制下载指定版本")
        getLatest.setIcon(GetIcon.getIcon("通用.网络.更新", 20, 20))
        getLatest.addActionListener({_: ActionEvent? ->
            GetNewerVersion.checkForUpdate(
                this,
                view,
                true,
                true,
                true
            )
        })

        downloadMenu.add(getSource)
        downloadMenu.add(checkUpdate)
        downloadMenu.add(getLatest)

        menuBar.add(downloadMenu)

    }


    companion object {
        private val view = JPanel()

        init {
            view.setBackground(CTColor.backColor)
            view.setLayout(BorderLayout())
            view.setPreferredSize(Dimension((300 * CTInfo.dpi).toInt(), (100 * CTInfo.dpi).toInt()))

            GetNewerVersion.checkForUpdate(null, view, false, false)
        }
    }
}
