package com.markelovstyle.images.letters

import com.markelovstyle.data.updateData
import com.markelovstyle.images.*
import com.markelovstyle.images.types.Borders
import com.markelovstyle.images.types.Letter
import java.awt.image.BufferedImage
import java.util.*
import kotlin.math.ceil

var letterHeight = 200
    set(value) {
        if (0 < value)
            field = value
    }
var letterWidth = 120
    set(value) {
        if (0 < value)
            field = value
    }

fun getLetters(source: BufferedImage, color: Int = -1): List<Letter> {  // default color is white; black is -16777216
    val th = thresholding(source)
    val image = cropBorders(th)

    val width = image.width
    val height = image.height

    val textTop = getTextTop(image)

    val borders: ArrayList<Borders> = arrayListOf()
    var isPreviousEmpty = true
    for (x in 0 until width) {
        var isEmpty = true
        for (y in 0 until height)
            if (image.getRGB(x, y) == color) {
                isEmpty = false
                break
            }
        if (isPreviousEmpty && !isEmpty) {
            borders.add(Borders(x, -1, 0, height))
        } else if (!isPreviousEmpty && isEmpty) {
            borders.last().right = x - 1
        }
        isPreviousEmpty = isEmpty
    }
    borders.last().right = image.width - 1
    return List(borders.size) { i ->
        crop(image, borders[i])
    }.map { crop ->
        Letter(
            getHash(crop),
            getLineHeight(crop, textTop)
        )
    }
}

fun getLineHeight(image: BufferedImage, textTop: Int, color: Int = -1): Int {  // default color is white; black is -16777216
    val borders = getBorders(image, color)
    return ceil(borders.height / 2F).toInt() - (textTop - borders.top)
}

fun getTextTop(image: BufferedImage, color: Int = -1): Int {  // bla-bla about default color, this one is white
    var lastCount = 0
    for (x in 0 until image.width)
        if (image.getRGB(x, image.height / 2) == color)
            lastCount++
    for (y in (0 until image.height / 2).reversed()) {
        var count = 0
        for (x in 0 until image.width)
            if (image.getRGB(x, y) == color)
                count++
        if (lastCount / count > 1.7)
            return y - 1
        lastCount = count
    }
    return 0  // won`t happen
}


fun addLetters(image: BufferedImage, recog: String) {
    val chars = recog.toCharArray()
    val letters = getLetters(image)
    for (i in letters.indices) {
        val letter = letters[i]
        val char = chars[i]
        updateData(char, letter.lineHeight, letter.hash)
    }
}