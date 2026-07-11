package com.wmp.countdown.CTComponent.CTProgressBar;

import javax.swing.*;
import java.awt.*;

public class ModernLoadingDialog extends JDialog {
    private CircleLoader loader;

    public ModernLoadingDialog(JFrame parent) {
        super(parent, "正在加载...", true);

        setLayout(new BorderLayout(20, 20));
        setUndecorated(true); // 无边框
        setBackground(new Color(255, 255, 255, 0));

        // 创建圆角面板
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 加载动画
        loader = new CircleLoader();
        loader.setPreferredSize(new Dimension(80, 80));


        // 添加到面板
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(loader, BorderLayout.CENTER);
        centerPanel.setOpaque(false);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        // 设置大小并居中
        pack();
        setLocationRelativeTo(parent);

    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) loader.startAnimation();
    }

    public void setValue(int value) {
        if (loader.isIndeterminate()) return;
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Invalid value: " + value);
        }

        loader.setValue(value);
    }

    public void setIndeterminate(boolean indeterminate) {
        loader.setIndeterminate(indeterminate);
    }

    public CircleLoader getLoader() {
        return loader;
    }
}
