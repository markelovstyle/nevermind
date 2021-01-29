package com.markelovstyle

import com.markelovstyle.data.specifyDataFile
import com.markelovstyle.images.letters.addLetters
import com.markelovstyle.images.recognizing.recognize
import com.markelovstyle.util.base64Decode
import iris.json.plain.JsonPlainParser
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URL
import javax.imageio.ImageIO


fun main() {
    specifyDataFile("data.db")
    var oldBase64 = ""
    while (true) {
        val json = JsonPlainParser.parse(URL("https://1win-vaucher.ru/static/php/voucher.php")).asMap()
        val base64 = json["src"].toString().drop(13)
        var i = 0
        // val base64 = ""
        val source: BufferedImage
        if (oldBase64 != base64 && base64.isNotBlank()) {
            val bytes = base64Decode(base64)
            val inputStream = ByteArrayInputStream(bytes)
            source = ImageIO.read(inputStream)
            inputStream.close()
            ImageIO.write(source, "bmp", File("testResources\\$i.bmp"))
            i++
            // addLetters(source, "1w-8~o1n|q")
            // println(recognize(source))
            oldBase64 = base64
        }
        Thread.sleep(1_000 * 60 * 60)  // every hour
    }
}
