package com.markelovstyle.http

import iris.json.plain.IrisJsonItem
import iris.json.plain.IrisJsonParser
import java.net.MalformedURLException
import java.net.URL

class Requester {

    fun sendGetRequest(url: String): IrisJsonItem? {
        return try {
            IrisJsonParser(URL(url).readText()).parse()
        } catch (e: MalformedURLException) { null }
    }
}


fun main() {
    // Potential test
    Requester().sendGetRequest("https://1win-vaucher.ru/static/php/voucher.php")
}

