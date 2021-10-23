package com.devsoncall.okdoc.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.devsoncall.okdoc.R

fun animKeyIn(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.key_anim_in)
    view.startAnimation(fadeInAnimation)
}

fun animKeyOut(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.key_anim_out)
    view.startAnimation(fadeInAnimation)
}

fun animFadeIn(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.fade_in)
    view.startAnimation(fadeInAnimation)
}

fun animFadeOut(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.fade_out)
    view.startAnimation(fadeInAnimation)
}

fun animSwipe(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.swipe_animation)
    view.startAnimation(fadeInAnimation)
}

fun animPushRightIn(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.push_right_in)
    view.startAnimation(fadeInAnimation)
}

fun animPushUpIn(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.push_up_in)
    view.startAnimation(fadeInAnimation)
}

fun animPushDownIn(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.push_down_in)
    view.startAnimation(fadeInAnimation)
}

fun animPushUpOut(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.push_up_out)
    view.startAnimation(fadeInAnimation)
}