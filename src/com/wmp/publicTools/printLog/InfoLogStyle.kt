package com.wmp.publicTools.printLog

import com.wmp.Main.isHasTheArg
import com.wmp.countdown.CTComponent.CTOptionPane
import com.wmp.countdown.CTComponent.CTProgressBar.LoadingDialog
import java.awt.Container
import javax.swing.Icon

class InfoLogStyle(style: LogStyle) : PrintLogStyle(style) {
    @JvmField
    val loading: LoadingDialog = LoadingDialog()

    fun systemPrint(owner: String?, logInfo: String) {
        Log.systemPrint(LogStyle.INFO, owner, logInfo)
    }

    fun message(c: Container?, owner: String, logInfo: String) {
        Log.print(style, owner, "弹窗信息->$logInfo", c)
        val title: String? = getTitle(owner)
        CTOptionPane.showMessageDialog(
            c, title, logInfo,
            icon, CTOptionPane.INFORMATION_MESSAGE, true
        )
    }

    /**
     * 自适应样式窗口 (屏保时全屏弹窗,否则为系统通知)
     * 
     * @param owner       标题
     * @param logInfo     显示的消息
     * @param maxShowTime 显示时间
     * @param waitTime    等待时间
     */
    fun adaptedMessage(owner: String, logInfo: String, maxShowTime: Int, waitTime: Int) {
        if (isHasTheArg("屏保:展示")) CTOptionPane.showFullScreenMessageDialog(owner, logInfo, maxShowTime, waitTime)
        else systemPrint(owner, logInfo)
    }

    fun showInputDialog(c: Container?, owner: String, logInfo: String): String {
        Log.print(style, owner, "弹窗信息->$logInfo", c)
        val title: String? = getTitle(owner)
        val s = CTOptionPane.showInputDialog(
            c, title, logInfo, icon,
            true
        )
        Log.print(style, owner, "输入信息->$s", c)
        return s
    }

    /**
     * 显示选择输入对话框
     * 
     * @param owner   对话框的父组件
     * @param logInfo 显示的消息
     * @param choices 显示的选项
     * @return 0-选择的选项  1-用户输入的字符串
     */
    fun showInputDialog(c: Container?, owner: String, logInfo: String, vararg choices: String?): Array<String?> {
        Log.print(style, owner, "弹窗信息->$logInfo", c)
        val title: String? = getTitle(owner)
        val ss = CTOptionPane.showConfirmInputDialog(
            c, title, logInfo,
            icon,
            true, *choices
        )
        Log.print(style, owner, "输入信息->" + ss.contentToString(), c)
        return ss
    }

    fun showChooseDialog(c: Container?, owner: String?, logInfo: String?): Int {
        Log.print(style, owner, "弹窗信息->$logInfo", c)
        val title: String? = getTitle(owner)
        val i = CTOptionPane.showConfirmDialog(
            c, title, logInfo,
            icon, CTOptionPane.INFORMATION_MESSAGE, true
        )
        val s = when (i) {
            CTOptionPane.YES_OPTION -> {
                "是"
            }

            CTOptionPane.NO_OPTION -> {
                "否"
            }

            else -> {
                "取消"
            }
        }

        Log.print(style, owner, "输入信息->$s", c)
        return i
    }

    fun showChooseDialog(c: Container?, owner: String?, logInfo: String?, vararg choices: String?): String {
        Log.print(style, owner, "弹窗信息->$logInfo", c)
        val title: String? = getTitle(owner)
        val s = CTOptionPane.showChoiceDialog(
            c, title, logInfo,
            icon, CTOptionPane.INFORMATION_MESSAGE, true, *choices
        )

        Log.print(style, owner, "输入信息->$s", c)
        return s
    }

    /**
     * 显示多选对话框
     * 
     * @param owner   对话框的父组件
     * @param logInfo 显示的消息
     * @param choices 显示的选项
     * @return 选中的选项, 取消返回null
     */
    fun showChoicesDialog(c: Container?, owner: String?, logInfo: String?, vararg choices: String?): Array<String?>? {
        Log.print(style, owner, "弹窗信息->$logInfo", c)
        val title: String? = getTitle(owner)
        val ss = CTOptionPane.showChoicesDialog(
            c, title, logInfo,
            icon, CTOptionPane.INFORMATION_MESSAGE, true, *choices
        )
        Log.print(style, owner, "输入信息->" + ss.contentToString(), c)
        if (ss.size == 0) return null
        return ss
    }

    fun showTimeChooseDialog(c: Container?, owner: String?, logInfo: String?, timeStyle: Int): IntArray? {
        Log.print(this.style, owner, "弹窗信息->$logInfo", c)
        val times = CTOptionPane.showTimeChooseDialog(
            c, logInfo,
            icon, CTOptionPane.INFORMATION_MESSAGE, timeStyle, true
        )
        Log.print(this.style, owner, "输入信息->" + times.contentToString(), c)
        return times
    }

    fun showTimeChooseDialog(
        c: Container?,
        owner: String?,
        logInfo: String?,
        timeStyle: Int,
        oldTimes: IntArray?
    ): IntArray? {
        Log.print(style, owner, "弹窗信息->$logInfo", c)
        Log.print(style, owner, "旧的时间->" + oldTimes.contentToString(), c)
        val times = CTOptionPane.showTimeChooseDialog(
            c, logInfo,
            icon, CTOptionPane.INFORMATION_MESSAGE, timeStyle, true, oldTimes
        )
        Log.print(style, owner, "输入信息->" + times.contentToString(), c)
        return times
    }

    companion object {
        private fun getTitle(owner: String?): String? {
            return owner
        }

        private val icon: Icon?
            get() = null
    }
}
