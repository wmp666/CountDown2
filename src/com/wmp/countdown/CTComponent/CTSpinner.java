package com.wmp.countdown.CTComponent;

import com.wmp.countdown.CTComponent.CTButton.CTRoundTextButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CTSpinner extends JPanel {

    private CTTextField textField = new CTTextField();
    private SpinnerModel model;

    public CTSpinner() {
        this(new SpinnerNumberModel());
    }

    public CTSpinner(SpinnerModel model) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.model = model;

        CTRoundTextButton last = new CTRoundTextButton("<");
        CTRoundTextButton next = new CTRoundTextButton(">");

        textField.setText(model.getValue().toString());

        last.setFocusable(false);
        next.setFocusable(false);

        last.addActionListener(e -> setPrevious(model));
        next.addActionListener(e -> setNext(model));

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT
                        || keyCode == KeyEvent.VK_KP_DOWN || keyCode == KeyEvent.VK_KP_RIGHT) {
                    //下移数字
                    setNext(model);
                } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_LEFT
                        || keyCode == KeyEvent.VK_KP_UP || keyCode == KeyEvent.VK_KP_LEFT) {
                    //上移数字

                    setPrevious(model);
                }
            }
        });
        this.add(last, BorderLayout.WEST);
        this.add(textField, BorderLayout.CENTER);
        this.add(next, BorderLayout.EAST);
    }

    private void setNext(SpinnerModel model) {
        Object nextValue = model.getNextValue();
        if (nextValue != null) {
            model.setValue(nextValue);
            textField.setText(nextValue.toString());
        }
    }

    private void setPrevious(SpinnerModel model) {
        Object previousValue = model.getPreviousValue();
        if (previousValue != null) {
            model.setValue(previousValue);
            textField.setText(previousValue.toString());
        }
    }

    public void setValue(Object value) {
        textField.setText(value.toString());
        model.setValue(value);
    }

    public int getValue() {
        return Integer.parseInt(textField.getText());
    }
}
