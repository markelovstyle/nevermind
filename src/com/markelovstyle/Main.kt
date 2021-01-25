package com.markelovstyle

import com.markelovstyle.compare.linearFind
import com.markelovstyle.data.specifyDataFile
import com.markelovstyle.data.updateData
import com.markelovstyle.images.*
import com.markelovstyle.util.Timer
import com.markelovstyle.util.base64Decode
import iris.json.plain.JsonPlainParser
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URL
import javax.imageio.ImageIO


fun main() {
    specifyDataFile("data.db")
    val json = JsonPlainParser.parse(URL("https://1win-vaucher.ru/static/php/voucher.php")).asMap()
    val base64 = json["src"].toString().drop(13)
    val source: BufferedImage
    if (base64.isBlank()) {
        source = ImageIO.read(File("testResources\\test.bmp"))
    } else {
        val bytes = base64Decode(base64)
        val inputStream = ByteArrayInputStream(bytes)
        source = ImageIO.read(inputStream)
        inputStream.close()
    }
    val timer = Timer()
    val th = thresholding(source)
    ImageIO.write(source, "bmp", File("testResources\\check.bmp"))
    val crop = cropBorders(th)
    val letters = getLetters(crop)
    for (i in 0 until letters.size) {
        val cropped = cropBorders(letters[i])
        val letter = addBorders(cropped, 120, 200)
        /* TODO:
            compare hash with db via hammingDistance
            create binaryFind by DataItem.pixels with O(log N)
         */
        val hash = getHash(letter)
        println(linearFind(hash))
        // ImageIO.write(letter, "bmp", File("testResources\\${i}.bmp"))
    }
    // addLetters(source, "1w-ngenx}j")
    timer.printTime()
}

fun addLetters(image: BufferedImage, recog: String) {
    val chars = recog.toCharArray()
    val th = thresholding(image)
    val crop = cropBorders(th)
    val letters = getLetters(crop)
    for (i in 0 until letters.size) {
        val cropped = cropBorders(letters[i])
        val char = chars[i]
        val letter = addBorders(cropped, 120, 200)
        val hash = getHash(letter)
        updateData(char, hash)
    }
}