package com.example.gricheditor.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * author：G
 * time：2021/4/28 15:41
 * about：
 **/
open class BaseViewModel:ViewModel() {

    val scope by lazy { MainScope() }
    val start: Int by lazy { 100 }
    val complete: Int by lazy { 200 }
    val refresh: Int by lazy { 201 }
    val loadMore: Int by lazy { 202 }
    val fail: Int by lazy { 500 }
    val pageShowEmptyData: Int by lazy { 0 }
    val pageShowData: Int by lazy { 1 }
    val pageState by lazy { MutableLiveData<Int>() }

    override fun onCleared() {
        scope.cancel()
        super.onCleared()
    }


    val coroutineExceptionHanlder by lazy {
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.e("coroutineError", "${throwable.message}-->${javaClass.name}")
            pageState.value = fail
            pageState.value = complete
        }
    }

}