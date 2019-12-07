package me.sedaph.newsapp.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckedTextView
import android.widget.TextView
import me.sedaph.newsapp.model.Category.Category

class SpinnerCategoryAdapter (private val context: Context?,
                              private val categories: List<Category>): BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.simple_spinner_dropdown_item, parent, false)

        val category: Category = getItem(position)

        val textView = view.findViewById(R.id.text1) as CheckedTextView
        textView.text = category.category
        return view
    }

    override fun getItem(position: Int): Category {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return categories[position].id.toLong()
    }

    override fun getCount(): Int {
        return categories.size
    }

}