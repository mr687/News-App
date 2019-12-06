package me.sedaph.newsapp.model.User

data class ResultLogin(
    val status: Boolean? = false,
    val message: String? = null,
    val user: User? = null
)