package me.sedaph.newsapp.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.android.synthetic.main.main_bottom_sheet.view.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.model.User.User

class Prefs (context: Context) {
    val PREFS_FILENAME = "me.sedaph.prefs"
    val LAYOUT_TYPE = "layout_type"
    val LAYOUT_GRID = R.id.settingGrid
    val LAYOUT_LIST = R.id.settingList
    val SORT_BY_NEWEST_ARTICLES = R.id.sortNewest
    val SORT_BY_OLDER_ARTICLES = R.id.sortOlder
    val SORT_BY_MOST_VIEWED = R.id.sortMostViewed
    val SORT_BY_FEWEST_VIEWS = R.id.sortFewestViews
    val SORT_BY = "sort_by"
    private val USER_ID = "uid"
    private val USER_IMAGE = "user_image"
    private val USER_NAME = "user_name"
    private val USER_TOKEN = "user_token"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var layout: Int?
        get() = prefs.getInt(LAYOUT_TYPE,LAYOUT_LIST)
        set(value) = prefs.edit().putInt(LAYOUT_TYPE, value!!).apply()

    var sorting: Int?
        get() = prefs.getInt(SORT_BY, SORT_BY_NEWEST_ARTICLES)
        set(value) = prefs.edit().putInt(SORT_BY, value!!).apply()

    var userId: Int
        get() = prefs.getInt(USER_ID, 0)
        set(value) = prefs.edit().putInt(USER_ID, value).apply()

    var userName: String?
        get() = prefs.getString(USER_NAME, null)
        set(value) = prefs.edit().putString(USER_NAME, value).apply()

    var userToken: String?
        get() = prefs.getString(USER_TOKEN, null)
        set(value) = prefs.edit().putString(USER_TOKEN, value).apply()

    var userImage: String?
        get() = prefs.getString(USER_IMAGE, null)
        set(value) = prefs.edit().putString(USER_IMAGE, value).apply()

    fun saveUser(user: User){
        userId = user.id
        userToken = user.token
        userName = user.name
        userImage = user.imageUrl
    }

    fun removeAuth(){
        prefs.edit().clear().commit()
    }
}