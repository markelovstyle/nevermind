package com.markelovstyle.images.recognizing

import com.markelovstyle.compare.closeFind
import com.markelovstyle.images.letters.getUnknownLetters
import java.awt.image.BufferedImage

fun recognize(image: BufferedImage): String =
        getUnknownLetters(image).map { closeFind(it) }.joinToString("") { "$it" }
