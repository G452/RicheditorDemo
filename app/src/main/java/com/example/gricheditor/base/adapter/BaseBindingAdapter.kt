package com.example.gricheditor.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * author：G
 * time：2021/4/29 15:01
 * about：DataBinding Adapter父类
 **/
abstract class BaseBindingAdapter<Binding : ViewDataBinding, ITEM>(val view: Int) :
    RecyclerView.Adapter<BindingHolder<Binding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<Binding> {
        val binding: Binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), view, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: BindingHolder<Binding>, position: Int) {
        convert(viewHolder, position)
        viewHolder.binding.executePendingBindings()
    }

    protected abstract fun convert(holder: BindingHolder<Binding>, pos: Int)


    override fun getItemCount(): Int = mListData.size

    var mListData = emptyList<ITEM>()
    private var oldListData = emptyList<ITEM>()
    val listCallback by lazy { ListCallback() }

    open fun setList(items: List<ITEM>?) {
        if (items != null) {
            this.mListData = items
            autoNotify(listCallback, ArrayList(oldListData), ArrayList(items)) { o, n -> o === n }
            oldListData = items
        }
    }

    var mOnRecycleItemClickLinster: ((ITEM, Int) -> Unit)? = null
    fun setOnRecycleItemClickLinster(mOnRecycleItemClickLinster: ((ITEM, Int) -> Unit)) {
        this.mOnRecycleItemClickLinster = mOnRecycleItemClickLinster
    }
}

class BindingHolder<Bind : ViewDataBinding>(val binding: Bind) :
    RecyclerView.ViewHolder(binding.root)