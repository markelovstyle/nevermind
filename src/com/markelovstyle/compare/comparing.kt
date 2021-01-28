package com.markelovstyle.compare

import com.markelovstyle.data.DataItem
import com.markelovstyle.data.data
import com.markelovstyle.images.countBinaryOnes
import java.math.BigInteger
import kotlin.math.abs

var accuracy = 0.0025f

//fun changeAccuracy(newAccuracy: Float): Boolean {
//    if (0 < newAccuracy && newAccuracy < 1) {
//        accuracy = newAccuracy
//        return true
//    }
//    return false
//}

fun checkAccuracy(first: Int, second: Int) = abs(first - second).toDouble() / 4096 < accuracy


fun closeFind(hash: BigInteger, lineHeight: Float): Char {
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

fun getMiss(hash: BigInteger, lineHeight: Float, item: DataItem) =
        hammingDistance(hash, item.hash) + 4096 * abs(lineHeight - item.lineHeight)

fun linearFind(hash: BigInteger, lineHeight: Float, start: Int = 0, end: Int = data.size - 1): Char {
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