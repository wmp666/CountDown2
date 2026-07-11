package com.wmp.publicTools.CTTool

import com.wmp.countdown.CTComponent.CTPanel.setsPanel.CTSetsPanel
import com.wmp.publicTools.printLog.Log
import java.util.*
import javax.swing.JDialog
import javax.swing.WindowConstants

abstract class CTTool {
    var ctSetsPanelList: Array<CTSetsPanel<*>?> = arrayOfNulls(0)
        private set
    @JvmField
    var name: String? = "CTTool"

    constructor(name: String?) {
        this.name = name
    }

    fun showTool() {
        val id = Random().nextInt()
        Log.info.loading.showDialog("工具${name}${id}", "正在打开${name}工具")

        val dialog = this.dialog
        dialog.setAlwaysOnTop(true)
        dialog.setLocationRelativeTo(null)
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        dialog.setModal(true)

        Log.info.loading.closeDialog("工具${name}${id}")
        dialog.setVisible(true)
    }


    fun setCtSetsPanelList(vararg ctSetsPanelList: CTSetsPanel<*>?) {
        this.ctSetsPanelList = ctSetsPanelList as Array<CTSetsPanel<*>?>
    }


    abstract val dialog: JDialog
}
