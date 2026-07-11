package com.wmp.publicTools.io

import java.util.regex.Pattern

object InfProcess {
    //处理duty.txt中的内容-三
    fun RDExtractNames(input: String): ArrayList<Array<String?>?> {
        val result = ArrayList<Array<String?>?>()
        // 正则表达式匹配大括号内的内容 {
        val pattern = Pattern.compile("\\[([^]]+)")
        val matcher = pattern.matcher(input)

        while (matcher.find()) {
            // 获取匹配到的内容（去掉括号后的部分）
            val group = matcher.group(1)
            // 按逗号分割并添加到结果列表
            val names: Array<String?> = group.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            result.add(names)
        }
        return result
    }


    //处理duty.txt中的内容-二
    @JvmStatic
    fun NDExtractNames(input: String): ArrayList<String?> {
        val result = ArrayList<String?>()
        // 正则表达式匹配大括号内的内容 {
        val pattern = Pattern.compile("\\[([^]]+)")
        val matcher = pattern.matcher(input)

        while (matcher.find()) {
            // 获取匹配到的内容（去掉大括号后的部分）
            val group = matcher.group(1)
            result.add(group)
        }
        return result
    }
}
