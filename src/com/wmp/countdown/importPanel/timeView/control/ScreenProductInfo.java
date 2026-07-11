package com.wmp.countdown.importPanel.timeView.control;

import java.util.ArrayList;

/**
 * 屏保信息
 * @param mainColor 主题颜色
 * @param mainTheme 主题
 * @param BGBasicPath 图片路径
 * @param BGImagePathList 图片路径列表
 * @param repaintTimer 刷新间隔
 */
public record ScreenProductInfo(String mainColor, String mainTheme, String BGBasicPath, ArrayList<String> BGImagePathList, int repaintTimer) {
    public ScreenProductInfo getNewSPInfo(String mainColor, String mainTheme, String BGBasicPath, ArrayList<String> BGImagePathList, int repaintTimer) {
        mainColor = mainColor == null ? this.mainColor : mainColor;
        mainTheme = mainTheme == null ? this.mainTheme : mainTheme;
        BGBasicPath = BGBasicPath == null ? this.BGBasicPath : BGBasicPath;
        BGImagePathList = BGImagePathList == null ? this.BGImagePathList : BGImagePathList;
        repaintTimer = repaintTimer == 0 ? this.repaintTimer : repaintTimer;
        return new ScreenProductInfo(mainColor, mainTheme, BGBasicPath, BGImagePathList, repaintTimer);
    }
}
