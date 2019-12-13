package me.sedaph.newsapp.model.Article

data class Article(
    val id: Int? = null,
    val category_id: Int? = null,
    val comment_count: Int? = null,
    val views_count: Int? = null,
    val favorite_count: Int? = null,
    val title: String? = null,
    val contents: String? = null,
    val createAt: String? = null,
    val removeAt: Int? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val type: Int? = null,
    val videoId: String? = null,
    val category: String? = null
)