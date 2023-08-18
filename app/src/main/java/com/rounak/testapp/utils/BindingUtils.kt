package com.rounak.testapp.utils

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rounak.testapp.R


@BindingAdapter("image")
fun loadImage(view: ImageView, url:String?){
    val imgUrl:String = url?:""
    //load image on each row with Glide library
    Glide.with(view)
        .load(imgUrl)
        .placeholder(R.drawable.placeholder)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("onCreate", "onLoadFailed for url: $imgUrl")
                Log.d("onCreate","image loading error: ${e.toString()}")
                return false // Allow calling onLoadFailed on the Target.
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false // Allow calling onResourceReady on the Target.
            }
        })
        .into(view)


}