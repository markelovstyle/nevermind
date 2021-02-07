package com.markelovstyle.compare

import com.markelovstyle.data.data
import com.markelovstyle.images.countBinaryOnes
import com.markelovstyle.images.side
import com.markelovstyle.images.types.UnknownLetter
import kotlin.math.abs
import kotlin.math.log2
import java.math.BigInteger as Hash

var accuracy = 0.0005f
    set(value) {
        if (0 < value && value < 1)
            field = value
    }

fun checkAccuracy(letter1: UnknownLetter, letter2: UnknownLetter) = abs(letter1.pixels - letter2.pixels).toDouble() / side * side < accuracy

fun closeFind(unknownLetter: UnknownLetter): Char {
    if (data.size < 1)
        throw Error("data is empty")

    var start = -1
    for (i in 0 until data.size)
        if (checkAccuracy(unknownLetter, data[i])) {
            start = i
            break
        }
    if (start == -1)
        return linearFind(unknownLetter)
    var end = start
    for (i in start + 1 until data.size)
        if (!checkAccuracy(unknownLetter, data[i])) {
            end = i - 1
            break
        }
    if (start == end)
        return data[start].char
    return linearFind(unknownLetter, start, end)
}

fun getMiss(letter1: UnknownLetter, letter2: UnknownLetter) =
        hammingDistance(letter1.hash, letter2.hash) + log2(letter2.pixels.toFloat()) * abs(letter1.lineHeight - letter2.lineHeight)

fun linearFind(unknownLetter: UnknownLetter, start: Int = 0, end: Int = data.size - 1): Char {
    if (data.size < 1)
        throw Error("data is empty")
    var bestLetter = data[start]
    var bestMiss = getMiss(unknownLetter, bestLetter)
    if (bestMiss != 0F)
        for (i in (start + 1)..end) {
            val currentLetter = data[i]
            val currentMiss = getMiss(unknownLetter, currentLetter)
            if (currentMiss < bestMiss) {
                bestLetter = currentLetter
                bestMiss = currentMiss
                if (bestMiss == 0F)
                    break
            }
        }
    return bestLetter.char
}

fun hammingDistance(a: Hash, b: Hash): Int = countBinaryOnes(a xor b)