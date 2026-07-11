package com.wmp.publicTools.appFileControl

import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.ImageIcon
import kotlin.math.*

object ColorImageGenerator {
    // 预设的亮度范围
    private const val MIN_BRIGHTNESS = 0.15f // 最小亮度
    private const val MAX_BRIGHTNESS = 0.95f // 最大亮度
    private const val TARGET_MID_BRIGHTNESS = 0.5f // 目标中间亮度

    /**
     * 基于灰度图生成指定颜色的彩色图片库，并将亮度框定在范围内
     */
    fun getColorfulImageMap(
        grayScaleMap: MutableMap<String?, ImageIcon?>?,
        color: Color
    ): MutableMap<String?, ImageIcon?> {
        val colorfulMap: MutableMap<String?, ImageIcon?> = HashMap<String?, ImageIcon?>()

        if (grayScaleMap.isNullOrEmpty()) {
            return colorfulMap
        }

        for (entry in grayScaleMap.entries) {
            val imageName = entry.key
            val grayIcon = entry.value

            // 将ImageIcon转换为BufferedImage
            val grayImage = iconToImage(grayIcon)

            // 应用亮度范围框定的颜色滤镜
            val coloredImage = applyColorWithBrightnessRange(grayImage, color)

            // 将BufferedImage转换回ImageIcon
            val coloredIcon = ImageIcon(coloredImage)

            colorfulMap[imageName] = coloredIcon
        }

        return colorfulMap
    }

    /**
     * 应用颜色滤镜，并将亮度框定在预设范围内
     */
    private fun applyColorWithBrightnessRange(grayImage: BufferedImage?, targetColor: Color): BufferedImage? {
        if (grayImage == null) return null

        val width = grayImage.width
        val height = grayImage.height
        val coloredImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        // 分析灰度图的亮度特性
        val analysis = analyzeBrightness(grayImage)

        // 获取目标颜色的HSL值
        val targetHSL = rgbToHsl(targetColor.red, targetColor.green, targetColor.blue)
        val targetHue = targetHSL[0]
        val targetSaturation = targetHSL[1]
        val targetLightness = targetHSL[2]

        // 计算自适应参数
        val params = calculateAdaptationParams(analysis, targetLightness)

        for (y in 0..<height) {
            for (x in 0..<width) {
                val pixel = grayImage.getRGB(x, y)
                val alpha = (pixel shr 24) and 0xff

                if (alpha == 0) {
                    // 完全透明的像素，保持透明
                    coloredImage.setRGB(x, y, pixel)
                    continue
                }

                // 获取原始灰度值
                val grayValue = (pixel shr 16) and 0xff
                val originalBrightness = grayValue / 255.0f

                // 步骤1: 将原始亮度映射到标准范围
                val normalizedBrightness = normalizeBrightness(originalBrightness, analysis)

                // 步骤2: 根据目标颜色亮度调整映射范围
                val adjustedBrightness = adjustForTargetColor(normalizedBrightness, targetLightness, params)

                // 步骤3: 确保亮度在框定范围内
                val finalBrightness = clampBrightness(adjustedBrightness)

                // 步骤4: 应用颜色
                val finalColor = applyColor(targetHue, targetSaturation, finalBrightness, params)

                // 合并Alpha通道
                val newPixel = (alpha shl 24) or (finalColor.red shl 16) or
                        (finalColor.green shl 8) or finalColor.blue
                coloredImage.setRGB(x, y, newPixel)
            }
        }

        return coloredImage
    }

