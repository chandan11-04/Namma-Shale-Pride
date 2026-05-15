package com.nammashale.shalepride.util

object Constants {
    // Database
    const val DATABASE_NAME = "shale_namma_pride_db"
    const val DATABASE_VERSION = 2  // Bumped for sentiment field (Feature 2)

    // SharedPreferences
    const val PREF_NAME = "shale_prefs"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_NAME = "user_name"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_USER_ROLE = "user_role"
    const val KEY_LANGUAGE = "language"
    const val KEY_DARK_MODE = "dark_mode"

    // Roles
    const val ROLE_ADMIN = "admin"
    const val ROLE_PARENT = "parent"

    // Languages
    const val LANG_ENGLISH = "en"
    const val LANG_KANNADA = "kn"

    // Cloudinary
    const val CLOUDINARY_CLOUD_NAME = "dbzbqg8t7"
    const val CLOUDINARY_UPLOAD_PRESET = "ml_default"
    const val CLOUDINARY_FOLDER_MEALS = "shale_meals"
    const val CLOUDINARY_FOLDER_FACILITIES = "shale_facilities"
    const val CLOUDINARY_FOLDER_STARS = "shale_stars"
    const val CLOUDINARY_FOLDER_ACTIVITIES = "shale_activities"

    // Post Types
    const val POST_TYPE_ACTIVITY = "activity"
    const val POST_TYPE_ANNOUNCEMENT = "announcement"
    const val POST_TYPE_ACHIEVEMENT = "achievement"
    const val POST_TYPE_MEAL = "meal"

    // Facility Categories
    val FACILITY_CATEGORIES = listOf(
        "Smart Class",
        "Library",
        "Toilets",
        "Labs",
        "Playground"
    )
    // Sentiment (Feature 2: AI Sentiment Analysis)
    const val SENTIMENT_POSITIVE = "POSITIVE"
    const val SENTIMENT_NEUTRAL  = "NEUTRAL"
    const val SENTIMENT_NEGATIVE = "NEGATIVE"
}
