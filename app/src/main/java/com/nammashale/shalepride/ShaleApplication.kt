package com.nammashale.shalepride

import android.app.Application
import com.cloudinary.android.MediaManager
import com.nammashale.shalepride.data.local.AppDatabase
import com.nammashale.shalepride.data.local.SeedData
import com.nammashale.shalepride.util.LocaleHelper
import com.nammashale.shalepride.util.SessionManager

class ShaleApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var sessionManager: SessionManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize Session Manager
        sessionManager = SessionManager(this)

        // Apply saved language
        LocaleHelper.applyLocale(this)

        // Initialize Room Database
        database = AppDatabase.getInstance(this)

        // Seed demo data on first launch (versioned — bump version to re-seed)
        val SEED_VERSION = "v4"
        if (sessionManager.getSeedVersion() != SEED_VERSION) {
            SeedData.populateIfEmpty(database)
            sessionManager.setSeedVersion(SEED_VERSION)
        }

        // Initialize Cloudinary
        initCloudinary()
    }

    private fun initCloudinary() {
        try {
            val config = HashMap<String, String>()
            config["cloud_name"] = "YOUR_CLOUD_NAME" // Replace with your Cloudinary cloud name
            MediaManager.init(this, config)
        } catch (e: Exception) {
            // Cloudinary already initialized or config error
            e.printStackTrace()
        }
    }

    companion object {
        lateinit var instance: ShaleApplication
            private set
    }
}
