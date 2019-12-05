package me.sedaph.newsapp.model.Article

data class ResultDetailArticle(
    val status: Boolean = false,
    val message: String? = null,
    val total: Int? = null,
    val articles: Article? = null
)