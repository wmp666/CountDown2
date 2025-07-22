package com.wmp.countdown.ui.component;

import com.wmp.WCompanent.WTextButton;
import com.wmp.countdown.tools.CDInfo;
import com.wmp.countdown.tools.printLog.Log;
import com.wmp.countdown.tools.uiTools.CDFontSizeStyle;
import com.wmp.countdown.ui.frame.AboutDialog;
import com.wmp.countdown.ui.frame.SettingsDialog;

import javax.swing.*;
import java.awt.*;

public class CDButtonsPanel extends JPanel implements CDComponent{

    public CDButtonsPanel() {
        this.setLayout(new GridLayout( 1, 0));
        this.setOpaque(false);

        initButtons();
    }

    private void initButtons() {
        WTextButton exitButton = new WTextButton("退出");
        exitButton.setForeground(Color.red);
        exitButton.setFont(CDInfo.FONT.getCDFont(Font.BOLD, CDFontSizeStyle.NORMAL));
        exitButton.addActionListener(e -> {

            if (!CDInfo.isCanExit) {
                Log.err.print("退出", "错误的关闭请求");
                return;
            }


            int i = Log.info.showChooseDialog(null, "退出", "是否退出?");
            if (i == JOptionPane.YES_OPTION){
                Log.exit(0);
            }
        });
        this.add(exitButton);

        WTextButton settingsButton = new WTextButton("设置");
        settingsButton.setFont(CDInfo.FONT.getCDFont(Font.BOLD, CDFontSizeStyle.NORMAL));
        settingsButton.addActionListener(e -> {
            try {
                new SettingsDialog();
            } catch (Exception ex) {
                Log.err.print( "Log", "关于窗口打开失败:" +  ex);
                throw new RuntimeException(ex);
            }
        });
        this.add(settingsButton);

        WTextButton aboutButton = new WTextButton("关于");
        aboutButton.setFont(CDInfo.FONT.getCDFont(Font.BOLD, CDFontSizeStyle.NORMAL));
        aboutButton.addActionListener(e -> {
            try {
                new AboutDialog();
            } catch (Exception ex) {
                Log.err.print( "Log", "关于窗口打开失败:" +  ex);
                throw new RuntimeException(ex);
            }
        });
        this.add(aboutButton);

        WTextButton logButton = new WTextButton("日志");
        logButton.setFont(CDInfo.FONT.getCDFont(Font.BOLD, CDFontSizeStyle.NORMAL));
        logButton.addActionListener(e -> Log.showLogDialog());
        this.add(logButton);
    }

    @Override
    public void refresh() {
        this.removeAll();

        initButtons();
    }
}
