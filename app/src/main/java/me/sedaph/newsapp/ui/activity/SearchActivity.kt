package me.sedaph.newsapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.main_toolbar.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.adapter.ArticlesAdapter
import me.sedaph.newsapp.model.Article.Article
import me.sedaph.newsapp.model.Article.ResultSearch
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import retrofit2.Call
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private var mApiService: APIService? = null
    private var mArticles: MutableList<Article> = ArrayList()
    private var mAdapter: ArticlesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        val query = intent.getStringExtra("query")
        supportActionBar!!.title = "Search for : $query"

        mApiService = RestClient.client.create(APIService::class.java)

        mAdapter = ArticlesAdapter(this,mArticles)
        searchRecyclerView.adapter = mAdapter
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView!!.itemAnimator = DefaultItemAnimator()

        fetchResultSearch(query)
    }

    private fun fetchResultSearch(query: String) {
        val call = mApiService!!.searchArticle(query)
        call.enqueue(object: retrofit2.Callback<ResultSearch>{
            override fun onFailure(call: Call<ResultSearch>, t: Throwable) {
            }
            override fun onResponse(call: Call<ResultSearch>, response: Response<ResultSearch>) {
                if(response.body() != null){
                    if(response.body()!!.status){
                        mArticles.addAll(response.body()!!.articles!!)
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }
}
