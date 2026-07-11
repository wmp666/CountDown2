package com.wmp.publicTools.CTTool.callRoll

import com.wmp.countdown.CTComponent.CTButton.CTTextButton
import com.wmp.countdown.CTComponent.CTOptionPane
import com.wmp.countdown.CTComponent.Menu.CTMenuItem
import com.wmp.countdown.CTComponent.Menu.CTPopupMenu
import com.wmp.countdown.infSet.InfSetDialog
import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.CTTool.CTTool
import com.wmp.publicTools.UITools.CTColor
import com.wmp.publicTools.UITools.CTFont
import com.wmp.publicTools.UITools.CTFontSizeStyle
import com.wmp.publicTools.printLog.Log
import java.awt.BorderLayout
import java.awt.Font
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.io.IOException
import java.util.*
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.math.abs

class CallRollTool : CTTool("点名器") {
    var nameLabel: JLabel = JLabel("名字")

    init {
        this.setCtSetsPanelList(CallRollSetsPanel())
    }

    override val dialog: JDialog
        get() {
            val dialog = JDialog()
            dialog.setLayout(BorderLayout())
            dialog.setTitle("点名器")
            dialog.setSize((300 * CTInfo.dpi).toInt(), (400 * CTInfo.dpi).toInt())
            dialog.contentPane.setBackground(CTColor.backColor)

            val label = JLabel("点名器")
            label.setForeground(CTColor.textColor)
            label.setHorizontalAlignment(JLabel.CENTER)
            label.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG))
            dialog.add(label, BorderLayout.NORTH)

            nameLabel.setForeground(CTColor.mainColor)
            nameLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.MORE_BIG))
            nameLabel.setHorizontalAlignment(JLabel.CENTER)

            val buttonPanel = JPanel()
            buttonPanel.setOpaque(false)
            buttonPanel.setLayout(GridLayout(1, 0))
            val setsButton = CTTextButton("设置")
            setsButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG))
            setsButton.addActionListener({_ ->
                try {
                    InfSetDialog("快捷工具设置")
                } catch (ex: Exception) {
                    Log.err.print(javaClass, "设置打开失败", ex)
                    throw RuntimeException(ex)
                }
            })
            buttonPanel.add(setsButton)
            val dianMingButton = CTTextButton("点名")
            dianMingButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG))
            dianMingButton.addActionListener({_ ->
                val popupMenu = CTPopupMenu()
                val CDMenuItem1 = CTMenuItem("点名1次")
                val CDMenuItem2 = CTMenuItem("点名2次")
                val CDMenuItem5 = CTMenuItem("点名5次")
                val CDMenuItem10 = CTMenuItem("点名10次")

                CDMenuItem1.addActionListener({ `_`: ActionEvent? -> callRoll(1) })
                CDMenuItem2.addActionListener({ `_`: ActionEvent? -> callRoll(2) })
                CDMenuItem5.addActionListener({ `_`: ActionEvent? -> callRoll(5) })
                CDMenuItem10.addActionListener({ `_`: ActionEvent? -> callRoll(10) })


                popupMenu.add(CDMenuItem1)
                popupMenu.add(CDMenuItem2)
                popupMenu.add(CDMenuItem5)
                popupMenu.add(CDMenuItem10)
                popupMenu.show(dianMingButton, 0, dianMingButton.getHeight())
            })
            buttonPanel.add(dianMingButton)

            dialog.add(nameLabel, BorderLayout.CENTER)
            dialog.add(buttonPanel, BorderLayout.SOUTH)

            dialog.pack()

            return dialog
        }

    private fun callRoll(nameCount: Int) {
        val nameList: Array<String>
        try {
            nameList = CallRollInfoControl.dianMingInfo
        } catch (ex: IOException) {
            Log.err.print(javaClass, "获取点名信息出错", ex)
            return
        }
        if (nameList.isEmpty()) {
            Log.err.print(javaClass, "获取点名信息出错")
            return
        }
        val resultName = arrayOfNulls<String>(nameCount)
        for (i in 0..<nameCount) {
            resultName[i] = nameList[Random().nextInt(nameList.size)]
        }


        val finalNameList: Array<String> = nameList
        Thread({
            //匀速循环
            run {
                val waitTime = 50
                var count = 0
                for (i in 0..49) {
                    val finalCount = count
                    SwingUtilities.invokeLater( {
                        nameLabel.setText(finalNameList[finalCount])
                        nameLabel.repaint()
                    })
                    try {
                        Thread.sleep(waitTime.toLong())
                    } catch (ex: InterruptedException) {
                        Log.err.print(javaClass, "线程中断", ex)
                    }
                    if (count >= finalNameList.size - 1) count = 0
                    else count++
                }
            }

            //匀速循环-循环到目标的前七个
            run {
                var countIndex = nameCount - 7
                if (countIndex < 0) countIndex = finalNameList.size - abs(countIndex)
                val waitTime = 50
                var count = 0
                for (i in countIndex..49) {
                    val finalCount = count
                    SwingUtilities.invokeLater({
                        nameLabel.setText(finalNameList[finalCount])
                        nameLabel.repaint()
                    })
                    try {
                        Thread.sleep(waitTime.toLong())
                    } catch (ex: InterruptedException) {
                        Log.err.print(javaClass, "线程中断", ex)
                    }
                    if (count >= finalNameList.size - 1) count = 0
                    else count++
                    if (count == countIndex) break
                }
            }

            //减速循环
            run {
                val step = 20
                var waitTime = 50
                var count = 0
                var index = 0
                while (true) {
                    val finalCount = index
                    SwingUtilities.invokeLater({
                        nameLabel.setText(finalNameList[finalCount])
                        nameLabel.repaint()
                    })

                    if (index >= finalNameList.size - 1) {
                        index = 0
                    } else {
                        index++
                    }
                    count++

                    waitTime += step * count
                    if (waitTime > 600) {
                        break
                    }

                    try {
                        Thread.sleep(waitTime.toLong())
                    } catch (ex: InterruptedException) {
                        Log.err.print(javaClass, "线程中断", ex)
                    }
                }
                System.err.println(count)
            }

            SwingUtilities.invokeLater({
                nameLabel.setText(resultName[0])
                nameLabel.repaint()
            })
            CTOptionPane.showFullScreenMessageDialog("点名结果", resultName.contentToString(), 0, 1)
        }).start()
    }
}
