package me.sedaph.newsapp.adapter

import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginTop
import me.sedaph.newsapp.R

class ProfileAdapter (private val context: Context?,
                      private val titles: Array<String>,
                      private val descriptions: Array<String>,
                      private val icons: TypedArray) : BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_menu_profile, parent, false)
        val menuIcon = view.findViewById(R.id.menuIcon) as ImageView
        val menuTitle = view.findViewById(R.id.menuTitle) as TextView
        val menuDescription = view.findViewById(R.id.menuDescription) as TextView
        val menuLine = view.findViewById(R.id.menuLine) as View

        menuIcon.setImageResource(icons.getResourceId(position, -1))
        menuTitle.text = titles[position]

        if (position < 3){
            menuDescription.text = descriptions[position]
        }else{
            menuDescription.visibility = View.GONE
            menuLine.visibility = View.GONE
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return titles.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return titles.size
    }

}