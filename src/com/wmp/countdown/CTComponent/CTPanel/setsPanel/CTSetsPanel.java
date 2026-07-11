package com.wmp.countdown.CTComponent.CTPanel.setsPanel;

import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.countdown.CTComponent.CTPanel.CTPanel;

public abstract class CTSetsPanel<T> extends CTPanel {

    private String name = "CTSetsPanel";
    //基础数据路径
    private CTInfoControl<T> infoControl;

    public CTSetsPanel(CTInfoControl<T> infoControl) {
        this.infoControl = infoControl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public CTInfoControl<T> getInfoControl() {
        return infoControl;
    }

    public void setInfoControl(CTInfoControl<T> infoControl) {
        this.infoControl = infoControl;
    }

    public abstract void save() throws Exception;


}
