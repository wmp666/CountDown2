package com.wmp.publicTools.io

import com.wmp.publicTools.printLog.Log
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 文件校验和工具类，用于验证下载文件的完整性
 */
object FileChecksum {
    /**
     * 计算文件的MD5校验和
     * 
     * @param file 要计算校验和的文件
     * @return 文件的MD5校验和字符串
     * @throws IOException 当文件读取出现问题时抛出
     */
    @Throws(IOException::class)
    fun calculateMD5(file: File): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            FileInputStream(file).use { fis ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while ((fis.read(buffer).also { bytesRead = it }) != -1) {
                    md.update(buffer, 0, bytesRead)
                }
            }
            val digest = md.digest()
            return BigInteger(1, digest).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            Log.err.print(FileChecksum::class.java, "计算MD5校验和失败", e)
            throw RuntimeException("无法获取MD5算法", e)
        }
    }

    /**
     * 计算文件的SHA-256校验和
     * 
     * @param file 要计算校验和的文件
     * @return 文件的SHA-256校验和字符串
     * @throws IOException 当文件读取出现问题时抛出
     */
    @JvmStatic
    @Throws(IOException::class)
    fun calculateSHA256(file: File): String {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            FileInputStream(file).use { fis ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while ((fis.read(buffer).also { bytesRead = it }) != -1) {
                    md.update(buffer, 0, bytesRead)
                }
            }
            val digest = md.digest()
            return BigInteger(1, digest).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            Log.err.print(FileChecksum::class.java, "计算SHA-256校验和失败", e)
            throw RuntimeException("无法获取SHA-256算法", e)
        }
    }

    /**
     * 验证文件的MD5校验和
     * 
     * @param file        待验证的文件
     * @param expectedMD5 期望的MD5校验和
     * @return 如果文件的MD5与期望值一致返回true，否则返回false
     * @throws IOException 当文件读取出现问题时抛出
     */
    @Throws(IOException::class)
    fun verifyMD5(file: File, expectedMD5: String?): Boolean {
        val actualMD5 = calculateMD5(file)
        return actualMD5.equals(expectedMD5, ignoreCase = true)
    }

    /**
     * 验证文件的SHA-256校验和
     * 
     * @param file           待验证的文件
     * @param expectedSHA256 期望的SHA-256校验和
     * @return 如果文件的SHA-256与期望值一致返回true，否则返回false
     * @throws IOException 当文件读取出现问题时抛出
     */
    @JvmStatic
    @Throws(IOException::class)
    fun verifySHA256(file: File, expectedSHA256: String?): Boolean {
        val actualSHA256 = calculateSHA256(file)
        return actualSHA256.equals(expectedSHA256, ignoreCase = true)
    }
}