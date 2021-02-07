package com.markelovstyle.data

import com.markelovstyle.images.types.Letter
import java.io.File
import java.io.FileNotFoundException
import com.markelovstyle.util.println
import java.lang.IllegalArgumentException

var file: File = File("data.db")
var data = loadData()

fun specifyDataFile(filename: String) {
    file = File(filename)
    if (!file.exists())
        throw FileNotFoundException("File $filename was not found.")
}

fun loadData(): MutableList<Letter> {
    data = file.readLines().map { line -> Letter(line) }.toMutableList()
    return data
}

fun updateData(unknownLetter: Letter) {
    var exists = false
    for (letter in data)
        if (letter.char == unknownLetter.char) {
            letter.hash = unknownLetter.hash
            if (unknownLetter.lineHeight != letter.lineHeight)
                println(unknownLetter.char, letter.lineHeight, unknownLetter.lineHeight)
            letter.lineHeight = unknownLetter.lineHeight
            exists = true
            break
        }
    if (!exists) {
        data.add(unknownLetter)
        println(unknownLetter.char)
    }
    sortData()
    saveData()
}

fun findLetter(char: Char): Letter {
    for (letter in data)
        if (letter.char == char)
            return letter
    throw IllegalArgumentException()
}

fun sortData() = data.sortBy { it.pixels }

fun saveData() = file.writeText(data.joinToString("\n") { "${it.char.toInt()},${it.lineHeight},${it.hash}" })