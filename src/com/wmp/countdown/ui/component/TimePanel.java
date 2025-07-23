package com.wmp.countdown.ui.component;

import com.wmp.countdown.tools.CDInfo;
import com.wmp.countdown.tools.printLog.Log;
import com.wmp.countdown.tools.uiTools.CDFontSizeStyle;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class TimePanel extends JPanel implements CDComponent {

    private final JLabel dayLabel = new JLabel();
    private final JLabel otherTimeLabel = new JLabel();

    public TimePanel() {
        this.setOpaque( false);
        this.setLayout(new BorderLayout());

        dayLabel.setText("Days");
        otherTimeLabel.setText("Hours : Minutes : Seconds");

        initUI();

        this.add(dayLabel, BorderLayout.CENTER);
        this.add(otherTimeLabel, BorderLayout.SOUTH);

        Timer timer = new Timer(34, e -> {
            String targetTime = CDInfo.targetTime;
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            long time = 0;
            try {
                // 获取时间, 并计算时间差
                time = dateFormat.parse(targetTime).getTime() - System.currentTimeMillis();
            } catch (ParseException ex) {
                Log.err.print("时间显示","时间数据化异常:\n" + ex.getMessage());
                throw new RuntimeException(ex);
            }
            //Log.info.print("时间显示","时间差:" + time);

            if (time <= 0) {
                dayLabel.setText("已结束");
                otherTimeLabel.setText("");
                return;
            }


            dayLabel.setText(String.format("%02d", time / (24L *60*60*1000)) + "天");
            time %= 24L *60*60*1000;// 去除n天(n * 24h)的时间,只留下余数
            otherTimeLabel.setText(String.format("%02d时%02d分%02d秒", time / 3600000, time / 60000 % 60, time / 1000 % 60));
        });
        timer.setRepeats(true);
        timer.start();
    }

    public void initUI(){



        dayLabel.setForeground(CDInfo.COLOR.getMainColor());
        dayLabel.setFont(CDInfo.FONT.getCDFont(Font.BOLD, CDFontSizeStyle.MORE_BIG));
        otherTimeLabel.setForeground(CDInfo.COLOR.getMainColor());
        otherTimeLabel.setFont(CDInfo.FONT.getCDFont(Font.BOLD, CDFontSizeStyle.BIG));
    }

    @Override
    public void refresh() {
        initUI();
    }
}
