package com.wmp.countdown.extraPanel.countdown;

import com.wmp.publicTools.DateTools;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public record CountDownInfos(ArrayList<CountDownInfo> list) {
    public CountDownInfo getLatestInfo(){
        AtomicReference<CountDownInfo> resultInfo = new AtomicReference<>(new CountDownInfo("数据出错", "3000.01.01 00:00:00"));
        //CDInfo resultInfo = new CDInfo("数据出错", "2030.01.01 00:00:00");
        AtomicLong remainderTime = new AtomicLong();

        list().forEach(info -> {
                long tempRemainderTime = DateTools.getRemainderTime(info.targetTime(), "yyyy.MM.dd HH:mm:ss");

                if (resultInfo.get().title().equals("数据出错")) {
                    resultInfo.set(info);
                    remainderTime.set(tempRemainderTime);
                    return;
                }

                if (tempRemainderTime > 0) {

                    if (remainderTime.get() > tempRemainderTime) {
                        resultInfo.set(info);
                        remainderTime.set(tempRemainderTime);
                    }
                }
        });
        return resultInfo.get();
    }
}
