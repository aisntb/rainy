package com.github.aisntb.rainy.listener

import com.github.aisntb.rainy.event.MessageEvent


interface ListenerAdapter {
    fun onMessageReceived(event: MessageEvent)
}
