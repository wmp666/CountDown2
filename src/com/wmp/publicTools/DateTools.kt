package com.wmp.publicTools

import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar
import com.wmp.countdown.CTComponent.CTOptionPane
import com.wmp.publicTools.printLog.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object DateTools {
    @JvmField
    val days: Array<String> = arrayOf(
        "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
        "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
        "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    )
    @JvmField
    val months: Array<String> = arrayOf(
        "正月",
        "二月",
        "三月",
        "四月",
        "五月",
        "六月",
        "七月",
        "八月",
        "九月",
        "十月",
        "十一月",
        "十二月"
    )

    /**
     * 判断当前时间是否是目标时间
     * 
     * @param targetDate 目标时间,格式为MM-dd
     * @return true:是目标时间
     */
    @JvmStatic
    fun dayIsNow(targetDate: String): Boolean {
        if (targetDate.startsWith("lunar")) {
            val lunar = Lunar.fromDate(Date())

            val split: Array<String?> =
                targetDate.substring(5).split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val targetMouth = split[0]!!.toInt()
            val targetDay = split[1]!!.toInt()

            return (targetMouth == abs(lunar.month) && targetDay == lunar.day)
        } else {
            val dateFormat: DateFormat = SimpleDateFormat("MM-dd")
            val date = dateFormat.format(Date())
            return targetDate == date
        }
    }


    /**
     * 获取两个时间间隔天数
     * 
     * @param targetDate 目标时间,格式为(lunar)MM-dd,"lunar"为农历的意思
     * @return 间隔天数
     */
    @JvmStatic
    fun getRemainderDay(targetDate: String): Int {
        var day = 0
        //获取今年年份 - 公历
        var solar = Solar.fromDate(Date())
        try {
            if (targetDate.startsWith("lunar")) {
                //获取今天的农历日期
                //1.分割目标时间 - 农历
                val split: Array<String?> =
                    targetDate.substring(5).split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val targetMonth = split[0]!!.toInt()
                val targetDay = split[1]!!.toInt()
                //2.获取今年年份 - 农历
                var lunarYear = Lunar.fromDate(Date()).year
                run {
                    //3.获取农历,并转成公历
                    val lunar = Lunar.fromYmd(lunarYear, targetMonth, targetDay)
                    val targetSolar = lunar.solar
                    //计算间隔时间
                    day = targetSolar.subtract(solar)
                }

                if (day <= 0) {
                    lunarYear += 1
                    run {
                        //3.获取农历,并转成公历
                        val lunar = Lunar.fromYmd(lunarYear, targetMonth, targetDay)
                        val targetSolar = lunar.solar
                        //计算间隔时间
                        day = targetSolar.subtract(solar)
                    }
                }
            } else {
                //获取目标时间 - 公历
                //1.分割目标时间

                val split: Array<String?> =
                    targetDate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val targetMonth = split[0]!!.toInt()
                val targetDay = split[1]!!.toInt()
                //2.获取目标时间
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.MONTH, targetMonth - 1)
                calendar.set(Calendar.DAY_OF_MONTH, targetDay)
                var targetSolar = Solar.fromCalendar(calendar)

                //获取间隔天数
                day = targetSolar.subtract(solar)

                if (day <= 0) {
                    //获取今年年份 - 公历
                    solar = Solar.fromDate(Date())
                    //获取目标时间 - 公历
                    calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
                    targetSolar = Solar.fromCalendar(calendar)
                    //计算间隔时间
                    day = targetSolar.subtract(solar)
                }
            }
        } catch (e: Exception) {
            Log.err.print(DateTools::class.java, "获取目标时间失败", e)
        }

        return day
    }

    /**
     * 判断当前时间是否在指定时间段内
     * 
     * @param beginTime 开始时间, 格式为HH:mm
     * @param endTime   结束时间, 格式为HH:mm
     * @return true:在指定时间段内
     */
    @JvmStatic
    fun isInTimePeriod(beginTime: String?, endTime: String?): Boolean {
        return getRemainderTime(beginTime) < 0 && getRemainderTime(endTime) > 0
    }

    /**
     * 获取两个时间间隔时间
     * 
     * @param targetTime 目标时间,格式为HH:mm
     * @return 间隔时间(ms)
     */
    @JvmStatic
    fun getRemainderTime(targetTime: String?): Long {
        return getRemainderTime(targetTime, "HH:mm")
    }

    /**
     * 获取两个时间间隔时间
     * 
     * @param targetTime 目标时间
     * @param format     时间格式
     * @return 间隔时间(ms)
     */
    @JvmStatic
    fun getRemainderTime(targetTime: String?, format: String): Long {
        var time: Long = 0

        if (targetTime.isNullOrEmpty()) {
            Log.err.print(DateTools::class.java, "获取目标时间失败: \n" + "请输入目标时间")
            return 0
        }

        if (targetTime.startsWith("lunar")) {
            Log.err.print(DateTools::class.java, "获取目标时间失败: \n" + "不支持农历")
        }
        try {
            val dateFormat: DateFormat = SimpleDateFormat(format)
            val calendar = Calendar.getInstance()
            calendar.setTime(dateFormat.parse(targetTime))

            // 设置lenient为false，使解析更严格
            dateFormat.isLenient = false

            // 获取当前时间作为基准
            val now = Calendar.getInstance()

            // 如果format只包含时间部分，使用当前日期补充
            if (!format.contains("y")) {
                calendar.set(Calendar.YEAR, now.get(Calendar.YEAR))
            }
            if (!format.contains("M")) {
                calendar.set(Calendar.MONTH, now.get(Calendar.MONTH))
            }
            if (!format.contains("d")) {
                calendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
            }
            time = calendar.getTime().time - Date().time
        } catch (e: Exception) {
            Log.err.systemPrint(DateTools::class.java, "获取目标时间失败", e)
        }
        return time
    }

    /**
     * 获取时间字符串
     * 
     * @param time 时间数组
     * @param type 时间类型
     * @return 时间字符串
     */
    @JvmStatic
    fun getTimeStr(time: IntArray, type: Int, separator: Char): String {
        return when (type) {
            CTOptionPane.HOURS_MINUTES -> String.format("%02d${separator}%02d", time[0], time[1])
            CTOptionPane.HOURS_MINUTES_SECOND -> String.format(
                "%02d${separator}%02d${separator}%02d",
                time[0],
                time[1],
                time[2]
            )
            else -> ""
        }
    }

    /**
     * 获取日期字符串
     * 
     * @param date 日期数组
     * @param type 日期类型
     * @return 日期字符串
     */
    fun getDateStr(date: IntArray, type: Int, separator: Char): String {
        return when (type) {
            CTOptionPane.YEAR_MONTH_DAY -> String.format(
                "%04d${separator}%02d${separator}%02d",
                date[0],
                date[1],
                date[2]
            )
            CTOptionPane.MONTH_DAY -> String.format("%02d${separator}%02d", date[0], date[1])
            else -> ""
        }
    }
}