    /**
     * 分析灰度图的亮度特性
     */
    private fun analyzeBrightness(grayImage: BufferedImage): BrightnessAnalysis {
        val analysis = BrightnessAnalysis()

        val width = grayImage.width
        val height = grayImage.height
        var totalBrightness = 0f
        var pixelCount = 0

        // 收集所有非透明像素的亮度
        val brightnessValues = IntArray(width * height)
        var index = 0

        for (y in 0..<height) {
            for (x in 0..<width) {
                val pixel = grayImage.getRGB(x, y)
                val alpha = (pixel shr 24) and 0xff

                if (alpha > 0) {
                    val grayValue = (pixel shr 16) and 0xff
                    val brightness = grayValue / 255.0f

                    // 更新统计信息
                    analysis.minBrightness = min(analysis.minBrightness, brightness)
                    analysis.maxBrightness = max(analysis.maxBrightness, brightness)
                    totalBrightness += brightness
                    brightnessValues[index++] = grayValue
                    pixelCount++
                }
            }
        }

        if (pixelCount > 0) {
            // 计算平均亮度
            analysis.avgBrightness = totalBrightness / pixelCount
            analysis.brightnessRange = analysis.maxBrightness - analysis.minBrightness

            // 计算中位数亮度
            if (index > 0) {
                val sortedValues = IntArray(index)
                System.arraycopy(brightnessValues, 0, sortedValues, 0, index)
                Arrays.sort(sortedValues, 0, index)
                analysis.medianBrightness = sortedValues[index / 2] / 255.0f
            }

            // 判断图像亮度类型
            analysis.isDark = analysis.avgBrightness < 0.3f
            analysis.isLight = analysis.avgBrightness > 0.7f

            // 计算对比度比例
            if (analysis.minBrightness > 0) {
                analysis.contrastRatio = analysis.maxBrightness / analysis.minBrightness
            } else {
                analysis.contrastRatio = analysis.maxBrightness / 0.01f
            }
        }

        return analysis
    }

    /**
     * 计算自适应参数
     */
    private fun calculateAdaptationParams(analysis: BrightnessAnalysis, targetLightness: Float): AdaptationParams {
        val params = AdaptationParams()

        // 1. 根据目标颜色亮度调整参数
        if (targetLightness < 0.3f) {
            // 目标颜色较暗，需要提亮输出
            params.brightnessScale = 1.5f
            params.brightnessOffset = 0.2f
            params.saturationScale = 0.8f
        } else if (targetLightness > 0.7f) {
            // 目标颜色较亮，需要压暗输出
            params.brightnessScale = 0.7f
            params.brightnessOffset = -0.1f
            params.saturationScale = 1.2f
        }

        // 2. 根据图像亮度特性调整对比度
        if (analysis.contrastRatio < 3.0f) {
            // 低对比度图像，增强对比度
            params.contrastScale = 1.5f
        } else if (analysis.contrastRatio > 10.0f) {
            // 高对比度图像，降低对比度
            params.contrastScale = 0.7f
        }

        // 3. 根据图像整体亮度调整中间点
        if (analysis.isDark) {
            params.midPointAdjustment = 0.2f // 暗图，提亮中间点
        } else if (analysis.isLight) {
            params.midPointAdjustment = -0.2f // 亮图，压暗中间点
        }

        return params
    }

    /**
     * 将原始亮度映射到标准范围
     */
    private fun normalizeBrightness(originalBrightness: Float, analysis: BrightnessAnalysis): Float {
        if (analysis.brightnessRange < 0.001f) {
            // 所有像素亮度相同
            return TARGET_MID_BRIGHTNESS
        }

        // 线性映射到0-1范围
        val normalized = (originalBrightness - analysis.minBrightness) / analysis.brightnessRange

        // 使用Gamma校正增强中间色调
        var gamma = if (normalized < 0.5f) {
            0.8f // 提亮暗部
        } else {
            1.2f // 压暗亮部
        }

        return normalized.toDouble().pow(gamma.toDouble()).toFloat()
    }

    /**
     * 根据目标颜色亮度调整亮度值
     */
    private fun adjustForTargetColor(
        normalizedBrightness: Float, targetLightness: Float,
        params: AdaptationParams
    ): Float {
        // 应用对比度调整

        val contrasted = adjustContrast(
            normalizedBrightness, TARGET_MID_BRIGHTNESS + params.midPointAdjustment,
            params.contrastScale
        )

        // 根据目标颜色亮度调整
        var adjusted = contrasted

        if (targetLightness < 0.3f) {
            // 目标颜色暗，提高亮度
            adjusted = min(1.0f, contrasted * params.brightnessScale + params.brightnessOffset)
        } else if (targetLightness > 0.7f) {
            // 目标颜色亮，降低亮度
            adjusted = max(0.0f, contrasted * params.brightnessScale + params.brightnessOffset)
        }

        return adjusted
    }

