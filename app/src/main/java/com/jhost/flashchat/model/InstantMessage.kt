package com.jhost.flashchat.model

class InstantMessage(message: String = "", author: String = "") {
    var message: String? = null
    var author: String? = null

    init {
        this.message = message
        this.author = author
    }
}