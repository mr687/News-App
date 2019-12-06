package me.sedaph.newsapp.utils

import android.app.Application
import android.text.TextUtils

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