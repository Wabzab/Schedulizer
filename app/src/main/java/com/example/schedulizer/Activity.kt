package com.example.schedulizer

import com.google.firebase.Timestamp


data class Activity (
    var id: String = "",
    var name: String = "",
    var desc: String = "",
    var start: Timestamp = Timestamp.now(),
    var duration: Long = 0,
    var tagID: String = "",
    var userID: String = "",
    var imageUri: String = ""
)