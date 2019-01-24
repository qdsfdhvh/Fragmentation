package me.yokeyword.fragmentation.helper.internal

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import me.yokeyword.fragmentation.R
import me.yokeyword.fragmentation.anim.FragmentAnimator

class AnimatorHelper(private val context: Context, animator: FragmentAnimator) {

    val noneAnim by lazy { R.anim.no_anim.loadAnim() }
    val noneAnimFixed: Animation by lazy { DefaultAnimation() }

    lateinit var enterAnim: Animation
    lateinit var exitAnim: Animation
    lateinit var popEnterAnim: Animation
    lateinit var popExitAnim: Animation

    init {
        notifyChanged(animator)
    }

    fun notifyChanged(animator: FragmentAnimator) {
        enterAnim = animator.enter.loadAnim()
        exitAnim = animator.exit.loadAnim()
        popEnterAnim = animator.popEnter.loadAnim()
        popExitAnim = animator.popExit.loadAnim()
    }

    fun compatChildFragmentExitAnim(fragment: Fragment): Animation? {
        val tag = fragment.tag ?: return null
        val parent = fragment.parentFragment ?: return null
        if ((tag.startsWith("android:switcher:") && fragment.userVisibleHint)
            || (parent.isRemoving && !fragment.isHidden )) {
            val animation = DefaultAnimation()
            animation.duration = exitAnim.duration
            return animation
        }
        return null
    }

    private fun Int.loadAnim(): Animation {
        val bak = if (this == 0) R.anim.no_anim else this
        return AnimationUtils.loadAnimation(context, bak)
    }

    private class DefaultAnimation: Animation()
}