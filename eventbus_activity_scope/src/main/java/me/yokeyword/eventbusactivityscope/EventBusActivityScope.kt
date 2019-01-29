package me.yokeyword.eventbusactivityscope

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

object EventBusActivityScope {

    private const val TAG = "EventBusActivityScope"

    private val eventBusScopePool by lazy {
        ConcurrentHashMap<Activity?, LazyEventBusInstance>()
    }

    private val sInitialized by lazy {
        AtomicBoolean(false)
    }

    fun init(context: Context?) {
        if (!sInitialized.compareAndSet(false, true)) return

        val bak = context?.applicationContext
        if (bak is Application) {
            bak.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

                private val mainHandler = Handler(Looper.getMainLooper())

                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                    eventBusScopePool[activity] = LazyEventBusInstance()
                }

                override fun onActivityPaused(activity: Activity?) {

                }

                override fun onActivityResumed(activity: Activity?) {

                }

                override fun onActivityStarted(activity: Activity?) {

                }

                override fun onActivityDestroyed(activity: Activity?) {

                }

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

                }

                override fun onActivityStopped(activity: Activity?) {
                    if (!eventBusScopePool.contains(activity)) return

                    mainHandler.post {
                        eventBusScopePool.remove(activity)
                    }
                }
            })
        }
    }

    fun getDefault(activity: Activity?): EventBus {
        if (null == activity) {
            Log.e(TAG, "Can't find the Activity, the Activity is null!")
            return invalidEventBus()
        }

        val instance = eventBusScopePool[activity]
        if (null == instance) {
            Log.e(TAG, "Can't find the Activity, it has been removed!")
            return invalidEventBus()
        }

        return instance.getInstance()
    }

    private object Holder {
        val eventBus = EventBus()
    }

    private fun invalidEventBus() = Holder.eventBus

    private class LazyEventBusInstance {

        private object Holder {
            val eventBus = EventBus()
        }

        internal fun getInstance() = Holder.eventBus
    }

}