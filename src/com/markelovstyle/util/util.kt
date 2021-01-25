package com.markelovstyle.util

import java.util.*


fun base64Decode(base64Str: String): ByteArray = Base64.getDecoder().decode(base64Str)

class Timer {
    private var start = clearTimer()
    private fun clearTimer(): Long {
        start = current()
        return start
    }

    private fun current() = System.currentTimeMillis()
    private fun getTime(): Long {
        val time = current() - start
        clearTimer()
        return time
    }

    fun printTime() {
        println("${getTime()} ms")
    }
}