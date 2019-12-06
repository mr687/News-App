package me.sedaph.newsapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.google.android.youtube.player.YouTubeThumbnailView.OnInitializedListener
import kotlinx.android.synthetic.main.item_video.view.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.model.Article.Article
import me.sedaph.newsapp.ui.activity.DetailActivity
import me.sedaph.newsapp.ui.activity.StreamActivity
import me.sedaph.newsapp.utils.Constants

class VideosAdapter(private val context: Context?,
                    private val videos: List<Article>) :
    RecyclerView.Adapter<VideosAdapter.VideoHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.item_video, parent, false)
        return VideoHolder(view)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        val video: Article = videos[position]

        holder.videoTitle.text = video.title
        holder.videoCommentCount.text = video.comment_count!!.toString()
        holder.videoCategory.text = video.category
        holder.videoDate.text = video.createAt

        holder.videoCardView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("position", video.id!!)
            context!!.startActivity(intent)
        }

        holder.videoThumbnail.initialize(Constants.API_KEY_YOUTUBE, object: OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubeThumbnailView?,
                p1: YouTubeThumbnailLoader?
            ) {
                p1?.setVideo(video.videoId)
                p1?.setOnThumbnailLoadedListener(object: YouTubeThumbnailLoader.OnThumbnailLoadedListener{
                    override fun onThumbnailLoaded(p0: YouTubeThumbnailView?, p1: String) {
                    }

                    override fun onThumbnailError(
                        p0: YouTubeThumbnailView?,
                        p1: YouTubeThumbnailLoader.ErrorReason
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

    class VideoHolder(view: View):RecyclerView.ViewHolder(view) {
        val videoTitle = view.videoTitle!!
        val videoCategory = view.videoCategory!!
        val videoDate = view.videoDate!!
        val videoCommentCount = view.videoCommentCount!!
        val videoCardView = view.videoCardView!!
        val videoThumbnail = view.videoThumbnail!!
    }

}