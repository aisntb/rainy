package com.github.aisntb.rainy.event

import com.github.aisntb.rainy.Bot
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

data class MessageEvent(
    val room: String,
    val message: String,
    val sender: String,
    val rawJson: Bot.ChatLogJson,
    val serverURL: String
) {
    // 기존 suspend 함수
    private suspend fun replyTextUnblock(msg: String) {
        val client = HttpClient(CIO)
        client.post("http://${serverURL}/reply") {
            contentType(ContentType.Application.Json)
            setBody("""{ "type": "text", "room": "${rawJson.chat_id}", "data": "$msg" }""")
        }

    }

    fun replyText(msg: String) {
        runBlocking {
            replyTextUnblock(msg)
        }
    }

    private suspend fun replyImageUnblock(data: String) {
        val client = HttpClient(CIO)
        client.post("http://${serverURL}/reply") {
            contentType(ContentType.Application.Json)
            setBody("""{ "type": "image", "room": "${rawJson.chat_id}", "data": "$data" }""")
        }

    }

    fun replyImage(data:String) {
        runBlocking {
            replyImageUnblock(data)
        }
    }
    private suspend fun replyImagesUnblock(data: String) {
        val client = HttpClient(CIO)
        client.post("http://${serverURL}/reply") {
            contentType(ContentType.Application.Json)
            setBody("""{ "type": "image_multiple", "room": "${rawJson.chat_id}", "data": $data }""")

        }

    }

    fun replyImages(data:String) {
        runBlocking {
            replyImagesUnblock(data)
        }
    }
}
