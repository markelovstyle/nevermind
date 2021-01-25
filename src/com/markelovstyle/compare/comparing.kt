package com.markelovstyle.compare

import com.markelovstyle.data.data
import com.markelovstyle.data.file
import com.markelovstyle.images.countBinaryOnes
import java.math.BigInteger

//fun binaryFind(hash: BigInteger): Char {
//
//}

fun linearFind(hash: BigInteger): Char {
    if (data.size == 0)
        throw UnknownError("data from ${file.absolutePath} is empty")
    var bestItem = data[0]
    var bestMiss = hammingDistance(hash, bestItem.hash)
    for (i in 1 until data.size) {
        val currentItem = data[i]
        val currentMiss = hammingDistance(hash, currentItem.hash)
        if (currentMiss < bestMiss) {
            bestMiss = currentMiss
            bestItem = currentItem
        }
    }
    return bestItem.char
}

fun hammingDistance(a: BigInteger, b: BigInteger): Long = countBinaryOnes(a xor b)
