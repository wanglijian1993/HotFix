package com.wlj.hotfit

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TestAcitivty  : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        findViewById<Button>(R.id.bn).setOnClickListener {
            Toast.makeText(this,"${2/0}测试Bug修复成功",Toast.LENGTH_SHORT).show()
//            Toast.makeText(this,"Bug修复成功",Toast.LENGTH_SHORT).show()
        }
    }

}