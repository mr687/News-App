package me.sedaph.newsapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_toolbar.*
import kotlinx.android.synthetic.main.detail_toolbar.articleImage
import kotlinx.android.synthetic.main.item_article.*
import kotlinx.android.synthetic.main.item_article.articleCommentCount
import kotlinx.android.synthetic.main.item_article.articleDate
import kotlinx.android.synthetic.main.item_article.articleTitle
import kotlinx.android.synthetic.main.item_video.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.model.Article.Article
import me.sedaph.newsapp.model.Article.ResultArticle
import me.sedaph.newsapp.model.Article.ResultDetailArticle
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import me.sedaph.newsapp.utils.Constants
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
        val type: Int = intent.getIntExtra("type", 0)
//        Toast.makeText(applicationContext, position.toString(), Toast.LENGTH_LONG).show()

        fetchArticle(position, type)
    }

    private fun fetchArticle(position: Int? = 0, type: Int? = 0){
        val call = mAPIService!!.detailArticle(position, type)
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

                    articleTitle.text = article.title!!
                    articleDate.text = article.createAt!!
                    articleCommentCount.text = article.comment_count!!.toString()
                    articleContent.text = article.contents!!

                    if(article.type == 0){
                        Picasso.get().load(article.imageUrl!!)
                            .into(articleImage)
                        articleImage.visibility = View.VISIBLE
                        detailThumbnailView.visibility = View.GONE
                        articleImage.setOnClickListener {
                            val intent = Intent(applicationContext, PhotoViewActivity::class.java)
                            intent.putExtra("imageUrl", article.imageUrl!!)
                            startActivity(intent)
                        }
                    }else if(article.type == 1){
                        articleImage.visibility = View.GONE
                        detailThumbnailView.visibility = View.VISIBLE
                        detailThumbnailView.setOnClickListener {
                            val intent = Intent(applicationContext, StreamActivity::class.java)
                            intent.putExtra("videoId", article.videoId)
                            startActivity(intent)
                        }
                        detailThumbnailView.initialize(Constants.API_KEY_YOUTUBE,
                            object: YouTubeThumbnailView.OnInitializedListener{
                                override fun onInitializationSuccess(
                                    p0: YouTubeThumbnailView?,
                                    p1: YouTubeThumbnailLoader?
                                ) {
                                    p1?.setVideo(article.videoId!!)
                                    p1?.setOnThumbnailLoadedListener(object: YouTubeThumbnailLoader.OnThumbnailLoadedListener{
                                        override fun onThumbnailLoaded(
                                            p0: YouTubeThumbnailView?,
                                            p1: String?
                                        ) {

                                        }

                                        override fun onThumbnailError(
                                            p0: YouTubeThumbnailView?,
                                            p1: YouTubeThumbnailLoader.ErrorReason?
                                        ) {

                                        }

                                    })
                                }

                                override fun onInitializationFailure(
                                    p0: YouTubeThumbnailView?,
                                    p1: YouTubeInitializationResult?
                                ) {
                                }

                            })
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
