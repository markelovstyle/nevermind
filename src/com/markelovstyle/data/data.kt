package com.markelovstyle.data

import com.markelovstyle.images.countBinaryOnes
import java.io.File
import java.io.FileNotFoundException
import java.math.BigInteger

var file: File = File("data.db")
var data = loadData()

class DataItem(val char: Char, var lineHeight: Float, var hash: BigInteger, var pixels: Int = -1) {
    init {
        if (pixels == -1)
            pixels = countBinaryOnes(hash)
    }

    override fun toString(): String {
        return "$char $lineHeight $pixels"
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
    val char = pair[0].toInt().toChar()
    val lineHeight = pair[1].toFloat()
    val hash = pair[2].toBigInteger()
    return DataItem(char, lineHeight, hash)
}

fun updateData(char: Char, lineHeight: Float, hash: BigInteger) {
    var exists = false
    for (item in data)
        if (item.char == char) {
            item.hash = hash
            item.lineHeight = lineHeight
            item.pixels = countBinaryOnes(hash)
            exists = true
            break
        }
    if (!exists)
        data.add(DataItem(char, lineHeight, hash))
    sortData()
    saveData()
}

fun sortData() = data.sortBy { it.pixels }

fun saveData() = file.writeText(data.joinToString("\n") { "${it.char.toInt()},${it.lineHeight},${it.hash}" })