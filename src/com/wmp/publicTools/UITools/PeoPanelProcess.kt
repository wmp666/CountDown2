package com.wmp.publicTools.UITools

import com.wmp.publicTools.UITools.CTFont.getCTFont
import com.wmp.publicTools.UITools.GetMaxSize.getHTMLToTextMaxLength
import com.wmp.publicTools.UITools.GetMaxSize.getLine
import com.wmp.publicTools.UITools.GetMaxSize.getMaxLength
import java.awt.Dimension
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JScrollPane

object PeoPanelProcess {
    /**
     * 用于获取值日人员的姓名
     * 
     * @param array 人员姓名数组
     * @return 人员姓名, 行数, 最大长度
     */
    @JvmStatic
    fun getPeopleName(array: MutableList<String?>): Array<Any?> {
        val sb = StringBuilder()
        sb.append("<html>")

        val size = array.size
        val index = (size + 2) / 3 // 修正组数计算逻辑

        for (i in 0..<index) {
            val base = i * 3
            sb.append(array.get(base))

            if (base + 1 < size) {
                sb.append("，").append(array.get(base + 1))
            }
            if (base + 2 < size) {
                sb.append("，").append(array.get(base + 2))
            }

            if (i < index - 1) {  // 仅在非最后一组后添加换行
                sb.append("<br>")
            }
        }

        sb.append("</html>")

        val maxLength = getMaxLength(sb.toString(), GetMaxSize.STYLE_HTML)

        return arrayOf(sb.toString(), index, maxLength)
    }

    @JvmStatic
    fun getShowPeoPanel(peo: MutableList<String?>): JScrollPane {
        val personLabel = JLabel()

        val objects = getPeopleName(peo)

        personLabel.setText(objects[0].toString())
        personLabel.setFont(getCTFont(Font.BOLD, CTFontSizeStyle.BIG))
        personLabel.setForeground(CTColor.mainColor)

        val scrollPane = JScrollPane(personLabel)

        // 根据文字数量调整窗口大小
        val lineCount = getLine(objects[0].toString(), GetMaxSize.STYLE_HTML) // 行数

        val fm = personLabel.getFontMetrics(personLabel.getFont())

        // 计算新的窗口尺寸（基础尺寸 + 动态调整）
        val newWidth = getHTMLToTextMaxLength(personLabel.getText(), fm) // 根据最大字符宽度计算总宽度
        var newHeight = lineCount * personLabel.getFont().getSize() + 5 // 每多一行增加30像素高度

        val maxShowHeight = 4 * personLabel.getFont().getSize() // 最大显示高度

        // 设置窗口大小
        if (newHeight >= maxShowHeight) {
            newHeight = maxShowHeight
        }
        scrollPane.setOpaque(false)
        scrollPane.getViewport().setOpaque(false)
        scrollPane.setBorder(BorderFactory.createEmptyBorder())

        scrollPane.setPreferredSize(Dimension(newWidth + 18, newHeight + 18))

        return scrollPane
    }
}
