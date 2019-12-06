package me.sedaph.newsapp.ui.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.ui.fragment.*
import me.sedaph.newsapp.utils.App

class MainActivity : AppCompatActivity() {

    // Initialize fragment
    private var homeFragment: HomeFragment = HomeFragment()
    private var profileFragment: ProfileFragment = ProfileFragment()
    private var categoryFragment: CategoryFragment = CategoryFragment()
    private var videoFragment: VideoFragment = VideoFragment()
    private var favoriteFragment: FavoriteFragment = FavoriteFragment()
    private val fragmentManager: FragmentManager = supportFragmentManager
    private var fragmentActive: Fragment = homeFragment

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        item ->
        when(item.itemId) {
            R.id.navHome -> {
                loadFragment(homeFragment)
                supportActionBar!!.title = "News App"
                return@OnNavigationItemSelectedListener true
            }
            R.id.navCategory -> {
                loadFragment(categoryFragment)
                supportActionBar!!.title = "Category"
                return@OnNavigationItemSelectedListener true
            }
            R.id.navVideo -> {
                loadFragment(videoFragment)
                supportActionBar!!.title = "Video"
                return@OnNavigationItemSelectedListener true
            }
            R.id.navFavorite -> {
                loadFragment(favoriteFragment)
                supportActionBar!!.title = "Favorite"
                return@OnNavigationItemSelectedListener true
            }
            R.id.navProfile -> {
                loadFragment(profileFragment)
                supportActionBar!!.title = "Profile"
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                loadFragment(homeFragment)
                supportActionBar!!.title = "News App"
                return@OnNavigationItemSelectedListener false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpToolbar()

        addFragment()

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.navSearch)
        val searchView  = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    private fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "News App"
    }

    private fun addFragment() {
        fragmentManager.beginTransaction().add(R.id.fragmentContainer,categoryFragment, "5").hide(categoryFragment).commit()
        fragmentManager.beginTransaction().add(R.id.fragmentContainer,videoFragment, "4").hide(videoFragment).commit()
        fragmentManager.beginTransaction().add(R.id.fragmentContainer,favoriteFragment, "3").hide(favoriteFragment).commit()
        fragmentManager.beginTransaction().add(R.id.fragmentContainer,profileFragment, "2").hide(profileFragment).commit()
        fragmentManager.beginTransaction().add(R.id.fragmentContainer,homeFragment, "1").commit()
    }

    private fun loadFragment(fragment: Fragment){
        fragmentManager.beginTransaction().hide(fragmentActive).show(fragment).commit()
        fragmentActive = fragment
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }
}
