package com.markelovstyle.util

import java.util.*


fun println(a: Any?, vararg b: Any?) {
    print(a); b.forEach{ print(", $it")}; println()
}

fun base64Decode(base64Str: String): ByteArray = Base64.getDecoder().decode(base64Str)

fun String.toChar() = this.toCharArray()[0]

fun getTime() = System.currentTimeMillis()

class Timer {
    private var start = clear()
    fun clear(): Long {
        start = getTime()
        return start
    }

    fun current(): Long {
        val time = getTime() - start
        clear()
        return time
    }

    fun printTime() {
        println("${current()} ms")
    }
}