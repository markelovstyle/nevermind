package com.markelovstyle

import com.markelovstyle.images.thresholding

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
    // base64 = File("base64.txt").readText()
    val timer = Timer()
    val th = thresholding(source)
    ImageIO.write(source, "bmp", File("testResources\\check.bmp"))
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
        println(hash)
        // TODO: compare hash with db via hammingDistance
        // println(hash)
        ImageIO.write(letter, "bmp", File("testResources\\${i}.bmp"))
    }

    // ImageIO.write(, "bmp", File("${i}.bmp"))

    timer.printTime()
}