package com.example.squadbuilder.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.squadbuilder.R

@BindingAdapter("imageUri")
fun bindImage(imageView: ImageView, imageUri: String?) {
    if (!imageUri.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(imageUri)
            .into(imageView)
    } else {
        imageView.setImageResource(R.drawable.icon_q) // 기본 이미지 설정
    }
}
