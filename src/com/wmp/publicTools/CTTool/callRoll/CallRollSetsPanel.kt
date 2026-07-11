package com.wmp.publicTools.CTTool.callRoll

import com.wmp.countdown.CTComponent.CTButton.CTTextButton
import com.wmp.countdown.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel
import com.wmp.publicTools.CTTool.callRoll.CallRollInfoControl.setDianMingNameList
import com.wmp.publicTools.io.GetPath
import java.awt.GridLayout

class CallRollSetsPanel : CTBasicSetsPanel<Any?>(null) {
    init {
        name = "点名设置"
    }

    @Throws(Exception::class)
    override fun save() {
    }

    override fun refresh() {
        this.removeAll()
        this.setLayout(GridLayout(0, 1))

        val inputButton = CTTextButton("导入")
        inputButton.addActionListener { _ ->
            val filePath = GetPath.getFilePath(this, "请选择文件", ".txt", "点名列表")
            if (filePath != null) {
                setDianMingNameList(filePath)
            }
        }
        this.add(inputButton)
    }
}
