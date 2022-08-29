package com.wlj.fixlibrary

import android.content.Context
import android.os.FileUtils
import android.util.Log
import com.wlj.fixlibrary.utils.FileUtil
import dalvik.system.BaseDexClassLoader
import dalvik.system.PathClassLoader
import java.io.*
import java.lang.Exception
import java.lang.reflect.Array
import java.nio.channels.FileChannel

class FixDexManager(context:Context) {

    val mCotnext=context

    private val mDexFile:File by lazy {
        //获取应用可以访问的目录
        mCotnext.getDir("odex",Context.MODE_PRIVATE)
    }

    /**
     * 修复
     */
    fun fixDex(fixDexPath:String){

        //1.先获取已经运行的dexElement
        val applicationClassLoader=mCotnext.classLoader
        println("classloaderName:${applicationClassLoader}")
        val applicatioDexElements = getDexElementsByClassloader(applicationClassLoader)
        //2.获取下载好的补丁 dexElement

        //2.1移动到系统可以访问的，dex目录下  ClassLoader

         val srcFIle= File(fixDexPath)
        if(!srcFIle.exists()){
            throw FileNotFoundException(fixDexPath)
        }

        val destFile=File(mDexFile,srcFIle.name)
        if(destFile.exists()){
            println("path${fixDexPath} has be laoded")
            return
        }
         FileUtil.copyFile(srcFIle,destFile)

        //2.2 ClassLoader读取dex路径 为什么加入到集合， 启动可能就要修复
          val fixdexFiles:MutableList<File> = ArrayList()
          fixdexFiles.add(destFile)

          val optimizedDirectory= File(mDexFile,"odex")
          if(!optimizedDirectory.exists()){
              optimizedDirectory.createNewFile()
          }
        var applicationDexelement: Any? =null
        for( file in fixdexFiles){

              //dex 路径  必须要在应用目录下得dex文件中  optimizedDirectory 解压路径
              val fixDexClassLaoder=BaseDexClassLoader(file.absolutePath,optimizedDirectory,null,applicationClassLoader)


              val fixDexElements= getDexElementsByClassloader(fixDexClassLaoder)
              //3.把补丁的dexElement插到已经运行的dexElements最前面

            //合并完成
            applicationDexelement = combineArray(fixDexElements, applicatioDexElements)
          }

        //合并的数组注入到原来的类中 applicationClassLoader
        if (applicationDexelement != null) {
            injectDexElements(applicationClassLoader,applicationDexelement)
        }

    }

    /**
     * 吧dexElement注入到classloader中
     */
    private fun injectDexElements(classLoader: ClassLoader?, dexElements: Any) {


        val pathListField=BaseDexClassLoader::class.java.getDeclaredField("pathList")
        pathListField.isAccessible=true
        val pathList = pathListField.get(classLoader)
        val dexElementsFiled=pathList.javaClass.getDeclaredField("dexElements")
        dexElementsFiled.isAccessible=true
        dexElementsFiled.set(pathList,dexElements)


    }

    fun combineArray(arrayLhs:Any?,arrayRhs:Any?): Any{
            val componentType: Class<*>? = arrayLhs?.javaClass?.componentType
            val i=Array.getLength(arrayLhs)
            val j=i+Array.getLength(arrayRhs)
            val newResult = Array.newInstance(componentType, j)
            for(k in 0 until j ){
                if(k<i){
                    Array.set(newResult,k,Array.get(arrayLhs,k))
                }else{
                    Array.set(newResult,k,Array.get(arrayRhs,k-i))
                }
            }
            return newResult
        }


    /**
     * 从classloader中获取element
     */
    private fun getDexElementsByClassloader(classloader: ClassLoader):Any?{

        try {
            //获取pathList
            val pathListField=BaseDexClassLoader::class.java.getDeclaredField("pathList")
            pathListField.isAccessible=true
            val pathList = pathListField.get(classloader)
            val dexElementsFiled=pathList.javaClass.getDeclaredField("dexElements")
            dexElementsFiled.isAccessible=true
            return dexElementsFiled.get(pathList)
        }catch (e:Exception){
           println("0-----e:${e}")
        }

      return null
    }
}