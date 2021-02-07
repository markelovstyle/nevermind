package com.markelovstyle

import com.markelovstyle.data.data
import com.markelovstyle.data.specifyDataFile
import com.markelovstyle.images.letters.addLetters
import com.markelovstyle.images.recognizing.recognize
import com.markelovstyle.util.Timer
import com.markelovstyle.util.base64Decode
import com.markelovstyle.util.println
import iris.json.plain.JsonPlainParser
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

fun main() {
    specifyDataFile("data.db")
    // TODO
}

fun printAlphabet() {
    val alphabet = "abcdefghijklmnopqrstuvwxyz0123456789!\"#\$%&'()*+,-./:;<=>?@[\\]^_`{|}~".map { it }.sorted().joinToString("")
    val now = data.map { it.char }.sorted().joinToString("")
    println(now)
    println(alphabet)
    println(alphabet.length - now.length)
    println((alphabet.toList() - now.toList()).joinToString(""))
}

fun collect(start: Int) {
    var i = start
    var oldBase64 = ""
    while (true) {
        val json = JsonPlainParser.parse(URL("https://1win-vaucher.ru/static/php/voucher.php")).asMap()
        val base64 = json["src"].toString().drop(13)
        val source: BufferedImage
        if (oldBase64 != base64 && base64.isNotBlank()) {
            val bytes = base64Decode(base64)
            val inputStream = ByteArrayInputStream(bytes)
            source = ImageIO.read(inputStream)
            inputStream.close()
            ImageIO.write(source, "bmp", File("testResources\\${i++}.bmp"))
            oldBase64 = base64
        }
        Thread.sleep(1_000 * 60 * 60)  // every hour
    }
}

fun add() {
    val results = arrayOf(
            "1w-0zit4di",
            "1w-~s%g*la",
            "1w-keweuk$",
            "1w-{tf~v8$",
            "1w-h8nln4c",
            "1w-gr8y#4c",
            "1w-pmn498d",
            "1w-h$|m9#0",
            "1w-|2ycewc",
            "1w-p~mdn?j",
            "1w-p3ys5w6",
            "1w-*5}*jcy",
            "1w-y#?$8{w",
            "1w-fxth?29",
            "1w-f9l60wb",
            "1w-qmr#6tt",
            "1w-i##@0o6",
            "1w-9qpjdd}",
            "1w-k4~xipy",
            "1w-mlt6#x}",
            "1w-2plgmzg",
            "1w-|6lqf#e",
            "1w-8wnkpkj",
            "1w-nmjd1x1",
            "1w-uo{0r2t",
            "1w-s{o|vs?",
            "1w-||#~7k~",
            "1w-|0fgb1m",
            "1w-i7~|cr@",
            "1w-b~uywap",
            "1w-126##}t",
            "1w-0mzt5cd",
            "1w-fzfo*o8",
            "1w-t?h2vf%",
            "1w-h9mcf5z",
            "1w-vp5fw|a",
            "1w-#~*4n@x",
            "1w-6\${d1{$",
            "1w-s#69}5g",
            "1w-n5*ec29",
            "1w-6i#3b87",
            "1w-~%%\$hpl",
            "1w-#ce}7ws",
            "1w-g7n~c~y",
            "1w-i5q%1|o",
            "1w-frsqq07",
            "1w-\$phgoy3",
            "1w-\$|iiekc",
            "1w-m9\$c#|~",
            "1w-sqg8{yc",
            "1w-ftd\$nl\$",
            "1w-1|fdm8k",
            "1w-d?3cas5",
            "1w-do9\$mqc",
            "1w-9gojsu%",
            "1w-czi5p%4",
            "1w-ia|hh73",
            "1w-zpmm4}s",
            "1w-4f?gzx{",
            "1w-\$d@%{8x",
            "1w-w0mhy0s",
            "1w-chnpa?g",
    )
    val files = File("testResources").listFiles() ?: throw IllegalArgumentException()
    val images = files.sortedBy {it.toString().substringAfter('\\').substringBefore('.').toInt()}.map {ImageIO.read(it)}
    if (images.size != results.size)
        throw IllegalArgumentException()
    for (i in results.indices)
        addLetters(images[i], results[i])
}

fun benchmark() {
    val files = File("testResources").listFiles() ?: throw IllegalArgumentException()
    val images = files.sortedBy {it.toString().substringAfter('\\').substringBefore('.').toInt()}.map {ImageIO.read(it)}
    for (image in images)
        recognize(image)
    val timer = Timer()
    for (image in images)
        recognize(image)
    println(timer.current() / images.size, "ms")
}