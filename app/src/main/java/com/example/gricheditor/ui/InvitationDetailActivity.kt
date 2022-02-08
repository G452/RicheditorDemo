package com.example.gricheditor.ui

import android.os.Bundle
import com.example.gricheditor.R
import com.example.gricheditor.base.BaseActicity
import com.example.gricheditor.constant.INVITATION_DATA
import com.example.gricheditor.databinding.ActivityInvitationDetailBinding
import com.example.gricheditor.model.InvitationModel
import kotlinx.android.synthetic.main.activity_invitation_detail.*

/**
 * author：G
 * time：2021/4/29 17:17
 * about：详情
 **/
class InvitationDetailActivity : BaseActicity<ActivityInvitationDetailBinding>() {
    private var invitationData: InvitationModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getSerializableExtra(INVITATION_DATA)?.let {
            invitationData = it as InvitationModel
            baseTitleView.setTitle(it.title)
            webview.loadData(it.content, "text/html;charset=UTF-8", null)
        }
    }
}