package com.example.gricheditor.util

import android.annotation.SuppressLint
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.gricheditor.App
import com.example.gricheditor.R
import java.lang.Exception


/**
 *
 * time：2021/2/25 17:08
 * about：toast优化
 */
@Suppress("DEPRECATION")
@SuppressLint("UseCompatLoadingForDrawables", "ShowToast", "StaticFieldLeak")
object ToastUtil {

    private var toast: Toast? = null
    private var textView: TextView? = null
    private val context by lazy { App.context }

    fun showToast(content: Any?) {
        try {
            if (toast == null) {
                if (context == null) return
                toast = Toast.makeText(context, content.toString(), Toast.LENGTH_SHORT)
                textView = TextView(context)
                textView?.text = content.toString()
                textView?.setTextColor(context!!.resources.getColor(R.color.ToastTextColor))
                textView?.textSize = 16f
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.HORIZONTAL
                layout.gravity = Gravity.CENTER
                layout.addView(textView)
                layout.background = context!!.resources.getDrawable(R.drawable.toast_bg)
                layout.setPadding(30, 16, 30, 16)
                toast?.view = layout
                toast?.setGravity(Gravity.CENTER, 0, 0)
            } else {
                textView?.text = content.toString()
            }
            toast?.show()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("sdasd-->", e.toString())
        }
    }
}