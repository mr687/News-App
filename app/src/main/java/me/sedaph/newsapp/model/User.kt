package me.sedaph.newsapp.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val imageUrl: String,
    val createAt: Int,
    val removeAt: Int,
    val token: String
)