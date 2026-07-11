package com.wmp.publicTools.UITools

import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.appFileControl.IconControl
import com.wmp.publicTools.appFileControl.IconControl.getIcon
import com.wmp.publicTools.appFileControl.IconControl.getIconStyle
import java.awt.Image
import java.net.URL
import java.util.*
import javax.swing.Icon
import javax.swing.ImageIcon
import kotlin.math.max

object GetIcon {
    fun getIcon(path: URL?, width: Int, height: Int, useDPI: Boolean): Icon? {
        var width = width
        var height = height
        if (path == null) {
            return null
        }

        if (useDPI) {
            width = (width * CTInfo.dpi).toInt()
            height = (height * CTInfo.dpi).toInt()
        }


        // 确保尺寸不为零且在合理范围内
        width = max(1, width)
        height = max(1, height)


        val icon = ImageIcon(path)
        // 保留对非GIF图像的缩放处理，GIF应由组件尺寸控制显示大小
        if (!path.getPath().lowercase(Locale.getDefault()).endsWith(".gif")) {
            icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH))
        }
        return icon
    }

    @JvmStatic
    fun getImageIcon(path: URL?, width: Int, height: Int, useDPI: Boolean) =
        getIcon(path, width, height, useDPI) as ImageIcon?

    fun getIcon(path: URL?, width: Int, height: Int) =
        getIcon(path, width, height, true)

    @JvmStatic
    fun getImageIcon(path: URL?, width: Int, height: Int) =
        getIcon(path, width, height, true) as ImageIcon?


    @JvmStatic
    fun getIcon(name: String?, colorStyle: Int, width: Int, height: Int, useDPI: Boolean): Icon? {
        var width = width
        var height = height
        if (name == null) {
            return null
        }

        if (useDPI) {
            width = (width * CTInfo.dpi).toInt()
            height = (height * CTInfo.dpi).toInt()
        }

        val icon = ImageIcon()
        // 保留对非GIF图像的缩放处理，GIF应由组件尺寸控制显示大小
        if (!getIconStyle(name)!!.startsWith("gif")) {
            icon.setImage(getIcon(name, colorStyle).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH))
        } else {
            icon.setImage(getIcon(name, colorStyle).getImage())
        }
        return icon
    }

    @JvmStatic
    fun getImageIcon(name: String?, colorStyle: Int, width: Int, height: Int, useDPI: Boolean) =
        getIcon(name, colorStyle, width, height, useDPI) as ImageIcon?

    @JvmStatic
    fun getImageIcon(name: String?, colorStyle: Int, width: Int, height: Int) =
        getIcon(name, colorStyle, width, height, true) as ImageIcon?

    @JvmStatic
    fun getIcon(name: String?, colorStyle: Int, width: Int, height: Int) =
        getIcon(name, colorStyle, width, height, true)

    fun getImageIcon(name: String?, width: Int, height: Int) =
        getIcon(name, IconControl.COLOR_COLORFUL, width, height, true) as ImageIcon?

    @JvmStatic
    fun getIcon(name: String?, width: Int, height: Int) =
        getIcon(name, IconControl.COLOR_COLORFUL, width, height, true)

    @JvmStatic
    fun getIcon(name: String?, width: Int, height: Int, useDPI: Boolean) =
        getIcon(name, IconControl.COLOR_COLORFUL, width, height, useDPI)
}
