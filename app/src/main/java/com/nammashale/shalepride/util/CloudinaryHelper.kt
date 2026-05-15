package com.nammashale.shalepride.util

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

object CloudinaryHelper {

    interface UploadListener {
        fun onStart()
        fun onProgress(progress: Int)
        fun onSuccess(imageUrl: String, publicId: String)
        fun onError(error: String)
    }

    fun uploadImage(
        imageUri: Uri,
        folder: String,
        listener: UploadListener
    ): String {
        return MediaManager.get().upload(imageUri)
            .unsigned(Constants.CLOUDINARY_UPLOAD_PRESET)
            .option("folder", folder)
            .option("resource_type", "image")
            .option("quality", "auto:good")
            .option("fetch_format", "auto")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    listener.onStart()
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    val progress = if (totalBytes > 0) ((bytes * 100) / totalBytes).toInt() else 0
                    listener.onProgress(progress)
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val secureUrl = resultData["secure_url"] as? String ?: ""
                    val publicId = resultData["public_id"] as? String ?: ""
                    listener.onSuccess(secureUrl, publicId)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    listener.onError(error.description ?: "Upload failed")
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    listener.onError("Upload rescheduled: ${error.description}")
                }
            })
            .dispatch()
    }

    /**
     * Generate optimized Cloudinary URL with transformations
     */
    fun getOptimizedUrl(originalUrl: String, width: Int = 800, quality: String = "auto"): String {
        if (originalUrl.isBlank()) return originalUrl
        // Apply Cloudinary transformations via URL manipulation
        return originalUrl.replace(
            "/upload/",
            "/upload/w_$width,q_$quality,f_auto/"
        )
    }
}
