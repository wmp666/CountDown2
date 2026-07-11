package com.wmp.countdown.infSet.panel.personalizationSets.basicSets;

import com.wmp.publicTools.UITools.CTColor;
import com.wmp.publicTools.UITools.CTFont;
import com.wmp.publicTools.UITools.CTFontSizeStyle;
import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.publicTools.printLog.Log;
import com.wmp.countdown.CTComponent.CTBorderFactory;
import com.wmp.countdown.CTComponent.CTCheckBox;
import com.wmp.countdown.CTComponent.CTComboBox;
import com.wmp.countdown.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel;
import com.wmp.countdown.CTComponent.CTTextField;
import com.wmp.countdown.frame.MainWindow;
import com.wmp.countdown.importPanel.finalPanel.FinalPanel;
import com.wmp.countdown.infSet.panel.personalizationSets.control.PBasicInfo;
import com.wmp.countdown.infSet.tools.SetStartUp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

public class PBBasicSetsPanel extends CTBasicSetsPanel<PBasicInfo> {
    //颜色 数据
    private final CTComboBox mainColorComboBox = new CTComboBox();
    private final CTComboBox mainThemeComboBox = new CTComboBox();
    private final CTCheckBox buttonColorCheckBox = new CTCheckBox("按钮颜色跟随主题");
    private final CTComboBox FontNameComboBox = new CTComboBox();
    //字体 数据
    private final ArrayList<CTTextField> FontSizeList = new ArrayList<>();
    //隐藏按钮 数据
    private final TreeMap<String, CTCheckBox> disposeButton = new TreeMap<>();
    //其他数据
    private final CTCheckBox StartUpdate = new CTCheckBox("启动检查更新");
    private final CTCheckBox canExit = new CTCheckBox("防止被意外关闭");
    private final CTCheckBox startUp = new CTCheckBox("开机自启动");
    private final CTCheckBox isSaveLog = new CTCheckBox("是否保存日志");

    public PBBasicSetsPanel(CTInfoControl<PBasicInfo> infoControl) {
        super(infoControl);

        setName("个性化设置");

        initUI();
    }

    private void initUI() {
        this.removeAll();


        JPanel SetsPanel = new JPanel();
        SetsPanel.setOpaque(false);
        JScrollPane mainPanelScroll = new JScrollPane(SetsPanel);
        mainPanelScroll.setOpaque(false);
        mainPanelScroll.getViewport().setOpaque(false);
        //调整滚动灵敏度
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanelScroll.setSize(400, 400);

        SetsPanel.setLayout(new GridBagLayout());//new GridLayout(0,1)
        GridBagConstraints gbc = new GridBagConstraints();


        JPanel ColorPanel = new JPanel();
        ColorPanel.setOpaque(false);
        ColorPanel.setLayout(new GridLayout(0, 2));
        ColorPanel.setBorder(CTBorderFactory.createTitledBorder("颜色设置"));
        //颜色设置
        {
            //主题色设置
            JPanel MainColorSets = new JPanel();
            MainColorSets.setOpaque(false);
            {

                MainColorSets.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel mainColorLabel = new JLabel("主题色:");
                mainColorLabel.setForeground(CTColor.textColor);
                mainColorLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));


                mainColorComboBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                //添加颜色项目
                mainColorComboBox.removeAllItems();
                mainColorComboBox.addItems("蓝色", "红色", "绿色", "白色", "黑色", "跟随系统(仅Windows)");

                MainColorSets.add(mainColorLabel);
                MainColorSets.add(mainColorComboBox);
            }

            //主题设置
            JPanel MainThemeSets = new JPanel();
            MainThemeSets.setOpaque(false);
            {

                MainThemeSets.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel mainThemeLabel = new JLabel("主题:");
                mainThemeLabel.setForeground(CTColor.textColor);
                mainThemeLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                mainThemeComboBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                //添加主题项目
                mainThemeComboBox.removeAllItems();
                mainThemeComboBox.addItems("浅色", "深色", "跟随系统(仅Windows)");

                MainThemeSets.add(mainThemeLabel);
                MainThemeSets.add(mainThemeComboBox);
            }

            buttonColorCheckBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

