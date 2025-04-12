package com.github.aisntb.rainy

import com.github.aisntb.rainy.event.MessageEvent

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import com.github.aisntb.rainy.listener.ListenerAdapter
import kotlinx.coroutines.runBlocking


class Bot(private val ip: String = "0.0.0.0") {
    private val listeners = mutableListOf<ListenerAdapter>()

    val botIp: String
        get() = ip

    fun addListener(listener: ListenerAdapter) {
        listeners.add(listener)
    }

    fun start() {
        embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            routing {
                post("/") {
                    val payload = call.receive<IncomingMessage>()
                    val event = MessageEvent(
                        room = payload.room,
                        message = payload.msg,
                        sender = payload.sender,
                        rawJson = payload.json,
                        serverURL = ip
                    )
                    listeners.forEach { it.onMessageReceived(event) }
                    call.respondText("OK")
                }
            }
        }.start(wait = true)
    }

    suspend fun retrieveUserByIdUnblock(userId: String): MessageEvent.UserInfo? {
        val userInfo = MessageEvent.fetchUserInfo(ip,userId)
        return userInfo
    }

    fun retrieveUserById(userId: String): MessageEvent.UserInfo? {
        return runBlocking {
            retrieveUserByIdUnblock(userId)
        }
    }

    @Serializable
    data class ChatLogJson(
        @SerialName("_id") val id: String,
        val chat_id: String,
        val user_id: String,
        val message: String,
        val attachment: String? = null,
        val v: String
    )

    @Serializable
    data class IncomingMessage(
        val msg: String,
        val room: String,
        val sender: String,
        val json: ChatLogJson
    )
}