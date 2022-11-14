package com.sixgroup.hospitality.model

import java.util.Date

data class PostModel(
    private val idPost: String,
    private val judul: String,
    private val uploadDate: Date,
    private val content: String,
    private val header: String,
    private val subHeader: String,
    private val uploader: AdminModel
)
