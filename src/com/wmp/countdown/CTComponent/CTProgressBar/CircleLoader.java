package com.wmp.countdown.CTComponent.CTProgressBar;

import com.wmp.publicTools.UITools.CTColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * 圆形加载动画,默认处于启动状态,可以通过<code>setLoading()</code>修改;
 * 不确定状态,可以通过<code>setIndeterminate()</code>修改
 */
public class CircleLoader extends JPanel {
    private boolean isIndeterminate = true;

    private int value = 0;

    private float rotationAngle = 0;
    private float sweepAngle = 45; // 扇形角度
    private final Timer timer;

    /**
     * <code>true</code>-加 <code>false</code>-减
     */
    private boolean sweepDirection;

    public CircleLoader() {
        setPreferredSize(new Dimension(80, 80));
        setMinimumSize(new Dimension(80, 80));
        setOpaque(false); // 透明背景

        // 定时器控制动画
        timer = new Timer(20, _ -> {

                rotationAngle -= 5;
                if (rotationAngle <= 0) {
                    rotationAngle = 360;
                }

                if (isIndeterminate) {
                    if (sweepDirection) sweepAngle += 3;
                    else sweepAngle -= 3;

                    if (sweepAngle >= 180) sweepDirection = false;
                    else if (sweepAngle <= 0) sweepDirection = true;
                }

            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // 启用抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        int depth = size/5;
        size -= depth;
        int x = (width - size) / 2;
        int y = (height - size) / 2;

        // 创建渐变色
        g2d.setColor(CTColor.mainColor);

        // 绘制旋转的扇形
        g2d.setStroke(new BasicStroke(depth, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));

        Arc2D arc = new Arc2D.Float(x, y, size, size,
                rotationAngle, sweepAngle,
                Arc2D.OPEN);
        g2d.draw(arc);

        g2d.dispose();
    }

    public void startAnimation() {
        timer.start();
    }

    public void stopAnimation() {
        timer.stop();
    }

    public void setIndeterminate(boolean indeterminate){
        isIndeterminate = indeterminate;

    }

    public boolean isIndeterminate() {
        return isIndeterminate;
    }

    public void setValue(int value) {
        if (isIndeterminate) return;
        //角度0~360
        //百分比0~100
        value = Math.max(0, Math.min(100, value));
        sweepAngle = (float) (value * 3.6);
        repaint();
    }

    public int getValue() {
        return value;
    }

    public void setLoading(boolean loading) {
        if (loading) {
            timer.start();
        } else {
            timer.stop();
        }
    }
}