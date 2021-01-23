package com.markelovstyle

import org.w3c.dom.Document
import java.io.File
import java.net.URL
import javax.imageio.IIOException
import javax.xml.parsers.DocumentBuilderFactory


fun main() {
    val file = downloadFile("https://1win-vaucher.ru/", "resources\\test.html")
    val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
    xmlDoc.documentElement.normalize()
    println("Root Node:" + xmlDoc.documentElement.nodeName)
}

fun downloadFile(url: String, path: String): File? {
    return try {
        File(path).also { it.writeBytes(URL(url).readBytes()) }
    } catch (e: IIOException) { null }
}