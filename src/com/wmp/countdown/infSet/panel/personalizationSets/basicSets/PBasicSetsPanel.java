package com.wmp.countdown.infSet.panel.personalizationSets.basicSets;

import com.wmp.countdown.CTComponent.CTPanel.setsPanel.CTListSetsPanel;
import com.wmp.countdown.infSet.panel.personalizationSets.control.PBasicInfoControl;
import com.wmp.countdown.infSet.panel.personalizationSets.control.PPanelInfoControl;

public class PBasicSetsPanel extends CTListSetsPanel {

    public PBasicSetsPanel() {
        super(null);

        setName("基础设置");

        this.clearCTList();
        this.add(new PBBasicSetsPanel(new PBasicInfoControl()));
        this.add(new PBPanelSetsPanel(new PPanelInfoControl()));
    }
}
