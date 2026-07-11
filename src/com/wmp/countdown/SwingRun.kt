package com.wmp.countdown

import com.wmp.Main.isHasTheArg
import com.wmp.countdown.frame.MainWindow
import com.wmp.publicTools.CTInfo
import com.wmp.publicTools.UITools.CTFont
import com.wmp.publicTools.UITools.CTFontSizeStyle
import com.wmp.publicTools.printLog.Log
import com.wmp.publicTools.update.GetNewerVersion
import java.awt.Font
import java.awt.Insets
import java.io.IOException
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

object SwingRun {
    @Throws(IOException::class)
    fun show() {

        //更新UI
        try {
            //设置默认字体
            val fontRes = FontUIResource(CTFont.getDefaultFont(Font.PLAIN, CTFontSizeStyle.SMALL))
            val keys = UIManager.getDefaults().keys()
            while (keys.hasMoreElements()) {
                val key = keys.nextElement()
                val value = UIManager.get(key)
                if (value is FontUIResource) UIManager.put(key, fontRes)
            }
            UIManager.put("PopupMenu.borderInsets", Insets(5, 10, 5, 10))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        //播放彩蛋启动代码
        CTInfo.easterEggModeMap.getRunnable("彩蛋启动运行", {}).run()


        MainWindow(CTInfo.DATA_PATH)


        if (CTInfo.StartUpdate &&
            !(isHasTheArg("StartUpdate:false"))
        ) {
            Log.info.print("Main", "开始启动自动检查更新")
            GetNewerVersion.checkForUpdate(
                null, null, true, false
            )
        }
    }
}
