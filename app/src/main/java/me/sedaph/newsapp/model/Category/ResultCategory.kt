package me.sedaph.newsapp.model.Category

data class ResultCategory (
    val status: Boolean = false,
    val message: String? = null,
    val categories: List<Category>? = null
)