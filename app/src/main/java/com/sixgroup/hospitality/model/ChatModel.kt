package com.sixgroup.hospitality.model

import java.util.Date

data class ChatModel(
    private val sender: UserModel,
    private val message: String,
    private val hasEmbed: Boolean,
    private val timestamp: Date
)
