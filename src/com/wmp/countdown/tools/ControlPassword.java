package com.wmp.countdown.tools;

import com.wmp.countdown.tools.io.IOForInfo;
import com.wmp.countdown.tools.printLog.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class ControlPassword {

    public static boolean checkPassword() {
        Log.info.print("Log", "检查密码");
        try {
            IOForInfo io = new IOForInfo(CDInfo.DATA_PATH + "password.txt");
            String password = io.GetInfos();

            if (password.isEmpty() || password.equals("err")) {
                Log.warn.print("Log", "密码为空, 建议设置");
                return true;
            }

            String s = Log.info.showInputDialog(null, "请输入密码", "请输入密码");
            if (s != null) {

                String encryption = encryption(s);

                if (encryption.equals(password)) {
                    Log.info.print("Log", "密码正确");
                    return true;
                } else {
                    Log.warn.print("Log", "密码错误");
                    return false;
                }

            }
        } catch (Exception e) {
            Log.err.print(null, "Log", "密码检查失败:\n" + e);
        }
        return false;
    }

    public static void setPassword() {
        Log.info.print("Log", "设置密码");

        String[] ss = Log.info.showInputDialog(null, "密码管理", "请输入密码\n注:清除密码时,可以不输入", "设置密码", "清除密码");
        String s = ss[1];
        if (s != null && ss[0].equals("设置密码")) {

            try {
                String encryption = encryption(s);
                IOForInfo io = new IOForInfo(CDInfo.DATA_PATH + "password.txt");
                io.SetInfo(encryption);
            } catch (Exception e) {
                Log.err.print(null, "Log", "密码加密失败:\n" + e);
                throw new RuntimeException(e);
            }

            Log.info.systemPrint("Log", "密码已保存");
        }else{
            IOForInfo io = new IOForInfo(CDInfo.DATA_PATH + "password.txt");
            try {
                io.SetInfo("");
            } catch (IOException e) {
                Log.err.print(null, "Log", "密码清除失败:\n" + e);
                throw new RuntimeException(e);
            }
            Log.info.systemPrint("Log", "密码已清除");
        }
    }

    private static String encryption(String s) throws Exception {
        MessageDigest sha = null;
        sha = MessageDigest.getInstance("SHA");

        byte[] byteArray = s.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
