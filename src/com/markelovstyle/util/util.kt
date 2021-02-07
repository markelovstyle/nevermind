package com.markelovstyle.util

import java.util.*


fun println(a: Any?, vararg b: Any?) {
    print(a); b.forEach{ print(", $it")}; println()
}

fun base64Decode(base64Str: String): ByteArray = Base64.getDecoder().decode(base64Str)

fun getTime() = System.currentTimeMillis()

operator fun String.times(times: Int): String {
    if (times < 0)
        throw IllegalArgumentException("Why so negative")
    if (times == 0)
        return ""
    if (times == 1)
        return this
    if (times == 2)
        return this + this
    val sb = StringBuilder(this.length * times)
    for (i in 0 until times)
        sb.append(this)
    return sb.toString()
}

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