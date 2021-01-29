package com.markelovstyle.data

import com.markelovstyle.images.types.DeterminedLetter
import java.io.File
import java.io.FileNotFoundException
import java.math.BigInteger

var file: File = File("data.db")
var data = loadData()

fun specifyDataFile(filename: String) {
    file = File(filename)
    if (!file.exists())
        throw FileNotFoundException("File $filename was not found.")
}

fun loadData(): MutableList<DeterminedLetter> {
    data = file.readLines().map { line -> DeterminedLetter(line) }.toMutableList()
    return data
}

fun updateData(char: Char, lineHeight: Int, hash: BigInteger) {
    var exists = false
    for (determinedLetter in data)
        if (determinedLetter.char == char) {
            determinedLetter.hash = hash
            determinedLetter.lineHeight = lineHeight
            exists = true
            break
        }
    if (!exists)
        data.add(DeterminedLetter(char, hash, lineHeight))
    sortData()
    saveData()
}

fun sortData() = data.sortBy { it.pixels }

fun saveData() = file.writeText(data.joinToString("\n") { "${it.char.toInt()},${it.lineHeight},${it.hash}" })