package com.example.schedulizer

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.preference.PreferenceManager


class SaveSharedPreferences {
    companion object {
        const val PREF_USER_NAME: String = "username"
        lateinit var user: User

        private fun getSharedPreferences(ctx: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(ctx)
        }

        fun setUserName(ctx: Context, userName: String) {
            val editor: Editor = getSharedPreferences(ctx).edit()
            editor.putString(PREF_USER_NAME, userName)
            editor.commit()
            setUser(ctx)
        }

        fun getUserName(ctx: Context): String {
            return getSharedPreferences(ctx).getString(PREF_USER_NAME, "")!!
        }

        fun setUser(ctx: Context) {
            val fetchUser = DatabaseManager.getUser(getSharedPreferences(ctx).getString(PREF_USER_NAME, "")!!)
            fetchUser.addOnSuccessListener { result ->
                val doc = result.documents[0]
                user = User(
                    uid = doc.id,
                    name = doc.get("Name") as String,
                    password = doc.get("Password") as String
                )
            }
        }
    }
}