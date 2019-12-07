package me.sedaph.newsapp.ui.fragment

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_profile.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.adapter.ProfileAdapter
import me.sedaph.newsapp.ui.activity.AddNewsActivity
import me.sedaph.newsapp.ui.activity.LoginActivity
import me.sedaph.newsapp.ui.activity.MainActivity
import me.sedaph.newsapp.ui.activity.RegisterActivity
import me.sedaph.newsapp.utils.Prefs

class ProfileFragment: Fragment(), View.OnClickListener{
    private var prefs: Prefs? = null
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

        prefs = context?.let { Prefs(it) }
        loadUserData()
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

    private fun loadUserData(){
        if(prefs!!.userName != null){
            labelWelcome.text = "Welcome, ${prefs!!.userName}!"
            if(prefs!!.userImage != null){
                Picasso.get().load(prefs!!.userImage).into(profileImage)
            }
            labelNotRegister.visibility = View.GONE
            labelRegisterNow.visibility = View.GONE
            labelLogin.visibility = View.GONE
            labelLogout.visibility = View.VISIBLE

            labelLogout.setOnClickListener { logout() }

            newsUserContainer.visibility = View.VISIBLE
            newsUserContainer.setOnClickListener {
                val intent = Intent(context, AddNewsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context!!.startActivity(intent)
            }
        }
    }

    private fun logout(){
        prefs!!.removeAuth()
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
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