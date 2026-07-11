package com.wmp.publicTools.UITools

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.formdev.flatlaf.themes.FlatMacLightLaf
import com.wmp.publicTools.printLog.Log
import java.awt.Color

class CTColor {

    override fun toString(): String {
        return "CTColor{ mainColor:" + String.format("#%06x", mainColor.rgb) +
                " textColor:" + String.format("#%06x", textColor.rgb) +
                " backColor:" + String.format("#%06x", backColor.rgb) + "}"
    }

    companion object {
        /**
         * 主色 白色
         */
        const val MAIN_COLOR_WHITE: String = "white"

        /**
         * 主色 蓝色
         */
        const val MAIN_COLOR_BLUE: String = "blue"

        /**
         * 主色 绿色
         */
        const val MAIN_COLOR_GREEN: String = "green"

        /**
         * 主色 红色
         */
        const val MAIN_COLOR_RED: String = "red"

        /**
         * 主色 黑色
         */
        const val MAIN_COLOR_BLACK: String = "black"

        /**
         * 主题 灰色
         */
        const val STYLE_DARK: String = "dark"

        /**
         * 主题 亮色
         */
        const val STYLE_LIGHT: String = "light"

        const val STYLE_SYSTEM: String = "system"

        var style: String? = STYLE_LIGHT
        @JvmField
        var mainColor: Color = Color(0x29A8E3)
        @JvmField
        var textColor: Color = getParticularColor(MAIN_COLOR_BLACK)
        @JvmField
        var backColor: Color = getParticularColor(MAIN_COLOR_WHITE)

        fun setColorList(mainColor: Color, backColor: Color, textColor: Color, theme: String?) {
            Companion.mainColor = mainColor
            Companion.backColor = backColor
            Companion.textColor = textColor
            style = theme
        }

        /**
         * 获取指定颜色
         * 
         * @param colorStyle 颜色样式
         * @return 颜色
         * @see CTColor
         * 
         * @see .MAIN_COLOR_WHITE
         * 
         * @see .MAIN_COLOR_BLUE
         * 
         * @see .MAIN_COLOR_GREEN
         * 
         * @see .MAIN_COLOR_RED
         * 
         * @see .MAIN_COLOR_BLACK
         * 
         * @see .STYLE_DARK
         * 
         * @see .STYLE_LIGHT
         */
        @JvmStatic
        fun getParticularColor(colorStyle: String): Color {
            return when (colorStyle) {
                MAIN_COLOR_WHITE -> Color.WHITE
                MAIN_COLOR_BLUE -> Color(0x29A8E3)
                MAIN_COLOR_GREEN -> Color(0x05E666)
                MAIN_COLOR_RED -> Color(0xFF0000)
                MAIN_COLOR_BLACK -> Color(0x282C34)
                STYLE_DARK -> getParticularColor(MAIN_COLOR_BLACK)
                STYLE_LIGHT -> getParticularColor(MAIN_COLOR_WHITE)
                STYLE_SYSTEM -> SystemColor.getAccentColor()
                else -> Color(0x29A8E3)
            }
        }

        /**
         * 获取主题颜色
         * @param theme 主题
         * @return 主题颜色 0:背景色 1:文字色
         */
        fun getThemeColor(theme: String): Array<Color?> {
            return when (theme) {
                STYLE_DARK -> arrayOf(
                    getParticularColor(MAIN_COLOR_BLACK),
                    getParticularColor(MAIN_COLOR_WHITE)
                )

                else -> arrayOf(
                    getParticularColor(MAIN_COLOR_WHITE),
                    getParticularColor(MAIN_COLOR_BLACK)
                )
            }
        }
    }
}
