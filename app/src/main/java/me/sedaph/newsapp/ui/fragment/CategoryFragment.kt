package me.sedaph.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_category.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.adapter.CategoryAdapter
import me.sedaph.newsapp.model.Category.Category
import me.sedaph.newsapp.model.Category.ResultCategory
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment: Fragment() {
    private var mApiService: APIService? = null

    private var mAdapter: CategoryAdapter? = null
    private val mCategories: MutableList<Category> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mApiService = RestClient.client.create((APIService::class.java))

        mAdapter = CategoryAdapter(context, mCategories)

        categoryRecyclerView!!.adapter = mAdapter
        categoryRecyclerView!!.layoutManager = GridLayoutManager(context, 3)
        categoryRecyclerView!!.itemAnimator = DefaultItemAnimator()

        fetchCategories()
    }

    private fun fetchCategories(){
        val call = mApiService!!.fetchCategories()
        call.enqueue(object: Callback<ResultCategory>{
            override fun onFailure(call: Call<ResultCategory>, t: Throwable) {
                Log.e("TAGG", t.localizedMessage.toString())
            }

            override fun onResponse(
                call: Call<ResultCategory>,
                response: Response<ResultCategory>
            ) {
                val result = response.body()
                if(result != null){
                    mCategories.addAll(result.categories!!)
                    mAdapter!!.notifyDataSetChanged()
                }
            }

        })
    }
}