package com.wmp.countdown.extraPanel.countdown;

/**
 * 倒计时信息
 *
 * @param title      名字
 * @param targetTime 目标时间(yyyy.MM.dd HH:mm:ss)
 */
public record CountDownInfo(String title, String targetTime) {
}
