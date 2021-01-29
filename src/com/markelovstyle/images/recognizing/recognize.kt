package com.markelovstyle.images.recognizing

import com.markelovstyle.compare.closeFind
import com.markelovstyle.images.letters.getLetters
import java.awt.image.BufferedImage

fun recognize(image: BufferedImage): String =
        getLetters(image).map { closeFind(it.hash, it.lineHeight) }.joinToString("") { "$it" }
