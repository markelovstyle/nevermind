package com.markelovstyle.images.types

class Borders(var left: Int, var right: Int, var top: Int, var bottom: Int) {
    init {
        var left = left
        var right = right
        var top = top
        var bottom = bottom
    }
    val width: Int
    get() = right - left
    val height: Int
    get() = bottom - top

    override fun toString(): String {
        return "($left, $right, $top, $bottom)"
    }
}