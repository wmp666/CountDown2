package com.wmp.countdown.importPanel.timeView;

import com.nlf.calendar.Lunar;
import com.wmp.publicTools.DateTools;
import com.wmp.publicTools.UITools.CTColor;
import com.wmp.publicTools.UITools.CTFont;
import com.wmp.publicTools.UITools.CTFontSizeStyle;
import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.countdown.CTComponent.CTPanel.CTViewPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class OtherTimeThingPanel extends CTViewPanel<Object> {
    private static String otherStrFormat = "<html>%s %s%s%s %s[%s]年<br>%s %s</html>";
    private final JLabel other = new JLabel();

    public OtherTimeThingPanel() {

        this.setName("时间详情");
        this.setID("OtherTimeThingPanel");
        this.setLayout(new BorderLayout());
        initPanel(CTFontSizeStyle.BIG);

        this.setIndependentRefresh(true, 2 * 60 * 1000);
    }

    @Override
    public CTInfoControl<Object> setInfoControl() {
        return null;
    }

    private void initPanel(CTFontSizeStyle size) {
        this.removeAll();

        other.setFont(CTFont.getCTFont(Font.BOLD, size));
        other.setForeground(CTColor.mainColor);
        this.add(other, BorderLayout.SOUTH);

    }


    @Override
    protected void easyRefresh() {
        if (!isScreenProductViewPanel()) {
            otherStrFormat = "<html>%s %s%s%s %s[%s]年<br>%s %s</html>";
            initPanel(CTFontSizeStyle.BIG);
        } else {
            otherStrFormat = "<html>%s %s%s%s %s[%s]年 %s %s</html>";
            initPanel(CTFontSizeStyle.MORE_BIG);
        }

        Calendar calendar = Calendar.getInstance();
        String week = "周" + new String[]{"天", "一", "二", "三", "四", "五", "六"}[calendar.get(Calendar.DAY_OF_WEEK) - 1];

        Lunar lunar = Lunar.fromDate(new Date());

        //周六 八月廿七 乙巳[蛇]年
        // 大雪 节日
        StringBuilder jie = new StringBuilder();
        for (String festival : lunar.getFestivals()) {
            jie.append(festival).append(" ");
        }
        other.setText(String.format(otherStrFormat,
                week, lunar.getMonth() < 0 ? "闰" : "", DateTools.months[Math.abs(lunar.getMonth()) - 1], DateTools.days[lunar.getDay() - 1], lunar.getYearInGanZhi(), lunar.getYearShengXiao(),
                lunar.getJieQi(), jie));

        this.repaint();

    }
}
