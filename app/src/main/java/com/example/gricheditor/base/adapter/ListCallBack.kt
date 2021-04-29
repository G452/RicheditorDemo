package com.example.gricheditor.base.adapter


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

class ListCallback : ListUpdateCallback {

    var realposition = 0
    lateinit var adapter: RecyclerView.Adapter<*>
    fun bind(adapter: RecyclerView.Adapter<*>) {
        if (!this::adapter.isInitialized) {
            this.adapter = adapter
        }
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(position, count, payload)
    }

    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(realposition, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(position, count)
    }

    fun setRealInsertPosition(realposition: Int) {
        this.realposition = realposition
    }
}

fun <T> RecyclerView.Adapter<*>.autoNotify(listCallback: ListCallback,old: List<T>, new: List<T>, compare: (T, T) -> Boolean) {
    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compare(old[oldItemPosition], new[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] == new[newItemPosition]
        }

        override fun getOldListSize() = old.size

        override fun getNewListSize() = new.size
    })
    listCallback.bind(this)
    listCallback.setRealInsertPosition(old.size)
    diff.dispatchUpdatesTo(listCallback)
}