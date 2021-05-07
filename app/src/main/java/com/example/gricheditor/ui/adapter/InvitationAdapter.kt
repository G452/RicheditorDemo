package com.example.gricheditor.ui.adapter

import com.example.gricheditor.base.adapter.BaseBindingAdapter
import com.example.gricheditor.databinding.AdapterInvitationItemBinding
import com.example.gricheditor.model.InvitationModel
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * time：2021/4/29 16:41
 * about：
 */
class InvitationAdapter : BaseBindingAdapter<AdapterInvitationItemBinding, InvitationModel>() {
    override fun convert(bind: AdapterInvitationItemBinding, item: InvitationModel, pos: Int) {
        bind.itemmodel = item
        bind.clickView.onClick { mOnRecycleItemClickLinster?.invoke(mListData[pos], pos) }
    }
}