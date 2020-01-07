package com.shengshijie.voice

import java.math.BigDecimal

object NumToCnAmountUtils {

    private val CN_UPPER_NUMBER = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")

    private val CN_UPPER_MONEY_UNIT = arrayOf(
        "分",
        "角",
        "元",
        "拾",
        "佰",
        "仟",
        "万",
        "拾",
        "佰",
        "仟",
        "亿",
        "拾",
        "佰",
        "仟",
        "兆",
        "拾",
        "佰",
        "仟"
    )

    private const val CN_FULL = "整"

    private const val CN_NEGATIVE = "负"

    private const val MONEY_PRECISION = 2

    private const val CN_ZERO_FULL = "零元$CN_FULL"

    fun numberToWord(amount: String): String {
        if (amount.isEmpty()) {
            return ""
        }
        val bigDecimal = try {
            BigDecimal(amount)
        } catch (e: Exception) {
            return ""
        }
        val sb = StringBuffer()
        val signum = bigDecimal.signum()
        if (signum == 0) {
            return CN_ZERO_FULL
        }
        var number = bigDecimal.movePointRight(MONEY_PRECISION)
            .setScale(0, 4).abs().toLong()
        val scale = number % 100
        var numUnit: Int
        var numIndex = 0
        var getZero = false
        if (scale <= 0) {
            numIndex = 2
            number /= 100
            getZero = true
        }
        if (scale > 0 && scale % 10 <= 0) {
            numIndex = 1
            number /= 10
            getZero = true
        }
        var zeroSize = 0
        while (number > 0) {
            numUnit = (number % 10).toInt()
            if (numUnit > 0) {
                if (numIndex == 9 && zeroSize >= 3) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[6])
                }
                if (numIndex == 13 && zeroSize >= 3) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[10])
                }
                sb.insert(0, CN_UPPER_MONEY_UNIT[numIndex])
                sb.insert(0, CN_UPPER_NUMBER[numUnit])
                getZero = false
                zeroSize = 0
            } else {
                ++zeroSize
                if (!getZero) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit])
                }
                if (numIndex == 2) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[numIndex])
                } else if ((numIndex - 2) % 4 == 0 && number % 1000 > 0) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[numIndex])
                }
                getZero = true
            }
            number /= 10
            ++numIndex
        }
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE)
        }
        if (scale <= 0) {
            sb.append(CN_FULL)
        }
        return sb.toString()
    }

}