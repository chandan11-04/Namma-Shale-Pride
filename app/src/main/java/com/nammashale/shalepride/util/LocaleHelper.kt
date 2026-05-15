package com.nammashale.shalepride.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    fun applyLocale(context: Context): Context {
        val prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        val langCode = prefs.getString(Constants.KEY_LANGUAGE, Constants.LANG_ENGLISH)
            ?: Constants.LANG_ENGLISH
        return setLocale(context, langCode)
    }

    fun setLocale(context: Context, langCode: String): Context {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    fun getCurrentLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(Constants.KEY_LANGUAGE, Constants.LANG_ENGLISH)
            ?: Constants.LANG_ENGLISH
    }
}
