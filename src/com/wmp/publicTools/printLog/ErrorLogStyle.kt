package com.wmp.publicTools.printLog

import com.wmp.publicTools.ExceptionStringConverter.convertToString
import java.awt.Container

class ErrorLogStyle(style: LogStyle) : PrintLogStyle(style) {
    fun systemPrint(owner: Class<*>, logInfo: Any?, e: Exception?) {
        Log.systemPrint(
            LogStyle.ERROR, owner.getName(), (logInfo.toString() + "\n"
                    + convertToString(e, true))
        )
    }

    fun print(owner: Class<*>, logInfo: Any) {
        super.print(owner.toString(), logInfo)
    }

    fun print(c: Container?, owner: Class<*>, logInfo: Any) {
        super.print(c, owner.toString(), logInfo)
    }

    fun print(owner: Class<*>, logInfo: Any?, e: Exception?) {
        super.print(
            owner.toString(), logInfo.toString() + "\n" +
                    convertToString(e, true)
        )
    }

    fun print(c: Container?, owner: Class<*>, logInfo: String?, e: Exception?) {
        super.print(
            c, owner.toString(), logInfo + "\n" +
                    convertToString(e, true)
        )
    }
}
