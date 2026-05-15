package com.nammashale.shalepride.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.nammashale.shalepride.R

// View Extensions
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

fun View.showSnackbarWithAction(
    message: String,
    actionText: String,
    action: () -> Unit
) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionText) { action() }
        .show()
}

// ImageView Extensions
fun ImageView.loadImage(url: String?, placeholder: Int = R.drawable.ic_placeholder) {
    if (url.isNullOrBlank()) {
        setImageResource(placeholder)
        return
    }
    // Resolve local file paths vs remote URLs
    val source = ImageStorage.resolveForGlide(url)
    Glide.with(this.context)
        .load(source)
        .apply(
            RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .centerCrop()
        )
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .into(this)
}

fun ImageView.loadCircleImage(url: String?, placeholder: Int = R.drawable.ic_placeholder) {
    Glide.with(this.context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .circleCrop()
        )
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .into(this)
}

// Context Extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
