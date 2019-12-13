package me.sedaph.newsapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import me.sedaph.newsapp.model.Article.ResultView
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import me.sedaph.newsapp.utils.App
import me.sedaph.newsapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

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

        viewUpdate(position, type)
//        Toast.makeText(applicationContext, position.toString(), Toast.LENGTH_LONG).show()
    }

    private fun viewUpdate(articleId: Int, type: Int){
        val call = mAPIService!!.viewUpdate(articleId)
        call.enqueue(object: Callback<ResultView>{
            override fun onFailure(call: Call<ResultView>, t: Throwable) {
            }
            override fun onResponse(call: Call<ResultView>, response: Response<ResultView>) {
                if(response.body() != null){
                    if(response.body()!!.status){
                        fetchArticle(articleId, type)
                    }
                }
            }
        })
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
                    articleCommentCount.text = article.views_count!!.toString()
                    articleContent.text = App.fromHtml(article.contents!!)

                    detailProgress.visibility = View.VISIBLE
                    if(article.type == 0){
                        Picasso.get().load(article.imageUrl!!)
                            .into(articleImage, object: com.squareup.picasso.Callback{
                                override fun onSuccess() {
                                    detailProgress.visibility = View.GONE
                                    articleImage.visibility = View.VISIBLE
                                    articleImage.setOnClickListener {
                                        val intent = Intent(applicationContext, PhotoViewActivity::class.java)
                                        intent.putExtra("imageUrl", article.imageUrl!!)
                                        startActivity(intent)
                                    }
                                }

                                override fun onError(e: Exception?) {
                                    detailProgress.visibility = View.GONE
                                    articleImage.visibility = View.VISIBLE
                                    Picasso.get().load(R.drawable.no_photo)
                                        .into(articleImage)
                                }

                            })
                        videoThumbnailContainer.visibility = View.GONE
                    }else if(article.type == 1){
                        articleImage.visibility = View.GONE
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
                                            detailProgress.visibility = View.GONE
                                            videoThumbnailContainer.visibility = View.VISIBLE
                                            videoThumbnailContainer.setOnClickListener {
                                                val intent = Intent(applicationContext, StreamActivity::class.java)
                                                intent.putExtra("videoId", article.videoId)
                                                startActivity(intent)
                                            }
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

            R.id.navShare -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "Change text..")
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
