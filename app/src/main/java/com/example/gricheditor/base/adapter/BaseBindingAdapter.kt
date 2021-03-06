package com.example.gricheditor.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bjx.base.adapter.binding.BindingHolder
import java.lang.reflect.ParameterizedType

/**
 * author：G
 * time：2021/4/29 15:01
 * about：DataBinding 单个item  Adapter父类 多个请用BaseMultiBindingAdapter
 **/
@Suppress("UNCHECKED_CAST")
abstract class BaseBindingAdapter<Binding : ViewDataBinding, ITEM> : RecyclerView.Adapter<BindingHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        return BindingHolder(parent.getViewBinding())
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        convert(holder.binding as Binding, getItem(position), position)
        holder.binding.executePendingBindings()
    }

    protected abstract fun convert(bind: Binding, item: ITEM, pos: Int)

    override fun getItemCount(): Int = mListData.size

    fun getItem(position: Int): ITEM = mListData[position]

    fun ViewGroup?.getViewBinding(position: Int = 0): Binding {
        val vbClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<Binding>>()
        val inflate = vbClass[position].getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        return inflate.invoke(null, LayoutInflater.from(this?.context), this, false) as Binding
    }

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


