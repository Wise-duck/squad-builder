package com.wiseduck.squadbuilder.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.wiseduck.squadbuilder.R

@BindingAdapter("imageUri")
fun bindImage(imageView: ImageView, imageUri: String?) {
    if (!imageUri.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(imageUri)
            .placeholder(R.drawable.icon_logo) // 기본 로딩 이미지 설정
            .error(R.drawable.icon_logo) // 로딩 에러 시 이미지 설정
            .into(imageView)
    } else {
        imageView.setImageResource(R.drawable.icon_q) // 기본 이미지 설정
    }
}
