package com.sixgroup.hospitality.model

data class ChatModel(
    var sender: String? = "",
    var message: String? = "",
    var timestamp: String? = ""
) {
    // getter dan setter sudah via property access kotlin
}
