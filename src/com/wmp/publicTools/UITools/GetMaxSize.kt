package com.wmp.publicTools.UITools

import java.awt.FontMetrics
import java.util.*
import kotlin.math.max
import kotlin.math.min

object GetMaxSize {
    @JvmField
    var STYLE_HTML: Int = 0
    var STYLE_PLAIN: Int = 1

    /**
     * 获取文本的最大行数和最大长度
     * 
     * @param s     文本
     * @param style 文本样式 0:HTML 1:PLAIN
     * @return 每行中最长的字数, 行数
     */
    fun getMaxSize(s: String, style: Int) =
        intArrayOf(getMaxLength(s, style), getLine(s, style))

    @JvmStatic
    fun getLine(s: String, style: Int): Int {
        val temp: String = if (style == STYLE_HTML) {
            s.replace("<html>|</html>".toRegex(), "").replace("<br>".toRegex(), "\n") // 去除HTML标签
        } else {
            s
        }
        return temp.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size
    }

    @JvmStatic
    fun getMaxLength(s: String, style: Int): Int {
        val temp: String = if (style == STYLE_HTML) {
            s.replace("<html>|</html>".toRegex(), "").replace("<br>".toRegex(), "\n") // 去除HTML标签
        } else {
            s
        }

        // 计算最长行的长度.mapToInt(String::length) 将每个字符串映射为其长度，然后使用 max() 方法找到最大值
        //.orElse(0) 如果数组为空，返回默认值 0
        val lines = temp.split("\n|\\\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Arrays.stream(lines).mapToInt { obj: String? -> obj!!.length }.max().orElse(0)
    }

    @JvmStatic
    fun getHTMLToTextMaxLength(s: String, fm: FontMetrics): Int {
        val strings = s.replace("<html>|</html>".toRegex(), "").replace("<br>".toRegex(), "\n").split("\n".toRegex())
            .dropLastWhile { it.isEmpty() }.toTypedArray() // 去除HTML标签
        var length = 0
        for (string in strings) {
            length = max(fm.stringWidth(string), length)
        }
        return min(length, 12 * fm.getFont().getSize())
    }
}
