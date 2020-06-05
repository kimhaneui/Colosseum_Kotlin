package kr.co.tjoeun.colosseum_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class ModifyActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        userListBtn.setOnClickListener {
            val myintent = Intent(mContext,UserListActivity::class.java)
            startActivity(myintent)
        }
        findTopicBtn.setOnClickListener {
            val myintent = Intent(mContext,MainActivity::class.java)
            startActivity(myintent)
        }

    }
    override fun setValues() {

    }
}
