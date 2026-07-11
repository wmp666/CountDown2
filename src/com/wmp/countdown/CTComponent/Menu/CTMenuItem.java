package com.wmp.countdown.CTComponent.Menu;


import javax.swing.*;

public class CTMenuItem extends JMenuItem {
    public CTMenuItem() {
        this("");
    }

    public CTMenuItem(String text) {
        this(text, null);
    }

    public CTMenuItem(String text, Icon icon) {
        super(text, icon);

    }
}