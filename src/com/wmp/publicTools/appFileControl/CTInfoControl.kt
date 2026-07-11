package com.wmp.publicTools.appFileControl

import java.io.File

/**
 * 用于管理各个组件的数据获取的标准类工具,在获取数据时会从缓存中获取(除非没有),可以通过refresh方法刷新缓存数据,
 * refresh方法需要用户自行实现,刷新缓存数据时请返回新的数据,可用于直接获取最新的数据
 */
abstract class CTInfoControl<T> {
    private var info: T? = null

    open val infoBasicFile: File? = null

    abstract fun setInfo(t: T)
    fun getInfo(): T {
        if (info == null) {
            info = refresh()
        }
        return info!!
    }

    /**
     * 刷新缓存数据
     * @return 新的数据
     */
    fun refresh(): T = refreshInfo().also { info = it }

    protected abstract fun refreshInfo(): T
}
