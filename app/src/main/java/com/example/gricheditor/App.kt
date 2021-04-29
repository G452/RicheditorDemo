package com.example.gricheditor

import android.app.Application
import com.tencent.mmkv.MMKV

/**
 * time：2021/4/28 12:27
 * about：
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        MMKV.initialize(this)
    }

    companion object {
        var context: App? = null
            private set
    }
}

