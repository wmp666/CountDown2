package com.wmp.countdown.frame;

import com.wmp.countdown.extraPanel.countdown.panel.CountDownPanel;
import com.wmp.countdown.importPanel.finalPanel.FinalPanel;
import com.wmp.countdown.importPanel.timeView.OtherTimeThingPanel;
import com.wmp.countdown.importPanel.timeView.TimeViewPanel;
import com.wmp.publicTools.CTInfo;
import com.wmp.publicTools.UITools.CTColor;
import com.wmp.publicTools.printLog.Log;
import com.wmp.countdown.CTComponent.CTBorderFactory;
import com.wmp.countdown.CTComponent.CTPanel.CTViewPanel;
import com.wmp.countdown.CTComponent.CTWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;

public class MainWindow extends CTWindow {
    //private final JPanel centerPane = new JPanel(); // 用于放置中间组件的面板

    public static final ArrayList<CTViewPanel> allPanelList = new ArrayList<>();
    //public static final ArrayList<CTViewPanel> showPanelList = new ArrayList<>();
    public static final TreeMap<String, CTViewPanel[]> panelMap = new TreeMap<>();
    private static final TreeMap<String, String[]> panelLocationMap = new TreeMap<>();
    public static JFrame mainWindow = new JFrame();

    public MainWindow(String path) throws IOException{

        mainWindow = this;

        panelLocationMap.put("上方", new String[]{"TimeViewPanel", "OtherTimeThingPanel"});
        panelLocationMap.put("下方", new String[]{"FinalPanel"});

        //添加组件
        TimeViewPanel timeViewPanel = new TimeViewPanel();
        OtherTimeThingPanel otherTimeThingPanel = new OtherTimeThingPanel();
        FinalPanel finalPanel = new FinalPanel();


        CountDownPanel countDownPanel = new CountDownPanel();
        allPanelList.add(countDownPanel);

        allPanelList.add(timeViewPanel); // 添加到列表中以便统一管理
        allPanelList.add(otherTimeThingPanel);
        allPanelList.add(finalPanel);


            initFrame();


                refreshPanel();
                // 确保窗口可见
                this.setVisible(true);
                this.toFront();

                //刷新
                Timer repaint = new Timer(500, e -> {

                    allPanelList.forEach(ctPanel -> {
                        ctPanel.setOpaque(false);

                        ctPanel.revalidate();
                        ctPanel.repaint();
                    });


                    // 获取本地图形环境
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    // 获取默认屏幕设备
                    GraphicsDevice gd = ge.getDefaultScreenDevice();

                    // 获取屏幕的工作区域（排除任务栏等系统UI）
                    Rectangle workArea = gd.getDefaultConfiguration().getBounds();

                    Dimension size = this.getPreferredSize();

                    Dimension screenSize = new Dimension(workArea.width, workArea.height);
                    if (size.height >= screenSize.getHeight() * 9 / 10)
                        this.setSize(new Dimension(size.width, (int) screenSize.getHeight()));
                    else this.setSize(new Dimension(size.width, size.height));
                    this.setLocation(screenSize.width - size.width - 5, 5);

                    //this.setLocation(0,0);
                    this.getContentPane().setBackground(CTColor.backColor);

                    this.repaint();
                });
                repaint.start();

                //刷新数据
                Timer strongRepaint = new Timer(60 * 60 * 1000, e -> refreshPanel());
                strongRepaint.start();

    }

    public static void refresh() {
        refreshPanel();
    }