    /**
     * 调整对比度
     */
    private fun adjustContrast(value: Float, midpoint: Float, contrast: Float): Float {
        val adjusted = ((value - midpoint) * contrast) + midpoint
        return min(1.0f, max(0.0f, adjusted))
    }

    /**
     * 确保亮度在框定范围内
     */
    private fun clampBrightness(brightness: Float): Float {
        // 使用Sigmoid函数将亮度限制在范围内
        val scaled = (brightness - 0.5f) * 2.0f // 缩放到-1到1
        val sigmoid = 1.0f / (1.0f + exp((-scaled * 3.0f).toDouble()).toFloat()) // 使用Sigmoid函数

        // 将Sigmoid输出映射到目标范围
        return MIN_BRIGHTNESS + sigmoid * (MAX_BRIGHTNESS - MIN_BRIGHTNESS)
    }

    /**
     * 应用颜色
     */
    private fun applyColor(hue: Float, saturation: Float, brightness: Float, params: AdaptationParams): Color {
        // 调整饱和度
        var adjustedSaturation = saturation * params.saturationScale
        adjustedSaturation = min(1.0f, max(0.0f, adjustedSaturation))

        // 将HSL转换为RGB
        val rgb = hslToRgb(hue, adjustedSaturation, brightness)
        return Color(rgb[0], rgb[1], rgb[2])
    }

    /**
     * 方法2: 使用直方图匹配技术确保亮度分布
     */
    fun applyHistogramMatching(grayImage: BufferedImage?, targetColor: Color): BufferedImage? {
        if (grayImage == null) return null

        val width = grayImage.width
        val height = grayImage.height
        val coloredImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        // 1. 构建灰度图的亮度直方图
        val histogram = IntArray(256)
        var totalPixels = 0

        for (y in 0..<height) {
            for (x in 0..<width) {
                val pixel = grayImage.getRGB(x, y)
                val alpha = (pixel shr 24) and 0xff

                if (alpha > 0) {
                    val grayValue = (pixel shr 16) and 0xff
                    histogram[grayValue]++
                    totalPixels++
                }
            }
        }

        // 2. 计算累积分布函数(CDF)
        val cdf = FloatArray(256)
        var cumulative = 0
        for (i in 0..255) {
            cumulative += histogram[i]
            cdf[i] = cumulative.toFloat() / totalPixels
        }

        // 3. 目标亮度分布的CDF（均匀分布）
        val targetCdf = FloatArray(256)
        for (i in 0..255) {
            targetCdf[i] = i.toFloat() / 255.0f
        }

        // 4. 直方图匹配：找到映射函数
        val mapping = IntArray(256)
        for (i in 0..255) {
            val sourceValue = cdf[i]
            var targetIndex = 0

            // 找到最接近的目标CDF值
            var minDiff = abs(sourceValue - targetCdf[0])
            for (j in 1..255) {
                val diff = abs(sourceValue - targetCdf[j])
                if (diff < minDiff) {
                    minDiff = diff
                    targetIndex = j
                }
            }
            mapping[i] = targetIndex
        }

        // 5. 应用颜色和映射
        val targetHSL = rgbToHsl(targetColor.red, targetColor.green, targetColor.blue)

        for (y in 0..<height) {
            for (x in 0..<width) {
                val pixel = grayImage.getRGB(x, y)
                val alpha = (pixel shr 24) and 0xff
                val grayValue = (pixel shr 16) and 0xff

                // 应用直方图匹配
                val matchedGray = mapping[grayValue]
                var brightness = matchedGray / 255.0f

                // 确保亮度在范围内
                brightness = MIN_BRIGHTNESS + brightness * (MAX_BRIGHTNESS - MIN_BRIGHTNESS)

                // 应用颜色
                val rgb = hslToRgb(targetHSL[0], targetHSL[1], brightness)

                val newPixel = (alpha shl 24) or (rgb[0] shl 16) or (rgb[1] shl 8) or rgb[2]
                coloredImage.setRGB(x, y, newPixel)
            }
        }

        return coloredImage
    }

