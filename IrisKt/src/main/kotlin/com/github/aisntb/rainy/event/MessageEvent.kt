package com.github.aisntb.rainy.event

import com.github.aisntb.rainy.Bot
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*

data class MessageEvent(
    val room: String,
    val message: String,
    val sender: String,
    val rawJson: Bot.ChatLogJson,
    val serverURL: String
) {

    companion object {
        suspend fun fetchUserInfo(serverURL: String, userId: String): UserInfo? {
            val client = HttpClient(CIO)
            val response = client.post("http://$serverURL/query") {
                contentType(ContentType.Application.Json)
                setBody("""{ "query": "SELECT * FROM open_chat_member WHERE user_id=?", "bind": ["$userId"] }""")
            }
            val jsonElement = Json.parseToJsonElement(response.bodyAsText()).jsonObject
            val dataArray = jsonElement.jsonObject["data"]?.jsonArray ?: JsonArray(emptyList())

            return dataArray?.firstOrNull()?.let { json ->
                UserInfo(
                    profileType = json.jsonObject["profile_type"]?.jsonPrimitive?.content ?: "",
                    profileImageUrl = json.jsonObject["profile_image_url"]?.jsonPrimitive?.content ?: "",
                    profileLinkId = json.jsonObject["profile_link_id"]?.jsonPrimitive?.content ?: "",
                    privilege = json.jsonObject["privilege"]?.jsonPrimitive?.content ?: "",
                    type = json.jsonObject["type"]?.jsonPrimitive?.content ?: "",
                    linkId = json.jsonObject["link_id"]?.jsonPrimitive?.content ?: "",
                    userId = json.jsonObject["user_id"]?.jsonPrimitive?.content ?: "",
                    nickname = json.jsonObject["nickname"]?.jsonPrimitive?.content ?: "",
                    report = json.jsonObject["report"]?.jsonPrimitive?.content ?: "",
                    originalProfileImageUrl = json.jsonObject["original_profile_image_url"]?.jsonPrimitive?.content ?: "",
                    id = json.jsonObject["_id"]?.jsonPrimitive?.content ?: "",
                    enc = json.jsonObject["enc"]?.jsonPrimitive?.content ?: "",
                    linkMemberType = json.jsonObject["link_member_type"]?.jsonPrimitive?.content ?: "",
                    fullProfileImageUrl = json.jsonObject["full_profile_image_url"]?.jsonPrimitive?.content ?: "",
                    involvedChatId = json.jsonObject["involved_chat_id"]?.jsonPrimitive?.content ?: ""
                )
            }
        }
    }

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

    val user: UserInfo?
        get() = runBlocking {
            fetchUserInfo(serverURL, rawJson.user_id)
        }


    data class UserInfo(
        val profileType: String,
        val profileImageUrl: String,
        val profileLinkId: String,
        val privilege: String,
        val type: String,
        val linkId: String,
        val userId: String,
        val nickname: String,
        val report: String,
        val originalProfileImageUrl: String,
        val id: String, // _id → id 로 이름 변경
        val enc: String,
        val linkMemberType: String,
        val fullProfileImageUrl: String,
        val involvedChatId: String
    )
}
