package com.example.gricheditor.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bjx.base.adapter.binding.BindingHolder

/**
 * author：G
 * time：2021/5/6 9:52
 * about：DataBinding 多item Adapter父类 单个请用BaseBindingAdapter
 **/
@Suppress("UNCHECKED_CAST")
abstract class BaseMultiBindingAdapter<ITEM> : RecyclerView.Adapter<BindingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        return BindingHolder(onCreateMultiViewHolder(parent, viewType))
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        convert(holder.binding, getItem(position), position)
        holder.binding.executePendingBindings()
    }

    abstract fun onCreateMultiViewHolder(parent: ViewGroup, viewType: Int): ViewDataBinding

    abstract fun convert(bind: ViewDataBinding, item: ITEM, position: Int)

    protected fun <Binding : ViewDataBinding> loadLayout(vbClass: Class<Binding>, parent: ViewGroup): Binding {
        val inflate = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        return inflate.invoke(null, LayoutInflater.from(parent.context), parent, false) as Binding
    }

    override fun getItemCount(): Int = mListData.size

    fun getItem(position: Int): ITEM = mListData[position]

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