package me.sedaph.newsapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_register.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.model.User.ResultRegister
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import me.sedaph.newsapp.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private var mApiService: APIService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mApiService = RestClient.client.create(APIService::class.java)

        actionRegister.setOnClickListener { register() }
        loginNow.setOnClickListener { openLogin() }
    }

    private fun openLogin(){
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun register(){
        if(TextUtils.isEmpty(userName.text)){
            userName.error = "Email empty"
            userEmail.requestFocus()
            return
        }
        if(TextUtils.isEmpty(userEmail.text)){
            userEmail.error = "Email empty"
            userEmail.requestFocus()
            return
        }
        if(TextUtils.isEmpty(userPassword.text)){
            userPassword.error = "Email empty"
            userPassword.requestFocus()
            return
        }
        if(!App.checkEmail(userEmail.text!!.trim())){
            userEmail.error = "Email format invalid"
            userEmail.requestFocus()
            return
        }
        val call = mApiService!!.register(
            userName.text!!.trim().toString(),
            userEmail.text!!.trim().toString(),
            userPassword.text.toString())
        call.enqueue(object: Callback<ResultRegister>{
            override fun onFailure(call: Call<ResultRegister>, t: Throwable) {
                Log.d("TAGG", t.localizedMessage)
            }

            override fun onResponse(call: Call<ResultRegister>, response: Response<ResultRegister>) {
                if(response.body() != null){
                    Toast.makeText(this@RegisterActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    if(!response.body()!!.status){
                        userEmail.error = response.body()!!.message
                    }else{
                        openLogin()
                    }
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }
}