    /**
     * 方法3: 简单但有效的亮度范围框定
     */
    fun applySimpleBrightnessRange(grayImage: BufferedImage?, targetColor: Color): BufferedImage? {
        if (grayImage == null) return null

        val width = grayImage.width
        val height = grayImage.height
        val coloredImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        // 获取目标颜色的HSL值
        val targetHSL = rgbToHsl(targetColor.red, targetColor.green, targetColor.blue)

        for (y in 0..<height) {
            for (x in 0..<width) {
                val pixel = grayImage.getRGB(x, y)
                val alpha = (pixel shr 24) and 0xff
                val grayValue = (pixel shr 16) and 0xff

                // 将灰度值转换为亮度值
                val brightness = grayValue / 255.0f

                // 简单但有效的亮度范围框定
                val finalBrightness: Float = if (brightness < 0.2f) {
                    // 暗部区域：提升到最小亮度以上
                    MIN_BRIGHTNESS + (brightness / 0.2f) * 0.1f
                } else if (brightness > 0.8f) {
                    // 亮部区域：降低到最大亮度以下
                    MAX_BRIGHTNESS - ((1.0f - brightness) / 0.2f) * 0.1f
                } else {
                    // 中间区域：线性映射到目标范围
                    MIN_BRIGHTNESS +
                            (brightness - 0.2f) / 0.6f * (MAX_BRIGHTNESS - MIN_BRIGHTNESS)
                }

                // 应用颜色
                val rgb = hslToRgb(targetHSL[0], targetHSL[1], finalBrightness)

                val newPixel = (alpha shl 24) or (rgb[0] shl 16) or (rgb[1] shl 8) or rgb[2]
                coloredImage.setRGB(x, y, newPixel)
            }
        }

        return coloredImage
    }

    // -------------------- 辅助方法 --------------------
    private fun iconToImage(icon: ImageIcon?): BufferedImage? {
        if (icon == null) return null

        val image = icon.getImage()
        val bufferedImage = BufferedImage(
            icon.iconWidth,
            icon.iconHeight,
            BufferedImage.TYPE_INT_ARGB
        )

        val g2d = bufferedImage.createGraphics()
        g2d.drawImage(image, 0, 0, null)
        g2d.dispose()

        return bufferedImage
    }

    private fun rgbToHsl(r: Int, g: Int, b: Int): FloatArray {
        val rNorm = r / 255.0f
        val gNorm = g / 255.0f
        val bNorm = b / 255.0f

        val max = max(max(rNorm, gNorm), bNorm)
        val min = min(min(rNorm, gNorm), bNorm)
        var h: Float
        val s: Float
        val l = (max + min) / 2.0f

        if (max == min) {
            s = 0.0f
            h = s
        } else {
            val d = max - min
            s = if (l > 0.5f) d / (2.0f - max - min) else d / (max + min)

            h = when (max) {
                rNorm -> {
                    (gNorm - bNorm) / d + (if (gNorm < bNorm) 6.0f else 0.0f)
                }

                gNorm -> {
                    (bNorm - rNorm) / d + 2.0f
                }

                else -> {
                    (rNorm - gNorm) / d + 4.0f
                }
            }
            h /= 6.0f
        }

        return floatArrayOf(h, s, l)
    }

    private fun hslToRgb(h: Float, s: Float, l: Float): IntArray {
        val r: Float
        val g: Float
        val b: Float

        if (s == 0.0f) {
            b = l
            g = b
            r = g
        } else {
            val q = if (l < 0.5f) l * (1.0f + s) else l + s - l * s
            val p = 2.0f * l - q
            r = hueToRgb(p, q, h + 1.0f / 3.0f)
            g = hueToRgb(p, q, h)
            b = hueToRgb(p, q, h - 1.0f / 3.0f)
        }

        return intArrayOf(
            clamp((r * 255).toInt()),
            clamp((g * 255).toInt()),
            clamp((b * 255).toInt())
        )
    }

