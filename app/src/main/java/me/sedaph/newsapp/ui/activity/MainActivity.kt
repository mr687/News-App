package me.sedaph.newsapp.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_bottom_sheet.*
import kotlinx.android.synthetic.main.main_toolbar.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.ui.fragment.*
import me.sedaph.newsapp.utils.Prefs

class MainActivity : AppCompatActivity() {

    // Initialize fragment
    private var homeFragment: HomeFragment = HomeFragment()
    private var profileFragment: ProfileFragment = ProfileFragment()
    private var categoryFragment: CategoryFragment = CategoryFragment()
    private var videoFragment: VideoFragment = VideoFragment()
    private var favoriteFragment: FavoriteFragment = FavoriteFragment()
    private val fragmentManager: FragmentManager = supportFragmentManager
    private var fragmentActive: Fragment = homeFragment
    private var dialogBottom: BottomSheetDialog? = null
    private var prefs: Prefs? = null

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
        prefs = Prefs(this)

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
                searchItem.collapseActionView()
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra("query", query)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.navSort -> {
                val view = layoutInflater.inflate(R.layout.main_bottom_sheet,null)
                val radioGroup = view.findViewById(R.id.radioGroup) as RadioGroup
                val settingGrid = view.findViewById(R.id.settingGrid) as MaterialButton
                val settingList= view.findViewById(R.id.settingList) as MaterialButton

                radioGroup.check(prefs!!.sorting!!)

                if(prefs!!.layout == prefs!!.LAYOUT_GRID){
                    settingGrid.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    settingGrid.iconTint = ContextCompat.getColorStateList(this, R.color.colorPrimary)
                    settingGrid.strokeColor = ContextCompat.getColorStateList(this, R.color.colorPrimary)
                    settingList.setTextColor(ContextCompat.getColor(this, R.color.colorGrey))
                    settingList.iconTint = ContextCompat.getColorStateList(this, R.color.colorGrey)
                    settingList.strokeColor = ContextCompat.getColorStateList(this, R.color.colorGrey)
                }else{
                    settingList.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    settingList.iconTint = ContextCompat.getColorStateList(this, R.color.colorPrimary)
                    settingList.strokeColor = ContextCompat.getColorStateList(this, R.color.colorPrimary)
                    settingGrid.setTextColor(ContextCompat.getColor(this, R.color.colorGrey))
                    settingGrid.iconTint = ContextCompat.getColorStateList(this, R.color.colorGrey)
                    settingGrid.strokeColor = ContextCompat.getColorStateList(this, R.color.colorGrey)
                }

                radioGroup.setOnCheckedChangeListener{ group, checkedId ->
                    when(checkedId){
                        R.id.sortNewest -> {
                            homeFragment.fetchArticles("createAt","desc")
                            prefs!!.sorting = prefs!!.SORT_BY_NEWEST_ARTICLES
                        }
                        R.id.sortOlder -> {
                            homeFragment.fetchArticles("createAt","asc")
                            prefs!!.sorting = prefs!!.SORT_BY_OLDER_ARTICLES
                        }
                        R.id.sortMostViewed -> {
                            homeFragment.fetchArticles("views_count","desc")
                            prefs!!.sorting = prefs!!.SORT_BY_MOST_VIEWED
                        }
                        R.id.sortFewestViews -> {
                            homeFragment.fetchArticles("views_count","asc")
                            prefs!!.sorting = prefs!!.SORT_BY_FEWEST_VIEWS
                        }
                    }
                    dialogBottom!!.dismiss()
                }
                settingGrid.setOnClickListener {
                    prefs!!.layout = prefs!!.LAYOUT_GRID
                    dialogBottom!!.dismiss()
                }
                settingList.setOnClickListener {
                    prefs!!.layout = prefs!!.LAYOUT_LIST
                    dialogBottom!!.dismiss()
                }

                dialogBottom = BottomSheetDialog(this)
                dialogBottom!!.setContentView(view)
                dialogBottom!!.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
