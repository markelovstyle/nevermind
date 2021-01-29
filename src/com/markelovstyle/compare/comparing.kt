package com.markelovstyle.compare

import com.markelovstyle.data.data
import com.markelovstyle.images.types.Letter
import com.markelovstyle.images.countBinaryOnes
import com.markelovstyle.images.side
import java.math.BigInteger
import kotlin.math.abs


var accuracy = 0.0025f
    set(value) {
        if (0 < value && value < 1)
            field = value
    }

fun checkAccuracy(first: Int, second: Int) = abs(first - second).toDouble() / side * side < accuracy


fun closeFind(hash: BigInteger, lineHeight: Int): Char {
    if (data.size < 1)
        throw Error("data is empty")
    val pixels = countBinaryOnes(hash)
    var start = -1
    for (i in 0 until data.size)
        if (checkAccuracy(pixels, data[i].pixels)) {
            start = i
            break
        }
    if (start == -1)
        return linearFind(hash, lineHeight)
    var end = start
    for (i in start + 1 until data.size)
        if (!checkAccuracy(pixels, data[i].pixels)) {
            end = i - 1
            break
        }
    return linearFind(hash, lineHeight, start, end)
}

fun getMiss(hash: BigInteger, lineHeight: Int, letter: Letter) =
        hammingDistance(hash, letter.hash) + letter.pixels * abs(lineHeight - letter.lineHeight)

fun linearFind(hash: BigInteger, lineHeight: Int, start: Int = 0, end: Int = data.size - 1): Char {
    if (data.size < 1)
        throw Error("data is empty")
    var bestItem = data[start]
    var bestMiss = getMiss(hash, lineHeight, bestItem)
    for (i in start + 1..end) {
        val currentItem = data[i]
        val currentMiss = getMiss(hash, lineHeight, currentItem)
        if (currentMiss < bestMiss) {
            bestMiss = currentMiss
            bestItem = currentItem
        }
    }
    return bestItem.char
}

fun hammingDistance(a: BigInteger, b: BigInteger): Int = countBinaryOnes(a xor b)