package me.sedaph.newsapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.userEmail
import kotlinx.android.synthetic.main.activity_login.userPassword
import kotlinx.android.synthetic.main.activity_register.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.model.User.ResultLogin
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import me.sedaph.newsapp.utils.App
import me.sedaph.newsapp.utils.Prefs
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var prefs: Prefs? = null
    private var mApiService: APIService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mApiService = RestClient.client.create(APIService::class.java)
        prefs = Prefs(this)

        registerNow.setOnClickListener { openRegister() }
        actionLogin.setOnClickListener { login() }
    }

    private fun login(){

        if(TextUtils.isEmpty(userEmail.text!!.trim())){
            userEmail.error = "Email empty"
            userEmail.requestFocus()
            return
        }
        if(!App.checkEmail(userEmail.text!!.trim())){
            userEmail.error = "Email format invalid"
            userEmail.requestFocus()
            return
        }
        if(TextUtils.isEmpty(userPassword.text!!.trim())){
            userPassword.error = "Password empty"
            userPassword.requestFocus()
            return
        }

        val call = mApiService!!.userLogin(userEmail.text!!.trim().toString(), userPassword.text.toString())
        call.enqueue(object: retrofit2.Callback<ResultLogin>{
            override fun onFailure(call: Call<ResultLogin>, t: Throwable) {
                Log.d("TAGG", t.localizedMessage)
            }

            override fun onResponse(call: Call<ResultLogin>, response: Response<ResultLogin>) {
                Log.d("TAGG", response.body().toString())
                if(!response.body()?.status!!){
                    Toast.makeText(this@LoginActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }else{
                    prefs!!.saveUser(response.body()?.user!!)
                    Toast.makeText(this@LoginActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    openMain()
                }
            }

        })
    }

    private fun openMain(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openRegister(){
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }
}
