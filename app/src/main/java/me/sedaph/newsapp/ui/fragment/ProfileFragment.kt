package me.sedaph.newsapp.ui.fragment

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_profile.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.adapter.ProfileAdapter
import me.sedaph.newsapp.ui.activity.LoginActivity
import me.sedaph.newsapp.ui.activity.RegisterActivity

class ProfileFragment: Fragment(), View.OnClickListener{
    private var mAdapter: ProfileAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        labelLogin.setOnClickListener(this)
        labelRegisterNow.setOnClickListener(this)

        val mIcons = context!!.resources.obtainTypedArray(R.array.profile_menu_icons)
        val mTitles = context!!.resources.getStringArray(R.array.profile_menu_titles)
        val mDescriptions = context!!.resources.getStringArray(R.array.profile_menu_descriptions)

        mAdapter = ProfileAdapter(context, mTitles, mDescriptions, mIcons)

        profileListView.adapter = mAdapter
        profileListView.setOnItemClickListener { _, _, position, _ ->
            when(position){
                0 -> {
                    MaterialAlertDialogBuilder(context)
                        .setMessage("New App V1.0.\nCopyright @ sedaph. All Rights Reserved.")
                        .setPositiveButton("Ok", null)
                        .show()
                }
                1 -> {
                    MaterialAlertDialogBuilder(context)
                        .setMessage("New App V1.0.\nCopyright @ sedaph. All Rights Reserved.")
                        .setPositiveButton("Ok", null)
                        .show()
                }
                2 -> {
                    MaterialAlertDialogBuilder(context)
                        .setMessage("New App V1.0.\nCopyright @ sedaph. All Rights Reserved.")
                        .setPositiveButton("Ok", null)
                        .show()
                }
                3 -> {
                    MaterialAlertDialogBuilder(context)
                        .setMessage("New App V1.0.\nCopyright @ sedaph. All Rights Reserved.")
                        .setPositiveButton("Ok", null)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.labelLogin -> {
                val intent = Intent(context, LoginActivity::class.java)
                context?.startActivity(intent)
            }
            R.id.labelRegisterNow -> {
                val intent = Intent(context, RegisterActivity::class.java)
                context?.startActivity(intent)
            }
        }
    }
}