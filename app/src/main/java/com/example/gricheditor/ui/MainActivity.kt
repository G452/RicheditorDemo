package com.example.gricheditor.ui

import android.content.Intent
import android.os.Bundle
import com.example.gricheditor.R
import com.example.gricheditor.base.BaseActicity
import com.example.gricheditor.constant.INVITATION_DATA
import com.example.gricheditor.constant.REFRESH_INVITATIONT
import com.example.gricheditor.databinding.ActivityMainBinding
import com.example.gricheditor.extentions.openActivity
import com.example.gricheditor.extentions.setVisible
import com.example.gricheditor.model.InvitationModel
import com.example.gricheditor.ui.adapter.InvitationAdapter
import com.example.gricheditor.viewmodel.MainVM
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * author：G
 * time：2021/4/28 12:14
 * about：
 **/
class MainActivity : BaseActicity() {

    val viewmodel by lazy { MainVM() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = binding<ActivityMainBinding>(R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewmodel
        viewmodel.initData()
        initView()
        LiveEventBus.get(REFRESH_INVITATIONT).observe(this, { viewmodel.initData() })
    }

    private fun initView() {
        baseTitleView.apply {
            backView?.setVisible(false)
            setRightTextClick { openActivity(SendInvitationActivity::class.java) }
        }
        no_data.onClick { openActivity(SendInvitationActivity::class.java) }
        val invitationAdapter = InvitationAdapter()
        mRecycleView.adapter = invitationAdapter
        invitationAdapter.setOnRecycleItemClickLinster { model, _ -> openDetailActivity(model) }
        viewmodel.pageState.observe(this, {
            when (it) {
                viewmodel.pageShowEmptyData -> no_data.setVisible(true)
                viewmodel.pageShowData -> no_data.setVisible(false)
            }
        })
    }

    //打开某个activity
    fun openDetailActivity(model: InvitationModel) {
        val intent = Intent(this, InvitationDetailActivity::class.java)
        intent.putExtra(INVITATION_DATA, model)
        startActivity(intent)
    }

}