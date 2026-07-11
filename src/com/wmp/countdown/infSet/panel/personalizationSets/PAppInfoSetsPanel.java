package com.wmp.countdown.infSet.panel.personalizationSets;

import com.wmp.publicTools.UITools.CTColor;
import com.wmp.publicTools.appFileControl.appInfoControl.AppInfo;
import com.wmp.publicTools.appFileControl.appInfoControl.AppInfoControl;
import com.wmp.countdown.CTComponent.CTCheckBox;
import com.wmp.countdown.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel;
import com.wmp.countdown.CTComponent.CTSpinner;

import javax.swing.*;
import java.awt.*;

public class PAppInfoSetsPanel extends CTBasicSetsPanel<AppInfo> {
    private final CTSpinner SSMDWaitTimeSpinner = new CTSpinner(new SpinnerNumberModel(5, 0, 60, 1));
    private final CTCheckBox joinInsiderCheckBox = new CTCheckBox("加入测试计划(你的版本号将保持测试版)");

    public PAppInfoSetsPanel() {
        super(new AppInfoControl());
        setName("应用信息设置");

        initUI();

    }

    private void initUI() {
        this.setBackground(CTColor.backColor);
        this.setLayout(new GridLayout(0, 1, 5, 5));

        JPanel waitTimePanel = new JPanel();
        waitTimePanel.setOpaque(false);
        waitTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("通知等待时间: ");
        label.setForeground(CTColor.textColor);

        waitTimePanel.add(label);
        waitTimePanel.add(SSMDWaitTimeSpinner);

        this.add(waitTimePanel);

        this.add(joinInsiderCheckBox);

        SSMDWaitTimeSpinner.setValue(getInfoControl().getInfo().messageShowTime);
        joinInsiderCheckBox.setSelected(getInfoControl().getInfo().joinInsiderProgram);
    }
    @Override
    public void save() {
        AppInfo appInfo = new AppInfo(SSMDWaitTimeSpinner.getValue(), joinInsiderCheckBox.isSelected());
        getInfoControl().setInfo(appInfo);
    }

    @Override
    public void refresh() {
        this.removeAll();

        initUI();

        this.revalidate();
        this.repaint();
    }
}
