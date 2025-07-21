package com.wmp.countdown;

import com.wmp.countdown.tools.io.ControlCDInfo;
import com.wmp.countdown.tools.printLog.Log;
import com.wmp.countdown.ui.frame.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        //设置系统UI

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }

        Log.info.systemPrint("Log", "Countdown启动");

        ControlCDInfo.initCDInfo();

        MainFrame mainFrame = new MainFrame();
    }
}
