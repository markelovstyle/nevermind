package com.markelovstyle.images.letters

import com.markelovstyle.compare.closeFind
import com.markelovstyle.data.DataItem
import com.markelovstyle.data.updateData
import com.markelovstyle.images.*
import com.markelovstyle.images.types.Borders
import java.awt.image.BufferedImage
import java.io.File
import java.lang.StringBuilder
import java.util.ArrayList
import javax.imageio.ImageIO

fun recognize(image: BufferedImage): String {
    val items = getLettersData(image)
    val builder = StringBuilder(items.size)
    for (item in items) {
        val hash = item.hash
        val lineHeight = item.lineHeight
        val char = closeFind(hash, lineHeight)
        builder.append(char)
    }
    return builder.toString()
}

fun getLetters(image: BufferedImage, color: Int = -1): Array<BufferedImage> {  // default color is white; black is -16777216
    val width = image.width
    val height = image.height

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
    return Array(borders.size) { i -> crop(image, borders[i]) }
}

fun getLettersData(image: BufferedImage): List<DataItem> {
    val th = thresholding(image)
    val crop = cropBorders(th)
    val letters = getLetters(crop)
    val items: MutableList<DataItem> = mutableListOf()
    for (i in letters.indices) {
        val letter = letters[i]
        val cropped = cropBorders(letter)
        val lineHeight = getLineHeight(letter)
        val normalizedLetter = addBorders(cropped, 120, 200)
        ImageIO.write(normalizedLetter, "bmp", File("testResources\\$i.bmp"))
        val hash = getHash(normalizedLetter)
        items.add(DataItem(0.toChar(), lineHeight, hash))
    }
    return items
}

fun addLetters(image: BufferedImage, recog: String) {
    val chars = recog.toCharArray()
    val letters = getLettersData(image)
    for (i in letters.indices) {
        val letter = letters[i]
        val hash = letter.hash
        val lineHeight = letter.lineHeight
        val char = chars[i]
        updateData(char, lineHeight, hash)
    }
}