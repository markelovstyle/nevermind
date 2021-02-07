package com.markelovstyle.images.types

import com.markelovstyle.images.countBinaryOnes
import java.math.BigInteger

open class UnknownLetter(hash: BigInteger, var lineHeight: Int) {
    var pixels: Int = countBinaryOnes(hash)
    var hash: BigInteger = hash
        set(value) {
            pixels = countBinaryOnes(hash)
            field = value
        }

    override fun toString(): String {
        return "$lineHeight"
    }
}