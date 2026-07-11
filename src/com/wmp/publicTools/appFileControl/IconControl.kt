package com.wmp.publicTools.appFileControl

import com.wmp.countdown.CTComponent.CTBorderFactory
import com.wmp.countdown.CTComponent.CTButton.CTTextButton
import com.wmp.countdown.CTComponent.CTOptionPane
import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.UITools.CTColor
import com.wmp.publicTools.appFileControl.ColorImageGenerator.getColorfulImageMap
import com.wmp.publicTools.appFileControl.tools.GetShowTreePanel
import com.wmp.publicTools.io.DownloadURLFile
import com.wmp.publicTools.io.GetPath
import com.wmp.publicTools.io.IOForInfo
import com.wmp.publicTools.io.IOForInfo.Companion.deleteDirectoryRecursively
import com.wmp.publicTools.io.IOForInfo.Companion.getInfo
import com.wmp.publicTools.io.IOForInfo.Companion.getInfos
import com.wmp.publicTools.io.ZipPack
import com.wmp.publicTools.printLog.Log
import com.wmp.publicTools.update.GetNewerVersion
import com.wmp.publicTools.web.GetWebInf.getWebInf
import org.json.JSONArray
import org.json.JSONObject
import java.awt.*
import java.awt.event.ActionEvent
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Path
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Consumer
import javax.swing.*
import javax.swing.event.TreeExpansionEvent
import javax.swing.event.TreeExpansionListener
import javax.swing.event.TreeSelectionEvent
import javax.swing.tree.DefaultMutableTreeNode
import kotlin.math.min

object IconControl {
    const val COLOR_DEFAULT: Int = 0
    const val COLOR_COLORFUL: Int = 1

    private val ALL_ICON_KEY = arrayOf<String?>(
        "关于.哔哩哔哩",
        "关于.Github",
        "关于.QQ",
        "关于.微信",

        "通用.保存",

        "通用.网络.更新",
        "通用.网络.下载",

        "通用.设置",
        "通用.快速启动",
        "通用.快捷工具",
        "通用.刷新",
        "通用.关于",
        "通用.日志",
        "通用.关闭",
        "通用.更多",
        "通用.添加",
        "通用.删除",

        "通用.文件.文件夹",
        "通用.文件.导入",
        "通用.文件.导出",

        "通用.祈愿",
        "通用.编辑",
        "通用.文档",
        "通用.进度"
    )

    private val DEFAULT_IMAGE_MAP: MutableMap<String?, ImageIcon?> = HashMap<String?, ImageIcon?>()
    private val COLORFUL_IMAGE_MAP: MutableMap<String?, MutableMap<String?, ImageIcon?>?> =
        HashMap<String?, MutableMap<String?, ImageIcon?>?>()

    private val ICON_STYLE_MAP: MutableMap<String?, String?> = HashMap<String?, String?>()

    init {
        DEFAULT_IMAGE_MAP["系统.图标"] = ImageIcon(IconControl::class.java.getResource(CTInfo.iconPath))
        COLORFUL_IMAGE_MAP["light"] = DEFAULT_IMAGE_MAP
        COLORFUL_IMAGE_MAP["dark"] = DEFAULT_IMAGE_MAP
        COLORFUL_IMAGE_MAP["err"] = DEFAULT_IMAGE_MAP
    }

    fun init(getNewerVersion: Boolean) {
        try {
            DEFAULT_IMAGE_MAP.clear()
            COLORFUL_IMAGE_MAP.clear()

            DEFAULT_IMAGE_MAP["系统.图标"] = ImageIcon(IconControl::class.java.getResource(CTInfo.iconPath))

            //获取基础图标
            val resourceInfos = getInfos(IconControl::class.java.getResource("imagePath.json")!!)
            val resourceJsonArray = JSONArray(resourceInfos)
            resourceJsonArray.forEach(Consumer { `object`: Any? ->
                val jsonObject = `object` as JSONObject
                Log.info.print(
                    "IconControl",
                    String.format("名称:%s|位置:%s", jsonObject.getString("name"), jsonObject.getString("path"))
                )
                val pathStr = jsonObject.getString("path")
                val path = IconControl::class.java.getResource(pathStr)
                if (path == null) {
                    Log.warn.print("IconControl", String.format("图标文件%s不存在", jsonObject.getString("path")))
                    DEFAULT_IMAGE_MAP[jsonObject.getString("name")] =
                        ImageIcon(IconControl::class.java.getResource("/image/optionDialogIcon/warn.png"))
                } else {
                    DEFAULT_IMAGE_MAP[jsonObject.getString("name")] = ImageIcon(path)
                }
                ICON_STYLE_MAP[jsonObject.getString("name")] = jsonObject.getString("style")
            })
        } catch (e: Exception) {
            Log.warn.message(null, IconControl::class.java.getName(), "图片加载失败:\n$e")
        }
           COLORFUL_IMAGE_MAP["light"] = DEFAULT_IMAGE_MAP
        COLORFUL_IMAGE_MAP["dark"] = DEFAULT_IMAGE_MAP
        COLORFUL_IMAGE_MAP["err"] = DEFAULT_IMAGE_MAP

    }

    @JvmStatic
    fun getIconStyle(name: String?): String? {
        return ICON_STYLE_MAP.getOrDefault(name, "png")
    }

    fun getDefaultIcon(name: String?): ImageIcon? {
        return DEFAULT_IMAGE_MAP.getOrDefault(
            name,
            DEFAULT_IMAGE_MAP["default"]
        )
    }

    fun getColorfulIcon(name: String?): ImageIcon? {
        val defaultMap = HashMap<String?, ImageIcon?>()
        defaultMap["default"] = DEFAULT_IMAGE_MAP["default"]
        return COLORFUL_IMAGE_MAP.getOrDefault(CTColor.style, defaultMap)!!
            .getOrDefault(name, DEFAULT_IMAGE_MAP["default"])
    }


    @JvmStatic
    fun getIcon(name: String?, colorStyle: Int): ImageIcon {
        val imageIcon = if (colorStyle == COLOR_DEFAULT) getDefaultIcon(name) else getColorfulIcon(name)
        if (imageIcon == null) {
            return ImageIcon(IconControl::class.java.getResource("/image/default.png"))
        }
        return imageIcon
    }


}
