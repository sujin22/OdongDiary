package com.example.owndiary

data class DiaryItem(
    val image: Int,
    val date: String,
    val text: String,
    var isFavorite: Boolean,
)