    public static void refreshPanel() {
        try {
            Log.info.systemPrint("MainWindow", "正在刷新");
            CTInfo.init(true);

            //初始化边框
            CTBorderFactory.BASIC_LINE_BORDER = BorderFactory.createLineBorder(new Color(200, 200, 200), (int) (2 * CTInfo.dpi), true);
            CTBorderFactory.FOCUS_GAINTED_BORDER = BorderFactory.createLineBorder(new Color(112, 112, 112), (int) (2 * CTInfo.dpi), true);


            panelMap.clear();

            //初始化组件位置
            allPanelList.forEach(panel -> {

                if (!CTInfo.disPanelList.contains(panel.getID())) {
                    panel.setVisible(true);
                    //showPanelList.add(panel);

                    if (Arrays.asList(panelLocationMap.get("上方")).contains(panel.getID())) {
                        java.util.List<CTViewPanel> temp = new ArrayList<>(Arrays.asList(panelMap.getOrDefault("上方", new CTViewPanel[0])));
                        temp.add(panel);
                        panelMap.put("上方", temp.toArray(new CTViewPanel[0]));
                    } else if (Arrays.asList(panelLocationMap.get("下方")).contains(panel.getID())) {
                        java.util.List<CTViewPanel> temp = new ArrayList<>(Arrays.asList(panelMap.getOrDefault("下方", new CTViewPanel[0])));
                        temp.add(panel);
                        panelMap.put("下方", temp.toArray(new CTViewPanel[0]));
                    } else {
                        // 使用 new ArrayList 包装，创建可变列表
                        java.util.List<CTViewPanel> temp = new ArrayList<>(Arrays.asList(panelMap.getOrDefault("中间", new CTViewPanel[0])));
                        temp.add(panel);
                        panelMap.put("中间", temp.toArray(new CTViewPanel[0]));
                    }
                } else panel.setVisible(false);
            });

               JPanel northPanel = new JPanel();
                JPanel southPanel = new JPanel();
                JPanel centerPanel = new JPanel();

                northPanel.setLayout(new GridBagLayout());
                southPanel.setLayout(new GridBagLayout());
                centerPanel.setLayout(new GridBagLayout());

                northPanel.setOpaque(false);
                southPanel.setOpaque(false);
                centerPanel.setOpaque(false);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.anchor = GridBagConstraints.WEST;
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1; // 添加水平权重
                //gbc.fill = GridBagConstraints.HORIZONTAL; // 水平填充
                gbc.insets = new Insets(0, 0, 0, 0); // 确保没有额外的边距

                TreeMap<String, Integer> countMap = new TreeMap<>();

                //1.将要显示的组件添加到显示列表
                panelMap.forEach((key, value) -> {
                    for (CTViewPanel panel : value) {
                        countMap.put(key, countMap.getOrDefault(key, 0) + 1);
                        gbc.gridy = countMap.get(key);

                        gbc.fill = GridBagConstraints.NONE;
                        if (key.equals("上方")) {
                            gbc.fill = GridBagConstraints.BOTH;
                            northPanel.add(panel, gbc);
                        } else if (key.equals("下方")) {
                            southPanel.add(panel, gbc);
                        } else {
                            centerPanel.add(panel, gbc);
                        }
                    }
                });

                JScrollPane scrollPane = new JScrollPane(centerPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());


                // 在添加新组件前移除所有现有组件
                mainWindow.getContentPane().removeAll();
                mainWindow.add(northPanel, BorderLayout.NORTH);
                mainWindow.add(southPanel, BorderLayout.SOUTH);
                mainWindow.add(scrollPane, BorderLayout.CENTER);

                mainWindow.revalidate();
                mainWindow.repaint();

                // 确保窗口可见并置于前端
                mainWindow.setVisible(true);
                //mainWindow.toFront();}

            allPanelList.forEach(panel -> {
                try {
                    panel.refresh();
                } catch (Exception e) {
                    Log.err.print(MainWindow.class, "刷新失败", e);
                }
            });
        } catch (Exception e) {
            Log.err.print(MainWindow.class, "刷新失败", e);
        }
    }

    private void initFrame() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle(CTInfo.appName);

        this.setLayout(new BorderLayout());

        this.getContentPane().setForeground(CTColor.backColor);
        try {
            this.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(CTInfo.iconPath))).getImage());
        } catch (Exception e) {
            Log.err.print(MainWindow.class, "图标加载失败", e);
        }

        this.pack();
    }
}