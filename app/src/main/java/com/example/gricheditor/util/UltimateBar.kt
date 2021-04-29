package com.example.gricheditor.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.gricheditor.R

/**
 * author：G
 * time：2021/1/14 15:30
 * about：状态栏工具
 */
class UltimateBar {
    private var activity: Activity
    private var type = 0

    constructor(activity: Activity) {
        this.activity = activity
    }

    private var colorBuilder: ColorBuilder? = null

    private constructor(activity: Activity, builder: ColorBuilder) {
        this.activity = activity
        type = TYPE_COLOR
        colorBuilder = builder
    }

    private var transparentBuilder: TransparentBuilder? = null

    private constructor(activity: Activity, builder: TransparentBuilder) {
        this.activity = activity
        type = TYPE_TRANSPARENT
        transparentBuilder = builder
    }

    private var immersionBuilder: ImmersionBuilder? = null

    private constructor(activity: Activity, builder: ImmersionBuilder) {
        this.activity = activity
        type = TYPE_IMMERSION
        immersionBuilder = builder
    }

    private var drawerBuilder: DrawerBuilder? = null

    private constructor(activity: Activity, builder: DrawerBuilder) {
        this.activity = activity
        type = TYPE_DRAWER
        drawerBuilder = builder
    }

    private var hideBuilder: HideBuilder? = null

    private constructor(activity: Activity, builder: HideBuilder) {
        this.activity = activity
        type = TYPE_HIDE
        hideBuilder = builder
    }

    fun apply() {
        when (type) {
            TYPE_COLOR -> {
                colorBuilder?.let {
                    setColorBar(
                        it.statusColor, it.statusDepth,
                        it.applyNav, it.navColor, it.navDepth
                    )
                }

            }
            TYPE_TRANSPARENT -> {
                transparentBuilder?.let {
                    setTransparentBar(
                        it.statusColor,
                        it.statusAlpha,
                        it.applyNav,
                        it.navColor,
                        it.navAlpha
                    )
                }
            }
            TYPE_IMMERSION -> {
                immersionBuilder?.let {
                    setTransparentBar(
                        Color.TRANSPARENT, 0, it.applyNav,
                        Color.TRANSPARENT, 0
                    )
                }
            }
            TYPE_DRAWER -> {
                drawerBuilder?.let {
                    setColorBarForDrawer(
                        it.statusColor, it.statusDepth,
                        it.applyNav, it.navColor, it.navDepth
                    )
                }
            }
            TYPE_HIDE -> {
                hideBuilder?.let {
                    setHideBar(it.applyNav)
                }
            }
        }
    }

