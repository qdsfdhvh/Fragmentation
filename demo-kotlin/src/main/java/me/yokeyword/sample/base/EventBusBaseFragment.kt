package me.yokeyword.sample.base

import android.os.Bundle
import me.yokeyword.eventbusactivityscope.EventBusActivityScope

abstract class EventBusBaseFragment: BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusActivityScope.getDefault(ctx).register(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBusActivityScope.getDefault(ctx).unregister(this)
    }

}