package com.wmp.countdown.infSet.tools;

import com.wmp.publicTools.printLog.Log;

import java.io.*;

public class SetStartUp {

    // 注册表项路径（当前用户）
    private static final String REGISTRY_PATH =
            "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
    // 自启动项名称
    private static final String ENTRY_NAME = "CountDown";

    /**
     * 添加程序到开机自启动
     *
     * @param programPath 程序的完整路径（需用双引号包裹路径）
     */
    public static void enableAutoStart(String programPath) {
        System.out.println("programPath" + programPath);
        try {
            executeCommand(new String[]{"reg", "add", REGISTRY_PATH, "/v", ENTRY_NAME, "/t", "REG_SZ", "/d", programPath, "/f"});
            System.out.println("已添加开机自启动！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除开机自启动项
     */
    public static void disableAutoStart() {
        try {
            executeCommand(new String[]{"reg", "delete", REGISTRY_PATH, "/v", ENTRY_NAME, "/f"});
            System.out.println("已移除开机自启动！");
        } catch (Exception e) {
            Log.err.print(SetStartUp.class, "错误", e);
        }
    }

    /**
     * 检查自启动项是否存在
     */
    public static boolean isAutoStartEnabled() {
        try {
            String[] command = {"reg", "query", REGISTRY_PATH, "/v", ENTRY_NAME};
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(ENTRY_NAME)) {
                    return true;
                }
            }
        } catch (IOException e) {
            Log.err.print(SetStartUp.class, "错误", e);
        }
        return false;
    }

    /**
     * 执行命令并等待完成
     */
    private static void executeCommand(String[] command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (reader.readLine() != null) {
            // 输出命令执行结果
            System.out.println(reader.readLine());
        }
    }

    /**
     * 获取当前JAR文件的路径（需在可执行JAR中运行）
     */
    public static String getFilePath() {
        String path = System.getProperty("user.dir");
        File appPath = new File(path);
        File[] files = appPath.listFiles();
        for (File file : files) {
            if (file.getName().equals("CountDown2.exe")) {
                return file.getPath();
            }
            if (file.getName().equals("CountDown2.jar")) {
                return file.getPath();
            }
        }
        return null;
    }

}
