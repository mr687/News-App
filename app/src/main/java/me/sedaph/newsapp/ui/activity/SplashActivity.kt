package me.sedaph.newsapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import me.sedaph.newsapp.R
import me.sedaph.newsapp.utils.App

class SplashActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

        if(App.prefs!!.userToken != null){
            Toast.makeText(this@SplashActivity, "Welcome back, ${App.prefs!!.userName}", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this@SplashActivity, "Welcome to news app", Toast.LENGTH_LONG).show()
        }
    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }
}
