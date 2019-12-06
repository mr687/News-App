package me.sedaph.newsapp.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import java.net.InetAddress

class App: Application() {
    companion object{
        internal fun checkEmail(email: CharSequence): Boolean{
            if(TextUtils.isEmpty(email)){
                return false
            }
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    }

    override fun onCreate() {
        super.onCreate()
    }
}