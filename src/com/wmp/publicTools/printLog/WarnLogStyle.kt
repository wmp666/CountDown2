package com.wmp.publicTools.printLog

import com.wmp.countdown.CTComponent.CTOptionPane
import com.wmp.countdown.CTComponent.CTProgressBar.LoadingDialog
import java.awt.Container
import javax.swing.Icon

class WarnLogStyle(style: LogStyle) : PrintLogStyle(style) {
    val loading: LoadingDialog = LoadingDialog()

    override fun print(owner: String, logInfo: Any) {
        Log.systemPrint(style, owner, logInfo.toString())
        print(null, owner, logInfo.toString())
    }

    fun message(c: Container?, owner: String, logInfo: String) {
        Log.print(style, owner, "弹窗信息->$logInfo", c)


        val title: String? = getTitle(owner)
        CTOptionPane.showMessageDialog(
            c, title, logInfo,
            icon, CTOptionPane.WARNING_MESSAGE, true
        )
    }

    fun showChooseDialog(c: Container?, owner: String?, logInfo: String?): Int {
        Log.print(style, owner, "弹窗信息->$logInfo", c)
        val title: String? = getTitle(owner)
        val i = CTOptionPane.showConfirmDialog(
            c, title, logInfo,
            icon, CTOptionPane.WARNING_MESSAGE, true
        )
        val s = when (i) {
            CTOptionPane.YES_OPTION -> "是"
            CTOptionPane.NO_OPTION -> "否"
            else -> "取消"
        }

        Log.print(style, owner, "输入信息->$s", c)
        return i
    }

    fun showChooseDialog(c: Container?, owner: String?, logInfo: String?, vararg choices: String?): String? {
        Log.print(style, owner, "弹窗信息->$logInfo", c)
        val title: String? = getTitle(owner)
        val s = CTOptionPane.showChoiceDialog(
            c, title, logInfo,
            icon, CTOptionPane.WARNING_MESSAGE, true, *choices
        )

        Log.print(style, owner, "输入信息->$s", c)
        return s
    }

    companion object {
        private fun getTitle(owner: String?): String? {
            return owner
        }

        private val icon: Icon?
            get() = null
    }
}
