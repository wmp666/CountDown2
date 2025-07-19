package com.wmp.countdown.ui.frame;

import com.wmp.WCompanent.WOptionPane;
import com.wmp.WCompanent.WTextButton;
import com.wmp.countdown.tools.uiTools.CDFont;
import com.wmp.countdown.tools.CDInfo;
import com.wmp.countdown.tools.printLog.Log;
import com.wmp.countdown.tools.uiTools.CDFontSizeStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;

public class MainFrame extends JDialog implements WindowListener {
    public MainFrame() {
        initFrame();
        Container pane = this.getContentPane();

        JLabel title = new JLabel("距离" + CDInfo.title + "还剩:");
        title.setForeground(CDInfo.COLOR.getTextColor());
        title.setFont(CDFont.getCDFont(Font.BOLD, CDFontSizeStyle.MORE_BIG));
        pane.add(title, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setOpaque(false);

        WTextButton exitButton = new WTextButton("退出");
        exitButton.addActionListener(e -> {
            int i = Log.info.showChooseDialog(this, "退出", "是否退出?");
            if (i == JOptionPane.YES_OPTION){
                Log.exit(0);
            }
        });
        buttonsPanel.add(exitButton);

        WTextButton aboutButton = new WTextButton("关于");
        aboutButton.addActionListener(e -> {
            try {
                new AboutDialog();
            } catch (Exception ex) {
                Log.err.print( "Log", "关于窗口打开失败:" +  ex);
                throw new RuntimeException(ex);
            }
        });
        buttonsPanel.add(aboutButton);


        this.pack();
        this.setVisible(true);

    }

    private void initFrame() {
        this.setTitle("Countdown V" + CDInfo.VERSION);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setUndecorated(true);
        this.setSize(400, 300);
        this.getContentPane().setBackground(CDInfo.COLOR.getBgColor());
        this.getContentPane().setLayout(new BorderLayout());
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        int i = Log.info.showChooseDialog(this, "提示", "是否关闭?");
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
