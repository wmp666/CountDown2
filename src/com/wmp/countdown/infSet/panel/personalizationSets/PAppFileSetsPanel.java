package com.wmp.countdown.infSet.panel.personalizationSets;

import com.wmp.publicTools.UITools.CTColor;
import com.wmp.publicTools.UITools.CTFont;
import com.wmp.publicTools.UITools.CTFontSizeStyle;
import com.wmp.publicTools.io.GetPath;
import com.wmp.publicTools.printLog.Log;
import com.wmp.countdown.CTComponent.CTBorderFactory;
import com.wmp.countdown.CTComponent.CTButton.CTTextButton;
import com.wmp.countdown.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class PAppFileSetsPanel extends CTBasicSetsPanel {
    public PAppFileSetsPanel() {
        super(null);
        setName("文件设置");

        initUI();
    }

    private void initUI() {
        this.setBackground(CTColor.backColor);
        this.setLayout(new GridLayout(0, 1, 5, 5));

        initParentDataPathSetsPanel();
    }

    private void initParentDataPathSetsPanel() {
        JPanel dataPathSetsPanel = new JPanel();
        dataPathSetsPanel.setOpaque(false);
        dataPathSetsPanel.setBorder(CTBorderFactory.createTitledBorder("数据父路径设置"));
        dataPathSetsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel dataPathLabel = new JLabel("数据父路径：");
        {
            String path = System.getenv("LOCALAPPDATA");
            if (path != null && !path.isEmpty()){
                File file = new File(path, "\\CountDown\\basicDataPath.txt");
                if (file.exists() && file.isFile()) {
                    try {
                        path = new File(Files.readString(file.toPath(), StandardCharsets.UTF_8)).getAbsolutePath();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            dataPathLabel.setText("<html>数据父路径：<br>" + path + "</html>");
        }
        dataPathLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        dataPathLabel.setForeground(CTColor.textColor);
        dataPathSetsPanel.add(dataPathLabel);

        CTTextButton dataPathButton = new CTTextButton("修改");
        dataPathButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        dataPathButton.addActionListener(e -> {

            String path = GetPath.getDirectoryPath(this, "选择数据父路径");
            //加载基础目录
            String localPath = System.getenv("LOCALAPPDATA");
            if (localPath != null && !localPath.isEmpty()){
                File file = new File(localPath, "\\CountDown\\basicDataPath.txt");
                if (path != null) {
                    try {
                        Files.writeString(file.toPath(), path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        dataPathLabel.setText("<html>数据父路径：<br>" + localPath + "</html>");
                    } catch (IOException ex) {
                        Log.err.print(getClass(), "数据父路径保存失败", ex);
                        throw new RuntimeException(ex);
                    }
                }
            }

        });
        dataPathSetsPanel.add(dataPathButton);

        this.add(dataPathSetsPanel);
    }

    @Override
    public void save() {

    }

    @Override
    public void refresh() {
        this.removeAll();
        initUI();
        this.revalidate();
        this.repaint();
    }
}
