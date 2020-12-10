package com.jstudio.loadinglayout

import java.util.*

/**
 * Created by Jason
 */
class LoadSir private constructor(builder: Builder? = null) {

    private var builder: Builder

    init {
        this.builder = builder ?: Builder()
    }

    private fun setBuilder(builder: Builder) {
        this.builder = builder
    }

    fun <T> register(
        target: Any,
        onReloadListener: Callback.OnReloadListener? = null,
        mapper: ((T) -> Class<out Callback>)? = null
    ): LoadService<T> {
        val targetContext = hasTarget(target, builder.targetList)
        val loadLayout: LoadLayout = targetContext.replaceView(target, onReloadListener)
        return LoadService(builder, loadLayout, mapper)
    }

    class Builder {
        val targetList = ArrayList<Target>()
        val callbacks = ArrayList<Callback>()
        var defaultCallback: Class<out Callback>? = null
            private set

        fun addCallback(callback: Callback): Builder {
            callbacks.add(callback)
            return this
        }

        fun addTargetContext(target: Target): Builder {
            targetList.add(target)
            return this
        }

        fun setDefaultCallback(defaultCallback: Class<out Callback>): Builder {
            this.defaultCallback = defaultCallback
            return this
        }

        fun buildDefault() = default.setBuilder(this)

        fun build() = LoadSir(this)

        init {
            targetList.add(ActivityTarget())
            targetList.add(ViewTarget())
        }
    }

    private fun hasTarget(target: Any?, targetList: List<Target>): Target =
        requireNotNull(targetList.find { it == target }) { "No target fit it" }

    companion object {
        private object Holder {
            val loadSir = LoadSir()
        }

        val default by lazy { Holder.loadSir }
        fun create(): Builder = Builder()
    }
}