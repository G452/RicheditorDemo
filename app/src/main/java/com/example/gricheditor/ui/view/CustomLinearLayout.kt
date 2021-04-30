package com.example.gricheditor.ui.view

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.annotation.RequiresApi

/**
 * 作者：G
 * ClassName：CustomLinearLayout
 * 时间：2020/7/30  9:35 AM
 * 概述： 解决android:fitsSystemWindows="true"后顶部出现白条
 */
@Suppress("DEPRECATION")
class CustomLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : LinearLayout(context, attrs, defStyleAttr) {

    override fun fitSystemWindows(insets: Rect): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            insets.left = 0
            insets.top = 0
            insets.right = 0
        }
        return super.fitSystemWindows(insets)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            super.onApplyWindowInsets(
                insets.replaceSystemWindowInsets(0, 0, 0, insets.systemWindowInsetBottom)
            )
        } else {
            insets
        }
    }
}