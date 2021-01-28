package com.markelovstyle

import com.markelovstyle.data.specifyDataFile
import com.markelovstyle.images.cropBorders
import com.markelovstyle.images.letters.recognize
import com.markelovstyle.images.thresholding
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
    ImageIO.write(cropBorders(thresholding(source)), "bmp", File("testResources\\check.bmp"))
    println(recognize(source))
}
