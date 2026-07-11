package com.wmp.countdown.infSet.panel.personalizationSets.basicSets;

import com.wmp.publicTools.CTInfo;
import com.wmp.publicTools.UITools.CTColor;
import com.wmp.publicTools.UITools.CTFont;
import com.wmp.publicTools.UITools.CTFontSizeStyle;
import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.countdown.CTComponent.CTBorderFactory;
import com.wmp.countdown.CTComponent.CTCheckBox;
import com.wmp.countdown.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel;
import com.wmp.countdown.CTComponent.CTTextField;
import com.wmp.countdown.frame.MainWindow;
import com.wmp.countdown.infSet.panel.personalizationSets.control.PPanelInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PBPanelSetsPanel extends CTBasicSetsPanel<PPanelInfo> {
    //隐藏面板 数据
    private final TreeMap<String, CTCheckBox> disposePanel = new TreeMap<>();
    //后台运行组件列表
    private final TreeMap<String, CTCheckBox> runInBackgroundPanel = new TreeMap<>();
    //兼容数据
    private final CTTextField dpi = new CTTextField();

    public PBPanelSetsPanel(CTInfoControl<PPanelInfo> infoControl) {
        super(infoControl);

        setName("组件设置");

        initUI();
    }

    private void initUI() {
        this.removeAll();

        JPanel SetsPanel = new JPanel();
        SetsPanel.setOpaque(false);

        JScrollPane mainPanelScroll = new JScrollPane(SetsPanel);
        mainPanelScroll.setOpaque(false);
        mainPanelScroll.getViewport().setOpaque(false);
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanelScroll.setSize(400, 400);

        SetsPanel.setLayout(new GridBagLayout());//new GridLayout(0,1)
        GridBagConstraints gbc = new GridBagConstraints();

        //设置组件隐藏
        JPanel disPanPanel = new JPanel();
        {

            disPanPanel.setOpaque(false);
            disPanPanel.setLayout(new GridLayout(0, 2));

            disPanPanel.setBorder(CTBorderFactory.createTitledBorder("隐藏部分组件"));
            {

                MainWindow.allPanelList.forEach(panel -> {

                    CTCheckBox checkBox = new CTCheckBox(panel.getName());
                    checkBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
                    checkBox.setBackground(CTColor.backColor);
                    checkBox.setForeground(CTColor.textColor);
                    disposePanel.put(panel.getID(), checkBox);
                });

                disposePanel.forEach((key, value) -> disPanPanel.add(value));
            }
        }

        //是否允许在后台运行
        JPanel canRunInBackgroundPanel = new JPanel();
        {

            canRunInBackgroundPanel.setOpaque(false);
            canRunInBackgroundPanel.setLayout(new GridLayout(0, 2));
            //设置组件是否可以后台运行
            canRunInBackgroundPanel.setBorder(CTBorderFactory.createTitledBorder("允许在后台运行的组件"));
            {

                MainWindow.allPanelList.forEach(panel -> {

                    CTCheckBox checkBox = new CTCheckBox(panel.getName());
                    checkBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
                    checkBox.setBackground(CTColor.backColor);
                    checkBox.setForeground(CTColor.textColor);
                    runInBackgroundPanel.put(panel.getID(), checkBox);
                });

                runInBackgroundPanel.forEach((_, value) -> canRunInBackgroundPanel.add(value));
            }
        }

        //其他设置
        JPanel compatiblePanel = new JPanel();
        {

            compatiblePanel.setOpaque(false);
            compatiblePanel.setLayout(new GridLayout(0, 2));
            compatiblePanel.setBorder(CTBorderFactory.createTitledBorder("其他设置"));

            {
                JPanel dpiPanel = new JPanel();
                dpiPanel.setOpaque(false);
                dpiPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                {
                    JLabel dpiLabel = new JLabel("组件放大倍率:");
                    dpiLabel.setForeground(CTColor.textColor);
                    dpiLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                    dpi.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
                    dpi.setText(String.valueOf(CTInfo.dpi));


                    dpiPanel.add(dpiLabel);
                    dpiPanel.add(dpi);
                }

                compatiblePanel.add(dpiPanel);


            }
        }


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        SetsPanel.add(disPanPanel, gbc);
        gbc.gridy++;
        SetsPanel.add(canRunInBackgroundPanel, gbc);
        gbc.gridy++;
        SetsPanel.add(compatiblePanel, gbc);

        this.setLayout(new BorderLayout());
        //this.setBackground(CTColor.backColor);
        this.add(mainPanelScroll, BorderLayout.CENTER);

        //显示数据
        {
            PPanelInfo info = getInfoControl().getInfo();

            disposePanel.forEach((key, value) -> {
                if (List.of(info.disPanelList()).contains( key)) {
                    value.setSelected(true);
                }
            });
            runInBackgroundPanel.forEach((key, value) -> {
                if (List.of(info.runInBackgroundList()).contains( key)) {
                    value.setSelected(true);
                }
            });
            dpi.setText(String.valueOf(CTInfo.dpi));

        }
    }

    @Override
    public void save() {

        //保存数据-个性化


            String[] disPanelList;
            String[] runInBackgroundPanelList;

            //设置隐藏面板
            {
                ArrayList<String> tempList = new ArrayList<>();
                disposePanel.forEach((key, value) -> {
                    if (value.isSelected()) {
                        tempList.add(key);
                    }
                });
                disPanelList = tempList.toArray(new String[0]);
            }
            //设置允许后台运行
            {
                ArrayList<String> tempList = new ArrayList<>();
                runInBackgroundPanel.forEach((key, value) -> {
                    if (value.isSelected()) {
                        tempList.add(key);
                    }
                });
                runInBackgroundPanelList = tempList.toArray(new String[0]);
            }
            double DDpi = 1.0;
            //设置DPI
            try {
                DDpi =  Double.parseDouble(dpi.getText());
            } catch (Exception _) {

            }

            getInfoControl().setInfo(new PPanelInfo(disPanelList, runInBackgroundPanelList, DDpi));

    }

    @Override
    public void refresh() {
        getInfoControl().refresh();

        initUI();

        this.revalidate();
        this.repaint();
    }
}
