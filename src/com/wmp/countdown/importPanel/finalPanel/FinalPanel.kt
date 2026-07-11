package com.wmp.countdown.importPanel.finalPanel

import com.wmp.Main.isHasTheArg
import com.wmp.countdown.CTComponent.CTButton.CTIconButton
import com.wmp.countdown.CTComponent.CTButton.CTRoundTextButton
import com.wmp.countdown.CTComponent.CTPanel.CTViewPanel
import com.wmp.countdown.frame.AboutDialog
import com.wmp.countdown.frame.MainWindow
import com.wmp.countdown.infSet.InfSetDialog
import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.UITools.CTColor
import com.wmp.publicTools.appFileControl.CTInfoControl
import com.wmp.publicTools.appFileControl.IconControl
import com.wmp.publicTools.printLog.Log
import com.wmp.publicTools.update.GetNewerVersion.checkForUpdate
import java.awt.Dimension
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JOptionPane

class FinalPanel : CTViewPanel<Any?>() {

    companion object {
        @JvmField
        val allButList = ArrayList<CTRoundTextButton?>()
    }

    init {
        this.setName("功能性按钮组")
        this.id = "FinalPanel"

        initPanel()

        initButton()
    }

    override fun setInfoControl(): CTInfoControl<Any?>? {
        return null
    }

    private fun initPanel() {
        this.setLayout(BoxLayout(this, BoxLayout.X_AXIS))
        this.setBackground(CTColor.backColor)
        // 添加弹性空间
        this.add(Box.createHorizontalGlue()) // 左侧弹簧
        this.add(Box.createRigidArea(Dimension(0, 0))) // 按钮间距
        this.add(Box.createHorizontalGlue()) // 右侧弹簧
    }

    private fun initButton() {

        allButList.clear()

        val settings = CTRoundTextButton("设置")
        settings.addActionListener{
            try {
                InfSetDialog()
            } catch (e: Exception) {
                Log.err.print(javaClass, "设置打开失败", e)
            }
        }
        allButList.add(settings)




        val about = CTRoundTextButton(
            "信息",
        )
        about.addActionListener {
            try {
                AboutDialog().isVisible = true
            } catch (e: Exception) {
                Log.err.print(javaClass, "打开失败", e)
            }
        }
        allButList.add(about)

        val update = CTRoundTextButton(
            "更新")
        update.addActionListener{ checkForUpdate(null, null, true) }
        allButList.add(update)

        // 自定义刷新方法
        val refresh = CTRoundTextButton(
            "刷新")
        refresh.addActionListener{ MainWindow.refresh() }
        allButList.add(refresh)

        //设置关闭按钮
        if (CTInfo.canExit) {
            val exit = CTRoundTextButton(
                "关闭")
            exit.addActionListener{
                val i = Log.info.showChooseDialog(null, "CTViewPanel-按钮组", "确认退出?")
                if (i == JOptionPane.YES_OPTION) {
                    Log.exit(0)
                }
            }



            allButList.add(exit)
        }

        val length = AtomicInteger()

        //按钮展示
        allButList.forEach(Consumer { ctButton: CTRoundTextButton? ->
            ctButton?.let { it.preferredSize = Dimension(ctButton.font.size*ctButton.text.length + 15, ctButton.font.size + 15) }
            length.getAndIncrement()
            this.add(ctButton)
        })


    }

    @Throws(IOException::class)
    override fun easyRefresh() {
        this.removeAll()

        initPanel()
        initButton()

        revalidate()
        repaint()
    }


}
