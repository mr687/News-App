package me.sedaph.newsapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_article.view.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.model.Article.Article
import me.sedaph.newsapp.ui.activity.DetailActivity

class ArticlesAdapter (private val context: Context?,
                       private val articles: List<Article>):
    RecyclerView.Adapter<ArticlesAdapter.ArticleHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleHolder {
        val root = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false)
        return ArticleHolder(root)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        if (itemCount > 0) run {
            var article: Article = articles.get(position)

            holder.articleContentPreview.text = article.contents!!
            holder.articleTitle.text = article.title!!
            holder.articleCommentCount.text = article.comment_count!!.toString()
            holder.articleDate.text = article.createAt

            Picasso.get().load(article.imageUrl).into(holder.articleImage)

            holder.articleCardView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                context?.startActivity(intent)
            }
        }
    }

    class ArticleHolder(view: View) : RecyclerView.ViewHolder(view){
        val articleTitle = view.articleTitle
        val articleContentPreview= view.articleContentPreview
        val articleDate= view.articleDate
        val articleCommentCount= view.articleCommentCount
        val articleImage= view.articleImage
        val articleCardView = view.articleCardView
    }

}