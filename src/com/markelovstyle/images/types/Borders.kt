package com.markelovstyle.images.types

class Borders(var left: Int, var right: Int, var top: Int, var bottom: Int) {
    val width: Int
        get() = right - left + 1
    val height: Int
        get() = bottom - top + 1

    override fun toString(): String {
        return "($left, $right, $top, $bottom)"
    }
}