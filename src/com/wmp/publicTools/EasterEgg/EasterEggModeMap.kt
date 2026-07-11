package com.wmp.publicTools.EasterEgg

import java.awt.Color

class EasterEggModeMap(
    version: String?, author: String?, appName: String?,
    iconPath: String?, optionPaneTitle: String?, optionPaneIsUseIcon: Boolean,
    backColor: Color?, textColor: Color?, mainColor: Color?, themeMode: String?,
    canExit: Boolean, whenEasterEggStart: Runnable?, vararg excludes: String?
) {
    private val map: MutableMap<String?, Any?> = HashMap()

    /**
     * 彩蛋模式参数(不修改的数据写进排除列表)
     * 
     * @param version               版本
     * @param author                作者
     * @param appName               软件名称
     * @param iconPath              图标路径
     * @param optionPaneTitle       提示窗标题
     * @param optionPaneIsUseIcon   提示窗是否使用图标
     * @param backColor             背景色
     * @param textColor             文字色
     * @param mainColor             主题色
     * @param themeMode             主题模式
     * @param canExit               是否可以退出
     * @param whenEasterEggStart    彩蛋启动运行
     * @param excludes              排除的参数
     * @author wmp
     * @since 2.0.6.1.2
     */
    init {
        map["版本"] = version
        map["作者"] = author
        map["软件名称"] = appName
        map["图标路径"] = iconPath
        map["提示窗标题"] = optionPaneTitle
        map["提示窗是否使用图标"] = optionPaneIsUseIcon
        map["背景色"] = backColor
        map["文字色"] = textColor
        map["主题色"] = mainColor
        map["主题模式"] = themeMode
        map["是否可以退出"] = canExit
        map["彩蛋启动运行"] = whenEasterEggStart

        for (exclude in excludes) {
            map.remove(exclude)
        }
    }

    /**
     * 添加更多彩蛋数据
     * @see .get
     * @param pair 彩蛋的键值对
     */
    fun addMore(vararg pair: EasterEggPair) {
        for (easterEggPair in pair) {
            map[easterEggPair.key] = easterEggPair.value
        }
    }

    /**
     * 获取参数
     * @param key 可用参数
     * 
     * `版本`
     * `作者`
     * `软件名称`
     * `图标路径`
     * `提示窗标题`
     * `提示窗是否使用图标`
     * `背景色`
     * `文字色`
     * `主题色`
     * `主题模式`
     * `是否可以退出`
     * `彩蛋启动运行`
     * `加载文字集`
     * `关闭文字集`
     * 
     * @param defaultValue 默认值
     * @return 参数
     */
    fun get(key: String?, defaultValue: Any?) = map.getOrDefault(key, defaultValue)

    /**
     * @see .get
     */
    fun getString(key: String?, defaultValue: String?) = map.getOrDefault(key, defaultValue) as String

    /**
     * @see .get
     */
    fun getStringList(key: String?, defaultValue: Array<String>?) = map.getOrDefault(key, defaultValue) as Array<String>?

    /**
     * @see .get
     */
    fun getInt(key: String?, defaultValue: Int) = map.getOrDefault(key, defaultValue) as Int

    /**
     * @see .get
     */
    fun getBoolean(key: String?, defaultValue: Boolean) = map.getOrDefault(key, defaultValue) as Boolean

    /**
     * @see .get
     */
    fun getColor(key: String?, defaultValue: Color?) = map.getOrDefault(key, defaultValue) as Color

    /**
     * @see .get
     */
    fun getRunnable(key: String?, defaultValue: Runnable) = map.getOrDefault(key, defaultValue) as Runnable
}
