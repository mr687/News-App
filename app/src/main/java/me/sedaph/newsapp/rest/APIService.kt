package me.sedaph.newsapp.rest

import me.sedaph.newsapp.model.Article.ResultArticle
import me.sedaph.newsapp.model.Article.ResultDetailArticle
import me.sedaph.newsapp.model.Category.ResultCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    // Category
    @GET("categories")
    fun fetchCategories(): Call<ResultCategory>

    //Articles
    @GET("articles")
    fun fetchArticles(
            @Query("offset") offset: Int? = 0,
            @Query("limit") limit: Int? = 10,
            @Query("type") type: Int? = 0,
            @Query("category") category: Int? = 0
        ): Call<ResultArticle>

    @GET("articles")
    fun detailArticle(
        @Query("id") id: Int? = 0,
        @Query("limit") limit: Int? = 1
    ): Call<ResultDetailArticle>
}