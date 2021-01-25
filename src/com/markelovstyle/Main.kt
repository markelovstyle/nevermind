package com.markelovstyle

import iris.json.plain.JsonPlainParser
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_BYTE_BINARY
import java.io.ByteArrayInputStream
import java.io.File
import java.math.BigInteger
import java.net.URL
import java.util.*
import javax.imageio.ImageIO


fun main() {
    val json = JsonPlainParser.parse(URL("https://1win-vaucher.ru/static/php/voucher.php")).asMap()
    val base64 = json["src"].toString().drop(13)
    val source: BufferedImage
    if (base64.isBlank()) {
        source = ImageIO.read(File("resources\\test.bmp"))
    } else {
        val bytes = base64Decode(base64)
        val inputStream = ByteArrayInputStream(bytes)
        source = ImageIO.read(inputStream)
        inputStream.close()
    }
    // base64 = File("base64.txt").readText()
    val timer = Timer()
    val th = thresholding(source)
    val crop = cropBorders(th)
    if (crop == null) {
        println("WARNING!")
        return
    }
    val letters = getLetters(crop)
    for (i in 0 until letters.size) {
        val cropped = cropBorders(letters[i])
        if (cropped == null) {
            println("WARNING!")
            continue
        }
        val letter = addBorders(cropped, 120, 200)
        val hash = getHash(letter)
        // TODO: compare hash with db via hammingDistance
        // println(hash)
        ImageIO.write(letter, "bmp", File("resources\\${i}.bmp"))
    }

    // ImageIO.write(, "bmp", File("${i}.bmp"))

    timer.printTime()
}

fun getHash(image: BufferedImage, side: Int = 64, color: Int = -1): BigInteger {  // default color is white; black is -16777216
    val resized = resize(image, side, side)
    var hash = 0.toBigInteger()
    for (y in 0 until side)
        for (x in 0 until side) {
            hash *= 2.toBigInteger()
            if (resized.getRGB(x, y) == color)
                hash += 1.toBigInteger()
        }
    return hash
}

fun hammingDistance(a: BigInteger, b: BigInteger): Long = countBinaryOnes(a xor b)

fun countBinaryOnes(number: BigInteger): Long = number.toString(2).chars().filter { item: Int -> item == '1'.toInt() }.count()

fun resize(source: BufferedImage, w: Int, h: Int): BufferedImage {
    val image = BufferedImage(w, h, TYPE_BYTE_BINARY)
    val g = image.createGraphics()
    g.drawImage(source, 0, 0, w, h, null)
    g.dispose()
    return image
}

fun thresholding(source: BufferedImage): BufferedImage {  // TODO: make param 'threshold' (redundant)
    val width = source.width
    val height = source.height
    val image = BufferedImage(width, height, TYPE_BYTE_BINARY)
    for (y in 0 until height)
        for (x in 0 until width)
            image.setRGB(x, y, source.getRGB(x, y))
    return image
}

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
                left = y
                break@left
            }
    right@
    for (x in (0 until width).reversed())
        for (y in 0 until height)
            if (image.getRGB(x, y) == color) {
                right = y
                break@right
            }

    return crop(image, Rectangle(left, top, right - left + 1, bottom - top + 1))
}

fun addBorders(source: BufferedImage, w: Int, h: Int): BufferedImage {
    val width = source.width
    val height = source.height

    val wBorder = (w - width) / 2
    val hBorder = (h - height) / 2

    val image = BufferedImage(w, h, TYPE_BYTE_BINARY)
    for (y in 0 until height)
        for (x in 0 until width)
            image.setRGB(x + wBorder, y + hBorder, source.getRGB(x, y))

    return image
}


fun crop(source: BufferedImage, rectangle: Rectangle): BufferedImage {
    val image = BufferedImage(rectangle.getWidth().toInt(), rectangle.getHeight().toInt(), TYPE_BYTE_BINARY)
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