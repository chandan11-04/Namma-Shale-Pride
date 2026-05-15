package com.nammashale.shalepride.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageStorage {

    /**
     * Copies a content URI image into the app's private files directory.
     * Returns the permanent file path (file:// URI string) that persists across app restarts.
     */
    fun savePermanently(context: Context, uri: Uri, folder: String = "images"): String {
        return try {
            val dir = File(context.filesDir, folder).also { it.mkdirs() }
            val fileName = "img_${UUID.randomUUID()}.jpg"
            val destFile = File(dir, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }

            destFile.absolutePath // Return permanent path
        } catch (e: Exception) {
            uri.toString() // Fallback to original URI if copy fails
        }
    }

    /**
     * Loads image: returns file path if it's a local file, else returns URL as-is for Glide.
     */
    fun resolveForGlide(storedPath: String): Any {
        return when {
            storedPath.startsWith("/") -> File(storedPath) // Local file path
            storedPath.startsWith("file://") -> File(storedPath.removePrefix("file://"))
            else -> storedPath // Cloudinary URL or content URI
        }
    }
}
