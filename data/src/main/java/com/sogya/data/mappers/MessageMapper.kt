package com.sogya.data.mappers

import com.sogya.data.models.MessageData
import com.sogya.domain.models.Message

class MessageMapper(
    private val messageData: MessageData
) {
    fun toMessageDomian(): Message = Message(
        messageData.message
    )
}