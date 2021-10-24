package com.devsoncall.okdoc.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.applandeo.materialcalendarview.EventDay
import com.devsoncall.okdoc.R

fun animKeyIn(view : View, context : Context) {
    val keyInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.key_anim_in)
    view.startAnimation(keyInAnimation)
}

fun animKeyOut(view : View, context : Context) {
    val keyOutAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.key_anim_out)
    view.startAnimation(keyOutAnimation)
}

fun animFadeIn(view : View, context : Context) {
    val fadeInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.fade_in)
    view.startAnimation(fadeInAnimation)
}

fun animFadeOut(view : View, context : Context) {
    val fadeOutAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.fade_out)
    view.startAnimation(fadeOutAnimation)
}

fun animSwipe(view : View, context : Context) {
    val swipeAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.swipe_animation)
    view.startAnimation(swipeAnimation)
}

fun animPushRightIn(view : View, context : Context) {
    val pushRightInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.push_right_in)
    view.startAnimation(pushRightInAnimation)
}

fun animPushUpIn(view : View, context : Context) {
    val pushUpInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.push_up_in)
    view.startAnimation(pushUpInAnimation)
}

fun animPushDownIn(view : View, context : Context) {
    val pushDownInAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.push_down_in)
    view.startAnimation(pushDownInAnimation)
}

fun animPushUpOut(view : View, context : Context) {
    val pushUpOutAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.push_up_out)
    view.startAnimation(pushUpOutAnimation)
}

fun animScaleUp(view : View, context : Context) {
    val scaleUpAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.scale_up)
    view.startAnimation(scaleUpAnimation)
}

fun animScaleDown(view : View, context : Context) {
    val scaleDownAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.scale_down)
    view.startAnimation(scaleDownAnimation)
}

fun animButtonPress(view: View, context: Context) {
    val scaleDownAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.scale_down)
    val scaleUpAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.scale_up)
    view.startAnimation(scaleDownAnimation)
    view.startAnimation(scaleUpAnimation)
}