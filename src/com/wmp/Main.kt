package com.wmp

import com.formdev.flatlaf.FlatLaf
import com.formdev.flatlaf.themes.FlatMacLightLaf
import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.StartupParameters
import com.wmp.publicTools.printLog.Log
import com.wmp.countdown.SwingRun
import java.util.*

object Main {
    /**
     * a.b.c.d.e 例:1.5.3.1.1<br></br>
     * a:主版本号<br></br>
     * b:功能更新版本号<br></br>
     * c:修订版本号/小功能更新<br></br>
     * d:只修复的问题,问题较少<br></br>
     * e:测试版本号
     */
    const val VERSION: String = "3.1.0"

    private val allArgs = TreeMap<String?, StartupParameters?>()
    @JvmField
    var argsList: ArrayList<String?> = ArrayList<String?>()

    init {
        //加载基础目录

        allArgs["StartUpdate:false"] = StartupParameters.creative("-StartUpdate:false", "/StartUpdate:false")

        allArgs["BasicDataPath"] = StartupParameters.creative("/BasicDataPath", "-BasicDataPath")

    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("版本：$VERSION")
        if (args.isNotEmpty()) {
            argsList = ArrayList<String?>(listOf(*args))
            println("使用的启动参数:${args.contentToString()}")
        }

        FlatLaf.setUseNativeWindowDecorations(false)
        FlatMacLightLaf.setup()


        CTInfo.version = CTInfo.easterEggModeMap.getString("版本", CTInfo.version)
        CTInfo.appName = CTInfo.easterEggModeMap.getString("软件名称", CTInfo.appName)
        CTInfo.author = CTInfo.easterEggModeMap.getString("作者", CTInfo.author)

        CTInfo.init(false)

        try {
            SwingRun.show()
        } catch (e: Exception) {
            Log.err.print(Main::class.java, "窗口初始化失败", e)
            Log.showLogDialog(true)
        }


        Log.info.print("Main", "初始化完毕")
    }

    /**
     * 判断是否存在参数
     * @param arg 参数 类型:
     * 
     *  * `StartUpdate:false`
     *  * `屏保:展示`
     *  * `screenProduct:view`
     *  * `CTInfo:isError`
     *  * `BasicDataPath`
     *  * `EasterEgg:notShow`
     * 
     * 
     * @return 是否存在
     */
    @JvmStatic
    fun isHasTheArg(arg: String?): Boolean {
        try {
            return allArgs[arg]!!.contains(argsList)
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * 获取当前参数下一位,若不存在传入的参数则返回null
     * @see .isHasTheArg
     * @param arg 参数
     * @return 下一位
     */
    @JvmStatic
    fun getTheArgNextArg(arg: String?): String? {
        if (allArgs[arg]!!.contains(argsList)) {
            val parameterList = allArgs[arg]!!.parameterList
            var index = -1
            for (i in parameterList.indices) {
                val tempIndex = argsList.indexOf(parameterList[i])
                if (tempIndex != -1) {
                    index = tempIndex
                    break
                }
            }
            return argsList[index + 1]
        } else return null
    }
}