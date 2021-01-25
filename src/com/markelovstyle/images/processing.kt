package com.markelovstyle.images

import java.awt.image.BufferedImage


fun resize(source: BufferedImage, w: Int, h: Int): BufferedImage {
    val image = BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY)
    val g = image.createGraphics()
    g.drawImage(source, 0, 0, w, h, null)
    g.dispose()
    return image
}

fun thresholding(source: BufferedImage): BufferedImage {  // TODO: make param 'threshold' (redundant)
    val width = source.width
    val height = source.height
    val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY)
    for (y in 0 until height)
        for (x in 0 until width)
            image.setRGB(x, y, source.getRGB(x, y))
    return image
}
