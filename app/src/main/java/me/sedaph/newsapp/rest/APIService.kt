package me.sedaph.newsapp.rest

import me.sedaph.newsapp.model.Article.ResultArticle
import me.sedaph.newsapp.model.Article.ResultDetailArticle
import me.sedaph.newsapp.model.Category.ResultCategory
import me.sedaph.newsapp.model.User.ResultLogin
import me.sedaph.newsapp.model.User.ResultRegister
import retrofit2.Call
import retrofit2.http.*

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

    //Users
    @POST("user_register")
    @FormUrlEncoded
    fun register(
        @Field("name") name: String? = null,
        @Field("email") email: String? = null,
        @Field("password") password: String? = null
    ):Call<ResultRegister>

    @POST("user_login")
    @FormUrlEncoded
    fun userLogin(
        @Field("email") email: String? = null,
        @Field("password") password: String? = null
    ): Call<ResultLogin>
}