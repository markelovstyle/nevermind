package com.markelovstyle.images

import com.markelovstyle.images.types.Borders
import java.awt.image.BufferedImage

fun getLineHeight(image: BufferedImage, color: Int = -1): Float {  // default color is white; black is -16777216
    val borders = getBorders(image, color)
    return (borders.top + borders.height / 2).toFloat() / image.height  // returns float in interval [0..1], where 0 is top, 1 is bottom
}

fun getBorders(image: BufferedImage, color: Int = -1): Borders {  // default color is white; black is -16777216
    val width = image.width
    val height = image.height

    var left = width
    var right = 0
    var top: Int = height
    var bottom = 0

    top@
    for (y in 0 until height)
        for (x in 0 until width)
            if (image.getRGB(x, y) == color) {
                top = y
                break@top
            }

    if (top == height)  // no pixels with that color
        throw IllegalArgumentException()

    bottom@
    for (y in (0 until height).reversed())
        for (x in 0 until width)
            if (image.getRGB(x, y) == color) {
                bottom = y
                break@bottom
            }
    left@
    for (x in 0 until width)
        for (y in 0 until height)
            if (image.getRGB(x, y) == color) {
                left = x
                break@left
            }
    right@
    for (x in (0 until width).reversed())
        for (y in 0 until height)
            if (image.getRGB(x, y) == color) {
                right = x
                break@right
            }
    return Borders(left, right, top, bottom)
}

fun cropBorders(image: BufferedImage, color: Int = -1): BufferedImage {  // default color is white; black is -16777216
    return crop(image, getBorders(image, color))
}

fun addBorders(source: BufferedImage, w: Int, h: Int): BufferedImage {
    val width = source.width
    val height = source.height

    val wBorder = (w - width) / 2
    val hBorder = (h - height) / 2

    val image = BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY)
    for (y in 0 until height)
        for (x in 0 until width)
            image.setRGB(x + wBorder, y + hBorder, source.getRGB(x, y))

    return image
}


fun crop(source: BufferedImage, borders: Borders): BufferedImage {
    val image = BufferedImage(borders.width, borders.height, BufferedImage.TYPE_BYTE_BINARY)
    val g = image.graphics
    g.drawImage(source, 0, 0,
            borders.width,
            borders.height,
            borders.left,
            borders.top,
            borders.right,
            borders.bottom,
            null)
    g.dispose()
    return image
}
