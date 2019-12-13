package me.sedaph.newsapp.model.Article

data class ResultSearch(
    val status: Boolean = false,
    val message: String? = null,
    val total: Int? = null,
    val articles: List<Article>? = null
)