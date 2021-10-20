package com.devsoncall.okdoc.utils


import android.widget.ImageView
import com.devsoncall.okdoc.R

fun setAvatar(amka : String?, imageViewAvatar : ImageView) {

    if (amka?.last()?.toString().equals("0")) {
        imageViewAvatar.setImageResource(R.drawable.ic_male_avatar)

    } else {
        imageViewAvatar.setImageResource(R.drawable.ic_female_avatar)
    }
}