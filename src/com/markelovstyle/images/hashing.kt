package com.markelovstyle.images

import com.markelovstyle.images.letters.letterHeight
import com.markelovstyle.images.letters.letterWidth
import java.awt.image.BufferedImage
import java.math.BigInteger

var side = 64
    set(value) {
        if (value in 9..128)
            field = value
    }

fun getHash(source: BufferedImage, color: Int = -1): BigInteger {  // default color is white; black is -16777216
    val image = addBorders(source, letterWidth, letterHeight)
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

fun countBinaryOnes(number: BigInteger): Int = number.toString(2).count { it == '1' }
