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
//
//fun weighted(source: BufferedImage, color: Int = -1): Array<Array<Int>> =
//    Array(source.height) { y -> Array(source.width) { x -> if (source.getRGB(x, y) == color) 1 else 2} }
//
//
//fun charged(source: BufferedImage, color: Int = -1): Array<Array<Int>> {
//    val energy = Array(source.height) { Array(source.width) { 0 } }
//    val xIndex = { index: Int -> if (index == source.width) 0 else (if (index == -1) source.width - 1 else index) }
//    val yIndex = { index: Int -> if (index == source.height) 0 else (if (index == -1) source.height - 1 else index) }
//    val absValue = { x: Int, y: Int -> if (source.getRGB(xIndex(x), yIndex(y)) == color) 1 else 0 }
//    val square = { number: Int -> number * number}
//    for (y in 0 until source.height)
//        for (x in 0 until source.width)
//            energy[y][x] =
//                    square(absValue(x - 1, y) - absValue(x + 1, y)) +
//                    square(absValue(x, y - 1) - absValue(x, y + 1))
//    return energy
//}
