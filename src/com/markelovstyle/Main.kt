package com.markelovstyle

import org.w3c.dom.Document
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


fun main() {
    val file = downloadFile("https://1win-vaucher.ru/", "resources\\test.html")
    val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
    xmlDoc.documentElement.normalize()
    println("Root Node:" + xmlDoc.documentElement.nodeName)
}

fun downloadFile(url: String, path: String): File? {
    try {
        BufferedInputStream(URL(url).openStream()).use { inputStream ->
            FileOutputStream(path).use { fileOS ->
                val data = ByteArray(1024)
                var byteContent: Int
                while (inputStream.read(data, 0, 1024).also { byteContent = it } != -1) {
                    fileOS.write(data, 0, byteContent)
                }
                return File(path)
            }
        }
    } catch (e: IOException) {
        return null
    }
}