    /**
     * StatusBar and navigationBar color
     *
     * @param statusColor StatusBar color
     * @param statusDepth StatusBar color depth
     * @param navColor    NavigationBar color
     * @param navDepth    NavigationBar color depth
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun setColorBar(
        @ColorInt statusColor: Int, statusDepth: Int,
        @ColorInt navColor: Int, navDepth: Int
    ) {
        setColorBar(statusColor, statusDepth, true, navColor, navDepth)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun setColorBar(@ColorInt statusColor: Int, @ColorInt navColor: Int) {
        setColorBar(statusColor, 0, navColor, 0)
    }

    /**
     * StatusBar and NavigationBar hide
     *
     * @param applyNav apply NavigationBar
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setHideBar(applyNav: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val decorView = activity.window.decorView
            var option = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            if (applyNav) {
                option = (option or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            }
            decorView.systemUiVisibility = option
        }
    }

    /**
     * StatusBar and NavigationBar color
     *
     * @param statusColor StatusBar color
     * @param statusDepth StatusBar color depth
     * @param applyNav    apply NavigationBar or no
     * @param navColor    NavigationBar color (applyNav == true)
     * @param navDepth    NavigationBar color depth (applyNav = true)
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setColorBar(
        @ColorInt statusColor: Int, statusDepth: Int, applyNav: Boolean,
        @ColorInt navColor: Int, navDepth: Int
    ) {
        val realStatusDepth = limitDepthOrAlpha(statusDepth)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val finalStatusColor = if (realStatusDepth == 0) statusColor else calculateColor(
                statusColor,
                realStatusDepth
            )
            window.statusBarColor = finalStatusColor
            if (applyNav) {
                val realNavDepth = limitDepthOrAlpha(navDepth)
                val finalNavColor =
                    if (realNavDepth == 0) navColor else calculateColor(navColor, realNavDepth)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                window.navigationBarColor = finalNavColor
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val finalStatusColor = if (realStatusDepth == 0) statusColor else calculateColor(
                statusColor,
                realStatusDepth
            )
            val decorView = window.decorView as ViewGroup
            decorView.addView(createStatusBarView(activity, finalStatusColor))
            if (applyNav && navigationBarExist(activity)) {
                val realNavDepth = limitDepthOrAlpha(navDepth)
                val finalNavColor =
                    if (realNavDepth == 0) navColor else calculateColor(navColor, realNavDepth)
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                decorView.addView(createNavBarView(activity, finalNavColor))
            }
            setRootView(activity, true)
        }
    }

    /**
     * StatusBar and NavigationBar transparent
     *
     * @param statusColor StatusBar color
     * @param statusAlpha StatusBar alpha
     * @param applyNav    apply NavigationBar or no
     * @param navColor    NavigationBar color (applyNav == true)
     * @param navAlpha    NavigationBar alpha (applyNav == true)
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setTransparentBar(
        @ColorInt statusColor: Int, statusAlpha: Int, applyNav: Boolean,
        @ColorInt navColor: Int, navAlpha: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val decorView = window.decorView
            var option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            val finalStatusColor = if (statusColor == 0) Color.TRANSPARENT else Color.argb(
                limitDepthOrAlpha(statusAlpha), Color.red(statusColor),
                Color.green(statusColor), Color.blue(statusColor)
            )
            window.statusBarColor = finalStatusColor
            if (applyNav) {
                option = option or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                val finalNavColor = if (navColor == 0) Color.TRANSPARENT else Color.argb(
                    limitDepthOrAlpha(navAlpha), Color.red(navColor),
                    Color.green(navColor), Color.blue(navColor)
                )
                window.navigationBarColor = finalNavColor
            }
            decorView.systemUiVisibility = option
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView = window.decorView as ViewGroup
            val finalStatusColor = if (statusColor == 0) Color.TRANSPARENT else Color.argb(
                limitDepthOrAlpha(statusAlpha), Color.red(statusColor),
                Color.green(statusColor), Color.blue(statusColor)
            )
            decorView.addView(createStatusBarView(activity, finalStatusColor))
            if (applyNav && navigationBarExist(activity)) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                val finalNavColor = if (navColor == 0) Color.TRANSPARENT else Color.argb(
                    limitDepthOrAlpha(navAlpha), Color.red(navColor),
                    Color.green(navColor), Color.blue(navColor)
                )
                decorView.addView(createNavBarView(activity, finalNavColor))
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setColorBarForDrawer(
        @ColorInt statusColor: Int, statusDepth: Int, applyNav: Boolean,
        @ColorInt navColor: Int, navDepth: Int
    ) {
        val realStatusDepth = limitDepthOrAlpha(statusDepth)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val decorView = window.decorView as ViewGroup
            var option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.statusBarColor = Color.TRANSPARENT
            val finalStatusColor = if (realStatusDepth == 0) statusColor else calculateColor(
                statusColor,
                realStatusDepth
            )
            decorView.addView(createStatusBarView(activity, finalStatusColor), 0)
            if (applyNav && navigationBarExist(activity)) {
                window.navigationBarColor = Color.TRANSPARENT
                val realNavDepth = limitDepthOrAlpha(navDepth)
                val finalNavColor =
                    if (realNavDepth == 0) navColor else calculateColor(navColor, realNavDepth)
                decorView.addView(createNavBarView(activity, finalNavColor), 1)
                option = option or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            }
            decorView.systemUiVisibility = option
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView = window.decorView as ViewGroup
            val finalStatusColor = if (realStatusDepth == 0) statusColor else calculateColor(
                statusColor,
                realStatusDepth
            )
            decorView.addView(createStatusBarView(activity, finalStatusColor), 0)
            if (applyNav && navigationBarExist(activity)) {
                val realNavDepth = limitDepthOrAlpha(navDepth)
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                val finalNavColor =
                    if (realNavDepth == 0) navColor else calculateColor(navColor, realNavDepth)
                decorView.addView(createNavBarView(activity, finalNavColor), 1)
            }
        }
    }

    private fun limitDepthOrAlpha(depthOrAlpha: Int): Int {
        if (depthOrAlpha < 0) {
            return 0
        }
        return if (depthOrAlpha > 255) {
            255
        } else depthOrAlpha
    }

    private fun createStatusBarView(context: Context, @ColorInt color: Int): View {
        val statusBarView = View(context)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(context)
        )
        params.gravity = Gravity.TOP
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(color)
        return statusBarView
    }

    private fun createNavBarView(context: Context, @ColorInt color: Int): View {
        val navBarView = View(context)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            getNavigationHeight(context)
        )
        params.gravity = Gravity.BOTTOM
        navBarView.layoutParams = params
        navBarView.setBackgroundColor(color)
        return navBarView
    }

    private fun navigationBarExist(activity: Activity): Boolean {
        val windowManager = activity.windowManager
        val d = windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics)
        }
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels
        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
    }

    @ColorInt
    private fun calculateColor(@ColorInt color: Int, alpha: Int): Int {
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }

    private fun setRootView(activity: Activity, fit: Boolean) {
        val parent = activity.findViewById<View>(R.id.content) as ViewGroup
        for (i in 0 until parent.childCount) {
            val childView = parent.getChildAt(i)
            if (childView is ViewGroup) {
                childView.setFitsSystemWindows(fit)
                childView.clipToPadding = fit
            }
        }
    }

    private fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    private fun getNavigationHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    class ColorBuilder {
        @ColorInt
        var statusColor = 0
        var statusDepth = 0
        var applyNav = false

        @ColorInt
        var navColor = 0
        var navDepth = 0
        fun statusColor(@ColorInt statusColor: Int): ColorBuilder {
            this.statusColor = statusColor
            return this
        }

        fun statusDepth(statusDepth: Int): ColorBuilder {
            this.statusDepth = statusDepth
            return this
        }

        fun applyNav(applyNav: Boolean): ColorBuilder {
            this.applyNav = applyNav
            return this
        }

        fun navColor(@ColorInt navColor: Int): ColorBuilder {
            this.navColor = navColor
            return this
        }

        fun navDepth(navDepth: Int): ColorBuilder {
            this.navDepth = navDepth
            return this
        }

        fun build(activity: Activity): UltimateBar {
            return UltimateBar(activity, this)
        }
    }

    class TransparentBuilder {
        @ColorInt
        var statusColor = 0
        var statusAlpha = 0
        var applyNav = false

        @ColorInt
        var navColor = 0
        var navAlpha = 0
        fun statusColor(@ColorInt statusColor: Int): TransparentBuilder {
            this.statusColor = statusColor
            return this
        }

        fun statusAlpha(statusAlpha: Int): TransparentBuilder {
            this.statusAlpha = statusAlpha
            return this
        }

        fun applyNav(applyNav: Boolean): TransparentBuilder {
            this.applyNav = applyNav
            return this
        }

        fun navColor(@ColorInt navColor: Int): TransparentBuilder {
            this.navColor = navColor
            return this
        }

        fun navAlpha(navAlpha: Int): TransparentBuilder {
            this.navAlpha = navAlpha
            return this
        }

        fun build(activity: Activity): UltimateBar {
            return UltimateBar(activity, this)
        }
    }

    class ImmersionBuilder {
        var applyNav = false
        fun applyNav(applyNav: Boolean): ImmersionBuilder {
            this.applyNav = applyNav
            return this
        }

        fun build(activity: Activity): UltimateBar {
            return UltimateBar(activity, this)
        }
    }

    class DrawerBuilder {
        @ColorInt
        var statusColor = 0
        var statusDepth = 0
        var applyNav = false

        @ColorInt
        var navColor = 0
        var navDepth = 0
        fun statusColor(@ColorInt statusColor: Int): DrawerBuilder {
            this.statusColor = statusColor
            return this
        }

        fun statusDepth(statusDepth: Int): DrawerBuilder {
            this.statusDepth = statusDepth
            return this
        }

        fun applyNav(applyNav: Boolean): DrawerBuilder {
            this.applyNav = applyNav
            return this
        }

        fun navColor(@ColorInt navColor: Int): DrawerBuilder {
            this.navColor = navColor
            return this
        }

        fun navDepth(navDepth: Int): DrawerBuilder {
            this.navDepth = navDepth
            return this
        }

        fun build(activity: Activity): UltimateBar {
            return UltimateBar(activity, this)
        }
    }

    class HideBuilder {
        var applyNav = false
        fun applyNav(applyNav: Boolean): HideBuilder {
            this.applyNav = applyNav
            return this
        }

        fun build(activity: Activity): UltimateBar {
            return UltimateBar(activity, this)
        }
    }

    companion object {
        private const val TYPE_COLOR = 1
        private const val TYPE_TRANSPARENT = 2
        private const val TYPE_IMMERSION = 3
        private const val TYPE_DRAWER = 4
        private const val TYPE_HIDE = 5
        fun newColorBuilder(): ColorBuilder {
            return ColorBuilder()
        }

        fun newTransparentBuilder(): TransparentBuilder {
            return TransparentBuilder()
        }

        fun newImmersionBuilder(): ImmersionBuilder {
            return ImmersionBuilder()
        }

        fun newDrawerBuilder(): DrawerBuilder {
            return DrawerBuilder()
        }

        fun newHideBuilder(): HideBuilder {
            return HideBuilder()
        }

        fun setImmersion(activity: Activity){
            newImmersionBuilder().applyNav(false).build(activity).apply()
        }

        /**
         * 白色字体状态栏
         *
         * @param activity
         */
        @SuppressLint("NewApi")
        fun setStatusBarColor(activity: Activity, corlor: Int) {
// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { API:23 6.0修改字体
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            // }
            val window = activity.window
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //设置状态栏颜色
            window.statusBarColor = ContextCompat.getColor(activity, corlor)
        }
    }
}