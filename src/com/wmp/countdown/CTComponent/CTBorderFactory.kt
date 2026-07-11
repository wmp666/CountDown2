package com.wmp.countdown.CTComponent

import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.UITools.CTColor
import com.wmp.publicTools.UITools.CTFont.getCTFont
import com.wmp.publicTools.UITools.CTFontSizeStyle
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.border.Border
import javax.swing.border.TitledBorder

object CTBorderFactory {
    @JvmField
    var BASIC_LINE_BORDER: Border = BorderFactory.createLineBorder(Color(200, 200, 200), (2 * CTInfo.dpi).toInt(), true)
    @JvmField
    var FOCUS_GAINTED_BORDER: Border =
        BorderFactory.createLineBorder(Color(112, 112, 112), (2 * CTInfo.dpi).toInt(), true)

    /**
     * 创建带标题的边框
     * 
     * @param title              标题
     * @param titleJustification 对齐方式 -- 以下之一:
     * 
     *  * `TitledBorder.LEFT`
     *  * `TitledBorder.CENTER`
     *  * `TitledBorder.RIGHT`
     *  * `TitledBorder.LEADING`
     *  * `TitledBorder.TRAILING`
     *  * `TitledBorder.DEFAULT_JUSTIFICATION` (leading)
     * 
     * @param titlePosition    对其位置    -- 以下之一:
     * 
     *  * ` TitledBorder.ABOVE_TOP`
     *  * `TitledBorder.TOP` (坐在顶线)
     *  * `TitledBorder.BELOW_TOP`
     *  * `TitledBorder.ABOVE_BOTTOM`
     *  * `TitledBorder.BOTTOM` (坐在底线)
     *  * `TitledBorder.BELOW_BOTTOM`
     *  * `TitledBorder.DEFAULT_POSITION` (标题位置
     * 由当前外观决定)
     * 
     */
    @JvmStatic
    @JvmOverloads
    fun createTitledBorder(
        title: String?,
        titleJustification: Int = TitledBorder.LEFT,
        titlePosition: Int = TitledBorder.TOP
    ): Border = BorderFactory.createTitledBorder(
        BASIC_LINE_BORDER,
        title,
        titleJustification,
        titlePosition,
        getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL),
        CTColor.textColor
    )
}
