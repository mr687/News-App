package me.sedaph.newsapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.main_toolbar.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.ui.fragment.CategoryFragment
import me.sedaph.newsapp.ui.fragment.HomeFragment

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        val id = intent.getIntExtra("id", 0)

        val homeFragment = HomeFragment()

        var bundle = Bundle()
        bundle.putInt("id", id)

        homeFragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.categoryFragment,homeFragment).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }
}
