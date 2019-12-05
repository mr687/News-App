package me.sedaph.newsapp.model.Article

data class ResultArticle(
    val status: Boolean = false,
    val message: String? = null,
    val total: Int? = null,
    val popular: Article? = null,
    val articles: List<Article>? = null
)