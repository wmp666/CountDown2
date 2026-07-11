package com.wmp.countdown.importPanel.timeView;

import com.wmp.publicTools.UITools.CTColor;
import com.wmp.publicTools.UITools.CTFont;
import com.wmp.publicTools.UITools.CTFontSizeStyle;
import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.countdown.CTComponent.CTPanel.CTViewPanel;
import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimeViewPanel extends CTViewPanel<Object> {

    private final JLabel timeView = new JLabel();

    public TimeViewPanel(){

        this.setName("时间显示组件");
        this.setID("TimeViewPanel");
        this.setLayout(new BorderLayout());

        this.setIndependentRefresh(true, 34);

    }

    @Override
    public CTInfoControl<Object> setInfoControl() {
        return null;
    }

    @Override
    protected void easyRefresh() {
        this.removeAll();

        //获取时间
        Date date = new Date();
        //格式化 11.22 23:05
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");


        timeView.setHorizontalAlignment(JLabel.CENTER);
        timeView.setText(dateFormat.format(date));
        timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        //timeView.setBackground(new Color(0x0ECECED, true));
        timeView.setForeground(CTColor.mainColor);
        this.add(timeView, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();

    }

}
