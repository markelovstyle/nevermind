package com.markelovstyle.images

import java.awt.image.BufferedImage
import java.math.BigInteger

fun getHash(image: BufferedImage, side: Int = 64, color: Int = -1): BigInteger {  // default color is white; black is -16777216
    val resized = resize(image, side, side)
    var hash = 0.toBigInteger()
    for (y in 0 until side)
        for (x in 0 until side) {
            hash *= 2.toBigInteger()
            if (resized.getRGB(x, y) == color)
                hash += 1.toBigInteger()
        }
    return hash
}

fun countBinaryOnes(number: BigInteger): Long = number.toString(2).chars().filter { item: Int -> item == '1'.toInt() }.count()
