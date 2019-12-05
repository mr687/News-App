package me.sedaph.newsapp.rest

import me.sedaph.newsapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {
    private val URL = Constants.API_URl
    private var mRetrofit: Retrofit? = null

    val client: Retrofit
        get(){
            if (mRetrofit == null)
            {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mRetrofit!!
        }
}