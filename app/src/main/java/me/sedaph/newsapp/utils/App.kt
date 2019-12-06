package me.sedaph.newsapp.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import android.widget.Toast
import java.net.InetAddress

val prefs: Prefs by lazy {
    App.prefs!!
}

class App: Application() {
    companion object{
        internal fun checkEmail(email: CharSequence): Boolean{
            if(TextUtils.isEmpty(email)){
                return false
            }
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}