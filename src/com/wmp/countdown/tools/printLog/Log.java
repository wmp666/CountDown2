package com.wmp.countdown.tools.printLog;


import com.wmp.WCompanent.WOptionPane;
import com.wmp.WCompanent.tools.GetIcon;
import com.wmp.countdown.tools.CDInfo;
import com.wmp.countdown.tools.OpenInExp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Log {
    private static final ArrayList<String> logInfList = new ArrayList<>();
    private static int index = 0;


    private static final TrayIcon trayIcon = new TrayIcon(GetIcon.getImageIcon(CDInfo.class.getResource(CDInfo.ICON_PATH), 16, 16).getImage(), "ClassTools");

    private static final JTextArea textArea = new JTextArea();

    private static final Thread thread = new Thread(() -> {
        while (true) {
            synchronized (logInfList) { // 恢复同步块
                int currentSize = logInfList.size();
                if (index < currentSize) {
                    //SwingUtilities.invokeLater(() -> {
                    for (int i = index; i < currentSize; i++) {
                        textArea.append(logInfList.get(i) + "\n");
                        //System.out.printf("内容(%s): %s%n", i, logInfList.get(i)); // 添加换行
                    }
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    //});
                    index = currentSize; // 在同步块内更新索引


                }
            }
            try {
                Thread.sleep(1000);  // 缩短刷新间隔
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    });

    public static InfoLogStyle info = new InfoLogStyle(LogStyle.INFO);
    public static PrintLogStyle warn = new PrintLogStyle(LogStyle.WARN);
    public static PrintLogStyle err = new PrintLogStyle(LogStyle.ERROR);

    static {
        SystemTray systemTray = SystemTray.getSystemTray();
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        thread.setDaemon(true);
        thread.start();  // 确保启动线程
    }

    public Log() {
    }

    public static void exit(int status) {

        // 改为自动关闭窗口
        System.exit(status);


        saveLog(false);
    }

    public static void systemPrint(LogStyle style, String owner, String logInfo) {

        if (SystemTray.isSupported() && Objects.requireNonNull(style) == LogStyle.INFO) {
            trayIcon.displayMessage(owner, logInfo, TrayIcon.MessageType.INFO);
        }
        Log.print(style, owner, logInfo, null);
    }
    public static void print(LogStyle style, String owner, String logInfo, Container c) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");
        String dateStr = dateFormat.format(date);

        String info;
        switch (style) {
            case INFO -> {

                info = "[" + dateStr + "]" +
                        "[info]" +
                        "[" + owner + "]: " +
                        logInfo;
                System.out.println(info);
                logInfList.add(info);
            }

            case WARN -> {

                info = "[" + dateStr + "]" +
                        "[警告]" +
                        "[" + owner + "] :" +
                        logInfo;
                trayIcon.displayMessage(owner, logInfo, TrayIcon.MessageType.WARNING);
                System.out.println(info);
                logInfList.add(info);
            }

            case ERROR -> {

                info = "[" + dateStr + "]" +
                        "[错误]" +
                        "[" + owner + "] :" +
                        logInfo;
                trayIcon.displayMessage(owner, logInfo, TrayIcon.MessageType.ERROR);
                System.err.println(info);


                String title = "世界拒绝了我";

                WOptionPane.showMessageDialog(c, title, logInfo, null, WOptionPane.ERROR_MESSAGE, true);

                logInfList.add(info);
            }
        }
    }

    public static ArrayList<String> getLogInfList() {
        return logInfList;
    }

    public static void showLogDialog() {
        //dialog.removeAll();
        JDialog dialog = new JDialog((Frame) null, false);
        dialog.setTitle("日志");
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(textArea);
        //scrollPane.setPreferredSize(new Dimension(480, 550));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dialog.dispose());


        JButton clearButton = new JButton("清空");
        clearButton.addActionListener(e -> {
            int i = Log.info.showChooseDialog(dialog, "日志-清空", "是否清空并保存?");
            if (i == JOptionPane.YES_OPTION){
                saveLog();
            }
            textArea.setText("");
            logInfList.clear();
            index = 0;

        });

        JButton openButton = new JButton("打开所在位置");
        openButton.addActionListener(e -> {
            if ( !Files.exists(Paths.get(CDInfo.DATA_PATH + "Log\\"))){
                try {
                    Files.createDirectories(Paths.get(CDInfo.DATA_PATH + "Log\\"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            OpenInExp.open(CDInfo.DATA_PATH + "Log\\");
        });

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            saveLog();
        });
        buttonPanel.add(closeButton);
        buttonPanel.add(openButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);




        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);

    }

    private static void saveLog() {
        saveLog(true);
    }

    private static void saveLog(boolean showMessageDialog){
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        //将logInfList中的内容转化为byte数组
        StringBuilder sb = new StringBuilder();
        for (String s : logInfList) {
            sb.append(s).append("\n");
        }
        // 实现日志保存
        try {
            if (!Files.exists(Paths.get(CDInfo.DATA_PATH + "Log\\"))){
                Files.createDirectories(Paths.get(CDInfo.DATA_PATH + "Log\\"));
            }
            Files.writeString(Paths.get(CDInfo.DATA_PATH + "Log\\Log_" + dateFormat.format(new Date()) + ".txt"),
                    sb.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            if (showMessageDialog)
                Log.info.message(null, "Log", "日志保存成功");
        } catch (IOException e) {
            if (showMessageDialog)
                Log.err.print("Log", "日志保存失败");
            throw new RuntimeException(e);
        }
    }
}


