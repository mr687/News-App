package me.sedaph.newsapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_add_news.*
import me.sedaph.newsapp.R
import me.sedaph.newsapp.adapter.SpinnerCategoryAdapter
import me.sedaph.newsapp.model.Article.ResultAddArticle
import me.sedaph.newsapp.model.Category.Category
import me.sedaph.newsapp.model.Category.ResultCategory
import me.sedaph.newsapp.model.Image.ResultImage
import me.sedaph.newsapp.rest.APIService
import me.sedaph.newsapp.rest.RestClient
import me.sedaph.newsapp.utils.App
import me.sedaph.newsapp.utils.Prefs
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class AddNewsActivity : AppCompatActivity() {
    private val OPEN_EDITOR_REQUEST: Int = 10101
    private var mContents: String = ""

    private var mApiService: APIService? = null
    private var mCategories: MutableList<Category> = ArrayList()
    private var mAdapaterCategory: SpinnerCategoryAdapter? = null
    private var categoryId: Int = 0

    private var prefs: Prefs? = null
    private var fileImage: File? = null
    private var mType: Int? = 0
    private var mCategoryId: Int? = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_news)

        mApiService = RestClient.client.create(APIService::class.java)
        prefs = Prefs(applicationContext)

        setSpinner()
        mAdapaterCategory = SpinnerCategoryAdapter(this, mCategories)
        newsCategory.adapter = mAdapaterCategory
        setCategory()

        newsContentButton.setOnClickListener {
            val intent = Intent(this, TextEditorActivity::class.java)
            intent.putExtra("contents", mContents)
            startActivityForResult(intent, OPEN_EDITOR_REQUEST)
        }
        newsImageButton.setOnClickListener {
            newsImageLabel.visibility = View.GONE
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            val fileUri = data?.data
                            Picasso.get().load(fileUri).into(newsImageView)
                            fileImage = ImagePicker.getFile(data)
                        }
                        ImagePicker.RESULT_ERROR -> {
                            Toast.makeText(applicationContext, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(applicationContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        openMain()
    }

    private fun openMain(){
        val intent = Intent(this@AddNewsActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun actionPost(imageUrl: String){
        var url = imageUrl

        if(mType!! > 0) url = newsYoutubeUrl.text!!.trim().toString()

        val call = mApiService!!.articlePost(
            prefs!!.userId,
            prefs!!.userToken,
            newsTitle.text!!.trim().toString(),
            mContents,
            mCategoryId,
            url,
            mType)

        call.enqueue(object: retrofit2.Callback<ResultAddArticle>{
            override fun onFailure(call: Call<ResultAddArticle>, t: Throwable) {
            }
            override fun onResponse(
                call: Call<ResultAddArticle>,
                response: Response<ResultAddArticle>
            ) {
                if(response.body() != null){
                    Toast.makeText(this@AddNewsActivity, response.body()!!.message!!, Toast.LENGTH_LONG).show()
                    if(response.body()!!.status!!){
                        openMain()
                    }
                }
            }
        })
    }

    private fun uploadImage(file: File){
        newsProgressBar.visibility = View.VISIBLE
        var fileReqBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        var part: MultipartBody.Part = MultipartBody.Part.createFormData("upload", file.name, fileReqBody)
        var uid: RequestBody = RequestBody.create(MediaType.parse("text/plain"), prefs!!.userId.toString())
        var token: RequestBody = RequestBody.create(MediaType.parse("text/plain"), prefs!!.userToken)

        val call = mApiService!!.articleImageUpload(uid, token, part)
        call.enqueue(object: retrofit2.Callback<ResultImage>{
            override fun onFailure(call: Call<ResultImage>, t: Throwable) {
                Log.d("TAGG", t.localizedMessage)
            }
            override fun onResponse(call: Call<ResultImage>, response: Response<ResultImage>) {
                if(response.body() != null){
                    newsProgressBar.visibility = View.GONE
                    Toast.makeText(this@AddNewsActivity, response.body()!!.message!!, Toast.LENGTH_LONG).show()
                    if(response.body()!!.status!!){
                        actionPost(response.body()!!.imageUrl!!)
                    }
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.actionPost -> {
                if(newsTitle.text!!.trim().isEmpty()){
                    newsTitle.error = "Title empty"
                }else if(mType!! > 0 && newsYoutubeUrl.text!!.trim().isEmpty()){
                    newsYoutubeUrl.error = "Youtube url empty"
                }else if(mContents.isEmpty()){
                    Toast.makeText(this@AddNewsActivity, "Contents empty", Toast.LENGTH_LONG).show()
                }else if(mType!! < 1 && fileImage == null){
                    Toast.makeText(this@AddNewsActivity, "Image empty", Toast.LENGTH_LONG).show()
                }else{
                    uploadImage(fileImage!!)
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setCategory(){
        val call = mApiService!!.fetchCategories()
        call.enqueue(object: retrofit2.Callback<ResultCategory>{
            override fun onFailure(call: Call<ResultCategory>, t: Throwable) {
            }
            override fun onResponse(
                call: Call<ResultCategory>,
                response: Response<ResultCategory>
            ) {
                if(response.body()!!.status){
                    var result = response.body()!!
                    mCategories.addAll(result.categories!!)
                    mAdapaterCategory!!.notifyDataSetChanged()
                    newsCategory.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            categoryId = mAdapaterCategory!!.getItemId(position).toInt()
                        }

                    }
                }
            }

        })
    }

    private fun setSpinner(){
        val type = arrayOf("Standart", "Youtube Url")
        val adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_item, type)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        newsType.adapter = adapterType
        newsType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> {
                        mType = 0
                        newsYoutubeUrlContainer.visibility = View.GONE
                        newsImageButton.visibility = View.VISIBLE
                    }
                    1 -> {
                        mType = 1
                        newsYoutubeUrlContainer.visibility = View.VISIBLE
                        newsImageButton.visibility = View.GONE
                    }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_EDITOR_REQUEST && resultCode == Activity.RESULT_OK){
            mContents = data!!.getStringExtra("contents")
            if(mContents.length > 50){
                newsContentButton.setText(App.fromHtml(mContents).substring(0,50) + "...")
            }else{
                newsContentButton.setText(App.fromHtml(mContents))
            }
        }
    }
}
