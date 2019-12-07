package me.sedaph.newsapp.ui.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.github.mthli.knife.KnifeText
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_text_editor.*
import kotlinx.android.synthetic.main.detail_toolbar.*
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlinx.android.synthetic.main.main_toolbar.toolbar
import me.sedaph.newsapp.R

class TextEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_editor)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace)
        setSupportActionBar(toolbar)
        supportActionBar!!.setBackgroundDrawable(resources.getDrawable(R.drawable.black))
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { closeEditor() }

        val mContents = intent.getStringExtra("contents")
        if(mContents != null) knife!!.fromHtml(mContents)
        knife!!.setSelection(knife!!.editableText.length)

        setup()
    }

    private fun closeEditor(){
        val intent = intent
        intent.putExtra("contents", knife!!.toHtml())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }

    private fun setup(){
        bold.setOnClickListener { knife!!.bold(!knife!!.contains(KnifeText.FORMAT_BOLD)) }
        italic.setOnClickListener { knife!!.italic(!knife!!.contains(KnifeText.FORMAT_ITALIC)) }
        underline.setOnClickListener { knife!!.underline(!knife!!.contains(KnifeText.FORMAT_UNDERLINED)) }
        strikethrough.setOnClickListener { knife!!.strikethrough(!knife!!.contains(KnifeText.FORMAT_STRIKETHROUGH)) }
        bullet.setOnClickListener { knife!!.bullet(!knife!!.contains(KnifeText.FORMAT_BULLET)) }
        quote.setOnClickListener { knife!!.quote(!knife!!.contains(KnifeText.FORMAT_QUOTE)) }
        clear.setOnClickListener { clearEditor() }
    }

    private fun clearEditor(){
        knife!!.clearFormats()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.actionUndo -> {
                knife!!.undo()
                true
            }
            R.id.actionRedo -> {
                knife!!.redo()
                true
            }
            R.id.actionSubmit -> {
                closeEditor()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
