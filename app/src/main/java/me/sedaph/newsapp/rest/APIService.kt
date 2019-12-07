package me.sedaph.newsapp.rest

import me.sedaph.newsapp.model.Article.ResultAddArticle
import me.sedaph.newsapp.model.Article.ResultArticle
import me.sedaph.newsapp.model.Article.ResultDetailArticle
import me.sedaph.newsapp.model.Article.ResultView
import me.sedaph.newsapp.model.Category.ResultCategory
import me.sedaph.newsapp.model.Image.ResultImage
import me.sedaph.newsapp.model.User.ResultLogin
import me.sedaph.newsapp.model.User.ResultRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Query("type") type: Int? = 0,
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

    // Update view
    @POST("view")
    @FormUrlEncoded
    fun viewUpdate(
        @Field("articleId") articleId: Int? = 0
    ): Call<ResultView>

    // post image
    @Multipart
    @POST("article_image")
    fun articleImageUpload(
        @Part("uid") uid: RequestBody,
        @Part("token") token: RequestBody,
        @Part file: MultipartBody.Part
    ):Call<ResultImage>

    // post article
    @POST("article_add")
    @FormUrlEncoded
    fun articlePost(
        @Field("uid") uid: Int? = 0,
        @Field("token") token: String? = null,
        @Field("title") title: String? = null,
        @Field("contents") contents: String? = null,
        @Field("category_id") category_id: Int? = 0,
        @Field("imageUrl") imageUrl: String? = null,
        @Field("type") type: Int? = 0
    ):Call<ResultAddArticle>
}