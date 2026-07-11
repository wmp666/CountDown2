package com.wmp.countdown.extraPanel.countdown.settings;

import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.publicTools.printLog.Log;
import com.wmp.countdown.CTComponent.CTOptionPane;
import com.wmp.countdown.CTComponent.CTPanel.setsPanel.CTTableSetsPanel;
import com.wmp.countdown.extraPanel.countdown.CountDownInfo;
import com.wmp.countdown.extraPanel.countdown.CountDownInfos;

import java.util.ArrayList;

public class CountDownSetsPanel extends CTTableSetsPanel<CountDownInfos> {


    public CountDownSetsPanel(CTInfoControl<CountDownInfos> cdInfoControl) {
        super(new String[]{"标题", "目标时间"}, null, cdInfoControl);

        this.setID("CountDownSetsPanel");
        this.setName("倒计时设置");

        setArray(getInfo());
    }

    private String[][] getInfo() {
        ArrayList<CountDownInfo> list = getInfoControl().refresh().list();
        String[][] data = new String[list.size()][2];

        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i).title();
            data[i][1] = list.get(i).targetTime();
        }
        if (list.isEmpty()) {
            data = new String[][]{{"null", "9999.12.30 23:59:59"}};
        }

        return data;
    }


    @Override
    public String[] addToTable() {

        String name = Log.info.showInputDialog(this, "CountDownSetsPanel-新建", "请输入标题");
        if (name == null || name.trim().isEmpty()) return null;

        int[] dates = Log.info.showTimeChooseDialog(this, "CountDownSetsPanel-新建", "请输入日期", CTOptionPane.YEAR_MONTH_DAY);
        if (dates.length != 3) return null;

        int[] times = Log.info.showTimeChooseDialog(this, "CountDownSetsPanel-新建", "请输入时间", CTOptionPane.HOURS_MINUTES_SECOND);
        if (times.length != 3) return null;

        String time = String.format("%04d.%02d.%02d %02d:%02d:%02d", dates[0], dates[1] ,dates[2], times[0], times[1], times[2]);

        return new String[]{name, time};
    }

    @Override
    public String[] modifyToTable(String[] oldArray) {
        String name = Log.info.showInputDialog(this, "CountDownSetsPanel-修改",
                String.format("原数据:%s\n请输入姓名\n注:若不修改不用输入内容", oldArray[0]));
        if (name == null || name.trim().isEmpty()) name = oldArray[0];

        int[] oldDates = new int[3];
        int[] oldTimes = new int[3];

        String[] temp = oldArray[1].split(" ");
        String[] oldStrDate = temp[0].split("\\.");
        for (int i = 0; i < 3; i++) {
            oldDates[i] = Integer.parseInt(oldStrDate[i]);
        }
        String[] oldStrTime = temp[1].split(":");
        for (int i = 0; i < 3; i++) {
            oldTimes[i] = Integer.parseInt(oldStrTime[i]);
        }



        int[] dates = Log.info.showTimeChooseDialog(this, "CountDownSetsPanel-新建", "请输入日期", CTOptionPane.YEAR_MONTH_DAY, oldDates);
        if (dates.length != 3) return null;

        int[] times = Log.info.showTimeChooseDialog(this, "CountDownSetsPanel-新建", "请输入时间", CTOptionPane.HOURS_MINUTES_SECOND, oldTimes);
        if (times.length != 3) return null;

        String time = String.format("%04d.%02d.%02d %02d:%02d:%02d", dates[0], dates[1] ,dates[2], times[0], times[1], times[2]);


        return new String[]{name, time};
    }

    @Override
    public void save() throws Exception {
        String[][] data = getArray();
        ArrayList<CountDownInfo> list = new ArrayList<>();
        for (int i = 1; i < data.length; i++) {
            CountDownInfo info = new CountDownInfo(data[i][0], data[i][1]);
            list.add(info);
        }
        getInfoControl().setInfo(new CountDownInfos(list));
    }

    @Override
    public String[][] resetData() {
        return getInfo();
    }
}
