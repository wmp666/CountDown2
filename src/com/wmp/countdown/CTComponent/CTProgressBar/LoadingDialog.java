package com.wmp.countdown.CTComponent.CTProgressBar;

import com.wmp.publicTools.CTInfo;
import com.wmp.publicTools.UITools.CTColor;
import com.wmp.publicTools.UITools.CTFont;
import com.wmp.publicTools.UITools.CTFontSizeStyle;
import com.wmp.publicTools.UITools.GetIcon;
import com.wmp.publicTools.appFileControl.IconControl;
import com.wmp.publicTools.printLog.Log;
import com.wmp.countdown.CTComponent.CTBorderFactory;

import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadingDialog extends JFrame {

    private final TreeMap<String, JPanel> PanelList = new TreeMap<>();

    private final TreeMap<String, CircleLoader> progressBarPanelList = new TreeMap<>();
    private final TreeMap<String, JLabel> textPanelList = new TreeMap<>();


    public LoadingDialog() {
        //生成弹窗
        this.setTitle("进度显示");
        this.setIconImage(GetIcon.getImageIcon("通用.进度", IconControl.COLOR_COLORFUL, 48, 48).getImage());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //this.setModal(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(0, 1));
        this.setAlwaysOnTop(true);

        this.getContentPane().setBackground(CTColor.backColor);
    }

    private void resetDialog() {
        updateTaskBar();

        this.setIconImage(GetIcon.getImageIcon("通用.进度", IconControl.COLOR_COLORFUL, 48, 48, false).getImage());
        this.getContentPane().setBackground(CTColor.backColor);
        this.revalidate();
        this.repaint();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(!PanelList.isEmpty());
    }

    private void updateTaskBar() {
        AtomicBoolean isIndeterminate = new AtomicBoolean(false);
        AtomicInteger value = new AtomicInteger(0);

        progressBarPanelList.values().forEach(progressBar -> {
            if (progressBar.isIndeterminate()) {
                isIndeterminate.set(true);

            }else value.addAndGet(progressBar.getValue());
        });
        if (Taskbar.isTaskbarSupported()) {
            Taskbar taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.PROGRESS_STATE_WINDOW) &&
            taskbar.isSupported(Taskbar.Feature.PROGRESS_VALUE_WINDOW)) {

                taskbar.setWindowProgressState(this, Taskbar.State.INDETERMINATE);

                if (!isIndeterminate.get()) {
                    taskbar.setWindowProgressState(this, Taskbar.State.NORMAL);
                    taskbar.setWindowProgressValue(this, !progressBarPanelList.isEmpty() ?value.get()/progressBarPanelList.size():1);
                }
            }
        }
    }

    public void showDialog(String id, String text) {
        showDialog(id, text, 0, true);
    }

    public void showDialog(String id, String text, int value) {
        showDialog(id, text, value, false);
    }

    public void showDialog(String id, String text, int value, boolean isIndeterminate) {
        Log.info.print("LoadingDialog", "显示进度条" + id);


    }

    public void updateDialog(String id, int value) {
        updateDialog(id, null, value);
    }

    public void updateDialog(String id, String text) {
        updateDialog(id, text, -2);
    }

    /**
     * 更新进度条
     *
     * @param id    进度条id
     * @param text  进度条文本
     * @param value 进度条值(为-1时,更改为不确定模式)
     */
    public void updateDialog(String id, String text, int value) {
        Log.info.print("LoadingDialog", "更新进度条" + id);


    }

    public void closeDialog(String id) {
        Log.info.print("LoadingDialog", "关闭进度条" + id);


    }
}
