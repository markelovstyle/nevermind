package com.markelovstyle.images

import com.markelovstyle.images.letters.letterHeight
import com.markelovstyle.images.letters.letterWidth
import com.markelovstyle.images.types.Borders
import java.awt.image.BufferedImage

fun getBorders(image: BufferedImage, color: Int = -1): Borders {  // default color is white; black is -16777216
    val left = getLeftBorder(image, color)
    val right = getRightBorder(image, color)
    val top = getTopBorder(image, color)
    val bottom = getBottomBorder(image, color)
    return Borders(left, right, top, bottom)
}

fun getTopBorder(image: BufferedImage, color: Int = -1): Int {
    for (y in 0 until image.height)
        for (x in 0 until image.width)
            if (image.getRGB(x, y) == color)
                return y
    return image.height - 1
}

fun getBottomBorder(image: BufferedImage, color: Int = -1): Int {
    for (y in (0 until image.height).reversed())
        for (x in 0 until image.width)
            if (image.getRGB(x, y) == color)
                return y
    return 0
}

fun getLeftBorder(image: BufferedImage, color: Int = -1): Int {
    for (x in 0 until image.width)
        for (y in 0 until image.height)
            if (image.getRGB(x, y) == color)
                return x
    return image.width - 1
}

fun getRightBorder(image: BufferedImage, color: Int = -1): Int {
    for (x in (0 until image.width).reversed())
        for (y in 0 until image.height)
            if (image.getRGB(x, y) == color)
                return x
    return 0
}

fun cropBorders(image: BufferedImage, color: Int = -1): BufferedImage {  // default color is white; black is -16777216
    return crop(image, getBorders(image, color))
}

fun addBorders(source: BufferedImage, w: Int = letterWidth, h: Int = letterHeight): BufferedImage {
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
            borders.right + 1,
            borders.bottom + 1,
            null)
    g.dispose()
    return image
}
