package com.example.schedulizer

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.preference.PreferenceManager


class SaveSharedPreferences {
    companion object {
        const val PREF_USER_NAME: String = "username"

        private fun getSharedPreferences(ctx: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(ctx)
        }

        fun setUserName(ctx: Context, userName: String) {
            val editor: Editor = getSharedPreferences(ctx).edit()
            editor.putString(PREF_USER_NAME, userName)
            editor.commit()
        }

        fun getUserName(ctx: Context): String {
            return getSharedPreferences(ctx).getString(PREF_USER_NAME, "")!!
        }
    }
}