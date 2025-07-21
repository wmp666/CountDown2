package com.wmp.countdown.ui.frame;

import com.wmp.WCompanent.WOptionPane;
import com.wmp.WCompanent.WTextButton;
import com.wmp.countdown.tools.io.ControlCDInfo;
import com.wmp.countdown.tools.uiTools.CDFont;
import com.wmp.countdown.tools.CDInfo;
import com.wmp.countdown.tools.printLog.Log;
import com.wmp.countdown.tools.uiTools.CDFontSizeStyle;
import com.wmp.countdown.ui.component.CDButtonsPanel;
import com.wmp.countdown.ui.component.TimePanel;
import com.wmp.countdown.ui.component.TitleLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;

public class MainFrame extends JDialog implements WindowListener {

    private final TitleLabel title = new TitleLabel();
    private final TimePanel timePanel = new TimePanel();
    private final CDButtonsPanel buttonsPanel = new CDButtonsPanel();

    public MainFrame() {
        initFrame();
        Container pane = this.getContentPane();

        pane.add(title, BorderLayout.NORTH);

        pane.add(timePanel, BorderLayout.CENTER);

        pane.add(buttonsPanel, BorderLayout.SOUTH);


        Timer refreshTimer = new Timer(200, e -> {
            refresh();


            this.pack();
            //获取屏幕大小
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(screenSize.width - this.getWidth(), 0);
        });
        refreshTimer.setRepeats(true);// 重复
        refreshTimer.start();


        this.setVisible(true);

    }


    private void initFrame() {
        Log.info.systemPrint("MainFrame", "开始初始化窗口");

        this.setTitle("Countdown V" + CDInfo.VERSION);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setUndecorated(true);
        this.setSize(400, 300);
        this.getContentPane().setLayout(new BorderLayout());
    }

    public void refresh(){

        ControlCDInfo.initCDInfo();

        this.getContentPane().setBackground(CDInfo.COLOR.getBgColor());


        title.refresh();
        timePanel.refresh();
        buttonsPanel.refresh();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        int i = Log.info.showChooseDialog(null, "提示", "是否关闭?");
        if (i == WOptionPane.YES_OPTION) Log.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
