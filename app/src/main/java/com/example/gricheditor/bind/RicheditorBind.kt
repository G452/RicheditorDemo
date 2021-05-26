@file:Suppress("DEPRECATION")

package com.example.gricheditor.bind

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gricheditor.model.InvitationModel
import com.example.gricheditor.ui.adapter.InvitationAdapter


@BindingAdapter("setInvitation")
fun setInvitation(recyclerView: RecyclerView, items: List<InvitationModel>?) {
    items?.let {
        (recyclerView.adapter as InvitationAdapter).setList(items)
    }
}

@BindingAdapter("setHtml")
fun setHtml(view: TextView, html: Any?) {
    html?.let { view.text = Html.fromHtml(html.toString()) }
}