            ColorPanel.add(MainColorSets);
            ColorPanel.add(MainThemeSets);
            ColorPanel.add(buttonColorCheckBox);

        }

        JPanel reSetFontPanel = new JPanel();
        reSetFontPanel.setOpaque(false);
        reSetFontPanel.setLayout(new GridBagLayout());
        reSetFontPanel.setBorder(CTBorderFactory.createTitledBorder("字体设置"));
        //字体设置
        {
            //字体设置
            JPanel setFontName = new JPanel();
            setFontName.setOpaque(false);
            {

                setFontName.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel FontNameLabel = new JLabel("字体:");
                FontNameLabel.setForeground(CTColor.textColor);
                FontNameLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                FontNameComboBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                //添加颜色项目
                FontNameComboBox.removeAllItems();
                FontNameComboBox.addItems(CTFont.getAllFontName());


                setFontName.add(FontNameLabel);
                setFontName.add(FontNameComboBox);
            }
            //字体大小设置
            JPanel setFontSize = new JPanel();
            setFontSize.setOpaque(false);
            {
                setFontSize.setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel FontSizeLabel = new JLabel("字体大小-大:");
                FontSizeLabel.setForeground(CTColor.textColor);
                FontSizeLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
                CTTextField FontSizeTextField = new CTTextField();
                FontSizeTextField.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
            }

            JButton button = new JButton("改为默认");
            button.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            button.addActionListener(e -> {
                CTFont.setSize(100, 60, 24, 19, 15, 12);
                CTFont.setFontName("微软雅黑");
                FontNameComboBox.setSelectedItem("微软雅黑");
                FontSizeList.forEach(textField -> textField.setText(String.valueOf(CTFont.getBasicSize()[0])));
                MainWindow.allPanelList.forEach(ctPanel -> {
                    try {
                        ctPanel.refresh();
                    } catch (Exception ex) {
                        Log.err.print(getClass(), "刷新失败", ex);
                    }
                });
                save();
            });


            reSetFontPanel.add(setFontName);

            reSetFontPanel.add(button);

        }



        JPanel otherPanel = new JPanel();
        otherPanel.setOpaque(false);
        otherPanel.setLayout(new GridLayout(0, 2));
        otherPanel.setBorder(CTBorderFactory.createTitledBorder("其他设置"));
        //其他设置
        {

            startUp.setBackground(CTColor.backColor);
            startUp.setForeground(CTColor.textColor);
            startUp.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

            canExit.setBackground(CTColor.backColor);
            canExit.setForeground(CTColor.textColor);
            canExit.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

            StartUpdate.setBackground(CTColor.backColor);
            StartUpdate.setForeground(CTColor.textColor);
            StartUpdate.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
            StartUpdate.setSelected(true);

            isSaveLog.setBackground(CTColor.backColor);
            isSaveLog.setForeground(CTColor.textColor);
            isSaveLog.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
            isSaveLog.setSelected(true);

            otherPanel.add(startUp);
            otherPanel.add(canExit);
            otherPanel.add(StartUpdate);
            otherPanel.add(isSaveLog);


        }

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        SetsPanel.add(ColorPanel, gbc);
        gbc.gridy++;
        SetsPanel.add(reSetFontPanel, gbc);
        gbc.gridy++;
        SetsPanel.add(otherPanel, gbc);

        this.setLayout(new BorderLayout());
        //this.setBackground(CTColor.backColor);
        this.add(mainPanelScroll, BorderLayout.CENTER);

        initData();
    }

    private void initData() {
        //显示数据
        PBasicInfo info = getInfoControl().getInfo();

        //主题色设置

            switch (info.mainColor()) {
                case "black" -> mainColorComboBox.setSelectedItem("黑色");
                case "white" -> mainColorComboBox.setSelectedItem("白色");
                case "green" -> mainColorComboBox.setSelectedItem("绿色");
                case "red" -> mainColorComboBox.setSelectedItem("红色");
                case "system" -> mainColorComboBox.setSelectedItem("跟随系统(仅Windows)");
                default -> mainColorComboBox.setSelectedItem("蓝色");
            }
        //主题设置
            switch (info.mainTheme()) {
                case "dark" -> mainThemeComboBox.setSelectedItem("深色");
                case "system" -> mainThemeComboBox.setSelectedItem("跟随系统(仅Windows)");
                default -> mainThemeComboBox.setSelectedItem("浅色");
            }
        // 按钮颜色行为
        buttonColorCheckBox.setSelected(info.buttonColor());
        //字体设置
        FontNameComboBox.setSelectedItem(CTFont.getFontName());
        //

            for (int i = 0; i < info.disposeButton().length; i++) {
                String s = info.disposeButton()[i];
                if (disposeButton.containsKey(s)) {
                    disposeButton.get(s).setSelected(true);
                }
            }
        //是否可关闭
        canExit.setSelected(!info.canExit());
        //是否自动更新

            StartUpdate.setSelected(info.startUpdate());

        //是否自动启动
        startUp.setSelected(SetStartUp.isAutoStartEnabled());
        //是否保存日志
        isSaveLog.setSelected(info.isSaveLog());


    }

    @Override
    public void save() {

        //设置主题色
        String tempMainColor = switch (Objects.requireNonNull(mainColorComboBox.getSelectedItem()).toString()) {
            case "黑色" -> "black";
            case "白色" -> "white";
            case "绿色" -> "green";
            case "红色" -> "red";
            case "跟随系统(仅Windows)" -> "system";
            default -> "blue";
        };

        //设置主题
        String tempMainThemeColor = switch (Objects.requireNonNull(mainThemeComboBox.getSelectedItem()).toString()) {
            case "深色" -> "dark";
            case "跟随系统(仅Windows)" -> "system";
            default -> "light";
        };

        //设置字体
        String tempFontName = Objects.requireNonNull(FontNameComboBox.getSelectedItem(), "微软雅黑").toString();
        //设置隐藏按钮

            ArrayList<String> disposeButtonList = new ArrayList<>();
            disposeButton.forEach((key, value) -> {
                if (value.isSelected()) {
                    disposeButtonList.add(key);
                }
            });

        //设置是否自动启动
        String Path = SetStartUp.getFilePath();
        if (!startUp.isSelected()) {
            SetStartUp.disableAutoStart();// 移除自启动
        } else {
            if (Path != null) {
                if (Path.endsWith(".jar")) {
                    SetStartUp.enableAutoStart("javaw -jar " + Path); // 使用javaw避免黑窗口
                } else if (Path.endsWith(".exe")) {
                    SetStartUp.enableAutoStart(Path);
                }
            }

        }
        getInfoControl().setInfo(
                new PBasicInfo(!canExit.isSelected(), StartUpdate.isSelected(), isSaveLog.isSelected(),
                        tempMainColor, tempMainThemeColor, buttonColorCheckBox.isSelected(),
                        tempFontName, disposeButtonList.toArray(new String[0])));
    }

    @Override
    public void refresh() {

        getInfoControl().refresh();

        initUI();

        this.revalidate();
        this.repaint();
    }
}
