package com.markelovstyle.images

import com.markelovstyle.images.letters.letterHeight
import com.markelovstyle.images.letters.letterWidth
import com.markelovstyle.util.times
import java.awt.image.BufferedImage
import java.math.BigInteger as Hash

var side = 64
    set(value) {
        if (value in 9..128)
            field = value
    }

fun getHash(source: BufferedImage,color: Int = -1): Hash {  // default color is white; black is -16777216
    val image = addBorders(source)
    val resized = resize(image, side, side)
    var hash = 0.toBigInteger()
    for (y in 0 until side)
        for (x in 0 until side) {
            hash = hash shl 1
            if (resized.getRGB(x, y) == color)
                hash++
        }
    return hash
}

fun imageFromHash(hash: Hash): BufferedImage {
    val str = hash.toString(2)
    val bytes = "0" * (side * side - str.length) + str
    val image = BufferedImage(side, side, BufferedImage.TYPE_BYTE_BINARY)
    for (y in 0 until side)
        for (x in 0 until side)
            image.setRGB(x, y, if (bytes[y * side + x] == '1') -1 else -16777216)
    return image
}

fun countBinaryOnes(number: Hash): Int = number.toString(2).count { it == '1' }
