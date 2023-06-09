package com.example.schedulizer

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class DatabaseManager {
    companion object {

        var db = Firebase.firestore

        fun addTag(tag: HashMap<String, String>) {
            db.collection("Tags")
                .add(tag)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }

        fun removeTag(tag: Tag) {
            db.collection("Tags")
                .document(tag.id)
                .delete()
                .addOnSuccessListener { _ ->
                    Log.d(ContentValues.TAG, "Document deleted")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error deleting document", e)
                }
        }

        fun getTag(name: String): Task<QuerySnapshot> {
            return db.collection("Tags")
                .whereEqualTo("userId", SaveSharedPreferences.user.uid)
                .whereEqualTo("name", name)
                .get()
        }

        fun getAllTags(): Task<QuerySnapshot> {
            return db.collection("Tags").get()
        }

        fun addUser(name: String, password: String) {
            db.collection("Users")
                .whereEqualTo("Name", name)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        val tag = hashMapOf(
                            "Name" to name,
                            "Password" to password,
                        )
                        db.collection("Users")
                            .add(tag)
                            .addOnSuccessListener { documentReference ->
                                Log.d(ContentValues.TAG, "User added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error adding user", e)
                            }
                    }
                }

        }

        fun getUser(name: String): Task<QuerySnapshot> {
            return db.collection("Users")
                .whereEqualTo("Name", name)
                .get()
        }

        fun addActivity(activity: Activity) {
            val document = db.collection("Activities").document()
            activity.id = document.id
            activity.userID = SaveSharedPreferences.user.uid
            val handle = document.set(activity)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.d("Firebase", "Save failed: $it") }
        }

        fun removeActivity(activity: Activity) {
            db.collection("Activities")
                .document(activity.id)
                .delete()
                .addOnSuccessListener { _ ->
                    Log.d(ContentValues.TAG, "Document deleted")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error deleting document", e)
                }
        }

        fun getAllActivities(): Task<QuerySnapshot> {
            return db.collection("Activities")
                .whereEqualTo("userID", SaveSharedPreferences.user.uid)
                .get()
        }

        fun getActivityByDate(date: Date): Task<QuerySnapshot> {
            Log.d("DATABASE", "Timestamp: ${Timestamp(date)}")
            return db.collection("Activities")
                .whereEqualTo("userID", SaveSharedPreferences.user.uid)
                .whereEqualTo("start", Timestamp(date))
                .get()
        }

    }
}