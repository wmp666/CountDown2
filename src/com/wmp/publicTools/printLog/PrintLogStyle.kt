package com.wmp.publicTools.printLog

import java.awt.Container

open class PrintLogStyle(@JvmField var style: LogStyle) {
    open fun print(owner: String, logInfo: Any) {
        Log.print(style, owner, logInfo, null)
    }

    open fun print(c: Container?, owner: String, logInfo: Any) {
        Log.print(style, owner, logInfo, c)
    }
}

