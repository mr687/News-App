package me.sedaph.newsapp.ui.activity

import android.os.Bundle
import android.util.Log
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_stream.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.utils.Constants

class StreamActivity :
    YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private var videoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)

        videoId = intent.getStringExtra("videoId")

        player.initialize(Constants.API_KEY_YOUTUBE,this)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if(!p2){
            p1?.cueVideo(videoId)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
    }
}
