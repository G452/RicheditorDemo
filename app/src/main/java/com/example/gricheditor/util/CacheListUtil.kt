package com.example.gricheditor.util

import android.annotation.SuppressLint
import android.util.Log
import com.example.gricheditor.model.InvitationModel
import com.tencent.mmkv.MMKV


/**
 * author：G
 * time：2021/4/15 13:32
 * about：已读状态保存
 **/
@SuppressLint("CommitPrefEdits")
object CacheListUtil {

    /**
     * 存入list
     **/
    fun setList(key: String, datalist: List<InvitationModel>) {
        if (datalist.isNullOrEmpty()) return
        try {
            MMKV.defaultMMKV().encode(key, GsonUtils.toJson(datalist))
        } catch (e: Exception) {
            Log.e("CacheListUtil->","setList-->$e")
        }
    }

    /**
     * 向list中添加一条
     **/
    fun addData(key: String, data: InvitationModel) {
        try {
            val readList: MutableList<InvitationModel> = ArrayList()
            getList(key).let { readList.addAll(it) }
            readList.add(0,data)
            setList(key, readList)
        } catch (e: Exception) {
            Log.e("CacheListUtil->","addData-->$e")
        }
    }

    /**
     * 获取list
     **/
    fun getList(key: String): List<InvitationModel> {
        val datalist: List<InvitationModel> = ArrayList()
        try {
            val strJson: String? = MMKV.defaultMMKV().decodeString(key, null)
            if (strJson.isNullOrEmpty()) {
                return datalist
            } else {
                GsonUtils.fromJsonArray(strJson, InvitationModel::class.java)?.let { return it }
            }
        } catch (e: Exception) {
            Log.e("CacheListUtil->","getList-->$e")
        }
        return datalist
    }

    /**
     * 清空
     **/
    fun clearReadList(key: String) {
        try {
            MMKV.defaultMMKV().removeValueForKey(key)
        } catch (e: Exception) {
            Log.e("CacheListUtil->","clearReadList-->$e")
        }
    }

}

