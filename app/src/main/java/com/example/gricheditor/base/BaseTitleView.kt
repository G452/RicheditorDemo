package com.example.gricheditor.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.gricheditor.R
import com.example.gricheditor.extentions.getStatusBarHeight
import com.example.gricheditor.extentions.setVisible
import kotlinx.android.synthetic.main.base_title_layout.view.*
import org.jetbrains.anko.textColor

/**
 * author：G
 * time：2021/3/2 11:54
 * about：标题
 **/
class BaseTitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.base_title_layout, this)
        isTopPadding(true)
        setBackClick { (context as Activity).finish() }
        initAttrbutSet(attrs, defStyleAttr)
    }

    @SuppressLint("Recycle", "CustomViewStyleable")
    private fun initAttrbutSet(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.BaseTitleView, defStyleAttr, 0).let {
                it.getBoolean(R.styleable.BaseTitleView_isTopPadding, true).let { isTopPadding(it) }
                it.getString(R.styleable.BaseTitleView_baseTitle)?.let { setTitle(it) }
                it.getString(R.styleable.BaseTitleView_rightText)
                    ?.let { if (it.isNotEmpty()) setRightText(it) }
                it.getInt(R.styleable.BaseTitleView_rightTextColor, Color.parseColor("#333333"))
                    .let { rightTextButton?.textColor = it }
                it.getInt(R.styleable.BaseTitleView_rightImg, -1)
                    .let { if (it != -1) setRightImg(it) }
            }
        }
    }

    /**
     * 设置标题
     */
    fun setTitle(title: Any) {
        titleView?.text = title.toString()
    }

    /**
     * 是否设置顶部Padding
     * 默认开启
     */
    fun isTopPadding(isshow: Boolean) {
        mRootView?.setPadding(0, if (isshow) context.getStatusBarHeight() else 0, 0, 0)
    }

    /**
     * 右侧图片
     */
    fun setRightImg(img: Int) {
        rightButton?.setVisible(true)
        rightButton?.setImageResource(img)
    }

    /**
     * 右侧文字按钮
     */
    fun setRightText(text: String) {
        rightTextButton?.setVisible(true)
        rightTextButton?.text = text
    }

    /**
     * 右侧图片点击
     */
    fun setRightImgClick(click: (View) -> Unit) {
        rightButton?.setOnClickListener(click)
    }

    /**
     * 右侧图片点击
     */
    fun setRightTextClick(click: (View) -> Unit) {
        rightTextButton?.setOnClickListener(click)
    }

    /**
     * 返回键点击
     * 默认是 finish()
     */
    fun setBackClick(click: (View) -> Unit) {
        backView?.setOnClickListener(click)
    }

    // TODO: 2021/3/2 还需要什么可以在下面拓展

}