package com.wmp.countdown.extraPanel.countdown.panel;

import com.wmp.publicTools.DateTools;
import com.wmp.publicTools.UITools.CTColor;
import com.wmp.publicTools.UITools.CTFont;
import com.wmp.publicTools.UITools.CTFontSizeStyle;
import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.publicTools.printLog.Log;
import com.wmp.countdown.CTComponent.CTPanel.CTViewPanel;
import com.wmp.countdown.extraPanel.countdown.CDInfoControl;
import com.wmp.countdown.extraPanel.countdown.CountDownInfo;
import com.wmp.countdown.extraPanel.countdown.CountDownInfos;
import com.wmp.countdown.extraPanel.countdown.settings.CountDownSetsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CountDownPanel extends CTViewPanel<CountDownInfos> {
    private CountDownInfo info = getInfoControl().getInfo().getLatestInfo();

    private final JLabel titleLabel = new JLabel();
    private final JLabel timeLabel = new JLabel();
    private final JLabel restTimeLabel = new JLabel();

    private final Font bigFont = CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.MORE_BIG);
    private final Font normalFont = CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG);

    private final AtomicBoolean b = new AtomicBoolean(false);

    public CountDownPanel() {
        this.setID("CountDownPanel");
        this.setName("倒计时界面");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setCtSetsPanelList(List.of(new CountDownSetsPanel(getInfoControl())));

        initUI();

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(timeLabel, BorderLayout.CENTER);
        this.add(restTimeLabel, BorderLayout.SOUTH);

        this.setIndependentRefresh(true, 34);

    }

    private void initUI() {


        titleLabel.setText("距" + info.title() + "还剩:");
        titleLabel.setForeground(CTColor.textColor);
        titleLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));

        timeLabel.setForeground(CTColor.mainColor);
        timeLabel.setFont(bigFont);
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        restTimeLabel.setForeground(CTColor.mainColor);
        restTimeLabel.setFont(normalFont);
        restTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
    }

    @Override
    public void strongRefresh() throws Exception {
        info = getInfoControl().refresh().getLatestInfo();
        super.strongRefresh();
    }

    @Override
    public CTInfoControl<CountDownInfos> setInfoControl() {
        return new CDInfoControl();
    }

    @Override
    protected void easyRefresh() {
        initUI();

        String targetTime = info.targetTime();
        long time = 0;
        try {
            // 获取时间, 并计算时间差
            time = DateTools.getRemainderTime(targetTime, "yyyy.MM.dd HH:mm:ss");
        } catch (Exception ex) {
            Log.err.print(getClass(), "时间数据化异常", ex);
        }
        //Log.info.print("时间显示","时间差:" + time);
        if (time < -10 * 1000) {
            CountDownInfo old = info;
            info = getInfoControl().getInfo().getLatestInfo();
            if (!old.title().equals(info.title()) && info.title().equals("数据出错")) {
                Log.info.systemPrint("时间显示", "已切换倒计时");
            }
        }

        if (time <= 0) {

            timeLabel.setText("已结束");
            restTimeLabel.setText("");
            if (!b.get()) {
                Log.info.adaptedMessage(info.title() + "倒计时", "已结束", 60, 3);
            }
            b.set(true);
            return;
        }

        b.set(false);
        timeLabel.setText(buildTimeText(time));

        this.revalidate();
        this.repaint();
    }

    /**
     * 构建倒计时显示：主要单位（大字体，显示在提示词下方），其余时间（原字体，显示在下方）。
     * 天不足1天时主要显示小时，小时不足1时主要显示分钟，以此类推到秒。
     */
    private String buildTimeText(long time) {
        long days = time / (24L * 60 * 60 * 1000);
        long rem = time % (24L * 60 * 60 * 1000);// 去除n天(n * 24h)的时间,只留下余数
        long hours = rem / 3600000;
        long minutes = rem / 60000 % 60;
        long seconds = rem / 1000 % 60;

        String primary;
        String rest;
        if (days >= 1) {
            primary = String.format("%02d天", days);
            rest = buildRest(hours, minutes, seconds);
        } else if (hours >= 1) {
            primary = String.format("%02d时", hours);
            rest = buildRest(0, minutes, seconds);
        } else if (minutes >= 1) {
            primary = String.format("%02d分", minutes);
            rest = buildRest(0, 0, seconds);
        } else {
            primary = String.format("%02d秒", seconds);
            rest = "";
        }

        restTimeLabel.setText(rest);
        return primary;
    }

    /** 将剩余的时间分量拼接为"X时X分X秒"，省略值为0的分量。 */
    private String buildRest(long hours, long minutes, long seconds) {
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(String.format("%02d时", hours));
        }
        if (minutes > 0) {
            sb.append(String.format("%02d分", minutes));
        }
        if (seconds > 0) {
            sb.append(String.format("%02d秒", seconds));
        }
        return sb.toString();
    }
}
