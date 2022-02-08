package com.example.gricheditor.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import java.lang.reflect.ParameterizedType

/**
 * author：G
 * time：2021/4/28 17:50
 * about：用于DataBinding模式的 BaseBindingFragment
 **/
@Suppress("UNCHECKED_CAST")
open class BaseFragment<Binding : ViewDataBinding> : Fragment() {

    protected lateinit var binding: Binding private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = inflater.getViewBinding(container)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
    }

    open fun initBinding() {}

    private fun <Binding : ViewDataBinding> LayoutInflater?.getViewBinding(container: ViewGroup?): Binding {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<Binding>>()[0].let {
            it.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java).let { method ->
                method.invoke(null, this, container, false) as Binding
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

}