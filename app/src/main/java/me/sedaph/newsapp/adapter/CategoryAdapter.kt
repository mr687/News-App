package me.sedaph.newsapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.model.Category.Category
import me.sedaph.newsapp.ui.activity.CategoryActivity
import me.sedaph.newsapp.ui.fragment.CategoryFragment

class CategoryAdapter(
    private val context: Context?,
    private val categories: List<Category>):

    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category:Category = categories.get(position)
        holder.categoryTitle.text = category.category

        holder.categoryCardView.setOnClickListener {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("id", category.id)
            context!!.startActivity(intent)
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val categoryImage = view.categoryImage
        val categoryTitle = view.categoryTitle
        val categoryCardView = view.categoryCardView
    }

}