package com.wmp.publicTools.CTTool.callRoll

import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.io.IOForInfo
import java.io.IOException
import java.nio.file.Path

object CallRollInfoControl {
    private val path = CTInfo.DATA_PATH + "CTTools\\DianMing\\"

    @get:Throws(IOException::class)
    @set:Throws(IOException::class)
    var dianMingInfo: Array<String>
        get() {
            val io = IOForInfo(path + "DianMingInfo.txt")
            val info = io.getInfo()
            if (info.isEmpty() || info[0] == "err") {
                return emptyArray()
            }
            return info
        }
        set(info) {
            val io = IOForInfo(path + "DianMingInfo.txt")
            io.setInfo(*info)
        }

    @JvmStatic
    fun setDianMingNameList(path: String) {
        IOForInfo.copyFile(Path.of(path), Path.of(CallRollInfoControl.path))
    }
}
