package com.wmp.countdown.ui.frame;

import com.wmp.WCompanent.WComboBox;
import com.wmp.WCompanent.WTextButton;
import com.wmp.WCompanent.WTextField;
import com.wmp.countdown.tools.CDInfo;
import com.wmp.countdown.tools.io.ControlCDInfo;
import com.wmp.countdown.tools.printLog.Log;
import com.wmp.countdown.tools.uiTools.CDFont;
import com.wmp.countdown.tools.uiTools.CDFontSizeStyle;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ItemEvent;

public class SettingsDialog extends JDialog {

    private final WTextField titleField = new WTextField();
    private final WTextField targetTimeField = new WTextField();
    private final WComboBox textColorComboBox = new WComboBox();
    private final WComboBox bgColorComboBox = new WComboBox();
    private final WComboBox mainColorComboBox = new WComboBox();
    private final WComboBox fontComboBox = new WComboBox();
    private final JCheckBox canExitCheckBox = new JCheckBox("允许退出");


    public SettingsDialog() {
        initFrame();



        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());


        initSettingsPanel(pane);

        WTextButton saveButton = new WTextButton("保存");
        saveButton.setFont( CDInfo.FONT.getCDFont(Font.PLAIN, CDFontSizeStyle.NORMAL));
        saveButton.addActionListener(e -> {
            Log.info.message(this, "SettingsDialog", "正在保存设置");
            ControlCDInfo.saveCDInfo( titleField.getText(), targetTimeField.getText(),
                    textColorComboBox.getSelectedItem().toString(), bgColorComboBox.getSelectedItem().toString(), mainColorComboBox.getSelectedItem().toString(),
                    fontComboBox.getSelectedItem().toString(), canExitCheckBox.isSelected());
            this.dispose();
        });
        pane.add(saveButton, BorderLayout.SOUTH);


        // 初始化变量
        initData();

        this.pack();
        this.setVisible(true);
    }

    private void initSettingsPanel(Container pane) {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout( ));
        settingsPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(1,2));
        titlePanel.setOpaque(false);
        titlePanel.add(new JLabel("标题:"));
        titlePanel.add(titleField);
        settingsPanel.add(titlePanel, gbc);

        JPanel targetTimePanel = new JPanel();
        targetTimePanel.setLayout(new GridLayout(1,2));
        targetTimePanel.setOpaque(false);
        targetTimePanel.add(new JLabel("目标时间 (yyyy.MM.dd HH:mm:ss):"));

        targetTimePanel.add(targetTimeField);
        gbc.gridy++;
        settingsPanel.add(targetTimePanel, gbc);

        JPanel colorPanel = new JPanel();
        colorPanel.setBorder(BorderFactory.createTitledBorder("颜色设置"));
        colorPanel.setLayout(new GridLayout(0, 2));
        colorPanel.setOpaque(false);

        colorPanel.add(new JLabel("文字颜色:"));

        textColorComboBox.removeAllItems();
        textColorComboBox.addItems(CDInfo.COLOR.getAllColorStyle());
        colorPanel.add(textColorComboBox);

        colorPanel.add(new JLabel("背景色:"));
        bgColorComboBox.removeAllItems();
        bgColorComboBox.addItems(CDInfo.COLOR.getAllColorStyle());
        colorPanel.add(bgColorComboBox);

        colorPanel.add(new JLabel("主题色:"));
        mainColorComboBox.removeAllItems();
        mainColorComboBox.addItems(CDInfo.COLOR.getAllColorStyle());
        colorPanel.add(mainColorComboBox);
        gbc.gridy++;
        settingsPanel.add(colorPanel, gbc);

        JPanel fontPanel = new JPanel();
        fontPanel.setLayout(new GridLayout(1,2));
        fontPanel.setOpaque(false);
        fontPanel.add(new JLabel("字体:"));
        fontComboBox.removeAllItems();
        fontComboBox.addItems(CDFont.getAllFontName());
        fontPanel.add(fontComboBox);
        gbc.gridy++;
        settingsPanel.add(fontPanel, gbc);

        gbc.gridy++;
        settingsPanel.add(canExitCheckBox, gbc);

        pane.add(settingsPanel, BorderLayout.CENTER);
    }

    private void initData() {
        Log.info.print("SettingsDialog", "正在初始化数据");
        titleField.setText(CDInfo.title);
        targetTimeField.setText(CDInfo.targetTime);
        textColorComboBox.setSelectedItem(CDInfo.COLOR.getTextColorStr());
        bgColorComboBox.setSelectedItem(CDInfo.COLOR.getBgColorStr());
        mainColorComboBox.setSelectedItem(CDInfo.COLOR.getMainColorStr());
        fontComboBox.setSelectedItem(CDInfo.FONT.getFontName());
    }

    private void initFrame() {
        Log.info.print("SettingsDialog", "正在初始化设置窗口");

        this.setTitle("设置");
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);
    }
}
