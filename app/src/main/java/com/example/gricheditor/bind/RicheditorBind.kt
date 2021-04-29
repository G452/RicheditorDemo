package com.example.gricheditor.bind

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gricheditor.model.InvitationModel
import com.example.gricheditor.ui.adapter.InvitationAdapter


@BindingAdapter("app:setInvitation")
fun setInvitation(recyclerView: RecyclerView, items: List<InvitationModel>?) {
    items?.let {
        (recyclerView.adapter as InvitationAdapter).setList(items)
    }
}