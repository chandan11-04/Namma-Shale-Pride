package com.nammashale.shalepride.util

import android.content.Context
import android.content.SharedPreferences
import com.nammashale.shalepride.data.model.User

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        prefs.edit().apply {
            putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            putString(Constants.KEY_USER_ID, user.id)
            putString(Constants.KEY_USER_NAME, user.name)
            putString(Constants.KEY_USER_EMAIL, user.email)
            putString(Constants.KEY_USER_ROLE, user.role)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)

    fun getUserId(): String = prefs.getString(Constants.KEY_USER_ID, "") ?: ""

    fun getUserName(): String = prefs.getString(Constants.KEY_USER_NAME, "") ?: ""

    fun getUserEmail(): String = prefs.getString(Constants.KEY_USER_EMAIL, "") ?: ""

    fun getUserRole(): String = prefs.getString(Constants.KEY_USER_ROLE, Constants.ROLE_PARENT) ?: Constants.ROLE_PARENT

    fun isAdmin(): Boolean = getUserRole() == Constants.ROLE_ADMIN

    fun setLanguage(langCode: String) {
        prefs.edit().putString(Constants.KEY_LANGUAGE, langCode).apply()
    }

    fun getLanguage(): String = prefs.getString(Constants.KEY_LANGUAGE, Constants.LANG_ENGLISH) ?: Constants.LANG_ENGLISH

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(Constants.KEY_DARK_MODE, enabled).apply()
    }

    fun isDarkMode(): Boolean = prefs.getBoolean(Constants.KEY_DARK_MODE, false)

    fun isSeeded(): Boolean = prefs.getBoolean("is_seeded", false)

    fun setSeeded(value: Boolean) {
        prefs.edit().putBoolean("is_seeded", value).apply()
    }

    fun getSeedVersion(): String = prefs.getString("seed_version", "") ?: ""

    fun setSeedVersion(version: String) {
        prefs.edit().putString("seed_version", version).apply()
    }

    fun logout() {
        val lang = getLanguage()
        val darkMode = isDarkMode()
        prefs.edit().clear().apply()
        // Preserve language and theme preferences after logout
        setLanguage(lang)
        setDarkMode(darkMode)
    }

    fun getUser(): User {
        return User(
            id = getUserId(),
            name = getUserName(),
            email = getUserEmail(),
            role = getUserRole()
        )
    }
}
