package com.markelovstyle.images.types

import java.math.BigInteger

private class Params(line: String) {
    val char: Char
    val lineHeight: Int
    val hash: BigInteger

    init {
        val pair = line.split(',')
        this.char = pair[0].toInt().toChar()
        this.lineHeight = pair[1].toInt()
        this.hash = pair[2].toBigInteger()
    }
}

class Letter(val char: Char, hash: BigInteger, lineHeight: Int): UnknownLetter(hash, lineHeight) {
    private constructor(params: Params): this(params.char, params.hash, params.lineHeight)
    constructor(line: String): this(Params(line))
    constructor(char: Char, unknownLetter: UnknownLetter): this(char, unknownLetter.hash, unknownLetter.lineHeight)
}