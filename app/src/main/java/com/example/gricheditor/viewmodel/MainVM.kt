package com.example.gricheditor.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.gricheditor.base.BaseViewModel
import com.example.gricheditor.constant.INVITATION_KEY
import com.example.gricheditor.extentions.showToast
import com.example.gricheditor.model.InvitationModel
import com.example.gricheditor.util.CacheListUtil
import kotlinx.coroutines.launch

/**
 * time：2021/4/28 15:49
 * about：
 */
class MainVM : BaseViewModel() {

    val _readList = MutableLiveData<MutableList<InvitationModel>>()

    fun initData() {
        scope.launch(coroutineExceptionHanlder) {
            val readList = CacheListUtil.getList(INVITATION_KEY)
            pageState.value = if (readList.isEmpty()) pageShowEmptyData else pageShowData
            _readList.value = ArrayList(readList)
        }
    }
}