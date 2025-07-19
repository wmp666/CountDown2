package com.wmp.countdown.tools.printLog;


import com.wmp.WCompanent.WOptionPane;
import com.wmp.WCompanent.tools.GetIcon;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class InfoLogStyle extends PrintLogStyle {



    public InfoLogStyle(LogStyle style) {
        super(style);
    }

    public void systemPrint(String owner, String logInfo) {
        Log.systemPrint(LogStyle.INFO, owner, logInfo);
    }

    public void message(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = owner;
        WOptionPane.showMessageDialog(c, title, logInfo, null, WOptionPane.INFORMATION_MESSAGE, true);
    }


    public String showInputDialog(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = owner;
        String s = WOptionPane.showInputDialog(c, title, logInfo, null,
                true);
        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return s;
    }

    /**
     * 显示选择输入对话框
     *
     * @param owner   对话框的父组件
     * @param logInfo 显示的消息
     * @param choices 显示的选项
     * @return 0-选择的选项  1-用户输入的字符串
     */

    public String[] showInputDialog(Container c, String owner, String logInfo, String... choices) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = owner;
        String[] ss = WOptionPane.showConfirmInputDialog(c, title, logInfo, null,
                true, choices);
        Log.print(getStyle(), owner, "输入信息->" + Arrays.toString(ss), c);
        return ss;
    }

    public int showChooseDialog(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = owner;
        int i = WOptionPane.showConfirmDialog(c, title, logInfo, null, true);
        String s ;
        if (i == WOptionPane.YES_OPTION) {
            s = "是";
        } else if (i == WOptionPane.NO_OPTION) {
            s = "否";
        }else {
            s = "取消";
        }

        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return i;
    }

    public String showChooseDialog(Container c, String owner, String logInfo, String... choices) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = owner;
        String s = WOptionPane.showConfirmDialog(c, title, logInfo, null, true, choices);

        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return s;
    }
}
