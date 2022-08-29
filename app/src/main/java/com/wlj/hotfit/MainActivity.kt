package com.wlj.hotfit

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wlj.fixlibrary.so_fix.SoHotFix
import com.wlj.fixlibrary.utils.FileUtil
import com.wlj.hotfit.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bnFixDex.setOnClickListener {

        }

        binding.bnFixSo.setOnClickListener {
            soFix();
        }

    }
    fun soFix() {
        // 从服务器下载 so ，比对 so 的版本
        // 现在下好了，在我的手机里面 /so/libmain.so
        // 先调用 sdk 方法动态加载或者修复
        val mainSoPath = File(Environment.getExternalStorageDirectory(), "so/libmain.so")
        val libSoPath = File(getDir("lib", Context.MODE_PRIVATE), "so")
        if (!libSoPath.exists()) {
            libSoPath.mkdirs()
        }
        val dst = File(libSoPath, "libmain.so")
        try {
            FileUtil.copyFile(mainSoPath, dst)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val soHotFix = SoHotFix(this)
            soHotFix.injectLoadPath(libSoPath.absolutePath)
            // 手动先加载起来
            // System.loadLibrary("unity1.so");
            // System.loadLibrary("unity2.so");
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadSo(view: View?) {
       //测试jar
    }
}