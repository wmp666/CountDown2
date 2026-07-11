package com.wmp.countdown.CTComponent

import com.wmp.publicTools.CTInfo
import java.awt.Dimension
import java.awt.event.WindowEvent
import java.awt.event.WindowStateListener
import java.awt.geom.RoundRectangle2D
import javax.swing.JFrame

open class CTWindow : JFrame(), WindowStateListener {
    init {
        this.setUndecorated(true)
        this.setOpacity(0.7f)
        this.setShape(
            RoundRectangle2D.Double(
                0.0,
                0.0,
                this.getWidth().toDouble(),
                this.getHeight().toDouble(),
                (CTInfo.arcw - 10).toDouble(),
                (CTInfo.arch - 10).toDouble()
            )
        )

        this.addWindowStateListener(this)
    }

    override fun setSize(d: Dimension) {
        super.setSize(d)

        this.setShape(
            RoundRectangle2D.Double(
                0.0,
                0.0,
                this.getWidth().toDouble(),
                this.getHeight().toDouble(),
                CTInfo.arcw.toDouble(),
                CTInfo.arch.toDouble()
            )
        )
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        this.setShape(
            RoundRectangle2D.Double(
                0.0,
                0.0,
                this.getWidth().toDouble(),
                this.getHeight().toDouble(),
                CTInfo.arcw.toDouble(),
                CTInfo.arch.toDouble()
            )
        )
    }

    override fun windowStateChanged(e: WindowEvent) {
        if (e.getNewState() == ICONIFIED) {
            this.setState(NORMAL)
        }
    }
}
