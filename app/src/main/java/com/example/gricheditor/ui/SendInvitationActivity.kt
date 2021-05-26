package com.example.gricheditor.ui

import android.os.Bundle
import com.example.gricheditor.R
import com.example.gricheditor.base.BaseActicity
import com.example.gricheditor.constant.INVITATION_KEY
import com.example.gricheditor.constant.REFRESH_INVITATIONT
import com.example.gricheditor.extentions.showToast
import com.example.gricheditor.model.InvitationModel
import com.example.gricheditor.util.CacheListUtil
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.activity_send_invitation.*
import kotlinx.coroutines.launch

/**
 * author：G
 * time：2021/4/28 13:51
 * about：
 **/
class SendInvitationActivity : BaseActicity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_invitation)
        initView()
    }

    private fun initView() {
        baseTitleView.setRightTextClick { saveInvitation() }
        mGRicheditorView.setPlaceholder("开始你的创作")
        mGRicheditorView.setEditPadding(16, 16, 16, 16)
    }

    private fun saveInvitation() {
        val title = if (titleEditView.text.isNullOrEmpty()) {
            showToast("请输入标题~")
            return
        } else {
            titleEditView.text.toString()
        }
        if (mGRicheditorView.getHtml().isNullOrEmpty()) {
            showToast("请输入正文~")
            return
        }
        mGRicheditorView.getHtml()?.let {
            scope.launch(coroutineExceptionHanlder) {
                val invitationModel = InvitationModel(title, it)
                CacheListUtil.addData(INVITATION_KEY, invitationModel)
                LiveEventBus.get(REFRESH_INVITATIONT).post(REFRESH_INVITATIONT)
                showToast("发布成功")
                finish()
            }
        }
    }
}