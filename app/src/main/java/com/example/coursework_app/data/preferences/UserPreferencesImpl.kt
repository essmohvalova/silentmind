package com.example.coursework_app.data.preferences

import android.content.Context
import com.example.coursework_app.domain.preferences.UserPreferences
import javax.inject.Inject
import androidx.core.content.edit

class UserPreferencesImpl @Inject constructor(context: Context) : UserPreferences {

    private val prefs = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    override fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    override fun setUserId(id: String) {
        prefs.edit { putString(KEY_USER_ID, id) }
    }

    override fun clearAll() {
        prefs.edit { clear() }
    }

    companion object {

        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USER_ID = "user_id"
    }
}
