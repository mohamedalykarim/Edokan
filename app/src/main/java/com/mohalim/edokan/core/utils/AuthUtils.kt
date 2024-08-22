package com.mohalim.edokan.core.utils

import com.google.firebase.auth.FirebaseAuth

object AuthUtils {
    fun checkUserAuthentication(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }
}