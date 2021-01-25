package com.markelovstyle.data

import com.markelovstyle.images.countBinaryOnes
import com.markelovstyle.util.toChar
import java.io.File
import java.io.FileNotFoundException
import java.math.BigInteger

var file: File = File("data.db")
var data: MutableList<DataItem> = loadData()

class DataItem(val char: Char, val hash: BigInteger, var pixels: Long = -1) {
    init {
        if (pixels == -1L)
            pixels = countBinaryOnes(hash)
    }
}

fun specifyDataFile(filename: String) {
    file = File(filename)
    if (!file.exists())
        throw FileNotFoundException("File $filename was not found.")
}

fun loadData(): MutableList<DataItem> {
    val lines = file.readLines()
    data = MutableList(lines.size) { i -> createDataItem(lines[i]) }
    return data
}

fun createDataItem(line: String): DataItem {
    val pair = line.split(',')
    val char = pair[0].toChar()
    val hash = pair[1].toBigInteger()
    return DataItem(char, hash)
}

fun updateData(char: Char, hash: BigInteger) {
    data.add(DataItem(char, hash))
    sortData()
    saveData()
}

fun sortData() = data.sortBy { it.pixels }

fun saveData() = file.writeText(data.joinToString("\n") { "${it.char},${it.hash}" })
