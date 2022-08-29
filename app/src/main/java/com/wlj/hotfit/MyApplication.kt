package com.wlj.hotfit

import android.app.Application
import android.os.Environment
import android.widget.Toast
import com.wlj.fixlibrary.dex_fix.FixDexManager
import java.io.File
import java.lang.Exception

class MyApplication :Application() {
    override fun onCreate() {
        super.onCreate()
       fixBug()
    }
    fun  fixBug(){
        try {
            val fixFile= File(Environment.getExternalStorageDirectory(),"fix.dex")
            if(fixFile.exists()){
                FixDexManager(this).fixDex(fixFile.absolutePath)
            }
            Toast.makeText(this,"修复成功", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Toast.makeText(this,"修复失败", Toast.LENGTH_SHORT).show()
        }

    }
}