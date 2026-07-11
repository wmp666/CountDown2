package com.wmp.countdown.CTComponent.CTPanel;

import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.publicTools.printLog.Log;
import com.wmp.countdown.CTComponent.CTPanel.setsPanel.CTSetsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CTViewPanel<T> extends CTPanel {

    /**
     * 用于保存数据,且一个组件最多拥有一个
     */
    private CTInfoControl<T> infoControl;

    private final Timer refreshTimer = new Timer(2 * 1000, e -> {
        try {
            easyRefresh();
        } catch (Exception ex) {
            Log.err.print(getClass(), "刷新失败", ex);
        }
    });
    private List<CTSetsPanel> ctSetsPanelList = new ArrayList<>();
    private boolean isScreenProductViewPanel = false;
    private boolean independentRefresh = false;
    /**
     * 忽略组件状态(是否显示)
     */
    private boolean ignoreState = false;

    public CTViewPanel() {
        super();
        infoControl = setInfoControl();
        this.setVisible(false);
    }

    public List<CTSetsPanel> getCtSetsPanelList() {
        return ctSetsPanelList;
    }

    public void setCtSetsPanelList(List<CTSetsPanel> ctSetsPanelList) {
        this.ctSetsPanelList = ctSetsPanelList;
    }

    public void toScreenProductViewPanel() {
        isScreenProductViewPanel = true;
    }

    public boolean isScreenProductViewPanel() {
        return isScreenProductViewPanel;
    }

    public boolean isIndependentRefresh() {
        return independentRefresh;
    }

    /**
     * 设置是否使用独立的刷新方式
     *
     * @param independentRefresh 是否使用独立刷新方式
     * @param delay              刷新间隔(ms)
     */
    public void setIndependentRefresh(boolean independentRefresh, int delay) {
        this.independentRefresh = independentRefresh;

        if (independentRefresh) {
            refreshTimer.setDelay(delay);
            refreshTimer.start();
        }
    }

    public boolean isIgnoreState() {
        return ignoreState;
    }

    public void setIgnoreState(boolean ignoreState) {
        this.ignoreState = ignoreState;
    }

    public void setRefreshTimerDelay(int delay) {
        refreshTimer.setDelay(delay);
    }

    /**
     * 用于集中管理刷新
     */
    @Override
    public final void refresh() throws Exception {
        synchronized (this){
            if (isVisible() || isIgnoreState()) {

                Log.info.print(getID(), "开始刷新");
                strongRefresh();
                if (independentRefresh)
                    refreshTimer.restart();
            } else {
                Log.info.print(getID(), "刷新被禁止");
                refreshTimer.stop();
            }
        }
    }

    public void strongRefresh() throws Exception {
        if (getInfoControl() != null)
            getInfoControl().refresh();
        easyRefresh();
    }

    /**
     * 设置数据控制类
     *
     * @return infoControl 组件内部存储的数据控制类
     */
    public abstract CTInfoControl<T> setInfoControl();

    public final CTInfoControl<T> getInfoControl(){
        return infoControl;
    }

    /**
     * 刷新时做什么
     */
    protected abstract void easyRefresh() throws Exception;
}
