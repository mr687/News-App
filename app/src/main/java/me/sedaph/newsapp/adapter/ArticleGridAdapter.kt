package me.sedaph.newsapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_article.view.articleCommentCount
import kotlinx.android.synthetic.main.item_article.view.articleDate
import kotlinx.android.synthetic.main.item_article.view.articleImage
import kotlinx.android.synthetic.main.item_article.view.articleTitle
import kotlinx.android.synthetic.main.item_article.view.homeProgress
import kotlinx.android.synthetic.main.item_article.view.homeThumbnailContainer
import kotlinx.android.synthetic.main.item_article.view.homeThumbnailView
import kotlinx.android.synthetic.main.item_article_grid.view.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.model.Article.Article
import me.sedaph.newsapp.ui.activity.DetailActivity
import me.sedaph.newsapp.ui.activity.PhotoViewActivity
import me.sedaph.newsapp.ui.activity.StreamActivity
import me.sedaph.newsapp.utils.App
import me.sedaph.newsapp.utils.Constants
import java.lang.Exception

class ArticleGridAdapter(private val context: Context?, private val articles: List<Article>): RecyclerView.Adapter<ArticleGridAdapter.ArticleHolder>(){
    class ArticleHolder(view: View) : RecyclerView.ViewHolder(view){
        val articleTitle = view.articleTitle!!
        val articleContentPreview= view.articleContent!!
        val articleDate= view.articleDate!!
        val articleCommentCount= view.articleCommentCount!!
        val articleImage= view.articleImage!!
        val articleCardView = view.articleCardView2!!
        val homeThumbnailContainer = view.homeThumbnailContainer!!
        val homeThumbnailView = view.homeThumbnailView!!
        val homeProgress = view.homeProgress!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleHolder {
        val root = LayoutInflater.from(context).inflate(R.layout.item_article_grid, parent, false)
        return ArticleHolder(root)
    }

    fun getItem(positon: Int):Article{
        return articles[positon]
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        if (itemCount > 0) run {
            var article: Article = articles[position]

            holder.articleContentPreview.text = App.fromHtml(article.contents!!)
            holder.articleTitle.text = article.title!!
            holder.articleCommentCount.text = article.views_count!!.toString()
            holder.articleDate.text = article.createAt
            holder.articleCardView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("position", article.id!!)
                intent.putExtra("type", article.type)
                context!!.startActivity(intent)
            }
            holder.homeProgress.visibility = View.VISIBLE
            if(article.type == 0){
                Picasso.get().load(article.imageUrl!!)
                    .error(ContextCompat.getDrawable(context!!, R.drawable.no_photo)!!)
                    .into(holder.articleImage, object: com.squareup.picasso.Callback{
                        override fun onSuccess() {
                            holder.homeProgress.visibility = View.GONE
                            holder.articleImage.visibility = View.VISIBLE
                        }

                        override fun onError(e: Exception?) {
                            holder.homeProgress.visibility = View.GONE
                            holder.articleImage.visibility = View.VISIBLE
                        }
                    })
                holder.homeThumbnailContainer.visibility = View.GONE
//                holder.articleImage.setOnClickListener {
//                    val intent = Intent(context, PhotoViewActivity::class.java)
//                    intent.putExtra("imageUrl", article.imageUrl!!)
//                    context!!.startActivity(intent)
//                }
            }else if(article.type == 1){
                holder.articleImage.visibility = View.GONE
//                holder.homeThumbnailContainer.setOnClickListener {
//                    val intent = Intent(context, StreamActivity::class.java)
//                    intent.putExtra("videoId", article.videoId)
//                    context!!.startActivity(intent)
//                }
                holder.homeThumbnailView.initialize(
                    Constants.API_KEY_YOUTUBE,
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
                                    holder.homeProgress.visibility = View.GONE
                                    holder.homeThumbnailContainer.visibility = View.VISIBLE
                                }

                                override fun onThumbnailError(
                                    p0: YouTubeThumbnailView?,
                                    p1: YouTubeThumbnailLoader.ErrorReason?
                                ) {
                                    holder.homeProgress.visibility = View.GONE
                                    holder.homeThumbnailContainer.visibility = View.VISIBLE
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

}