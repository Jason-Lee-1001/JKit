package com.jstudio.jkit

import android.app.Activity
import android.app.Application
import android.app.Service
import android.os.Bundle
import java.util.*
import kotlin.system.exitProcess

/**
 * Created by Jason
 */
class AppKit private constructor() : Application.ActivityLifecycleCallbacks {

    private val logTag = AppKit::class.java.simpleName
    private var activityStack: Stack<Activity> = Stack()
    private var serviceList: LinkedList<Service> = LinkedList()

    fun init(app: Application) = app.registerActivityLifecycleCallbacks(this)

    companion object {
        @JvmStatic
        fun obtain(): AppKit = Holder.instance

        private object Holder {
            internal var instance = AppKit()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityStack.add(activity)
        LogKit.i(logTag, "stack size:${activityStack.size}, ${activity.javaClass.simpleName} created")
    }

    override fun onActivityStarted(activity: Activity) {
        LogKit.i(logTag, "${activity.javaClass.simpleName} started")
    }

    override fun onActivityResumed(activity: Activity) {
        LogKit.i(logTag, "${activity.javaClass.simpleName} resumed")
    }

    override fun onActivityPaused(activity: Activity) {
        LogKit.i(logTag, "${activity.javaClass.simpleName} paused")
    }

    override fun onActivityStopped(activity: Activity) {
        LogKit.i(logTag, "${activity.javaClass.simpleName} stopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        LogKit.i(logTag, "${activity.javaClass.simpleName} saved state")
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityStack.remove(activity)
        LogKit.i(logTag, "stack size:${activityStack.size}, ${activity.javaClass.simpleName} removed")
    }

    fun getActivityStackSize(): Int = activityStack.size

    fun getSizeOfServices(): Int = serviceList.size

    fun topActivity(): Activity? = activityStack.takeIf { it.size > 0 }?.lastElement()

    fun bottomActivity(): Activity? = activityStack.takeIf { it.size > 0 }?.lastElement()

    fun addService(service: Service) {
        serviceList.add(service)
    }

    fun removeService(service: Service) = serviceList.remove(service)

    fun stopAllService() {
        serviceList.forEach { it.stopSelf() }
        serviceList.clear()
    }

    fun finishAllActivity() {
        activityStack.forEach { it.finish() }
        activityStack.clear()
    }

    @Synchronized
    fun terminateApp() {
        stopAllService()
        finishAllActivity()
        exitProcess(0)
    }

    @Synchronized
    fun quit() {
        stopAllService()
        finishAllActivity()
    }
}