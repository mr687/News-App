package me.sedaph.newsapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.adapter.ArticlesAdapter
import me.sedaph.newsapp.model.Article.Article
import me.sedaph.newsapp.model.Article.ResultArticle
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import me.sedaph.newsapp.ui.activity.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class HomeFragment : Fragment(){
    private var mApiService: APIService? = null
    private var mAdapter: ArticlesAdapter? = null
    private val mArticles: MutableList<Article> = ArrayList()
    private var category_id: Int? = 0
    private var allowRefresh: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        category_id = arguments?.getInt("id", 0)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mApiService = RestClient.client.create(APIService::class.java)

        mAdapter = ArticlesAdapter(context, mArticles)

        articleRecyclerView!!.adapter = mAdapter
        articleRecyclerView!!.layoutManager = LinearLayoutManager(context)
        articleRecyclerView!!.itemAnimator = DefaultItemAnimator()

        fetchArticles()
    }

    fun fetchArticles(sort: String? = "createAt", order: String? = "desc"){
        val call = mApiService!!.fetchArticles(0,50,2,category_id, sort, order)
        call.enqueue(object: Callback<ResultArticle>{
            override fun onFailure(call: Call<ResultArticle>, t: Throwable) {}
            override fun onResponse(call: Call<ResultArticle>, response: Response<ResultArticle>) {
                mArticles.clear()
                val result = response.body()
                if(result != null && result.status){
                    val popular = result.popular!!
                    mArticles.addAll(result.articles!!)
                    mAdapter!!.notifyDataSetChanged()

                    popularProgress.visibility = View.VISIBLE
                    topArticleImage.visibility = View.GONE
                    Picasso.get().load(popular.imageUrl)
                        .error(ContextCompat.getDrawable(context!!, R.drawable.no_photo)!!)
                        .into(topArticleImage, object: com.squareup.picasso.Callback{
                            override fun onSuccess() {
                                popularProgress.visibility = View.GONE
                                topArticleImage.visibility = View.VISIBLE
                            }

                            override fun onError(e: Exception?) {
                                popularProgress.visibility = View.GONE
                                topArticleImage.visibility = View.VISIBLE
                            }

                        })
                    topArticleTitle.text = popular.title
                    topArticleCommentCount.text = popular.comment_count!!.toString()
                    topArticleDate.text = popular.createAt!!

                    topArticleImage.setOnClickListener {
                        openDetail(popular.id!!)
                    }
                    topArticleTitle.setOnClickListener {
                        openDetail(popular.id!!)
                    }
                }
            }
        })
    }

    private fun openDetail(id: Int? = 0){
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("position", id)
        intent.putExtra("type", 0)
        startActivity(intent)
    }
}