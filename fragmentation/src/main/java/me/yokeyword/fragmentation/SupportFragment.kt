package me.yokeyword.fragmentation

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import me.yokeyword.fragmentation.anim.FragmentAnimator

abstract class SupportFragment: Fragment(), ISupportFragment {

    private val delegate by lazyAndroid {
        SupportFragmentDelegate(this)
    }

    protected var ctx: FragmentActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            delegate.onAttach(context)
            ctx = context
        }
    }

    override fun onDetach() {
        ctx = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(savedInstanceState)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return delegate.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        delegate.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        delegate.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        delegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    override fun onDestroyView() {
        delegate.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        delegate.onDestroy()
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        delegate.onHiddenChanged(hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        delegate.setUserVisibleHint(isVisibleToUser)
    }

    override fun enqueueAction(runnable: Runnable?) {
        if (runnable == null) return
        delegate.post(runnable)
    }

    override fun post(runnable: Runnable?) {
        if (runnable == null) return
        delegate.post(runnable)
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        delegate.onEnterAnimationEnd(savedInstanceState)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        delegate.onLazyInitView(savedInstanceState)
    }

    override fun onSupportVisible() {
        delegate.onSupportVisible()
    }

    override fun onSupportInvisible() {
        delegate.onSupportInvisible()
    }

    override fun isSupportVisible(): Boolean {
        return delegate.isSupportVisible()
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return delegate.onCreateFragmentAnimator()
    }

    override fun getFragmentAnimator(): FragmentAnimator {
        return delegate.getFragmentAnimator()
    }

    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator) {
        delegate.setFragmentAnimator(fragmentAnimator)
    }

    override fun onBackPressedSupport(): Boolean {
        return delegate.onBackPressedSupport()
    }

    override fun setFragmentResult(resultCode: Int, bundle: Bundle?) {
        delegate.setFragmentResult(resultCode, bundle)
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        delegate.onFragmentResult(requestCode, resultCode, data)
    }

    override fun onNewBundle(args: Bundle?) = delegate.onNewBundle(args)

    override fun putNewBundle(newBundle: Bundle?) = delegate.putNewBundle(newBundle)

    override fun getSupportDelegate(): SupportFragmentDelegate = delegate

    override fun extraTransaction(): ExtraTransaction = delegate.extraTransaction()

    open fun dispatchTouchEventSupport(ev: MotionEvent?): Boolean {
        return false
    }

    open fun dispatchKeyEventSupport(event: KeyEvent?): Boolean {
        return false
    }

}