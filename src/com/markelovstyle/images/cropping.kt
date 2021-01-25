package com.markelovstyle.images

import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.util.ArrayList


fun cropBorders(image: BufferedImage, color: Int = -1): BufferedImage? {  // default color is white; black is -16777216
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
        return null

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

    return crop(image, Rectangle(left, top, right - left + 1, bottom - top + 1))
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


fun crop(source: BufferedImage, rectangle: Rectangle): BufferedImage {
    val image = BufferedImage(rectangle.getWidth().toInt(), rectangle.getHeight().toInt(), BufferedImage.TYPE_BYTE_BINARY)
    val g = image.graphics
    g.drawImage(source, 0, 0,
            rectangle.getWidth().toInt(),
            rectangle.getHeight().toInt(),
            rectangle.getX().toInt(),
            rectangle.getY().toInt(),
            (rectangle.getX() + rectangle.getWidth().toInt()).toInt(),
            (rectangle.getY() + rectangle.getHeight().toInt()).toInt(),
            null)
    g.dispose()
    return image
}

fun getLetters(image: BufferedImage, color: Int = -1): ArrayList<BufferedImage> {  // default color is white; black is -16777216
    val width = image.width
    val height = image.height

    val emptyLines: ArrayList<Int> = arrayListOf()
    for (x in 0 until width) {
        var isEmpty = true
        for (y in 0 until height)
            if (image.getRGB(x, y) == color) {
                isEmpty = false
                break
            }
        if (isEmpty)
            emptyLines.add(x)
    }
    val borders: ArrayList<Int> = arrayListOf(0, emptyLines.first())
    for (i in 1 until emptyLines.size)
        if (emptyLines[i] - emptyLines[i - 1] != 1) {
            borders.add(emptyLines[i - 1])
            borders.add(emptyLines[i])
        }
    borders.add(emptyLines.last())
    borders.add(width - 1)

    val rectangles: ArrayList<Rectangle> = arrayListOf()
    for (i in 0 until borders.size step 2)
        rectangles.add(Rectangle(borders[i] + 1, 0, borders[i + 1] - borders[i], height))

    val letters: ArrayList<BufferedImage> = arrayListOf()
    for (rectangle in rectangles)
        letters.add(crop(image, rectangle))

    return letters
}
