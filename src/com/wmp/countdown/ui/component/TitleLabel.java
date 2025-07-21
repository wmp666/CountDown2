package com.wmp.countdown.ui.component;

import com.wmp.countdown.tools.CDInfo;
import com.wmp.countdown.tools.uiTools.CDFontSizeStyle;

import javax.swing.*;
import java.awt.*;

public class TitleLabel extends JLabel implements CDComponent{
    public TitleLabel() {

        this.setText("距离" + CDInfo.title + "还剩:");
        this.setForeground(CDInfo.COLOR.getTextColor());
        this.setFont(CDInfo.FONT.getCDFont(Font.BOLD, CDFontSizeStyle.NORMAL));

    }

    @Override
    public void refresh(){
        this.setText("距离" + CDInfo.title + "还剩:");
        this.setForeground(CDInfo.COLOR.getTextColor());
        this.setFont(CDInfo.FONT.getCDFont(Font.BOLD, CDFontSizeStyle.NORMAL));
    }
}