    private fun hueToRgb(p: Float, q: Float, t: Float): Float {
        var t = t
        if (t < 0.0f) t += 1.0f
        if (t > 1.0f) t -= 1.0f
        if (t < 1.0f / 6.0f) return p + (q - p) * 6.0f * t
        if (t < 1.0f / 2.0f) return q
        if (t < 2.0f / 3.0f) return p + (q - p) * (2.0f / 3.0f - t) * 6.0f
        return p
    }

    private fun clamp(value: Int): Int {
        return min(255, max(0, value))
    }

    // -------------------- 使用示例 --------------------
    @JvmStatic
    fun main(args: Array<String>) {
        // 创建测试灰度图Map
        val grayMap: MutableMap<String?, ImageIcon?> = HashMap<String?, ImageIcon?>()

        // 创建不同亮度的测试图片
        val brightnessLevels = intArrayOf(30, 100, 180, 230) // 不同亮度值
        for (i in brightnessLevels.indices) {
            val img = createTestImage(64, 64, brightnessLevels[i])
            grayMap["img$i"] = ImageIcon(img)
        }

        // 测试各种颜色
        val testColors = arrayOf<Color>(
            Color.BLACK,  // 极暗
            Color.DARK_GRAY,  // 暗
            Color.GRAY,  // 中
            Color.LIGHT_GRAY,  // 亮
            Color.WHITE // 极亮
        )

        for (color in testColors) {
            println("处理颜色: " + colorToString(color))
            val result = getColorfulImageMap(grayMap, color)
            println("生成图片数量: " + result.size)

            // 测试其他方法
            for (entry in grayMap.entries) {
                val grayImage = iconToImage(entry.value)

                // 测试直方图匹配方法
                val histResult = applyHistogramMatching(grayImage, color)

                // 测试简单方法
                val simpleResult = applySimpleBrightnessRange(grayImage, color)
            }
        }
    }

    private fun createTestImage(width: Int, height: Int, baseBrightness: Int): BufferedImage {
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = img.createGraphics()

        // 创建渐变背景
        for (y in 0..<height) {
            var brightness = baseBrightness + (y * 50.0 / height).toInt()
            brightness = min(255, max(0, brightness))

            for (x in 0..<width) {
                val pixel = (255 shl 24) or (brightness shl 16) or (brightness shl 8) or brightness
                img.setRGB(x, y, pixel)
            }
        }

        // 绘制不同亮度的形状
        g2d.color = Color(baseBrightness - 50, baseBrightness - 50, baseBrightness - 50)
        g2d.fillRect(10, 10, 20, 20)

        g2d.color = Color(baseBrightness, baseBrightness, baseBrightness)
        g2d.fillOval(30, 30, 30, 30)

        g2d.color = Color(baseBrightness + 50, baseBrightness + 50, baseBrightness + 50)
        g2d.fillRoundRect(50, 50, 30, 20, 10, 10)

        g2d.dispose()
        return img
    }

    private fun colorToString(color: Color): String {
        return String.format(
            "RGB(%d,%d,%d)",
            color.red, color.green, color.blue
        )
    }

    /**
     * 亮度分析类
     */
    private class BrightnessAnalysis {
        var minBrightness = 1.0f // 最小亮度
        var maxBrightness = 0.0f // 最大亮度
        var avgBrightness = 0.0f // 平均亮度
        var medianBrightness = 0.0f // 中位数亮度
        var brightnessRange = 0.0f // 亮度范围
        var isDark = false // 是否偏暗
        var isLight = false // 是否偏亮
        var contrastRatio = 0.0f // 对比度比例
    }

    /**
     * 自适应参数类
     */
    private class AdaptationParams {
        var brightnessScale = 1.0f // 亮度缩放因子
        var brightnessOffset = 0.0f // 亮度偏移量
        var contrastScale = 1.0f // 对比度缩放因子
        var saturationScale = 1.0f // 饱和度缩放因子
        var midPointAdjustment = 0.0f // 中间点调整
    }
}