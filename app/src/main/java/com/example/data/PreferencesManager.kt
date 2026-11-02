package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

enum class ThemeMode(val code: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun fromCode(code: String?): ThemeMode {
            return when (code) {
                "dark" -> DARK
                "system" -> SYSTEM
                else -> LIGHT // Default to LIGHT as requested by user
            }
        }
    }
}

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("listkita_prefs", Context.MODE_PRIVATE)

    private val _language = MutableStateFlow(loadLanguage())
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _currency = MutableStateFlow(loadCurrency())
    val currency: StateFlow<AppCurrency> = _currency.asStateFlow()

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _lastBackupTime = MutableStateFlow(prefs.getLong(KEY_LAST_BACKUP, 0L))
    val lastBackupTime: StateFlow<Long> = _lastBackupTime.asStateFlow()

    private fun loadLanguage(): AppLanguage {
        val savedCode = prefs.getString(KEY_LANGUAGE, null)
        if (savedCode != null) {
            return AppLanguage.fromCode(savedCode)
        }
        val deviceLang = Locale.getDefault().language
        return if (deviceLang.startsWith("en", ignoreCase = true)) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.INDONESIAN
        }
    }

    private fun loadCurrency(): AppCurrency {
        val savedCode = prefs.getString(KEY_CURRENCY, null)
        return AppCurrency.fromCode(savedCode)
    }

    private fun loadThemeMode(): ThemeMode {
        val savedTheme = prefs.getString(KEY_THEME_MODE, null)
        return ThemeMode.fromCode(savedTheme)
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
        _language.value = language
    }

    fun setCurrency(currency: AppCurrency) {
        prefs.edit().putString(KEY_CURRENCY, currency.code).apply()
        _currency.value = currency
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.code).apply()
        _themeMode.value = mode
    }

    fun setLastBackupTime(timestamp: Long) {
        prefs.edit().putLong(KEY_LAST_BACKUP, timestamp).apply()
        _lastBackupTime.value = timestamp
    }

    companion object {
        private const val KEY_LANGUAGE = "pref_app_language"
        private const val KEY_CURRENCY = "pref_app_currency"
        private const val KEY_THEME_MODE = "pref_theme_mode"
        private const val KEY_LAST_BACKUP = "pref_last_backup_time"
    }
}
