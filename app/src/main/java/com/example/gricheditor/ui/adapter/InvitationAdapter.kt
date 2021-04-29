package com.example.gricheditor.ui.adapter

import com.example.gricheditor.R
import com.example.gricheditor.base.adapter.BaseBindingAdapter
import com.example.gricheditor.base.adapter.BindingHolder
import com.example.gricheditor.databinding.AdapterInvitationItemBinding
import com.example.gricheditor.model.InvitationModel
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * time：2021/4/29 16:41
 * about：
 */
class InvitationAdapter :
    BaseBindingAdapter<AdapterInvitationItemBinding, InvitationModel>(R.layout.adapter_invitation_item) {
    override fun convert(holder: BindingHolder<AdapterInvitationItemBinding>, pos: Int) {
        holder.binding.itemmodel = mListData[pos]
        holder.binding.clickView.onClick { mOnRecycleItemClickLinster?.invoke(mListData[pos],pos) }
    }
}