package com.example.schedulizer


class Utilities {

    companion object {
        fun isInputValid(name: String, password: String): Boolean {
            if (name.isEmpty() || password.isEmpty()) {
                return false
            }
            return true
        }
    }

}