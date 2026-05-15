package com.nammashale.shalepride.ui.facility

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nammashale.shalepride.databinding.ActivityFacilityFullscreenBinding
import com.nammashale.shalepride.util.loadImage

class FacilityFullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFacilityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra("image_url") ?: ""
        val name = intent.getStringExtra("name") ?: ""

        binding.ivFullscreen.loadImage(imageUrl)
        binding.tvName.text = name

        binding.btnClose.setOnClickListener { finish() }
    }
}
