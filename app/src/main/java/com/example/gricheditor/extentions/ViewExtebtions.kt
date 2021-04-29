package com.example.gricheditor.extentions

import android.view.View
import android.widget.Toast
import com.example.gricheditor.App
import com.example.gricheditor.util.ToastUtil

/**
 * author：G
 * time：2021/4/28 14:10
 * about：view扩展
 **/
fun View.isVisible(): Boolean = visibility == View.VISIBLE

fun View.setVisible(isShow: Boolean) {
    visibility = if (isShow) View.VISIBLE else View.GONE
}

fun showToast(msg: Any) {
    ToastUtil.showToast(msg)
}
