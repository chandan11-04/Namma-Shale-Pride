package com.nammashale.shalepride.ui.meal

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nammashale.shalepride.R
import com.nammashale.shalepride.util.loadImage

class MealUploadDialog(
    context: Context,
    private val imageUri: Uri,
    private val onUpload: (menu: String) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
        }

        val imageView = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 400
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageURI(imageUri)
        }

        val inputLayout = TextInputLayout(context, null, com.google.android.material.R.attr.textInputOutlinedStyle).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 32 }
            hint = context.getString(R.string.enter_meal_menu)
        }

        val editText = TextInputEditText(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        inputLayout.addView(editText)

        val button = MaterialButton(context).apply {
            text = context.getString(R.string.upload_meal)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 32 }
            setOnClickListener {
                val menu = editText.text.toString().trim()
                if (menu.isNotBlank()) {
                    onUpload(menu)
                    dismiss()
                } else {
                    inputLayout.error = context.getString(R.string.enter_meal_menu)
                }
            }
        }

        layout.addView(imageView)
        layout.addView(inputLayout)
        layout.addView(button)

        setContentView(layout)
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }
}
