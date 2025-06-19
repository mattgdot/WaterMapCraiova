package com.app.water4craiova.model.service.impl

import android.util.Log
import com.app.water4craiova.model.User
import com.app.water4craiova.model.service.AccountService
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AccountServiceImpl @Inject constructor() : AccountService {
    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = Firebase.auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                auth.currentUser?.reload()
                this.trySend(auth.currentUser?.let {
                    User(it.uid, it.email.orEmpty(), it.displayName.orEmpty())
                } ?: User())
            }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose {
                Firebase.auth.removeAuthStateListener(listener)
            }
        }

    override fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }

    override fun sendRecoveryEmail(email: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener {
            onResult(it.exception)
        }
    }

    override fun createAccount(email: String, password: String, username:String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(
            email,password
        ).addOnCompleteListener {
            if (it.isComplete && it.isSuccessful) {
                Firebase.auth.currentUser?.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(username).build()
                )
            }

            onResult(it.exception)
        }
    }
}