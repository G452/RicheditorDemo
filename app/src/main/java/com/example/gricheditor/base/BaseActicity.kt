package com.example.gricheditor.base

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.gricheditor.util.UltimateBar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


/**
 * author：G
 * time：2021/4/28 12:14
 * about：
 **/
@Suppress("DEPRECATION")
open class BaseActicity : AppCompatActivity() {
    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): T =
        DataBindingUtil.setContentView(this, resId)

    val scope by lazy { MainScope() }
    var context: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        UltimateBar.setImmersion(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        super.onCreate(savedInstanceState)
        context = this
    }


    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    val coroutineExceptionHanlder by lazy {
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.e("coroutineException", throwable.message + "///" + this.javaClass.name)
        }
    }
}