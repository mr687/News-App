package me.sedaph.newsapp.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.Log

class App: Application() {
    companion object{
        internal fun checkEmail(email: CharSequence): Boolean{
            if(TextUtils.isEmpty(email)){
                return false
            }
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        internal fun fromHtml(html: String): Spanned{
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
                }
                else -> {
                    Html.fromHtml(html)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}