package me.sedaph.newsapp.model.Category

import me.sedaph.newsapp.model.User

data class ResultCategory (
    val status: Boolean = false,
    val message: String? = null,
    val categories: List<Category>? = null
)