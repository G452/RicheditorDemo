package com.example.gricheditor.extentions

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import com.example.gricheditor.constant.INVITATION_DATA

/**
 * author：G
 * time：2021/4/28 14:01
 * about：context扩展
 **/

//打开某个activity
fun <T> Context.openActivity(className: Class<T>) {
    val intent = Intent(this, className)
    startActivity(intent)
}

//获取状态栏高度
fun Context.getStatusBarHeight(): Int {
    var statusBarHeight = 0
    try {
        val resourceId: Int =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        statusBarHeight = if (isAllScreenDevice()) 80 else 63
    }
    return statusBarHeight
}

//判断是否是全面屏
fun Context.isAllScreenDevice(): Boolean {
    // 低于 API 21的，都不会是全面屏。。。
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        return false
    }
    try {
        val windowManager: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getRealSize(point)
        val width: Float
        val height: Float
        if (point.x < point.y) {
            width = point.x.toFloat()
            height = point.y.toFloat()
        } else {
            width = point.y.toFloat()
            height = point.x.toFloat()
        }
        if (height / width >= 1.97f) {
            return true
        }
    } catch (e: java.lang.Exception) {
        return false
    }
    return false
}