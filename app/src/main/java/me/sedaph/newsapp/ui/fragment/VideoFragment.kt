package me.sedaph.newsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_video.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.adapter.VideosAdapter
import me.sedaph.newsapp.model.Article.Article
import me.sedaph.newsapp.model.Article.ResultArticle
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoFragment : Fragment(){
    private var mApiService: APIService? = null
    private var mAdapter: VideosAdapter? = null
    private val mVideos: MutableList<Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_video, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mApiService = RestClient.client.create(APIService::class.java)

        mAdapter = VideosAdapter(context, mVideos)

        videoRecyclerView.adapter = mAdapter
        videoRecyclerView.layoutManager = LinearLayoutManager(context)
        videoRecyclerView.itemAnimator = DefaultItemAnimator()
        videoRecyclerView.setHasFixedSize(true)

        fetchVideos()
    }

    private fun fetchVideos(){
        val call = mApiService!!.fetchArticles(0,10,1)
        call.enqueue(object: Callback<ResultArticle>{
            override fun onFailure(call: Call<ResultArticle>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResultArticle>, response: Response<ResultArticle>) {
                val result = response.body()
                if(result != null){
                    mVideos.addAll(result.articles!!)
                    mAdapter!!.notifyDataSetChanged()
                }
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }
}