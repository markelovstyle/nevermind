package com.markelovstyle.images.letters

import com.markelovstyle.data.updateData
import com.markelovstyle.images.*
import com.markelovstyle.images.types.Borders
import com.markelovstyle.images.types.Letter
import com.markelovstyle.images.types.UnknownLetter
import java.awt.image.BufferedImage

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

//var i = 0

fun getLetterAreas(image: BufferedImage, color: Int = -1): List<Borders> {

    val width = image.width
    val height = image.height

    val areas: ArrayList<Borders> = arrayListOf()
    var isPreviousEmpty = true
    for (x in 0 until width) {
        var isEmpty = true
        for (y in 0 until height)
            if (image.getRGB(x, y) == color) {
                isEmpty = false
                break
            }
        if (isPreviousEmpty && !isEmpty) {
            areas.add(Borders(x, -1, 0, height - 1))
        } else if (!isPreviousEmpty && isEmpty) {
            areas.last().right = x - 1
        }
        isPreviousEmpty = isEmpty
    }
    areas.last().right = image.width - 1

    for (i in areas.indices) {
        val borders = areas[i]
        if (borders.width > letterWidth) {
            val leftBorder = Borders(borders.left, borders.right - borders.width / 2, borders.top, borders.bottom)
            val rightBorder = Borders(borders.right - borders.width / 2 + 1, borders.right, borders.top, borders.bottom)
            areas.removeAt(i)
            areas.add(i, leftBorder)
            areas.add(i + 1, rightBorder)
        }
    }
    return areas
}

fun getUnknownLetters(source: BufferedImage, color: Int = -1): List<UnknownLetter> {  // default color is white; black is -16777216
    val th = thresholding(source)
    val image = cropBorders(th)
    val areas = getLetterAreas(image, color)
    val textTop = getTopBorder(crop(image, areas[1]))  // bad practice
    return List(areas.size) { i ->
        crop(image, areas[i])
    }.map { crop ->
        UnknownLetter(
            getHash(cropBorders(crop)),
            getTopBorder(crop) - textTop
        )
    }
}

fun addLetters(image: BufferedImage, recog: String) {
    val chars = recog.toCharArray()
    val letters = getUnknownLetters(image)
    for (i in letters.indices) {
        val letter = letters[i]
        val char = chars[i]
        updateData(Letter(char, letter))
    }
}