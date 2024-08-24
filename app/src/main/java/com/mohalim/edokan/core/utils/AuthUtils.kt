package com.mohalim.edokan.core.utils

import com.google.firebase.auth.FirebaseAuth

object AuthUtils {

    fun checkUserAuthentication(firebaseAuth : FirebaseAuth): Boolean {
        val currentUser = firebaseAuth.currentUser
        return currentUser != null
    }

    fun signOut(firebaseAuth : FirebaseAuth) {
        firebaseAuth.signOut()

    }
}