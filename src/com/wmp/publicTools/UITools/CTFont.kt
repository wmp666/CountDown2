package com.wmp.publicTools.UITools

import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.printLog.Log
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.util.*

object CTFont {
    private var fontName: String = "微软雅黑"

    private var BigBigSize = 100
    private var moreBigSize = 60
    private var bigSize = 20
    private var normalSize = 19
    private var smallSize = 15
    private var moreSmallSize = 12


    @JvmStatic
    fun getCTFont(fontStyle: Int, sizeStyle: CTFontSizeStyle): Font {
        val size = when (sizeStyle) {
            CTFontSizeStyle.BIG_BIG -> BigBigSize
            CTFontSizeStyle.MORE_BIG -> moreBigSize
            CTFontSizeStyle.BIG -> bigSize
            CTFontSizeStyle.NORMAL -> normalSize
            CTFontSizeStyle.SMALL -> smallSize
            CTFontSizeStyle.MORE_SMALL -> moreSmallSize
        } //12 14/-15-/16 18/(-19-/)20 -23-/24/25
        return Font(fontName, fontStyle, (size * CTInfo.dpi).toInt())
    }

    @JvmStatic
    fun getDefaultFont(fontStyle: Int, sizeStyle: CTFontSizeStyle): Font {
        val size = when (sizeStyle) {
            CTFontSizeStyle.BIG_BIG -> BigBigSize
            CTFontSizeStyle.MORE_BIG -> moreBigSize
            CTFontSizeStyle.BIG -> bigSize
            CTFontSizeStyle.NORMAL -> normalSize
            CTFontSizeStyle.SMALL -> smallSize
            CTFontSizeStyle.MORE_SMALL -> moreSmallSize
        } //12 14/-15-/16 18/(-19-/)20 -23-/24/25
        val allFontName: Array<String> = allFontName
        for (s in allFontName) {
            if (s == "Microsoft YaHei UI") return Font("Microsoft YaHei UI", fontStyle, (size * CTInfo.dpi).toInt())
            if (s == "宋体") return Font("宋体", fontStyle, (size * CTInfo.dpi).toInt())
        }
        return Font("宋体", fontStyle, (size * CTInfo.dpi).toInt())
    }

    @JvmStatic
    val allFontName: Array<String>
        get() {
            //获取所有字体
            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment() //获取本地图形环境
            return ge.getAvailableFontFamilyNames()
        }

    @JvmStatic
    fun getFontName(): String {
        return fontName
    }

    @JvmStatic
    fun setFontName(fontName: String) {
        //获取所有字体
        val fontNames: Array<String> = allFontName
        //判断是否存在该字体
        val isExist = Arrays.asList(*fontNames).contains(fontName)
        if (!isExist) {
            Log.err.print(CTFont::class.java, "不存在该字体:$fontName")
            return
        }
        CTFont.fontName = fontName
    }

    @JvmStatic
    fun setSize(bigBigSize: Int, moreBigSize: Int, bigSize: Int, normalSize: Int, smallSize: Int, moreSmallSize: Int) {
        BigBigSize = bigBigSize
        CTFont.moreBigSize = moreBigSize
        CTFont.bigSize = bigSize
        CTFont.normalSize = normalSize
        CTFont.smallSize = smallSize
        CTFont.moreSmallSize = moreSmallSize
    }

    @JvmStatic
    val basicSize: IntArray
        /**
         * 获取字体大小
         * 
         * @return BigBigSize, moreBigSize, bigSize, normalSize, smallSize, moreSmallSize
         */
        get() = intArrayOf(
            BigBigSize,
            moreBigSize,
            bigSize,
            normalSize,
            smallSize,
            moreSmallSize
        )

    val size: IntArray
        get() = intArrayOf(
            (BigBigSize * CTInfo.dpi).toInt(),
            (moreBigSize * CTInfo.dpi).toInt(),
            (bigSize * CTInfo.dpi).toInt(),
            (normalSize * CTInfo.dpi).toInt(),
            (smallSize * CTInfo.dpi).toInt(),
            (moreSmallSize * CTInfo.dpi).toInt()
        )

    @JvmStatic
    fun getSize(index: CTFontSizeStyle): Int {
        var size = 0
        when (index) {
            CTFontSizeStyle.BIG_BIG -> size = BigBigSize
            CTFontSizeStyle.MORE_BIG -> size = moreBigSize
            CTFontSizeStyle.BIG -> size = bigSize
            CTFontSizeStyle.NORMAL -> size = normalSize
            CTFontSizeStyle.SMALL -> size = smallSize
            CTFontSizeStyle.MORE_SMALL -> size = moreSmallSize
        } //12 14/-15-/16 18/(-19-/)20 -23-/24/25
        return (size * CTInfo.dpi).toInt()
    }
}
