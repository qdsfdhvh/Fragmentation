package me.yokeyword.fragmentation

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import me.yokeyword.fragmentation.anim.FragmentAnimator

abstract class SupportActivity: AppCompatActivity(), ISupportActivity {

    private val delegate by lazyAndroid {
        SupportActivityDelegate(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delegate.onPostCreate(savedInstanceState)
    }

    override fun onDestroy() {
        delegate.onDestroy()
        super.onDestroy()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return delegate.dispatchTouchEvent(ev)
                || dispatchTouchEventEvent(getActiveFragment(), ev)
                || dispatchTouchEventSupport(ev)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return dispatchKeyEvent(getActiveFragment(), event)
                || dispatchKeyEventSupport(event)
    }

    override fun onBackPressed() {
        delegate.onBackPressed()
    }

    override fun onBackPressedSupport() {
        delegate.onBackPressedSupport()
    }

    override fun getFragmentAnimator(): FragmentAnimator =  delegate.getFragmentAnimator()

    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator) = delegate.setFragmentAnimator(fragmentAnimator)

    override fun onCreateFragmentAnimator() = delegate.onCreateFragmentAnimator()

    override fun post(runnable: Runnable) = delegate.post(runnable)

    override fun getSupportDelegate(): SupportActivityDelegate = delegate

    override fun extraTransaction(): ExtraTransaction = delegate.extraTransaction()

    protected open fun dispatchTouchEventSupport(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    protected open fun dispatchKeyEventSupport(event: KeyEvent?): Boolean {
        return super.dispatchKeyEvent(event)
    }

}