package com.example.schedulizer

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DatabaseManager {
    companion object {

        val db = Firebase.firestore

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
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "Document deleted")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error deleting document", e)
                }
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
    }
}