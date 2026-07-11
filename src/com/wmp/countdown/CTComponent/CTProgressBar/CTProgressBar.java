package com.wmp.countdown.CTComponent.CTProgressBar;

import com.wmp.countdown.CTComponent.CTGradientRoundProgressBarUI;

import javax.swing.*;
import java.awt.*;

public class CTProgressBar extends JProgressBar {

    public CTProgressBar() {
        this(0, 100);
    }

    public CTProgressBar(int min, int max) {
        super(min, max);
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setMinimumSize(new Dimension(100, 20));
        this.setUI(new CTGradientRoundProgressBarUI());


    }


    public CircleLoader toCircleLoader(){
        CircleLoader circleLoader = new CircleLoader();
        circleLoader.setIndeterminate(isIndeterminate());
        double value = getValue() - getMinimum();
        double range = getMaximum() - getMinimum();
        int result = (int) (value / range * 100);
        circleLoader.setValue(result);
        circleLoader.startAnimation();
        return circleLoader;
    }
}
