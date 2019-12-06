package me.sedaph.newsapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_toolbar.*
import kotlinx.android.synthetic.main.detail_toolbar.articleImage
import kotlinx.android.synthetic.main.item_article.*
import kotlinx.android.synthetic.main.item_article.articleCommentCount
import kotlinx.android.synthetic.main.item_article.articleDate
import kotlinx.android.synthetic.main.item_article.articleTitle
import me.sedaph.newsapp.R
import me.sedaph.newsapp.model.Article.Article
import me.sedaph.newsapp.model.Article.ResultArticle
import me.sedaph.newsapp.model.Article.ResultDetailArticle
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity :
    AppCompatActivity() {
    private var mAPIService: APIService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setUpToolbar()

        mAPIService = RestClient.client.create(APIService::class.java)

        val position: Int = intent.getIntExtra("position", 0)
//        Toast.makeText(applicationContext, position.toString(), Toast.LENGTH_LONG).show()

        fetchArticle(position)
    }

    private fun fetchArticle(position: Int? = 0){
        val call = mAPIService!!.detailArticle(position)
        call.enqueue(object: Callback<ResultDetailArticle>{
            override fun onFailure(call: Call<ResultDetailArticle>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ResultDetailArticle>,
                response: Response<ResultDetailArticle>
            ) {
                val result = response.body()
                if(result!!.status){
                    val article:Article = result.articles!!

                    Picasso.get().load(article.imageUrl!!)
                        .into(articleImage)

                    articleTitle.text = article.title!!
                    articleDate.text = article.createAt!!
                    articleCommentCount.text = article.comment_count!!.toString()
                    articleContent.text = article.contents!!

                    articleImage.setOnClickListener {
                        val intent = Intent(applicationContext, PhotoViewActivity::class.java)
                        intent.putExtra("imageUrl", article.imageUrl!!)
                        startActivity(intent)
                    }
                }
            }

        })
    }

    private fun setUpToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.navLove -> {
                return true
            }

            R.id.navShare -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "Halo sedaph!.")
                startActivity(Intent.createChooser(intent, getString(R.string.label_send_to)))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }
}
